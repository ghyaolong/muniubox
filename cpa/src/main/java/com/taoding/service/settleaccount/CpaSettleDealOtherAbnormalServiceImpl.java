package com.taoding.service.settleaccount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.domain.settleaccount.AssetClassBalanceVO;
import com.taoding.domain.settleaccount.CpaFinalLiquidation;
import com.taoding.service.fixedAsset.FixedAssetService;
import com.taoding.service.fixedAsset.IntangibleAssetService;
import com.taoding.service.fixedAsset.LongApportionedAssetsService;
import com.taoding.service.subject.CpaCustomerSubjectService;
import com.taoding.service.ticket.TicketService;
import com.taoding.service.voucher.CpaVoucherService;

/**
 * 期末结账加载其他异常数据
 * @author admin
 *
 */
@SuppressWarnings("all")
@Service
public class CpaSettleDealOtherAbnormalServiceImpl implements CpaSettleDealOtherAbnormalService {
	
	@Autowired
	private CpaFinalLiquidationService finalLiquidationService ; //期末结转
	@Autowired
	private CpaCustomerSubjectService customerSubjectService ; //科目期初
	@Autowired
	private FixedAssetService fixedAssetService; //固定资产
	@Autowired
	private IntangibleAssetService intangibleAssetService ;// 无形资产
	@Autowired
	private LongApportionedAssetsService longApportionedAssetsService ;//长期待摊费用
	@Autowired
	private TicketService ticketService ;// 票据
	@Autowired
	private CpaVoucherService voucherService ; // 凭证
	
	/**
	 * 加载其他异常数据
	 * 2017年12月26日 下午2:56:14
	 * @param bookId
	 * @return
	 */
	public List<AssetClassBalanceVO> loadOtherAbnormalData(String bookId){

		String currentPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		
		List<AssetClassBalanceVO> lists = new ArrayList<AssetClassBalanceVO>() ;
		lists.add(deal_finalLiquidation(bookId, currentPeriod));
		lists.add(deal_customersubject(bookId));
		lists.add(deal_fixedAsset(bookId));
		lists.add(deal_intangibleAsset(bookId));
		lists.add(deal_longApportionedAsset(bookId));
		lists.add(deal_balanceSheet(bookId, currentPeriod));
		lists.add(deal_cashflow(bookId, currentPeriod));
		lists.add(deal_ownersequity(bookId, currentPeriod));
		lists.add(deal_faultNo(bookId, currentPeriod));
		lists.add(deal_noAccountBill(bookId));
		
		return lists;
	}
	
