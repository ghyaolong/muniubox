package com.taoding.service.customerTaxItem;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.customerTaxItem.CpaFormula;
import com.taoding.domain.customerTaxItem.TaxTemplate;
import com.taoding.mapper.customerTaxItem.CpaFormulaDao;
import com.taoding.mapper.customerTaxItem.TaxTemplateDao;
import com.taoding.mapper.subject.CpaSubjectDao;

/**
 * 税项模板ServiceImpl
 * 
 * @author mhb
 * @version 2017-11-22
 */
@Service
@Transactional
public class TaxTemplateServiceImpl extends DefaultCurdServiceImpl<TaxTemplateDao, TaxTemplate> implements TaxTemplateService {
    
	@Autowired
	private CpaSubjectDao cpaSubjectDao;
	@Autowired CpaFormulaDao cpaFormulaDao;
	@Override
	public Object saveTemplate(TaxTemplate taxTemplate) {
		if (taxTemplate == null || StringUtils.isEmpty(taxTemplate.getName())||StringUtils.isEmpty(taxTemplate.getSubjectId())||cpaSubjectDao.get(taxTemplate.getSubjectId())==null) {
			throw new LogicException("添加税项模板信息不完整或信息有误!");
		}
		if(Collections3.isEmpty(taxTemplate.getCpaFormulaList())){
			throw new LogicException("税项模板公式信息不能为空!");
		}
		
		if(StringUtils.isEmpty(taxTemplate.getProvince())){
			taxTemplate.setProvince("0");   //通用模板为0
			taxTemplate.setCity("0");  //市同省
 		}else{
 			taxTemplate.setCity(taxTemplate.getProvince());  //市同省
 		}
		
		taxTemplate.setId(UUID.randomUUID().toString().replace("-", ""));
		this.preInsert(taxTemplate);
		this.preUpdate(taxTemplate);
		taxTemplate.setEnable(true);
		/*插入税项模板*/
		dao.insert(taxTemplate);
		for (CpaFormula cpaFormula : taxTemplate.getCpaFormulaList()) {
			   if(StringUtils.isEmpty(cpaFormula.getSubjectId())||cpaSubjectDao.get(cpaFormula.getSubjectId())==null){
				   throw new LogicException("税项模板公式信息不完整!");
			   }
			   cpaFormula.setTaxTemplateId(taxTemplate.getId());
			   this.preInsert(cpaFormula);
			   this.preUpdate(cpaFormula);
			   cpaFormula.setId(UUID.randomUUID().toString().replace("-", ""));
			   
		}
		/*插入税项模板公式*/
		cpaFormulaDao.insertList(taxTemplate.getCpaFormulaList());
		return true;
	}

	@Override
	public Object editTemplate(TaxTemplate taxTemplate) {
		if (taxTemplate == null || StringUtils.isEmpty(taxTemplate.getId())) {
			throw new LogicException("修改信息有误!");
		}
		if (dao.get(taxTemplate.getId()) == null) {
			throw new LogicException("修改信息不存在!");
		}
		dao.update(taxTemplate);
		return true;
	}

	@Override
	public Object deleteTemplate(String id) {
		if (StringUtils.isEmpty(id)) {
			throw new LogicException("删除信息有误!");
		}
		if (dao.get(id) == null) {
			throw new LogicException("删除信息不存在!");
		}
		TaxTemplate taxTemplate = new TaxTemplate();
		taxTemplate.setId(id);
		taxTemplate.setDelFlag(TaxTemplate.DEL_FLAG_DELETE);
		dao.update(taxTemplate);
		return true;
	}

	@Override
	public Object enableTemplate(String id) {
		boolean flag=false;
		if (StringUtils.isEmpty(id)) {
			throw new LogicException("信息有误!");
		}

		TaxTemplate taxTemplate = dao.get(id);
		if (taxTemplate == null) {
			throw new LogicException("信息不存在!");
		}
		if(!taxTemplate.isEnable()){
			flag=true;
		}
		TaxTemplate t = new TaxTemplate();
			 t.setId(id);
	         t.setEnable(flag);
			 dao.update(t);
		return true;
	}
}
