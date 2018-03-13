package com.taoding.domain.fixedAsset;

import java.util.Date;

import lombok.Data;

import com.taoding.common.annotation.ValidationBean;


/**
 * 固定资产Entity
 * @author lixc
 * @version 2017-11-27
 */
@Data
@ValidationBean
public class AssetTypeAndAsset {
	
	private int id;
	
	private String assetTypeId;//资产类型
	
	private String assetName;//资产名称
	
	private int count;//次数
	
	private String  accountId;//账薄ID

	private Date lastUpdate;//上次修改时间
	
	private Integer type;//0 固定资产 1 无形资产
}