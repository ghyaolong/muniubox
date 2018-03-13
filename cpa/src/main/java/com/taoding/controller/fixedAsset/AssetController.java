package com.taoding.controller.fixedAsset;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.taoding.common.controller.BaseController;
import com.taoding.common.excel.ExportExcel;
import com.taoding.common.utils.DateUtils;
import com.taoding.domain.fixedAsset.AssetType;
import com.taoding.domain.fixedAsset.FixedAsset;
import com.taoding.domain.fixedAsset.IntangibleAsset;
import com.taoding.domain.fixedAsset.LongApportionedAssets;
import com.taoding.domain.fixedAsset.ParamDepreciationDetail;
import com.taoding.service.fixedAsset.AssetTypeService;
import com.taoding.service.fixedAsset.FixedAssetService;
import com.taoding.service.fixedAsset.IntangibleAssetService;
import com.taoding.service.fixedAsset.LongApportionedAssetsService;

@RestController
@RequestMapping("/asset")
public class AssetController extends BaseController {

	@Autowired
	private AssetTypeService assetTypeService;

	@Autowired
	private FixedAssetService fixedAssetService;
	
	@Autowired
	private LongApportionedAssetsService longApportionedAssetsService;
	
	@Autowired
	private IntangibleAssetService intangibleAssetService;

	/**
	 * @Description: TODO(折旧(摊销)明细表)
	 * @param periodFrom 账期from
	 * @param periodTo 账期TO
	 * @param assetClass 资产分类
	 * @param assetType 资产类型
	 * @param codes 编码数组
	 * @return Object 返回类型
	 * @throws
	 * @author lixc
	 * @date 2017年12月13日
	 */
	@PostMapping("/assetDetailList")
	public Object assetDetailList(@RequestBody ParamDepreciationDetail paramDepreciationDetail) {

		return fixedAssetService.findAssetListByCodesAndPeriodAndType(
				paramDepreciationDetail.getBookId(), paramDepreciationDetail.getPeriodFrom(),
				paramDepreciationDetail.getPeriodTo(), paramDepreciationDetail.getAssetType(),
				paramDepreciationDetail.getAssetClass(), paramDepreciationDetail.getCodes());
	}

	/**
	 * @Description: TODO(获得资产分类)
	 * @return Object 返回类型
	 * @throws
	 * @author lixc
	 * @date 2017年12月13日
	 */
	@GetMapping("/getAssetClass")
	public Object getAssetClass() {
		return AssetType.getAssetArray();
	}

	/**
	 * @Description: TODO(获得资产类型)
	 * @param type
	 *            获得资产分类
	 * @param accountId
	 *            账簿Id
	 * @return Object 返回类型
	 * @throws
	 * @author lixc
	 * @date 2017年12月13日
	 */
	@GetMapping("/getAssetTypeByClass")
	public Object getAssetTypeByClass(@RequestParam("type") Integer type,
			@RequestParam("accountId") String accountId) {
		return assetTypeService.findAssetTypeListByAccountId(accountId, type);		
				
	}

	/**
	 * @Description: TODO(获得资产名)
	 * @param poriodFrom
	 *            账期
	 * @param poriodTo
	 *            账期
	 * @param accountId
	 * @param type
	 * @param assetTypeId
	 * @return Object 返回类型
	 * @throws
	 * @author lixc
	 * @date 2017年12月13日
	 */
	@GetMapping("/getAssetList")
	public Object getAssetList( @RequestParam("periodFrom") String periodFromStr, @RequestParam("periodTo") String periodToStr,
			@RequestParam("accountId") String accountId, @RequestParam("type") Integer type, 
			@RequestParam(value = "assetTypeId", required = false) String assetTypeId) {
		return fixedAssetService.findAssetNameList( DateUtils.parseDate(periodFromStr), DateUtils.parseDate(periodToStr), 
				                                   accountId, type, assetTypeId);
	}

	/**
	 * 
	 * @Description: 下载
	 * @param pageSize 条数
	 * @param pageNo 页号
	 * @param currentPeriod date 当前账期
	 * @param accountId String 账薄ID
	 * @param periodFrom  账期from
	 * @param periodTo 账期TO
	 * @param assetClass 资产分类 1:固定资产 2 无形资产 3待摊资产
	 * @param assetTypes 资产类型  
	 * @param codes
	 * @param isAll
	 *            true 全部 false 当前页
	 * @author mhb
	 * @date 2017年12月13日
	 */
  @PostMapping("/exportData")
   public void exportData(@RequestBody Map<String,Object> queryMap,
	HttpServletResponse response) {  
		queryMap.put("isTotUp", "true");  
			queryMap.put("isTotUp", "true"); 
		try {
			if(Integer.valueOf(queryMap.get("assetClass").toString())==AssetType.FIXED_ASSET_TYPE){
				PageInfo<FixedAsset> pages = fixedAssetService.findFixedAssetListByPage(queryMap);
				String fileName = "固定资产数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
				new ExportExcel("固定资产数据", FixedAsset.class, 1,2,3,4,6,7,8,11,10,13,17).setDataList(pages.getList()).write(response, fileName).dispose();
			}else if(Integer.valueOf(queryMap.get("assetClass").toString())==AssetType.INTANGIBLE_ASSET_TYPE){
				PageInfo<IntangibleAsset> pages = intangibleAssetService.findIntangibleAssetByPage(queryMap);
				String fileName = "无形资产数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
				 new ExportExcel("无形资产数据", FixedAsset.class, 1,2,3,4,6,7,8,11,10,13,17).setDataList(pages.getList()).write(response, fileName).dispose();
			}else if(Integer.valueOf(queryMap.get("assetClass").toString())==AssetType.APPORTIONED_ASSET_TYPE){
				PageInfo<LongApportionedAssets> pages = longApportionedAssetsService.findListByPage(queryMap);
				String fileName = "待摊资产数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
				new ExportExcel("待摊资产数据", FixedAsset.class, 1,2,3,4,6,7,8,11,10,13,17).setDataList(pages.getList()).write(response, fileName).dispose();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
