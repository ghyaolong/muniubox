package com.taoding.controller.fixedAsset;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.taoding.common.controller.BaseController;
import com.taoding.common.excel.ExportExcel;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.DateUtils;
import com.taoding.domain.fixedAsset.FixedAsset;
import com.taoding.domain.fixedAsset.IntangibleAsset;
import com.taoding.domain.fixedAsset.LongApportionedAssets;
import com.taoding.service.fixedAsset.AssetTypeAndAssetService;
import com.taoding.service.fixedAsset.AssetTypeService;
import com.taoding.service.fixedAsset.IntangibleAssetService;

@RestController
@RequestMapping("/intangibleAsset")
public class IntangibleAssetController extends BaseController{

	@Autowired
	private AssetTypeService assetTypeService; //资产类型
	
	
	@Autowired
	private IntangibleAssetService intangibleAssetService; //无形资产
	
	
	@Autowired
	private AssetTypeAndAssetService assetTypeAndAssetService;
	
	
	/**
	 * 
	* @Description: TODO(无形资产保存) 
	* @param IntangibleAsset
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	@PostMapping("/saveIntangibleAsset")
	public Object saveIntangibleAsset(@RequestBody IntangibleAsset IntangibleAsset){
		intangibleAssetService.saveIntangibleAsset(IntangibleAsset);
		return true;	
	}
	
	/**
	 * 
	* @Description: TODO(通过查询条件获得分页) 
	* @param queryMap
	* {pageNo	int	否	分页页码
	* pageSize	int	否	每页显示多少条
	* currentPeriod date 当前账期
	* accountId String 账薄ID
	* }
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	@PostMapping("/findIntangibleAssetListByPage")
	public Object findIntangibleAssetList(@RequestBody Map<String, Object> queryMap){
		return intangibleAssetService.findIntangibleAssetByPage(queryMap);
	}
	
	/**
	 * 
	* @Description: TODO(无形资产删除批量) 
	* @param Id
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	@DeleteMapping("/deleteIntangibleAsset/{ids}")
	public Object deleteIntangibleAsset(@PathVariable("ids") String Ids){
		intangibleAssetService.deleteIntangibleAssetByIds(Ids);
		return true;
	}
	
	/**
	* @Description: TODO(获得IntangibleAsset) 
	* @param id
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月30日
	 */
	@GetMapping("/getIntangibleAsset/{id}")
	public Object getIntangibleAsset(@PathVariable("id") String id){
		
		return intangibleAssetService.get(id);
	}
	
	/**
	 * 
	* @Description: TODO(无形资产对账) 
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月1日
	 */
	@PostMapping("/reconciliation/{accountId}")
	public Object intangibleAssetReconciliation(@PathVariable("accountId")String accountId){
		return intangibleAssetService.reconciliation(accountId);
	}
	
	 /**
	  * 
	 * @Description: TODO(批量处理) 
	 * @param ids
	 * @return Object 返回类型    
	 * @throws 
	 * @author lixc
	 * @date 2017年12月1日
	  */
	@PostMapping("/batctHanded/{ids}")
	public Object batctAccumulated(@PathVariable("ids") String ids){
		intangibleAssetService.batctHandedByIds(ids);
		return true;
	}
	
	
	/**
	 * 
	* @Description: TODO(获得客户默认的 摊销费用科目 累计摊销科目) 
	* @param bookId
	* @return Object 返回类型    {depreciationFeeSubject：{},depreciationCumulationSubject:{}}
	* @throws 
	* @author lixc
	* @date 2017年12月4日
	 */
	@GetMapping("/getFeeSubjectAndCumulationSubjectByBookId/{bookId}")
	public Object getFeeSubjectAndCumulationSubjectByCustomInfoId(@PathVariable("bookId") String bookId){
		return intangibleAssetService.getFeeSubjectAndCumulationSubjectByBookId(bookId);
	}
	
	
	/**
	* @Description: TODO(获得月折旧额) 
	* @param originalValue
	* @param originalDepreciation
	* @param lifecycle
	* @param depreciationType
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月12日
	 */
	@PostMapping("/getDepreciationPerMonth")
	public Object getDepreciationPerMonth(@RequestBody Map<String, Object> paramMap){
		
		if (null == paramMap.get("accountBookId")
				&& StringUtils.isBlank(String.valueOf(paramMap
						.get("accountBookId")))) {
			throw new LogicException("账簿不能为空!");
		}

		BigDecimal originalValue= null;
		
		if (null == paramMap.get("originalValue")) {
			throw new LogicException("原值不能为空!");
		} else {
			originalValue= new BigDecimal(String.valueOf(paramMap
					.get("originalValue")));
		}
		 
		
		String accountBookId = String.valueOf(paramMap.get("accountBookId"));
		BigDecimal originalDepreciation = null;
		Integer lifecycle = null;
		Integer depreciationType=null;
		if(null != paramMap.get("originalDepreciation")){
			 originalDepreciation = new BigDecimal(String.valueOf(paramMap.get("originalDepreciation")));
		}
		
		if(null != paramMap.get("lifecycle")){
			lifecycle = Integer.parseInt(String.valueOf(paramMap.get("lifecycle")));
		}
		 
		if(null != paramMap.get("depreciationType")){
			depreciationType = Integer.parseInt(String.valueOf(paramMap.get("depreciationType")));
		}
 		
 		return intangibleAssetService.getDepreciationPerMonth(
 				accountBookId,
				originalValue, 
				originalDepreciation, 
				lifecycle, 
				depreciationType);
	}
	
	/**
	  * 
	 * @Description: 下载
	 * @param pageSize 条数
	 * @param pageNo 页号
	 * @param currentPeriod date 当前账期
	 * @param accountId String 账薄ID    
	 * @param isAll true 全部  false 当前页 
	 * @author mhb
	 * @date 2017年12月13日
	  */
	@PostMapping("/exportData")
     public void exportData(@RequestBody Map<String,Object> queryMap,
			HttpServletResponse response) {  
		queryMap.put("exportMark", FixedAsset.EXPORT_MARK);
		PageInfo<IntangibleAsset> pages = intangibleAssetService.findIntangibleAssetByPage(queryMap);
		String fileName = "无形资产数据" + DateUtils.getDate("yyyyMMddHHmmss")+ ".xlsx";
		try {
			new ExportExcel("无形资产数据", LongApportionedAssets.EXPORT_HEARDER,FixedAsset.class,1,2,6,7,8,10,11,12,13,14).setDataList(pages.getList()).write(response, fileName).dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
