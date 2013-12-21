package fucverg.saulmm.gdg.utils;

import android.util.Log;
import fucverg.saulmm.gdg.data.db.DBTableInfo;
import fucverg.saulmm.gdg.data.db.entities.DBEntity;

public class DbUtils {
	public static final String COMMA = ",";

	public static <G extends DBEntity> DBTableInfo getDBTableInfo (Class<G> type) {
		DBTableInfo info = null;

		try {
			G dbEntry = type.newInstance();

			info = new DBTableInfo();
			info.setProjection( dbEntry.getProjection());
			info.setTableName(dbEntry.getTableName());

		} catch (Exception e) {
			Log.e("[ERROR] fucverg.saulmm.gdg.data.db.DBHandler.DBTableInfo.getDBTableInfo ",
					"Exception: " + e.getMessage());
		}

		return info;
	}
}
