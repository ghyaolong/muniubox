package com.taoding.service.report.profit;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.taoding.common.service.CrudService;
import com.taoding.domain.report.profit.ProfileCustomerFomula;
import com.taoding.mapper.report.profit.ProfileCustomerFomulaDao;

public interface ProfileCustomerFomulaService extends CrudService<ProfileCustomerFomulaDao, ProfileCustomerFomula> {

	/**
	 * 重置/初始化客户公式
	 * @author fc 
	 * @version 2017年12月27日 下午2:30:01 
	 * @param accountId
	 * @return
	 */
	public boolean resetFormula(String accountId);
	
	/**
	 * 保存公式
	 * @author fc 
	 * @version 2017年12月27日 下午2:38:08 
	 * @param profileCustomerFomula
	 * @return
	 */
	public boolean saveFormula(ProfileCustomerFomula profileCustomerFomula);
	
	
	/**
	 * 根据账簿id获取客户公式列表
	 * @author fc 
	 * @version 2017年12月27日 下午3:14:34 
	 * @param accountId
	 * @return
	 */
	public List<ProfileCustomerFomula> getProfileCustomerFomulaList(String accountId);
	
	/**
	 * 根据账簿id删除客户公式
	 * @author fc 
	 * @version 2017年12月27日 下午3:39:11 
	 * @param accountId
	 * @return
	 */
	public boolean deleteByAccountId(String accountId);
	
	/**
	 * 根据账簿id和项目id获取当前项目的公式
	 * @author fc 
	 * @version 2017年12月29日 下午3:15:31 
	 * @param accountId
	 * @param itemId
	 * @return
	 */
	public List<ProfileCustomerFomula> getItemFomulaList(String accountId,String itemId);
	
	/**
	 * 保存/修改客户公式
	 * @author fc 
	 * @version 2017年12月29日 下午3:41:14 
	 * @return
	 */
	public boolean saveCustomerFomula(ProfileCustomerFomula profileCustomerFomula);
	
	/**
	 * 删除客户公式
	 * @author fc 
	 * @version 2017年12月29日 下午4:08:12 
	 * @param id
	 * @return
	 */
	public boolean delCustomerFomula(String id);
}
