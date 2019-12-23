package org.techtown.mission10;

import android.os.Bundle;

// 어떤 프래그먼트를 보여줄지 선택하는 메서드 포함
public interface FragmentCallback {

    public void onFragmentSelected(int position, Bundle bundle);

}
