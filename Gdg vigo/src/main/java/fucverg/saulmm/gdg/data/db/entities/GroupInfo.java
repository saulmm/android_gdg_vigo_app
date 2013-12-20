package fucverg.saulmm.gdg.data.db.entities;

import android.provider.BaseColumns;

import static android.util.Log.d;
import static fucverg.saulmm.gdg.data.db.entities.GroupInfo.GroupEntry.*;
import static fucverg.saulmm.gdg.utils.DbUtils.COMMA;

// TODO Merge with People entity
public class GroupInfo extends DBEntity {
	public String id;
	public String name;
	public String urlId;
	public String about;
	public String tagLine;

	public static final String CREATE_GROUP_INFO =
			"CREATE TABLE " + TABLE_NAME + "(" +
					COLUMN_NAME_ENTRY_ID + " TEXT PRIMARY KEY" + COMMA +
					COLUMN_NAME_NAME + " TEXT " + COMMA +
					COLUMN_NAME_SLOGAN + " TEXT " + COMMA +
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


	public void setTagLine (String tagLine) {
		this.tagLine = tagLine;
	}


	public String getTagLine () {
		return tagLine;
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
		public static final String COLUMN_NAME_SLOGAN = "tagLine";
		public static final String COLUMN_NAME_URL_ID = "urlId";
		public static final String COLUMN_NAME_URL_ABOUT = "about";

		public static final String[] GROUP_PROJECTION = {
			GroupEntry.COLUMN_NAME_ENTRY_ID,
			GroupEntry.COLUMN_NAME_NAME,
			GroupEntry.COLUMN_NAME_SLOGAN,
			GroupEntry.COLUMN_NAME_URL_ABOUT,
			GroupEntry.COLUMN_NAME_URL_ID
		};
	}


	@Override
	public DBEntity createDBEntity (String[] fields) {
		d("[DEBUG] fucverg.saulmm.gdg.data.db.entities.GroupInfo.createDBEntity ",
				"The fields are: \n");

		for (int i = 0; i < fields.length; i++) {
			String field = fields[i];
			d("[DEBUG] fucverg.saulmm.gdg.data.db.entities.GroupInfo.createDBEntity ",
					"Field ["+i+"]:" + field);
		}

		GroupInfo groupInfo = new GroupInfo();
		groupInfo.setId(fields[0]);
		groupInfo.setName(fields[1]);
		groupInfo.setTagLine(fields[2]);
		groupInfo.setAbout(fields[3]);
		groupInfo.setUrlId(fields[4]);

		return groupInfo;
	}
}
