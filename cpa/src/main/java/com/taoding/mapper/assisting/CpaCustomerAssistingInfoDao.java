
package com.taoding.mapper.assisting;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.taoding.common.dao.CrudDao;
import com.taoding.domain.assisting.CpaCustomerAssistingInfo;

/**
 * 辅助核算DAO接口
 * @author csl
 * @version 2017-11-16
 */
@Repository
@Mapper
public interface CpaCustomerAssistingInfoDao extends CrudDao<CpaCustomerAssistingInfo> {
	
	/**
	 * 分页+按条件查询用户
	 * @param queryMap
	 * @return
	 * add csl 
	 * 2017-11-18 11:58:29
	 */
	public List<CpaCustomerAssistingInfo> findListById(Map<String,Object> queryMap);
	
	/**
	 * 根据名字查找
	 * @param name
	 * @param id
	 * @return
	 * add csl
	 * 2017-11-18 16:18:09
	 */
	public CpaCustomerAssistingInfo findByName(@Param("name") String name,@Param("id") String id);
	
	/**
	 * 查询编号的最大号码
	 * @return
	 */
	public String findMaxNoByInfoNo();
}