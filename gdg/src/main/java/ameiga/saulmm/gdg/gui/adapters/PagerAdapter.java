package ameiga.saulmm.gdg.gui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import ameiga.saulmm.gdg.gui.fragments.EventsFragment;
import ameiga.saulmm.gdg.gui.fragments.GroupFragment;
import ameiga.saulmm.gdg.gui.fragments.MembersFragment;
import ameiga.saulmm.gdg.gui.fragments.PostsFragment;
import ameiga.saulmm.gdg.R;

public class PagerAdapter extends FragmentPagerAdapter {
	private final String[] pagerTitles;

	private  GroupFragment aboutFragment = new GroupFragment();
	private EventsFragment eventsFragment = new EventsFragment();
	private PostsFragment postFragment = new PostsFragment();
	private  MembersFragment membersFragment = new MembersFragment();


	public PagerAdapter (FragmentManager fragmentManager, Context context) {
		super(fragmentManager);
		pagerTitles = context.getResources()
				.getStringArray(R.array.pager_titles);


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
	public Fragment getItem (int position) {
		switch (position) {
			case 0: return eventsFragment;

			case 1: return postFragment;

			case 2: return membersFragment;

			case 3: return aboutFragment;

			default: return null;
		}
	}


	public MembersFragment getMembersFragment () {
		return membersFragment;
	}


	public PostsFragment getPostFragment () {
		return postFragment;
	}


	public EventsFragment getEventsFragment () {
		return eventsFragment;
	}

}
