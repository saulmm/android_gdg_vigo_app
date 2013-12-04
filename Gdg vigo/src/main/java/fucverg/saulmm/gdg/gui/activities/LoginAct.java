package fucverg.saulmm.gdg.gui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.db.DBHandler;
import fucverg.saulmm.gdg.data.plus.GPlusHandler;
import fucverg.saulmm.gdg.data.plus.GPlusListener;

public class LoginAct extends Activity {

	private GPlusHandler plusHandler;
	private ProgressDialog progressDialog;
	private SignInButton googleSignInButton;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		plusHandler = GPlusHandler.getInstance(this);
		plusHandler.addPlusListener(plusCallbacks);
		plusHandler.initPlusclient(
				"http://schemas.google.com/AddActivity",
				"http://schemas.google.com/ListenActivity");

		initUI();


		// Debug


		DBHandler dbHandler = new DBHandler(this);
		dbHandler.insertActivity("apsdefa","adf","adsf","apsdofa","adf","adsf","apsdofa","adf","adsf");

	}


	private void initUI () {
		setContentView(R.layout.activity_login);

		googleSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
		googleSignInButton.setOnClickListener(onClickCallBack);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Please wait..."); // todo extract string
	}


	private View.OnClickListener onClickCallBack = new View.OnClickListener() {

		@Override
		public void onClick (View view) {

			if (view.getId() == R.id.sign_in_button) {
				googleSignInButton.setEnabled(false);

				progressDialog.show();
				plusHandler.signInWithGoogle(LoginAct.this);
			}
		}
	};


	public GPlusListener plusCallbacks = new GPlusListener() {
		@Override
		public void onConnected () {
			progressDialog.dismiss();
			Toast.makeText(LoginAct.this, "Connection successfull :D", Toast.LENGTH_SHORT).show();

			Intent mainActintent = new Intent(LoginAct.this, LoginActivity.class);
			startActivity(mainActintent);

			LoginAct.this.finish();
		}


		@Override
		public void onnConnectionFailed (ConnectionResult connectionResult) {
			googleSignInButton.setEnabled(true);
			progressDialog.dismiss();

			Log.d("[DEBUG] fucverg.saulmm.gdg.gui.activities.LoginAct.onConnectionFailed ", "Failed");
			try {
				connectionResult.startResolutionForResult(LoginAct.this, GPlusHandler.RESOLV_ERROR);
			} catch (IntentSender.SendIntentException e) {
				e.printStackTrace();
			}
		}


		@Override
		public void onTokenReceived (String token) {

		}


		@Override
		public void onDisconnected () {

		}
	};


}
