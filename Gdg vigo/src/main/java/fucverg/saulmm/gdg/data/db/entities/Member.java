package fucverg.saulmm.gdg.data.db.entities;

import android.provider.BaseColumns;

import java.io.Serializable;

import static fucverg.saulmm.gdg.Utils.DB_UTILS.COMMA;
import static fucverg.saulmm.gdg.data.db.entities.Member.MemberEntry.*;

public class Member implements Serializable {
	public String image;
	public String occupation;
	public String name;
	public String displayName;
	public String id;

	public static final String CREATE_TABLE_MEMBERS =
			"CREATE TABLE " + TABLE_NAME + "(" +
					COLUMN_NAME_ENTRY_ID + " TEXT PRIMARY KEY" + COMMA +
					COLUMN_NAME_OCCUPATION+ " TEXT " + COMMA +
					COLUMN_NAME_NAME + " TEXT " + COMMA +
					COLUMN_NAME_IMAGE + " TEXT " + ")";

	public String getId () {
		return id;
	}


	public String getImage () {
		return image;
	}


	public String getName () {
		return name;
	}


	public String getOccupation () {
		return occupation;
	}


	// Inner class that defines the db table contents
	public static abstract class MemberEntry implements BaseColumns {
		public static final String TABLE_NAME = "member";
		public static final String COLUMN_NAME_ENTRY_ID = "id";
		public static final String COLUMN_NAME_OCCUPATION = "occupation";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_IMAGE = "image";
	}


}
