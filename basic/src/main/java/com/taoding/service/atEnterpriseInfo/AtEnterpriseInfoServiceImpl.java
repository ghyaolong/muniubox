
package com.taoding.service.atEnterpriseInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.DictUtils;
import com.taoding.common.utils.Digests;
import com.taoding.common.utils.Encodes;
import com.taoding.common.utils.EnterpriseUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.common.utils.QueryConditionFieldsArray;
import com.taoding.common.utils.QueryConditionSql;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UserUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseInfo;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseInfoDetail;
import com.taoding.domain.atEnterpriseInfo.AtLegalPersonInfo;
import com.taoding.domain.atEnterpriseInfo.AtLinkman;
import com.taoding.domain.office.Office;
import com.taoding.domain.role.Role;
import com.taoding.domain.role.SysRoleGroup;
import com.taoding.domain.user.User;
import com.taoding.mapper.atEnterpriseInfo.AtEnterpriseInfoDao;
import com.taoding.mapper.atEnterpriseInfo.AtEnterpriseInfoDetailDao;
import com.taoding.mapper.atEnterpriseInfo.AtLegalPersonInfoDao;
import com.taoding.mapper.atEnterpriseInfo.AtLinkmanDao;
import com.taoding.service.office.OfficeService;
import com.taoding.service.role.SysRoleGroupService;
import com.taoding.service.user.SysEnterpriseUserService;
import com.taoding.service.user.UserService;
import com.taoding.service.user.UserServiceImpl;


/**
 * 企业信息Service
 * @author Lin
 * @version 2017-09-28
 */
