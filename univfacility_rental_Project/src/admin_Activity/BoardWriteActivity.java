package admin_Activity;

import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;

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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BoardWriteActivity extends ActionBarActivity {

	Button btnsubmit;
	EditText subject, content;
	TextView ID;
	Http_Get get ;
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
		setContentView(R.layout.activity_board_write);
		
		subject = (EditText)findViewById(R.id.boardwrite_et_subject);
		content = (EditText)findViewById(R.id.boardwrite_et_content);
		ID = (TextView)findViewById(R.id.boardwriter_tv_writer);
		
		if(SessionControl.cookies!=null){
			for (int i = 0; i < SessionControl.cookies.size(); i++) {
				Cookie cookie = SessionControl.cookies.get(i);

				if (!cookie.getName().equals("JSESSIONID")) {
					ID.setText(cookie.getName());
				}
			}
		}
		
		btnsubmit = (Button)findViewById(R.id.boardwrite_btn_submit);
		btnsubmit.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
			public void onClick(View v) {
				
				ArrayList<NameValuePair> params=new ArrayList<NameValuePair>();
				Httptask task=new Httptask();
				params.add(new BasicNameValuePair("BOARD_ID",ID.getText().toString()));
				params.add(new BasicNameValuePair("BOARD_SUBJECT",subject.getText().toString()));
				params.add(new BasicNameValuePair("BOARD_CONTENT",content.getText().toString()));

				task.execute(params);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.board_write, menu);
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
	
	
	class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>
	   {
	      @Override 
	      protected String doInBackground(ArrayList<NameValuePair>... params) {
	         
	         String url = SessionControl.URL+"BoardAdd.ad";
	      
	         get= new Http_Get(); 
	         String result=get.httpget(url, params[0]);   
	  	   
	      return result;
	   }
	      
	      @Override 
	      protected void onPostExecute(String result)
	      {    
	         super.onPostExecute(result);
	         
	         if(result.equals("1")){ 
	        	 intent = new Intent(getApplicationContext(),BoardActivity.class);
	        	 startActivity(intent);
	            finish();           
	         }else{
		         Toast.makeText(getApplicationContext(), "게시물 등록 완료" , Toast.LENGTH_SHORT).show();
	         }
	       
	      }
	   }
}
