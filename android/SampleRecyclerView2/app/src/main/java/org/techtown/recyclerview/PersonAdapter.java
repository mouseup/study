package org.techtown.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

// 어댑터 클래스가 새로 정의한 리스너 인터페이스 구현
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder>
                            implements OnPersonItemClickListener {
    ArrayList<Person> items = new ArrayList<Person>();

    OnPersonItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.person_item, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Person item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Person item) {
        items.add(item);
    }

    public void setItems(ArrayList<Person> items) {
        this.items = items;
    }

    public Person getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Person item) {
        items.set(position, item);
    }

    // 외부에서 리스너를 설정할 수 있도록 메서드 추가
    public void setOnItemClickListener(OnPersonItemClickListener listener) {
        this.listener = listener;
    }

    // 인터페이스에서 정의한 onItemClick 메서드 추가
    // 뷰홀더 클래스 안에서 뷰가 클릭되었을 때 호출
    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;

        public ViewHolder(View itemView, final OnPersonItemClickListener listener) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);

            // itemView에 OnClickListener 설정
//            아이템 뷰에서 클릭 이벤트를 직접 처리하고,
//            아이템 뷰는 뷰홀더 객체가 가지고 있으니,
//            아이템 클릭 이벤트는 뷰홀더에서 작성.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // 뷰홀더에 표시할 아이템이 어댑터에서 몇 번째인지 정보 반환
                    int position = getAdapterPosition();

                    // 아이템 뷰 클릭 시 미리 정의한 다른 리스너의 메서드 호출하기
                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Person item) {
            textView.setText(item.getName());
            textView2.setText(item.getMobile());
        }

    }

}
