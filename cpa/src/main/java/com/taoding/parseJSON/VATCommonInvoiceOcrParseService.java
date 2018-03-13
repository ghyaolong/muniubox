package com.taoding.parseJSON;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.taoding.common.utils.AreaDictUtils;
import com.taoding.common.utils.MyStringUtils;
import com.taoding.domain.ticket.vo.Summary;
import com.taoding.invoiceTemp.VATCommonInvoice;
import com.taoding.ocr.module.Location;
import com.taoding.ocr.module.WordsResult;
import com.taoding.service.ocr.OCRService;

/**
 * Created by yaochenglong on 2017/11/23.
 * 增值税普通发票解析器
 */
public class VATCommonInvoiceOcrParseService implements OCRService<VATCommonInvoice> {

	private static final Logger logger = LoggerFactory.getLogger(VATCommonInvoiceOcrParseService.class);

	public static void main(String[] args) {
		// System.out.println(baseStr);
		VATCommonInvoiceOcrParseService p = new VATCommonInvoiceOcrParseService();
		//p.parse(baseStr);

	}

	@Override
	public VATCommonInvoice parse(List<WordsResult> wordsResults) {
		//GeneralTicket entity = JSON.parseObject(jsonStr, GeneralTicket.class);
		// GeneralTicket entity = JsonUtil.jsonToPojo(jsonStr, GeneralTicket.class);
		//List<WordsResult> wordsResults = entity.getWords_result();
		List<WordsResult> list = new ArrayList<>(wordsResults);

		// List<Map<String, Location>> summaryList = new ArrayList<>();
		List<String> eachSummary = new ArrayList<String>();
		List<String> summaryTempList = new ArrayList<String>();
		List<List<String>> summaryLists = new ArrayList<List<String>>();

		List<Summary> summaryColl = new ArrayList<Summary>();

		Collections.sort(list);
		VATCommonInvoice vat = new VATCommonInvoice();
		// 记录循环的计数器
		int count = 0;
		// 公司名称计数器,第一次出现则为购买方，第二次出现带有名称的字符则为销售方名称。
		int nameCount = 0;
		// 纳税人识别号计数器,第一次出现则为购买方，第二次出现带有名称的字符则为销售方名称。
		int TINCount = 0;
		
		int ticketCodeCount = 0;

		BigDecimal number1 = null;
		BigDecimal number2 = null;
		int ticketNumCount = 0;

		Integer topBegin = null;
		Integer leftBegin = null;
		Integer topEnd = null;
		Integer leftEnd = null;
		Integer leftTemp = 0;
		for (int i = 0; i < list.size(); i++) {
			WordsResult wordsResult = list.get(i);
			// 取出普通发票的抬头
			Location location = wordsResult.getLocation();
			// 获取抬头
			String value = wordsResult.getWords();
			/**
			 * XXX 发票解析
			 * 无法获取发票号码等属性，因为目前百度的票据识别服务识别出来的发票号码等属性不具有规律性，
			 * 目前无法获取，看等到12月份百度出的模板功能，是否可以取出相关的属性数据
			 */
			// 发票号码

			if (ticketNumCount == 0) {
				String ticketNumberPattern = "(?<!\\d)(\\d{8})(?!\\d)";
				Pattern ticketNumberR = Pattern.compile(ticketNumberPattern);
				Matcher ticketNumberM = ticketNumberR.matcher(value);
				while (ticketNumberM.find()) {
					String ticket_no = ticketNumberM.group(0);
					vat.setInvoice_no(ticket_no);
					ticketNumCount++;
				}
			}

			// 发票代码
			String ticketCodePattern = "(?<!\\d)(\\d{10})(?!\\d)";
			Pattern ticketCodeP = Pattern.compile(ticketCodePattern);
			Matcher ticketCodeM = ticketCodeP.matcher(value);
			while (ticketCodeM.find()) {
				if(ticketCodeCount==0) {
					String ticket_code = ticketCodeM.group(0);
					vat.setInvoice_code(ticket_code);
					
					String areaCode = ticket_code.substring(0, 4);
					
					// 第8位表示发票的种类，3表示普通发票，1表示专用发票
					String _8 = ticket_code.substring(7, 8);
					if (_8.equals("1")) {
						vat.setType(VATCommonInvoice.INVOICE_TYPE_SPECIAL);
					} else if (_8.equals("3")) {
						vat.setType(VATCommonInvoice.INVOICE_TYPE_NORMAL);
					}
					
					String province = AreaDictUtils.getProvinceByCode(areaCode);
					vat.setProvince(province);
				}
				ticketCodeCount++;
			}
			// 发票校验码 XXX 发票校验码
			
			/////////////////////////////////////获取摘要相关逻辑/////////////////////////////////////////////////////////
			// 发票摘要
			/*if (value.indexOf("货物或应税劳务") != -1) {
				topBegin = location.getTop();
				leftBegin = location.getLeft();
			}

			//
			if (topBegin != null && leftBegin != null) {

				if (value.indexOf("价税合计") != -1) {
					topEnd = location.getTop();
					leftEnd = location.getLeft();
				}

				Map<String, Location> ml = null;
				if (topEnd != null && leftEnd != null) {
					if (location.getTop() - topBegin > 60 && Math.abs(location.getLeft() - leftBegin) > 10
							&& location.getTop() < topEnd) {
						logger.info("====" + value);
						// eachSummary.add(value);

					}
				} else {
					if (location.getTop() - topBegin > 60 && Math.abs(location.getLeft() - leftBegin) > 10) {
						ml = new HashMap<>();
						ml.put(value, location);
						logger.info("====++" + value);
						eachSummary.add(value);
					}
				}
			}*/
			//////////////////////////////////获取摘要相关逻辑/////////////////////////////////////////////////////////

			// 开票日期
			if (value.indexOf("开票日期") != -1) {
				String invoice_date = value.substring(value.indexOf(":") + 1);
				vat.setInvoice_date(invoice_date);
				logger.info("开票日期:" + invoice_date);
			}
			// 称:西安炳益智能电子科技有限公司
			// 名称

			if ((value.indexOf("名称") != -1 || value.indexOf("称") != -1 || value.indexOf("称:") != -1)
					&& value.indexOf("劳") == -1) {
				// System.out.println(location.getTop());
				if (value.indexOf(":") != -1) {
					value = value.substring(value.indexOf(":") + 1);
				} else {
					value = value.substring(value.indexOf("称") + 1);
				}

				if (nameCount == 0) {
					vat.setPurchaser(value);
					nameCount++;
				} else {
					vat.setSeller(value);
				}

				logger.info("销售方名称:" + value);
			}

			// 购买方/销售方纳税人识别号
			/*String tinReg ="\\*^*(\\\\d{15}|\\\\d{18}|\\\\d{20})$";
			boolean isMatch = Pattern.matches(tinReg,value);
			if(isMatch){
			   System.out.println(":::::::"+value);
			}*/

			String TIN = "";
			if (value.indexOf("纳税人识别号") != -1) {

				TIN = value.substring(value.indexOf(":") != -1 ? value.indexOf(":") + 1 : value.length());
				if (TINCount == 0) {
					// 购买方纳税人
					vat.setPurchaserTIN(TIN);
					logger.info("购买方纳税人:" + TIN);
				} else {
					// 销售方，有可能是空
					vat.setSellerTIN(TIN);
					logger.info("销售方纳税人:" + TIN);
				}
				/*if (location.getTop()<500){
				    //购买方纳税人
				    vat.setPurchaserTIN(TIN);
				    logger.info("购买方纳税人:"+TIN);
				}else{
				    //销售方，有可能是空
				    vat.setSellerTIN(TIN);
				    logger.info("销售方纳税人:"+TIN);
				}*/

				TINCount++;

			}

			// 继续查找销售方
			if (value.length() == 18) {
				// 继续查找，
				vat.setSellerTIN(value);
				logger.info("销售方纳税人:" + TIN);
			}

			// 购买方/销售方电话或地址
			// 6//地址、电话:西安市高新区高新路6号1幢1单元12501宽13571882169//96>9/4/4+9128*-/9>719>7<547
			// 地址、电话:西安市高新区锦业一路19号旗远铺抛2号楼二层1020T029-89381952
			String addOrTel = "";
			if (value.indexOf("电话") != -1) {
				//

				if (value.indexOf("//") != -1) {
					addOrTel = value.substring(value.indexOf("//") + 2);
				}

				if (value.indexOf("//") != -1 && value.lastIndexOf("//") != -1) {

					// addOrTel = addOrTel.substring(value.indexOf("//")+2,value.lastIndexOf("//"));
					addOrTel = value.substring(value.indexOf("//") + 2, value.lastIndexOf("//"));
				}
				addOrTel = addOrTel == "" ? value : addOrTel;
				addOrTel = addOrTel
						.substring(addOrTel.indexOf(":") != -1 ? addOrTel.indexOf(":") + 1 : addOrTel.length());

				if (location.getTop() < 700) {
					// 购买方地址或电话
					vat.setPurchaserAddOrTel(addOrTel);
				} else {
					// 销售方地址或电话
					vat.setSellerAddOrTel(addOrTel);
				}
			}

			// 开户行及账号
			String sellerBankDepositOrAccount = "";
			if (value.indexOf("开户行及账号") != -1) {
				int pos = value.indexOf(":");
				if (pos != -1) {
					sellerBankDepositOrAccount = value
							.substring(value.indexOf(":") != -1 ? value.indexOf(":") + 1 : value.length());
				} else {
					sellerBankDepositOrAccount = value
							.substring(value.indexOf("号") != -1 ? value.indexOf("号") + 1 : value.length());
				}
				if (!"".equals(sellerBankDepositOrAccount)) {
					String bankAccount = MyStringUtils.extractNumFromString(value).replace("/", "");
					String baseStr = MyStringUtils.evictNumFromString(sellerBankDepositOrAccount);
					String bankDeposit = baseStr.substring(baseStr.indexOf(":") + 1).replace("/", "");
					if (location.getTop() < 700 && !MyStringUtils.isEmtpy(value)) {
						vat.setPurchaserBankAccount(bankAccount);
						vat.setPurchaserBankDeposit(bankDeposit);
					} else {
						vat.setSellerBankAccount(bankAccount);
						vat.setSellerBankDeposit(bankDeposit);
					}
				}
			}

			// 收款人
			if (value.indexOf("收款人") != -1) {
				// String payee =
				// value.substring(value.indexOf(":")!=-1?value.indexOf(":")+1:value.length());
				// vat.setPayee(payee);

				int pos = value.indexOf(":");
				String payee;
				if (pos != -1) {
					payee = value.substring(value.indexOf(":") != -1 ? value.indexOf(":") + 1 : value.length());
				} else {
					payee = value.substring(value.indexOf("人") != -1 ? value.indexOf("人") + 1 : value.length());
				}
				vat.setReCheck(payee);
			}

			// 复核
			if (value.indexOf("复核") != -1) {
				// String reCheck =
				// value.substring(value.indexOf(":")!=-1?value.indexOf(":")+1:value.length());
				int pos = value.indexOf(":");
				String reCheck;
				if (pos != -1) {
					reCheck = value.substring(value.indexOf(":") != -1 ? value.indexOf(":") + 1 : value.length());
				} else {
					reCheck = value.substring(value.indexOf("人") != -1 ? value.indexOf("人") + 1 : value.length());
				}
				vat.setReCheck(reCheck);
			}

			// 开票人
			if (value.indexOf("开票人") != -1) {
				int pos = value.indexOf(":");
				String drawer;
				if (pos != -1) {
					drawer = value.substring(value.indexOf(":") != -1 ? value.indexOf(":") + 1 : value.length());
				} else {
					drawer = value.substring(value.indexOf("人") != -1 ? value.indexOf("人") + 1 : value.length());
				}
				vat.setDrawer(drawer);
			}
			//

			// 合计金额

			if (value.indexOf("￥") != -1) {
				String result = MyStringUtils.extractFlatOrDoubleFromString(value).replace(",", ".").replace("。", ".");
				if (count == 0) {
					number1 = new BigDecimal(result);
				} else if (count == 1) {
					number2 = new BigDecimal(result);

					if (number1.compareTo(number2) == -1) {
						vat.setAmountSum(number2);
						vat.setTaxSum(number1);
					} else {
						vat.setAmountSum(number1);
						vat.setTaxSum(number2);
					}

				} /*else if (count == 2){
					   //税价合计
					   vat.setAmountAndTaxSum(result);
					}*/
				count++;
			}

			// 税价合计
			if (value.indexOf("小写") != -1) {
				// 需要考虑加税合计的小写部分被盖章遮盖的情况
				String reg = "\\b[\\d.]+\\b";
				// String reg="\\d+(?:\\.\\d+)?%?";
				Pattern pattern = Pattern.compile(reg);
				Matcher m = pattern.matcher(value);
				while (m.find()) {
					String result = m.group(0);
					vat.setAmountAndTaxSum(new BigDecimal(result));
				}
			}

		}

		// 分析摘要

		logger.info("获取的摘要信息：" + JSON.toJSONString(eachSummary));
		
		// XXX 因识别出的摘要信息不具有规律性，无法写出通用性的获取摘要方法，
		//只能等待百度出的模板功能区获取摘要。

		/*   
		// eachSummary
		Collections.reverse(eachSummary);
		for (Iterator<String> it = eachSummary.iterator(); it.hasNext();) {
			String value = it.next();
			if (!value.contains("%")) {
				it.remove();
			} else {
				break;
			}

		}
		Collections.reverse(eachSummary);

		int group = 0;
		for (String str : eachSummary) {
			if (str.contains("%")) {
				group++;
			}
		}

		int groupSize = eachSummary.size() / group;

		logger.info("获取的摘要信息：" + JSON.toJSONString(eachSummary));

		for (int i = 0; i < eachSummary.size(); i++) {
			if (i % groupSize == 0) {

				if (i != 0) {
					summaryLists.add(summaryTempList);
				}
				summaryTempList = new ArrayList<String>();
				summaryTempList.add(eachSummary.get(i));
			} else {
				summaryTempList.add(eachSummary.get(i));
				if (i == eachSummary.size() - 1) {
					summaryLists.add(summaryTempList);
				}
			}
		}

		System.out.println("--------" + JSON.toJSONString(summaryLists));

		Summary summary = new Summary();
		for (int i = 0; i < eachSummary.size(); i++) {
			if (i % groupSize == 0) {
				summary = new Summary();
				summary.setName(eachSummary.get(i));
			}
			if (i % groupSize != 0) {
				// String reg = "\\b[\\d.]+\\b";
				String reg = "\\d+(?:\\.\\d+)?%?";
				Pattern ticketCodeP = Pattern.compile(reg);
				Matcher ticketCodeM = ticketCodeP.matcher(eachSummary.get(i));
				while (ticketCodeM.find()) {
					String value = ticketCodeM.group(0);
					// XXX 需要考虑未识别出.的情况
					int position = value.length() - value.indexOf(".") + 1;
					if (position == 4) {
						String amount = value.substring(0, value.indexOf(".") + 2);
						String taxRate = value.substring(value.indexOf(".") + 2);
						summary.setAmount(amount);
						summary.setTaxRate(taxRate);
					} else if (position == 2) {
						summary.setTaxAmount(value);
					} else if (position == 0) {
						// 未识别出.的情况
						if (value.indexOf("%") != -1) {
							String tempValue = value.replace("%", "");
							// 金额
							String pref = tempValue.substring(0, tempValue.length() - 1);
							// 税率
							String suff = tempValue.substring(tempValue.length() - 1);
							try {
								BigDecimal prefB = new BigDecimal(pref);
								BigDecimal suffB = new BigDecimal(suff);
								BigDecimal multiply = prefB.multiply(suffB);
								double doubleValue = multiply.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								if (doubleValue == Double.parseDouble(taxAmount)) {
									summary.setTaxRate(suff);
									summary.setTaxRate(pref);
								} else {
									// 金额
									pref = tempValue.substring(0, tempValue.length() - 2);
									// 税率
									suff = tempValue.substring(tempValue.length() - 2);
									summary.setTaxRate(suff);
									summary.setTaxRate(pref);
								}

							} catch (Exception e) {
								logger.info("解析摘要详情出错", e);
							}
						}
					}

				}

			}

		}

		String summaryJson = JSON.toJSONString(summaryColl);*/
		
		
		String s = JSON.toJSONString(vat);
		String ss = JSON.toJSONString(eachSummary);
		logger.info("增值税普通发票解析结果：" + s);
		return vat;
	}

