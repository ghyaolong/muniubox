package com.taoding.service.settleaccount;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.taoding.domain.settleaccount.CpaFinalLiquidationOperatingData;

/**
 * 经营数据分析相关接口
 * @author LX-PC
 *
 */
public interface CpaFinalLiquidationOperatingDataService {
	
	/**
	 * 加载 结账经营数据分析
	 * 2017年12月27日 下午5:05:44
	 * @param bookId
	 * @return
	 */
	public Object loadOperatingData(String bookId);
	
	/**
	 * 插入一条 分析数据
	 * @param bookId	所属账簿ID
	 * @param type		数据的类型
	 * @param value		数据的值
	 * @param accountDate	数据所属的账期
	 */
	public void insertOperatingData(String bookId, OperatingDataType type, BigDecimal value, Date accountDate);
	
	/**
	 * 插入一条 分析数据
	 * @param bookId		所属账簿ID
	 * @param customerId	客户ID
	 * @param type			数据的类型
	 * @param value			数据的值
	 * @param accountDate	数据所属的账期	
	 */
	public void insertOperatingData(String bookId, String customerId, OperatingDataType type, BigDecimal value, Date accountDate);
	/**
	 * 按照数据类型 获取已记录的账期的分析数据
	 * @param bookId	账簿ID	
	 * @param type		数据类型
	 * @param preMonth	距离当前账期的月份
	 */
	public List<CpaFinalLiquidationOperatingData> getOperatingDatas(String bookId, OperatingDataType type, int preMonth);
	
	/**
	 * 对一类分析数据求平均值
	 * @param datas	数据
	 * @return
	 */
	public BigDecimal avgOfOperatingDatas(List<CpaFinalLiquidationOperatingData> datas);
	
	/**
	 * 删除指定账期内的经营分析数据
	 * @param bookId	所属账簿ID
	 * @param accountDate	所属账期
	 */
	public void deleteOperatingData(String bookId, Date accountDate);

}
