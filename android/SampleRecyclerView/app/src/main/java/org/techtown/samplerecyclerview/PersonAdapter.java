package org.techtown.samplerecyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

// PersonAdapter 클래스가 RecyclerView.Adapter 클래스 상속
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    // 데이터는 Person 객체로 만드는데 여러 아이템을 이 어댑터에서 관리해야 하기 때문에
    //  클래스 안에 ArrayList 자료형으로 된 items 라는 변수를 하나 만든다
    ArrayList<Person> items = new ArrayList<Person>();

    // 뷰홀더 객체가 만들어질때 자동 호출
    // person_item.xml 레이아웃을 이용해 뷰 객체 만들어준다.
    // 그리고 뷰 객체를 새로 만든 뷰 홀더 객체에 담아 반환한다.
    // int viewType - 각 아이템을 위한 뷰를 여러 가지로 나누어 보여주고 싶을 때 사용
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.d("MyButton", "onCreateViewHolder 호출");

        // 인플레이션을 진행하기 위해서는 Context 객체가 필요한데 파라미터로 전달되는 뷰그룹 객체의
        // viewGroup.getContext() 메서드를 이용하여 Context 객체참조
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        // 파라미터로 전달되는 뷰그룹 객체는 각 아이템을 위한 뷰그룹 객체이므로
        // xml 레이아웃을 인플레이션하여 이 뷰그룹 객체에 설정
        View itemView = inflater.inflate(R.layout.person_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    // 뷰홀더 객체가 재사용 될때 자동 호출
    // 사용자가 스크롤하여 보이지 않게 된 뷰 객체를 새로 보일 쪽에 재사용할때 사용
    // 뷰홀더가 재사용될 때 호출되므로 뷰 객체는 기존 것을 그대로 사용하고 데이터만 바꿔준다.
    // 재활용할 수 있는 뷰홀더 객체를 파라미터로 전달하기 때문에
    // 그 뷰홀더에 현재 아이템에 맞는 데이터만 설정
    // position 파라미터를 이용해 ArrayList에서 Person 객체를 꺼내어 설정할 수 있다.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.d("MyButton", "onBindViewHolder 호출");
        Person item = items.get(position);
        viewHolder.setItem(item);
    }

    // 어댑터에서 관리하는 아이템의 개수 반환
    // 어댑터가 ArrayList 안에 들어 있는 전체 아이템의 개수를 알아야 하므로
    // getItemCount() 는 ArrayList의 size()를 호출한다
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


    // 리스트 형태로 보일 때 각각의 아이템은 뷰로 만들어지며
    // 각각의 아이템을 위한 뷰는 뷰홀더에 담아 두게 된다.
    // 이 뷰홀더 역할을 하는 클래스를 PersonAdapter 클래스 안에 넣어둔다
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;

        // 뷰홀더 생성자로 전달되는 뷰 객체 참조하기
        // RecyclerView.ViewHolder 클래스를 상속하여 정의된 ViewHolder 클래스의 생성자에는
        // 뷰 객체가 전달된다.
        public ViewHolder(View itemView) {

            // 전달 받은 이 객체를 부모 클래스의 변수에 담아두게 된다
            super(itemView);
            Log.d("MyButton", "ViewHolder 호출");

            // 전달 받은 뷰 객체에 들어 있는 텍스트뷰 참조하기
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
        }

        // 전달받은 뷰 객체의 이미지나 텍스트뷰를 findViewById 메서드로 찾아 변수에 할당하면
        // setItem 메서드는 이 뷰홀더에 들어 있는 뷰 객체의 데이터를 다른 것으로 보이도록 한다.
        public void setItem(Person item) {
            Log.d("MyButton", "setItem 호출");
            textView.setText(item.getName());
            textView2.setText(item.getMobile());
        }

    }

}
