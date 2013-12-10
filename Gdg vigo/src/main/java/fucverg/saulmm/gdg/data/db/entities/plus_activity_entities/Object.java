package fucverg.saulmm.gdg.data.db.entities.plus_activity_entities;

public class Object {
	public String objectType;
	public String content;
	public String url;
	public Repliers replies;
	public Plusoners plusoners;
	public Reshares resharers;
	public Attachments[] attachments;


	public String getObjectType () {
		return objectType;
	}


	public String getContent () {
		return content;
	}


	public String getUrl () {
		return url;
	}


	public Repliers getReplies () {
		return replies;
	}


	public Plusoners getPlusoners () {
		return plusoners;
	}


	public Reshares getResharers () {
		return resharers;
	}


	public Attachments[] getAttachments () {
		return attachments;
	}
}
