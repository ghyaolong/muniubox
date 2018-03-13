package com.taoding.service.subject;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.subject.CpaSubject;
import com.taoding.mapper.subject.CpaSubjectDao;

/**
 * 科目详细
 * @author czy
 * 2017年11月17日 下午3:06:17
 */
public interface CpaSubjectService extends CrudService<CpaSubjectDao, CpaSubject>{

	/**
	 * 查询全部科目
	 * 2017年11月17日 下午3:24:57
	 * @param type
	 * @return
	 */
	public List<CpaSubject> findAllList(boolean type); 
	
	/**
	 * 根据父No查询子科目数据
	 * 2017年11月18日 上午10:44:55
	 * @param parentNo
	 * @param type
	 * @return
	 */
	public List<CpaSubject> findByParentNo(boolean type,String parentNo);
	
	
	/**
	 * 新增会计科目
	 * 2017年11月17日 下午4:19:22
	 * @param subject
	 */
	public Object insertCpaSubject(CpaSubject subject);
	
	/**
	 * 修改会计科目
	 * 2017年11月17日 下午5:00:27
	 * @param subject
	 * @return
	 */
	public Object updateCpaSubject(CpaSubject subject);
	
	
	/**
	 * 根据id删除会计科目
	 * 2017年11月18日 上午10:15:31
	 * @param id
	 * @return
	 */
	public Object deleteById(String id);
	
	/**
	 * 根据科目类型查询科目信息
	 * 2017年11月18日 下午2:00:20
	 * @param catalogId
	 * @return
	 */
	public List<CpaSubject> findAllBycatalogId(String catalogId);
}
