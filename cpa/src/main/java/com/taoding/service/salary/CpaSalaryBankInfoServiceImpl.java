package com.taoding.service.salary;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.domain.salary.CpaSalaryBankInfo;
import com.taoding.domain.salary.CpaSalaryEmployee;
import com.taoding.mapper.salary.CpaSalaryBankInfoDao;
import com.taoding.mapper.salary.CpaSalaryEmployeeDao;


@Service
@Transactional
public class CpaSalaryBankInfoServiceImpl  extends DefaultCurdServiceImpl<CpaSalaryBankInfoDao, CpaSalaryBankInfo> implements CpaSalaryBankInfoService {

	@Override
	public boolean saveSalarySetting(CpaSalaryBankInfo cpaSalaryBankInfo) {
		if(StringUtils.isEmpty(cpaSalaryBankInfo.getCustomerId())){
			return false;
		}
		int i = 0;
		if(StringUtils.isEmpty(cpaSalaryBankInfo.getId())){
			cpaSalaryBankInfo.setId(UUIDUtils.getUUid());
			i = dao.insertSetting(cpaSalaryBankInfo);
		}else{
			i = dao.updateSetting(cpaSalaryBankInfo);
		}
		if(i > 0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public CpaSalaryBankInfo getSalarysetting(String customerId) {
		return dao.getSalarysetting(customerId);
	}

}
