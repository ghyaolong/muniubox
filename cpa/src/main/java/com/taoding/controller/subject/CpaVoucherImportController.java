package com.taoding.controller.subject;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.taoding.common.controller.BaseController;
import com.taoding.common.excel.ImportExcel;
import com.taoding.common.utils.Collections3;
import com.taoding.domain.subject.CertificateVO;
import com.taoding.service.subject.CpaVoucherImportService;


/**
 * 凭证导入
 */
@RestController
@RequestMapping(value = "/cpavoucherimport")
public class CpaVoucherImportController extends BaseController{
	
	@Autowired
	private CpaVoucherImportService cpaVoucherImportService ;

	/**
	 * 导入凭证
	 * @param file Excel 文件
	 * @param response
	 * @param customerId 客户 id
	 * @param bookId 账薄 id
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/import")
	public Object savaCertificateData( MultipartFile file, HttpServletResponse response, String customerId, String bookId) throws Exception {
		if (file.isEmpty()) {  
			return false;
		}
		ImportExcel ei = new ImportExcel(file, 0, 0);
		List<CertificateVO> list = ei.getDataListTwo(CertificateVO.class);
		
		System.out.println(list.size() + "===========================================");
		if(Collections3.isEmpty(list)){
			return false;
		}
			
		return cpaVoucherImportService.savaCertificateData(list, customerId, bookId);
	}
}
