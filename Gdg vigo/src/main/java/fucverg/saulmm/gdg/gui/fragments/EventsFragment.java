package fucverg.saulmm.gdg.gui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fucverg.saulmm.gdg.data.api.ApiHandler;

public class EventsFragment extends Fragment {

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		TextView text = new TextView(getActivity());
		text.setText("Events");

		// Debug
		ApiHandler aHandler = new ApiHandler(getActivity());
		aHandler.testRequest();
		Log.d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.EventsFragment.onCreateView ", "Debuggin: " + aHandler.getActivitiesURL());


		return text;
	}
}
