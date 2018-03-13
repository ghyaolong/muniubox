package com.taoding.mapper.settleaccount;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.domain.settleaccount.CpaFinalLiquidation;

/**
 * 期末结转
 * @author czy
 * 2017年12月22日 下午5:28:11
 */
@Repository
@Mapper
public interface CpaFinalLiquidationDao {

	/**
	 * 根据账期+账簿ID 判断是否期末结转
	 * 2017年12月23日 上午9:29:55
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	public CpaFinalLiquidation findByBookId(@Param("bookId") String bookId ,
			@Param("currentPeriod") String currentPeriod);
	
	/**
	 * 新增期末结转数据
	 * 2017年12月23日 上午9:29:04
	 * @param finalLiquidation
	 * @return
	 */
	public int insert(CpaFinalLiquidation finalLiquidation);
	
}
