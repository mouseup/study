package org.techtown.sampleparcelable;

import android.os.Parcel;
import android.os.Parcelable;

//안드로이드에서 intent로 Object를 넘길때
//Object를 넘기기 위해서는 Serializable이랑 Parcelable이 있는데
//Android에서 성능적인 이슈로 Parcelable를 사용하는걸 추천한다.
//그래서 Parcelable interface를 구현해야한다.
//Parcelable를 사용하기 위해서는 몇가지 필수 사항이 있다.
//Constructor를 필수적으로 선언해야 한다.
//describeContents, writeToParcel를 override 해야한다.
//저장된 정보를 Parcel 로 부터 읽어들여 객체를 생성하는 역할을 하기위해 Creator를 구현해야 한다.


public class SimpleData implements Parcelable {

    int number;
    String message;

//  생성자 만들기
    public SimpleData(int num, String msg) {
        number = num;
        message = msg;
    }

//  parcel 객체에서 읽기 - 객체를 받았을 때 직렬화를 풀어줌
    public SimpleData(Parcel src) {
        number = src.readInt();
        message = src.readString();
    }

//  parcel 객체로부터 데이터를 읽어 들여 객체 생성
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

//      SimpleData 생성자를 호출해 parcel 객체에서 읽기
        public SimpleData createFromParcel(Parcel in) {
            return new SimpleData(in);
        }

        public SimpleData[] newArray(int size) {
            return new SimpleData[size];
        }

    };

//  직렬화하려는 객체의 유형을 구분 - 데이터가 어떤 종류인지 설명
//  Parcelable 객체가 file descriptor를 포함하고 있다면 CONTENTS_FILE_DESCRIPTOR를 리턴하고
//  그 외는 0을 리턴
    public int describeContents() {
        return 0;
    }

//  SimpleData 객체 안에 들어 있는 데이터를 parcel 객체로 만드는 역할
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(message);
    }

}
