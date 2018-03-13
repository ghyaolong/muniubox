package com.taoding.service.ticket.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.taoding.CPAApplication;
import com.taoding.common.utils.JsonUtil;
import com.taoding.domain.ticket.Ticket;
import com.taoding.mapper.ticket.TicketDao;
import com.taoding.service.ticket.TicketService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CPAApplication.class)
public class TicketServiceImplTest {
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private TicketDao ticketDao;

	@Test
	public void init() {
		
		try {
			ticketService.init("1");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void selectByPrimaryKey() throws Exception {
		Ticket ticket = ticketDao.selectByPrimaryKey("001", "f0eb9a13bf9d4f3593fb74ed17e14f4b");
		System.out.println(JsonUtil.objectToJson(ticket));
	}
	
	@Test
	public void insert() {
		Ticket ticket = new Ticket();
		ticket.setCustomerId("1001");
		ticket.setBookId("1");
		ticket.setAccountingId("");
		ticket.setSubjectContent("");
		ticket.setType((byte) 1);
		ticket.setTicketKey("6100171320");
		ticket.setTicketCode("14062420");
		ticket.setTicketCheckCode("81137 17520 13357 42089");
		ticket.setPassword("");
		ticket.setPayerName("西安炳益智能电子科技有限公司");
		ticket.setPayerCode("91610131MA6U106Q1M");
		ticket.setPayerAddress("西安市高新区高新路6号1幢1单元12501室 13571882169");
		ticket.setPayerAccount("中国民生银行股份有限公司西安高新开发区支行699227091");
//		ticket.setPayeeName("陕西诚悦物业管理有限贵任公司");
		ticket.setPayeeName("中国建设银行");
		ticket.setPayeeCode("916100007412779719");
		ticket.setPayeeAddress("西安市高新区锦业一路19号旗远铺樾2号楼二层10207室 029-89381952");
		ticket.setPayeeAccount("民生银行西安高新开发区支行1203014210003578");
		ticket.setProvince("陕西省");
		ticket.setCity("");
		ticket.setSummaryWord("电费");
//		ticket.setSummaryWord("飞机票");
		ticket.setSummary("[{\"name\":\"电费（物业管理）\",\"spec\":\"\",\"unit\":\"元\",\"count\":\"\",\"price\":\"\",\"amount\":\"871.79\",\"taxRate\":\"17%\",\"taxAmount\":\"148.21\"},{\"name\":\"水费（物业管理）\",\"spec\":\"\",\"unit\":\"元\",\"count\":\"\",\"price\":\"\",\"amount\":\"108.40\",\"taxRate\":\"11%\",\"taxAmount\":\"11.92\"},{\"name\":\"物业管理服务费\",\"spec\":\"\",\"unit\":\"元\",\"count\":\"\",\"price\":\"\",\"amount\":\"531.64\",\"taxRate\":\"6%\",\"taxAmount\":\"31.90\"}]");
//		ticket.setSummary("[{\"lineNo\":\"001\",\"statementDate\":\"2017-10-13\",\"direction\":0,\"amount\":\"5000.00\"},{\"lineNo\":\"002\",\"statementDate\":\"2017-10-15\",\"direction\":1,\"amount\":\"5000.00\"}]");
		ticket.setPayee("");
		ticket.setRecheck("");
		ticket.setDrawer("贾锦秀");
		ticket.setSeller("");
		ticket.setTax(new BigDecimal("0.17"));
		ticket.setTaxRate(new BigDecimal("192.03"));
		ticket.setTicketDate(new Date());
		ticket.setAmount(new BigDecimal("1511.83"));
		ticket.setTotalAmount(new BigDecimal("1703.86"));
		ticket.setTicketUrl("https://developers.itextpdf.com/sites/default/files/styles/1140_full_width_no_upscale/public/Calculator.-Positive-income.-Plus-000014053057_Small_0.jpg?itok=nNbq7-lv");
		ticket.setRemarks("银座：7-8月");
		ticket.setIsIdentify(Byte.valueOf("1"));
		try {
			ticketService.insertTicket(ticket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void selectListTest() {
		Map<String, Object> condition = new HashMap<>();
		condition.put("bookId", "1");
		condition.put("deleted", 0);
		condition.put("summaryWord", "飞机票");
		condition.put("payerName", "西安");
		List<Ticket> tickets = null;
		try {
			tickets = ticketDao.selectList(condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(tickets);
	}
	
	@Test
	public void selectIdInIds() {
		Map<String, Object> condition = new HashMap<>();
		condition.put("bookId", "1");
		String[] str = new String[] {"2adfd66d14fe4c4dbfc49f6e5e421688", "3dc42bba5e0b4a5b82c8f35321243dc3"};
		List<String> list = Arrays.asList(str);
		condition.put("ids", list);
		List<Ticket> selectList = ticketDao.selectList(condition);
		System.out.println(selectList);
	}
	
	@Test
	public void update() {
		Ticket ticket = ticketService.geTicketById("001", "6f242f5872c144fda317dd4f1ebeb840");
		ticket.setTotalAmount(new BigDecimal(300));
		ticketService.updateTicket(ticket);
	}
	
	@Test
	public void deleteAll() {
		ticketService.deleteAll("50");
	}
	
}
