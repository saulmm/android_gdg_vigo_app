package fucverg.saulmm.gdg.data.db.entities;

import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.Image;
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.PlusLink;

public class PlusPerson {

	public String id;
	public String objectType;
	public String displayName;
	public String tagline;
	public String aboutMe;
	public Image image;
	public String isPlusUser;
	public String plusOneCount;
	public String verified;
	public PlusLink[] urls;


	public String getId () {
		return id;
	}


	public String getObjectType () {
		return objectType;
	}


	public String getDisplayName () {
		return displayName;
	}


	public String getTagline () {
		return tagline;
	}


	public String getAboutMe () {
		return aboutMe;
	}


	public Image getImage () {
		return image;
	}


	public String getIsPlusUser () {
		return isPlusUser;
	}


	public String getPlusOneCount () {
		return plusOneCount;
	}


	public String getVerified () {
		return verified;
	}


	public PlusLink[] getUrls () {
		return urls;
	}
}

