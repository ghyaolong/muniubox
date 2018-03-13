package com.taoding.common.pdf;

import java.io.IOException;
import java.util.List;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceCmyk;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.taoding.common.utils.DateUtils;
import com.taoding.domain.voucher.CpaVoucher;
import com.taoding.domain.voucher.CpaVoucherSubject;

public class VoucherThreePdf extends WsPdf<List<CpaVoucher>> implements WsPdfDoc<List<CpaVoucher>> {

	private final static float HEIGHT = 20;
	
	private final static float UNIT_HEIHGT = (A4.getHeight() - 60) / 3;
			
	private final static Rectangle RECTANGLE = new Rectangle(
			0, A4.getHeight() - 30 - UNIT_HEIHGT, A4.getWidth(),UNIT_HEIHGT).applyMargins(10, 25, 10, 40, false);
	private final static Color COLOR = new DeviceCmyk(0.1f, 0.1f, 0.1f, 0.1f);
	
	private int unit;
	private int unitIndex;
	private int totalUnitIndex;
	private String unitIndexStr = "";
	
	private int transformIndex = 0;
	
	private CpaVoucher voucher;
	private List<CpaVoucherSubject> subjects;
	
	VoucherThreePdf(String dest, List<CpaVoucher> data) throws IOException {
		super(dest, data);
	}
	
	@Override
	public void createPdfDoc() throws IOException {
		for (CpaVoucher cpaVoucher : data) {
			if (totalUnitIndex % 3 == 0) {
				pdfCanvas = newPdfCanvas();
				drawBindingLine();
			}
			
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
				.setFontSize(FontSize.FONT_SIZE_10)
				.setTextAlignment(TextAlignment.CENTER)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBold()
				.setWidthPercent(33));
		table.addCell(cell.clone(false)
				.add("附单据数：" + voucher.getTicketCount())
				.setFontSize(FontSize.FONT_SIZE_10)
				.setTextAlignment(TextAlignment.RIGHT)
				.setVerticalAlignment(VerticalAlignment.BOTTOM));
		canvas.add(table);

		table = new Table(new float[] { 1, 1, 1 })
				.setFont(FONT)
				.setFontSize(FontSize.FONT_SIZE_10)
				.setWidthPercent(100);
		table.addCell(cell.clone(false)
				.add("核算单位：" + "陕西房居易房地产营销策划有限公司")
				.setWidthPercent(50));
		table.addCell(cell.clone(false)
				.add(DateUtils.formatDate(voucher.getVoucherDate(), "yyyy-MM-dd"))
				.setTextAlignment(TextAlignment.CENTER)
				.setWidthPercent(25));
		table.addCell(cell.clone(false)
				.add("凭证号：记" + voucher.getVoucherNo() + unitIndexStr)
				.setTextAlignment(TextAlignment.RIGHT)
				.setWidthPercent(25));
		canvas.add(table);
	}

	@Override
	protected void drawContent() {
		int count = subjects.size();
		int index = 0;
		
		Cell cell = new Cell()
				.setFontSize(FontSize.FONT_SIZE_10)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setTextAlignment(TextAlignment.CENTER)
				.setHeight(HEIGHT);
		
		while (true) {
			
			if (unitIndex != 1 && totalUnitIndex % 3 == 0) {
				pdfCanvas = newPdfCanvas();
			}
			
			newCanvas(RECTANGLE);
			if (transformIndex != 0 && transformIndex <= 2) {
				AffineTransform transform = AffineTransform.getTranslateInstance(0, - UNIT_HEIHGT);
				pdfCanvas.concatMatrix(transform);
			}
			
			drawTitle();
			
			Table table = new Table(new float[]{3, 4, 1.5f, 1.5f}).setWidthPercent(100).setFont(FONT);
			
			for (int i = 0; i < 7; i++) {
				if (i == 0) {
					Cell bold = new Cell()
							.setBold()
							.setHeight(HEIGHT).setBackgroundColor(COLOR)
							.setTextAlignment(TextAlignment.CENTER)
							.setVerticalAlignment(VerticalAlignment.MIDDLE);
					table.addCell(bold.clone(false).add("摘要").setWidthPercent(30))
						.addCell(bold.clone(false).add("科目").setWidthPercent(40))
						.addCell(bold.clone(false).add("借方").setWidthPercent(15))
						.addCell(bold.clone(false).add("贷方").setWidthPercent(15));
					continue;
				}
				
				if (i == 6) {
					table.addCell(new Cell(1, 2)
							.add("合计：" + voucher.getAccountCapital())
							.setTextAlignment(TextAlignment.LEFT)
							.setVerticalAlignment(VerticalAlignment.MIDDLE)
							.setBold()
							.setHeight(HEIGHT).setBold().setBackgroundColor(COLOR));
					table.addCell(new Cell()
							.add(voucher.getAmountDebit().toString())
							.setTextAlignment(TextAlignment.RIGHT)
							.setHeight(HEIGHT)
							.setFontSize(FontSize.FONT_SIZE_10)
							.setVerticalAlignment(VerticalAlignment.MIDDLE)
							.setBackgroundColor(COLOR));
					table.addCell(new Cell()
							.add(voucher.getAmountCredit().toString())
							.setTextAlignment(TextAlignment.RIGHT)
							.setVerticalAlignment(VerticalAlignment.MIDDLE)
							.setHeight(HEIGHT)
							.setFontSize(FontSize.FONT_SIZE_10)
							.setBackgroundColor(COLOR));
					
					canvas.add(table);
					drawFoot();
					canvas.close();
					totalUnitIndex++;
					if (++transformIndex == 3) {
						transformIndex = 0;
					}
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
		table.setFont(FONT).setFontSize(FontSize.FONT_SIZE_10);
		
		Cell cell = new Cell().setBorder(null);
		
		table.addCell(cell.clone(false).clone(false).add("记账：" + voucher.getCreateBy()));
		table.addCell(cell.clone(false).add("审核：XXX"));
		table.addCell(cell.clone(false).add("制单：XXX"));
		canvas.add(table);
	} 

	protected void drawBindingLine() throws IOException {
		float point = 0;
		while (point <= pageSize.getWidth()) {
			pdfCanvas.beginText().setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD), 8);
			pdfCanvas.moveText(point, UNIT_HEIHGT + 30).showText(".");
			pdfCanvas.endText();
			pdfCanvas.beginText().setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD), 8);
			pdfCanvas.moveText(point, UNIT_HEIHGT * 2 + 30).showText(".");
			pdfCanvas.endText();
			point += 2.5f;
		}
		
		pdfCanvas.circle(pageSize.getWidth() / 2, pageSize.getHeight() - 25, 10).setLineWidth(0.5f);
		pdfCanvas.circle(pageSize.getWidth() / 4, pageSize.getHeight() - 25, 10).setLineWidth(0.5f);
		pdfCanvas.circle(pageSize.getWidth() - pageSize.getWidth() / 4, pageSize.getHeight() - 25, 10).setLineWidth(0.5f);
		pdfCanvas.stroke();
	}
	
}
