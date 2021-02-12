package user_Activity;

import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import bean.MemberBean;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Join extends Activity {
	
	ArrayAdapter<CharSequence> adspin;
	
	MemberBean mb ;
	 EditText ID;
	 EditText PW;
	 EditText NAME;
	 EditText STUNUM;
	 EditText EMAIL;
	 EditText PHONE;
	 Toast toast;
	 httptask task;
	 Http_Get get;
		Cookie cookie;
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.join);
	    
	    getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
	    
	    ID =(EditText)findViewById(R.id.MEMBER_ID);
	    PW =(EditText)findViewById(R.id.MEMBER_PW);
	    NAME =(EditText)findViewById(R.id.MEMBER_NAME);
	    STUNUM =(EditText)findViewById(R.id.MEMBER_STUNUM);
	    EMAIL =(EditText)findViewById(R.id.MEMBER_EMAIL);
	    PHONE =(EditText)findViewById(R.id.MEMBER_PHONE);
	    
	    Button button = (Button)findViewById(R.id.join_btn);
	    button.setOnClickListener(new OnClickListener() {
    
			@Override
			public void onClick(View v) {
					mb= new MemberBean();
					    	mb.setMEMBER_ID(ID.getText().toString());
					    	mb.setMEMBER_PW(PW.getText().toString());
					    	mb.setMEMBER_NAME(NAME.getText().toString());
					    	mb.setMEMBER_STUNUM(STUNUM.getText().toString());
					    	mb.setMEMBER_EMAIL(EMAIL.getText().toString());
					    	mb.setMEMBER_PHONE(PHONE.getText().toString());
				
					ArrayList<NameValuePair> params=new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("MEMBER_ID",mb.getMEMBER_ID()));
					params.add(new BasicNameValuePair("MEMBER_PW",mb.getMEMBER_PW()));
					params.add(new BasicNameValuePair("MEMBER_NAME",mb.getMEMBER_NAME()));
					params.add(new BasicNameValuePair("MEMBER_EMAIL",mb.getMEMBER_EMAIL()));
					params.add(new BasicNameValuePair("MEMBER_PHONE",mb.getMEMBER_PHONE()));
					params.add(new BasicNameValuePair("MEMBER_STUNUM",mb.getMEMBER_STUNUM()));
					
					task = new httptask();
					task.execute(params);
					
					finish();
					
			}	    
	});

}
	class httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>
	{
	    @Override
		protected String doInBackground(ArrayList<NameValuePair>... params) {
			
	    	String url = SessionControl.URL+"Join.ad";
				
	    	get= new Http_Get(); 
			String result= get.httpget(url, params[0]);

			return result;
		}
	
	@Override
	protected void onPostExecute(String result)
	{    
		super.onPostExecute(result);
		
		if(result.equals("1")){
			 toast= Toast.makeText(Join.this, "가입 완료", Toast.LENGTH_SHORT);
			 toast.show();
			}else{
			 toast= Toast.makeText(Join.this, "가입 실패", Toast.LENGTH_SHORT);
			 toast.show();
			}
	}	
	}
}

