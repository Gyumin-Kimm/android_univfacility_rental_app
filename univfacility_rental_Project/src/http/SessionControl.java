package http;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
 
public class SessionControl {
	public static String URL= "http://172.30.13.228:8080/project/";
	//172.30.21.126
	//172.30.13.228
	//172.30.10.126
	//192.168.11.22 6106-1
	public static  DefaultHttpClient httpclient ;
	public static  List<Cookie> cookies=null;
	
	public static HttpClient getHttpClient(){
		if(httpclient==null)
			SessionControl.setHttpclient(new DefaultHttpClient());
	return httpclient;
	}
	
	public static void setHttpclient(DefaultHttpClient httpclient)
	{
		SessionControl.httpclient = httpclient;
	}
	
}