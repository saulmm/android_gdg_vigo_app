package ameiga.saulmm.gdg.data.db;

import java.util.Arrays;

public class DBTableInfo {
	private String tableName;
	private String [] projection;


	public void setTableName (String tableName) {
		this.tableName = tableName;
	}


	public void setProjection (String[] projection) {
		this.projection = projection;
	}


	public String getTableName () {
		return tableName;
	}


	public String[] getProjection () {
		return projection;
	}


	@Override
	public String toString () {
		return "DBTableInfo{" +
				"tableName='" + tableName + '\'' +
				", projection=" + Arrays.toString(projection) +
				'}';
	}
}