package fucverg.saulmm.gdg.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import fucverg.saulmm.gdg.Configuration;
import fucverg.saulmm.gdg.data.db.entities.PlusActivity;

public class DBHandler extends SQLiteOpenHelper {

	public DBHandler (Context context) {
		super(context, Configuration.DATABASE_NAME, null, Configuration.DATABASE_VERSION);
	}


	@Override
	public void onCreate (SQLiteDatabase db) {
		db.execSQL(PlusActivity.CREATE_TABLE_ACTIVITIES);
	}


	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(PlusActivity.DELETE_TABLE_ACTITIES);
	}


	@Override
	public void onDowngrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}


	public void insertActivity(
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
		insertValues.put(PlusActivity.ActivityEntry.COLUMN_NAME_ENTRY_ID, id);
		insertValues.put(PlusActivity.ActivityEntry.COLUMN_NAME_TITLE, title);
		insertValues.put(PlusActivity.ActivityEntry.COLUMN_NAME_URL, url);
		insertValues.put(PlusActivity.ActivityEntry.COLUMN_NAME_ID_MEMBER, idMember);
		insertValues.put(PlusActivity.ActivityEntry.COLUMN_NAME_CONTENT_URL, content_url);
		insertValues.put(PlusActivity.ActivityEntry.COLUMN_NAME_CONTENT_TITLE, content_title);
		insertValues.put(PlusActivity.ActivityEntry.COLUMN_NAME_CONTENT_TYPE, content_type);
		insertValues.put(PlusActivity.ActivityEntry.COLUMN_NAME_CONTENT_DESCRIPTION, content_description);
		insertValues.put(PlusActivity.ActivityEntry.COLUMN_NAME_DATE, date);

		long rowId = db.insert(PlusActivity.ActivityEntry.TABLE_NAME,
				null, insertValues);

		if(rowId > 0)
			Log.d("[DEBUG][DB] fucverg.saulmm.gdg.data.db.DBHandler.insertActivity ",
					"Insert was successfully, id: " + rowId);

		else
			Log.e("[ERROR][DB] fucverg.saulmm.gdg.data.db.DBHandler.insertActivity ",
					"Something went wrong inserting an activity");


	}
}
