package com.taoding.service.report.profit;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.report.profit.ProfileCustomerFomula;
import com.taoding.domain.report.profit.ProfileFomulaTemplate;
import com.taoding.mapper.report.profit.ProfileCustomerFomulaDao;


@Service
@Transactional
public class ProfileCustomerFomulaServiceImpl  extends DefaultCurdServiceImpl<ProfileCustomerFomulaDao, ProfileCustomerFomula> implements ProfileCustomerFomulaService {

	@Autowired
	ProfileFomulaTemplateService pftsService;
	
	@Override
	public boolean resetFormula(String accountId) {
		if(StringUtils.isEmpty(accountId)){
			throw new LogicException("传入账簿id为空！");
		}
		List<ProfileCustomerFomula> pcflist = this.getProfileCustomerFomulaList(accountId);
		if(pcflist != null && pcflist.size() > 0){
			//如果客户公式表存在数据则删除
			if(!this.deleteByAccountId(accountId)){
				throw new LogicException("删除原有数据失败！");
			}
		}
		List<ProfileFomulaTemplate> profileFomulaTemplateList = pftsService.getFomulaTemplateList();
		if(profileFomulaTemplateList == null || profileFomulaTemplateList.size() <= 0){
			throw new LogicException("基础模板公式表为空！");
		}
		ProfileCustomerFomula profileCustomerFomula = new ProfileCustomerFomula();
		profileCustomerFomula.setAccountId(accountId);
		for(ProfileFomulaTemplate pt : profileFomulaTemplateList){
			profileCustomerFomula.setSubjectId(pt.getSubjectId());
			profileCustomerFomula.setOperation(pt.getOperation());
			profileCustomerFomula.setOperandSource(pt.getOperandSource());
			profileCustomerFomula.setItemId(pt.getItemId());
			if(!this.saveFormula(profileCustomerFomula)){
				throw new LogicException("保存失败！");
			}
		}
		return true;
	}

	@Override
	public boolean saveFormula(ProfileCustomerFomula profileCustomerFomula) {
		int i = dao.saveFormula(profileCustomerFomula);
		if(i > 0){
			return true;
		}
		return false;
	}

	@Override
	public List<ProfileCustomerFomula> getProfileCustomerFomulaList(String accountId) {
		return dao.getProfileCustomerFomulaList(accountId);
	}

	@Override
	public boolean deleteByAccountId(String accountId) {
		int i = dao.deleteByAccountId(accountId);
		if(i > 0){
			return true;
		}
		return false;
	}

	@Override
	public List<ProfileCustomerFomula> getItemFomulaList(String accountId,String itemId) {
		return dao.getItemFomulaList(accountId,itemId);
	}

	@Override
	public boolean saveCustomerFomula(ProfileCustomerFomula profileCustomerFomula) {
		if(StringUtils.isEmpty(profileCustomerFomula.getId())){
			return this.saveFormula(profileCustomerFomula);
		}else{
			int i = dao.updateFormula(profileCustomerFomula);
			if(i > 0){
				return true;
			}else{
				return false;
			}
		}
	}

	@Override
	public boolean delCustomerFomula(String id) {
		int i = dao.delCustomerFomula(id);
		if(i > 0){
			return true;
		}else{
			return false;
		}
	}

}
