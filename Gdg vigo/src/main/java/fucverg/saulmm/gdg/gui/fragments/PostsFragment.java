package fucverg.saulmm.gdg.gui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.koushikdutta.async.future.FutureCallback;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.api.ApiHandler;
import fucverg.saulmm.gdg.data.db.DBHandler;
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.PlusRequestInfo;
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.Post;
import fucverg.saulmm.gdg.gui.adapters.PostAdapter;

import java.util.LinkedList;

import static android.util.Log.d;

public class PostsFragment extends Fragment {
	private LinkedList<Post> postList;
	private DBHandler dbHandler;
	private ApiHandler apiHandler;
	private String nextPageToken;

	private PostAdapter postAdapter;
	private ProgressBar progressBar;
	private ProgressBar progressBarSpinner;
	private FrameLayout bottomBar;


	@Override
	@SuppressWarnings("unchecked")
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onCreateView ",
				"PostActivity Created...");

		View rootView = inflater.inflate(
				R.layout.fragment_posts, null);

		if(savedInstanceState != null) {
			d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onCreateView ",
					"The saved instance is diferent from null");
			postList  = (LinkedList<Post>) savedInstanceState.get("activities");

		} else {
			d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onCreateView ",
					"No saved state found...");
			nextPageToken = "first";

			initApi();
			initGui(rootView);

		}

		return rootView;
	}


	@Override
	public void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("activities", postList);
		d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onSaveInstanceState ", "Data saved...");

	}


	private void initApi () {
		apiHandler = new ApiHandler(this.getActivity());
		dbHandler = new DBHandler(this.getActivity());
		apiHandler.getActivities(null, plusSearchCallBack);
		postList = new LinkedList<Post>();


//		postList = (LinkedList<Post>) dbHandler.getMembersByToken(nextPageToken);
	}


	private void initGui (View rootView) {
		ListView postListView = (ListView) rootView.findViewById(R.id.fp_post_list);
		postListView.setOnScrollListener(listScrollCallBack );

		progressBarSpinner = (ProgressBar) rootView.findViewById(R.id.fp_progress_spinner);
		progressBarSpinner.setIndeterminate(true);

		postAdapter = new PostAdapter(getActivity(), postList);
		postListView.setAdapter(postAdapter);
		postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick (AdapterView<?> adapterView, View view, int position, long l) {
				d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onItemClick ",
						"URL: "+Uri.parse(postList.get(position).getUrl()));
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(postList.get(position).getUrl()));
				startActivity(i);

			}
		});

		progressBar = (ProgressBar) rootView.findViewById(R.id.fp_progress_bar);
		bottomBar = (FrameLayout) rootView.findViewById(R.id.fp_bottom_bar);
	}


	AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick (AdapterView<?> adapterView, View view, int position, long l) {
			d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onItemClick ",
					"URL: "+Uri.parse(postList.get(position).getUrl()));
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(postList.get(position).getUrl()));
			startActivity(i);

		}
	};




	private boolean noMoreResults = false;
	FutureCallback<PlusRequestInfo> plusSearchCallBack = new FutureCallback<PlusRequestInfo>() {

		@Override
		public void onCompleted (Exception e, PlusRequestInfo plusRequestInfo) {
			progressBarSpinner.setVisibility(View.GONE);

			if (plusRequestInfo != null) {

//				for (Post act : plusRequestInfo.items) {
//					act.setPageToken(nextPageToken);
//					dbHandler.insertActivity(act);
//				}

				nextPageToken = plusRequestInfo.nextPageToken;

				for (Post post : plusRequestInfo.items) {
					postAdapter.add(post);
//					postList.add(post);
				}

				postList =(LinkedList<Post>) postAdapter.getActivities();

				if (bottomBar.getVisibility() == View.VISIBLE) {
					Animation hideBar = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_up_off);
					hideBar.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationEnd (Animation animation) {
							bottomBar.setVisibility(View.INVISIBLE);
						}

						@Override
						public void onAnimationStart (Animation animation) {}


						@Override
						public void onAnimationRepeat (Animation animation) {}
					});

					bottomBar.startAnimation(hideBar);
				}

				if (plusRequestInfo.items.size() == 0)
					noMoreResults = true;

//				if(plusRequestInfo.items.size() > 0)
//					apiHandler.getActivities(nextPageToken, plusSearchCallBack);

			} else {
				Log.e("[ERROR] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onCompleted ",
						"Error: " + e.getMessage());
			}
		}
	};

	AbsListView.OnScrollListener listScrollCallBack = new AbsListView.OnScrollListener() {

		@Override
		public void onScroll (AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			if((visibleItemCount + firstVisibleItem) == totalItemCount && totalItemCount != 0 && !noMoreResults) {
				if (progressBar != null && bottomBar.getVisibility() == View.INVISIBLE) {

					bottomBar.startAnimation(AnimationUtils.loadAnimation(getActivity(),
							R.anim.translate_up_on));

					bottomBar.setVisibility(View.VISIBLE);
					apiHandler.getActivities(nextPageToken, plusSearchCallBack);
				}
			}
		}

		@Override
		public void onScrollStateChanged (AbsListView absListView, int i) {}
	};

}