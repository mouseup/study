package com.aso.shoppinghistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>
                            implements OnCategoryItemClickListener {
    ArrayList<Category> items = new ArrayList<Category>();


    OnCategoryItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.category_item, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Category item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Category item) {
        items.add(item);
    }

    public void setItems(ArrayList<Category> items) {
        this.items = items;
    }

    public Category getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Category item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnCategoryItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textCategory;

        public ViewHolder(View itemView, final OnCategoryItemClickListener listener) {
            super(itemView);

            textCategory= itemView.findViewById(R.id.textCategory);

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

        public void setItem(Category item) {
            textCategory.setText(item.geteditCategory());
        }

    }

}
