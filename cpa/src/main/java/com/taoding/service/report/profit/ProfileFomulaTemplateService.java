package com.taoding.service.report.profit;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.report.profit.ProfileFomulaTemplate;
import com.taoding.mapper.report.profit.ProfileFomulaTemplateDao;


public interface ProfileFomulaTemplateService extends CrudService<ProfileFomulaTemplateDao, ProfileFomulaTemplate> {

	/**
	 * 获取模板公式列表
	 * @author fc 
	 * @version 2017年12月27日 下午2:50:25 
	 * @return
	 */
	public List<ProfileFomulaTemplate> getFomulaTemplateList();
}
