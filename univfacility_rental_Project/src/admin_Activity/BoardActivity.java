package admin_Activity;

import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.BoardAdapter;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import bean.BoardBean;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BoardActivity extends ActionBarActivity {

	ListView MyList;
	Button btnwrite;
	BoardAdapter MyAdapter;
	Intent intent;
	BoardBean Bb;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
		setContentView(R.layout.activity_board);
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		Httptask task = new Httptask();
		task.execute(params);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.board, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}else if (id == R.id.action_edit)//action bar 湲��벐湲� 踰꾪듉 
		{
            Intent intent = new Intent(getApplicationContext(), BoardWriteActivity.class);
            startActivity(intent);
            finish();
            return true;
	
		}
		return super.onOptionsItemSelected(item);
	}

	class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>{
		JSONArray board_array;
		JSONObject board_object;
		Http_Get get;
		ArrayList<BoardBean> board_list;
		BoardBean bb; 
		
		@Override 
		protected String doInBackground(ArrayList<NameValuePair>... params) {

			get= new Http_Get(); 
			String result=get.httpget(SessionControl.URL+"BoardListGet.ad",params[0]);	

		return result;
		}
		
		@Override 
		protected void onPostExecute(String Jason)
		
		{    
			super.onPostExecute(Jason);
			 try {
				   board_list = new ArrayList<BoardBean>(); 
				 
		        	board_array = new JSONArray(Jason); 
		        	
		        	for (int i=0; i<board_array.length(); i++) {
		        	     
		             board_object = board_array.getJSONObject(i);  
		             bb = new BoardBean();
		   
		             bb.setBOARD_NUM(String.valueOf(board_object.getInt("BOARD_NUM")));
		             bb.setBOARD_READCOUNT(String.valueOf(board_object.getString("BOARD_READCOUNT"))); 
		             bb.setBOARD_SUBJECT(board_object.getString("BOARD_SUBJECT"));
		             bb.setBOARD_DATE(board_object.getString("BOARD_DATE"));
		             bb.setBOARD_ID(board_object.getString("BOARD_ID"));
		          
		             board_list.add(bb);	
		        	}
		        	
		        	 MyAdapter = new BoardAdapter(getApplicationContext(), R.layout.listitem_board, board_list); 
		        	 MyList=(ListView)findViewById(R.id.board_lv);
		        	 MyList.setAdapter(MyAdapter);
		        	 
		        	 MyList.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
							intent = new Intent(getApplicationContext(),BoardContentActivity.class);
							intent.putExtra("BOARD_NUM",board_list.get(position).getBOARD_NUM().toString());
							
							BoardActivity.this.startActivity(intent);
						}
					});
		        }
			 			catch(JSONException e) {
		               Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();  
		        }
		} 
	}
}
