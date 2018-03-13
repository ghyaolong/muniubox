package com.taoding.mapper.settleaccount;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.domain.settleaccount.CpaFinalLiquidationCustomer;


/**
 * 企业期末结转基础配置
 * @author czy
 * 2017年12月21日 下午3:33:22
 */
@Repository
@Mapper
public interface CpaFinalLiquidationCustomerDao {

	
	/**
	 * 批量新增企业期末结转基础配置
	 * 2017年12月22日 上午10:28:32
	 * @param lists
	 * @return
	 */
	public int batchInsert(List<CpaFinalLiquidationCustomer> lists);
	
	/**
	 * 通过账簿ID 获取 企业 期末结转配置
	 * 2017年12月21日 下午3:34:51
	 * @param bookId
	 * @return
	 */
	public List<CpaFinalLiquidationCustomer> findListByBookId(String bookId);
	
}
