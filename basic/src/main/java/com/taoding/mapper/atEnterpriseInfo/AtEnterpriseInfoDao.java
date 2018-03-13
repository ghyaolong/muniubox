
package com.taoding.mapper.atEnterpriseInfo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseInfo;

/**
 * 企业信息DAO接口
 * @author Lin
 * @version 2017-09-28
 */
@Repository
@Mapper
public interface AtEnterpriseInfoDao extends CrudDao<AtEnterpriseInfo> {

	/**
	 * 更新公司状态
	 * @param entity
	 * @return
	 * add csl
	 * 2017-10-20 16:57:16
	 */
	public int update(AtEnterpriseInfo atEnterpriseInfo);
	
	/**
	 * 分页+按条件查询用户
	 * @param queryMap
	 * @return
	 */
	public List<AtEnterpriseInfo> findAllByPage(Map<String,Object> queryMap);
	
	/**
	 * 根据用户ID查询公司信息
	 * @param userId
	 * @return
	 */
	public List<AtEnterpriseInfo> findCompanyByUserId(@Param("userId") String userId,@Param("delFlag")String delFlag);

	/**
	 * 查询公司最大的公司编码
	 * @return
	 */
	public String findCompanyCodeMaxNo();
	
	/**
	 * 更改公司的状态
	 * @param atEnterpriseInfo
	 * @return
	 */
	public int updateState(AtEnterpriseInfo atEnterpriseInfo);
	
	/**
	 * 获取AtEnterpriseInfo的信息
	 * @param id
	 * @return
	 */
	public AtEnterpriseInfo getInfo(String id);
}