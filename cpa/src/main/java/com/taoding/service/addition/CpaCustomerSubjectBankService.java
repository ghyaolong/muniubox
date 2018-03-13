package com.taoding.service.addition;

import java.util.List;
import java.util.Map;

import com.taoding.common.service.CrudService;
import com.taoding.domain.addition.CpaCustomerSubjectBank;
import com.taoding.mapper.addition.CpaCustomerSubjectBankDao;

/**
 * 客户科目银行信息Service
 * 
 * @author mhb
 * @version 2017-11-20
 */
public interface CpaCustomerSubjectBankService extends CrudService<CpaCustomerSubjectBankDao, CpaCustomerSubjectBank> {

	/**
	 * 添加客户银行信息
	 * 
	 * @param  customerSubjectBank
	 * @return
	 */
	Object saveCustomerSubjectBank(CpaCustomerSubjectBank customerSubjectBank);
	/**
	 * 修改客户科目银行信息
	 * 
	 * @param  customerSubjectBank
	 * @return
	 */

	Object editCustomerSubjectBank(CpaCustomerSubjectBank customerSubjectBank);
	/**
	 * 删除客户科目银行信息
	 * 
	 * @param  id
	 * @return
	 */

	Object deleteCustomerSubjectBank(String id);
	
	/**
	 * 查询公司科目银行信息
	 * 
	 * @param maps 
	 * @return
	 */
	List<CpaCustomerSubjectBank> selectList(Map<String, Object> maps);
	
}
