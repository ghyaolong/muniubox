
package com.taoding.service.assisting;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.NextNoUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.assisting.CpaAssistingExpenseType;
import com.taoding.domain.assisting.CpaAssistingProject;
import com.taoding.mapper.assisting.CpaAssistingProjectDao;

/**
 * 辅助核算模块项目信息表Service
 * @author csl
 * @version 2017-11-20
 */
@Service
@Transactional
public class CpaAssistingProjectServiceImpl extends DefaultCurdServiceImpl<CpaAssistingProjectDao, CpaAssistingProject>
	implements CpaAssistingProjectService{

	@Autowired
	private CpaAssistingExpenseTypeService cpaAssistingExpenseTypeService;
	
	/**
	 * 查询所有的辅助核算类型中的项目信息(传递参数isAll=true 表示不执行分页，isAll=false 表示执行分页)
	 * @param queryMap
	 * @return info
	 */
	public Object findAllList(Map<String, Object> queryMap){
		String isAll = queryMap.get("isAll").toString();
		if(StringUtils.isEmpty(isAll) || "false".equals(isAll)){
			//处理分页
			PageUtils.page(queryMap);
		}
		List<CpaAssistingProject> lists= dao.findAllList(queryMap);
		PageInfo<CpaAssistingProject> info = new PageInfo<CpaAssistingProject>(lists);	
		if("true".equals(isAll)){
			return lists ;
		}
		return info;
		}

	/**
	 * 根据辅助核算类型的项目的id新增项目信息
	 * @param queryMap
	 * @return
	 */
	@Override
	public Object insertProject(Map<String, Object> queryMap) {
		if (!(queryMap.containsKey("projectName") && queryMap.get("projectName") != null 
				&& StringUtils.isNotEmpty(String.valueOf(queryMap.get("projectName"))))) {
			throw new LogicException("项目名称不能为空！");
		}
		String projectName = queryMap.get("projectName").toString();
		String accountId = queryMap.get("accountId").toString();
		if (StringUtils.isNotEmpty(projectName)&& StringUtils.isNoneEmpty(accountId)) {
			CpaAssistingProject project = this.findByName(projectName,accountId);
			if (project != null && StringUtils.isNotEmpty(project.getProjectName())) {
				throw new LogicException("项目名称已存在！");
			}
		}
		if (StringUtils.isEmpty(queryMap.get("id").toString())) {
			throw new LogicException("辅助核算类型的对象为空！");
		}
		CpaAssistingProject project1 = new CpaAssistingProject();
		project1.setProjectName(projectName);
		project1.setAccountId(accountId);
		project1.setProjectNo(this.findMaxNoByInfoNo(accountId));
		//如果费用类型为空时，则拿到第一项费用类型
		if (!(queryMap.containsKey("expenseTypeId") && queryMap.get("expenseTypeId") != null 
				&& StringUtils.isNotEmpty(String.valueOf(queryMap.get("expenseTypeId"))))) {
			//获取数据库中的第一条费用类型
			List<CpaAssistingExpenseType> expenseTypeLists = cpaAssistingExpenseTypeService.findAllList();
			if (Collections3.isEmpty(expenseTypeLists)) {
				throw new LogicException("费用信息不存在！");
			}
			queryMap.put("expenseTypeId", expenseTypeLists.get(0).getId());
		}
		project1.setExpenseTypeId(queryMap.get("expenseTypeId").toString());
		project1.setRemarks(projectName);
		this.save(project1);
		return project1.getProjectNo();
	}

	/**
	 * 编辑项目信息
	 * @param cpaAssistingProject
	 * @return 
	 */
	@Override
	public Object updateProject(CpaAssistingProject cpaAssistingProject) {
		if (cpaAssistingProject!=null && StringUtils.isNoneEmpty(cpaAssistingProject.getId())&&StringUtils.isNoneEmpty(cpaAssistingProject.getAccountId())) {
			CpaAssistingProject cpaAssistingProject2 = this.findByName(cpaAssistingProject.getProjectName(),cpaAssistingProject.getAccountId());
			if (cpaAssistingProject2!=null && !cpaAssistingProject2.getId().equals(cpaAssistingProject.getId())) {
				throw new LogicException("辅助核算类型中的项目是新增条目，无法编辑!");
			}
			cpaAssistingProject.setRemarks(cpaAssistingProject.getProjectName());
			int count = dao.update(cpaAssistingProject);
			if (count > 0) {
				return true;
			}
			return false;
		}
		throw new LogicException("编辑对象不能为空!");
	}

	/**
	 * 根据项目id删除项目的信息
	 * @param id
	 * @return 
	 */
	@Override
	public Object deleteProject(String id) {
		if (dao.get(id)==null) {
			throw new LogicException("你要删除的对象的id不存在！");
		}
		int count = dao.delete(id);
		if (count > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 根据项目名称查找对象
	 * @param projectName
	 * @param accountId
	 * @return
	 */
	public CpaAssistingProject findByName(String projectName,String accountId){
		 return dao.findByName(projectName,accountId);
	}
	
	/**
	 * 查询最大编号
	 * @param accountId
	 * @return
	 */
	public String findMaxNoByInfoNo(String accountId){
		String maxNo = dao.findMaxNoByInfoNo(accountId);
		String nextNo = "";
		if(StringUtils.isNotEmpty(maxNo)){
			nextNo = NextNoUtils.getNextNo(maxNo);
		}else{
			nextNo = "001";
		}
		return nextNo;
	}
}