package user_Activity;

import http.SessionControl;

import org.apache.http.cookie.Cookie;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class My_Page extends ActionBarActivity {

	Button Mybook_btn, MyAnswer_btn, aaa;
	TextView UserId_tv; String Member_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
		setContentView(R.layout.my_page);
		
		ActivityManager.addActivity(this);
		
		 if(SessionControl.cookies!=null){
				for (int i = 0; i < SessionControl.cookies.size(); i++) {
					Cookie cookie = SessionControl.cookies.get(i);

					if (!cookie.getName().equals("JSESSIONID")) {
						Member_id = cookie.getName();
					}
				}
			}
		 UserId_tv = (TextView)findViewById(R.id.UserId_tv);
		UserId_tv.setText(Member_id);
		
		Mybook_btn = (Button)findViewById(R.id.my_book_btn);
		Mybook_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(My_Page.this, My_Book.class);
				startActivity(intent);
			}
		});
		
		MyAnswer_btn = (Button)findViewById(R.id.my_answer_btn);
		MyAnswer_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(My_Page.this, My_Answer.class);
				startActivity(intent);
			}
		});
		

	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_page, menu);
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
