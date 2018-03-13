package com.taoding.service.settleaccount;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.taoding.domain.settleaccount.CpaFinalLiquidationBasic;

public class CpaFinalLIquidationTool {

	/**
	 * 
	* @Description: map 求和
	* @param map
	* @return BigDecimal 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月25日
	 */
	public static BigDecimal sumMapValue(Map<String, BigDecimal> map){
		if(null != map){
			BigDecimal sum = BigDecimal.ZERO;
			Iterator<BigDecimal> it = map.values().iterator();
			while (it.hasNext()) { 
				sum = sum.add(it.next());
			}
			return sum;	
		} 
		return BigDecimal.ZERO;
	}
	/**
	 * 
	* @Description:  list 按type升序排序
	* @param list void 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月27日
	 */
	public static void sortCpaFinalLiquidationBasicListByTypeUp(List<CpaFinalLiquidationBasic> list){
		
		if(CollectionUtils.isEmpty(list)) return ;
		// 排序
		list.sort(new Comparator<CpaFinalLiquidationBasic>() {

			@Override
			public int compare(CpaFinalLiquidationBasic a,CpaFinalLiquidationBasic b) {
				if(b.getType() == null){
					return 1;
				}
				if(null == a.getType()){
					return -1;
				}
				
				return a.getType()-b.getType() ;
			}
		});
		
	}
}
