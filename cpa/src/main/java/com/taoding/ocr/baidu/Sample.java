package com.taoding.ocr.baidu;

import java.util.HashMap;

import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;
import com.taoding.common.utils.MyFileUtils;

/**
 * Created by Administrator on 2017/11/24.
 */
public class Sample {
    //设置APPID/AK/SK
    public static final String APP_ID = "TicketOCR";
    public static final String API_KEY = "6fCF4kG2KFL43R4rQDrUZr6U";
    public static final String SECRET_KEY = "GlzE7HyqPe9LFEDDud5qSRbmg2h0sd5g";

    static AipOcr client = null;
    static{
        client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 初始化一个AipOcr
        //AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
    }

    public static void main(String[] args) {

        /**
        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 调用接口
        String path = "test.jpg";
        JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
        System.out.println(res.toString(2));*/
        String imgPath = "1.jpg";
        generalRecognition(imgPath);

    }
    public static void generalRecognition(String imgPath) {
        // 参数为本地图片路径
        String imagePath = "1.jpg";
       /* JSONObject response = client.basicGeneral(imagePath, new HashMap<String, String>());
        System.out.println(response.toString());*/

        // 参数为本地图片文件二进制数组
        byte[] file = MyFileUtils.readImageFile(imagePath);
        JSONObject response2 = client.accurateGeneral(file, new HashMap<String, String>());
        System.out.println("---------------");
        System.out.println(response2.toString());

        // 参数为图片url
        //String url = "http://some.com/a.jpg";
        //JSONObject response3 = client.basicGeneralUrl(url, new HashMap<String, String>());
        //System.out.println(response3.toString());
    }

}
