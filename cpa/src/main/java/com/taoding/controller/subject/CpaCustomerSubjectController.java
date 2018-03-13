package com.taoding.controller.subject;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.domain.subject.CpaAssistingData;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.domain.vouchersummary.CpaVoucherSummary;
import com.taoding.service.assisting.CpaCustomerAssistingService;
import com.taoding.service.subject.CpaCustomerSubjectService;
import com.taoding.service.vouchersummary.CpaVoucherSummaryService;

/**
 * 企业科目
 * 
 * @author czy 2017年11月17日 下午2:10:48
 */
@RestController
@RequestMapping(value = "/customersubject")
public class CpaCustomerSubjectController extends BaseController {

	@Autowired
	private CpaCustomerSubjectService customerSubjectService;
	@Autowired
	private CpaCustomerAssistingService customerAssistingService;
	@Autowired
	private CpaVoucherSummaryService voucherSummaryService ;
	/**
	 * 查询企业全部科目
	 * 
	 * @return
	 */
	@GetMapping("/init")
	public Object init() {
		
		customerSubjectService.init("1", "1001");
		
		return null ;
	}
	
	/**
	 * 查询企业全部科目
	 * 
	 * @return
	 */
	@PostMapping("/listData")
	public Object listData(@RequestBody Map<String,Object> maps) {
		
		String bookId = maps.get("bookId").toString();
		String customerId = maps.get("customerId").toString();
		Object isAll = maps.get("isAll");
		
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空") ;
		}
		if(!StringUtils.isNotEmpty(customerId)){
			throw new LogicException("客户ID不能为空") ;
		}
		boolean hashChild = false ;
		if(isAll != null && "true".equals(isAll.toString())){
			hashChild = true ;
		}
		
		return customerSubjectService.findAllList(bookId,hashChild);
	}
	
	/**
	 * 根据ID查询会计科目
	 * 
	 * @return
	 */
	@PostMapping("/getInfo")
	public Object getInfo(@RequestBody Map<String,Object> maps) {
		String id = maps.get("id").toString();
		String bookId = maps.get("bookId").toString();
		if(!StringUtils.isNotEmpty(id)){
			throw new LogicException("科目ID不能为空") ;
		}
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空") ;
		}
		return customerSubjectService.getById(bookId,id);
	}

	/**
	 * 根据父No生成下一个子科目编号
	 * 2017年11月18日 下午1:50:11
	 * @param parentNo
	 * @return
	 */
	@PostMapping("/getNextNo")
	public Object getNextNo(@RequestBody Map<String,Object> maps) {
		
		String parentNo = maps.get("parentNo").toString() ;
		String bookId = maps.get("bookId").toString();
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空") ;
		}
		if(!StringUtils.isNotEmpty(parentNo)){
			throw new LogicException("科目编号不能为空") ;
		}
		return customerSubjectService.getNextNo(bookId,parentNo);
	}
	
	/**
	 * 新增科目
	 * 2017年11月20日 下午4:22:46
	 * @param customerSubject
	 * @return
	 */
    @PutMapping("/save")
    public Object save(@RequestBody CpaCustomerSubject customerSubject)  {
    	return customerSubjectService.insertCustomerSubject(customerSubject);
    }
    
    /**
     * 新增辅助核算科目
     * 2017年11月20日 下午4:22:46
     * @param assistingData
     * @return
     */
    @PutMapping("/saveAssisting")
    public Object saveAssisting(@RequestBody CpaAssistingData assistingData)  {
    	return customerSubjectService.insertAssistingSubject(assistingData);
    }
    
    /**
     * 新增科目
     * 2017年11月20日 下午4:22:46
     * @param customerSubject
     * @return
     */
    @PutMapping("/update")
    public Object update(@RequestBody CpaCustomerSubject customerSubject)  {
    	return customerSubjectService.updateCustomerSubject(customerSubject);
    }
    
    /**
     * 删除科目
     * 2017年11月20日 下午4:22:46
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Object delete(@PathVariable("id") String id)  {
    	if(StringUtils.isNotEmpty(id)){
			return customerSubjectService.deleteById(id);
		}
		throw new LogicException("科目ID不能为空") ;   	
    }
    
    /**
     * 试算平衡数据
     * 2017年11月20日 下午4:22:46
     * @param bookId
     * @return
     */
    @GetMapping("/balanceData/{id}")
    public Object balanceData(@PathVariable("id") String bookId)  {
    	if(StringUtils.isNotEmpty(bookId)){
			return customerSubjectService.trialBalanceData(bookId);
		}
    	throw new LogicException("账簿ID不能为空") ;   
    }
    
    
	/**
	 * 获取企业基础辅助核算类型列表
	 * 2017年11月22日 下午2:33:18
	 * @return
	 */
	@GetMapping(value="/assistingData/{id}")
	public Object assistingData(@PathVariable("id") String bookId) {
		if(StringUtils.isNotEmpty(bookId)){
			return customerAssistingService.findList(bookId);
		}
    	throw new LogicException("账簿ID不能为空") ;   
	}
	
	/**
	 * 结账
	 * 2017年11月22日 下午5:24:14
	 * @return
	 */
	@PutMapping("/settleAccount/{id}")
	public Object settleAccount(@PathVariable("id") String bookId){
		if(StringUtils.isNotEmpty(bookId)){
			return customerSubjectService.updateFinishByBookId(true, bookId);
		}
    	throw new LogicException("账簿ID不能为空") ;  
		
	}
	
	/**
	 * 金额实时计算
	 * 2017年11月22日 下午5:24:14
	 * @return
	 */
	@PutMapping("/computeAccount")
	public Object computeAccount(@RequestBody CpaCustomerSubject customerSubject){
		return customerSubjectService.updateSubjectMoney(customerSubject);
	}
    
	
	/**
	 * 根据id 查询科目信息及科目余额
	 * 2017年11月30日 上午10:37:28
	 * @param bookId
	 * @param id
	 * @return
	 */
	@PostMapping("/getInfoAndBalance")
	public Object getInfoAndBalance(@RequestBody Map<String,Object> maps){
		String bookId = maps.get("bookId").toString();
		String id = maps.get("id").toString();
		if (!StringUtils.isNotEmpty(bookId)) {
			throw new LogicException("账簿ID不能为空");
		}
		if (!StringUtils.isNotEmpty(id)) {
			throw new LogicException("科目ID不能为空");
		}
		return voucherSummaryService.findInfoAndAmountById(id, bookId, CpaVoucherSummary.CURRENT_FRONT_DATA) ;
	}
}
