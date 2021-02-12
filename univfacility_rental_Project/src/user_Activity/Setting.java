package user_Activity;

import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Setting extends Activity {

	String version = "���� ���� v1.0\n" + "�ֽ� ���� v1.0";
	TextView UserId;
	Button Pwd_set, Logout, Qa, Version, Developer_info;
	httptask task;
	Http_Get get;
	String Member_id;
	Toast toast;
	Intent intent;
	
	public static ArrayList<Activity> actList = new ArrayList<Activity>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.setting);
	    
	 getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
	    
	 if(SessionControl.cookies!=null){
			for (int i = 0; i < SessionControl.cookies.size(); i++) {
				Cookie cookie = SessionControl.cookies.get(i);

				if (!cookie.getName().equals("JSESSIONID")) {
					Member_id = cookie.getName();
				}
			}
		}
	 
	    UserId=(TextView)findViewById(R.id.textView3);
		UserId.setText(Member_id); //�α��������� ���̵� ǥ��
		
		
		//////////�α׾ƿ� ��ư
	    Logout =(Button)findViewById(R.id.logout_btn);  
	    Logout.setOnClickListener(new OnClickListener() { 
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);     
		        //���⼭ this�� Activity�� this
		
			    //���⼭ ���ʹ� �˸�â�� �Ӽ� ����
			    builder.setTitle("�α׾ƿ�")        			// ���� ����
			    .setMessage("�α׾ƿ��Ͻðڽ��ϱ�?")        		// �޼��� ����
			    .setCancelable(false)        				// �ڷ� ��ư Ŭ���� ��� ���� ����
			    .setIcon(R.drawable.ic_launcher) 			//������ ����
			    .setPositiveButton("Ȯ��", new DialogInterface.OnClickListener(){       
			     // Ȯ�� ��ư Ŭ���� ����
			    public void onClick(DialogInterface dialog, int whichButton){
			    
			    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("MEMBER_ID",Member_id));
					
					task = new httptask();
					task.execute(params); 
					
					intent = new Intent(Setting.this, Login.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//���� ��Ƽ��Ƽ ���� �����ϰ� Login��Ƽ��Ƽ�� �̵�
					startActivity(intent);
					
			    }    }
			    )
			    .setNegativeButton("���", new DialogInterface.OnClickListener(){      
			         // ��� ��ư Ŭ���� ����
			    public void onClick(DialogInterface dialog, int whichButton){
			    dialog.cancel();
			    }
			    });
			    
			    AlertDialog dialog = builder.create();    // �˸�â ��ü ����
			    dialog.show();    // �˸�â ����
			} 
			}  ); 
	   
	    
	    //////////Q&A �� �����Ű� ��ư
	    Qa =(Button)findViewById(R.id.qa_btn); 
	    Qa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(Setting.this, User_Qna.class);
				startActivity(intent);
			}
		});
	    
	    ////////���� ���� ��ư
	    Version =(Button)findViewById(R.id.version_btn); 
	    Version.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toast = Toast.makeText(Setting.this,version,Toast.LENGTH_SHORT);
				toast.show(); 
			}
		});
	    
	    //////////������ ���� ��ư
	    Developer_info = (Button)findViewById(R.id.developer_info_btn);
	    Developer_info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(Setting.this, Develop_info.class);
				startActivity(intent);
			}
		});
	}

	
	public void mOnClick_passwd(View v){
		final LinearLayout linear=(LinearLayout)View.inflate(this, R.layout.passwd_set, null);
		
		new AlertDialog.Builder(this)
		.setTitle("��й�ȣ �缳��")
		.setIcon(R.drawable.ic_launcher)
		.setView(linear)
		.setPositiveButton("����", null)
		.setNegativeButton("���", null)
		.show();
		}
	
	class httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>
		{
		    @Override
			protected String doInBackground(ArrayList<NameValuePair>... params) {
				
		    	String url = SessionControl.URL+"Logout.ad";
					
		    	get= new Http_Get(); 
				String result= get.httpget(url, params[0]);
				//������ url��û�� ������, ���䵥���͸�  StringŸ������ ���� 
				//ȸ������ ������ �������� "1"�� ���� 
				SessionControl.cookies =SessionControl.httpclient.getCookieStore().getCookies();
				Cookie cookie;
				for (int i = 0; i < SessionControl.cookies.size(); i++) {
					cookie = SessionControl.cookies.get(i);
					Log.i("log",cookie.getName());
					if (!cookie.getName().equals("JSESSIONID")) {
						Member_id = cookie.getName();
					}
				}
	
				
				return result;
			}
		
		@Override
		protected void onPostExecute(String result)
		{    
			super.onPostExecute(result);
			
			if(result.equals("1")){
				
				toast= Toast.makeText(Setting.this, "�α׾ƿ� �Ϸ�", Toast.LENGTH_SHORT);
				 toast.show();
				}else{
				 toast= Toast.makeText(Setting.this, "�α׾ƿ� ����", Toast.LENGTH_SHORT);
				 toast.show();
				}
			// ���Խ��� ��  result�������� �����޽��� �ٸ��� �ߵ��� 
			// �ߺ��� ���̵� �߸��� �Էµ� ���Խ��� ������ �ٸ��� �ߵ��� ����?
		}	}
				
}
	
