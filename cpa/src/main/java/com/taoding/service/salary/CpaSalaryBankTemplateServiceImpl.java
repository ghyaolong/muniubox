package com.taoding.service.salary;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.salary.CpaSalaryBankTemplate;
import com.taoding.domain.salary.CpaSalaryEmployee;
import com.taoding.mapper.salary.CpaSalaryBankTemplateDao;
import com.taoding.mapper.salary.CpaSalaryEmployeeDao;


@Service
@Transactional
public class CpaSalaryBankTemplateServiceImpl  extends DefaultCurdServiceImpl<CpaSalaryBankTemplateDao, CpaSalaryBankTemplate> implements CpaSalaryBankTemplateService {

	@Override
	public List<CpaSalaryBankTemplate> salaryBankTemplateList() {
		return dao.salaryBankTemplateList();
	}

	@Override
	public CpaSalaryBankTemplate getBankTemplateById(String id) {
		return dao.getBankTemplateById(id);
	}

	@Override
	public CpaSalaryBankTemplate getBankTemplateByCode(String code) {
		return dao.getBankTemplateByCode(code);
	}

}
