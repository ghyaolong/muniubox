package com.taoding.service.customerTaxItem;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.common.utils.DateUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.customerTaxItem.CustomerTaxFormula;
import com.taoding.domain.customerTaxItem.CustomerTaxItem;
import com.taoding.domain.vouchersummary.CpaVoucherSummary;
import com.taoding.mapper.customerTaxItem.CustomerTaxFormulaDao;
import com.taoding.mapper.customerTaxItem.CustomerTaxItemDao;
import com.taoding.mapper.customerTaxItem.TaxValueAddedDao;
import com.taoding.service.accountingBook.AccountingBookService;
import com.taoding.service.vouchersummary.CpaVoucherSummaryService;

/**
 * 客户税计算逻辑细则ServiceImpl
 * 
 * @author mhb
 * @Date 2017-12-19
 */
@Slf4j
@Service
@Transactional
public class CustomerTaxAlgorithmRuleDetailedServiceImpl {

	@Autowired
	private AccountingBookService accountingBookService;

	@Autowired
	private CustomerTaxItemDao customerTaxItemDao;

	@Autowired
	private TaxValueAddedDao taxValueAddedDao;

	@Autowired
	private CustomerTaxFormulaDao customerTaxFormulaDao;
	
	@Autowired 
	private CpaVoucherSummaryService cpaVoucherSummaryService;
	/* 
	 * 免税金额条件限制判断 (月计提)
	 * */
	public static final BigDecimal REGULAR_MONEY = new BigDecimal("100000.000"); 
	 /*
	  * 附加税免税项
	  * */ 
	public static final String[] EXCEPT_TAX_TERM_ARRAY = {"教育费附加", "地方教育附加","水利建设基金"};
	
	 /*
	  * 收入税率
	  * */ 
	public static final double INCOME_TAX_RATE = 3;
	 /*
	  * 印花税
	  * */ 
	public static final String STAMP_DUTY="印花税";
	/*
	  * 用于印花税税额小于1元不交印花税
	  * */ 
	public static final BigDecimal STAMP_DUTY_TAX=new BigDecimal("1.00");

	/**
	 * 计提税金及附加(小规模纳税人)
	 * @param accountingId  账薄id
	 * @param subjectTaxMap  税额(获取数据)
	 * @param incomeMoney 收入  
	 * @param exemption   免税金额
	 * @param accruedType 计提方式
	 * @return
	 */
	protected Map<String, BigDecimal> smallScaleTaxpayer(String accountingId,Map<String, BigDecimal> subjectTaxMap, BigDecimal incomeMoney,BigDecimal exemption, Byte accruedType) {
		log.info("generalTaxpayer===>accountingId"+accountingId+"subjectTaxMap:"+JSON.toJSONString(subjectTaxMap)+"incomeMoney:"+incomeMoney
				+"exemption"+exemption+"accruedType"+accruedType);
		int month = 0;
		if (accruedType == CustomerTaxItem.MONTH_ACCRUED_TYPE) {
			month = 1;
		} else if (accruedType == CustomerTaxItem.SEASON_ACCRUED_TYPE) {
			month = 3;
		}
		if (incomeMoney.compareTo(exemption) <= 0) {
			return subjectTaxMap;
		} else if (incomeMoney.compareTo(exemption) > 0&& incomeMoney.compareTo(REGULAR_MONEY.multiply(new BigDecimal(month))) <= 0) {
			taxFee(accountingId, EXCEPT_TAX_TERM_ARRAY, subjectTaxMap);
			return subjectTaxMap;
		} else {
			taxFee(accountingId, null, subjectTaxMap);
			return subjectTaxMap;
		}
	}

	/**
	 * 税额计算
	 * @param accountingId 账薄id
	 * @param  exceptTaxTermArray 免税项
	 * @return Map<String, Object> 
	 * @author mhb
	 * @Date 2017年12月20日
	 */
	protected void taxFee(String accountingId, String[] exceptTaxTermArray,Map<String, BigDecimal> subjectTaxMap) {
		log.info("taxFee===>accountingId"+accountingId+"exceptTaxTermArray:"+JSON.toJSONString(exceptTaxTermArray)
				+"subjectTaxMap"+JSON.toJSONString(subjectTaxMap));
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("accountingBookId", accountingId);
		List<CustomerTaxItem> customerTaxItemList = customerTaxItemDao.findList(queryMap);
		log.info("税项:"+JSON.toJSONString(customerTaxItemList));
		for (CustomerTaxItem customerTaxItem : customerTaxItemList) {
			if(exceptTaxTermArray!=null){
				for (String exceptTaxTerm : exceptTaxTermArray) {
					if (customerTaxItem.getName().equals(exceptTaxTerm)) {
						continue;
					}
				}
			}
			BigDecimal subjectTaxMoney=sumFormulaSubjectTax(accountingId,customerTaxItem.getId(),customerTaxItem.getAccruedType(),customerTaxItem.getRate());
			subjectTaxMap.put(customerTaxItem.getSubjectId(), subjectTaxMoney); 
			log.info("税项计算出来的税额为:"+subjectTaxMoney);
			subjectTaxMap.put(customerTaxItem.getSubjectId(), subjectTaxMoney);
		}
	}

