package com.taoding.service.user;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageInfo;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.serviceUtils.EnterpriseUtils;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.DictUtils;
import com.taoding.common.utils.Digests;
import com.taoding.common.utils.Encodes;
import com.taoding.common.utils.PageUtils;
import com.taoding.common.utils.QueryConditionFieldsArray;
import com.taoding.common.utils.QueryConditionSql;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.office.Office;
import com.taoding.domain.role.Role;
import com.taoding.domain.role.SysRoleGroup;
import com.taoding.domain.user.SysEnterpriseUser;
import com.taoding.domain.user.SysUserExtend;
import com.taoding.domain.user.User;
import com.taoding.mapper.menu.MenuDao;
import com.taoding.mapper.office.OfficeDao;
import com.taoding.mapper.role.RoleDao;
import com.taoding.mapper.role.SysRoleGroupDao;
import com.taoding.mapper.user.UserDao;

@Service
@Transactional(readOnly=true)
public class UserServiceImpl extends DefaultCurdServiceImpl<UserDao, User> implements UserService{
	

	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private SysRoleGroupDao sysRoleGroupDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private SysEnterpriseUserService sysEnterpriseUserService;
	@Autowired
	private SysUserExtendService sysUserExtendService;
	@Autowired
	private OfficeDao officeDao;
	
	/**
	 * 分页+按条件查询用户
	 * @param queryMap
	 * @return
	 */
	public PageInfo<User> findAllUserByPage(Map<String,Object> queryMap){
		
		if(queryMap.get("isAll") == null || "".equals(queryMap.get("isAll")) || "false".equals(queryMap.get("isAll"))){
			PageUtils.page(queryMap);
		}
		//添加查询条件
		String queryConditionVal = (String)queryMap.get("queryCondition");
		if(queryConditionVal != null && !"".equals(queryConditionVal)){
			queryMap.put("queryConditionSql", QueryConditionSql.getQueryConditionSql("a",QueryConditionFieldsArray.userQueryFields, queryConditionVal));
		}
		//处理分页
		List<User> userLists = dao.findAllUserList(queryMap);
		PageInfo<User> info = new PageInfo<User>(userLists);
		return info;

	}
	/**
	 * 根据企业注册的公司账户查询用户是否存在
	 * @param user
	 *  * add mhb 
	 * 2017-10-28 11:06:26
	 * @return
	 */
	public User findEnterpriseforLoginName(User user) {
		return dao.findEnterpriseforLoginName(user);
	}
	
	/**
	 * 插入模板企业用户表数据
	 * @param atEnterpriseInfo
	 * @return
	 * add mhb
	 * 2017年10月28日12:07:50
	 */
	@Transactional(readOnly=false)
	public void saveUserEnterprise(User user) {
		if (StringUtils.isBlank(user.getId())){
			// XXX preInsert
			//preInsert(user);
			dao.insertUserEnterprise(user);
		}
	}
	

	/**
	 * 插入模板企业角色菜单表数据
	 * @param id
	 * @return
	 * @author mhb
	 * 2017年10月28日12:07:50
	 */
	public void saveRoleMenuEnterpriseTemplate(String id,String enterpriseTemplate) {
		menuDao.insertRoleMenuEnterpriseTemplate(id,enterpriseTemplate);		
	}
	
