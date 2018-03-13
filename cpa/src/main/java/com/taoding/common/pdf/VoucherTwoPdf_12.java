package com.taoding.common.pdf;

import java.io.IOException;
import java.util.List;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.taoding.domain.voucher.CpaVoucher;

public class VoucherTwoPdf_12 extends VoucherTwoPdf {
	
	private final static float BINDING_HEIHGT = 340;
	
	VoucherTwoPdf_12(String dest, List<CpaVoucher> data) throws IOException {
		super(dest, data);
		HEIGHT_01 = 20;
		HEIGHT_02 = 30;
		CONTENT_HEADER_CELL = new Cell()
				.setBold()
				.setHeight(HEIGHT_01).setBackgroundColor(COLOR)
				.setTextAlignment(TextAlignment.CENTER)
				.setVerticalAlignment(VerticalAlignment.MIDDLE);
		CONTENT_FOOT_CELL = new Cell(1, 2)
				.setTextAlignment(TextAlignment.LEFT)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBold()
				.setHeight(HEIGHT_01).setBackgroundColor(COLOR);
	}

	@Override
	protected void drawBindingLine() throws IOException {
		float point = 0;
		while (point <= pageSize.getWidth()) {
			pdfCanvas.beginText().setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD), 8);
			pdfCanvas.moveText(point, pageSize.getHeight() / 2).showText(".");
			pdfCanvas.endText();
			pdfCanvas.beginText().setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD), 8);
			pdfCanvas.moveText(point, BINDING_HEIHGT).showText(".");
			pdfCanvas.endText();
			pdfCanvas.beginText().setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD), 8);
			pdfCanvas.moveText(point, pageSize.getHeight() - BINDING_HEIHGT).showText(".");
			pdfCanvas.endText();
			point += 2.5f;
		}
		pdfCanvas.moveTo(5, BINDING_HEIHGT + 5);
		pdfCanvas.lineTo(0, BINDING_HEIHGT);
		pdfCanvas.moveTo(5, BINDING_HEIHGT - 5);
		pdfCanvas.lineTo(0, BINDING_HEIHGT);
		
		pdfCanvas.moveTo(pageSize.getWidth() - 5, pageSize.getHeight() - BINDING_HEIHGT + 5);
		pdfCanvas.lineTo(pageSize.getWidth(), pageSize.getHeight() - BINDING_HEIHGT);
		pdfCanvas.moveTo(pageSize.getWidth() - 5, pageSize.getHeight() - BINDING_HEIHGT - 5);
		pdfCanvas.lineTo(pageSize.getWidth(), pageSize.getHeight() - BINDING_HEIHGT);
		
		pdfCanvas.circle(30, pageSize.getHeight() - 30, 10).setLineWidth(0.5f);
		pdfCanvas.circle(pageSize.getWidth() - 30, 30, 10).setLineWidth(0.5f);
		
		pdfCanvas.stroke();
	}
	
}
