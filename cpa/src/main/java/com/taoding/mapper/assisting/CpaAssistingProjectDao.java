
package com.taoding.mapper.assisting;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.assisting.CpaAssistingProject;

/**
 * 辅助核算模块存货信息表DAO接口
 * @author csl
 * @version 2017-11-20
 */
@Repository
@Mapper
public interface CpaAssistingProjectDao extends CrudDao<CpaAssistingProject> {
	/**
	 * 查询辅助核算类型的项目类型+分页
	 * @param queryMap
	 * @return
	 */
	public List<CpaAssistingProject> findAllList(Map<String,Object> queryMap);
	
	/**
	 * 根据项目名称查询所有的对象
	 * @param projectName
	 * @return
	 */
	public CpaAssistingProject findByName(@Param("projectName") String projectName,@Param("accountId") String accountId);
	
	/**
	 * 查询编号的最大号码
	 * @param accountId
	 * @return
	 */
	public String findMaxNoByInfoNo(String accountId);
}