package com.taoding.controller.voucher;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.NumberToCN;
import com.taoding.domain.voucher.CpaVoucher;
import com.taoding.service.voucher.CpaVoucherService;
import com.taoding.service.voucher.CpaVoucherSubjectService;

/**
 * 凭证
 * 
 * @author czy 2017年11月28日 下午1:52:33
 */
@RestController
@RequestMapping("/voucher")
public class CpaVoucherController extends BaseController {

	@Autowired
	private CpaVoucherService voucherService;
	@Autowired
	private CpaVoucherSubjectService voucherSubjectService;

	/**
	 * 通init 2017年11月28日 下午5:32:27
	 * 
	 * @param id
	 * @return
	 */
//	@GetMapping("/init/{id}")
//	public Object init(@PathVariable("id") String bookId) {
//		if (StringUtils.isNotEmpty(bookId)) {
//			voucherService.init(bookId);
//			return voucherSubjectService.init(bookId);
//		}
//		throw new LogicException("账簿ID不能为空");
//	}
	
	/**
	 * 查询账期所有凭证
	 * 2017年12月6日 上午11:26:17
	 * @param maps
	 * @return
	 */
	@PostMapping("/listData")
	public Object listData(@RequestBody Map<String,String> maps){
		String bookId = maps.get("bookId").toString();
		if (!StringUtils.isNotEmpty(bookId)) {
			throw new LogicException("账簿ID不能为空");
		}
		return voucherService.findAllListByPeriod(maps);
	}

	/**
	 * 获取下一个记账凭证编号 及当前账期最后一天
	 * 2017年11月18日 下午1:50:11
	 * 
	 * @param parentNo
	 * @return
	 */
	@GetMapping("/getNextNo/{id}")
	public Object getNextNo(@PathVariable("id") String bookId) {
		if (StringUtils.isNotEmpty(bookId)) {
			return voucherService.getNextVoucherNo(bookId);
		}
		throw new LogicException("账簿ID不能为空");
	}

	/**
	 * 通过凭证id查询凭证及凭证科目信息 2017年11月28日 下午5:32:27
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping("/getInfo")
	public Object getInfo(@RequestBody Map<String,Object> maps) {
		String bookId = maps.get("bookId").toString();
		String id = maps.get("id").toString();
		if (!StringUtils.isNotEmpty(bookId)) {
			throw new LogicException("账簿ID不能为空");
		}
		if (!StringUtils.isNotEmpty(id)) {
			throw new LogicException("凭证ID不能为空");
		}
		return voucherService.findById(bookId, id);
	}

	
	/**
	 * 通过凭证编号+账期查询凭证科目信息
	 * 用途：前台通过切换科目编号查询凭证
	 * 2017年11月29日 下午4:07:59
	 * @param maps
	 * @return
	 */
	@PostMapping("/getByNoPeriod")
	public Object getByNoPeriod(@RequestBody Map<String, Object> maps) {
		
		String bookId = maps.get("bookId").toString();
		String voucherNo = maps.get("voucherNo").toString();
		String voucherPeriod = maps.get("voucherPeriod").toString();
		
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		if(!StringUtils.isNotEmpty(voucherNo)){
			throw new LogicException("凭证编号不能为空");
		}
		if(!StringUtils.isNotEmpty(voucherPeriod)){
			throw new LogicException("所属账期不能为空");
		}
		return voucherService.findByNoAndPeriod(bookId, voucherNo,voucherPeriod);
	}

	/**
	 * 新增凭证 2017年11月28日 下午3:06:45
	 * 
	 * @param voucher
	 * @return
	 */
	@PutMapping("/save")
	public Object save(@RequestBody CpaVoucher voucher) {
		if(!StringUtils.isNotEmpty(voucher.getBookId())){
			throw new LogicException("账簿ID不能为空");
		}
		return voucherService.insertCpaVoucher(voucher);
	}

	/**
	 * 金额转大写 2017年11月28日 下午4:30:40
	 * 
	 * @param amount
	 * @return
	 */
	@PostMapping("/numberToCN")
	public Object numberToCN(@RequestBody Map<String, Object> maps) {
		String amount = maps.get("amount").toString() ;
		if (StringUtils.isNotEmpty(amount)) {
			BigDecimal numberOfMoney = new BigDecimal(amount);
			return NumberToCN.number2CNMontrayUnit(numberOfMoney);
		}
		throw new LogicException("金额不能为空");
	}
	
	/**
	 * 修改凭证 2017年11月28日 下午3:06:45
	 * 
	 * @param voucher
	 * @return
	 */
	@PutMapping("/update")
	public Object update(@RequestBody CpaVoucher voucher) {
		if(!StringUtils.isNotEmpty(voucher.getId())){
			throw new LogicException("凭证ID不能为空");
		}
		if(!StringUtils.isNotEmpty(voucher.getBookId())){
			throw new LogicException("账簿ID不能为空");
		}
		return voucherService.updateCpaVoucher(voucher);
	}
	
