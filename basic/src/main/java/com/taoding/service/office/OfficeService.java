package com.taoding.service.office;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.taoding.common.entity.TreeNode;
import com.taoding.common.service.CrudService;
import com.taoding.domain.office.Office;
import com.taoding.mapper.office.OfficeDao;

/**
 * 机构Service
 */
public interface OfficeService extends CrudService<OfficeDao, Office> {

	/**
	 * 获取机构树形机构数据
	 * @return
	 */
	public TreeNode treeOffice(Map<String,Object> queryMap);

	/**
	 * 组织机构添加
	 * 
	 * @param office
	 * @return
	 */
	public Object nextLowerSave(Office office);

	/**
	 * 组织机构删除
	 * 
	 * @param office
	 * @return
	 */
	public Object deleteOffice(String officeId);

	/**
	 * 组织机构禁用
	 * 
	 * @param id 
	 * @param useable 1启用  0禁用
	 * 
	 */
	public Object deleteAtOffice(String id,String useable);
	/**
	 * 查询部门下的用户
	 * 
	 * @param id
	 * @param queryCondition
	 * @return
	 */
	public Object findOfficeForUser(Map<String,Object> queryMap);

	/**
	 * 根据id查询组织机构信息
	 * 
	 * @param id
	 * @return
	 */
	public Object getEditOffice(String id);

	/**
	 * 插入新建企业组织机构
	 * 
	 * @param enterpriseOfficeList
	 *            add mhb 2017年10月28日14:10:38
	 */
	public void saveEnterpriseOfficeList(List<Office> enterpriseOfficeList,
			String enterpriseTemplate, String strId);
	/**
	 * 组织机构编辑
	 * 
	 * @param office
	 * @return 
	 * @author mhb 
	 * @date 2017年11月08日11:54:56
	 */
	public Object edit(Office office);
	
	/**
	    * @Description: TODO(查询用户组织结构树 调用 mhb组织结构树) 
	    * @return TreeNode 返回类型    
	    * @throws 
	    * @author lixc
	    * @date 2017年11月23日
	     */
	public TreeNode treeOfficeAndUser();
}
