package com.taoding.mq;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taoding.common.exception.BRecognitionException;
import com.taoding.common.utils.DateUtils;
import com.taoding.domain.customerInfo.CustomerInfo;
import com.taoding.domain.ticket.Ticket;
import com.taoding.domain.ticket.vo.Summary;
import com.taoding.invoiceTemp.ElecPaymentVoucher;
import com.taoding.invoiceTemp.ElecReturnedBill;
import com.taoding.invoiceTemp.PaymentReceiptBill;
import com.taoding.invoiceTemp.QuotaInvoice;
import com.taoding.invoiceTemp.SummaryDocument;
import com.taoding.invoiceTemp.TrainTicket;
import com.taoding.invoiceTemp.VATCommonInvoice;
import com.taoding.ocr.baidu.RecognitionTicketService;
import com.taoding.ocr.module.GeneralTicket;
import com.taoding.ocr.module.WordsResult;
import com.taoding.parseJSON.ElecPaymentVoucherOcrParseService;
import com.taoding.parseJSON.ElecReturnedBillOcrParseService;
import com.taoding.parseJSON.PaymentReceiptBillOcrParseService;
import com.taoding.parseJSON.QuotaOcrParseService;
import com.taoding.parseJSON.SummaryDocumentOcrParseService;
import com.taoding.parseJSON.TrainTicketOcrParseService;
import com.taoding.parseJSON.VATCommonInvoiceOcrParseService;
import com.taoding.service.customerInfo.CustomerInfoService;
import com.taoding.service.ocr.OCRParseFactory;
import com.taoding.service.ticket.TicketService;

/**
 * Created by yaochenglong on 2017/11/30.
 */
/**   
 * @ClassName:  Listener   
 * @Description:TODO
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月26日 下午2:13:04   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved. 
 */
@Component
//@Configuration
@RabbitListener(queues = "foo")
public class Listener {

	private static Logger logger = Logger.getLogger(Listener.class);

	public static final String ROOT = "upload-dir";

	private int nThreads = 8;

	private int MAX_QUEUQ_SIZE = 2000;


	/**
	 * 票据相关服务
	 */
	@Autowired
	private TicketService ticketService;

	@Autowired
	private CustomerInfoService customerInfoService;

	/* public ExecutorService executor = Executors.newFixedThreadPool(4); */
	private ThreadPoolExecutor executor = new ThreadPoolExecutor(nThreads, 8, 10, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(MAX_QUEUQ_SIZE), new ThreadPoolExecutor.CallerRunsPolicy());

	long timeSum = 0;

	@Bean
	public Queue fooQueue() {
		return new Queue("foo");
	}

