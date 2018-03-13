package com.taoding.common.utils;

/**
 * 公用查询条件关键子列表参数
 * @author lixc
 *2017-10-13 14:51:28
 */
public class QueryConditionFieldsArray {
	
	//用户管理查询字段
	public static String[] userQueryFields={"no","name","login_name","mobile","email"};
	
	//公司管理查询字段&csl
	public static String[] atEnterpriserFields={"a.company_code","a.company_name","a.company_account","b.legal_person_name"};
	
	//员工管理查询字段
    public static String[] officeEmployeeQueryFields={"a.no","a.name","a.email","a.login_name","a.phone"};
	//组织机构员工查询字段
    public static String[] employeeQueryFields={"a.no","a.name","a.email","a.login_name","a.phone"};
    
    //角色组
    public static String[] groupQueryFields={"role_group_no","role_group_name"};
    //角色
    public static String[] roleQueryFields={"enname","name"};
    //员工查询 add lixc  2017年10月28日14:55:02
    public static String[] employeeAtQueryFields={"job_number","emp_name","telephone","mailbox","mobile","empDept"};
    
     //客户管理查询
  	public static String[] customerQueryFields={"a.name","cab.code"};
}
