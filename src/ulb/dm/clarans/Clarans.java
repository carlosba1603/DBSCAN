package ulb.dm.clarans;

import java.util.ArrayList;
import java.util.List;

import ulb.dm.clustering.DataPoint;
import ulb.dm.clustering.Cluster;;

public class Clarans {
	
	public int n;
	public int k;
	public int maxNeighbors;
	public int numLocal;
	
	
	
	public static List<Cluster> clarans( List<DataPoint> dataset, List<DataPoint> centroids, int maxNeighbors, int numLocal ) {
		
		centroids = getRandomCentroids(dataset);
		
		n = dataset.size();
		k = centroids.size();
		this.maxNeighbors = maxNeighbors;
		this.numLocal = numLocal;
		
	
		claransAlgorithm();
		
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

	}
	
	
	//Clarans 
	public List<DataPoint> ClaransAlgorithm ()
	{
		ArrayList <DissimilarityMatrix> dyssimilarityMatrix = new DataPoint [n][2];
		
		
		
		//
		for (int i = 0; i < numLocal; i ++)
		{
			
		}
	}
	
	//Choose random node
	
	public List<DataPoint> getRandomNode (List<DataPoint> dataset, List<DataPoint> centroids)
	{
	
	int index = (int) Math.round((Math.random()*k));
	boolean stop = false;	
	DataPoint centroid = null;
	List<DataPoint> newNode = new ArrayList<DataPoint>();

    while (!stop) {
        
    	stop = true;	
        centroid = dataset.get((int) Math.round(Math.random()*n));
        
        for (int i = 0; i < k; i++)
        {
            if (centroid == centroids.get(i)) 
            {
            	stop = false;
                break;
            }
        }
    }

    for (int j = 0; j < centroids.size();j++)
    { 	
    	if (j == index)
    	{
    		newNode.add(centroid);
    	}
    	newNode.add(centroids.get(j));	
    }

    
    return newNode;
	}
	
	//Change centroid
	
	public DissimilarityMatrix changeCentroid (List<DataPoint> dataset)
	
	
	
	
	public static DataPoint getNewCentroid( List<DataPoint> dataset, DataPoint currentCentroid, int maxNeighbor, int numLocal ) {
		
		DataPoint newCentroid = null;
		
		
		
		return newCentroid;
		
	}
	
}
