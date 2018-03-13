package com.taoding.mapper.menu;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.menu.Menu;
 
@Repository
@Mapper
public interface MenuDao extends CrudDao<Menu> {

	public List<Menu> findByParentIdsLike(Menu menu);

	/**
	 *  返回用户所偶的菜单那
	 * @param menu
	 * @return
	 */
	public List<Menu> findByUserId(@Param("userId") String userId ,
			                       @Param("enterpriseBusinessId") String enterpriseBusinessId,
			                       @Param("DEL_FLAG_NORMAL") String DEL_FLAG_NORMAL);
	
	public int updateParentIds(Menu menu);
	
	public int updateSort(Menu menu);
	
	/**
	 * 基础资源获得菜单列表
	 * @param menu
	 * @return
	 * add lixc 
	 * 2017年10月20日14:25:13
	 */
	public List<Menu> findByResourceId(Menu menu);

	public void insertRoleMenuEnterpriseTemplate(@Param("id") String id,@Param("enterpriseTemplate") String enterpriseTemplate);
	
	/**
	 * 根据userId和enterpriseId查找该用户以某个企业的身份所能使用的所有权限
	 * @param userId
	 * @param enterpriseId
	 * @return
	 */
	public List<Menu> listMenuByUserId(@Param("userId") String userId);
	
}
