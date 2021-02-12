package admin_Activity;

import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import user_Activity.MainActivity;
import admin_Activity.AdminActivity.Httptask1;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import bean.MemberBean;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MemberInfoActivity extends ActionBarActivity {

	TextView id, pw, name, email, phone, stunum; 
	Intent intent;
	Httptask task = new Httptask();
	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();;
	
	static int var = 1;	//서버 주소 받아오기 위한 변수

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
		setContentView(R.layout.activity_member_info);
		
		intent = getIntent();
		var = 1;

		params.add(new BasicNameValuePair("MEMBER_ID", intent.getExtras().getString("MEMBER_ID")));
		
		id = (TextView)findViewById(R.id.member_info_id);
		pw = (TextView)findViewById(R.id.member_info_pw);
		name = (TextView)findViewById(R.id.member_info_name);
		email = (TextView)findViewById(R.id.member_info_email);
		phone = (TextView)findViewById(R.id.member_info_phone);
		stunum = (TextView)findViewById(R.id.member_info_stunum);
		
		task.execute(params);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.member_info, menu);
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
		}else if (id == R.id.action_discard){
			var = 0;
			
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setTitle("삭제 확인")        
			.setMessage("회원 정보를 삭제 하시겠습니까?")       
			.setCancelable(false)       
			.setPositiveButton("확인", new DialogInterface.OnClickListener(){
				@SuppressWarnings("unchecked")
				public void onClick(DialogInterface dialog, int whichButton){
					task.cancel(true);
					task = new Httptask();
	
					params.add(new BasicNameValuePair("MEMBER_ID", intent.getExtras().getString("MEMBER_ID")));
					task.execute(params);
					
		            intent =  new Intent(getApplicationContext(),MemberActivity.class);
		            startActivity(intent);
		            finish();
				}
			})
			.setNegativeButton("취소", new DialogInterface.OnClickListener(){ 
				public void onClick(DialogInterface dialog, int whichButton){
					dialog.cancel();
				}
			});

			AlertDialog dialog = builder.create();   
			dialog.show();    

            return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override  //back 버튼을 눌렀을 때 호출 
	   public boolean onKeyDown(int keyCode, KeyEvent event) {
	       switch(keyCode) {
	         case KeyEvent.KEYCODE_BACK:
	        	 intent = new Intent(getApplicationContext(),MemberActivity.class);
	        	 startActivity(intent);
	        	 finish();
                 return false;
	          default:
	            return false;
	      }
	  }
	
	class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>{
		JSONArray member_array;
		JSONObject member_object;
		Http_Get get;
		MemberBean mb; 
		
		@Override
		protected String doInBackground(ArrayList<NameValuePair>... params) {
		
			get= new Http_Get(); 
			String result;
			if(var == 1){
				result =get.httpget(SessionControl.URL+"MemberView.ad",params[0]);	
			}else{
				result =get.httpget(SessionControl.URL+"MemberDelete.ad",params[0]);
			}

		return result;
		}
		
		@Override 
		protected void onPostExecute(String Jason)
		
		{    
			super.onPostExecute(Jason);
			 try {
				 
		        	member_array = new JSONArray(Jason); 
    	     
		             member_object = member_array.getJSONObject(0);
		             mb = new MemberBean();
 
		             mb.setMEMBER_ID(member_object.getString("MEMBER_ID"));
		             mb.setMEMBER_PW(member_object.getString("MEMBER_PW"));
		             mb.setMEMBER_NAME(member_object.getString("MEMBER_NAME"));
		             mb.setMEMBER_EMAIL(member_object.getString("MEMBER_EMAIL"));
		             mb.setMEMBER_PHONE(member_object.getString("MEMBER_PHONE"));
		             mb.setMEMBER_STUNUM(member_object.getString("MEMBER_STUNUM"));
		             
		             id.setText(mb.getMEMBER_ID());
		             pw.setText(mb.getMEMBER_PW());
		             name.setText(mb.getMEMBER_NAME());
		             email.setText(mb.getMEMBER_EMAIL());
		             phone.setText(mb.getMEMBER_PHONE());
		             stunum.setText(mb.getMEMBER_STUNUM());
		        }
			 			catch(JSONException e) {
			 				if(var == 1){
			 					 Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
			 				}else{
			 					Toast.makeText(getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
			 				}    
		        }
		} 
	}
}
