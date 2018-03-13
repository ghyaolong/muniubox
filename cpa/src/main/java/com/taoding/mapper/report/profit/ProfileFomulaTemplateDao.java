package com.taoding.mapper.report.profit;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.report.profit.ProfileFomulaTemplate;


@Repository
@Mapper
public interface ProfileFomulaTemplateDao extends CrudDao<ProfileFomulaTemplate> {

	
	/**
	 * 获取模板公式列表
	 * @author fc 
	 * @version 2017年12月27日 下午2:50:25 
	 * @return
	 */
	public List<ProfileFomulaTemplate> getFomulaTemplateList();
}
