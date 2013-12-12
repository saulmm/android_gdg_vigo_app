package fucverg.saulmm.gdg.plus;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.plus.PlusClient;
import fucverg.saulmm.gdg.utils.PlusUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author saulmm2@gmail.com
 */
@SuppressWarnings("unused")
public class GPlusHandler {

	public static final int RESOLV_ERROR = 101;

	private static GPlusHandler instance = null;

	private Context context;
	private ArrayList<GPlusListener> plusListeners;

	private PlusClient plusClient;
	private ConnectionResult connectionResult;


	/**
	 * Singleton implementation
	 *
	 * @param context app context
	 */
	private GPlusHandler (Context context) {
		this.context = context;
		this.plusListeners = new ArrayList<GPlusListener>();
	}


	/**
	 * Singleton getter implementation
	 *
	 * @param context app context
	 * @return a class instance
	 */
	public static GPlusHandler getInstance (Context context) {
		if(instance == null)
			instance = new GPlusHandler(context);

		return instance;
	}


	/**
	 * Add a listener to the plus even listener list to
	 * receive plus events.
	 *
	 * @param listener event listener
	 */
	public void addPlusListener (GPlusListener listener) {
		plusListeners.add(listener);
	}


	/**
	 * Remove the specified listener to the plus event
	 * listener list to stop to receive plus events.
	 *
	 * @param listener event listener
	 */
	public void removePlusListener (GPlusListener listener) {
		plusListeners.remove(listener);
	}


	/**
	 * Connect with google.
	 */
	public void connect () {

		if(plusClient != null) {
			connectionResult = null;
			plusClient.connect();

		} else
			throw new IllegalStateException("The plus client is null...");
	}


	/**
	 * Init he google plus client
	 *
	 * @param visibleActivities: the ways to save and share
	 * the user's actions  to their Google+ account.
	 */
	public void initPlusclient (String... visibleActivities) {
		PlusClient.Builder pClientBuilder = new PlusClient.Builder(
			context, conCallbacks, onConFailed);

		pClientBuilder.setVisibleActivities(visibleActivities);
		plusClient = pClientBuilder.build();
	}

	// TODO
	public void signInWithGoogle(Activity a)  {

		if(plusClient != null) {

			if(connectionResult == null) {
				connect();

			} else {
				try {
					connectionResult.startResolutionForResult(
							a, RESOLV_ERROR);


				} catch (IntentSender.SendIntentException e) {
					connectionResult = null;
					plusClient.connect();
				}
			}

		} else {
			throw new IllegalStateException("Plus client Is null");
		}
	}


	/**
	 * Public function to request an authenticated token
	 */
	public void requestToken(Activity a) {
		new TokenTasks().execute(a);
	}


	/**
	 * Task to generate a authenticated token to make request to
	 * plus http api
	 */
	private class TokenTasks extends AsyncTask<Activity, Void, String> {

		@Override
		protected String doInBackground (Activity... activities) {
			String token = null;

			try {
				token = GoogleAuthUtil.getToken(
						context, plusClient.getAccountName(),
						PlusUtils.PLUS_SCOPE);

			} catch (IOException e) {
				Log.e("[ERROR] fucverg.saulmm.gdg.data.plus.GPlusHandler.TokenTasks.doInBackground ",
						"Error: " + e.getMessage());

			} catch (UserRecoverableAuthException e) {
				activities[0].startActivityForResult(e.getIntent(), 1000);

			} catch (GoogleAuthException e) {
				e.printStackTrace();
			}

			return token;
		}


		@Override
		protected void onPostExecute (String token) {
			super.onPostExecute(token);

			for (GPlusListener plusListener : plusListeners) {
				plusListener.onTokenReceived(token);
			}
		}
	}





	private GooglePlayServicesClient.ConnectionCallbacks conCallbacks
			= new GooglePlayServicesClient.ConnectionCallbacks() {

		@Override
		public void onConnected (Bundle bundle) {
			for (GPlusListener plusListener : plusListeners) {
				plusListener.onConnected();
			}
		}


		@Override
		public void onDisconnected () {
			for (GPlusListener plusListener : plusListeners) {
				plusListener.onDisconnected();
			}
		}
	};


	private GooglePlayServicesClient.OnConnectionFailedListener onConFailed =
			new GooglePlayServicesClient.OnConnectionFailedListener() {

		@Override
		public void onConnectionFailed (ConnectionResult connectionResult) {
			for (GPlusListener plusListener : plusListeners) {
				plusListener.onnConnectionFailed(connectionResult);
			}
		}
	};



}