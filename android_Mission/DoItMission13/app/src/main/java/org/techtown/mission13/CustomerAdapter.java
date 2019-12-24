package org.techtown.mission13;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder>
                            implements OnCustomerItemClickListener {
    ArrayList<Customer> items = new ArrayList<Customer>();

    OnCustomerItemClickListener listener;

    // Implement Methods - onCreateViewHolder, onBindViewHolder, getItemCount
    // onCreateViewHolder, onBindViewHolder - 뷰홀더 객체가 만들어 질때와 재사용될 때 자동 호출
    // int viewType - 각 아이템을 위한 뷰를 여러 가지로 나누어 보여주고 싶을 때 사용
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        // viewGroup.getContext() - Context 객체 참조
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.customer_item, viewGroup, false);

        // 뷰 객체를 새로 만든 뷰홀더 객체에 담아 반환
        return new ViewHolder(itemView, this);
    }

    // 스크롤하여 보이지 않게 된 뷰 객체를 새로 보일 쪽에 재사용할때 사용
    // ArrayList<Customer> items = new ArrayList<Customer>();
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Customer item = items.get(position);
        viewHolder.setItem(item);
    }

    // 어댑터에서 관리하는 아이템의 개수 반환
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Customer item) {
        items.add(item);
    }

    public void setItems(ArrayList<Customer> items) {
        this.items = items;
    }

    public Customer getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Customer item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnCustomerItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    // 리스트 형태로 보일 각각의 아이템은 뷰로 만들어지며 각각의 아이템을 위한 뷰는 뷰홀더에 담아 두게 된다
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;
        TextView textView3;
        ImageView imageView;

        // 뷰 홀더 생성자로 전달되는 뷰 객체 참조
        // RecyclerView.ViewHolder 클래스를 상속하여 정의된 ViewHolder 클래스의 생성자에는 뷰 객체가 전달된다.
        // 그리고 전달 받은 이 객체를 부모 클래스의 변수에 담아두게 되는데 생성자 안에서 super 메서드를 호출하면 된다.
        public ViewHolder(View itemView, final OnCustomerItemClickListener listener) {
            super(itemView);

            // 전달받은 뷰 객체의 이미지나 텍스트뷰를 findViewById 메서드로 찾아 변수에 할당
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        // 뷰홀더에 들어있는 뷰 객체의 데이터를 다른 것으로 보이도록 하는 역할
        public void setItem(Customer item) {
            textView.setText(item.getName());
            textView2.setText(item.getBirth());
            textView3.setText(item.getMobile());
            imageView.setImageResource(item.getResId());
        }

    }

}
