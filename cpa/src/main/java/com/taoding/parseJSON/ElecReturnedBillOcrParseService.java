package com.taoding.parseJSON;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.taoding.invoiceTemp.ElecReturnedBill;
import com.taoding.ocr.module.WordsResult;
import com.taoding.service.ocr.OCRService;


/**
 * @ClassName:  ElecReturnedBillOcrParseService   
 * @Description:解析电子回单
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月20日 上午9:33:35   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved.
 */
public class ElecReturnedBillOcrParseService implements OCRService<ElecReturnedBill>{
	
	private static Logger logger = Logger.getLogger(ElecReturnedBillOcrParseService.class);

	@Override 
	public ElecReturnedBill parse(List<WordsResult> wordsResults) {
		/*GeneralTicket entity = JSON.parseObject(jsonStr, GeneralTicket.class);
		List<WordsResult> wordsResults = entity.getWords_result();*/
		List<WordsResult> list = new ArrayList<>(wordsResults);
		ElecReturnedBill bill = new ElecReturnedBill();
		
		//账号计数器
		int accountCount=0;
		//开户行计数器
		int bankCount = 0;
		for (int i = 0; i < wordsResults.size(); i++) {
			WordsResult wordsResult = list.get(i);
			//Location location = wordsResult.getLocation();
			// 获取抬头
			String value = wordsResult.getWords();
			
			//页码
			if(value.indexOf("页码")!=-1) {
				String result = value.substring(value.indexOf("页码")+3);
				bill.setPageNum(result);
			}
			
			if(value.indexOf("电子回单")!=-1) {
				bill.setTitle(value);
			}
			
			if(value.indexOf("回单编号")!=-1) {
				value = value.substring(value.indexOf(":")+1);
				bill.setBillNo(value);
			}
			
			if(value.indexOf("账号")!=-1) {
				if(accountCount==0) {
					//付款方
					bill.setPayerAccount(list.get(i+1).getWords().toString());
				}else {
					//收款方
					bill.setPayeeAccount(list.get(i+1).getWords().toString());
				}
				accountCount++;
			}
			
			if(value.indexOf("付款方")!=-1) {
				
				if("付款方户名".equals(value)) {
					bill.setPayerAccountName(list.get(i+1).getWords().toString());
				}else {
					bill.setPayerAccountName(value.substring(value.indexOf("付款方户名")+5));
				}
			}
			
			if(value.indexOf("收款方")!=-1&& value.indexOf("回单") == -1) {
				
				if("收款方户名".equals(value)) {
					bill.setPayeeAccountName(list.get(i+1).getWords().toString());
				}else {
					bill.setPayeeAccountName(value.substring(value.indexOf("收款方户名")+5));
				}
			}
			
			if(value.indexOf("开户行")!=-1) {
				if(bankCount==0) {
					//付款方
					if("开户行".equals(value)) {
						if(list.get(i+3).getWords().toString().indexOf("开户行")!=-1) {
							bill.setPayerBank(list.get(i+1).getWords().toString()+list.get(i+2).getWords().toString());
						}else {
							bill.setPayerBank(list.get(i+1).getWords().toString());							
						}
						bankCount++;
					}else {
						if(list.get(i+1).getWords().indexOf("开户行")!=-1) {
							String result = value.substring(value.indexOf("开户行")+3);
							bill.setPayerBank(result);
						}else if(list.get(i+2).getWords().indexOf("开户行")!=-1) {
							String result = value.substring(value.indexOf("开户行")+3)+list.get(i+1).getWords().toString();
							bill.setPayerBank(result);
						}
						
						//bill.setPayeeAccount(value.substring(value.indexOf("开户行")));							
						bankCount++;
					}
				}else {
					//收款方
					if("开户行".equals(value)) {
						if(list.get(i+2).getWords().indexOf("金额")!=-1) {
							bill.setPayeeBank(list.get(i+1).getWords());
						}else if(list.get(i+3).getWords().indexOf("金额")!=-1) {
							bill.setPayeeBank(list.get(i+1).getWords()+list.get(i+2).getWords());
						}else {
							bill.setPayerBank(list.get(i+1).getWords().toString());
						}
					}else {
						if(list.get(i+2).getWords().indexOf("金额")!=-1) {
							bill.setPayeeBank(value.substring(value.indexOf("开户行")+3)+list.get(i+1).getWords());
						}else {
							bill.setPayerBank(value.substring(value.indexOf("开户行")+3));
						}
					}
				}
			}
			
			//金额大写
			if(value.indexOf("大写")!=-1) {
				String upperCase = list.get(i+1).getWords();
				bill.setAmountUpper(upperCase);
			}
			//金额小写
			if(value.indexOf("小写")!=-1) {
				String upperCase = list.get(i+1).getWords();
				if(StringUtils.isNotEmpty(upperCase)) {
					try {
						BigDecimal b = new BigDecimal(upperCase);
						bill.setAmountLower(b);
					} catch (Exception e) {
						logger.error("获取金额异常，金额："+upperCase);
					}
					
				}
				
			}
			//币种
			if(value.indexOf("币种")!=-1) {
				String upperCase = list.get(i+1).getWords();
				bill.setCurrency(upperCase);
			}
			//摘要 XXX 有可能识别不到
			if(value.indexOf("摘要")!=-1) {
				String upperCase = list.get(i+1).getWords();
				bill.setSummary(upperCase);
			}
			//交易时间
			if(value.indexOf("交易时间")!=-1) {
				String dealTime = list.get(i+1).getWords();
				StringBuffer sb = new StringBuffer(dealTime);
				sb.insert(10, " ");
				sb.insert(13, ":");
				sb.insert(16, ":");
				bill.setDealDate(sb.toString());
			}
			//会计时间
			if(value.indexOf("会计时间")!=-1) {
				String upperCase = list.get(i+1).getWords();
				StringBuffer sb = new StringBuffer(upperCase);
				sb.insert(4, "-");
				sb.insert(7, "-");
				bill.setAccountDate(sb.toString());
			}
			//附言
			if(value.indexOf("附言")!=-1) {
				if(list.get(i+2).getWords().indexOf("回单")!=-1) {
					String remark = list.get(i+1).getWords();
					bill.setRemark(remark);					
				}
			}
			
			if(value.indexOf("打印日期")!=-1) {
				String result = value.substring(value.indexOf("打印日期")+4);
				if(result.charAt(result.length()-1)!='-') {
					StringBuffer sb = new StringBuffer(result);
					sb.insert(result.length()-2, '-');
					bill.setPrintDate(sb.toString());
				}
			}
			
		}
		
		String s = JSON.toJSONString(bill);
		logger.info("解析的电子回执单结果："+s);
		return bill;
	}
	
