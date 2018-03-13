package com.taoding.mapper.settleaccount;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.domain.settleaccount.CpaFinalLiquidationProportion;

/**
 * 比例
 * @author admin
 */
@Repository
@Mapper
public interface CpaFinalLiquidationProportionDao {
	
	/**
	 * 新增比例
	 * @param 
	 * @return
	 */
	public int insertProportion(CpaFinalLiquidationProportion liquidationProportion);
	
	/**
	 * 根据 bookId 查询比例
	 * @param bookId
	 * @return
	 */
	public CpaFinalLiquidationProportion findProportionByBookId(@Param("bookId")String bookId);
	
	/**
	 * 修改比例
	 * 2017年12月29日 下午2:35:22
	 * @param liquidationProportion
	 * @return
	 */
	public int updateProportion(CpaFinalLiquidationProportion liquidationProportion);

}
