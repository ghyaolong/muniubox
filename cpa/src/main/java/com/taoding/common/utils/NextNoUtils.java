package com.taoding.common.utils;

public class NextNoUtils {

	/**
	 * 获取下一个三位编号
	 * 2017年11月20日 下午3:27:06
	 * @param maxNo
	 * @return
	 */
	public static String getNextNo(String maxNo){
		
		Integer nextInt = Integer.valueOf(maxNo) + 1;
		String nextStr = String.valueOf(nextInt);
		String returnNo = "";
		if(nextStr.length() == 1){
			returnNo = "00" + nextStr ;
		}else if(nextStr.length() == 2){
			returnNo = "0" + nextStr ;
		}else {
			returnNo = nextStr ;
		}
		return returnNo ;
	}
	
	/**
	 * 获取上一个编号
	 * 2017年12月4日 下午2:12:26
	 * @param maxNo
	 * @return
	 */
	public static String getPreviousNo(String maxNo){
		
		Integer nextInt = Integer.valueOf(maxNo) - 1;
		String nextStr = String.valueOf(nextInt);
		String returnNo = "";
		if(nextStr.length() == 1){
			returnNo = "00" + nextStr ;
		}else if(nextStr.length() == 2){
			returnNo = "0" + nextStr ;
		}else {
			returnNo = nextStr ;
		}
		return returnNo ;
	}
	
	
}
