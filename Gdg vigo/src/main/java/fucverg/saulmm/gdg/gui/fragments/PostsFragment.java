package fucverg.saulmm.gdg.gui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.koushikdutta.async.future.FutureCallback;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.api.ApiHandler;
import fucverg.saulmm.gdg.data.db.DBHandler;
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.Activity;
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.PlusRequestInfo;
import fucverg.saulmm.gdg.gui.adapters.PostAdapter;

import java.util.List;

import static android.util.Log.d;

public class PostsFragment extends Fragment {
	DBHandler dbHandler;
	private ApiHandler apiHandler;
	private List<Activity> postList;
	private PostAdapter postAdapter;


	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onCreateView ", "PostActivity Created");

		View rootView = inflater.inflate(R.layout.fragment_posts, null);

		initApi();
		initGui(rootView);



		return rootView;
	}


	private void initGui (View rootView) {
		ListView postListView = (ListView) rootView.findViewById(R.id.fp_post_list);
		postAdapter = new PostAdapter(getActivity(), postList);
		postListView.setAdapter(postAdapter);
	}


	private void initApi () {
		apiHandler = new ApiHandler(this.getActivity());
		apiHandler.getActivities(null, plusSearchCallBack);

		dbHandler = new DBHandler(this.getActivity());
		postList = dbHandler.getActivities();
		d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.PostsFragment.initApi ", "List: " + postList.size());



		for (Activity activity : postList) {
			d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.PostsFragment.initApi ", "\n"+activity.toString()+" \n");
		}
	}


	FutureCallback<PlusRequestInfo> plusSearchCallBack = new FutureCallback<PlusRequestInfo>() {
		String nextPageToken = "";


		@Override
		public void onCompleted (Exception e, PlusRequestInfo plusRequestInfo) {
			if (plusRequestInfo != null) {

				nextPageToken = plusRequestInfo.nextPageToken;

				for (Activity act : plusRequestInfo.items) {
					dbHandler.insertActivity(act);
				}

				if(postList.size() <= 0)  {
					postList = dbHandler.getActivities();
					postAdapter.addAll(postList);
				}

			} else {
				Log.e("[ERROR] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onCompleted ", "Error: " + e.getMessage());
			}

		}
	};
}
