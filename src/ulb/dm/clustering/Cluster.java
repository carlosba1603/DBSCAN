package ulb.dm.clustering;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cluster {
	
	public Set<DataPoint> dataPoints;//USe for clarans only
	
	
	public String label;
	public boolean visited;
	public boolean clustered;
	
	//For dbscan
	public List<DataPoint> coreDataPoints;
	public List<DataPoint> borderDataPoints;
	
	public DataPoint centroid;
	
	public Cluster() {
		this.dataPoints = new HashSet<>();
	}
	
	public Cluster( HashSet<DataPoint> dataPoints ) {
		this.dataPoints = dataPoints;
	}
}
