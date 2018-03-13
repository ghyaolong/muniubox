package com.taoding.service.ticket.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.taoding.CPAApplication;
import com.taoding.service.ticket.TicketListService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CPAApplication.class)
public class TicketListServiceImplTest {
	
	@Autowired
	private TicketListService ticketListService;

	@Test
	public void init() {
		ticketListService.init("1");
	}
	
	@Test
	public void deleteAll() {
		ticketListService.deleteAll("1");
	}
	
}
