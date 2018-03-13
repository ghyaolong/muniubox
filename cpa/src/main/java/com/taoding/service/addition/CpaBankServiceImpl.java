package com.taoding.service.addition;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.StringUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.addition.CpaBank;
import com.taoding.domain.user.User;
import com.taoding.mapper.addition.CpaBankDao;

/**
 * 银行信息ServiceImpl
 * 
 * @author mhb
 * @version 2017-11-18
 */
@Service
@Transactional
public class CpaBankServiceImpl extends DefaultCurdServiceImpl<CpaBankDao, CpaBank> implements CpaBankService {

	@Override
	public Object saveBank(CpaBank cpaBank) {
		if(StringUtils.isNotEmpty(cpaBank.getBankName())){
		List<CpaBank> cpaBankList=	dao.findAllList(cpaBank.getBankName());
		     if(cpaBankList.size()>0){
		    	 throw new LogicException("银行名称重复!");
		     }
		}
		cpaBank.setId(UUID.randomUUID().toString());
		cpaBank.setCreateDate(new Date());
		cpaBank.setUpdateDate(new Date());
		cpaBank.setCreateBy(new User("张三"));
		cpaBank.setUpdateBy(new User("10010"));
		dao.insert(cpaBank);
		return true;
		  
	}

	@Override
	public Object editBank(CpaBank cpaBank) {
		cpaBank.setCreateDate(new Date());
		cpaBank.setUpdateDate(new Date());
		cpaBank.setCreateBy(new User("张三"));
		cpaBank.setUpdateBy(new User("10011"));
		  dao.update(cpaBank);
		return true;
	}

	@Override
	public Object deleteBank(String id) {
		if(StringUtils.isEmpty(id)){
			throw new LogicException("删除信息有误!");
		}
		if(dao.get(id)==null){
			throw new LogicException("所要删除的银行信息不存在!");
		}
		
		CpaBank cpaBank= new CpaBank();
		cpaBank.setId(id);
		cpaBank.setDelFlag(Global.YES);
		return  dao.update(cpaBank);
	}

	@Override
	public List<CpaBank> getBankList(String bankName) {
		return dao.findAllList(bankName);
	}

}
