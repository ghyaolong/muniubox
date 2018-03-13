package com.taoding.common.pdf;

import java.io.IOException;
import java.util.List;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.taoding.domain.voucher.CpaVoucher;

public class VoucherOnePdf extends VoucherPdf_12 {

	VoucherOnePdf(String dest, List<CpaVoucher> data) throws IOException {
		super(dest, data);
		pageSize = new PageSize(PageSize.A4.rotate());
		HEIGHT_01 = 45;
		HEIGHT_02 = 55;
		RECTANGLE = new Rectangle(
				0, 0, A4.getHeight(), A4.getWidth()).applyMargins(40, 40, 40, 60, false);
		CONTENT_HEADER_CELL = new Cell()
				.setBold()
				.setHeight(HEIGHT_01)
				.setTextAlignment(TextAlignment.CENTER)
				.setVerticalAlignment(VerticalAlignment.MIDDLE);
		CONTENT_FOOT_CELL = new Cell(1, 2)
				.setTextAlignment(TextAlignment.LEFT)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBold()
				.setHeight(HEIGHT_01)
				.setFontSize(FontSize.FONT_SIZE_18);
		fontSize = FontSize.FONT_SIZE_12;
		headerFontSize = FontSize.FONT_SIZE_18;
	}
	
}
