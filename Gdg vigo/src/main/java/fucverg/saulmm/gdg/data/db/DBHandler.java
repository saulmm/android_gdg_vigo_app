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
import fucverg.saulmm.gdg.data.db.entities.PlusActivity;

import java.util.LinkedList;
import java.util.List;

import static android.util.Log.d;
import static android.util.Log.e;
import static fucverg.saulmm.gdg.data.db.entities.Event.EventEntry;
import static fucverg.saulmm.gdg.data.db.entities.PlusActivity.ActivityEntry.*;

public class DBHandler extends SQLiteOpenHelper {

	public DBHandler (Context context) {
		super(context, Configuration.DATABASE_NAME, null, Configuration.DATABASE_VERSION);
	}


	@Override
	public void onCreate (SQLiteDatabase db) {
		db.execSQL(PlusActivity.CREATE_TABLE_ACTIVITIES);
		db.execSQL(Event.CREATE_TABLE_EVENTS);
	}


	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(PlusActivity.DELETE_TABLE_ACTITIES);
		db.execSQL(Event.DELETE_TABLE_EVENTS);
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
		insertValues.put(EventEntry.COLUMN_NAME_TITLE_DESCRIPTION, description );
		insertValues.put(EventEntry.COLUMN_NAME_TITLE_GROUP_URL, groupUrl);
		insertValues.put(EventEntry.COLUMN_NAME_TITLE_PLUS_URL, plusUrl);
		insertValues.put(EventEntry.COLUMN_NAME_TITLE_TITLE, title);
		insertValues.put(EventEntry.COLUMN_NAME_TITLE_LOCATION, location);

		try {
			long rowID = db.insertOrThrow(EventEntry.TABLE_NAME,
					null, insertValues);

			d("[DEBUG] fucverg.saulmm.gdg.data.db.DBHandler.insertEvent ", "Event inserted with id : "+rowID);

		} catch (SQLException e) {
			Log.e("[ERROR] fucverg.saulmm.gdg.data.db.DBHandler.insertEvent ",
					"Something went wrogin with the insert ERROR: "+e.getMessage());
		}
	}


	public void insertActivity (
			String id,
			String title,
			String url,
			String idMember,
			String content_description,
			String content_type,
			String content_title,
			String content_url,
			String date) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues insertValues = new ContentValues();
		insertValues.put(COLUMN_NAME_ENTRY_ID, id);
		insertValues.put(COLUMN_NAME_TITLE, title);
		insertValues.put(COLUMN_NAME_URL, url);
		insertValues.put(COLUMN_NAME_ID_MEMBER, idMember);
		insertValues.put(COLUMN_NAME_CONTENT_URL, content_url);
		insertValues.put(COLUMN_NAME_CONTENT_TITLE, content_title);
		insertValues.put(COLUMN_NAME_CONTENT_TYPE, content_type);
		insertValues.put(COLUMN_NAME_CONTENT_DESCRIPTION, content_description);
		insertValues.put(COLUMN_NAME_DATE, date);


		long rowId = 0;

		try {
			rowId = db.insertOrThrow(PlusActivity.ActivityEntry.TABLE_NAME,
					null, insertValues);

			d("[DEBUG] fucverg.saulmm.gdg.data.db.DBHandler.insertActivity ", "Insert successfully with the id: "+rowId);

		} catch (SQLException e) {
			e("[ERROR][DB] fucverg.saulmm.gdg.data.db.DBHandler.insertActivity ",
					"Something went wrong inserting an activity ERROR: "+e.getMessage());
		}
	}


	public List<Event> getEvents() {
		LinkedList<Event> evenLists = new LinkedList<Event>();

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

		Cursor c = db.query(
				EventEntry.TABLE_NAME, projection, null, null, null, null, null);
				
		if(c != null) {
			c.moveToFirst();
			Event eventFromDB = new Event();

			while (c.moveToNext()) {

				String id = c.getString(c.getColumnIndexOrThrow(
						EventEntry.COLUMN_NAME_ENTRY_ID));

				String start = c.getString(c.getColumnIndexOrThrow(
						EventEntry.COLUMN_NAME_TITLE_START));

				String end = c.getString(c.getColumnIndexOrThrow(
						EventEntry.COLUMN_NAME_TITLE_END));

				String title = c.getString(c.getColumnIndexOrThrow(
						EventEntry.COLUMN_NAME_TITLE_DESCRIPTION));

				String description = c.getString(c.getColumnIndexOrThrow(
						EventEntry.COLUMN_NAME_TITLE_TEMPORAL_RELATION));

				String temporal_relation = c.getString(c.getColumnIndexOrThrow(
						EventEntry.COLUMN_NAME_TITLE_GROUP_URL));

				String group_url = c.getString(c.getColumnIndexOrThrow(
						EventEntry.COLUMN_NAME_TITLE_PLUS_URL));

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
			}

			evenLists.addLast(eventFromDB);


			
		} else {
			Log.e("[ERROR] fucverg.saulmm.gdg.data.db.DBHandler.getEvents ", "The cursor is null");
		}

		return evenLists;
		
	}
}
