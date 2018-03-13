package com.taoding.service.user;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
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

	public void insertUser(User user);

	/**
	 * 员工删除
	 * 
	 * @param user
	 * @return
	 */
	public void deleteUser(String id);

	/**
	 * 根据id查询员工信息
	 * 
	 * @param id
	 * @return
	 */
	public Object findUserById(String id);


	
	/**
	 * 根据企业注册的公司账户查询用户是否存在
	 * @param user
	 *  * add mhb 
	 * 2017-10-28 11:06:26
	 * @return
	 */
	public User findEnterpriseforLoginName(User user);
	
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
	public Object updateUserLoginFlag(User user);
	/**
	 * 导入
	 * @param list
	 * @return
	 */
	public void importData(List<User> list);
	
}
