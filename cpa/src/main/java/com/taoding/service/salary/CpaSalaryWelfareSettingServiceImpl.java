
package com.taoding.service.salary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.StringUtil;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.domain.salary.CpaSalaryCompanyWelfare;
import com.taoding.domain.salary.CpaSalaryWelfareProject;
import com.taoding.domain.salary.CpaSalaryWelfareSetting;
import com.taoding.domain.salary.CpaSalaryWelfareTemplate;
import com.taoding.domain.salary.CpaSalaryWelfareType;
import com.taoding.mapper.salary.CpaSalaryCompanyWelfareDao;
import com.taoding.mapper.salary.CpaSalaryRoundRuleDao;
import com.taoding.mapper.salary.CpaSalaryWelfareProjectDao;
import com.taoding.mapper.salary.CpaSalaryWelfareSettingDao;
import com.taoding.mapper.salary.CpaSalaryWelfareTypeDao;

import lombok.extern.slf4j.Slf4j;

/**
 * 客户社保公积金配置Service
 * @author csl
 * @version 2017-11-24
 */
@Service
@Transactional
@Slf4j
public class CpaSalaryWelfareSettingServiceImpl extends DefaultCurdServiceImpl<CpaSalaryWelfareSettingDao, CpaSalaryWelfareSetting>
	implements CpaSalaryWelfareSettingService{
	
	@Autowired
	private CpaSalaryWelfareTemplateService salaryWelfareTemplateService;

	@Autowired
	private CpaSalaryWelfareProjectDao projectDao;
	
	@Autowired	
	private CpaSalaryCompanyWelfareDao welfareDao;
	
	@Autowired
	private CpaSalaryWelfareSettingService settingService;
	
	@Autowired
	private CpaSalaryCompanyWelfareService cpaSalaryCompanyWelfareService;
	
	@Autowired
	private CpaSalaryWelfareTypeDao cpaSalaryWelfareTypeDao;
	
	@Autowired
	private CpaSalaryRoundRuleDao cpaSalaryRoundRuleDao;
	
	BigDecimal yibai = new BigDecimal(100);
	/**
	 * 根据账薄id查找该企业所有的社保/公积金方案
	 * @param customerId
	 * @return
	 */
	public List<CpaSalaryWelfareProject> findProjectAndItemByCutomerId(String customerId){
		log.info("findProjectAndItemByCutomerId"+customerId+"=============================================");
		//先查询方案列表，如果方案列表为空，则初始化模板数据到方案列表中
		List<CpaSalaryWelfareSetting> itemTypeList = dao.findListByCustomerId(customerId);
		if (Collections3.isEmpty(itemTypeList)) {
			//需要初始化模板数据
			this.initSalaryProjectData(customerId);
		}
		 List<CpaSalaryWelfareProject> projectLists = projectDao.findProjectAndItemByCustomerId(customerId);
		 //遍历获取的集合
		 for (CpaSalaryWelfareProject cpaSalaryWelfareProject : projectLists) {
			 List<CpaSalaryWelfareSetting> items = cpaSalaryWelfareProject.getItems();
			 //遍历litem
			 for (CpaSalaryWelfareSetting settingItem : items) {
				 //设置方案的名称
				 settingItem.setProjectName(cpaSalaryWelfareProject.getProjectName());
				 //设置缴纳项目的名称
				 CpaSalaryWelfareType cpaSalaryWelfareType = cpaSalaryWelfareTypeDao.get(settingItem.getWelfareItemTypeId());
				 settingItem.setName(cpaSalaryWelfareType.getName());
				 //设置取整规则的名称
				 settingItem.setCompanyRoundRule(cpaSalaryRoundRuleDao.get(settingItem.getCompanyRoundRuleId()).getRuleName());
				 settingItem.setIndividualRoundRule(cpaSalaryRoundRuleDao.get(settingItem.getIndividualRoundRuleId()).getRuleName());
			}
		}
		 return projectLists;
	}
	
	/**
	 * 初始化社保公积金模板数据
	 * @param customerId
	 * @return
	 */
	public Object  initSalaryProjectData(String customerId){
			//如果社保/公积金方案为空，默认获取北京地区的模板方案
			String isDefault=CpaSalaryWelfareTemplate.IS_DEFAULT;
			//设置默认的社保/公积金方案名称为本地城镇
			String projectId = UUIDUtils.getUUid().replace("-", "");
			List<CpaSalaryWelfareSetting> settingList = new LinkedList<CpaSalaryWelfareSetting>();
			//获取所有的默认模板
			List<CpaSalaryWelfareTemplate> tempList = salaryWelfareTemplateService.findListByDefault(isDefault);
			for (CpaSalaryWelfareTemplate cswTemp : tempList) {
				if (StringUtil.isNotEmpty(cswTemp.getWelfareItemTypeId())) {
					CpaSalaryWelfareSetting salaryWelfareSetting = new CpaSalaryWelfareSetting();
					salaryWelfareSetting.setId(UUID.randomUUID().toString());
					salaryWelfareSetting.setCustomerId(customerId);
					salaryWelfareSetting.setProjectId(projectId);
					salaryWelfareSetting.setWelfareItemTypeId(cswTemp.getWelfareItemTypeId());
					salaryWelfareSetting.setCompanyRate(new BigDecimal(cswTemp.getCompanyRate()));
					salaryWelfareSetting.setCompanyFix(new BigDecimal(cswTemp.getCompanyFix()));
					salaryWelfareSetting.setCompanyRoundRuleId(cswTemp.getCompanyRoundRuleId());
					salaryWelfareSetting.setIndividualRate(new BigDecimal(cswTemp.getIndividualRate()));
					salaryWelfareSetting.setIndividualFix(new BigDecimal(cswTemp.getIndividualFix()));
					salaryWelfareSetting.setIndividualRoundRuleId(cswTemp.getIndividualRoundRuleId());
					salaryWelfareSetting.setRemarks(projectId);
					salaryWelfareSetting.setDelFlag(cswTemp.getDelFlag());
					salaryWelfareSetting.setNo(cswTemp.getNo());
					this.preInsert(salaryWelfareSetting);
					//添加模板数据到社保公积金缴纳项目中
					settingList.add(salaryWelfareSetting);
				}
			}
			//将添加有模板数据的集合添加到CpaSalaryWelfareSetting类中
			int count =dao.insertList(settingList);
			if (count==0) {
				throw new LogicException("模板数据添加失败！");
			}
			//并把对应的默认方案名称添加到方案表中
			CpaSalaryWelfareProject project = new CpaSalaryWelfareProject();
			project.setId(projectId);
			project.setProjectName("本地城镇");
			int count2 = projectDao.insert(project);
			if (count2==0) {
				throw new LogicException("添加方案名称初始化失败！");
			}
			return true;
	}

	/**
	 * 根据id删除企业社保公积金缴纳项目
	 * @param id
	 * @param customerId
	 * @return
	 */
	@Override
	public Object deleteById(String id) {
		//根据id查找社保公积金配置对象
		CpaSalaryWelfareSetting cpaSalaryWelfareSetting = dao.get(id);
		if (cpaSalaryWelfareSetting == null || StringUtils.isEmpty(cpaSalaryWelfareSetting.getProjectId()) 
				|| StringUtils.isEmpty(cpaSalaryWelfareSetting.getCustomerId())) {
			throw new LogicException("无法删除该对象！");
		}
		//获取该对象的方案id获取所有的缴纳项目列表
		List<CpaSalaryWelfareSetting> settingLists = dao.findListByName(cpaSalaryWelfareSetting.getProjectId());
		//根据projectId获取客户社保公积金列表
		CpaSalaryCompanyWelfare welfare = new CpaSalaryCompanyWelfare();
		welfare.setWelfareSettingId(cpaSalaryWelfareSetting.getProjectId());
		welfare.setCustomerId(cpaSalaryWelfareSetting.getCustomerId());
		//根据方案id获取客户的社保公积金信息列表
		List<CpaSalaryCompanyWelfare> welfarelists = welfareDao.findByWelfareId(welfare);
		if (welfarelists.size()>0) {
			throw new LogicException("该方案正在使用，无法删除！");
		}else if (settingLists.size()==1) {
			throw new LogicException("每个参保方案必须保留一个！");
		}
		int count=dao.deleteById(id);
		if (count>0) {
			return true;
		}
		throw new LogicException("删除失败");
	}

	/**
	 * 根据方案名称id删除方案
	 * @param projectId
	 * @param customerId
	 * @return
	 */
	@Override
	public Object deleteByName(String projectId,String customerId) {
		//获取企业去重后所有的方案名称id
		List<CpaSalaryWelfareSetting> nameLists = dao.findAllNameByCustomerId(customerId);
		//遍历所有的方案名称
		for (CpaSalaryWelfareSetting nameList1 : nameLists) {
			if (!nameList1.getProjectId().equals(projectId)) {
				continue;
			}
			//根据projectId获取客户社保公积金列表
			CpaSalaryCompanyWelfare welfare = new CpaSalaryCompanyWelfare();
			welfare.setWelfareSettingId(projectId);
			welfare.setCustomerId(customerId);
			List<CpaSalaryCompanyWelfare> welfarelists = welfareDao.findByWelfareId(welfare);
			//该企业的社保公积金方案如果被员工使用则不允许删除
			if(welfarelists.size()>0){
				throw new LogicException("该方案正在使用，无法删除！");
			}else if (nameLists.size()==1&&nameList1.getProjectId().equals(projectId)) {
				throw new LogicException("必须保留一个参保方案！");
			}else if(nameLists.size()>1&&nameList1.getProjectId().equals(projectId)){
				dao.deleteByName(projectId,customerId);
				projectDao.delete(projectId);
				return true;
			}
		}
		throw new LogicException("删除失败！");
	}

	/**
	 * 获取所有的方案名称
	 * @param 
	 * @return
	 */
	@Override
	public List<CpaSalaryWelfareSetting> findAllName() {
		return dao.findAllName();
	}

	/**
	 * 修改或者新增企业社保公积金方案
	 * @param
	 * @return
	 */
	@Override
	public Object saveProject(CpaSalaryWelfareSetting cpaSalaryWelfareSetting) {
		//获取前台传递数据集合
		List<CpaSalaryWelfareSetting> frontLists = cpaSalaryWelfareSetting.getLists();
		//从数据库中获取企业所有的社保公积金方案的id、方案名称id、缴纳项目
		List<CpaSalaryWelfareSetting> behindLists = dao.findListByCustomerId(cpaSalaryWelfareSetting.getCustomerId());
		//获取数据库中所有的社保公积金方案的名称
		List<CpaSalaryWelfareProject> projectNameLists = projectDao.findAllList(cpaSalaryWelfareSetting.getCustomerId());
		
		
		//数据库社保公积金方案的名称map
		Map<String, CpaSalaryWelfareProject> projectSchemaIdAndItemMap = new HashMap<String, CpaSalaryWelfareProject>();
		//社保公积金方案的id、方案名称id、缴纳项目CpaSalaryWelfareSetting类map
		Map<String, CpaSalaryWelfareSetting> itemIdAndItemMap = new HashMap<String, CpaSalaryWelfareSetting>();
		//用去存储需要删除的缴纳项目
		Set<String> frontIdSet = new HashSet<>();
		
		//遍历数据库中社保公积金方案的名称map
		for (CpaSalaryWelfareProject salaryWelfareProjectItem : projectNameLists) {
			projectSchemaIdAndItemMap.put(salaryWelfareProjectItem.getId(), salaryWelfareProjectItem);	
			projectSchemaIdAndItemMap.put(salaryWelfareProjectItem.getProjectName(), salaryWelfareProjectItem);
		}
		//遍历数据库中CpaSalaryWelfareSetting类map
		for (CpaSalaryWelfareSetting salaryWelfareSettingItem : behindLists) {
			//遍历数据库库中传递的CpaSalaryWelfareSetting实体的信息条目id存放到map中
			itemIdAndItemMap.put(salaryWelfareSettingItem.getId(), salaryWelfareSettingItem);
		}
		
		List<CpaSalaryWelfareSetting> toDelete = new ArrayList<CpaSalaryWelfareSetting>();
		List<CpaSalaryWelfareSetting> toUpdate = new ArrayList<CpaSalaryWelfareSetting>();
		List<CpaSalaryWelfareSetting> toInsert = new ArrayList<CpaSalaryWelfareSetting>();
		
		
		//遍历前台传递的数据集合
		for (CpaSalaryWelfareSetting frontItem : frontLists) {
			//如果数据库社保公积金方案的名称map包含有前台传递的id
			if (projectSchemaIdAndItemMap.containsKey(frontItem.getCpaSalaryWelfareProject().getId())) {
				//传递的方案名称和后台的数据相等的情况下，判断缴纳项目的配置信息的id是否与CpaSalaryWelfareSetting类缴纳项目配置信息的id相同
				if (itemIdAndItemMap.containsKey(frontItem.getId())) {
					//若方案的id相等的情况下，需要更新方案的名称
					projectDao.update(frontItem.getCpaSalaryWelfareProject());
					toUpdate.add(frontItem);
				} else {
					this.preInsert(frontItem);
					this.preUpdate(frontItem);
					frontItem.setProjectId(frontItem.getCpaSalaryWelfareProject().getId());
					frontItem.setCustomerId(cpaSalaryWelfareSetting.getCustomerId());
					//获取缴纳项目的no
					frontItem.setNo(cpaSalaryWelfareTypeDao.get(frontItem.getWelfareItemTypeId()).getNo());
					toInsert.add(frontItem);
					
				}
			} else {
				//将新的方案名称插入到CpaSalaryWelfareProject
				CpaSalaryWelfareProject project = new CpaSalaryWelfareProject();
				if (StringUtils.isEmpty(frontItem.getCpaSalaryWelfareProject().getProjectName())) {
					throw new LogicException("方案名称不能为空！");
				}else {
					//如果添加的项目名称与数据库重复，则抛出异常
					if (projectSchemaIdAndItemMap.containsKey(frontItem.getCpaSalaryWelfareProject().getProjectName())) {
						throw new LogicException("方案名称不能重复！");
					}
					project.setProjectName(frontItem.getCpaSalaryWelfareProject().getProjectName());
					this.insertProject(cpaSalaryWelfareSetting, project);
					//获取新增方案的id
					CpaSalaryWelfareProject projectByProjectName = projectDao.getProjectByProjectName(project);
					this.preInsert(frontItem);
					this.preUpdate(frontItem);
					frontItem.setProjectId(projectByProjectName.getId());
					//获取缴纳项目的no
					frontItem.setNo(cpaSalaryWelfareTypeDao.get(frontItem.getWelfareItemTypeId()).getNo());
					frontItem.setCustomerId(cpaSalaryWelfareSetting.getCustomerId());
					toInsert.add(frontItem);
				}
					
			}
			//将前台传递的所有缴纳项目的id存放在set集合中
			if (StringUtils.isEmpty(frontItem.getId())) {
				frontItem.setId(UUIDUtils.getUUid().replace("-", "").toString());
				frontIdSet.add(frontItem.getId());
			}else{
				frontIdSet.add(frontItem.getId());
			}
		}
		//遍历数据库中所有的缴纳项目的id
		for (CpaSalaryWelfareSetting salaryWelfareSettingItem : behindLists) {
			//如果前台出演地的缴纳项目的id集合中不包含后台的数据，则删除该项
			if (!frontIdSet.contains(salaryWelfareSettingItem.getId())) {
				toDelete.add(salaryWelfareSettingItem);
			}
		}
		//执行批量修改、插入、删除的方法
		if (!Collections3.isEmpty(toUpdate)) {
			log.info(JSON.toJSONString(toUpdate));
			dao.updateWelfare(toUpdate);
		}
		if (!Collections3.isEmpty(toInsert)) {
			log.info(JSON.toJSONString(toInsert));
			dao.insertWelfare(toInsert);
		}
		if (!Collections3.isEmpty(toDelete)) {
			log.info(JSON.toJSONString(toDelete));
			dao.deleteWelfare(toDelete);
		}
		//社保公积金方案保存成功后，必须刷新企业客户社保公积金信息表
		this.refreshWelfare(cpaSalaryWelfareSetting.getCustomerId());
		return true;
	}

	/**
	 * 根据方案名称获取企业的方案列表
	 * @param maps
	 * @return
	 */
	@Override
	public List<CpaSalaryWelfareSetting> findCustomerIdByWelfareId(CpaSalaryWelfareSetting cpaSalaryWelfareSetting) {
		return dao.findCustomerIdByWelfareId(cpaSalaryWelfareSetting);
	}
	
	/**
	 * 插入新的方案名称
	 * @param cpaSalaryWelfareSetting
	 * @param project
	 */
	public void insertProject(CpaSalaryWelfareSetting cpaSalaryWelfareSetting,CpaSalaryWelfareProject project){
		project.setId(UUIDUtils.getUUid().replace("-", "").toString());
		this.preInsert(project);
		project.setCustomerId(cpaSalaryWelfareSetting.getCustomerId());
		cpaSalaryWelfareSetting.setProjectId(project.getId());
		projectDao.insert(project);
	}
	
	/**
	 * 社保公积金方案保存成功后，必须刷新企业客户社保公积金信息表
	 * @param customerId
	 * @return
	 */
	public Object refreshWelfare(String customerId){
		//获取数据库中企业所有的客户社保公积金信息列表
		Map<String,Object> maps= new HashMap<String, Object>();
		maps.put("customerId", customerId);
		//获取当前账期
		String currentVoucherPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(customerId);
		maps.put("period", currentVoucherPeriod);
		//根据企业的当前账期查找企业的客户社保公积金信息列表
		List<CpaSalaryCompanyWelfare> welfareList = welfareDao.findAllByPage(maps);
		//判断数据库中企业所有的客户社保公积金信息列表的条数，如果条数等于0，则不执行计算的方法
		if (welfareList.size()==0) {
			return true;
		}
		//执行计算列表的方法
		//遍历数据库中企业所有的客户社保公积金信息列表
		for (CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare : welfareList) {
			String projectId = cpaSalaryCompanyWelfare.getWelfareSettingId();
			CpaSalaryWelfareSetting salaryWelfareSetting = new CpaSalaryWelfareSetting();
			salaryWelfareSetting.setCustomerId(customerId);
			salaryWelfareSetting.setProjectId(projectId);
			//根据projectId获取CpaSalaryWelfareSetting中该方案下的方案列表
			List<CpaSalaryWelfareSetting> lists = settingService.findCustomerIdByWelfareId(salaryWelfareSetting);
			//遍历所有的企业客户社保公积金方案列表
			for (CpaSalaryWelfareSetting cpaSalaryWelfareSetting1 : lists) {	
				//如果welfare_item_type_id为医疗时
				if (StringUtils.isNoneEmpty(cpaSalaryWelfareSetting1.getWelfareItemTypeId()) 
						&& cpaSalaryWelfareSetting1.getWelfareItemTypeId().equals(CpaSalaryWelfareProject.YILIAO)) {
					//执行获取医疗的数据
					cpaSalaryCompanyWelfareService.getYiliaoData(cpaSalaryCompanyWelfare, cpaSalaryWelfareSetting1);
				}
				//如果welfare_item_type_id为养老时
				if (StringUtils.isNoneEmpty(cpaSalaryWelfareSetting1.getWelfareItemTypeId()) 
						&& cpaSalaryWelfareSetting1.getWelfareItemTypeId().equals(CpaSalaryWelfareProject.YANGLAO)){
					//执行获取养老的数据
					cpaSalaryCompanyWelfareService.getYanglaoData(cpaSalaryCompanyWelfare, cpaSalaryWelfareSetting1);
				}
				//如果welfare_item_type_id为失业时
				if (StringUtils.isNoneEmpty(cpaSalaryWelfareSetting1.getWelfareItemTypeId()) 
						&& cpaSalaryWelfareSetting1.getWelfareItemTypeId().equals(CpaSalaryWelfareProject.SHIYE)){
					//执行获取失业的数据
					cpaSalaryCompanyWelfareService.getShiyeData(cpaSalaryCompanyWelfare, cpaSalaryWelfareSetting1);
				}
				//如果welfare_item_type_id为生育时
				if (StringUtils.isNoneEmpty(cpaSalaryWelfareSetting1.getWelfareItemTypeId()) 
						&& cpaSalaryWelfareSetting1.getWelfareItemTypeId().equals(CpaSalaryWelfareProject.SHENGYU)){
					//执行获取生育的数据
					cpaSalaryCompanyWelfareService.getShengyuData(cpaSalaryCompanyWelfare, cpaSalaryWelfareSetting1);
				}
				//如果welfare_item_type_id为工伤时
				if (StringUtils.isNoneEmpty(cpaSalaryWelfareSetting1.getWelfareItemTypeId())
						&& cpaSalaryWelfareSetting1.getWelfareItemTypeId().equals(CpaSalaryWelfareProject.GONGSHANG)){
					//执行获取工伤的数据
					cpaSalaryCompanyWelfareService.getGongshangData(cpaSalaryCompanyWelfare, cpaSalaryWelfareSetting1);
				}
				//如果welfare_item_type_id为公积金时
				if (StringUtils.isNoneEmpty(cpaSalaryWelfareSetting1.getWelfareItemTypeId()) 
						&& cpaSalaryWelfareSetting1.getWelfareItemTypeId().equals(CpaSalaryWelfareProject.GONGJIJIN)){
					//执行获取公积金的数据
					cpaSalaryCompanyWelfareService.getGongjijinData(cpaSalaryCompanyWelfare, cpaSalaryWelfareSetting1);
				}
				//如果welfare_item_type_id为大病时
				if (StringUtils.isNoneEmpty(cpaSalaryWelfareSetting1.getWelfareItemTypeId()) 
						&& cpaSalaryWelfareSetting1.getWelfareItemTypeId().equals(CpaSalaryWelfareProject.DABING)){
					//执行获取大病的数据
					cpaSalaryCompanyWelfareService.getDabingData(cpaSalaryCompanyWelfare, cpaSalaryWelfareSetting1);
				}
			}
			//计算每项的总和
			BigDecimal countCompany = cpaSalaryCompanyWelfare.getYiliaoCompany().add(cpaSalaryCompanyWelfare.getYanglaoCompany())
					.add(cpaSalaryCompanyWelfare.getShiyeCompany()).add(cpaSalaryCompanyWelfare.getShengyuCompany())
					.add(cpaSalaryCompanyWelfare.getGongshangCompany()).add(cpaSalaryCompanyWelfare.getGongjijinCompany())
					.add(cpaSalaryCompanyWelfare.getDabingCompany());
			BigDecimal countIndividual = cpaSalaryCompanyWelfare.getYiliaoIndividual().add(cpaSalaryCompanyWelfare.getYanglaoIndividual())
					.add(cpaSalaryCompanyWelfare.getShiyeIndividual()).add(cpaSalaryCompanyWelfare.getGongjijinIndividual());
			cpaSalaryCompanyWelfare.setCountCompany(countCompany);
			cpaSalaryCompanyWelfare.setCountIndividual(countIndividual);
			welfareDao.updateWelfare(cpaSalaryCompanyWelfare);
		}
		return true;
	}
	

	/**
	 * 根据客户id删除企业的社保公积金方案
	 * @param customerId
	 * @return
	 */
	public Object deleteProjectByCustomerId(String customerId){
		//初始化方案表
		Integer initFlag = this.findMaxInitFlag(customerId);
		CpaSalaryWelfareSetting setting = new CpaSalaryWelfareSetting();
		setting.setCustomerId(customerId);
		setting.setInitFlag(initFlag);
		Integer count = dao.initCpaSalaryWelfareSetting(setting);
		if(count>0){
			//初始化客户社保公积金信息
			CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare = new CpaSalaryCompanyWelfare();
			cpaSalaryCompanyWelfare.setInitFlag(initFlag);
			cpaSalaryCompanyWelfare.setCustomerId(customerId);
			welfareDao.initCpaSalaryCompanyWelfare(cpaSalaryCompanyWelfare);
			//初始化方案
			CpaSalaryWelfareProject project = new CpaSalaryWelfareProject();
			project.setCustomerId(customerId);
			project.setInitFlag(initFlag);
			projectDao.initCpaSalaryWelfareProject(project);
			return true;
		}
		throw new LogicException("社保公积金初始化失败！");
	}
	
	/**
	 * 根据客户id查询社保公公积金方案初始化标记的最大值
	 * @param customerId
	 * @return
	 */
	public Integer findMaxInitFlag(String customerId){
		Integer maxNo = dao.findMaxInitFlag(customerId);
		return maxNo+1;
	}


}