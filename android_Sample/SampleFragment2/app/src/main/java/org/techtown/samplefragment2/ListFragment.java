package org.techtown.samplefragment2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ListFragment extends Fragment {

    // 화면에서 선택된 버튼에 따라 다른 프래그먼트의 이미지를 바꿔주려면 액티비티 쪽으로 데이터를 전달해야 하므로
    // 액티비티에 onImageSelected() 메서드를 정의 한 후 그 메서드를 호출하도록 한다.
    // 그런데 매번 액티비티마다 다른 이름의 메서드를 만들면 프래그먼트가 올라간 액티비티가 다른 액티비티로 변경되었을때
    // 해당 액티비티가 무엇인지 매번 확인해야하는 번거로움이 있다.
    // 이 때문에 인터페이스를 하나 정의한 후에 액티비티가 이 인터페이스를 구현하도록 하는것이 좋다
    public static interface ImageSelectionCallback {
        public void onImageSelected(int position);
    }

    public ImageSelectionCallback callback;

    // 프래그먼트가 액티비티 위에 올라오는 시점에 호출
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // callback 변수에 객체 할당
        if (context instanceof ImageSelectionCallback) {
            callback = (ImageSelectionCallback) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, container, false);

        Button button = rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onImageSelected(0);
                }
            }
        });

        Button button2 = rootView.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onImageSelected(1);
                }
            }
        });

        Button button3 = rootView.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onImageSelected(2);
                }
            }
        });

        return rootView;
    }

}
