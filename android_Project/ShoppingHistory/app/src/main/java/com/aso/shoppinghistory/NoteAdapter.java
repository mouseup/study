package com.aso.shoppinghistory;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aso.shoppinghistory.R;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>
                            implements OnNoteItemClickListener {
    ArrayList<Note> items = new ArrayList<Note>();

    OnNoteItemClickListener listener;

    int layoutType = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.note_item, viewGroup, false);

        return new ViewHolder(itemView, this, layoutType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Note item = items.get(position);
        viewHolder.setItem(item);
        viewHolder.setLayoutType(layoutType);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Note item) {
        items.add(item);
    }

    public void setItems(ArrayList<Note> items) {
        this.items = items;
    }

    public Note getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnNoteItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public void switchLayout(int position) {
        layoutType = position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout2;

        ImageView moodImageView2;

        ImageView pictureImageView;

        ImageView weatherImageView2;

        TextView contentsTextView2;

        TextView locationTextView2;

        TextView dateTextView2;

        public ViewHolder(View itemView, final OnNoteItemClickListener listener, int layoutType) {
            super(itemView);


            layout2 = itemView.findViewById(R.id.layout2);

            moodImageView2 = itemView.findViewById(R.id.moodImageView2);

            pictureImageView = itemView.findViewById(R.id.pictureImageView);

            weatherImageView2 = itemView.findViewById(R.id.weatherImageView2);

            contentsTextView2 = itemView.findViewById(R.id.contentsTextView2);

            locationTextView2 = itemView.findViewById(R.id.locationTextView2);

            dateTextView2 = itemView.findViewById(R.id.dateTextView2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });

            setLayoutType(layoutType);
        }

        public void setItem(Note item) {
            String mood = item.getMood();
            int moodIndex = Integer.parseInt(mood);
            setMoodImage(moodIndex);

            String picturePath = item.getPicture();
            if (picturePath != null && !picturePath.equals("")) {
                pictureImageView.setVisibility(View.VISIBLE);
                pictureImageView.setImageURI(Uri.parse("file://" + picturePath));

            } else {
                pictureImageView.setVisibility(View.GONE);
                pictureImageView.setImageResource(R.drawable.noimagefound);

            }

            // set weather
            String weather = item.getWeather();
            int weatherIndex = Integer.parseInt(weather);
            setWeatherImage(weatherIndex);

            contentsTextView2.setText(item.getContents());

            locationTextView2.setText(item.getAddress());

            dateTextView2.setText(item.getCreateDateStr());
        }

        public void setMoodImage(int moodIndex) {
            switch(moodIndex) {
                case 0:
                    moodImageView2.setImageResource(R.drawable.smile1_48);
                    break;
                case 1:
                    moodImageView2.setImageResource(R.drawable.smile2_48);
                    break;
                case 2:
                    moodImageView2.setImageResource(R.drawable.smile3_48);
                    break;
                case 3:
                    moodImageView2.setImageResource(R.drawable.smile4_48);
                    break;
                case 4:
                    moodImageView2.setImageResource(R.drawable.smile5_48);
                    break;
                default:
                    moodImageView2.setImageResource(R.drawable.smile3_48);
                    break;
            }
        }

        public void setWeatherImage(int weatherIndex) {
            switch(weatherIndex) {
                case 0:
                    weatherImageView2.setImageResource(R.drawable.weather_icon_1);
                    break;
                case 1:
                    weatherImageView2.setImageResource(R.drawable.weather_icon_2);
                    break;
                case 2:
                    weatherImageView2.setImageResource(R.drawable.weather_icon_3);
                    break;
                case 3:
                    weatherImageView2.setImageResource(R.drawable.weather_icon_4);
                    break;
                case 4:
                    weatherImageView2.setImageResource(R.drawable.weather_icon_5);
                    break;
                case 5:
                    weatherImageView2.setImageResource(R.drawable.weather_icon_6);
                    break;
                case 6:
                    weatherImageView2.setImageResource(R.drawable.weather_icon_7);
                    break;
                default:
                    weatherImageView2.setImageResource(R.drawable.weather_icon_1);
                    break;
            }
        }

        public void setLayoutType(int layoutType) {
                layout2.setVisibility(View.VISIBLE);
        }

    }

}