	/**
	 * 删除凭证
	 * 2017年12月4日 下午1:43:07
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete")
	public Object delete(@RequestBody Map<String,Object> maps){
		String id = maps.get("id").toString();
		String bookId = maps.get("bookId").toString();
		if(!StringUtils.isNotEmpty(id)){
			throw new LogicException("凭证ID不能为空");
		}
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		return voucherService.deleteById(bookId, id);
	}
	
	/**
	 * 查询当前账期所有编号
	 * 2017年12月4日 下午2:35:54
	 * @param maps
	 * @return
	 */
	@PostMapping("/getAllNo")
	public Object getAllVoucherNo(@RequestBody Map<String,Object> maps){
		String bookId = maps.get("bookId").toString();
		String voucherPeriod = maps.get("voucherPeriod").toString();
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		if(!StringUtils.isNotEmpty(voucherPeriod)){
			throw new LogicException("当前所属账期不能为空");
		}
		return voucherService.findAllNoByPeriod(bookId, voucherPeriod);
	}
	
	/**
	 * 调整凭证编号(可将编号调整至任意编号前)
	 * 2017年12月4日 下午2:48:29
	 * @param maps
	 * @return
	 */
	@PutMapping("/adjustmentNo")
	public Object adjustmentVoucherNo(@RequestBody CpaVoucher voucher){
		String id = voucher.getId();
		String bookId = voucher.getBookId();
		String voucherNo = voucher.getVoucherNo();
		if(!StringUtils.isNotEmpty(id)){
			throw new LogicException("凭证ID不能为空");
		}
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		if(!StringUtils.isNotEmpty(voucherNo)){
			throw new LogicException("凭证编号不能为空");
		}
		return voucherService.adjustmentVoucherNo(bookId, id, voucherNo);
	}

	/**
	 * 重新整理凭证编号
	 * 2017年12月5日 上午11:55:09
	 * @param lists
	 * @return
	 */
	@PutMapping("/reorganizeNo")
	public Object reorganizeVoucherNo(@RequestBody List<CpaVoucher> lists){
		if(lists != null && lists.size() > 0){
			return voucherService.reorganizeVoucherNo(lists);
		}
		throw new LogicException("提交数据不能为空");
	}
	
	/**
	 * 批量删除
	 * 2017年12月6日 上午9:57:59
	 * @param voucher
	 * @return
	 */
	@DeleteMapping("/batchDelete")
	public Object batchDelete(@RequestBody CpaVoucher voucher){
		
		String bookId = voucher.getBookId();
		String [] deleteIds  = voucher.getBatchIds(); 
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		if(deleteIds.length == 0){
			throw new LogicException("没有提交删除数据");
		}
		return voucherService.batchDelete(bookId, deleteIds);
	}
	
	/**
	 * 凭证合并数据
	 * 2017年12月6日 上午9:57:59
	 * @param voucher
	 * @return
	 */
	@PostMapping("/mergeVoucherData")
	public Object mergeVoucherData(@RequestBody CpaVoucher voucher){
		
		String bookId = voucher.getBookId();
		String [] mergeIds  = voucher.getBatchIds(); 
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		if(mergeIds.length < 2){
			throw new LogicException("合并凭证数据格式不正确");
		}
		return voucherService.mergeVoucherData(bookId, mergeIds);
	}
	
	/**
	 * 凭证合并保存
	 * 2017年12月6日 下午5:20:52
	 * @param voucher
	 * @return
	 */
	@PutMapping("/mergeVoucherSave")
	public Object mergeVoucherSave(@RequestBody CpaVoucher voucher) {
		String bookId = voucher.getBookId();
		String [] mergeIds  = voucher.getBatchIds(); 
		Date voucherPeriod = voucher.getVoucherPeriod();
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		if(mergeIds.length < 1){
			throw new LogicException("合并凭证数据格式不正确");
		}
		if(voucherPeriod == null){
			throw new LogicException("当前账期不能为空");
		}
		return voucherService.mergeVoucherSave(voucher);
	}
	
	/**
	 * 复制凭证保存
	 * 2017年12月6日 下午5:20:52
	 * @param voucher
	 * @return
	 */
	@PutMapping("/pasteVoucherSave")
	public Object pasteVoucherSave(@RequestBody CpaVoucher voucher) {
		String bookId = voucher.getBookId();
		String [] mergeIds  = voucher.getBatchIds(); 
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		if(mergeIds.length == 0){
			throw new LogicException("复制粘贴凭证不能为空");
		}
		return voucherService.pasteVoucherSave(voucher);
	}
	
	
	/**
	 * 
	* @Description: TODO(凭证统计；是否显示细节前台处理,求和统计一级科目求和) 
	* @param voucherPeriod  账期
	* @param bookId  账薄ID
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月19日
	 */
	@GetMapping("/statisticsVoucher")
    public Object statisticsVoucher(
    		@RequestParam("voucherPeriod")String voucherPeriod,
    		@RequestParam("bookId")String bookId){
    	return voucherService.findVoucherSummaryListAndStatistics(voucherPeriod,bookId,true);
    }
}
