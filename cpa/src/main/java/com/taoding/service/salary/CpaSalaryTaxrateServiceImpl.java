package com.taoding.service.salary;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.salary.CpaSalaryTaxrate;
import com.taoding.mapper.salary.CpaSalaryTaxrateDao;

/**
 * 税率Service
 * @author csl
 * @version 2017-12-13
 */
@Service
@Transactional
public class CpaSalaryTaxrateServiceImpl extends DefaultCurdServiceImpl<CpaSalaryTaxrateDao, CpaSalaryTaxrate> 
	implements CpaSalaryTaxrateService{

	
}