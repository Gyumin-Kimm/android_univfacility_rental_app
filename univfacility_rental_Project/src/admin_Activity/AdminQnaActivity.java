package admin_Activity;

import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.AdminQnaAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import bean.QnaBean;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AdminQnaActivity extends ActionBarActivity {

	ListView MyList , reMyList;
	AdminQnaAdapter MyAdapter, reMyAdapter;
	Intent intent;
	Httptask task;
	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
		setContentView(R.layout.activity_admin_qna);
		
		
		task = new Httptask();
		task.execute(params);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_qna, menu);
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
	
	
	//Get the Qna List 
	class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>{
		JSONArray qna_array;
		JSONObject qna_object;
		Http_Get get;
		ArrayList<bean.QnaBean> qna_list1 , qna_list2;
		QnaBean qb; 
		
		@Override 
		protected String doInBackground(ArrayList<NameValuePair>... params) {

			get= new Http_Get(); 
			String result=get.httpget(SessionControl.URL+"QaList.ad",params[0]);	

		return result;
		}
		
		@Override
		protected void onPostExecute(String Jason)
		
		{    
			super.onPostExecute(Jason);
			 try {
				   qna_list1 = new ArrayList<bean.QnaBean>(); 
				   qna_list2 = new ArrayList<QnaBean>();
				 
		        	qna_array = new JSONArray(Jason); 
		        	
		        	for (int i=0; i<qna_array.length(); i++) {
		        	     
			             qna_object = qna_array.getJSONObject(i);  
			             qb = new QnaBean();
			   
			             qb.setQNA_NUM(String.valueOf(qna_object.getInt("QNA_NUM")));
			             qb.setQNA_ID(qna_object.getString("QNA_ID")); 
			             qb.setQNA_DATE(qna_object.getString("QNA_DATE"));
			             qb.setQNA_REPLY(qna_object.getString("QNA_REPLY"));
			             
			             if(qb.getQNA_REPLY().equals("NOCONTENT")){
			            	 qna_list1.add(qb);
			             }else{
			            	 qna_list2.add(qb);
			             }
		             
		             
		        	}
		        	
		        	 MyAdapter = new AdminQnaAdapter(getApplicationContext(), R.layout.listitem_adminqna, qna_list1); 
		        	 MyList=(ListView)findViewById(R.id.adminqna_lv);
		        	 MyList.setAdapter(MyAdapter);
		        	 
		        	 MyList.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
							intent = new Intent(getApplicationContext(),AdminQnaInfoActivity.class);
							intent.putExtra("QNA_NUM",qna_list1.get(position).getQNA_NUM().toString());
							
							AdminQnaActivity.this.startActivity(intent);
							finish();
						}
					});
		        	 
		        	 reMyAdapter = new AdminQnaAdapter(getApplicationContext(), R.layout.listitem_adminqna, qna_list2);  
		        	 reMyList=(ListView)findViewById(R.id.adminqna_re_lv);
		        	 reMyList.setAdapter(reMyAdapter);
		        	 
		        	 reMyList.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
							intent = new Intent(getApplicationContext(),AdminQnaInfoActivity.class);
							intent.putExtra("QNA_NUM",qna_list2.get(position).getQNA_NUM().toString());
							
							AdminQnaActivity.this.startActivity(intent);
							finish();
						}
		        		 
					});
		        }
			 			catch(JSONException e) {
		               Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();  
		        }
		} 
	}
}
