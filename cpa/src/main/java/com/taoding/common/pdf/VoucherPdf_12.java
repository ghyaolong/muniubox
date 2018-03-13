package com.taoding.common.pdf;

import java.io.IOException;
import java.util.List;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.taoding.common.utils.DateUtils;
import com.taoding.domain.voucher.CpaVoucher;
import com.taoding.domain.voucher.CpaVoucherSubject;

public class VoucherPdf_12 extends WsPdf<List<CpaVoucher>> implements WsPdfDoc<List<CpaVoucher>> {
	
	private int unit;
	private int unitIndex;
	private String unitIndexStr;
	
	
	protected static float HEIGHT_01 = 20;
	protected static float HEIGHT_02 = 30;
	
	protected static float fontSize = FontSize.FONT_SIZE_8;
	protected static float headerFontSize = FontSize.FONT_SIZE_14;
	
	protected static Rectangle RECTANGLE = new Rectangle(
			0, 0, A4.getWidth(), 340).applyMargins(20, 25, 20, 40, false);
	
	
	protected static Cell CONTENT_HEADER_CELL = new Cell()
					.setBold()
					.setHeight(HEIGHT_01)
					.setTextAlignment(TextAlignment.CENTER)
					.setVerticalAlignment(VerticalAlignment.MIDDLE);
	protected static Cell CONTENT_FOOT_CELL = new Cell(1, 2)
					.setTextAlignment(TextAlignment.LEFT)
					.setVerticalAlignment(VerticalAlignment.MIDDLE)
					.setBold()
					.setHeight(HEIGHT_01);
			
	private CpaVoucher voucher;
	private List<CpaVoucherSubject> subjects;

	VoucherPdf_12(String dest, List<CpaVoucher> data) throws IOException {
		super(dest, data);
		pageSize = new PageSize(pageSize.getWidth(), 340);
	}

	@Override
	public void createPdfDoc() throws IOException {
		for (CpaVoucher cpaVoucher : data) {
			voucher = cpaVoucher;
			subjects = cpaVoucher.getSubjectLists();
			unit = (subjects.size() + 4) / 5;
			unitIndex = 1;
			unitIndexStr = "";
			drawContent();
		}
		close();
	}
	
	@Override
	protected void drawTitle() {
		if (unit > 1) {
			unitIndexStr = "("+ unitIndex + "/" + unit + ")";
		}
		
		Table table = new Table(new float[] { 1, 1, 1 })
				.setFont(FONT)
				.setWidthPercent(100);
		Cell cell = new Cell().setBorder(null);
		
		table.addCell(cell.clone(false).add("").setWidthPercent(33));
		table.addCell(cell.clone(false)
				.add("记账凭证")
				.setFontSize(headerFontSize)
				.setTextAlignment(TextAlignment.CENTER)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBold()
				.setWidthPercent(33));
		table.addCell(cell.clone(false)
				.add("附单据数：" + voucher.getTicketCount())
				.setFontSize(fontSize)
				.setTextAlignment(TextAlignment.RIGHT)
				.setVerticalAlignment(VerticalAlignment.BOTTOM));
		canvas.add(table);

		table = new Table(new float[] { 1, 1, 1 })
				.setFont(FONT)
				.setFontSize(fontSize)
				.setWidthPercent(100);
		table.addCell(cell.clone(false)
				.add("核算单位：" + "陕西房居易房地产营销策划有限公司")
				.setWidthPercent(40));
		table.addCell(cell.clone(false)
				.add(DateUtils.formatDate(voucher.getVoucherDate(), "yyyy-MM-dd"))
				.setTextAlignment(TextAlignment.CENTER)
				.setWidthPercent(30));
		table.addCell(cell.clone(false)
				.add("凭证号：记" + voucher.getVoucherNo() + unitIndexStr)
				.setTextAlignment(TextAlignment.RIGHT)
				.setWidthPercent(30));
		canvas.add(table);
	}

	@Override
	protected void drawContent() {
		int count = subjects.size();
		int index = 0;
		
		Cell cell = new Cell()
				.setFontSize(fontSize)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setTextAlignment(TextAlignment.CENTER)
				.setHeight(HEIGHT_02);
		
		while (true) {
			
			newPdfCanvas();
			newCanvas(RECTANGLE);
			
			drawTitle();
			
			Table table = new Table(new float[]{3, 4, 1.5f, 1.5f}).setWidthPercent(100).setFont(FONT);
			
			for (int i = 0; i < 7; i++) {
				if (i == 0) {
					table.addCell(CONTENT_HEADER_CELL.clone(false).add("摘要").setWidthPercent(30))
						.addCell(CONTENT_HEADER_CELL.clone(false).add("科目").setWidthPercent(40))
						.addCell(CONTENT_HEADER_CELL.clone(false).add("借方").setWidthPercent(15))
						.addCell(CONTENT_HEADER_CELL.clone(false).add("贷方").setWidthPercent(15));
					continue;
				}
				
				if (i == 6) {
					table.addCell(CONTENT_FOOT_CELL.clone(false)
							.add("合计：" + voucher.getAccountCapital()));
					table.addCell(new Cell()
							.add(voucher.getAmountDebit().toString())
							.setTextAlignment(TextAlignment.RIGHT)
							.setHeight(HEIGHT_01)
							.setFontSize(fontSize)
							.setVerticalAlignment(VerticalAlignment.MIDDLE));
					table.addCell(new Cell()
							.add(voucher.getAmountCredit().toString())
							.setTextAlignment(TextAlignment.RIGHT)
							.setVerticalAlignment(VerticalAlignment.MIDDLE)
							.setHeight(HEIGHT_01)
							.setFontSize(fontSize));
					
					canvas.add(table);
					drawFoot();
					canvas.close();
					continue;
				}
				
				if (index >= count) {
					table.addCell(cell.clone(false).add(""))
					.addCell(cell.clone(false).add(""))
					.addCell(cell.clone(false).add(""))
					.addCell(cell.clone(false).add(""));
					continue;
				}
				
				CpaVoucherSubject subject = subjects.get(index++);
				String amountDebit = subject.getAmountDebit() == null ? "" : subject.getAmountDebit().toString();
				String amountCredit = subject.getAmountCredit() == null ? "" : subject.getAmountCredit().toString();
				
				table.addCell(cell.clone(false).add(subject.getAbstracts()).setTextAlignment(TextAlignment.LEFT))
					.addCell(cell.clone(false).add(subject.getSubjectNo() + " " + subject.getSubjectName()).setTextAlignment(TextAlignment.LEFT))
					.addCell(cell.clone(false).add(amountDebit).setTextAlignment(TextAlignment.RIGHT))
					.addCell(cell.clone(false).add(amountCredit).setTextAlignment(TextAlignment.RIGHT));
			}
			
			if (index >= count) {
				break;
			}
			unitIndex++;
		}
		
	}

	@Override
	protected void drawFoot() {
		Table table = new Table(new float[] { 1, 1, 1 }).setWidthPercent(80);
		table.setFont(FONT).setFontSize(fontSize);
		
		Cell cell = new Cell().setBorder(null);
		
		table.addCell(cell.clone(false).clone(false).add("记账：" + voucher.getCreateBy()));
		table.addCell(cell.clone(false).add("审核：XXX"));
		table.addCell(cell.clone(false).add("制单：XXX"));
		canvas.add(table);
	}

}
