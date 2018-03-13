package com.taoding.mapper.checkoutSetting;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.checkoutSetting.SettleAccountBasic;
/**
 * 企业结账启用禁用配置Service
 * 
 * @author mhb
 * @version 2017-12-25
 */
@Repository
@Mapper
public interface SettleAccountBasicDao extends CrudDao<SettleAccountBasic> {
	/**
	 * 根据类型查询企业结账启用禁用配置模板数据
	 * @param basicType 类型  1 基础
	 * @return
	 */
	List<SettleAccountBasic> findAllList();
}
