package org.techtown.mission13;


// 어댑터 안에 들어갈 각 아이템의 데이터를 담아둘 클래스
public class Customer {
    String name;
    String birth;
    String mobile;
    int resId;

    public Customer(String name, String birth, String mobile) {
        this.name = name;
        this.birth = birth;
        this.mobile = mobile;
    }

    public Customer(String name, String birth, String mobile, int resId) {
        this.name = name;
        this.birth = birth;
        this.mobile = mobile;
        this.resId = resId;
    }

    // Getter and Setter

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
