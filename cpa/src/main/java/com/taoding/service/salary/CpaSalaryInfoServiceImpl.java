package com.taoding.service.salary;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
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
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.DictUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.salary.CpaSalaryCompanyWelfare;
import com.taoding.domain.salary.CpaSalaryEmployee;
import com.taoding.domain.salary.CpaSalaryInfo;
import com.taoding.domain.salary.CpaSalaryTaxrate;
import com.taoding.mapper.salary.CpaSalaryCompanyWelfareDao;
import com.taoding.mapper.salary.CpaSalaryEmployeeDao;
import com.taoding.mapper.salary.CpaSalaryInfoDao;
import com.taoding.mapper.salary.CpaSalaryTaxrateDao;

/**
 * 员工薪酬Service
 * @author csl
 * @version 2017-12-13
 */
@Service
@Transactional
public class CpaSalaryInfoServiceImpl extends DefaultCurdServiceImpl<CpaSalaryInfoDao, CpaSalaryInfo>
	implements CpaSalaryInfoService{
	
	@Autowired
	private CpaSalaryWelfareSettingService cpaSalaryWelfareSettingService;
	
	@Autowired
	private CpaSalaryEmployeeDao cpaSalaryEmployeeDao;
	
	@Autowired
	private CpaSalaryTaxrateDao cpaSalaryTaxrateDao;
	
	@Autowired
	private CpaSalaryCompanyWelfareDao cpaSalaryCompanyWelfareDao;

	
	BigDecimal yibai = new BigDecimal(100);
	
	/**
	 * 查询薪酬列表+分页
	 * @param maps
	 * @return
	 */
	public PageInfo<CpaSalaryInfo> findAllByPage(Map<String,Object> maps){
		String customerId = maps.get("customerId").toString();
		//获取当前面账期
		if (StringUtils.isEmpty(customerId)) {
			throw new LogicException("查询的企业不存在！");
		}
		//获取当前账期
		Date currentPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(customerId)); 
		if (currentPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		if (null==maps.get("period")) {
			maps.put("period", currentPeriod);
		}
		String isAll = maps.get("isAll").toString();
		if(StringUtils.isEmpty(isAll) || "false".equals(isAll) || isAll == ""){
			//处理分页
			PageUtils.page(maps);
		}
		List<CpaSalaryInfo> cpaSalaryInfoLists = dao.findAllByPage(maps);
		PageInfo<CpaSalaryInfo> pageInfo = new PageInfo<CpaSalaryInfo>(cpaSalaryInfoLists);
		return pageInfo;
	}
	
	/**
	 * 根据薪资id编辑员工信息
	 * @param maps
	 * @return
	 */
	public Object updateEmployeeById(Map<String,Object> maps){
		if (StringUtils.isEmpty(maps.get("id").toString())) {
			throw new LogicException("编辑对象不存在！");
		}
		//根据薪资的id查询薪酬对象信息
		 CpaSalaryInfo cpaSalaryInfo = dao.get(maps.get("id").toString());
		if (cpaSalaryInfo == null && StringUtils.isEmpty(cpaSalaryInfo.getEmployeeId())) {
			throw new LogicException("查找的薪资对象不存在！");
		}
		//如果是历史账期则不允许修改
		Date currentPeriod =DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(cpaSalaryInfo.getCustomerId())) ;
		if (currentPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		if (cpaSalaryInfo.getPeriod().before(currentPeriod)) {
			throw new LogicException("该薪资属于历史账期，员工不允许编辑！");
		}
		//根据employeeId查找员工对象信息
		CpaSalaryEmployee cpaSalaryEmployee = cpaSalaryEmployeeDao.get(cpaSalaryInfo.getEmployeeId());
		if (cpaSalaryEmployee == null && StringUtils.isEmpty(cpaSalaryEmployee.getId())) {
			throw new LogicException("查找的员工对象不存在！");
		}
		cpaSalaryEmployee.setEmployeeName(maps.get("employeeName").toString());
		cpaSalaryEmployee.setGender(maps.get("gender").toString());
		cpaSalaryEmployee.setPhone(maps.get("phone").toString());
		cpaSalaryEmployee.setEmail(maps.get("email").toString());
		cpaSalaryEmployee.setHukou(maps.get("hukou").toString());
		cpaSalaryEmployee.setIdentity(maps.get("identity").toString());
		cpaSalaryEmployee.setOnBoardData(DateUtils.parseDate(maps.get("onBoarData").toString()));
		cpaSalaryEmployee.setStatus(maps.get("status").toString());
		cpaSalaryEmployee.setBankId(maps.get("bankId").toString());
		int count = cpaSalaryEmployeeDao.updateEmployee(cpaSalaryEmployee);
		if (count ==0 ) {
			return false;
		}
		return true;
	}

	
	/**
	 * 根据薪资id删除员工
	 * @param id
	 * @return
	 */
	public Object deleteEmployeeById(String id){
		if (StringUtils.isEmpty(id)) {
			throw new LogicException("删除对象不存在！");
		}
		//根据id查找薪资对象
		CpaSalaryInfo cpaSalaryInfo = dao.get(id);
		//如果是历史账期则不允许修改
		Date currentPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(cpaSalaryInfo.getCustomerId())) ;
		if (currentPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		if (cpaSalaryInfo.getPeriod().before(currentPeriod)) {
			throw new LogicException("该薪资属于历史账期，员工不允许删除！");
		}
		int count = cpaSalaryEmployeeDao.delete(cpaSalaryInfo.getEmployeeId());
		if (count == 0) {
			throw new LogicException("员工删除失败！");
		}
		int count1 = dao.delete(id);
		if (count1>0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 快速调整薪资列表信息
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Object updateSalaryInfo(CpaSalaryInfo cpaSalaryInfo){
		if (StringUtils.isEmpty(cpaSalaryInfo.getCustomerId())) {
			throw new LogicException("企业信息不存在！");
		}
		String customerId = cpaSalaryInfo.getCustomerId();
		//获取当前账期
		Date currentPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(customerId));
		if (currentPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		//如果当前账期为空时，则根据当前账期查询
		if (null==cpaSalaryInfo.getPeriod()) {
			cpaSalaryInfo.setPeriod(currentPeriod);
		}
		//如果当前账期不为空时，则要判断传入的账期与当前账期是否相等
		if (cpaSalaryInfo.getPeriod().before(currentPeriod)) {
			throw new LogicException("该账期已结账，不可调整！");
		}
		Map<String,Object> maps = new HashMap<String, Object>();
		maps.put("customerId", customerId);
		//获取传递的薪资的薪资信息
		List<CpaSalaryInfo> frontlists = cpaSalaryInfo.getLists();
		//遍历前端传递的数据集合
		for (CpaSalaryInfo frontItems : frontlists) {
			//根据前端传递的id查找对应的薪酬对象信息
			CpaSalaryInfo cpaSalaryInfo2 = dao.get(frontItems.getId());
			if (cpaSalaryInfo2 != null) {
				//将前端修改的属性值赋值给匹配对象
				cpaSalaryInfo2.setSalary(frontItems.getSalary());
				cpaSalaryInfo2.setAllowance(frontItems.getAllowance());
				cpaSalaryInfo2.setWithhold(frontItems.getWithhold());
				cpaSalaryInfo2.setWorkOvertime(frontItems.getWorkOvertime());
				//计算应发工资(基本工资+补助-扣款+加班)
				BigDecimal salaryCount = cpaSalaryInfo2.getSalary().add(cpaSalaryInfo2.getAllowance())
						.subtract(cpaSalaryInfo2.getWithhold()).add(cpaSalaryInfo2.getWorkOvertime());
				cpaSalaryInfo2.setSalary(salaryCount);
				
				//计算扣除合计（社保公积金的总和）
				BigDecimal withholdCount = cpaSalaryInfo2.getGongjijinIndividual().add(cpaSalaryInfo2.getShiyeIndividual())
						.add(cpaSalaryInfo2.getYanglaoIndividual()).add(cpaSalaryInfo2.getYiliaoIndividual());
				cpaSalaryInfo2.setWithholdCount(withholdCount);
				
				//计算调整本期个税额(免税所得+其他税前扣除+准予扣除捐赠+允许扣除税费+减免税额)
				BigDecimal adjustCurrentTax = cpaSalaryInfo2.getTaxFree().add(cpaSalaryInfo2.getOtherPretaxDeductions())
						.add(cpaSalaryInfo2.getDeductDonations()).add(cpaSalaryInfo2.getTaxDeductible())
						.add(cpaSalaryInfo2.getLessTaxPaid());
				//计算应纳税所得额(应发工资-扣除合计-个税起征点-调整本期个税额)
				//根据员工id查找对应的员工
				CpaSalaryEmployee cpaSalaryEmployee = cpaSalaryEmployeeDao.get(cpaSalaryInfo2.getEmployeeId());
				//获取员工的个税起征点
				if (cpaSalaryEmployee.getIndividualTaxLevy().intValue()==0) {
					//获取数据字典中的个税起征点的数据
					cpaSalaryEmployee.setIndividualTaxLevy(new BigDecimal(DictUtils.getDictLabel(cpaSalaryEmployee.getIndividualTaxLevy().toString(), "individual_tax_levy", "").toString()));
					//将个税起征点的数据保存到员工的信息表中
					cpaSalaryEmployeeDao.updateEmployee(cpaSalaryEmployee);
				}
				BigDecimal taxBasic = salaryCount.subtract(withholdCount).subtract(cpaSalaryEmployee.getIndividualTaxLevy())
						.subtract(adjustCurrentTax);
				
				//如果应发工资-调整本期税额-扣除合计-3500<=0时,不予征税
				if (taxBasic.doubleValue()<0 || taxBasic.doubleValue() == 0) {
					cpaSalaryInfo2.setTaxBasic(new BigDecimal(0));
					cpaSalaryInfo2.setIndividualTax(new BigDecimal(0));
					this.save(cpaSalaryInfo2);
					break;
				}
				cpaSalaryInfo2.setTaxBasic(taxBasic);
				//根据应纳税所得额查找对应的税率和扣除速算数
				CpaSalaryTaxrate cpaSalaryTaxrate = cpaSalaryTaxrateDao.findByTaxBasic(taxBasic);
				//计算代扣个税(应纳税所得额*税率-扣除速算数)
				BigDecimal individualTax = taxBasic.multiply(cpaSalaryTaxrate.getRate().divide(yibai, 2,BigDecimal.ROUND_HALF_UP))
						.subtract(cpaSalaryTaxrate.getQuickDeduction());
				cpaSalaryInfo2.setIndividualTax(individualTax);
				
				//计算实发工资(应发工资-扣除合计-代扣个税)
				BigDecimal finalPayAccount = salaryCount.subtract(withholdCount).subtract(individualTax);
				cpaSalaryInfo2.setFinalPayAccount(finalPayAccount);
				this.save(cpaSalaryInfo2);
			}
		}
		return true;
	}
	
	/**
	 * 修改企业的薪酬相关的信息后必须刷新企业的薪酬列表
	 * @param customerId
	 * @return
	 */
	public Object refreshSalaryInfo(String customerId){
		if (StringUtils.isEmpty(customerId)) {
			throw new LogicException("企业信息不存在！");
		}
		//重新计算薪酬列表所有的信息
		//根据customerId查询所有的薪资列表
		Map<String,Object> maps = new HashMap<String, Object>();
		//获取当前账期
		String currentVoucherPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(customerId);
		if (currentVoucherPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		maps.put("period", currentVoucherPeriod);
		maps.put("customerId", customerId);
		List<CpaSalaryInfo> cpaSalaryInfoLists = dao.findAllByPage(maps);
		//如果数据库中的薪酬的条数为0 时，则不执行计算的方法
		if (cpaSalaryInfoLists.size()==0) {
			return true;
		}
		//遍历所有的薪资
		for (CpaSalaryInfo cpaSalaryInfo : cpaSalaryInfoLists) {
			//计算应发工资(基本工资+补助-扣款+加班)
			BigDecimal salaryCount = cpaSalaryInfo.getSalary().add(cpaSalaryInfo.getAllowance())
					.subtract(cpaSalaryInfo.getWithhold()).add(cpaSalaryInfo.getWorkOvertime());
			cpaSalaryInfo.setSalary(salaryCount);
			//计算扣除合计（社保公积金的总和）
			BigDecimal withholdCount = cpaSalaryInfo.getGongjijinIndividual().add(cpaSalaryInfo.getShiyeIndividual())
					.add(cpaSalaryInfo.getYanglaoIndividual()).add(cpaSalaryInfo.getYiliaoIndividual());
			cpaSalaryInfo.setWithholdCount(withholdCount);
			
			//计算调整本期个税额(免税所得+其他税前扣除+准予扣除捐赠+允许扣除税费+减免税额)
			BigDecimal adjustCurrentTax = cpaSalaryInfo.getTaxFree().add(cpaSalaryInfo.getOtherPretaxDeductions())
					.add(cpaSalaryInfo.getDeductDonations()).add(cpaSalaryInfo.getTaxDeductible())
					.add(cpaSalaryInfo.getLessTaxPaid());
			//计算应纳税所得额(应发工资-扣除合计-个税起征点-调整本期个税额)
			//根据员工id查找对应的员工
			CpaSalaryEmployee cpaSalaryEmployee = cpaSalaryEmployeeDao.get(cpaSalaryInfo.getEmployeeId());
			//获取员工的个税起征点
			if (cpaSalaryEmployee.getIndividualTaxLevy().intValue()==0) {
				//获取数据字典中的个税起征点的数据
				cpaSalaryEmployee.setIndividualTaxLevy(new BigDecimal(DictUtils.getDictLabel(cpaSalaryEmployee.getIndividualTaxLevy().toString(), "individual_tax_levy", "").toString()));
				//将个税起征点的数据保存到员工的信息表中
				cpaSalaryEmployeeDao.updateEmployee(cpaSalaryEmployee);
			}
			BigDecimal taxBasic = salaryCount.subtract(withholdCount).subtract(cpaSalaryEmployee.getIndividualTaxLevy())
					.subtract(adjustCurrentTax);
			
			//如果应发工资-调整本期税额-扣除合计-3500<=0时,不予征税
			if (taxBasic.doubleValue()<0 || taxBasic.doubleValue() == 0) {
				cpaSalaryInfo.setTaxBasic(new BigDecimal(0));
				cpaSalaryInfo.setIndividualTax(new BigDecimal(0));
				this.save(cpaSalaryInfo);
				break;
			}
			cpaSalaryInfo.setTaxBasic(taxBasic);
			//根据应纳税所得额查找对应的税率和扣除速算数
			CpaSalaryTaxrate cpaSalaryTaxrate = cpaSalaryTaxrateDao.findByTaxBasic(taxBasic);
			//计算代扣个税(应纳税所得额*税率-扣除速算数)
			BigDecimal individualTax = taxBasic.multiply(cpaSalaryTaxrate.getRate().divide(yibai, 2,BigDecimal.ROUND_HALF_UP))
					.subtract(cpaSalaryTaxrate.getQuickDeduction());
			cpaSalaryInfo.setIndividualTax(individualTax);
			
			//计算实发工资(应发工资-扣除合计-代扣个税)
			BigDecimal finalPayAccount = salaryCount.subtract(withholdCount).subtract(individualTax);
			cpaSalaryInfo.setFinalPayAccount(finalPayAccount);
			this.save(cpaSalaryInfo);
		}
		return true;
	}
	
	/**
	 * 批量设置工资(传递薪资对象包含有基本工资，补助和customerId)
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Object batchUpdateSalary(CpaSalaryInfo cpaSalaryInfo){
		if (StringUtils.isEmpty(cpaSalaryInfo.getCustomerId())) {
			throw new LogicException("企业不存在！");
		}
		//获取当前账期
		Date currentVoucherPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(cpaSalaryInfo.getCustomerId()));
		if (currentVoucherPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		if (null==cpaSalaryInfo.getPeriod()) {
			cpaSalaryInfo.setPeriod(currentVoucherPeriod);
		}
		//如果当前账期不为空时，则要判断传入的账期与当前账期是否相等
		if (cpaSalaryInfo.getPeriod().before(currentVoucherPeriod)) {
			throw new LogicException("该账期已结账，不可调整！");
		}
		Map<String,Object> maps = new HashMap<String, Object>();
		maps.put("period", cpaSalaryInfo.getPeriod());
		maps.put("customerId", cpaSalaryInfo.getCustomerId());
		//根据customerId获取企业所有的薪资列表
		List<CpaSalaryInfo> cpaSalaryInfoLists = dao.findAllByPage(maps);
		//将后台传递的数据保存在map中
		Map<String,CpaSalaryInfo> cpaSalaryInfoMap = new HashMap<String,CpaSalaryInfo>();
		for (CpaSalaryInfo cpaSalaryInfoItem : cpaSalaryInfoLists) {
			cpaSalaryInfoMap.put(cpaSalaryInfoItem.getId(), cpaSalaryInfoItem);
		}
		//获取前台传递的批量修改的数据集合
		List<CpaSalaryInfo> frontLists = cpaSalaryInfo.getLists();
		//如果数据库中的薪酬信息的条数为0 时，则不执行计算的方法
		if (cpaSalaryInfoLists.size()==0 || frontLists.size() == 0) {
			throw new LogicException("选择的员工过多或者未选择员工！");
		}
		//遍历前端传递的数据集合
		for (CpaSalaryInfo frontListsItem : frontLists) {
			if (cpaSalaryInfoMap.containsKey(frontListsItem.getId())) {
				CpaSalaryInfo cpaSalaryInfo2 = dao.get(frontListsItem.getId());
				cpaSalaryInfo2.setSalary(frontListsItem.getSalary());
				cpaSalaryInfo2.setAllowance(frontListsItem.getAllowance());
				this.save(cpaSalaryInfo2);
			}
		}
		//批量修改工资后，刷新列表
		this.refreshSalaryInfo(cpaSalaryInfo.getCustomerId());
		return true;
	}
	
	/**
	 * 薪资批量设置社保公积金基数
	 * @param maps
	 * @return
	 */
	public Object batchUpdateBasic(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare){
		if (StringUtils.isEmpty(cpaSalaryCompanyWelfare.getCustomerId())) {
			throw new LogicException("企业不存在！");
		}
		//获取当前账期
		Date currentVoucherPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(cpaSalaryCompanyWelfare.getCustomerId()));
		if (currentVoucherPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		if (null==cpaSalaryCompanyWelfare.getPeriod()) {
			cpaSalaryCompanyWelfare.setPeriod(currentVoucherPeriod);
		}
		//如果当前账期不为空时，则要判断传入的账期与当前账期是否相等
		if (cpaSalaryCompanyWelfare.getPeriod().before(currentVoucherPeriod)) {
			throw new LogicException("该账期已结账，不可调整！");
		}
		Map<String,Object> maps = new HashMap<String, Object>();
		maps.put("period", cpaSalaryCompanyWelfare.getPeriod());
		maps.put("customerId", cpaSalaryCompanyWelfare.getCustomerId());
		//获取企业所有的客户社保公积金信息列表
		List<CpaSalaryCompanyWelfare> salaryCompanyWelfareLists = cpaSalaryCompanyWelfareDao.findAllByPage(maps);
		//将后台获取的数据全部保存在map集合中
		Map<String,CpaSalaryCompanyWelfare> salaryCompanyWelfareMap = new HashMap<String,CpaSalaryCompanyWelfare>();
		for (CpaSalaryCompanyWelfare cpaSalaryCompanyWelfareItem : salaryCompanyWelfareLists) {
			salaryCompanyWelfareMap.put(cpaSalaryCompanyWelfareItem.getId(), cpaSalaryCompanyWelfareItem);
		}
		//获取前端传递的批量数据集合
		List<CpaSalaryCompanyWelfare> frontLists = cpaSalaryCompanyWelfare.getList();
		//如果数据库中的客户社保公积金的条数为0 时或者前端传递的批量数据为0时，则不执行计算的方法
		if (salaryCompanyWelfareLists.size()==0 || frontLists.size()==0) {
			throw new LogicException("选择的员工过多或者未选择员工！");
		}
		//遍历企业前端传递的批量社保公积金数据集合
		for (CpaSalaryCompanyWelfare cpaSalaryCompanyWelfareItem : frontLists) {
			if (salaryCompanyWelfareMap.containsKey(cpaSalaryCompanyWelfareItem.getId())) {
				CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare2 = cpaSalaryCompanyWelfareDao.get(cpaSalaryCompanyWelfareItem.getId());
				cpaSalaryCompanyWelfare2.setDabingBasic(cpaSalaryCompanyWelfareItem.getDabingBasic());
				cpaSalaryCompanyWelfare2.setYiliaoBasic(cpaSalaryCompanyWelfareItem.getYiliaoBasic());
				cpaSalaryCompanyWelfare2.setYanglaoBasic(cpaSalaryCompanyWelfareItem.getYanglaoBasic());
				cpaSalaryCompanyWelfare2.setShengyuBasic(cpaSalaryCompanyWelfareItem.getShengyuBasic());
				cpaSalaryCompanyWelfare2.setGongshangBasic(cpaSalaryCompanyWelfareItem.getGongshangBasic());
				cpaSalaryCompanyWelfare2.setGongjijinBasic(cpaSalaryCompanyWelfareItem.getGongjijinBasic());
				cpaSalaryCompanyWelfare2.setShiyeBasic(cpaSalaryCompanyWelfareItem.getShiyeBasic());
				cpaSalaryCompanyWelfareDao.update(cpaSalaryCompanyWelfare2);
			}
		}
		//重新计算企业所有的社保公积金信息列表
		cpaSalaryWelfareSettingService.refreshWelfare(cpaSalaryCompanyWelfare.getCustomerId());
		//批量修改完社保公积金基数后，刷新薪酬列表
		this.refreshSalaryInfo(cpaSalaryCompanyWelfare.getCustomerId());
		return true;
	}

	/**
	 * 批量调整本期个税
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Object updateAdjustTax(CpaSalaryInfo cpaSalaryInfo){
		if (StringUtils.isEmpty(cpaSalaryInfo.getCustomerId())) {
			throw new LogicException("企业不存在！");
		}
		if (null==cpaSalaryInfo.getPeriod()) {
			//获取当前账期
			Date currentVoucherPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(cpaSalaryInfo.getCustomerId()));
			if (currentVoucherPeriod==null) {
				throw new LogicException("无法获取当前账期！");
			}
			cpaSalaryInfo.setPeriod(currentVoucherPeriod);
		}
		Map<String,Object> maps = new HashMap<String, Object>();
		maps.put("period", cpaSalaryInfo.getPeriod());
		maps.put("customerId", cpaSalaryInfo.getCustomerId());
		//根据customerId获取企业所有的薪资列表
		List<CpaSalaryInfo> cpaSalaryInfoLists = dao.findAllByPage(maps);
		//将后台传递的数据保存在map中
		Map<String,CpaSalaryInfo> cpaSalaryInfoMap = new HashMap<String,CpaSalaryInfo>();
		for (CpaSalaryInfo cpaSalaryInfoItem : cpaSalaryInfoLists) {
			cpaSalaryInfoMap.put(cpaSalaryInfoItem.getId(), cpaSalaryInfoItem);
		}
		//获取前台传递的批量修改的数据集合
		List<CpaSalaryInfo> frontLists = cpaSalaryInfo.getLists();
		//如果数据库中的薪酬信息的条数为0 时，则不执行计算的方法
		if (cpaSalaryInfoLists.size()==0 || frontLists.size() == 0 || frontLists.size()>1) {
			throw new LogicException("选择的员工过多或者未选择员工！");
		}
		//遍历前端传递的数据集合
		for (CpaSalaryInfo frontListsItem : frontLists) {
			if (cpaSalaryInfoMap.containsKey(frontListsItem.getId())) {
				CpaSalaryInfo cpaSalaryInfo2 = dao.get(frontListsItem.getId());
				cpaSalaryInfo2.setTaxFree(frontListsItem.getTaxFree());
				cpaSalaryInfo2.setOtherPretaxDeductions(frontListsItem.getOtherPretaxDeductions());
				cpaSalaryInfo2.setDeductDonations(frontListsItem.getDeductDonations());
				cpaSalaryInfo2.setTaxDeductible(frontListsItem.getTaxDeductible());
				cpaSalaryInfo2.setLessTaxPaid(frontListsItem.getLessTaxPaid());
				this.save(cpaSalaryInfo2);
			}
		}
		//批量修改工资后，刷新薪资列表
		this.refreshSalaryInfo(cpaSalaryInfo.getCustomerId());
		return true;
	}
	
	/**
	 * 批量删除员工和薪酬
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Object batchDeleteSalary(CpaSalaryInfo cpaSalaryInfo){
		if (StringUtils.isEmpty(cpaSalaryInfo.getCustomerId())) {
			throw new LogicException("企业不存在！");
		}
		//获取当前账期
		Date currentVoucherPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(cpaSalaryInfo.getCustomerId()));
		if (currentVoucherPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		if (null==cpaSalaryInfo.getPeriod()) {
			cpaSalaryInfo.setPeriod(currentVoucherPeriod);
		}
		//如果当前账期不为空时，则要判断传入的账期与当前账期是否相等
		if (cpaSalaryInfo.getPeriod().before(currentVoucherPeriod)) {
			throw new LogicException("该账期已结账，不可调整！");
		}
		//先删除员工再删除薪酬信息
		//获取批量传递的所有薪酬id
		List<CpaSalaryInfo> frontLists = cpaSalaryInfo.getLists();
		if (frontLists.size()==0) {
			throw new LogicException("选择的员工过多或者未选择员工！");
		}
		//遍历前端传递的批量数据
		for (CpaSalaryInfo cpaSalaryInfoItem : frontLists) {
			//根据薪酬的id查找薪酬对象信息
			CpaSalaryInfo cpaSalaryInfo2 = dao.get(cpaSalaryInfoItem.getId());
			//根据employeeId删除员工信息
			int count = cpaSalaryEmployeeDao.delete(cpaSalaryInfo2.getEmployeeId());
			if (count == 0 ) {
				throw new LogicException("删除员工失败！");
			}
			int count1 = dao.delete(cpaSalaryInfoItem.getId());
			if (count1 == 0) {
				throw new LogicException("删除员工信息失败！");
			}
		}
		return true;
	}
	
	/**
	 * 调整个税起征点
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Object updateIndividualTaxLevy(CpaSalaryInfo cpaSalaryInfo){
		if (StringUtils.isEmpty(cpaSalaryInfo.getCustomerId())) {
			throw new LogicException("企业不存在！");
		}
		//获取当前账期
		Date currentVoucherPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(cpaSalaryInfo.getCustomerId()));
		if (currentVoucherPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		if (null==cpaSalaryInfo.getPeriod()) {
			cpaSalaryInfo.setPeriod(currentVoucherPeriod);
		}
		//如果当前账期不为空时，则要判断传入的账期与当前账期是否相等
		if (cpaSalaryInfo.getPeriod().before(currentVoucherPeriod)) {
			throw new LogicException("该账期已结账，不可调整！");
		}
		//获取批量设置的个税额
		List<CpaSalaryInfo> cpaSalaryLists = cpaSalaryInfo.getLists();
		if (Collections3.isEmpty(cpaSalaryLists) && cpaSalaryLists.size()==0) {
			throw new LogicException("选择的员工过多或者未选择员工！");
		}
		for (CpaSalaryInfo cpaSalaryInfo2 : cpaSalaryLists) {
			CpaSalaryEmployee employee = new CpaSalaryEmployee();
			//根据薪酬的id获取薪酬对象
			if (StringUtils.isEmpty(cpaSalaryInfo2.getId())) {
				throw new LogicException("薪酬信息不存在！");
			}
			CpaSalaryInfo cpaSalaryInfo3 = dao.get(cpaSalaryInfo2.getId());
			if (cpaSalaryInfo3==null || StringUtils.isEmpty(cpaSalaryInfo3.getEmployeeId())) {
				throw new LogicException("员工信息有误！");
			}
			employee.setId(cpaSalaryInfo3.getEmployeeId());
			employee.setIndividualTaxLevy(cpaSalaryInfo2.getIndividualTaxLevy());
			cpaSalaryEmployeeDao.updateEmployee(employee);
		}
		//批量修改个税起征点后，刷新薪资列表
		this.refreshSalaryInfo(cpaSalaryInfo.getCustomerId());
		return true;
	}
	
	/**
	 * 根据客户id删除企业的薪酬信息（初始化薪酬信息）
	 * @param customerId
	 * @return
	 */
	public Object deleteSalaryInfoByCustomerId(String customerId){
		if (StringUtils.isEmpty(customerId)) {
			throw new LogicException("该企业的客户id为空！");
		}
		//初始化薪酬信息
		Integer initFlag = this.findMaxInitFlag(customerId);
		CpaSalaryInfo cpaSalaryInfo = new CpaSalaryInfo();
		cpaSalaryInfo.setCustomerId(customerId);
		cpaSalaryInfo.setInitFlag(initFlag);
		int count = dao.initCpaSalaryInfo(cpaSalaryInfo);
		if (count>0) {
			return true;
		}
		throw new LogicException("薪酬信息初始化失败！！");
	}
	
	/**
	 * 根据客户id查询薪酬信息初始化标记的最大值
	 * @param customerId
	 * @return
	 */
	public Integer findMaxInitFlag(String customerId){
		Integer maxNo = dao.findMaxInitFlag(customerId);
		if (maxNo == null ) {
			return CpaSalaryInfo.INITGLAG_ZERO;
		}
		return maxNo+1;
	}
	
	/**
	 * 获取计提工资
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Double getAccruedSalary(String bookId,String customerId){
		if (StringUtils.isEmpty(customerId)) {
			throw new LogicException("无法确定企业信息！");
		}
		Date currentPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(bookId)) ;
		if (currentPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		CpaSalaryInfo cpaSalaryInfo = new CpaSalaryInfo();
		cpaSalaryInfo.setCustomerId(customerId);
		cpaSalaryInfo.setPeriod(currentPeriod);
		return dao.getAccruedSalary(cpaSalaryInfo);
	}
	
	/**
	 * 获取代扣五险一金
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Double getWithHoldAccount(String bookId,String customerId){
		if (StringUtils.isEmpty(customerId)) {
			throw new LogicException("无法确定企业信息！");
		}
		Date currentPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(bookId));
		if (currentPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		CpaSalaryInfo cpaSalaryInfo = new CpaSalaryInfo();
		cpaSalaryInfo.setCustomerId(customerId);
		cpaSalaryInfo.setPeriod(currentPeriod);
		return dao.getWithHoldAccount(cpaSalaryInfo);
	}
	
	/**
	 * 获取代扣个税
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Double getIndividualTax(String bookId,String customerId){
		if (StringUtils.isEmpty(customerId)) {
			throw new LogicException("无法确定企业信息！");
		}
		Date currentPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(bookId)) ;
		if (currentPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		CpaSalaryInfo cpaSalaryInfo = new CpaSalaryInfo();
		cpaSalaryInfo.setCustomerId(customerId);
		cpaSalaryInfo.setPeriod(currentPeriod);
		return dao.getIndividualTax(cpaSalaryInfo);
	}
	
	/**
	 * 现金发放工资
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Double getCashSalary(String bookId,String customerId){
		if (StringUtils.isEmpty(customerId)) {
			throw new LogicException("无法确定企业信息！");
		}
		Date currentPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(bookId)) ;
		if (currentPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		CpaSalaryInfo cpaSalaryInfo = new CpaSalaryInfo();
		cpaSalaryInfo.setCustomerId(customerId);
		cpaSalaryInfo.setPeriod(currentPeriod);
		return dao.getCashSalary(cpaSalaryInfo);
	}
	
	/**
	 * 银行发放工资
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Double getBankSalary(String bookId,String customerId){
		if (StringUtils.isEmpty(customerId)) {
			throw new LogicException("无法确定企业信息！");
		}
		Date currentPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(bookId)) ;
		if (currentPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		//获取当前账期的上月
		Date lastMouth = this.getLastMouth(currentPeriod);
		CpaSalaryInfo cpaSalaryInfo = new CpaSalaryInfo();
		cpaSalaryInfo.setCustomerId(customerId);
		cpaSalaryInfo.setPeriod(lastMouth);
		return dao.getBankSalary(cpaSalaryInfo);
	}
	
	/**
	 * 获取当前账期的上月时间
	 * @param date
	 * @return
	 */
	public Date getLastMouth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}
	
	/**
	 * 获取企业的个人缴纳的社保公积金的分项信息
	 * @param customerId
	 * @return
	 */
	public Map<String,BigDecimal> getIndividualSocialSecurity(String bookId,String customerId){
		if (StringUtils.isEmpty(customerId)) {
			throw new LogicException("企业不存在！");
		}
		Date currentPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(bookId));
		if (currentPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		CpaSalaryInfo cpaSalaryInfo = new CpaSalaryInfo();
		cpaSalaryInfo.setCustomerId(customerId);
		cpaSalaryInfo.setPeriod(currentPeriod);
		//用于保存获取的社保公积金的分项信息
		Map<String,BigDecimal> maps = new HashMap<String,BigDecimal>();
		//获取职工代扣五险一金
		CpaSalaryCompanyWelfare individualSocialSecurity = dao.getIndividualSocialSecurity(cpaSalaryInfo);
		if(individualSocialSecurity != null && individualSocialSecurity.getYanglaoIndividual() != null ){
			maps.put("yanglaoIndividual", individualSocialSecurity.getYanglaoIndividual());
		}
		if(individualSocialSecurity != null && individualSocialSecurity.getYiliaoIndividual() != null ){
			maps.put("yiliaoIndividual", individualSocialSecurity.getYiliaoIndividual());
		}
		if(individualSocialSecurity != null && individualSocialSecurity.getShiyeIndividual() != null ){
			maps.put("shiyeIndividual", individualSocialSecurity.getShiyeIndividual());
		}
		if(individualSocialSecurity != null && individualSocialSecurity.getGongjijinIndividual() != null ){
			maps.put("gongjijinIndividual", individualSocialSecurity.getGongjijinIndividual());
		}
		return maps;
	}
}