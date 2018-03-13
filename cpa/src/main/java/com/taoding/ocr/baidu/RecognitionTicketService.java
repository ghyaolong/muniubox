package com.taoding.ocr.baidu;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.baidu.aip.ocr.AipOcr;
import com.taoding.common.exception.BRecognitionException;
import com.taoding.common.utils.MyFileUtils;
import com.taoding.common.utils.StringUtils;

/**   
 * @ClassName:  BOCRService   
 * @Description:百度OCR票据识别服务
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月7日 上午10:22:57   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved. 
 */
//@Service
@Component
@ConfigurationProperties(prefix="ocr")
public class RecognitionTicketService {

	private static Logger logger = Logger.getLogger(RecognitionTicketService.class);
	
	private static String appId;
	
	private static String apiKey;
	
	public static String secretKey;
	
	
	// 设置APPID/AK/SK
	/*public static final String APP_ID = "TicketOCR";
	public static final String API_KEY = "6fCF4kG2KFL43R4rQDrUZr6U";
	public static final String SECRET_KEY = "GlzE7HyqPe9LFEDDud5qSRbmg2h0sd5g";
*/
	public static final String APP_ID = appId;
	public static final String API_KEY = apiKey;
	public static final String SECRET_KEY = secretKey;
	
	static AipOcr client = null;

	/*static {
		client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(10000);
		client.setSocketTimeoutInMillis(60000);
	}*/
	
	private static void init() {
		//client = new AipOcr(appId, apiKey, secretKey);
		// 初始化一个AipOcr
		 AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(10000);
		client.setSocketTimeoutInMillis(60000);
	}

	public static void main(String[] args) throws BRecognitionException {

		// **
		// 可选：设置代理服务器地址, http和socket二选一，或者均不设置
		// client.setHttpProxy("proxy_host", proxy_port); // 设置http代理
		// client.setSocketProxy("proxy_host", proxy_port); // 设置socket代理

		// 调用接口
		// String path = "test.jpg";
		// JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
		// System.out.println(res.toString(2));
		String imgPath = "train_01.png";
		String s = generalRecognition(imgPath);
		System.out.println(s);

	}

	public static String generalRecognition(String imgPath) throws BRecognitionException {
		// 参数为本地图片路径
		// String imagePath = "1.jpg";
		/* JSONObject response = client.basicGeneral(imagePath, new HashMap<String, String>());
		System.out.println(response.toString());*/
		init();
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("probability", "true");

		// 参数为本地图片文件二进制数组
		byte[] file = MyFileUtils.readImageFile(imgPath);
		// JSONObject response2 = client.accurateGeneral(file, new HashMap<String,
		// String>());
		try {
			JSONObject response2 = client.receipt(file, options);
			// String errorCode = response2.get("error_code").toString();
			String errorCode = null;
			try {
				Object object = response2.get("error_code");
				if (object != null) {
					errorCode = object.toString();
				}
			} catch (JSONException e) {
				// 抛出异常，说明没有找到error_code,不用理会
			}
			if (StringUtils.isNotEmpty(errorCode)) {
				logger.error("调用百度OCR服务异常，百度OCR错误码:" + errorCode);
				throw new BRecognitionException("调用百度OCR服务异常，百度OCR错误码:" + errorCode);

			}

			// {"error_msg":"IAM Certification failed","error_code":14}
			logger.info("解析结果：" + response2.toString());
			return response2.toString();
		} catch (Exception e) {
			throw new BRecognitionException("调用百度OCR服务异常", e);
		}

		// 参数为图片url
		// String url = "http://some.com/a.jpg";
		// JSONObject response3 = client.basicGeneralUrl(url, new HashMap<String,
		// String>());
		// System.out.println(response3.toString());
	}

	
	
	@Value("${ocr.appId}")
	public void setAppId(String appId) {
		RecognitionTicketService.appId = appId;
	}

	@Value("${ocr.apiKey}")
	public void setApiKey(String apiKey) {
		RecognitionTicketService.apiKey = apiKey;
	}

	
	@Value("${ocr.secretKey}")
	public void setSecretKey(String secretKey) {
		RecognitionTicketService.secretKey = secretKey;
	}
	
	

}
