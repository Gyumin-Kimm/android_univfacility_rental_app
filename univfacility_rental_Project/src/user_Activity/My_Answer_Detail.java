package user_Activity;

import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andproject.R;

public class My_Answer_Detail extends ActionBarActivity {

   
   Httptask task;
   Http_Get get;
   TextView Qna_info_date, Qna_info_content, Qna_info_answer;

   Intent intent;
   String Qa_num;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.my_answer_detail);
      
      intent = getIntent();
       Qa_num = intent.getExtras().getString("Qa_num").toString();
       
       Qna_info_date = (TextView)findViewById(R.id.qna_info_date);
       Qna_info_content = (TextView)findViewById(R.id.qna_info_content);
       Qna_info_answer = (TextView)findViewById(R.id.qna_info_answer);
       
         ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
         params.add(new BasicNameValuePair("QNA_NUM",Qa_num));
         
         task = new Httptask();
         task.execute(params);
         
   }

   
   class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String>
   {   JSONArray qna_array;
      JSONObject qna_object;
      
      @Override 
      protected String doInBackground(ArrayList<NameValuePair>... params) {

         String url = SessionControl.URL + "QaGet.ad";

         get = new Http_Get();
         String result = get.httpget(url, params[0]);

         return result;

      }
      
      @Override
      protected void onPostExecute(String Jason) {
         super.onPostExecute(Jason);
         try {
            qna_array = new JSONArray(Jason); 
            qna_object = new JSONObject();

            for (int i = 0; i < qna_array.length(); i++) {
               qna_object = qna_array.getJSONObject(i); 
               
               Qna_info_date.setText(qna_object.getString("QNA_DATE"));
               Qna_info_content.setText(qna_object.getString("QNA_CONTENT"));
               Qna_info_answer.setText(qna_object.getString("QNA_REPLY"));
            }

         } catch (JSONException e) {
            Toast.makeText(My_Answer_Detail.this, e.getMessage(),Toast.LENGTH_SHORT).show();
         }
      }

   }
   
}