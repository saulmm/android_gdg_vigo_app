package ameiga.saulmm.gdg.plus;


public interface GPlusListener {
	void onConnected ();
	void onTokenReceived (String token);
	void onDisconnected ();
}
