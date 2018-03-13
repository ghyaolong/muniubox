package com.taoding.service.report.assetliability;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.domain.report.Operation;
import com.taoding.domain.report.assetliability.Catalogue;
import com.taoding.domain.report.assetliability.Item;
import com.taoding.domain.report.assetliability.ItemFormula;
import com.taoding.domain.report.assetliability.ItemFormula.OperandSource;
import com.taoding.service.vouchersummary.CpaVoucherSummaryService;
import com.taoding.domain.report.assetliability.OperationSource;
import com.taoding.domain.report.assetliability.Type;
import com.taoding.domain.vouchersummary.CpaVoucherSummary;

@Service
public class AssetLiabilityFacadeServiceImpl implements AssetLiabilityFacadeService {

	@Autowired
	ItemFormulaService itemFormulaService;

	@Autowired
	TypeService typeService;

	@Autowired
	OperationSourceService operationSourceService;

	@Autowired
	CpaVoucherSummaryService cpaVoucherSummaryService;

	@Override
	public boolean init(int accountId, int accoutingRule) {
		return itemFormulaService.init(accountId, accoutingRule);
	}

	@Override
	public boolean saveCustomerFormular(ItemFormula itemFormula) {
		return itemFormulaService.saveCustomerFormular(itemFormula);
	}

	@Override
	public boolean deleteFormularById(int id) {
		return itemFormulaService.deleteFormularById(id);
	}

	@Override
	public List<ItemFormula> getFormularByItemIdAndaccountingId(int itemId, int accountId) {
		return itemFormulaService.getFormularByItemIdAndaccountingId(itemId, accountId);
	}

	@Override
	public boolean updateFormulaById(int id, String subjectId, Integer operation, Integer operationSourceId) {
		return itemFormulaService.updateFormulaById(id, subjectId, operation, operationSourceId);
	}

	@Override
	public boolean reInitFormularByAccountingId(Integer accrountingId) {
		return itemFormulaService.reInitFormularByAccountingId(accrountingId);
	}

	@Override
	public List<OperationSource> listOperationSourceListByModelId(Integer modelId) {
		return operationSourceService.listOperationSourceListByModelId(modelId);
	}

	@Override
	public Map<String, Object> getReportByBookIdAndPeriod(String accountId, Date period) {

		List<Catalogue> catalogueList = typeService.getAssetLiabilityTypeByAccountIdAndAccountRuleId(accountId,
				CurrentAccountingUtils.getCurrentAccountRuleByAccountingBookId(accountId));

		for (Catalogue catalogue : catalogueList) {
			
			BigDecimal beginningBalance = BigDecimal.ZERO;
			BigDecimal endingBalanceOfFinance = BigDecimal.ZERO;
			
			for (Type type : catalogue.getTypeList()) {
				this.accumulatingType(type);

				beginningBalance = this.compute(beginningBalance, type.getBeginningBalance(), Operation.ADD.getValue());
				endingBalanceOfFinance = this.compute(endingBalanceOfFinance, type.getEndingBalanceOfFinance(),
						Operation.ADD.getValue());
			}
			
			catalogue.setEndingBalanceOfFinance(endingBalanceOfFinance);
			catalogue.setBeginningBalance(beginningBalance);
		}
		
		Map<String, Object> report = new LinkedHashMap<String, Object>();
		report.put("balance", true);
		report.put("report", catalogueList);
		return report;
	}

	@Override
	public List<Type> getAssetLiabilityTypeByAccountRule(int accountRuleId) {
		return typeService.getAssetLiabilityTypeByAccountRule(accountRuleId);
	}

