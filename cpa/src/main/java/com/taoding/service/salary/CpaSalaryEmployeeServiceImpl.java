package com.taoding.service.salary;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.DictUtils;
import com.taoding.common.utils.NextNoUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.domain.addition.CpaBank;
import com.taoding.domain.salary.CpaSalaryCompanyWelfare;
import com.taoding.domain.salary.CpaSalaryEmployee;
import com.taoding.domain.salary.CpaSalaryInfo;
import com.taoding.mapper.salary.CpaSalaryCompanyWelfareDao;
import com.taoding.mapper.salary.CpaSalaryEmployeeDao;
import com.taoding.mapper.salary.CpaSalaryInfoDao;
import com.taoding.service.addition.CpaBankService;

/**
 * 社保公积金员工表Service
 * @author csl
 * @version 2017-12-13
 */
@Service
@Transactional
public class CpaSalaryEmployeeServiceImpl extends DefaultCurdServiceImpl<CpaSalaryEmployeeDao, CpaSalaryEmployee> 
	implements CpaSalaryEmployeeService{

	@Autowired
	private CpaBankService cpaBankService;
	
	@Autowired
	private CpaSalaryInfoDao cpaSalaryInfoDao;
	
	@Autowired
	private CpaSalaryCompanyWelfareDao cpaSalaryCompanyWelfareDao;
	
	@Autowired
	private CpaSalaryCompanyWelfareService cpaSalaryCompanyWelfareService;
	
	@Autowired
	private CpaSalaryInfoService cpaSalaryInfoService;
	/**
	 * 薪酬中新增员工
	 * @param cpaSalaryEmployee
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public Object addEmployee(CpaSalaryEmployee cpaSalaryEmployee) {
		//新增cpaSalaryEmployee员工
		if (StringUtils.isEmpty(cpaSalaryEmployee.getCustomerId())) {
			throw new LogicException("企业不存在！");
		}
		if (cpaSalaryEmployee != null && StringUtils.isNoneEmpty(cpaSalaryEmployee.getEmployeeName()) 
				&& StringUtils.isNoneEmpty(cpaSalaryEmployee.getCustomerId())) {
			CpaSalaryEmployee employee = this.findByName(cpaSalaryEmployee);
			if (employee != null && StringUtils.isNotEmpty(employee.getEmployeeName())) {
				throw new LogicException("你所添加的员工的姓名已存在!");
			}
		}
		//根据客户id查找企业员工的最大编号
		cpaSalaryEmployee.setEmployeeNo(this.findMaxNoByInfoNo(cpaSalaryEmployee.getCustomerId()));
		this.save(cpaSalaryEmployee);
		
		//将新增的员工添加到CpaSalaryCompanyWelfare客户社保公积金信息中
		CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare = new CpaSalaryCompanyWelfare();
		cpaSalaryCompanyWelfare.setEmployeeId(cpaSalaryEmployee.getId());
		cpaSalaryCompanyWelfare.setCustomerId(cpaSalaryEmployee.getCustomerId());
		CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare2 = cpaSalaryCompanyWelfareDao.findByEmployeeId(cpaSalaryCompanyWelfare);
		if (cpaSalaryCompanyWelfare2 != null && cpaSalaryCompanyWelfare2.getEmployeeId().equals(cpaSalaryEmployee.getId())) {
			throw new LogicException("新增的员工已存在！请重新新增员工！");
		}
		cpaSalaryCompanyWelfare.setPeriod(DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(cpaSalaryEmployee.getCustomerId())));
		cpaSalaryCompanyWelfareService.save(cpaSalaryCompanyWelfare);
		
		//将新增的员工添加到CpaSalaryEmployee薪酬中
		CpaSalaryEmployee employee = this.findByName(cpaSalaryEmployee);
		CpaSalaryInfo cpaSalaryInfo = new CpaSalaryInfo();
		cpaSalaryInfo.setCustomerId(cpaSalaryEmployee.getCustomerId());
		if (employee != null && StringUtils.isNotEmpty(employee.getId())) {
			cpaSalaryInfo.setEmployeeId(employee.getId());
		}
		cpaSalaryInfo.setId(UUIDUtils.getUUid().replace("-", ""));
		//如果当前账期为空，则根据当前账期查询
		if (null == cpaSalaryInfo.getPeriod()) {
			//获取当前账期
			String currentPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(cpaSalaryEmployee.getCustomerId());
				Date period = DateUtils.parseDate(currentPeriod);
				cpaSalaryInfo.setPeriod(period);
		}
		this.preInsert(cpaSalaryInfo);
		int count = cpaSalaryInfoDao.insert(cpaSalaryInfo);
		if (count ==0) {
			return false;
		}
		return true;
	}

	/**
	 * 根据名字查找对象
	 * @param cpaSalaryEmployee
	 * @return
	 */
	public CpaSalaryEmployee findByName(CpaSalaryEmployee cpaSalaryEmployee){
		return dao.findByName(cpaSalaryEmployee);
	}
	
	/**
	 * 查询企业员工最大编号
	 * @param customerId
	 * @return
	 */
	public String findMaxNoByInfoNo(String customerId){
		String maxNo = dao.findMaxNoByInfoNo(customerId);
		String nextNo = "";
		if(StringUtils.isNotEmpty(maxNo)){
			nextNo = NextNoUtils.getNextNo(maxNo);
		}else{
			nextNo = "001";
		}
		return nextNo;
	}
	
	/**
	 * 查询企业所有的员工
	 * @param cpaSalaryEmployee
	 * @return
	 */
	public List<CpaSalaryEmployee> findSalaryEmployeeList(CpaSalaryEmployee cpaSalaryEmployee){
		return dao.findList(cpaSalaryEmployee);
	}

	
	/**
	 * 导入员工数据
	 * @param list
	 * @param customerId
	 * @return
	 */
	public Map<String,Object> importEmployeeData(List<CpaSalaryEmployee> list,String customerId){
		if (Collections3.isEmpty(list)) {
			throw new LogicException("导入数据为空！请重新导入");
		}
		 return checkEmployeeList(list,customerId);
	}
	
	/**
	 * 校验导入员工的信息列表
	 * @param list
	 * @param customerId
	 * @return
	 */
	public Map<String,Object> checkEmployeeList(List<CpaSalaryEmployee> list,String customerId){
		//用于保存保存成功的数据和失败的数据
		Map<String,Object> maps = new HashMap<String,Object>();
		//用于收集错误信息
		List<String> sbLists = new ArrayList<String>();
		//用于保存成功的数据
		List<CpaSalaryEmployee> newLists = new ArrayList<CpaSalaryEmployee>();
		int num=0;
		//用于保存数据库和新添加的员工姓名
		Map<String,CpaSalaryEmployee> employeeMap = new HashMap<String,CpaSalaryEmployee>();
		//获取数据库所有的员工信息
		CpaSalaryEmployee cpaSalaryEmployee = new CpaSalaryEmployee();
		cpaSalaryEmployee.setCustomerId(customerId);
		List<CpaSalaryEmployee> employeeList = dao.findList(cpaSalaryEmployee);
		//将数据库中的员工姓名添加到保存所有的员工姓名的map中
		for (CpaSalaryEmployee cpaSalaryEmployeeItem : employeeList) {
			employeeMap.put(cpaSalaryEmployeeItem.getEmployeeName(), cpaSalaryEmployeeItem);
		}
		//遍历导入的数据集合
		for (CpaSalaryEmployee employeeItem : list) {
			//根据银行名称查询银行id
			List<CpaBank> bankList = cpaBankService.getBankList(employeeItem.getBankName());
			num++;
			if (StringUtils.isEmpty(employeeItem.getEmployeeName())) {
				sbLists.add("第"+num+"行数据员工姓名不能为空！");
				continue ;
			}else if (employeeMap.containsKey(employeeItem.getEmployeeName())) {
				sbLists.add("第"+num+"行数据员工姓名重复！");
				continue;
			}else if(Collections3.isEmpty(bankList) || bankList.size() != 1){
				sbLists.add("第"+num+"行数据银行信息有误！");
				continue;
			}else if (StringUtils.isNoneBlank(employeeItem.getEmployeeName())) {
				if (!StringUtils.check(employeeItem.getEmployeeName(), CpaSalaryEmployee.REGEX_CHECK_EMPLOYEENAME)) {
					sbLists.add("第"+num+"行数据员工姓名不合法!");
					continue ;
				}
				newLists.add(employeeItem);
			}
			//将员工的姓名添加到map中
			employeeMap.put(employeeItem.getEmployeeName(), employeeItem);
			employeeItem.setBankId(bankList.get(0).getId());
			//将导入的信息保存到数据库中
			employeeItem.setEmployeeNo(this.findMaxNoByInfoNo(customerId));
			if (StringUtils.isNotBlank(employeeItem.getGender())) {
				employeeItem.setGender(DictUtils.getDictValue(employeeItem.getGender(), "sex", ""));
			}
			//将员工信息保存到员工表中
			employeeItem.setCustomerId(customerId);
			this.save(employeeItem);
			//将薪资的信息保存到薪资表中
			//根据员工姓名查找查找到对应的薪资信息
			employeeItem.setCustomerId(customerId);
			CpaSalaryEmployee cpaSalaryEmployee3 = dao.findByName(employeeItem);
			CpaSalaryInfo cpaSalaryInfo = new CpaSalaryInfo();
			cpaSalaryInfo.setEmployeeId(cpaSalaryEmployee3.getId());
			cpaSalaryInfo.setId(UUIDUtils.getUUid().replace("-", ""));
			if (employeeItem.getSalary()==null) {
				cpaSalaryInfo.setSalary(new BigDecimal(0));
			}else{
				cpaSalaryInfo.setSalary(new BigDecimal(employeeItem.getSalary()));
			}
			if (employeeItem.getAllowance() == null) {
				cpaSalaryInfo.setAllowance(new BigDecimal(0));
			}else{
				cpaSalaryInfo.setAllowance(new BigDecimal(employeeItem.getAllowance()));
			}
			cpaSalaryInfo.setCustomerId(customerId);
			cpaSalaryInfo.setPeriod(DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(cpaSalaryInfo.getCustomerId())));
			cpaSalaryInfoDao.insert(cpaSalaryInfo);
		}
		maps.put("errorMsg", sbLists);
		maps.put("successTotal", newLists.size());
		//重新刷新薪酬列表
		cpaSalaryInfoService.refreshSalaryInfo(customerId);
		return maps;
	}
	
}