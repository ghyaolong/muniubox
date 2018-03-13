package com.taoding.mapper.subject;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.subject.CpaSubjectAssisting;


/**
 * 科目 辅助核算项关系
 * @author czy
 * 2017年11月22日 下午3:34:49
 */
@Repository
@Mapper
public interface CpaSubjectAssistingDao extends CrudDao<CpaSubjectAssisting>{

	/**
	 * 根据科目Id 查询该科目所有辅助核算项 
	 * 2017年11月22日 下午3:48:17
	 * @param subjectId
	 * @return
	 */
	public List<CpaSubjectAssisting> findBySubjectId(String subjectId);
	
	/**
	 * 根据科目Id 删除科目所有辅助核算项
	 * 2017年11月22日 下午3:48:41
	 * @param subjectId
	 * @return
	 */
	public int deleteBySubjectId(String subjectId);
	
	
}
