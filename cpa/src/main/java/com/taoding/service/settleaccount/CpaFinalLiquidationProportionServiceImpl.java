package com.taoding.service.settleaccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.domain.settleaccount.CpaFinalLiquidationProportion;
import com.taoding.mapper.settleaccount.CpaFinalLiquidationProportionDao;

@Service
public class CpaFinalLiquidationProportionServiceImpl implements CpaFinalLiquidationProportionService{

	@Autowired
	private CpaFinalLiquidationProportionDao finalLiquidationProportionDao;
	
	/**
	 * 新增比例
	 */
	@Override
	@Transactional
	public Object insertProportion(CpaFinalLiquidationProportion liquidationProportion) {
		if(StringUtils.isEmpty(liquidationProportion.getBookId())){
			throw new LogicException("账簿ID不能为空");
		}
		if(liquidationProportion.getProportion() == null){
			throw new LogicException("比例不能为空");
		}
		liquidationProportion.setId(UUIDUtils.getUUid());
		int count = finalLiquidationProportionDao.insertProportion(liquidationProportion);
		if(count > 0){
			return true;
		}
		return false;
	}

	/**
	 * 根据 bookId 查询比例
	 */
	@Override
	public CpaFinalLiquidationProportion findProportionByBookId(String bookId) {
		if(StringUtils.isEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		CpaFinalLiquidationProportion finalLiquidationProportion = finalLiquidationProportionDao.findProportionByBookId(bookId);
		return finalLiquidationProportion;
	}

	/**
	 * 修改比例
	 */
	@Override
	public Object updateProportion(
			CpaFinalLiquidationProportion liquidationProportion) {
		if(StringUtils.isEmpty(liquidationProportion.getId())){
			throw new LogicException("ID不能为空");
		}
		if(liquidationProportion.getProportion() == null){
			throw new LogicException("比例不能为空");
		}
		int count = finalLiquidationProportionDao.updateProportion(liquidationProportion);
		if(count > 0){
			return true;
		}
		return false;
	}
	
	

}
