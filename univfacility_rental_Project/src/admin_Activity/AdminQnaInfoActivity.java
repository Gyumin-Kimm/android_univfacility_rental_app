package admin_Activity;

import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
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
public class AdminQnaInfoActivity extends ActionBarActivity {

	Intent intent;
	Httptask task;
	Httptask1 task1;
	ArrayList<NameValuePair> params;
	TextView qnum, qid, qdate, qcontent;
	EditText answer;
	Button submit;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
		setContentView(R.layout.activity_admin_qna_info);
		
		intent = getIntent();
		
		params = new ArrayList<NameValuePair>();
		task = new Httptask();
		params.add(new BasicNameValuePair("QNA_NUM", intent.getExtras().getString("QNA_NUM")));
		
		task.execute(params);
		
		//onclick sumbit btn
		submit = (Button)findViewById(R.id.qna_info_btn);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				
				params = new ArrayList<NameValuePair>();
				task1 = new Httptask1();
				
				params.add(new BasicNameValuePair("QNA_NUM", qnum.getText().toString()));
				params.add(new BasicNameValuePair("QNA_REPLY", answer.getText().toString()));
				
				task1.execute(params);
				
				intent = new Intent(getApplicationContext(),AdminQnaActivity.class);
				startActivity(intent);
				finish();
				
			}
		});	
	}
	@Override  //back 버튼을 눌렀을 때 호출 
	   public boolean onKeyDown(int keyCode, KeyEvent event) {
	       switch(keyCode) {
	         case KeyEvent.KEYCODE_BACK:
	        	 startActivity(new Intent(getApplicationContext(),AdminQnaActivity.class));
	        	 finish();
	         
	          default:
	            return false;
	      }
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.admin_qna_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// Get the Qna Jason
	class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>{
		JSONArray qna_array;
		JSONObject qna_object;
		Http_Get get;
		QnaBean qb; 
		
		@Override 
		protected String doInBackground(ArrayList<NameValuePair>... params) {

			get= new Http_Get(); 
			String result=get.httpget(SessionControl.URL+"QaGet.ad",params[0]);	

		return result;
		}
		
		@Override 
		protected void onPostExecute(String Jason)
		
		{    
			super.onPostExecute(Jason);
			 try {
				 
				   qna_array = new JSONArray(Jason); 
		        	     
		             qna_object = qna_array.getJSONObject(0); 
		             qb = new QnaBean();
		   
		             qb.setQNA_NUM(String.valueOf(qna_object.getInt("QNA_NUM")));
		             qb.setQNA_ID(qna_object.getString("QNA_ID")); 
		             qb.setQNA_DATE(qna_object.getString("QNA_DATE"));
		             qb.setQNA_CONTENT(qna_object.getString("QNA_CONTENT"));
		             qb.setQNA_REPLY(qna_object.getString("QNA_REPLY")); 
		             
		             qnum = (TextView)findViewById(R.id.qna_info_num);
		     		 qid = (TextView)findViewById(R.id.qna_info_id);
		     		 qdate = (TextView)findViewById(R.id.qna_info_date);
		     		 qcontent = (TextView)findViewById(R.id.qna_info_content);
		     		 answer = (EditText)findViewById(R.id.qna_info_answer);
		     		 
		     		 if(!qb.getQNA_REPLY().equals("NOCONTENT")){
		     			 answer.setEnabled(false);
		     			 submit.setEnabled(false);
		     		 }
		     		 
		     		 qnum.setText(qb.getQNA_NUM());
		     		 qid.setText(qb.getQNA_ID());
		     		 qdate.setText(qb.getQNA_DATE());
		     		 qcontent.setText(qb.getQNA_CONTENT());
		     		 answer.setText(qb.getQNA_REPLY());
	 	
		        }
			 			catch(JSONException e) {
		               Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();  
		        }
		} 
	}
	
	//After reply, send to server
	class Httptask1 extends AsyncTask<ArrayList<NameValuePair>,Void,String>{
		Http_Get get;
		
		@Override 
		protected String doInBackground(ArrayList<NameValuePair>... params) {

			get= new Http_Get(); 
			String result=get.httpget(SessionControl.URL+"QaReply.ad",params[0]);	

		return result;
		}
	}
}