	/**
	 * 根据资产负债表的大类型分类统计
	 * 
	 * @param items
	 */
	private void accumulatingType(Type type) {

		Map<String, Item> idAndItemMap = new LinkedHashMap<String, Item>();

		BigDecimal beginningBalance = BigDecimal.ZERO;
		BigDecimal endingBalanceOfFinance = BigDecimal.ZERO;

		for (int i = 0; i < type.getItems().size(); i++) {
			this.accumulatingItem(type.getItems().get(i));

			Item item = type.getItems().get(i);

			// 子项目的名字需要加和特别的标记，比如第一个子项前加上“其中：”，其它前加上两个空格
			if (Item.IS_PARENT_ITEM.equals(item.getParentId())) {
				idAndItemMap.put(item.getId(), item);
			} else {
				if (idAndItemMap.get(String.valueOf(item.getParentId())).getSubItemList().size() == 0) {
					item.setItemName("其中：" + item.getItemName());
				} else {
					item.setItemName("&nbsp;&nbsp;" + item.getItemName());
				}
				idAndItemMap.get(String.valueOf(item.getParentId())).getSubItemList().add(item);
			}

			this.sumAccumulationItem(i, type.getItems());

			// 计算整个大类型的期末金额和年初金额
			if (item.getAccumulationType().equals(Integer.valueOf(0))) {
				beginningBalance = this.compute(beginningBalance, item.getBeginningBalance(), item.getOperation());
				endingBalanceOfFinance = this.compute(endingBalanceOfFinance, item.getEndingBalanceOfFinance(),
						item.getOperation());
			}
		}

		type.setBeginningBalance(beginningBalance);
		type.setEndingBalanceOfFinance(endingBalanceOfFinance);
	}

	/**
	 * 如果某项是汇聚项，也就是说其accumulationType的值为非0，那么根据逻辑进行计算
	 * 目前只有计算前两项的值，其对应的accumulationType的值为1
	 * 
	 * @param location
	 * @param items
	 */
	private void sumAccumulationItem(int location, List<Item> items) {
		// 如果是聚合项，并且值是1，那么计算前两项的值
		Item item = items.get(location);

		if (item.getAccumulationType() == 1) {
			// 如果i小于2，说明该项目前面的项目不足两个，所以不计算
			if (location >= 2) {
				Item firstItem = items.get(location - 2);
				Item secondItem = items.get(location - 1);
				
				BigDecimal beginningBalance = this.compute(firstItem.getBeginningBalance(),
						secondItem.getBeginningBalance(), item.getOperation());
				BigDecimal endingBalanceOfFinance = this.compute(firstItem.getEndingBalanceOfFinance(),
						secondItem.getEndingBalanceOfFinance(), item.getOperation());
				
				item.setEndingBalanceOfFinance(endingBalanceOfFinance);
				item.setBeginningBalance(beginningBalance);
			}
		}
	}

	/**
	 * 根据运算规则计算值
	 * 
	 * @param LeftOperand
	 *            左操作数
	 * @param rightOperand
	 *            右操作数
	 * @param operation
	 *            运算符
	 * @return
	 */
	private BigDecimal compute(BigDecimal leftOperand, BigDecimal rightOperand, Integer operation) {
		BigDecimal result = BigDecimal.ZERO;
		if (operation == Operation.MINUS.getValue()) {
			result = leftOperand.subtract(rightOperand);
		} else if (operation == Operation.ADD.getValue()) {
			result = leftOperand.add(rightOperand);
		}
		return result;
	}

	/**
	 * 分别统计单个报表项，根据公式算每个项目的值
	 * 
	 * @param item
	 *            单个报表项
	 */
	private void accumulatingItem(Item item) {
		List<ItemFormula> formulaLists = item.getFormulas();
		BigDecimal endingBalanceOfFinance = BigDecimal.ZERO;
		BigDecimal beginningBalanceOfYear = BigDecimal.ZERO;
		for (ItemFormula formula : formulaLists) {
			this.getOperand(formula);

			if (Operation.ADD.getValue() == formula.getOperation()) {
				beginningBalanceOfYear = beginningBalanceOfYear.add(formula.getBeginningBalanceOfYear());
				endingBalanceOfFinance = endingBalanceOfFinance.add(formula.getEndingBalanceOfFinance());
			} else if (Operation.MINUS.getValue() == formula.getOperation()) {
				beginningBalanceOfYear = beginningBalanceOfYear.subtract(formula.getBeginningBalanceOfYear());
				endingBalanceOfFinance = endingBalanceOfFinance.subtract(formula.getEndingBalanceOfFinance());
			}
		}

		item.setBeginningBalance(beginningBalanceOfYear);
		item.setEndingBalanceOfFinance(endingBalanceOfFinance);

		// 公式不统一显示给前台，根据项目的id去拉取所对应的公式
		item.setFormulas(null);
	}

