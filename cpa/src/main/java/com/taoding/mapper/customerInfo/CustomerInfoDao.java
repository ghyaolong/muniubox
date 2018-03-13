package com.taoding.mapper.customerInfo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.customerInfo.CustomerInfo;

/**
 * 客户管理详请Dao
 * 
 * @author mhb
 * @version 2017-11-16
 */
@Repository
@Mapper
public interface CustomerInfoDao extends CrudDao<CustomerInfo> {
	public List<CustomerInfo> findCustomInfoListByMap(Map<String, Object> queryMap);
	/**
	 * 获取最大企业编号 
	 * @param enterpriseMarking 企业标示
	 * @return
	 * @author mhb
	 */
	public String findCustomerInfoEnterpriseMarkingForMaxNo(@Param("enterpriseMarking") String currentUserEnter); 
}
