package com.taoding.service.settleaccount;

import java.util.List;

import com.taoding.domain.settleaccount.CpaFinalLiquidation;
import com.taoding.domain.voucher.CpaVoucher;


/**
 * 期末结转
 * @author czy
 * 2017年12月21日 下午3:59:09
 */
public interface CpaFinalLiquidationService {
	
	/**
	 * 获取企业 期末结转数据
	 * 2017年12月21日 下午3:59:52
	 * @param bookId
	 * @return
	 */
	public Object loadFinalLiquidationData(String bookId);
	
	/**
	 * 新增期末结转数据
	 * 2017年12月22日 下午4:30:23
	 * @param finalLiquidation
	 * @return
	 */
	public int insert(CpaFinalLiquidation finalLiquidation);
	
	/**
	 * 根据账期+账簿ID 判断是否期末结转
	 * 2017年12月22日 下午4:31:33
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	public CpaFinalLiquidation findByBookId(String bookId ,String currentPeriod);
	
	/**
	 * 查看要生成的凭证
	 * 2017年12月23日 上午10:03:34
	 * @param bookId
	 * @param subKey
	 * @return
	 */
	public CpaVoucher showBuildVoucher(String bookId ,String subKey);
	
	/**
	 * 生成凭证
	 * 2017年12月23日 上午10:28:51
	 * @param finalLiquidation
	 * @return
	 */
	public Object generateVoucher(CpaFinalLiquidation finalLiquidation) ;
	
	/**
	* @Description:获得 结转本期损益（收入） 科目list
	* @param bookId
	* @return List<String> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月27日
	 */
	public List<String> get_JZ_CURRENT_PROFIT_REVENUE_List(String bookId);
}
