package ameiga.saulmm.gdg.data.db.entities;

import ameiga.saulmm.gdg.data.api.entities.Image;
import ameiga.saulmm.gdg.data.api.entities.Url;

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
	public Url[] urls;


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


	public Url[] getUrls () {
		return urls;
	}
}

