package adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.andproject.R;

import facility.Admin_Fac_Book;

public class ApprovalAdapter extends BaseAdapter{
	
	private Context ctx;
	private LayoutInflater inflater = null;
	private ArrayList<Admin_Fac_Book> book_list;
	public TextView txt1;
	public TextView txt2;
	public TextView txt3;
	public TextView txt4;
	public TextView txt5;
	int layout;
	
	public ApprovalAdapter(Context context, int alayout ,ArrayList<Admin_Fac_Book> arrayList){
		this.ctx = context;
		this.book_list = arrayList;
		this.inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout = alayout;
	}

	@Override
	public int getCount() {
		return book_list.size();
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
		txt1 = (TextView)convertView.findViewById(R.id.approval_id);
		txt1.setText(book_list.get(position).getMEMBER_ID());
		
		txt2 = (TextView)convertView.findViewById(R.id.approval_fac_code);
		txt2.setText(book_list.get(position).getFAC_CODE());
		
		txt3 = (TextView)convertView.findViewById(R.id.approval_date);
		txt3.setText(book_list.get(position).getBOOK_DAY()); 
		
		txt4 = (TextView)convertView.findViewById(R.id.approval_state);
		txt4.setText(book_list.get(position).getBOOK_AGREE());
		
		txt5 = (TextView)convertView.findViewById(R.id.approval_booknum);
		txt5.setText(book_list.get(position).getBOOK_NUM());

		return convertView;
	}
}
