package fucverg.saulmm.gdg.gui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import fucverg.saulmm.gdg.gui.fragments.GroupFragment;
import fucverg.saulmm.gdg.gui.fragments.EventsFragment;
import fucverg.saulmm.gdg.gui.fragments.MembersFragment;
import fucverg.saulmm.gdg.gui.fragments.PostsFragment;

public class PagerAdapter extends FragmentPagerAdapter {
	private String[] pagerTitles = {"Events", "Posts", "Members", "Gdg Vigo	"};

	private final GroupFragment aboutFragment = new GroupFragment();
	private final EventsFragment eventsFragment = new EventsFragment();
	private final PostsFragment postFragment = new PostsFragment();
	private final MembersFragment membersFragment = new MembersFragment();


	public PagerAdapter (FragmentManager fragmentManager, Context context) {
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
	public Fragment getItem (int selectedPage) {

		switch (selectedPage) {
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
