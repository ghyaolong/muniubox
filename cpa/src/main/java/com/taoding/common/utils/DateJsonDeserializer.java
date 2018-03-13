package com.taoding.common.utils;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.taoding.configuration.Global;
/**
 * 
* @ClassName: DateJsonDeserializer 
* @Description: TODO(用户处理requsetBody 日期类型转换) 
* @author lixc 
* @date 2017年11月20日 下午6:21:16 
*
 */
public class DateJsonDeserializer extends JsonDeserializer<Date>   {
	  
	    public Date deserialize(JsonParser jsonParser,DeserializationContext deserializationContext)  
	            throws IOException,JsonProcessingException  
	    {  
	        try  
	        {  
	        	String dataStr= jsonParser.getText();
	            return DateUtils.parseDate(dataStr, Global.DATA_FORMATE);
	        }  
	        catch(Exception e)  
	        {  
	            System.out.println(e.getMessage());  
	            throw new RuntimeException(e);  
	        }   
	    }  
}
