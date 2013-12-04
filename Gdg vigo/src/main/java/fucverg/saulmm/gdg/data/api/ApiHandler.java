package fucverg.saulmm.gdg.data.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import fucverg.saulmm.gdg.Configuration;
import fucverg.saulmm.gdg.data.api.entities.Activity;
import fucverg.saulmm.gdg.data.api.entities.Attachments;
import fucverg.saulmm.gdg.data.api.entities.PlusRequestInfo;
import fucverg.saulmm.gdg.data.db.DBHandler;

import static android.util.Log.d;


public class ApiHandler {
	final String apiEndPoint = "https://www.googleapis.com/plus/v1/";
	private final Context con;
	private final DBHandler dbHandler;
	private int eventCount = 0;


	public ApiHandler (Context con) {
		this.con = con;

		// Enable the ion global loggin
		Ion.getDefault(con).setLogging("Http", Log.DEBUG);
		dbHandler = new DBHandler(con);
	}


	FutureCallback<PlusRequestInfo> plusSearchCallBack = new FutureCallback<PlusRequestInfo>() {
		String nextPageToken = "";


		@Override
		public void onCompleted (Exception e, PlusRequestInfo plusRequestInfo) {

			if (plusRequestInfo != null) {
				nextPageToken = plusRequestInfo.nextPageToken;


				for (Activity act : plusRequestInfo.items) {

					String id = act.id;
					String title = act.title;
					String idMember = act.actor.id;
					String date = act.published;
					String url = act.url;

					String content_title = "";
					String content_description = "";
					String content_type = "";
					String content_url = "";


					if (act.object.attachments != null) {
						for (Attachments attach : act.object.attachments) {
							content_type = attach.objectType;
							content_url = attach.url;
							content_description = attach.content;
							content_title = attach.displayName;
						}
					}

					d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.onCompleted ", "id " + id + " " +
							"title" + title + " idMember " + idMember + " date " + date + " ctitle " + content_title + " cdes " + content_description +
							"ctype: " + content_type + " curl: " + content_url);

					dbHandler.insertActivity(id, title, url, idMember, content_description, content_type, content_title, content_url, date);
				}

				d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.onCompleted ", "Next Page Token : "+nextPageToken+"\n");
				if (nextPageToken != null && plusRequestInfo.items.size() > 0)
					activityRequest(nextPageToken);

			} else {
				Log.e("[ERROR] fucverg.saulmm.gdg.data.api.ApiHandler.onCompleted ", "Error : " + e.getMessage());
			}

		}
	};


		public void activityRequest (String nextPageToken) {
			Ion.with(con, getActivitiesURL(nextPageToken))
					.as(PlusRequestInfo.class)
					.setCallback(plusSearchCallBack);
		}


		public String getActivitiesURL (String nextToken) {


			Uri.Builder uriBuilder = new Uri.Builder()
					.path(apiEndPoint)
					.appendPath("activities")
					.appendQueryParameter("query", "gdg+vigo")
					.appendQueryParameter("maxResults", "20")
					.appendQueryParameter("key", Configuration.API_KEY);

			if (nextToken != null )
				uriBuilder.appendQueryParameter("pageToken", nextToken);

			return Uri.decode(uriBuilder.build().toString());

		}
	}