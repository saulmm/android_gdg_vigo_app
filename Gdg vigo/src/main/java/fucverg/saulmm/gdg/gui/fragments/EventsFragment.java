package fucverg.saulmm.gdg.gui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.koushikdutta.async.future.FutureCallback;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.api.ApiHandler;
import fucverg.saulmm.gdg.data.db.entities.Event;
import fucverg.saulmm.gdg.gui.activities.MainActivity;
import fucverg.saulmm.gdg.gui.activities.UpdateListener;
import fucverg.saulmm.gdg.gui.adapters.EventAdapterListener;
import fucverg.saulmm.gdg.gui.adapters.EventsAdapter;
import fucverg.saulmm.gdg.gui.views.PullListView;
import fucverg.saulmm.gdg.gui.views.PullRefreshListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.util.Log.d;

public class EventsFragment extends Fragment {
	private LinkedList<Event> linkedEvents;
	private PullListView eventList;
	private ProgressBar progressSpinner;
	private EventsAdapter eventsAdapter;
	private TextView errorTestView;
	private View rootView;
	private ApiHandler apiHanler;
	private TimerTask timerClass;
	private ProgressBar progressBar;


	public EventsFragment () {
		MainActivity.upListeners.add(updateCallback);
	}


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
				initGui(rootView, getActivity());
			}

		} else {
			initGui(rootView, getActivity());
			initApi(getActivity());
		}

		return rootView;
	}


	private void initGui (View rootView, Context context) {
		progressBar = (ProgressBar) rootView.findViewById(R.id.fe_progress_bar);
		
		eventList = (PullListView) rootView.findViewById(R.id.fe_events_list);
		eventList.setListener(pullCallback);

		progressSpinner = (ProgressBar) rootView.findViewById(R.id.fe_spinner_progress);
		errorTestView = (TextView) rootView.findViewById(R.id.fe_error_message);

		eventsAdapter = new EventsAdapter(context, linkedEvents, eventAdapterListener);
		eventList.setAdapter(eventsAdapter);
	}





	private void initApi (Context context) {
		apiHanler.makeEventRequest(gdgEventsCallback);

		if (progressSpinner.getVisibility() == View.INVISIBLE) {
			progressSpinner.setVisibility(View.VISIBLE);
			d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.EventsFragment.initApi ",
					"The spinner is now visible");

		} else {
			d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.EventsFragment.initApi ",
					"The spinner now is not visible");
		}
	}


	@Override
	public void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("events", linkedEvents);
	}


	UpdateListener updateCallback = new UpdateListener() {
		@Override
		public void onUpdate (Context context) {
			linkedEvents.clear();
			eventsAdapter.notifyDataSetChanged();
			apiHanler.makeEventRequest(gdgEventsCallback);

			Timer myTimer = new Timer();
			myTimer.schedule(timerClass, 0, 1000);

		}
	};


	FutureCallback<List<Event>> gdgEventsCallback = new FutureCallback<List<Event>>() {
		@Override
		public void onCompleted (Exception e, List<Event> events) {
			d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.EventsFragment.onCompleted ",
					"Request complete...");


			if (progressBar.getVisibility() == View.VISIBLE) {
				progressBar.setVisibility(View.GONE);
				progressBar.setIndeterminate(false);
			}

			if (progressSpinner.getVisibility() == View.VISIBLE) {
				progressSpinner.setVisibility(View.INVISIBLE);
			}

			if (events != null) {
				eventsAdapter.clear();

				for (Event event : events)
					linkedEvents.addFirst(event);

			} else {
				Log.e("[ERROR] fucverg.saulmm.gdg.gui.fragments.EventsFragment.onCompleted ",
						"Error : " + e.getMessage());

				if (errorTestView.getVisibility() == View.INVISIBLE)
					errorTestView.setVisibility(View.VISIBLE);
			}
		}
	};



	PullRefreshListener pullCallback = new PullRefreshListener() {
		@Override
		public void onRefresh (float percent) {

			if (progressBar.getVisibility() == View.GONE)
				progressBar.setVisibility(View.VISIBLE);

			progressBar.setProgress((int) (percent + 5));

			if(percent >= 99) {
				progressBar.setIndeterminate(true);
				initGui(rootView, getActivity());
				initApi(getActivity());
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