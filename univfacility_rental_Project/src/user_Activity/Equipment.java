package user_Activity;

import http.Http_Get;
import http.SessionControl;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bean.EquipBean;

import com.example.andproject.R;

import user_Activity.ActivityManager;
import user_Activity.Book_step3;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Equipment extends ActionBarActivity {
    
	ArrayAdapter<CharSequence> adspin;
	Spinner spin;
	
	Button Equip_choice_btn , Submit_btn;
	ListView MyList, MyList2;
	Toast toast; 
	 
	Httptask task; 
	Http_Get get;

	JSONArray equip_array;
	JSONObject equip_object;
	
	ArrayList<EquipBean> equip_list;
	EquipBean equip;
	BookEquipment_Adapter adapter; 
	
	static ArrayList<EquipBean> equip_choice_list =new ArrayList<EquipBean>();
	static BookEquipment_Adapter2 adapter2;
	static TextView tv1, tv2, tv3;

	
	String bd_name,fc_code,fc_name,ch_date,book_reason,book_mem,book_phone;
	String equip_code="";																	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#856e59"))); 
		setContentView(R.layout.equipment);
		
		ActivityManager.addActivity(this);
		
		  Intent intent = getIntent();
	      bd_name = intent.getExtras().getString("Building_name").toString();
	      fc_code = intent.getExtras().getString("Facility_code").toString();
	      fc_name = intent.getExtras().getString("Facility_name").toString();
	      ch_date = intent.getExtras().getString("Choice_date").toString();
	      book_reason =intent.getExtras().getString("Book_reason").toString();
		  book_mem =intent.getExtras().getString("Book_mem").toString();
		  book_phone=intent.getExtras().getString("Book_phone").toString();
		
		  
		 spin = (Spinner)findViewById(R.id.spinner1);
		  
		    adspin = ArrayAdapter.createFromResource(this, R.array.equipment,//출력할 건물 이름들의 문자열(배열)리소스 id 
				     android.R.layout.simple_spinner_item);//스피너 자체에 적용할 레이아웃
		    adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//클릭했을때 나타나는 팝업의 레이아웃
		   
		    spin.setAdapter(adspin); 
		    
		    
		    OnClickListener myOnClickListener = new OnClickListener() {
	            @SuppressWarnings("unchecked")
				@Override
	            public void onClick(View button) {
	                if (button.getId() == R.id.equip_choice_btn){
	                	
	                	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
						  params.add(new BasicNameValuePair("EQIP_TYPE", spin.getSelectedItem().toString()));
						
						  task = new Httptask();
						//  task.execute(params);
						  task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
						//서버에 url요청. 장비 유형을  파라미터로 넘겨줌.
						  
	                }
	                else if (button.getId() == R.id.submit_btn){
	                	
	            		Intent intent = new Intent(Equipment.this, Book_step3.class );
	            		
	            		for(EquipBean equip : equip_choice_list){
	            			
	            			equip_code += equip.getEQIP_CODE()+",";
	            		}
	            		
	            		intent.putExtra("Building_name", bd_name);
						intent.putExtra("Facility_code", fc_code);
						intent.putExtra("Facility_name", fc_name);
						intent.putExtra("Choice_date", ch_date);
						intent.putExtra("Book_reason", book_reason);
						intent.putExtra("Book_mem", book_mem);
						intent.putExtra("Book_phone", book_phone);
	            		intent.putExtra("EQUIP_LIST", equip_code);
	            		
	            		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						Equipment.this.startActivity(intent);
	                }
	            }
	        };
		    
		    findViewById(R.id.equip_choice_btn).setOnClickListener(myOnClickListener);
		    findViewById(R.id.submit_btn).setOnClickListener(myOnClickListener);
		    
		    
		    MyList2 =(ListView)findViewById(R.id.listView2);
		
		  }
		