	// static String baseStr =
	// "{\"log_id\":8414793703725140578,\"words_result\":[{\"probability\":{\"average\":0.999186,\"min\":0.997802,\"variance\":0},\"words\":\"6100171320\",\"location\":{\"top\":118,\"left\":264,\"width\":691,\"height\":80}},{\"probability\":{\"average\":0.909304,\"min\":0.553771,\"variance\":0.025214},\"words\":\"陕西壇花通发票\",\"location\":{\"top\":57,\"left\":1081,\"width\":719,\"height\":107}},{\"probability\":{\"average\":0.742012,\"min\":0.55866,\"variance\":0.033618},\"words\":\"为、\",\"location\":{\"top\":149,\"left\":1512,\"width\":46,\"height\":43}},{\"probability\":{\"average\":0.990876,\"min\":0.964214,\"variance\":1.67E-4},\"words\":\"№14062420\",\"location\":{\"top\":113,\"left\":1924,\"width\":408,\"height\":82}},{\"probability\":{\"average\":0.998377,\"min\":0.996625,\"variance\":1.0E-6},\"words\":\"6100171320\",\"location\":{\"top\":156,\"left\":2409,\"width\":209,\"height\":40}},{\"probability\":{\"average\":0.997115,\"min\":0.991944,\"variance\":6.0E-6},\"words\":\"14062420\",\"location\":{\"top\":208,\"left\":2379,\"width\":234,\"height\":42}},{\"probability\":{\"average\":0.732855,\"min\":0.517476,\"variance\":0.046388},\"words\":\"要局\",\"location\":{\"top\":231,\"left\":1332,\"width\":174,\"height\":67}},{\"probability\":{\"average\":0.999175,\"min\":0.995936,\"variance\":1.0E-6},\"words\":\"校验码81137175201335742089\",\"location\":{\"top\":275,\"left\":310,\"width\":797,\"height\":70}},{\"probability\":{\"average\":0.999199,\"min\":0.997577,\"variance\":1.0E-6},\"words\":\"开票日期:2017年08月26日\",\"location\":{\"top\":262,\"left\":2001,\"width\":572,\"height\":64}},{\"probability\":{\"average\":0.925432,\"min\":0.868761,\"variance\":0.003212},\"words\":\"A2\",\"location\":{\"top\":263,\"left\":2731,\"width\":103,\"height\":65}},{\"probability\":{\"average\":0.999947,\"min\":0.999947,\"variance\":0},\"words\":\"名\",\"location\":{\"top\":361,\"left\":329,\"width\":46,\"height\":43}},{\"probability\":{\"average\":0.999936,\"min\":0.999936,\"variance\":0},\"words\":\"购\",\"location\":{\"top\":392,\"left\":250,\"width\":39,\"height\":37}},{\"probability\":{\"average\":0.99897,\"min\":0.993711,\"variance\":3.0E-6},\"words\":\"称:西安炳益智能电子科技有限公司\",\"location\":{\"top\":358,\"left\":540,\"width\":675,\"height\":63}},{\"probability\":{\"average\":0.993642,\"min\":0.954499,\"variance\":1.22E-4},\"words\":\"密13+>270<6/2>/933/>/24829297\",\"location\":{\"top\":360,\"left\":1610,\"width\":916,\"height\":80}},{\"probability\":{\"average\":0.828364,\"min\":0.702081,\"variance\":0.015819},\"words\":\"1q买\",\"location\":{\"top\":458,\"left\":97,\"width\":197,\"height\":41}},{\"probability\":{\"average\":0.997101,\"min\":0.971272,\"variance\":3.3E-5},\"words\":\"纳税人识别号:91610131MA6U106Q1M\",\"location\":{\"top\":418,\"left\":331,\"width\":994,\"height\":73}},{\"probability\":{\"average\":0.99562,\"min\":0.888478,\"variance\":3.39E-4},\"words\":\"地址、电话:西安市高新区高新路6号1幢1单元12501室13571882169\",\"location\":{\"top\":479,\"left\":332,\"width\":1232,\"height\":65}},{\"probability\":{\"average\":0.960173,\"min\":0.526299,\"variance\":0.012207},\"words\":\"码/96>9/4/4+9128*-/9>719>7<547\",\"location\":{\"top\":424,\"left\":1600,\"width\":929,\"height\":96}},{\"probability\":{\"average\":0.996811,\"min\":0.972061,\"variance\":3.4E-5},\"words\":\"2>5<85<00<8+9722/934*<\\/+8>\",\"location\":{\"top\":479,\"left\":1695,\"width\":824,\"height\":52}},{\"probability\":{\"average\":0.973591,\"min\":0.498251,\"variance\":0.011474},\"words\":\"4方|开户行及账号:中国民生银行股份有限公司西安高新开发区支行699227091\",\"location\":{\"top\":520,\"left\":7,\"width\":1521,\"height\":104}},{\"probability\":{\"average\":0.983132,\"min\":0.820377,\"variance\":0.002014},\"words\":\"区835384/99/-28<929+--1763012\",\"location\":{\"top\":525,\"left\":1613,\"width\":912,\"height\":72}},{\"probability\":{\"average\":0.972665,\"min\":0.674643,\"variance\":0.008074},\"words\":\"货物或应税劳务、服务名称\",\"location\":{\"top\":614,\"left\":293,\"width\":500,\"height\":46}},{\"probability\":{\"average\":0.94683,\"min\":0.521912,\"variance\":0.022569},\"words\":\"规格型号「单位数量\",\"location\":{\"top\":609,\"left\":911,\"width\":579,\"height\":54}},{\"probability\":{\"average\":0.999911,\"min\":0.999903,\"variance\":0},\"words\":\"单价\",\"location\":{\"top\":615,\"left\":1616,\"width\":119,\"height\":47}},{\"probability\":{\"average\":0.999653,\"min\":0.999309,\"variance\":0},\"words\":\"金额\",\"location\":{\"top\":613,\"left\":1891,\"width\":164,\"height\":49}},{\"probability\":{\"average\":0.99985,\"min\":0.999739,\"variance\":0},\"words\":\"税率\",\"location\":{\"top\":612,\"left\":2169,\"width\":96,\"height\":52}},{\"probability\":{\"average\":0.999734,\"min\":0.999472,\"variance\":0},\"words\":\"税额\",\"location\":{\"top\":617,\"left\":2373,\"width\":164,\"height\":45}},{\"probability\":{\"average\":0.721766,\"min\":0.721766,\"variance\":0},\"words\":\"弟\",\"location\":{\"top\":618,\"left\":2642,\"width\":39,\"height\":37}},{\"probability\":{\"average\":0.998207,\"min\":0.988186,\"variance\":1.4E-5},\"words\":\"电费(物业管理)\",\"location\":{\"top\":681,\"left\":276,\"width\":321,\"height\":46}},{\"probability\":{\"average\":0.931988,\"min\":0.931988,\"variance\":0},\"words\":\"元\",\"location\":{\"top\":684,\"left\":1219,\"width\":47,\"height\":45}},{\"probability\":{\"average\":0.999029,\"min\":0.997215,\"variance\":1.0E-6},\"words\":\"871.7917%\",\"location\":{\"top\":690,\"left\":2021,\"width\":245,\"height\":41}},{\"probability\":{\"average\":0.998528,\"min\":0.996195,\"variance\":2.0E-6},\"words\":\"148.21联\",\"location\":{\"top\":691,\"left\":2489,\"width\":194,\"height\":46}},{\"probability\":{\"average\":0.998245,\"min\":0.9929,\"variance\":5.0E-6},\"words\":\"水费(物业管理)\",\"location\":{\"top\":724,\"left\":273,\"width\":323,\"height\":54}},{\"probability\":{\"average\":0.979624,\"min\":0.979624,\"variance\":0},\"words\":\"元\",\"location\":{\"top\":737,\"left\":1219,\"width\":46,\"height\":43}},{\"probability\":{\"average\":0.997649,\"min\":0.99124,\"variance\":7.0E-6},\"words\":\"108.4011%\",\"location\":{\"top\":739,\"left\":2022,\"width\":244,\"height\":37}},{\"probability\":{\"average\":0.992912,\"min\":0.968695,\"variance\":1.48E-4},\"words\":\"11.92\",\"location\":{\"top\":737,\"left\":2507,\"width\":179,\"height\":39}},{\"probability\":{\"average\":0.999466,\"min\":0.998597,\"variance\":0},\"words\":\"物业管理服务费\",\"location\":{\"top\":770,\"left\":162,\"width\":404,\"height\":83}},{\"probability\":{\"average\":0.966538,\"min\":0.966538,\"variance\":0},\"words\":\"元\",\"location\":{\"top\":779,\"left\":1219,\"width\":47,\"height\":45}},{\"probability\":{\"average\":0.999446,\"min\":0.997458,\"variance\":1.0E-6},\"words\":\"531.646%\",\"location\":{\"top\":785,\"left\":2020,\"width\":234,\"height\":34}},{\"probability\":{\"average\":0.944802,\"min\":0.618121,\"variance\":0.017787},\"words\":\"31.90|发\",\"location\":{\"top\":777,\"left\":2505,\"width\":178,\"height\":44}},{\"probability\":{\"average\":0.999781,\"min\":0.999781,\"variance\":0},\"words\":\"票\",\"location\":{\"top\":816,\"left\":2635,\"width\":49,\"height\":46}},{\"probability\":{\"average\":0.999543,\"min\":0.999543,\"variance\":0},\"words\":\"联\",\"location\":{\"top\":859,\"left\":2635,\"width\":47,\"height\":45}},{\"probability\":{\"average\":0.999996,\"min\":0.999996,\"variance\":0},\"words\":\"购\",\"location\":{\"top\":921,\"left\":2636,\"width\":45,\"height\":41}},{\"probability\":{\"average\":0.999959,\"min\":0.999959,\"variance\":0},\"words\":\"买\",\"location\":{\"top\":963,\"left\":2638,\"width\":42,\"height\":39}},{\"probability\":{\"average\":0.940864,\"min\":0.940864,\"variance\":0},\"words\":\"万\",\"location\":{\"top\":1009,\"left\":2637,\"width\":45,\"height\":41}},{\"probability\":{\"average\":0.813501,\"min\":0.813501,\"variance\":0},\"words\":\"H\",\"location\":{\"top\":1083,\"left\":168,\"width\":34,\"height\":33}},{\"probability\":{\"average\":0.731628,\"min\":0.731628,\"variance\":0},\"words\":\"人\",\"location\":{\"top\":1067,\"left\":399,\"width\":48,\"height\":46}},{\"probability\":{\"average\":0.999745,\"min\":0.999745,\"variance\":0},\"words\":\"记\",\"location\":{\"top\":1038,\"left\":2633,\"width\":53,\"height\":50}},{\"probability\":{\"average\":0.996371,\"min\":0.996371,\"variance\":0},\"words\":\"计\",\"location\":{\"top\":1075,\"left\":638,\"width\":50,\"height\":47}},{\"probability\":{\"average\":0.987837,\"min\":0.907125,\"variance\":9.31E-4},\"words\":\"￥1511.83\",\"location\":{\"top\":1082,\"left\":1907,\"width\":247,\"height\":44}},{\"probability\":{\"average\":0.983387,\"min\":0.869392,\"variance\":0.001633},\"words\":\"￥192.03|账\",\"location\":{\"top\":1085,\"left\":2401,\"width\":284,\"height\":45}},{\"probability\":{\"average\":0.999968,\"min\":0.999968,\"variance\":0},\"words\":\"凭\",\"location\":{\"top\":1130,\"left\":2641,\"width\":40,\"height\":38}},{\"probability\":{\"average\":0.865454,\"min\":0.830456,\"variance\":0.001225},\"words\":\"Wg\",\"location\":{\"top\":1171,\"left\":11,\"width\":204,\"height\":43}},{\"probability\":{\"average\":0.999762,\"min\":0.99948,\"variance\":0},\"words\":\"价税合计(大写)\",\"location\":{\"top\":1155,\"left\":361,\"width\":365,\"height\":48}},{\"probability\":{\"average\":0.999122,\"min\":0.997162,\"variance\":1.0E-6},\"words\":\"壹仟柒佰零叁圆捌角陆分\",\"location\":{\"top\":1159,\"left\":912,\"width\":551,\"height\":46}},{\"probability\":{\"average\":0.98084,\"min\":0.807382,\"variance\":0.002819},\"words\":\"(小写)￥1703。86\",\"location\":{\"top\":1153,\"left\":1953,\"width\":469,\"height\":55}},{\"probability\":{\"average\":0.941191,\"min\":0.860401,\"variance\":0.003472},\"words\":\"证UE\",\"location\":{\"top\":1164,\"left\":2635,\"width\":218,\"height\":51}},{\"probability\":{\"average\":0.901522,\"min\":0.901522,\"variance\":0},\"words\":\"/\",\"location\":{\"top\":1226,\"left\":156,\"width\":229,\"height\":110}},{\"probability\":{\"average\":0.997715,\"min\":0.983907,\"variance\":1.7E-5},\"words\":\"称:陕西诚悦物业管理有限责任公司\",\"location\":{\"top\":1238,\"left\":539,\"width\":676,\"height\":59}},{\"probability\":{\"average\":0.918227,\"min\":0.482445,\"variance\":0.027665},\"words\":\"畚/银座:7-8月\",\"location\":{\"top\":1242,\"left\":1607,\"width\":306,\"height\":81}},{\"probability\":{\"average\":0.677147,\"min\":0.462595,\"variance\":0.05386},\"words\":\"1氵售\",\"location\":{\"top\":1315,\"left\":96,\"width\":199,\"height\":57}},{\"probability\":{\"average\":0.998885,\"min\":0.994824,\"variance\":2.0E-6},\"words\":\"纳税人识别号:916100007412779719\",\"location\":{\"top\":1289,\"left\":328,\"width\":1006,\"height\":64}},{\"probability\":{\"average\":0.999845,\"min\":0.999614,\"variance\":0},\"words\":\"地址、电话\",\"location\":{\"top\":1344,\"left\":326,\"width\":263,\"height\":50}},{\"probability\":{\"average\":0.791702,\"min\":0.791702,\"variance\":0},\"words\":\"7\",\"location\":{\"top\":1302,\"left\":2309,\"width\":74,\"height\":70}},{\"probability\":{\"average\":0.995193,\"min\":0.890175,\"variance\":3.04E-4},\"words\":\":西安市高新区锦业一路19号旗远锦樾2号楼二层10207室029-89381952\",\"location\":{\"top\":1365,\"left\":582,\"width\":981,\"height\":37}},{\"probability\":{\"average\":0.992351,\"min\":0.763151,\"variance\":0.001422},\"words\":\"方|开户行及账号:民生银行西安高新开发区支行1203014210003578\",\"location\":{\"top\":1389,\"left\":245,\"width\":1306,\"height\":82}},{\"probability\":{\"average\":0.796248,\"min\":0.603805,\"variance\":0.037034},\"words\":\"汪王\",\"location\":{\"top\":1388,\"left\":1619,\"width\":42,\"height\":39}},{\"probability\":{\"average\":0.750584,\"min\":0.385571,\"variance\":0.044917},\"words\":\"1910077\",\"location\":{\"top\":1370,\"left\":2012,\"width\":429,\"height\":115}},{\"probability\":{\"average\":0.998706,\"min\":0.994949,\"variance\":5.0E-6},\"words\":\"收款人:\",\"location\":{\"top\":1471,\"left\":265,\"width\":187,\"height\":51}},{\"probability\":{\"average\":0.997972,\"min\":0.996798,\"variance\":2.0E-6},\"words\":\"复核:\",\"location\":{\"top\":1470,\"left\":935,\"width\":132,\"height\":51}},{\"probability\":{\"average\":0.999686,\"min\":0.998457,\"variance\":0},\"words\":\"开票人:贾锦秀\",\"location\":{\"top\":1470,\"left\":1455,\"width\":339,\"height\":54}},{\"probability\":{\"average\":0.997803,\"min\":0.997803,\"variance\":0},\"words\":\"售\",\"location\":{\"top\":1474,\"left\":2036,\"width\":80,\"height\":45}},{\"probability\":{\"average\":0.993736,\"min\":0.993736,\"variance\":0},\"words\":\"6\",\"location\":{\"top\":1518,\"left\":2090,\"width\":30,\"height\":28}},{\"probability\":{\"average\":0.702043,\"min\":0.497594,\"variance\":0.029318},\"words\":\"票专屏章\",\"location\":{\"top\":1445,\"left\":2127,\"width\":329,\"height\":115}},{\"probability\":{\"average\":0.605242,\"min\":0.429593,\"variance\":0.030853},\"words\":\"2o\",\"location\":{\"top\":1525,\"left\":2105,\"width\":52,\"height\":39}},{\"probability\":{\"average\":0.898662,\"min\":0.42682,\"variance\":0.028951},\"words\":\"990112524\",\"location\":{\"top\":1544,\"left\":2156,\"width\":188,\"height\":44}}],\"words_result_num\":76}";
	//
	static String baseStr = "{\"log_id\":7182378519639747897,\"words_result\":[{\"probability\":{\"average\":0.9988,\"min\":0.9961,\"variance\":1.0E-6},\"words\":\"6100171320\",\"location\":{\"top\":34,\"left\":130,\"width\":122,\"height\":23}},{\"probability\":{\"average\":0.911445,\"min\":0.516114,\"variance\":0.031512},\"words\":\"陕西植通发票\",\"location\":{\"top\":21,\"left\":279,\"width\":183,\"height\":29}},{\"probability\":{\"average\":0.988094,\"min\":0.926526,\"variance\":2.82E-4},\"words\":\"№240309996100171320\",\"location\":{\"top\":34,\"left\":497,\"width\":181,\"height\":23}},{\"probability\":{\"average\":0.998081,\"min\":0.995288,\"variance\":2.0E-6},\"words\":\"24030999\",\"location\":{\"top\":54,\"left\":618,\"width\":61,\"height\":16}},{\"probability\":{\"average\":0.997635,\"min\":0.993859,\"variance\":3.0E-6},\"words\":\"校验码4495497273\",\"location\":{\"top\":76,\"left\":135,\"width\":91,\"height\":14}},{\"probability\":{\"average\":0.865365,\"min\":0.618888,\"variance\":0.021659},\"words\":\"G932\",\"location\":{\"top\":80,\"left\":262,\"width\":23,\"height\":9}},{\"probability\":{\"average\":0.955037,\"min\":0.525566,\"variance\":0.013711},\"words\":\"开票日期:2017年11月08日\",\"location\":{\"top\":72,\"left\":516,\"width\":150,\"height\":18}},{\"probability\":{\"average\":0.814061,\"min\":0.509335,\"variance\":0.047158},\"words\":\"EE购\",\"location\":{\"top\":100,\"left\":3,\"width\":79,\"height\":20}},{\"probability\":{\"average\":0.944707,\"min\":0.594633,\"variance\":0.017659},\"words\":\"称:陕西谢谢文化传播有限公司\",\"location\":{\"top\":96,\"left\":141,\"width\":153,\"height\":15}},{\"probability\":{\"average\":0.97686,\"min\":0.749872,\"variance\":0.003317},\"words\":\"纳税人识别号:91610113MA6U72EC61\",\"location\":{\"top\":110,\"left\":86,\"width\":255,\"height\":19}},{\"probability\":{\"average\":0.977396,\"min\":0.658332,\"variance\":0.004896},\"words\":\"854/08293<<5*<9<5*87050\",\"location\":{\"top\":101,\"left\":469,\"width\":187,\"height\":17}},{\"probability\":{\"average\":0.973363,\"min\":0.654027,\"variance\":0.004993},\"words\":\"48089<6<\\/+3*-+-24+79*>1\",\"location\":{\"top\":114,\"left\":439,\"width\":185,\"height\":16}},{\"probability\":{\"average\":0.981638,\"min\":0.981638,\"variance\":0},\"words\":\"5\",\"location\":{\"top\":118,\"left\":641,\"width\":15,\"height\":14}},{\"probability\":{\"average\":0.995712,\"min\":0.987728,\"variance\":1.8E-5},\"words\":\"地址、电话\",\"location\":{\"top\":130,\"left\":87,\"width\":67,\"height\":13}},{\"probability\":{\"average\":0.991442,\"min\":0.954281,\"variance\":2.33E-4},\"words\":\"2087193\",\"location\":{\"top\":130,\"left\":190,\"width\":43,\"height\":12}},{\"probability\":{\"average\":0.899106,\"min\":0.899106,\"variance\":0},\"words\":\"码\",\"location\":{\"top\":124,\"left\":417,\"width\":13,\"height\":11}},{\"probability\":{\"average\":0.975794,\"min\":0.479093,\"variance\":0.011221},\"words\":\"415>6++4/*/*77<82<56>99\",\"location\":{\"top\":127,\"left\":438,\"width\":186,\"height\":15}},{\"probability\":{\"average\":0.978047,\"min\":0.883157,\"variance\":0.001806},\"words\":\"开户行及账号\",\"location\":{\"top\":146,\"left\":87,\"width\":66,\"height\":12}},{\"probability\":{\"average\":0.988873,\"min\":0.921323,\"variance\":3.9E-4},\"words\":\"45049285708>/-4*4-14<1>5*1\",\"location\":{\"top\":140,\"left\":446,\"width\":208,\"height\":16}},{\"probability\":{\"average\":0.992539,\"min\":0.943593,\"variance\":2.32E-4},\"words\":\"货物或应税劳务、服务名称\",\"location\":{\"top\":163,\"left\":77,\"width\":127,\"height\":14}},{\"probability\":{\"average\":0.998282,\"min\":0.993897,\"variance\":4.0E-6},\"words\":\"规格型号单位\",\"location\":{\"top\":163,\"left\":235,\"width\":95,\"height\":14}},{\"probability\":{\"average\":0.997704,\"min\":0.997704,\"variance\":0},\"words\":\"数\",\"location\":{\"top\":165,\"left\":354,\"width\":12,\"height\":10}},{\"probability\":{\"average\":0.996775,\"min\":0.993575,\"variance\":1.0E-5},\"words\":\"单价\",\"location\":{\"top\":164,\"left\":417,\"width\":31,\"height\":11}},{\"probability\":{\"average\":0.955809,\"min\":0.816215,\"variance\":0.004446},\"words\":\"金额税率税额\",\"location\":{\"top\":160,\"left\":488,\"width\":169,\"height\":16}},{\"probability\":{\"average\":0.984224,\"min\":0.968469,\"variance\":2.48E-4},\"words\":\"餐费\",\"location\":{\"top\":176,\"left\":72,\"width\":23,\"height\":11}},{\"probability\":{\"average\":0.978881,\"min\":0.968922,\"variance\":9.9E-5},\"words\":\"22\",\"location\":{\"top\":182,\"left\":409,\"width\":10,\"height\":10}},{\"probability\":{\"average\":0.761292,\"min\":0.761292,\"variance\":0},\"words\":\"C\",\"location\":{\"top\":192,\"left\":47,\"width\":10,\"height\":9}},{\"probability\":{\"average\":0.897923,\"min\":0.656554,\"variance\":0.014958},\"words\":\"222.333%\",\"location\":{\"top\":182,\"left\":520,\"width\":67,\"height\":16}},{\"probability\":{\"average\":0.799278,\"min\":0.618075,\"variance\":0.011579},\"words\":\"667联\",\"location\":{\"top\":182,\"left\":653,\"width\":49,\"height\":21}},{\"probability\":{\"average\":0.967358,\"min\":0.964975,\"variance\":6.0E-6},\"words\":\"区南\",\"location\":{\"top\":197,\"left\":369,\"width\":59,\"height\":21}},{\"probability\":{\"average\":0.997063,\"min\":0.997063,\"variance\":0},\"words\":\"票\",\"location\":{\"top\":215,\"left\":682,\"width\":14,\"height\":13}},{\"probability\":{\"average\":0.558437,\"min\":0.361774,\"variance\":0.058219},\"words\":\"000\",\"location\":{\"top\":230,\"left\":344,\"width\":97,\"height\":20}},{\"probability\":{\"average\":0.768119,\"min\":0.481236,\"variance\":0.046249},\"words\":\"联gk\",\"location\":{\"top\":225,\"left\":682,\"width\":56,\"height\":15}},{\"probability\":{\"average\":0.999738,\"min\":0.999738,\"variance\":0},\"words\":\"购\",\"location\":{\"top\":242,\"left\":683,\"width\":12,\"height\":12}},{\"probability\":{\"average\":0.966418,\"min\":0.956325,\"variance\":5.4E-5},\"words\":\"发票卡章\",\"location\":{\"top\":248,\"left\":372,\"width\":58,\"height\":21}},{\"probability\":{\"average\":0.99982,\"min\":0.99982,\"variance\":0},\"words\":\"买\",\"location\":{\"top\":252,\"left\":682,\"width\":14,\"height\":13}},{\"probability\":{\"average\":0.998292,\"min\":0.998292,\"variance\":0},\"words\":\"合\",\"location\":{\"top\":282,\"left\":103,\"width\":14,\"height\":14}},{\"probability\":{\"average\":0.999819,\"min\":0.999819,\"variance\":0},\"words\":\"计\",\"location\":{\"top\":282,\"left\":162,\"width\":15,\"height\":14}},{\"probability\":{\"average\":0.949452,\"min\":0.735283,\"variance\":0.009194},\"words\":\"￥222.3\",\"location\":{\"top\":285,\"left\":498,\"width\":51,\"height\":11}},{\"probability\":{\"average\":0.993617,\"min\":0.965964,\"variance\":1.37E-4},\"words\":\"价税合计(大写\",\"location\":{\"top\":301,\"left\":92,\"width\":86,\"height\":16}},{\"probability\":{\"average\":0.940782,\"min\":0.663547,\"variance\":0.015391},\"words\":\"贰佰貳拾玖整\",\"location\":{\"top\":300,\"left\":232,\"width\":99,\"height\":15}},{\"probability\":{\"average\":0.993942,\"min\":0.951329,\"variance\":2.03E-4},\"words\":\"(小写)229.00\",\"location\":{\"top\":302,\"left\":505,\"width\":113,\"height\":19}},{\"probability\":{\"average\":0.8975,\"min\":0.8975,\"variance\":0},\"words\":\"销\",\"location\":{\"top\":331,\"left\":65,\"width\":13,\"height\":12}},{\"probability\":{\"average\":0.99987,\"min\":0.99987,\"variance\":0},\"words\":\"名\",\"location\":{\"top\":326,\"left\":85,\"width\":13,\"height\":11}},{\"probability\":{\"average\":0.988793,\"min\":0.889599,\"variance\":7.85E-4},\"words\":\"称:西安市雁塔区陕南人家餐厅\",\"location\":{\"top\":324,\"left\":140,\"width\":152,\"height\":14}},{\"probability\":{\"average\":0.992796,\"min\":0.912788,\"variance\":3.17E-4},\"words\":\"纳税人识别号:61242219790225002301\",\"location\":{\"top\":337,\"left\":85,\"width\":276,\"height\":18}},{\"probability\":{\"average\":0.9673,\"min\":0.828229,\"variance\":0.003892},\"words\":\"告地址、电话\",\"location\":{\"top\":345,\"left\":62,\"width\":93,\"height\":23}},{\"probability\":{\"average\":0.831043,\"min\":0.534492,\"variance\":0.02604},\"words\":\"安事塔区吉祥路与太白南建\",\"location\":{\"top\":352,\"left\":173,\"width\":95,\"height\":9}},{\"probability\":{\"average\":0.763693,\"min\":0.484511,\"variance\":0.050852},\"words\":\"东侧言舞花小区\",\"location\":{\"top\":349,\"left\":283,\"width\":60,\"height\":17}},{\"probability\":{\"average\":0.805868,\"min\":0.420555,\"variance\":0.054068},\"words\":\"号楼中部029-887\",\"location\":{\"top\":353,\"left\":345,\"width\":65,\"height\":13}},{\"probability\":{\"average\":0.98195,\"min\":0.771548,\"variance\":0.002558},\"words\":\"开户行及来号:中国银行四安太白南路支行\",\"location\":{\"top\":366,\"left\":86,\"width\":208,\"height\":19}},{\"probability\":{\"average\":0.873002,\"min\":0.540677,\"variance\":0.025568},\"words\":\"款人:贾利民一\",\"location\":{\"top\":381,\"left\":82,\"width\":81,\"height\":18}},{\"probability\":{\"average\":0.950732,\"min\":0.881152,\"variance\":0.002447},\"words\":\"核:何小莉\",\"location\":{\"top\":381,\"left\":237,\"width\":81,\"height\":16}},{\"probability\":{\"average\":0.931512,\"min\":0.866115,\"variance\":0.004277},\"words\":\"开票\",\"location\":{\"top\":387,\"left\":376,\"width\":25,\"height\":11}},{\"probability\":{\"average\":0.999319,\"min\":0.999319,\"variance\":0},\"words\":\"人\",\"location\":{\"top\":386,\"left\":398,\"width\":20,\"height\":20}},{\"probability\":{\"average\":0.997912,\"min\":0.994656,\"variance\":5.0E-6},\"words\":\":韩伟\",\"location\":{\"top\":385,\"left\":414,\"width\":37,\"height\":15}},{\"probability\":{\"average\":0.99819,\"min\":0.993368,\"variance\":5.0E-6},\"words\":\"销售方:(章\",\"location\":{\"top\":387,\"left\":522,\"width\":73,\"height\":13}}],\"words_result_num\":57}";

}
