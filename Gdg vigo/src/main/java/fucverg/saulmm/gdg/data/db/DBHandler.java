package fucverg.saulmm.gdg.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import fucverg.saulmm.gdg.Configuration;
import fucverg.saulmm.gdg.data.db.entities.DBEntity;
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
	private static final String SEL_MEMBER_BY_NAME =
			MemberEntry.COLUMN_NAME_NAME + "LIKE ?";

	private static final String SEL_MEMBER_BY_ID =
			MemberEntry.COLUMN_NAME_ENTRY_ID + "LIKE ?";

	private final String[] memberProjection = new String[4];
	private final String[] activityProjection = new String[9];
	private final String[] eventProjection = new String[9];


	public DBHandler (Context context) {
		super(context, Configuration.DATABASE_NAME, null, Configuration.DATABASE_VERSION);

		memberProjection[0] = MemberEntry.COLUMN_NAME_ENTRY_ID;
		memberProjection[1] = MemberEntry.COLUMN_NAME_NAME;
		memberProjection[2] = MemberEntry.COLUMN_NAME_OCCUPATION;
		memberProjection[3] = MemberEntry.COLUMN_NAME_IMAGE;

		activityProjection[0] = ActivityEntry.COLUMN_NAME_ENTRY_ID;
		activityProjection[1] = ActivityEntry.COLUMN_NAME_TITLE;
		activityProjection[2] = ActivityEntry.COLUMN_NAME_URL;
		activityProjection[3] = ActivityEntry.COLUMN_NAME_ID_MEMBER;
		activityProjection[4] = ActivityEntry.COLUMN_NAME_CONTENT_URL;
		activityProjection[5] = ActivityEntry.COLUMN_NAME_CONTENT_TITLE;
		activityProjection[6] = ActivityEntry.COLUMN_NAME_CONTENT_TYPE;
		activityProjection[7] = ActivityEntry.COLUMN_NAME_CONTENT_DESCRIPTION;
		activityProjection[8] = ActivityEntry.COLUMN_NAME_DATE;

		eventProjection[0] = EventEntry.COLUMN_NAME_ENTRY_ID;
		eventProjection[1] = EventEntry.COLUMN_NAME_TITLE_END;
		eventProjection[2] = EventEntry.COLUMN_NAME_TITLE_DESCRIPTION;
		eventProjection[3] = EventEntry.COLUMN_NAME_TITLE_START;
		eventProjection[4] = EventEntry.COLUMN_NAME_TITLE_TEMPORAL_RELATION;
		eventProjection[5] = EventEntry.COLUMN_NAME_TITLE_GROUP_URL;
		eventProjection[6] = EventEntry.COLUMN_NAME_TITLE_PLUS_URL;
		eventProjection[7] = EventEntry.COLUMN_NAME_TITLE_LOCATION;
		eventProjection[8] = EventEntry.COLUMN_NAME_TITLE_TITLE;
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
					"Error: " + e.getMessage());
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
					"Inserted member: " + rowId);

		} catch (SQLException e) {
			Log.e("[ERROR] fucverg.saulmm.gdg.data.db.DBHandler.insertMember ",
					"Error member: " + e.getMessage());
		}
	}


	public Member getMemberbyId (String id) {
		final String selection = EventEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
		final SQLiteDatabase db = getReadableDatabase();
		final String[] selectionArgs = {id};
		final String projection[] = {
				MemberEntry.COLUMN_NAME_ENTRY_ID,
				MemberEntry.COLUMN_NAME_IMAGE,
				MemberEntry.COLUMN_NAME_NAME,
				MemberEntry.COLUMN_NAME_OCCUPATION,
		};

		Cursor c = db.query(MemberEntry.TABLE_NAME,
				projection, selection, selectionArgs,
				null, null, null);

		Member memberFromDB = null;

		if (c != null && c.getCount() > 0) {
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


	@SuppressWarnings("unchecked")
	public <G extends DBEntity> List<G> getAllElements (G element) {
		LinkedList<G> elementList = new LinkedList<G>();
		SQLiteDatabase db = getReadableDatabase();

		String[] tableProjection = new String[11];
		String tableName = null;

		if (element instanceof Member) {
			tableProjection = memberProjection;
			tableName = MemberEntry.TABLE_NAME;

		} else if (element instanceof Activity) {
			tableProjection = activityProjection;
			tableName = ActivityEntry.TABLE_NAME;

		} else if (element instanceof Event) {
			tableProjection = eventProjection;
			tableName = EventEntry.TABLE_NAME;
		}

		d("[DEBUG] fucverg.saulmm.gdg.data.db.DBHandler.getAllElements ",
				"The selected table is : " + tableName);

		Cursor cursor = db.query(tableName,
				tableProjection, null, null, null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();

			if (cursor.getCount() > 0) {
				String [] fields = new String[cursor.getColumnCount()];

				while (cursor.moveToNext()) {
					for (int i = 0; i < fields.length; i++)
						fields[i]  = cursor.getString(cursor.getColumnIndexOrThrow(
								tableProjection[i]));

					G resulted= (G) element.createDBEntity(fields);
					elementList.add(resulted);
				}
			}
		} else
			Log.e("[ERROR] fucverg.saulmm.gdg.data.db.DBHandler.getEvents ",
					"The cursor is null");

		return elementList;
	}


	public Member getMemberByName (String searchName) {
		d("[DEBUG] fucverg.saulmm.gdg.data.db.DBHandler.getMemberByName ", "Searchname : " + searchName);

		final String selection = MemberEntry.COLUMN_NAME_NAME + " LIKE ?";
		final SQLiteDatabase db = getReadableDatabase();
		final String[] selectionArgs = {searchName};
		final String projection[] = {
				MemberEntry.COLUMN_NAME_ENTRY_ID,
				MemberEntry.COLUMN_NAME_IMAGE,
				MemberEntry.COLUMN_NAME_NAME,
				MemberEntry.COLUMN_NAME_OCCUPATION,
		};

		Cursor c = db.query(MemberEntry.TABLE_NAME,
				projection, selection, selectionArgs,
				null, null, null);

		Member memberFromDB = null;

		if (c != null && c.getCount() > 0) {
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


}
