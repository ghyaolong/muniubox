package com.taoding.mapper.addition;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.addition.CpaCustomerSubjectBank;
/**
 * 公司银行Dao
 * @author mhb
 * @version 2017-11-20
 */
@Repository
@Mapper
public interface CpaCustomerSubjectBankDao extends CrudDao<CpaCustomerSubjectBank> {
	/**
	 * 查询公司联系人信息
	 * 
	 * @param coutomerId  公司id
	 * @param  delFlag   删除标识
	 * @return
	 */
	List<CpaCustomerSubjectBank> findCustomerSubjectBankByCoutomerInfoId(@Param("coutomerId") String id,@Param("delFlag") String delFlag);
	/**
	 * 查询公司科目银行信息
	 * 
	 * @param maps 
	 * @return
	 */
	List<CpaCustomerSubjectBank> selectList(Map<String, Object> maps); 
}
