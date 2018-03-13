package com.taoding.common.pdf;

import java.io.IOException;
import java.util.List;

import com.taoding.domain.voucher.CpaVoucher;

public final class WsPdfDocFactory {
	
	public static WsPdfDoc<DefaultRenderData> createDefaultPdf(String dest, DefaultRenderData data) throws IOException{
		return new DefaultPdf(dest, data);
	}
	
	public static WsPdfDoc<List<CpaVoucher>> createVoucherOnePdf(String dest, List<CpaVoucher> data) throws IOException{
		return new VoucherOnePdf(dest, data);
	}
	
	public static WsPdfDoc<List<CpaVoucher>> createVoucherTwoPdf(String dest, List<CpaVoucher> data) throws IOException{
		return new VoucherTwoPdf(dest, data);
	}
	
	public static WsPdfDoc<List<CpaVoucher>> createVoucherPdf_12(String dest, List<CpaVoucher> data) throws IOException{
		return new VoucherPdf_12(dest, data);
	}
	
	public static WsPdfDoc<List<CpaVoucher>> createVoucherPdf_14(String dest, List<CpaVoucher> data) throws IOException{
		return new VoucherPdf_14(dest, data);
	}
	
	public static WsPdfDoc<List<CpaVoucher>> createVoucherTwoPdf_12(String dest, List<CpaVoucher> data) throws IOException{
		return new VoucherTwoPdf_12(dest, data);
	}
	
	public static WsPdfDoc<List<CpaVoucher>> createVoucherTwoPdf_14(String dest, List<CpaVoucher> data) throws IOException{
		return new VoucherTwoPdf_14(dest, data);
	}
	
	public static WsPdfDoc<List<CpaVoucher>> createVoucherThreePdf(String dest, List<CpaVoucher> data) throws IOException{
		return new VoucherThreePdf(dest, data);
	}

}
