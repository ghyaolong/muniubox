
package com.taoding.service.atEnterpriseInfo;

import java.util.List;
import java.util.Map;
import com.github.pagehelper.PageInfo;
import com.taoding.common.service.CrudService;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseInfo;
import com.taoding.mapper.atEnterpriseInfo.AtEnterpriseInfoDao;

/**
 * 企业信息Service
 * @author Lin
 * @version 2017-09-28
 */
public interface AtEnterpriseInfoService extends CrudService<AtEnterpriseInfoDao, AtEnterpriseInfo> {
	
	/**
	 * 更改企业的状态
	 * @param atEnterpriseInfo
	 * @return
	 */
	public int update(AtEnterpriseInfo atEnterpriseInfo);
	
	/**
	 * 分页+按条件查询用户
	 * @param maps
	 * @return
	 */
	public PageInfo<AtEnterpriseInfo> findAllByPage(Map<String,Object> maps);
	
	/**
	 * 根据用户ID查询公司信息
	 * @param userId
	 * @param delFlag
	 * @return
	 */
	List<AtEnterpriseInfo> findCompanyByUserId(String userId,String delFlag);
	
	/**
	 * 获取AtEnterpriseInfo的信息
	 * @param id
	 * @return
	 */
	public AtEnterpriseInfo getInfo(String id);
	
	/**
	 * 编辑详情页面的信息
	 * @param atEnterpriseInfo
	 * @return
	 */
	public void updateInfo(AtEnterpriseInfo atEnterpriseInfo);
}