package org.techtown.movie;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    ArrayList<Movie> items = new ArrayList<Movie>();

    // onCreateViewHolder - 뷰홀더가 만들어지는 시점
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        // movie_item.xml 인플레이션
        View itemView = inflater.inflate(R.layout.movie_item, viewGroup, false);
        // 뷰홀더 객체 생성 및 반환
        return new ViewHolder(itemView);
    }

    // 현재 인덱스에 맞는 Movie 객체를 찾아 뷰홀더에 객체 설정
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Movie item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Movie item) {
        items.add(item);
    }

    public void setItems(ArrayList<Movie> items) {
        this.items = items;
    }

    public Movie getItem(int position) {
        return items.get(position);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;

        public ViewHolder(View itemView) {
            super(itemView);
            // 리스트형태로 보일 때 각각의 아이템은 뷰로 만들어지며 이 각각의 아이템을 위한 뷰는 뷰 홀더에 담아 두게 된다.
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
        }

        // 이 뷰홀더 안에는 Movie 객체가 담기게 되므로 setItem() 메서드의 파라미터로 Movie 객체 전달
        public void setItem(Movie item) {
            // 영화 이름
            textView.setText(item.movieNm);
            // 관객수
            textView2.setText(item.audiCnt + " 명");
        }

    }

}
