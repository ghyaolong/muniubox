package com.taoding.domain.accountingbook;

import lombok.Data;
/**
 * 
* @ClassName: TaxpayerProperty 
* @Description: 
* @author lixc 
* @date 2017年11月21日 上午10:48:11 
*
 */
@Data
public class TaxpayerProperty {
private String id;
private String name;
private Integer  isValidated;//0 无效 1 有效 
}
