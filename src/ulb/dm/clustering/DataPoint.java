package ulb.dm.clustering;
import java.util.HashMap;



public class DataPoint {
	
	
	
	HashMap<String,String> attributtes;
	public String idAttribute;
	
	public DataPointType pointType;
	public boolean visited;
	public boolean clustered;
	
	public DataPoint( HashMap<String,String> attributtes, String idAttribute ) {
		
		this.attributtes = attributtes;
		this.idAttribute = idAttribute;
		this.pointType = DataPointType.NOISE;
	}
	
	public double getEuclidianDistance( DataPoint p1  ) {
		
		double sum = 0.0d;
		double euclidianDistance = 0.0d;
		
		for( String key :  attributtes.keySet() ) {
			if( !key.equalsIgnoreCase( this.idAttribute ) ) {
				sum += Math.pow( getDiffAttribute( p1, key ), 2);
			}
		}
		
		euclidianDistance = Math.sqrt( sum );
		
		return euclidianDistance;
		
	}
	
	public double getDiffAttribute( DataPoint p1, String key ) {
		
		double a = Double.parseDouble( this.attributtes.get(key) );
		double b = Double.parseDouble( p1.attributtes.get(key) );
		
		return a-b;
	}
}
