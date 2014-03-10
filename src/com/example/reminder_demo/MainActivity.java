package com.example.reminder_demo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reminder_demo.scrollview.TwoWayAbsListView;
import com.example.reminder_demo.scrollview.TwoWayGridView;
/**
 * Main Activity
 * @author Nhampd
 *
 */
public class MainActivity extends Activity {

	public static TwoWayGridView mListView;
	TextView tv_month;
	LayoutInflater inflater;
	HorzGridViewAdapter adapter;

	GridView grid;

	Calendar mCalendar;

	List<String> listDay = new ArrayList<String>();// mảng chứa ngày của tháng
	HashMap<String, Integer> hm_DayOfMonth = new HashMap<String, Integer>();// lưu
																			// thứ
																			// và
																			// key
																			// tương
																			// ứng
																			// của
																			// nó
	List<String> listDayOfWeek = new ArrayList<String>();

	// -------------------------------------------------------//
	// ------mảng chứa các kiểu ngày của các tháng---//
	List<String> list28 = new ArrayList<String>();
	List<String> list29 = new ArrayList<String>();
	List<String> list30 = new ArrayList<String>();
	List<String> list31 = new ArrayList<String>();
	// ------------------------------------------------------//

	// ngày tháng năm hiện tại
	public static int current_Month;
	public static int current_Year;
	public static int current_Day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		// set time zone japan
		// mCalendar= new GregorianCalendar(TimeZone.getTimeZone("Japan"));
		mCalendar = Calendar.getInstance();
		Date dateCurrent = mCalendar.getTime();
		SimpleDateFormat dayFormat = new SimpleDateFormat("MMMM",Locale.JAPAN);
		String dayCurrent = dayFormat.format(dateCurrent);

		createList();
		getCurrentDate();
		fitDayInMonth(current_Month);
		getListDayOfWeek();

		// Log.d("day", listDay.length+"");
		// Log.d("dow", listDayOfWeek.length+"");
		//
		tv_month = (TextView) findViewById(R.id.tv_month);
		tv_month.setText(dayCurrent);

		mListView = (TwoWayGridView) findViewById(R.id.horz_gridview);
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		adapter = new HorzGridViewAdapter(listDay, listDayOfWeek,
				hm_DayOfMonth, inflater, this);
		mListView.setAdapter(adapter);
		mListView.setSelection(current_Day - 1);
		mListView.setOnScrollListener(new TwoWayAbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(TwoWayAbsListView view,
					int scrollState) {
				// TODO Auto-generated method stub
				// if(scrollState==0)
				// Toast.makeText(getApplicationContext(), "item ="+scrollState,
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onScroll(TwoWayAbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					nextMonth();
				}
			}
		});

	}

	/*
	 * khởi tạo danh sách ngày
	 */
	void createList() {
		// list28 = getResources().getStringArray(R.array.list28);
		// list29 = getResources().getStringArray(R.array.list29);
		// list30 = getResources().getStringArray(R.array.list30);
		// list31 = getResources().getStringArray(R.array.list31);

		for (int i = 1; i <= 28; i++) {
			list28.add(i + "");
		}
		for (int i = 1; i <= 29; i++) {
			list29.add(i + "");
		}
		for (int i = 1; i <= 30; i++) {
			list30.add(i + "");
		}
		for (int i = 1; i <= 31; i++) {
			list31.add(i + "");
		}
	}

	int pos_listDay = 0;

	/**
	 * Tháng tiếp theo
	 */
	void nextMonth() {
		pos_listDay = listDay.size();
		if (current_Month < 11) {
			current_Month++;
		} else {
			current_Month = 0;
			current_Year++;
		}

		// settext title
		mCalendar.set(current_Year, current_Month, 1);
		Long time = mCalendar.getTimeInMillis();
		Date dateCurrent = new Date(time);
		SimpleDateFormat dayFormat = new SimpleDateFormat("MMMM",Locale.JAPAN);
		String dayCurrent = dayFormat.format(dateCurrent);
		tv_month.setText(dayCurrent + "-" + current_Year);

		fitDayInMonth(current_Month);
		getListDayOfWeek();
		adapter = new HorzGridViewAdapter(listDay, listDayOfWeek,
				hm_DayOfMonth, inflater, this);
		mListView.setAdapter(adapter);
		mListView.setSelection(pos_listDay - 1);
	}

	/*
	 * lấy ngày tháng năm hiện tại
	 */
	void getCurrentDate() {
		current_Year = mCalendar.get(Calendar.YEAR);
		current_Month = mCalendar.get(Calendar.MONTH);
		current_Day = mCalendar.get(Calendar.DAY_OF_MONTH);
		Toast.makeText(getApplicationContext(), "current " + current_Month,
				Toast.LENGTH_SHORT).show();
		// current_Year = 2014;
		// current_Month = 2;
		// current_Day = 3;
	}

	/*
	 * get day of week by date
	 */
	String getDayOfWeek_ByDate(int year, int month, int day) {
		// month--;
		// day--;
		mCalendar.set(year, month, day);
		Long time = mCalendar.getTimeInMillis();
		Date date = new Date(time);
		 SimpleDateFormat formatDayOfWeek = new SimpleDateFormat("EEE",Locale.JAPAN);
		return formatDayOfWeek.format(date);
	}

	/*
	 * get list day of week
	 */
	void getListDayOfWeek() {
		for (int i = pos_listDay; i < listDay.size(); i++) {
			int day = Integer.parseInt(listDay.get(i));
			String dayweek = getDayOfWeek_ByDate(current_Year, current_Month,
					day);
			listDayOfWeek.add(dayweek);
			mCalendar.set(current_Year, current_Month, day);

			hm_DayOfMonth.put(dayweek, mCalendar.get(Calendar.DAY_OF_WEEK));
		}
	}

	// danh sach ngay cho tung tháng
	void fitDayInMonth(int month) {
		month++;
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			for (String st : list31) {

				listDay.add(st);
			}
		}

		if (month == 4 || month == 6 || month == 9 || month == 11) {
			for (String st : list30) {

				listDay.add(st);
			}
		}

		if (month == 2) {
			if (Nhuan(current_Year)) {
				for (String st : list29) {
					listDay.add(st);
				}
			} else {
				for (String st : list28) {
					listDay.add(st);
				}
			}
		}
	}

	/*
	 * Kiểm tra năm nhuân
	 */
	boolean Nhuan(int year) {
		if (year % 4 == 0) {
			return true;
		} else {
			return false;
		}
	}
}