	@Override
	public PageInfo<User> findAiUser(Map<String,Object> queryMap) {
		if(queryMap.get("isAll") == null || "".equals(queryMap.get("isAll")) || "false".equals(queryMap.get("isAll"))){
			PageUtils.page(queryMap);
		}
         if(StringUtils.isNotBlank((String)queryMap.get("sex"))){
        	 queryMap.put("sex", DictUtils.getDictValue((String)queryMap.get("sex"), "sex", "1"));	 
		}
		String queryConditionVal = (String)queryMap.get("queryCondition");
		queryMap.put("queryConditionSql", QueryConditionSql.getQueryConditionSql(QueryConditionFieldsArray.officeEmployeeQueryFields, queryConditionVal));
		List<User> userList = dao.findEmpUserList(queryMap);
		List<User> userPage = new ArrayList<User>();
		for (User userTemp : userList) {
		String officeNames= Collections3.extractToString( dao.selectOfficeByUserId(userTemp), "name", ",");
		        userTemp.setOfficeNames(officeNames);
				List<SysRoleGroup> sysRoleGroupList = sysRoleGroupDao
						.findRoleGroupList(new SysRoleGroup(userTemp));
				StringBuilder roleGropNames = new StringBuilder();
				if (!CollectionUtils.isEmpty(sysRoleGroupList)) {
					roleGropNames.append(Collections3.extractToString(sysRoleGroupList, "roleGroupName", ","));
				}
				// 用户组封装
				SysRoleGroup sysRoleGroup = new SysRoleGroup();
				sysRoleGroup.setUser(userTemp);
				List<SysRoleGroup> groupList = sysRoleGroupDao.selectSysGroupRole(sysRoleGroup);
				StringBuilder roleNames = new StringBuilder();
				if (!CollectionUtils.isEmpty(groupList)) {
					for (SysRoleGroup g : groupList) {
						if (StringUtils.isEmpty(roleNames)) {
							roleNames.append(g.getRoleGroupName() + "-").append(Collections3.extractToString(
											g.getRoleList(), "name", ","));
						} else {
							roleNames.append(",").append(Collections3.extractToString(g.getRoleList(), "name", ","));
						}
					}
				}
				if (StringUtils.isNotEmpty(roleGropNames.toString())) {
					if (StringUtils.isNotEmpty(roleNames)) {
						roleNames.insert(0, roleGropNames.toString() + "|");
					} else {
						roleNames.insert(0, roleGropNames.toString());
					}
				}
				userTemp.setRoleNames(roleNames.toString());
				if (userTemp.getSysUserExtend() != null
						&& StringUtils.isNotEmpty(userTemp.getSysUserExtend()
								.getSex())) {
				    userTemp.setSex(DictUtils.getDictLabel(userTemp.
					 getSysUserExtend().getSex(), "sex", ""));  
				}
				userPage.add(userTemp);
		}
		PageInfo<User> info = new PageInfo<User>(userPage);
		return info;
	}

	/**
	 * 处理组织机构字符串  角色组字符串信息
	 * @param user
	 * @return 
	 * @author mhb
	 */
	@Transactional(noRollbackFor = LogicException.class)
	@Override
	public void insertUser(User user) {
		//判断登陆账户是否重复
		if(StringUtils.isNotBlank(user.getLoginName())){
			 User  loginUser=dao.findEnterpriseforLoginName(user); 
			  if(loginUser!=null){
				  throw new LogicException("登陆账户重复!");
			  }
		}
		//处理组织机构
		if(!CollectionUtils.isEmpty(user.getOfficeListStr())){
			for (String id : user.getOfficeListStr()){ 
				      if(officeDao.findAllList(new Office(id)).size()>0) {
				    	  user.getOfficeList().add(new Office(id));
				      }
			   }
		 }
				if(StringUtils.isNotBlank(user.getPassword())){
					user.setPassword(entryptPassword(user.getPassword()));
				}else{
					if(StringUtils.isNotBlank(user.getLoginName())){
					 user.setPassword(entryptPassword(user.getLoginName()+"abc"));//默认密码
					}
				}
				// 处理用户角色，角色组
				getRoleAndGroup(user);
				// 角色数据有效性验证，过滤不在授权内的角色
				List<Role> roleList =new ArrayList();
				List<String> roleIdList = user.getRoleIdList();
				for (Role r : roleDao.findAllList(new Role())) {
					if (roleIdList.contains(r.getId())) {
						roleList.add(r);
					}
				}
				user.setRoleList(roleList);
				if (StringUtils.isNotEmpty(user.getSex())) {
					SysUserExtend sysUserExtend = new SysUserExtend();
				    sysUserExtend.setSex(DictUtils.getDictValue(user.getSex(), "sex", "1"));
					 user.setSysUserExtend(sysUserExtend);
				}
				saveUser(user);
	}
	
