package com.taoding.common.pdf;

import java.io.IOException;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.taoding.common.utils.StringUtils;

public class DefaultPdf extends WsPdf<DefaultRenderData> implements WsPdfDoc<DefaultRenderData> {

	private Table table;

	DefaultPdf(String dest, DefaultRenderData data) throws IOException {
		super(dest, data, true);
		float[] colums = new float[data.getHeaderData().length];
		for (int i = 0; i < colums.length; i++) {
			colums[i] = 1;
		}
		table = new Table(colums).setWidthPercent(100).setFontSize(FontSize.FONT_SIZE_12);
	}

	@Override
	public void createPdfDoc() throws IOException {
		document.setFont(FONT);
		if (StringUtils.isNotBlank(data.getTitle())) {
			drawTitle();
		}
		drawContent();
		close();
	}

	@Override
	protected void drawTitle() {
		Paragraph paragraph = new Paragraph(data.getTitle())
				.setBold()
				.setFontSize(FontSize.FONT_SIZE_14)
				.setTextAlignment(TextAlignment.CENTER);
		document.add(paragraph);
	}

	@Override
	protected void drawContent() {
		Cell headerCell = new Cell()
				.setVerticalAlignment(VerticalAlignment.MIDDLE).setBold();
		
		Cell cell = new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE);

		String[] header = data.getHeaderData();
		for (String str : header) {
			table.addHeaderCell(headerCell.clone(false).add(str));
		}
		
		for (String str : data.getData()) {
			table.addCell(cell.clone(false).add(str));
		}
		document.add(table);
	}

	@Override
	protected void drawFoot() {}
	
	@Override
	protected void close() {
		document.close();
	}

}
