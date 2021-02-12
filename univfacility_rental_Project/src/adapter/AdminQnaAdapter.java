package adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import bean.QnaBean;

import com.example.andproject.R;

public class AdminQnaAdapter extends BaseAdapter{
	private Context ctx;
	private LayoutInflater inflater = null;
	private ArrayList<QnaBean> qna_list;
	public TextView txt1;
	public TextView txt2;
	public TextView txt3;
	int layout;
	
	public AdminQnaAdapter(Context context, int alayout, ArrayList<QnaBean> arrayList){
		this.ctx = context;
		this.qna_list = arrayList;
		this.inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout = alayout;
	}

	@Override
	public int getCount() {
		return qna_list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = inflater.inflate(layout, parent, false);
		}
			txt1 = (TextView)convertView.findViewById(R.id.adminqna_num);
			txt1.setText(qna_list.get(position).getQNA_NUM());
			
			txt2 = (TextView)convertView.findViewById(R.id.adminqna_id);
			txt2.setText(qna_list.get(position).getQNA_ID());
			
			txt3 = (TextView)convertView.findViewById(R.id.adminqna_date);
			txt3.setText(qna_list.get(position).getQNA_DATE());
		
			return convertView;
	}
}
