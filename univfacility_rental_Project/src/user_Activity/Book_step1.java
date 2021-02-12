package user_Activity;


import facility.Fac_Details;
import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import user_Activity.Login.Httptask;


import com.example.andproject.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Book_step1 extends Activity {
    ArrayList<Fac_Details> fac_list;//
	Fac_Details fac;//
	
	ArrayAdapter<CharSequence> adspin;
	Spinner spin;
	
	ArrayList<Fac_Details> arItem;
	Fac_Details fd;
	
	Button btn;
	
	ListView MyList;
	BookStep1_Adapter MyAdapter;
	
	Intent intent;
	
	Httptask task;
	Http_Get get;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.book_step1);
	    getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59"))); 
	    
	    spin = (Spinner)findViewById(R.id.spinner1);
	    
	    adspin = ArrayAdapter.createFromResource(this, R.array.buildings,
			     android.R.layout.simple_spinner_item);
	    adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	   
	    spin.setAdapter(adspin);
	   
	    
		btn = (Button) findViewById(R.id.board_btn);
		btn.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				  ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				  params.add(new BasicNameValuePair("FAC_MAIN", spin.getSelectedItem().toString()));

				  task = new Httptask();
				  task.execute(params);
			}
		});
	  }
	

	class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>{   
		JSONArray fac_array;
		JSONObject fac_object;
		ArrayList<Fac_Details> fac_list;
		Fac_Details fac;
		BookStep1_Adapter adapter;
		
		@Override
		protected void onPreExecute() {
			 super.onPreExecute();
		}
	
		@Override 
		protected String doInBackground(ArrayList<NameValuePair>... params) {
			
			String url = SessionControl.URL+"FacDetailsList.ad";
		
			get= new Http_Get(); 
			String result=get.httpget(url, params[0]);	
			
			SessionControl.cookies =SessionControl.httpclient.getCookieStore().getCookies();
			
			Cookie cookie;
			
			if(!SessionControl.cookies.isEmpty()){
			for(int i=0; i<SessionControl.cookies.size();i++)
			{
				cookie=SessionControl.cookies.get(i);
				
				
				 String MemberId =   cookie.getValue()  ;
				 String path =   cookie.getPath()  ;
				 String name =   cookie.getName()  ;
				  
				 Log.e("cookie1", MemberId);
				 Log.e("cookie2", path);
				 Log.e("cookie3", name);
				 
			}}
		return result;
	}
		
		@Override 
		protected void onPostExecute(String Jason)

		{    
			super.onPostExecute(Jason);
			 try {
				   fac_list = new ArrayList<Fac_Details>();
				   
				   fac_array = new JSONArray(Jason); 
		           fac_object = new JSONObject();
		        	
		        	for (int i=0; i<fac_array.length(); i++) {
		        	     
		             fac_object = fac_array.getJSONObject(i);  
		             
		             fac = new Fac_Details();

		             fac.setFAC_CODE(fac_object.getString("FAC_CODE")); 
		             fac.setFAC_NAME(fac_object.getString("FAC_NAME")); 
		             fac.setFAC_MAX(fac_object.getString("FAC_MAX"));
	                 fac.setFAC_MODE("독점");
		          
		             fac_list.add(fac);	
		        	}
		        	
		        	 MyAdapter = new BookStep1_Adapter(Book_step1.this, R.layout.book_step1_list, fac_list);  
		        	 MyList=(ListView)findViewById(R.id.listView1);
		        	 MyList.setAdapter(MyAdapter);
		        	 
		        	 MyList.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parentView,
									View view, int position, long id) {
							intent = new Intent(Book_step1.this, Book_step2.class);
							intent.putExtra("Building_name", spin.getSelectedItem().toString());
							intent.putExtra("Facility_code", MyAdapter.txt1.getText().toString());
							intent.putExtra("Facility_name", MyAdapter.txt2.getText().toString());
							Book_step1.this.startActivity(intent);
		               
							}
						});	 
		        }
			 			catch(JSONException e) {
		               Toast.makeText(Book_step1.this,e.getMessage(),Toast.LENGTH_SHORT).show();  
		        }
		} 
	}
	
	
}

class BookStep1_Adapter extends BaseAdapter {
	Context maincon;
	LayoutInflater Inflater;
	ArrayList<Fac_Details> fac_list;
	int layout;
	TextView txt1;
	TextView txt2;
	TextView txt3;
	TextView txt4;
	

	public BookStep1_Adapter(Context context, int alayout, ArrayList<Fac_Details> aarSrc) {
		maincon = context;
		Inflater = (LayoutInflater)context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		fac_list = aarSrc;
		layout = alayout;
	}

	public int getCount() { 
		return fac_list.size();
	}

	public String getItem(int position) {
		return fac_list.get(position).getFAC_CODE();
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
	
		if (convertView == null) {
			convertView = Inflater.inflate(layout, parent, false);
		}  
		txt1 = (TextView)convertView.findViewById(R.id.textView1);
		txt1.setText(fac_list.get(position).getFAC_CODE());

		txt2 = (TextView)convertView.findViewById(R.id.textView2);
		txt2.setText(fac_list.get(position).getFAC_NAME()); 
		
		txt3 = (TextView)convertView.findViewById(R.id.textView3);
		txt3.setText(fac_list.get(position).getFAC_MAX()); 
		
		txt4 = (TextView)convertView.findViewById(R.id.textView4); 
		txt4.setText(fac_list.get(position).getFAC_MODE()); 
		
		return convertView;
	} }


