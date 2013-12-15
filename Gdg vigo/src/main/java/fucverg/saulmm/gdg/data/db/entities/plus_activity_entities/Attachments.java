package fucverg.saulmm.gdg.data.db.entities.plus_activity_entities;

import java.io.Serializable;

public class Attachments implements Serializable {
	public String objectType;
	public String displayName;
	public String content;
	public String url;
	public Thumbnail[] thumbnails;
	public Image image;
	public Image fullImage;



	public String getObjectType () {
		return objectType;
	}

	public String getDisplayName () {
		return displayName;
	}

	public String getContent () {
		return content;
	}

	public String getUrl () {
		return url;
	}
}
