package com.taoding.service.office;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public TreeNode treeOffice();

	/**
	 * 组织机构添加
	 * 
	 * @param office
	 * @return
	 */
	public void nextLowerSave(Office office);

	/**
	 * 组织机构删除
	 * 
	 * @param office
	 * @return
	 */
	public void deleteOffice(String officeId);

	/**
	 * 组织机构禁用
	 * 
	 * @param office
	 * 
	 */
	public void deleteAtOffice(Office office);
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
}
