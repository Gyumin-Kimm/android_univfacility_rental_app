package user_Activity;


import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andproject.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class Book_step3 extends Activity {
   
   Intent intent;
   
   Spinner spin1;
   Spinner spin2;
   ArrayAdapter<CharSequence> adspin;
   
   Httptask task;
   Http_Get get;
   
   Button btn, equiv_btn;   Toast toast;
   
   String bd_name,fc_code,fc_name,ch_date;
   TextView BD_name,FC_code,FC_name,CH_date,Mem_id,Equip;
   
   EditText Reason,Mem_list,Phone; 
   String reason, mem_list, phone, equip_list;  
   String get_reason, get_mem_list, get_phone;
   
   String Member_id; String start_t,last_t;

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59"))); 
       setContentView(R.layout.book_step3);
   
       ActivityManager.addActivity(this);
       
       if(SessionControl.cookies!=null){
            for (int i = 0; i < SessionControl.cookies.size(); i++) {
               Cookie cookie = SessionControl.cookies.get(i);

               if (!cookie.getName().equals("JSESSIONID")) {
                  Member_id = cookie.getName();
               }
            }
         }
       
       intent = getIntent();
       bd_name = intent.getExtras().getString("Building_name").toString();
       fc_code = intent.getExtras().getString("Facility_code").toString();
       fc_name = intent.getExtras().getString("Facility_name").toString();
        ch_date = intent.getExtras().getString("Choice_date").toString();
        
        if(intent.getExtras().getString("Book_reason")!=null){
           get_reason=intent.getExtras().getString("Book_reason").toString();
        }
        if(intent.getExtras().getString("Book_mem")!=null){
           get_mem_list = intent.getExtras().getString("Book_mem").toString();
        }
        if(intent.getExtras().getString("Book_phone")!=null){
           get_phone = intent.getExtras().getString("Book_phone").toString();
        }

        Equip = (TextView)findViewById(R.id.equip_tv);
        
        if(intent.getExtras().getString("EQUIP_LIST")!=null)
        {
           equip_list = intent.getExtras().getString("EQUIP_LIST").toString(); }
        
        if(equip_list==null)
        {    Equip.setText("기자재를 선택하세요");    }
        else{
           Equip.setText(equip_list);
        }
       
       BD_name =(TextView)findViewById(R.id.bd_name);  BD_name.setText(bd_name);
       FC_code =(TextView)findViewById(R.id.fac_code);  FC_code.setText("["+fc_code+"]");
       FC_name =(TextView)findViewById(R.id.fac_name);  FC_name.setText(fc_name);   
        CH_date =(TextView)findViewById(R.id.book_day);  CH_date.setText(ch_date);
        Mem_id = (TextView)findViewById(R.id.mem_id); Mem_id.setText(Member_id);
   
       spin1 = (Spinner)findViewById(R.id.spinner1);
       spin2 = (Spinner)findViewById(R.id.spinner2);
    
       adspin = ArrayAdapter.createFromResource(this, R.array.hour,
              android.R.layout.simple_spinner_item);
       adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      
      spin1.setAdapter(adspin);
      
      spin2.setAdapter(adspin); 
      
      Reason = (EditText)findViewById(R.id.book_reason); 
      if(get_reason!=null){
         Reason.setText(get_reason);
      }
      Mem_list = (EditText)findViewById(R.id.book_mem);
      if(get_mem_list!=null){
         Mem_list.setText(get_mem_list);
      }
      Phone = (EditText)findViewById(R.id.book_phone); 
      if(get_phone!=null){
         Phone.setText(get_phone);
      }
     
      btn = (Button)findViewById(R.id.submit_btn); 
      btn.setOnClickListener(new OnClickListener() {
      
      @SuppressWarnings("unchecked")
      @Override
      public void onClick(View v) {
         // TODO Auto-generated method stub
         
         reason=Reason.getText().toString();
          mem_list=Mem_list.getText().toString();
          phone=Phone.getText().toString(); 
         
          start_t = spin1.getSelectedItem().toString()+":00"; 
          last_t = spin2.getSelectedItem().toString()+":59";
      
         
         ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
         params.add(new BasicNameValuePair("MEMBER_ID", Member_id));
         params.add(new BasicNameValuePair("BOOK_TIME1", start_t));
         params.add(new BasicNameValuePair("BOOK_TIME2", last_t ));
         params.add(new BasicNameValuePair("BOOK_REASON",reason));
         params.add(new BasicNameValuePair("BOOK_MEMBER", mem_list));
         params.add(new BasicNameValuePair("MEMBER_PHONE", phone));
         params.add(new BasicNameValuePair("FAC_CODE", fc_code));
         params.add(new BasicNameValuePair("BOOK_DAY", ch_date));
         params.add(new BasicNameValuePair("EQUIP_LIST", equip_list));
         
         task = new Httptask();
         task.execute(params);
         
         
      }
   });
      
      equiv_btn = (Button)findViewById(R.id.equiv_btn);
      
      equiv_btn.setOnClickListener(new OnClickListener() {
      
      @Override
      public void onClick(View v) {
         // TODO Auto-generated method stub
         Intent intent = new Intent(Book_step3.this, Equipment.class );
         intent.putExtra("Building_name", bd_name);
         intent.putExtra("Facility_code", fc_code);
         intent.putExtra("Facility_name", fc_name);
         intent.putExtra("Choice_date", ch_date);
         intent.putExtra("Book_reason", Reason.getText().toString());
         intent.putExtra("Book_mem", Mem_list.getText().toString());
         intent.putExtra("Book_phone", Phone.getText().toString());
         
         
         
         Book_step3.this.startActivity(intent);    
      }
   });
     
   }
   
   class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>
   {   
      
      @Override 
      protected String doInBackground(ArrayList<NameValuePair>... params) {
         
         String url = SessionControl.URL+"FacBookInsert.ad";
      
         get= new Http_Get(); 
         String result=get.httpget(url, params[0]);   
         
         
      return result;
   }
      
      @Override
      protected void onPostExecute(String result)

      {    
         super.onPostExecute(result);
         
         if(result.equals("1")){ 
            toast= Toast.makeText(Book_step3.this, "접수 성공" , Toast.LENGTH_SHORT);
            toast.show();            
            
            Equipment.equip_choice_list.clear();
            
            finish();
            
      }else{
         toast= Toast.makeText(Book_step3.this, "접수 실패" , Toast.LENGTH_SHORT);
         toast.show();
      } 
      
   }
   
} }