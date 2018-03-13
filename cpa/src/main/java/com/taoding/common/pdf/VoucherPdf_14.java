package com.taoding.common.pdf;

import java.io.IOException;
import java.util.List;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.taoding.domain.voucher.CpaVoucher;

public class VoucherPdf_14 extends VoucherPdf_12 {

	VoucherPdf_14(String dest, List<CpaVoucher> data) throws IOException {
		super(dest, data);
		pageSize = new PageSize(pageSize.getWidth(), 396);
		HEIGHT_01 = 30;
		HEIGHT_02 = 35;
		RECTANGLE = new Rectangle(
				0, 0, A4.getWidth(), 396).applyMargins(20, 25, 20, 40, false);
		CONTENT_HEADER_CELL = new Cell()
				.setBold()
				.setHeight(HEIGHT_01)
				.setTextAlignment(TextAlignment.CENTER)
				.setVerticalAlignment(VerticalAlignment.MIDDLE);
		CONTENT_FOOT_CELL = new Cell(1, 2)
				.setTextAlignment(TextAlignment.LEFT)
				.setVerticalAlignment(VerticalAlignment.MIDDLE)
				.setBold()
				.setHeight(HEIGHT_01);
	}

}
