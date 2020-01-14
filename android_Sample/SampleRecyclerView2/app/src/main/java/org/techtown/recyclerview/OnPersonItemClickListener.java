package org.techtown.recyclerview;

import android.view.View;

// 어댑터의 외부, 즉, 액티비티 또는 프래그먼트에서 아이템 클릭 이벤트를 처리하고 싶은 경우 커스텀 인터페이스 정의
public interface OnPersonItemClickListener {
    public void onItemClick(PersonAdapter.ViewHolder holder, View view, int position);
}

