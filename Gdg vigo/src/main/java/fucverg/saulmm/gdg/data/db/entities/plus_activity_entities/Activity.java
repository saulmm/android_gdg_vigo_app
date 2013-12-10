package fucverg.saulmm.gdg.data.db.entities.plus_activity_entities;

import android.provider.BaseColumns;

import static fucverg.saulmm.gdg.Utils.DB_UTILS.COMMA;
import static fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.Activity.ActivityEntry.*;

public class Activity {
	public Provider provider;
	public Object object;

	public Actor  actor;
	public String kind;
	public String etag;
	public String title;
	public String published;
	public String updated;
	public String id;
	public String url;
	public String verb;

	public String content_description;
	public String content_type;
	public String content_url;
	public String date;

//	public void setActorID (String id) {
//
//		if(actor == null)
//			actor = new Member();
//
//		this.actor.id = id;
//	}


	public Actor getActor () {
		return actor;
	}




	public void setDate (String date) {
		this.date = date;
	}


	public void setProvider (Provider provider) {
		this.provider = provider;
	}


	public void setObject (Object object) {
		this.object = object;
	}


//	public void setMember (Member member) {
//		this.actor = member;
//	}


	public void setKind (String kind) {
		this.kind = kind;
	}


	public void setEtag (String etag) {
		this.etag = etag;
	}


	public void setTitle (String title) {
		this.title = title;
	}


	public void setPublished (String published) {
		this.published = published;
	}


	public void setUpdated (String updated) {
		this.updated = updated;
	}


	public void setId (String id) {
		this.id = id;
	}


	public void setUrl (String url) {
		this.url = url;
	}


	public void setVerb (String verb) {
		this.verb = verb;
	}


	public void setContent_description (String content_description) {
		this.content_description = content_description;
	}


	public void setContent_type (String content_type) {
		this.content_type = content_type;
	}


	public void setContent_url (String content_url) {
		this.content_url = content_url;
	}


	public Provider getProvider () {
		return provider;
	}


	public Object getObject () {
		return object;
	}




	public String getKind () {
		return kind;
	}


	public String getEtag () {
		return etag;
	}


	public String getTitle () {
		return title;
	}


	public String getPublished () {
		return published;
	}


	public String getUpdated () {
		return updated;
	}


	public String getId () {
		return id;
	}


	public String getUrl () {
		return url;
	}


	public String getVerb () {
		return verb;
	}


	public String getContent_description () {
		return content_description;
	}


	public String getContent_type () {
		return content_type;
	}


	public String getContent_url () {
		return content_url;
	}


	public String getDate () {
		return date;
	}


	@Override
	public String toString () {
		return "Activity{" +
				"provider=" + provider +
				", object=" + object +
//				", member=" + actor +
				", kind='" + kind + '\'' +
				", etag='" + etag + '\'' +
				", title='" + title + '\'' +
				", published='" + published + '\'' +
				", updated='" + updated + '\'' +
				", id='" + id + '\'' +
				", url='" + url + '\'' +
				", verb='" + verb + '\'' +
				", content_description='" + content_description + '\'' +
				", content_type='" + content_type + '\'' +
				", content_url='" + content_url + '\'' +
				", date='" + date + '\'' +
				'}';
	}


	public static final String CREATE_TABLE_ACTIVITIES =
			"CREATE TABLE " + TABLE_NAME + "(" +
					COLUMN_NAME_ENTRY_ID + " TEXT PRIMARY KEY" + COMMA +
					COLUMN_NAME_TITLE+ " TEXT " + COMMA +
					COLUMN_NAME_CONTENT_DESCRIPTION + " TEXT " + COMMA +
					COLUMN_NAME_URL + " TEXT " + COMMA +
					COLUMN_NAME_ID_MEMBER + " TEXT " + COMMA +
					COLUMN_NAME_CONTENT_TYPE + " TEXT " + COMMA +
					COLUMN_NAME_CONTENT_URL + " TEXT " + COMMA +
					COLUMN_NAME_CONTENT_TITLE + " TEXT " + COMMA +
					COLUMN_NAME_DATE + " TEXT " + ")";

	public static final String DELETE_TABLE_ACTITIES =
			"DROP TABLE IF EXISTS "+ TABLE_NAME;




	// Inner class that defines the db table contents
	public static abstract class ActivityEntry implements BaseColumns {
		public static final String TABLE_NAME = "activities";
		public static final String COLUMN_NAME_ENTRY_ID = "id";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_CONTENT_DESCRIPTION = "description";
		public static final String COLUMN_NAME_URL = "url";
		public static final String COLUMN_NAME_ID_MEMBER = "idMember";
		public static final String COLUMN_NAME_CONTENT_TYPE = "content_type";
		public static final String COLUMN_NAME_CONTENT_URL = "content_url";
		public static final String COLUMN_NAME_CONTENT_TITLE = "content_title";
		public static final String COLUMN_NAME_DATE = "date";
	}


}
