package com.taoding.service.user;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.taoding.common.service.CrudService;
import com.taoding.domain.user.User;
import com.taoding.mapper.user.UserDao;

public interface UserService extends CrudService<UserDao, User> {

	/**
	 * 分页+按条件查询用户
	 * 
	 * @param user
	 * @param queryMap
	 * @return
	 */
	public PageInfo<User> findAllUserByPage(Map<String,Object> queryMap);

	/**
	 * 员工查询列表
	 * 
	 * @param user
	 * @return
	 */
	public PageInfo<User> findAiUser(Map<String,Object> queryMap);

	/**
	 * 员工添加
	 * 
	 * @param user
	 * @return
	 */

	public Object insertUser(User user);

	/**
	 * 员工删除
	 * 
	 * @param user
	 * @return
	 */
	public Object deleteUser(String id);
	/**
	 * 根据企业注册的公司账户查询用户是否存在
	 * @param user
	 *  * add mhb 
	 * 2017-10-28 11:06:26
	 * @return
	 */
	public User findEnterpriseforLoginName(String loginName);
	
	/**
	 * 插入模板企业用户表数据
	 * @param atEnterpriseInfo
	 * @return
	 * add mhb
	 */
	public void saveUserEnterprise(User user);

	/**
	 * 插入模板企业角色菜单表数据
	 * @param id
	 * @return
	 * add mhb
	 * 2017年10月28日12:07:50
	 */
	public void saveRoleMenuEnterpriseTemplate(String id,String enterpriseTemplate);

	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return
	 * @author lixc
	 * @date 
	 */
	public User getUserByLoginName(String loginName);
	
	
	/**
	 * 查询用户信息(包括公司信息)
	 * @param id,delFlag
	 * @return
	 */
	public User getUserInfo(String id,String delFlag);
	
	/**
	 * 更改用户启用/禁用状态
	 * @param id
	 * @param delFlag
	 * @return
	 */
	public Object updateUserLoginFlag(Map<String,Object> maps);
	/**
	 * 导入
	 * @param list
	 * @return
	 */
	public Object importData(List<User> list);
	/**
	 * 获取员工工号
	 * @return String
	 * @author mhb
	 */
	public String getNextNo();
	/**
	 * 恢复初始密码
	 * @return String 员工id
	 * @author mhb
	 */
	public Object initPassWord(String id);
	
	
	/**
	 * 获取手机验证码(更换手机号)
	 * @param mobile 手机号
	 * @return 
	 */
	public Object updateMobileGetCode(String mobile);
	
	/**
	 * 获取验证码(找回密码)
	 * @param mobile 手机号
	 * @return
	 */
	public Object findPasswordGetCode(String mobile);
	
	/**
	 * 修改用户密码(通过手机修改密码)
	 * @param loginName 用户名
	 * @param validateCode 验证码
	 * @param newPassword 新密码
	 * @return
	 */
	public Map<Object, String> updatePasswordByPhone(String loginName, String validateCode, String newPassword);
	
	/**
	 * 根据用户名、手机号、邮箱 获取用户信息
	 * @param info 参数： 用户名、手机号、邮箱
	 * @return
	 */
	public User findUserInfo(String info);
	
	/**
	 * 修改用户密码 (通过用户名修改密码)
	 * @param loginName 参数 :用户名
	 * @return
	 */
    public boolean updateUserPassword(String loginName,String oriPassword,String newPassword);
    
    /**
	 * 根据用户 id 获取用户信息  (验证原密码)
	 * @param id id
	 * @return
	 */
	public Object validOriPassword(String name, String password);
	
}
