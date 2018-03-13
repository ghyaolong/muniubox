package com.taoding.service.subject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.NextNoUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.common.utils.UserUtils;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.assisting.CpaCustomerAssisting;
import com.taoding.domain.subject.CpaAssistingData;
import com.taoding.domain.subject.CpaAssistingEntity;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.domain.subject.CpaSubject;
import com.taoding.domain.subject.CpaSubjectAssisting;
import com.taoding.domain.user.User;
import com.taoding.domain.voucher.CpaVoucherSubject;
import com.taoding.domain.voucher.CpaVoucherTempleteSubject;
import com.taoding.domain.vouchersummary.CpaVoucherSummary;
import com.taoding.mapper.subject.CpaCustomerSubjectDao;
import com.taoding.service.accountingBook.AccountingBookService;
import com.taoding.service.assisting.CpaCustomerAssistingService;
import com.taoding.service.voucher.CpaVoucherSubjectService;
import com.taoding.service.voucher.CpaVoucherTempleteSubjectService;

/**
 * 科目期初
 * 
 * @author czy 2017年11月20日 上午11:38:46
 */
@Service
public class CpaCustomerSubjectServiceImpl extends
		DefaultCurdServiceImpl<CpaCustomerSubjectDao, CpaCustomerSubject>
		implements CpaCustomerSubjectService {

	@Autowired
	private CpaSubjectService subjectService;
	@Autowired
	private CpaSubjectAssistingService subjectAssistingService ;
	@Autowired
	private CpaCustomerAssistingService  customerAssistingService ;
	@Autowired
	private AccountingBookService accountingBookService ;
	@Autowired
	private CpaVoucherSubjectService voucherSubjectService;
	@Autowired
	private CpaVoucherTempleteSubjectService voucherTempleteSubjectService;
	
	/**
	 * 初始化企业科目期初数据
	 * 2017年12月6日 下午6:12:53
	 * @param bookId
	 * @param customerId
	 */
	@Transactional
	public void init(String bookId,String customerId){
		this.batchInsert(bookId,customerId);
	}
	
	
	/**
	 * 根据账簿ID查询该账簿下所有科目信息 2017年11月20日 下午1:55:43
	 * 
	 * @param bookId
	 * @param hashChild
	 * @return
	 */
	@Override
	public List<CpaCustomerSubject> findAllList(String bookId,boolean hashChild) {
		return dao.findAllList(bookId,hashChild);
	}

	/**
	 * 新增企业的会计科目 
	 * 2017年11月20日 上午11:44:39
	 * @param customerSubject
	 * @return
	 */
	@Override
	@Transactional
	public Object insertCustomerSubject(CpaCustomerSubject customerSubject) {
		CpaCustomerSubject subject = this.findByParentNoAndName(
				customerSubject.getBookId(), customerSubject.getParent(),
				customerSubject.getSubjectNo());
		
		if(subject != null && StringUtils.isNotEmpty(subject.getId())){
			throw new LogicException("科目编号已经存在");
		}
		
		CpaCustomerSubject subject2 = this.findByParentNoAndName(
				customerSubject.getBookId(), customerSubject.getParent(),
				customerSubject.getSubjectName());
		
		if(subject2 != null && StringUtils.isNotEmpty(subject2.getId())){
			throw new LogicException("科目已经存在");
		}
		
		CpaCustomerSubject parentCustomer = this.findBySubjectNo(customerSubject.getBookId(), customerSubject.getParent());
		
		List<CpaVoucherSubject> subjectLists = voucherSubjectService.findBySubjectId(customerSubject.getBookId(), parentCustomer.getId());
		if(subjectLists != null && subjectLists.size() > 0){
			throw new LogicException("当前科目下存在凭证，不能添加下级科目");
		}
		List<CpaVoucherTempleteSubject>  templeteSubjectLists = voucherTempleteSubjectService.findBySubjectId(customerSubject.getBookId(), parentCustomer.getId());
		if(templeteSubjectLists != null && templeteSubjectLists.size() > 0){
			throw new LogicException("当前科目已被凭证模板引用，不能添加下级科目");
		}
		
		String id = UUIDUtils.getUUid();
		
		User createBy = new User();
		createBy.setId(UserUtils.getCurrentUserId());
		
		Map<String,Object> maps = new HashMap<String, Object>();
		
		String isAssisting [] = customerSubject.getAssistingdata();
		if(isAssisting != null && isAssisting.length > 0 ){
			customerSubject.setAssisting(true);
			for (int i = 0; i < isAssisting.length; i++) {
				if(StringUtils.isNotEmpty(isAssisting[i])){
					CpaSubjectAssisting assisting = new CpaSubjectAssisting();
					assisting.setSubjectId(id);
					assisting.setAssistantId(isAssisting[i]);
					assisting.setCreateDate(new Date());
					assisting.setCreateBy(createBy);
					subjectAssistingService.save(assisting);
				}
			}
		}else{
			customerSubject.setAssisting(false);
		}
		
		//处理是否有子节点
		if(!updateHashChild(customerSubject,1)){
			throw new LogicException("处理是否是子节点失败！");
		};
		
		customerSubject.setBasicSubject(false);
		customerSubject.setId(id);
		customerSubject.setSubjectNo(getNextNo(customerSubject.getBookId(), customerSubject.getParent()));
		customerSubject.setCreateDate(new Date());
		customerSubject.setCreateBy(createBy);
		
		int count = dao.insert(customerSubject) ;
		if(count > 0){
			maps.put("state", true);
			maps.put("id", id);
		}else{
			maps.put("state",false);
		}
		return maps;
	}

	/**
	 * 批量新增科目  给企业默认生成基础数据调用
	 * 2017年11月20日 上午11:45:37
	 * @param bookId
	 * @return
	 */
	@Override
	@Transactional
	public int batchInsert(String bookId,String customerId) {

		List<CpaCustomerSubject> customerSubjectLists = new ArrayList<CpaCustomerSubject>();
		AccountingBook accountingBook = accountingBookService.get(bookId);
		if(accountingBook == null || !StringUtils.isNotEmpty(accountingBook.getId())){
			throw new LogicException("查询不到账簿信息");
		}
		
		boolean type = false ;
		if(accountingBook.getAccountingSystemId().equals("1")){ //小企业会计准则
			type = true ;
		}
		
		List<CpaSubject> subjectLists = subjectService.findAllList(type);
		
		if (subjectLists != null && subjectLists.size() > 0) {
			for (int i = 0; i < subjectLists.size(); i++) {
				CpaSubject subject = subjectLists.get(i);
				if (subject != null) {
					CpaCustomerSubject customerSubject = new CpaCustomerSubject();
					customerSubject.setId(subject.getId());
					customerSubject.setBookId(bookId);
					customerSubject.setCustomerId(customerId);
					customerSubject.setCatalogId(subject.getCatalogId());
					customerSubject.setSubjectNo(subject.getSubjectNo());
					customerSubject.setSubjectName(subject.getSubjectName());
					customerSubject.setDirection(subject.isDirection());
					customerSubject.setParent(subject.getParent());
					customerSubject.setLevel(subject.getLevel());
					customerSubject.setHashChild(subject.isHashChild());
					customerSubject.setAssisting(false);
					customerSubject.setSubject(false);
					customerSubject.setFinish(false);
					customerSubject.setInventory(false);
					customerSubject.setBasicSubject(true);
					User createBy = new User();
					createBy.setId(UserUtils.getCurrentUserId());
					customerSubject.setCreateBy(createBy);
					customerSubject.setCreateDate(new Date());
					customerSubjectLists.add(customerSubject);
				}
			}
			return dao.batchInsert(customerSubjectLists);
		}
		return 0;
	}

	/**
	 * 修改科目
	 * 2017年11月20日 下午5:08:30
	 * @param customerSubject
	 * @return
	 */
	@Override
	public Object updateCustomerSubject(CpaCustomerSubject customerSubject) {
		CpaCustomerSubject subject = this.findByParentNoAndName(customerSubject.getBookId(), 
				customerSubject.getParent(), customerSubject.getSubjectName());
		if(subject != null && !subject.getId().equals(customerSubject.getId())){
			throw new LogicException("科目已经存在");
		}
		
		String isAssisting [] = customerSubject.getAssistingdata();

		User createBy = new User();
		createBy.setId(UserUtils.getCurrentUserId());
		
		//删除之前选择的辅助核算和科目之间的关系
		subjectAssistingService.deleteBySubjectId(customerSubject.getId());

		if(isAssisting != null && isAssisting.length > 0 ){
			customerSubject.setAssisting(true);
			for (int i = 0; i < isAssisting.length; i++) {
				if(StringUtils.isNotEmpty(isAssisting[i])){
					CpaSubjectAssisting assisting = new CpaSubjectAssisting();
					assisting.setSubjectId(customerSubject.getId());
					assisting.setAssistantId(isAssisting[i]);
					assisting.setCreateDate(new Date());
					assisting.setCreateBy(createBy);
					subjectAssistingService.save(assisting);
				}
			}
		}else{
			customerSubject.setAssisting(false);
		}
		customerSubject.setUpdateBy(createBy);
		customerSubject.setUpdateDate(new Date());
		
		if(!updateHashChild(customerSubject,2)){
			throw new LogicException("处理是否是子节点失败！");
		};
		int count = dao.update(customerSubject);
		if(count > 0){
			return true ;
		}
		return false ;
	}
	
	/**
	 * 根据父No生成下一个科目编号 2017年11月20日 下午3:55:11
	 * 
	 * @param parentNo
	 * @return
	 */
	@Override
	public String getNextNo(String bookId, String parentNo) {

		String maxNo = dao.findMaxNoByParentNo(bookId, parentNo);
		String nextNo = "";
		if (StringUtils.isNotEmpty(maxNo)) {
			nextNo = dealNextNo(maxNo);
		} else {
			nextNo = "001";
		}
		return parentNo + "." + nextNo;
	}

	/**
	 * 获取下一个科目编号 2017年11月20日 下午3:57:11
	 * 
	 * @param maxNo
	 * @return
	 */
	public String dealNextNo(String maxNo) {
		String subData = maxNo.substring(maxNo.lastIndexOf(".") + 1, maxNo.length());
		return NextNoUtils.getNextNo(subData);
	}

	/**
	 * 查询当前父节点下科目名称唯一性 2017年11月20日 下午4:15:31
	 * 
	 * @param bookId
	 * @param parentNo
	 * @param subjectName
	 * @return
	 */
	@Override
	public CpaCustomerSubject findByParentNoAndName(String bookId,
			String parentNo, String subjectName) {
		return dao.findByParentNoAndName(bookId, parentNo, subjectName);
	}

	/**
	 * 根据父No查询 其下所有子科目信息 
	 * 2017年11月22日 上午9:18:11
	 * @param bookId
	 * @param parentNo
	 * @return
	 */
	@Override
	public List<CpaCustomerSubject> findListByParentNo(String bookId, String parentNo) {
		return dao.findListByParentNo(bookId, parentNo);
	}

	/**
	 * 根据id删除科目
	 * 2017年11月20日 上午11:45:37
	 * @param bookId
	 * @return
	 */
	@Override
	@Transactional
	public Object deleteById(String id) {
		CpaCustomerSubject customerSubject = this.get(id);
		if(customerSubject != null && StringUtils.isNotEmpty(customerSubject.getSubjectNo())){
			List<CpaCustomerSubject> lists = this.findListByParentNo(customerSubject.getBookId(), customerSubject.getSubjectNo());
			if(lists != null && lists.size() > 0){
				throw new LogicException("当前科目下存在子科目，不能删除");
			}
			List<CpaVoucherSubject> subjectLists = voucherSubjectService.findBySubjectId(customerSubject.getBookId(), id);
			if(subjectLists != null && subjectLists.size() > 0){
				throw new LogicException("当前科目下存在凭证，不能删除");
			}
			List<CpaVoucherTempleteSubject>  templeteSubjectLists = voucherTempleteSubjectService.findBySubjectId(customerSubject.getBookId(), id);
			if(templeteSubjectLists != null && templeteSubjectLists.size() > 0){
				throw new LogicException("当前科目已被凭证模板引用，不能删除");
			}

			int count = dao.delete(id);
			if(count > 0){
				if(!updateHashChild(customerSubject,3)){
					throw new LogicException("处理是否是子节点失败！");
				};
				return true ;
			}
			return false ;
		}
		throw new LogicException("删除失败，该条数据格式不正确");
	}

	
	/**
	 * 
	* @Description: TODO(处理是否有子节点字段)
	* @param customerSubject  
	* @param bookId 账簿ＩＤ
	* @param parentNo　父编号
	* @param operationType 操作类型 1 add 2 update 3 delete
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月16日
	 */
	private boolean updateHashChild(CpaCustomerSubject customerSubject,int operationType){
		
		boolean flag = true;
		if(1 == operationType && null != customerSubject){
		
			
			CpaCustomerSubject parentSubject = this.findBySubjectNo(customerSubject.getBookId(), customerSubject.getParent());
			
			if(null != parentSubject && !parentSubject.isAssisting()){
				parentSubject.setHashChild(true);
				flag = dao.update(parentSubject)>0;
			}
			
			List<CpaCustomerSubject>  sameLevels = this.findListByParentNo(customerSubject.getBookId(),customerSubject.getSubjectNo());
			if(null == sameLevels || sameLevels.size() == 0){
				parentSubject.setHashChild(false);
			}else{
				customerSubject.setHashChild(true);
			}
			
			
		}else if(2 == operationType && null != customerSubject){
			if(customerSubject.isAssisting()){//辅助核算  目前没有 验证父节点的业务，先不考虑
				customerSubject.setHashChild(false);
			}else {//判断是否有子节点
				customerSubject.setHashChild(true);//没有
			}
			flag = true;
		}else if(3 == operationType && null != customerSubject){
			//删除判断父节点是否还有子节点   在删除后执行
			List<CpaCustomerSubject>  sameLevels = this.findListByParentNo(customerSubject.getBookId(),customerSubject.getParent());
			if(null == sameLevels || sameLevels.size() == 0){
				CpaCustomerSubject parentSubject = this.findBySubjectNo(customerSubject.getBookId(), customerSubject.getParent());
				parentSubject.setHashChild(false);
				flag = dao.update(parentSubject)>0;
			}
		}else{
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * 企业金额试算平衡数据
	 * 2017年11月22日 上午10:47:11
	 * @param bookId
	 * @return
	 */
	@Override
	public Object trialBalanceData(String bookId) {

		Map<String, Object> maps = new HashMap<String, Object>();
		
		Object beginTotal = dao.totalBeginningMoney(bookId);
		Object currentTotal = dao.totalCurrentYearMoney(bookId);
		
		maps.put("beginTotal", beginTotal) ;
		maps.put("currentTotal",currentTotal) ;
		
		return maps;
	}

	/**
	 * 根据id查询科目
	 * 2017年11月22日 下午4:26:13
	 * @param id
	 * @return
	 */
	@Override
	public CpaCustomerSubject getById(String bookId,String id) {
		CpaCustomerSubject subject = dao.getById(bookId,id);
		if(subject != null){
			List<CpaCustomerAssisting> lists = customerAssistingService.findListBySubjectId(id);
			if(lists != null && lists.size() > 0){
				subject.setAssistingLists(lists);
				subject.setAssistingSize(lists.size());
			}
		}
		return subject;
	}

	/**
	 * 根据企业ID修改结账状态
	 * 2017年11月22日 下午5:18:08
	 * @param finish
	 * @param bookId
	 * @return
	 */
	@Override
	@Transactional
	public Object updateFinishByBookId(boolean finish, String bookId) {
		int count = dao.updateFinishByBookId(finish, bookId);
		if(count > 0){
			return true;
		}
		return false;
	}

	/**
	 * 新增辅助核算项科目
	 * 2017年11月23日 下午4:12:58
	 * @param cpaAssistingData
	 * @return
	 */
	@Override
	@Transactional
	public Object insertAssistingSubject(CpaAssistingData cpaAssistingData) {
		if(!StringUtils.isNotEmpty(cpaAssistingData.getId())){
			throw new LogicException("数据格式不正确");
		}
		Map<String,Object> maps = new HashMap<String, Object>();
		CpaCustomerSubject customerSubject = this.get(cpaAssistingData.getId());
		if(customerSubject != null){
			List<CpaAssistingEntity> lists = cpaAssistingData.getLists();
			if(lists != null && lists.size() > 0){
				//只有一个辅助核算项
				if(cpaAssistingData.getAssistingSize() == 1){
					String id = "";
					for (CpaAssistingEntity assistingEntity : lists) {
						if(assistingEntity != null){
							String subjectNo = customerSubject.getSubjectNo()+"_"+assistingEntity.getNo();
							String subjectName = customerSubject.getSubjectName()+"_"+assistingEntity.getName();
							CpaCustomerSubject checkSubject = this.findByParentNoAndName(customerSubject.getBookId(), 
									customerSubject.getSubjectNo(), subjectName);
							
							if(checkSubject == null || !StringUtils.isNotEmpty(checkSubject.getId())){
								//判断是不是存货
								boolean inventory = false ;
								String type = assistingEntity.getType();
								if( StringUtils.isNotEmpty(type) && CpaCustomerAssisting.TYPE_GOODS.equals(type)){
									inventory = true ;
								}
								id = saveAssistingSubject(customerSubject.getBookId(),customerSubject.getCustomerId(), 
										customerSubject.getCatalogId(),subjectNo, subjectName, customerSubject.isDirection(), 
										customerSubject.getSubjectNo(), customerSubject.getLevel()+1,inventory);
							}
						}
					}
					if(StringUtils.isNotEmpty(id)){
						maps.put("state",true);
						maps.put("id",id);
					}else{
						maps.put("state",false);
					}
				}else{
					String code = "", name = "";
					boolean inventory = false ;
					for (CpaAssistingEntity cpaAssistingEntity : lists) {
						code += "_"+cpaAssistingEntity.getNo();
						name += "_"+cpaAssistingEntity.getName();
						//判断是不是存货
						String type = cpaAssistingEntity.getType();
						if( StringUtils.isNotEmpty(type) && CpaCustomerAssisting.TYPE_GOODS.equals(type)){
							inventory = true ;
						}
					}
					if(code != null && code.length() > 0){
						
						String subjectNo = customerSubject.getSubjectNo()+code ;
						String subjectName = customerSubject.getSubjectName()+name ;
						CpaCustomerSubject checkSubject = this.findByParentNoAndName(customerSubject.getBookId(), 
								customerSubject.getSubjectNo(), subjectName);
						
						if(checkSubject == null || !StringUtils.isNotEmpty(checkSubject.getId())){
							String id = saveAssistingSubject(customerSubject.getBookId(),customerSubject.getCustomerId(), 
									customerSubject.getCatalogId(), subjectNo, subjectName, customerSubject.isDirection(), 
									customerSubject.getSubjectNo(), customerSubject.getLevel()+1,inventory);
							if(StringUtils.isNotEmpty(id)){
								maps.put("state",true);
								maps.put("id",id);
							}else{
								maps.put("state",false);
							}
						}else{
							maps.put("state",false);
						}
					}
				}
			}
		}
		return maps;
	}
	
	/**
	 * 公共新增辅助核算项方法
	 * 2017年12月1日 下午1:33:42
	 * @param bookId
	 * @param catalogId
	 * @param subjectNo
	 * @param subjectName
	 * @param direction
	 * @param parent
	 * @param level
	 */
	@Transactional
	public String saveAssistingSubject(String bookId,String customerId,String catalogId,
			String subjectNo,String subjectName,boolean direction,
			String parent,Integer level,boolean inventory){
		CpaCustomerSubject assistingSubject = new CpaCustomerSubject() ;
		String id = UUIDUtils.getUUid();
		assistingSubject.setId(id);
		assistingSubject.setBookId(bookId);
		assistingSubject.setCustomerId(customerId);
		assistingSubject.setCatalogId(catalogId);
		assistingSubject.setSubjectNo(subjectNo);
		assistingSubject.setSubjectName(subjectName);
		assistingSubject.setDirection(direction);
		assistingSubject.setParent(parent);
		assistingSubject.setBasicSubject(false);
		assistingSubject.setLevel(level);
		assistingSubject.setAssisting(false); //// 0: 非辅助核算项 // 1: 是辅助核算项
		assistingSubject.setSubject(true); // 是否是科目，如果是科目
		assistingSubject.setFinish(false);
		assistingSubject.setInventory(inventory);
		assistingSubject.setCreateDate(new Date());
		User createBy = new User();
		createBy.setId(UserUtils.getCurrentUserId());
		assistingSubject.setCreateBy(createBy);
		int count = dao.insert(assistingSubject);
		if(count > 0){
			return id ;
		}
		return null;
	}
	
	/**
	 * 修改科目期初金额
	 * 2017年11月24日 上午9:42:28
	 * @param customerSubject
	 * @return
	 */
	@Override
	@Transactional
	public Object updateSubjectMoney(CpaCustomerSubject customerSubject) {
		String catalogId = customerSubject.getCatalogId();
		BigDecimal beginningBalances = customerSubject.getBeginningBalances();
		BigDecimal currentYearDebit = customerSubject.getCurrentYearDebit();
		BigDecimal currentYearCredit = customerSubject.getCurrentYearCredit();
		boolean direction = customerSubject.isDirection();// 1：借 0：贷
		if(StringUtils.isNotEmpty(catalogId)){
			if("5".equals(catalogId)){ //损益类科目 id 为 5
				//返回的结果是int类型，-1表示小于，0是等于，1是大于。
				int isCompare = currentYearDebit.compareTo(currentYearCredit);
				if(isCompare == 1){
					customerSubject.setCurrentYearCredit(currentYearDebit); 
					customerSubject.setProfitAndLose(currentYearDebit);
				}else if(isCompare == -1){
					customerSubject.setCurrentYearDebit(currentYearCredit);
					customerSubject.setProfitAndLose(currentYearCredit);
				}else{
					customerSubject.setProfitAndLose(currentYearDebit);
				}
				customerSubject.setInitBalance(beginningBalances);
			}else{
				BigDecimal initBalance;
				if(direction){ //借方   年初余额 = 期初余额 - 本年累计借方 + 本年累计贷方
					initBalance = beginningBalances.subtract(currentYearDebit).add(currentYearCredit);
				}else{ //贷方  年初余额 = 期初余额 + 本年累计借方 - 本年累计贷方
					initBalance = beginningBalances.add(currentYearDebit).subtract(currentYearCredit);
				}
				customerSubject.setInitBalance(initBalance);
			}
			int count = dao.updateSubjectMoney(customerSubject);
			if(count > 0){
				//修改成功  实时更新父节点金额
				this.computeParentMoney(customerSubject.getBookId(), customerSubject.getParent());
				return true ;
			}
			return false ;
		}
		throw new LogicException("数据格式不正确");
	}

	/**
	 * 计算父节点金额
	 * 2017年11月24日 上午10:52:22
	 * @param parentNo
	 * @return
	 */
	@Transactional
	public void computeParentMoney(String bookId,String parentNo){
		if(parentNo != null && !"0".equals(parentNo)){
			CpaCustomerSubject parentSubject  = dao.findBySubjectNo(bookId, parentNo);
			if(parentSubject != null && StringUtils.isNotEmpty(parentSubject.getId())){
				CpaCustomerSubject totalSubject = dao.totalByParentNo(bookId, parentNo);
				if(totalSubject != null){
					parentSubject.setBeginningBalances(totalSubject.getBeginningBalances());
					parentSubject.setCurrentYearDebit(totalSubject.getCurrentYearDebit());
					parentSubject.setCurrentYearCredit(totalSubject.getCurrentYearCredit());
					parentSubject.setInitBalance(totalSubject.getInitBalance());
					parentSubject.setProfitAndLose(totalSubject.getProfitAndLose());
					parentSubject.setUpdateDate(new Date());
					User updateBy = new User();
					updateBy.setId(UserUtils.getCurrentUserId());
					parentSubject.setUpdateBy(updateBy);
					dao.updateSubjectMoney(parentSubject);
				}
			}
			computeParentMoney(bookId, parentSubject.getParent());
		}
	}

	/**
	 * 根据id 查询科目信息及科目余额
	 * 2017年11月30日 上午10:27:24
	 * @param id
	 * @return
	 */
//	@Override
//	public CpaCustomerSubject findInfoAndAmountById(String bookId, String id) {
//		
//		String currentPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
//		String nextPeriod = DateUtils.getPerFirstDayOfMonth(DateUtils.StringToDate(currentPeriod, "yyyy-MM-dd"));
//		
//		CpaCustomerSubject customerSubject = dao.findInfoAndAmountById(bookId, id,nextPeriod);
//		if(customerSubject != null){
//			BigDecimal surplusAmount;
//			if (customerSubject.isDirection()) { // 借方  余额 = 期初  + 借方 - 贷方
//				surplusAmount = customerSubject.getBeginningBalances()
//						.add(customerSubject.getSumDebit()).subtract(customerSubject.getSumCredit());
//			} else { // 贷方  余额 = 期初  + 贷方 - 借方
//				surplusAmount = customerSubject.getBeginningBalances()
//						.add(customerSubject.getSumCredit()).subtract(customerSubject.getSumDebit());
//			}
//			customerSubject.setSurplusAmount(surplusAmount);
//			
//			List<CpaCustomerAssisting> lists = customerAssistingService.findListBySubjectId(id);
//			if(lists != null && lists.size() > 0){
//				customerSubject.setAssistingLists(lists);
//				customerSubject.setAssistingSize(lists.size());
//			}
//		}
//		return customerSubject;
//	}

	/**
	 * 通过科目编号查询科目信息 
	 * 2017年12月1日 下午3:55:57
	 * @param bookId
	 * @param subjectNo
	 * @return
	 */
	@Override
	public CpaCustomerSubject findBySubjectNo(String bookId, String subjectNo) {
		return dao.findBySubjectNo(bookId, subjectNo);
	}

	/**
	 * 根据ID查询科目信息，此方法只供凭证汇总模块使用
	 * 2017年12月13日 下午5:23:21
	 * @param bookId
	 * @param id
	 * @return
	 */
	@Override
	public CpaVoucherSummary getVoucherSummarySubjectById(String bookId,String id){
		return dao.getVoucherSummarySubjectById(bookId, id);
	}

	/**
	 * 通过科目编号查询科目信息，此方法只供凭证汇总模块使用
	 * 2017年12月14日 上午9:42:44
	 * @param bookId
	 * @param subjectNo
	 * @return
	 */
	@Override
	public CpaVoucherSummary getVoucherSummaryBySubjectNo(String bookId, String subjectNo) {
		return dao.getVoucherSummaryBySubjectNo(bookId, subjectNo);
	}
	
}
