package fasttest;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.transform.ResultTransformer;


public class ObjectProjection {

	private int columns;
	private int rows = 100;
	private int size = 0;
	
	private Object[][] projection = new Object[columns][rows];
	
	public ObjectProjection(int columns) {
		this.columns = columns;
		this.projection = new Object[rows][columns];
	}

	public void setCell(int row, int column, Object object) {
		size = row+1;
		ensureCapacity();
		projection[row][column] = object;
	}

	private void ensureCapacity() {
		if (projection.length < size) {
			Object[][] _new = new Object[projection.length+10][columns];
			System.arraycopy(projection, 0, _new, 0, projection.length);
			projection = _new;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> transformResult(ResultTransformer transformer, boolean aggregate) {
		List<Object> result = new ArrayList<Object>();
		for (int row = 0; row < size; row++) {
			if (aggregate && row>0) break; 
			result.add(transformer.transformTuple(projection[row], null));
		}
		return transformer.transformList(result);
	}

}