	/**
	 * 插入员工相关表信息
	 * 
	 * @param user
	 * @return 
	 * @author mhb
	 * @Date 2017-10-19 11:15:10
	 */
	@Transactional
	public void saveUser(User user){
		boolean flag = true;// 验证用户一个用户可以多个企业
		user.setEnterpriseMarking(EnterpriseUtils.getCurrentUserEnter());
		if (StringUtils.isBlank(user.getId())) {
			 //XXX PreInsert
			//preInsert(user);
			dao.insert(user);
		} else {
			if (flag) {
				// 更新用户数据
				 //XXX preUpdate
				//preUpdate(user);
				dao.update(user);
			}
		}

		if (StringUtils.isNotBlank(user.getId())) {
			// 更新用户与角色关联
			dao.deleteUserRoleGroup(user);// 删除角色组管理
			dao.deleteUserRole(user);
			flag = false;
			if (user.getRoleList() != null && user.getRoleList().size() > 0) {
				dao.insertUserRole(user);
				flag = true;
			}
			if (user.getRoleGroupList() != null
					&& user.getRoleGroupList().size() > 0) {
				dao.insertUserRoleGroup(user);
				flag = true;
			}

			if (!CollectionUtils.isEmpty(user.getOfficeList())) {
				// 批量删除后添加
				SysEnterpriseUser sysEnterpriseUser = new SysEnterpriseUser();
				sysEnterpriseUser.setUser(user);
				sysEnterpriseUserService.deleteRealy(sysEnterpriseUser);// 物理删除
				for (Office office : user.getOfficeList()) {// 插入
					sysEnterpriseUser = new SysEnterpriseUser();
					sysEnterpriseUser.setUser(user);
					sysEnterpriseUser.setOffice(office);
					sysEnterpriseUser.setEnterpriseId(EnterpriseUtils.getCurrentUserEnter());
				sysEnterpriseUserService.save(sysEnterpriseUser);
				}
			}
			if (null != user.getSysUserExtend()) {
				user.getSysUserExtend().setUser(user);
				sysUserExtendService.save(user.getSysUserExtend());
			}
		}
		
	}

   /**
	 * 处理用户的角色组，及角色
	 * 
	 * @param ids
	 * @param user
	 * @return add lixc 2017-10-19 11:15:10
	 */
	@Transactional
	public void getRoleAndGroup(User user) {
		if (null != user && StringUtils.isNotEmpty(user.getRoleAiId())) {
			String[] arrayList = user.getRoleAiId().split(",");
			user.getRoleGroupList().clear();
			user.getRoleList().clear();
			for (int i = 0; i < arrayList.length; i++) {
				if (arrayList[i].indexOf("_group") > 0) {// 用户组
					if (null != user.getRoleGroupList()) {
						SysRoleGroup roleGroup = new SysRoleGroup();
						roleGroup.setId(StringUtils.replace(arrayList[i],
								"_group", ""));
						user.getRoleGroupList().add(roleGroup);
					}
				} else {// 角色
					if (null != user.getRoleList()) {
						Role role = new Role();
						role.setId(arrayList[i]);
						user.getRoleList().add(role);
					}
				}
			}
		}
	}
	@Override
	@Transactional(noRollbackFor = LogicException.class)
	public void deleteUser(String id) {
		/*if (UserUtils.getUser().getId().equals(id)) {
			throw new LogicException("删除用户失败, 不允许删除当前用户");
		} else */if (User.isAdmin(id)) {
			throw new LogicException("删除用户失败, 不允许删除超级管理员用户");
		} else {
			dao.delete(new User(id));
		}
	}

