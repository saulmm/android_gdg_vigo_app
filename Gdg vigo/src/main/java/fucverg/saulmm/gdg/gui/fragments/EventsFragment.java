package fucverg.saulmm.gdg.gui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.koushikdutta.async.future.FutureCallback;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.api.ApiHandler;
import fucverg.saulmm.gdg.data.db.entities.Event;
import fucverg.saulmm.gdg.gui.adapters.EventAdapterListener;
import fucverg.saulmm.gdg.gui.adapters.EventsAdapter;

import java.util.LinkedList;
import java.util.List;

import static android.util.Log.d;

public class EventsFragment extends Fragment {
//	private DBHandler dbHandler;
	private LinkedList<Event> linkedEvents;
	private ListView eventList;
	private ProgressBar progressSpinner;
	private EventsAdapter eventsAdapter;
	private TextView errorTestView;



	@Override
	@SuppressWarnings("unchecked")
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_event, null);
		linkedEvents = new LinkedList<Event>();

		if(savedInstanceState != null) {
			Object restoredEvents = savedInstanceState.getSerializable("events");

			if (restoredEvents instanceof LinkedList) {
				linkedEvents = (LinkedList<Event>) restoredEvents;
				initUI(rootView);
			}

		} else {
			initUI(rootView);
			initApi();
		}

		return rootView;
	}


	private void initUI (View rootView) {
		eventList = (ListView) rootView.findViewById(R.id.fe_events_list);
		progressSpinner = (ProgressBar) rootView.findViewById(R.id.fe_spinner_progress);
		errorTestView = (TextView) rootView.findViewById(R.id.fe_error_message);

		eventsAdapter = new EventsAdapter(getActivity(), linkedEvents, eventAdapterListener);
		eventList.setAdapter(eventsAdapter);
	}


	private void initApi () {
		ApiHandler apiHanler = new ApiHandler(getActivity());
		apiHanler.getEventsURL();
		apiHanler.makeEventRequest(gdgEventsCallback);

		if (progressSpinner.getVisibility() == View.INVISIBLE)
			progressSpinner.setVisibility(View.VISIBLE);
	}


	@Override
	public void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.EventsFragment.onSaveInstanceState ",
				"Numbers of members saved: "+linkedEvents.size());

		outState.putSerializable("events", linkedEvents);
	}


	FutureCallback<List<Event>> gdgEventsCallback = new FutureCallback<List<Event>>() {
		@Override
		public void onCompleted (Exception e, List<Event> events) {
			d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.EventsFragment.onCompleted ",
					"Request complete...");

			if (progressSpinner.getVisibility() == View.VISIBLE)
				progressSpinner.setVisibility(View.INVISIBLE);


			if(events != null) {
				for (Event event : events) {
					linkedEvents.addFirst(event);
				}

				eventsAdapter.addAll(linkedEvents);

			} else {
				Log.e("[ERROR] fucverg.saulmm.gdg.gui.fragments.EventsFragment.onCompleted ",
						"Error : " + e.getMessage());

				if(errorTestView.getVisibility() == View.INVISIBLE)
					errorTestView.setVisibility(View.VISIBLE);
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





