package com.taoding.mapper.office;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.TreeDao;
import com.taoding.common.entity.TreeNode;
import com.taoding.domain.office.Office;
import com.taoding.domain.user.User;

/**
 * 机构DAO接口
 */
@Repository
@Mapper
public interface OfficeDao extends TreeDao<Office> {
	/**
	 * 查询子机构
	 * 
	 * @param id
	 * @return  
	 */

	public List<Office> findOfficeList(@Param("id") String id);

	/**
	 * 组织机构是否为顶级
	 * 
	 * @param office
	 * @return add
	 */

	public List<Office> findTopOfficeList(Office office);

	public List<Office> findEnterpriseTemplateForOffice(Office office);

	public void insertEnterpriseOfficeList(
			@Param("enterpriseOfficeList") List<Office> enterpriseOfficeList);
	/**
	 * 查询组织机构员工
	 * 
	 * @param office
	 * @return add
	 */
	public List<User> findOfficeForUser(Office office);

	/**
	 * 查询组织机构父级机构
	 * 
	 * @param id
	 * @return 
	 */
	public Office getTreeOffice(String id);

	public void updateOffice(Office office);
	/**
	 * 根据父级Id,企业标示查询组织机构顶级ID
	 * 
	 * @param office
	 * @return add
	 */
	public Office findTreeOfficeById(@Param("parentId") String parentId,@Param("enterpriseMarking")String enterpriseMarking);
}
