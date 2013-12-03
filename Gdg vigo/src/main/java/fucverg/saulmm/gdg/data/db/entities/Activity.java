package fucverg.saulmm.gdg.data.db.entities;

import android.provider.BaseColumns;

import java.net.URL;
import java.util.Date;

public class Activity {

	private int id;
	private String title;
	private String description;
	private String type;
	private String idActivity;
	private String idMember;
	private String contentTitle;
	private String contentDescription;
	private URL url;
	private URL contentURL;
	private Date date;


	public int getId () {
		return id;
	}


	public void setId (int id) {
		this.id = id;
	}


	public String getTitle () {
		return title;
	}


	public void setTitle (String title) {
		this.title = title;
	}


	public String getDescription () {
		return description;
	}


	public void setDescription (String description) {
		this.description = description;
	}


	public String getType () {
		return type;
	}


	public void setType (String type) {
		this.type = type;
	}


	public String getIdActivity () {
		return idActivity;
	}


	public void setIdActivity (String idActivity) {
		this.idActivity = idActivity;
	}


	public String getIdMember () {
		return idMember;
	}


	public void setIdMember (String idMember) {
		this.idMember = idMember;
	}


	public String getContentTitle () {
		return contentTitle;
	}


	public void setContentTitle (String contentTitle) {
		this.contentTitle = contentTitle;
	}


	public String getContentDescription () {
		return contentDescription;
	}


	public void setContentDescription (String contentDescription) {
		this.contentDescription = contentDescription;
	}


	public URL getUrl () {
		return url;
	}


	public void setUrl (URL url) {
		this.url = url;
	}


	public URL getContentURL () {
		return contentURL;
	}


	public void setContentURL (URL contentURL) {
		this.contentURL = contentURL;
	}


	public Date getDate () {
		return date;
	}


	public void setDate (Date date) {
		this.date = date;
	}


	@Override
	public String toString () {
		return "Activity{" +
				"id=" + id +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", type='" + type + '\'' +
				", idActivity='" + idActivity + '\'' +
				", idMember='" + idMember + '\'' +
				", contentTitle='" + contentTitle + '\'' +
				", contentDescription='" + contentDescription + '\'' +
				", url=" + url +
				", contentURL=" + contentURL +
				", date=" + date +
				'}';
	}

	// Inner class that defines the db table contents
	public static abstract class FeedEntry implements BaseColumns {
		public static final String TABLE_NAME = "activities";
		public static final String COLUMN_NAME_ENTRY_ID = "id";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_DESCRIPTION = "description";
		public static final String COLUMN_NAME_URL = "url";
		public static final String COLUMN_NAME_ID_ACTIVITY = "idActivity";
		public static final String COLUMN_NAME_ID_MEMBER = "idMember";
		public static final String COLUMN_NAME_TYPE = "type";
		public static final String COLUMN_NAME_CONTENT_URL = "content_url";
		public static final String COLUMN_NAME_CONTENT_TITLE = "content_title";
		public static final String COLUMN_NAME_DATE = "date";
	}
}
