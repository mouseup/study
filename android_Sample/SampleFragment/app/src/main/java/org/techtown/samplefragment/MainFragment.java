package org.techtown.samplefragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // ViewGroup rootView
        // - 최상위 레이아웃은 메인 프래그먼트 안에 들어 있는 것이고 메인 프레그먼트는 이 레이아웃을 화면에 보여주기 위한 틀
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        // rootView의 findViewById() 메서드를 사용하여 레이아웃에 들어있는 버튼 객체를 찾아낸다.
        // 이 객체의 setOnClickListener() 메서드를 호출하여 리스너를 등록하면 버튼이 클릭되었을때 이벤트를 처리할수 있다.
        Button button = rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            // MainActivity 객체를 참조한후 onFragmentChanged() 메서드 호출
            @Override
            public void onClick(View v) {
                // getActivity() 메서드를 호출하면 프래그먼트가 올라가 있는 액티비티가 어떤 것인지 확인할수 있다.
                MainActivity activity = (MainActivity) getActivity();
                // onFragmentChanged() - 메인 액티비티에 새로 추가한 메서드
                // - 프래그먼트 메니저를 이용해 프래그먼트를 전환하는 메서드
                // - 하나의 프래그먼트에서 다른 프래그먼트를 직접 띄우는 것이 아니라 액티비티를 통해 띄워야 함
                activity.onFragmentChanged(0);
            }
        });

        return rootView;
    }


}
