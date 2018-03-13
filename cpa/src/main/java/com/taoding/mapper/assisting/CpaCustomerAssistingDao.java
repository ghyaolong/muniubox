
package com.taoding.mapper.assisting;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.assisting.CpaCustomerAssisting;

/**
 * 辅助核算类型DAO接口
 * @author csl
 * @version 2017-11-16
 */
@Repository
@Mapper
public interface CpaCustomerAssistingDao extends CrudDao<CpaCustomerAssisting> {
	
	/**
	 * 查找所有的辅助核算类型的列表
	 * @param customerId
	 * @return
	 */
	public List<CpaCustomerAssisting> findList(String customerId);

	/**
	 * 遍历辅助核算模板中的数据添加到辅助核算类型中
	 * @param findList
	 */
	public int insertList(@Param("findList") List<CpaCustomerAssisting> findList);
	
	/**
	 * 根据sort查询最大排序号 
	 * @param accountId
	 * @return
	 */
	public String findMaxSortByInfoSort(String accountId);
	
	/**
	 * 通过名字查找自定义分类名称
	 * @param name
	 * @param accountId
	 * @return
	 */
	public CpaCustomerAssisting findByName(@Param("catalogName") String catalogName,@Param("accountId") String accountId);
	
	/**
	 * 根据id和custom删除自定义分类
	 * @param id
	 * @param isCustom
	 * @return
	 */
	public int deleteCustom(@Param("id") String id);
	
	/**
	 * 根据科目id,查询科目下所有辅助核算信息
	 * 2017年11月22日 下午4:39:55
	 * @param subjectId
	 * @return
	 */
	public List<CpaCustomerAssisting> findListBySubjectId(String subjectId);
}