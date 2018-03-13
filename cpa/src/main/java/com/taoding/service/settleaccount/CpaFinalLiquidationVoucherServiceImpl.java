package com.taoding.service.settleaccount;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taoding.domain.settleaccount.CpaFinalLiquidationVoucher;
import com.taoding.mapper.settleaccount.CpaFinalLiquidationVoucherDao;


/**
* @Description: 
* @author lixc 
* @date 2017年12月23日 下午2:09:29 
*/
@Service
public class CpaFinalLiquidationVoucherServiceImpl implements CpaFinalLiquidationVoucherService {

	@Autowired
	private CpaFinalLiquidationVoucherDao finalLiquidationVoucherDao;
	
	@Override
	public List<CpaFinalLiquidationVoucher> findAllList(String bookId,
			String voucherPeriod) {
		return finalLiquidationVoucherDao.findAllList(bookId, voucherPeriod);
	}

}
