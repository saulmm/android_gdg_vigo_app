package fucverg.saulmm.gdg.data.db.entities;

import android.provider.BaseColumns;

public class Member {

	public abstract class FeedEntry implements BaseColumns {
		public static final String TABLE_NAME = "actors";
		public static final String COLUMN_NAME_ENTRY_ID = "id";
		public static final String COLUMN_NAME_TITLE = "displayName";
	}
}
