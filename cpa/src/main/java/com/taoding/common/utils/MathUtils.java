package com.taoding.common.utils;

import java.math.BigDecimal;
import java.util.Date;

import com.taoding.configuration.Global;
import com.taoding.domain.fixedAsset.AssetType;

/**
 * 
 * @ClassName: MathUtils
 * @Description: TODO(用于一些简单的运算)
 * @author lixc
 * @date 2017年11月30日 上午10:23:14
 *
 */
public class MathUtils {

	/**
	 * 
	 * @Description: TODO(运算月折旧额) 年折旧率 =（1 - 残值率）/ 使用期限（年）* 100% 月折旧率 = 年折旧率 /
	 *               12 月折旧额 = 原值 * 月折旧率
	 * 
	 *               比如，原值10000元，残值率5%，使用期限为120个月 年折旧率 = (1 - 0.05)/(120 / 12) *
	 *               100 = 9.5％ 月折旧率 = 9.5％ / 12 = 0.791666％ = 0.7917％ 月折旧额 =
	 *               10000 *　0.7917％ = 79.17元
	 * @param residualRate
	 *            残值率 %
	 * @param lifecycle
	 *            使用期限(月)
	 * @param originalValue
	 *            原值
	 * @param keepCount
	 *            保留小数点位数
	 * @return BigDecimal 返回类型
	 * @throws
	 * @author lixc
	 * @date 2017年11月30日
	 */
	public static BigDecimal getDepreciationTypeYear(Double residualRate,
			int lifecycle, BigDecimal originalValue, int keepCount) {

		BigDecimal tempV =null;
		if(null != residualRate){
			tempV = new BigDecimal(1 - residualRate / 100);
			tempV = originalValue.multiply(tempV);
		}
		else{
			tempV= originalValue;
		}
		
		tempV = tempV.divide(new BigDecimal(lifecycle), keepCount,
				BigDecimal.ROUND_HALF_UP);
		return tempV;
	}

	/**
	 * 
	 * @Description: TODO(一次提足折旧 ;一产欠摊销 )
	 * @param residualRate
	 *            残值率 %
	 * @param originalValue
	 *            原值
	 * @param keepCount
	 *            保留小数
	 * @return BigDecimal 返回类型
	 * @throws
	 * @author lixc
	 * @date 2017年11月30日
	 */
	public static BigDecimal getDepreciationTypeOne(Double residualRate,
			BigDecimal originalValue, int keepCount,Integer type) {
		if (keepCount <= 0 || keepCount >= 10)
			keepCount = 2;
		
		BigDecimal result = null;
		if(null != type && type == AssetType.FIXED_ASSET_TYPE)//固定资产
		{
			BigDecimal tempV = new BigDecimal(residualRate);
			tempV = tempV.divide(new BigDecimal(100), keepCount,
					BigDecimal.ROUND_HALF_UP);
			result = originalValue.subtract(tempV);
		}else if(null != type && (type == AssetType.INTANGIBLE_ASSET_TYPE 
				|| type == AssetType.APPORTIONED_ASSET_TYPE)){//无形 长期摊销
			result = originalValue;
		}
	
		return result;
	}

	/**
	 * 
	 * @Description: TODO(双倍余客递减)
	 * @param residualRate
	 *            残值率 %
	 * @param depreciationStartDate
	 *            起始折旧日期
	 * @param originalDepreciation
	 *            期初累计折旧额值
	 * @param lifecycle
	 *            使用期限(月)
	 * @param originalValue
	 *            原值
	 * @param currentDate  对比日期
	 *           
	 * @param keepCount
	 *            保留小数
	 * @param type 1为固定资产，2为无形资产
	 * @return BigDecimal 返回类型
	 * @throws
	 * @author lixc
	 * @date 2017年11月30日
	 */
	public static BigDecimal getDepreciationTypeDouble(Double residualRate,
			Date depreciationStartDate, BigDecimal originalDepreciation,
			int lifecycle, BigDecimal originalValue, Date currentDate,
			int keepCount,int type) {

		if (null == depreciationStartDate
				|| null == originalDepreciation 
				|| null == originalValue
				|| null == currentDate) {
			return null;
		}

		if (lifecycle <= 0) {
			return null;
		}

		if (keepCount <= 0) {
			keepCount = Global.KEEP_DIGITS;
		}

//		// 比较日期不能小于等于 初始化日期
//		if (currentDate.compareTo(depreciationStartDate) <= 0) {
//			return null;
//		}

		Date tempDate = null;
		if (lifecycle >= 24) {// 超过两年
			tempDate = DateUtils.addMonths(depreciationStartDate,
					lifecycle - 24);
			if (tempDate.compareTo(currentDate) > 0) {
				// x-2 算法 (原值-累计折旧)*2/(lifecycle/12)*100%/12
				return depreciationTypeDoubleBefore2(originalDepreciation,
						lifecycle, originalValue, keepCount);
			} else {
				// 2 算法 ：(原值-原值*残值率-累计折旧)/2/12
				return depreciationTypeDoubleAfter2(residualRate,
						originalDepreciation, originalValue, keepCount,type);
			}

		} else {// 两年内 2 算法
				
			return depreciationTypeDoubleAfter2(residualRate,
					originalDepreciation, originalValue, keepCount,type);
		}

	}

	
	
