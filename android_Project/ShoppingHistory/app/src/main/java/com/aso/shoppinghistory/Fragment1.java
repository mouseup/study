package com.aso.shoppinghistory;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.aso.shoppinghistory.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Fragment1 extends Fragment {
    private static final String TAG = "Fragment1";

    RecyclerView recyclerView;
    NoteAdapter adapter;

    Button fromDate;
    Button toDate;
    Context context;
    OnTabItemSelectedListener listener;
    Calendar c;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;

        if (context instanceof OnTabItemSelectedListener) {
            listener = (OnTabItemSelectedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (context != null) {
            context = null;
            listener = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);

        initUI(rootView);

        // 데이터 로딩
        loadNoteListData();

        return rootView;
    }



    private void initUI(ViewGroup rootView) {

        Date currentDate = new Date();
        String currentDateString = AppConstants.dateFormat5.format(currentDate);

        fromDate = rootView.findViewById(R.id.fromDate);
        fromDate.setText(AppConstants.getMonthBefore(1));
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //현재 날짜로 dialog를 띄우기 위해 날짜를 구함
                c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day=c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dateDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        c.set(year, monthOfYear, dayOfMonth);
                        String currentDateString = AppConstants.dateFormat5.format(c.getTime());
                        fromDate.setText(currentDateString );

                        // 데이터 로딩
                        loadNoteListData();

                    }
                }, year, month, day);
                dateDialog.show();

            }
        });


        toDate = rootView.findViewById(R.id.toDate);
        toDate.setText(currentDateString);
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //현재 날짜로 dialog를 띄우기 위해 날짜를 구함
                c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day=c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dateDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        c.set(year, monthOfYear, dayOfMonth);
                        String currentDateString = AppConstants.dateFormat5.format(c.getTime());
                        toDate.setText(currentDateString );

                        // 데이터 로딩
                        loadNoteListData();

                    }
                }, year, month, day);
                dateDialog.show();

            }
        });


        recyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        adapter = new NoteAdapter();

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnNoteItemClickListener() {
            @Override
            public void onItemClick(NoteAdapter.ViewHolder holder, View view, int position) {
                Note item = adapter.getItem(position);

                Log.d(TAG, "아이템 선택됨 : " + item.get_id());

                Toast.makeText(getContext(), "아이템 선택됨 : " + item.getContents(), Toast.LENGTH_LONG).show();
                if (listener != null) {
                    listener.showFragment2(item);
                }


            }
        });

    }

    /**
     * 리스트 데이터 로딩
     */
    public int loadNoteListData() {
        AppConstants.println("loadNoteListData called.");

        String sql = "select _id, CONTENTS, PICTURE, CREATE_DATE, MODIFY_DATE from " + NoteDatabase.TABLE_NOTE +
//                " where create_date > '" + fromDate.getText().toString() + "' " +
//                "  and create_date < '" + toDate.getText().toString() + "' " +
                " order by CREATE_DATE desc";


        int recordCount = -1;
        NoteDatabase database = NoteDatabase.getInstance(context);
        if (database != null) {
            Cursor outCursor = database.rawQuery(sql);

            recordCount = outCursor.getCount();
            AppConstants.println("record count : " + recordCount + "\n");

            ArrayList<Note> items = new ArrayList<Note>();

            for (int i = 0; i < recordCount; i++) {
                outCursor.moveToNext();

                int _id = outCursor.getInt(0);
                String contents = outCursor.getString(1);
                String picture = outCursor.getString(2);
                String dateStr = outCursor.getString(3);
                String createDateStr = null;
                if (dateStr != null && dateStr.length() > 10) {
                    try {
                        Date inDate = AppConstants.dateFormat4.parse(dateStr);
                        createDateStr = AppConstants.dateFormat3.format(inDate);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    createDateStr = "";
                }

                items.add(new Note(_id, contents, picture, dateStr));
            }

            outCursor.close();

            adapter.setItems(items);
            adapter.notifyDataSetChanged();

        }

        return recordCount;
    }


}
