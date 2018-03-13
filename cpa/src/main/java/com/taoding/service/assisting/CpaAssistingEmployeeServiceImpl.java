
package com.taoding.service.assisting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.NextNoUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.addition.CpaBank;
import com.taoding.domain.assisting.CpaAssistingBalanceDepartment;
import com.taoding.domain.assisting.CpaAssistingEmployee;
import com.taoding.domain.assisting.CpaAssistingPosition;
import com.taoding.mapper.assisting.CpaAssistingBalanceDepartmentDao;
import com.taoding.mapper.assisting.CpaAssistingEmployeeDao;
import com.taoding.mapper.assisting.CpaAssistingPositionDao;
import com.taoding.service.addition.CpaBankService;

/**
 * 辅助核算模块员工Service
 * @author csl
 * @version 2017-11-20
 */
@Service
@Transactional
public class CpaAssistingEmployeeServiceImpl extends DefaultCurdServiceImpl<CpaAssistingEmployeeDao, CpaAssistingEmployee> 
		implements CpaAssistingEmployeeService{

	@Autowired
	private CpaAssistingBalanceDepartmentDao  departmentDao; 

	@Autowired
	private CpaAssistingPositionDao positionDao;
	
	@Autowired
	private CpaBankService cpaBankService;
	/**
	 * 查询员工的列表(传递参数isAll=true 表示不执行分页，isAll=false 表示执行分页)
	 */
	public Object findAllList(Map<String, Object> queryMap){
		String isAll = queryMap.get("isAll").toString();
		if(StringUtils.isEmpty(isAll) || "false".equals(isAll)){
			//处理分页
			PageUtils.page(queryMap);
		}
		List<CpaAssistingEmployee> lists= dao.findAllList(queryMap);
		PageInfo<CpaAssistingEmployee> info = new PageInfo<CpaAssistingEmployee>(lists);
		if("true".equals(isAll)){
			return lists ;
		}
		return info;
	}
	
	/**
	 * 新增辅助核算类型中的部门详细条目
	 * @param cpaCustomerAssistingInfo
	 * add csl
	 * 2017-11-18 15:54:11
	 */
	public Object insertEmployee(Map<String, Object> queryMap){
		if (!(queryMap.containsKey("name") && queryMap.get("name") != null 
				&& StringUtils.isNotEmpty(String.valueOf(queryMap.get("name"))))) {
			throw new LogicException("部门名称不能为空！");
		}
		String name = queryMap.get("name").toString();
		String accountId = queryMap.get("accountId").toString();
		if (StringUtils.isNotEmpty(name)&& StringUtils.isNotEmpty(accountId)) {
			CpaAssistingEmployee employee = this.findByName(name,accountId);
			if (employee != null && StringUtils.isNotEmpty(employee.getName())) {
				throw new LogicException("名称已存在！");
			}
		}
		if (StringUtils.isEmpty(queryMap.get("id").toString())) {
			throw new LogicException("辅助核算类型对象为空！");
		}
		CpaAssistingEmployee employee1 = new CpaAssistingEmployee();
		employee1.setName(name);
		employee1.setAccountId(accountId);
		employee1.setEmployeeNo(this.findMaxNoByInfoNo(accountId));
		if (queryMap.containsKey("gender") && queryMap.get("gender") != null) {
			employee1.setGender(queryMap.get("gender").toString());
		}
		//如果插入的部门信息为空时，则获取部门的第一条信息
		if (!(queryMap.containsKey("departmentId") && queryMap.get("departmentId") != null 
				&& StringUtils.isNotEmpty(String.valueOf(queryMap.get("departmentId"))))) {
			//查询企业所有的部门信息
			List<CpaAssistingBalanceDepartment> departmentLists = departmentDao.findList(accountId);
			if (Collections3.isEmpty(departmentLists)) {
				throw new LogicException("部门信息不存在！");
			}
			queryMap.put("departmentId", departmentLists.get(0).getId());
		}
		employee1.setDepartmentId(queryMap.get("departmentId").toString());
		//若果职位信息为空时，则获取企业的第一条职位信息
		if ((!(queryMap.containsKey("positionId") && queryMap.get("positionId") != null 
				&& StringUtils.isNotEmpty(String.valueOf(queryMap.get("positionId")))))) {
			//查询企业所有的职位信息
			List<CpaAssistingPosition> positionLists = positionDao.findList(accountId);
			if (Collections3.isEmpty(positionLists)) {
				throw new LogicException("职位信息不存在！");
			}
			queryMap.put("positionId", positionLists.get(0).getId());
		}
		employee1.setPositionId(queryMap.get("positionId").toString());
		if (queryMap.containsKey("phone") && queryMap.get("phone") != null) {
			employee1.setPhone(queryMap.get("phone").toString());
		}
		if (queryMap.containsKey("email") && queryMap.get("email") != null) {
			employee1.setEmail(queryMap.get("email").toString());
		}
		if (queryMap.containsKey("hukou") && queryMap.get("hukou") != null) {
			employee1.setHukou(queryMap.get("hukou").toString());
		}
		if (queryMap.containsKey("identity") && queryMap.get("identity") != null) {
			employee1.setIdentity(queryMap.get("identity").toString());
		}
		if (queryMap.containsKey("status") && queryMap.get("status") != null) {
			employee1.setStatus(queryMap.get("status").toString());
		}
		if (queryMap.containsKey("onBoardData") && queryMap.get("onBoardData") != null) {
			employee1.setOnBoardData(DateUtils.parseDate(queryMap.get("onBoardData").toString()));
		}
		if (!(queryMap.containsKey("goodsId") && queryMap.get("goodsId") != null 
				&& StringUtils.isNotEmpty(String.valueOf(queryMap.get("goodsId"))))) {
			//获取银行的信息列表
			List<CpaBank> bankLists = cpaBankService.getBankList(null);
			queryMap.put("bankId", bankLists.get(0).getId());
		}
		employee1.setBankId(queryMap.get("bankId").toString());
		if (queryMap.containsKey("creditCard") && queryMap.get("creditCard") != null) {
			employee1.setCreditCard(queryMap.get("creditCard").toString());
		}
		employee1.setRemarks(name);
		this.save(employee1);
		return employee1.getEmployeeNo();
	}
	
	/**
	 * 根据名字查找
	 * @param name
	 * @param accountId
	 * @return
	 */
	public CpaAssistingEmployee findByName(String name,String accountId){
		return dao.findByName(name,accountId);
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
	
	/**
	 * 编辑员工
	 * @param employee
	 * @return
	 */
	@Override
	public Object updateEmployee(CpaAssistingEmployee employee) {
		if (StringUtils.isEmpty(employee.getId())) {
			throw new LogicException("id不能为空！");
		}
		CpaAssistingBalanceDepartment cpaAssistingBalanceDepartment = new CpaAssistingBalanceDepartment();
		if (StringUtils.isEmpty(employee.getAccountId())) {
			throw new LogicException("账薄id不能为空！");
		}
		cpaAssistingBalanceDepartment.setAccountId(employee.getAccountId());
		List<CpaAssistingBalanceDepartment> departmentList = departmentDao.findList(cpaAssistingBalanceDepartment);
		Map<String,CpaAssistingBalanceDepartment> maps = new HashMap<String,CpaAssistingBalanceDepartment>();
		for (CpaAssistingBalanceDepartment departments : departmentList) {
			maps.put(departments.getId(), departments);
		}
		if (!maps.containsKey(employee.getDepartmentId())) {
			throw new LogicException("部门id不存在！");
		}
		CpaAssistingPosition cpaAssistingPosition = new CpaAssistingPosition();
		if (StringUtils.isEmpty(employee.getAccountId())) {
			throw new LogicException("账薄id不能为空！");
		}
		cpaAssistingPosition.setAccountId(employee.getAccountId());
		List<CpaAssistingPosition> positionLists = positionDao.findList(cpaAssistingPosition);
		Map<String,CpaAssistingPosition> positionMaps = new HashMap<String,CpaAssistingPosition>();
		for (CpaAssistingPosition positions : positionLists) {
			positionMaps.put(positions.getId(), positions);
		}
		if (!positionMaps.containsKey(employee.getPositionId())) {
			throw new LogicException("职位id不存在！");
		}
		employee.setOnBoardData(DateUtils.parseDate(employee.getOnBoardData()));
		employee.setEmployeeNo(this.findMaxNoByInfoNo(employee.getAccountId()));
		if (employee!=null && StringUtils.isNoneEmpty(employee.getId()) && StringUtils.isNoneEmpty(employee.getAccountId())) {
			CpaAssistingEmployee employee2 = this.findByName(employee.getName(),employee.getAccountId());
			if (employee2!=null && !employee2.getId().equals(employee.getId())) {
				throw new LogicException("辅助核算类型条目是新增条目，无法编辑！");
			}
			int count = dao.update(employee);
			if (count>0) {
				return true;
			}
			return false;
		}
		throw new LogicException("编辑对象为空！");
	}


	/**
	 * 删除员工
	 * @param id
	 * @return
	 */
	@Override
	public Object deleteEmployee(String id) {
		if (dao.get(id)==null) {
			throw new LogicException("你要删除的对象的id不存在！");
		}
		int count = dao.delete(id);
		if (count>0) {
			return true;
		}
		return false;
	}
	
}