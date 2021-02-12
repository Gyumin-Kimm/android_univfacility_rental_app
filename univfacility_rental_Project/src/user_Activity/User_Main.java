package user_Activity;

import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;

import admin_Activity.BoardActivity;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class User_Main extends Activity  {
	public static String userId = null;
	String MemberId , Member_id;
	Intent intent;
	Button Board_btn, Book_btn, Mypage_btn, Setting_btn;
	httptask task;
	Http_Get get;
	Toast toast;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
	    setContentView(R.layout.user_main);
	    
	    ActivityManager.addActivity(this);
	  
		 if(SessionControl.cookies!=null){
				for (int i = 0; i < SessionControl.cookies.size(); i++) {
					Cookie cookie = SessionControl.cookies.get(i);

					if (!cookie.getName().equals("JSESSIONID")) {
						Member_id = cookie.getName();
					}
				}
			}
	    
	    Board_btn =(Button)findViewById(R.id.board_btn);
	    Board_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(User_Main.this, BoardActivity.class);
				startActivity(intent);
			}
		});
	    
	    
		Book_btn =(Button)findViewById(R.id.book_btn);
		Book_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(User_Main.this, Book_step1.class);
				startActivity(intent);
			}
		});
		
		Mypage_btn =(Button)findViewById(R.id.mypage_btn);
		Mypage_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(User_Main.this, My_Page.class);
				startActivity(intent);
			}
		});
		
		
		Setting_btn =(Button)findViewById(R.id.setting_btn);
		Setting_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(User_Main.this, Setting.class);
				startActivity(intent);
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
	                          .setIcon(R.drawable.ic_launcher)
	                          .setPositiveButton("확인", new DialogInterface.OnClickListener(){
	                            @SuppressWarnings("unchecked")
								public void onClick(DialogInterface dialog, int whichButton){
	                             
	           					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	        					params.add(new BasicNameValuePair("MEMBER_ID",Member_id));
	        					
	        					task = new httptask();
	        					task.execute(params); 
	        					
	        					intent = new Intent(User_Main.this, Login.class);
	        					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//쌓인 액티비티 전부 제거하고 Login액티비티로 이동
	        					startActivity(intent);
	           					  
	           					  startActivity(intent);
	                           }
	                         })
	                         .setNegativeButton("취소", null).show();
	                         return false;
	          default:
	            return false;
	      }
	  }
	

	class httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>
		{
		    @Override
			protected String doInBackground(ArrayList<NameValuePair>... params) {
				
		    	String url = SessionControl.URL+"Logout.ad";
					
		    	get= new Http_Get(); 
				String result= get.httpget(url, params[0]);
		
				return result;
			}
		
		@Override
		protected void onPostExecute(String result)
		{    
			super.onPostExecute(result);
			if(result.equals("1")){
				 toast= Toast.makeText(User_Main.this, "로그아웃 완료", Toast.LENGTH_SHORT);
				 toast.show();
				}else{
				 toast= Toast.makeText(User_Main.this, "로그아웃 실패", Toast.LENGTH_SHORT);
				 toast.show();
				}
			// 가입실패 시  result값에따라 오류메시지 다르게 뜨도록 
			// 중복된 아이디나 잘못된 입력등 가입실패 이유가 다르게 뜨도록 구현?
		}	}
	
}
