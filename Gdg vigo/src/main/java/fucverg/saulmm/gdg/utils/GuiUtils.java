package fucverg.saulmm.gdg.utils;

import android.content.Context;
import android.text.util.Linkify;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import fucverg.saulmm.gdg.data.db.DBHandler;
import fucverg.saulmm.gdg.data.db.entities.Member;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuiUtils {
	private static final Pattern PLUS_PATTERN= Pattern.compile("\\+(\\w+[ \\w+]+)");
	private static final Pattern HASHTAG_PATTERN = Pattern.compile("(#\\w+)");
	private static final Pattern MENTION_PATTERN= Pattern.compile("@(\\w+)");
	private static final Pattern LINK_PATTERN = Pattern.compile("Link: \\w+");

	public final static String PLUS_URL =  "https://plus.google.com/";
	public final static String TWITTER_URL = "https://twitter.com/";
	public final static String PLUS_SEARCH_URL = PLUS_URL + "u/0/s";

	public static DBHandler GUI_DB_HANDLER;

	static final LinearLayout.LayoutParams link_params = new
			LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


	public static LinearLayout.LayoutParams getLinkParams () {
		link_params.setMargins(0, 0, 0, 30);
		return link_params;
	}

	/**
	 * TransformFilter used to manage the matched pattern in the url,
	 * based in the name on the plus person, ask to the database to the id of the member
	 * by the name given, if the member is not found, we return a search url.
	 */
	private final static Linkify.TransformFilter plusTransformFilter = new Linkify.TransformFilter() {

		@Override
		public String transformUrl (Matcher matcher, String url) {
			String personName = matcher.group(1);
			Member member = GUI_DB_HANDLER.getMemberbyName(personName);

			if ( member != null ) {
				url = member.getId();

			} else {
				url = "u/0/s/" + personName.replace( " ", "%20" );
			}

			return url;
		}
	};

	private final static Linkify.TransformFilter hashtagMentionTransform = new Linkify.TransformFilter() {

		@Override
		public String transformUrl (Matcher matcher, String url) {
			String hashtagMention = matcher.group(1);
			return url + hashtagMention;
		}
	};



	private final static  Linkify.TransformFilter linkTransform = new Linkify.TransformFilter() {
		@Override
		public String transformUrl (Matcher matcher, String s) {
			return s;
		}
	};





	/**
	 * Indicates that the web urls should be linkiables in
	 * the target
	 *
	 * @param target the textView to linkify.
	 */
	public static void addAllLinksLinkify (TextView target) {
		Linkify.addLinks(target, Linkify.WEB_URLS);
	}


	/**
	 * Indicates that the plus names should be links pointing
	 * to the name profile or a name search
	 */
	public static void addPlusLinkify (TextView target) {
		Linkify.addLinks(target, PLUS_PATTERN,
				PLUS_URL, null, plusTransformFilter);
	}


	public static void addLinkLinkify (TextView target) {
		Linkify.addLinks(target, LINK_PATTERN, "", null, linkTransform );
	}



	public static void addHashtagLinkify (TextView target) {
		Linkify.addLinks(target, HASHTAG_PATTERN,
				PLUS_SEARCH_URL, null, hashtagMentionTransform);
	}


	public static void addMentionLinkify (TextView target) {
		Linkify.addLinks(target, HASHTAG_PATTERN,
				TWITTER_URL, null, hashtagMentionTransform);
	}


	public static void showShortToast (Context c, String s) {
		Toast.makeText(c, s, Toast.LENGTH_SHORT).show();
	}


	public static void linkifyTextView (TextView target) {
		addAllLinksLinkify(target);
		addPlusLinkify(target);
		addHashtagLinkify(target);
		addMentionLinkify(target);
	}
}