	@Override
	public Object findUserById(String id) {
		return dao.get(id);
	}

	@Override
	public User getUserByLoginName(String loginName){
				List<User>list =dao.getByLoginName(new User(null, loginName));
				if (CollectionUtils.isEmpty(list)){
					throw new LogicException("登陆用户不存在！");
				}
				User user=list.get(0);
				Role role=new Role(user);
				role.setEnterpriseMarking(user.getEnterpriseMarking());
				user.setRoleList(roleDao.findList(role));
			return user;
		}
	
	
	/**
	 * 查询用户信息(包括公司信息)
	 * @param id
	 * @return
	 */
	@Override
	public User getUserInfo(String id,String delFlag) {
		//XXX 需要重新实现
		User user = dao.get(id);
		/*List<AtEnterpriseInfo> companyLists = atEnterpriseInfoService.findCompanyByUserId(id,delFlag);
		user.setEnterpriseInfoLists(companyLists);*/
		return user;
	}
	
	/**
	 * 更改用户启用/禁用状态
	 * @param id
	 * @param delFlag
	 * @return
	 */
	@Override
	@Transactional
	public Object updateUserLoginFlag(User user) {
		String loginFlag = "0";
		boolean adminFlag = false ;
		if (User.isAdmin(user.getId()) && user.getLoginFlag().equals("0")) {
			adminFlag = true ;
			
		} else if (!User.isAdmin(user.getId()) && user.getLoginFlag().equals("0")) {
			loginFlag = "1" ;
		}else if(!User.isAdmin(user.getId()) && user.getLoginFlag().equals("1")){
			loginFlag = "0" ;
		}else{
			
		}
		if(!adminFlag){
			int count = dao.updateUserLoginFlag(user.getId(), loginFlag);
			if(count > 0){
				return true ;
			}
		}
		return false ;
	}
	
