package com.taoding.controller.voucher;

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
import com.taoding.domain.voucher.CpaVoucherTemplete;
import com.taoding.service.voucher.CpaVoucherTempleteService;

/**
 * 凭证模板
 * @author czy
 * 2017年11月28日 下午1:57:22
 */
@RestController
@RequestMapping("/vouchertemplete")
public class CpaVoucherTempleteController extends BaseController{

	@Autowired
	private CpaVoucherTempleteService voucherTempleteService ;
	
	/**
	 * 查询企业所保存的凭证模板列表
	 * 2017年11月28日 下午2:19:42
	 * @return
	 */
	@PostMapping("/listData")
	public Object listData(@RequestBody Map<String,Object> maps){
		String bookId = maps.get("bookId").toString();
		boolean type = (boolean) maps.get("type");
		if(StringUtils.isNotEmpty(bookId)){
			return voucherTempleteService.findListByBookId(bookId,type);
		}
		throw new LogicException("账簿ID不能为空") ;
	}
	
	/**
	 * 通过模板id查询模板及模板科目信息（包括科目剩余金额）
	 * 2017年11月28日 下午5:32:27
	 * @param id
	 * @return
	 */
	@GetMapping("/getInfo/{id}")
	public Object getInfo(@PathVariable("id") String id){
		if(StringUtils.isNotEmpty(id)){
			return voucherTempleteService.findById(id);
		}
		throw new LogicException("模板ID不能为空") ;
	}
	
	/**
	 * 新增科目
	 * 2017年11月28日 下午3:06:45
	 * @param voucherTemplete
	 * @return
	 */
	@PutMapping("/save")
	public Object save(@RequestBody CpaVoucherTemplete voucherTemplete){
		return voucherTempleteService.insertVoucherTemplete(voucherTemplete);
	}
	
	/**
	 * 根据模板id删除模板
	 * 2017年11月28日 下午4:38:00
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete/{id}")
	public Object delete(@PathVariable("id") String id){
		if(StringUtils.isNotEmpty(id)){
			return voucherTempleteService.deleteById(id);
		}
		throw new LogicException("模板ID不能为空") ;
	}
	
}
