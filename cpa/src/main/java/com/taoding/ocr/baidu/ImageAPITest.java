package com.taoding.ocr.baidu;

import java.util.HashMap;
import java.util.Map;

import com.taoding.common.utils.Base64ImageUtils;

/**
 * Created by Administrator on 2017/11/23.
 */
public class ImageAPITest {

    private static String TICKET_GENERAL="https://aip.baidubce.com/rest/2.0/ocr/v1/receipt";
//    public static void main(String[] args) {
////        String token = AuthService.getAuth();
////        System.out.println(token);
//    }

    private static void testGeneralImage(){
        String token = "24.c0fd2e61aec31a67d161bfce4385cc59.2592000.1514024418.282335-10427875";

        String Filepath = "1.jpg";
        String image = Base64ImageUtils.GetImageStrFromPath(Filepath);
        String url = "https://aip.baidubce.com/rest/2.0/face/v1/detect?access_token="+token;

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("image", image);
        bodys.put("face_fields", "age,beauty,expression,gender,glasses,race,qualities");

       /* try {
            CloseableHttpResponse response =  HttpClientUtils.doHttpsPost(url,headers,bodys);
            System.out.println(HttpClientUtils.toString(response));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
