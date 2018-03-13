package com.taoding.service.subject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.NumberToCN;
import com.taoding.common.utils.UserUtils;
import com.taoding.domain.subject.CertificateVO;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.domain.user.User;
import com.taoding.domain.voucher.CpaVoucher;
import com.taoding.domain.voucher.CpaVoucherSubject;
import com.taoding.mapper.subject.CpaVoucherImportDao;

@Service
public class CpaVoucherImportServiceImpl extends DefaultCurdServiceImpl<CpaVoucherImportDao, CpaVoucher> implements CpaVoucherImportService {
	
	/**
	 * 导入凭证
	 * 进行批量插入凭证
	 * 批量插入 凭证科目
	 * @param list VO
	 * @param customerId 客户 id
	 * @param bookId 账薄 id
	 */
	@Override
	@Transactional
	public Object savaCertificateData(List<CertificateVO> list, String customerId, String bookId){
		// 凭证科目
		List<CpaVoucherSubject> cvsList = new ArrayList<CpaVoucherSubject>();
		// 凭证表
		List<CpaVoucher> cvList = new ArrayList<CpaVoucher>();
		
		User user = new User();
		user.setId(UserUtils.getCurrentUserId());
		
		Map<String, List<CertificateVO>> map = new HashMap<String, List<CertificateVO>>();
		System.out.println(list.size() + "------------------------------");
		// 查看科目编码是否存在于数据库，并替换科目 id
		if(list.size() >0){
			for(CertificateVO vo:list){
				CpaCustomerSubject ccs = dao.getCpaCustomerSubjectBySubjectNo(vo.getSubjectCode(), bookId);
				if(ccs == null){
					throw new LogicException("当前科目编码不存在" + vo.getSubjectCode());
				}
				vo.setCertificateNum(vo.getCertificateNum().substring(vo.getCertificateNum().lastIndexOf("-") + 1));
				vo.setSubjectCode(ccs.getId());
				// 查看数据库里是否存在该条数据
				System.out.println(vo.getDate() + "凭证日期为 " );
				System.out.println(vo.getCertificateNum() + "凭证编号为 " );
				CpaVoucher cv = dao.findByVoucherNoAndVoucherDate(vo.getCertificateNum(), vo.getDate());
				if(cv != null){
					throw new LogicException( "凭证日期为 " + vo.getDate() + " 凭证编号为 " + vo.getCertificateNum() + "已存在");
				}
				
			}
		}
		// 循环组装数据，根据 凭证号 来组装 map，key 为凭证号 + "-" + 日期
		for(CertificateVO vo:list){
			List<CertificateVO> voList = new ArrayList<CertificateVO>();
			for(CertificateVO vo2:list){
				if(map.get(vo.getCertificateNum() + "-" + vo.getDate().substring(0,vo.getDate().lastIndexOf("-"))) != null){
					List<CertificateVO> voList2 = map.get(vo.getCertificateNum() + "-" + vo.getDate().substring(0,vo.getDate().lastIndexOf("-")));
					if(!voList2.contains(vo2) && vo.getCertificateNum().equals(vo2.getCertificateNum()) && (vo.getDate().substring(0,vo.getDate().lastIndexOf("-"))).equals(vo2.getDate().substring(0,vo2.getDate().lastIndexOf("-")))){
						voList2.add(vo2);
					}
				}else{
					if(!voList.contains(vo2) && vo.getCertificateNum().equals(vo2.getCertificateNum()) && vo.getDate().substring(0,vo.getDate().lastIndexOf("-")).equals(vo2.getDate().substring(0,vo2.getDate().lastIndexOf("-")))){
						voList.add(vo2);
					}
					map.put(vo.getCertificateNum() + "-" + vo.getDate().substring(0,vo.getDate().lastIndexOf("-")), voList);
				}
			}
		}
		
		//SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		// 遍历 map，重新组装 保存对象 list
		 int i = 0;
		 System.out.println("通过Map.entrySet遍历key和value");
		  for (Map.Entry<String, List<CertificateVO>> entry : map.entrySet()) {
			   System.out.println("key= " + entry.getKey());
			   System.out.println("and value= " + entry.getValue());
			   List<CertificateVO> voList = entry.getValue();
			   
			   // 构造凭证对象
			   CpaVoucher cv = new CpaVoucher();
			   // 凭证编号
			   cv.setVoucherNo(voList.get(0).getCertificateNum());
			   // 客户 ID
			   cv.setCustomerId(customerId);
			   // 账薄 ID
			   cv.setBookId(bookId);
			   // 凭证日期
			   cv.setVoucherDate(DateUtils.parseDate(voList.get(0).getDate()));
			   // id
			   String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			   cv.setId(uuid);
			  
			   cv.setCreateBy(user);

			   // 定义总金额出始值
			   Double totalAmount = 0.0;
			  /* for(int i = 0; i < voList.size(); i++){
				   voList.get(i).getAmount();
			   }*/
			  
			   for(CertificateVO vo2:voList){
				   i++;
				   // 凭证科目
				   CpaVoucherSubject cvs = new CpaVoucherSubject();
				   String uuid2 = UUID.randomUUID().toString().replaceAll("-", "");
				   System.out.println(vo2.getDate().substring(0,vo2.getDate().lastIndexOf("-") + 1 )+ "01");
				   // 所属账期
				   cvs.setVoucherPeriod(DateUtils.parseDate(vo2.getDate().substring(0,vo2.getDate().lastIndexOf("-") + 1 )+ "01"));
				   // 科目 id
				   cvs.setSubjectId(vo2.getSubjectCode());
				   //摘要
				   cvs.setAbstracts(vo2.getSummary());
				   // 科目名称
				   cvs.setSubjectName(vo2.getSubjectNum());
				   // 金额
				   if("借".equals(vo2.getDirection())){
					   cvs.setAmountDebit(new BigDecimal(vo2.getAmount()));
				   }else {
					   cvs.setAmountCredit(new BigDecimal(vo2.getAmount()));
				   }
				   // 金额累计
				   totalAmount = add(totalAmount.toString(), vo2.getAmount());
				   
				   // 凭证 id
				   cvs.setVoucherId(uuid);
				   cvs.setId(uuid2);
				   cvs.setCreateBy(user);
				   cvs.setSort(i);
				   cvsList.add(cvs);
				   
				   System.out.println(totalAmount.toString() + "===============================================================================================");
			   }
			   
			// 借方金额 贷方金额 金额大写
			   cv.setAmountDebit(new BigDecimal(totalAmount.toString()));
			   cv.setAmountCredit(new BigDecimal(totalAmount.toString()));
			   cv.setAccountCapital(NumberToCN.number2CNMontrayUnit(new BigDecimal(totalAmount.toString())));
			   
			   cvList.add(cv);
		   
		  }
		  try{
			  dao.batchInsertCpaVoucher(cvList);
			  dao.batchInsertCpaVoucherSubject(cvsList);
		  }catch(Exception e){
			  e.printStackTrace();
			  throw new LogicException("导入凭证失败");
		  }
		  return true;
	}
	
	/** 精准计算金额   加法 */
	 public static double add(String v1,String v2){
	        BigDecimal b1 = new BigDecimal(v1);
	        BigDecimal b2 = new BigDecimal(v2);
	        return b1.add(b2).doubleValue();
	  }
}
