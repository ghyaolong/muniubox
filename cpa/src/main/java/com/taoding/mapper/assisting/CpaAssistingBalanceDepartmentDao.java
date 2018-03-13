
package com.taoding.mapper.assisting;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.assisting.CpaAssistingBalanceDepartment;

/**
 * 辅助核算部门DAO接口
 * @author csl
 * @version 2017-11-20
 */
@Repository
@Mapper
public interface CpaAssistingBalanceDepartmentDao extends CrudDao<CpaAssistingBalanceDepartment> {

	/**
	 * 查询辅助核算类型的类型+分页
	 * @param queryMap
	 * @return
	 */
	public List<CpaAssistingBalanceDepartment> findAllList(Map<String,Object> queryMap);
	
	/**
	 * 根据名字查找
	 * @param departmentName
	 * @param accountId
	 * @return
	 * add csl
	 * 2017-11-18 16:18:09
	 */
	public CpaAssistingBalanceDepartment findByName(@Param("departmentName") String departmentName,@Param("accountId") String accountId);
	
	/**
	 * 查询编号的最大号码
	 * @param accountId
	 * @return
	 */
	public String findMaxNoByInfoNo(String accountId);
	
	/**
	 * 查询所有的部门信息
	 * @param accountId
	 * @returnn
	 */
	public List<CpaAssistingBalanceDepartment> findList(String accountId);
}