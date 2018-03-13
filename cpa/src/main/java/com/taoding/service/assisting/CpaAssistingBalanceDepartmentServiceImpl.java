
package com.taoding.service.assisting;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.NextNoUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.assisting.CpaAssistingBalanceDepartment;
import com.taoding.domain.assisting.CpaAssistingExpenseType;
import com.taoding.mapper.assisting.CpaAssistingBalanceDepartmentDao;

/**
 * 辅助核算部门Service
 * @author csl
 * @version 2017-11-20
 */
@Service
@Transactional
public class CpaAssistingBalanceDepartmentServiceImpl 
	extends DefaultCurdServiceImpl<CpaAssistingBalanceDepartmentDao, CpaAssistingBalanceDepartment>
	implements CpaAssistingBalanceDepartmentService{
	
	@Autowired
	 private CpaAssistingExpenseTypeService cpaAssistingExpenseTypeService;
	/**
	 * 查询所有的辅助核算类型中的部门信息(传递参数isAll=true 表示不执行分页，isAll=false 表示执行分页)
	 */
	public Object findAllList(Map<String, Object> queryMap){
		String isAll = queryMap.get("isAll").toString();
		if(StringUtils.isEmpty(isAll) || "false".equals(isAll)){
			//处理分页
			PageUtils.page(queryMap);
		}
		List<CpaAssistingBalanceDepartment> lists= dao.findAllList(queryMap);
		PageInfo<CpaAssistingBalanceDepartment> info = new PageInfo<CpaAssistingBalanceDepartment>(lists);	
		if("true".equals(isAll)){
			return lists;
		}
		return info;
		
	}
	
	
	/**
	 * 新增辅助核算类型中的部门详细条目
	 * @param cpaCustomerAssistingInfo
	 * add csl
	 * 2017-11-18 15:54:11
	 */
	public Object insertDepart(Map<String, Object> queryMap){
		if (!(queryMap.containsKey("departmentName") && queryMap.get("departmentName") != null 
				&& StringUtils.isNotEmpty(String.valueOf(queryMap.get("departmentName"))))) {
			throw new LogicException("部门名称不能为空，请输入部门名称！");
		}
		if (StringUtils.isNotEmpty(queryMap.get("departmentName").toString())) {
			CpaAssistingBalanceDepartment department = this.findByName(queryMap.get("departmentName").toString(),queryMap.get("accountId").toString());
			if (department!=null && StringUtils.isNotEmpty(department.getDepartmentName())) {
				throw new LogicException("名称已存在！");
			}
		}
		if (StringUtils.isEmpty(queryMap.get("id").toString())) {
			throw new LogicException("辅助核算类型对象为空！");
		}
		CpaAssistingBalanceDepartment Department1 = new CpaAssistingBalanceDepartment();
		Department1.setAccountId(queryMap.get("accountId").toString());
		Department1.setDepartmentNo(this.findMaxNoByInfoNo(queryMap.get("accountId").toString()));
		Department1.setDepartmentName(queryMap.get("departmentName").toString());
		//如果费用类型为空时，则拿到第一项费用类型
		if (!(queryMap.containsKey("expenseTypeId") && queryMap.get("expenseTypeId") != null 
				&& StringUtils.isNotEmpty(String.valueOf(queryMap.get("expenseTypeId"))))) {
			//获取数据库中的第一条费用类型
			List<CpaAssistingExpenseType> expenseTypeLists = cpaAssistingExpenseTypeService.findAllList();
			queryMap.put("expenseTypeId", expenseTypeLists.get(0).getId());
		}
		Department1.setExpenseTypeId(queryMap.get("expenseTypeId").toString());
		this.save(Department1);
		return Department1.getDepartmentNo();
	}
	
	/**
	 * 根据名字查找
	 * @param name
	 * @return
	 */
	public CpaAssistingBalanceDepartment findByName(String departmentName,String accountId){
		return dao.findByName(departmentName,accountId);
	}
	
	/**
	 * 查询最大编号
	 * @param no
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
	
	/**
	 * 编辑部门
	 * @param department
	 * @return
	 */
	@Override
	public Object updateDepart(CpaAssistingBalanceDepartment department) {
		if (department!=null && StringUtils.isNoneEmpty(department.getId())&&StringUtils.isNoneEmpty(department.getAccountId())) {
			CpaAssistingBalanceDepartment department2 = this.findByName(department.getDepartmentName(),department.getAccountId());
			if (department2 != null && !department.getId().equals(department2.getId())) {
				throw new LogicException("辅助核算类型条目是新增条目，无法编辑！");
			}
			int count = dao.update(department);
			if (count>0) {
				return true;
			}
			return false;
		}
		throw new LogicException("编辑对象为空！");
	}


	/**
	 * 删除部门
	 * @param id
	 * @return
	 */
	@Override
	public Object deleteDepart(String id) {
		if (dao.get(id)==null) {
			throw new LogicException("你要删除的对象的id不存在！");
		}
		int count = dao.delete(id);
		if (count>0) {
			return true;
		}
		return false;
	}

	/**
	 * 查询所有的员工
	 * @param 
	 * @return
	 */
	@Override
	public List<CpaAssistingBalanceDepartment> findAllList() {
		return dao.findAllList();
	}
}