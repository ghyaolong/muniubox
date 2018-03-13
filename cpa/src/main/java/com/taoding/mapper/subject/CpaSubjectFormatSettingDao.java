package com.taoding.mapper.subject;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.subject.CpaSubjectFormatSetting;

/**
 * 科目Excel导入设置
 * @author fc
 */
@Repository
@Mapper
public interface CpaSubjectFormatSettingDao extends CrudDao<CpaSubjectFormatSetting>{
	
	/**
	 * 根据客户id获取Excel导入设置详情
	 * @author fc 
	 * @version 2017年12月20日 上午9:38:02 
	 * @param id
	 * @return
	 */
	public CpaSubjectFormatSetting getSettingByCustomerId(@Param("customerId") String customerId);
	
	/**
	 * 删除Excel导入设置
	 * @author fc 
	 * @version 2017年12月20日 上午10:03:07 
	 * @return
	 */
	public int delSettingById(@Param("id") String id);
}
