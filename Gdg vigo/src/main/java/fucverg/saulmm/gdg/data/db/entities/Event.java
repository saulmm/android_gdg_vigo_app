package fucverg.saulmm.gdg.data.db.entities;

import android.provider.BaseColumns;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import static fucverg.saulmm.gdg.data.db.entities.Event.EventEntry.*;
import static fucverg.saulmm.gdg.utils.DbUtils.COMMA;

public class Event extends DBEntity implements Serializable {
	String id;
	String end;
	String description;
	String start;
	String temporalRelation;
	String title;

	@SerializedName("link")
	String group_url;

	String gPlusEventLink;
	String location;


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


	public static final String CREATE_TABLE_EVENTS =
			"CREATE TABLE " + EventEntry.TABLE_NAME + "(" +
				COLUMN_NAME_ENTRY_ID + " TEXT PRIMARY KEY " + COMMA +
				COLUMN_NAME_TITLE_TITLE + " TEXT " + COMMA +
				COLUMN_NAME_TITLE_START + " TEXT " + COMMA +
				COLUMN_NAME_TITLE_END + " TEXT " + COMMA +
				COLUMN_NAME_TITLE_DESCRIPTION + " TEXT " + COMMA +
				COLUMN_NAME_TITLE_TEMPORAL_RELATION + " TEXT" + COMMA +
				COLUMN_NAME_TITLE_GROUP_URL + " TEXT" + COMMA +
				COLUMN_NAME_TITLE_PLUS_URL + " TEXT" + COMMA +
				COLUMN_NAME_TITLE_LOCATION + " TEXT" + ");";


	public static final String DELETE_TABLE_EVENTS =
			"DROP TABLE IF EXISTS " + TABLE_NAME;


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


	public static abstract class EventEntry implements BaseColumns {
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


	}
}
