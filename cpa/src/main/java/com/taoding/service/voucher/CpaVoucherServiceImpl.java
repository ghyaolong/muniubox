package com.taoding.service.voucher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.NextNoUtils;
import com.taoding.common.utils.NumberToCN;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.common.utils.UserUtils;
import com.taoding.domain.fixedAsset.FixedAsset;
import com.taoding.domain.user.User;
import com.taoding.domain.voucher.CpaVoucher;
import com.taoding.domain.voucher.CpaVoucherSubject;
import com.taoding.domain.voucher.CpaVoucherTemplete;
import com.taoding.domain.vouchersummary.CpaVoucherSummary;
import com.taoding.domain.vouchersummary.SummaryEntity;
import com.taoding.mapper.voucher.CpaVoucherDao;
import com.taoding.service.fixedAsset.FixedAssetService;
import com.taoding.service.vouchersummary.CpaVoucherSummaryService;

/**
 * 凭证
 * @author czy
 * 2017年11月28日 下午12:00:43
 */
@Service
public class CpaVoucherServiceImpl implements CpaVoucherService {

	@Autowired
	private CpaVoucherDao voucherDao;
	@Autowired
	private CpaVoucherSubjectService voucherSubjectService ;
	@Autowired
	private CpaVoucherTempleteService voucherTempleteService ;
	@Autowired
	private CpaVoucherSummaryService voucherSummaryService ; //凭证汇总
	@Autowired
	private FixedAssetService fixedAssetService;//固定资产
	
    /**
     * 为一个客户初始化一张凭证科目表
     * @param bookId
     * @return
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int init(String bookId) {
		return voucherDao.init(bookId); 
	}

	/**
	 * 新增凭证及凭证科目
	 * 2017年11月29日 上午10:00:02
	 * @param bookId
	 * @param voucher
	 * @return
	 */
	@Override
	@Transactional
	public Object insertCpaVoucher(CpaVoucher voucher) {
		
		Map<String,Object> maps = new HashMap<String, Object>();
		
		String id = UUIDUtils.getUUid();
		String bookId = voucher.getBookId();
		
		List<CpaVoucherSubject> subjectLists = voucher.getSubjectLists();
		
		BigDecimal amountDebit = new BigDecimal(0);
		BigDecimal amountCredit = new BigDecimal(0);
		
		User createBy = new User();
		createBy.setId(UserUtils.getCurrentUserId());
		
		String voucherPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		
		List<SummaryEntity> summaryList = new ArrayList<SummaryEntity>();
		
		if(subjectLists != null && subjectLists.size() > 0){
			for (CpaVoucherSubject voucherSubject : subjectLists) {
				if(voucherSubject != null){
					if(voucherSubject.getAmountDebit() != null){
						amountDebit = amountDebit.add(voucherSubject.getAmountDebit());
					}
					if(voucherSubject.getAmountCredit() != null){
						amountCredit = amountCredit.add(voucherSubject.getAmountCredit());
					}
					voucherSubject.setId(UUIDUtils.getUUid());
					voucherSubject.setVoucherPeriod(DateUtils.StringToDate(voucherPeriod, "yyyy-MM-dd"));
					voucherSubject.setVoucherId(id);
					voucherSubject.setCreateDate(new Date());
					voucherSubject.setCreateBy(createBy);
					
					SummaryEntity summaryEntity = new SummaryEntity();
					summaryEntity.setSubjectId(voucherSubject.getSubjectId());
					summaryEntity.setAmountDebit(voucherSubject.getAmountDebit());
					summaryEntity.setAmountCredit(voucherSubject.getAmountCredit());
					summaryList.add(summaryEntity);
				}
			}
		}
		
		int countSubject = voucherSubjectService.batchInsert(bookId,subjectLists);
		if(countSubject > 0){
			voucher.setId(id);
			voucher.setAmountDebit(amountDebit);
			voucher.setAmountCredit(amountCredit);
			voucher.setAccountCapital(NumberToCN.number2CNMontrayUnit(amountDebit));
			voucher.setVoucherPeriod(DateUtils.StringToDate(voucherPeriod, "yyyy-MM-dd"));
			voucher.setVoucherNo(getMaxNo(bookId));
			voucher.setCreateDate(new Date());
			voucher.setCreateBy(createBy);
			int count = voucherDao.insert(bookId,voucher);
			if(count > 0){
				String templeteId = voucher.getTempleteId() ;
				if(StringUtils.isNotEmpty(templeteId)){
					CpaVoucherTemplete voucherTemplete = voucherTempleteService.findById(templeteId);
					//如果是草稿，则删除
					if(voucherTemplete != null && !voucherTemplete.isType()){
						voucherTempleteService.deleteById(templeteId);
					}
				}
				//处理凭证汇总
				voucherSummaryService.dealVoucherSummary(summaryList, bookId,voucherPeriod, true);
				
				maps.put("id",id);
				maps.put("state",true);
				return maps;
			}
		}
		maps.put("state",false);
		return maps;
	}

