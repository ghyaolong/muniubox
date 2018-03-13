package com.taoding.mapper.customerInfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.customerInfo.CpaCustomerContactInfo;

/**
 * 客户签约信息Dao
 * @author mhb
 * @version 2017-11-16
 */
@Repository
@Mapper
public interface CpaCustomerContactInfoDao extends CrudDao<CpaCustomerContactInfo> {
	/**
	 * 根据id删除公司信息
	 * 
	 * @param coutomerId  公司id
	 * @param delFlag  删除标识
	 * @return
	 */

	List<CpaCustomerContactInfo> findCustomerContactInfoByCoutomerInfoId(@Param("coutomerId") String id,@Param("delFlag")String delFlag);
}
