package admin_Activity;

import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.MemberAdapter;
import android.annotation.TargetApi;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import bean.MemberBean;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MemberActivity extends ActionBarActivity {

	ListView MyList;
	MemberAdapter MyAdapter;
	Intent intent;
	Httptask task;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
		setContentView(R.layout.activity_member);
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		task = new Httptask();
		task.execute(params);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.member, menu);
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
	
	class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>{
		JSONArray member_array;
		JSONObject member_object;
		Http_Get get;
		ArrayList<MemberBean> member_list;
		MemberBean mb; 
		
		@Override 
		protected String doInBackground(ArrayList<NameValuePair>... params) {

			get= new Http_Get(); 
			String result=get.httpget(SessionControl.URL+"MemberList.ad",params[0]);	
		
		
		return result;
		}
		
		@Override 
		protected void onPostExecute(String Jason)
		
		{    
			super.onPostExecute(Jason);
			 try {
				   member_list = new ArrayList<MemberBean>();
				 
		        	member_array = new JSONArray(Jason); 
		        	
		        	for (int i=0; i<member_array.length(); i++) {
			        	     
			             member_object = member_array.getJSONObject(i);
			             mb = new MemberBean();
	 
			             mb.setMEMBER_ID(member_object.getString("MEMBER_ID"));
			             mb.setMEMBER_NAME(member_object.getString("MEMBER_NAME"));
			          
			             member_list.add(mb);	 
		        	}
		        	
		        	 MyAdapter = new MemberAdapter(getApplicationContext(), R.layout.listitem_member, member_list); //fac_list瑜� 由ъ뒪�듃酉곗뿉 梨꾩� 
		        	 MyList=(ListView)findViewById(R.id.member_lv);
		        	 MyList.setAdapter(MyAdapter);
		        	 
		        	 MyList.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
							intent = new Intent(getApplicationContext(),MemberInfoActivity.class);
						intent.putExtra("MEMBER_ID",member_list.get(position).getMEMBER_ID().toString());
							
							MemberActivity.this.startActivity(intent);
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
