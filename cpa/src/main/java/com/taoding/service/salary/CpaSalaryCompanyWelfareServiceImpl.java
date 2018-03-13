
package com.taoding.service.salary;

import java.math.BigDecimal;
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
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.common.utils.RoundUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.salary.CpaSalaryCompanyWelfare;
import com.taoding.domain.salary.CpaSalaryRoundRule;
import com.taoding.domain.salary.CpaSalaryWelfareProject;
import com.taoding.domain.salary.CpaSalaryWelfareSetting;
import com.taoding.mapper.salary.CpaSalaryCompanyWelfareDao;

/**
 * 客户员工社保公积金表Service
 * @author csl
 * @version 2017-11-24
 */
@Service
@Transactional
public class CpaSalaryCompanyWelfareServiceImpl extends DefaultCurdServiceImpl<CpaSalaryCompanyWelfareDao, CpaSalaryCompanyWelfare>
	implements CpaSalaryCompanyWelfareService{

	@Autowired
	private CpaSalaryWelfareSettingService settingService;
	
	CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare = new CpaSalaryCompanyWelfare();
	BigDecimal yibai = new BigDecimal(100);
	/**
	 * 查询所有的客户社保公积金信息列表
	 * @param maps
	 * @return
	 */
	@Override
	public PageInfo<CpaSalaryCompanyWelfare> findWelfare(Map<String, Object> maps) {
		String isAll = maps.get("isAll").toString();
		if(StringUtils.isEmpty(isAll) || "false".equals(isAll)){
			//处理分页
			PageUtils.page(maps);
		}
		List<CpaSalaryCompanyWelfare> lists = dao.findAllByPage(maps);
		PageInfo<CpaSalaryCompanyWelfare> pageInfo = new PageInfo<CpaSalaryCompanyWelfare>(lists);
		return pageInfo;
	}

	/**
	 * 快速调整客户社保公积金详情（必须传入customerId和客户社保公积金条目的id）
	 * @param companyWelfare
	 * @return
	 * @throws  
	 */
	@Override
	public Object saveWelfare(CpaSalaryCompanyWelfare companyWelfare){
		//根据welfareSettingId查找企业的社保公积金方案
		CpaSalaryWelfareSetting CpaSalaryWelfareSetting = new CpaSalaryWelfareSetting();
		CpaSalaryWelfareSetting.setCustomerId(companyWelfare.getList().get(0).getCustomerId());
		//获取传递的所有对象的集合
		List<CpaSalaryCompanyWelfare> list = companyWelfare.getList();
		//遍历传递的集合对象
		for (CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare1 : list) {
			//根据id获取预修改的对象
			if (StringUtils.isEmpty(cpaSalaryCompanyWelfare1.getId())) {
				throw new LogicException("查询的对象不存在！");
			}
			cpaSalaryCompanyWelfare.setId(cpaSalaryCompanyWelfare1.getId());
			if (StringUtils.isEmpty(cpaSalaryCompanyWelfare1.getWelfareSettingId())) {
				throw new LogicException("方案名称不存在！！");
			}
			String projectId = cpaSalaryCompanyWelfare1.getWelfareSettingId();
			CpaSalaryWelfareSetting.setProjectId(projectId);
			cpaSalaryCompanyWelfare.setPeriod(companyWelfare.getPeriod());
			//根据projectId获取CpaSalaryWelfareSetting中该方案下的方案列表
			List<CpaSalaryWelfareSetting> lists = settingService.findCustomerIdByWelfareId(CpaSalaryWelfareSetting);
			//遍历所有的企业客户社保公积金方案列表
			for (CpaSalaryWelfareSetting cpaSalaryWelfareSetting : lists) {	
				//如果welfare_item_type_id为医疗时
				if (StringUtils.isNoneEmpty(cpaSalaryWelfareSetting.getWelfareItemTypeId()) 
						&& cpaSalaryWelfareSetting.getWelfareItemTypeId().equals(CpaSalaryWelfareProject.YILIAO)) {
					cpaSalaryCompanyWelfare.setYiliaoBasic(cpaSalaryCompanyWelfare1.getYiliaoBasic());
					//执行获取医疗的数据
					this.getYiliaoData(cpaSalaryCompanyWelfare, cpaSalaryWelfareSetting);
				}
				//如果welfare_item_type_id为养老时
				if (StringUtils.isNoneEmpty(cpaSalaryWelfareSetting.getWelfareItemTypeId()) 
						&& cpaSalaryWelfareSetting.getWelfareItemTypeId().equals(CpaSalaryWelfareProject.YANGLAO)){
					cpaSalaryCompanyWelfare.setYanglaoBasic(cpaSalaryCompanyWelfare1.getYanglaoBasic());
					//执行获取养老的数据
					this.getYanglaoData(cpaSalaryCompanyWelfare, cpaSalaryWelfareSetting);
				}	
				//如果welfare_item_type_id为失业时
				if (StringUtils.isNoneEmpty(cpaSalaryWelfareSetting.getWelfareItemTypeId()) 
						&& cpaSalaryWelfareSetting.getWelfareItemTypeId().equals(CpaSalaryWelfareProject.SHIYE)){
					cpaSalaryCompanyWelfare.setShiyeBasic(cpaSalaryCompanyWelfare1.getShiyeBasic());
					//执行获取失业的数据
					this.getShiyeData(cpaSalaryCompanyWelfare, cpaSalaryWelfareSetting);
				}
				//如果welfare_item_type_id为生育时
				if (StringUtils.isNoneEmpty(cpaSalaryWelfareSetting.getWelfareItemTypeId()) 
						&& cpaSalaryWelfareSetting.getWelfareItemTypeId().equals(CpaSalaryWelfareProject.SHENGYU)){
					cpaSalaryCompanyWelfare.setShengyuBasic(cpaSalaryCompanyWelfare1.getShengyuBasic());
					//执行获取生育的数据
					this.getShengyuData(cpaSalaryCompanyWelfare, cpaSalaryWelfareSetting);
				}
				//如果welfare_item_type_id为工伤时
				if (StringUtils.isNoneEmpty(cpaSalaryWelfareSetting.getWelfareItemTypeId())
						&& cpaSalaryWelfareSetting.getWelfareItemTypeId().equals(CpaSalaryWelfareProject.GONGSHANG)){
					cpaSalaryCompanyWelfare.setGongshangBasic(cpaSalaryCompanyWelfare1.getGongjijinBasic());
					//执行获取工伤的数据
					this.getGongshangData(cpaSalaryCompanyWelfare, cpaSalaryWelfareSetting);
				}
				//如果welfare_item_type_id为公积金时
				if (StringUtils.isNoneEmpty(cpaSalaryWelfareSetting.getWelfareItemTypeId()) 
						&& cpaSalaryWelfareSetting.getWelfareItemTypeId().equals(CpaSalaryWelfareProject.GONGJIJIN)){
					cpaSalaryCompanyWelfare.setGongjijinBasic(cpaSalaryCompanyWelfare1.getGongjijinBasic());
					//执行获取公积金的数据
					this.getGongjijinData(cpaSalaryCompanyWelfare, cpaSalaryWelfareSetting);
				}
				//如果welfare_item_type_id为大病时
				if (StringUtils.isNoneEmpty(cpaSalaryWelfareSetting.getWelfareItemTypeId()) 
						&& cpaSalaryWelfareSetting.getWelfareItemTypeId().equals(CpaSalaryWelfareProject.DABING)){
					cpaSalaryCompanyWelfare.setDabingBasic(cpaSalaryCompanyWelfare1.getDabingBasic());
					//执行获取大病的数据
					this.getDabingData(cpaSalaryCompanyWelfare, cpaSalaryWelfareSetting);
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
			dao.updateWelfare(cpaSalaryCompanyWelfare);
		}
			return true;

	}

	/**
	 * 执行计算后的医疗的费用
	 * @param cpaSalaryCompanyWelfare
	 * @param cpaSalaryWelfareSetting
	 */
	public void getYiliaoData(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare,CpaSalaryWelfareSetting cpaSalaryWelfareSetting){
		BigDecimal yiliaoBasic = cpaSalaryCompanyWelfare.getYiliaoBasic();
		//获取社保公积金方案中医疗的个人和单位的缴纳比例以及固定金额(后台保存的是整数，没有除100的值)
		BigDecimal yiliaocompanyRate = cpaSalaryWelfareSetting.getCompanyRate().divide(yibai, 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal yiliaocompanyFix = cpaSalaryWelfareSetting.getCompanyFix();
		String companyRoundRuleId = cpaSalaryWelfareSetting.getCompanyRoundRuleId();
		BigDecimal yiliaoindividualRate = cpaSalaryWelfareSetting.getIndividualRate().divide(yibai, 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal yiliaoindividualFix = cpaSalaryWelfareSetting.getIndividualFix();
		String individualRoundRuleId = cpaSalaryWelfareSetting.getIndividualRoundRuleId();
		//计算单位缴纳医疗的费用
		BigDecimal yiliaoCompany = yiliaoBasic.multiply(yiliaocompanyRate).add(yiliaocompanyFix);
		//计算个人缴纳医疗的费用
		BigDecimal yiliaoIndividual = yiliaoBasic.multiply(yiliaoindividualRate).add(yiliaoindividualFix);
		//根据不同的取整规则设置当前结果的取整规则
		//执行见角进元的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.CornerToYuan)) {
				RoundUtils.CornerToYuan(yiliaoCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.CornerToYuan)) {
				RoundUtils.CornerToYuan(yiliaoIndividual);
			}
		}
		//执行见角进分的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.CentToCorner)) {
				RoundUtils.CornerToYuan(yiliaoCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.CentToCorner)) {
				RoundUtils.CornerToYuan(yiliaoIndividual);
			}
		}
		//执行四舍五入到分的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.RoundAndRound)) {
				RoundUtils.CornerToYuan(yiliaoCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.RoundAndRound)) {
				RoundUtils.CornerToYuan(yiliaoIndividual);
			}
		}
		//执行四舍五入到角的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToCorner)) {
				RoundUtils.CornerToYuan(yiliaoCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.RoundToCorner)) {
				RoundUtils.CornerToYuan(yiliaoIndividual);
			}
		}
		//执行四舍五入到元的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)) {
				RoundUtils.CornerToYuan(yiliaoCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)) {
				RoundUtils.CornerToYuan(yiliaoIndividual);
			}
		}
		//将执行取整规则后的数据赋值cpaSalaryCompanyWelfare对象
		cpaSalaryCompanyWelfare.setYiliaoCompany(yiliaoCompany);
		cpaSalaryCompanyWelfare.setYiliaoIndividual(yiliaoIndividual);
	}
	
	/**
	 * 执行计算后的养老的费用
	 * @param cpaSalaryCompanyWelfare
	 * @param cpaSalaryWelfareSetting
	 */
	public void getYanglaoData(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare,CpaSalaryWelfareSetting cpaSalaryWelfareSetting){
		BigDecimal yanglaoBasic = cpaSalaryCompanyWelfare.getYanglaoBasic();
		//获取社保公积金方案中养老的个人和单位的缴纳比例以及固定金额
		BigDecimal yaonglaoCompanyRate = cpaSalaryWelfareSetting.getCompanyRate().divide(yibai, 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal yanglaoCompanyFix = cpaSalaryWelfareSetting.getCompanyFix();
		String companyRoundRuleId = cpaSalaryWelfareSetting.getCompanyRoundRuleId();
		BigDecimal yanglaoindividualRate = cpaSalaryWelfareSetting.getIndividualRate().divide(yibai, 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal yanglaoindividualFix = cpaSalaryWelfareSetting.getIndividualFix();
		String individualRoundRuleId = cpaSalaryWelfareSetting.getIndividualRoundRuleId();
		//计算养老单位缴纳的费用
		BigDecimal yanglaoCompany = yanglaoBasic.multiply(yaonglaoCompanyRate).add(yanglaoCompanyFix);
		//计算养老个人缴纳的费用
		BigDecimal yanglaoIndividual = yanglaoBasic.multiply(yanglaoindividualRate).add(yanglaoindividualFix);
		//根据不同的取整规则设置当前结果的取整规则
		//执行见角进元的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.CornerToYuan)) {
				RoundUtils.CornerToYuan(yanglaoCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.CornerToYuan)) {
				RoundUtils.CornerToYuan(yanglaoIndividual);
			}
		}
		//执行见分进角的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.CentToCorner)) {
				RoundUtils.CentToCorner(yanglaoCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.CentToCorner)) {
				RoundUtils.CentToCorner(yanglaoIndividual);
			}
		}
		//执行四舍五入到分的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.RoundAndRound)) {
				RoundUtils.RoundAndRound(yanglaoCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.RoundAndRound)) {
				RoundUtils.RoundAndRound(yanglaoIndividual);
			}
		}
		//执行四舍五入到角的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToCorner)) {
				RoundUtils.RoundToCorner(yanglaoCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.RoundToCorner)) {
				RoundUtils.RoundToCorner(yanglaoIndividual);
			}
		}
		//执行四舍五入到元的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)) {
				RoundUtils.RoundToYuan(yanglaoCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)) {
				RoundUtils.RoundToYuan(yanglaoIndividual);
			}
		}
		//将执行取整规则后的数据赋值cpaSalaryCompanyWelfare对象
		cpaSalaryCompanyWelfare.setYanglaoCompany(yanglaoCompany);;
		cpaSalaryCompanyWelfare.setYanglaoIndividual(yanglaoIndividual);
	}
	
	/**
	 * 获取计算后的失业的费用
	 * @param cpaSalaryCompanyWelfare
	 * @param cpaSalaryWelfareSetting
	 */
	public void getShiyeData(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare,CpaSalaryWelfareSetting cpaSalaryWelfareSetting){
		BigDecimal shiyeBasic = cpaSalaryCompanyWelfare.getShiyeBasic();
		//获取社保公积金方案中失业的个人和单位的缴纳比例以及固定金额
		BigDecimal shiyeCompanyRate = cpaSalaryWelfareSetting.getCompanyRate().divide(yibai, 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal shiyeCompanyFix = cpaSalaryWelfareSetting.getCompanyFix();
		String companyRoundRuleId = cpaSalaryWelfareSetting.getCompanyRoundRuleId();
		BigDecimal shiyeIndividualRate = cpaSalaryWelfareSetting.getIndividualRate().divide(yibai, 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal shiyeIndividualFix = cpaSalaryWelfareSetting.getIndividualFix();
		String individualRoundRuleId = cpaSalaryWelfareSetting.getIndividualRoundRuleId();
		//计算失业单位缴纳的费用
		BigDecimal shiyeCompany = shiyeBasic.multiply(shiyeCompanyRate).add(shiyeCompanyFix);
		//计算失业个人缴纳的费用
		BigDecimal shiyeIndividual = shiyeBasic.multiply(shiyeIndividualRate).add(shiyeIndividualFix);
		//根据不同的取整规则设置当前结果的取整规则
		//执行见角进元的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)) {
				RoundUtils.RoundToYuan(shiyeCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)) {
				RoundUtils.RoundToYuan(shiyeIndividual);
			}
		}
		//执行见分进角的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.CentToCorner)) {
				RoundUtils.CentToCorner(shiyeCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.CentToCorner)) {
				RoundUtils.CentToCorner(shiyeIndividual);
			}
		}
		//执行四舍五入到分的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.RoundAndRound)) {
				RoundUtils.RoundAndRound(shiyeCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.RoundAndRound)) {
				RoundUtils.RoundAndRound(shiyeIndividual);
			}
		}
		//执行四舍五入到角的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToCorner)) {
				RoundUtils.RoundToCorner(shiyeCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.RoundToCorner)) {
				RoundUtils.RoundToCorner(shiyeIndividual);
			}
		}
		//执行四舍五入到元的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)) {
				RoundUtils.RoundToYuan(shiyeCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)) {
				RoundUtils.RoundToYuan(shiyeIndividual);
			}
		}
		//将执行取整规则后的数据赋值cpaSalaryCompanyWelfare对象
		cpaSalaryCompanyWelfare.setShiyeCompany(shiyeCompany);;
		cpaSalaryCompanyWelfare.setShiyeIndividual(shiyeIndividual);
	}
	
	/**
	 * 获取计算后的生育的费用
	 * @param cpaSalaryCompanyWelfare
	 * @param cpaSalaryWelfareSetting
	 */
	public void getShengyuData(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare,CpaSalaryWelfareSetting cpaSalaryWelfareSetting){
		BigDecimal shengyuBasic = cpaSalaryCompanyWelfare.getShengyuBasic();
		//获取社保公积金方案中生育的个人和单位的缴纳比例以及固定金额
		BigDecimal shengyuCompanyRate = cpaSalaryWelfareSetting.getCompanyRate().divide(yibai, 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal shengyuCompanyFix = cpaSalaryWelfareSetting.getCompanyFix();
		String companyRoundRuleId = cpaSalaryWelfareSetting.getCompanyRoundRuleId();
		//计算生育单位缴纳的费用
		BigDecimal shengyuCompany = shengyuBasic.multiply(shengyuCompanyRate).add(shengyuCompanyFix);
		//根据不同的取整规则设置当前结果的取整规则
		//执行见角进元的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId) && companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)){
			RoundUtils.RoundToYuan(shengyuCompany);
		}
		//执行见分进角的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId) && companyRoundRuleId.equals(CpaSalaryRoundRule.CentToCorner)){
			RoundUtils.CentToCorner(shengyuCompany);
		}
		//执行四舍五入到分的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId) && companyRoundRuleId.equals(CpaSalaryRoundRule.RoundAndRound)){
			RoundUtils.RoundAndRound(shengyuCompany);
		}
		//执行四舍五入到角的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId) && companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToCorner)){
			RoundUtils.RoundToCorner(shengyuCompany);
		}
		//执行四舍五入到元的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId) && companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)){
			RoundUtils.RoundToYuan(shengyuCompany);
		}
		//将执行取整规则后的数据赋值cpaSalaryCompanyWelfare对象
		cpaSalaryCompanyWelfare.setShengyuCompany(shengyuCompany);
	}
	
	/**
	 * 获取计算后的工伤的费用
	 * @param cpaSalaryCompanyWelfare
	 * @param cpaSalaryWelfareSetting
	 */
	public void getGongshangData(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare,CpaSalaryWelfareSetting cpaSalaryWelfareSetting){
		BigDecimal gongshangBasic = cpaSalaryCompanyWelfare.getGongshangBasic();
		//获取社保公积金方案中工伤的个人和单位的缴纳比例以及固定金额
		BigDecimal gongshangCompanyRate = cpaSalaryWelfareSetting.getCompanyRate().divide(yibai, 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal gongshangCompanyFix = cpaSalaryWelfareSetting.getCompanyFix();
		String companyRoundRuleId = cpaSalaryWelfareSetting.getCompanyRoundRuleId();
		//计算工伤单位缴纳的费用
		BigDecimal gongshangCompany = gongshangBasic.multiply(gongshangCompanyRate).add(gongshangCompanyFix);
		//根据不同的取整规则设置当前结果的取整规则
		//执行见角进元的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId) && companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)){
			RoundUtils.RoundToYuan(gongshangCompany);
		}
		//执行见分进角的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId) && companyRoundRuleId.equals(CpaSalaryRoundRule.CentToCorner)){
			RoundUtils.CentToCorner(gongshangCompany);
		}
		//执行四舍五入到分的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.RoundAndRound)) {
			RoundUtils.RoundAndRound(gongshangCompany);
		}
		//执行四舍五入到角的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId) && companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToCorner)){
			RoundUtils.RoundToCorner(gongshangCompany);

		}
		//执行四舍五入到元的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId) && companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)){
			RoundUtils.RoundToYuan(gongshangCompany);
		}
		//将执行取整规则后的数据赋值cpaSalaryCompanyWelfare对象
		cpaSalaryCompanyWelfare.setGongshangCompany(gongshangCompany);
		}
	}
		
	/**
	 * 获取计算后的公积金的费用
	 * @param cpaSalaryCompanyWelfare
	 * @param cpaSalaryWelfareSetting
	 */
	public void getGongjijinData(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare,CpaSalaryWelfareSetting cpaSalaryWelfareSetting){
		BigDecimal gongjijinBasic = cpaSalaryCompanyWelfare.getGongjijinBasic();
		//获取社保公积金方案中公积金的个人和单位的缴纳比例以及固定金额
		BigDecimal gongjijinCompanyRate = cpaSalaryWelfareSetting.getCompanyRate().divide(yibai, 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal gongjijinCompanyFix = cpaSalaryWelfareSetting.getCompanyFix();
		String companyRoundRuleId = cpaSalaryWelfareSetting.getCompanyRoundRuleId();
		BigDecimal gongjijinIndividualRate = cpaSalaryWelfareSetting.getIndividualRate().divide(yibai, 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal gongjijinIndividualFix = cpaSalaryWelfareSetting.getIndividualFix();
		String individualRoundRuleId = cpaSalaryWelfareSetting.getIndividualRoundRuleId();
		//计算公积金单位缴纳的费用
		BigDecimal gongjijinCompany = gongjijinBasic.multiply(gongjijinCompanyRate).add(gongjijinCompanyFix);
		//计算公积金个人缴纳的费用
		BigDecimal gongjijinIndividual = gongjijinBasic.multiply(gongjijinIndividualRate).add(gongjijinIndividualFix);
		//根据不同的取整规则设置当前结果的取整规则
		//执行见角进元的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)) {
				RoundUtils.RoundToYuan(gongjijinCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)) {
				RoundUtils.RoundToYuan(gongjijinIndividual);
			}
		}
		//执行见分进角的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.CentToCorner)) {
				RoundUtils.CentToCorner(gongjijinCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.CentToCorner)) {
				RoundUtils.CentToCorner(gongjijinIndividual);
			}
		}
		//执行四舍五入到分的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.RoundAndRound)) {
				RoundUtils.RoundAndRound(gongjijinCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.RoundAndRound)) {
				RoundUtils.RoundAndRound(gongjijinIndividual);
			}
		}
		//执行四舍五入到角的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToCorner)) {
				RoundUtils.RoundToCorner(gongjijinCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.RoundToCorner)) {
				RoundUtils.RoundToCorner(gongjijinIndividual);
			}
		}
		//执行四舍五入到元的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId)){
			if (companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)) {
				RoundUtils.RoundToYuan(gongjijinCompany);
			}
			if (individualRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)) {
				RoundUtils.RoundToYuan(gongjijinIndividual);
			}
		}
		//将执行公积金取整规则后的数据赋值cpaSalaryCompanyWelfare对象
		cpaSalaryCompanyWelfare.setGongjijinCompany(gongjijinCompany);;
		cpaSalaryCompanyWelfare.setGongjijinIndividual(gongjijinIndividual);
	}

	/**
	 * 获取计算后的大病的费用
	 * @param cpaSalaryCompanyWelfare
	 * @param cpaSalaryWelfareSetting
	 */
	public void getDabingData(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare,CpaSalaryWelfareSetting cpaSalaryWelfareSetting){
		BigDecimal dabingBasic = cpaSalaryCompanyWelfare.getDabingBasic();
		//获取社保公积金方案中大病的个人和单位的缴纳比例以及固定金额
		BigDecimal dabingCompanyRate = cpaSalaryWelfareSetting.getCompanyRate().divide(yibai, 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal dabingCompanyFix = cpaSalaryWelfareSetting.getCompanyFix();
		String companyRoundRuleId = cpaSalaryWelfareSetting.getCompanyRoundRuleId();
		//计算大病单位缴纳的费用
		BigDecimal dabingCompany = dabingBasic.multiply(dabingCompanyRate).add(dabingCompanyFix);
		//根据不同的取整规则设置当前结果的取整规则
		//执行见角进元的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId) && companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)){
			RoundUtils.RoundToYuan(dabingCompany);
		}
		//执行见分进角的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId) && companyRoundRuleId.equals(CpaSalaryRoundRule.CentToCorner)){
			RoundUtils.CentToCorner(dabingCompany);
		}
		//执行四舍五入到分的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId) && companyRoundRuleId.equals(CpaSalaryRoundRule.RoundAndRound)){
			RoundUtils.RoundAndRound(dabingCompany);
		}
		//执行四舍五入到角的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId) && companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToCorner)){
			RoundUtils.RoundToCorner(dabingCompany);
		}
		//执行四舍五入到元的方法
		if (StringUtils.isNoneEmpty(companyRoundRuleId) && companyRoundRuleId.equals(CpaSalaryRoundRule.RoundToYuan)){
			RoundUtils.RoundToYuan(dabingCompany);
		}
		//将执行大病取整规则后的数据赋值cpaSalaryCompanyWelfare对象
		cpaSalaryCompanyWelfare.setDabingCompany(dabingCompany);;
	}
	
	/**
	 * 初始化客户社保公积金信息
	 * @param cpaSalaryCompanyWelfare
	 */
	public void initCpaSalaryCompanyWelfare(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare){
		dao.initCpaSalaryCompanyWelfare(cpaSalaryCompanyWelfare);
	}
	
	/**
	 * 获取企业公司缴纳的五险一金的总和
	 * @param customerId
	 * @return
	 */
	public Double getCompanyTax(String bookId,String customerId){
		if (StringUtils.isEmpty(customerId)) {
			throw new LogicException("无法确定企业信息！");
		}
		Date currentPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(bookId)) ;
		if (currentPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		CpaSalaryCompanyWelfare companyWelfare = new CpaSalaryCompanyWelfare();
		companyWelfare.setCustomerId(customerId);
		companyWelfare.setPeriod(currentPeriod);
		return dao.getCompanyTax(companyWelfare);
	}
	
	/**
	 * 获取企业缴纳员工的五险一金的各分项和
	 * @param customerId
	 * @return
	 */
	public Map<String,BigDecimal> getCompanySocialSecurity(String bookId,String customerId){
		if (StringUtils.isEmpty(customerId)) {
			throw new LogicException("企业不存在！");
		}
		Date currentPeriod = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(bookId)) ;
		if (currentPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		CpaSalaryCompanyWelfare companyWelfare = new CpaSalaryCompanyWelfare();
		companyWelfare.setCustomerId(customerId);
		companyWelfare.setPeriod(currentPeriod);
		//用于保存获取的社保公积金的分项信息
		Map<String,BigDecimal> maps = new HashMap<String,BigDecimal>();
		//获取单位承担五险一金---养老保险
		CpaSalaryCompanyWelfare companySocialSecurity = dao.getCompanySocialSecurity(companyWelfare);
		maps.put("yanglaoCompany", companySocialSecurity.getYanglaoCompany());
		maps.put("yiliaoCompany", companySocialSecurity.getYiliaoCompany());
		maps.put("shiyeCompany", companySocialSecurity.getShiyeCompany());
		maps.put("gongshangCompany", companySocialSecurity.getGongshangCompany());
		maps.put("shengyuCompany", companySocialSecurity.getShengyuCompany());
		maps.put("gongjijinCompany", companySocialSecurity.getGongjijinCompany());
		maps.put("dabingCompany", companySocialSecurity.getDabingCompany());
		return maps;
	}
}