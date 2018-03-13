package com.taoding.mapper.report.profit;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.report.profit.ProfileCustomerHistory;


@Repository
@Mapper
public interface ProfileCustomerHistoryDao extends CrudDao<ProfileCustomerHistory> {

	/**
	 * 保存
	 * @author fc 
	 * @version 2017年12月29日 下午2:37:03 
	 * @param profileCustomerHistory
	 * @return
	 */
	public int saveHistory(ProfileCustomerHistory profileCustomerHistory);
}
