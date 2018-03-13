package com.taoding.service.customerTaxItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.customerTaxItem.CustomerTaxItem;
import com.taoding.domain.customerTaxItem.TaxValueAdded;
import com.taoding.mapper.accountingBook.AccountingBookDao;
import com.taoding.mapper.customerTaxItem.TaxValueAddedDao;
import com.taoding.service.vouchersummary.CpaVoucherSummaryService;

/**
 * 客户税计算逻辑ServiceImpl
 * 
 * @author mhb
 * @Date 2017-12-19
 */
@Slf4j
@Service
@Transactional
public class CustomerTaxAlgorithmRuleServiceImpl extends CustomerTaxAlgorithmRuleDetailedServiceImpl implements CustomerTaxAlgorithmRuleService {
	
	@Autowired
	private AccountingBookDao accountingBookDao;
	@Autowired
	private TaxValueAddedDao taxValueAddedDao;
	
	@Autowired 
	private CpaVoucherSummaryService cpaVoucherSummaryService;
	/*计提税金及附加计算规则:
	 * 一、小规模纳税入
	 * 1、附加税免税政策勾选
	 *   	1）  收入<免税金额   全免
	 *      2) 免税金额 < 收入 < 30W(10W) 附加税免税政策(教育费附加、地方教育附加、水利建设基金)全免，其他税项交
	 *      3) 收入 > 30W (10W) 全不免
	 * 2、附加税免税金额未勾选
     *   全交
	 *二、一般纳税人
	 * 1、附加税免税金额未勾选(税全不免)
	 * 2、 附加税免税政策勾选 
	 * 	 1)、进项税额 < 销项税额   销项税额 <= 10W(30W)      不交（附加税免税政策） 其他费用交
  	 *	 2)、进项税额 < 销项税额   销项税额 > 10W(30W)       全交
     *   3)、进项税额 > 销项税额   销项税额 <= 10W(30W)      不交 附加税免税政策     其他费用交
     *   4)、进项税额 > 销项税额   销项税额 > 10W(30W)       全交
	 * */

	@Override
	public BigDecimal suMcustomerAccruedTaxAdditiveSmallScale(String accountingId, List<String> ids) {
		log.info("suMcustomerAccruedTaxAdditiveSmallScale===start");
	Map<String, BigDecimal>	 subjectTaxMap=customerAccruedTaxAdditiveSmallScale(accountingId,ids);
	BigDecimal sumSubjectTaxMoney = new BigDecimal("0.000");
		if (subjectTaxMap.size() > 0) {
			for (BigDecimal subjectTaxMoney : subjectTaxMap.values()) {
				sumSubjectTaxMoney = sumSubjectTaxMoney.add(subjectTaxMoney);
			}
		}
		log.info("sumSubjectTaxMoney:"+sumSubjectTaxMoney);
		log.info("suMcustomerAccruedTaxAdditiveSmallScale===end");
		return sumSubjectTaxMoney;
	}

	@Override
	public BigDecimal suMcustomerAccruedTaxAdditiveGeneral(String accountingId,String incomeTaxId, String outputTaxId) {
		log.info("suMcustomerAccruedTaxAdditiveGeneral===start");
		Map<String, BigDecimal>	 subjectTaxMap=customerAccruedTaxAdditiveGeneral(accountingId,incomeTaxId,outputTaxId);
		BigDecimal sumSubjectTaxMoney = new BigDecimal("0.000");
			if (subjectTaxMap.size() > 0) {
				for (BigDecimal subjectTaxMoney : subjectTaxMap.values()) {
					sumSubjectTaxMoney = sumSubjectTaxMoney.add(subjectTaxMoney);
				}
			}
			log.info("sumSubjectTaxMoney:"+sumSubjectTaxMoney);
			log.info("suMcustomerAccruedTaxAdditiveGeneral===end");
			return sumSubjectTaxMoney;
	}
	
