package fucverg.saulmm.gdg.gui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.koushikdutta.async.future.FutureCallback;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.api.ApiHandler;
import fucverg.saulmm.gdg.data.api.entities.Url;
import fucverg.saulmm.gdg.data.db.DBHandler;
import fucverg.saulmm.gdg.data.db.entities.GroupInfo;
import fucverg.saulmm.gdg.data.db.entities.PlusPerson;
import fucverg.saulmm.gdg.utils.GuiUtils;

import static android.util.Log.e;

public class AboutFragment extends Fragment {
	private DBHandler dbHandler;

	private TextView groupName;
	private TextView groupSlogan;
	private TextView groupContent;
	private LinearLayout baseLayout;
	private LinearLayout groupURLLayout;
	private ProgressBar progressBarSpinner;


	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_about, null);
		initUI(rootView);
		initApi();

		return rootView;
	}


	private void initUI (View rootView) {
		groupName = (TextView) rootView.findViewById(R.id.fa_title);
		groupSlogan = (TextView) rootView.findViewById(R.id.fa_slogan);
		groupContent = (TextView) rootView.findViewById(R.id.fa_content);
		groupURLLayout = (LinearLayout) rootView.findViewById(R.id.fa_links_layout);
		progressBarSpinner = (ProgressBar) rootView.findViewById(R.id.fa_progress_spinner);
		baseLayout = (LinearLayout) rootView.findViewById(R.id.fa_framelayout);
	}


	/**
	 * Tries to get the info of the group from the db, if there isn't info,
	 * makes a request to the google+ people api to retrieve te about info
	 * of the gdg profile and save it int the app db.
	 */
	private void initApi () {
		dbHandler = new DBHandler(getActivity());

		try {
			GroupInfo groupInfo = dbHandler.getAllElements(
					new GroupInfo(), null, null, false).get(0);

			if (baseLayout.getVisibility() == View.GONE)
				baseLayout.setVisibility(View.VISIBLE);

			fillAboutUIElements(groupInfo);

		} catch (IndexOutOfBoundsException e) {

			ApiHandler apiHandler = new ApiHandler(getActivity());
			apiHandler.getGdgAboutInfo(plusPersonData);

			if (progressBarSpinner.getVisibility() == View.INVISIBLE)
				progressBarSpinner.setVisibility(View.VISIBLE);
		}
	}


	/**
	 * Callback that is fired when the request is complete, for each url of the
	 * profile appends a textview into the url layout, after, put the data in all
	 * ui elements in the fragment, and after inserts the data in the db to don't make
	 * more requests in the future.
	 */
	FutureCallback <PlusPerson> plusPersonData = new FutureCallback<PlusPerson>() {

		@Override
		public void onCompleted (Exception e, PlusPerson plusPerson) {
			progressBarSpinner.setVisibility(View.GONE);
			baseLayout.setVisibility(View.VISIBLE);

			if(plusPerson != null) {
				String content = plusPerson.getAboutMe()
						.replaceAll("<br />", "");

				for (Url link : plusPerson.getUrls()) {
					String url = "<a href=\"" + link.getValue() + "\"> - " + link.getLabel() + "</a>\n";

					TextView urlTextView = new TextView(getActivity());
					urlTextView.setText(Html.fromHtml(url));
					urlTextView.setMovementMethod(LinkMovementMethod.getInstance());
					urlTextView.setTextAppearance(getActivity(), R.style.LinkStyle);

					groupURLLayout.addView(urlTextView, GuiUtils.getLinkParams());
				}

				GroupInfo apiGroupInfo = new GroupInfo();
				apiGroupInfo.setAbout(content);
				apiGroupInfo.setId(plusPerson.getId());
				apiGroupInfo.setTagLine(plusPerson.getTagline());
				apiGroupInfo.setName(plusPerson.getDisplayName());

				fillAboutUIElements(apiGroupInfo);

				dbHandler.insertGroupInfo(apiGroupInfo);

			} else {
				e("[ERROR] fucverg.saulmm.gdg.gui.fragments.AboutFragment.onCompleted ",
						"Error retrieving the gdg about data.");
			}
		}
	};


	private void fillAboutUIElements (GroupInfo groupInfo) {
		groupName.setText(groupInfo.getName());
		groupSlogan.setText(groupInfo.getTagLine());
		groupContent.setText(Html.fromHtml(groupInfo.getAbout()));
	}
}
