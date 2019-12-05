package org.techtown.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertPerson();
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryPerson();
            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePerson();
            }
        });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePerson();
            }
        });
    }

    public void insertPerson() {
        println("insertPerson 호출됨");

        // Uri 는 내용 제공자를 정의할 때 만들었던 Uri 값을 사용한다.
        String uriString = "content://org.techtown.provider/person";

        // uri 객체 생성
        Uri uri = new Uri.Builder().build().parse(uriString);

        // getContentResolver().query 호출하면서 uri 객체를 파라미터로 전달
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        String[] columns = cursor.getColumnNames();
        println("columns count -> " + columns.length);
        for (int i = 0; i < columns.length; i++) {
            println("#" + i + " : " + columns[i]);
        }

        // 레코드를 추가할때 ContentValues 객체 사용
        ContentValues values = new ContentValues();
        values.put("name", "john");
        values.put("age", 20);
        values.put("mobile", "010-1000-1000");

        uri = getContentResolver().insert(uri, values);
        println("insert 결과 -> " + uri.toString());
    }

    public void queryPerson() {
        try {
            String uriString = "content://org.techtown.provider/person";
            Uri uri = new Uri.Builder().build().parse(uriString);

            String[] columns = new String[] {"name", "age", "mobile"};
            Cursor cursor = getContentResolver().query(uri, columns, null, null, "name ASC");
            println("query 결과 : " + cursor.getCount());

            int index = 0;
            while(cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(columns[0]));
                int age = cursor.getInt(cursor.getColumnIndex(columns[1]));
                String mobile = cursor.getString(cursor.getColumnIndex(columns[2]));

                println("#" + index + " -> " + name + ", " + age + ", " + mobile);
                index += 1;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePerson() {
        String uriString = "content://org.techtown.provider/person";
        Uri uri = new Uri.Builder().build().parse(uriString);

        // mobile 칼럼의 값이 010-1000-1000 인 레코드만 010-2000-2000로 수정
        // ? - selectionArgs 배열 변수의 첫 번째 원소로 대체
        String selection = "mobile = ?";
        String[] selectionArgs = new String[] {"010-1000-1000"};
        ContentValues updateValue = new ContentValues();
        updateValue.put("mobile", "010-2000-2000");

        int count = getContentResolver().update(uri, updateValue, selection, selectionArgs);
        println("update 결과 : " + count);
    }

    public void deletePerson() {
        String uriString = "content://org.techtown.provider/person";
        Uri uri = new Uri.Builder().build().parse(uriString);

        String selection = "name = ?";
        String[] selectionArgs = new String[] {"john"};

        int count = getContentResolver().delete(uri, selection, selectionArgs);
        println("delete 결과 : " + count);
    }

    public void println(String data) {
        textView.append(data + "\n");
    }
}