////서버와 통신을 위한 내부클래스	
//기자재 type을 고르고 선택버튼눌렀을때  수행되는 쓰레드 
class Httptask extends AsyncTask<ArrayList<NameValuePair>,Void,String> {  
			@Override //작업자 스레드 (메인 스레드와 분리.Thread 의 run()의 역활)
			protected String doInBackground(ArrayList<NameValuePair>... params) {
				
				String url = SessionControl.URL+"EquipList.ad";
			
				get= new Http_Get(); 
				String result=get.httpget(url, params[0]);	
				
			return result;
		}
			
			@Override //작업 스레드 완료 시 동작 하는 부분. doInBackground 수행 후 메인/UI Thread에 의해서 실행.
			protected void onPostExecute(String Jason)

			{    
				super.onPostExecute(Jason);
				try {
					
					equip_list = new ArrayList<EquipBean>();
				

					equip_array = new JSONArray(Jason); // JSON배열 생성(Jason배열의 한 요소가
														// JasonObject)
					equip_object = new JSONObject();

					for (int i = 0; i < equip_array.length(); i++) {

						equip_object = equip_array.getJSONObject(i); // JSON배열에서 i번째
																		// Jason객체 를 꺼냄

						equip = new EquipBean();

						// Jason객체의 "FAC_CODE"의 값을 String으로 꺼내서 Fac_Details 객체인 fac의
						// 멤버변수 FAC_CODE에 저장
						equip.setEQIP_CODE(equip_object.getString("EQIP_CODE"));
						equip.setEQIP_NAME(equip_object.getString("EQIP_NAME"));
						equip.setEQIP_OUT_STATUS(equip_object.getString("EQIP_OUT_STATUS"));

						equip_list.add(equip); // 데이터가 채워진 Fac_Details객체들을
												// ArrayList<Fac_Details>에 저장.
					}

					adapter = new BookEquipment_Adapter(Equipment.this, R.layout.equipment_list,
							equip_list);
					MyList = (ListView) findViewById(R.id.listView1);
					MyList.setAdapter(adapter);
					MyList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); //
					
				} catch (JSONException e) {
					Toast.makeText(Equipment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
				}
				
			
				adapter2 = new BookEquipment_Adapter2(Equipment.this, R.layout.equipment_choice_list,
						equip_choice_list);
				//MyList2 = (ListView) findViewById(R.id.listView2);
				MyList2.setAdapter(adapter2);
		} }
			
}

// 어댑터 클래스
class BookEquipment_Adapter extends BaseAdapter {
		Context maincon;
		LayoutInflater Inflater;
		ArrayList<EquipBean> equip_list;
		ArrayList<EquipBean> equip_list2;
		
		int layout;
		TextView txt1,txt2,txt3;
		EquipBean a;
		
		Button btn;
		protected BookEquipment_Adapter2 adapter2;
		boolean removed = false;
		Button selected_btn;

		public BookEquipment_Adapter(Context context, int alayout,
				ArrayList<EquipBean> aarSrc) {
			maincon = context;
			Inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			equip_list = aarSrc;
			layout = alayout;
		}

		public int getCount() {
			return equip_list.size();
		}

		public String getItem(int position) {
			return equip_list.get(position).getEQIP_CODE();
		}

		public long getItemId(int position) {
			return position;
		}

		// 각 항목의 뷰 생성

		// 차일드뷰 , 리스트뷰
		public View getView(final int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = Inflater.inflate(layout, parent, false);
			} // 커스텀 레이아웃에 있는 뷰들에 데이터를 넣어준다.
			txt1 = (TextView)convertView.findViewById(R.id.textView1);
			txt1.setText(equip_list.get(position).getEQIP_CODE()); 

			txt2 = (TextView)convertView.findViewById(R.id.textView2);
			txt2.setText(equip_list.get(position).getEQIP_NAME()); 

			txt3 = (TextView)convertView.findViewById(R.id.textView3);
			txt3.setText(equip_list.get(position).getEQIP_OUT_STATUS());

