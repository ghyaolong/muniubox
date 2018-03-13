package com.taoding.domain.customerInfo;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.common.excel.ExcelField;
import com.taoding.common.utils.DictUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.accountingbook.AccountingBook;

/**
 * 客户管理详请entity
 * 
 * @author mhb
 * @version 2017-11-16
 */
@Data
@ToString
@ValidationBean
public class CustomerInfo extends DataEntity<CustomerInfo> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 默认客户编号
	 */
	public static final String DEFAULT_CUSTOMER_NO="000000001";
	
	
	@NotEmpty
	@Length(min = 1, max = 200, message = "name长度必须介于 1 和 200 之间")
	@ExcelField(title = "客户名称", sort = 1, align = 2)
	private String name; // 名称
	@NotEmpty
	@Length(min = 1, max = 40, message = "编号长度必须介于 1 和 40 之间")
	private String no; // no
	@ExcelField(title = "助记码", sort = 2, align = 2)
	@Length(min = 0, max = 10, message = "助记码长度必须介于 0 和 10 之间")
	private String code; // 助记码
	private String type; //  1 小规模纳税人 2 常规纳税人
	
	@NotEmpty
	@Length(min = 1, max = 30, message = "法人长度必须介于 1 和 30 之间")
	private String corporation; // 法人
	@Length(min = 0, max = 200, message = "备址长度必须介于 0 和 200 之间")
	private String addressBackup; // address_backup
	@NotEmpty
	@Length(min = 1, max = 200, message = "注册地址长度必须介于 1 和 200 之间")
	private String addressRegisted; // address_registed
	@ExcelField(title = "省份", sort = 3, align = 2)
	@Length(min = 0, max = 100, message = "省份长度必须介于 0 和 100 之间")
	private String province; // 省份

	 
	private Integer accountStatus; // 记账状况 可能的状态：0 未建账，1 已建账  2 未开始， 3进行中，4预警期，5已完成

	private String accountStatusName;//记账状态名称
	
	@ExcelField(title = "报税状况", sort = 6, align = 2)
	@Length(min = 0, max = 20, message = "可能的状态：            未建账， X月未申报，预警期，X月已完成长度必须介于 0 和 20 之间")
	private String taxStatus; // 报税状况

	@ExcelField(title = "收款状况", sort = 7, align = 2)
	@Length(min = 0, max = 20, message = "收款状况长度必须介于 0 和 20 之间")
	private String incomeStatus; // 收款状况

	@Length(min = 0, max = 4, message = "0： 正常            1：禁用            默认 0长度必须介于 0 和 4 之间")
	private String status; // 状态 字典： cpa_custorm_status

	@ExcelField(title = "会计", sort = 8, align = 2)
	@Length(min = 0, max = 64, message = "会计长度必须介于 0 和 64 之间")
	private String accounting; // 会计
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JsonFormat(pattern="yyyy-MM")
	private Date currentPeriod;//当前账期 默认为 每月1号
	
	/*纳税人性质*/
	@ExcelField(title = "税人性质", sort = 4, align = 2)
	private  String taxpayerPropertyName;
	/*会计制度*/
	private String  accountSystemName;

	private String area;//地区
	
	private String city; //城市
	
	private String  accountBookId;// 账薄Id 
	
	public CustomerInfo() {
		super();
	}

	public CustomerInfo(String id) {
		super(id);
	}

	@ExcelField(title = "状态", sort = 9, align = 2)
	public String getTypeLabel() {
		if(StringUtils.isNotBlank(status)){
			if("1".equals(status)){
				return DictUtils.getDictLabel(String.valueOf(Global.TRUE_1), "cpa_custorm_status", "");
			}else{
				return DictUtils.getDictLabel(String.valueOf(Global.FALSE_0), "cpa_custorm_status", "");
			}
		}
		return "";
	}
	
	
	public Integer getAccountStatus(){
		if(null == accountStatus) return 0;
		return  accountStatus;
	}
	/**
	 * 
	* @Description: TODO(返回建账状态名称) 
	* @return String 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	@ExcelField(title = "记账状况", sort = 5, align = 2)
	public String getAccountStatusName(){
		return AccountingBook.findAccountStatusName(this.accountStatus, this.currentPeriod);
	}
}