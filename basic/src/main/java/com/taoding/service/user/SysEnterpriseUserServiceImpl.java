package com.taoding.service.user;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.EnterpriseUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseInfo;
import com.taoding.domain.office.Office;
import com.taoding.domain.user.SysEnterpriseUser;
import com.taoding.domain.user.User;
import com.taoding.mapper.office.OfficeDao;
import com.taoding.mapper.user.SysEnterpriseUserDao;
import com.taoding.mapper.user.UserDao;

/**
 * 企业，组织，用户管理Service
 * 
 * @author mhb
 * @version 2017-10-26
 */
@Service
@Transactional
public class SysEnterpriseUserServiceImpl extends
		DefaultCurdServiceImpl<SysEnterpriseUserDao, SysEnterpriseUser>
		implements SysEnterpriseUserService {

	@Autowired
	private SysEnterpriseUserDao sysEnterpriseUserDao;
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private UserDao userDao;

	public Object updateOfficeUser(String userId, String newOfficeId, String oldOfficeId) {
		if(User.isAdmin(userId)||StringUtils.isEmpty(userId)||StringUtils.isEmpty(newOfficeId)||StringUtils.isEmpty(oldOfficeId)){
			throw new LogicException("超级管理员不能调机构或员工相关信息有误!");
		}
		User user =userDao.get(userId);
		if(user==null){
			throw new LogicException("员工信息不存在!");
		}
		 if(user.getDelFlag().equals(Global.YES)||user.getLoginFlag().equals(Global.NO)){
		    throw new LogicException("员工信息不存在或已删除禁用!");
		} 
		SysEnterpriseUser sysEnterpriseUser= new SysEnterpriseUser();
		sysEnterpriseUser.setUser(new User(userId));
		sysEnterpriseUser.setOffice(new Office(newOfficeId));
		sysEnterpriseUser.setEnterpriseMarking(EnterpriseUtils.getCurrentUserEnter());
		/* 查询员工是否已属于调入机构的员工 */
		List<SysEnterpriseUser> newList = sysEnterpriseUserDao.findList(sysEnterpriseUser);
		if (newList.size() > 0) {
			throw new LogicException("此员工已属于调入机构");
		}
		sysEnterpriseUser.setOffice(new Office(oldOfficeId));
		/* 查询调入前机构信息 */
		List<SysEnterpriseUser> oldlist = sysEnterpriseUserDao.findList(sysEnterpriseUser);
		for (SysEnterpriseUser sysEn : oldlist) {
			if (StringUtils.isNotBlank(sysEn.getId())) {
				sysEn.setId(sysEn.getId());
				sysEn.setOffice(new Office(newOfficeId));
				/* 修改组织机构员工表 */
				sysEnterpriseUserDao.update(sysEn);
			}
		}
		return true;
	}

	@Transactional
	public void deleteByUser(SysEnterpriseUser sysEnterpriseUser) {
		dao.deleteByUser(sysEnterpriseUser);
	}
	/**
	 * 导入处理插入 
	 * @param enterpriseOfficeList
	 * @param atEnterpriseInfo
	 * @param user
	 */
	public void saveSysEnterpriseUser(List<Office> enterpriseOfficeList,
			AtEnterpriseInfo atEnterpriseInfo,User user) {
		try {
			List<SysEnterpriseUser>  sysEnterpriseUserList= new LinkedList<SysEnterpriseUser>();
			 for (Office office : enterpriseOfficeList) {
				 SysEnterpriseUser sysEnterpriseUser = new SysEnterpriseUser();
				     sysEnterpriseUser.setUser(user);
					 sysEnterpriseUser.setEnterpriseId(atEnterpriseInfo.getId());
					 sysEnterpriseUser.setOffice(office);
					 //XXX PreInsert
					 preInsert(sysEnterpriseUser);
					 sysEnterpriseUserList.add(sysEnterpriseUser);
				}
			 dao.insertSysEnterpriseUser(sysEnterpriseUserList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}