	/*public static void main(String[] args) {
		ElecReturnedBillOcrParseService e = new ElecReturnedBillOcrParseService();
		e.parse(str);
	}
	
	static final String str ="{\"log_id\":4847717163776757152,\"words_result\":[{\"probability\":{\"average\":0.992355,\"min\":0.970053,\"variance\":1.19E-4},\"words\":\"页码,2/6\",\"location\":{\"top\":11,\"left\":566,\"width\":66,\"height\":16}},{\"probability\":{\"average\":0.998827,\"min\":0.997644,\"variance\":1.0E-6},\"words\":\"电子回单\",\"location\":{\"top\":81,\"left\":302,\"width\":88,\"height\":27}},{\"probability\":{\"average\":0.994507,\"min\":0.961589,\"variance\":8.8E-5},\"words\":\"回单编号:32100374342475381333\",\"location\":{\"top\":120,\"left\":232,\"width\":178,\"height\":18}},{\"probability\":{\"average\":0.98551,\"min\":0.928498,\"variance\":8.13E-4},\"words\":\"第1次打印\",\"location\":{\"top\":120,\"left\":572,\"width\":52,\"height\":18}},{\"probability\":{\"average\":0.999073,\"min\":0.998149,\"variance\":1.0E-6},\"words\":\"账号\",\"location\":{\"top\":146,\"left\":119,\"width\":25,\"height\":15}},{\"probability\":{\"average\":0.995566,\"min\":0.968106,\"variance\":6.2E-5},\"words\":\"26130501040008831\",\"location\":{\"top\":146,\"left\":193,\"width\":110,\"height\":15}},{\"probability\":{\"average\":0.999732,\"min\":0.999475,\"variance\":0},\"words\":\"账号\",\"location\":{\"top\":147,\"left\":387,\"width\":25,\"height\":14}},{\"probability\":{\"average\":0.997664,\"min\":0.976673,\"variance\":2.6E-5},\"words\":\"6228480218362989673\",\"location\":{\"top\":146,\"left\":466,\"width\":123,\"height\":15}},{\"probability\":{\"average\":0.994723,\"min\":0.931949,\"variance\":2.34E-4},\"words\":\"付款方户名西安创业天下网络科技有限公司\",\"location\":{\"top\":170,\"left\":72,\"width\":249,\"height\":27}},{\"probability\":{\"average\":0.976263,\"min\":0.925117,\"variance\":8.93E-4},\"words\":\"收款方户名\",\"location\":{\"top\":170,\"left\":343,\"width\":71,\"height\":25}},{\"probability\":{\"average\":0.809684,\"min\":0.499296,\"variance\":0.048959},\"words\":\"李军军\",\"location\":{\"top\":172,\"left\":509,\"width\":33,\"height\":14}},{\"probability\":{\"average\":0.988695,\"min\":0.968587,\"variance\":2.03E-4},\"words\":\"开户行\",\"location\":{\"top\":204,\"left\":121,\"width\":35,\"height\":14}},{\"probability\":{\"average\":0.972796,\"min\":0.657168,\"variance\":0.007164},\"words\":\"中国农业银行股份有限公司西安老\",\"location\":{\"top\":198,\"left\":161,\"width\":158,\"height\":11}},{\"probability\":{\"average\":0.986462,\"min\":0.9463,\"variance\":5.38E-4},\"words\":\"庙分理处\",\"location\":{\"top\":213,\"left\":223,\"width\":46,\"height\":13}},{\"probability\":{\"average\":0.985011,\"min\":0.756932,\"variance\":0.002838},\"words\":\"开户行国农业银行股份有限公司西安北大街中\",\"location\":{\"top\":195,\"left\":385,\"width\":238,\"height\":27}},{\"probability\":{\"average\":0.997606,\"min\":0.992223,\"variance\":1.0E-5},\"words\":\"段分理处\",\"location\":{\"top\":212,\"left\":503,\"width\":47,\"height\":14}},{\"probability\":{\"average\":0.998992,\"min\":0.995925,\"variance\":2.0E-6},\"words\":\"金额大写)\",\"location\":{\"top\":236,\"left\":84,\"width\":57,\"height\":17}},{\"probability\":{\"average\":0.935861,\"min\":0.443652,\"variance\":0.024602},\"words\":\"贰仟伍佰零陆元捌角捌分\",\"location\":{\"top\":236,\"left\":188,\"width\":120,\"height\":16}},{\"probability\":{\"average\":0.997449,\"min\":0.990172,\"variance\":1.2E-5},\"words\":\"金额(小写)\",\"location\":{\"top\":237,\"left\":347,\"width\":65,\"height\":14}},{\"probability\":{\"average\":0.983552,\"min\":0.899984,\"variance\":0.001173},\"words\":\"2506.88\",\"location\":{\"top\":236,\"left\":505,\"width\":42,\"height\":15}},{\"probability\":{\"average\":0.992447,\"min\":0.989238,\"variance\":1.0E-5},\"words\":\"币种\",\"location\":{\"top\":263,\"left\":81,\"width\":23,\"height\":11}},{\"probability\":{\"average\":0.999129,\"min\":0.998545,\"variance\":0},\"words\":\"人民币\",\"location\":{\"top\":263,\"left\":208,\"width\":33,\"height\":12}},{\"probability\":{\"average\":0.998435,\"min\":0.996996,\"variance\":2.0E-6},\"words\":\"摘要\",\"location\":{\"top\":263,\"left\":347,\"width\":24,\"height\":12}},{\"probability\":{\"average\":0.999838,\"min\":0.999452,\"variance\":0},\"words\":\"转账取款\",\"location\":{\"top\":263,\"left\":483,\"width\":47,\"height\":13}},{\"probability\":{\"average\":0.999934,\"min\":0.999848,\"variance\":0},\"words\":\"交易时间\",\"location\":{\"top\":287,\"left\":69,\"width\":47,\"height\":14}},{\"probability\":{\"average\":0.972672,\"min\":0.768719,\"variance\":0.004758},\"words\":\"2017-10-09143413\",\"location\":{\"top\":286,\"left\":173,\"width\":106,\"height\":15}},{\"probability\":{\"average\":0.999169,\"min\":0.997175,\"variance\":1.0E-6},\"words\":\"会计日期\",\"location\":{\"top\":286,\"left\":335,\"width\":46,\"height\":15}},{\"probability\":{\"average\":0.995425,\"min\":0.988875,\"variance\":9.0E-6},\"words\":\"20171009\",\"location\":{\"top\":287,\"left\":479,\"width\":52,\"height\":12}},{\"probability\":{\"average\":0.682367,\"min\":0.393979,\"variance\":0.080509},\"words\":\"行股份有\",\"location\":{\"top\":315,\"left\":444,\"width\":52,\"height\":17}},{\"probability\":{\"average\":0.9958,\"min\":0.992563,\"variance\":1.0E-5},\"words\":\"附言\",\"location\":{\"top\":337,\"left\":79,\"width\":27,\"height\":15}},{\"probability\":{\"average\":0.997201,\"min\":0.979581,\"variance\":2.7E-5},\"words\":\"付工资,扣8月和9月养老金\",\"location\":{\"top\":336,\"left\":179,\"width\":139,\"height\":17}},{\"probability\":{\"average\":0.995497,\"min\":0.979152,\"variance\":6.7E-5},\"words\":\"回单专用章\",\"location\":{\"top\":350,\"left\":441,\"width\":63,\"height\":17}},{\"probability\":{\"average\":0.998705,\"min\":0.970404,\"variance\":2.4E-5},\"words\":\"回单仅表明您的账户有金融性交易,不能作为到账凭证,不可作为收款方发货依据\",\"location\":{\"top\":386,\"left\":163,\"width\":376,\"height\":17}},{\"probability\":{\"average\":0.962304,\"min\":0.539355,\"variance\":0.013349},\"words\":\"打印日期:2017-10-31\",\"location\":{\"top\":413,\"left\":486,\"width\":107,\"height\":17}}],\"words_result_num\":34}";
	static final String str2="{\"log_id\":5254498436618483554,\"words_result\":[{\"probability\":{\"average\":0.997137,\"min\":0.988623,\"variance\":1.7E-5},\"words\":\"页码,6/6\",\"location\":{\"top\":8,\"left\":577,\"width\":66,\"height\":16}},{\"probability\":{\"average\":0.995947,\"min\":0.98881,\"variance\":1.9E-5},\"words\":\"电子回单\",\"location\":{\"top\":80,\"left\":307,\"width\":90,\"height\":26}},{\"probability\":{\"average\":0.992885,\"min\":0.935712,\"variance\":1.92E-4},\"words\":\"回单编号:32250245430078574449\",\"location\":{\"top\":120,\"left\":238,\"width\":182,\"height\":15}},{\"probability\":{\"average\":0.967098,\"min\":0.836025,\"variance\":0.004295},\"words\":\"第1次打印\",\"location\":{\"top\":121,\"left\":583,\"width\":51,\"height\":15}},{\"probability\":{\"average\":0.999851,\"min\":0.999703,\"variance\":0},\"words\":\"账号\",\"location\":{\"top\":148,\"left\":125,\"width\":23,\"height\":11}},{\"probability\":{\"average\":0.992154,\"min\":0.954103,\"variance\":1.78E-4},\"words\":\"26130501040008831\",\"location\":{\"top\":146,\"left\":222,\"width\":110,\"height\":14}},{\"probability\":{\"average\":0.999871,\"min\":0.999749,\"variance\":0},\"words\":\"账号\",\"location\":{\"top\":148,\"left\":448,\"width\":23,\"height\":11}},{\"probability\":{\"average\":0.995734,\"min\":0.990581,\"variance\":1.4E-5},\"words\":\"560\",\"location\":{\"top\":146,\"left\":560,\"width\":21,\"height\":15}},{\"probability\":{\"average\":0.996383,\"min\":0.984771,\"variance\":3.4E-5},\"words\":\"付款方户名\",\"location\":{\"top\":171,\"left\":76,\"width\":73,\"height\":14}},{\"probability\":{\"average\":0.973773,\"min\":0.709066,\"variance\":0.005516},\"words\":\"西安创业天下网络科技有限公司\",\"location\":{\"top\":170,\"left\":200,\"width\":153,\"height\":18}},{\"probability\":{\"average\":0.969006,\"min\":0.859106,\"variance\":0.003029},\"words\":\"收款方户名\",\"location\":{\"top\":172,\"left\":399,\"width\":74,\"height\":14}},{\"probability\":{\"average\":0.998018,\"min\":0.992256,\"variance\":8.0E-6},\"words\":\"莲湖区支库\",\"location\":{\"top\":171,\"left\":539,\"width\":55,\"height\":15}},{\"probability\":{\"average\":0.971945,\"min\":0.403601,\"variance\":0.014699},\"words\":\"开户行中国农业银行股份有限公司西安老关庙分理处\",\"location\":{\"top\":197,\"left\":125,\"width\":257,\"height\":14}},{\"probability\":{\"average\":0.874669,\"min\":0.626272,\"variance\":0.030851},\"words\":\"开户行\",\"location\":{\"top\":197,\"left\":448,\"width\":35,\"height\":15}},{\"probability\":{\"average\":0.998236,\"min\":0.994522,\"variance\":3.0E-6},\"words\":\"2601220000\",\"location\":{\"top\":198,\"left\":513,\"width\":107,\"height\":11}},{\"probability\":{\"average\":0.998826,\"min\":0.994807,\"variance\":4.0E-6},\"words\":\"金额大写)\",\"location\":{\"top\":222,\"left\":85,\"width\":60,\"height\":15}},{\"probability\":{\"average\":0.923639,\"min\":0.625965,\"variance\":0.022155},\"words\":\"伍元球角整\",\"location\":{\"top\":222,\"left\":248,\"width\":54,\"height\":14}},{\"probability\":{\"average\":0.993007,\"min\":0.974456,\"variance\":7.3E-5},\"words\":\"金额(小写)\",\"location\":{\"top\":222,\"left\":409,\"width\":67,\"height\":15}},{\"probability\":{\"average\":0.972822,\"min\":0.915672,\"variance\":0.001107},\"words\":\"5.40\",\"location\":{\"top\":221,\"left\":555,\"width\":24,\"height\":16}},{\"probability\":{\"average\":0.99687,\"min\":0.996306,\"variance\":0},\"words\":\"币种\",\"location\":{\"top\":248,\"left\":82,\"width\":25,\"height\":13}},{\"probability\":{\"average\":0.999264,\"min\":0.998932,\"variance\":0},\"words\":\"人民币\",\"location\":{\"top\":249,\"left\":238,\"width\":34,\"height\":11}},{\"probability\":{\"average\":0.992131,\"min\":0.978859,\"variance\":7.4E-5},\"words\":\"转账取款\",\"location\":{\"top\":248,\"left\":519,\"width\":43,\"height\":14}},{\"probability\":{\"average\":0.999935,\"min\":0.999789,\"variance\":0},\"words\":\"交易时间\",\"location\":{\"top\":273,\"left\":71,\"width\":45,\"height\":14}},{\"probability\":{\"average\":0.97301,\"min\":0.792317,\"variance\":0.004169},\"words\":\"2017-10-24120515\",\"location\":{\"top\":272,\"left\":202,\"width\":106,\"height\":15}},{\"probability\":{\"average\":0.998855,\"min\":0.995759,\"variance\":3.0E-6},\"words\":\"会计日期\",\"location\":{\"top\":273,\"left\":395,\"width\":46,\"height\":14}},{\"probability\":{\"average\":0.996987,\"min\":0.993616,\"variance\":4.0E-6},\"words\":\"20171024\",\"location\":{\"top\":274,\"left\":514,\"width\":54,\"height\":11}},{\"probability\":{\"average\":0.754695,\"min\":0.510948,\"variance\":0.059413},\"words\":\"附言\",\"location\":{\"top\":324,\"left\":81,\"width\":24,\"height\":15}},{\"probability\":{\"average\":0.992292,\"min\":0.963712,\"variance\":1.3E-4},\"words\":\"实时扣税请求(3001)\",\"location\":{\"top\":325,\"left\":214,\"width\":129,\"height\":14}},{\"probability\":{\"average\":0.973422,\"min\":0.872047,\"variance\":0.002572},\"words\":\"回单专用章\",\"location\":{\"top\":338,\"left\":476,\"width\":64,\"height\":17}},{\"probability\":{\"average\":0.917326,\"min\":0.917326,\"variance\":0},\"words\":\"器\",\"location\":{\"top\":300,\"left\":565,\"width\":70,\"height\":65}},{\"probability\":{\"average\":0.998824,\"min\":0.985704,\"variance\":9.0E-6},\"words\":\"本回单仅表明您的账户有金融性交易,不能作为到账凭证,不可作为收款方发货依据\",\"location\":{\"top\":374,\"left\":154,\"width\":395,\"height\":16}},{\"probability\":{\"average\":0.968164,\"min\":0.562344,\"variance\":0.01267},\"words\":\"打印日期:2017-1031\",\"location\":{\"top\":404,\"left\":495,\"width\":109,\"height\":13}}],\"words_result_num\":32}";
*/
}
