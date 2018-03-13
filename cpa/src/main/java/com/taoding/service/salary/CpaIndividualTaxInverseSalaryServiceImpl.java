
package com.taoding.service.salary;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.salary.CpaIndividualTaxInverseSalary;
import com.taoding.mapper.salary.CpaIndividualTaxInverseSalaryDao;

/**
 * 取整规则Service
 * @author csl
 * @version 2017-11-24
 */
@Service
@Transactional
public class CpaIndividualTaxInverseSalaryServiceImpl extends DefaultCurdServiceImpl<CpaIndividualTaxInverseSalaryDao, CpaIndividualTaxInverseSalary> 
	implements CpaIndividualTaxInverseSalaryService{
	
	/**
	 * 工资计算公式 
	 * 工资=(个税 + 扣除数)/税率 + 五险一金 + 起征点
	 * 个税计算公式
	 * 个税 = (工资 - 五险一金 - 起征点) * 税率 - 扣除数 
	 * 税率表
	 * 级数	应纳税所得额(含税)				应纳税所得额(不含税)				税率(%)			速算扣除数
	 * 1		不超过1500元的					不超过1455元的						3				0
	 * 2		超过1500元至4,500元的部分		超过1455元至4,155元的部分			10				105
	 * 3		超过4,500元至9,000元的部分		超过4,155元至7,755元的部分			20				555
	 * 4		超过9,000元至35,000元的部分		超过7,755元至27,255元的部分			25				1,005
	 * 5		超过35,000元至55,000元的部分	超过27,255元至41,255元的部分		30				2,755
	 * 6		超过55,000元至80,000元的部分	超过41,255元至57,505元的部分		35				5,505
	 * 7		超过80,000元的部分				超过57,505的部分					45				13,505
	 * 算个税
	 * 以某员工月薪10000元为例，缴纳五险一金的金额为1000元，应缴纳个税为：
	 * 个税 = (10000 - 1000 - 3500) * 税率 - 扣除数
	 * =  5500 * 20% - 555
	 * =  1100 - 555
	 * =  545（元）
	 * 反算工资：
	 * 已知个税（545元），五险一金（1000元）和起征点（3500）算工资
	 * 工资 = (个税 + 扣除数)/税率 + 五险一金 + 起征点
	 * = (545 + 扣除数)/税率 + 1000 + 3500
	 * = (545 + 扣除数)/税率 + 4500
	 * 将税率表中的相应税率和扣除数逐条代入上述公式：
	 * 级数			税率			扣除数										工资
	 * 1			3			0			(545 + 0)/0.03 + 4500		=	22666.66
	 * 2			10			105			(545 + 105)/0.1 + 4500		=	11000
	 * 3			20			555			(545 + 555)/0.2 + 4500		=	10000
	 * 4			25			1005		(545 + 1005)/0.25 + 4500	=	10700
	 * 5			30			2755		(545 + 2755)/0.30 + 4500	=	15500
	 * 6			35			5505		(545 + 5505)/0.35 + 4500	=	21785.714
	 * 7			45			13505		(545 + 13505)/0.45 + 4500	=	35722.2222
	 * 																	取最小值10000，就是实际工资
	 * @param individualTaxInverseSalary
	 * @return
	 */
	public Object inverseSalary(CpaIndividualTaxInverseSalary individualTaxInverseSalary){
		//获取传递的个人所得税
		return true;
	}
}