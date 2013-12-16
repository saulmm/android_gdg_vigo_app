package fucverg.saulmm.gdg.utils;

import com.google.android.gms.common.Scopes;

public class PlusUtils {
	public static final String PLUS_SCOPE = String.format
			("oauth2:%s https://www.googleapis.com/auth/plus.me",
			Scopes.PLUS_PROFILE);

	public static final String PLUS_MEMBER_PHOTO = "https://plus.google.com/s2/photos/profile/";
}
