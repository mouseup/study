package org.techtown.mission14;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        // 리싸이클러뷰가 격자형태로 보이도록
        // 2 - 컬럼 갯수
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);


        adapter = new ProductAdapter();

        adapter.addItem(new Product("토끼털 코트", "리본", 7, 574770, R.drawable.clothes1));
        adapter.addItem(new Product("어깨 패치 H라인 원피스", "헤지스레이디스", 19, 297580, R.drawable.clothes2));
        adapter.addItem(new Product("그린체크 패턴 소매배색 긴팔 남방", "헤지스레이디스", 16, 144500, R.drawable.clothes3));
        adapter.addItem(new Product("네이비 마드라스 체크 퍼피 남방", "헤지스레이디스", 16, 170150, R.drawable.clothes4));
        adapter.addItem(new Product("토끼털 코트", "리본", 7, 574770, R.drawable.clothes1));
        adapter.addItem(new Product("어깨 패치 H라인 원피스", "헤지스레이디스", 19, 297580, R.drawable.clothes2));
        adapter.addItem(new Product("그린체크 패턴 소매배색 긴팔 남방", "헤지스레이디스", 16, 144500, R.drawable.clothes3));
        adapter.addItem(new Product("네이비 마드라스 체크 퍼피 남방", "헤지스레이디스", 16, 170150, R.drawable.clothes4));

        recyclerView.setAdapter(adapter);

        // 어댑터에 리스너 설정하기
        adapter.setOnItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onItemClick(ProductAdapter.ViewHolder holder, View view, int position) {

                // 아이템 클릭 시 어댑터에서 해당 아이템의 Product 객체 가져오기
                Product item = (Product) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택된 제품 : " + item.getName(), Toast.LENGTH_LONG).show();

            }
        });

    }
}