	/**
	 * 本所公式项所配置的取数规则获取相应的操作数
	 * 
	 * @param formula
	 */
	private void getOperand(ItemFormula formula) {

		// 下面的代码是用来测试的
		Random random = new Random();
		formula.setBeginningBalanceOfYear(BigDecimal.valueOf(random.nextInt(50000)));
		formula.setEndingBalanceOfFinance(BigDecimal.valueOf(random.nextInt(50000)));

		return;

		/**
		 * CpaVoucherSummary summary =
		 * cpaVoucherSummaryService.findInfoAndAmountById(formula.getSubjectId(),
		 * String.valueOf(formula.getAccountingId()), 1); if (null == summary) { return;
		 * } if (OperandSource.BALANCE.getValue() == formula.getOperationSourceId()) {
		 * formula.setBeginningBalanceOfYear(BigDecimal.ZERO);
		 * formula.setEndingBalanceOfFinance(summary.getBalance()); return; }
		 * 
		 * if (OperandSource.BALANCE_FROM_DEBIT.getValue() ==
		 * formula.getOperationSourceId()) {
		 * formula.setBeginningBalanceOfYear(BigDecimal.ZERO);
		 * formula.setEndingBalanceOfFinance(summary.getCurrentPeriodDebit()); return; }
		 * 
		 * if (OperandSource.BALANCE_FROM_CREDI.getValue() ==
		 * formula.getOperationSourceId()) {
		 * formula.setBeginningBalanceOfYear(BigDecimal.ZERO);
		 * formula.setEndingBalanceOfFinance(summary.getCurrentPeriodCredit()); return;
		 * }
		 * 
		 * if (OperandSource.POSITIVE_BALANCE_FROM_DEBIT.getValue() ==
		 * formula.getOperationSourceId()) {
		 * formula.setBeginningBalanceOfYear(BigDecimal.ZERO);
		 * formula.setEndingBalanceOfFinance(
		 * summary.getCurrentPeriodDebit().compareTo(BigDecimal.ZERO) >= 0 ?
		 * summary.getCurrentPeriodDebit() : BigDecimal.ZERO); return; }
		 * 
		 * if (OperandSource.NEATIVE_BALANCE_FROM_DEBIT.getValue() ==
		 * formula.getOperationSourceId()) {
		 * formula.setBeginningBalanceOfYear(BigDecimal.ZERO);
		 * formula.setEndingBalanceOfFinance(
		 * summary.getCurrentPeriodDebit().compareTo(BigDecimal.ZERO) < 0 ?
		 * summary.getCurrentPeriodDebit() : BigDecimal.ZERO); return; }
		 * 
		 * if (OperandSource.POSITIVE_BALANCE_FROM_CREDI.getValue() ==
		 * formula.getOperationSourceId()) {
		 * formula.setBeginningBalanceOfYear(BigDecimal.ZERO);
		 * formula.setEndingBalanceOfFinance(
		 * summary.getCurrentPeriodCredit().compareTo(BigDecimal.ZERO) >= 0 ?
		 * summary.getCurrentPeriodCredit() : BigDecimal.ZERO); return; }
		 * 
		 * if (OperandSource.NEATIVE_BALANCE_FROM_CREDI.getValue() ==
		 * formula.getOperationSourceId()) {
		 * formula.setBeginningBalanceOfYear(BigDecimal.ZERO);
		 * formula.setEndingBalanceOfFinance(
		 * summary.getCurrentPeriodCredit().compareTo(BigDecimal.ZERO) >= 0 ?
		 * summary.getCurrentPeriodCredit() : BigDecimal.ZERO); return; }
		 **/
	}
}
