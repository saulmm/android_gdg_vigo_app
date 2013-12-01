package fucverg.saulmm.gdg.data.plus;

import com.google.android.gms.common.ConnectionResult;

public interface GPlusListener {
	void onConnected();
	void onnConnectionFailed (ConnectionResult cResult);
	void onTokenReceived(String token);
	void onDisconnected();
}
