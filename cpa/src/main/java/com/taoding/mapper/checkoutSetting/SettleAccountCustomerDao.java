package com.taoding.mapper.checkoutSetting;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.checkoutSetting.SettleAccountCustomer;
/**
 * 企业结账启用禁用配置Dao
 * 
 * @author mhb
 * @version 2017-12-25
 */
@Repository
@Mapper
public interface SettleAccountCustomerDao extends CrudDao<SettleAccountCustomer> {
	/**
	 * 初始化插入企业结账启用禁用配置
	 * @param settleAccountBasicList 结账基础数据 
	 * @return
	 */
	int batchInsert(@Param("settleAccountCustomerList")List<SettleAccountCustomer> settleAccountCustomerList);
	/**
	 * 启用/禁用
	 * @param id  主键id
	 * @return
	 */
	int updateEnabled(@Param("id")String id,@Param("enable") boolean enable);
	/**
	 * 查询账薄下企业结账配置
	 * @param basicType 类型 1 基础 2 其他异常
	 * @param bookId 账薄id
	 * @return
	 */
	List<SettleAccountCustomer> findList(@Param("bookId")String bookId,@Param("basicType") int basicType);
}
