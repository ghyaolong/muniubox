package com.taoding.mapper.subject;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.subject.CpaSubject;


/**
 * 科目详细
 * @author czy
 * 2017年11月17日 下午3:06:17
 */
@Repository
@Mapper
public interface CpaSubjectDao extends CrudDao<CpaSubject> {

	/**	
	 * 根据类型查询该类型下所有的科目
	 * 2017年11月23日 下午2:05:09
	 * @param type
	 * @return
	 */
	public List<CpaSubject> findAllList(boolean type) ;
	
	/**
	 * 检索科目编号唯一性
	 * 2017年11月23日 下午2:26:06
	 * @param type
	 * @param subjectNo
	 * @return
	 */
	public CpaSubject findBySubjectNo(@Param("type") boolean type ,@Param("subjectNo") String subjectNo );
	
	/**
	 * 检索科目名称唯一性
	 * 2017年11月23日 下午2:26:36
	 * @param type
	 * @param subjectName
	 * @param parentNo
	 * @return
	 */
	public CpaSubject findBySubjectName(@Param("type") boolean type ,@Param("subjectName") String subjectName,@Param("parentNo") String parentNo);
	
	/**
	 * 根据父No查询子科目数据
	 * 2017年11月18日 上午10:44:55
	 * @param parentNo
	 * @return
	 */
	public List<CpaSubject> findByParentNo(@Param("type") boolean type ,@Param("parentNo") String parentNo);
	
	/**
	 * 根据科目类型查询科目信息
	 * 2017年11月18日 下午2:00:20
	 * @param catalogId
	 * @return
	 */
	public List<CpaSubject> findAllBycatalogId(String catalogId);
	
}
