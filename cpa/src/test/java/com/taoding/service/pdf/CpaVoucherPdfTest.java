package com.taoding.service.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.taoding.CPAApplication;
import com.taoding.common.pdf.DefaultRenderData;
import com.taoding.common.pdf.WsPdfDoc;
import com.taoding.common.pdf.WsPdfDocFactory;
import com.taoding.domain.voucher.CpaVoucher;
import com.taoding.service.voucher.CpaVoucherService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CPAApplication.class)
public class CpaVoucherPdfTest {

	@Autowired
	private CpaVoucherService cpaVoucherService;

	private final static String DEST = "src/main/resources/pdf/default.pdf";
	private final static String DEST_ONE = "src/main/resources/pdf/voucherOne.pdf";
	private final static String DEST_TWO = "src/main/resources/pdf/voucherTwo.pdf";
	private final static String DEST_12 = "src/main/resources/pdf/voucher_12.pdf";
	private final static String DEST_14 = "src/main/resources/pdf/voucher_14.pdf";
	private final static String DEST_TWO_12 = "src/main/resources/pdf/voucherTwo_12.pdf";
	private final static String DEST_TWO_14 = "src/main/resources/pdf/voucherTwo_14.pdf";
	private final static String DEST_THREE = "src/main/resources/pdf/voucherThree.pdf";

	@Test
	public void createDefaultPdf() throws IOException {
		String title = "测试默认PDF";
		String[] headerData = { "测试1列", "测试2列", "测试3列", "测试4列", "测试5列", "测试6列", "测试7列" };
		List<String> content = new ArrayList<>();
		for (int i = 0; i < 140 * 6; i++) {
			content.add("测试数据");
		}
		DefaultRenderData data = new DefaultRenderData(title, headerData, content);
		data.setTitle("哈哈哈哈，我是标题");
		
		WsPdfDoc<DefaultRenderData> doc = WsPdfDocFactory.createDefaultPdf(DEST, data);
		doc.createPdfDoc();
	}

	@Test
	public void createVoucherOnePdf() throws IOException {
		Map<String, String> condition = new HashMap<>();
		condition.put("bookId", "1");
		condition.put("voucherPeriod", "2017-10-01");
		List<CpaVoucher> data = cpaVoucherService.findAllListByPeriod(condition);
		WsPdfDoc<List<CpaVoucher>> pdf = WsPdfDocFactory.createVoucherOnePdf(DEST_ONE, data);

		pdf.createPdfDoc();
	}

	@Test
	public void createVoucherTwoPdf() throws IOException {
		// CpaVoucher voucher = cpaVoucherService.findById("1",
		// "be6926aedb5a4d85b7cfe973aead9a6e");
		// List<CpaVoucher> data = new ArrayList<>();
		// data.add(voucher);
		// voucher = cpaVoucherService.findById("1",
		// "345c6e9b34ad43eca67d063d2df35452");
		// data.add(voucher);
		Map<String, String> condition = new HashMap<>();
		condition.put("bookId", "1");
		condition.put("voucherPeriod", "2017-10-01");
		List<CpaVoucher> data = cpaVoucherService.findAllListByPeriod(condition);
		WsPdfDoc<List<CpaVoucher>> pdf = WsPdfDocFactory.createVoucherTwoPdf(DEST_TWO, data);

		pdf.createPdfDoc();
	}

	@Test
	public void createVoucherPdf_12() throws IOException {
		// CpaVoucher voucher = cpaVoucherService.findById("1",
		// "be6926aedb5a4d85b7cfe973aead9a6e");
		// List<CpaVoucher> data = new ArrayList<>();
		// data.add(voucher);
		// voucher = cpaVoucherService.findById("1",
		// "345c6e9b34ad43eca67d063d2df35452");
		// data.add(voucher);
		Map<String, String> condition = new HashMap<>();
		condition.put("bookId", "1");
		condition.put("voucherPeriod", "2017-10-01");
		List<CpaVoucher> data = cpaVoucherService.findAllListByPeriod(condition);
		WsPdfDoc<List<CpaVoucher>> pdf = WsPdfDocFactory.createVoucherPdf_12(DEST_12, data);

		pdf.createPdfDoc();
	}

	@Test
	public void createVoucherPdf_14() throws IOException {
		// CpaVoucher voucher = cpaVoucherService.findById("1",
		// "be6926aedb5a4d85b7cfe973aead9a6e");
		// List<CpaVoucher> data = new ArrayList<>();
		// data.add(voucher);
		// voucher = cpaVoucherService.findById("1",
		// "345c6e9b34ad43eca67d063d2df35452");
		// data.add(voucher);
		Map<String, String> condition = new HashMap<>();
		condition.put("bookId", "1");
		condition.put("voucherPeriod", "2017-10-01");
		List<CpaVoucher> data = cpaVoucherService.findAllListByPeriod(condition);
		WsPdfDoc<List<CpaVoucher>> pdf = WsPdfDocFactory.createVoucherPdf_14(DEST_14, data);

		pdf.createPdfDoc();
	}

	@Test
	public void createVoucherTwoPdf_12() throws IOException {
		// CpaVoucher voucher = cpaVoucherService.findById("1",
		// "be6926aedb5a4d85b7cfe973aead9a6e");
		// List<CpaVoucher> data = new ArrayList<>();
		// data.add(voucher);
		// voucher = cpaVoucherService.findById("1",
		// "345c6e9b34ad43eca67d063d2df35452");
		// data.add(voucher);
		Map<String, String> condition = new HashMap<>();
		condition.put("bookId", "1");
		condition.put("voucherPeriod", "2017-10-01");
		List<CpaVoucher> data = cpaVoucherService.findAllListByPeriod(condition);
		WsPdfDoc<List<CpaVoucher>> pdf = WsPdfDocFactory.createVoucherTwoPdf_12(DEST_TWO_12, data);

		pdf.createPdfDoc();
	}

	@Test
	public void createVoucherTwoPdf_14() throws IOException {
		// CpaVoucher voucher = cpaVoucherService.findById("1",
		// "be6926aedb5a4d85b7cfe973aead9a6e");
		// List<CpaVoucher> data = new ArrayList<>();
		// data.add(voucher);
		// voucher = cpaVoucherService.findById("1",
		// "345c6e9b34ad43eca67d063d2df35452");
		// data.add(voucher);
		Map<String, String> condition = new HashMap<>();
		condition.put("bookId", "1");
		condition.put("voucherPeriod", "2017-10-01");
		List<CpaVoucher> data = cpaVoucherService.findAllListByPeriod(condition);
		WsPdfDoc<List<CpaVoucher>> pdf = WsPdfDocFactory.createVoucherTwoPdf_14(DEST_TWO_14, data);

		pdf.createPdfDoc();
	}

	@Test
	public void createVoucherThreePdf() throws IOException {
		// CpaVoucher voucher = cpaVoucherService.findById("1",
		// "be6926aedb5a4d85b7cfe973aead9a6e");
		// List<CpaVoucher> data = new ArrayList<>();
		// data.add(voucher);
		// voucher = cpaVoucherService.findById("1",
		// "345c6e9b34ad43eca67d063d2df35452");
		// data.add(voucher);
		Map<String, String> condition = new HashMap<>();
		condition.put("bookId", "1");
		condition.put("voucherPeriod", "2017-10-01");
		List<CpaVoucher> data = cpaVoucherService.findAllListByPeriod(condition);
		WsPdfDoc<List<CpaVoucher>> pdf = WsPdfDocFactory.createVoucherThreePdf(DEST_THREE, data);

		pdf.createPdfDoc();
	}

}
