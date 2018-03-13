package com.taoding.service.report.profit;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.domain.report.profit.ProfileCustomerHistory;
import com.taoding.domain.report.profit.ProfitItem;
import com.taoding.mapper.report.profit.ProfileCustomerHistoryDao;


@Service
@Transactional
public class ProfileCustomerHistoryServiceImpl  extends DefaultCurdServiceImpl<ProfileCustomerHistoryDao, ProfileCustomerHistory> implements ProfileCustomerHistoryService {

	@Autowired
	ProfitItemService pcfService;
	
	@Override
	public boolean saveProfitHistory(String accountId) {
		Date nowDate = new Date();
		List<ProfitItem> profitItemList = pcfService.getgetProfitItem(accountId);
		for(ProfitItem p : profitItemList){
			this.treeSave(p,nowDate,accountId);
		}
		return true;
	}

	@Override
	public boolean saveHistory(ProfileCustomerHistory profileCustomerHistory) {
		int i = dao.saveHistory(profileCustomerHistory);
		if(i > 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 遍历树结构存储利润历史
	 * @author fc 
	 * @version 2017年12月29日 下午3:11:53 
	 * @param profitItem
	 * @param nowDate
	 * @param accountId
	 */
	public void treeSave(ProfitItem profitItem,Date nowDate,String accountId){
		ProfileCustomerHistory pch = new ProfileCustomerHistory();
		pch.setId(UUIDUtils.getUUid());
		pch.setAccount_period(nowDate);
		pch.setCurrentMoney(profitItem.getCurrentMoney());
		pch.setYearMoney(profitItem.getCurrentMoney());
		pch.setAccountId(accountId);
		pch.setItemId(Integer.parseInt(profitItem.getId()));
		this.saveHistory(pch);
		if(profitItem.getSubProfitItems() == null || profitItem.getSubProfitItems().size() < 0){
			return;
		}
		for(ProfitItem p : profitItem.getSubProfitItems()){
			treeSave(p,nowDate,accountId);
		}
	}

}
