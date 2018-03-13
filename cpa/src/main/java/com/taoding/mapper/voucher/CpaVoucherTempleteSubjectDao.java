package com.taoding.mapper.voucher;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.voucher.CpaVoucherTempleteSubject;

/**
 * 凭证模板科目
 * 
 * @author czy 2017年11月28日 上午11:34:22
 */
@Repository
@Mapper
public interface CpaVoucherTempleteSubjectDao extends CrudDao<CpaVoucherTempleteSubject>{

	/**
	 * 批量新增模板科目
	 * 2017年11月28日 下午4:20:53
	 * @param lists
	 * @return
	 */
	public int batchInsert(List<CpaVoucherTempleteSubject> lists);
	
	
	/**
	 * 根据凭证模板ID删除模板科目
	 * 2017年11月28日 下午4:43:45
	 * @param templeteId
	 * @return
	 */
	public int deleteByTempleteId(String templeteId);
	
	/**
	 * 通过模板id查询模板科目
	 * 2017年11月28日 下午5:37:34
	 * @param templeteId
	 * @param bookId
	 * @return
	 */
	public List<CpaVoucherTempleteSubject> findByTempleteId(@Param("bookId") String bookId,
			@Param("templeteId") String templeteId);
	
	
	/**
	 * 通过科目id查询所有记账凭证所有科目信息
	 * 2017年12月1日 下午3:06:24
	 * @param bookId
	 * @param subjectId
	 * @return
	 */
	public List<CpaVoucherTempleteSubject> findBySubjectId(@Param("bookId") String bookId,
			@Param("subjectId") String subjectId);
}
