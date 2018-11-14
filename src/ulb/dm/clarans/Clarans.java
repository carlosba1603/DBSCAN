package ulb.dm.clarans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ulb.dm.clustering.DataPoint;
import ulb.dm.clustering.Cluster;;

public class Clarans {
	

	
	public static List<Cluster> clarans( List<DataPoint> dataset, List<DataPoint> centroids, int maxNeighbors, int numLocal ) {
		
		
		centroids = new ArrayList<DataPoint> ();
		HashMap<String,String> hc1 = new HashMap<String,String> ();
		hc1.put("Actions", "id137");
		hc1.put("C1", "6.47390395996");
		hc1.put("C2", "93.7242014429");	
		DataPoint c1 = new DataPoint(hc1, "id137");
		centroids.add(c1);
		
		HashMap<String,String> hc2 = new HashMap<String,String> ();
		hc2.put("Actions", "id91");
		hc2.put("C1", "94.1977118379");
		hc2.put("C2", "4.09522116449");	
		DataPoint c2 = new DataPoint(hc2, "id91");
		centroids.add(c2);
		
		HashMap<String,String> hc3 = new HashMap<String,String> ();
		hc3.put("Actions", "id141");
		hc3.put("C1", "50.3583738409");
		hc3.put("C2", "47.3792286943");	
		DataPoint c3 = new DataPoint(hc3, "id141");
		centroids.add(c3);	

		
		int n = dataset.size();
		int k = centroids.size();
		
		
		Node initialSol = new Node (dataset, centroids);
		//Iterator i = initial.DissimilarityMatrix.keySet().iterator();
		//while (i.hasNext())
		//{
		//	String key = (String) i.next();
		//System.out.println("llave "+key+";valor"+initial.DissimilarityMatrix.get(key).centroid.idAttribute);
		//}
		//List<DataPoint> h = getRandomNode(dataset,centroids);
		

	     claransAlgorithm(initialSol,maxNeighbors, numLocal );
		
		List<Cluster> claransClusters = new ArrayList<>();
		
		return null;
		
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
	public static List<DataPoint> claransAlgorithm (Node initialSol,int maxNeighbors, int numLocal )
	{
		//Iterate over
		
		return null;
	}
	
	//Change one centroid randomly
	
	public static List<DataPoint> getRandomNode (List<DataPoint> dataset, List<DataPoint> centroids)
	{
	
	int k = centroids.size();
	int n = dataset.size();
		 
	Random r = new Random();
	int index = r.nextInt(k);
	int indexC = r.nextInt(n); 
	boolean stop = false;	
	DataPoint centroid = null;
	List<DataPoint> newNode = new ArrayList<DataPoint>(centroids);
    
	
    while (!stop) 
    {    
    	stop = true;	
        centroid = dataset.get(indexC); 
        
        for (int i = 0; i < k; i++)
        {
            if (centroid.idAttribute.equalsIgnoreCase(centroids.get(i).idAttribute)) 
            {
            	stop = false;
                break;
            }
        }
    }
    
    newNode.set(index, centroid);
    
    return newNode;
	}
	
	//Create random node
	public static List<DataPoint> getRandomNode (List<DataPoint> dataset, int k)
	{
		ArrayList <Integer> indexes = new ArrayList <Integer>();
		int n = dataset.size();
		
		for (int i = 0;i<k;i++)
		{
			Random r = new Random();
			int index = r.nextInt(n);
			
			
		}
	}
	

	//Check unique random numbers
	public static checkRandomNum (ArrayList <Integer> numbers, int num)
	{
		for (int i = 0; i < numbers.size(); i++)
		{
			if(num = numbers.get(i))
			{
				
			}
		}
	}
	
	
	
	
	public static DataPoint getNewCentroid( List<DataPoint> dataset, DataPoint currentCentroid, int maxNeighbor, int numLocal ) {
		
		DataPoint newCentroid = null;
		
		
		
		return newCentroid;
		
	}
	
}