	/**
	 * 此消息客户端处理已经上传的图片
	 * 
	 * @param imgPath
	 */
	@RabbitHandler
	public void process(@Payload Map<String, String> map) {

		if (!CollectionUtils.isEmpty(map)) {

			String imgPath = map.get("imgPath");
			String accountDate = map.get("accountDate");
			String customerId = map.get("customerId");
			String bookId = map.get("bookId");
			logger.info("Listener:" + imgPath);

			// 这里建议使用RabbitMQ，将图片的解析和图片的归档分开处理，减少操作步骤，提高操作效率。
			// 需要考虑如何从队列里加快去数据的速度。因为调用百度的服务，比较耗时，考虑如何在耗时的时间段里，不需要后面的消息继续等待。

			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						//long start = System.currentTimeMillis();
						GeneralTicket generalTicket = recognitionImg(imgPath,accountDate,customerId,bookId);
						
						if (generalTicket != null) {
							List<WordsResult> words_result = generalTicket.getWords_result();
							//words_result.forEach((wordsResult) -> System.out.println(wordsResult.getWords()));
							for(int i=0;i<words_result.size();i++) {
							//for (WordsResult wordsResult : words_result) {
								WordsResult wordsResult = words_result.get(i);
								if (wordsResult.getWords().contains("电子回单")) {
									//电子回单
									ElecReturnedBill elecReturnedBill = OCRParseFactory
											.prase(new ElecReturnedBillOcrParseService(), words_result);
									logger.info("解析电子回单结果:" + JSON.toJSONString(elecReturnedBill));
									saveElecReturnedBill(imgPath, accountDate, customerId, bookId, elecReturnedBill);
									logger.info("保存电子回单结果：" + JSON.toJSONString(elecReturnedBill));
									break;
								} else if (wordsResult.getWords().contains("应税劳务")) {
									//增值税
									VATCommonInvoice vatCommonInvoice = OCRParseFactory
											.prase(new VATCommonInvoiceOcrParseService(), words_result);
									logger.info("解析增值税结果:" + JSON.toJSONString(vatCommonInvoice));
									vatCommonInvoice = saveVATCommonInvoice(imgPath, accountDate, customerId, bookId,
											vatCommonInvoice);
									logger.info("保存增值税结果:" + JSON.toJSONString(vatCommonInvoice));
									break;
								} else if (wordsResult.getWords().contains("票请到12306")) {
									// 火车票
									TrainTicket trainTicket = OCRParseFactory.prase(new TrainTicketOcrParseService(),
											words_result);
									logger.info("解析的火车票结果:" + JSON.toJSONString(trainTicket));
									saveTrainTicket(imgPath, accountDate, customerId, bookId, trainTicket);
									logger.info("保存火车票结果:" + JSON.toJSONString(trainTicket));
									break;
								} else if (wordsResult.getWords().contains("定额发票")) {
									QuotaInvoice quotaInvoice = OCRParseFactory.prase(new QuotaOcrParseService(),
											words_result);
									logger.info("解析的定额发票结果:" + JSON.toJSONString(quotaInvoice));
									saveQuotaInvoice(imgPath, accountDate, customerId, bookId, quotaInvoice);
									break;
								}else if(wordsResult.getWords().contains("收款回单")||wordsResult.getWords().contains("付款回单")){
									//收付回款单(电子回单一种)
									PaymentReceiptBill bill = OCRParseFactory.prase(new PaymentReceiptBillOcrParseService(), words_result);
									logger.info("解析的收付回款单："+JSON.toJSONString(bill));
									savePaymentReceiptBill(imgPath, accountDate, customerId, bookId,bill);
									logger.info("保存的收付回款单："+JSON.toJSONString(bill));
									break;
								}else if(wordsResult.getWords().contains("汇兑凭证")){
									SummaryDocument summaryDocument = OCRParseFactory.prase(new SummaryDocumentOcrParseService(), words_result);
									logger.info("解析的汇兑凭证："+JSON.toJSONString(summaryDocument));
									saveSummaryDocument(imgPath, accountDate, customerId, bookId,summaryDocument);
									logger.info("保存的汇兑凭证："+JSON.toJSONString(summaryDocument));
									break;
								}else if(wordsResult.getWords().contains("电子缴税付款凭证")){
									ElecPaymentVoucher elecPaymentVoucher = OCRParseFactory.prase(new ElecPaymentVoucherOcrParseService(), words_result);
									logger.info("解析的电子缴税付款凭证："+JSON.toJSONString(elecPaymentVoucher));
									saveElecPaymentVoucher(imgPath, accountDate, customerId, bookId,elecPaymentVoucher);
									logger.info("保存的电子缴税付款凭证："+JSON.toJSONString(elecPaymentVoucher));
									break;
								}else {
									if(i==words_result.size()-1) {
										logger.info("未识别客户["+customerId+"],账套["+bookId+"],第["+accountDate+"]期的票据["+imgPath+"]");
										saveUnrecognisable(imgPath,accountDate,customerId, bookId);
									}
								}
							}
						}
						/*long end = System.currentTimeMillis();
						timeSum = timeSum + (end - start);
						logger.info("耗时:" + timeSum / 1000 + "秒");*/
					} catch (Exception e) {
						logger.error("保存[" + imgPath + "]失败", e);
					}
				}
			});
		}

	}
	
	/**
	 * 
	 * 保存 中国工商银行，中国民生银行 电子缴税付款凭证
	 * @param:  imgPath
	 * @param:  accountDate
	 * @param:  customerId
	 * @param:  bookId
	 * @param:  elecPaymentVoucher
	 */
	private void saveElecPaymentVoucher(String imgPath, String accountDate, String customerId, String bookId,
			ElecPaymentVoucher elecPaymentVoucher) {
		Ticket t = new Ticket();
		t.setBookId(bookId);
		t.setCustomerId(customerId);
		t.setTicketUrl(imgPath);
		t.setAccountDate(DateUtils.parseDate(accountDate));
		if (elecPaymentVoucher != null) {
			t.setType(TicketService.BANK_TICKET);
			t.setPayerAccount(elecPaymentVoucher.getPayerAccount());
			t.setPayerName(elecPaymentVoucher.getPayerName());
			t.setPayeeAccount(elecPaymentVoucher.getPayeeAccount());
			t.setPayeeName(elecPaymentVoucher.getPayeeName());
			t.setSummaryWord(elecPaymentVoucher.getSummary());
			
			Summary summary = new Summary();
			summary.setName(elecPaymentVoucher.getSummary());
			summary.setPrice(elecPaymentVoucher.getAmountLowerCase());
			t.setSummary(JSON.toJSONString(summary));
		}
		ticketService.insertTicket(t);
		logger.info("插入ElecPaymentVoucher ticket success");
	}

	/**
	 * 保存汇总凭证
	 *	
	 * @param: @param imgPath
	 * @param: @param accountDate
	 * @param: @param customerId
	 * @param: @param bookId
	 * @param: @param summaryDocument
	 */
	private void saveSummaryDocument(String imgPath, String accountDate, String customerId, String bookId,
			SummaryDocument summaryDocument) {
		Ticket t = new Ticket();
		t.setBookId(bookId);
		t.setCustomerId(customerId);
		t.setTicketUrl(imgPath);
		t.setAccountDate(DateUtils.parseDate(accountDate));
		if (summaryDocument != null) {
			t.setType(TicketService.BANK_TICKET);
			t.setPayerAccount(summaryDocument.getPayerAccount());
			t.setPayerName(summaryDocument.getPayerName());
			t.setPayeeAccount(summaryDocument.getPayeeAccount());
			t.setPayeeName(summaryDocument.getPayeeName());
			t.setSummaryWord(summaryDocument.getSummary());
			
			Summary summary = new Summary();
			summary.setName(summaryDocument.getSummary());
			summary.setPrice(summaryDocument.getAmountLowerCase());
			t.setSummary(JSON.toJSONString(summary));
		}
		ticketService.insertTicket(t);
		logger.info("插入SummaryDocument ticket success");
	}

	/**
	 * 没有匹配中系统中任何一种模板的票据，也就是未识别票据
	 *	
	 * @param: @param imgPath
	 * @param: @param customerId
	 * @param: @param bookId
	 */
	protected void saveUnrecognisable(String imgPath,String accountDate, String customerId, String bookId) {
		Ticket t = new Ticket();
		t.setBookId(bookId);
		t.setCustomerId(customerId);
		t.setTicketUrl(imgPath);
		t.setAccountDate(DateUtils.parseDate(accountDate));
		ticketService.insertTicket(t);
		logger.info("插入Unrecognisable ticket success");
		
	}

	/**
	 *	保存收付回款单
	 * @param:  imgPath
	 * @param:  accountDate
	 * @param:  customerId
	 * @param:  bookId
	 * @param:  bill      
	 */  
	private void savePaymentReceiptBill(String imgPath, String accountDate, String customerId, String bookId,
			PaymentReceiptBill bill) {
		Ticket t = new Ticket();
		// 账簿ID
		t.setBookId(bookId);
		t.setCustomerId(customerId);
		// 定额发票
		t.setType(TicketService.COMMON_TICKET);
		t.setTicketUrl(imgPath);
		t.setAccountDate(DateUtils.parseDate(accountDate));
		
		if (bill != null) {
			t.setTicketCheckCode(bill.getSerialNumber());
			t.setPayerName(bill.getPayeeAccountName());
			t.setPayerAccount(bill.getPayerBank() + " " + bill.getPayerAccount());
			t.setPayeeName(bill.getPayeeAccountName());
			t.setPayeeAccount(bill.getPayeeBank() + " " + bill.getPayeeAccount());
			t.setAmount(bill.getAmountLower());
			t.setSummaryWord(bill.getSummaryKey());
			// XXX 电子回单摘要问题......摘要详情不确定如何存，因格式不统一，这里不清楚是存摘要，还是附言,需要确认
			if(!CollectionUtils.isEmpty(bill.getSummaryList())) {
				t.setSummary(JSON.toJSONString(bill.getSummaryList()));
			}else {
				Summary sum = new Summary();
				sum.setAmount(bill.getAmountLower());
				sum.setName(bill.getRemark());
				t.setSummary(JSON.toJSONString(sum));
			}
			t.setRemarks(bill.getRemark());
			try {
				t.setTicketDate(DateUtils.parseDate(bill.getDealDate(), "YYYY年MM月DD日"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		ticketService.insertTicket(t);
		logger.info("插入PaymentReceiptBill success");
		
	}

	/**
	 * 保存定额发票
	 * @param:  imgPath
	 * @param:  accountDate
	 * @param:  customerId
	 * @param:  bookId
	 * @param:  quotaInvoice
	 */
	private void saveQuotaInvoice(String imgPath, String accountDate, String customerId, String bookId,
			QuotaInvoice quotaInvoice) {
		// 保存定额发票
		Ticket t = new Ticket();
		// 账簿ID
		t.setBookId(bookId);
		t.setCustomerId(customerId);
		// 定额发票
		t.setType(TicketService.COMMON_TICKET);
		t.setTicketUrl(imgPath);
		t.setAccountDate(DateUtils.parseDate(accountDate));
		if (quotaInvoice != null) {
			//
			// 付款方
			CustomerInfo info = customerInfoService.get(customerId);
			if (null != info) {
				t.setPayerAccount(info.getName());
			}
			//摘要关键字
			t.setSummaryWord(quotaInvoice.getSummaryKey());
			// 摘要，
			Summary summary = new Summary();
			summary.setName(quotaInvoice.getSummaryKey());
			summary.setAmount(quotaInvoice.getDenomination());
			// 金额
			t.setSummary(JSON.toJSONString(summary));
		}
		ticketService.insertTicket(t);
		logger.info("插入quotaInvoice success");
		
	}

	/**
	 * 保存火车票
	 * @param: @param trainTicket
	 * @param: @return
	 */
	private void saveTrainTicket(String imgPath, String accountDate, String customerId, String bookId,
			TrainTicket trainTicket) {
		Ticket t = new Ticket();
		// 账簿ID
		t.setBookId(bookId);
		t.setCustomerId(customerId);
		// 银行票据，也是电子回单
		t.setType(TicketService.COMMON_TICKET);
		t.setTicketUrl(imgPath);
		t.setAccountDate(DateUtils.parseDate(accountDate));
		if (trainTicket != null) {
			// 发票日期
			String date = trainTicket.getDepartureTime();
			try {
				t.setTicketDate(DateUtils.parseDate(date, "YYYY-MM-DD hh:mm:ss"));
			} catch (ParseException e) {
				logger.error("保存火车票开票日期失败:[火车票日期：" + date + "]");
				logger.error("保存火车票开票日期失败：" + e);
			}
			//
			// 付款方
			CustomerInfo info = customerInfoService.get(customerId);
			if (null != info) {
				t.setPayerAccount(info.getName());
			}

			// 摘要，
			Summary summary = new Summary();
			summary.setName("火车票");
			summary.setAmount(trainTicket.getPrice());
			// 金额
			t.setSummary(JSON.toJSONString(summary));
		}
		ticketService.insertTicket(t);
		logger.info("插入trainTicket success");
	}

	/**
	 * 
	 * @Title: saveElecReturnedBill   
	 * @Description: 保存电子回单
	 * @param: @param imgPath
	 * @param: @param accountDate
	 * @param: @param customerId
	 * @param: @param bookId
	 * @param: @param elecReturnedBill      
	 * @return: void      
	 * @throws
	 */
	private void saveElecReturnedBill(String imgPath, String accountDate, String customerId, String bookId,
			ElecReturnedBill bill) {
		Ticket t = new Ticket();
		// 账簿ID
		t.setBookId(bookId);
		t.setCustomerId(customerId);
		// 银行票据，也是电子回单
		t.setType(TicketService.BANK_TICKET);
		t.setTicketUrl(imgPath);
		t.setAccountDate(DateUtils.parseDate(accountDate));
		if (bill != null) {
			t.setTicketCheckCode(bill.getBillNo());

			t.setPayerName(bill.getPayeeAccountName());
			t.setPayerAccount(bill.getPayerBank() + " " + bill.getPayerAccount());
			t.setPayeeName(bill.getPayeeAccountName());
			t.setPayeeAccount(bill.getPayeeBank() + " " + bill.getPayeeAccount());
			t.setAmount(bill.getAmountLower());
			t.setSummaryWord(bill.getSummary());
			// XXX 电子回单摘要问题......摘要详情不确定如何存，因格式不统一，这里不清楚是存摘要，还是附言,需要确认
			Summary sum = new Summary();
			sum.setAmount(bill.getAmountLower());
			sum.setName(bill.getRemark());
			t.setSummary(JSON.toJSONString(sum));
			t.setRemarks(bill.getRemark());
			try {
				t.setTicketDate(DateUtils.parseDate(bill.getDealDate(), "YYYY-MM-DD hh:mm:ss"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		ticketService.insertTicket(t);
		logger.info("插入ElecReturnedBill success");
	}

	/**   
	 * @Title: saveVATCommonInvoice   
	 * @Description: 解析图片并保存
	 * @param: imgPath 图片保存路径
	 * @param: accountDate 账期
	 * @param: customerId 客户Id
	 * @param: @return      
	 * @return: VATCommonInvoice      
	 * @throws   
	 */
	private VATCommonInvoice saveVATCommonInvoice(String imgPath, String accountDate, String customerId, String bookId,
			VATCommonInvoice vat) {
		Ticket t = new Ticket();
		// 账簿ID
		t.setBookId(bookId);
		t.setCustomerId(customerId);
		t.setType(TicketService.VAT_COMMON_TICKET);
		t.setTicketUrl(imgPath);
		t.setAccountDate(DateUtils.parseDate(accountDate));
		/*try {
			t.setAccountDate(DateUtils.parseDate(accountDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}*/

		if (vat != null) {
			// 发票号码
			t.setTicketKey(vat.getInvoice_code());
			// 发票代码
			t.setTicketCode(vat.getInvoice_no());
			// 校验码
			t.setTicketCheckCode("81137 17520 13357 42089");
			// 发票密码
			t.setPassword("");
			t.setPayerName(vat.getPurchaser());
			t.setPayerCode(vat.getPurchaserTIN());
			t.setPayerAddress(vat.getPurchaserAddOrTel());
			t.setPayerAccount(vat.getPurchaserBankAccount());
			t.setPayeeName(vat.getSeller());
			t.setPayeeCode(vat.getSellerTIN());
			t.setPayeeAddress(vat.getSellerAddOrTel());
			t.setPayeeAccount(vat.getSellerBankAccount());
			t.setProvince(vat.getProvince());
			t.setCity("");
			t.setSummaryWord("电费");
			t.setSummary(
					"[{\"name\":\"电费（物业管理）\",\"spec\":\"\",\"unit\":\"元\",\"count\":\"\",\"price\":\"\",\"amount\":\"871.79\",\"taxRate\":\"17%\",\"taxAmount\":\"148.21\"},{\"name\":\"水费（物业管理）\",\"spec\":\"\",\"unit\":\"元\",\"count\":\"\",\"price\":\"\",\"amount\":\"108.40\",\"taxRate\":\"11%\",\"taxAmount\":\"11.92\"},{\"name\":\"物业管理服务费\",\"spec\":\"\",\"unit\":\"元\",\"count\":\"\",\"price\":\"\",\"amount\":\"531.64\",\"taxRate\":\"6%\",\"taxAmount\":\"31.90\"}]");
			t.setPayee(vat.getPayee());
			t.setRecheck(vat.getReCheck());
			t.setDrawer(vat.getDrawer());
			t.setSeller("");

			t.setTax(vat.getTaxSum());

			// t.setTaxRate(new BigDecimal("192.03"));
			try {
				logger.info("开票日期:" + DateUtils.parseDate(vat.getInvoice_date(), "yyyy年MM月dd日"));
				t.setTicketDate(DateUtils.parseDate(vat.getInvoice_date(), "yyyy年MM月dd日"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			t.setAmount(vat.getAmountSum());
			t.setTotalAmount(vat.getAmountAndTaxSum());
			t.setRemarks("");
		}
		ticketService.insertTicket(t);
		logger.info("插入VATCommonInvoice success");
		return vat;
	}

	/**   
	 * 调用百度图片识别服务，并将返回的json数据封装成{@link com.taoding.ocr.module.GeneralTicket}对象
	 * @param: imgPath 图片路径
	 * @return: GeneralTicket对象          
	 */
	private  GeneralTicket recognitionImg(String imgPath,String accountDate,String customerId,String bookId) {
		GeneralTicket ticket = null;
		try {
			String recognJson = RecognitionTicketService.generalRecognition(imgPath);
			ticket = JSON.parseObject(recognJson, GeneralTicket.class);
		} catch (BRecognitionException e) {
			try {
				String recognJson = RecognitionTicketService.generalRecognition(imgPath);
				ticket = JSON.parseObject(recognJson, GeneralTicket.class);
			} catch (BRecognitionException e2) {
				logger.error("第一次尝试解析[" + imgPath + "]失败", e);
				// 如果调用百度服务失败，则是未识别票据
				saveUnrecognisable(imgPath,accountDate,customerId, bookId);
			}
			logger.error("解析[" + imgPath + "]失败", e);
		}
		return ticket;
	}

}
