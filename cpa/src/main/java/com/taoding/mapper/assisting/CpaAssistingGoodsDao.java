
package com.taoding.mapper.assisting;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.assisting.CpaAssistingGoods;

/**
 * 辅助核算模块存货信息表DAO接口
 * @author csl
 * @version 2017-11-20
 */
@Repository
@Mapper
public interface CpaAssistingGoodsDao extends CrudDao<CpaAssistingGoods> {
	
	/**
	 * 查询存货+分页
	 * @param queryMap
	 * @return
	 */
	public List<CpaAssistingGoods> findAllList(Map<String,Object> queryMap);
	
	/**
	 * 根据名字查找
	 * @param name
	 * @param accountId
	 * @return
	 * add csl
	 * 2017-11-18 16:18:09
	 */
	public CpaAssistingGoods findByName(@Param("goodsName")String goodsName ,@Param("accountId") String accountId);
	
	/**
	 * 查询编号的最大号码
	 * @param accountId
	 * @return
	 */
	public String findMaxNoByInfoNo(String accountId);
}