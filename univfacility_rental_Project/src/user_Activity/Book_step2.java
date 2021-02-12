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
   //��¥����,��ȸ ,�뿩 ��û ��ư 

   TextView choice_date;
   Calendar dateAndtime=Calendar.getInstance();
   String fomat_string;
   
   Httptask task;
   Http_Get get;
   
   String bd_name; String fc_code; String fc_name; 
   //�� ��Ƽ��Ƽ�κ��� �Ѱܹ��� �ǹ���,�ü��ڵ�,�ü����� ������ String��ü 
   TextView BD_name; TextView FC_code; TextView FC_name; 
   //�� String�� ä�� �ǹ���,�ü��ڵ� ,�ü����� ��Ÿ�� TextView
   
   BookStep2_Adapter MyAdapter;
   ListView MyList;
   
   //DatePickerDialog ��ȭ���� ����Ǹ�, ��ȭ���ڿ� ������ OnDateSetListener�� ���� 
   //����ڰ� ������ ��¥���� �޾ƿ���, updateLabel()�� ���� ������ ��¥�� ����.

   DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
      
      @Override
      public void onDateSet(DatePicker view, int year, int monthOfYear,
            int dayOfMonth) {
         // TODO Auto-generated method stub
         dateAndtime.set(Calendar.YEAR, year);
         dateAndtime.set(Calendar.MONTH, monthOfYear);
         dateAndtime.set(Calendar.DAY_OF_MONTH,dayOfMonth);
         updateLabel(); //������ ��¥�� TextView�� ä�� ���̰��ϴ� �޼ҵ�
         
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
       //�� ��Ƽ��Ƽ���� �Ѱܹ��� �ǹ���,�ü��ڵ�,�ü����� TextView�� ä�� ���̰���. 
      
       date_choice_btn =(Button)findViewById(R.id.mypage_btn);
       date_choice_btn.setOnClickListener(new OnClickListener() {
         //��¥ ���� ��ư�� Ŭ���ϸ� ȣ��Ǵ� �޼ҵ� 
         @Override
         public void onClick(View v) {
            // TODO Auto-generated method stub
            new DatePickerDialog(Book_step2.this, d, dateAndtime.get(Calendar.YEAR), 
                                          dateAndtime.get(Calendar.MONTH),
                                          dateAndtime.get(Calendar.DAY_OF_MONTH)).show();
         } //d�� OnDateSetListener.  ������ 3�� �μ��� ���� �ð��� �� �� �Ϸ� ��Ÿ���� 
      });
       
       
       choice_date = (TextView)findViewById(R.id.textView1);
       
       updateLabel(); //ó���� �� �޼ҵ尡 ȣ��� ���� ��¥ �����ϱ� ���̴ϱ� �׳� ����ð��� TextView�� ä�� ���̰Ե�.  
       
       //R.layout.choice2_list ���̾ƿ���� �並 ���� �ű⿡ arItem��� �ϳ�(MyItem��ü)��  ä���. 
      ListView MyList;
      MyList=(ListView)findViewById(R.id.listView1);
      MyList.setAdapter(MyAdapter); //����͸� ����Ʈ�信 ���� . ����Ϳ��� ������� ���ϵ� �䰡 ����Ʈ���� ���׸� ä��������
      
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
               //��û�� �ۼ� �������� �ü��� ����(�ǹ���, �ü��ڵ�, �ü���)�� ������ ��¥ �ѱ��

            }
         });  
   }
   
   //����ڰ� ��¥�� �����ϸ�  TextView�� ������ ��¥�� ä�� ���̰���. 
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
      
      
      @Override //�۾��� ������ (���� ������� �и�.Thread �� run()�� ��Ȱ)
      protected String doInBackground(ArrayList<NameValuePair>... params) {
         
         String url = SessionControl.URL+"FacBookList.ad";
      
         get= new Http_Get(); 
         String result=get.httpget(url, params[0]);   
         
         
      return result;
   }
      
      @Override //�۾� ������ �Ϸ� �� ���� �ϴ� �κ�. doInBackground ���� �� ����/UI Thread�� ���ؼ� ����.
      protected void onPostExecute(String Jason)

      {    
         super.onPostExecute(Jason);
          try {
               fac_book_list= new ArrayList<Fac_Book>();//Fac_Book��ü���� �����ϴ�  fac_list�迭 ���� 
               
                fac_book_array = new JSONArray(Jason); //JSON�迭 ����(Jason�迭�� �� ��Ұ� JasonObject) 
                  fac_book_object = new JSONObject();
                 
                 
                 for (int i=0; i<fac_book_array.length(); i++) {
                      
                  fac_book_object = fac_book_array.getJSONObject(i);  //JSON�迭���� i��° Jason��ü �� ���� 
                   
                  fac_book = new Fac_Book();
                   
                   //Jason��ü�� "FAC_CODE"�� ���� String���� ������  Fac_Details ��ü�� fac�� ������� FAC_CODE�� ���� 
                  fac_book.setBOOK_STATE(fac_book_object.getString("BOOK_AGREE")); 
                  fac_book.setBOOK_TIME(fac_book_object.getString("BOOK_TIME1")+"~"+fac_book_object.getString("BOOK_TIME2")); 
                  fac_book.setBOOK_PERSON(fac_book_object.getString("MEMBER_ID"));
                  fac_book.setBOOK_REASON(fac_book_object.getString("BOOK_REASON"));
              
                  fac_book_list.add(fac_book);    //�����Ͱ� ä����  Fac_Details��ü����  ArrayList<Fac_Details>�� ����. 
                 }
                  MyAdapter = new BookStep2_Adapter(Book_step2.this, R.layout.book_step2_list, fac_book_list); //fac_list�� ����Ʈ�信 ä�� 
                  MyList=(ListView)findViewById(R.id.listView1);
                  MyList.setAdapter(MyAdapter); 
                  
              }
                   catch(JSONException e) {
                     Toast.makeText(Book_step2.this,e.getMessage(),Toast.LENGTH_SHORT).show();  
              }
      } 
      
   }
}

   //����� Ŭ����
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
   
   // �� �׸��� �� ����   
   
                                      //   ���ϵ�� ,        ����Ʈ�� 
      public View getView(int position, View convertView, ViewGroup parent) {
      
         if (convertView == null) {
            convertView = Inflater.inflate(layout, parent, false);
         }    //Ŀ���� ���̾ƿ��� �ִ� ��鿡 �����͸� �־��ش�. 
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