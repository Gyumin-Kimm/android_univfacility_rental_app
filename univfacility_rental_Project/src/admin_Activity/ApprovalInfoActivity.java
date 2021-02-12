package admin_Activity;

import facility.Admin_Fac_Book;
import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.andproject.R;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ApprovalInfoActivity extends ActionBarActivity {

	final CharSequence[] items = {"승인", "보류", "거절"};
	Button state, equip;
	TextView date, id, phone, reason, code, members;
	Intent intent;
	ArrayList<NameValuePair> params;
	Httptask task;
	
	static int var = 0;// == 0 approval info
					   // == 1 state change

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
		setContentView(R.layout.activity_approval_info);

		intent = getIntent();
		
		final String book_num = intent.getExtras().getString("BOOK_NUM");
		var = 0;
		
		date = (TextView)findViewById(R.id.approval_info_date);
		id = (TextView)findViewById(R.id.approval_info_memberid);
		phone = (TextView)findViewById(R.id.approval_info_phone);
		reason = (TextView)findViewById(R.id.approval_info_reason);
		members = (TextView)findViewById(R.id.approval_info_members);
		code = (TextView)findViewById(R.id.approval_info_code);
		
		params = new ArrayList<NameValuePair>();
		task = new Httptask();

		params.add(new BasicNameValuePair("BOOK_NUM", book_num));
		task.execute(params);

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);   
		
		state= (Button)findViewById(R.id.approval_info_btn);
		state.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder
					.setTitle("승인 상태 선택")        
					.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int index){
							
							task.cancel(true);
							
							state.setText(items[index]);
							var = 1;
							
							params = new ArrayList<NameValuePair>();
							task = new Httptask();
							params.add(new BasicNameValuePair("BOOK_AGREE", state.getText().toString()));
							params.add(new BasicNameValuePair("BOOK_NUM", book_num));
							task.execute(params);
							
							dialog.dismiss();
							finish();
						}
					})
					.show();
			}
		});
		
		equip = (Button)findViewById(R.id.approval_info_equip);
		equip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(getApplicationContext(),EquipmentActivity.class);
				intent.putExtra("BOOK_NUM", book_num);
				
				startActivity(intent);
			}
		});
	}


	class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>{
		JSONArray approval_array;
		JSONObject approval_object;
		Http_Get get;
		Admin_Fac_Book fb; 
		
		@Override 
		protected String doInBackground(ArrayList<NameValuePair>... params) {

			get= new Http_Get(); 
			String result;
			if(var == 0){
				result =get.httpget(SessionControl.URL+"FacBookDetails.ad",params[0]);	
			}else{
				result =get.httpget(SessionControl.URL+"FacBookUpdate.ad",params[0]);
			}
			
		return result;
		}
		
		@Override 
		protected void onPostExecute(String Jason)
		
		{    
			super.onPostExecute(Jason);
			 try {
				   approval_array = new JSONArray(Jason); 

				   approval_object = approval_array.getJSONObject(0); 
		            fb = new Admin_Fac_Book();
		             fb.setMEMBER_ID(approval_object.getString("MEMBER_ID")); 
		             fb.setFAC_CODE(approval_object.getString("FAC_CODE"));
		             fb.setBOOK_DAY(approval_object.getString("BOOK_DAY"));
		             fb.setBOOK_AGREE(approval_object.getString("BOOK_AGREE"));
		             fb.setBOOK_REASON(approval_object.getString("BOOK_REASON"));
		             fb.setMEMBER_PHONE(approval_object.getString("MEMBER_PHONE"));
		             fb.setBOOK_MEMBER(approval_object.getString("BOOK_MEMBER"));
		             fb.setBOOK_TIME1(approval_object.getString("BOOK_TIME1"));
		             fb.setBOOK_TIME2(approval_object.getString("BOOK_TIME2"));

		     		String format_string = String.format("%s, %s ~ %s", 
		     				fb.getBOOK_DAY(),fb.getBOOK_TIME1(),fb.getBOOK_TIME2());
		             
		             date.setText(format_string);
		             phone.setText(fb.getMEMBER_PHONE());
		             reason.setText(fb.getBOOK_REASON());
		             code.setText(fb.getFAC_CODE());
		             members.setText(fb.getBOOK_MEMBER());
		             id.setText(fb.getMEMBER_ID());
		             state.setText(fb.getBOOK_AGREE());   

		        }
			 			catch(JSONException e) {
			 				if(var == 0){
			 					 Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
			 				}else{
			 					Toast.makeText(getApplicationContext(), "상태 변경 완료.", Toast.LENGTH_SHORT).show();
			 				}
		        }
		} 
	}
}
