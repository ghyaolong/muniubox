package com.taoding.controller.ocr;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.taoding.common.exception.BRecognitionException;
import com.taoding.common.utils.FileUtils;
import com.taoding.common.utils.JsonUtil;
import com.taoding.common.utils.MyFileUtils;
import com.taoding.domain.AjaxJson;
import com.taoding.mq.Sender;
import com.taoding.service.ocr.OCRService;

/**
 * Created by yaochenglong on 2017/11/29.
 * 票据业务
 */
//@RestController
public class InvoiceController {

	public final Logger logger = Logger.getLogger(this.getClass());

	public static final String ROOT = "upload-dir";

	private final ResourceLoader resourceLoader;
	
	
	@Value("${ticketImg.path}")
	private String imgPath;

	@Autowired
	private Sender sender;

	@Autowired
	public InvoiceController(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	// 显示图片的方法关键 匹配路径像 localhost:8080/b7c76eb3-5a67-4d41-ae5c-1642af3f8746.png
	@RequestMapping(method = RequestMethod.GET, value = "/{filename:.+}")
	@ResponseBody
	public ResponseEntity<?> getFile(@PathVariable String filename) {

		try {

			ResponseEntity<Resource> ok = ResponseEntity
					.ok(resourceLoader.getResource("file:" + Paths.get(imgPath).toString()));

			return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT, filename).toString()));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * 上传图片
	 * 使用NIO可以提高图片上传效率。
	 * @param files图片
	 * @param accountDate所属账期
	 * @param customerId 客户id
	 * @param accountBookId:账簿Id
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/uploadImg")
	public AjaxJson uploadImg(@RequestParam(value = "file", required = false) MultipartFile[] files, String accountDate,
			String customerId, String accountBookId, HttpServletRequest request) {
		Long start = System.currentTimeMillis();
		AjaxJson j = new AjaxJson();
		List<String> errorImgList = new ArrayList<String>();
		j.setMsg("上传成功");
		if (StringUtils.isEmpty(accountDate) || StringUtils.isEmpty(customerId) || StringUtils.isEmpty(accountBookId)) {
			j.setSuccess(false);
			//throw new LogicException("参数accountDate,customerId或accountBookId不能为空");
			j.setMsg("参数accountDate,customerId或bookId不能为空");
			
			return j;
		}
		if (files != null) {
			String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
			String newFileName = null;
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					
					String fileName = null;
					try {
						fileName = file.getOriginalFilename();
						String ext = MyFileUtils.getExtensionName(fileName);
						if (ext == null) {
							// 说明上传的文件不是没有扩展名，不是正规的文件
							errorImgList.add(fileName+" [无法识别的文件]");
						} else {
							boolean isImg = MyFileUtils.isImg(ext);
							if (!isImg) {
								errorImgList.add(fileName+" [不是图片，请上传图片]");
							}

							// 判断文件大小
							if (!MyFileUtils.isAllowSize(file.getSize())) {
								errorImgList.add(fileName+" [图片文件过大，不能超过4M]");
							}
						}
						newFileName = UUID.randomUUID().toString().replace("-", "") + ext;
						
						
						//String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath().replaceAll("/", "\\\\");
						
						//Files.copy(file.getInputStream(), Paths.get(filePath+imgPath, newFileName));
						FileUtils.uploadFile(file.getBytes(), filePath + imgPath, newFileName);
						
					} catch (IOException | RuntimeException e) {
						errorImgList.add(fileName);
						logger.error("上传文件失败",e);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(!CollectionUtils.isEmpty(errorImgList)) {
						j.setSuccess(false);
						j.setMsg("上传失败");
						j.setData(errorImgList);
						logger.error("上传失败["+JSON.toJSONString(errorImgList)+"]");
						//throw new LogicException("上传失败["+JSON.toJSONString(errorImgList)+"]");
						return j;
					}
					
					// 将图片的路径地址作为消息传递给MQ
					//String imgPath = Paths.get(ROOT, newFileName).toString();
					String imgPath1 = filePath + imgPath+ newFileName;
					logger.info("上传的图片路径地址："+imgPath1);
					Map<String, String> map = new HashMap<String, String>();
					map.put("imgPath", imgPath1);
					map.put("accountDate", accountDate);
					map.put("customerId", customerId);
					map.put("bookId", accountBookId);
					sender.send(map);
					Long end = System.currentTimeMillis();
					logger.info("上传花费时间:" + (end - start));
				} else {
					j.setSuccess(false);
					j.setMsg("未有图片上传");
					return j;
					//throw new LogicException("未有图片上传");
				}
			}
		}
		return j;
	}

	/**
	 * 通过百度的OCR技术，识别上传的票据的数据，
	 * 读取数据，并保存
	 * @return
	 */
	@Deprecated
	@GetMapping("/entering")
	public String intelliEntering() throws IOException {
		// 这里建议使用RabbitMQ，将图片的解析和图片的归档分开处理，减少操作步骤，提高操作效率。
		Path path = Paths.get(ROOT);
		DirectoryStream<Path> paths = Files.newDirectoryStream(path);
		for (Path p : paths) {
			String imgPath = p.getParent().toString() + File.separator + p.getFileName().toString();
			System.out.println(imgPath);
			Object o = null;
			/*try {
				//o = ocrService.execute(imgPath);
				ocrService.parse(null);
			} catch (BRecognitionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			return JsonUtil.objectToJson(o);
			// System.out.println(p.getFileName());
		}
		String imgPath = "";
		// ocrService.execute(imgPath);
		return "operate ok";
	}
}
