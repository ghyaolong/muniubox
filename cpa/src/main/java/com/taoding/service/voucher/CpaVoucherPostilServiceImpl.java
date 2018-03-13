package com.taoding.service.voucher;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.common.utils.UserUtils;
import com.taoding.domain.user.User;
import com.taoding.domain.voucher.CpaVoucherPostil;
import com.taoding.mapper.voucher.CpaVoucherPostilDao;

/**
 * 凭证批注
 * @author czy
 * 2017年11月28日 下午3:09:14
 */
@Service
public class CpaVoucherPostilServiceImpl extends
		DefaultCurdServiceImpl<CpaVoucherPostilDao, CpaVoucherPostil> implements
		CpaVoucherPostilService {

	/**
	 * 根据凭证ID查询批注
	 * 2017年12月8日 下午1:44:56
	 * @param voucherId
	 * @return
	 */
	@Override
	public List<CpaVoucherPostil> findListByVoucherId(String voucherId) {
		return dao.findListByVoucherId(voucherId);
	}
	
	/**
	 * 新增批注
	 * 2017年12月8日 下午2:13:11
	 * @param voucherPostil
	 * @return
	 */
	@Override
	@Transactional
	public Object insert(CpaVoucherPostil voucherPostil) {
		voucherPostil.setId(UUIDUtils.getUUid());
		voucherPostil.setCreateDate(new Date());
		User createBy = new User();
		createBy.setId(UserUtils.getCurrentUserId());
		voucherPostil.setCreateBy(createBy);
		int count = dao.insert(voucherPostil);
		if(count > 0){
			return true;
		}
		return false;
	}


	/**
	 * 根据id删除批注
	 * 2017年12月8日 下午2:01:12
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public Object deleteById(String id) {
		int count = dao.deleteById(id);
		if(count > 0){
			return true;
		}
		return false;
	}
	
}
