package ulb.dm.clarans;

import java.util.ArrayList;
import java.util.List;

import ulb.dm.clustering.DataPoint;
import ulb.dm.clustering.Cluster;;

public class Clarans {
	
	
	
	
	public static List<Cluster> clarans( List<DataPoint> dataset, List<DataPoint> centroids ) {
		
		List<Cluster> claransClusters = new ArrayList<>();
		
		//Create clusters based in the k centroids
		
		// get the currentCost
		
		//while the model has not converge
			//For each of the cluster
					//get new centroid for the cluster
					
			//classify
			//if the cost is lower than current cost keep the new centroids
			//else keep current centroids
		
		//Once clarans is finished return the Model
		
		
		return claransClusters;
	}
	
	
	public static DataPoint getNewCentroid( List<DataPoint> dataset, DataPoint currentCentroid, int maxNeighbor, int numLocal ) {
		
		DataPoint newCentroid = null;
		
		
		
		return newCentroid;
		
	}
	
}
