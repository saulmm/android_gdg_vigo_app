package fucverg.saulmm.gdg.gui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import fucverg.saulmm.gdg.gui.fragments.BaseFragment;

public class CaregoriesAdapater extends FragmentPagerAdapter {
	private String[] pagerTitles = {"Events", "Posts", "Members"};



	public CaregoriesAdapater (FragmentManager fragmentManager, Context context) {
		super(fragmentManager);
	}


	@Override
	public CharSequence getPageTitle (int position) {
		return pagerTitles[position];
	}


	@Override
	public int getCount () {
		return pagerTitles.length;
	}


	@Override
	public Fragment getItem (int i) {
		return new BaseFragment();
	}
}
