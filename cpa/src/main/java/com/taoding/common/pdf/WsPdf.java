package com.taoding.common.pdf;

import java.io.IOException;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;

public abstract class WsPdf<D> {
	
	private final String dest;
	private final PdfDocument pdfDocument;
	protected final Document document;
	protected PdfFont FONT;
	protected PageSize pageSize = A4;
	
	protected D data;
	
	protected PdfCanvas pdfCanvas;
	protected Canvas canvas;
	
	protected final static PageSize A4 = PageSize.A4;
	protected static String FONT_LIB = "src/main/resources/font/NotoSansCJKtc-Regular.otf";
	
	protected WsPdf(String dest, D data) throws IOException {
		this(dest, data, false);
	}
	
	protected WsPdf(String dest, D data, boolean isDocument) throws IOException {
		this.dest = dest;
		this.pdfDocument = new PdfDocument(new PdfWriter(dest));
		this.FONT = PdfFontFactory.createFont(FONT_LIB, PdfEncodings.IDENTITY_H, false);
		this.data = data;
		if (isDocument) {
			this.document = new Document(pdfDocument);
		} else {
			this.document = null;
		}
	}
	
	protected void close() {
		pdfDocument.close();
	}
	
	public final String getDest() {
		return dest;
	}
	
	protected final PdfCanvas newPdfCanvas() {
		pdfCanvas = new PdfCanvas(pdfDocument.addNewPage(pageSize));
		return pdfCanvas;
	}
	
	protected final Canvas getCanvas() {
		return canvas;
	}
	
	protected final Canvas newCanvas(Rectangle rectangle) {
		canvas = new Canvas(pdfCanvas, pdfDocument, rectangle);
		return canvas;
	}
	
	protected final void setPageSize(PageSize pageSize) {
		pdfDocument.setDefaultPageSize(pageSize);
	}
	
	protected abstract void drawTitle();
	
	protected abstract void drawContent();
	
	protected abstract void drawFoot();
	
}
