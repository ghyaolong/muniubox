package com.taoding.mapper.customerInfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.customerInfo.CpaCustomerLinkman;
/**
 * 客户管理详请Dao
 * @author mhb
 * @version 2017-11-16
 */
@Repository
@Mapper
public interface CpaCustomerLinkmanDao extends CrudDao<CpaCustomerLinkman> {
	/**
	 * 查询公司联系人信息
	 * 
	 * @param coutomerId  公司id
	 * @param  delFlag   删除标识
	 * @param id       联系人id
	 * @return
	 */
	List<CpaCustomerLinkman>  findCustomerLinkmanByCoutomerInfoId(@Param("coutomerId") String coutomerId,@Param("delFlag")String delFlag,@Param("id") String id);
}
