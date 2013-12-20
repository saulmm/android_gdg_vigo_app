package fucverg.saulmm.gdg.data.db.entities;

import android.provider.BaseColumns;

import static fucverg.saulmm.gdg.data.db.entities.Member.MemberEntry.*;
import static fucverg.saulmm.gdg.utils.DbUtils.COMMA;

public class Member extends DBEntity {


	public String image;
	public String occupation;
	public String name;
	public String displayName;
	public String id;


	public static final String CREATE_TABLE_MEMBERS =
			"CREATE TABLE " + TABLE_NAME + "(" +
					COLUMN_NAME_ENTRY_ID + " TEXT PRIMARY KEY" + COMMA +
					COLUMN_NAME_OCCUPATION+ " TEXT " + COMMA +
					COLUMN_NAME_NAME + " TEXT " + COMMA +
					COLUMN_NAME_IMAGE + " TEXT " + ")";


	public void setImage (String image) {
		this.image = image;
	}


	public void setOccupation (String occupation) {
		this.occupation = occupation;
	}


	public void setName (String name) {
		this.name = name;
	}


	public void setDisplayName (String displayName) {
		this.displayName = displayName;
	}


	public void setId (String id) {
		this.id = id;
	}


	public String getId () {
		return id;
	}


	public String getImage () {
		return image;
	}


	public String getName () {
		return name;
	}


	public String getOccupation () {
		return occupation;
	}


	@Override
	public DBEntity createDBEntity (String[] fields) {
		Member member = new Member();
		member.setId(fields[0]);
		member.setName(fields[1]);
		member.setOccupation(fields[2]);
		member.setImage(fields[3]);

		return member;
	}


	// Inner class that defines the db table contents
	public static abstract class MemberEntry implements BaseColumns {
		public static final String TABLE_NAME = "member";
		public static final String COLUMN_NAME_ENTRY_ID = "id";
		public static final String COLUMN_NAME_OCCUPATION = "occupation";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_IMAGE = "image";


		public final static String[] MEMBER_PROJECTION = {
			MemberEntry.COLUMN_NAME_ENTRY_ID,
			MemberEntry.COLUMN_NAME_NAME,
			MemberEntry.COLUMN_NAME_OCCUPATION,
			MemberEntry.COLUMN_NAME_IMAGE
		};
	}


}
