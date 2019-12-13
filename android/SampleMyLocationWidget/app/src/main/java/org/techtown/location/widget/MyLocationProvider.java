package org.techtown.location.widget;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.List;

// 앱 위젯 제공자 클래스 정의
// 앱 위젯이 주기적으로 업데이트될 때 처리할 코드 구현
public class MyLocationProvider extends AppWidgetProvider {

    public static double ycoord = 0.0D;
    public static double xcoord = 0.0D;


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    // 앱 위젯이 주기적으로 업데이트될 때마다 호출 되는 onUpdate 메서드 안에서는
    // 앱 위젯으로 표현되는 텍스트뷰를 눌렀을 때 실행한 인텐트를 지정하고
    // gps 위치 확인을 위해 새로 정의한 서비스를 시작한다.
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.d("MyLocationProvider", "onUpdate() called : " + ycoord + ", " + xcoord);

        final int size = appWidgetIds.length;

        for (int i = 0; i < size; i++) {
            int appWidgetId = appWidgetIds[i];

            //String uri = "geo:"+ ycoord + "," + xcoord + "?z=10";
            //Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));

            // geo- 지도
            // 지도를 띄우기 위한 uri 문자열 생성
            // 내 위치 정보로 지도를 띄우는데 사용되는 uri 문자열 포맷
            // geo:<latitude>,<longitude>?z=<zoomlevel>
            // z - 지도가 나타날 때 사용되는 확대/축소 수준 지정
            String uriBegin = "geo:" + ycoord + "," + xcoord;
            String query = ycoord + "," + xcoord + "(" + "내위치" + ")";
            String encodedQuery = Uri.encode(query);
            // 확대 축소
            String uriString = uriBegin + "?q=" + encodedQuery + "&z=15";
            Uri uri = Uri.parse(uriString);

            // 지도를 띄우기 위한 인텐트 객체 생성
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            // 지도를 띄우기 위한 펜딩 인텐트 객체 생성
            // PendingIntent - 대기 인텐트
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.mylocation);

            // 뷰를 눌렀을 때 실행할 펜딩 인텐드 객체 지정
            // mylocation.xml의 글자 클릭시 pendingIntent 실행
            // 텍스트뷰를 눌렀을 때 내 위치를 이용해 지도를 보여줄 수 있는 가장 간단한 방법은
            // "geo." 로 시작하는 uri 객체를 만들어 인텐트로 지도를 띄워주는 것이다.
            // 텍스트뷰를 눌렀을 때 내 위치 좌표를 이용해 지도를 띄워주기 위해 설정하는 인텐트는 미리 설정되어 있어야 하므로
            // pendingIntent 객체로 만들어 설정한다.
            // 이 객체는 remoteviews 객체의 setOnClickPendingIntent 메서드를 이용하여 설정한다.
            views.setOnClickPendingIntent(R.id.txtInfo, pendingIntent);

            // 앱 위젯 화면 업데이트
            // 앱 위젯 매니저 객체의 updateAppWidget 메서드 호출하여 위젯을 업데이트하면
            // 텍스트뷰의 클릭 이벤트를 처리하기 위한 인테트가 설정된다.
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        // gps 위치 확인을 위한 서비스 시작
        context.startService(new Intent(context, GPSLocationService.class));


    }


    // 위치정보 확인
    public static class GPSLocationService extends Service {
        public static final String TAG = "GPSLocationService";

        private LocationManager manager = null;

        private LocationListener listener = new LocationListener() {

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }

            // 위치 정보가 확인되면 onLocationChanged 호출
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged() called.");

                updateCoordinates(location.getLatitude(), location.getLongitude());

                // 서비스 종료 - 전원 최소한 사용
                stopSelf();
            }
        };

        public IBinder onBind(Intent intent) {
            return null;
        }


        // 서비스가 생성되면서 호출 - 위치 관리자 객체 참조
        public void onCreate() {
            super.onCreate();

            Log.d(TAG, "onCreate() called.");

            // 서비스가 생성될 때 위치 관리자 객체 참조
            manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        }

        // 서비스 시작
        public int onStartCommand(Intent intent, int flags, int startId) {

            // 서비스가 시작할 때 startListening 메서드 호출
            startListening();

            return super.onStartCommand(intent, flags, startId);
        }

        public void onDestroy() {
            stopListening();

            Log.d(TAG, "onDestroy() called.");

            super.onDestroy();
        }

        // 위치정보확인해서 변수에 할당
        private void startListening() {
            Log.d(TAG, "startListening() called.");

            final Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            final String bestProvider = manager.getBestProvider(criteria, true);

            if (bestProvider != null && bestProvider.length() > 0) {

                // 위치 관리자에 위치 정보 요청
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                manager.requestLocationUpdates(bestProvider, 500, 10, listener);
			} else {
				final List<String> providers = manager.getProviders(true);

				for (final String provider : providers) {
					manager.requestLocationUpdates(provider, 500, 10, listener);
				}
			}
		}

		private void stopListening() {
			try {
				if (manager != null && listener != null) {
					manager.removeUpdates(listener);
				}

				manager = null;
			} catch (final Exception ex) {

			}
		}

		private void updateCoordinates(double latitude, double longitude) {
			Geocoder coder = new Geocoder(this);
			List<Address> addresses = null;
			String info = "";

			Log.d(TAG, "updateCoordinates() called.");

			try {
				addresses = coder.getFromLocation(latitude, longitude, 2);

				if (null != addresses && addresses.size() > 0) {
					int addressCount = addresses.get(0).getMaxAddressLineIndex();

					if (-1 != addressCount) {
						for (int index = 0; index <= addressCount; ++index) {
							info += addresses.get(0).getAddressLine(index);

							if (index < addressCount)
								info += ", ";
						}
					} else {
						info += addresses.get(0).getFeatureName() + ", "
								+ addresses.get(0).getSubAdminArea() + ", "
								+ addresses.get(0).getAdminArea();
					}
				}

				Log.d(TAG, "Address : " + addresses.get(0).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

			coder = null;
			addresses = null;

			// 위치 좌표와 주소 정보를 포함한 문자열 생성
			if (info.length() <= 0) {
				info = "[내 위치] " + latitude + ", " + longitude
						+ "\n터치하면 지도로 볼 수 있습니다.";
			} else {
				info += ("\n" + "[내 위치] " + latitude + ", " + longitude + ")");
				info += "\n터치하면 지도로 볼 수 있습니다.";
			}

			// RemoteViews 객체 생성한 후 텍스트뷰의 텍스트 설정
			RemoteViews views = new RemoteViews(getPackageName(), R.layout.mylocation);

			// 텍스트뷰에 내 위치 정보 보여줌
			views.setTextViewText(R.id.txtInfo, info);

			ComponentName thisWidget = new ComponentName(this, MyLocationProvider.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);

			// 위젯 업데이트
			manager.updateAppWidget(thisWidget, views);

			xcoord = longitude;
			ycoord = latitude;
			Log.d(TAG, "coordinates : " + latitude + ", " + longitude);

		}
	}

}
