package fucverg.saulmm.gdg.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import fucverg.saulmm.gdg.Configuration;
import fucverg.saulmm.gdg.data.db.entities.Event;
import fucverg.saulmm.gdg.data.db.entities.Member;
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.Activity;
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.Attachments;

import java.util.LinkedList;
import java.util.List;

import static android.util.Log.d;
import static android.util.Log.e;
import static fucverg.saulmm.gdg.data.db.entities.Event.EventEntry;
import static fucverg.saulmm.gdg.data.db.entities.Member.MemberEntry;
import static fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.Activity.ActivityEntry;

public class DBHandler extends SQLiteOpenHelper {

	public DBHandler (Context context) {
		super(context, Configuration.DATABASE_NAME, null, Configuration.DATABASE_VERSION);
	}


	@Override
	public void onCreate (SQLiteDatabase db) {
		db.execSQL(Activity.CREATE_TABLE_ACTIVITIES);
		db.execSQL(Event.CREATE_TABLE_EVENTS);
		db.execSQL(Member.CREATE_TABLE_MEMBERS);
	}


	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(Activity.DELETE_TABLE_ACTITIES);
		db.execSQL(Event.DELETE_TABLE_EVENTS);
//		db.execSQL(Member.DELETE_TABLE_EVENTS);
	}


	@Override
	public void onDowngrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}


	public void insertEvent (
			String id,
			String end,
			String description,
			String start,
			String temporalRelation,
			String title,
			String groupUrl,
			String plusUrl,
			String location) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues insertValues = new ContentValues();
		insertValues.put(EventEntry.COLUMN_NAME_ENTRY_ID, id);
		insertValues.put(EventEntry.COLUMN_NAME_TITLE_START, start);
		insertValues.put(EventEntry.COLUMN_NAME_TITLE_END, end);
		insertValues.put(EventEntry.COLUMN_NAME_TITLE_TEMPORAL_RELATION, temporalRelation);
		insertValues.put(EventEntry.COLUMN_NAME_TITLE_DESCRIPTION, description);
		insertValues.put(EventEntry.COLUMN_NAME_TITLE_GROUP_URL, groupUrl);
		insertValues.put(EventEntry.COLUMN_NAME_TITLE_PLUS_URL, plusUrl);
		insertValues.put(EventEntry.COLUMN_NAME_TITLE_TITLE, title);
		insertValues.put(EventEntry.COLUMN_NAME_TITLE_LOCATION, location);

		try {
			long rowID = db.insertOrThrow(EventEntry.TABLE_NAME,
					null, insertValues);

			d("[DEBUG] fucverg.saulmm.gdg.data.db.DBHandler.insertEvent ",
					"Event inserted with id : " + rowID);

		} catch (SQLException e) {
			Log.e("[ERROR] fucverg.saulmm.gdg.data.db.DBHandler.insertEvent ",
					"Error: "+e.getMessage());
		}
	}


	public void insertActivity (Activity activity) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues insertValues = new ContentValues();
		insertValues.put(ActivityEntry.COLUMN_NAME_ENTRY_ID, activity.getId());
		insertValues.put(ActivityEntry.COLUMN_NAME_TITLE, activity.getTitle());
		insertValues.put(ActivityEntry.COLUMN_NAME_URL, activity.getUrl());
		insertValues.put(ActivityEntry.COLUMN_NAME_ID_MEMBER, activity.getActor().getId());
		insertValues.put(ActivityEntry.COLUMN_NAME_DATE, activity.getDate());

		if (activity.object.attachments != null) {
			Attachments attachment = activity.object.attachments[0];
			insertValues.put(ActivityEntry.COLUMN_NAME_CONTENT_URL, activity.getContent_url());
			insertValues.put(ActivityEntry.COLUMN_NAME_CONTENT_TITLE, attachment.getDisplayName());
			insertValues.put(ActivityEntry.COLUMN_NAME_CONTENT_TYPE, attachment.getObjectType());
			insertValues.put(ActivityEntry.COLUMN_NAME_CONTENT_DESCRIPTION, attachment.getContent());
		}

		long rowId = 0;

		try {
			rowId = db.insertOrThrow(ActivityEntry.TABLE_NAME,
					null, insertValues);

			d("[DEBUG] fucverg.saulmm.gdg.data.db.DBHandler.insertActivity ",
					"Insert successfully with the id: " + rowId);

		} catch (SQLException e) {
			e("[ERROR][DB] fucverg.saulmm.gdg.data.db.DBHandler.insertActivity ",
					"Something went wrong inserting an activity ERROR: " + e.getMessage());
		}
	}


	public List<Activity> getActivities () {
		LinkedList<Activity> activitiesList = new LinkedList<Activity>();

		SQLiteDatabase db = getReadableDatabase();

		String[] projection = {
				ActivityEntry.COLUMN_NAME_ENTRY_ID,
				ActivityEntry.COLUMN_NAME_TITLE,
				ActivityEntry.COLUMN_NAME_URL,
				ActivityEntry.COLUMN_NAME_ID_MEMBER,
				ActivityEntry.COLUMN_NAME_CONTENT_URL,
				ActivityEntry.COLUMN_NAME_CONTENT_TITLE,
				ActivityEntry.COLUMN_NAME_CONTENT_TYPE,
				ActivityEntry.COLUMN_NAME_CONTENT_DESCRIPTION,
				ActivityEntry.COLUMN_NAME_DATE
		};

		Cursor c = db.query(ActivityEntry.TABLE_NAME,
				projection, null, null, null, null, null);

		if (c != null) {
			c.moveToFirst();

			while (c.moveToNext()) {
				Activity activityFromDB = new Activity();

				String id = c.getString(c.getColumnIndexOrThrow(
						ActivityEntry.COLUMN_NAME_ENTRY_ID));

				String title = c.getString(c.getColumnIndexOrThrow(
						ActivityEntry.COLUMN_NAME_TITLE));

				String content_description = c.getString(c.getColumnIndexOrThrow(
						ActivityEntry.COLUMN_NAME_CONTENT_DESCRIPTION));

				String url = c.getString(c.getColumnIndexOrThrow(
						ActivityEntry.COLUMN_NAME_URL));

				String id_member = c.getString(c.getColumnIndexOrThrow(
						ActivityEntry.COLUMN_NAME_ID_MEMBER));

				String content_type = c.getString(c.getColumnIndexOrThrow(
						ActivityEntry.COLUMN_NAME_CONTENT_TYPE));

				String content_url = c.getString(c.getColumnIndexOrThrow(
						ActivityEntry.COLUMN_NAME_CONTENT_URL));

				String date = c.getString(c.getColumnIndexOrThrow(
						ActivityEntry.COLUMN_NAME_DATE));

				activityFromDB.setId(id);
				activityFromDB.setTitle(title);
				activityFromDB.setUrl(url);
				activityFromDB.setActorID(id_member);
				activityFromDB.setContent_description(content_description);
				activityFromDB.setContent_type(content_type);
				activityFromDB.setContent_url(content_url);
				activityFromDB.setDate(date);

				activitiesList.add(activityFromDB);
			}
		} else {
			Log.e("[ERROR] fucverg.saulmm.gdg.data.db.DBHandler.getActivities ", "The cursor is null: ");
		}

		return activitiesList;
	}


	public void insertMember (Member member) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues insertValues = new ContentValues();
		insertValues.put(MemberEntry.COLUMN_NAME_ENTRY_ID, member.getId());
		insertValues.put(MemberEntry.COLUMN_NAME_OCCUPATION, member.getOccupation());
		insertValues.put(MemberEntry.COLUMN_NAME_NAME, member.getName());
		insertValues.put(MemberEntry.COLUMN_NAME_IMAGE, member.getImage());
		long rowId = 0;

		try {
			rowId = db.insertOrThrow(MemberEntry.TABLE_NAME,
					null, insertValues);

			d("[DEBUG] fucverg.saulmm.gdg.data.db.DBHandler.insertMember ",
					"Inserted member: "+rowId);

		} catch (SQLException e) {
			Log.e("[ERROR] fucverg.saulmm.gdg.data.db.DBHandler.insertMember ",
					"Error member: "+e.getMessage());
		}
	}


	public Member getMemberbyId (String id) {
		final String selection = EventEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
		final SQLiteDatabase db = getReadableDatabase();
		final String[] selectionArgs = { id };
		final String projection [] = {
			MemberEntry.COLUMN_NAME_ENTRY_ID,
			MemberEntry.COLUMN_NAME_IMAGE,
			MemberEntry.COLUMN_NAME_NAME,
			MemberEntry.COLUMN_NAME_OCCUPATION,
		};

		Cursor c = db.query(MemberEntry.TABLE_NAME,
				projection, selection, selectionArgs,
				null, null, null);

		Member memberFromDB = null;

		if( c != null && c.getCount() > 0 ) {
			c.moveToFirst();

			String memberID = c.getString(c.getColumnIndexOrThrow(
					MemberEntry.COLUMN_NAME_ENTRY_ID));

			String occupation = c.getString(c.getColumnIndexOrThrow(
					MemberEntry.COLUMN_NAME_OCCUPATION));

			String imgURL = c.getString(c.getColumnIndexOrThrow(
					MemberEntry.COLUMN_NAME_IMAGE));

			String name = c.getString(c.getColumnIndexOrThrow(
					MemberEntry.COLUMN_NAME_NAME));

			memberFromDB = new Member();
			memberFromDB.setName(name);
			memberFromDB.setId(memberID);
			memberFromDB.setImage(imgURL);
			memberFromDB.setOccupation(occupation);

		} else {
			Log.e("[ERROR] fucverg.saulmm.gdg.data.db.DBHandler.getEvents ",
					"The cursor is null");
		}

		return memberFromDB;
	}


	public List<Event> getEvents () {
		LinkedList<Event> eventList = new LinkedList<Event>();
		SQLiteDatabase db = getReadableDatabase();

		String[] projection = {
				EventEntry.COLUMN_NAME_ENTRY_ID,
				EventEntry.COLUMN_NAME_TITLE_START,
				EventEntry.COLUMN_NAME_TITLE_END,
				EventEntry.COLUMN_NAME_TITLE_TITLE,
				EventEntry.COLUMN_NAME_TITLE_DESCRIPTION,
				EventEntry.COLUMN_NAME_TITLE_TEMPORAL_RELATION,
				EventEntry.COLUMN_NAME_TITLE_GROUP_URL,
				EventEntry.COLUMN_NAME_TITLE_PLUS_URL,
				EventEntry.COLUMN_NAME_TITLE_LOCATION
		};

		Cursor c = db.query(EventEntry.TABLE_NAME,
				projection, null, null, null, null, null);

		if (c != null) {
			c.moveToFirst();

			while (c.moveToNext()) {
				Event eventFromDB = new Event();

				String id = c.getString(c.getColumnIndexOrThrow(
						EventEntry.COLUMN_NAME_ENTRY_ID));

				String start = c.getString(c.getColumnIndexOrThrow(
						EventEntry.COLUMN_NAME_TITLE_START));

				String end = c.getString(c.getColumnIndexOrThrow(
						EventEntry.COLUMN_NAME_TITLE_END));

				String title = c.getString(c.getColumnIndexOrThrow(
						EventEntry.COLUMN_NAME_TITLE_TITLE));

				String description = c.getString(c.getColumnIndexOrThrow(
						EventEntry.COLUMN_NAME_TITLE_DESCRIPTION));

				String temporal_relation = c.getString(c.getColumnIndexOrThrow(
						EventEntry.COLUMN_NAME_TITLE_TEMPORAL_RELATION));

				String group_url = c.getString(c.getColumnIndexOrThrow(
						EventEntry.COLUMN_NAME_TITLE_GROUP_URL));

				String location = c.getString(c.getColumnIndexOrThrow(
						EventEntry.COLUMN_NAME_TITLE_LOCATION));

				eventFromDB.setId(id);
				eventFromDB.setStart(start);
				eventFromDB.setEnd(end);
				eventFromDB.setTitle(title);
				eventFromDB.setDescription(description);
				eventFromDB.setTemporalRelation(temporal_relation);
				eventFromDB.setGroup_url(group_url);
				eventFromDB.setLocation(location);
				eventList.addFirst(eventFromDB);
			}
		} else {
			Log.e("[ERROR] fucverg.saulmm.gdg.data.db.DBHandler.getEvents ", "The cursor is null");
		}

		return eventList;
	}
}
