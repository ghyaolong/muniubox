package com.taoding.mapper.report.profit;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.report.profit.ProfileCustomerFomula;


@Repository
@Mapper
public interface ProfileCustomerFomulaDao extends CrudDao<ProfileCustomerFomula> {

	/**
	 * 保存公式
	 * @author fc 
	 * @version 2017年12月27日 下午2:38:08 
	 * @param profileCustomerFomula
	 * @return
	 */
	public int saveFormula(ProfileCustomerFomula profileCustomerFomula);
	
	/**
	 * 根据账簿id获取客户公式列表
	 * @author fc 
	 * @version 2017年12月27日 下午3:14:34 
	 * @param accountId
	 * @return
	 */
	public List<ProfileCustomerFomula> getProfileCustomerFomulaList(@Param("accountId") String accountId);
	
	/**
	 * 根据账簿id删除客户公式
	 * @author fc 
	 * @version 2017年12月27日 下午3:39:11 
	 * @param accountId
	 * @return
	 */
	public int deleteByAccountId(@Param("accountId") String accountId);
	
	/**
	 * 根据账簿id和项目id获取当前项目的公式
	 * @author fc 
	 * @version 2017年12月29日 下午3:15:31 
	 * @param accountId
	 * @param itemId
	 * @return
	 */
	public List<ProfileCustomerFomula> getItemFomulaList(@Param("accountId") String accountId,@Param("itemId") String itemId);
	
	/**
	 * 修改公式
	 * @author fc 
	 * @version 2017年12月29日 下午3:49:03 
	 * @param profileCustomerFomula
	 * @return
	 */
	public int updateFormula(ProfileCustomerFomula profileCustomerFomula);
	
	/**
	 * 删除客户公式
	 * @author fc 
	 * @version 2017年12月29日 下午4:08:12 
	 * @param id
	 * @return
	 */
	public int delCustomerFomula(@Param("id") String id);
}
