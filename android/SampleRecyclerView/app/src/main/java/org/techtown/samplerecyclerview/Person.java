package org.techtown.samplerecyclerview;

import android.util.Log;

// 어댑터 안에 들어갈 각 아이템의 데이터를 담아둘 클래스 정의
public class Person {

    String name;
    String mobile;

    public Person(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;

    }



    public String getName() {        return name;    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
