package fucverg.saulmm.gdg.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import fucverg.saulmm.gdg.Configuration;
import fucverg.saulmm.gdg.data.api.entities.Attachments;
import fucverg.saulmm.gdg.data.api.entities.Post;
import fucverg.saulmm.gdg.data.db.entities.DBEntity;
import fucverg.saulmm.gdg.data.db.entities.Event;
import fucverg.saulmm.gdg.data.db.entities.GroupInfo;
import fucverg.saulmm.gdg.data.db.entities.Member;

import java.util.LinkedList;
import java.util.List;

import static android.util.Log.d;
import static android.util.Log.e;
import static fucverg.saulmm.gdg.data.api.entities.Post.DELETE_TABLE_ACTIVITIES;
import static fucverg.saulmm.gdg.data.api.entities.Post.PostEntry;
import static fucverg.saulmm.gdg.data.db.entities.Event.EventEntry;
import static fucverg.saulmm.gdg.data.db.entities.GroupInfo.GroupEntry;
import static fucverg.saulmm.gdg.data.db.entities.Member.MemberEntry;


public class DBHandler extends SQLiteOpenHelper {

	public DBHandler (Context context) {
		super(context, Configuration.DATABASE_NAME,
				null, Configuration.DATABASE_VERSION);
	}


	@Override
	public void onCreate (SQLiteDatabase db) {
		db.execSQL(Post.CREATE_TABLE_ACTIVITIES);
		db.execSQL(Event.CREATE_TABLE_EVENTS);
		db.execSQL(Member.CREATE_TABLE_MEMBERS);
		db.execSQL(GroupInfo.CREATE_GROUP_INFO);

		d("[DEBUG] fucverg.saulmm.gdg.data.db.DBHandler.onCreate ",
				"All tables have been created...");
	}


	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DELETE_TABLE_ACTIVITIES);
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


	public void insertActivity (Post post) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues insertValues = new ContentValues();
		insertValues.put(PostEntry.COLUMN_NAME_ENTRY_ID, post.getId());
		insertValues.put(PostEntry.COLUMN_NAME_TITLE, post.getTitle());
		insertValues.put(PostEntry.COLUMN_NAME_URL, post.getUrl());
		insertValues.put(PostEntry.COLUMN_NAME_ID_MEMBER, post.getActor().getId());
		insertValues.put(PostEntry.COLUMN_NAME_DATE, post.getDate());
		insertValues.put(PostEntry.COLUMN_NAME_PAGE_TOKEN, post.getPageToken());

		if (post.object.attachments != null) {
			Attachments attachment = post.object.attachments[0];
			insertValues.put(PostEntry.COLUMN_NAME_CONTENT_URL, post.getContent_url());
			insertValues.put(PostEntry.COLUMN_NAME_CONTENT_TITLE, attachment.getDisplayName());
			insertValues.put(PostEntry.COLUMN_NAME_CONTENT_TYPE, attachment.getObjectType());
			insertValues.put(PostEntry.COLUMN_NAME_CONTENT_DESCRIPTION, attachment.getContent());
		}

		long rowId = 0;

		try {
			rowId = db.insertOrThrow(PostEntry.TABLE_NAME,
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

		} catch (SQLException e) {
			Log.e("[ERROR] fucverg.saulmm.gdg.data.db.DBHandler.insertMember ",
					"Error member: " + e.getMessage());
		}
	}


	public void insertGroupInfo (GroupInfo info) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues insertValues = new ContentValues();
		insertValues.put(GroupEntry.COLUMN_NAME_ENTRY_ID, info.getId());
		insertValues.put(GroupEntry.COLUMN_NAME_NAME, info.getName());
		insertValues.put(GroupEntry.COLUMN_NAME_SLOGAN, info.getTagLine());
		insertValues.put(GroupEntry.COLUMN_NAME_URL_ABOUT, info.getAbout());
		insertValues.put(GroupEntry.COLUMN_NAME_URL_ID, info.getUrlId());

		long rowId = 0;

		try {
			rowId = db.insertOrThrow(GroupEntry.TABLE_NAME,
					null, insertValues);

			d("[DEBUG] fucverg.saulmm.gdg.data.db.DBHandler.insertGroupInfo ",
					"Insert result was: " + rowId);

		} catch (SQLException e) {
			Log.e("[ERROR] fucverg.saulmm.gdg.data.db.DBHandler.insertGroupInfo ",
					"Error inserting the group info: " + e.getMessage());
		}
	}



	public Member getMemberbyId (String id) {
		final String selection = MemberEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
		final String[] selectionArgs = {id};

		Member foundMember = null;

		try {
			foundMember = getAllElements(new Member(),
					selection, selectionArgs, false).get(0);

		} catch (IndexOutOfBoundsException ignored) {};

		return foundMember;
	}


	public Member getMemberbyName (String name) {
		final String selection = MemberEntry.COLUMN_NAME_NAME+ " LIKE ?";
		final String[] selectionArgs = {name};

		Member foundMember = null;

		try {
			foundMember = getAllElements(new Member(),
					selection, selectionArgs, false).get(0);

		} catch (IndexOutOfBoundsException ignored) {};

		return foundMember;
	}


	public List<Post> getMembersByToken (String token) {
		final String selection = PostEntry.COLUMN_NAME_PAGE_TOKEN+" LIKE ? ";
		final String[] selectionArgs = { token };

		return getAllElements(new Post(), selection, selectionArgs, false);
	}


	@SuppressWarnings("unchecked")
	public <G extends DBEntity> List<G> getAllElements (G element, String selection, String[] args, boolean reverse) {

		LinkedList<G> elementList = new LinkedList<G>();
		SQLiteDatabase db = getReadableDatabase();

		String[] tableProjection = new String[11];
		String tableName = null;

		if (element instanceof Member) {
			tableProjection = MemberEntry.MEMBER_PROJECTION;
			tableName = MemberEntry.TABLE_NAME;

		} else if (element instanceof Post) {
			tableProjection = PostEntry.POST_PROJECTION;
			tableName = PostEntry.TABLE_NAME;

		} else if (element instanceof Event) {
			tableProjection = EventEntry.EVENT_PROJECTION;
			tableName = EventEntry.TABLE_NAME;

		} else if (element instanceof GroupInfo) {
			tableProjection = GroupEntry.GROUP_PROJECTION;
			tableName = GroupEntry.TABLE_NAME;
		}

		Cursor cursor = db.query(tableName,
				tableProjection, selection, args,
				null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();

			if (cursor.getCount() > 0) {
				String[] fields = new String[cursor.getColumnCount()];

				do {
					for (int i = 0; i < fields.length; i++)
						fields[i] = cursor.getString(cursor.getColumnIndexOrThrow(
								tableProjection[i]));

					G resulted = (G) element.createDBEntity(fields);

					if (reverse)
						elementList.addFirst(resulted);
					else
						elementList.add(resulted);

				} while ((cursor.moveToNext()));
			}
		}
		return elementList;
	}
}
