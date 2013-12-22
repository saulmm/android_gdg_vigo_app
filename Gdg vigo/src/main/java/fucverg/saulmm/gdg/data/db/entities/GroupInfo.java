package fucverg.saulmm.gdg.data.db.entities;

import android.provider.BaseColumns;

import static fucverg.saulmm.gdg.utils.DbUtils.COMMA;

// TODO Merge with People entity
public class GroupInfo extends DBEntity implements BaseColumns {
	// Database table fields
	public static final String TABLE_NAME = "group_info";
	public static final String COLUMN_NAME_ENTRY_ID = "id";
	public static final String COLUMN_NAME_NAME = "name";
	public static final String COLUMN_NAME_SLOGAN = "tagLine";
	public static final String COLUMN_NAME_URL_ID = "urlId";
	public static final String COLUMN_NAME_URL_ABOUT = "about";

	// Database table projection
	public static final String[] GROUP_PROJECTION = {
			COLUMN_NAME_ENTRY_ID,
			COLUMN_NAME_NAME,
			COLUMN_NAME_SLOGAN,
			COLUMN_NAME_URL_ABOUT,
			COLUMN_NAME_URL_ID
	};

	// Entity attributes
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


	public String[] getFields() {
		return new String [] {
			id, name, tagLine, about, urlId
		};
	}


	@Override
	public String getTableName () {
		return TABLE_NAME;
	}


	@Override
	public String[] getProjection () {
		return GROUP_PROJECTION;
	}


	@Override
	public DBEntity createDBEntity (String[] fields) {
		GroupInfo groupInfo = new GroupInfo();
		groupInfo.setId(fields[0]);
		groupInfo.setName(fields[1]);
		groupInfo.setTagLine(fields[2]);
		groupInfo.setAbout(fields[3]);
		groupInfo.setUrlId(fields[4]);

		return groupInfo;
	}


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
}
