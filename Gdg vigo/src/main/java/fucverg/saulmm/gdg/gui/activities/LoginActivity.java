package fucverg.saulmm.gdg.gui.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.gui.adapters.CaregoriesAdapater;

import static android.util.Log.d;

public class LoginActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
	private PagerSlidingTabStrip tabs;

	private int currentColor = 0xFF666666;
	private Drawable oldBackground = null;
	private final Handler handler = new Handler();



	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();


	}


	private void initUI () {

		setContentView(R.layout.activity_main);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		tabs.setOnPageChangeListener(this);
		pager.setAdapter(new CaregoriesAdapater(getSupportFragmentManager(), this));
		tabs.setViewPager(pager);
	}


	private void changeColor(int newColor) {

		tabs.setIndicatorColor(newColor);

		// change ActionBar color just if an ActionBar is available
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			Drawable colorDrawable = new ColorDrawable(newColor);
			Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
			LayerDrawable ld = new LayerDrawable(new Drawable[] { colorDrawable, bottomDrawable });

			if (oldBackground == null) {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					ld.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(ld);
				}

			} else {

				TransitionDrawable td = new TransitionDrawable(new Drawable[] { oldBackground, ld });

				// workaround for broken ActionBarContainer drawable handling on
				// pre-API 17 builds
				// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					td.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(td);
				}


				td.startTransition(200);

			}

			oldBackground = ld;

			// http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
			getActionBar().setDisplayShowTitleEnabled(false);
			getActionBar().setDisplayShowTitleEnabled(true);

		}

		currentColor = newColor;

	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentColor", currentColor);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentColor = savedInstanceState.getInt("currentColor");
		changeColor(currentColor);
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



	@Override
	public void onPageScrolled (int i, float v, int i2) {

	}


	@Override
	public void onPageSelected (int i) {
		switch (i) {
			case 0:
				d("[DEBUG] fucverg.saulmm.gdg.gui.activities.LoginActivity.onPageSelected ", "0");
				changeColor(Color.parseColor("#FFBC00"));
				break;

			case 1:
				d("[DEBUG] fucverg.saulmm.gdg.gui.activities.LoginActivity.onPageSelected ", "1");
				changeColor(Color.parseColor("#0059D7"));
				break;

			case 2:
				d("[DEBUG] fucverg.saulmm.gdg.gui.activities.LoginActivity.onPageSelected ", "2");
				changeColor(Color.parseColor("#00894B"));
				break;

			case 3:
				d("[DEBUG] fucverg.saulmm.gdg.gui.activities.LoginActivity.onPageSelected ", "3");
				changeColor(Color.parseColor("#DD4F37"));
				break;
		}

	}


	@Override
	public void onPageScrollStateChanged (int i) {

	}
}