			btn = (Button)convertView.findViewById(R.id.button1);
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				
					selected_btn = (Button)v.findViewById(R.id.button1);//클릭된 항목 뷰의 버튼을 가져와서 
					selected_btn.setText("삭제");//버튼의 글자를"삭제"로 변경 
					
					String code = equip_list.get(position).getEQIP_CODE();
					Log.i("aa",equip_list.get(position).getEQIP_CODE());
					
					if( Equipment.equip_choice_list.size() == 0 ){//선택된 아이템이 없으면  추가
						
						
						a = new EquipBean();
						a.setEQIP_CODE(equip_list.get(position).getEQIP_CODE());
						a.setEQIP_NAME(equip_list.get(position).getEQIP_NAME());
						Equipment.equip_choice_list.add(a);
						Equipment.adapter2.notifyDataSetChanged();
						removed = false; 
			
				
					}else{ //선택된 아이템이 이미 있으면 
						
					for ( int i=0; i<Equipment.equip_choice_list.size(); i++ ){// 선택된 아이템을 하나씩 꺼내서 비교
						
				
						if(code.equals(Equipment.equip_choice_list.get(i).getEQIP_CODE())){ //선택된거에 이미 있으면 삭제 
							
							
							Equipment.equip_choice_list.remove(i);
							Equipment.adapter2.notifyDataSetChanged() ;
							removed = true;//삭제되었음
							
							selected_btn = (Button)v.findViewById(R.id.button1); //삭제된 항목 뷰의 버튼을 가져와서 
							selected_btn.setText("추가"); //버튼의 글자를 "추가"로 변경
						}
					  }
						
					      if(removed!=true){ //삭제된 항목은 이부분을 수행하지않고 지나감. 삭제되지않았다면,선택되지않았던 항목이므로 추가 
					
					    	a = new EquipBean();
					    	a.setEQIP_CODE(equip_list.get(position).getEQIP_CODE());
					    	a.setEQIP_NAME(equip_list.get(position).getEQIP_NAME());
							Equipment.equip_choice_list.add(a);
							Equipment.adapter2.notifyDataSetChanged();
							removed = false; // 다른 항목들이 추가되어야하므로 다시 false로 만들어줘야함  
							
					      }removed = false; //삭제된 항목을 바로 다시 눌렀을때에도 추가되어야하므로 여기서도 false로 만들어줘야함! 
					}
				}
			});

			return convertView;
		}
	}
/// adapter2 = new BookEquipment_Adapter2(Equipment.this, R.layout.equipment_choice_list,
/// equip_choice_list);
class BookEquipment_Adapter2 extends BaseAdapter {
		Context maincon;
		LayoutInflater Inflater;
		ArrayList<EquipBean> equip_list2;
		EquipBean a;
		int layout;
		TextView txt1;
		TextView txt2;
		

		public BookEquipment_Adapter2(Context context, int alayout,
				ArrayList<EquipBean> aarSrc) {
			maincon = context;
			Inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			equip_list2 = aarSrc;
			layout = alayout;
		}

		public int getCount() {
			return equip_list2.size();
		}

		public String getItem(int position) {
			return equip_list2.get(position).getEQIP_CODE();
		}

		public long getItemId(int position) {
			return position;
		}

		// 각 항목의 뷰 생성

		// 차일드뷰 , 리스트뷰
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = Inflater.inflate(layout, parent, false);
			} // 커스텀 레이아웃에 있는 뷰들에 데이터를 넣어준다.
			txt1 = (TextView) convertView.findViewById(R.id.textView1);
			txt1.setText(equip_list2.get(position).getEQIP_CODE()); 

			txt2 = (TextView) convertView.findViewById(R.id.textView2);
			txt2.setText(equip_list2.get(position).getEQIP_NAME()); 

			return convertView;
		}
	}
