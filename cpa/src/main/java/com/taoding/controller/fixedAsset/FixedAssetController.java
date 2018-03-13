package com.taoding.controller.fixedAsset;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.taoding.common.controller.BaseController;
import com.taoding.common.excel.ExportExcel;
import com.taoding.common.excel.ImportExcel;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.common.utils.DateUtils;
import com.taoding.domain.fixedAsset.AssetType;
import com.taoding.domain.fixedAsset.FixedAsset;
import com.taoding.service.fixedAsset.AssetTypeAndAssetService;
import com.taoding.service.fixedAsset.AssetTypeService;
import com.taoding.service.fixedAsset.FixedAssetService;


@RestController
@RequestMapping("/fixedAsset")
public class FixedAssetController extends BaseController{

	@Autowired
	private AssetTypeService assetTypeService; //资产类型
	
	
	@Autowired
	private FixedAssetService fixedAssetService; //固定资产
	
	
	@Autowired
	private AssetTypeAndAssetService assetTypeAndAssetService;
	
	/**
	 * 
	* @Description: TODO(通过账簿获得客户资产类别列表) 
	* @return Object 返回类型    
	* @parama accountId 账簿 ID
	* @param type 1 固定 2 无形
	* @throws 
	* @author lixc
	* @date 2017年11月27日
	 */
	@GetMapping("/findAssetTypeListByAccountId")
	public Object findAssetTypeListByAccountId(@RequestParam("accountId") String accountId,
											   @RequestParam("type") Integer type){
		return assetTypeService.findAssetTypeListByAccountId(accountId,type);
	}
	
	/**
	 * 
	* @Description: TODO(保存资产类型) 
	* @param assetType
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月27日
	 */
	@PostMapping("/saveAssetType")
	public Object saveAssetType(@RequestBody AssetType assetType){
		assetTypeService.saveAssetType(assetType);
		return true;
	}
	
	
	/**
	* @Description: TODO(删除资产类型) 
	* @return Ojbect 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	@DeleteMapping("/deleteAssetType/{Id}")
	public Object DeleteAssetType(@PathVariable("Id") String Id){
		AssetType assetType = new AssetType();
		assetType.setId(Id);
		assetTypeService.delete(assetType);
		return true;		
	}
	
	
	

	/**
	* @Description: TODO(获得资产类型) 
	* @return Ojbect 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	@GetMapping("/getAssetTypeById/{Id}")
	public Object getAssetTypeById(@PathVariable("Id") String Id){
		return assetTypeService.get(Id);		
	}
	
	
	/**
	 * 
	* @Description: TODO(固定资产保存) 
	* @param fixedAsset
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	@PostMapping("/saveFixedAsset")
	public Object saveFixedAsset(@RequestBody FixedAsset fixedAsset){
		fixedAssetService.saveFixedAsset(fixedAsset);
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
	@PostMapping("/findFixedAssetListByPage")
	public Object findFixedAssetList(@RequestBody Map<String, Object> queryMap){
		return fixedAssetService.findFixedAssetListByPage(queryMap);
	}
	
	/**
	 * 
	* @Description: TODO(固定资产删除批量) 
	* @param Id
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	@DeleteMapping("/deleteFixedAsset/{ids}")
	public Object deleteFixedAsset(@PathVariable("ids") String Ids){
		fixedAssetService.deleteFixedAssetByIds(Ids);
		return true;
	}
	
	/**
	* @Description: TODO(获得FixedAsset) 
	* @param id
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月30日
	 */
	@GetMapping("/getFixedAsset/{id}")
	public Object getFixedAsset(@PathVariable("id") String id){
		return fixedAssetService.get(id);
	}
	
	/**
	 * 
	* @Description: TODO(固定资产对账) 
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月1日
	 */
	@PostMapping("/reconciliation/{accountId}")
	public Object fixedAssetReconciliation(@PathVariable("accountId")String accountId){
		return fixedAssetService.reconciliation(accountId);
	}
	
	 /**
	  * 
	 * @Description: TODO(批量清理) 
	 * @param ids
	 * @return Object 返回类型    
	 * @throws 
	 * @author lixc
	 * @date 2017年12月1日
	  */
	@PostMapping("/batctAccumulated/{ids}")
	public Object batctAccumulated(@PathVariable("ids") String ids){
		fixedAssetService.batctAccumulatedByIds(ids);
		return true;
	}
	
