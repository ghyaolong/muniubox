package com.taoding.mapper.addition;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.addition.CpaBank;
/**
 * 银行Dao
 * @author mhb
 * @version 2017-11-18
 */
@Repository
@Mapper
public interface CpaBankDao extends CrudDao<CpaBank> {
	/**
	 * 查询银行信息
	 * @param cpaBank 银行Entity
	 * @return
	 */
	List<CpaBank> findAllList(@Param("bankName")String bankName);
}
