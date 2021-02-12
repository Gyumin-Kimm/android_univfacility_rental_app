package admin_Activity;

import com.example.andproject.R;

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

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ManageActivity extends ActionBarActivity {

	Button member, qna;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
		setContentView(R.layout.activity_manage);
		
		member = (Button)findViewById(R.id.manage_member);
		qna = (Button)findViewById(R.id.manage_qna);
		
		member.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(getApplicationContext(),MemberActivity.class);
				startActivity(intent);
			}
		});
		
		qna.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(getApplicationContext(),AdminQnaActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage, menu);
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
