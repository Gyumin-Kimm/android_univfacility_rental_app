package admin_Activity;

import facility.Admin_Fac_Book;
import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.ApprovalAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ApprovalActivity extends ActionBarActivity {

	ApprovalAdapter MyAdapter;
    ListView MyList;
    Button approval_btn_choosedate, approval_btn_search;
    TextView date_tv;
    DatePickerDialog datePickerDialog;
    String format_string;
    Intent intent;
    Calendar dateAndtime = Calendar.getInstance();

    static final int DATE_DIALOG_ID = 1;
    
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			dateAndtime.set(Calendar.YEAR, year);
			dateAndtime.set(Calendar.MONTH, monthOfYear);
			dateAndtime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateLabel();	
		}
	};
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
		setContentView(R.layout.activity_approval);

        date_tv = (TextView)findViewById(R.id.approval_tv_date);
  
        approval_btn_choosedate = (Button)findViewById(R.id.approval_btn_choosedate);
        approval_btn_choosedate.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(final View v) {
  		
        		new DatePickerDialog(ApprovalActivity.this, d, dateAndtime.get(Calendar.YEAR), 
                        dateAndtime.get(Calendar.MONTH),
                        dateAndtime.get(Calendar.DAY_OF_MONTH)).show();

        	}
        });
        
        // 조회 및 갱신 버튼
        approval_btn_search = (Button)findViewById(R.id.approval_btn_search);
        approval_btn_search.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("BOOK_DAY", date_tv.getText().toString()));
				
		        Httptask task = new Httptask();
		        task.execute(params);	
			}
		});  
	}
	
	// 날짜 포맷
	private void updateLabel(){
		format_string = String.format("%d-%d-%d", dateAndtime.get(Calendar.YEAR),
													dateAndtime.get(Calendar.MONTH)+1,
													dateAndtime.get(Calendar.DAY_OF_MONTH));
		
		date_tv.setText(format_string);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.approval, menu);
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
	class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>{
		JSONArray approval_array;
		JSONObject approval_object;
		Http_Get get;
		ArrayList<Admin_Fac_Book> approval_list;
		Admin_Fac_Book fb; 
		
		@Override 
		protected String doInBackground(ArrayList<NameValuePair>... params) {

			get= new Http_Get(); 
			String result=get.httpget(SessionControl.URL+"FacBookDetailsall.ad",params[0]);	
		
		return result;
		}
		
		@Override 
		protected void onPostExecute(String Jason)
		
		{    
			super.onPostExecute(Jason);
			 try {
				   approval_list = new ArrayList<Admin_Fac_Book>(); 
				 
				   approval_array = new JSONArray(Jason); 
		        	
		        	for (int i=0; i<approval_array.length(); i++) {
		        	     
			        	approval_object = approval_array.getJSONObject(i); 
			            fb = new Admin_Fac_Book();
			   
			             fb.setMEMBER_ID(approval_object.getString("MEMBER_ID")); 
			             fb.setFAC_CODE(approval_object.getString("FAC_CODE"));
			             fb.setBOOK_DAY(approval_object.getString("BOOK_DAY"));
			             fb.setBOOK_AGREE(approval_object.getString("BOOK_AGREE"));
			             fb.setBOOK_NUM(String.valueOf(approval_object.getInt("BOOK_NUM")));

		             approval_list.add(fb);	 
		        	}
		        	 
		        	 MyAdapter = new ApprovalAdapter(getApplicationContext(), R.layout.listitem_approval, approval_list);
		             MyList = (ListView)findViewById(R.id.approval_lv);
		        	 MyList.setAdapter(MyAdapter);
		        	 
		        	 MyList.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
							intent = new Intent(getApplicationContext(),ApprovalInfoActivity.class);
							intent.putExtra("BOOK_DAY",approval_list.get(position).getBOOK_DAY().toString());
							intent.putExtra("BOOK_NUM",approval_list.get(position).getBOOK_NUM().toString());
							
							ApprovalActivity.this.startActivity(intent);
						}
					});
		        }
			 			catch(JSONException e) {
		               Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();  
		        }
		} 
	}
}
