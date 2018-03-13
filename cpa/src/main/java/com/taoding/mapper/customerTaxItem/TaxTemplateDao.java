package com.taoding.mapper.customerTaxItem;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.customerTaxItem.TaxTemplate;

/**
 * 税项模板Dao
 * 
 * @author mhb
 * @version 2017-11-22
 */
@Repository
@Mapper
public interface TaxTemplateDao extends CrudDao<TaxTemplate> {


	/**
	 * 查询税项模板
	 * 
	 * @param region  地区
	 * @param delFlag 是否删除/默认省份模板
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	List<TaxTemplate> findTaxTemplateList(@Param("delFlag")String delFlag,@Param("province") String region);

}

