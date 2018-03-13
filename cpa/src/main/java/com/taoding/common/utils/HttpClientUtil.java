package com.taoding.common.utils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taoding.common.exception.LogicException;
import com.taoding.common.response.Response;
import com.taoding.common.response.ResponseCode;

/**
 * @category http请求工具类 
 * @author LX-PC
 *
 */
public class HttpClientUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * 提供HTTP GET 请求 
	 * @param url 请求地址 即请求行
	 * @param param 请求参数 key-value 集合
	 * @return
	 */
	public static String doGet(String url, Map<String, String> param) {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();

			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);

			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}
	
	/**
	 * HTTP GET 请求 
	 * @param url 请求行
	 * @return
	 */
	public static String doGet(String url) {
		return doGet(url, null);
	}

	/**
	 * HTTP POST 请求 (Content-Type: application/x-www-form-urlencoded)
	 * @param url 请求地址 即请求行
	 * @param param 请求参数 key-value 集合
	 * @return
	 */
	public static String doPost(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}

	/**
	 * HTTP POST 请求 
	 * @param url 请求地址 即请求行
	 * @return
	 */
	public static String doPost(String url) {
		return doPost(url, null);
	}

	/**
	 * HTTP POST 请求 (Content-Type: application/json)
	 * @param url 请求地址 请求行
	 * @param json 参数 json 字符串
	 * @return
	 */
	public static String doPostJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}
	
	/**
	 * 根据url和jwt token请求服务器上特定的接口，并按给定的类信息序返列化返回的信息
	 * collectionClass： 如果返回的是一个集合，比如List<Menu>，那么collectionClass的值应该是List.class
	 * elementClass: 如果返回的是一个简单的对象，比如Menu，那么collectionClass为null, elementClass的值为Menu.class
	 * @param url 服务器接口地址
	 * @param token jwtToken
	 * @param collectionClass 返回结果的集合类型
	 * @param elementClass 返回结果的简单类型
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object sendGet(final String url, String token, final Class collectionClass, final Class elementClass) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Authorization", "Bearer " + token);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            String result = handleResponse(response);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            if (null != collectionClass) {
            	JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClass);
            	return mapper.readValue(result, javaType);
            } else {
            	return mapper.readValue(result, elementClass);
            }
        } catch (IOException e) {
        	logger.error(e.getMessage());
            throw new LogicException("和基础服务的连接异常");
        } finally {
            if (null != response) {
               try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    response = null;
                }
            }
        }
    }

    /**
     * 抽取http请求的响应体
     * @param response HttpResponse
     * @return String
     */
    private static String handleResponse(final HttpResponse response){
        String result = null;
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() != 200) {
        	throw new LogicException("向基础服务请求异常，状态码：" + statusLine.getStatusCode());
        }
        if (entity == null) {
        	throw new LogicException("向基础服务请求异常，没有返回响应信息");
        }
        ContentType contentType = ContentType.getOrDefault(entity);
        Charset charset = contentType.getCharset();
        try {
            String resultString = EntityUtils.toString(entity, charset);
            ObjectMapper mapper = new ObjectMapper();
            Response respon = mapper.readValue(resultString, Response.class);
            if (respon.getStatus() != ResponseCode.OK.getValue()) {
            	throw new LogicException(respon.getMessage());
            }
            result = resultString.substring(25, resultString.length() - 16);
        } catch (IOException e) {
        	logger.error(e.getMessage());
        	throw new LogicException("基础服务返回的信息格式不正确");
        }
        return  result;
    }
}