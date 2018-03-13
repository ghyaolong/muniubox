
package com.taoding.mapper.salary;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.salary.CpaSalaryWelfareSetting;

/**
 * 客户社保公积金配置DAO接口
 * @author csl
 * @version 2017-11-24
 */
@Repository
@Mapper
public interface CpaSalaryWelfareSettingDao extends CrudDao<CpaSalaryWelfareSetting> {
	
	/**
	 * 根据账薄id获取所有的社保/公积金缴费项目
	 * @param customerId
	 * @return
	 */
	public List<CpaSalaryWelfareSetting> findListByCustomerId(String customerId);
	
	/**
	 * 遍历模板数据到社保公积金配置信息中
	 * @param itemTypeList
	 * @return
	 */
	public int insertList(@Param("itemTypeList") List<CpaSalaryWelfareSetting> itemTypeList);
	
	/**
	 * 根据id删除企业的社保公积金缴纳项目
	 * @param id
	 * @param customerId
	 * @return
	 */
	public int deleteById(String id);
	
	/**
	 * 获取所有的方案名称
	 * @return
	 */
	public List<CpaSalaryWelfareSetting> findAllName();
	
	/**
	 * 根据方案名字查询方案的集合条目
	 * @param name
	 * @return
	 */
	public List<CpaSalaryWelfareSetting> findListByName(String projectId);
	
	/**
	 * 根据方案名称删除方案
	 * @param name
	 * @return
	 */
	public int deleteByName(@Param("projectId") String projectId,@Param("customerId") String customerId);
	
	/**
	 * 快速调整客户社保公积金详情
	 * @param cpaSalaryWelfareSetting
	 * @return
	 */
	public List<CpaSalaryWelfareSetting> findCustomerIdByWelfareId(CpaSalaryWelfareSetting cpaSalaryWelfareSetting);
	
	/**
	 * 批量保存需要更新的缴纳项目集合
	 * @param toUpdate
	 * @return
	 */
	public int updateWelfare(@Param("lists") List<CpaSalaryWelfareSetting> lists);
	
	/**
	 *批量 插入缴纳项目的集合
	 * @param toInsert
	 * @return
	 */
	public int insertWelfare(@Param("lists") List<CpaSalaryWelfareSetting> lists);
	
	/**
	 * 批量删除缴纳项目的集合
	 * @param toDelete
	 * @return
	 */
	public int deleteWelfare(@Param("lists") List<CpaSalaryWelfareSetting> lists);
	
	/**
	 * 根据客户id查询企业的初始化标记的最大值
	 * @param customerId
	 * @return
	 */
	public Integer findMaxInitFlag(String customerId);
	
	/**
	 * 初始化社保公积金方案
	 * @param cpaSalaryWelfareSetting
	 * @return
	 */
	public Integer initCpaSalaryWelfareSetting(CpaSalaryWelfareSetting cpaSalaryWelfareSetting);
	
	/**
	 * 根据客户id获取企业所有的社保公积金方案
	 * @param customerId
	 * @return
	 */
	public List<CpaSalaryWelfareSetting> findAllNameByCustomerId(String customerId);
}