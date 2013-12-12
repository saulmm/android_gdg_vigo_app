package fucverg.saulmm.gdg.data.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import fucverg.saulmm.gdg.Configuration;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.db.DBHandler;
import fucverg.saulmm.gdg.data.db.entities.Event;
import fucverg.saulmm.gdg.data.db.entities.Member;
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.PlusRequestInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.util.Log.DEBUG;
import static android.util.Log.d;


public class ApiHandler {
	final String apiEndPoint = "https://www.googleapis.com/plus/v1/";
	final String eventsEndPoint = "https://developers.google.com/events/feed/json";
	private final Context context;
	private final DBHandler dbHandler;
	private int eventCount = 0;


	public ApiHandler (Context con) {
		this.context = con;

		// Enable the ion global log
		Ion.getDefault(con).setLogging("Http", DEBUG);
		dbHandler = new DBHandler(con);
	}



	public void getActivities (String nextPageToken, FutureCallback<PlusRequestInfo> plusSearchCallBack) {
		Ion.with(context, getActivitiesURL(nextPageToken))
				.as(PlusRequestInfo.class)
				.setCallback(plusSearchCallBack);
	}


	public String getActivitiesURL (String nextToken) {
		Log.d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.getActivitiesURL ", "Getting Activities...");

		Uri.Builder uriBuilder = new Uri.Builder()
				.path(apiEndPoint)
				.appendPath("activities")
				.appendQueryParameter("query", "gdg+vigo")
				.appendQueryParameter("maxResults", "20")
				.appendQueryParameter("key", Configuration.API_KEY);

		if (nextToken != null)
			uriBuilder.appendQueryParameter("pageToken", nextToken);

		return Uri.decode(uriBuilder.build().toString());
	}


	public String getEventURL (String gdgGroupID) {
		String eventURL = new Uri.Builder()
				.path(eventsEndPoint)
				.appendQueryParameter("group", gdgGroupID)
				.appendQueryParameter("start", "0")
				.build().toString();

		return Uri.decode(eventURL);
	}


	public void getEvents (FutureCallback<List<Event>> gdgEventsCallback) {
		Ion.with(context, getEventURL(Configuration.GDG_VIGO_ID))
				.as(new TypeToken<List<Event>>() {})
				.setCallback(gdgEventsCallback);
	}



	public List<Member> getMembers () {
		InputStream is = context.getResources().openRawResource(R.raw.members);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		Type type = new TypeToken<ArrayList<Member>>() {}.getType();
		Gson gson = new Gson();

		List<Member> membersJSON = gson.fromJson(reader, type);
		List<Member> membersDB = dbHandler.getAllElements(new Member());

		d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.getMembers ",
				"Members JSON : " + membersJSON.size());

		d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.getMembers ",
				"Members DB: " + membersDB.size());

		// +1 because db starts at 1
		if (membersJSON.size() > (membersDB.size() + 1))
			for (Member member : membersJSON)
				dbHandler.insertMember(member);

		else
			d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.getMembers ",
					"No new members...");

		return membersJSON;
	}
}