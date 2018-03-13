package com.taoding.invoiceTemp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.taoding.domain.ticket.vo.Summary;

import lombok.Data;

/**
 * Created by yaohcenglong on 2017/11/23.
 * 增值税普通发票模板
 */
@Data
public class VATCommonInvoice implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -913031658820932885L;
	
	/**
	 * 普通发票
	 */
	public static Integer INVOICE_TYPE_NORMAL = 1;
	
	/**
	 * 专有发票
	 */
	public static Integer INVOICE_TYPE_SPECIAL = 2;

	//每张票据的抬头，如：陕西省增值税专用发票    陕西省增值税普通发票  回执单......
    private String title;

    //发票所在省
    private String province;

    //发票号码
    private String invoice_no;

    //发票代码
    private String invoice_code;

    //检验码
    private String checkCode;

    //开票日期
    private String invoice_date;

    //销售方
    private String seller;
    //销售方纳税人识别号
    private String sellerTIN;

    //销售方地址或电话
    private String sellerAddOrTel;

    //销售方开户行
    private String sellerBankDeposit;

    //销售方开户行银行账号
    private String sellerBankAccount;

    private String invoiceDate;
    //购买方
    private String purchaser;

    //购买方纳税人识别号
    private String purchaserTIN;

    //购买方地址或电话
    private String purchaserAddOrTel;

    private String purchaserBankDeposit;

    private String purchaserBankAccount;

    //开票人
    private String drawer;
    //领款人
    private String payee;

    //复核
    private String reCheck;

    //金额合计
    private BigDecimal amountSum;
    //税额合计
    private BigDecimal taxSum;

    //税价合计
    private BigDecimal amountAndTaxSum;
    
    //发票类型  1：增值税普通发票    2.增值税专用发票
    private int type;
    
    //摘要关键字
    private String summaryKey;
    
    //发票摘要
    private List<List<Summary>> summaryList = new ArrayList<>();
    
}
