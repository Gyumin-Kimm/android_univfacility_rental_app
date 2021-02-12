package user_Activity;


import http.SessionControl;

import org.apache.http.cookie.Cookie;

import admin_Activity.AdminActivity;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends ActionBarActivity {
	Cookie cookie=null;
	Intent intent=null;
	String Member_id="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActivityManager.addActivity(this);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
	
		if (SessionControl.cookies != null) { ////�α��� ���̸�
				for (int i = 0; i < SessionControl.cookies.size(); i++){
					cookie = SessionControl.cookies.get(i);	
					if (!cookie.getName().equals("JSESSIONID")) {
						Member_id = cookie.getName();
					}
				}
				
				if(Member_id.equals("")){			
					intent = new Intent(this, Login.class);
					this.startActivity(intent); // �α��� �������� �̵�
				}else{
					if (Member_id.equals("admin")){ // ���̵� admin�̸�
						intent = new Intent(this, AdminActivity.class);
						this.startActivity(intent); // ������ �������� �̵�
					}else{ // ���̵� admin�� �ƴϸ�
						intent = new Intent(this, User_Main.class);
						this.startActivity(intent); // ���� �������� �̵�
					}
				}
		}else{			
			intent = new Intent(this, Login.class);
			this.startActivity(intent); // �α��� �������� �̵�	
		}
	}
		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
}