	@Override
	public Map<String, BigDecimal> customerAccruedTaxAdditiveSmallScale(String accountingId,List<String> ids) {
		log.info("customerAccruedTaxAdditiveSmallScale===start");
		log.info("accountingId:"+accountingId+";ids"+JSON.toJSONString(ids));
		Map<String, BigDecimal> subjectTaxMap = new HashMap<String, BigDecimal>();
		TaxValueAdded taxValueAdded = taxValueAddedDao.getAccountingId(accountingId);
		log.info("TaxValueAdded:"+JSON.toJSONString(taxValueAdded));
		if (taxValueAdded != null) {
			BigDecimal incomeMoney = getIncome(taxValueAdded.getDeclarantPeriod(), accountingId,ids); 
			log.info("获取收入为:"+incomeMoney);
			BigDecimal exemption = taxValueAdded.getExemption();  //免税金额
			if (taxValueAdded.isSurtaxExemptable()) {
				return smallScaleTaxpayer(accountingId, subjectTaxMap, incomeMoney, exemption, taxValueAdded.getDeclarantPeriod());
			} else {
				taxFee(accountingId, null, subjectTaxMap);
			}
		}
		log.info("税额:"+ JSON.toJSONString(subjectTaxMap));
		log.info("customerAccruedTaxAdditiveSmallScale====end");
		return subjectTaxMap;
	}
	@Override
	public Map<String, BigDecimal> customerAccruedTaxAdditiveGeneral(String accountingId,String incomeTaxId,String outputTaxId) {
		log.info("customerAccruedTaxAdditiveGeneral===start"); 
		log.info("accountingId:"+accountingId+";incomeTaxId"+incomeTaxId+";outputTaxId"+outputTaxId);
		Map<String, BigDecimal> subjectTaxMap = new HashMap<String, BigDecimal>();
		TaxValueAdded taxValueAdded = taxValueAddedDao.getAccountingId(accountingId); 
		log.info("TaxValueAdded:"+JSON.toJSONString(taxValueAdded));
		List<String> subjectIds= new ArrayList<String>();
		subjectIds.add(outputTaxId);
		BigDecimal outputTax=cpaVoucherSummaryService.statisticsAmountBySummaryIds(subjectIds, accountingId, Integer.valueOf(taxValueAdded.getDeclarantPeriod()));
		log.info("科目税额:===>>"+outputTax);
		if (taxValueAdded != null) {
			if(taxValueAdded.isSurtaxExemptable()){
				if(taxValueAdded.getDeclarantPeriod()==CustomerTaxItem.MONTH_ACCRUED_TYPE){
					if(outputTax.compareTo(REGULAR_MONEY)<=0){
						taxFee(accountingId, EXCEPT_TAX_TERM_ARRAY, subjectTaxMap);
					}else{
					     taxFee(accountingId, null, subjectTaxMap);
					}
				}else if(taxValueAdded.getDeclarantPeriod()==CustomerTaxItem.SEASON_ACCRUED_TYPE){
					if(outputTax.compareTo(REGULAR_MONEY.multiply(new BigDecimal(3)))<=0){
						taxFee(accountingId, EXCEPT_TAX_TERM_ARRAY, subjectTaxMap);
					}else{
						taxFee(accountingId, null, subjectTaxMap);
					}
				}
			}else{
				 taxFee(accountingId, null, subjectTaxMap);
			}
		}
		log.info("税额:"+ JSON.toJSONString(subjectTaxMap));
		log.info("customerAccruedTaxAdditiveGeneral===end");
		return subjectTaxMap;
	}

	@Override
	public BigDecimal reductionTaxIncome(String accountingId ,List<String> incomeMoneyIds) {
		log.info("reductionTaxIncome===start");
		TaxValueAdded taxValueAdded = taxValueAddedDao.getAccountingId(accountingId);
		log.info("TaxValueAdded:"+JSON.toJSONString(taxValueAdded));
		if(taxValueAdded!=null){
			BigDecimal incomeMoney= getIncome(taxValueAdded.getDeclarantPeriod(), accountingId,incomeMoneyIds);
			log.info("收入:"+incomeMoney+";免税金额:"+taxValueAdded.getExemption());
			if(incomeMoney.compareTo(taxValueAdded.getExemption())<=0){
				 return  getSubjectTaxFormula(incomeMoney,INCOME_TAX_RATE);
			}
		}
		log.info("reductionTaxIncome===end");
		return BigDecimal.ZERO;
	}
	
	@Override
	public BigDecimal sumReductionTaxIncomeAdded(String accountingId,List<String> incomeMoneyIds) {
		log.info("sumReductionTaxIncomeAdded===start");
		Map<String, BigDecimal>	 subjectTaxMap=reductionTaxIncomeAdded(accountingId,incomeMoneyIds);
		BigDecimal sumSubjectTaxMoney = new BigDecimal("0.000");
			if (subjectTaxMap.size() > 0) {
				for (BigDecimal subjectTaxMoney : subjectTaxMap.values()) {
					sumSubjectTaxMoney = sumSubjectTaxMoney.add(subjectTaxMoney);
				}
			}
			log.info("减免附加税免税政策税额计算汇总:"+sumSubjectTaxMoney);
			log.info("sumReductionTaxIncomeAdded===end");
			return sumSubjectTaxMoney;
	}

