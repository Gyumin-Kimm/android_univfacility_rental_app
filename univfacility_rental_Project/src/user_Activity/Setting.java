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

	String version = "현재 버전 v1.0\n" + "최신 버전 v1.0";
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
		UserId.setText(Member_id); //로그인정보에 아이디 표시
		
		
		//////////로그아웃 버튼
	    Logout =(Button)findViewById(R.id.logout_btn);  
	    Logout.setOnClickListener(new OnClickListener() { 
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);     
		        //여기서 this는 Activity의 this
		
			    //여기서 부터는 알림창의 속성 설정
			    builder.setTitle("로그아웃")        			// 제목 설정
			    .setMessage("로그아웃하시겠습니까?")        		// 메세지 설정
			    .setCancelable(false)        				// 뒤로 버튼 클릭시 취소 가능 설정
			    .setIcon(R.drawable.ic_launcher) 			//아이콘 설정
			    .setPositiveButton("확인", new DialogInterface.OnClickListener(){       
			     // 확인 버튼 클릭시 설정
			    public void onClick(DialogInterface dialog, int whichButton){
			    
			    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("MEMBER_ID",Member_id));
					
					task = new httptask();
					task.execute(params); 
					
					intent = new Intent(Setting.this, Login.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//쌓인 액티비티 전부 제거하고 Login액티비티로 이동
					startActivity(intent);
					
			    }    }
			    )
			    .setNegativeButton("취소", new DialogInterface.OnClickListener(){      
			         // 취소 버튼 클릭시 설정
			    public void onClick(DialogInterface dialog, int whichButton){
			    dialog.cancel();
			    }
			    });
			    
			    AlertDialog dialog = builder.create();    // 알림창 객체 생성
			    dialog.show();    // 알림창 띄우기
			} 
			}  ); 
	   
	    
	    //////////Q&A 및 오류신고 버튼
	    Qa =(Button)findViewById(R.id.qa_btn); 
	    Qa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(Setting.this, User_Qna.class);
				startActivity(intent);
			}
		});
	    
	    ////////버전 정보 버튼
	    Version =(Button)findViewById(R.id.version_btn); 
	    Version.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toast = Toast.makeText(Setting.this,version,Toast.LENGTH_SHORT);
				toast.show(); 
			}
		});
	    
	    //////////개발자 정보 버튼
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
		.setTitle("비밀번호 재설정")
		.setIcon(R.drawable.ic_launcher)
		.setView(linear)
		.setPositiveButton("변경", null)
		.setNegativeButton("취소", null)
		.show();
		}
	
	class httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>
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
				
				toast= Toast.makeText(Setting.this, "로그아웃 완료", Toast.LENGTH_SHORT);
				 toast.show();
				}else{
				 toast= Toast.makeText(Setting.this, "로그아웃 실패", Toast.LENGTH_SHORT);
				 toast.show();
				}
			// 가입실패 시  result값에따라 오류메시지 다르게 뜨도록 
			// 중복된 아이디나 잘못된 입력등 가입실패 이유가 다르게 뜨도록 구현?
		}	}
				
}
	