	/**
	 * 通过Id查询凭证及凭证科目
	 * 2017年11月29日 上午10:24:37
	 * @param bookId
	 * @param id
	 * @return
	 */
	@Override
	public CpaVoucher findById(String bookId,String id) {
		CpaVoucher voucher = voucherDao.getById(bookId, id);
		//查询凭证的科目
		dealVoucherSubject(voucher, bookId);
		return voucher;
	}

	/**
	 * 获取下一个记账凭证编号
	 * 2017年11月29日 下午1:47:42
	 * @param bookId
	 * @return
	 */
	@Override
	public Object getNextVoucherNo(String bookId) {
		
		Map<String,Object> maps = new HashMap<String, Object>();
		maps.put("nextNo", getMaxNo(bookId));
		maps.put("lastDate", DateUtils.getLastMonthDate(CurrentAccountingUtils.getCurrentVoucherPeriod(bookId)));
		return maps;
	}
	
	/**
	 * 通过账期+凭证编号查询凭证信息 
	 * @param bookId
	 * @param voucherNo
	 * @param voucherPeriod
	 * @return
	 */
	@Override
	public CpaVoucher findByNoAndPeriod(String bookId, String voucherNo,
			String voucherPeriod) {
		CpaVoucher voucher = voucherDao.findByNoAndPeriod(bookId, voucherNo, voucherPeriod);
		//查询凭证的科目
		dealVoucherSubject(voucher, bookId);
		return voucher;
	}

	/**
	 * 修改凭证
	 * 2017年12月4日 上午10:23:02
	 * @param voucher
	 * @return
	 */
	@Override
	@Transactional
	public Object updateCpaVoucher(CpaVoucher voucher) {
		String id = voucher.getId();
		String bookId = voucher.getBookId();
		
		String voucherIds [] = new String [1] ;
		voucherIds [0] = id ;
		List<SummaryEntity> deleteLists = voucherSubjectService.findDeleteSubjectAmount(bookId, voucherIds);
		
		//删除之前保存的科目
		voucherSubjectService.deleteByVoucherId(bookId, voucher.getId());
		
		User createBy = new User();
		createBy.setId(UserUtils.getCurrentUserId());
		
		List<CpaVoucherSubject> subjectLists = voucher.getSubjectLists();
		BigDecimal amountDebit = new BigDecimal(0);
		BigDecimal amountCredit = new BigDecimal(0);
		
		List<SummaryEntity> summaryLists = new ArrayList<SummaryEntity>();
		
		if(subjectLists != null && subjectLists.size() > 0){
			for (CpaVoucherSubject voucherSubject : subjectLists) {
				if(voucherSubject != null){
					if(voucherSubject.getAmountDebit() != null){
						amountDebit = amountDebit.add(voucherSubject.getAmountDebit());
					}
					if(voucherSubject.getAmountCredit() != null){
						amountCredit = amountCredit.add(voucherSubject.getAmountCredit());
					}
					voucherSubject.setId(UUIDUtils.getUUid());
					voucherSubject.setVoucherId(id);
					voucherSubject.setVoucherPeriod(voucher.getVoucherPeriod());
					voucherSubject.setCreateDate(new Date());
					voucherSubject.setCreateBy(createBy);
					
					SummaryEntity summaryEntity = new SummaryEntity();
					summaryEntity.setSubjectId(voucherSubject.getSubjectId());
					summaryEntity.setAmountDebit(voucherSubject.getAmountDebit());
					summaryEntity.setAmountCredit(voucherSubject.getAmountCredit());
					summaryLists.add(summaryEntity);
				}
			}
		}
		
		int countSubject = voucherSubjectService.batchInsert(bookId,subjectLists);
		if(countSubject > 0){
			voucher.setAmountDebit(amountDebit);
			voucher.setAmountCredit(amountCredit);
			voucher.setAccountCapital(NumberToCN.number2CNMontrayUnit(amountDebit));
			voucher.setUpdateDate(new Date());
			voucher.setUpdateBy(createBy);
			int count = voucherDao.updateVoucherAmount(bookId,voucher);
			if(count > 0){
				String voucherPeriod = DateUtils.formatDate(voucher.getVoucherPeriod(), "yyyy-MM-dd");
				//处理凭证汇总 减去凭证最开始保存科目金额
				voucherSummaryService.dealVoucherSummary(deleteLists, bookId, voucherPeriod,false);
				//处理凭证汇总 加上新保存科目金额
				voucherSummaryService.dealVoucherSummary(summaryLists, bookId,voucherPeriod,true);
				
				return true ;
			}
		}
		return false;
	}

