package com.taoding.service.subject;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.common.utils.UserUtils;
import com.taoding.domain.subject.CpaSubject;
import com.taoding.domain.user.User;
import com.taoding.mapper.subject.CpaSubjectDao;

/**
 * 科目详情
 * 
 * @author czy 2017年11月17日 下午3:08:46
 */
@Service
public class CpaSubjectServiceImpl extends
		DefaultCurdServiceImpl<CpaSubjectDao, CpaSubject> implements
		CpaSubjectService {

	/**
	 * 查询全部科目
	 * 2017年11月17日 下午3:24:57
	 * @param type
	 * @return
	 */
	@Override
	public List<CpaSubject> findAllList(boolean type) {
		return dao.findAllList(type);
	}
	
	/**
	 * 根据父No查询子科目数据
	 * 2017年11月18日 上午10:44:55
	 * @param type
	 * @param parentNo
	 * @return
	 */
	public List<CpaSubject> findByParentNo(boolean type ,String parentNo){
		return dao.findByParentNo(type,parentNo);
	}

	/**
	 * 新增会计科目
	 * 2017年11月17日 下午4:19:22
	 * @param cubject
	 */
	@Override
	@Transactional
	public Object insertCpaSubject(CpaSubject subject) {
		
		String subjectNo = subject.getSubjectNo() ;
		String subjectName = subject.getSubjectName() ;
		if(subjectNo != null && !"".equals(subjectNo.trim())){
			CpaSubject cpaSubject = dao.findBySubjectNo(subject.isType(), subjectNo);
			if(cpaSubject != null && StringUtils.isNotEmpty(cpaSubject.getId())){
				throw new LogicException("科目编号已存在");
			}
		}
		if(subjectName != null && !"".equals(subjectName.trim())){
			CpaSubject cpaSubject = dao.findBySubjectName(subject.isType(), subjectName, subject.getParent());
			if(cpaSubject != null && StringUtils.isNotEmpty(cpaSubject.getId())){
				throw new LogicException("科目名称已存在");
			}
		}
		
		if(!updateHashChild(subject, 1)){
			throw new LogicException("处理是否是子节点失败！");
		}
		
		subject.setId(UUIDUtils.getUUid());
		
		User createBy = new User();
		createBy.setId(UserUtils.getCurrentUserId());
		
		subject.setCreateBy(createBy);
		subject.setCreateDate(new Date());
		
		int count = dao.insert(subject);
		if(count > 0){
			return true ;
		}
		return false ;
	}

	
	/**
	 * 
	* @Description: TODO(更改是否有子节点) 
	* @param subject
	* @param operationType 1 add 2 update 3 delete 
	* @return boolean 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月16日
	 */
	private  boolean  updateHashChild(CpaSubject subject,int operationType){
		
		boolean flag = true;
		
		if(null != subject && 1 == operationType){
			if(!"0".equals(subject.getParent())){//非根节点，添加默认只能添加子节点 业务设定 
			
				CpaSubject parentSubject = dao.findBySubjectNo(subject.isType(), subject.getParent());
				if(null != parentSubject){
					parentSubject.setHashChild(true);
					flag = dao.update(parentSubject)>0;
				}
				//判断是否有下级
				List<CpaSubject> list = dao.findByParentNo(subject.isType(), subject.getSubjectNo());
				if(CollectionUtils.isNotEmpty(list)){
					subject.setHashChild(true);
				}else{
					subject.setHashChild(false);
				}
			}
		}else if(null != subject && 2 == operationType){//没有业务不处理 ，如果没有变动父节点 的业务 不做梳理
			flag = true;
		}else if(null != subject && 3 == operationType){//用于删除后
			List<CpaSubject> list = dao.findByParentNo(subject.isType(), subject.getParent());
			if(CollectionUtils.isEmpty(list)){
				
				CpaSubject parentSubject = dao.findBySubjectNo(subject.isType(), subject.getParent());
				if(null != parentSubject){
					parentSubject.setHashChild(false);
					flag = dao.update(parentSubject)>0;
				}
			}
		}else{
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * 修改会计科目
	 * 2017年11月17日 下午5:00:27
	 * @param subject
	 * @return
	 */
	@Override
	@Transactional
	public Object updateCpaSubject(CpaSubject subject) {
		
		String id = subject.getId();
		String subjectNo = subject.getSubjectNo() ;
		String subjectName = subject.getSubjectName() ;
		
		if(subjectNo != null && !"".equals(subjectNo.trim())){
			CpaSubject cpaSubject = dao.findBySubjectNo(subject.isType(), subjectNo);
			if(cpaSubject != null && !cpaSubject.getId().equals(id)){
				throw new LogicException("科目编号已存在");
			}
		}
		
		if(subjectName != null && !"".equals(subjectName.trim())){
			CpaSubject cpaSubject = dao.findBySubjectName(subject.isType(), subjectName, subject.getParent());
			if(cpaSubject != null && !cpaSubject.getId().equals(id)){
				throw new LogicException("科目名称已存在");
			}
		}
		
		int count = dao.update(subject);
		if(count > 0){
			return true ;
		}
		return false ;
	}

	/**
	 * 根据id删除会计科目
	 * 2017年11月18日 上午10:15:31
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public Object deleteById(String id) {
		CpaSubject subject = this.get(id);
		if(subject != null){
			List<CpaSubject> lists = this.findByParentNo(subject.isType(),subject.getSubjectNo());
			if(lists != null && lists.size() > 0){
				throw new LogicException("当前科目下存在子科目，不能删除");
			}
		}
		int count = dao.delete(id);
		if(count > 0){
			if(!updateHashChild(subject, 3)){
				throw new LogicException("处理是否是子节点失败！");
			}
			return true ;
		}
		return false ;
	}
	
	/**
	 * 根据科目类型查询科目信息
	 * 2017年11月18日 下午 2:01:31
	 * @param catalogId
	 */
	@Override
	public List<CpaSubject> findAllBycatalogId(String catalogId) {
		return dao.findAllBycatalogId(catalogId);
	}

}
