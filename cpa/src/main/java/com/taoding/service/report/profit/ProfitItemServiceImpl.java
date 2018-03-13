package com.taoding.service.report.profit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.domain.report.profit.ProfileCustomerFomula;
import com.taoding.domain.report.profit.ProfitItem;
import com.taoding.domain.subject.CpaCustomerImport;
import com.taoding.mapper.report.profit.ProfitItemDao;

@Service
@Transactional
public class ProfitItemServiceImpl  extends DefaultCurdServiceImpl<ProfitItemDao, ProfitItem> implements ProfitItemService {

	@Autowired
	private ProfileCustomerFomulaService pcfsService;
	
	//一级序号数组
	private final String[]  oneLevelSerialNumber = {"一、","二、","三、","四、","五、","六、","七、","八、","九、","十、"};
	
	//二级序号数组
	private final String[]  twoLevelSerialNumber = {"（一）","（二）","（三）","（四）","（五）","（六）","（七）","（八）","（九）","(十)"};
	
	//汇总项在List中的位置
	private Integer[] aggregationLocation = {-1,-1,-1,-1,-1,-1,-1};
	
	//汇总项
	private final String[] aggregationStr = {"营业收入","营业利润","利润总额","净利润","其他综合收益的税后净额","综合收益总额","每股收益："};
	//零
	public static final BigDecimal BIGDECIMAL_ZERO = new BigDecimal(0);
	public static final BigDecimal BIGDECIMAL_ONE = new BigDecimal(1);
	@Override
	public List<ProfitItem> getgetProfitItem(String accountId) {
		List<ProfileCustomerFomula> fomulaList = pcfsService.getProfileCustomerFomulaList(accountId);
		if(fomulaList == null ||fomulaList.size() <= 0){
			throw new LogicException("客户公式表未初始化！");
		}
		//获取当前会计准则
		Integer rule = CurrentAccountingUtils.getCurrentAccountRuleByAccountingBookId(accountId);
		List<ProfitItem> profitItemList = this.profitItemListByRule(rule, accountId);
		List<ProfitItem> parentList = new ArrayList<ProfitItem>();
		List<ProfitItem> childList = new ArrayList<ProfitItem>();
		//构造树结构
		Integer[] aggregationLocationNew = {-1,-1,-1,-1,-1,-1,-1};
		for(int i = 0,j = 0;i < profitItemList.size();i++){
			ProfitItem p = profitItemList.get(i);
			this.computeMoney(p);
			//获取需要汇总项的位置
			if(p.getParentId().equals(-1)){
				for(int k = 0;k < aggregationStr.length;k++){
					if((this.getPreName(p.getName())).equals(aggregationStr[k])){
						aggregationLocationNew[k] = j;
					}
				}
				if(oneLevelSerialNumber.length > j){
					p.setName(oneLevelSerialNumber[j] + p.getName());
				}
				j++;
				parentList.add(p);
			}else{
				childList.add(p);
			}
		}
		this.aggregationLocation = aggregationLocationNew;
		for(ProfitItem pf : parentList){
			this.makeProfitItemTree(childList,pf);
		}
		for(ProfitItem parent: parentList){
			if(parent.getShowRule() == ProfitItem.SHOW_RULE_GENERAL){
				this.makeGeneralShow(parent);
			}else if(parent.getShowRule() == ProfitItem.SHOW_RULE_NUMBER){
				this.makeNumberShow(parent);
			}
		}
		this.computerAggregationMoney(parentList);
		return parentList;
	}

	@Override
	public List<ProfitItem> profitItemListByRule(Integer rule, String accountId) {
		return dao.profitItemListByRule(rule,accountId);
	}
	
