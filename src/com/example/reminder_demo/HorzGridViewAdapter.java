package com.example.reminder_demo;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HorzGridViewAdapter extends BaseAdapter {

	List<String> listDay;
	List<String> listDayOfWeek;
	HashMap<String, Integer> hm_DayOfWeek;
	LayoutInflater inflater;
	Context mContext;

	// HorzGridView stuff
	private int columns;// Used to set childSize in TwoWayGridView
	private int rows;// used with TwoWayGridView
	private int itemPadding;
	private int columnWidth;
	private int rowHeight;

	public HorzGridViewAdapter(List<String> listDay, List<String> listDayOfWeek,
			HashMap<String, Integer> hm_DayOfWeek, LayoutInflater inflater,
			Context mContext) {
		super();
		this.listDay = listDay;
		this.listDayOfWeek = listDayOfWeek;
		this.hm_DayOfWeek = hm_DayOfWeek;
		this.inflater = inflater;
		this.mContext = mContext;
		// Get dimensions from values folders; note that the value will change
		// based on the device size but the dimension name will remain the same
		Resources res = mContext.getResources();
		itemPadding = (int) res.getDimension(R.dimen.horz_item_padding);
		int[] rowsColumns = res.getIntArray(R.array.horz_gv_rows_columns);
		rows =1;
		columns = listDay.size();

		// Initialize the layout params
		MainActivity.mListView.setNumRows(rows);

		// HorzGridView size not established yet, so need to set it using a
		// viewtreeobserver
		ViewTreeObserver vto = MainActivity.mListView.getViewTreeObserver();

		OnGlobalLayoutListener onGlobalLayoutListener = new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				// First use the gridview height and width to determine child
				// values
				//rowHeight = (int) ((float) (MainActivity.mListView.getHeight() / rows) - 2 * itemPadding);
				rowHeight=500;
				columnWidth = (int) ((float) (MainActivity.mListView.getWidth() / columns) - 2 * itemPadding);

				MainActivity.mListView.setRowHeight(rowHeight);

				// Then remove the listener
				ViewTreeObserver vto = MainActivity.mListView
						.getViewTreeObserver();

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					vto.removeOnGlobalLayoutListener(this);
				} else {
					vto.removeGlobalOnLayoutListener(this);
				}

			}
		};

		vto.addOnGlobalLayoutListener(onGlobalLayoutListener);
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {


		if (v == null) {

			v = inflater.inflate(R.layout.item_date, parent,false);
		}
		//get data from xml
		TextView tv_day = (TextView) v.findViewById(R.id.tv_day);
		TextView tv_day_of_week = (TextView) v
				.findViewById(R.id.tv_day_of_week);
		TextView tv_content=(TextView)v.findViewById(R.id.tv_content);
		int keyDay = hm_DayOfWeek.get(listDayOfWeek.get(position));
		tv_content.setText("");
		//set color background content
		if (keyDay==7 || keyDay==1) {
			//saturday and sunday
			tv_content.setBackgroundResource(R.color.background_weekend);
		}	else{
			tv_content.setBackgroundResource(R.color.background_grid);
		}
		
		tv_day.setText(listDay.get(position));
	
		if (keyDay == 7) {
			tv_day_of_week.setBackgroundResource(R.drawable.bg_saturday);
		} else if (keyDay == 1) {
			tv_day_of_week.setBackgroundResource(R.drawable.bg_sunday);
		} else {
			tv_day_of_week.setBackgroundColor(Color.WHITE);
		}
		tv_day_of_week.setText(listDayOfWeek.get(position));
		
		//  tv_day_of_week.setText(hm_DayOfWeek.get(listDayOfWeek.get(position))+ "");
		

		return v;
	
	}

	private class ViewHandler {
		TextView tv_day;
		TextView tv_dayofmonth;
	}

	@Override
	public int getCount() {

		return listDayOfWeek.size();
	}

	@Override
	public Object getItem(int position) {

		return listDayOfWeek.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

}
