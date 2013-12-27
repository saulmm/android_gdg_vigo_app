package ameiga.saulmm.gdg.data.db.entities;

import java.io.Serializable;

public abstract class DBEntity implements Serializable {

	public abstract DBEntity createDBEntity (String [] fields);
	abstract public String getTableName();
	abstract public String[] getProjection();
}
