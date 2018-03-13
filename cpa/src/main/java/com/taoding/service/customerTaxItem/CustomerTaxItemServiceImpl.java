package com.taoding.service.customerTaxItem;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.customerInfo.CustomerInfo;
import com.taoding.domain.customerTaxItem.CpaFormula;
import com.taoding.domain.customerTaxItem.CustomerTaxFormula;
import com.taoding.domain.customerTaxItem.CustomerTaxItem;
import com.taoding.domain.customerTaxItem.TaxTemplate;
import com.taoding.mapper.accountingBook.AccountingBookDao;
import com.taoding.mapper.customerTaxItem.CpaFormulaDao;
import com.taoding.mapper.customerTaxItem.CustomerTaxFormulaDao;
import com.taoding.mapper.customerTaxItem.CustomerTaxItemDao;
import com.taoding.mapper.customerTaxItem.TaxTemplateDao;
import com.taoding.mapper.subject.CpaCustomerSubjectDao;
import com.taoding.mapper.subject.CpaSubjectDao;
import com.taoding.service.customerInfo.CustomerInfoService;

/**
 * 客户税项设置ServiceImpl
 * 
 * @author mhb
 * @version 2017-11-22
 */
@Service
@Transactional
public class CustomerTaxItemServiceImpl  extends
DefaultCurdServiceImpl<CustomerTaxItemDao, CustomerTaxItem> implements
CustomerTaxItemService {

	@Autowired
	private AccountingBookDao   accountingBookDao;
	@Autowired
	private TaxTemplateDao taxTemplateDao;
	
	@Autowired
	private CpaSubjectDao cpaSubjectDao;
	@Autowired
	private CustomerTaxFormulaDao customerTaxFormulaDao;
	@Autowired
	private CpaFormulaDao cpaFormulaDao;
	@Autowired
	private CustomerInfoService customerInfoService;  
	
	@Autowired 
	private CpaCustomerSubjectDao cpaCustomerSubjectDao;

	@Override
	public Object saveCustomerTaxtemplate(AccountingBook accountingBook) {
		/*查询客户税项默认模板*/ 
		if(StringUtils.isNotEmpty(accountingBook.getCustomerInfoId())){
			CustomerInfo customerInfo=customerInfoService.get(accountingBook.getCustomerInfoId());
			if(customerInfo!=null&&StringUtils.isNotEmpty(customerInfo.getProvince())){
				if(accountingBook!=null&&StringUtils.isNotEmpty(accountingBook.getTaxpayerPropertyId())){
					List<TaxTemplate> taxtemplatelist = taxTemplateDao.findTaxTemplateList(CustomerTaxItem.DEL_FLAG_NORMAL,customerInfo.getProvince());
					for (TaxTemplate taxtemplate : taxtemplatelist) {
						if (StringUtils.isNotEmpty(taxtemplate.getName())&& StringUtils.isNotEmpty(taxtemplate.getSubjectId())) {
							CustomerTaxItem customertaxtemplate = new CustomerTaxItem();
							this.preInsert(customertaxtemplate);
							this.preUpdate(customertaxtemplate);
							customertaxtemplate.setId(UUIDUtils.getUUid());
							customertaxtemplate.setName(taxtemplate.getName());
							customertaxtemplate.setRate(taxtemplate.getRate());
							customertaxtemplate.setDeleteable(CustomerTaxItem.DEL_EABLE_DELETE);   //默认模板不能删除字段
							customertaxtemplate.setSubjectId(taxtemplate.getSubjectId());
							customertaxtemplate.setEnable(CustomerTaxItem.DEL_EABLE_NORMAL);
							customertaxtemplate.setDelFlag(CustomerTaxItem.DEL_FLAG_NORMAL);
							customertaxtemplate.setAccountingBookId(accountingBook.getId());
							if(accountingBook.getTaxpayerPropertyId().equals(CustomerTaxItem.SMALL_SCALE_TAXPAYER)){
								customertaxtemplate.setAccruedType(CustomerTaxItem.MONTH_ACCRUED_TYPE);
							}else if(accountingBook.getTaxpayerPropertyId().equals(CustomerTaxItem.GENERAL_TAXPAYER)){
								customertaxtemplate.setAccruedType(CustomerTaxItem.SEASON_ACCRUED_TYPE);
							}
							/*插入客户税项模板*/
							dao.insert(customertaxtemplate);
							/*插入客户税项模板公式*/
						List<CpaFormula> cpaFormulaList=cpaFormulaDao.findCustomerTaxtemplateByTaxTemplateId(taxtemplate.getId());
							for (CpaFormula cpaFormula : cpaFormulaList) {
								 if(StringUtils.isNotEmpty(cpaFormula.getSubjectId())){
									 CustomerTaxFormula customerTaxFormula=new CustomerTaxFormula();
										this.preInsert(customerTaxFormula);
										this.preUpdate(customerTaxFormula);
										customerTaxFormula.setId(UUIDUtils.getUUid());
										customerTaxFormula.setCustomerTaxId(customertaxtemplate.getId()); //客户税项设置id
										customerTaxFormula.setSubjectId(taxtemplate.getSubjectId()); //关联科目
										customerTaxFormula.setOperator(cpaFormula.isOperator());   //运算符
										customerTaxFormulaDao.insert(customerTaxFormula);
								 }
							}
						}
					}
				}
			}
		}
		return true;
	}
	@Override
	public Object saveTax(CustomerTaxItem customerTaxItem) {
		if (accountingBookDao.get(customerTaxItem.getAccountingBookId()) == null||cpaCustomerSubjectDao.get(customerTaxItem.getSubjectId())==null) {
			throw new LogicException("账薄信息或科目信息不存在!");
		}
		AccountingBook accountingBook =accountingBookDao.get(customerTaxItem.getAccountingBookId());
			this.preInsert(customerTaxItem);
			this.preUpdate(customerTaxItem);
			customerTaxItem.setId(UUIDUtils.getUUid());
			customerTaxItem.setDeleteable(CustomerTaxItem.DEL_EABLE_NORMAL);
			customerTaxItem.setEnable(CustomerTaxItem.DEL_EABLE_NORMAL);
			/*插入客户新增税项*/
			dao.insert(customerTaxItem);
		return true;
	}
	@Override
	public Object editTax(CustomerTaxItem customerTaxItem) {
		if (accountingBookDao.get(customerTaxItem.getAccountingBookId()) == null||cpaCustomerSubjectDao.get(customerTaxItem.getSubjectId())==null){
			throw new LogicException("账薄信息或科目信息不存在!");
		}
		 CustomerTaxItem cti= dao.get(customerTaxItem.getId());
		 if(cti==null){
			 throw new LogicException("修改客户税项信息不存在!");
		 };
		 if(cti.isEnable()){
			 customerTaxItem.setEnable(CustomerTaxItem.DEL_EABLE_NORMAL);
		 }
		 dao.update(customerTaxItem);
		return true;
	}

	@Override
	public Object deleteTax(String id) {
		 if(StringUtils.isEmpty(id)){
			 throw new LogicException("删除信息有误!");
		 }
		 CustomerTaxItem customerTaxItem=dao.get(id);
		if(customerTaxItem==null){
			throw new LogicException("删除信息不存在!");	
		} 
		if(!customerTaxItem.isDeleteable()){
			throw new LogicException("基础税项模板不允许删除!");	
		}
		CustomerTaxItem deleteCustomerTaxItem = new CustomerTaxItem();
		deleteCustomerTaxItem.setId(id);
		deleteCustomerTaxItem.setDelFlag(CustomerTaxItem.DEL_FLAG_DELETE);
		dao.update(deleteCustomerTaxItem);
		return true;
	}

	@Override
	public Object enableTax(String id) {
		boolean flag=false;
		 if(StringUtils.isEmpty(id)){
			 throw new LogicException("启用/禁用信息有误!");
		 }
		 CustomerTaxItem customerTaxItem=dao.get(id);
		 if(customerTaxItem==null){
				throw new LogicException("启用/禁用信息不存在!");	
			} 
		 if(!customerTaxItem.isDeleteable()){
			 throw new LogicException("基础税项模板不允许禁用!");	 
		 }
		 if(customerTaxItem.isEnable()==false){
			 flag=true;
		 }
		 CustomerTaxItem enableTax=new CustomerTaxItem();
		      enableTax.setId(id);
		      enableTax.setEnable(flag);
		      dao.update(enableTax);
		return true;
	}

	@Override
	public Object findCustomerTaxList(Map<String, Object> queryMap) {
		
		if(null  != queryMap.get("accountingBookId")){
			if(accountingBookDao.get(String.valueOf(queryMap.get("accountingBookId")))==null){
				throw new LogicException("账薄信息不存在!");	
			}
		}
		
	
		List<CustomerTaxItem> customerTaxItemList=dao.findList(queryMap);
		PageInfo<CustomerTaxItem> info = new PageInfo<CustomerTaxItem>(customerTaxItemList);
		return info;
	}
	@Override
	public Object selectTaxById(String id) {
		return dao.get(id);
	}
}
