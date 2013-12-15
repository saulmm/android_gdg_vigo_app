package fucverg.saulmm.gdg.gui.activities;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.gui.adapters.PagerAdapter;

public class MainActivity extends FragmentActivity {
	private PagerSlidingTabStrip tabs;

	private final Handler handler = new Handler();
	private int currentColor = 0xFF666666;
	private Drawable oldBackground = null;
	private ProgressBar mProgressBar;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		Log.d("[DEBUG] fucverg.saulmm.gdg.gui.activities.MainActivity.onCreate ",
				"\n\n////////////////////////////////////////////////\n\n THIS IS A NEW EXECUTION \n\n////////////////////////////////////////////////\n\n");


		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
	}


	private void initUI () {
		boolean isTablet = getResources().getBoolean(R.bool.is_tablet);

		if (isTablet)
			Toast.makeText(this, "Hi :D ", Toast.LENGTH_SHORT).show();

		else {
			initNormalUI();
			changeColor(getResources().getColor(R.color.google_green));

		}
	}


	private void initNormalUI () {
		PagerAdapter catAdapter = new PagerAdapter(getSupportFragmentManager(), this);

		setContentView(R.layout.activity_main);

		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabs.setOnPageChangeListener(onChangeCallBack);

		pager.setAdapter(catAdapter);
		tabs.setViewPager(pager);

		getActionBar().setTitle("Events");
	}


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
			switch (i) {
				case 0:
					getActionBar().setTitle("Events");
					changeColor(getResources().getColor(R.color.google_green));
					break;

				case 1:
					getActionBar().setTitle("Posts");
					changeColor(getResources().getColor(R.color.google_red));
					break;

				case 2:
					getActionBar().setTitle("Members");
					changeColor(getResources().getColor(R.color.google_yellow));
					break;

				case 3:
					getActionBar().setTitle("GDG Vigo");
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
