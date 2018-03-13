package com.taoding.service.subject;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.subject.CpaSubjectAssisting;
import com.taoding.mapper.subject.CpaSubjectAssistingDao;

/**
 * 科目 辅助核算项关系
 * 
 * @author czy 2017年11月22日 下午3:34:49
 */
@Service
public class CpaSubjectAssistingServiceImpl extends
		DefaultCurdServiceImpl<CpaSubjectAssistingDao, CpaSubjectAssisting>
		implements CpaSubjectAssistingService {

	/**
	 * 根据科目Id 查询该科目所有辅助核算项 
	 * 2017年11月22日 下午3:48:17
	 * @param subjectId
	 * @return
	 */
	@Override
	public List<CpaSubjectAssisting> findBySubjectId(String subjectId) {
		return dao.findBySubjectId(subjectId);
	}

	/**
	 * 根据科目Id 删除科目所有辅助核算项 
	 * 2017年11月22日 下午3:48:41
	 * @param subjectId
	 * @return
	 */
	@Override
	@Transactional
	public int deleteBySubjectId(String subjectId) {
		return dao.deleteBySubjectId(subjectId);
	}

}
