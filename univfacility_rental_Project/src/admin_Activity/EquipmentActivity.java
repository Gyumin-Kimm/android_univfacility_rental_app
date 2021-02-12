package admin_Activity;

import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.EquipmentAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import bean.EquipBean;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EquipmentActivity extends ActionBarActivity {
	
	ListView MyList;
	EquipmentAdapter MyAdapter;
	Intent intent;
	Httptask task;
	ArrayList<NameValuePair> params;
	
	String member_id;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipment);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
		
		intent = getIntent();
		
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("BOOK_NUM", intent.getExtras().getString("BOOK_NUM")));
		
		task = new Httptask();
		task.execute(params);
	}

	class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>{
		JSONArray equip_array;
		JSONObject equip_object;
		Http_Get get;
		ArrayList<EquipBean> equip_list;
		EquipBean eb; 
		
		@Override 
		protected String doInBackground(ArrayList<NameValuePair>... params) {

			get= new Http_Get(); 
			String result=get.httpget(SessionControl.URL+"EquipAdminList.ad",params[0]);	

		return result;
		}
		
		@Override 
		protected void onPostExecute(String Jason)
		
		{    
			super.onPostExecute(Jason);
			 try {
				   equip_list = new ArrayList<EquipBean>();
				 
		        	equip_array = new JSONArray(Jason); 
     
		        	for(int i=0;i<equip_array.length();i++){
		        		
			             equip_object = equip_array.getJSONObject(i);
			             eb = new EquipBean();
	 
			             eb.setEQIP_CODE(equip_object.getString("EQIP_CODE"));
			             eb.setEQIP_NAME(equip_object.getString("EQIP_NAME"));
			             eb.setEQIP_TYPE(equip_object.getString("EQIP_TYPE"));
			          
			             equip_list.add(eb);	
		        	}
		        	
		        	 MyAdapter = new EquipmentAdapter(getApplicationContext(), R.layout.listitem_equipment, equip_list); 
		        	 MyList=(ListView)findViewById(R.id.equip_lv);
		        	 MyList.setAdapter(MyAdapter);	
		        }
			 			catch(JSONException e) {
		               Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();  
		        }
		} 
	}
}
