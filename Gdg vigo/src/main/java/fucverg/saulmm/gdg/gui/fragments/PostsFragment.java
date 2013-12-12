package fucverg.saulmm.gdg.gui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import com.koushikdutta.async.future.FutureCallback;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.api.ApiHandler;
import fucverg.saulmm.gdg.data.db.DBHandler;
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.Activity;
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.PlusRequestInfo;
import fucverg.saulmm.gdg.gui.adapters.PostAdapter;

import java.util.LinkedList;

import static android.util.Log.d;

public class PostsFragment extends Fragment {
	DBHandler dbHandler;
	private ApiHandler apiHandler;
	private LinkedList<Activity> postList;
	private PostAdapter postAdapter;
	private String nextPageToken;


	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onCreateView ", "PostActivity Created");

		View rootView = inflater.inflate(R.layout.fragment_posts, null);

		if(savedInstanceState != null) {
			postList  = (LinkedList<Activity>) savedInstanceState.get("activities");


		} else {
			nextPageToken = "first";
			initApi();
		}



		initGui(rootView);
		return rootView;
	}

	@Override
	public void onSaveInstanceState (Bundle outState) {
		outState.putSerializable("activities", postList);
		d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onSaveInstanceState ", "Data saved...");
		super.onSaveInstanceState(outState);

	}


	private void initGui (View rootView) {
		ListView postListView = (ListView) rootView.findViewById(R.id.fp_post_list);
		postListView.setOnScrollListener(listScrollCalback);
		postAdapter = new PostAdapter(getActivity(), postList);
		postListView.setAdapter(postAdapter);
	}




	private void initApi () {
		apiHandler = new ApiHandler(this.getActivity());
		apiHandler.getActivities(null, plusSearchCallBack);

		dbHandler = new DBHandler(this.getActivity());
		postList = (LinkedList<Activity>) dbHandler.getAllElements(new Activity());
	}


	FutureCallback<PlusRequestInfo> plusSearchCallBack = new FutureCallback<PlusRequestInfo>() {


		@Override
		public void onCompleted (Exception e, PlusRequestInfo plusRequestInfo) {
			if (plusRequestInfo != null) {



				d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.getActivitiesURL ", "Getting Activities result: " + plusRequestInfo.items.size());
				for (Activity act : plusRequestInfo.items) {
					act.setPageToken(nextPageToken);
					dbHandler.insertActivity(act);
				}

				nextPageToken = plusRequestInfo.nextPageToken;

				if(postList.size() <= 0)  {
					postList = (LinkedList<Activity>) dbHandler.getAllElements(new Activity());
					postAdapter.addAll(postList);
				}

				if(plusRequestInfo.items.size() > 0)
					apiHandler.getActivities(nextPageToken, plusSearchCallBack);

			} else {
				Log.e("[ERROR] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onCompleted ", "Error: " + e.getMessage());
			}

		}
	};

	AbsListView.OnScrollListener listScrollCalback = new AbsListView.OnScrollListener() {
		@Override
		public void onScrollStateChanged (AbsListView absListView, int i) {

		}


		@Override
		public void onScroll (AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onScroll ", "\nFirstVisibleItem: "+firstVisibleItem+"" +
					"Visible item Count: "+visibleItemCount+" Total: "+totalItemCount);
		}
	};
}
