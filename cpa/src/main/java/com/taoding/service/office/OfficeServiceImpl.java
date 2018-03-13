package com.taoding.service.office;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.taoding.common.entity.TreeNode;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultTreeServiceImpl;
import com.taoding.common.serviceUtils.EnterpriseUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.common.utils.QueryConditionFieldsArray;
import com.taoding.common.utils.QueryConditionSql;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.office.Office;
import com.taoding.domain.user.User;
import com.taoding.mapper.office.OfficeDao;
import com.taoding.mapper.user.UserDao;

@Service
public class OfficeServiceImpl extends DefaultTreeServiceImpl<OfficeDao, Office> 
	implements OfficeService{
	@Autowired
	private UserDao userDao;
	
	@Override
	@Transactional
	public TreeNode treeOffice() {
		Office office = dao.findTreeOfficeById("0",EnterpriseUtils.getCurrentUserEnter());
		if(StringUtils.isNotBlank(office.getId())){
			return treeOfficeList(office.getId());
		}
		return null;
		
	}
	
	@Transactional
	public TreeNode treeOfficeList(String id) {
		Office officeTree = dao.getTreeOffice(id);
		TreeNode tree=null;
		if (StringUtils.isNotBlank(officeTree.getId())
				&& StringUtils.isNotBlank(officeTree.getName())) {
			tree = new TreeNode(officeTree.getId(), officeTree.getName());
			List<Office> childrenOfficeList = dao.findOfficeList(id);
			for (Office office : childrenOfficeList) {
				if(StringUtils.isNotBlank(office.getId())){
					TreeNode o = treeOfficeList(office.getId());
					tree.getChildren().add(o);
				}
			}
		}

	return tree;
	}
	
	@Transactional(noRollbackFor = LogicException.class)
	@Override
	public void nextLowerSave(Office office) {
		List<Office> offices = dao.findAllList(office);
		if (offices.size() > 0) {
			throw new LogicException("组织机构编号重复!");
		}
		StringBuilder parentIdsStr = new StringBuilder();
		/*查询父级机构*/
		List<Office> officeList = dao.findTopOfficeList(office);
		  for (Office office2 : officeList) {
				if (office != null && office2 != null
						&& StringUtils.isNotBlank(office2.getGrade())
						&& StringUtils.isNotBlank(office2.getParentIds())) {
					BigDecimal num = new BigDecimal(office2.getGrade());
					BigDecimal num2 = new BigDecimal("1");
					office.setGrade(num.add(num2).toString());
					parentIdsStr.append(office2.getParentIds());
					parentIdsStr.append(office2.getId() + ",");
					office.setParentIds(parentIdsStr.toString());
				}
				super.save(office);
		}
	}
	@Transactional(noRollbackFor = LogicException.class)
	@Override
	public void deleteOffice(String officeId) {
		if(StringUtils.isNotBlank(officeId)){
			Office o = dao.getTreeOffice(officeId);
			List<Office> idLists = dao.findTopOfficeList(o);
			if (idLists.size() < 1) {
				throw new LogicException("删除机构失败, 不允许删除顶级机构");
			}
			selectTreeOffice(officeId);
			deleteTreeOffice(officeId);
		}
	}

	@Transactional
	public void deleteTreeOffice(String id) {
			Office office = new Office();
			office.setId(id);
			office.setDelFlag("1");
			dao.update(office);
		List<Office> officeList = dao.findOfficeList(id);
		if (officeList.size() < 1) {
			return;
		}
		for (Office office2 : officeList) {
			if(StringUtils.isNotBlank(office2.getId())){
				deleteTreeOffice(office2.getId());
			}
		}
	}
	
	@Override
	@Transactional
	public void deleteAtOffice(Office office) {
		office.setEnterpriseMarking(EnterpriseUtils.getCurrentUserEnter());
		office.setUseable("0");
		dao.update(office);
		List<Office> officesList = dao.findOfficeList(office.getId());
		for (Office o  : officesList) {
			if(StringUtils.isNotBlank(o.getId())){
				deleteAtOffice(o);
			}
		}
	}
	@Override
	@Transactional
	public Object findOfficeForUser(Map<String,Object> queryMap) {
		if(queryMap.get("isAll") == null || "".equals(queryMap.get("isAll")) || "false".equals(queryMap.get("isAll"))){
			PageUtils.page(queryMap);
		}
		String queryConditionVal = (String)queryMap.get("queryCondition");
		queryMap.put("queryConditionSql", QueryConditionSql.getQueryConditionSql(QueryConditionFieldsArray.employeeQueryFields, queryConditionVal));
		List<User> list=userDao.findOfficeForUser(queryMap);
		PageInfo<User> info = new PageInfo<User>(list);
		return info;
	}

	@Override
	@Transactional(noRollbackFor = LogicException.class)
	public Object getEditOffice(String id) {
		if(StringUtils.isBlank(id)){
		   throw new LogicException("组织机构编号不合法!");
		}
		return dao.getTreeOffice(id);
	}
	
	/**
	 * 删除时查询组织机构下是否有员工
	 * @param office
	 * @return 
	 * @author mhb 
	 * @date 2017年11月08日11:54:56
	 */
	@Transactional(noRollbackFor = LogicException.class)
	private void selectTreeOffice(String id) {
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("officeId", id);
		queryMap.put("enterpriseMarking", EnterpriseUtils.getCurrentUserEnter());
		List<User> userList=userDao.findOfficeForUser(queryMap);
		if (userList.size() > 0) {
			throw new LogicException("请先把组织机构内全部转移或删除后,在删除组织");
		}
		List<Office> officesList = dao.findOfficeList(id);
		for (Office o : officesList) {
			if(StringUtils.isNotBlank(o.getId())){
				selectTreeOffice(o.getId());
			}
			
		}
	}

	/**
	 * 插入新建企业组织机构
	 * @param enterpriseOfficeList
	 * add mhb 
	 * 2017年10月28日14:10:38
	 */
	@Transactional(readOnly = false)
	public void saveEnterpriseOfficeList(List<Office> enterpriseOfficeList,
			String enterpriseTemplate, String strId) {
		try {
			Office officeEnterpriseTemplate = new Office();
			officeEnterpriseTemplate.setEnterpriseMarking(enterpriseTemplate);
			officeEnterpriseTemplate.setDelFlag("0");
			List<Office> enterpriseTemplateOfficeList = dao
					.findEnterpriseTemplateForOffice(officeEnterpriseTemplate);
			for (Office office : enterpriseTemplateOfficeList) {
				office.setId(office.getId() + strId);
				office.setParent(new Office(office.getParentId() + strId));
				if (office.getParentIds().endsWith(",")) {
					String parentIds = office.getParentIds();
					parentIds = parentIds.replaceAll(",", strId + ",");
					office.setParentIds(parentIds);
				}
				office.setEnterpriseMarking(strId);
				enterpriseOfficeList.add(office);
			}
			// 部门表插入数据
			dao.insertEnterpriseOfficeList(enterpriseOfficeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
