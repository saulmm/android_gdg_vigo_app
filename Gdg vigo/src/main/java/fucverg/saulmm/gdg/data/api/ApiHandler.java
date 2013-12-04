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

import static android.util.Log.d;


public class ApiHandler {
	final String apiEndPoint = "https://www.googleapis.com/plus/v1/";
	final String testURL = "https://www.googleapis.com/plus/v1/activities?query=gdg+vigo&maxResults=20&key=AIzaSyDxLA_UStQTt9EdW5WTVUtdtQ38EkDmqeQ";
	private final Context con;
	private int eventCount =0;


	public ApiHandler (Context con) {
		this.con = con;

		// Enable the ion global loggin
		Ion.getDefault(con).setLogging("Http", Log.DEBUG);

	}
	
	
	FutureCallback<PlusRequestInfo> plusSearchCallBack = new FutureCallback<PlusRequestInfo>() {
		@Override
		public void onCompleted (Exception e, PlusRequestInfo plusRequestInfo) {
			
			if(plusRequestInfo != null) {
				
			} else {
				Log.e("[ERROR] fucverg.saulmm.gdg.data.api.ApiHandler.onCompleted ", "");
			}
				
		}
	};


	public void testRequest (String nexToken) {

		Ion.with(con, getActivitiesURL(nexToken))
				.as(PlusRequestInfo.class)
				.setCallback(new FutureCallback<PlusRequestInfo>() {
					@Override
					public void onCompleted (Exception e, PlusRequestInfo requestInfo) {
						String requestToken = null;

						if (requestInfo != null) {
							 requestToken = requestInfo.nextPageToken;

//							d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.onCompleted ", "Next token is: "+requestToken);
//							d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.onCompleted ", "The url is: "+getActivitiesURL(requestToken));


							if(requestInfo.items == null)
								return;

							for (Activity act : requestInfo.items) {
								String output = "\n/////////  Events to date : "+eventCount+" ////////////////////////\n";
								d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.onCompleted ", "The provider is: "+act.provider.title);

								output +="The provider of this activity is: "+act.provider.title.toUpperCase()+"\n";

								if(act.object.attachments != null) {

									for (Attachments attach : act.object.attachments ) {
										if(attach.objectType.equals("EVENT"))
											eventCount++;

										output += "Attachment url: "+attach.url+"\n";
										output += "Attachment content: "+attach.content+"\n";

									}
								}

								output +=
										"Act id: "+act.id+" \n"+
										"Act url: "+act.url+"\n"+
										"Pub title: "+act.title+ "\n" +
										"Actor: "+act.actor.displayName+"\n" +
										"Actor id: "+act.actor.id+"\n" +
										"Actor img: "+act.actor.id+"\n" +
										"Number of +1: "+act.object.plusoners.totalItems+"\n"+
										"Number of replies: "+act.object.replies.totalItems+"\n"+
										"Number of shares: "+act.object.resharers.totalItems+"\n"+
										"Date: "+act.published;

								output += "\n/////////////////////////////////\n";
								d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.onCompleted ", ""+output);
							}

							if(requestToken != null)
								testRequest(requestToken);

						} else {
							Log.e("[ERROR] fucverg.saulmm.gdg.data.api.ApiHandler.onCompleted ", "Error : " + e.getMessage());
						}


					}
				});
	}


	public String getActivitiesURL (String nextToken) {



		Uri.Builder uriBuilder = new Uri.Builder()
				.path(apiEndPoint)
				.appendPath("activities")
				.appendQueryParameter("query", "gdg+vigo")
				.appendQueryParameter("maxResults", "20")
				.appendQueryParameter("key", Configuration.API_KEY);

		if(nextToken != null)
			uriBuilder.appendQueryParameter("pageToken", nextToken);

		return Uri.decode(uriBuilder.build().toString());


	}
}