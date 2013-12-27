package ameiga.saulmm.gdg.gui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.koushikdutta.async.future.FutureCallback;
import ameiga.saulmm.gdg.data.api.ApiHandler;
import ameiga.saulmm.gdg.data.db.entities.Event;
import ameiga.saulmm.gdg.gui.adapters.EventAdapterListener;
import ameiga.saulmm.gdg.gui.adapters.EventsAdapter;
import ameiga.saulmm.gdg.gui.views.PullListView;
import ameiga.saulmm.gdg.gui.views.PullRefreshListener;
import ameiga.saulmm.gdg.utils.GuiUtils;
import ameiga.saulmm.gdg.R;

import java.util.LinkedList;
import java.util.List;

import static android.util.Log.d;

public class EventsFragment extends Fragment {
	private LinkedList<Event> linkedEvents;
	private ProgressBar progressSpinner;
	private EventsAdapter eventsAdapter;
	private View rootView;
	private ApiHandler apiHanler;
	private ProgressBar pullRequestProgressBar;
	private LinearLayout errorLayout;
	private boolean nowLoading;


	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		linkedEvents = new LinkedList<Event>();
		apiHanler = new ApiHandler(getActivity());
	}


	@Override
	@SuppressWarnings("unchecked")
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_event, null);

		if(savedInstanceState != null) {
			Object restoredEvents = savedInstanceState.getSerializable("events");

			if (restoredEvents instanceof LinkedList) {
				linkedEvents = (LinkedList<Event>) restoredEvents;
				initGui(rootView, getActivity(), false);
			}

		} else {
			initGui(rootView, getActivity(), true);
			initApi();
		}

		return rootView;
	}


	@Override
	public void onPause () {
		super.onPause();
		pullRequestProgressBar.setVisibility(View.INVISIBLE);
		pullRequestProgressBar.setIndeterminate(false);
	}


	/**
	 * Initializes and configures the components of the ui
	 * @param rootView: inflated view to get its child.
	 * @param context: the current context.
	 * @param showLoadingProgressBar: order to show the circular loading bar.
	 */
	private void initGui (View rootView, Context context, boolean showLoadingProgressBar) {
		pullRequestProgressBar = (ProgressBar) rootView.findViewById(R.id.fe_progress_bar);
		progressSpinner = (ProgressBar) rootView.findViewById(R.id.fe_spinner_progress);

		if (showLoadingProgressBar)
			progressSpinner.setVisibility(View.VISIBLE);

		// Main ListView
		eventsAdapter = new EventsAdapter(context, linkedEvents, eventAdapterListener);
		if (linkedEvents.size() > 0)
			progressSpinner.setVisibility(View.INVISIBLE);

		PullListView eventList = (PullListView) rootView.findViewById(R.id.fe_events_list);
		eventList.setVerticalFadingEdgeEnabled(false);
		eventList.setVisibility(View.VISIBLE);
		eventList.setListener(pullRefreshCallback);
		eventList.setAdapter(eventsAdapter);

		// UI items to display when an error
		final View rooty = rootView;
		errorLayout = (LinearLayout) rootView.findViewById(R.id.error_layout);
		View errorButton = rootView.findViewById(R.id.error_reload_button);
		errorButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View view) {
			pullRequestProgressBar.setIndeterminate(true);
			pullRequestProgressBar.setVisibility(View.VISIBLE);

			linkedEvents.clear();

			initGui(rooty, getActivity(), false);
			initApi();
			}
		});

	}


	private void initApi () {
		apiHanler.makeEventRequest(eventsRequestCallback);
		nowLoading = true;
	}


	@Override
	public void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("events", linkedEvents);
	}


	/**
	 * Callback fired by the apiHandler.makeEventRequest(), it manages various loading components
	 * and fills the adapter, also show the error ui elements when an error happens.
	 */
	FutureCallback<List<Event>> eventsRequestCallback = new FutureCallback<List<Event>>() {
		@Override
		public void onCompleted (Exception e, List<Event> eventsRequestResponse) {
			d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.EventsFragment.onCompleted ",
					"Events received...");
			pullRequestProgressBar.setVisibility(View.INVISIBLE);
			errorLayout.setVisibility(View.INVISIBLE);
			nowLoading = false;

			pullRequestProgressBar.setVisibility(View.INVISIBLE);
			pullRequestProgressBar.setIndeterminate(false);

			if (progressSpinner.getVisibility() == View.VISIBLE) {
				progressSpinner.setVisibility(View.INVISIBLE);
			}

			if (eventsRequestResponse != null) {
				eventsAdapter.clear();

				for (Event event : eventsRequestResponse)
					linkedEvents.addFirst(event);

			} else {
				errorLayout.setVisibility(View.VISIBLE);
				linkedEvents.clear();
				GuiUtils.showShortToast(getActivity(), "No hay red");
			}
		}
	};


	/**
	 *  Callback of the Pull to Refresh feature
	 */
	PullRefreshListener pullRefreshCallback = new PullRefreshListener() {
		@Override
		public void onRefresh (float percent) {
			if (!nowLoading) {
				pullRequestProgressBar.setVisibility(View.VISIBLE);
				pullRequestProgressBar.setProgress((int) percent);

				d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.EventsFragment.onRefresh ",
						"Percent: " + percent + " %");

				if(percent >= 95) {
					eventsAdapter.clear();
					pullRequestProgressBar.setIndeterminate(true);

					initGui(rootView, getActivity(), true);
					initApi();
				}
			}
		}


		@Override
		public void onUp () {
		   if (!pullRequestProgressBar.isIndeterminate() && pullRequestProgressBar.getVisibility() == View.VISIBLE) {
			   pullRequestProgressBar.setVisibility(View.INVISIBLE);
			   pullRequestProgressBar.setProgress(0);
		   }
		}
	};


	EventAdapterListener eventAdapterListener = new EventAdapterListener() {
		@Override
		public void mapPressed (String location) {

		// Todo move all urls to a static class
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.es/maps?q="+location));
		startActivity(i);
		}
	};
}