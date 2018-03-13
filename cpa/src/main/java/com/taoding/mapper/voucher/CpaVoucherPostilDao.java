package com.taoding.mapper.voucher;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.voucher.CpaVoucherPostil;

/**
 * 凭证批注
 * 
 * @author czy 2017年11月28日 上午11:25:04
 */
@Repository
@Mapper
public interface CpaVoucherPostilDao extends CrudDao<CpaVoucherPostil>{

	/**
	 * 根据凭证ID查询批注
	 * 2017年12月8日 下午1:44:56
	 * @param voucherId
	 * @return
	 */
	public List<CpaVoucherPostil> findListByVoucherId(String voucherId);
	
	/**
	 * 根据id删除批注
	 * 2017年12月8日 下午2:01:12
	 * @param id
	 * @return
	 */
	public int deleteById(String id);
}
