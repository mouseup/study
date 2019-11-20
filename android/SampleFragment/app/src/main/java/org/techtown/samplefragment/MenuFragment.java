package org.techtown.samplefragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


public class MenuFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 첫 번째 파라미터는 xml 레이아웃 파일이 되므로 R.layout.fragment_menu이 입력
        // 두 번째 파라미터는 이 xml 레이아웃이 설정될 뷰그룹 객체 - onCreateView() 메서드로 전달되는
        // 두 번째 파라미터가 이 프래그먼트의 가장 상위 레이아웃이므로 container 객체를 전달하면 된다
        // inflate() 메서드를 호출하면 인플레이션이 진행되고 그 그결과로 ViewGroup 객체가 반환
        // 이 객체를 return 키워드를 사용하여 반환
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }


}