	/**
	 * 计算公式项的本期金额和本年累计金额
	 * @author fc 
	 * @version 2017年12月28日 上午11:45:00 
	 * @param p
	 */
	public void computeMoney(ProfitItem p){
		List<ProfileCustomerFomula> fomulaList = p.getProfileCustomerFomulaList();
		BigDecimal currentMoney = ProfitItemServiceImpl.BIGDECIMAL_ZERO;
		BigDecimal yearMoney = ProfitItemServiceImpl.BIGDECIMAL_ZERO;
		BigDecimal hhh = null;
		for(int j = 0; j < fomulaList.size(); j++){
			hhh = new BigDecimal(fomulaList.get(j).getOperation());
			currentMoney = currentMoney.add(new BigDecimal(fomulaList.get(j).getOperation()).multiply(getMoneyByfomula(fomulaList.get(j)).get("currentMoney")));
			yearMoney = yearMoney.add(new BigDecimal(fomulaList.get(j).getOperation()).multiply(getMoneyByfomula(fomulaList.get(j)).get("yearMoney")));
		}
		p.setCurrentMoney(currentMoney);
		p.setYearMoney(yearMoney);
	}
	
	/**
	 * 获取公式对应的本期金额和本年累计金额
	 * @author fc 
	 * @version 2017年12月28日 上午11:45:54 
	 * @param pf
	 * @return
	 */
	public Map<String,BigDecimal> getMoneyByfomula(ProfileCustomerFomula pf){
		Map<String,BigDecimal> fomulaMoneyMap = new  HashMap<String,BigDecimal>();
		fomulaMoneyMap.put("currentMoney",ProfitItemServiceImpl.BIGDECIMAL_ONE);
		fomulaMoneyMap.put("yearMoney",ProfitItemServiceImpl.BIGDECIMAL_ONE);
		return fomulaMoneyMap;
	}
	
	/**
	 * 递归构造树结构
	 * @author fc 
	 * @version 2017年12月28日 上午11:52:19 
	 */
	public void makeProfitItemTree(List<ProfitItem> childList,ProfitItem pf){
		List<ProfitItem> cList = new ArrayList<ProfitItem>();
		for(ProfitItem profitItem : childList){
			if(profitItem.getParentId() != null && profitItem.getParentId() > 0){
				if(profitItem.getParentId().equals(Integer.parseInt(pf.getId()))){
					cList.add(profitItem);
				}
			}
		}
		if(cList.size() <= 0){
			return;
		}else{
			pf.setSubProfitItems(cList);
			for(int i= 0; i<pf.getSubProfitItems().size(); i++){
				makeProfitItemTree(childList,pf.getSubProfitItems().get(i));
			}
		}
	}
	
	/**
	 * 构造普通展示结构
	 * @author fc 
	 * @version 2017年12月28日 下午2:59:43 
	 */
	public void makeGeneralShow(ProfitItem parent){
		if(parent.getSubProfitItems() != null && parent.getSubProfitItems().size() > 0){
			Integer operate = 0;
			for(int i = 0;i < parent.getSubProfitItems().size();i++){
				if(i == 0 ){
					operate = parent.getSubProfitItems().get(i).getOperation();
					if(operate == -1){
						parent.getSubProfitItems().get(i).setName("减："+parent.getSubProfitItems().get(i).getName());
					}else if(operate == 1){
						parent.getSubProfitItems().get(i).setName("加："+parent.getSubProfitItems().get(i).getName());
					}
				}else{
					if(parent.getSubProfitItems().get(i).getOperation() != operate){
						operate = parent.getSubProfitItems().get(i).getOperation();
						if(operate == -1){
							parent.getSubProfitItems().get(i).setName("减："+parent.getSubProfitItems().get(i).getName());
						}else if(operate == 1){
							parent.getSubProfitItems().get(i).setName("加："+parent.getSubProfitItems().get(i).getName());
						}
					}
				}
				this.addQiZhong(parent.getSubProfitItems().get(i));
			}
		}
	}
	
	/**
	 * 添加"其中："二字
	 * @author fc 
	 * @version 2017年12月28日 下午3:13:13 
	 */
	public void addQiZhong(ProfitItem parent){
		if(parent.getSubProfitItems() == null || parent.getSubProfitItems().size() <= 0){
			return;
		}else{
			parent.getSubProfitItems().get(0).setName("其中："+parent.getSubProfitItems().get(0).getName());
			for(ProfitItem p : parent.getSubProfitItems()){
				addQiZhong(p);
			}
		}
	}
	
