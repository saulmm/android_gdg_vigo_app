package fucverg.saulmm.gdg.data.api.entities;

import android.provider.BaseColumns;

import static fucverg.saulmm.gdg.data.api.entities.Url.UrlEntry.*;
import static fucverg.saulmm.gdg.utils.DbUtils.COMMA;

public class Url {
	public String value;
	public String type;
	public String label;
	public String group_id;

	public static final String CREATE_TABLE_URL =
			"CREATE TABLE " + TABLE_NAME + "(" +
					COLUMN_NAME_ENTRY_ID + " TEXT PRIMARY KEY" + COMMA +
					COLUMN_NAME_LABEL+ " TEXT " + COMMA +
					COLUMN_NAME_NAME + " TEXT " + COMMA +
					COLUMN_NAME_GROUP + " TEXT " + ")";


	public void setValue (String value) {
		this.value = value;
	}


	public void setType (String type) {
		this.type = type;
	}


	public void setLabel (String label) {
		this.label = label;
	}


	public void setGroup_id (String group_id) {
		this.group_id = group_id;
	}


	public String getValue () {
		return value;
	}


	public String getType () {
		return type;
	}


	public String getLabel () {
		return label;
	}


	// Inner class that defines the db table contents
	public static abstract class UrlEntry implements BaseColumns {
		public static final String TABLE_NAME = "url";
		public static final String COLUMN_NAME_ENTRY_ID = "id";
		public static final String COLUMN_NAME_LABEL = "label";
		public static final String COLUMN_NAME_NAME = "value";
		public static final String COLUMN_NAME_GROUP = "group_id";
	}
}