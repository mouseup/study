package com.aso.shoppinghistory;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Fcategory extends Fragment {

    EditText editCategory ;
    TextView categoryCount;

    RecyclerView recyclerView;
    CategoryAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fcategory, container, false);

        initUI(rootView);

        return rootView;

    }


    private void initUI(ViewGroup rootView) {

        editCategory = rootView.findViewById(R.id.editCategory);
        categoryCount = rootView.findViewById(R.id.categoryCount);

        recyclerView = rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        adapter = new CategoryAdapter();

//        adapter.addItem(new Category("김준수", "1995-10-20", "010-1000-1000"));
//        adapter.addItem(new Category("이희연", "1994-02-13", "010-2000-2000"));

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnCategoryItemClickListener() {
            @Override
            public void onItemClick(CategoryAdapter.ViewHolder holder, View view, int position) {
                Category item = adapter.getItem(position);

//                Toast.makeText(getApplicationContext(), "아이템 선택됨 : " + item.getName(), Toast.LENGTH_LONG).show();
            }
        });

        Button buttonAdd = rootView.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapter.addItem(new Category(editCategory.getText().toString()));
                adapter.notifyDataSetChanged();

                categoryCount.setText(adapter.getItemCount() + " 개");
            }
        });



    }


}