	/**
	 * 
	 * @Description: TODO(双倍余客递减)
	 * @param residualRate
	 *            残值率 %
	 * @param depreciationStartDate
	 *            起始折旧日期
	 * @param originalDepreciation
	 *            期初累计折旧额值
	 * @param lifecycle
	 *            使用期限(月)
	 * @param originalValue
	 *            原值
	 * @param currentDate  对比日期
	 *           
	 * @param keepCount
	 *            保留小数
	 * @param type 1为固定资产，2为无形资产
	 * @return BigDecimal 返回类型
	 * @throws
	 * @author lixc
	 * @date 2017年11月30日
	 */
	public static BigDecimal sumDepreciationTypeDouble(Double residualRate,
			Date depreciationStartDate, BigDecimal originalDepreciation,
			int lifecycle, BigDecimal originalValue, Date currentDate,
			int keepCount , int type) {

		if (null == residualRate || null == depreciationStartDate
				|| null == originalDepreciation || null == originalValue
				|| null == currentDate) {
			return null;
		}

		if (lifecycle <= 0) {
			return null;
		}

		if (keepCount <= 0) {
			keepCount = Global.KEEP_DIGITS;
		}

        BigDecimal result =  BigDecimal.ZERO;
      
		
	   int countM = DateUtils.getMonthsBetween(currentDate, depreciationStartDate);
	   
	   if((lifecycle-countM)>24 || lifecycle < 24 ){
			// x-2 算法 (原值-累计折旧)*2/(lifecycle/12)*100%/12
		   result= depreciationTypeDoubleBefore2(originalDepreciation,
					lifecycle, originalValue, keepCount);
		   if(lifecycle < 24){
			   result=result.multiply(new BigDecimal(lifecycle)).setScale(keepCount);
		   }else{
			   result=result.multiply(new BigDecimal(countM + 24 - lifecycle)).setScale(keepCount);
		   }
		   
	   }else{//两月内
		   
		   result= depreciationTypeDoubleBefore2(originalDepreciation,
					lifecycle, originalValue, keepCount);
		   result=result.multiply(new BigDecimal(lifecycle-24));
		   
		   BigDecimal temp=depreciationTypeDoubleAfter2(residualRate,
					originalDepreciation, originalValue, keepCount,type);
		   temp=temp.multiply(new BigDecimal(lifecycle-countM));
		   
		   result=result.add(temp);
	   }
		
		 return result;
	}
	
	// x-2 算法 (原值-累计折旧)*2/X*100%/12
	public static BigDecimal depreciationTypeDoubleBefore2(
			BigDecimal originalDepreciation, int lifecycle,
			BigDecimal originalValue, int keepCount) {

		BigDecimal temp = originalValue.subtract(originalDepreciation);
		temp = temp.multiply(new BigDecimal(2));
		temp = temp.divide(new BigDecimal(lifecycle), keepCount,
				BigDecimal.ROUND_HALF_UP);
		return temp;
	}

	/**
	 * 
	* @Description: TODO((原值-原值*残值率-累计折旧)/2/12) 
	* @param residualRate
	* @param originalDepreciation
	* @param originalValue
	* @param keepCount
	* @param assetType 1为固定资产，2为无形资产
	* @return BigDecimal 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月6日
	 */
	public static BigDecimal depreciationTypeDoubleAfter2(Double residualRate,
			BigDecimal originalDepreciation, BigDecimal originalValue,
			int keepCount,int assetType) {
		
		BigDecimal temp = null;
		if(AssetType.FIXED_ASSET_TYPE == assetType){//固定资产
			temp = originalValue.multiply(new BigDecimal(
					residualRate / 100));
			temp = originalValue.subtract(temp).subtract(originalDepreciation);
			temp = temp.divide(new BigDecimal(2 * 12), keepCount,
					BigDecimal.ROUND_HALF_UP);
		}else if(AssetType.INTANGIBLE_ASSET_TYPE == assetType ||AssetType.APPORTIONED_ASSET_TYPE== assetType ){//无形资产
			
			temp = originalValue.subtract(originalDepreciation);
			temp = temp.divide(new BigDecimal(2 * 12), keepCount,
					BigDecimal.ROUND_HALF_UP);
		}
	
		return temp;
	}
}
