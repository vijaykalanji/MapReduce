package masterserver;

import java.io.Serializable;

public interface IMRTask extends Serializable{
	public Object execute(Class cl);

}
