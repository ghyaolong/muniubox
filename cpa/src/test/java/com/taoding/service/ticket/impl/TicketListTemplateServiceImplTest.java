package com.taoding.service.ticket.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.taoding.CPAApplication;
import com.taoding.service.ticket.TicketListTemplateService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CPAApplication.class)
public class TicketListTemplateServiceImplTest {
	
	@Autowired
	private TicketListTemplateService ticketListTemplateService;

	@Test
	public void init() {
		ticketListTemplateService.init("1");
	}
	
	@Test
	public void deleteAll() {
		ticketListTemplateService.deleteAll("50");
	}

}
