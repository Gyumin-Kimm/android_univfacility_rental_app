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
import bean.QnaBean;

import com.example.andproject.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class My_Answer extends ActionBarActivity {
   
   Httptask task;
   Http_Get get;
   String Member_id;
   ListView MyList;
   Intent intent;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59")));
      setContentView(R.layout.my_answer);
      
         if(SessionControl.cookies!=null){
             for(int i =0 ;i<SessionControl.cookies.size(); i++){
               Cookie cookie=SessionControl.cookies.get(i);
               
            if (!cookie.getName().equals("JSESSIONID")) {
               Member_id = cookie.getName();
            }
             }
      }
      
       ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("MEMBER_ID", Member_id));
      
        task = new Httptask();
        task.execute(params);
   }
   
   

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.my__answer, menu);
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
   {   JSONArray qna_array;
      JSONObject qna_object;
      ArrayList<QnaBean> qna_list;
      QnaBean qna;
      MyAnswer_Adapter adapter;
      
   
      @Override //작업자 스레드 (메인 스레드와 분리.Thread 의 run()의 역활)
      protected String doInBackground(ArrayList<NameValuePair>... params) {
         
         String url = SessionControl.URL+"QaList.ad"; ////////////
      
         get= new Http_Get(); 
         String result=get.httpget(url, params[0]);   
         
            return result;
   }
      
      @Override //작업 스레드 완료 시 동작 하는 부분. doInBackground 수행 후 메인/UI Thread에 의해서 실행.
      protected void onPostExecute(String Jason)

      {    
         super.onPostExecute(Jason);
          try {
              qna_list = new ArrayList<QnaBean>();//Fac_Details객체들을 저장하는  fac_list배열 생성 
               
               qna_array = new JSONArray(Jason); //JSON배열 생성(Jason배열의 한 요소가 JasonObject) 
               qna_object = new JSONObject();
                 
                 
                 for (int i=0; i<qna_array.length(); i++) {
                      
                  qna_object = qna_array.getJSONObject(i);  //JSON배열에서 i번째 Jason객체 를 꺼냄 
                   
                  qna = new QnaBean();
                   
                   //Jason객체의 "FAC_CODE"의 값을 String으로 꺼내서  Fac_Details 객체인 fac의 멤버변수 FAC_CODE에 저장  
                  qna.setQNA_DATE(qna_object.getString("QNA_DATE")); 
                  qna.setQNA_CONTENT(qna_object.getString("QNA_CONTENT")); 
                  qna.setQNA_NUM(qna_object.getString("QNA_NUM"));
         
                 
                   qna_list.add(qna);   //데이터가 채워진  Fac_Details객체들을  ArrayList<Fac_Details>에 저장. 
                 }
                 
                 adapter = new MyAnswer_Adapter(My_Answer.this, R.layout.my_answer_list, qna_list); //fac_list占쏙옙 占쏙옙占쏙옙트占썰에 채占쏙옙 
                  MyList=(ListView)findViewById(R.id.listView1);
                  MyList.setAdapter(adapter);
                  
                  MyList.setOnItemClickListener(new OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> parentView,
                           View view, int position, long id) {
                     intent = new Intent(My_Answer.this, My_Answer_Detail.class);
                     intent.putExtra("Qa_num", qna_list.get(position).getQNA_NUM().toString());
                     
                     My_Answer.this.startActivity(intent);
                     }
                  });
                  
              }
                   catch(JSONException e) {
                     Toast.makeText(My_Answer.this,e.getMessage(),Toast.LENGTH_SHORT).show();  
              }
      } 
   } 
}

//어댑터 클래스
class MyAnswer_Adapter extends BaseAdapter {
   Context maincon;
   LayoutInflater Inflater;
   ArrayList<QnaBean> arSrc;
   int layout;
   TextView txt1;
   TextView txt2;
   
   public MyAnswer_Adapter(Context context, int alayout, ArrayList<QnaBean> aarSrc) {
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
      return arSrc.get(position).getQNA_NUM(); // 
   }

   public long getItemId(int position) {
      return position;
   }

   // 각 항목의 뷰 생성   
   
                                 //  차일드뷰 ,        리스트뷰 
   public View getView(int position, View convertView, ViewGroup parent) {
   
      if (convertView == null) {
         convertView = Inflater.inflate(layout, parent, false);
      }     //커스텀 레이아웃에 있는 뷰들에 데이터를 넣어준다. 
      txt1 = (TextView)convertView.findViewById(R.id.textView1);
      txt1.setText(arSrc.get(position).getQNA_DATE()); //시설코드를  textView1에 출력  

      txt2 = (TextView)convertView.findViewById(R.id.textView2);
      txt2.setText(arSrc.get(position).getQNA_CONTENT()); //시설명을  textView2에 출력 
   
             
      return convertView;
   } 

}
   

   