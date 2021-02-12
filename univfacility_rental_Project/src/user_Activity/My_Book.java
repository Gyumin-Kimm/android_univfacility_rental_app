package user_Activity;


import facility.My_FacBook;
import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class My_Book extends ActionBarActivity {

   Httptask task;
   Http_Get get;
   ListView MyList;
   String Member_id;
   Cookie cookie;
   Intent intent;
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
       setContentView(R.layout.my_book);
      
       if(SessionControl.cookies!=null){
          for(int i =0 ;i<SessionControl.cookies.size(); i++){
            Cookie cookie=SessionControl.cookies.get(i);
            
            if(!cookie.getName().equals("JSESSIONID"))
            {
                  Member_id = cookie.getName();
               }
         }

       }
       
       ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("MEMBER_ID", Member_id ));
      
      task = new Httptask();
      task.execute(params);
       
   }
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      
      getMenuInflater().inflate(R.menu.my_book, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
   
      int id = item.getItemId();
      if (id == R.id.action_settings) {
         return true;
      }else if(id == R.id.action_discard) {
         
         
         return true;
         
      }
      return super.onOptionsItemSelected(item);
   }
   
   
   class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>
   {   JSONArray fac_array;
      JSONObject fac_object;
      ArrayList<My_FacBook> my_fac_list;
      My_FacBook my_fac;
      MyBook_Adapter adapter;
      String book_num; 
      
      @Override 
      protected String doInBackground(ArrayList<NameValuePair>... params) {
         
         String url = SessionControl.URL+"FacUserBookList.ad";
      
         get= new Http_Get(); 
         String result=get.httpget(url, params[0]);   
         
      return result;
   }
      
      @Override 
      protected void onPostExecute(String Jason)

      {    
         super.onPostExecute(Jason);
          try {
              
              my_fac_list = new ArrayList<My_FacBook>();
               
               fac_array = new JSONArray(Jason); 
                 fac_object = new JSONObject();
                 
                 for (int i=0; i<fac_array.length(); i++) {
                      
                   fac_object = fac_array.getJSONObject(i);  
                   
                   my_fac= new My_FacBook();
                   
                   my_fac.setFAC_MAIN(fac_object.getString("FAC_MAIN")); 
                   my_fac.setFAC_CODE(fac_object.getString("FAC_CODE")); 
                   my_fac.setFAC_NAME(fac_object.getString("FAC_NAME"));
                   my_fac.setBOOK_STATE(fac_object.getString("BOOK_AGREE")); 
                   my_fac.setBOOK_NUM(String.valueOf(fac_object.getInt("BOOK_NUM")));
                   
                   my_fac_list.add(my_fac);   
                 
            
                  adapter = new MyBook_Adapter(My_Book.this, R.layout.my_book_list, my_fac_list); //fac_list�� ����Ʈ�信 ä�� 
                  MyList=(ListView)findViewById(R.id.listView1);
                  MyList.setAdapter(adapter);
                  
                  MyList.setOnItemClickListener(new OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> parentView,
                           View view, int position, long id) {
                     intent = new Intent(My_Book.this, My_Book_Detail.class);
                     intent.putExtra("Book_num", my_fac_list.get(position).getBOOK_NUM());
                  
                     My_Book.this.startActivity(intent);
                     }
                  });
                  
                 } }
                   catch(JSONException e) {
                     Toast.makeText(My_Book.this,e.getMessage(),Toast.LENGTH_SHORT).show();  
              }
      } 
   }
} 
   //어댑터 클래스
class MyBook_Adapter extends BaseAdapter {
   Context maincon;
   LayoutInflater Inflater;
   ArrayList<My_FacBook> arSrc;
   int layout;
   TextView Building_name, Fac_code, Fac_name, Book_state; 

   public MyBook_Adapter(Context context, int alayout, ArrayList<My_FacBook> aarSrc) {
      maincon = context;
      Inflater = (LayoutInflater)context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE);
      arSrc = aarSrc;
      layout = alayout;
   }

   public int getCount() {
      return arSrc.size();
   }

   public String getItem(int position) {
      return arSrc.get(position).getBOOK_NUM();
   }

   public long getItemId(int position) {
      return position;
   }

   // 각 항목의 뷰 생성
   public View getView(int position, View convertView, ViewGroup parent) {
   
      if (convertView == null) {
         convertView = Inflater.inflate(layout, parent, false);
      }
      
      Building_name = (TextView)convertView.findViewById(R.id.building_name);
      Building_name.setText(arSrc.get(position).getFAC_MAIN());

      Fac_code = (TextView)convertView.findViewById(R.id.fac_code);
      Fac_code.setText(arSrc.get(position).getFAC_CODE());
      
      Fac_name = (TextView)convertView.findViewById(R.id.fac_name);
      Fac_name.setText(arSrc.get(position).getFAC_NAME());
      
      Book_state= (TextView)convertView.findViewById(R.id.book_state);
      Book_state.setText(arSrc.get(position).getBOOK_STATE());
      
      return convertView;
   }
   
}