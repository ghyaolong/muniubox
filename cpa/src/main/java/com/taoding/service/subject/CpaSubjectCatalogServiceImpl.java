package com.taoding.service.subject;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.subject.CpaSubject;
import com.taoding.domain.subject.CpaSubjectCatalog;
import com.taoding.mapper.subject.CpaSubjectCatalogDao;


/**
 * 科目类型
 * @author czy
 * 2017年11月17日 下午2:01:49
 */

@Service
public class CpaSubjectCatalogServiceImpl extends
		DefaultCurdServiceImpl<CpaSubjectCatalogDao, CpaSubjectCatalog>
		implements CpaSubjectCatalogService {

	@Autowired
	private CpaSubjectService subjectService ; 
	
	/**
	 * 查询所有科目类型
	 * 2017年11月17日 下午2:40:54
	 * @return
	 */
	@Override
	public List<CpaSubjectCatalog> findAllList() {
		return dao.findAllList();
	}

	/**
	 * 根据名称查询科目
	 * 2017年11月18日 下午2:15:03
	 * @param name
	 * @return
	 */
	@Override
	public CpaSubjectCatalog findByName(String name) {
		return dao.findByName(name);
	}
	
	/**
	 * 新增科目类型
	 * 2017年11月18日 下午2:17:21
	 * @param cpaSubjectCatalog
	 * @return
	 */
	@Override
	@Transactional
	public Object insertCpaSubjectCatalog(CpaSubjectCatalog cpaSubjectCatalog) {
		
		String name = cpaSubjectCatalog.getName() ;
		if(cpaSubjectCatalog != null && StringUtils.isNotEmpty(name)){
			CpaSubjectCatalog cpaSubjectCatalog2 = this.findByName(name);
			if(cpaSubjectCatalog2 != null && StringUtils.isNotEmpty(cpaSubjectCatalog2.getName())){
				throw new LogicException("科目类型已经存在");
			}
		}
		this.save(cpaSubjectCatalog);
		return true;
	}

	/**
	 * 修改科目类型
	 * 2017年11月18日 下午2:16:01
	 * @param cpaSubjectCatalog
	 * @return
	 */
	@Override
	@Transactional
	public Object updateCpaSubjectCatalog(CpaSubjectCatalog cpaSubjectCatalog) {
		if(cpaSubjectCatalog != null && StringUtils.isNotEmpty(cpaSubjectCatalog.getId())){
			CpaSubjectCatalog cpaSubjectCatalog2 = this.findByName(cpaSubjectCatalog.getName());
			if(cpaSubjectCatalog2 != null && !cpaSubjectCatalog.getId().equals(cpaSubjectCatalog2.getId())){
				throw new LogicException("科目类型已经存在");
			}
		}
		int count = dao.update(cpaSubjectCatalog);
		if(count > 0){
			return true;
		}
		return false;
	}

	/**
	 * 根据id删除科目类型
	 * 2017年11月18日 下午2:31:05
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public Object deleteById(String id) {
		
		List<CpaSubject> lists = subjectService.findAllBycatalogId(id);
		if(lists != null && lists.size() > 0){
			throw new LogicException("当前科目类型下存在子科目，不能删除");
		}
		int count = dao.delete(id);
		if(count > 0){
			return true ;
		}
		return false;
	}
	
}
