package com.taoding.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.user.SysUserExtend;

/**
 * 用户扩展DAO接口
 * @author lixc
 * @version 2017-10-19
 */
@Repository
@Mapper
public interface SysUserExtendDao extends CrudDao<SysUserExtend> {
	
}