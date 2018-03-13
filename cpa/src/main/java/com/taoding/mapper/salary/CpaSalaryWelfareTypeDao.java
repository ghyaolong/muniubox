
package com.taoding.mapper.salary;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import com.taoding.common.dao.CrudDao;
import com.taoding.domain.salary.CpaSalaryWelfareType;

/**
 * 社保公积金缴纳项目表DAO接口
 * @author csl
 * @version 2017-11-24
 */
@Repository
@Mapper
public interface CpaSalaryWelfareTypeDao extends CrudDao<CpaSalaryWelfareType> {
	
	/**
	 * 查询所有的社保公积金缴纳项目的名称
	 * @param
	 * @return
	 */
	public List<CpaSalaryWelfareType> findAllName();
}