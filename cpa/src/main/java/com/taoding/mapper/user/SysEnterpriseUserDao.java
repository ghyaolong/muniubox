package com.taoding.mapper.user;

 

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.user.SysEnterpriseUser;
 

/**
 * 企业，组织，用户管理DAO接口
 * @author lixc
 * @version 2017-10-26
 */
@Repository
@Mapper
public interface SysEnterpriseUserDao extends CrudDao<SysEnterpriseUser> {
	
	/**
	 * 删除企业，组织，用户
	 * @param sysEnterpriseUser
	 * @return
	 */
	public int deleteRealy(SysEnterpriseUser sysEnterpriseUser);
	/**
	 * 插入企业用户关联表
	 * @param sysEnterpriseUserList
	 */
	public void insertSysEnterpriseUser(@Param("sysEnterpriseUserList") List<SysEnterpriseUser> sysEnterpriseUserList);
	
	
}