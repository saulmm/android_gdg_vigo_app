package fucverg.saulmm.gdg.data.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

import static android.util.Log.d;


public class ApiHandler {
	final String apiEndPoint = "https://www.googleapis.com/plus/v1/";
	final String testURL = "https://www.googleapis.com/plus/v1/activities?query=gdg+vigo&maxResults=20&pageToken=&pageToken=Ci0IoIYzEiWCh1Cz3n2z3n37-q0B-_qtAZi37gSYt-4Ex4XbBceF2wXp2sIGGAIKJxIlnp9QpYBaxsBj9tyCAdDHkQGp_64BgOC1AcWN1AHJ090BurjxAQooEiaRhwmRhwma3ZQBsfCkAZmc2AGE5LACy7ypA8u8qQOe1MYDiY3KAwopEifmpA2T4q0BuKfmAevnjQK81tMC_6mwA_-psAP5-KIE1p-8BNarkwUKKRIn_uMCsKmEAc787gH_2LECpdrVAvvH9AKfn6UD2PnGA52O8AOVsJsECiUSI6P4LqP4LuzZQNaoU9aoU8uJigLLiYoCtuqQAuz-oALs_qACCicSJcqhTsqhTsqhTuK5owPiuaMD4rmjA-K5owOxj64EsY-uBLGPrgQKKhIoy86UA8vOlAPLzpQD6qWwBZS4lwrdnskK0KOFDdCjhQ3Qo4UN0KOFDQomEiTPgBXXkhXQ4kSOxEng5oQB_tiVAfPbnAHz25wB6aPaAaaz3AEKKhIoy5P8AZXatQLen58D3p-fA-zvpATs76QE7JvNBeybzQXMm40GzJuNBgokEiKgyhzq_yj07EXz-0uKk03ih1aZ7uABi8bUAvnT7QKb2e0CCikSJ_bgbv-GpgHE59UB47KDAsTDjwKP9aYD6-KxA7jIwQP2358E1sqpBAomEiTJnQTkggfeowzhkVKuiqIBl-rzAdHAjQLyq8ECr7LYAv-f3gIKKBImla0xxd1D7pqNAZDo-QGoyrMD_6O2A_212AO8o_AD5ZDBBMy_zQQKJRIjppEs9O0zp8c6k55q3b1_i8m6AbLP6gGHkYICuY6KAs7gkwIKLwiiggMSJ7KuTOnKnQLtrMMCuKiGA8zBsQPMwbEDyJuyA_fIgATnqK0EqpPrBBgBEJXeipIFGPvD15QFIgA&key=AIzaSyDxLA_UStQTt9EdW5WTVUtdtQ38EkDmqeQ";
	private final Context con;


	public ApiHandler (Context con) {
		this.con = con;
	}


	public void testRequest () {

		Ion.with(con, testURL)
				.as(GsonHelperClass.class)
				.setCallback(new FutureCallback<GsonHelperClass>() {
					@Override
					public void onCompleted (Exception e, GsonHelperClass tweets) {
						d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.onCompleted ", "SIZE OF ELEMENTS  " + tweets.getItems().size());

						for (GsonHelperClass.ActivityGson act : tweets.getItems()) {
							d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.onCompleted ", "PUB TITLE: " + act.getTitle());
							d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.onCompleted ", "NAME OF ACTOR: " + act.getActor().getDisplayName());
							d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.onCompleted ", "OBJECT TYPE: " + act.getObject().getObjectType());
							try {
								for(GsonHelperClass.Attachments att : act.getObject().attachments) {
									Log.d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.onCompleted ", "At obType: " + att.objectType);
								}
							} catch (Exception ea) {
								d("[DEBUG] fucverg.saulmm.gdg.data.api.ApiHandler.onCompleted ", "Broken "+ea.getMessage() );
							}

						}
					}
				});
	}


	public String getActivitiesURL () {

		Uri uriBuilder = new Uri.Builder()
				.path(apiEndPoint)
				.appendPath("activities")
				.appendQueryParameter("query", "gdg+vigo")
				.appendQueryParameter("maxResults", "20")
				.build();


		return uriBuilder.toString();


	}
}

class GsonHelperClass {
	String kind;
	String etag;
	String nextPageToken;
	String selfLink;
	String title;
	String updated;
	List<ActivityGson> items;


	public String getNextPageToken () {
		return nextPageToken;
	}


	public List<ActivityGson> getItems () {
		return items;


	}


	class ActivityGson {
		String kind;
		String etag;
		String title;
		String published;
		String updated;
		String url;
		Actor actor;
		String verb;
		NestedObject object;


		public NestedObject getObject () {
			return object;
		}


		public Actor getActor () {
			return actor;
		}


		public String getTitle () {
			return title;
		}
	}

	class Actor {
		String id;
		String displayName;
		String url;
		ActorImage image;


		public String getDisplayName () {
			return displayName;
		}
	}

	class ActorImage {
		String url;
	}

	class NestedObject {
		String objectType;
		String content;
		String url;
//	List<Replies> replies;
//	List<Pluson> plusoners;
//	List<Reshare> resharers;
	Attachments[] attachments;


		public String getObjectType () {
			return objectType;
		}
	}

	class Replies {
		String totalItems;
		String selfLink;
	}

	class Reshare {
		String totalItems;
		String selfLink;
	}

	class Pluson {
		String totalItems;
		String selfLink;
	}

	class Attachments {
		String objectType;
		String displayName;
		String content;
		String url;
	}
}