	/**
	 * 处理是否期末结转
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	private AssetClassBalanceVO deal_finalLiquidation(String bookId, String currentPeriod){
		//判断是否期末结账
		CpaFinalLiquidation finalLiquidation = finalLiquidationService.findByBookId(bookId, currentPeriod);
		boolean flag = false;
		String message = "请先进行期末结账";
		if(finalLiquidation != null && finalLiquidation.isSettleAccounts()){
			flag = true ;
			message = "已期末结账";
		}
		return new AssetClassBalanceVO("OTHER_LIQUIDATION", "期末结转", flag ,message);
	}
	
	/**
	 * 判断科目期初金额是否平衡
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	private AssetClassBalanceVO deal_customersubject(String bookId){
		Map<String,Object> maps = (Map<String, Object>) customerSubjectService.trialBalanceData(bookId);
		Map<String,Object> beginmaps = (Map<String, Object>) maps.get("beginTotal");
		Map<String,Object> currentmaps = (Map<String, Object>) maps.get("currentTotal");
		
		BigDecimal beginningDebit = (BigDecimal) beginmaps.get("beginning_debit");
		BigDecimal beginningCredit = (BigDecimal) beginmaps.get("beginning_credit");
		BigDecimal currentDebit = (BigDecimal) currentmaps.get("current_debit");
		BigDecimal currentCredit = (BigDecimal) currentmaps.get("current_credit");
		
		int beginning = beginningDebit.compareTo(beginningCredit);
		int current = currentDebit.compareTo(currentCredit);
		
		boolean flag = false;
		String message = "不平衡";
		if(beginning == 0 && current == 0){
			flag = true ;
			message = "平衡";
		}
		return new AssetClassBalanceVO("OTHER_CUSTOMERSUBJECT", "科目期初", flag ,message);
	}
	
	/**
	 * 判断 固定资产 是否平衡
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	private AssetClassBalanceVO deal_fixedAsset(String bookId){
		Map<String,Object> maps = fixedAssetService.reconciliation(bookId);
		boolean flag = true;
		String message = "平衡";
		if(maps != null && maps.size() > 0){
			boolean isAssetEquilibria = (boolean) maps.get("isAssetEquilibria") ; //资产平衡
			boolean isAccumulatedEquilibria = (boolean) maps.get("isAccumulatedEquilibria") ; //累计平衡
			if(!isAssetEquilibria || !isAccumulatedEquilibria){
				flag =  false ;
				message = "不平衡";
			}
		}
		return new AssetClassBalanceVO("OTHER_FIXEDASSET", "固定资产", flag ,message);
	}
	
	/**
	 * 判断 无形资产  是否平衡
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	private AssetClassBalanceVO deal_intangibleAsset(String bookId){
		
		Map<String,Object> maps = intangibleAssetService.reconciliation(bookId);
		boolean flag = true;
		String message = "平衡";
		if(maps != null && maps.size() > 0){
			boolean isAssetEquilibria = (boolean) maps.get("isAssetEquilibria") ; //资产平衡
			boolean isAccumulatedEquilibria = (boolean) maps.get("isAccumulatedEquilibria") ; //累计平衡
			if(!isAssetEquilibria || !isAccumulatedEquilibria){
				flag =  false ;
				message = "不平衡";
			}
		}
		return new AssetClassBalanceVO("OTHER_FIXEDASSET", "无形资产", flag ,message);
	}
	
	/**
	 * 判断 长期待摊费用  是否平衡
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	private AssetClassBalanceVO deal_longApportionedAsset(String bookId){
		Map<String,Object> maps = (Map<String,Object>)longApportionedAssetsService.reconciliation(bookId);
		boolean flag = true;
		String message = "平衡";
		if(maps != null && maps.size() > 0){
			boolean isAssetEquilibria = (boolean) maps.get("isAssetEquilibria") ; //资产平衡
			if(!isAssetEquilibria){
				flag =  false ;
				message = "不平衡";
			}
		}
		return new AssetClassBalanceVO("OTHER_LONGAPPORTIONEDASSET", "长期待摊费用", flag ,message);
	}
	
	/**
	 * 判断 资产负债表  是否平衡
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	private AssetClassBalanceVO deal_balanceSheet(String bookId, String currentPeriod){
		return new AssetClassBalanceVO("OTHER_BALANCESHEET", "资产负债表", true ,"平衡");
	}
	
	/**
	 * 判断 现金流量表  是否平衡
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	private AssetClassBalanceVO deal_cashflow(String bookId, String currentPeriod){
		return new AssetClassBalanceVO("OTHER_CASHFLOW", "现金流量表", true ,"平衡");
	}
	
	/**
	 * 判断 所有者权益变动表  是否平衡
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	private AssetClassBalanceVO deal_ownersequity(String bookId, String currentPeriod){
		return new AssetClassBalanceVO("OTHER_OWNERSEQUITY", "所有者权益变动表", true ,"平衡");
	}
	
	/**
	 * 判断 是否有断号
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	private AssetClassBalanceVO deal_faultNo(String bookId, String currentPeriod){
		boolean flag = voucherService.isFaultVoucherNo(currentPeriod, bookId);
		
		String message = "无断号" ;
		if(!flag){
			message = "有断号";
		}
		
		return new AssetClassBalanceVO("OTHER_FAULTNO", "断号", flag ,message);
	}
	
	/**
	 * 判断 是否有 未做账票据
	 * @param bookId
	 * @param currentPeriod
	 * @return
	 */
	private AssetClassBalanceVO deal_noAccountBill(String bookId){
		
		boolean hasUnComplated = ticketService.hasUnComplatedTicket(bookId);
		String message = "有";
		boolean  flag = false ;
		if(!hasUnComplated){
			flag = true ;
			message = "无";
		}
		return new AssetClassBalanceVO("OTHER_NOACCOUNTBILL", "未做账票据", true ,"无");
	}
	
	
}
