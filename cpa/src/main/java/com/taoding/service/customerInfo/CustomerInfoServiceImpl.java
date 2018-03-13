package com.taoding.service.customerInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.serviceUtils.EnterpriseUtils;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.addition.CpaCustomerSubjectBank;
import com.taoding.domain.customerInfo.CpaCustomerContactInfo;
import com.taoding.domain.customerInfo.CpaCustomerLinkman;
import com.taoding.domain.customerInfo.CustomerInfo;
import com.taoding.mapper.accountingBook.AccountingBookDao;
import com.taoding.mapper.addition.CpaCustomerSubjectBankDao;
import com.taoding.mapper.customerInfo.CpaCustomerContactInfoDao;
import com.taoding.mapper.customerInfo.CpaCustomerLinkmanDao;
import com.taoding.mapper.customerInfo.CustomerInfoDao;

/**
 * 客户管理详请Service
 * 
 * @author mhb
 * @version 2017-11-16
 */
@Service
@Transactional
public class CustomerInfoServiceImpl extends
		DefaultCurdServiceImpl<CustomerInfoDao, CustomerInfo> implements
		CustomerInfoService {
	@Autowired
	private CpaCustomerLinkmanDao cpaCustomerLinkmanDao;   //公司联系人
	@Autowired
	CpaCustomerContactInfoDao cpaCustomerContactInfoDao;   //签约信息
 	@Autowired
	private CpaCustomerSubjectBankDao cpaCustomerSubjectBankDao;  //银行信息
 	@Autowired
	private AccountingBookDao accountingBookDao;//账薄信息

	@Override
	public Object findCoutomerInfo(String id) {
		if (StringUtils.isEmpty(id)) {
			throw new LogicException("查询信息有误!");
		}
		/* 根据查询公司信息 */
		 CustomerInfo  customerInfo =dao.get(id);
		 /*查询账套信息*/
		 AccountingBook accountingBook=accountingBookDao.findCustomerAccountBook(id);
		/* 查询公司签约信息 */
		List<CpaCustomerContactInfo> customerContactInfoList = cpaCustomerContactInfoDao.findCustomerContactInfoByCoutomerInfoId(id, Global.NO);
		/* 查询公司联系人信息 */
		List<CpaCustomerLinkman> customerLinkmanList = cpaCustomerLinkmanDao.findCustomerLinkmanByCoutomerInfoId(id, Global.NO, null);
		/* 查询公司银行信息 */
		List<CpaCustomerSubjectBank>    cpaCustomerSubjectBankList=cpaCustomerSubjectBankDao.findCustomerSubjectBankByCoutomerInfoId(id,Global.NO);
		Map<String, Object> maps= new HashMap<String, Object>();
		maps.put("customerInfo", customerInfo);
		maps.put("accountingBook", accountingBook);
		maps.put("customerContactInfoList", customerContactInfoList);
		maps.put("customerLinkmanList", customerLinkmanList);
		maps.put("cpaCustomerSubjectBankList", cpaCustomerSubjectBankList);
		return maps;
	}
	

	@Override
	public Object saveCustomerInfo(CustomerInfo customerInfo) {
		if (customerInfo == null||StringUtils.isEmpty(customerInfo.getNo())) {
			throw new LogicException("添加信息有误!");
		}
		this.preInsert(customerInfo);
		this.preUpdate(customerInfo);
		//客户编号生成
		 customerInfo.setNo(this.getNextNo());
		customerInfo.setId(UUIDUtils.getUUid());	
		customerInfo.setEnterpriseMarking(EnterpriseUtils.getCurrentUserEnter());
		dao.insert(customerInfo);
		return true;
	}
	@Override
	public Object deleteCustomerInfo(String id) {
		/* 查询信息是否存在或已经删除 */
		CustomerInfo cf = dao.get(id);
		if(cf==null){
			throw new LogicException("删除信息不存在!");
		}
		CustomerInfo customerInfo = new CustomerInfo();
			customerInfo.setId(id);
			customerInfo.setDelFlag(Global.YES);
		dao.update(customerInfo);
		/*删除公司联系人*/
		CpaCustomerLinkman	cpaCustomerLinkman=new CpaCustomerLinkman();
			cpaCustomerLinkman.setCustomerInfoId(id);
		    cpaCustomerLinkman.setDelFlag(Global.YES);
        cpaCustomerLinkmanDao.update(cpaCustomerLinkman);  
        /*删除公司签约信息*/    
	    CpaCustomerContactInfo cpaCustomerContactInfo  = new CpaCustomerContactInfo();
		    cpaCustomerContactInfo.setCustomerId(id);
		    cpaCustomerContactInfo.setDelFlag(Global.YES);
	    cpaCustomerContactInfoDao.update(cpaCustomerContactInfo);
	    /*删除公司银行信息*/ 
	   CpaCustomerSubjectBank cpaCustomerSubjectBank= new CpaCustomerSubjectBank();
	        cpaCustomerSubjectBank.setDelFlag(Global.YES);
	        cpaCustomerSubjectBank.setCustomerInfo(customerInfo);
        cpaCustomerSubjectBankDao.update(cpaCustomerSubjectBank);
		return true;
	}

	@Override
	public Object editCustomerInfo(CustomerInfo customerInfo) {
		/*判断对象为空,根据公司id查询公司信息是否存在*/
		if(customerInfo==null||StringUtils.isEmpty(customerInfo.getId())){
			throw new LogicException("修改信息有误!");
		}
		if(dao.get(customerInfo.getId())==null){
			throw new LogicException("修改信息不存在!");
		}
		//如果修改客户时已经创建账薄则同步账薄表中的纳税人性质
		if(!(dao.get(customerInfo.getId()).getType().equals(customerInfo.getType()))){
			AccountingBook accountingBook=accountingBookDao.findCustomerAccountBook(customerInfo.getId());
			if(accountingBook!=null){
				accountingBookDao.updateType(accountingBook.getId(),customerInfo.getType());
			}
		}
		dao.update(customerInfo);
		return true;
	}

	@Override
	public Object saveCustomerLinkman(CpaCustomerLinkman cpaCustomerLinkman) {
		/*判断联系人对象是否为空,联系人关联的公司信息是否存在,联系人是否存在*/
		if(cpaCustomerLinkman==null||StringUtils.isEmpty(cpaCustomerLinkman.getCustomerInfoId())){
			throw new LogicException("添加联系人信息有误!");
		}
		
		if(dao.get(cpaCustomerLinkman.getCustomerInfoId())==null){
			throw new LogicException("联系人所在的公司不存在!");
		}
				this.preInsert(cpaCustomerLinkman);
				this.preUpdate(cpaCustomerLinkman);
				cpaCustomerLinkman.setId(UUIDUtils.getUUid());
				cpaCustomerLinkman.setEnterpriseMarking(EnterpriseUtils.getCurrentUserEnter());
				cpaCustomerLinkmanDao.insert(cpaCustomerLinkman);
		return true;
	}

	@Override
	public Object deleteCustomerLinkman(String id) {
		/* 查询信息是否已经删除 */
		List<CpaCustomerLinkman> cpaCustomerLinkmanList = cpaCustomerLinkmanDao.findCustomerLinkmanByCoutomerInfoId(null, Global.NO, id);
		   if(Collections3.isEmpty(cpaCustomerLinkmanList)){
			   throw new LogicException("已经删除或删除信息有误!");
		   }
		   for (CpaCustomerLinkman ccl : cpaCustomerLinkmanList) {
				CpaCustomerLinkman cpaCustomerLinkman = new CpaCustomerLinkman();
				cpaCustomerLinkman.setId(id);
				cpaCustomerLinkman.setDelFlag(Global.YES);
				cpaCustomerLinkmanDao.update(cpaCustomerLinkman);
				return true;
			}
		return true;
	}

	@Override
	public Object editCustomerLinkman(CpaCustomerLinkman cpaCustomerLinkman) {
		/*判断对象为空,联系人关联公司信息是否存在或根据id查询联系人信息是否存在*/
		if(cpaCustomerLinkman==null||StringUtils.isEmpty(cpaCustomerLinkman.getCustomerInfoId())){
			throw new LogicException("修改公司联系人信息有误!");
		}
		if(dao.get(cpaCustomerLinkman.getCustomerInfoId())==null
		  ||Collections3.isEmpty(cpaCustomerLinkmanDao.findCustomerLinkmanByCoutomerInfoId(null, Global.NO,cpaCustomerLinkman.getId()))){
			throw new LogicException("联系人所在的公司不存在!");
		}
			cpaCustomerLinkmanDao.update(cpaCustomerLinkman);
			return true;
	}

	@Override
	public List<CustomerInfo> findCustomInfoListByMap(
			Map<String, Object> queryMap) {
		return dao.findCustomInfoListByMap(queryMap);
	}
	@Override
	public String getNextNo() {
		 String no=dao.findCustomerInfoEnterpriseMarkingForMaxNo(EnterpriseUtils.getCurrentUserEnter());
		 if(StringUtils.isEmpty(no)){
			 return CustomerInfo.DEFAULT_CUSTOMER_NO;
		 }
		 try {
			 Integer NextNoInteger=Integer.valueOf(no)+1;
			 if(NextNoInteger.toString().length()>=CustomerInfo.DEFAULT_CUSTOMER_NO.length()){
				 return NextNoInteger.toString();
			 }
		  return CustomerInfo.DEFAULT_CUSTOMER_NO.substring(0, CustomerInfo.DEFAULT_CUSTOMER_NO.length()-NextNoInteger.toString().length())+NextNoInteger.toString();
		} catch (Exception e) {
			throw new LogicException("公司编号生成数据异常!");
		}
	}
	@Override
	public boolean updateEnable(String id) {
		 if(StringUtils.isEmpty(id)||dao.get(id)==null){
			 throw new LogicException("启用/禁用信息有误");
		 }
		CustomerInfo customerInfo= dao.get(id);
		String status="0";
		if(customerInfo.getStatus().equals(Global.HIDE)){
			status=Global.SHOW;
		}
		CustomerInfo customerInfoUpdate= new CustomerInfo();
		customerInfoUpdate.setStatus(status);
		customerInfoUpdate.setId(id);
		dao.update(customerInfoUpdate);
		return true;
	}
}
