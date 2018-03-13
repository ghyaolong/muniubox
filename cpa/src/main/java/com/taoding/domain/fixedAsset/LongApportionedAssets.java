package com.taoding.domain.fixedAsset;


import lombok.Data;
import lombok.EqualsAndHashCode;

import com.taoding.common.annotation.ValidationBean;

/**
 * 长期待摊资产Entity
 * 
 * @author mhb
 * @version 2017-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ValidationBean
public class LongApportionedAssets extends FixedAsset {

	private static final long serialVersionUID = -3497614898296332915L;
	/*
	 * 扩充字段
	 */
	private String[] ids;
}