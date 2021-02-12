package user_Activity;



import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import bean.QnaBean;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class User_Qna extends ActionBarActivity {

	TextView time;
	Calendar cal;
	String date;
	Button sucess_btn, last_btn;
	Httptask task;
	Http_Get get;
	EditText edt;
	String Member_id;
	Intent intent;
	QnaBean qnabean;

		/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.qa);
		
		    if (SessionControl.cookies != null) {
				for (int i = 0; i < SessionControl.cookies.size(); i++) {
					
					Cookie cookie = SessionControl.cookies.get(i);
					
					if (!cookie.getName().equals("JSESSIONID")) {
						Member_id = cookie.getName();
					}
				}
		    }

		   cal = new GregorianCalendar();
		   date= String.format("%d-%d-%d",cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,
		    	 cal.get(Calendar.DAY_OF_MONTH));
		    
		   time=(TextView)findViewById(R.id.textView2);
		   time.setText(date);
		    
		   edt =(EditText)findViewById(R.id.editText1);
		   
		   last_btn =(Button)findViewById(R.id.cancel_btn);
		   last_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				edt.setText("");
			}
		});
		   
		   sucess_btn=(Button)findViewById(R.id.write_btn);
		   sucess_btn.setOnClickListener(new OnClickListener() {

			    @Override
				public void onClick(View v) {
			    	qnabean = new QnaBean();
					qnabean.setQNA_ID(Member_id);
					qnabean.setQNA_DATE(date);
					qnabean.setQNA_CONTENT(edt.getText().toString());
					 // TODO Auto-generated method stub
					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("QNA_DATE",qnabean.getQNA_DATE()));
					params.add(new BasicNameValuePair("MEMBER_ID",qnabean.getQNA_ID()));
					params.add(new BasicNameValuePair("QNA_CONTENT",qnabean.getQNA_CONTENT()));

					task = new Httptask();
					task.execute(params);
					
					finish();
				}
			});
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qa_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>
	{
		@Override //작업자 스레드 (메인 스레드와 분리.Thread 의 run()의 역활)
		protected String doInBackground(ArrayList<NameValuePair>... params) {
			
			String url = SessionControl.URL+"QaAdd.ad";
		
			get= new Http_Get(); 
			String result=get.httpget(url, params[0]);	
			//서버로 url요청을 보내고, 응답데이터를  String타입으로 리턴 
			//로그인 성공시 서버에서 "1"을 보냄 
		
		return result;
	}
		
		@Override //작업 스레드 완료 시 동작 하는 부분. doInBackground 수행 후 메인/UI Thread에 의해서 실행.
		protected void onPostExecute(String result)
		{    
			super.onPostExecute(result);
	
			if(result.equals("1")){ 
				Toast.makeText(User_Qna.this, "전송 완료" , Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(User_Qna.this, "전송 실패" , Toast.LENGTH_SHORT).show();
			}
       }
	}
}
