package com.taoding.invoiceTemp;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 
 * @ClassName:  TrainTicket   
 * @Description:火车票
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月22日 下午2:19:39   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved.
 */
@Data
public class TrainTicket {
	//车票代码
	private String ticketCode;
	
	//从哪站到哪站的信息，如 蔡家坡站T56西安站
	private String fromTo;
	//车票号码
	private String ticketNo;
	//票价
	private BigDecimal price;
	//身份证
	private String idenNO;
	//用户名
	private String name;
	//发车时间
	private String departureTime;
	//售票点
	private String ticketBooth;
}
