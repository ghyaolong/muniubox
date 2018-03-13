package com.taoding.service.office;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.taoding.common.entity.TreeNode;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultTreeServiceImpl;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.DictUtils;
import com.taoding.common.utils.EnterpriseUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.common.utils.QueryConditionFieldsArray;
import com.taoding.common.utils.QueryConditionSql;
import com.taoding.common.utils.StringUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.office.Office;
import com.taoding.domain.user.User;
import com.taoding.mapper.office.OfficeDao;
import com.taoding.mapper.user.UserDao;

@Service
@Transactional
public class OfficeServiceImpl extends DefaultTreeServiceImpl<OfficeDao, Office> 
	implements OfficeService{
	@Autowired
	private UserDao userDao;
	
	@Override
	public TreeNode treeOffice(Map<String,Object> queryMap) {
		String useable=(String)queryMap.get("useable");
		Office office = dao.findTreeOfficeById("0",EnterpriseUtils.getCurrentUserEnter());
		if(StringUtils.isNotBlank(office.getId())){
			return treeOfficeList(office.getId(),"2",useable);
		}
		return new TreeNode();
		
	}
	
	/**
	 * 
	* @Description: TODO(修改mhb 添加类型 区分是否要加载用户信息) 
	* @param id
	* @type 1用户 2 组织机构
	* @useable 1启用  
	* @return TreeNode 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月23日
	 */
	public TreeNode treeOfficeList(String id,String type,String useable) {
		Office officeTree = dao.getTreeOffice(id);
		TreeNode tree=null;
		User user= null;
		if (StringUtils.isNotBlank(officeTree.getId())&& StringUtils.isNotBlank(officeTree.getName())) {
			tree = new TreeNode(officeTree.getId(), officeTree.getName());
			
			List<Office> childrenOfficeList = dao.findOfficeList(id,useable);
			for (Office office : childrenOfficeList) {
				if(StringUtils.isNotBlank(office.getId())){
					TreeNode o = treeOfficeList(office.getId(),type,useable);
					o.setAttr("office");
					tree.getChildren().add(o);
				}
			}
			
			if("1".equals(type)){
				//获得用户列表
				TreeNode nodeTemp = null;
				user = new User();
				user.setEnterpriseMarking(EnterpriseUtils.getCurrentUserEnter());
				user.setOffice(officeTree);
				List<User>userList=userDao.findUserByOfficeId(user);
				if(!CollectionUtils.isEmpty(userList)){
					for (User u : userList) {
						nodeTemp = new TreeNode(u.getId(),u.getName());
						nodeTemp.setAttr("user");
						tree.getChildren().add(nodeTemp);
					}
				}
			} 
		}

	return tree;
	}
	
	@Override
	public Object nextLowerSave(Office office) {		
		if(office==null||dao.findAllList(office).size()>0){
		throw new LogicException("添加组织机构下一级信息有误或组织机构编号重复!");
		}
		StringBuilder parentIdsStr = new StringBuilder();
		/*查询父级机构*/
		if(StringUtils.isEmpty(office.getParentId())){
			throw new LogicException("所要添加的上级信息有误!");	
		}
		List<Office> officeList = dao.findTopOfficeList(office.getParentId(),EnterpriseUtils.getEnterpriseTemplate());
		if(Collections3.isEmpty(officeList)){
			throw new LogicException("所要添加的上级信息不存在!");
		}
		  for (Office office2 : officeList) {
				if (office != null && office2 != null&& StringUtils.isNotBlank(office2.getGrade())
						&& StringUtils.isNotBlank(office2.getParentIds())) {
					BigDecimal num = new BigDecimal(office2.getGrade());
					BigDecimal num2 = new BigDecimal("1");
					office.setGrade(num.add(num2).toString());
					parentIdsStr.append(office2.getParentIds());
					parentIdsStr.append(office2.getId() + ",");
					office.setParentIds(parentIdsStr.toString());
					office.setEnterpriseMarking(EnterpriseUtils.getCurrentUserEnter());
				}
				office.setCode(UUID.randomUUID().toString().replace("-", ""));
				office.setUseable(Office.DEL_FLAG_DELETE); //默认启用
				super.save(office);
		}
		return true;
	}
	@Override
	public Object deleteOffice(String officeId) {
		if(StringUtils.isEmpty(officeId)){
			throw new LogicException("删除的机构信息有误!");
		}
		Office o = dao.getTreeOffice(officeId);
		if((o==null&&StringUtils.isNotBlank(o.getName()))||StringUtils.isEmpty(o.getParentId())){
		  throw new LogicException(o.getName()+"机构删除的信息有误或已经删除!");
		}
		
		List<Office> idLists = dao.findTopOfficeList(o.getParentId(),EnterpriseUtils.getCurrentUserEnter());
		if (idLists.size() < 1) {
			throw new LogicException("删除机构失败, 不允许删除顶级机构");
		}
		/*删除组织机构*/
		deleteTreeData(officeId);
		return true;
	}
	
	@Override
	public Object deleteAtOffice(String id,String useable) {
		if(StringUtils.isBlank(id)||StringUtils.isBlank(useable)){
			throw new LogicException("数据信息异常!");
		}
		Office o = dao.getTreeOffice(id);
		if(o==null){
			  throw new LogicException("数据信息异常!");
		}
		
		if(StringUtils.isNotEmpty(o.getUseable())){
			if(StringUtils.isNotEmpty(o.getParentId())){
				List<Office> idLists = dao.findTopOfficeList(o.getParentId(),EnterpriseUtils.getCurrentUserEnter());
				if (idLists.size() < 1) {
					throw new LogicException("顶级机构禁止操作");
				}
					deleteAtTreeOffice(id,useable);
			}
			
		}
		return true;
	}
	private void deleteAtTreeOffice(String id,String useable) {
		 Office office = new Office();
		 office.setId(id);
		 office.setUseable(useable);
		 dao.update(office);
		 List<Office> childrenOfficeList = dao.findOfficeList(id,null);
		 for (Office o : childrenOfficeList) {
			 if(StringUtils.isEmpty(o.getId())){
				 throw new LogicException("机构信息有误!");
			 }
			 deleteAtTreeOffice(o.getId(),useable);
		}
	}
	@Override
	public Object findOfficeForUser(Map<String,Object> queryMap) {
		if(queryMap.get("isAll") == null || "".equals(queryMap.get("isAll")) || "false".equals(queryMap.get("isAll"))){
			PageUtils.page(queryMap);
		}
		String queryConditionVal = (String)queryMap.get("queryCondition");
		queryMap.put("queryConditionSql", QueryConditionSql.getQueryConditionSql(QueryConditionFieldsArray.employeeQueryFields, queryConditionVal));
		List<User> list=userDao.findOfficeForUser(queryMap);
		for (User user : list) {
			 if(user.getSysUserExtend()!=null&&StringUtils.isNotEmpty(user.getSysUserExtend().getSex())){
				 user.setSex(DictUtils.getDictLabel(user.getSysUserExtend().getSex(), "sex", "")); 
			 }
		}
		PageInfo<User> info = new PageInfo<User>(list);
		return info;
	}

	@Override
	public Object getEditOffice(String id) {
		if(StringUtils.isBlank(id)){
		   throw new LogicException("组织机构编号不合法!");
		}
		Office office=dao.getTreeOffice(id);
		if(office==null){
		throw new LogicException("查询信息有误!");
		}
		return office;
	}
	/**
	 * 删除组织机构
	 * @param office
	 * @return 
	 * @author mhb 
	 * @date 2017年11月08日11:54:56
	 */
	private void deleteTreeData(String id){
		Office office = new Office();
		office.setId(id);
		office.setDelFlag(Global.YES);
		dao.update(office);
		Map<String,Object> queryMap = new HashMap<String, Object>();
		queryMap.put("officeId", id);
		queryMap.put("enterpriseMarking", EnterpriseUtils.getCurrentUserEnter());
		List<User> userList=userDao.findOfficeForUser(queryMap);
		if(userList.size()>0){
			throw new LogicException("请先把组织机构内员工全部转移或删除后,在删除组织");
		}
		if(StringUtils.isNotEmpty(id)){
			List<Office> childrenOfficeList=dao.findOfficeList(id,null);
			for (Office o : childrenOfficeList) {
				  if(StringUtils.isNotBlank(o.getId())){
					  deleteTreeData(o.getId());
				  }
			}
		}
		
		
	}
	/**
	 * 插入新建企业组织机构
	 * @param enterpriseOfficeList
	 * add mhb 
	 * 2017年10月28日14:10:38
	 */
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

	@Override
	public Object edit(Office office) {
		if(StringUtils.isEmpty(office.getId())||StringUtils.isEmpty(office.getName())){
			throw new LogicException("编辑组织信息有误!");
		}
		if(dao.get(office.getId())==null){
			throw new LogicException("编辑组织信息不存在!");
		}
		office.setEnterpriseMarking(EnterpriseUtils.getCurrentUserEnter());
		dao.update(office);
		return true;
	}


		@Override
		public TreeNode treeOfficeAndUser(){
			Office office = dao.findTreeOfficeById("0",EnterpriseUtils.getCurrentUserEnter());
			if(StringUtils.isNotBlank(office.getId())){
				return treeOfficeList(office.getId(),"1",null);
			}
			return new TreeNode();
		}
}
