package org.techtown.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    EditText editText2;
    TextView textView;

    SQLiteDatabase database;

    String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String databaseName = editText.getText().toString();
                createDatabase(databaseName);
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableName = editText2.getText().toString();
                createTable(tableName);

                insertRecord();
            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableName = editText2.getText().toString();
                selectData(tableName);

            }
        });



    }

    public void selectData(String tableName){
        println("selectData 호출됨");
        if (database != null) {
            String sql = "select name, age, mobile from " + tableName;
            Cursor cursor = database.rawQuery(sql, null);
            println("조회된 데이터 개수 : " + cursor.getCount());

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                String name = cursor.getString(0);
                int age = cursor.getInt(1);
                String mobile = cursor.getString(2);

                println("#" + i + " -> " + name + ", " + age + ", " + mobile );

            }

            cursor.close();
        }


    }


    private void createDatabase(String name) {
        println("createDatabase 호출됨.");

        // 데이터베이스를 만들기 위한 메서드 실행
        // name- 데이타베이스 이름
        database = openOrCreateDatabase(name, MODE_PRIVATE, null);

        println("데이터베이스 생성함 : " + name);
    }

    private void createTable(String name) {
        println("createTable 호출됨.");

        if (database == null) {
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }

        // 테이블을 만들기 위한 sql 실행
        // _id - 내부생성
        database.execSQL("create table if not exists " + name + "("
            + " _id integer PRIMARY KEY autoincrement, "
            + " name text, "
            + " age integer, "
            + " mobile text)");

        println("테이블 생성함 : " + name);
    }

    private void insertRecord() {
        println("insertRecord 호출됨.");

        if (database == null) {
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }

        if (tableName == null) {
            println("테이블을 먼저 생성하세요.");
            return;
        }

        database.execSQL("insert into " + tableName
                        + "(name, age, mobile) "
                        + " values "
                        + "('John', 20, '010-1000-1000')");

        println("레코드 추가함.");
    }

    public void println(String data) {
        textView.append(data + "\n");
    }

}
