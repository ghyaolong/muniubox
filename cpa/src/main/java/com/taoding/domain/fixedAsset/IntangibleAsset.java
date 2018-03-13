package com.taoding.domain.fixedAsset;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.taoding.common.annotation.ValidationBean;

/**
 * 
 * @ClassName: IntangibleAsset
 * @Description: TODO(无形资产)
 * @author lixc
 * @date 2017年12月6日 上午10:27:53
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ValidationBean
public class IntangibleAsset extends FixedAsset {

	private static final long serialVersionUID = -420920091817655601L;
}