	/**
	 * 构造序号展示结构第二级
	 * @author fc 
	 * @version 2017年12月28日 下午3:44:46 
	 */
	public void makeNumberShow(ProfitItem parent){
		if(parent.getSubProfitItems() != null && parent.getSubProfitItems().size() > 0){
			for(int i = 0;i < parent.getSubProfitItems().size();i++){
				if(twoLevelSerialNumber.length > i){
					parent.getSubProfitItems().get(i).setName(twoLevelSerialNumber[i]+parent.getSubProfitItems().get(i).getName());
				}
				this.addThirdNumber(parent.getSubProfitItems().get(i));
			}
		}
	}
	
	/**
	 * 序号展示结构第三级以及其他层级构造
	 * @author fc 
	 * @version 2017年12月28日 下午3:51:50 
	 */
	public void addThirdNumber(ProfitItem parent){
		if(parent.getSubProfitItems() == null || parent.getSubProfitItems().size() <= 0){
			return;
		}else{
			for(int i = 0;i < parent.getSubProfitItems().size();i++){
				parent.getSubProfitItems().get(i).setName((i+1)+"."+parent.getSubProfitItems().get(i).getName());
				addThirdNumber(parent.getSubProfitItems().get(i));
			}
		}
	}
	
	/**
	 * 计算汇总项的金额
	 * @author fc 
	 * @version 2017年12月28日 下午5:15:38 
	 */
	public void computerAggregationMoney(List<ProfitItem> parentList){
		//普通层级汇总项金额计算(营业利润,利润总额,净利润)
		for(int i = 1;i < 4;i++){
			if(aggregationLocation[i] != -1){
				if(aggregationLocation[i-1] != -1)
				{
					Map<String,BigDecimal> map1 = this.computerGeneralMoney(parentList.get(aggregationLocation[i-1]));
					parentList.get(aggregationLocation[i]).setCurrentMoney(map1.get("currentMoney"));
					parentList.get(aggregationLocation[i]).setYearMoney(map1.get("yearMoney"));
				}else{
					throw new LogicException("计算"+aggregationStr[i]+"条件不足！");
				}
			}
		}
		//其他综合收益的税后净额汇总金额计算
		if(aggregationLocation[4] != -1){
			this.computerOtherIncome(parentList.get(aggregationLocation[4]));
		}
		//综合收益总额
		if(aggregationLocation[5] != -1){
			if(aggregationLocation[4] != -1 && aggregationLocation[3] != -1){
				BigDecimal currentMoney = ProfitItemServiceImpl.BIGDECIMAL_ZERO;
				BigDecimal yearMoney = ProfitItemServiceImpl.BIGDECIMAL_ZERO;
				if(parentList.get(aggregationLocation[4]).getCurrentMoney() != null){
					currentMoney = currentMoney.add(parentList.get(aggregationLocation[4]).getCurrentMoney());
				}
				if(parentList.get(aggregationLocation[3]).getCurrentMoney() != null){
					currentMoney = currentMoney.add(parentList.get(aggregationLocation[3]).getCurrentMoney());
				}
				if(parentList.get(aggregationLocation[4]).getYearMoney() != null){
					yearMoney = yearMoney.add(parentList.get(aggregationLocation[4]).getYearMoney());
				}
				if(parentList.get(aggregationLocation[3]).getYearMoney() != null){
					yearMoney = yearMoney.add(parentList.get(aggregationLocation[3]).getYearMoney());
				}
				parentList.get(aggregationLocation[5]).setCurrentMoney(currentMoney);
				parentList.get(aggregationLocation[5]).setYearMoney(yearMoney);
			}else{
				throw new LogicException("计算综合收益总额条件不足！");
			}
		}
		//每股收益
		if(aggregationLocation[6] != -1){
			ProfitItem perShare = parentList.get(aggregationLocation[6]);
			BigDecimal currentMoney = ProfitItemServiceImpl.BIGDECIMAL_ZERO;
			BigDecimal yearMoney = ProfitItemServiceImpl.BIGDECIMAL_ZERO;
			for(ProfitItem p : perShare.getSubProfitItems()){
				if(p.getCurrentMoney() != null){
					currentMoney = currentMoney.add(p.getCurrentMoney());
				}
				if(p.getYearMoney() != null){
					yearMoney = yearMoney.add(p.getYearMoney());
				}
			}
			perShare.setCurrentMoney(currentMoney);
			perShare.setYearMoney(yearMoney);
		}
		
	}
	
