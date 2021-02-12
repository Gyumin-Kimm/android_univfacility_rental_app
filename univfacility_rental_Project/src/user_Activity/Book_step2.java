package user_Activity;


import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andproject.R;

import facility.Fac_Book;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class Book_step2 extends ActionBarActivity {

   Intent intent;
   
   ArrayList<Fac_Book> fac_book_list;
   Fac_Book fac_book;
   
   Button date_choice_btn, search_btn, application_btn;
   //날짜선택,조회 ,대여 신청 버튼 

   TextView choice_date;
   Calendar dateAndtime=Calendar.getInstance();
   String fomat_string;
   
   Httptask task;
   Http_Get get;
   
   String bd_name; String fc_code; String fc_name; 
   //전 액티비티로부터 넘겨받은 건물명,시설코드,시설명을 저장할 String객체 
   TextView BD_name; TextView FC_code; TextView FC_name; 
   //위 String을 채워 건물명,시설코드 ,시설명을 나타낼 TextView
   
   BookStep2_Adapter MyAdapter;
   ListView MyList;
   
   //DatePickerDialog 대화상자 실행되면, 대화상자에 설정한 OnDateSetListener를 통해 
   //사용자가 선택한 날짜값을 받아오며, updateLabel()을 통해 선택한 날짜를 설정.

   DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
      
      @Override
      public void onDateSet(DatePicker view, int year, int monthOfYear,
            int dayOfMonth) {
         // TODO Auto-generated method stub
         dateAndtime.set(Calendar.YEAR, year);
         dateAndtime.set(Calendar.MONTH, monthOfYear);
         dateAndtime.set(Calendar.DAY_OF_MONTH,dayOfMonth);
         updateLabel(); //선택한 날짜를 TextView에 채워 보이게하는 메소드
         
      }
   };
   
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59"))); 
       setContentView(R.layout.book_step2);
       
       ActivityManager.addActivity(this);

       intent = getIntent();
       bd_name = intent.getExtras().getString("Building_name").toString();
       fc_code = intent.getExtras().getString("Facility_code").toString();
       fc_name = intent.getExtras().getString("Facility_name").toString();
       
       BD_name =(TextView)findViewById(R.id.textView7);  BD_name.setText(bd_name);
       FC_code =(TextView)findViewById(R.id.textView8);  FC_code.setText("["+fc_code+"]");
       FC_name =(TextView)findViewById(R.id.textView9);  FC_name.setText(fc_name);
       //전 액티비티에서 넘겨받은 건물명,시설코드,시설명을 TextView에 채워 보이게함. 
      
       date_choice_btn =(Button)findViewById(R.id.mypage_btn);
       date_choice_btn.setOnClickListener(new OnClickListener() {
         //날짜 선택 버튼을 클릭하면 호출되는 메소드 
         @Override
         public void onClick(View v) {
            // TODO Auto-generated method stub
            new DatePickerDialog(Book_step2.this, d, dateAndtime.get(Calendar.YEAR), 
                                          dateAndtime.get(Calendar.MONTH),
                                          dateAndtime.get(Calendar.DAY_OF_MONTH)).show();
         } //d는 OnDateSetListener.  마지막 3개 인수는 현재 시간을 월 년 일로 나타낸것 
      });
       
       
       choice_date = (TextView)findViewById(R.id.textView1);
       
       updateLabel(); //처음에 이 메소드가 호출될 때는 날짜 선택하기 전이니까 그냥 현재시간이 TextView에 채워 보이게됨.  
       
       //R.layout.choice2_list 레이아웃대로 뷰를 만들어서 거기에 arItem목록 하나(MyItem객체)를  채운다. 
      ListView MyList;
      MyList=(ListView)findViewById(R.id.listView1);
      MyList.setAdapter(MyAdapter); //어댑터를 리스트뷰에 연결 . 어댑터에서 만들어진 차일드 뷰가 리스트뷰의 한항목에 채워질것임
      
      MyList.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
               
         } });
      
      
      
      search_btn=(Button)findViewById(R.id.board_btn);
         
      search_btn.setOnClickListener(new OnClickListener() {
            
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View v) {
               // TODO Auto-generated method stub
               
               ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair("FAC_CODE", fc_code));
               params.add(new BasicNameValuePair("BOOK_DAY", fomat_string));
               
               task = new Httptask();
               task.execute(params);
               
            }
         });
          
      application_btn =(Button)findViewById(R.id.book_btn); 
          
      application_btn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
               // TODO Auto-generated method stub
               Intent intent = new Intent(Book_step2.this, Book_step3.class );
               intent.putExtra("Building_name", bd_name);
               intent.putExtra("Facility_code", fc_code);
               intent.putExtra("Facility_name", fc_name);
               intent.putExtra("Choice_date", fomat_string);
               Book_step2.this.startActivity(intent); 
               //신청서 작성 페이지에 시설물 정보(건물명, 시설코드, 시설명)와 선택한 날짜 넘기기

            }
         });  
   }
   
   //사용자가 날짜를 선택하면  TextView에 선택한 날짜를 채워 보이게함. 
   private void updateLabel() {
      
      fomat_string = String.format("%d-%d-%d",dateAndtime.get(Calendar.YEAR),
                                    dateAndtime.get(Calendar.MONTH)+1,
                                    dateAndtime.get(Calendar.DAY_OF_MONTH));
      choice_date.setText(fomat_string);
   }

   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.choice_1, menu);
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
   
   class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>
   {   JSONArray fac_book_array;
      JSONObject fac_book_object;
      
      
      @Override //작업자 스레드 (메인 스레드와 분리.Thread 의 run()의 역활)
      protected String doInBackground(ArrayList<NameValuePair>... params) {
         
         String url = SessionControl.URL+"FacBookList.ad";
      
         get= new Http_Get(); 
         String result=get.httpget(url, params[0]);   
         
         
      return result;
   }
      
      @Override //작업 스레드 완료 시 동작 하는 부분. doInBackground 수행 후 메인/UI Thread에 의해서 실행.
      protected void onPostExecute(String Jason)

      {    
         super.onPostExecute(Jason);
          try {
               fac_book_list= new ArrayList<Fac_Book>();//Fac_Book객체들을 저장하는  fac_list배열 생성 
               
                fac_book_array = new JSONArray(Jason); //JSON배열 생성(Jason배열의 한 요소가 JasonObject) 
                  fac_book_object = new JSONObject();
                 
                 
                 for (int i=0; i<fac_book_array.length(); i++) {
                      
                  fac_book_object = fac_book_array.getJSONObject(i);  //JSON배열에서 i번째 Jason객체 를 꺼냄 
                   
                  fac_book = new Fac_Book();
                   
                   //Jason객체의 "FAC_CODE"의 값을 String으로 꺼내서  Fac_Details 객체인 fac의 멤버변수 FAC_CODE에 저장 
                  fac_book.setBOOK_STATE(fac_book_object.getString("BOOK_AGREE")); 
                  fac_book.setBOOK_TIME(fac_book_object.getString("BOOK_TIME1")+"~"+fac_book_object.getString("BOOK_TIME2")); 
                  fac_book.setBOOK_PERSON(fac_book_object.getString("MEMBER_ID"));
                  fac_book.setBOOK_REASON(fac_book_object.getString("BOOK_REASON"));
              
                  fac_book_list.add(fac_book);    //데이터가 채워진  Fac_Details객체들을  ArrayList<Fac_Details>에 저장. 
                 }
                  MyAdapter = new BookStep2_Adapter(Book_step2.this, R.layout.book_step2_list, fac_book_list); //fac_list를 리스트뷰에 채움 
                  MyList=(ListView)findViewById(R.id.listView1);
                  MyList.setAdapter(MyAdapter); 
                  
              }
                   catch(JSONException e) {
                     Toast.makeText(Book_step2.this,e.getMessage(),Toast.LENGTH_SHORT).show();  
              }
      } 
      
   }
}

   //어댑터 클래스
   class BookStep2_Adapter extends BaseAdapter {
      Context maincon;
      LayoutInflater Inflater;
      ArrayList<Fac_Book> arSrc;
      int layout;

      public BookStep2_Adapter(Context context, int alayout, ArrayList<Fac_Book> aarSrc) {
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
         return arSrc.get(position).getBOOK_TIME();
      }
   
      public long getItemId(int position) {
         return position;
      }
   
   // 각 항목의 뷰 생성   
   
                                      //   차일드뷰 ,        리스트뷰 
      public View getView(int position, View convertView, ViewGroup parent) {
      
         if (convertView == null) {
            convertView = Inflater.inflate(layout, parent, false);
         }    //커스텀 레이아웃에 있는 뷰들에 데이터를 넣어준다. 
         TextView txt1 = (TextView)convertView.findViewById(R.id.textView1);
         txt1.setText(arSrc.get(position).getBOOK_STATE()); 
         TextView txt2 = (TextView)convertView.findViewById(R.id.textView2);
         txt2.setText(arSrc.get(position).getBOOK_TIME());
         TextView txt3 = (TextView)convertView.findViewById(R.id.textView3);
         txt3.setText(arSrc.get(position).getBOOK_PERSON());
         TextView txt4 = (TextView)convertView.findViewById(R.id.textView4);
         txt4.setText(arSrc.get(position).getBOOK_REASON());
         return convertView;
      }
   }