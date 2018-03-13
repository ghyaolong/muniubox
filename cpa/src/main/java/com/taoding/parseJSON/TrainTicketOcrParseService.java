package com.taoding.parseJSON;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.taoding.common.exception.BRecognitionException;
import com.taoding.common.utils.StringUtils;
import com.taoding.invoiceTemp.TrainTicket;
import com.taoding.ocr.module.WordsResult;
import com.taoding.service.ocr.OCRService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName:  TrainTicketOcrParseService   
 * @Description:解析火车票
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月22日 下午2:20:56   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved.
 */
@Slf4j
public class TrainTicketOcrParseService implements OCRService<TrainTicket> {
	
	public static void main(String[] args) throws BRecognitionException {
		TrainTicketOcrParseService t = new TrainTicketOcrParseService();
		List<WordsResult> parse = ParseUtilsTest.parse("train_01.png");
		TrainTicket ticket = t.parse(parse);
		System.out.println(JSON.toJSON(ticket));
	}

	@Override
	public TrainTicket parse(List<WordsResult> words_result) throws BRecognitionException {
		TrainTicket t = new TrainTicket();
		for (int i = 0; i < words_result.size(); i++) {
			String value = words_result.get(i).getWords().toString();
			if(i==0) {
				if (StringUtils.isNotEmpty(value)) {
					// 第一行是发票代码
					t.setTicketCode(value);
				}				
			}
			
			if(i==2) {
				if (StringUtils.isNotEmpty(value)) {
					// 第三行是发票代码
					t.setFromTo(value);
				}	
			}
			
			//发车时间和座位号 如：2017年11月20日17:50开02车18C号
			String departureReg="\\d{4}年\\d{2}月\\d{2}日";
			Pattern departurePattern = Pattern.compile(departureReg);
			Matcher departureMatcher = departurePattern.matcher(value);
			if(departureMatcher.find()) {
				if(StringUtils.isNotEmpty(value)) {
					String departureTime = value.substring(0, 16)+":00";;
					//XXX 座位号 如：开02车18C号
					//String seat = value.substring(17);
					t.setDepartureTime(departureTime);
				}
				
			}
			
			
			//提取火车票的票价  ^￥\b[\d.]+\b
			String priceReg="^￥\\b[\\d.]+\\b";
			Pattern pattern = Pattern.compile(priceReg);
			Matcher matcher = pattern.matcher(value);
			if(matcher.find()) {
				String price = value.substring(0, 6);
				if(StringUtils.isNotEmpty(price)){
					price = price.replace("￥", "");
					try {
						BigDecimal b = new BigDecimal(price);
						t.setPrice(b);
					} catch (Exception e) {
						log.error("获取火车票票价异常，票价："+price);
					}
				}else {
					price=null;
				}
				// XXX 正则表达式，提取出来的票价，缺失小数点后的数字，后期再处理
				//String aa = matcher.group(0);
				//String price = matcher.group(0).replaceAll("￥", "");
				//t.setPrice(price);
			}
			
			//提取身份证和用户名
			String idAndNameReg="\\d{10}\\*{3}";
			Pattern idPattern = Pattern.compile(idAndNameReg);
			Matcher idMatch = idPattern.matcher(value);
			if(idMatch.find()) {
				String id = value.substring(0,value.lastIndexOf("*")+1);
				String name = value.substring(value.lastIndexOf("*")+5);
				t.setIdenNO(id);
				t.setName(name);
			}
			
			//最后一行，提取车票号码和车票发售点
			if(i==words_result.size()-1) {
				String ticketNoReg = "[\\u4E00-\\u9FA5]+";
				Pattern ticketNoPattern = Pattern.compile(ticketNoReg);
				Matcher ticketM = ticketNoPattern.matcher(value);
				if(ticketM.find()) {
					String ticketBooth = ticketM.group(0);
					String ticketNo = value.substring(0,value.indexOf(ticketBooth));
					t.setIdenNO(ticketNo);
					t.setTicketBooth(ticketBooth);
				}
			}
			
			
		}
		return t;
	}
	
	static String str="{\"log_id\":3826772910834353757,\"words_result\":[{\"probability\":{\"average\":0.993843,\"min\":0.974125,\"variance\":8.5E-5},\"words\":\"L099741\",\"location\":{\"top\":21,\"left\":49,\"width\":172,\"height\":40}},{\"probability\":{\"average\":0.929534,\"min\":0.393776,\"variance\":0.035923},\"words\":\"多检票:二层B16\",\"location\":{\"top\":15,\"left\":341,\"width\":331,\"height\":50}},{\"probability\":{\"average\":0.999069,\"min\":0.993258,\"variance\":3.0E-6},\"words\":\"太原南站D2529西安北站\",\"location\":{\"top\":56,\"left\":49,\"width\":593,\"height\":62}},{\"probability\":{\"average\":0.990189,\"min\":0.974561,\"variance\":6.5E-5},\"words\":\"Taiyuannan\",\"location\":{\"top\":113,\"left\":65,\"width\":157,\"height\":31}},{\"probability\":{\"average\":0.918735,\"min\":0.705298,\"variance\":0.009194},\"words\":\"Xi'anbei\",\"location\":{\"top\":112,\"left\":481,\"width\":115,\"height\":30}},{\"probability\":{\"average\":0.998135,\"min\":0.9692,\"variance\":3.7E-5},\"words\":\"2017年11月20日17:50开02车18D号\",\"location\":{\"top\":145,\"left\":20,\"width\":564,\"height\":35}},{\"probability\":{\"average\":0.99337,\"min\":0.958876,\"variance\":2.0E-4},\"words\":\"￥178.5元\",\"location\":{\"top\":185,\"left\":33,\"width\":137,\"height\":33}},{\"probability\":{\"average\":0.985397,\"min\":0.985397,\"variance\":0},\"words\":\"网\",\"location\":{\"top\":184,\"left\":294,\"width\":34,\"height\":33}},{\"probability\":{\"average\":0.998185,\"min\":0.994748,\"variance\":6.0E-6},\"words\":\"二等座\",\"location\":{\"top\":178,\"left\":487,\"width\":92,\"height\":40}},{\"probability\":{\"average\":0.998653,\"min\":0.993074,\"variance\":5.0E-6},\"words\":\"限乘当日当次车\",\"location\":{\"top\":220,\"left\":26,\"width\":212,\"height\":35}},{\"probability\":{\"average\":0.994436,\"min\":0.959303,\"variance\":1.17E-4},\"words\":\"1403031976***202X王平平\",\"location\":{\"top\":291,\"left\":18,\"width\":428,\"height\":44}},{\"probability\":{\"average\":0.997452,\"min\":0.974193,\"variance\":3.3E-5},\"words\":\"买票请到12306发货请到95306\",\"location\":{\"top\":339,\"left\":103,\"width\":357,\"height\":32}},{\"probability\":{\"average\":0.741656,\"min\":0.359714,\"variance\":0.074515},\"words\":\"CcR\",\"location\":{\"top\":396,\"left\":25,\"width\":33,\"height\":9}},{\"probability\":{\"average\":0.855408,\"min\":0.362091,\"variance\":0.041395},\"words\":\"中国铁路祝您旅途愉快。u4自ca\",\"location\":{\"top\":369,\"left\":146,\"width\":392,\"height\":42}},{\"probability\":{\"average\":0.992704,\"min\":0.969249,\"variance\":5.7E-5},\"words\":\"26468310581121L099741太原南售\",\"location\":{\"top\":411,\"left\":24,\"width\":425,\"height\":34}}],\"words_result_num\":15}";

}