	/**
	 * 普通层级汇总项金额计算
	 * @author fc 
	 * @version 2017年12月28日 下午5:42:58 
	 * @param profitItem
	 * @return
	 */
	public Map<String,BigDecimal> computerGeneralMoney(ProfitItem profitItem){
		BigDecimal currentMoney = ProfitItemServiceImpl.BIGDECIMAL_ZERO;
		BigDecimal yearMoney = ProfitItemServiceImpl.BIGDECIMAL_ZERO;
		if(profitItem.getCurrentMoney() != null){
			currentMoney = currentMoney.add(profitItem.getCurrentMoney());
		}
		if(profitItem.getYearMoney() != null){
			yearMoney = yearMoney.add(profitItem.getYearMoney());
		}
		if(profitItem.getSubProfitItems() != null){
			Integer operate = 0;
			for(ProfitItem p : profitItem.getSubProfitItems()){
				operate = p.getOperation();
				if(operate == -1){
					if(p.getCurrentMoney() != null){
						currentMoney = currentMoney.subtract(p.getCurrentMoney());
					}
					if(p.getYearMoney() != null){
						yearMoney = yearMoney.subtract(p.getYearMoney());
					}
				}
				if(operate == 1){
					if(p.getCurrentMoney() != null){
						currentMoney = currentMoney.add(p.getCurrentMoney());
					}
					if(p.getYearMoney() != null){
						yearMoney = yearMoney.add(p.getYearMoney());
					}
				}
			}
		}
		Map<String,BigDecimal> moneyMap = new HashMap<String,BigDecimal>();
		moneyMap.put("currentMoney", currentMoney);
		moneyMap.put("yearMoney", yearMoney);
		return moneyMap;
	}
	
	/**
	 * 其他综合收益的税后净额汇总金额计算
	 * @author fc 
	 * @version 2017年12月29日 上午9:47:11 
	 * @param OtherIncome
	 */
	public void computerOtherIncome(ProfitItem OtherIncome){
		if(OtherIncome.getSubProfitItems() != null){
			BigDecimal currentMoneyFirst = ProfitItemServiceImpl.BIGDECIMAL_ZERO;
			BigDecimal yearMoneyFirst = ProfitItemServiceImpl.BIGDECIMAL_ZERO;
			for(ProfitItem p : OtherIncome.getSubProfitItems()){
				if(p.getSubProfitItems() != null){
					BigDecimal currentMoney = ProfitItemServiceImpl.BIGDECIMAL_ZERO;
					BigDecimal yearMoney = ProfitItemServiceImpl.BIGDECIMAL_ZERO;
					for(ProfitItem pf : p.getSubProfitItems()){
						if(pf.getCurrentMoney() != null){
							currentMoney = currentMoney.add(pf.getCurrentMoney());
						}
						if(pf.getYearMoney() != null){
							yearMoney = yearMoney.add(pf.getYearMoney());
						}
					}
					p.setCurrentMoney(currentMoney);
					p.setYearMoney(yearMoney);
					if(p.getCurrentMoney() != null){
						currentMoneyFirst = currentMoneyFirst.add(p.getCurrentMoney());
					}
					if(p.getYearMoney() != null){
						yearMoneyFirst = yearMoneyFirst.add(p.getYearMoney());
					}
				}
			}
			OtherIncome.setCurrentMoney(currentMoneyFirst);
			OtherIncome.setYearMoney(yearMoneyFirst);
		}
	}
	
	/**
	 * 获取不带括号的(纯净的)名字
	 * @author fc 
	 * @version 2017年12月29日 上午10:15:50 
	 * @return
	 */
	public String getPreName(String str){
		if(str != null){
			String[] strArry = str.split("（");
			return strArry[0].trim();
		}
		return "";
	}
	
	/**
	 * 如果当前数值为0则返回null
	 * @author fc 
	 * @version 2017年12月29日 下午2:05:53 
	 * @return
	 */
	public BigDecimal setZeroBigDecimal(BigDecimal bigDecimal){
		if(bigDecimal.equals(ProfitItemServiceImpl.BIGDECIMAL_ZERO)){
			return null;
		}else{
			return bigDecimal;
		}
	}

}
