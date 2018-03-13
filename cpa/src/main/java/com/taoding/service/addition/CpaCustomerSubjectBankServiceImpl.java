package com.taoding.service.addition;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.addition.CpaCustomerSubjectBank;
import com.taoding.mapper.addition.CpaBankDao;
import com.taoding.mapper.addition.CpaCustomerSubjectBankDao;
import com.taoding.mapper.customerInfo.CustomerInfoDao;
import com.taoding.mapper.subject.CpaSubjectDao;

/**
 * 客户科目银行信息Service
 * 
 * @author mhb
 * @version 2017-11-20
 */
@Service
@Transactional
public class CpaCustomerSubjectBankServiceImpl extends DefaultCurdServiceImpl<CpaCustomerSubjectBankDao, CpaCustomerSubjectBank> implements CpaCustomerSubjectBankService {

	@Autowired
	private CpaBankDao cpaBankDao;
	@Autowired
	private CustomerInfoDao CustomerInfoDao;
	@Autowired
	private CpaSubjectDao cpaSubjectDao;
	
	@Override
	public Object saveCustomerSubjectBank(CpaCustomerSubjectBank customerSubjectBank) {
		checkCustomerSubjectBank(customerSubjectBank);
		customerSubjectBank.setId(UUIDUtils.getUUid());
		this.preInsert(customerSubjectBank);
		this.preUpdate(customerSubjectBank); 
		dao.insert(customerSubjectBank);
		return true;
	}

	@Override
	public Object editCustomerSubjectBank(CpaCustomerSubjectBank customerSubjectBank) {
		checkCustomerSubjectBank(customerSubjectBank);
		if(StringUtils.isEmpty(customerSubjectBank.getId())){
			throw new LogicException("修改信息不完整!");
		}
		if(dao.get(customerSubjectBank.getId())==null){
			throw new LogicException("所要修改信息不存在!");
		}
		dao.update(customerSubjectBank);
		return true;
	}

	@Override
	public Object deleteCustomerSubjectBank(String id) {
		if(StringUtils.isEmpty(id)){
			throw new LogicException("删除信息有误或已删除!");
		}
		if(dao.get(id)==null){
			throw new LogicException("删除信息不存在!");
		}
		CpaCustomerSubjectBank customerSubjectBank=new CpaCustomerSubjectBank();
			customerSubjectBank.setId(id);
			customerSubjectBank.setDelFlag(Global.YES);
		dao.update(customerSubjectBank);
		return true;
	}
	/**
	 * 用于效验公司信息  科目信息  银行信息 是否存在
	 * @param customerSubjectBank
	 * 
	 * @return
	 */
	public void checkCustomerSubjectBank(CpaCustomerSubjectBank customerSubjectBank){
		/*判断对象不为空  公司信息是否为空  科目信息是否为空  银行信息是否为空*/
		if(customerSubjectBank==null
			||customerSubjectBank.getCustomerInfo()==null||StringUtils.isEmpty(customerSubjectBank.getCustomerInfo().getId())
			||customerSubjectBank.getCpaSubject()==null||StringUtils.isEmpty(customerSubjectBank.getCpaSubject().getId())
			||customerSubjectBank.getCpaBank()==null||StringUtils.isEmpty(customerSubjectBank.getCpaBank().getId())){
			throw new LogicException("添加信息有误!");
		}
		/*判断公司信息是否存在*/
		if(CustomerInfoDao.get(customerSubjectBank.getCustomerInfo().getId())==null){
			throw new LogicException("关联公司信息不存在!");
		}
		/*判断科目信息是否存在*/
		if(cpaSubjectDao.get(customerSubjectBank.getCpaSubject().getId())==null){
			throw new LogicException("关联科目信息不存在!");
		}
		
		/*判断银行信息是否存在*/
		if(cpaBankDao.get(customerSubjectBank.getCpaBank().getId())==null){
			throw new LogicException("关联银行信息不存在!");
		}
	}
	
	
	public List<CpaCustomerSubjectBank> selectList(Map<String, Object> maps){
		return dao.selectList(maps);
	}
}
