package fucverg.saulmm.gdg.gui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.api.ApiHandler;
import fucverg.saulmm.gdg.data.db.DBHandler;
import fucverg.saulmm.gdg.data.db.entities.Event;
import fucverg.saulmm.gdg.data.db.entities.Member;
import fucverg.saulmm.gdg.gui.adapters.MembersAdapter;

import java.util.ArrayList;

import static android.util.Log.d;

public class MembersFragment extends Fragment {
	private DBHandler dbHandler;
	private ArrayList<Member> members;


	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_member, null);

		if(savedInstanceState != null) {
			ArrayList<Event> recoveredEvents = (ArrayList<Event>) savedInstanceState.get("members");

		}

		initApi();
		initUI(rootView);

		return rootView;
	}


	@Override
	public void onSaveInstanceState (Bundle outState) {
		outState.putSerializable("members", members);
		d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.MembersFragment.onSaveInstanceState ", "Members saved...");

		super.onSaveInstanceState(outState);

	}


	private void initApi () {
		ApiHandler apiHanler = new ApiHandler(getActivity());
		members = (ArrayList<Member>) apiHanler.getMembers();
	}

	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
			String userID = members.get(i).getId();

			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://plus.google.com/"+userID+"/posts")));

		}
	};


	private void initUI (View rootView) {
		ListView eventList = (ListView) rootView.findViewById(R.id.fm_member_list);
		eventList.setOnItemClickListener(onItemClickListener);

		MembersAdapter membersAdapter = new MembersAdapter(getActivity(), members);
		eventList.setAdapter(membersAdapter);
	}


}



