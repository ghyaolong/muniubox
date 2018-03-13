package com.taoding.common.pdf;

import java.util.List;

public class DefaultRenderData {
	

	private String Title;
	
	private String[] headerData;
	
	private List<String> data;

	/**
	 * @param title
	 * @param headerData
	 * @param data
	 */
	public DefaultRenderData(String title, String[] headerData, List<String> data) {
		super();
		Title = title;
		this.headerData = headerData;
		this.data = data;
	}
	
	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String[] getHeaderData() {
		return headerData;
	}

	public void setHeaderData(String[] headerData) {
		this.headerData = headerData;
	}

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}

}
