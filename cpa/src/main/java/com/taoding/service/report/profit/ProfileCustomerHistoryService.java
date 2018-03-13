package com.taoding.service.report.profit;

import com.taoding.common.service.CrudService;
import com.taoding.domain.report.profit.ProfileCustomerHistory;
import com.taoding.mapper.report.profit.ProfileCustomerHistoryDao;

public interface ProfileCustomerHistoryService extends CrudService<ProfileCustomerHistoryDao, ProfileCustomerHistory> {
	
	/**
	 * 保存历史利润报表
	 * @author fc 
	 * @version 2017年12月29日 下午2:16:01 
	 * @return
	 */
	public boolean saveProfitHistory(String accountId);
	
	/**
	 * 保存
	 * @author fc 
	 * @version 2017年12月29日 下午2:37:03 
	 * @param profileCustomerHistory
	 * @return
	 */
	public boolean saveHistory(ProfileCustomerHistory profileCustomerHistory);
}
