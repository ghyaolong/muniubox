
package com.taoding.service.assisting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.pagehelper.StringUtil;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.NextNoUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.assisting.CpaAssistingTemplate;
import com.taoding.domain.assisting.CpaCustomerAssisting;
import com.taoding.mapper.assisting.CpaCustomerAssistingDao;

/**
 * 辅助核算类型Service
 * 
 * @author csl
 * @version 2017-11-16
 */
@Service
@Transactional
public class CpaCustomerAssistingServiceImpl extends
		DefaultCurdServiceImpl<CpaCustomerAssistingDao, CpaCustomerAssisting> implements CpaCustomerAssistingService {

	@Autowired
	private CpaCustomerAssistingService cpaCustomerAssistingService;
	
	@Autowired
	private CpaCustomerAssistingInfoService cpaCustomerAssistingInfoService;
	
	@Autowired
	private CpaAssistingExpenseTypeService cpaAssistingExpenseTypeService;
	
	@Autowired
	private CpaAssistingBalanceDepartmentService cpaAssistingBalanceDepartmentService;
	
	@Autowired
	private CpaAssistingGoodsTypeService cpaAssistingGoodsTypeService;
	
	@Autowired
	private CpaAssistingGoodsService cpaAssistingGoodsService;
	
	@Autowired
	private CpaAssistingEmployeeService cpaAssistingEmployeeService;
	
	@Autowired
	private CpaAssistingPositionService cpaPositionService;
	
	@Autowired
	private CpaAssistingProjectService projectService;
	
	@Autowired
	private CpaAssistingTemplateService templateService;

	/**
	 * 查询所有辅助核算类型的列表
	 * @param customerId
	 */
	public List<CpaCustomerAssisting> findList(String accountId) {
		//如果根据账薄id查询的辅助核算类型的信息不存在，则初始化模板数据
		if (Collections3.isEmpty(dao.findList(accountId))) {
			//初始化辅助核算模板
			this.initAssisting(accountId);
		}
		return dao.findList(accountId);
	}
	
	/**
	 * 初始化辅助核算列表
	 * @param customerId
	 * @param findList
	 * @return
	 */
	public void initAssisting(String accountId){
		List<CpaCustomerAssisting> findList = new ArrayList<CpaCustomerAssisting>();
		//获取辅助核算类型的模板数据
		List<CpaAssistingTemplate> TempList = templateService.findAllList();
		try {
			for (CpaAssistingTemplate c : TempList) {
				if (StringUtils.isNotBlank(c.getCatalogName()) && StringUtils.isNotBlank(c.getSort())
						&& StringUtils.isNotBlank(c.getRemarks())) {
					CpaCustomerAssisting cpaCustomerAssisting = new CpaCustomerAssisting();
					// 将辅助核算模板中的数据添加到CpaCustomerAssisting
		
					cpaCustomerAssisting.setId(UUID.randomUUID().toString());
					// 以后需要关联客户ID，暂时先这样写
					cpaCustomerAssisting.setAccountId(accountId);
					cpaCustomerAssisting.setTemplateId(c.getId());
					cpaCustomerAssisting.setCatalogName(c.getCatalogName());
					cpaCustomerAssisting.setSort(c.getSort());
					cpaCustomerAssisting.setRemarks(c.getRemarks());
					cpaCustomerAssisting.setIsCustom("0");
					cpaCustomerAssisting.setCreateDate(new Date());
					cpaCustomerAssisting.setUpdateDate(new Date());
					cpaCustomerAssisting.setAssistingInfoType(c.getAssistingInfoType());
					// 添加辅助核算模板数据到到企业辅助核算的集合中
					findList.add(cpaCustomerAssisting);
				}
			}
			//将添加后的企业辅助核算类型的数据添加到企业辅助核算类型cpaCustomerAssisting中
			int count = dao.insertList(findList);
			if (count==0) {
				throw new LogicException("添加辅助核算类型模板失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 增加辅助核算类型中的自定义分类
	 * @param cpaCustomerAssisting
	 * @return
	 */
	public Object insertCustom(CpaCustomerAssisting cpaCustomerAssisting){
		String catalogName =cpaCustomerAssisting.getCatalogName();
		String accountId = cpaCustomerAssisting.getAccountId();
		if (StringUtils.isEmpty(accountId)) {
			throw new LogicException("账薄不存在！");
		}
		cpaCustomerAssisting.setSort(this.findMaxSortByInfoSort(accountId));
		cpaCustomerAssisting.setTemplateId("9");
		cpaCustomerAssisting.setIsCustom("1");
		//设置为自定义类型
		cpaCustomerAssisting.setAssistingInfoType(CpaCustomerAssisting.TYPE_CUSTOM);
		cpaCustomerAssisting.setRemarks(catalogName);
		if (cpaCustomerAssisting !=null && StringUtils.isNotEmpty(catalogName)) {
			CpaCustomerAssisting cpaCustomerAssisting2 = this.findByName(catalogName,accountId);
			if (cpaCustomerAssisting2!=null && StringUtils.isNotEmpty(cpaCustomerAssisting2.getCatalogName())) {
				throw new LogicException("自定义分类已存在！");
			}
		}
		if (StringUtils.isEmpty(cpaCustomerAssisting.getAccountId())) {
			throw new LogicException("企业不存在！");
		}
		this.save(cpaCustomerAssisting);
		return true;
	}
	
	/**
	 * 根据id和custom删除自定义分类
	 * @param id
	 * @param custom
	 * @return
	 */
	public Object deleteCustom(String id){
		CpaCustomerAssisting assisting = dao.get(id);
		if (StringUtil.isNotEmpty(assisting.getIsCustom()) && !assisting.getIsCustom().equals("1")) {
			throw new LogicException("非自定义类型的辅助核算类型不能删除！");
		}
		int count = dao.deleteCustom(id);
		if (count>0) {
			return true;
		}
		return false;	
	}

	/**
	 * 查询最大的排序号
	 * @param 
	 * @return
	 */
	public String findMaxSortByInfoSort(String accountId){
		String maxNo = dao.findMaxSortByInfoSort(accountId);
		String nextNo = "";
		if(StringUtils.isNotEmpty(maxNo)){
			nextNo = NextNoUtils.getNextNo(maxNo);
		}else{
			nextNo = "001";
		}
		return nextNo;
	}
	
	/**
	 * 通过名字查找自定义分类名称
	 * @param name
	 * @param accountId
	 * @return
	 */
	public CpaCustomerAssisting findByName(String catalogName,String accountId) {
		return dao.findByName(catalogName,accountId);
	}
	
	/**
	 * 根据科目id,查询科目下所有辅助核算信息
	 * 2017年11月22日 下午4:43:04
	 * @param subjectId
	 * @return
	 */
	@Override
	public List<CpaCustomerAssisting> findListBySubjectId(String subjectId) {
		return dao.findListBySubjectId(subjectId);
	}
	
	/**
	 * 新增辅助核算类型的详情条目
	 * @param 
	 * @return
	 * add csl
	 * 2017-11-18 15:18:16
	 */
	@Override
	public Object insertAllDetail(Map<String,Object> maps){
		if (StringUtils.isEmpty(maps.get("id").toString())) {
			throw new LogicException("辅助核算类型id不能为空！");
		}
		//根据id查询辅助核算类型
		CpaCustomerAssisting cpaCustomerAssisting = cpaCustomerAssistingService.get(maps.get("id").toString());
		String assistingInfoType=cpaCustomerAssisting.getAssistingInfoType();
		if (assistingInfoType.equals(CpaCustomerAssisting.TYPE_DEPART)) {
			//新增部门的信息
			return cpaAssistingBalanceDepartmentService.insertDepart(maps);
		}else if(assistingInfoType.equals(CpaCustomerAssisting.TYPE_GOODS)) {
			//新增存货的信息
			return cpaAssistingGoodsService.insertGoods(maps);
		}else if(assistingInfoType.equals(CpaCustomerAssisting.TYPE_EMPLOYEE)){
			//新增员工的信息
			return cpaAssistingEmployeeService.insertEmployee(maps);
		}else if(assistingInfoType.equals(CpaCustomerAssisting.TYPE_POSITION)){
			//新增职位的信息
			return cpaPositionService.insertPosition(maps);
		}else if (assistingInfoType.equals(CpaCustomerAssisting.TYPE_PROJECT)) {
			//新增项目的信息
			return projectService.insertProject(maps);
		}
		//新增客户、供应商、第三方支付的信息
		return cpaCustomerAssistingInfoService.insertCpaCustomerAssistingInfo(maps);
	}
	
	/**
	 * 根据辅助核算类型的id查找相应的列表
	 * @param request
	 * @returns
	 */
	@Override
	public Object findTypeListById(Map<String,Object> maps){
		if (StringUtils.isEmpty(maps.get("id").toString())) {
			throw new LogicException("辅助核算类型id不能为空！");
		}
		//根据id查询辅助核算类型
		CpaCustomerAssisting cpaCustomerAssisting = cpaCustomerAssistingService.get(maps.get("id").toString());
		String assistingInfoType=cpaCustomerAssisting.getAssistingInfoType();
		maps.put("accountId", cpaCustomerAssisting.getAccountId());
		if (assistingInfoType.equals(CpaCustomerAssisting.TYPE_DEPART)) {
			//调用部门的信息
			return cpaAssistingBalanceDepartmentService.findAllList(maps);
		}else if(assistingInfoType.equals(CpaCustomerAssisting.TYPE_EMPLOYEE)){
			//执行查询员工的信息
			return cpaAssistingEmployeeService.findAllList(maps);
		}else if(assistingInfoType.equals(CpaCustomerAssisting.TYPE_GOODS)){
			//执行查询存货的信息
			return cpaAssistingGoodsService.findAllList(maps);
		}else if(assistingInfoType.equals(CpaCustomerAssisting.TYPE_POSITION)){
			//执行查询职位的信息
			return cpaPositionService.findAllList(maps);
		}else if (assistingInfoType.equals(CpaCustomerAssisting.TYPE_PROJECT)) {
			//执行查询项目的信息
			return projectService.findAllList(maps);
		}
			//查询除类型为部门，员工，存货，职位以外的辅助核算类型的详情
			//根据名字搜索符合条件的列表
			return cpaCustomerAssistingInfoService.findListById(maps);
	}
	
}