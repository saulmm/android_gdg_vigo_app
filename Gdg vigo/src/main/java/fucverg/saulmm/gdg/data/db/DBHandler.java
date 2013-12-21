package fucverg.saulmm.gdg.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import fucverg.saulmm.gdg.Configuration;
import fucverg.saulmm.gdg.data.api.entities.Post;
import fucverg.saulmm.gdg.data.api.entities.Url;
import fucverg.saulmm.gdg.data.db.entities.DBEntity;
import fucverg.saulmm.gdg.data.db.entities.Event;
import fucverg.saulmm.gdg.data.db.entities.GroupInfo;
import fucverg.saulmm.gdg.data.db.entities.Member;
import fucverg.saulmm.gdg.utils.DbUtils;

import java.util.LinkedList;
import java.util.List;

import static android.util.Log.d;
import static fucverg.saulmm.gdg.data.api.entities.Post.DELETE_TABLE_ACTIVITIES;


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
		db.execSQL(Url.CREATE_TABLE_URL);

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


	@SuppressWarnings({"UnusedAssignment", "UnusedDeclaration"})
	public <G extends DBEntity> void insertElement(Class<G> type, String [] fields) {
		long rowID = 0;
		SQLiteDatabase db = getWritableDatabase();

		ContentValues insertValues = new ContentValues();
		DBTableInfo dbTableInfo = DbUtils.getDBTableInfo(type);
		String [] tableProjection = dbTableInfo.getProjection();

		for (int i = 0; i < tableProjection .length; i++)
			insertValues.put(tableProjection [i], fields[i]);

		try {
			rowID = db.insertOrThrow(dbTableInfo
					.getTableName(), null, insertValues);

		} catch (SQLException e) {
			Log.e("[ERROR] fucverg.saulmm.gdg.data.db.DBHandler.insertElement ",
					"SQL Exception inserting: "+ e.getCause());
		}
	}


	public Member getMemberById (String id) {
		final String selection = Member.COLUMN_NAME_ENTRY_ID + " LIKE ?";
		final String[] selectionArgs = {id};

		Member foundMember = null;

		try {
			foundMember = getAllElements(Member.class,
					selection, selectionArgs, false).get(0);

		} catch (IndexOutOfBoundsException ignored) {};

		return foundMember;
	}


	public Member getMemberByName (String name) {
		final String selection = Member.COLUMN_NAME_NAME+ " LIKE ?";
		final String[] selectionArgs = {name};

		Member foundMember = null;

		try {
			foundMember = getAllElements(Member.class,
					selection, selectionArgs, false).get(0);

		} catch (IndexOutOfBoundsException ignored) {};

		return foundMember;
	}


	@SuppressWarnings("unchecked")
	public <G extends DBEntity> List<G> getAllElements (Class<G> type, String selection, String[] args, boolean reverse) {

		LinkedList<G> elementList = new LinkedList<G>();
		SQLiteDatabase db = getReadableDatabase();

		DBTableInfo dbTableInfo = DbUtils.getDBTableInfo(type);
		String[] tableProjection = dbTableInfo.getProjection();
		String tableName = dbTableInfo.getTableName();

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

					G resulted = null;
					try {
						resulted = (G) type.newInstance().createDBEntity(fields);

					} catch (InstantiationException e) {
						e.printStackTrace();

					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}

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
