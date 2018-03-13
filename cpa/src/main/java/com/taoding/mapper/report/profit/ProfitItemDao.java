package com.taoding.mapper.report.profit;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.report.profit.ProfitItem;


@Repository
@Mapper
public interface ProfitItemDao extends CrudDao<ProfitItem> {

	/**
	 * 获取利润项目列表
	 * @author fc 
	 * @version 2017年12月27日 下午5:06:51 
	 * @return
	 */
	public List<ProfitItem> profitItemListByRule(@Param("rule") Integer rule,@Param("accountId") String accountId);
}
