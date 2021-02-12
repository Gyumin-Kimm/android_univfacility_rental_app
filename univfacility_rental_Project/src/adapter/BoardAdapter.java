package adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import bean.BoardBean;

import com.example.andproject.R;

public class BoardAdapter extends BaseAdapter{
	
	private Context ctx;
	private LayoutInflater inflater = null;
	private ArrayList<BoardBean> board_list;
	public TextView txt1;
	public TextView txt2;
	public TextView txt3;
	public TextView txt4;
	public TextView txt5;
	int layout;
	
	public BoardAdapter(Context context, int alayout, ArrayList<BoardBean> arrayList){
		this.ctx = context;
		this.board_list = arrayList;
		this.inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout = alayout;
	}
	
	@Override
	public int getCount() {
		return board_list.size();
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
			txt1 = (TextView)convertView.findViewById(R.id.board_num);
			txt1.setText(board_list.get(position).getBOARD_NUM());
			
			txt2 = (TextView)convertView.findViewById(R.id.board_subject);
			txt2.setText(board_list.get(position).getBOARD_SUBJECT());
			
			txt3 = (TextView)convertView.findViewById(R.id.board_id);
			txt3.setText(board_list.get(position).getBOARD_ID());
			
			txt4 = (TextView)convertView.findViewById(R.id.board_date);
			txt4.setText(board_list.get(position).getBOARD_DATE());
			
			txt5 = (TextView)convertView.findViewById(R.id.board_readcount);
			txt5.setText(board_list.get(position).getBOARD_READCOUNT());
			
		
			return convertView;
	}

}
