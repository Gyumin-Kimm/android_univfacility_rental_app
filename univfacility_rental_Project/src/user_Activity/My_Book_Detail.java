package user_Activity;

import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andproject.R;

public class My_Book_Detail extends ActionBarActivity {
   
   Httptask task;
   Http_Get get;
   Intent intent; 
   String Member_id,Book_num; 
   TextView BD_name,FC_code,FC_name,User_id,Book_state,
            Book_day,Book_time,Book_reason,Book_member,Member_phone;
   Cookie cookie; 
   ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
   
    static int var = 1;
    
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.my_book_detail);
      
      intent = getIntent();
       Book_num = intent.getExtras().getString("Book_num").toString();
   
       if (SessionControl.cookies != null) {
         for (int i = 0; i < SessionControl.cookies.size(); i++) {

            cookie = SessionControl.cookies.get(i);

            if (!cookie.getName().equals("JSESSIONID")) {
               Member_id = cookie.getName();
            }
         }
      }
       
       User_id = (TextView)findViewById(R.id.member_id);
      User_id.setText(Member_id);
      ////쿠키로 로그인 아이디 받아와서 TextView에 채움.(대여자)
   
      BD_name = (TextView)findViewById(R.id.building_name);
      FC_code = (TextView)findViewById(R.id.fac_code);
      FC_name = (TextView)findViewById(R.id.fac_name);
      Book_state = (TextView) findViewById(R.id.book_state);
        
      Book_day = (TextView)findViewById(R.id.book_day);
       Book_time = (TextView)findViewById(R.id.book_time);
       Book_reason =(TextView)findViewById(R.id.book_reason);
       Book_member =(TextView)findViewById(R.id.book_member);
       Member_phone =(TextView)findViewById(R.id.member_phone);
       /////서버에서 데이터를 받아 채울 TextView들을 초기화해줌.     
       
       ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("BOOK_NUM", Book_num));
      
      
      task = new Httptask();
      task.execute(params);
      
   }
      @Override
      public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.my__book__detail, menu);
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
         }else if(id == R.id.action_discard) {
            var = 0;
            
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            
            builder.setTitle("삭제 확인")        
            .setMessage("신청 내역을 삭제 하시겠습니까?")       
            .setCancelable(false)       
            .setPositiveButton("확인", new DialogInterface.OnClickListener(){
               @SuppressWarnings("unchecked")
               public void onClick(DialogInterface dialog, int whichButton){
                  task.cancel(true);
                  task = new Httptask();
      
                  params.add(new BasicNameValuePair("BOOK_NUM", Book_num));
                  task.execute(params);
                  
                     intent =  new Intent(getApplicationContext(),My_Page.class);
                     startActivity(intent);
                     finish();
               }
            })
            .setNegativeButton("취소", new DialogInterface.OnClickListener(){ 
               public void onClick(DialogInterface dialog, int whichButton){
                  dialog.cancel();
               }
            });

            AlertDialog dialog = builder.create();   
            dialog.show();    

            return true;
         }
         return super.onOptionsItemSelected(item);
      }
      
   class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>
   {   JSONArray my_book_array;
      JSONObject my_book_object;
      
       @Override //작업자 스레드 (메인 스레드와 분리.Thread 의 run()의 역활)
         protected String doInBackground(ArrayList<NameValuePair>... params) {

            get= new Http_Get(); 
            String result;
            if(var == 1){
               result =get.httpget(SessionControl.URL+"FacBookUserDetails.ad",params[0]);   
            }else{
               result =get.httpget(SessionControl.URL+"FacBookDelete.ad",params[0]);
            }
            
            return result;
         }
         
      @Override
      protected void onPostExecute(String Jason) {
         super.onPostExecute(Jason);
         try {

            my_book_array = new JSONArray(Jason); 
            my_book_object = new JSONObject();

            for (int i = 0; i < my_book_array.length(); i++) {
               my_book_object = my_book_array.getJSONObject(i); 
               
               BD_name.setText(my_book_object.getString("FAC_MAIN")); 
               FC_code.setText(my_book_object.getString("FAC_CODE")); 
               FC_name.setText(my_book_object.getString("FAC_NAME"));
               Book_state.setText(my_book_object.getString("BOOK_AGREE")); 
               Book_day.setText(my_book_object.getString("BOOK_DAY"));
               Book_time.setText(my_book_object.getString("BOOK_TIME1")+"~"+ my_book_object.getString("BOOK_TIME2"));
               Book_reason.setText(my_book_object.getString("BOOK_REASON"));
               Book_member.setText(my_book_object.getString("BOOK_MEMBER"));
               Member_phone.setText(my_book_object.getString("MEMBER_PHONE"));
            }

          } catch (JSONException e) {
                  if(var == 1){
                      Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                  }else{
                     Toast.makeText(getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                  }  
               }
            }
         }
      }