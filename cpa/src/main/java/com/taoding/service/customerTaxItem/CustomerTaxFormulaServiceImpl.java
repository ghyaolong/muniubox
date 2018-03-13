package com.taoding.service.customerTaxItem;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.domain.customerTaxItem.CustomerTaxFormula;
import com.taoding.mapper.accountingBook.AccountingBookDao;
import com.taoding.mapper.customerTaxItem.CustomerTaxFormulaDao;
import com.taoding.mapper.customerTaxItem.CustomerTaxItemDao;
import com.taoding.mapper.subject.CpaCustomerSubjectDao;
import com.taoding.mapper.subject.CpaSubjectDao;

/**
 * 客户税项公式ServiceImpl
 * 
 * @author mhb
 * @version 2017-11-24
 */
@Service
@Transactional
public class CustomerTaxFormulaServiceImpl extends DefaultCurdServiceImpl<CustomerTaxFormulaDao, CustomerTaxFormula> implements CustomerTaxFormulaService {

	@Autowired
	private CpaSubjectDao cpaSubjectDao;
	@Autowired
	private AccountingBookDao   accountingBookDao;
	@Autowired
	private CustomerTaxItemDao customerTaxItemDao;
	
	@Autowired 
	private CpaCustomerSubjectDao cpaCustomerSubjectDao;
	@Override
	public Object saveTaxFormula(CustomerTaxFormula customerTaxFormula) {
		if(StringUtils.isEmpty(customerTaxFormula.getCustomerTaxId())||customerTaxItemDao.get(customerTaxFormula.getCustomerTaxId())==null){
			throw new LogicException("数据异常!");
		}
		CustomerTaxFormula deleteTaxFormula= new CustomerTaxFormula();
		deleteTaxFormula.setCustomerTaxId(customerTaxFormula.getCustomerTaxId());
		deleteTaxFormula.setDelFlag(CustomerTaxFormula.DEL_FLAG_DELETE);
		dao.update(deleteTaxFormula);
		
		if(Collections3.isEmpty(customerTaxFormula.getCustomerTaxFormulaList())){
			throw new LogicException("添加信息为空!");
		}
		 for (CustomerTaxFormula cft : customerTaxFormula.getCustomerTaxFormulaList()) {
			   
			   if(StringUtils.isEmpty(cft.getSubjectId())){
				   throw new LogicException("添加信息不完整!");
			   }
				if(cpaCustomerSubjectDao.get(cft.getSubjectId())==null){
					throw new LogicException("关联的科目信息或客户税项信息不存在!");	
				}
				cft.setId(UUIDUtils.getUUid());
				cft.setCustomerTaxId(customerTaxFormula.getCustomerTaxId());
				this.preInsert(cft);
				this.preUpdate(cft);
				dao.insert(cft);
		} 
		return true;
	}
	@Override
	public Object findList(Map<String, Object> queryMap) {
		if(customerTaxItemDao.get(String.valueOf(queryMap.get("customerTaxId")))==null||accountingBookDao.get(String.valueOf(queryMap.get("accountingBookId")))==null){
			throw new LogicException("客户税项设置信息或账薄信息不存在!");	
		}
		List<CustomerTaxFormula> customerTaxFormulaList=dao.findList(queryMap);
		PageInfo<CustomerTaxFormula> info = new PageInfo<CustomerTaxFormula>(customerTaxFormulaList);
		return info; 
	}
}
