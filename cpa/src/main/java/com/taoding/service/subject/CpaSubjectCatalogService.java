package com.taoding.service.subject;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.subject.CpaSubjectCatalog;
import com.taoding.mapper.subject.CpaSubjectCatalogDao;


/**
 * 科目类型
 * @author czy
 * 2017年11月17日 下午1:58:20
 */

public interface CpaSubjectCatalogService extends CrudService<CpaSubjectCatalogDao, CpaSubjectCatalog>{

	/**
	 * 查询所有科目类型
	 * 2017年11月17日 下午2:40:54
	 * @return
	 */
	public List<CpaSubjectCatalog> findAllList();
	
	/**
	 * 根据名称查询科目
	 * 2017年11月18日 下午2:15:03
	 * @param name
	 * @return
	 */
	public CpaSubjectCatalog findByName(String name);
	
	/**
	 * 新增科目类型
	 * 2017年11月18日 下午2:17:21
	 * @param cpaSubjectCatalog
	 * @return
	 */
	public Object insertCpaSubjectCatalog(CpaSubjectCatalog cpaSubjectCatalog);
	
	/**
	 * 修改科目类型
	 * 2017年11月18日 下午2:16:01
	 * @param cpaSubjectCatalog
	 * @return
	 */
	public Object updateCpaSubjectCatalog(CpaSubjectCatalog cpaSubjectCatalog);
	
	/**
	 * 根据id删除科目类型
	 * 2017年11月18日 下午2:31:05
	 * @param type
	 * @param id
	 * @return
	 */
	public Object deleteById(String id);

}
