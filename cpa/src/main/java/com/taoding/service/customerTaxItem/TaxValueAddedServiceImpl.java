package com.taoding.service.customerTaxItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.customerTaxItem.TaxValueAdded;
import com.taoding.domain.customerTaxItem.TaxValueAddedDetail;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.mapper.accountingBook.AccountingBookDao;
import com.taoding.mapper.customerTaxItem.TaxValueAddedDao;
import com.taoding.mapper.customerTaxItem.TaxValueAddedDetailDao;
import com.taoding.mapper.subject.CpaCustomerSubjectDao;

/**
 * 客户增值税设置ServiceImpl
 * 
 * @author mhb
 * @version 2017-11-27
 */
@Service
@Transactional
public class TaxValueAddedServiceImpl extends
		DefaultCurdServiceImpl<TaxValueAddedDao, TaxValueAdded> implements
		TaxValueAddedService {
	@Autowired
	private AccountingBookDao accountingBookDao;
	@Autowired
	private TaxValueAddedDetailDao taxValueAddedDetailDao;
	@Autowired
	private CpaCustomerSubjectDao cpaCustomerSubjectDao;

	@Override
	public Object insert(TaxValueAdded taxValueAdded) {
		if (StringUtils.isEmpty(taxValueAdded.getAccountingId())||accountingBookDao.get(taxValueAdded.getAccountingId()) == null) {
			throw new LogicException("关联账薄信息有误!");
		}
		/*根据账薄id删除客户已有的增值税设置*/
		dao.deleteAccountingId(taxValueAdded.getAccountingId());
		/*根据账薄id删除客户已有的增值税设置细则*/
		taxValueAddedDetailDao.deleteAccountingId(taxValueAdded.getAccountingId());
		List<Double> taxRateList = new ArrayList<Double>();
			for (TaxValueAddedDetail taxValueAddedDetail : taxValueAdded.getTaxValueAddedDetailList()) {
				if (StringUtils.isEmpty(taxValueAddedDetail.getCostSubjectId())
						|| StringUtils.isEmpty(taxValueAddedDetail.getInputSubjectId())
						|| StringUtils.isEmpty(taxValueAddedDetail.getNotPayVat())) {
					throw new LogicException("客户增值税设置细则添加不完整!");
				}
				this.preInsert(taxValueAddedDetail);
				this.preUpdate(taxValueAddedDetail);
				taxRateList.add(taxValueAddedDetail.getTaxRate());
				taxValueAddedDetail.setAccountingId(taxValueAdded.getAccountingId());
				taxValueAddedDetail.setId(UUID.randomUUID().toString().replace("-", ""));
			}
			taxValueAddedDetailDao.insertList(taxValueAdded.getTaxValueAddedDetailList());
		 for (int i = 0; i < taxRateList.size() - 1; i++) {
			for (int j = taxRateList.size() - 1; j > i; j--) {
				if (taxRateList.get(j).equals(taxRateList.get(i))) {
					throw new LogicException("客户增值税设置细则税率重复!");
				}
			}
		}
		this.preInsert(taxValueAdded);
		this.preUpdate(taxValueAdded);
		taxValueAdded.setId(UUID.randomUUID().toString().replace("-", ""));
		dao.insert(taxValueAdded);
		return true;
	}

	@Override
	public Object getIncrementTax(String accountingId) {
		 if(StringUtils.isEmpty(accountingId)||accountingBookDao.get(accountingId) == null){
			 throw new LogicException("关联账薄信息有误!!");
		 }
		 TaxValueAdded	 taxValueAdded =dao.getAccountingId(accountingId);
		 List<TaxValueAddedDetail>	taxValueAddedDetailList = taxValueAddedDetailDao.getAccountingId(accountingId);
		 for (TaxValueAddedDetail taxValueAddedDetail : taxValueAddedDetailList) {
			 if(StringUtils.isNotEmpty(taxValueAddedDetail.getCostSubjectId())){
				 taxValueAddedDetail.setCostSubjectName(sujectName(taxValueAddedDetail.getCostSubjectId()));  
			 }
			 if(StringUtils.isNotEmpty(taxValueAddedDetail.getInputSubjectId())){
				 taxValueAddedDetail.setInputSubjectName(sujectName(taxValueAddedDetail.getInputSubjectId()));  
			 }
			 if(StringUtils.isNotEmpty(taxValueAddedDetail.getOutputSubjectId())){
				 taxValueAddedDetail.setOutputSubjectName(sujectName(taxValueAddedDetail.getOutputSubjectId()));  
			 }
			 if(StringUtils.isNotEmpty(taxValueAddedDetail.getNotPayVat())){
				 taxValueAddedDetail.setNotPayVatName(sujectName(taxValueAddedDetail.getNotPayVat()));  
			 }
		}
		 if(taxValueAdded!=null){
			 taxValueAdded.setTaxValueAddedDetailList(taxValueAddedDetailList);
			 return taxValueAdded;
		 }
		 taxValueAdded= new TaxValueAdded();
		 taxValueAdded.setTaxValueAddedDetailList(taxValueAddedDetailList);
		return taxValueAdded;
	}
	
	
	public String sujectName(String sujectId){
		CpaCustomerSubject cpaCustomerSubject=cpaCustomerSubjectDao.get(sujectId);
		if(cpaCustomerSubject==null||StringUtils.isEmpty(cpaCustomerSubject.getSubjectNo())
				||StringUtils.isEmpty(cpaCustomerSubject.getSubjectName())){
			return "";
		}
		return cpaCustomerSubject.getSubjectNo().concat(" "+cpaCustomerSubject.getSubjectName());
	}
}
