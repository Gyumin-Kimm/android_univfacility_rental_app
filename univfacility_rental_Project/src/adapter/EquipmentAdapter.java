package adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import bean.EquipBean;

import com.example.andproject.R;

public class EquipmentAdapter extends BaseAdapter {
	
	private Context ctx;
	private LayoutInflater inflater = null;
	private ArrayList<EquipBean> equip_list;
	public TextView txt1;
	public TextView txt2;
	public TextView txt3;
	int layout;
	
	public EquipmentAdapter(Context context, int alayout ,ArrayList<EquipBean> arrayList){
		this.ctx = context;
		this.equip_list = arrayList;
		this.inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout = alayout;
	}
	@Override
	public int getCount() {
		return equip_list.size();
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
	public View getView(int position, View convertView, ViewGroup parent){

		if(convertView == null){
			convertView = inflater.inflate(layout, parent, false);
		}
		txt1 = (TextView)convertView.findViewById(R.id.equip_tv_code);
		txt1.setText(equip_list.get(position).getEQIP_CODE());
		
		txt2 = (TextView)convertView.findViewById(R.id.equip_tv_name);
		txt2.setText(equip_list.get(position).getEQIP_NAME()); 
		
		txt3 = (TextView)convertView.findViewById(R.id.equip_tv_type);
		txt3.setText(equip_list.get(position).getEQIP_TYPE());

		return convertView;
	}
}
