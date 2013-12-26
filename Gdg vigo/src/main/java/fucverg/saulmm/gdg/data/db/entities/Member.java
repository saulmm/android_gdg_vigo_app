package fucverg.saulmm.gdg.data.db.entities;

import static fucverg.saulmm.gdg.utils.DbUtils.COMMA;

public class Member extends DBEntity {
	// Database fields
	public static final String TABLE_NAME = "member";
	public static final String COLUMN_NAME_ENTRY_ID = "id";
	public static final String COLUMN_NAME_OCCUPATION = "occupation";
	public static final String COLUMN_NAME_NAME = "name";
	public static final String COLUMN_NAME_IMAGE = "image";

	// Database Projection
	public final static String[] MEMBER_PROJECTION = {
			COLUMN_NAME_ENTRY_ID,
			COLUMN_NAME_NAME,
			COLUMN_NAME_OCCUPATION,
			COLUMN_NAME_IMAGE
	};

	// Entity fields
	public String image;
	public String occupation;
	public String name;
	public String displayName;
	public String id;


	// Database create statement
	public static final String CREATE_TABLE_MEMBERS =
			"CREATE TABLE " + TABLE_NAME + "(" +
					COLUMN_NAME_ENTRY_ID + " TEXT PRIMARY KEY" + COMMA +
					COLUMN_NAME_OCCUPATION+ " TEXT " + COMMA +
					COLUMN_NAME_NAME + " TEXT " + COMMA +
					COLUMN_NAME_IMAGE + " TEXT " + ")";


	public String[] getFields() {
		return new String[] {
			id, name, occupation, image
		};
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


	public static String getInsertStatment (String[] fields) {
		return String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (\"%s\", \"%s\", \"%s\", \"%s\")"
			, TABLE_NAME, COLUMN_NAME_ENTRY_ID, COLUMN_NAME_NAME, COLUMN_NAME_OCCUPATION, COLUMN_NAME_IMAGE
			, fields[0], fields[1], fields[2], fields[3]);
	}


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
	public String getTableName () {
		return TABLE_NAME;
	}


	@Override
	public String[] getProjection () {
		return MEMBER_PROJECTION;
	}
	}

