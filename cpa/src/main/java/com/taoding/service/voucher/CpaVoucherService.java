package com.taoding.service.voucher;

import java.util.List;
import java.util.Map;

import com.taoding.domain.voucher.CpaVoucher;

/**
 * 凭证
 * @author czy
 * 2017年11月28日 上午11:22:21
 */
public interface CpaVoucherService {

    /**
     * 为一个客户初始化一张凭证科目表
     * @param bookId
     * @return
     */
    int init(String bookId);
	
	/**
	 * 新增凭证及凭证科目
	 * 2017年11月29日 上午10:00:02
	 * @param voucher
	 * @return
	 */
	public Object insertCpaVoucher(CpaVoucher voucher);
	
	/**
	 * 通过Id查询凭证及凭证科目
	 * 2017年11月29日 上午10:24:37
	 * @param id
	 * @return
	 */
	public CpaVoucher findById(String bookId,String id);
	
	/**
	 * 获取下一个记账凭证编号
	 * 2017年11月29日 下午1:47:42
	 * @param bookId
	 * @return
	 */
	public Object getNextVoucherNo(String bookId);
	
	/**
	 * 通过账期+凭证编号查询凭证信息
	 * 2017年11月29日 下午3:55:19
	 * @param bookId
	 * @param voucherNo
	 * @param voucherPeriod
	 * @return
	 */
	public CpaVoucher findByNoAndPeriod(String bookId,String voucherNo,String voucherPeriod);
	
	/**
	 * 修改凭证
	 * 2017年12月4日 上午10:23:02
	 * @param voucher
	 * @return
	 */
	public Object updateCpaVoucher(CpaVoucher voucher);
	
	/**
	 * 根据Id 删除凭证
	 * 2017年12月4日 下午1:43:40
	 * @param bookId
	 * @param id
	 * @return
	 */
	public Object deleteById(String bookId,String id);
	
	/**
	 * 查询当前账期的所有编号
	 * 2017年12月4日 下午2:32:14
	 * @param bookId
	 * @param voucherPeriod
	 * @return
	 */
	public List<String> findAllNoByPeriod(String bookId,String voucherPeriod);
	
	/**
	 * 调整凭证编号(可将编号调整至任意编号前)
	 * 2017年12月4日 下午2:51:01
	 * @param bookId
	 * @param id
	 * @param voucherNo
	 * @return
	 */
	public Object adjustmentVoucherNo(String bookId,String id,String voucherNo);
	
	/**
	 * 重新整理凭证编号
	 * 2017年12月5日 下午1:33:49
	 * @param lists
	 * @return
	 */	
	public Object reorganizeVoucherNo(List<CpaVoucher> lists);
	
	/**
	 * 批量删除
	 * 2017年12月6日 上午10:08:24
	 * @param bookId
	 * @param deleteIds
	 * @return
	 */
	public Object batchDelete(String bookId,String [] deleteIds);
	
	/**
	 * 查询当前账期所有数据
	 * 2017年12月6日 上午10:55:14
	 * @param maps
	 * @return
	 */
	public List<CpaVoucher> findAllListByPeriod(Map<String,String> maps);
	
	/**
	 * 查询需要合并的凭证基础数据
	 * 2017年12月6日 上午10:08:24
	 * @param bookId
	 * @param mergeIds
	 * @return
	 */
	public List<CpaVoucher> findMergeVoucherByIds(String bookId,String [] mergeIds);
	
	/**
	 * 凭证合并数据
	 * 2017年12月6日 下午2:00:09
	 * @param bookId
	 * @param period
	 * @param mergeIds
	 * @return
	 */
	public Object mergeVoucherData(String bookId,String [] mergeIds);
	
	/**
	 * 凭证合并保存
	 * 2017年12月6日 下午5:22:08
	 * @param voucher
	 * @return
	 */
	public Object mergeVoucherSave(CpaVoucher voucher);

	/**
	 * 复制凭证保存
	 * 2017年12月8日 下午10:15:08
	 * @param voucher
	 * @return
	 */
	public Object pasteVoucherSave(CpaVoucher voucher);
	
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
	public Object findVoucherSummaryListAndStatistics(String voucherPeriod,String bookId,boolean isSum);
	
	/**
	 * 判断 凭证是否断号
	 * 2017年12月26日 下午6:13:56
	 * @param voucherPeriod
	 * @param bookId
	 * @return
	 */
	public boolean isFaultVoucherNo(String voucherPeriod,String bookId);
 
}
