package fucverg.saulmm.gdg.gui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.gui.adapters.CaregoriesAdapater;

public class LoginActivity extends FragmentActivity {

	private PagerSlidingTabStrip tabs;


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

		pager.setAdapter(new CaregoriesAdapater(getSupportFragmentManager(), this));
		tabs.setViewPager(pager);
	}

}
