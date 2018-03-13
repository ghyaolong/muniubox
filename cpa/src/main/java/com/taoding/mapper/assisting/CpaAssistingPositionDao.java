
package com.taoding.mapper.assisting;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.assisting.CpaAssistingPosition;

/**
 * 职位DAO接口
 * @author csl
 * @version 2017-11-22
 */
@Repository
@Mapper
public interface CpaAssistingPositionDao extends CrudDao<CpaAssistingPosition> {
	/**
	 * 查询辅助核算类型的类型+分页
	 * @param queryMap
	 * @return
	 */
	public List<CpaAssistingPosition> findAllList(Map<String,Object> queryMap);
	
	/**
	 * 根据名字查找
	 * @param positionName
	 * @return
	 * add csl
	 * 2017-11-18 16:18:09
	 */
	public CpaAssistingPosition findByName(@Param("positionName") String positionName,@Param("accountId") String accountId);
	
	/**
	 * 查询编号的最大号码
	 * @param accountId
	 * @return
	 */
	public String findMaxNoByInfoNo(String accountId);
	
	/**
	 * 查询所有的职位
	 * @param accountId
	 * @return
	 */
	public List<CpaAssistingPosition> findList(String accountId);
}