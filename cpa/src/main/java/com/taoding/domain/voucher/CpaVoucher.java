package com.taoding.domain.voucher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

/**
 * 凭证
 * @author czy
 * 2017年11月28日 上午11:22:21
 */
@Data
@ToString
@ValidationBean
public class CpaVoucher extends DataEntity<CpaVoucher> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5678740446988325097L;

	// 账簿ID
	@NotEmpty(message = "账簿ID不能为空")
	private String bookId;
	
	// 客户ID
	private String customerId;
	
	// 凭证编号
	private String voucherNo;
	
	// 凭证所属账期
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date voucherPeriod;
	
	// 凭证日期
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date voucherDate;
	
	// 凭证类型  1手动凭证 2资产类凭证 3智能凭证
	private Integer voucherType = 1 ;
	
	// 借方金额
	private BigDecimal amountDebit;
	
	// 贷方金额
	private BigDecimal amountCredit;
	
	// 金额大写
	private String accountCapital;
	
	// 单据张数
	private Integer ticketCount = 0;
	
	//凭证所有科目
	private List<CpaVoucherSubject> subjectLists = new ArrayList<CpaVoucherSubject>();
	
	/**
	 * 扩充字段
	 */
	//用户姓名，仅供查询使用
	private String userName;

	//重新整理编号使用
	private Integer sort ;
	
	//批量处理提交Id数组
	private String [] batchIds ;

	//模板/草稿ID
	private String templeteId ;
	
}
