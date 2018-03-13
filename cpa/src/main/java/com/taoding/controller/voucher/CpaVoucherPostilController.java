package com.taoding.controller.voucher;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.domain.voucher.CpaVoucherPostil;
import com.taoding.service.voucher.CpaVoucherPostilService;

/**
 * 凭证批注
 * @author czy
 * 2017年12月8日 下午2:06:57
 */
@RestController
@RequestMapping("/voucherpostil")
public class CpaVoucherPostilController extends BaseController {

	@Autowired
	private CpaVoucherPostilService voucherPostilService;
	
	/**
	 * 根据凭证ID查询批注信息
	 * 2017年12月8日 下午2:10:16
	 * @param voucherId
	 * @return
	 */
	@GetMapping("/listPostil/{id}")
	public Object listData(@PathVariable("id") String voucherId){
		if(StringUtils.isNotEmpty(voucherId)){
			return voucherPostilService.findListByVoucherId(voucherId);
		}
		throw new LogicException("凭证ID不能为空");
	}
	
	/**
	 * 新增批注
	 * 2017年12月8日 下午2:16:41
	 * @param voucherPostil
	 * @return
	 */
	@PutMapping("/savePostil")
	public Object savePostil(@RequestBody CpaVoucherPostil voucherPostil){
		return voucherPostilService.insert(voucherPostil);
	}
	
	/**
	 * 根据批注ID删除批注
	 * 2017年12月8日 下午2:18:09
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete/{id}")
	public Object delete(@PathVariable("id") String id){
		if(StringUtils.isNotEmpty(id)){
			return voucherPostilService.deleteById(id);
		}
		throw new LogicException("批注ID不能为空");
	}
}
