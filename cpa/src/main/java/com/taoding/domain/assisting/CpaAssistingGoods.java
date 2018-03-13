
package com.taoding.domain.assisting;

import org.hibernate.validator.constraints.Length;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.ToString;

/**
 * 辅助核算模块存货信息表Entity
 * @author csl
 * @version 2017-11-20
 */
@Data
@ToString
@ValidationBean
public class CpaAssistingGoods extends DataEntity<CpaAssistingGoods> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=0, max=64, message="存货编号长度必须介于 0 和 64 之间")
	private String goodsNo;		// 存货编号
	
	@Length(min=1, max=20, message="存货名称必须介于 1 和 40 之间")
	private String goodsName;		// 存货名称
	
	@Length(min=1, max=20, message="规格长度必须介于 1 和 20 之间")
	private String spec;		// 规格
	
	@Length(min=0, max=20, message="核算单位长度必须介于 0 和 20 之间")
	private String unit;		// 核算单位
	
	private String initMoney;		// 初始金额
	
	@Length(min=0, max=11, message="初始数量长度必须介于 0 和 11 之间")
	private String initCount;		// 初始数量
	
	@Length(min=1, max=64, message="账薄id长度必须介于 1 和 64 之间")
	private String accountId;		// 账薄id
	
	@Length(min=0, max=64, message="存货类型ID长度必须介于 0 和 64 之间")
	private String goodsId;		// 存货类型ID
	
	@Length(min=0, max=64, message="存货类型ID长度必须介于 0 和 64 之间")
	private String goodsSource;   //货物来源
	
	private CpaAssistingGoodsType cpaAssistingGoodsType; //存货类型
	
	//以下数据用来存储数据
	private String name;
	private String no;
}