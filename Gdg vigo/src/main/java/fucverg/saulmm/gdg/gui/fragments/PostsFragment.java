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
import fucverg.saulmm.gdg.data.api.entities.PlusRequestInfo;
import fucverg.saulmm.gdg.data.api.entities.Post;
import fucverg.saulmm.gdg.gui.adapters.PostAdapter;

import java.util.LinkedList;

public class PostsFragment extends Fragment {
	private LinkedList<Post> postList;
	private ApiHandler apiHandler;
	private String nextPageToken;

	private PostAdapter postAdapter;
	private ProgressBar progressBar;
	private ProgressBar progressBarSpinner;
	private FrameLayout bottomBar;


	@Override
	@SuppressWarnings("unchecked")
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_posts, null);
		boolean request = true;
		nextPageToken = "first";

		if(savedInstanceState != null) {
			Object restoredPost = savedInstanceState.get("activities");
			String restoredToken = savedInstanceState.getString("token");
			nextPageToken = restoredToken;

			if (restoredPost instanceof LinkedList) {
				request = false;

				postList = (LinkedList<Post>) restoredPost;
				if(postList.size() == 0)
					request = true;
			}
		}

		initApi(request);
		initGui(rootView);

		return rootView;
	}


	@Override
	public void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("activities", postList);
		outState.putString("token", nextPageToken);
	}



	private void initApi (boolean makeRequest) {
		apiHandler = new ApiHandler(this.getActivity());

		if (makeRequest) {
			postList = new LinkedList<Post>();
			apiHandler.getActivities(null, plusSearchCallBack);
		}
	}


	private void initGui (View rootView) {
		ListView postListView = (ListView) rootView.findViewById(R.id.fp_post_list);
		postListView.setOnScrollListener(listScrollCallBack );

		progressBarSpinner = (ProgressBar) rootView.findViewById(R.id.fp_progress_spinner);
		progressBarSpinner.setIndeterminate(true);

		if (postList.size() > 0)
			progressBarSpinner.setVisibility(View.GONE);

		postAdapter = new PostAdapter(getActivity(), postList);
		postListView.setAdapter(postAdapter);
		postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick (AdapterView<?> adapterView, View view, int position, long l) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(postList.get(position).getUrl()));
				startActivity(i);

			}
		});

		progressBar = (ProgressBar) rootView.findViewById(R.id.fp_progress_bar);
		bottomBar = (FrameLayout) rootView.findViewById(R.id.fp_bottom_bar);
	}


	private boolean noMoreResults = false;
	FutureCallback<PlusRequestInfo> plusSearchCallBack = new FutureCallback<PlusRequestInfo>() {

		@Override
		public void onCompleted (Exception e, PlusRequestInfo plusRequestInfo) {
			hideProgressBar();

			if (plusRequestInfo != null) {
				nextPageToken = plusRequestInfo.nextPageToken;
				for (Post post : plusRequestInfo.items)
					postAdapter.add(post);

				postList = (LinkedList<Post>) postAdapter.getActivities();

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


			} else {
				Log.e("[ERROR] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onCompleted ",
						"Error: " + e.getMessage());
			}
		}
	};


	private void hideProgressBar () {
		if(progressBarSpinner.getVisibility() == View.VISIBLE)
			progressBarSpinner.setVisibility(View.GONE);
	}


	/**
	 *
	 */
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