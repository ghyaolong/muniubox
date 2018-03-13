package com.taoding.mapper.voucher;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.voucher.CpaVoucherTemplete;

/**
 * 凭证模板
 * 
 * @author czy 2017年11月28日 上午11:27:56
 */
@Repository
@Mapper
public interface CpaVoucherTempleteDao extends CrudDao<CpaVoucherTemplete>{

	
	/**
	 * 查询 企业所保存的凭证模板
	 * 2017年11月28日 下午2:32:11
	 * @param bookId
	 * @param  type==true 模板  type==false 草稿
	 * @return
	 */
	public List<CpaVoucherTemplete> findListByBookId(@Param("bookId")String bookId,
			@Param("type") boolean type);
	
	/**
	 * 根据模板名称查询-检索模板名称唯一性
	 * 2017年11月28日 下午3:28:10
	 * @param bookId
	 * @param templeteName
	 * @return
	 */
	public CpaVoucherTemplete findByTempleteName(@Param("bookId") String bookId, 
			@Param("templeteName") String templeteName);
	
	
}
