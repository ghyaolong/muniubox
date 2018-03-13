
package com.taoding.mapper.assisting;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.assisting.CpaAssistingExpenseType;

/**
 * 辅助核算模块的费用类型DAO接口
 * @author csl
 * @version 2017-11-20
 */
@Repository
@Mapper
public interface CpaAssistingExpenseTypeDao extends CrudDao<CpaAssistingExpenseType> {
	
	/**
	 * 查询所有的费用
	 */
	public List<CpaAssistingExpenseType> findAllList();
}