	@Override
	public Map<String, BigDecimal> reductionTaxIncomeAdded(String accountingId,List<String> incomeMoneyIds) {
		log.info("reductionTaxIncomeAdded===start");
		AccountingBook accountingBook =accountingBookDao.get(accountingId);
		log.info("AccountingBook:"+JSON.toJSONString(accountingBook));
		TaxValueAdded taxValueAdded = taxValueAddedDao.getAccountingId(accountingId);
		log.info("TaxValueAdded:"+JSON.toJSONString(taxValueAdded));
		int month = 3;
		Map<String, BigDecimal> taxAddedMap= new HashMap<String, BigDecimal>();
		if(accountingBook!=null&&StringUtils.isNotEmpty(accountingBook.getTaxpayerPropertyId())){
			if(taxValueAdded!=null){
				BigDecimal exemption = taxValueAdded.getExemption();  //免税金额
				BigDecimal incomeMoney= getIncome(taxValueAdded.getDeclarantPeriod(), accountingId,incomeMoneyIds);
				log.info("免税金额:"+exemption+";收入"+incomeMoney+";计提方式:"+taxValueAdded.getDeclarantPeriod()+";纳税人性质:"+accountingBook.getTaxpayerPropertyId());
				if(accountingBook.getTaxpayerPropertyId().equals(CustomerTaxItem.SMALL_SCALE_TAXPAYER)){
					if(taxValueAdded.getDeclarantPeriod() == CustomerTaxItem.MONTH_ACCRUED_TYPE){
						if(exemption.compareTo(incomeMoney)<0&&incomeMoney.compareTo(REGULAR_MONEY) <= 0){
							 taxAdded(accountingId, EXCEPT_TAX_TERM_ARRAY,taxAddedMap);
						}
					}else if(taxValueAdded.getDeclarantPeriod()==CustomerTaxItem.SEASON_ACCRUED_TYPE){
						if(exemption.compareTo(incomeMoney)<0&&incomeMoney.compareTo(REGULAR_MONEY.multiply(new BigDecimal(month))) <= 0){
							 taxAdded(accountingId, EXCEPT_TAX_TERM_ARRAY,taxAddedMap);
						}
					}
				}else if(accountingBook.getTaxpayerPropertyId().equals(CustomerTaxItem.GENERAL_TAXPAYER)){
					if(incomeMoney.compareTo(exemption)<0){
						 taxAdded(accountingId, EXCEPT_TAX_TERM_ARRAY,taxAddedMap);
					}
				}
			}
		}
		log.info("taxAddedMap"+JSON.toJSONString(taxAddedMap));
		log.info("reductionTaxIncomeAdded===end");
		return taxAddedMap;
	}

	@Override
	public BigDecimal suMSmallScale(String accountingId, List<String> ids) {
		log.info("suMSmallScale===start");
		log.info("accountingId:"+accountingId+";ids"+JSON.toJSONString(ids));
		Map<String, BigDecimal> subjectTaxMap = new HashMap<String, BigDecimal>();
		TaxValueAdded taxValueAdded = taxValueAddedDao.getAccountingId(accountingId);
		log.info("TaxValueAdded:"+JSON.toJSONString(taxValueAdded));
		BigDecimal sumTax= new BigDecimal("0.00");//税额汇总 
		if(taxValueAdded!=null){
			BigDecimal  taxMoney=suMcustomerAccruedTaxAdditiveSmallScale(accountingId,ids);  //其他税额 (其他税项中包含印花税)
			BigDecimal incomeMoney = getIncome(taxValueAdded.getDeclarantPeriod(), accountingId,ids); //获取收入
			Map<String, BigDecimal> stampDutyMoneys =taxAdded(accountingId,STAMP_DUTY.split(","),subjectTaxMap);//获取印花税税额
			BigDecimal stampDutyTaxMoney= new BigDecimal("0.00");//印花税税额 
			for (BigDecimal stampDuty: stampDutyMoneys.values()) {
				stampDutyTaxMoney=stampDutyTaxMoney.add(stampDuty);
			}
			sumTax=sumTax.add(taxMoney).add(incomeMoney);
			if(stampDutyTaxMoney.compareTo(STAMP_DUTY_TAX)<0){
				sumTax=sumTax.subtract(stampDutyTaxMoney);
			}
		}
		log.info("sumTax"+sumTax);
		log.info("suMSmallScale===end");
		return sumTax;
	}

	@Override
	public BigDecimal suMGeneral(String accountingId, String incomeTaxId, String outputTaxId,List<String> ids) {
		log.info("suMGeneral===start");
		log.info("accountingId:"+accountingId+";ids"+JSON.toJSONString(ids));
		Map<String, BigDecimal> subjectTaxMap = new HashMap<String, BigDecimal>();
		TaxValueAdded taxValueAdded = taxValueAddedDao.getAccountingId(accountingId);
		log.info("TaxValueAdded:"+JSON.toJSONString(taxValueAdded));
		BigDecimal sumTax= new BigDecimal("0.00");//税额汇总
		if(taxValueAdded!=null){
			BigDecimal otherTax=suMcustomerAccruedTaxAdditiveGeneral( accountingId, incomeTaxId,  outputTaxId);//其他税额 (其他税项中包含印花税)
			BigDecimal incomeMoney = getIncome(taxValueAdded.getDeclarantPeriod(), accountingId,ids); //获取收入
			Map<String, BigDecimal> stampDutyMoneys =taxAdded(accountingId,STAMP_DUTY.split(","),subjectTaxMap);//获取印花税税额
			BigDecimal stampDutyTaxMoney= new BigDecimal("0.00");//印花税税额 
			for (BigDecimal stampDuty: stampDutyMoneys.values()) {
				stampDutyTaxMoney=stampDutyTaxMoney.add(stampDuty);
			}
			sumTax=sumTax.add(otherTax).add(incomeMoney);
			if(stampDutyTaxMoney.compareTo(STAMP_DUTY_TAX)<0){
				sumTax=sumTax.subtract(stampDutyTaxMoney);
			}
		}
		log.info("sumTax"+sumTax);
		log.info("suMGeneral===end");
		return sumTax;
	}
}
