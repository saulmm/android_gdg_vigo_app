package ameiga.saulmm.gdg.data.db;

import ameiga.saulmm.gdg.data.db.entities.DBEntity;

public abstract class DBEntry extends DBEntity {
	abstract public String getTableName();
	abstract public String[] getProjection();
}
