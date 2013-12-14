package fucverg.saulmm.gdg.data.db.entities;

import java.io.Serializable;

public abstract class DBEntity implements Serializable {

	public abstract DBEntity createDBEntity (String [] fields);
}
