package ulb.dm.hilbert;

import java.util.ArrayList;
import java.util.List;

import ulb.dm.clustering.Cluster;
import ulb.dm.clustering.DataPoint;


public class HilbertCurveIndexing {

	public static void indexData( List<DataPoint> dataset ){
		
		
		
	}
	
	public static List<DataPoint> getCentroids( List<DataPoint> dataset, boolean random ){
		List<DataPoint> centroids = new ArrayList<>();
		
		if( random )
			return getRandomCentroids(dataset);
		else
			return getHilbertCentroids(dataset);
		
	}
	
	public static List<DataPoint> getRandomCentroids( List<DataPoint> dataset ){
		List<DataPoint> centroids = new ArrayList<>();
		
		
		return centroids;
	}
	
	public static List<DataPoint> getHilbertCentroids( List<DataPoint> dataset ){
		List<DataPoint> centroids = new ArrayList<>();
		
		//Index data adding the hilbert distance to the data point
		indexData(dataset);
		
		return centroids;
	}
	
}
