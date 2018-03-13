package com.taoding.mapper.customerTaxItem;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.customerTaxItem.CustomerTaxItem;
/**
 * 客户税项设置Dao
 * @author mhb
 * @version 2017-11-24
 */
@Repository
@Mapper
public interface CustomerTaxItemDao extends CrudDao<CustomerTaxItem> {
	/**
	 * 查询客户税项列表
	 * 
	 * @param queryMap
	 * @return
	 * @author mhb
	 * @Date 2017年11月24日
	 */
	List<CustomerTaxItem> findList(Map<String, Object> queryMap);

}
