package com.taoding.common.utils;

public class QueryConditionSql {

	public static  String  getQueryConditionSql(String alias, String[] fields,String value){
		if(StringUtils.isEmpty(value)){
			return null;
		}
		if(null==fields || fields.length==0){
			return null;
		}
		StringBuilder StrBuildersql = new StringBuilder();
		StrBuildersql.append(" and ");
		StrBuildersql.append(" ( ");
		
		for (int j = 0; j < fields.length; j++) {
			if(j==fields.length-1){
				StrBuildersql.append(alias).append(".").append(fields[j]).append(" like \'%").append(value).append("%\'");
			}else{
				StrBuildersql.append(alias).append(".").append(fields[j]).append(" like \'%").append(value).append("%\'").append(" or ");	
			}
		}
		StrBuildersql.append("  ) ");
		return StrBuildersql.toString();
	}
	
    /**
     * 别名在字段里面标示
     * @param fields
     * @param value
     * @return
     */
	public static  String  getQueryConditionSql(String[] fields,String value){
		if(StringUtils.isEmpty(value)){
			return null;
		}
		if(null==fields || fields.length==0){
			return null;
		}
		StringBuilder StrBuildersql = new StringBuilder();
		StrBuildersql.append(" and ");
		StrBuildersql.append(" ( ");
		
		for (int j = 0; j < fields.length; j++) {
			if(j==fields.length-1){
				StrBuildersql.append(fields[j]).append(" like \'%").append(value).append("%\'");
			}else{
				StrBuildersql.append(fields[j]).append(" like \'%").append(value).append("%\'").append(" or ");	
			}
		}
		StrBuildersql.append("  ) ");
		return StrBuildersql.toString();
	}
//	private static String[] fields={"no","name","login_name","mobile","mobile","email"};
//	public static void main(String[] args) {
//		System.out.println(getQueryConditionSql("ggg", fields, "----"));
//	}
}
