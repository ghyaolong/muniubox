
package com.taoding.domain.salary;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 客户社保公积金方案Entity
 * @author csl
 * @version 2017-11-28
 */
@Data
@ToString
@ValidationBean
public class CpaSalaryWelfareProject extends DataEntity<CpaSalaryWelfareProject> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=1, max=200, message="方案名称长度必须介于 1 和 200 之间")
	private String projectName;		// 方案名称
	
	@Length(min=0, max=64, message="客户id长度必须介于 0 和 64 之间")
	private String customerId;		// 客户id
	
	private Integer initFlag;   //初始化标记
	
	private List<CpaSalaryWelfareSetting> items = null;
	
	public static final String YILIAO = "1031";  //医疗
	public static final String YANGLAO = "1032";	//养老
	public static final String GONGSHANG = "1033";		//工伤
	public static final String SHENGYU = "1034";	//生育
	public static final String SHIYE = "1035";	 //失业
	public static final String GONGJIJIN = "1036";  	//公积金
	public static final String DABING = "1037";  //大病
}