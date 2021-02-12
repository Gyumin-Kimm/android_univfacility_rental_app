package admin_Activity;

import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import user_Activity.ActivityManager;
import user_Activity.MainActivity;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AdminActivity extends ActionBarActivity {
	
	TextView qna, approval;
	Httptask task;
	Httptask1 task1;
	String Member_id;
	Http_Get get;
	Button btn_logout;
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
		setContentView(R.layout.activity_admin);
		ActivityManager.addActivity(this);
		
		//cookie 값 받아오기
		SessionControl.cookies =SessionControl.httpclient.getCookieStore().getCookies();
		Cookie cookie;
		for (int i = 0; i < SessionControl.cookies.size(); i++) {
			cookie = SessionControl.cookies.get(i);
			if (!cookie.getName().equals("JSESSIONID")) {
				Member_id = cookie.getName();
			}
		}
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		task = new Httptask();
		task.execute(params);
		
		//버튼 클릭리스너
		OnClickListener myOnClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getId() == R.id.btnnotice)
					startActivity(new Intent(getApplicationContext(),BoardActivity.class));
				else if(v.getId() == R.id.btnapproval)
					startActivity(new Intent(getApplicationContext(),ApprovalActivity.class));
				else if(v.getId() == R.id.btnmanage)
					startActivity(new Intent(getApplicationContext(),ManageActivity.class));
			}
		};
		findViewById(R.id.btnnotice).setOnClickListener(myOnClickListener);
		findViewById(R.id.btnapproval).setOnClickListener(myOnClickListener);
		findViewById(R.id.btnmanage).setOnClickListener(myOnClickListener);		
		
		
		
		btn_logout = (Button)findViewById(R.id.btnlogout);
		btn_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);     
		
			    //여기서 부터는 알림창의 속성 설정
			    builder.setTitle("로그아웃")        			// 제목 설정
			    .setMessage("로그아웃하시겠습니까?")        		// 메세지 설정
			    .setCancelable(false)        				// 뒤로 버튼 클릭시 취소 가능 설정
			    .setIcon(R.drawable.ic_launcher) 			//아이콘 설정
			    .setPositiveButton("확인", new DialogInterface.OnClickListener(){ // 확인 버튼 클릭시 설정
			    	public void onClick(DialogInterface dialog, int whichButton){
			    		task.cancel(true);
			    		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("MEMBER_ID",Member_id));
						
						task1 = new Httptask1();
						task1.execute(params); 
						
						intent = new Intent(getApplicationContext(), MainActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//쌓인 액티비티 전부 제거
						startActivity(intent);
			    	}    
			    })
			    .setNegativeButton("취소", new DialogInterface.OnClickListener(){// 취소 버튼 클릭시 설정
				    public void onClick(DialogInterface dialog, int whichButton){
				    	dialog.cancel();
				    }
			    });
			    
			    AlertDialog dialog = builder.create();    // 알림창 객체 생성
			    dialog.show(); 
			}
		});
	}

	@Override  //back 버튼을 눌렀을 때 호출 
	   public boolean onKeyDown(int keyCode, KeyEvent event) {
	       switch(keyCode) {
	         case KeyEvent.KEYCODE_BACK:
	          new AlertDialog.Builder(this)
	                          .setTitle("로그아웃")
	                          .setMessage("로그아웃 하시겠습니까?")
	                          .setCancelable(false)
	                          .setIcon(R.drawable.ic_launcher)
	                          .setPositiveButton("확인", new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog, int whichButton) {
	                            	task.cancel(true);
	                            	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	            					params.add(new BasicNameValuePair("MEMBER_ID",Member_id));
	            					
	            					task1 = new Httptask1();
	            					task1.execute(params); 
	            					
	                              Intent intent = new Intent(AdminActivity.this, MainActivity.class);
	           					  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//쌓인 액티비티 전부 제거
	           					  startActivity(intent);
	                           }
	                         })
	                         .setNegativeButton("취소", null).show();
	                         return false;
	          default:
	            return false;
	      }
	  }
	
	// admin 알림
	class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>{
		JSONArray ad_array;
		JSONObject ad_object;
		Http_Get get;
		String str_qna, str_approval;
		
		@Override 
		protected String doInBackground(ArrayList<NameValuePair>... params) {

			get= new Http_Get(); 
			String result=get.httpget(SessionControl.URL+"FacBookAdmin.ad",params[0]);	
			
		return result;
		
		}
		
		@Override
		protected void onPostExecute(String Jason){    
			super.onPostExecute(Jason);
			 try {
		        	 ad_array= new JSONArray(Jason); 

		             ad_object = ad_array.getJSONObject(0);  

		             str_qna = "현재 들어온 강의실 대여 승인 요청수는 " +
		            		 String.valueOf(ad_object.getInt("BOOKCOUNT")) +
		            		 "건 입니다. ";
		             str_approval = "현재 들어온 QnA 답변 요청수는 " +
		            		 String.valueOf(ad_object.getInt("QACOUNT")) +
		            		 "건 입니다. ";
       
		             qna = (TextView)findViewById(R.id.tv_qna);
		             approval = (TextView)findViewById(R.id.tv_approval);
		             
		             qna.setText(str_qna);
		             approval.setText(str_approval);
		        }
			 			catch(JSONException e) {
		               Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();  
		        }
		} 
	}
	class Httptask1 extends AsyncTask<ArrayList<NameValuePair>,Void,String>
	{
	    @Override
		protected String doInBackground(ArrayList<NameValuePair>... params) {
			
	    	String url = SessionControl.URL+"Logout.ad";
				
	    	get= new Http_Get(); 
			String result= get.httpget(url, params[0]);
			//서버로 url요청을 보내고, 응답데이터를  String타입으로 리턴 
			//회원가입 성공시 서버에서 "1"을 보냄 
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
				Toast.makeText(getApplicationContext(), "로그아웃 완료", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), "로그아웃 실패", Toast.LENGTH_SHORT).show();
			}
		}	
	}
			
}
