package fasttest;

import com.rits.cloning.Cloner;

public class CopyUtils {

	public static Object copy(Object object) {
		Cloner cloner = new Cloner();
		return cloner.deepClone(object);
	}
	
}
