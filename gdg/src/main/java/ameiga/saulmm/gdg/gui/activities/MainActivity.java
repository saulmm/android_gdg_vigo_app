package ameiga.saulmm.gdg.gui.activities;

import ameiga.saulmm.gdg.R;
import ameiga.saulmm.gdg.gui.adapters.PagerAdapter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.ProgressBar;
import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
	public static ArrayList<UpdateListener> upListeners = new ArrayList<UpdateListener>();
	private int selectedPage;
	private PagerSlidingTabStrip tabs;


	private final Handler handler = new Handler();
	private int currentColor = 0xFF666666;
	private Drawable oldBackground = null;
	private ProgressBar mProgressBar;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
	}


	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
//		if (selectedPage <= 1) {
//			MenuInflater inflater = getMenuInflater();
//			inflater.inflate(R.menu.pull, menu);
//		}

		return super.onCreateOptionsMenu(menu);
	}



	/**
	 *  Depending of the bool configured by the screen size, loads the phone gui
	 */
	private void initUI () {
		boolean isTablet = getResources().getBoolean(R.bool.is_tablet);

		if (!isTablet) {
			initNormalUI();
			changeColor(getResources().getColor(R.color.google_green));
		}
	}


	/**
	 *  The normal UI is defined by the PagerSlidingTabStrip framework by Andreas Stuetz <andreas.stuetz@gmail.com>,
	 *  a library to display a tabs ui like google play store app.
	 */
	private void initNormalUI () {
		PagerAdapter catAdapter = new PagerAdapter(getSupportFragmentManager(), this);

		setContentView(R.layout.activity_main);

		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabs.setShouldExpand(true);
		tabs.setOnPageChangeListener(onChangeCallBack);

		pager.setAdapter(catAdapter);
		tabs.setViewPager(pager);

		getActionBar().setTitle(getResources().getString(R.string.card_events));
	}


	/**
	 *  This method changes the color of the action bar with the given color.
	 * @param newColor
	 * by by Andreas Stuetz <andreas.stuetz@gmail.com>,
	 */
	private void changeColor(int newColor) {
		tabs.setIndicatorColor(newColor);

		// change ActionBar color just if an ActionBar is available
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			Drawable colorDrawable = new ColorDrawable(newColor);
			Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
			LayerDrawable ld = new LayerDrawable(new Drawable[] { colorDrawable, bottomDrawable });

			if (oldBackground == null) {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
					ld.setCallback(drawableCallback);
				else
					getActionBar().setBackgroundDrawable(ld);

			} else {
				TransitionDrawable td = new TransitionDrawable(new Drawable[] { oldBackground, ld });

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
					td.setCallback(drawableCallback);

				else
					getActionBar().setBackgroundDrawable(td);

				td.startTransition(200);
			}

			oldBackground = ld;

			getActionBar().setDisplayShowTitleEnabled(false);
			getActionBar().setDisplayShowTitleEnabled(true);
		}
		currentColor = newColor;
	}


	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};


	ViewPager.OnPageChangeListener onChangeCallBack = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageSelected (int i) {
			selectedPage = i;
			invalidateOptionsMenu(); // Fires onCreateOptionsMenu

			switch (i) {
				case 0:
					getActionBar().setTitle(getResources().getString(R.string.card_events));
					changeColor(getResources().getColor(R.color.google_green));
					break;

				case 1:
					getActionBar().setTitle(getResources().getString(R.string.card_posts));
					changeColor(getResources().getColor(R.color.google_red));
					break;

				case 2:
					getActionBar().setTitle(getResources().getString(R.string.card_members));
					changeColor(getResources().getColor(R.color.google_yellow));
					break;

				case 3:
					getActionBar().setTitle(getResources().getString(R.string.app_name));
					changeColor(getResources().getColor(R.color.google_blue));
					break;
			}
		}

		@Override
		public void onPageScrolled (int i, float v, int i2) {}


		@Override
		public void onPageScrollStateChanged (int i) {}
	};



}
