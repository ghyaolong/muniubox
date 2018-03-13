package com.taoding.service.user;

import java.util.List;

import org.springframework.stereotype.Service;

import com.taoding.common.service.CrudService;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseInfo;
import com.taoding.domain.office.Office;
import com.taoding.domain.user.SysEnterpriseUser;
import com.taoding.domain.user.User;
import com.taoding.mapper.user.SysEnterpriseUserDao;




/**
 * 企业，组织，用户管理Service
 * 
 * @author mhb
 * @version 2017-10-26
 */
@Service
public interface SysEnterpriseUserService extends  CrudService<SysEnterpriseUserDao, SysEnterpriseUser> {

	/**
	 * 员工调机构
	 * 
	 * @param sysEnterpriseUser
	 * @param oldOfficeId
	 * @return  
	 */
	 
	public void updateOfficeUser(String userId, String newOfficeId, String oldOfficeId);
	/**
	 * 删除员工企业关联信息表信息
	 * 
	 * @param sysEnterpriseUser
	 * @param  
	 * @return  
	 */
	public void deleteRealy(SysEnterpriseUser sysEnterpriseUser);
	
	/**
	 * 导入处理插入 
	 * @param enterpriseOfficeList
	 * @param atEnterpriseInfo
	 * @param user
	 */
	public void saveSysEnterpriseUser(List<Office> enterpriseOfficeList,
			AtEnterpriseInfo atEnterpriseInfo,User user);
}