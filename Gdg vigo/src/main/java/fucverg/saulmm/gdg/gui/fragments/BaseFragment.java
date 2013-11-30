package fucverg.saulmm.gdg.gui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fucverg.saulmm.gdg.R;

public class BaseFragment extends Fragment {
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_base, null);
		return rootView;
	}
}
