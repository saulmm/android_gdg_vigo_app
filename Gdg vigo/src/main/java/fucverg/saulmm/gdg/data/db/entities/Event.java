package fucverg.saulmm.gdg.data.db.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import static fucverg.saulmm.gdg.utils.DbUtils.COMMA;

public class Event extends DBEntity implements Serializable {
	// Database table fields
	public static final String TABLE_NAME = "events";
	public static final String COLUMN_NAME_ENTRY_ID = "id";
	public static final String COLUMN_NAME_TITLE_START = "start";
	public static final String COLUMN_NAME_TITLE_END = "end";
	public static final String COLUMN_NAME_TITLE_TITLE = "title";
	public static final String COLUMN_NAME_TITLE_DESCRIPTION = "description";
	public static final String COLUMN_NAME_TITLE_TEMPORAL_RELATION = "temporalRelation";
	public static final String COLUMN_NAME_TITLE_GROUP_URL = "group_url";
	public static final String COLUMN_NAME_TITLE_PLUS_URL = "plus_url";
	public static final String COLUMN_NAME_TITLE_LOCATION = "location";

	// Database table projection
	public static final String [] EVENT_PROJECTION = {
			COLUMN_NAME_ENTRY_ID,
			COLUMN_NAME_TITLE_END,
			COLUMN_NAME_TITLE_DESCRIPTION,
			COLUMN_NAME_TITLE_START,
			COLUMN_NAME_TITLE_TEMPORAL_RELATION,
			COLUMN_NAME_TITLE_GROUP_URL,
			COLUMN_NAME_TITLE_PLUS_URL,
			COLUMN_NAME_TITLE_LOCATION,
			COLUMN_NAME_TITLE_TITLE
	};

	// Entity fields
	@SerializedName("link")
	public String group_url;
	public String id;
	public String end;
	public String description;
	public String start;
	public String temporalRelation;
	public String title;
	public String gPlusEventLink;
	public String location;


	// Database create statement
	public static final String CREATE_TABLE_EVENTS =
		"CREATE TABLE " + TABLE_NAME + "(" +
				COLUMN_NAME_ENTRY_ID + " TEXT PRIMARY KEY " + COMMA +
				COLUMN_NAME_TITLE_TITLE + " TEXT " + COMMA +
				COLUMN_NAME_TITLE_START + " TEXT " + COMMA +
				COLUMN_NAME_TITLE_END + " TEXT " + COMMA +
				COLUMN_NAME_TITLE_DESCRIPTION + " TEXT " + COMMA +
				COLUMN_NAME_TITLE_TEMPORAL_RELATION + " TEXT" + COMMA +
				COLUMN_NAME_TITLE_GROUP_URL + " TEXT" + COMMA +
				COLUMN_NAME_TITLE_PLUS_URL + " TEXT" + COMMA +
				COLUMN_NAME_TITLE_LOCATION + " TEXT" + ");";

	// Database delete statement
	public static final String DELETE_TABLE_EVENTS =
			"DROP TABLE IF EXISTS " + TABLE_NAME;


	@Override
	public DBEntity createDBEntity (String[] fields) {
		Event event = new Event();
		event.setId(fields[0]);
		event.setEnd(fields[1]);
		event.setDescription(fields[2]);
		event.setStart(fields[3]);
		event.setTemporalRelation(fields[4]);
		event.setGroup_url(fields[5]);
		event.setgPlusEventLink(fields[6]);
		event.setLocation(fields[7]);
		event.setTitle(fields[8]);

		return event;
	}


	@Override
	public String getTableName () {
		return TABLE_NAME;
	}


	@Override
	public String[] getProjection () {
		return EVENT_PROJECTION;
	}


	public String getId () {
		return id;
	}


	public String getEnd () {
		return end;
	}


	public String getDescription () {
		return description;
	}


	public String getStart () {
		return start;
	}


	public String getTemporalRelation () {
		return temporalRelation;
	}


	public String getTitle () {
		return title;
	}


	public String getGroup_url () {
		return group_url;
	}


	public String getgPlusEventLink () {
		return gPlusEventLink;
	}


	public String getLocation () {
		return location;
	}


	public void setId (String id) {
		this.id = id;
	}


	public void setEnd (String end) {
		this.end = end;
	}


	public void setDescription (String description) {
		this.description = description;
	}


	public void setStart (String start) {
		this.start = start;
	}


	public void setTemporalRelation (String temporalRelation) {
		this.temporalRelation = temporalRelation;
	}


	public void setTitle (String title) {
		this.title = title;
	}


	public void setGroup_url (String group_url) {
		this.group_url = group_url;
	}


	public void setgPlusEventLink (String gPlusEventLink) {
		this.gPlusEventLink = gPlusEventLink;
	}


	public void setLocation (String location) {
		this.location = location;
	}


	@Override
	public String toString () {
		return "Event{" +
			"end='" + end + '\'' +
			", description='" + description + '\'' +
			", start='" + start + '\'' +
			", temporalRelation='" + temporalRelation + '\'' +
			", title='" + title + '\'' +
			", group_url='" + group_url + '\'' +
			", gPlusEventLink='" + gPlusEventLink + '\'' +
			", location='" + location + '\'' +
			'}';
	}







		
}
