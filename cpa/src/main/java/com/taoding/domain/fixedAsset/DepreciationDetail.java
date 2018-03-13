package com.taoding.domain.fixedAsset;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;

import com.taoding.common.exception.LogicException;

import lombok.Data;

@Data
public class DepreciationDetail {
	//资产分类
	private Integer assetClass;
	//资产分类名称
	private String assetClassName;
	//资产类别
	private AssetType assetType;
	//资产名称
	private String assetName;
	//资产编号
	private String assetCode;
	//资产列表
	private List<FixedAsset> assetList = new ArrayList<FixedAsset>();
	public void addTotal(){
		if(CollectionUtils.isEmpty(assetList)) return ;
		try {
			//本期折旧求和
			BigDecimal sumMonthDepreciation = 
					 assetList.stream().filter(asset -> null != asset && null != asset.getThisMonthDepreciation())
					.map((asset) -> asset.getThisMonthDepreciation())
					.reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
			//处理期末
			FixedAsset lasetFixedAsset = assetList.get(assetList.size()-1);
			FixedAsset fixedAsset = new FixedAsset();
			BeanUtils.copyProperties(fixedAsset,lasetFixedAsset);
			fixedAsset.setShowLable("期末");
			if(null != sumMonthDepreciation){
				fixedAsset.setThisMonthDepreciation(sumMonthDepreciation);
			}
			assetList.add(fixedAsset);
			//清除属性
			fixedAsset.setId(null);
			//处理期初
			fixedAsset = new FixedAsset();
			BeanUtils.copyProperties(fixedAsset,assetList.get(0));
			fixedAsset.setShowLable("期初");
			assetList.add(0, fixedAsset);
		} catch (Exception e) {
			
			throw new LogicException(e.getMessage());
		} 
		
	}
}
