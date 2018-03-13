package com.taoding.domain.fixedAsset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;


/**
 * 固定资产Entity
 * @author lixc
 * @version 2017-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ValidationBean
public class AssetType extends DataEntity<AssetType> {
	
	
	private static final long serialVersionUID = -9088780116186381885L;
	
	public static final String[] ASSET_NAME = {"固定资产","无形资产","长期待摊"}; 
	//1为固定资产，2为无形资产 3 待摊资产
	public  static final Integer FIXED_ASSET_TYPE = 1;
	public  static final Integer INTANGIBLE_ASSET_TYPE = 2; 
	public  static final Integer APPORTIONED_ASSET_TYPE = 3;
	
	@Length(min=1, max=64, message="所属账簿长度必须介于 1 和 64 之间")
	private String accountId;		// 所属账簿
	
	private String no;		// 类别编号
	
	@Length(min=1, max=100, message="类别名称长度必须介于 1 和 100 之间")
	private String name;		// 类别名称
	
	@NotNull(message="资产类型不能为空！")
	private Integer type;		// 区分是固定资产还是无形资产，1为固定资产，2为无形资产
	
//	@NotEmpty(message="使用期限不能为空!")
	private Integer lifecycle;		// 使用期限(月)
	
	private String residualRate;	// 残值率
	
	public static final List<Map<String, Object>> assetArray = Lists.newArrayList();
	
	public static List<Map<String, Object>> getAssetArray() {
		
		if(CollectionUtils.isEmpty(assetArray)){
			Map<String , Object> tempMap= new HashMap<String, Object>();
			
			tempMap.put("key", AssetType.FIXED_ASSET_TYPE);
			tempMap.put("lable", ASSET_NAME[AssetType.FIXED_ASSET_TYPE-1]);
			assetArray.add(tempMap);
			
			tempMap= new HashMap<String, Object>();
			tempMap.put("key", AssetType.INTANGIBLE_ASSET_TYPE);
			tempMap.put("lable", ASSET_NAME[AssetType.INTANGIBLE_ASSET_TYPE-1]);
			assetArray.add(tempMap);
			
			tempMap= new HashMap<String, Object>();
			tempMap.put("key", AssetType.APPORTIONED_ASSET_TYPE);
			tempMap.put("lable", ASSET_NAME[AssetType.APPORTIONED_ASSET_TYPE-1]);
			
			assetArray.add(tempMap);	
		}
		
		return assetArray;
	}
}