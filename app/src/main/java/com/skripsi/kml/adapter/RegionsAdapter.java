package com.skripsi.kml.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skripsi.kml.R;
import com.skripsi.kml.model.Regions;

import java.util.ArrayList;

public class RegionsAdapter extends BaseAdapter {

    Context context;
    ArrayList<Regions> list = new ArrayList<Regions>();

	public RegionsAdapter(Context c, ArrayList<Regions> list){
		context = c;
		this.list = list;;
	}
	public int getCount() {
		return list.size();
	}

	public Object getItem(int arg0) {
		return arg0;
	}

	public long getItemId(int arg0) {
		return arg0;
	}
	
	@Override
	public View getView(final int position, View arg1, ViewGroup arg2) {
		LayoutInflater inflater = (LayoutInflater) context
    			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView = new View(context);
		gridView = inflater.inflate(R.layout.model_region, null);
		TextView titleTV = (TextView)gridView.findViewById(R.id.titleTV);
		TextView descTV = (TextView)gridView.findViewById(R.id.descTV);
		titleTV.setText(list.get(position).getTitle());
		descTV.setText(list.get(position).getDescription());
    	return gridView;
	}

}