package com.taoding.domain.fixedAsset;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.common.excel.ExcelField;
import com.taoding.domain.assisting.CpaAssistingBalanceDepartment;
import com.taoding.domain.assisting.CpaAssistingEmployee;
import com.taoding.domain.subject.CpaCustomerSubject;


/**
 * 固定资产Entity
 * @author lixc
 * @version 2017-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ValidationBean
public class FixedAsset extends DataEntity<FixedAsset> { 
	private static final long serialVersionUID = 6060387327758228549L;
	//折旧方法：            1. 平均年限法；            2. 双倍余客递减；            3. 不计提折旧额；            
	//4. 一次提足折旧；            5. 不摊销；       6. 一产欠摊销；
	public static final int DEPRECIATION_TYPE_AVERAGING_YEAR = 1;
	public static final int DEPRECIATION_TYPE_DOUBLE_BALANCE_DECLINE = 2;
	public static final int DEPRECIATION_TYPE_NO_DEPRECIATION = 3;
	public static final int DEPRECIATION_TYPE_ONE_DEPRECIATION = 4;
	public static final int DEPRECIATION_TYPE_NO_AMORTIZATION = 5;
	public static final int DEPRECIATION_TYPE_ONE_AMORTIZATION = 6;
	//默认编号
	public static final String DEFAULT_CODE = "001";
	//导出标示exportMark
	public static final String EXPORT_MARK = "1";
	
	
	
	//状态 0 正常 1 已清理/处理  2已结账
	public static final Integer STATUS_NORMAL = 0;
	public static final Integer STATUS_ALREADY_CLEAN_OR_HANDE = 1;
	public static final Integer STATUS_CLOSE_ACCOUNTS = 2;
	public static final String [] EXPORT_HEARDER = {"资产名称", "入账日期", "原值(元)", "期限(月)", "期初累计摊销(元)", "净值(元)", "月摊销额(元)", "本月摊销(元)", "录入时间"};
	
	@ExcelField(title="折旧日期", align=2, sort=10 ,groups = 17)
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date depreciationDate;// 折旧/摊销日期
	@ExcelField(title="资产名称", align=2, sort=20 ,groups = 2)
	@NotEmpty(message="资产名称不能为空！")
	@Length(min=0, max=255, message="资产名称长度必须介于 1 和 255 之间")
	private String name;		// 资产名称
	
	@ExcelField(title="资产类型", align=2, sort=30 ,groups = 3,value="assetType.name")
	private AssetType assetType;
	
	@Length(min=0, max=64, message="资产类型， 指向客户资产类型表的对应记录长度必须介于 1 和 64 之间")
	private String assetTypeId;		// 资产类型id， 指向客户资产类型表的对应记录的id
	
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date currentPeriod;		// 当前账期
	
//	@NotNull(message="员工不能为空")
	@ExcelField(title="员工", align=2, sort=40 ,groups = 4,value="assistingEmployee.name")
	private CpaAssistingEmployee assistingEmployee;		// 员工
	
//	@NotNull(message="部门不能为空")
	@ExcelField(title="部门", align=2, sort=50 ,groups = 5,value="assistingBalanceDepartment.departmentName")
	private CpaAssistingBalanceDepartment assistingBalanceDepartment;//部门
	
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	@NotNull(message="入账日期不能为空")
	@ExcelField(title="入账日期", align=2, sort=60 ,groups = 6)   
	private Date accountedForDate;		// 入账日期
	
	@NotNull(message="原值不能为空")
	@ExcelField(title="原值(元)", align=2, sort=70 ,groups = 7)     
	private BigDecimal originalValue;	// 原值
	
	@NotNull(message="使用期限不能为空")
	@ExcelField(title="期限(月)", align=2, sort=80 ,groups = 8)
	private Integer lifecycle;		// 使用期限(月)
	@ExcelField(title="残值率(%)", align=2, sort=90 ,groups = 9)  
	private Double residualRate;		// 残值率
	@ExcelField(title="期初累计(元)", align=2, sort=95 ,groups = 10)    //本期折旧
	private BigDecimal originalDepreciation; // 期初累计折旧额值
	@ExcelField(title="净值(元)", align=2, sort=100 ,groups = 11)    
	private BigDecimal netWorth;//净值  运算方式待定
	
	
	
	@ExcelField(title="月折扣额(元)", align=2, sort=110 ,groups = 12)
	private BigDecimal depreciationPerMonth;//月折旧额		
	
	
	private Integer depreciationType;		// 折旧方法：            1. 平均年限法；            2. 双倍余客递减；            3. 不计提折旧额；            4. 一次提足折旧；            5. 不摊销；   6. 一产欠摊销；
	

	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date depreciationStartDate;		// 起始折旧日期
	
	@NotEmpty(message="账簿不能空！")
	private String accountId;//账薄ID
	
	
	private CpaCustomerSubject depreciationFeeSubject;		// 折旧费用科目
	
	private CpaCustomerSubject depreciationCumulationSubject; // 累计折旧科目
	@ExcelField(title="本月折扣", align=2, sort=120 ,groups = 13)
	private BigDecimal thisMonthDepreciation;//本月折旧
	private Integer status;//状态 status  0 正常  1 已清理  2 已结账
	@ExcelField(title="录入时间", align=2, sort=130 ,groups = 14)
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date enterDate;//录入日期
	
	private String voucherId;//凭证Id
	private String code;//编号
	@ExcelField(title="清理日期", align=2, sort=140 ,groups = 15)
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date clearDate;//清理日期
	@ExcelField(title="资产类型", align=2, sort=30 ,groups = 16)
	private String assetTypeName; //资产类型
	
	

	//虚拟字段 用于前台展示  应用的可以自行扩展 
	private String ShowLable;
	
/*	private String str;
	
	public String setTest(String str){
		this.str = str;
		return str;
	}
	public String getTest(){
		return str;
	}*/
}