	/**
	 * 计算税额(税额*税率)
	 * @param sumFormulaSubjectTax 税额
	 * @return rate 税率(%)
	 * @author mhb
	 * @Date 2017年12月20日
	 */
	protected BigDecimal getSubjectTaxFormula(BigDecimal sumFormulaSubjectTax,Double rate) {
		if (sumFormulaSubjectTax != null && rate!=null) {
			return (sumFormulaSubjectTax.multiply(new BigDecimal(0.01*rate))).setScale(Global.KEEP_DIGITS_THREE, BigDecimal.ROUND_HALF_UP);
		}
		return BigDecimal.ZERO;
	}

	/**
	 * 获取收入
	 * @param declarantPeriod 计提方式
	 * @return BigDecimal
	 * @author mhb
	 * @Date 2017年12月20日
	 */
	protected BigDecimal getIncome(byte declarantPeriod, String bookId,List<String> ids) {
		log.info("getIncome===>declarantPeriod:"+declarantPeriod+";bookId:"+bookId+";ids"+JSON.toJSONString(ids));
		if (declarantPeriod == CustomerTaxItem.MONTH_ACCRUED_TYPE) { 
			return cpaVoucherSummaryService.statisticsAmountBySummaryIds(ids,bookId,CpaVoucherSummary.MONTH_BALANCE);
		} else if (declarantPeriod == CustomerTaxItem.SEASON_ACCRUED_TYPE) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				String dateStr=CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
				log.info("当前账期:"+dateStr);
			Date date = format.parse(dateStr);
				Integer currentMonth = Integer.valueOf(DateUtils.formatDate(date, "MM"));
				if (currentMonth % 3 != 0) {
					throw new LogicException("季度计提需当前账期为季度月.");
				}
				return	cpaVoucherSummaryService.statisticsAmountBySummaryIds(ids,bookId,CpaVoucherSummary.QUARTER_BALANCE); 
			} catch (Exception e) {
				throw new LogicException("计提方式(季度计提)获取收入失败.");
			}
		}
		throw new LogicException("收入统计失败");
	}
	
	/**
	 * 减免税款收入(附件税免税政策)
	 * @param accountingId 账薄id
	 * @param  exceptTaxTermArray 免税项
	 * @return Map<String, Object> 
	 * @author mhb
	 * @Date 2017年12月20日
	 */
	protected Map<String, BigDecimal> taxAdded(String accountingId, String[] exceptTaxTermArray,Map<String, BigDecimal> taxAddedDetailedMap) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("accountingBookId", accountingId);
		List<CustomerTaxItem> customerTaxItemList = customerTaxItemDao.findList(queryMap);
		log.info("税项:"+JSON.toJSONString(customerTaxItemList));
		for (CustomerTaxItem customerTaxItem : customerTaxItemList) {
				for (String exceptTaxTerm : exceptTaxTermArray) {
					if (customerTaxItem.getName().equals(exceptTaxTerm)) {
						BigDecimal  subjectTax= new BigDecimal("0.000");
						subjectTax= sumFormulaSubjectTax(accountingId,customerTaxItem.getId(),customerTaxItem.getAccruedType(),customerTaxItem.getRate());
						taxAddedDetailedMap.put(customerTaxItem.getSubjectId(), subjectTax);
					}
				}
		}
		return taxAddedDetailedMap;
	}
	
	/**   
	* @Title: 根据税项设置公式汇总税额   
	* @param accountingId
	* @param customerTaxItemId
	* @param accruedType
	* @param rate
	* @return BigDecimal   
	* @throws   
	*/
	private BigDecimal sumFormulaSubjectTax(String accountingId,String customerTaxItemId,byte accruedType,double rate){
		BigDecimal sumFormulaSubjectTax = new BigDecimal("0");
		Map<String, Object> queryFormulaMap = new HashMap<String, Object>();
		queryFormulaMap.put("customerTaxId", customerTaxItemId);
		//查询公式列表数据
		List<CustomerTaxFormula> customerTaxFormulaList = customerTaxFormulaDao.findList(queryFormulaMap);
		log.info("税项公式:"+JSON.toJSONString(customerTaxFormulaList));
		int num=0;
		boolean flag=true;
		for (CustomerTaxFormula customerTaxFormula : customerTaxFormulaList) { 
			List<String> list= new ArrayList<String>();
			  list.add(customerTaxFormula.getSubjectId());
			BigDecimal  formulaSubjectTax=cpaVoucherSummaryService.statisticsAmountBySummaryIds(list, accountingId,Integer.valueOf(accruedType)); 
			log.info("税项公式中科目获取到的税额为:"+formulaSubjectTax);
			if(num==0){
				sumFormulaSubjectTax=formulaSubjectTax;
			}
			if(num>0){
				if (flag) {
					sumFormulaSubjectTax=sumFormulaSubjectTax.add(formulaSubjectTax);
				} else {
					sumFormulaSubjectTax=sumFormulaSubjectTax.subtract(formulaSubjectTax);
				}
			}
			flag=customerTaxFormula.isOperator();
			num++;
		}
		BigDecimal subjectTaxMoney = getSubjectTaxFormula(sumFormulaSubjectTax,rate);
		log.info("税项计算出来的税额为:"+subjectTaxMoney);
		return subjectTaxMoney;
	}
}
