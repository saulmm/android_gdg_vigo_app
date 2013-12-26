package fucverg.saulmm.gdg.gui.fragments;

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
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.api.ApiHandler;
import fucverg.saulmm.gdg.data.db.entities.Event;
import fucverg.saulmm.gdg.gui.adapters.EventAdapterListener;
import fucverg.saulmm.gdg.gui.adapters.EventsAdapter;
import fucverg.saulmm.gdg.gui.views.PullListView;
import fucverg.saulmm.gdg.gui.views.PullRefreshListener;
import fucverg.saulmm.gdg.utils.GuiUtils;

import java.util.LinkedList;
import java.util.List;

public class EventsFragment extends Fragment {
	private LinkedList<Event> linkedEvents;
	private ProgressBar progressSpinner;
	private EventsAdapter eventsAdapter;
	private View rootView;
	private ApiHandler apiHanler;
	private ProgressBar progressBar;
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
				initGui(rootView, getActivity(), true);
			}

		} else {
			initGui(rootView, getActivity(), true);
			initApi();
		}

		return rootView;
	}


	/**
	 * Initializes and configures the components of the ui
	 * @param rootView: inflated view to get its child.
	 * @param context: the current context.
	 * @param showLoadingProgressBar: order to show the circular loading bar.
	 */
	private void initGui (View rootView, Context context, boolean showLoadingProgressBar) {
		progressBar = (ProgressBar) rootView.findViewById(R.id.fe_progress_bar);
		progressSpinner = (ProgressBar) rootView.findViewById(R.id.fe_spinner_progress);

		if (showLoadingProgressBar)
			progressSpinner.setVisibility(View.VISIBLE);

		// Main ListView
		eventsAdapter = new EventsAdapter(context, linkedEvents, eventAdapterListener);
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
				progressBar.setIndeterminate(true);
				progressBar.setVisibility(View.VISIBLE);

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
			nowLoading = false;

			if (progressBar.getVisibility() == View.VISIBLE) {
				progressBar.setVisibility(View.GONE);
				progressBar.setIndeterminate(false);
			}

			if (progressSpinner.getVisibility() == View.VISIBLE) {
				progressSpinner.setVisibility(View.INVISIBLE);
			}

			if (eventsRequestResponse != null) {
				errorLayout.setVisibility(View.INVISIBLE);
				eventsAdapter.clear();

				for (Event event : eventsRequestResponse)
					linkedEvents.addFirst(event);

			} else {
				errorLayout.setVisibility(View.VISIBLE);
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
				if (progressBar.getVisibility() == View.GONE)
					progressBar.setVisibility(View.VISIBLE);

				progressBar.setProgress((int) percent);

				if(percent >= 99) {
					progressBar.setIndeterminate(true);
					initGui(rootView, getActivity(), false);
					initApi();
				}
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


	EventAdapterListener eventAdapterListener = new EventAdapterListener() {
		@Override
		public void mapPressed (String location) {

		// Todo move all urls to a static class
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.es/maps?q="+location));
		startActivity(i);
		}
	};



}