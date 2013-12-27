package ameiga.saulmm.gdg.gui.fragments;

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
import android.widget.*;
import com.koushikdutta.async.future.FutureCallback;
import ameiga.saulmm.gdg.data.api.ApiHandler;
import ameiga.saulmm.gdg.data.api.entities.PlusRequestInfo;
import ameiga.saulmm.gdg.data.api.entities.Post;
import ameiga.saulmm.gdg.gui.adapters.PostAdapter;
import ameiga.saulmm.gdg.gui.views.PullListView;
import ameiga.saulmm.gdg.gui.views.PullRefreshListener;
import ameiga.saulmm.gdg.utils.GuiUtils;
import ameiga.saulmm.gdg.R;

import java.util.LinkedList;

import static android.util.Log.d;
import static android.util.Log.e;

@SuppressWarnings("SpellCheckingInspection")
public class PostsFragment extends Fragment {
	private boolean noMoreResults = false;

	private LinkedList<Post> postList;
	private ApiHandler apiHandler;
	private String nextPageToken;

	private PostAdapter postAdapter;
	private FrameLayout bottomBar;
	private LinearLayout errorLayout;
	private ProgressBar bottomProgressBar;
	private ProgressBar loadingProgressBar;
	private ProgressBar pullRefreshProgressBar;
	private View rootView;


	@Override
	@SuppressWarnings("unchecked")
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		boolean request = true;

		rootView = inflater.inflate(R.layout.fragment_posts, null);
		nextPageToken = "first";

		if(savedInstanceState != null) {
			Object restoredPost = savedInstanceState.get("activities");
			nextPageToken = savedInstanceState.getString("token");

			if (restoredPost instanceof LinkedList) {
				request = false;

				postList = (LinkedList<Post>) restoredPost;

				if(postList.size() == 0)
					request = true;
			}
		}

		initApi(request);
		initGui(rootView, getActivity(), true);
		return rootView;
	}


	@Override
	public void onResume () {
		super.onResume();
		pullRefreshProgressBar.setVisibility(View.INVISIBLE);
		pullRefreshProgressBar.setIndeterminate(false);
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
			apiHandler.getActivities(null, apiCallback);
		}
	}


	/**
	 * Initializes and configures the components of the ui
	 * @param rootView: inflated view to get its child.
	 * @param context: the current context.
	 * @param showLoadingProgressbar: order to show the circular loading bar.
	 */
	private void initGui (View rootView, Context context, boolean showLoadingProgressbar) {
		postAdapter = new PostAdapter(context, postList);

		// Circular indeterminate view.
		loadingProgressBar = (ProgressBar) rootView.findViewById(R.id.fp_progress_spinner);

		// A bar at the bottom show when more data is coming.
		bottomBar = (FrameLayout) rootView.findViewById(R.id.fp_bottom_bar);
		bottomProgressBar = (ProgressBar) rootView.findViewById(R.id.fp_progress_bottom_bar);

		// Progress bar of the pull to refresh.
		pullRefreshProgressBar = (ProgressBar) rootView.findViewById(R.id.fp_progress_bar);
		pullRefreshProgressBar.setVisibility(View.VISIBLE);
		pullRefreshProgressBar.setIndeterminate(true);

		// Main list with its adapter and events
		PullListView postListView = (PullListView) rootView.findViewById(R.id.fp_post_list);
		postListView.setVerticalFadingEdgeEnabled(false);
		postListView.setListener(pullRefreshCallback);
		postListView.setOnScrollListener(listScrollCallBack);
		postListView.setAdapter(postAdapter);
		postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick (AdapterView<?> adapterView, View view, int position, long l) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(postList.get(position).getUrl()));
			startActivity(i);
			}
		});

		// UI items to display when an error.
		final View rootyView = rootView;
		errorLayout = (LinearLayout) rootView.findViewById(R.id.error_layout);
		errorLayout.setVisibility(View.INVISIBLE);
		View errorButton = rootView.findViewById(R.id.error_reload_button);
		errorButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View view) {
			pullRefreshProgressBar.setIndeterminate(true);
			postList.clear();

			initGui(rootyView, getActivity(), false);
			initApi(true);
			}
		});

		if(showLoadingProgressbar) {
			loadingProgressBar.setIndeterminate(true);
			loadingProgressBar.setVisibility(View.VISIBLE);
		}

		if (postList.size() > 0)
			loadingProgressBar.setVisibility(View.GONE);
	}


	/**
	 * This callback is fired by the apiHandler.getActivities() method when
	 * the request is complete.
	 *
	 * If the requestResponse is not null it adds the data to the adapter.
	 */
	FutureCallback<PlusRequestInfo> apiCallback = new FutureCallback<PlusRequestInfo>() {

		@Override
		public void onCompleted (Exception e, PlusRequestInfo plusRequestInfo) {
			d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onCompleted ",
					"Posts received...");

			loadingProgressBar.setVisibility(View.INVISIBLE);
			pullRefreshProgressBar.setVisibility(View.INVISIBLE);
			pullRefreshProgressBar.setIndeterminate(false);

			// the request response it's ok.
			if (plusRequestInfo != null) {
				errorLayout.setVisibility(View.INVISIBLE);
				nextPageToken = plusRequestInfo.nextPageToken;

				for (Post post : plusRequestInfo.items) {
					postAdapter.add(post);
				}


				postList = (LinkedList<Post>) postAdapter.getActivities();

				if (bottomBar.getVisibility() == View.VISIBLE && getActivity() != null) {
					Animation hideBar = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_up_off);
					hideBar.setAnimationListener(GuiUtils.getHideListener(bottomBar));
					bottomBar.startAnimation(hideBar);
				}

				// noMoreResults is used to manage the bottom bar behavior
				if (plusRequestInfo.items.size() == 0)
					noMoreResults = true;


			} else {
				e("[ERROR] fucverg.saulmm.gdg.gui.fragments.PostsFragment.onCompleted ",
						"Error: " + e.getMessage());


				errorLayout.setVisibility(View.VISIBLE);
				GuiUtils.showShortToast(getActivity(), "No hay red");
			}
		}
	};



	/**
	 * This scroll callback is used as scrollListener handler of the event listview, it detects
	 * if the user  has been scrolled to the bottom of the list, then, shows a 'loading view' that starts with an
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
					apiHandler.getActivities(nextPageToken, apiCallback);
				}
			}
		}

		@Override
		public void onScrollStateChanged (AbsListView absListView, int i) {}
	};


	/**
	 * Callback of the Pull to Refresh feature
	 */
	PullRefreshListener pullRefreshCallback = new PullRefreshListener() {
		@Override
		public void onRefresh (float percent) {
			pullRefreshProgressBar.setVisibility(View.VISIBLE);
			pullRefreshProgressBar.setProgress((int) (percent));

			if(percent >= 95) {
				pullRefreshProgressBar.setIndeterminate(true);
				postList.clear();

				initGui(rootView, getActivity(), true);
				initApi(true);
			}
		}


		@Override
		public void onUp () {
			if (!pullRefreshProgressBar.isIndeterminate() && pullRefreshProgressBar.getVisibility() == View.VISIBLE) {
				pullRefreshProgressBar.setVisibility(View.GONE);
				pullRefreshProgressBar.setProgress(0);
			}
		}
	};
}