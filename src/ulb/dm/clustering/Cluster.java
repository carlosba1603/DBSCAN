package ulb.dm.clustering;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Cluster {
	
	public Set<DataPoint> dataPoints;
	
	
	public String label;
	public boolean visited;
	public boolean clustered;
	
	
	
	public DataPoint centroid;
	
	public Cluster() {
		this.dataPoints = new HashSet<>();
	}
	
	public Cluster( HashSet<DataPoint> dataPoints ) 
	{
		this.dataPoints = dataPoints;
	}
	
	public Cluster(DataPoint centroid ) 
	{
		this.dataPoints = new HashSet<>();
		this.centroid = centroid;
	}
	
	public String toString()
	{
		String s = "";
		Iterator<DataPoint> i = dataPoints.iterator();
		while (i.hasNext())
		{
			s+=centroid+";"+i.next().toString()+"\n";
		}
		
		
		return s;
	}
	
}
