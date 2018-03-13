package com.taoding.controller.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.logging.LogException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.domain.report.assetliability.ItemFormula;
import com.taoding.service.report.assetliability.AssetLiabilityFacadeService;


@RestController
public class AssetLiabilityController {
	
	@Autowired
	AssetLiabilityFacadeService assetLiabilityFacadeService;
	
	/**
	 * 根据模块名获取取数规则
	 * @param modelId
	 * @return
	 */
	@GetMapping("/report/assetLiability/operationSource")
	public Object listOperationSourceByModelId() {
		return assetLiabilityFacadeService.listOperationSourceListByModelId(1);
	}
	
	
	/**
	 * 添加公式项
	 * @param itemFormula
	 * @return
	 */
	@PostMapping("/report/assetLiability/formula")
	public Object addFormula(@RequestBody ItemFormula itemFormula) {
		return assetLiabilityFacadeService.saveCustomerFormular(itemFormula);
	}
	
	/**
	 * 修改公式项。只能更新某个公式项的操作符，取 数规则和关联科目
	 * @param itemFormula
	 * @return
	 */
	@PutMapping("/report/assetLiability/formula")
	public Object updateFormula(@RequestBody ItemFormula itemFormula) {
		return assetLiabilityFacadeService.updateFormulaById(Integer.valueOf(itemFormula.getId()), itemFormula.getSubjectId(), 
				itemFormula.getOperation(), itemFormula.getOperationSourceId());
	}
	
	/**
	 * 根据id删除公式项
	 * @param id
	 * @return
	 */
	@DeleteMapping("/report/assetLiability/formula/{id}")
	public Object deleteFormula(@PathVariable("id") Integer id) {
		return assetLiabilityFacadeService.deleteFormularById(id);
	}
	
	/**
	 * 重新初始化公式
	 * @param accountingId
	 * @return
	 */
	@PostMapping("/report/assetLiability/formula/{accountingId}/init")
	public Object reInitCustomerFormula(@PathVariable("accountingId") Integer accountingId) {
		return assetLiabilityFacadeService.reInitFormularByAccountingId(accountingId);
	}
	
	/**
	 * 获取某个项目下的所有公式
	 * @param itemId
	 * @param accountingId
	 * @return
	 */
	@GetMapping("/report/assetLiability/formula/{itemId}/{accountingId}")
	public Object getFormularsByItemId(@PathVariable("itemId") Integer itemId, @PathVariable("accountingId") Integer accountingId) {
		return assetLiabilityFacadeService.getFormularByItemIdAndaccountingId(itemId, accountingId);
	}
	
	/**
	 * 根据会计准则获取报表项
	 * @return
	 */
	@GetMapping("/report/assetLiability/report/{accountingId}/{accountPeroid}")
	public Object assetLiabilityReport(@PathVariable("accountingId") String accountingId, @PathVariable("accountPeroid") String periodString) {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date period;
		try {
			period = simpleDateFormat.parse(periodString);
			return assetLiabilityFacadeService.getReportByBookIdAndPeriod(accountingId, period);
		} catch (ParseException e) {
			throw new LogException("账期格式不对");
		}
	}
}
