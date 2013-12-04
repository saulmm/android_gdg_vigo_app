package fucverg.saulmm.gdg.data.db.entities;

import android.provider.BaseColumns;

import java.net.URL;
import java.util.Date;

public class PlusActivity {

	private String id;
	private String title;
	private String description;
	private String idMember;
	private String content_type;
	private String contentTitle;
	private String contentDescription;
	private URL url;
	private URL contentURL;
	private Date date;

	private static final String COMMA = ",";
	public static final String CREATE_TABLE_ACTIVITIES =
			"CREATE TABLE " + ActivityEntry.TABLE_NAME + "(" +
					ActivityEntry.COLUMN_NAME_ENTRY_ID + " TEXT PRIMARY KEY" + COMMA +
					ActivityEntry.COLUMN_NAME_TITLE+ " TEXT " + COMMA +
					ActivityEntry.COLUMN_NAME_DESCRIPTION + " TEXT " + COMMA +
					ActivityEntry.COLUMN_NAME_URL + " TEXT " + COMMA +
					ActivityEntry.COLUMN_NAME_ID_MEMBER + " TEXT " + COMMA +
					ActivityEntry.COLUMN_NAME_TYPE + " TEXT " + COMMA +
					ActivityEntry.COLUMN_NAME_CONTENT_URL + " TEXT " + COMMA +
					ActivityEntry.COLUMN_NAME_CONTENT_TITLE + " TEXT " + COMMA +
					ActivityEntry.COLUMN_NAME_DATE + " TEXT " + ")";

	public static final String DELETE_TABLE_ACTITIES =
			"DROP TABLE IF EXISTS "+ ActivityEntry.TABLE_NAME;


	// Inner class that defines the db table contents
	public static abstract class ActivityEntry implements BaseColumns {
		public static final String TABLE_NAME = "activities";
		public static final String COLUMN_NAME_ENTRY_ID = "id";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_DESCRIPTION = "description";
		public static final String COLUMN_NAME_URL = "url";
		public static final String COLUMN_NAME_ID_MEMBER = "idMember";
		public static final String COLUMN_NAME_TYPE = "content_type";
		public static final String COLUMN_NAME_CONTENT_URL = "content_url";
		public static final String COLUMN_NAME_CONTENT_TITLE = "content_title";
		public static final String COLUMN_NAME_DATE = "date";
	}
}
