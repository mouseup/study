package org.techtown.mission10;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class Fragment1 extends Fragment {
	private static final String TAG = "Fragment1";

	FragmentCallback callback;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		if (context instanceof FragmentCallback) {
			callback = (FragmentCallback) context;
		} else {
			Log.d(TAG, "Activity is not FragmentCallback.");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);
		
		Button button1 = rootView.findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callback.onFragmentSelected(1, null);
			}
			
		});


		ViewPager pager = rootView.findViewById(R.id.pager);

		// 미리 로딩해 놓을 아이템의 개수를 3개로 함
		// 뷰페이저는 어댑터가 가지고 있는 아이템 중에서 몇 개를 미리 로딩해 두었다가 좌우 스크롤할 때 빠르게 보여줄 수 있다.
		pager.setOffscreenPageLimit(3);

		CustomerPagerAdapter adapter = new CustomerPagerAdapter(getFragmentManager());

		for (int i = 0; i < 3; i++) {
			PageFragment page = createPage(i);

			// 프래그먼트 객체를 파라미터로 넘겨주면 어댑터 안에 들어있는 ArraryList에 추가
			adapter.addItem(page);
		}

		// ViewPager 객체에 adapter 설정
		pager.setAdapter(adapter);


		return rootView;
	}

	public PageFragment createPage(int index) {
		String name = "화면 " + index;
		PageFragment fragment = PageFragment.newInstance(name);

		return fragment;
	}


	// 어댑터는 뷰페이저에 보여줄 각 프레그먼트를 관리하는 역할
	// 뷰페이저에 설정하면 서로 상호작용하면서 화면에 표시해준다.
	class CustomerPagerAdapter extends FragmentStatePagerAdapter {

		//프래그먼트들을 담아둘 ArrayList 객체 생성
		ArrayList<Fragment> items = new ArrayList<Fragment>();

		public CustomerPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public void addItem(Fragment item) {
			items.add(item);
		}

		@Override
		public Fragment getItem(int position) {
			return items.get(position);
		}

		@Override
		public int getCount() {
			return items.size();
		}
	}



}
