package com.taoding.service.user;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;















import com.alibaba.druid.stat.TableStat.Mode;
import com.github.pagehelper.PageInfo;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.CacheMapUtils;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.DictUtils;
import com.taoding.common.utils.Digests;
import com.taoding.common.utils.Encodes;
import com.taoding.common.utils.EnterpriseUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.common.utils.QueryConditionFieldsArray;
import com.taoding.common.utils.QueryConditionSql;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UserUtils;
import com.taoding.common.utils.ValidCodeUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseInfo;
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
import com.taoding.service.atEnterpriseInfo.AtEnterpriseInfoService;

@Service
@Transactional
public class UserServiceImpl extends DefaultCurdServiceImpl<UserDao, User> implements UserService{
	

	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	/**
	 * 工号
	 */
	public static final String DEFAULT_USER_NO="000000001";
	
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
	private AtEnterpriseInfoService atEnterpriseInfoService;
	@Autowired
	private OfficeDao officeDao;
	
	// 文件上传路径
	@Value("${initPassword.password}")
	private String password;
	
	/**
	 * 分页+按条件查询用户 
	 * 不带企业标示
	 * @param queryMap
	 * @return
	 */
	public PageInfo<User> findAllUserByPage(Map<String,Object> queryMap){
		
		String isAll = queryMap.get("isAll").toString();
		if(!StringUtils.isNotEmpty(isAll) || "false".equals(isAll)){
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
	public User findEnterpriseforLoginName(String loginName) {
		return dao.findEnterpriseforLoginName(loginName,null);
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
			preInsert(user);
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
		queryMap.put("pageSize", "10");
		if(queryMap.get("isAll") == null || "".equals(queryMap.get("isAll")) || "false".equals(queryMap.get("isAll"))){
			PageUtils.page(queryMap);
		}
         if(StringUtils.isNotBlank((String)queryMap.get("sex"))){
        	 queryMap.put("sex", DictUtils.getDictValue((String)queryMap.get("sex"), "sex", "1"));	 
		}
		String queryConditionVal = (String)queryMap.get("queryCondition");
		queryMap.put("delFlag", Global.NO);
		queryMap.put("loginFlag", Global.START_USERING);
		queryMap.put("enterpriseMarking", EnterpriseUtils.getCurrentUserEnter());
		queryMap.put("queryConditionSql", QueryConditionSql.getQueryConditionSql(QueryConditionFieldsArray.officeEmployeeQueryFields, queryConditionVal));
		
		List<User> userList = dao.findEmpUserList(queryMap);
		for (User userTemp : userList) {
			    if(StringUtils.isNotEmpty(userTemp.getId())){
			    	userTemp.setOfficeList(dao.selectOfficeByUserId(userTemp.getId()));
			    }
				/*查询角色、角色组*/
				if(StringUtils.isNotEmpty(userTemp.getId())){
					List<SysRoleGroup> groupList = sysRoleGroupDao.selectSysGroupRole(userTemp.getId(),EnterpriseUtils.getCurrentUserEnter());
					userTemp.setRoleGroupList(groupList);
				}
				if (userTemp.getSysUserExtend() != null&& StringUtils.isNotEmpty(userTemp.getSysUserExtend().getSex())) {
				    userTemp.setSex(DictUtils.getDictLabel(userTemp.
					 getSysUserExtend().getSex(), "sex", ""));  
				}else{
					userTemp.setSex("");
				}
		}
		PageInfo<User> info = new PageInfo<User>(userList);
		return info;
	}

	/**
	 * 处理组织机构字符串  角色组字符串信息
	 * @param user
	 * @return 
	 * @author mhb
	 */
	@Override
	public Object insertUser(User user) {
		if(user==null){
			throw new LogicException("添加信息有误!");
		}
		//判断登陆账户是否重复
		 
		if(StringUtils.isNotBlank(user.getLoginName())){
			User  loginUser=dao.findEnterpriseforLoginName(user.getLoginName(),UserUtils.getCurrentUserEnterpriseId()); 
			//修改判断
			  if(StringUtils.isNotEmpty(user.getId())){
				   if(loginUser!=null||!(user.getId().equals(loginUser.getId()))){
					   throw new LogicException("登陆账户重复!");
				   }
			  }else{
				  //新增
				  user.setNo(this.getNextNo());  //员工编号生成 
				  if(loginUser!=null){
					  throw new LogicException("登陆账户重复!");
				  }
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
		//密码
		if(StringUtils.isNotBlank(user.getPassword())){
			user.setPassword(entryptPassword(user.getPassword()));
		}else{
			if(StringUtils.isNotBlank(user.getLoginName())){
			 user.setPassword(entryptPassword(user.getLoginName()+"abc"));//默认密码
			}
		}
		// 处理用户角色，角色组
		getRoleAndGroup(user);
		//性别
		if (StringUtils.isNotEmpty(user.getSex())) {
			SysUserExtend sysUserExtend = new SysUserExtend();
		    sysUserExtend.setSex(user.getSex());
			 user.setSysUserExtend(sysUserExtend);
		}
		/*默认为普通用户*/
		user.setUserType(Global.USERADMIN);
		saveUser(user);
		return true;
	}
	
	/**
	 * 插入员工相关表信息
	 * 
	 * @param user
	 * @return 
	 * @author mhb
	 * @Date 2017-10-19 11:15:10
	 */
	public void saveUser(User user){
		boolean flag = true;// 验证用户一个用户可以多个企业
		user.setEnterpriseMarking(EnterpriseUtils.getCurrentUserEnter());
		if (StringUtils.isBlank(user.getId())) {
			preInsert(user);
			dao.insert(user);
		} else {
			if (flag) {
				// 更新用户数据
				preUpdate(user);
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
				sysEnterpriseUserService.deleteByUser(sysEnterpriseUser);
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
	 * @return add mhb 2017-10-19 11:15:10
	 */	
	public void getRoleAndGroup(User user) {
		if(StringUtils.isNotEmpty(user.getRoleAiId())){
			String[] arrayList = user.getRoleAiId().split(",");
			user.getRoleGroupList().clear();
			user.getRoleList().clear();
			//效验角色 角色组信息
			for (int i = 0; i < arrayList.length; i++) {
				if(!arrayList[i].equals("0")){
				boolean flag=false;
				if(roleDao.get(arrayList[i])!=null){
					user.getRoleList().add(new Role(arrayList[i]));
					flag=true;
				  continue;
				}
				
				if(sysRoleGroupDao.get(arrayList[i])!=null){
					SysRoleGroup sysRoleGroup = new SysRoleGroup();
					sysRoleGroup.setId(arrayList[i]);
					user.getRoleGroupList().add(sysRoleGroup);
					flag=true;
				  continue;
				}
				if(!flag){
				 throw new LogicException("角色信息数据异常!");  
				}
			  }
			}
		}
	}
	
	@Override
	public Object deleteUser(String id) {
		if (StringUtils.isBlank(id) || dao.get(id) == null) {
			throw new LogicException("用户不存在!");
		}
		User user = dao.get(id);
		if (!userCanBeDeleted(user)) {
			throw new LogicException("不允许删除管理员用户或该用户已经删除或已禁用!");
		}
		
		dao.delete(new User(id));
		return true;
	}
	
	private boolean userCanBeDeleted(User user) {
		//是admin，不能被删除
		if (User.isAdmin(user.getId())) {
			return false;
		}
		
		//是企业管理员不能被删除
		if (user.getUserType().equals(Global.ATENTERPRISEADMIN)) {
			return false;
		}
		//当前登陆的用户不能删除
		/*if(UserUtils.getCurrentUserId().equals(user.getId())){
			return false;
		}*/
		return true;
		
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
		User user = dao.get(id);
		List<AtEnterpriseInfo> companyLists = atEnterpriseInfoService.findCompanyByUserId(id,delFlag);
		user.setEnterpriseInfoLists(companyLists);
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
	public Object updateUserLoginFlag(Map<String,Object> maps) {
		
		String id = maps.get("id").toString();
		String flag = maps.get("loginFlag").toString();
		
		String loginFlag = "0";
		boolean adminFlag = false ;
		if (User.isAdmin(id) && flag.equals("0")) {
			adminFlag = true ;
			
		} else if (!User.isAdmin(id) && flag.equals("0")) {
			loginFlag = "1" ;
		}else if(!User.isAdmin(id) && flag.equals("1")){
			loginFlag = "0" ;
		}else{
			
		}
		if(!adminFlag){
			int count = dao.updateUserLoginFlag(id, loginFlag);
			if(count > 0){
				return true ;
			}
		}
		return false ;
	}
	
	@Override
	public Object importData(List<User> list) {
		List<User> userList=checkUserList(list);
		   for (User user : userList) {
			   user.setNo(getNextNo());
			   saveUser(user);
		}
		return true;
	}

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
		    
		    //验证手机号
			if(StringUtils.isBlank(user.getLoginName())){
				 if(!StringUtils.check(user.getLoginName(), StringUtils.REGEX_CHECK_PHONE)){
				       throw new LogicException("第"+num+"条数据手机号不合法!");
				 }
				 user.setMobile(user.getLoginName());
			}
			
			//性别
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
		   }else{
			   //取默认组织机构
				Office officeDefault= new Office();
				officeDefault.setIsDefault("1");
				List<Office> officeListDefault= officeDao.findAllList(officeDefault);
				if(!CollectionUtils.isEmpty(officeListDefault)){
					 user.setOfficeList(officeListDefault);
				}
		   }
		   //普通用户
		   user.setUserType(Global.USERADMIN);
		   newList.add(user); 
	}
		return newList;
	}
	/*批量导入效验角色,角色组信息*/
	private void checkRole(User user, int num) {
		//查询企业标示下所有角色信息
		Role r =new Role();
		r.setEnterpriseMarking(EnterpriseUtils.getCurrentUserEnter());
		List<Role> roleList = roleDao.findAllList(r);
		//查询企业标示下所有角色组信息
		SysRoleGroup srg= new SysRoleGroup();
		srg.setEnterpriseMarking(EnterpriseUtils.getCurrentUserEnter());
		List<SysRoleGroup> sysRoleGroupList =sysRoleGroupDao.findList(srg);
		
		//获取批量导入角色组  角色信息      格式 eg:淘丁-系统管理员,23423|淘丁-23423,fdf 
		String roleGroups = user.getRoleNames().replaceAll(","," ").replaceAll("\\|", ",");
		String[] roleGroupNames =roleGroups.split(",");
		for (String str : roleGroupNames) {
			if (str.indexOf("-") != -1) {
				//获取角色组名称
				String roleGroupName = str.substring(0,str.indexOf("-"));
				//获取角色名称
				String[] roleNames = str.substring(str.indexOf("-") + 1,str.length()).replace(" ", ",").split(",");
				//验证角色组信息是否匹配
				boolean falgRoleGroup = false;
				for (SysRoleGroup sysRoleGroup : sysRoleGroupList) {
					if (sysRoleGroup.getRoleGroupName().equals(roleGroupName)) {
						//去掉重复的角色组信息
						if(!user.getRoleGroupList().contains(sysRoleGroup)){
							user.getRoleGroupList().add(sysRoleGroup);
						}
						falgRoleGroup = true;
					  break;
					}
				}
				//提示有误角色组信息
				if(!falgRoleGroup){
					user.getRoleGroupList().clear();
					throw new LogicException("第"+num+"角色组信息验证有误!");
				}
				//验证角色信息是否匹配
				for (String str1 : roleNames) {
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
					//提示有误角色信息
					if(!falgRole){
						user.getRoleList().clear();
						throw new LogicException("第"+num+"角色信息验证有误!");
					}
				}
				
			}
		}
		  //取默认角色信息
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
	/*效验组织机构*/
	private void checkOffice(User user,int num) {
		//去掉重复的组织机构
		 String[] arrayOfficeNames=StringUtils.split(user.getOfficeNames(),",");
		 List<String> officeNamesList = new ArrayList<>();  
		 boolean flag;  
		 for(int i=0;i<arrayOfficeNames.length;i++){  
		     flag = false;  
		     for(int j=0;j<officeNamesList.size();j++){  
		         if(arrayOfficeNames[i].equals(officeNamesList.get(j))){  
		             flag = true;  
		             break;  
		         }  
		     }  
		     if(!flag){  
		    	 officeNamesList.add(arrayOfficeNames[i]);  
		     }  
		 } 
		 //与数据库已有组织机构匹配
		 for (String str : officeNamesList) {
			 Office office=new  Office();
			 office.setName(str);
			 office.setEnterpriseMarking(EnterpriseUtils.getCurrentUserEnter());
			 List<Office> officeList= officeDao.findAllList(office);
			 if(officeList.size()<=0){
				 user.getOfficeList().clear();
				 throw new LogicException("第"+num+"组织机构信息验证有误!");
			 }else{ 
				 /*组织机构名称多条,取第一调组织机构*/
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
	@Override
	public String getNextNo() {
		 String no=dao.findUserEnterpriseMarkingForMaxNo(EnterpriseUtils.getCurrentUserEnter());
		 if(StringUtils.isEmpty(no)){
			 return DEFAULT_USER_NO;
		 }
		 try {
			 	Integer NextNoInteger=Integer.valueOf(no)+1;
				 if(NextNoInteger.toString().length()>=DEFAULT_USER_NO.length()){
					 return NextNoInteger.toString();
				 }
		    return DEFAULT_USER_NO.substring(0, DEFAULT_USER_NO.length()-NextNoInteger.toString().length())+NextNoInteger.toString();
		    } catch (Exception e) {
			 throw new LogicException("员工工号生成数据异常!");
		    }
	}
	@Override
	public Object initPassWord(String id) {
		if(StringUtils.isEmpty(id)){
			 throw new LogicException("数据异常!");
		}
		if(dao.get(id)==null){
			throw new LogicException("员工信息不存在!");
		}
		User user= new User();
		user.setId(id);
		user.setPassword(entryptPassword(password));
		int num=dao.updatePasswordById(user);
		return true;
		 
	}
	
	// 获取手机验证码(更换手机号)
	@Override
	public Object updateMobileGetCode(String mobile) {
		String regexMobile = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
		 Pattern p = Pattern.compile(regexMobile);
		 Matcher m = p.matcher(mobile);
		 boolean result = m.find();
		 System.out.println(result + "==========================");
		if(result){
			User user = dao.findUserByMobile(mobile);
			if(user != null){
				throw new LogicException("手机号码已存在");
			}else {
				return user;
			}
		}else {
			throw new LogicException("手机号码格式不正确");
		}
	}
	
	
	// 修改用户密码(通过手机修改密码)
	@Override
	public Map<Object, String> updatePasswordByPhone(String loginName, String validateCode,
			String newPassword) {
		if(StringUtils.isEmpty(loginName) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(validateCode)){
			throw new LogicException("数据异常");
		}
		Map<Object, String> map = new HashMap<Object, String>();
//		UserUtils.getCurrentUserId()
		User user = dao.findUserByLoginName(loginName);
		System.out.println("======================================================");
		if(user == null){
			map.put("msg", "用户不存在");
			map.put("flag", "false");
			throw new LogicException("用户不存在");
		}
		// 从 session 获取短信验证码
		//CacheMapUtils cacheMapUtils = CacheMapUtils.getInstance();
		Object o  = CacheMapUtils.getValue(user.getMobile());
		String validCode = null;
		if(null != o) {
			validCode = o.toString();
		}
		System.out.println("从缓冲中获取[" + user.getMobile() + "]的验证码是：" + validCode);
		if(validCode != null){
			if(!validCode.equals(validateCode)){
				map.put("msg", "短信验证码不正确");
				map.put("flag", "false");
				throw new LogicException("短信验证码不正确");
			}
		}
		if(user != null){
			String pwd = entryptPassword(newPassword);
			dao.updatePasswordByPhone(pwd, loginName);
			map.put("flag", "true");
		}
		return map;
	}
	
	// 获取验证码(找回密码)
	@Override
	public Object findPasswordGetCode(String mobile) {
		if(StringUtils.isEmpty(mobile)){
			throw new LogicException("数据异常");
		}
		User user = dao.findUserByMobile(mobile);
		if(user == null){
			throw new LogicException("手机号码不存在");
		}else {
			return user;
		}
	}
	
	@Override   //根据用户名、手机号、邮箱 获取用户信息
	public User findUserInfo(String info) {
		// 逻辑
		User user = (User)dao.findUserInfo(info);
		if(user == null){
			throw new LogicException("员工信息不存在!");
		}
		return user;
	}
	@Override    //修改用户密码
	public boolean updateUserPassword(String loginName,String oriPassword,String newPassword) {
		boolean isSuccess = false;
		User user = findUserInfo(loginName);
		if(user != null){
			if (!UserUtils.validatePassword(oriPassword, user.getPassword())) {
				//原密码输入错误
				isSuccess = false;
			} else{
				//修改密码
				int i = dao.updateUserPassword(loginName,entryptPassword(newPassword));
				if(i > 0){
					isSuccess = true;
				}else{
					isSuccess =false;
				}
			} 
		}else{
			//用户不存在；
			isSuccess = false;
		}
		return isSuccess;
	}
	
	@Override
	// 验证原密码
	public Object validOriPassword(String name, String password) {
		if (StringUtils.isEmpty(password) || StringUtils.isEmpty(name)) {
			throw new LogicException("参数异常");
		}
		// 获取用户 id
		System.out.println(UserUtils.getCurrentUserId()
				+ "+=================================================");
		User user = dao.getUserById(UserUtils.getCurrentUserId());
		if (user != null) {
			if (!UserUtils.validatePassword(password, user.getPassword())) {
				throw new LogicException("密码不正确");
			} else {
				return true;
			}
		} else {
			throw new LogicException("未获取到用户 id");
		}
	}
	
	
	
}
