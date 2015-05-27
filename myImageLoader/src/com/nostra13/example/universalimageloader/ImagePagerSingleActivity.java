package com.nostra13.example.universalimageloader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ImagePagerSingleActivity extends FragmentActivity {
	private static final String STATE_POSITION = "STATE_POSITION";

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
    private ImagePagerAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        // 设置全屏
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);

       /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.image_detail_pager);

        Bundle bundle = getIntent().getBundleExtra("data");
        assert bundle != null;

		pagerPosition = bundle.getInt("idx"); // 选择的图片索引位置

        /**
         * 可以在这里先模拟一组本地图片 2015/5/27 20:01
         */
		String[] urls = bundle.getStringArray(Constants.Extra.IMAGES); // 需要查看的图片

		mPager = (HackyViewPager) findViewById(R.id.pager);
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);

		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
				.getAdapter().getCount());
		indicator.setText(text);

		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
            }

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);

                // 重置 2015/2/26 12:47
                ImageDetailFragment fragment = (ImageDetailFragment) getSupportFragmentManager()
                        .findFragmentByTag(mPager.getCurrentItem() + ""); // 根据索引Tag获取缓存的fragment
                if (fragment != null) { // 获取之后，将图片更改为点击的索引值
                    fragment.reset();
                }
            }

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public String[] fileList;

		public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.length;
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList[position];
            ImageDetailFragment fragment = ImageDetailFragment.newInstance(url, ImagePagerSingleActivity.this);

            // 根据索引Tag设置缓存，帮助将图片更改为点击的索引值  2015/3/22 11:26
//            getSupportFragmentManager().beginTransaction().add(fragment, position + "").commit();

            return fragment;
		}

	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }

        return  super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
    }
}