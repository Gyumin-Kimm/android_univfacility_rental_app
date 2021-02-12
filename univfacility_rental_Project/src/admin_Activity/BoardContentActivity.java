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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import bean.BoardBean;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BoardContentActivity extends ActionBarActivity {

	TextView writer, subject, content;
	Intent intent;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
		setContentView(R.layout.activity_board_content);
		
		intent = getIntent();
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		Httptask task = new Httptask();
		params.add(new BasicNameValuePair("BOARD_NUM", intent.getExtras().getString("BOARD_NUM")));

		writer = (TextView)findViewById(R.id.boardcontent_tv_writer);
		subject = (TextView)findViewById(R.id.boardcontent_tv_subject);
		content = (TextView)findViewById(R.id.boardcontent_tv_content);
		
		task.execute(params);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.board_content, menu);
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
		JSONArray board_array;
		JSONObject board_object;
		Http_Get get;
		BoardBean bb; 
		
		@Override 
		protected String doInBackground(ArrayList<NameValuePair>... params) {

			get= new Http_Get(); 
			String result=get.httpget(SessionControl.URL+"BoardDetailsGet.ad",params[0]);	

		return result;
		}
		
		@Override 
		protected void onPostExecute(String Jason)
		
		{    
			super.onPostExecute(Jason);
			if(Jason.equals("-1")){
				Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT);
				finish();
			}else{
				
				try {
			        	board_array = new JSONArray(Jason); 
 
			             board_object = board_array.getJSONObject(0); 
			             bb = new BoardBean();
	
			             bb.setBOARD_CONTENT(board_object.getString("BOARD_CONTENT")); 
			             bb.setBOARD_SUBJECT(board_object.getString("BOARD_SUBJECT"));
			             bb.setBOARD_ID(board_object.getString("BOARD_ID"));
			             
			             writer.setText(bb.getBOARD_ID());
			             subject.setText(bb.getBOARD_SUBJECT());
			             content.setText(bb.getBOARD_CONTENT());
	
			        }
				 			catch(JSONException e) {
			               Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();  
				 			}
			
			}
		} 
	}
}
