package com.taoding.service.fixedAsset;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.taoding.common.utils.DateUtils;
import com.taoding.domain.fixedAsset.FixedAsset;
/**
 * 
* @ClassName: AssetUtils 
* @Description: 处理资产业务 非数据库操作 
* @author lixc 
* @date 2017年12月22日 上午10:31:11 
*
 */
public class AssetUtils {

	/**
	 * 
	* @Description:保存时处理 净值 本月结账  入账日期
	* @param asset void 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月22日
	 */
	public static <T extends FixedAsset> void changeSave(T asset){
		
		if(null == asset) return ;
		//处理计算更新 净值 本月折旧 
		if(null != asset.getAccountedForDate() && null != asset.getDepreciationStartDate())
		{
			String forDateStr = DateUtils.formatDate(asset.getAccountedForDate(), "yyyy-MM");
			String startDateStr = DateUtils.formatDate(asset.getDepreciationStartDate(), "yyyy-MM");
			if(StringUtils.isNotBlank(startDateStr) && startDateStr.compareTo(forDateStr) >= 0){
				asset.setThisMonthDepreciation(asset.getDepreciationPerMonth());//当月折旧/摊销
				asset.setNetWorth(asset.getOriginalValue().subtract(asset.getOriginalDepreciation()).
						                                   subtract(asset.getThisMonthDepreciation()));// 净值  原值-累计折旧-本月折旧
			}
		}
		asset.setEnterDate(new Date());//入账日期
	}
	
	
	public static void changeAssetListBeforSave(List<? extends FixedAsset> list){
		
	}
}
