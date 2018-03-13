package com.taoding.controller.fixedAsset;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.taoding.common.controller.BaseController;
import com.taoding.common.excel.ExportExcel;
import com.taoding.common.utils.DateUtils;
import com.taoding.domain.fixedAsset.FixedAsset;
import com.taoding.service.fixedAsset.FixedAssetClearService;
@RestController
@RequestMapping("/fixedAssetClear")
public class FixedAssetClearController extends BaseController {
	
	@Autowired
	private FixedAssetClearService  fixedAssetClearService;
	/**
	 * 
	* 固定资产清理(通过查询条件获得)分页 
	* @param queryMap
	* {pageNo	int	否	分页页码
	* pageSize	int	否	每页显示多少条
	* currentPeriod date 当前账期
	* accountId String 账薄ID
	* }
	* @return Object 返回类型    
	* @throws 
	* @author mhb
	* @date 2017年11月29日
	 */
	@PostMapping("/findListByPage")
	public Object findFixedAssetClearList(@RequestBody Map<String, Object> queryMap){
		return fixedAssetClearService.findFixedAssetClearListByPage(queryMap);
	}
	
	/**
	 * 
	*  还原
	* @param queryMap 
	* currentPeriod date 当前账期
	* accountId String 账薄ID
	* }
	* @return Object 返回类型    
	* @throws 
	* @author mhb
	* @date 2017年11月29日
	 */
	@PutMapping("/restore/{ids}")
	public Object restore(@PathVariable("ids") String ids){
		return fixedAssetClearService.restore(ids);
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
		PageInfo<FixedAsset> pages =fixedAssetClearService.findFixedAssetClearListByPage(queryMap);
		String fileName = "固定资产清理数据" + DateUtils.getDate("yyyyMMddHHmmss")+ ".xlsx";
		try {
			new ExportExcel("固定资产清理数据", FixedAsset.class,1,2,3,6,7,8,10,14,15).setDataList(pages.getList()).write(response, fileName).dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
