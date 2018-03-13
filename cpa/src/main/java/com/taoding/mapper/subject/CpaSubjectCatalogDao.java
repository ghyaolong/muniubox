package com.taoding.mapper.subject;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.subject.CpaSubjectCatalog;

/**
 * 科目类型，目前只分为五类
 * @author czy
 *
 */
@Repository
@Mapper
public interface CpaSubjectCatalogDao extends CrudDao<CpaSubjectCatalog> {

	
	/**
	 * 根据名称查询科目
	 * 2017年11月18日 下午2:15:03
	 * @param name
	 * @return
	 */
	public CpaSubjectCatalog findByName(String name);
	
	
}