	@Override
	@Transactional
	public void importData(List<User> list) {
		List<User> userList=checkUserList(list);
		   for (User user : userList) {
			   saveUser(user);
		}
	}
	@Transactional(noRollbackFor = LogicException.class)
	public List<User> checkUserList(List<User> list) {
        List<User> newList= new ArrayList<User>();
		int num=0;
		for (User user : list) {
			num++;
			/*验证登陆账户*/
			if(StringUtils.isEmpty(user.getLoginName())){
				throw new LogicException("第"+num+"条数据登陆账户不能为空!");
			}else  if(!StringUtils.check(user.getLoginName(), StringUtils.REGEX_CHECK_PHONE)){
				throw new LogicException("第"+num+"条数据登陆账户必须是手机号!");
			}else{
				List<User> loginUserList=dao.getByLoginName(user);
		     	if(!Collections3.isEmpty(loginUserList)){
		     		throw new LogicException("第"+num+"条数据登陆账户重复!");
		     	}
		        user.setPassword(entryptPassword(user.getLoginName()+"abc"));
			}
			/*验证邮箱格式*/
		    if(StringUtils.isNotBlank(user.getEmail())){
				if(!StringUtils.check(user.getEmail(),StringUtils.REGEX_CHECK_EMAIL)) {
					throw new LogicException("第"+num+"条数据邮箱格式不正确!");
				}
			}
		
			if(StringUtils.isBlank(user.getMobile())){
				throw new LogicException("第"+num+"条数据手机号不能为空!");
			}else if(!StringUtils.check(user.getLoginName(), StringUtils.REGEX_CHECK_PHONE)){
				throw new LogicException("第"+num+"条数据手机号不合法!");
			}
			
			
			if (StringUtils.isNotEmpty(user.getSex())) {
				SysUserExtend sysUserExtend = new SysUserExtend();
			    sysUserExtend.setSex(DictUtils.getDictValue(user.getSex(), "sex", "1"));
				 user.setSysUserExtend(sysUserExtend);
			}
			/*批量导入效验角色,角色组信息*/
			if(StringUtils.isNotBlank(user.getRoleNames())){
			      checkRole(user,num);
		   }
		   if(StringUtils.isNotBlank(user.getOfficeNames())){
			//验证组织机构
			    checkOffice(user,num);
		   }
		   newList.add(user); 
	}
		return newList;
	}
	/*批量导入效验角色,角色组信息*/
	private void checkRole(User user, int num) {
		List<Role> roleList = roleDao.findAllList(new Role());
		List<SysRoleGroup> sysRoleGroupList =sysRoleGroupDao.findList(new SysRoleGroup());
		/*淘丁-系统管理员,23423|淘丁-23423,fdf*/				
		String roleGroups = user.getRoleNames().replaceAll(","," ").replaceAll("\\|", ",");
		String[] roleGroupNames =roleGroups.split(",");
		for (String str : roleGroupNames) {
			if (str.indexOf("-") != -1) {
				String roleGroupName = str.substring(0,str.indexOf("-"));
				String[] roleNames = str.substring(str.indexOf("-") + 1,str.length()).replace(" ", ",").split(",");
				/*验证角色组信息*/
				boolean falgRoleGroup = false;
				for (SysRoleGroup sysRoleGroup : sysRoleGroupList) {
					if (sysRoleGroup.getRoleGroupName().equals(roleGroupName)) {
						/*去掉重复的角色组信息*/
						if(!user.getRoleGroupList().contains(sysRoleGroup)){
							user.getRoleGroupList().add(sysRoleGroup);
						}
						falgRoleGroup = true;
					  break;
					}
				}
				if(!falgRoleGroup){
					user.getRoleGroupList().clear();
					throw new LogicException("第"+num+"角色组信息验证有误!");
				}
				
				for (String str1 : roleNames) {
					//验证角色信息
					boolean falgRole = false;
					for (Role role : roleList) {
						if (role.getName().equals(str1)) {
							/*去掉重复的角色信息*/
							 if(!user.getRoleList().contains(role)){
								 user.getRoleList().add(role);
							 }
							falgRole = true;
							break;
						}
					}
					if(!falgRole){
						user.getRoleList().clear();
						throw new LogicException("第"+num+"角色信息验证有误!");
					}
				}
				
			}
		}
		  
		   if (CollectionUtils.isEmpty(user.getRoleList())) {
				for (Role role : roleList) {
					if (StringUtils.isNotEmpty(role.getIsDefault())) {
						if (role.getIsDefault().equals("1")) {
							user.getRoleList().add(role);
						}
					}
				}
			}
	}
	/*1效验组织机构*/
	private void checkOffice(User user,int num) {
		//去掉重复的
		 String[] arrayOfficeNames=StringUtils.split(user.getOfficeNames(),",");
		 List<String> OfficeNamesList = new ArrayList<>();  
		 boolean flag;  
		 for(int i=0;i<arrayOfficeNames.length;i++){  
		     flag = false;  
		     for(int j=0;j<OfficeNamesList.size();j++){  
		         if(arrayOfficeNames[i].equals(OfficeNamesList.get(j))){  
		             flag = true;  
		             break;  
		         }  
		     }  
		     if(!flag){  
		    	 OfficeNamesList.add(arrayOfficeNames[i]);  
		     }  
		 } 
		 //与数据库已有组织机构匹配
		 for (String str : OfficeNamesList) {
			 Office office=new  Office();
			 office.setName(str);
			 List<Office> officeList= officeDao.findAllList(office);
			 if(officeList.size()<=0){
				 user.getOfficeList().clear();
				 throw new LogicException("第"+num+"组织机构信息验证有误!");
			 }else{ 
				 if(!Collections3.isEmpty(officeList)){
					 if(officeList.get(0)!=null&&StringUtils.isNotBlank(officeList.get(0).getId())){
						 user.getOfficeList().add(officeList.get(0));
					 }
				 }
			 }
		}
	}
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
	}
	
}
