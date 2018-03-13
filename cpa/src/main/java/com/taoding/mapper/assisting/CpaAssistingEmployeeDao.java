
package com.taoding.mapper.assisting;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.assisting.CpaAssistingEmployee;

/**
 * 辅助核算模块员工DAO接口
 * @author csl
 * @version 2017-11-20
 */
@Repository
@Mapper
public interface CpaAssistingEmployeeDao extends CrudDao<CpaAssistingEmployee> {
	
	/**
	 *	查询所有的员工+分页
	 * @param queryMap
	 * @return
	 */
	public List<CpaAssistingEmployee> findAllList(Map<String,Object> queryMap);
	
	/**
	 * 根据名字查找
	 * @param name
	 * @param accountId
	 * @return
	 * add csl
	 * 2017-11-18 16:18:09
	 */
	public CpaAssistingEmployee findByName(@Param("name") String name,@Param("accountId") String accountId);
	
	/**
	 * 查询编号的最大号码
	 * @param accountId
	 * @return
	 */
	public String findMaxNoByInfoNo(String accountId);
	
	/**
	 * 查询所有的员工信息
	 * @return
	 */
	public List<CpaAssistingEmployee> findList(CpaAssistingEmployee cpaAssistingEmployee);
}