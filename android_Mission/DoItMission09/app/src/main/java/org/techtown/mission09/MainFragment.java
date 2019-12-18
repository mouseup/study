package org.techtown.mission09;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainFragment extends Fragment {
    EditText nameInput;
    EditText ageInput;

    Button birthButton;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

    Date selectedDate;

    public MainFragment() {

    }


    // 프래그먼트에는 setContentView 메서드가 없다
    // 그래서 인플레이션 객체인 LayoutInflater 를 사용해 인플레이션을 진행해야 한다.
    // onCreateView - 인플레이션이 필요한 시점에 자동으로 호출
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate 메서드가 호출되면 인플레이션 과정이 끝나고 프래그먼트가 하나의 뷰처럼 동작한다.
        // ViewGroup 객체 리턴  - 이 프래그먼트의 가장 상위 레이아웃
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);


        nameInput = rootView.findViewById(R.id.nameInput);
        ageInput = rootView.findViewById(R.id.ageInput);

        birthButton = rootView.findViewById(R.id.birthButton);
        birthButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // 날짜 선택
                showDateDialog();
            }
        });

        Button saveButton = rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nameStr = nameInput.getText().toString();
                String ageStr = ageInput.getText().toString();
                String birthStr = birthButton.getText().toString();

                Toast.makeText(getContext(), "이름 : " + nameStr + ", 나이 : " + ageStr + ", 생년월일 : " + birthStr, Toast.LENGTH_SHORT).show();
            }
        });


        // set selected date using current date
        Date curDate = new Date();
        setSelectedDate(curDate);

        // ViewGroup 객체 리턴 - 프래그먼트 화면으로 보이게 됨
        return rootView;
    }

    private void showDateDialog() {
        String birthDateStr = birthButton.getText().toString();

        Calendar calendar = Calendar.getInstance();
        Date curBirthDate = new Date();
        try {
            curBirthDate = dateFormat.parse(birthDateStr);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        calendar.setTime(curBirthDate);

        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH);
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext(),  birthDateSetListener,  curYear, curMonth, curDay);
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener birthDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(Calendar.YEAR, year);
            selectedCalendar.set(Calendar.MONTH, monthOfYear);
            selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Date curDate = selectedCalendar.getTime();
            setSelectedDate(curDate);
        }
    };

    private void setSelectedDate(Date curDate) {
        selectedDate = curDate;

        String selectedDateStr = dateFormat.format(curDate);
        birthButton.setText(selectedDateStr);
    }

}
