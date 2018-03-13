package com.taoding.domain.fixedAsset;

import java.util.List;

import lombok.Data;

@Data
public class ParamDepreciationDetail {
			
	private String bookId;//账薄ID
	
	private String periodFrom;//账期From
	
	private String periodTo;//账期TO
	
	private Integer assetClass;//资产分类
	
	private String assetType;
	
	private List<String> codes;
}
