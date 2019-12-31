package org.techtown.mission28;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.List;

// 앱 위젯 제공자 클래스 정의 - 앱 위젯이 주기적으로 업데이트될 때 처리하는 코드 구현
public class MyLocationProvider extends AppWidgetProvider {

	static String receiver = "010-6397-1052";

	static PendingIntent sentIntent;
	static PendingIntent deliveredIntent;

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

	// onUpdate - 앱 위젯이 주기적으로 업데이트될 때마다 호출
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		if (sentIntent == null) {
			sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT_ACTION"), 0);
		}

		if (deliveredIntent == null) {
			deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED_ACTION"), 0);
		}

		Log.d("MyLocationProvider", "onUpdate() called.");

		final int size = appWidgetIds.length;

        for (int i = 0; i < size; i++) {
            int appWidgetId = appWidgetIds[i];

			Intent sendIntent = new Intent(context, GPSLocationService.class);
			sendIntent.putExtra("command", "send");
			sendIntent.putExtra("receiver", receiver);

            PendingIntent pendingIntent = PendingIntent.getService(context, 0, sendIntent, 0);

            // RemoteViews 객체 생성한 후 텍스트뷰의 텍스트 설정
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.mylocation);

            // 뷰를 눌렀을 때 실행할 펜딩 인텐트 객체 지정
            views.setOnClickPendingIntent(R.id.button, pendingIntent);

            // 앱 위젯 매니저 객체의 updateAppWidget 메서드 호출
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        // gps 위치 확인을 위한 서비스 시작
        Intent startIntent = new Intent(context, GPSLocationService.class);
		startIntent.putExtra("command", "start");
        context.startService(startIntent);
	}


	public static class GPSLocationService extends Service {
		public static final String TAG = "GPSLocationService";

		public static double ycoord = 0.0D;
		public static double xcoord = 0.0D;

		private LocationManager manager = null;

		private LocationListener listener = new LocationListener() {

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}

			public void onLocationChanged(Location location) {
				Log.d(TAG, "onLocationChanged() called.");

				updateCoordinates(location.getLatitude(), location.getLongitude());

				// 서비스 종료 - 앱 위젯의 업데이트 주기 때마다 한 번씩만 위치 정보를 확인하고 종료함으로써 전원 최소화
				stopSelf();
			}
		};

		BroadcastReceiver sentReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				switch(getResultCode()){
					case Activity.RESULT_OK:
						// 전송 성공
						Toast.makeText(context, "전송 완료", Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						// 전송 실패
						Toast.makeText(context, "전송 실패", Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						// 서비스 지역 아님
						Toast.makeText(context, "서비스 지역 아님", Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						// 무선 꺼짐
						Toast.makeText(context, "무선(Radio)이 꺼져 있음", Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						// PDU 실패
						Toast.makeText(context, "PDU 실패", Toast.LENGTH_SHORT).show();
						break;
				}
			}
		};

		BroadcastReceiver deliveredReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()){
					case Activity.RESULT_OK:
						// 도착 완료
						Toast.makeText(context, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
						break;
					case Activity.RESULT_CANCELED:
						// 도착 안됨
						Toast.makeText(context, "SMS 도착 안됨", Toast.LENGTH_SHORT).show();
						break;
				}
			}
		};


		public IBinder onBind(Intent intent) {
			return null;
		}

		public void onCreate() {
			super.onCreate();

			Log.d(TAG, "onCreate() called.");

			// 서비스가 생성될 때 위치 관리자 객체 참조
			manager = (LocationManager) getSystemService(LOCATION_SERVICE);

			registerReceiver(sentReceiver, new IntentFilter("SMS_SENT_ACTION"));
			registerReceiver(deliveredReceiver, new IntentFilter("SMS_DELIVERED_ACTION"));
		}

		public int onStartCommand(Intent intent, int flags, int startId) {
			if (intent != null) {
				String command = intent.getStringExtra("command");
				if (command != null) {
					if (command.equals("start")) {

						// 서비스가 시작할 때 호출
						startListening();
					} else if (command.equals("send")) {
						String receiver = intent.getStringExtra("receiver");

						String contents = "내 위치 : " + xcoord + ", " + ycoord;
						sendSMS(receiver, contents);
					}
				}
			}

			return super.onStartCommand(intent, flags, startId);
		}

		public void onDestroy() {
			stopListening();

			unregisterReceiver(sentReceiver);
			unregisterReceiver(deliveredReceiver);

			Log.d(TAG, "onDestroy() called.");

			super.onDestroy();
		}

		private void sendSMS(String receiver, String contents) {
			SmsManager mSmsManager = SmsManager.getDefault();
			mSmsManager.sendTextMessage(receiver, null, contents, sentIntent, deliveredIntent);
		}

		private void startListening() {
			Log.d(TAG, "startListening() called.");

			final Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW);

			final String bestProvider = manager.getBestProvider(criteria, true);

			try {
				if (bestProvider != null && bestProvider.length() > 0) {
					manager.requestLocationUpdates(bestProvider, 500, 10, listener);
				} else {
					final List<String> providers = manager.getProviders(true);

					for (final String provider : providers) {
						// 위치 관리자에 위치 정보 요청
						manager.requestLocationUpdates(provider, 500, 10, listener);
					}
				}
			} catch(SecurityException e) {
				e.printStackTrace();
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

		// 위치 정보가 확인되면 호출 - 텍스트뷰에 내 위치 정보 보여주기
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

			// 위치 좌표와 주소 정보를 포함하는 문자열 생성
			if (info.length() <= 0) {
				info = "[내 위치] " + latitude + ", " + longitude;
			} else {
				info += ("\n" + "[내 위치] " + latitude + ", " + longitude + ")");
			}

			xcoord = longitude;
			ycoord = latitude;
			Log.d(TAG, "coordinates : " + latitude + ", " + longitude);

		}

	}

}
