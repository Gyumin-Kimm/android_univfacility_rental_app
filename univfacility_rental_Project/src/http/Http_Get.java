package http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;



// ������ url��û�� ������, ���䵥���͸�  StringŸ������ �������ִ� ������ �ϴ� Ŭ����

public class Http_Get {
	
  public String httpget(String url, ArrayList<NameValuePair> params)
  {
	  	 String result="";
	  	 
		 HttpClient  client = SessionControl.getHttpClient();
		 try{
				HttpPost post= new HttpPost(url); //������ ��ûurl�� ���� (��û����� POST��� )
				
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,"UTF-8");
				post.setEntity(ent);
				//���۵Ǵ� ������ ���ڵ� ����� �����ϱ� ���ؼ�  UrlEncodedFormEntity��ü �����κ�
				//���۵Ǵ� ��(params) ���ڵ���� (HTTP.UTF_8)
				HttpResponse responsePOST=client.execute(post); 
				//��û ó�� ����� HttpResponse��ü.(���䵥����) �� ��ü�κ��� ���ϴ� ����� ������ ���� 
				HttpEntity resEntity= responsePOST.getEntity(); 
				//���� ��ü�� getEntity()�޼���� HttpEntity��ü�� �����ϸ� 
				//�� ��ü�� getContent�޼���� ����Ƽ�� ���� �� �ִ� �Է� ��Ʈ���� ����
			 	if (resEntity != null) {
					BufferedReader rd = new BufferedReader(new InputStreamReader(
							responsePOST.getEntity().getContent()));
					
					String line = "";
					while ((line = rd.readLine()) != null) {
							result += line;}
                    //responsePOST.getEntity().getContent()�޼ҵ带 ����ؼ�
					//���������� �����ϴ� �����͸� ���� �� �ִ�
					//�Է� ��Ʈ���� ���� ������,���ϰ� ���δ�����
					//�����͸� ���� �� �ֵ��� BufferedReader��ü�� �����ϴ� �κ�
			 			}
				 
		 } catch(Exception e){
			   e.printStackTrace();
        	}
		return result; 
	}

}
