package fucverg.saulmm.gdg.data.db.entities;

import android.provider.BaseColumns;

import static fucverg.saulmm.gdg.data.db.entities.GroupInfo.GroupEntry.*;
import static fucverg.saulmm.gdg.utils.DbUtils.COMMA;

public class GroupInfo extends DBEntity {
	public String id;
	public String name;
	public String urlId;
	public String about;


	public static final String CREATE_GROUP_INFO =
			"CREATE TABLE " + TABLE_NAME + "(" +
					COLUMN_NAME_ENTRY_ID + " TEXT PRIMARY KEY" + COMMA +
					COLUMN_NAME_NAME + " TEXT " + COMMA +
					COLUMN_NAME_URL_ID + " TEXT " + COMMA +
					COLUMN_NAME_URL_ABOUT + " TEXT " + ")";


	public String getId () {
		return id;
	}


	public String getName () {
		return name;
	}


	public String getUrlId () {
		return urlId;
	}


	public String getAbout () {
		return about;
	}


	public void setId (String id) {
		this.id = id;
	}


	public void setName (String name) {
		this.name = name;
	}


	public void setUrlId (String urlId) {
		this.urlId = urlId;
	}


	public void setAbout (String about) {
		this.about = about;
	}


	// Inner class that defines the db table contents
	public static abstract class GroupEntry implements BaseColumns {
		public static final String TABLE_NAME = "group_info";
		public static final String COLUMN_NAME_ENTRY_ID = "id";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_URL_ID = "urlId";
		public static final String COLUMN_NAME_URL_ABOUT = "about";
	}


	@Override
	public DBEntity createDBEntity (String[] fields) {
		GroupInfo groupInfo = new GroupInfo();
		groupInfo.setId(fields[0]);
		groupInfo.setName(fields[1]);
		groupInfo.setUrlId(fields[2]);
		groupInfo.setAbout(fields[3]);

		return groupInfo;
	}
}
