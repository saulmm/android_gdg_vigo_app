package ameiga.saulmm.gdg.gui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.koushikdutta.async.future.FutureCallback;
import ameiga.saulmm.gdg.Configuration;
import ameiga.saulmm.gdg.data.api.ApiHandler;
import ameiga.saulmm.gdg.data.api.entities.Url;
import ameiga.saulmm.gdg.data.db.DBHandler;
import ameiga.saulmm.gdg.data.db.entities.GroupInfo;
import ameiga.saulmm.gdg.data.db.entities.PlusPerson;
import ameiga.saulmm.gdg.utils.GuiUtils;
import ameiga.saulmm.gdg.R;

import static android.util.Log.d;
import static android.util.Log.e;

public class GroupFragment extends Fragment {
	private DBHandler dbHandler;
	private TextView groupName;
	private TextView groupSlogan;
	private TextView groupContent;
	private LinearLayout baseLayout;
	private LinearLayout groupURLLayout;
	private ProgressBar progressBarSpinner;
	private Button errorReloadButton;
	private LinearLayout errorLayout;
	private ScrollView scrollView;


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
		errorLayout = (LinearLayout) rootView.findViewById(R.id.error_layout);
		errorLayout.setVisibility(View.INVISIBLE);
		scrollView = (ScrollView) rootView.findViewById(R.id.fa_scrollView);

		final View _rootView = rootView;
		errorReloadButton = (Button) rootView.findViewById(R.id.error_reload_button);
		errorReloadButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View view) {
				d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.GroupFragment.onClick ",
						"Click");
				initUI(_rootView);
				initApi();
			}
		});
	}


	/**
	 * Tries to get the info of the group from the db, if there isn't info,
	 * makes a request to the 'google+ people' api to retrieve te about info
	 * of the gdg g+ profile and save it in the app db.
	 */
	private void initApi () {
		dbHandler = new DBHandler(getActivity());

		try {
			GroupInfo groupInfo = dbHandler.getAllElements(
					GroupInfo.class, null, null, false).get(0);

			if (baseLayout.getVisibility() == View.GONE)
				baseLayout.setVisibility(View.VISIBLE);

			fillAboutUIElements(groupInfo);

			Url [] urls = (Url[]) dbHandler.getAllElements(
					Url.class, null, null, false).toArray();

			fillUrlsLayout(urls);

		} catch (IndexOutOfBoundsException e) {
			ApiHandler apiHandler = new ApiHandler(getActivity());
			apiHandler.getGdgAboutInfo(plusPersonData);
			d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.GroupFragment.initApi ",
					"Making request...");

			progressBarSpinner.setVisibility(View.VISIBLE);
		}
	}


	/**
	 * Callback that is fired when the request is complete, for each url of the
	 * profile appends a textview into the url layout, after, put t¡he data in all
	 * ui elements in the fragment, and after inserts the data in the db to don't make
	 * more requests in the future.
	 */
	FutureCallback <PlusPerson> plusPersonData = new FutureCallback<PlusPerson>() {

		@Override
		public void onCompleted (Exception e, PlusPerson plusPerson) {
			progressBarSpinner.setVisibility(View.GONE);
			d("[DEBUG] fucverg.saulmm.gdg.gui.fragments.GroupFragment.onCompleted ",
					"Posts completed...");

			if(plusPerson != null) {
				baseLayout.setVisibility(View.VISIBLE);
				scrollView.setVisibility(View.VISIBLE);
				errorLayout.setVisibility(View.INVISIBLE);

				String content = plusPerson.getAboutMe()
						.replaceAll("<br />", "");

				fillUrlsLayout(plusPerson.getUrls());

				GroupInfo apiGroupInfo = new GroupInfo();
				apiGroupInfo.setAbout(content);
				apiGroupInfo.setId(plusPerson.getId());
				apiGroupInfo.setTagLine(plusPerson.getTagline());
				apiGroupInfo.setName(plusPerson.getDisplayName());

				fillAboutUIElements(apiGroupInfo);

				dbHandler.insertElement(GroupInfo.class,
						apiGroupInfo.getFields());

			} else {
				e("[ERROR] fucverg.saulmm.gdg.gui.fragments.AboutFragment.onCompleted ",
						"Error retrieving the gdg about data.");

				errorLayout.setVisibility(View.VISIBLE);
				scrollView.setVisibility(View.INVISIBLE);

				GuiUtils.showShortToast(getActivity(), "No hay red");
			}
		}
	};


	/**
	 * Parse a html tag <a href="www.example.com">example</a> and linkifies to 'example'
	 * then put that text in a text view and appends it to the ulr viewgroup.
	 *
	 * @param urls: urls to linkify.
	 */
	private void fillUrlsLayout (Url[] urls) {

		if (getActivity() != null) {
			for (Url link : urls) {
				String url = "<a href=\"" + link.getValue() + "\"> - " + link.getLabel() + "</a>\n";

				TextView urlTextView = new TextView(getActivity());
				urlTextView.setText(Html.fromHtml(url));
				urlTextView.setMovementMethod(LinkMovementMethod.getInstance());
				urlTextView.setTextAppearance(getActivity(), R.style.LinkStyle);

				groupURLLayout.addView(urlTextView, GuiUtils.getLinkParams());

				Url urlEnt = new Url();
				urlEnt.setGroup_id(Configuration.GROUP_ID);
				urlEnt.setLabel(link.getLabel());
				urlEnt.setValue(link.getValue());

				dbHandler.insertElement(Url.class,
						urlEnt.getFields());
			}
		}
	}


	private void fillAboutUIElements (GroupInfo groupInfo) {
		groupName.setText(groupInfo.getName());
		groupSlogan.setText(groupInfo.getTagLine());
		groupContent.setText(Html.fromHtml(groupInfo.getAbout()));
	}
}
