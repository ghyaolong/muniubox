package com.taoding.service.checkoutSetting;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.domain.checkoutSetting.SettleAccountOperatingBasic;
import com.taoding.domain.checkoutSetting.SettleAccountOperatingSetting;
import com.taoding.mapper.checkoutSetting.SettleAccountOperatingBasicDao;
import com.taoding.mapper.checkoutSetting.SettleAccountOperatingSettingDao;
import com.taoding.service.accountingBook.AccountingBookService;

/**
 * 经营数据分析设置ServiceImpl
 * 
 * @author mhb
 * @version 2017-12-25
 */
@Slf4j
@Service
public class SettleAccountOperatingSettingServiceImpl extends
		DefaultCurdServiceImpl<SettleAccountOperatingSettingDao, SettleAccountOperatingSetting>
		implements SettleAccountOperatingSettingService {
	
	@Autowired
	private SettleAccountOperatingBasicDao settleAccountOperatingBasicDao;
	@Autowired
	private AccountingBookService accountingBookService;

	@Override
	public Object initSave(String bookId,String customerId) {
	List<SettleAccountOperatingBasic>	settleAccountOperatingBasicList=settleAccountOperatingBasicDao.findAllList();
	List<SettleAccountOperatingSetting> settleAccountOperatingSettingList= new LinkedList<SettleAccountOperatingSetting>();
	for (SettleAccountOperatingBasic settleAccountOperatingBasic : settleAccountOperatingBasicList) {
		SettleAccountOperatingSetting settleAccountOperatingSetting = new SettleAccountOperatingSetting();
		BeanUtils.copyProperties(settleAccountOperatingBasic, settleAccountOperatingSetting);
		settleAccountOperatingSetting.setId(UUIDUtils.getUUid());
		settleAccountOperatingSetting.setBookId(bookId);
		settleAccountOperatingSetting.setCustomerId(customerId);
		settleAccountOperatingSettingList.add(settleAccountOperatingSetting);
	}
	log.info("settleAccountOperatingSettingList===>>"+JSON.toJSONString(settleAccountOperatingSettingList));
	dao.batchInsert(settleAccountOperatingSettingList);	 
		return true;
	}
	@Override
	public List<SettleAccountOperatingSetting> findSettleAccountOperatingListData( String bookId) {
		if(accountingBookService.get(bookId)==null){
			throw new LogicException("账薄或信息不存在");
		}
		return dao.findList(bookId);
	}
}
