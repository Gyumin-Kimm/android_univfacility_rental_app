package adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import bean.MemberBean;

import com.example.andproject.R;

public class MemberAdapter extends BaseAdapter{
	private Context ctx;
	private LayoutInflater inflater = null;
	private ArrayList<MemberBean> member_list;
	public TextView txt1;
	public TextView txt2;
	int layout;
	
	public MemberAdapter(Context context, int alayout, ArrayList<MemberBean> arrayList){
		this.ctx = context;
		this.member_list = arrayList;
		this.inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout = alayout;
	}
	
	@Override
	public int getCount(){
		return member_list.size();
	}
	
	@Override
	public Object getItem(int position){
		return position;
	}
	
	@Override
	public long getItemId(int position){
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){

		if(convertView == null){
			convertView = inflater.inflate(layout, parent, false);
		}

		txt1 = (TextView)convertView.findViewById(R.id.member_id);
		txt1.setText(member_list.get(position).getMEMBER_ID());
		
		txt2 = (TextView)convertView.findViewById(R.id.member_name);
		txt2.setText(member_list.get(position).getMEMBER_NAME());
		
		return convertView;

	}
}
