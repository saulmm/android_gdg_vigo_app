package fucverg.saulmm.gdg.gui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.gui.adapters.PagerAdapter;

public class HolderActivity extends FragmentActivity {
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();
	}


	private void initUI () {
		setContentView(R.layout.activity_main);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);

		pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), this));
	}
}


