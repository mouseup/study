package org.techtown.diary;

import android.view.View;

// 아이템 선택시 이벤트 처리
public interface OnNoteItemClickListener {
    public void onItemClick(NoteAdapter.ViewHolder holder, View view, int position);
}
