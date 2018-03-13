package com.taoding.domain.customerTaxItem;



import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

import org.hibernate.validator.constraints.Length;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

@ValidationBean
@Data
public class TaxValueAdded extends DataEntity<TaxValueAdded> {
	
	@NotNull(message="账薄信息不能为空")
	@Length(min=1, max=64, message="账薄长度必须介于 1 和 64 之间")
	private String accountingId;//账薄id
	private BigDecimal exemption;//免税金额
	private boolean surtaxExemptable;//是否是附加税免税政策，0：否，1：是
	private  byte declarantPeriod;// 申报周期：1. 月申报；2. 季申报
	private List<TaxValueAddedDetail> taxValueAddedDetailList;
}
