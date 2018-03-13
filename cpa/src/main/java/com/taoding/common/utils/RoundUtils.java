package com.taoding.common.utils;

import java.math.BigDecimal;

/**
 * 运算的取整规则
 * @author csl
 * @data 2017-11-29 16:51:23
 */
public class RoundUtils {
	
	/**
	 * 见角进元
	 * @param b
	 * @return
	 */
	public static BigDecimal CornerToYuan(BigDecimal b){
		return b.setScale(0, BigDecimal.ROUND_UP);
	}
	
	/**
	 * 见分进角
	 * @param b
	 * @return
	 */
	public static BigDecimal CentToCorner(BigDecimal b){
		return b.setScale(1,BigDecimal.ROUND_UP);
	}
	
	/**
	 * 四舍五入到分
	 * @param b
	 * @return
	 */
	public static BigDecimal RoundAndRound(BigDecimal b){
		return b.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 四舍五入到角
	 * @param b
	 * @return
	 */
	public static BigDecimal RoundToCorner(BigDecimal b){
		return b.setScale(1,BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 四舍五入到元
	 * @param b
	 * @return
	 */
	public static BigDecimal RoundToYuan(BigDecimal b){
		return b.setScale(0,BigDecimal.ROUND_HALF_UP);
	}
}
