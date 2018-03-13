package com.taoding.service.report.assetliability;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.report.assetliability.Item;
import com.taoding.domain.report.assetliability.ItemFormula;
import com.taoding.domain.report.assetliability.ItemFormulaTemplate;
import com.taoding.domain.report.assetliability.Type;
import com.taoding.mapper.accountingBook.AccountingBookDao;
import com.taoding.mapper.report.assetliability.ItemFormulaDao;
import com.taoding.mapper.report.assetliability.TypeDao;


@Service
@Transactional
public class ItemFormulaServiceImpl extends DefaultCurdServiceImpl<ItemFormulaDao, ItemFormula> implements ItemFormulaService{
	
	@Autowired
	TypeDao typeDao;
	
	@Autowired
	AccountingBookDao accountingBookDao;
	
	@Override
	public boolean init(int accountId, int accoutingRule) {
		List<Type> typeList = typeDao.getAssetLiabilityTypeByAccountRule(accoutingRule);
		List<ItemFormula> formulaLlist = new ArrayList<>();
		List<ItemFormulaTemplate> tempList = null;
		List<Item> itemList = null;
		ItemFormula itemFormula = null;
		for(Type type : typeList) {
			itemList = type.getItems();
			for (Item item : itemList) {
				tempList = item.getFormulaTemplats();
				for(ItemFormulaTemplate ft : tempList) {
					itemFormula = new ItemFormula();
					itemFormula.setItemId(ft.getItemId());
					itemFormula.setSubjectId(ft.getSubjectId());
					itemFormula.setOperation(ft.getOperation());
					itemFormula.setOperationSourceId(ft.getOperationSourceId());
					itemFormula.setAccountingId(accountId);
					formulaLlist.add(itemFormula);
				}
			}
		}
		return dao.batchSaveCustomerFormular(formulaLlist) == formulaLlist.size() ? true : false;
	}

	@Override
	public boolean saveCustomerFormular(ItemFormula itemFormula) {
		return dao.saveCustomerFormular(itemFormula)  > 0 ? true: false;
	}

	@Override
	public boolean deleteFormularById(int id) {
		return dao.deleteFormular(id) > 0 ? true: false;
	}

	@Override
	public List<ItemFormula> getFormularByItemIdAndaccountingId(int itemId, int accountId) {
		return dao.getFormularByItemIdAndaccountingId(itemId, accountId);
	}

	@Override
	public boolean updateFormulaById(int id, String subjectId, Integer operation, Integer operationSourceId) {
		ItemFormula itemFormula = new ItemFormula();
		itemFormula.setId(String.valueOf(id));
		itemFormula.setSubjectId(subjectId);
		itemFormula.setOperation(operation);
		itemFormula.setOperationSourceId(operationSourceId);
		return dao.updateFormula(itemFormula) > 0 ? true: false;
	}

	@Override
	public boolean reInitFormularByAccountingId(Integer accrountingId) {
		dao.deleteFormulaByAccountId(accrountingId);
		int accountRule = Integer.valueOf(accountingBookDao.get(String.valueOf(accrountingId)).getAccountingSystemId());
		return this.init(accrountingId, accountRule);
	}

}
