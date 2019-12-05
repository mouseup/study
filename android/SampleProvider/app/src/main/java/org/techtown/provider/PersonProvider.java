package org.techtown.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

// 내용 제공자 클래스
// PersonProvider 클래스에는 insert, query, update, delete 메서드 정의
public class PersonProvider extends ContentProvider {

    private static final String AUTHORITY = "org.techtown.provider";
    private static final String BASE_PATH = "person";

    // 내용 제공자를 만들기 위한 고유한 값을 가진 CONTENT_URI 를 만들어야 한다.
    // content:// - 내용 제공자에 의해 제어되는 데이터라는 의미로 항상 content:// 로 시작
    // AUTHORITY - 특정 내용 제공자를 구분하는 고유의 값
    // BASE_PATH - 요청할 데이터의 자료형을 결정(여기서는 테이블 이름)
    // ID - 맨 뒤의 1과 같은 숫자를 가리키며 요청할 데이터 레코드를 지정
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    private static final int PERSONS = 1;
    private static final int PERSON_ID = 2;

    // UriMatcher  - URI를 매칭하는데 사용
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, PERSONS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", PERSON_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DatabaseHelper helper = new DatabaseHelper(getContext());
        database = helper.getWritableDatabase();

        return true;
    }

    // Uri uri,
    // String[] strings, - 어떤 칼럼들을 조회할 것인지(null - 모든 칼럼 조회)
    // String s, - where 조건 지정(null - 조건 없음)
    // String[] strings1,  - where 조건 있을 경우 그 안에 들어갈 조건 값을 대체
    // String s1 - 정렬 칼럼 지정
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor;

        // match() 메서드를 호출하면 uriMatcher에 addURI 메서드를 이용해 추가된 URI 중에서 실행 가능한 것이 있는지 확인해준다.
        switch (uriMatcher.match(uri)) {
            case PERSONS:
                cursor =  database.query(DatabaseHelper.TABLE_NAME, DatabaseHelper.ALL_COLUMNS,
                        s,null,null,null,DatabaseHelper.PERSON_NAME +" ASC");
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }

        // 내용 제공자에게 접근하기 위하여 getContentResolver 호출하면 ContentResolver 객체 반환
        // 이 객체에는 insert, query, update, delete 등의 메서드가 정의되어 있어
        // 내용 제공자의 URI를 파라미터로 전달하면서 데이터를 조회, 추가, 수정, 삭제하는 일이 가능하다.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    // MIME 타입이 무엇인지를 알고 싶을 때 사용
    // MIME 타입을 알수 없는 경우에는 null 값 반환
    // 이 메서드들이 실행될 때는 Uri 값이 먼저 매칭되므로 Uri 값이 유효한 경우에는 해당 기능이 실행되고
    // 그렇지 않은 경우에는 예외가 발생한다.
    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case PERSONS:
                return "vnd.android.cursor.dir/persons";
            default:
                throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }
    }


    // 결과값으로 영향을 받은 레코드의 개수 반환
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);

        if (id > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);

            // notifyChange - 레코드가 추가, 수정, 삭제되었을 때 변경이 일어났음을 알려주는 역할
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("추가 실패 -> URI :" + uri);
    }

    // String s, - where 절
    // String[] strings - where 조건 있을 경우 그 안에 들어갈 조건 값을 대체
    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case PERSONS:
                count =  database.delete(DatabaseHelper.TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    // ContentValues contentValues - 저장할 칼럼명과 값들이 들어간 ContentValues 객체
    // 결과 값으로 새로 추가된 값의 Uri 정보가 반환된다.
    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case PERSONS:
                count =  database.update(DatabaseHelper.TABLE_NAME, contentValues, s, strings);
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