@Service
@Transactional
public class AtEnterpriseInfoServiceImpl extends DefaultCurdServiceImpl<AtEnterpriseInfoDao, AtEnterpriseInfo>
	implements AtEnterpriseInfoService{
	
	//用于密码加密
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	
	//公司的默认编码
	private static final String default_no = "000000001";

	@Autowired
	private AtEnterpriseInfoDetailService atEnterpriseInfoDetailService;
	
	@Autowired
	private AtLegalPersonInfoService atLegalPersonInfoService;
	
	@Autowired
	private AtLinkmanService atLinkmanService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private SysEnterpriseUserService sysEnterpriseUserService;
	
	@Autowired
	private SysRoleGroupService sysRoleGroupService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AtEnterpriseInfoDetailDao detailDao;
	
	@Autowired
	private AtLegalPersonInfoDao legalPersonInfoDao;
	
	@Autowired
	private AtLinkmanDao linkmanDao; 
	
	// 文件上传路径
	@Value("${initPassword.password}")
	private String password;
	/**
	 * 保存公司信息
	 * @param atEnterpriseInfo
	 * add csl 
	 * @date 2017-11-15 16:30:36
	 */
	@Transactional(readOnly = false)
	public void save(AtEnterpriseInfo atEnterpriseInfo) {
		//如果公司编码为空时，则自动生成公司编码
		if (StringUtils.isEmpty(atEnterpriseInfo.getCompanyCode())) {
			atEnterpriseInfo.setCompanyCode(this.GetNextNo());
		}
		//默认自动生成公司账号
		
		
		super.save(atEnterpriseInfo);
		if (null!=atEnterpriseInfo.getAtEnterpriseInfoDetail()) {
			atEnterpriseInfo.getAtEnterpriseInfoDetail().setAtEnterpriseInfo(atEnterpriseInfo);
				if(null!=atEnterpriseInfo.getId()){
					atEnterpriseInfo.getAtEnterpriseInfoDetail().setAtEnterpriseInfoId(atEnterpriseInfo.getId());
				}
			atEnterpriseInfoDetailService.save(atEnterpriseInfo.getAtEnterpriseInfoDetail());		
		if (null!=atEnterpriseInfo.getAtLegalPersonInfo()) {
			atEnterpriseInfo.getAtLegalPersonInfo().setAtEnterpriseInfo(atEnterpriseInfo);
				if(null!=atEnterpriseInfo.getId()){
					atEnterpriseInfo.getAtLegalPersonInfo().setAtEnterpriseInfoId(atEnterpriseInfo.getId());
				}
			atLegalPersonInfoService.save(atEnterpriseInfo.getAtLegalPersonInfo());
			}
		if(null!=atEnterpriseInfo.getAtLinkman()){
			if(null!=atEnterpriseInfo.getId()){
				atEnterpriseInfo.getAtLinkman().setAtEnterpriseInfoId(atEnterpriseInfo.getId());
			}
			atEnterpriseInfo.getAtLinkman().setAtEnterpriseInfo(atEnterpriseInfo);
			atLinkmanService.save(atEnterpriseInfo.getAtLinkman());
		}
			//////////////////////////////////////*****创建企业基础模板******/////////////////////////////////////////
			//查询默认模板企业标示
			String enterpriseTemplate=EnterpriseUtils.getEnterpriseTemplate();
			//插入用户表数据
			User userEnterprise = new User();
			//设置新的用户的类型为企业管理员
			userEnterprise.setUserType(Global.ATENTERPRISEADMIN);
			userEnterprise.setAtEnterpriseInfo(atEnterpriseInfo);
			userEnterprise.setEnterpriseMarking(atEnterpriseInfo.getId());
			/////////此处需要给用户默认的密码，以前的通过配置文件获取默认的密码，需要处理/////////////
			
			if(StringUtils.isNotBlank(userEnterprise.getPassword())){
				userEnterprise.setPassword(entryptPassword(userEnterprise.getPassword()));
		        }else{
		        	userEnterprise.setPassword(entryptPassword(atEnterpriseInfo.getAtEnterpriseInfoDetail().getCompanyPhone()+"abc"));  //默认密码
		        }
			//获取员工工号
			userEnterprise.setNo(UserServiceImpl.DEFAULT_USER_NO);
			//员工添加初始密码
			userEnterprise.setPassword(entryptPassword(password));  
			userService.saveUserEnterprise(userEnterprise);
			//部门表插入数据			
			 List<Office> enterpriseOfficeList= new LinkedList<Office>(); 
			 officeService.saveEnterpriseOfficeList(enterpriseOfficeList,enterpriseTemplate,atEnterpriseInfo.getId());
			//企业关联用户表插入数据
			sysEnterpriseUserService.saveSysEnterpriseUser(enterpriseOfficeList,atEnterpriseInfo,userEnterprise);
			//插入模板角色组    
		 	List<SysRoleGroup> roleGroupList= new LinkedList<SysRoleGroup>();
		 	sysRoleGroupService.saveEnterpriseTemplateSysRoleGroupList(roleGroupList,userEnterprise,enterpriseTemplate,atEnterpriseInfo.getId());
		 	//插入模板角色
		 	List<Role>   roleList =new LinkedList<Role>();
		 	sysRoleGroupService.saveEnterpriseTemplateSysRoleList(roleList,userEnterprise,enterpriseTemplate,atEnterpriseInfo.getId());
		 	//插入用户角色关联表
		 	sysRoleGroupService.saveEnterpriseTemplateSysUserRoleList(roleList); 
		 	//插入用户角色组关联表
			sysRoleGroupService.saveEnterpriseTemplateSysUserRoleGroupList(roleGroupList);
			//插入角色与菜单关系表
			userService.saveRoleMenuEnterpriseTemplate(atEnterpriseInfo.getId(),enterpriseTemplate);	
		}
	}
	
	/**
	 * 更改公司的是否可用的状态
	 * @param atEnterpriseInfo
	 * add csl 
	 * @date 2017-11-15 16:30:36
	 */
	@Transactional(readOnly=false)
	public int update(AtEnterpriseInfo atEnterpriseInfo){
		return dao.updateState(atEnterpriseInfo);
	}

	/**
	 * 查询所有的列表
	 * @param atEnterpriseInfo
	 * add csl 
	 * @date 2017-11-15 16:30:36
	 */
	@Override
	public PageInfo<AtEnterpriseInfo> findAllByPage(Map<String, Object> queryMap) {
		String isAll = queryMap.get("isAll").toString();
		if(StringUtils.isEmpty(isAll) || "false".equals(isAll)){
			//处理分页
			PageUtils.page(queryMap);
		}
		//添加查询条件
		String queryConditionVal = (String)queryMap.get("queryCondition");
		if(queryConditionVal != null && !"".equals(queryConditionVal)){
			queryMap.put("queryConditionSql", QueryConditionSql.getQueryConditionSql(QueryConditionFieldsArray.atEnterpriserFields, queryConditionVal));
		}
		List<AtEnterpriseInfo> atenterpriseInfoLists= dao.findAllByPage(queryMap);
		//获取字典数据
		for (AtEnterpriseInfo atEnterpriseInfoTemp : atenterpriseInfoLists) {
			if(atEnterpriseInfoTemp != null){
				//获取公司启禁用状态的字典数据
				if(StringUtils.isNotEmpty(atEnterpriseInfoTemp.getCompanyState())){
					atEnterpriseInfoTemp.setCompanyState(DictUtils.getDictLabel(atEnterpriseInfoTemp.getCompanyState(), "at_start_forbid", ""));
				}else{
					atEnterpriseInfoTemp.setCompanyState("");
				}
				//获取公司规模的字典数据
				if(StringUtils.isNotEmpty(atEnterpriseInfoTemp.getCompanyScale())){
					atEnterpriseInfoTemp.setCompanyScale(DictUtils.getDictLabel(atEnterpriseInfoTemp.getCompanyScale(), "at_customer_type", ""));
				}else{
					atEnterpriseInfoTemp.setCompanyScale("");
				}
				//获取公司客户数量的字典数据
				if(StringUtil.isNotEmpty(atEnterpriseInfoTemp.getCustomerNum())) {
					atEnterpriseInfoTemp.setCustomerNum(DictUtils.getDictLabel(atEnterpriseInfoTemp.getCustomerNum(), "at_customer_type", ""));
				}else{
					atEnterpriseInfoTemp.setCustomerNum("");
				}
				//获取公司所属行业的字典数据
				if(StringUtils.isNotEmpty(atEnterpriseInfoTemp.getAtEnterpriseInfoDetail().getIndustryInvolved())){
					atEnterpriseInfoTemp.getAtEnterpriseInfoDetail().setIndustryInvolved(DictUtils.getDictLabel(atEnterpriseInfoTemp.getAtEnterpriseInfoDetail().getIndustryInvolved(), "at_trades_type", ""));
				}else{
					atEnterpriseInfoTemp.getAtEnterpriseInfoDetail().setIndustryInvolved("");
				}
				//获取公司经营状态的字典数据
				if(StringUtils.isNotEmpty(atEnterpriseInfoTemp.getAtEnterpriseInfoDetail().getManageForm())){
					atEnterpriseInfoTemp.getAtEnterpriseInfoDetail().setManageForm(DictUtils.getDictLabel(atEnterpriseInfoTemp.getAtEnterpriseInfoDetail().getManageForm(), "at_manage_form", ""));
				}else{
					atEnterpriseInfoTemp.getAtEnterpriseInfoDetail().setManageForm("");
				}
				//获取核名认证的字典数据
				if(StringUtil.isNotEmpty(atEnterpriseInfoTemp.getIsNameAutherticate())) {
					atEnterpriseInfoTemp.setIsNameAutherticate(DictUtils.getDictLabel(atEnterpriseInfoTemp.getIsNameAutherticate(), "yes_no", ""));
				}else{
					atEnterpriseInfoTemp.setIsNameAutherticate("");
				}
			}
		}
		PageInfo<AtEnterpriseInfo> info = new PageInfo<AtEnterpriseInfo>(atenterpriseInfoLists);	
		return info;
	}
	
	@Override
	public List<AtEnterpriseInfo> findCompanyByUserId(String userId,String delFlag) {
		return dao.findCompanyByUserId(userId,delFlag);
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
	  
	  //获取当前最大的公司编码
	  public String GetNextNo(){
		  //查找企业的公司编号的最大号码
		  String companyCode = dao.findCompanyCodeMaxNo();
		  if (StringUtils.isEmpty(companyCode)) {
			return default_no;
		  }
		  try {
			Integer NextCompanyCodeInteger = Integer.valueOf(companyCode)+1;
			  if (NextCompanyCodeInteger.toString().length()>default_no.length()) {
				return NextCompanyCodeInteger.toString();
			  }
			  return default_no.substring(0, default_no.length()-NextCompanyCodeInteger.toString().length())+NextCompanyCodeInteger.toString();
		} catch (NumberFormatException e) {
			throw new LogicException("公司编号异常！");
		}
	  }

  /**
   * 获取AtEnterpriseInfo信息
   */
	@Override
	public AtEnterpriseInfo getInfo(String id) {
		return dao.getInfo(id);
	}

	/**
	 * 编辑公司管理详情页面中的信息
	 */
	@Override
	public void updateInfo(AtEnterpriseInfo atEnterpriseInfo) {
		if (StringUtils.isEmpty(atEnterpriseInfo.getId()) ) {
			throw new LogicException("修改的对象不存在！");
		}
		super.save(atEnterpriseInfo);
		if (atEnterpriseInfo.getAtEnterpriseInfoDetail() != null) {
			//通过atEnterpriseInfoId查找AtEnterpriseInfoDetail的id
			AtEnterpriseInfoDetail atEnterpriseInfoDetail = atEnterpriseInfoDetailService.getByAtEnterpriseInfoId(atEnterpriseInfo.getId());
			if (StringUtils.isEmpty(atEnterpriseInfoDetail.getId())) {
				throw new LogicException("添加的对象不存在！");
			}
			atEnterpriseInfo.getAtEnterpriseInfoDetail().setId(atEnterpriseInfoDetail.getId());
			detailDao.update(atEnterpriseInfo.getAtEnterpriseInfoDetail());		
			}else{
				atEnterpriseInfo.getAtEnterpriseInfoDetail().setAtEnterpriseInfo(atEnterpriseInfo);
				if(null!=atEnterpriseInfo.getId()){
					atEnterpriseInfo.getAtEnterpriseInfoDetail().setAtEnterpriseInfoId(atEnterpriseInfo.getId());
				}
				detailDao.insert(atEnterpriseInfo.getAtEnterpriseInfoDetail());
			}
		if (atEnterpriseInfo.getAtLegalPersonInfo() != null) {
			//通过atEnterpriseInfoId查找AtLegalPersonInfo的id
			AtLegalPersonInfo atLegalPersonInfo = atLegalPersonInfoService.getByAtEnterpriseInfoId(atEnterpriseInfo.getId());
			if (StringUtils.isEmpty(atLegalPersonInfo.getId())) {
				throw new LogicException("添加的对象不存在！");
			}
			atEnterpriseInfo.getAtLegalPersonInfo().setId(atLegalPersonInfo.getId());
			legalPersonInfoDao.update(atEnterpriseInfo.getAtLegalPersonInfo());
			}else{
				atEnterpriseInfo.getAtLegalPersonInfo().setAtEnterpriseInfo(atEnterpriseInfo);
				if(null!=atEnterpriseInfo.getId()){
					atEnterpriseInfo.getAtLegalPersonInfo().setAtEnterpriseInfoId(atEnterpriseInfo.getId());
				}
				legalPersonInfoDao.insert(atEnterpriseInfo.getAtLegalPersonInfo());
			}
		if(atEnterpriseInfo.getAtLinkman()!= null){
			AtLinkman atLinkman = atLinkmanService.getByAtEnterpriseInfoId(atEnterpriseInfo.getId());
			if (StringUtils.isEmpty(atLinkman.getId())) {
				throw new LogicException("添加的对象不存在！");
			}
			atEnterpriseInfo.getAtLinkman().setId(atLinkman.getId());
			linkmanDao.update(atEnterpriseInfo.getAtLinkman());
			}else{
				if(null!=atEnterpriseInfo.getId()){
					atEnterpriseInfo.getAtLinkman().setAtEnterpriseInfoId(atEnterpriseInfo.getId());
				}
				atEnterpriseInfo.getAtLinkman().setAtEnterpriseInfo(atEnterpriseInfo);
				linkmanDao.insert(atEnterpriseInfo.getAtLinkman());
			}
		
		}
	
 
}