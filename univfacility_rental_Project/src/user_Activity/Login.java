package user_Activity;

import gcm.PreferenceUtil;
import http.Http_Get;
import http.SessionControl;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import admin_Activity.AdminActivity;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andproject.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Login extends Activity {
//gcm 

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String SENDER_ID = "435396826560";

	private GoogleCloudMessaging _gcm;
	private String _regId;

	EditText ID =null;
	EditText PW =null;
	Toast toast;
	Button login_btn; 
	Httptask task;
	Http_Get get ;
	Intent intent;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("1","aa");
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.login);
	    
	    ActivityManager.addActivity(this);
	  
	    getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59"))); 
	    
		  // google play service
		 if (checkPlayServices())
	      {  	
	         _gcm = GoogleCloudMessaging.getInstance(this);
	         _regId = getRegistrationId();
	 
	         if (TextUtils.isEmpty(_regId)){
	            registerInBackground(); }
	      }
	      else
	      {
	         Log.i("MainActivity.java | onCreate", "|No valid Google Play Services APK found.|");
	       
	      }
	 
	      // display received msg
	      String msg = getIntent().getStringExtra("msg");
	      if (!TextUtils.isEmpty(msg))
	      {}
		
		ID =(EditText)findViewById(R.id.MEMBER_ID);
		PW =(EditText)findViewById(R.id.MEMBER_PW);
		
		login_btn =(Button)findViewById(R.id.login_btn);
		login_btn.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
						
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("MEMBER_ID", ID.getText()
						.toString()));
				params.add(new BasicNameValuePair("MEMBER_PW", PW.getText()
						.toString()));
				
			      
				task = new Httptask();
				task.execute(params);
				
//					intent = new Intent(getApplicationContext(), MainActivity.class);
//					startActivity(intent);  

	          }
		 }); 
	} 
	
	@Override  //back 
	   public boolean onKeyDown(int keyCode, KeyEvent event) {
	       switch(keyCode) {
	         case KeyEvent.KEYCODE_BACK:
	          new AlertDialog.Builder(this)
	                          .setTitle("앱 종료")
	                          .setMessage("앱을 종료합니다")
	                          .setIcon(R.drawable.ic_launcher)
	                          .setPositiveButton("확인", new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog, int whichButton) {
	                               ActivityManager.finishedAllActivity();   	
	                           }
	                         })
	                         .setNegativeButton("취소", null).show();
	                         return false;
	          default:
	            return false;
	      }
	  }
	
	
	public void mOnClick_join(View v){
		Intent intent = new Intent(this, Join.class);
		startActivity(intent);
	}
	
	// Get the Login
	class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>
	{
		@Override 
		protected String doInBackground(ArrayList<NameValuePair>... params) {
			
			String url = SessionControl.URL+"Login.ad";
		
			get= new Http_Get(); 
			String result=get.httpget(url, params[0]);	
		
		SessionControl.cookies =SessionControl.httpclient.getCookieStore().getCookies();
		   
        ArrayList<NameValuePair> pparmas= new ArrayList<NameValuePair>();
        pparmas.add(new BasicNameValuePair("REG_ID",_regId));
        pparmas.add(new BasicNameValuePair("MEMBER_ID",ID.getText().toString()));
         get.httpget(SessionControl.URL+"SetRegId.ad", pparmas);
	
		return result;
	}
		
		@Override 
		protected void onPostExecute(String result)
		{    
			super.onPostExecute(result);
	
			if(result.equals("1")){ 
				SessionControl.cookies =SessionControl.httpclient.getCookieStore().getCookies();
				
				Intent intent = new Intent(Login.this, MainActivity.class);
				startActivity(intent);				
		}else{
			toast= Toast.makeText(Login.this, "로그인 실패" , Toast.LENGTH_SHORT);
			toast.show();
		}
       }
	}
	@Override
	   protected void onNewIntent(Intent intent)
	   {
	      super.onNewIntent(intent);
	  	Log.i("A","aa");
	      // display received msg
	      String msg = intent.getStringExtra("msg");
	      Log.i("MainActivity.java | onNewIntent", "|" + msg + "|");
	      if (!TextUtils.isEmpty(msg))
	      { }
	   }
	 
	   // google play service
	   private boolean checkPlayServices()
	   {
	      int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	      if (resultCode != ConnectionResult.SUCCESS)
	      {
	         if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
	         {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
	         }
	         else
	         {
	            Log.i("MainActivity.java | checkPlayService", "|This device is not supported.|");
	           
	            finish();
	         }
	         return false;
	      }
	      return true;
	   }
	 
	   // registration  id
	   private String getRegistrationId()
	   {
	      String registrationId = PreferenceUtil.instance(getApplicationContext()).regId();
	      if (TextUtils.isEmpty(registrationId))
	      {
	         Log.i("MainActivity.java | getRegistrationId", "|Registration not found.|");
	    
	         return "";
	      }
	      int registeredVersion = PreferenceUtil.instance(getApplicationContext()).appVersion();
	      int currentVersion = getAppVersion();
	      if (registeredVersion != currentVersion)
	      {
	         Log.i("MainActivity.java | getRegistrationId", "|App version changed.|");
	        
	         return "";
	      }
	      return registrationId;
	   }
	 
	   // app version
	   private int getAppVersion()
	   { 
	      try
	      {
	         PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
	         return packageInfo.versionCode;
	      }
	      catch (NameNotFoundException e)
	      {
	         // should never happen
	         throw new RuntimeException("Could not get package name: " + e);
	      }
	   }
	 
	   // gcm registration id
	   private void registerInBackground()
	   { 
	      new AsyncTask<Void, Void, String>()
	      {
	         @Override
	         protected String doInBackground(Void... params)
	         {
	            String msg = ""; 
	            try
	            {
	               if (_gcm == null){
	                  _gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
	               }
	               
	               _regId = _gcm.register(SENDER_ID);
	               msg = "Device registered, registration ID=" + _regId;
	          
	              
	               // For this demo: we don't need to send it because the device
	               // will send upstream messages to a server that echo back the
	               // message using the 'from' address in the message.
	               // Persist the regID - no need to register again.
	               storeRegistrationId(_regId);
	               
	              
	            }
	            catch (IOException ex)
	            {
	               msg = "Error :" + ex.getMessage();
	               // If there is an error, don't just keep trying to register.
	               // Require the user to click a button again, or perform
	               // exponential back-off.
	            }
	 
	            return msg;
	         }
	 
	         @Override
	         protected void onPostExecute(String msg)
	         {
	            Log.i("MainActivity.java | onPostExecute", "|" + msg + "|");
	        
	         }
	      }.execute(null, null, null);
	   }
	 
	   // registration id preference
	   private void storeRegistrationId(String regId)
	   {
	      int appVersion = getAppVersion();
	      Log.i("MainActivity.java | storeRegistrationId", "|" + "Saving regId on app version " + appVersion + "|");
	      PreferenceUtil.instance(getApplicationContext()).putRedId(regId);
	      PreferenceUtil.instance(getApplicationContext()).putAppVersion(appVersion);
	   }

}	 