	/**
	 * 
	* @Description: TODO(获得客户默认的 折旧费用科目 累计折旧科目) 
	* @param customInfoId
	* @return Object 返回类型    {depreciationFeeSubject：{},depreciationCumulationSubject:{}}
	* @throws 
	* @author lixc
	* @date 2017年12月4日
	 */
	@GetMapping("/getFeeSubjectAndCumulationSubjectByBookId/{bookId}")
	public Object getFeeSubjectAndCumulationSubjectByCustomInfoId(@PathVariable("bookId") String bookId){
		return fixedAssetService.getFeeSubjectAndCumulationSubjectBybookId(bookId);
	}
	
	/**
	 * 
	* @Description: TODO(查询资产类型) 
	* @param accountId
	* @param type 资产类型  1 固定资产  2 无形资产
	* @param assetName
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月4日
	 */
	@PostMapping("/getAssetTypeByNameAndAccountId")
	public Object getAssetTypeBy(@RequestParam("accountId")String accountId, 
			                     @RequestParam("type") int type, 
			                     @RequestParam("assetName") String assetName){
		return assetTypeAndAssetService.findAssetType(accountId, type, assetName);
	}
	
	/**
	* @Description: TODO(获得月折旧额) 
	* @param depreciationStartDate
	* @param originalValue
	* @param originalDepreciation
	* @param lifecycle
	* @param depreciationType
	* @param residualRate
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
		Date depreciationStartDate = null;
		BigDecimal originalDepreciation = null;
		Integer lifecycle = null;
		Integer depreciationType=null;
		Double residualRate = null;
		if(null != paramMap.get("depreciationStartDate")){
			 depreciationStartDate  = DateUtils.parseDate(paramMap.get("depreciationStartDate"));
		}
		
		if(null != paramMap.get("originalDepreciation")){
			 originalDepreciation = new BigDecimal(String.valueOf(paramMap.get("originalDepreciation")));
		}
		
		if(null != paramMap.get("lifecycle")){
			lifecycle = Integer.parseInt(String.valueOf(paramMap.get("lifecycle")));
		}
		 
		if(null != paramMap.get("depreciationType")){
			depreciationType = Integer.parseInt(String.valueOf(paramMap.get("depreciationType")));
		}
		
		if(null != paramMap.get("residualRate")){
			residualRate= Double.parseDouble(String.valueOf(paramMap.get("residualRate")));
		}
		
		String periodStr = CurrentAccountingUtils.getCurrentVoucherPeriod(accountBookId);
 		Date period = DateUtils.parseDate(periodStr);
 		
 		return fixedAssetService.getDepreciationPerMonth(depreciationStartDate,originalValue, originalDepreciation, 
				lifecycle, depreciationType, period, residualRate, AssetType.FIXED_ASSET_TYPE);
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
		PageInfo<FixedAsset> pages = fixedAssetService.findFixedAssetListByPage(queryMap);
		String fileName = "固定资产数据" + DateUtils.getDate("yyyyMMddHHmmss")+ ".xlsx";
		try {
			new ExportExcel("固定资产数据", FixedAsset.class,1,2,3,4,5,6,7,8,9,10,11,12,13,14).setDataList(pages.getList()).write(response, fileName).dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 /**
	 * @Description: TODO(结账获取 固定资产计提，长期摊销，无形摊销 ) 
	 * @param bookId 账薄ID
	 * @param type 1 固定  2 无形 3 长期摊销
	 * @return Object 返回类型    
	 * @throws 
	 * @author lixc
	 * @date 2017年12月20日
	  */
	@GetMapping("/sumThisMonthDepreciation")
	public Object sumThisMonthDepreciation(@RequestParam("bookId")String bookId ,
			                               @RequestParam("type")int type){
		
		return fixedAssetService.sumThisMonthDepreciation(bookId, type);
	}
	
	/**
	 * 导入固定资产数据
	 * @param file
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/importListData")
	public Object importListData(MultipartFile file,@RequestParam("bookId")String bookId,HttpServletResponse response) throws Exception {
		ImportExcel ei = new ImportExcel(file, 0, 0);
		List<FixedAsset> list = ei.getDataList(FixedAsset.class, 2,6,7,8,9,10,16);
		if (Collections3.isEmpty(list)) {
			throw new LogicException("导入数据为空");
		}
		return fixedAssetService.importListData(list,bookId);
	}
}