	/**
	 * 根据Id 删除凭证
	 * 2017年12月4日 下午1:43:40
	 * @param bookId
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public Object deleteById(String bookId,String id) {
		
		String voucherIds [] = new String [1] ;
		voucherIds [0] = id ;
		List<SummaryEntity> deleteLists = voucherSubjectService.findDeleteSubjectAmount(bookId, voucherIds);
		
		CpaVoucher voucher = voucherDao.getById(bookId, id);
		if(voucher != null && StringUtils.isNotEmpty(voucher.getId())){
			int count = voucherDao.deleteById(bookId, id);
			if(count > 0){
				//删除凭证所关联科目
				voucherSubjectService.deleteByVoucherId(bookId, id);
				
				String  voucherPeriod = DateUtils.formatDate(voucher.getVoucherPeriod(), "yyyy-MM-dd");
				//查询大于当前编号的数据
				List<CpaVoucher> lists = voucherDao.findGreaterThanVoucherNo(bookId, voucher.getVoucherNo(),voucherPeriod);
				if(lists != null && lists.size() > 0){
					for (CpaVoucher cpaVoucher : lists) {
						if(cpaVoucher != null && StringUtils.isNotEmpty(cpaVoucher.getId())){
							//所有数据编号 减 1
							voucherDao.updateVoucherNo(bookId, cpaVoucher.getId(), NextNoUtils.getPreviousNo(cpaVoucher.getVoucherNo()));
						}
					}
				}
			}
			String voucherPeriod = DateUtils.formatDate(voucher.getVoucherPeriod(), "yyyy-MM-dd");
			//减去要删除科目的金额
			voucherSummaryService.dealVoucherSummary(deleteLists, bookId, voucherPeriod,false);
			//批量更改固定资产状态为正常状态
			fixedAssetService.updateBatchStatusByVoucherIds(voucherIds, FixedAsset.STATUS_NORMAL);
			
			return true;
		}
		throw new LogicException("数据格式不正确");
	}

	/**
	 * 查询当前账期的所有编号
	 * 2017年12月4日 下午2:32:14
	 * @param bookId
	 * @param voucherPeriod
	 * @return
	 */
	@Override
	public List<String> findAllNoByPeriod(String bookId,
			String voucherPeriod) {
		return voucherDao.findAllNoByPeriod(bookId, voucherPeriod);
	}

	/**
	 * 调整凭证编号(可将编号调整至任意编号前)
	 * 2017年12月4日 下午2:51:01
	 * @return
	 */
	@Override
	@Transactional
	public Object adjustmentVoucherNo(String bookId,String id,String newNo) {
		CpaVoucher voucher = voucherDao.getById(bookId, id);
		if(voucher != null && StringUtils.isNotEmpty(voucher.getId())){
			if(Integer.valueOf(voucher.getVoucherNo()) > Integer.valueOf(newNo)){ //往前调
				
				String startNo = newNo ;
				String endNo =  voucher.getVoucherNo();
				adjustmentNo(bookId, voucher.getVoucherNo(), startNo, endNo, true);
				
			}else if(Integer.valueOf(voucher.getVoucherNo()) < Integer.valueOf(newNo)){ //往后调
				
				String startNo = voucher.getVoucherNo() ;
				String endNo = newNo ;
				adjustmentNo(bookId, voucher.getVoucherNo(), startNo, endNo, false);

			}else{
				//不处理
			}
			int count = voucherDao.updateVoucherNo(bookId, id, newNo);
			if(count  > 0){
				return true;
			}
			return false;
		}
		throw new LogicException("数据格式不正确");
	}
	
