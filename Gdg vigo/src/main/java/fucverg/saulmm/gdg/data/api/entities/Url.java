package fucverg.saulmm.gdg.data.api.entities;

import fucverg.saulmm.gdg.data.db.entities.DBEntity;

import static fucverg.saulmm.gdg.utils.DbUtils.COMMA;

public class Url extends DBEntity {
	// Database table fields
	public static final String TABLE_NAME = "url";
	public static final String COLUMN_NAME_ENTRY_ID = "id";
	public static final String COLUMN_NAME_LABEL = "label";
	public static final String COLUMN_NAME_NAME = "value";
	public static final String COLUMN_NAME_GROUP = "group_id";

	// Database table projection
	public static final String [] URL_PROJECTION = {
		COLUMN_NAME_ENTRY_ID,
		COLUMN_NAME_LABEL,
		COLUMN_NAME_NAME,
		COLUMN_NAME_GROUP
	};

	// Database create statement
	public static final String CREATE_TABLE_URL =
		"CREATE TABLE " + TABLE_NAME + "(" +
				COLUMN_NAME_ENTRY_ID + " TEXT PRIMARY KEY" + COMMA +
				COLUMN_NAME_LABEL+ " TEXT " + COMMA +
				COLUMN_NAME_NAME + " TEXT " + COMMA +
				COLUMN_NAME_GROUP + " TEXT " + ")";

	// Entity fields
	public String id;
	public String value;
	public String type;
	public String label;
	public String group_id;


	public String[] getFields () {
		return new String []  {
				id,
				label,
				value,
				group_id
		};
	}


	@Override
	public DBEntity createDBEntity (String[] fields) {
		Url url = new Url();
		url.setId(fields[0]);
		url.setValue(fields[1]);
		url.setType(fields[2]);
		url.setLabel(fields[3]);
		url.setGroup_id(fields[4]);
		return url;
	}


	@Override
	public String getTableName () {
		return TABLE_NAME;
	}


	@Override
	public String[] getProjection () {
		return URL_PROJECTION;
	}


	public void setId (String id) {
		this.id = id;
	}


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
}