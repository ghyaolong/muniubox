package com.taoding.service.report.profit;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.report.profit.ProfileFomulaTemplate;
import com.taoding.mapper.report.profit.ProfileFomulaTemplateDao;


@Service
@Transactional
public class ProfileFomulaTemplateServiceImpl  extends DefaultCurdServiceImpl<ProfileFomulaTemplateDao, ProfileFomulaTemplate> implements ProfileFomulaTemplateService {

	@Override
	public List<ProfileFomulaTemplate> getFomulaTemplateList() {
		return dao.getFomulaTemplateList();
	}

}