	/**
	 * 重新整理凭证编号
	 * 2017年12月5日 下午1:33:49
	 * @param lists
	 * @return
	 */	
	@Override
	public Object reorganizeVoucherNo(List<CpaVoucher> lists) {
		if(lists != null && lists.size() > 0){
			for (CpaVoucher cpaVoucher : lists) {
				if(cpaVoucher != null){
					String newNo = NextNoUtils.getNextNo(String.valueOf(cpaVoucher.getSort()-1));
					if(StringUtils.isNotEmpty(cpaVoucher.getId()) && StringUtils.isNotEmpty(cpaVoucher.getBookId())){
						voucherDao.updateVoucherNo(cpaVoucher.getBookId(), cpaVoucher.getId(), newNo);
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 批量删除
	 * 2017年12月6日 上午10:16:24
	 * @param bookId
	 * @param period
	 * @param deleteIds
	 * @return
	 */
	@Override
	@Transactional
	public Object batchDelete(String bookId,String[] deleteIds) {
		
		List<SummaryEntity> deleteLists = voucherSubjectService.findDeleteSubjectAmount(bookId, deleteIds);
		
		String period = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		
		//删除凭证数据
		voucherDao.batchDelete(bookId, deleteIds);
		//删除凭证科目表数据
		voucherSubjectService.batchDeleteByVoucherId(bookId, deleteIds);
		//重新整理凭证编号
		Map<String,String> maps = new HashMap<String, String>();
		maps.put("bookId", bookId);
		maps.put("voucherPeriod", period);
		maps.put("type", "0");
		List<CpaVoucher> lists = this.findAllListByPeriod(maps);
		for (int i = 0; i < lists.size(); i++) {
			if(lists.get(i) != null){
				voucherDao.updateVoucherNo(bookId, lists.get(i).getId(), NextNoUtils.getNextNo(String.valueOf(i)));
			}
		}
		
		//减去要删除科目的金额
		voucherSummaryService.dealVoucherSummary(deleteLists, bookId, period,false);
		
		//批量更改固定资产状态为正常状态
		fixedAssetService.updateBatchStatusByVoucherIds(deleteIds, FixedAsset.STATUS_NORMAL);
		
		return true;
	}

	/**
	 * 查询当前账期所有数据
	 * 2017年12月6日 上午10:55:14
	 * @param bookId
	 * @param voucherPeriod
	 * @return
	 */
	public List<CpaVoucher> findAllListByPeriod(Map<String,String> maps){
		List<CpaVoucher> lists = voucherDao.findAllListByPeriod(maps);
		String voucherPeriod = maps.get("voucherPeriod").toString();
		if(!StringUtils.isNotEmpty(voucherPeriod)){
			maps.put("voucherPeriod", CurrentAccountingUtils.getCurrentVoucherPeriod(maps.get("bookId").toString()));
		}
		if(lists != null && lists.size() > 0){
			for (CpaVoucher voucher : lists) {
				//查询凭证的科目
				dealVoucherSubject(voucher, maps.get("bookId").toString());
			}
		}
		return lists;
	}
	
	/**
	 * 查询需要合并的凭证基础数据
	 * 2017年12月6日 上午10:08:24
	 * @param bookId
	 * @param period
	 * @param mergeIds
	 * @return
	 */
	@Override
	public List<CpaVoucher> findMergeVoucherByIds(String bookId, String[] mergeIds) {
		List<CpaVoucher> lists = voucherDao.findMergeVoucherByIds(bookId,  mergeIds);
		if(lists != null && lists.size() > 0){
			for (CpaVoucher voucher : lists) {
				//查询凭证的科目
				dealVoucherSubject(voucher,bookId);
			}
		}
		return lists;
	}

	/**
	 * 合并凭证数据
	 * 2017年12月6日 下午2:00:09
	 * @param bookId
	 * @param period
	 * @param mergeIds
	 * @return
	 */
	@Override
	@Transactional
	public Object mergeVoucherData(String bookId, String[] mergeIds) {
		List<CpaVoucher> lists = this.findMergeVoucherByIds(bookId, mergeIds);
		if(lists != null && lists.size() > 0){
			CpaVoucher baseVoucher = lists.get(0);
			for (CpaVoucher cpaVoucher : lists) {
				if(cpaVoucher != null && StringUtils.isNotEmpty(cpaVoucher.getId())){
					if(!baseVoucher.getId().equals(cpaVoucher.getId())){
						baseVoucher.setAmountDebit(baseVoucher.getAmountDebit().add(cpaVoucher.getAmountDebit()));
						baseVoucher.setAmountCredit(baseVoucher.getAmountCredit().add(cpaVoucher.getAmountCredit()));
					}
				}
			}
			baseVoucher.setSubjectLists(voucherSubjectService.findMergeVoucherSubject(bookId, mergeIds));
			baseVoucher.setAccountCapital(NumberToCN.number2CNMontrayUnit(baseVoucher.getAmountDebit()));
			mergeIds = ArrayUtils.removeElement(mergeIds, baseVoucher.getId());
			baseVoucher.setBatchIds(mergeIds);
			return baseVoucher ;
		}
		return new LogicException("数据格式不正确");
	}
	
	/**
	 * 凭证合并保存
	 * 2017年12月6日 下午5:22:08
	 * @param voucher
	 * @return
	 */
	@Override
	@Transactional
	public Object mergeVoucherSave(CpaVoucher voucher) {
		String [] batchIds = voucher.getBatchIds();
		updateCpaVoucher(voucher);
		batchDelete(voucher.getBookId(), batchIds);
		return true;
	}

	/**
	 * 复制凭证保存
	 * 2017年12月8日 下午10:15:08
	 * @param voucher
	 * @return
	 */
	@Override
	@Transactional
	public Object pasteVoucherSave(CpaVoucher voucher) {
		String bookId = voucher.getBookId();
		List<CpaVoucher> lists = this.findMergeVoucherByIds(bookId, voucher.getBatchIds());
		
		String voucherPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		String lastDate = DateUtils.getLastMonthDate(voucherPeriod);
		
		List<CpaVoucherSubject> subjectNewLists = new ArrayList<CpaVoucherSubject>();
		
		List<SummaryEntity> summaryList = new ArrayList<SummaryEntity>();
		
		if(lists != null && lists.size() > 0){
			for (CpaVoucher cpaVoucher : lists) {
				if(cpaVoucher != null && StringUtils.isNotEmpty(cpaVoucher.getId())){
					String voucherId = UUIDUtils.getUUid() ;
					cpaVoucher.setId(voucherId);
					cpaVoucher.setVoucherDate(DateUtils.StringToDate(lastDate, "yyyy-MM-dd"));
					cpaVoucher.setVoucherPeriod(DateUtils.StringToDate(voucherPeriod, "yyyy-MM-dd"));
					cpaVoucher.setVoucherNo(getMaxNo(bookId));
					cpaVoucher.setCreateDate(new Date());
					User createBy = new User();
					createBy.setId(UserUtils.getCurrentUserId());
					cpaVoucher.setCreateBy(createBy);
					int count = voucherDao.insert(bookId, cpaVoucher);
					if(count > 0){
						List<CpaVoucherSubject> subjectLists = cpaVoucher.getSubjectLists();
						if(subjectLists != null && subjectLists.size() > 0){
							for (CpaVoucherSubject cpaVoucherSubject : subjectLists) {
								if(cpaVoucherSubject != null && StringUtils.isNotEmpty(cpaVoucherSubject.getId())){
									cpaVoucherSubject.setId(UUIDUtils.getUUid());
									cpaVoucherSubject.setVoucherId(voucherId);
									cpaVoucherSubject.setVoucherPeriod(DateUtils.StringToDate(voucherPeriod, "yyyy-MM-dd"));
									cpaVoucherSubject.setCreateDate(new Date());
									cpaVoucherSubject.setCreateBy(createBy);
									subjectNewLists.add(cpaVoucherSubject);
									
									SummaryEntity summaryEntity = new SummaryEntity();
									summaryEntity.setSubjectId(cpaVoucherSubject.getSubjectId());
									summaryEntity.setAmountDebit(cpaVoucherSubject.getAmountDebit());
									summaryEntity.setAmountCredit(cpaVoucherSubject.getAmountCredit());
									summaryList.add(summaryEntity);
									
								}
							}
						}
					}
				}
			}
			voucherSubjectService.batchInsert(bookId, subjectNewLists);
			
			return true;
		}
		throw new LogicException("没有获取到数据");
	}

	/**
	 *  公共调整凭证编号
	 * 2017年12月4日 下午4:02:48
	 * @param bookId
	 * @param voucherNo
	 * @param startNo
	 * @param endNo
	 * @param direction
	 */
	@Transactional
	private void adjustmentNo(String bookId,String voucherNo,String startNo,String endNo,boolean direction){
		List<CpaVoucher> lists = voucherDao.findAdjustmentVoucherNoData(bookId, startNo, endNo);
		if(lists != null && lists.size() > 0){
			for (CpaVoucher cpaVoucher : lists) {
				if(cpaVoucher != null && StringUtils.isNotEmpty(cpaVoucher.getId())){
					if(!voucherNo.equals(cpaVoucher.getVoucherNo())){
						if(direction){ //所有数据编号 加 1
							voucherDao.updateVoucherNo(bookId, cpaVoucher.getId(), NextNoUtils.getNextNo(cpaVoucher.getVoucherNo()));
						}else{ //所有数据编号 减 1
							voucherDao.updateVoucherNo(bookId, cpaVoucher.getId(), NextNoUtils.getPreviousNo(cpaVoucher.getVoucherNo()));
						}
					}
				}
			}
		}
	}
	
	/**
	 * 公共处理查询凭证的科目
	 * 2017年12月6日 下午2:09:21
	 * @param voucher
	 * @param bookId
	 */
	private void dealVoucherSubject(CpaVoucher voucher,String bookId){
		if(voucher != null && StringUtils.isNotEmpty(voucher.getId())){
			List<CpaVoucherSubject> subjectLists = voucherSubjectService.findByVoucherId(bookId,voucher.getId());
			if(subjectLists != null && subjectLists.size() > 0){
				voucher.setSubjectLists(subjectLists);
			}
		}
	}
	
	/**
	 * 查询下一个编号
	 * 2017年12月8日 下午4:20:01
	 * @param bookId
	 * @return
	 */
	private String getMaxNo(String bookId){
		String maxNo = voucherDao.getMaxNo(bookId, CurrentAccountingUtils.getCurrentVoucherPeriod(bookId));
		String nextNo = "001";
		if(StringUtils.isNotEmpty(maxNo)){
			nextNo = NextNoUtils.getNextNo(maxNo);
		}
		return nextNo ;
	}

	
	/**
	 * 
	* @Description: TODO(凭证汇总表) 
	* @param voucherPeriod 账期
	* @param bookId 账薄ID
	* @param isSum 是否显示统计
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月19日 
	 */
	@Override
	public Object findVoucherSummaryListAndStatistics(String voucherPeriod,String bookId,boolean isSum) {
		
	 List<CpaVoucherSummary> list =  voucherSummaryService.findAllList(bookId, voucherPeriod);
		
	 if(CollectionUtils.isNotEmpty(list) && isSum){
		 list.add(voucherSummaryService.findSumDebitAndCredit(bookId, voucherPeriod, "0"));
	 }
	 
		return list;
	}

	/**
	 * 判断 凭证是否断号
	 * 2017年12月26日 下午6:13:56
	 * @param voucherPeriod
	 * @param bookId
	 * @return
	 */
	@Override
	public boolean isFaultVoucherNo(String voucherPeriod, String bookId) {
		String maxNo = voucherDao.getMaxNo(bookId, voucherPeriod);
		Integer size = voucherDao.findAllSize(bookId, voucherPeriod);
		
		if(StringUtils.isEmpty(maxNo)){
			maxNo = "0";
		}
		
		if(Integer.valueOf(maxNo) == size){
		 	return true ;
		}
		return false;
	}
	
}
