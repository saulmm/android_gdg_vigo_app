package fucverg.saulmm.gdg.gui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.api.ApiHandler;
import fucverg.saulmm.gdg.data.db.entities.PlusPerson;
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.PlusLink;
import fucverg.saulmm.gdg.utils.GuiUtils;

import static android.util.Log.e;

public class AboutFragment extends Fragment {

	private TextView groupName;
	private ImageView groupImage;
	private TextView groupSlogan;
	private TextView groupContent;
	private LinearLayout linksLayout;

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_about, null);
		initGui(rootView);
		initApi();
		return rootView;
	}


	private void initGui (View rootView) {
		groupName = (TextView) rootView.findViewById(R.id.fa_title);
		groupSlogan = (TextView) rootView.findViewById(R.id.fa_slogan);
		groupImage = (ImageView) rootView.findViewById(R.id.fa_image);
		groupContent = (TextView) rootView.findViewById(R.id.fa_content);
		linksLayout = (LinearLayout) rootView.findViewById(R.id.fa_links_layout);

	}


	private void initApi () {
		ApiHandler apiHandler = new ApiHandler(getActivity());
		apiHandler.getGdgAboutInfo(plusPersonData);
	}


	FutureCallback <PlusPerson> plusPersonData = new FutureCallback<PlusPerson>() {

		@Override
		public void onCompleted (Exception e, PlusPerson plusPerson) {
			if(plusPerson != null) {
				String content = plusPerson.getAboutMe().replaceAll("<br />", "");

				groupName.setText(plusPerson.getDisplayName());
				groupSlogan.setText(plusPerson.getTagline());
				groupContent.setText(Html.fromHtml(content));

				for (PlusLink link : plusPerson.getUrls()) {
					String url = "<a href=\"" + link.getValue() + "\"> - " + link.getLabel() + "</a>\n";
					TextView newLink = new TextView(getActivity());
					newLink.setText(Html.fromHtml(url));
					newLink.setMovementMethod(LinkMovementMethod.getInstance());

					newLink.setTextAppearance(getActivity(), R.style.LinkStyle);
					linksLayout.addView(newLink, GuiUtils.getLinkParams());
				}

				Ion.with(getActivity(), "https://plus.google.com/s2/photos/profile/"+plusPerson.getId()+"?sz=100")
						.withBitmap()
						.placeholder(R.drawable.placeholder)
						.intoImageView(groupImage);

			} else {
				e("[ERROR] fucverg.saulmm.gdg.gui.fragments.AboutFragment.onCompleted ",
						"Error retrieving the gdg about data");

			}
		}
	};
}
