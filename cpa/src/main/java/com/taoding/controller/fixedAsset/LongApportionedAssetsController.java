package com.taoding.controller.fixedAsset;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.taoding.common.controller.BaseController;
import com.taoding.common.excel.ExportExcel;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.DateUtils;
import com.taoding.domain.fixedAsset.FixedAsset;
import com.taoding.domain.fixedAsset.LongApportionedAssets;
import com.taoding.service.fixedAsset.LongApportionedAssetsService;


@RestController
@RequestMapping("/apportioned")
public class LongApportionedAssetsController extends BaseController{
	
	
	@Autowired
	private LongApportionedAssetsService longApportionedAssetsService;
	
	/**
	 * 
	* 长期待摊资产保存/修改
	* @param LongApportionedAssets
	* @return Object 返回类型    
	* @throws 
	* @author MHB
	* @date 2017年12月11日
	 */
	@PostMapping("/save")
	public Object saveApportionedAssets(@RequestBody LongApportionedAssets longApportionedAssets){
		longApportionedAssetsService.saveApportionedAssets(longApportionedAssets);
		return true;	
	}
	/**
	 * 
	* 长期待摊资产删除
	* @param id 
	* @return Object 返回类型    
	* @throws 
	* @author MHB
	* @date 2017年12月11日
	 */
	@PutMapping("/delete/{ids}")
	public Object deleteApportionedAssets(@PathVariable("ids") String ids){
		longApportionedAssetsService.batchDeleteApportionedAssets(ids);
		return true;	
	}
	
	/**
	 * 
	* 通过查询条件获得分页 
	* @param queryMap
	* {pageNo	 	分页页码
	* pageSize	 	每页显示多少条
	* currentPeriod date 当前账期
	* accountId String 账薄ID
	* }
	* @return Object 返回类型    
	* @throws 
	* @author MHB
	* @date 2017年12月12日
	 */
	@PostMapping("/findListByPage")
	public Object findListByPage(@RequestBody Map<String, Object> queryMap){
		return longApportionedAssetsService.findListByPage(queryMap);
	}
	
	/**
	 * 
	* 待摊资产对账
	* @return Object 返回类型    
	* @throws 
	* @author mhb
	* @date 2017年12月13日
	 */
	@PostMapping("/reconciliation/{accountId}")
	public Object intangibleAssetReconciliation(@PathVariable("accountId")String accountId){
		return longApportionedAssetsService.reconciliation(accountId);
	}
	
	/**
	 * 
	* 获得客户默认的 摊销费用科目 累计摊销科目
	* @param bookId 账薄id
	* @return Object 返回类型    {depreciationFeeSubject：{},depreciationCumulationSubject:{}}
	* @throws 
	* @author mhb
	* @date 2017年12月4日
	 */
	@GetMapping("/getFeeSubjectAndCumulationSubjectByBookId/{bookId}")
	public Object getFeeSubjectAndCumulationSubjectByCustomInfoId(@PathVariable("bookId") String bookId){
		return longApportionedAssetsService.getFeeSubjectAndCumulationSubjectByBookId(bookId);
	}
	
	/**
	  * 
	 * 批量处理
	 * @param ids
	 * @return Object 返回类型    
	 * @throws 
	 * @author mhb
	 * @date 2017年12月1日
	  */
	@PostMapping("/batctHanded/{ids}")
	public Object batctAccumulated(@PathVariable("ids") String ids){
		longApportionedAssetsService.batctHandedByIds(ids);
		return true;
	}
	/**
	* LongApportionedAssets 
	* @param id
	* @return Object 返回类型    
	* @throws 
	* @author MHB
	* @date 2017年12月14日
	 */
	@GetMapping("/getApportionedAsset/{id}")
	public Object getIntangibleAsset(@PathVariable("id") String id){
		return longApportionedAssetsService.get(id);
	}
	
	/**
	  * 
	 * 下载
	 * @param pageSize 条数
	 * @param pageNo 页号
	 * @param currentPeriod date 当前账期
	 * @param accountId String 账薄ID    
	 * @author mhb
	 * @date 2017年12月13日
	  */
	@PostMapping("/exportData")
	public void exportData(@RequestBody Map<String,Object> queryMap,
			HttpServletResponse response) { 
		PageInfo<LongApportionedAssets> pages = longApportionedAssetsService.findListByPage(queryMap);
		String fileName = "待摊资产数据" + DateUtils.getDate("yyyyMMddHHmmss")+ ".xlsx";
		try {
			new ExportExcel("待摊资产数据", LongApportionedAssets.EXPORT_HEARDER,FixedAsset.class,1,2,6,7,8,10,11,12,13,14).setDataList(pages.getList()).write(response, fileName).dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	* 获得月摊销额  
	* @param originalValue 原值 
	* @param lifecycle 使用期限(月)
	* @param depreciationType 月摊销额 
	* @param depreciationType 摊销方法
	* @return Object 返回类型    
	* @throws 
	* @author mhb
	* @date 2017年12月12日
	 */
	@PostMapping("/getDepreciationPerMonth")
	public Object getDepreciationPerMonth(@RequestBody Map<String, Object> paramMap){
		BigDecimal originalValue= null;
		
		if (null == paramMap.get("originalValue")) {
			throw new LogicException("原值不能为空!");
		} else {
			originalValue= new BigDecimal(String.valueOf(paramMap.get("originalValue")));
		}  
		Integer lifecycle = null;
		Integer depreciationType=null;
		if(null != paramMap.get("lifecycle")){
			lifecycle = Integer.parseInt(String.valueOf(paramMap.get("lifecycle")));
		}
		 
		if(null != paramMap.get("depreciationType")){
			depreciationType = Integer.parseInt(String.valueOf(paramMap.get("depreciationType")));
		}
 		return longApportionedAssetsService.getDepreciationPerMonth(originalValue,lifecycle,  depreciationType);
	}
	
}
