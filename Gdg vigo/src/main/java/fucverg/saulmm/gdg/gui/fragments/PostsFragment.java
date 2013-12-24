package fucverg.saulmm.gdg.gui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.koushikdutta.async.future.FutureCallback;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.api.ApiHandler;
import fucverg.saulmm.gdg.data.api.entities.PlusRequestInfo;
import fucverg.saulmm.gdg.data.api.entities.Post;
import fucverg.saulmm.gdg.gui.adapters.PostAdapter;
import fucverg.saulmm.gdg.gui.views.PullListView;
import fucverg.saulmm.gdg.gui.views.PullRefreshListener;

import java.util.LinkedList;

import static android.util.Log.e;

public class PostsFragment extends Fragment {
	private LinkedList<Post> postList;
	private ApiHandler apiHandler;
	private String nextPageToken;

	private PostAdapter postAdapter;
	private ProgressBar bottomProgressBar;
	private ProgressBar progressBarSpinner;
	private FrameLayout bottomBar;
	private View rootView;
	private Context context;
	private ProgressBar progressBar;




	@Override
	@SuppressWarnings("unchecked")
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.context = getActivity();

		rootView = inflater.inflate(R.layout.fragment_posts, null);
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
		initGui(rootView, getActivity());

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


	private void initGui (View rootView, Context context) {
		PullListView postListView = (PullListView) rootView.findViewById(R.id.fp_post_list);
		postListView.setListener(pullRefreshCallback);
		postListView.setOnScrollListener(listScrollCallBack );
		postListView.setVerticalFadingEdgeEnabled(false);

		progressBar = (ProgressBar) rootView.findViewById(R.id.fp_progress_bar);

		progressBarSpinner = (ProgressBar) rootView.findViewById(R.id.fp_progress_spinner);
		progressBarSpinner.setIndeterminate(true);
		progressBarSpinner.setVisibility(View.VISIBLE);

		if (postList.size() > 0)
			progressBarSpinner.setVisibility(View.GONE);

		postAdapter = new PostAdapter(context, postList);
		postListView.setAdapter(postAdapter);
		postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick (AdapterView<?> adapterView, View view, int position, long l) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(postList.get(position).getUrl()));
				startActivity(i);

			}
		});

		bottomProgressBar = (ProgressBar) rootView.findViewById(R.id.fp_progress_bottom_bar);
		bottomBar = (FrameLayout) rootView.findViewById(R.id.fp_bottom_bar);
	}


	private boolean noMoreResults = false;
	FutureCallback<PlusRequestInfo> plusSearchCallBack = new FutureCallback<PlusRequestInfo>() {

		@Override
		public void onCompleted (Exception e, PlusRequestInfo plusRequestInfo) {
			hideProgressBar();

			if (progressBar.getVisibility() == View.VISIBLE) {
				progressBar.setVisibility(View.GONE);
				progressBar.setIndeterminate(false);
			}

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
				e("[ERROR] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onCompleted ",
						"Error: " + e.getMessage());
			}
		}
	};


	private void hideProgressBar () {
		if(progressBarSpinner.getVisibility() == View.VISIBLE)
			progressBarSpinner.setVisibility(View.GONE);
	}


	/**
	 * This scroll callback is used as scrollListener handler of the event listview, it detects
	 * if it has been scrolled to the bottom of the list, then, shows a 'loading view' that starts with an
	 * animation, after, makes a request to retrieve the next 20 activities to append in the listview.
	 */
	AbsListView.OnScrollListener listScrollCallBack = new AbsListView.OnScrollListener() {

		@Override
		public void onScroll (AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			if((visibleItemCount + firstVisibleItem) == totalItemCount && totalItemCount != 0 && !noMoreResults) {
				if (bottomProgressBar != null && bottomBar.getVisibility() == View.INVISIBLE) {

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


	/**
	 *
	 */
	PullRefreshListener pullRefreshCallback = new PullRefreshListener() {
		@Override
		public void onRefresh (float percent) {
			if (progressBar.getVisibility() == View.GONE)
				progressBar.setVisibility(View.VISIBLE);

			progressBar.setProgress((int) (percent));

			if(percent >= 99) {
				progressBar.setIndeterminate(true);
				postList.clear();

				initGui(rootView, getActivity());
				initApi(true);
			}
		}


		@Override
		public void onUp () {
			if (!progressBar.isIndeterminate() && progressBar.getVisibility() == View.VISIBLE) {
				progressBar.setVisibility(View.GONE);
				progressBar.setProgress(0);
			}
		}
	};
}