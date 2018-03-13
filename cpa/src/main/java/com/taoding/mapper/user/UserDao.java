package com.taoding.mapper.user;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.office.Office;
import com.taoding.domain.user.User;

@Repository
@Mapper
public interface UserDao extends CrudDao<User> {
	

	/**
	 * 分页+按条件查询用户
	 * @param queryMap
	 * @return
	 */
	public List<User> findAllUserList(Map<String,Object> queryMap);
	
	
	/**
	 * 根据登录名称查询用户
	 * @param loginName
	 * @return
	 */
	public List<User> getByLoginName(User user);

	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	public List<User> findUserByOfficeId(User user);
	
	/**
	 * 查询全部用户数目
	 * @return
	 */
	public long findAllCount(User user);
	
	/**
	 * 更新用户密码
	 * @param user
	 * @return
	 */
	public int updatePasswordById(User user);
	
	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(User user);

	/**
	 * 删除用户角色关联数据
	 * @param user
	 * @return
	 */
	public int deleteUserRole(User user);
	/**
	 * 插入用户角色关联数据
	 * @param user
	 * @return
	 */
	public int insertUserRole(User user);
	
	
	/**
	 * 插入用户角色关联数据
	 * @param user
	 * @return
	 */
	public int insertUserRoleForRoleGroup(@Param("roleId") String roleId,@Param("userList") List<String> userList);
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);
	
	/**
	 * 批量插入用户角色关联数据
	 * @param user
	 * @return
	 */
	public int insertUserRoleBatch(List<User> userList);
	
	/**
	 * 查询用户角色关联表数据
	 * @param user
	 * @return
	 */
	public long findUserRoleCount(User user);

	/**
	 * 查询所有用户的工号
	 * @param User
	 * @return Long
	 */
	public Integer findCount(User user);
	
	
	/**
	 * 删除用户角色组关联数据
	 * @param user
	 * @return
	 * 2017年10月19日11:31:40
	 * add lixc
	 */
	public int deleteUserRoleGroup(User user);
	
	
	/**
	 * 插入用户角色关联数据
	 * @param user
	 * @return
	 * 2017年10月19日11:31:54
	 * add lixc
	 */
	public int insertUserRoleGroup(User user);
	
	/**
	 * 根据企业注册的公司账户查询用户是否存在
	 * @param user
	 *  * add mhb 
	 * 2017-10-28 11:06:26
	 * @return
	 */
	public User findEnterpriseforLoginName(User user);

	public void insertUserEnterprise(User userEnterprise);
	
	
	/**
	 * 员工查询
	 * @param user
	 * @return
	 */
	public List<User> findEmpUserList(Map<String,Object> queryMap);
	/**
	 * 查询组织机构员工
	 * 
	 * @param office
	 * @return add
	 */
	public List<User> findOfficeForUser(Map<String,Object> queryMap);
	
	/**
	 * 更改用户启用/禁用状态
	 * @param id
	 * @param loginFlag
	 * @return
	 */
	public int updateUserLoginFlag(@Param("id") String id,@Param("loginFlag") String loginFlag);


	public List<Office> selectOfficeByUserId(User userTemp);
}
