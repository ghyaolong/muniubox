package com.taoding.service.report.profit;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.report.profit.ProfitItem;
import com.taoding.mapper.report.profit.ProfitItemDao;

public interface ProfitItemService extends CrudService<ProfitItemDao, ProfitItem> {

	
	/**
	 * 获取利润表
	 * @author fc 
	 * @version 2017年12月27日 下午4:57:02 
	 * @param accountId
	 * @return
	 */
	public List<ProfitItem> getgetProfitItem(String accountId);
	
	/**
	 * 获取客户对应利润项目列表根据会计准则
	 * @author fc 
	 * @version 2017年12月27日 下午5:06:51 
	 * @return
	 */
	public List<ProfitItem> profitItemListByRule(Integer rule, String accountId);

}
