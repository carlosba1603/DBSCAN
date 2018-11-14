package ulb.dm.clarans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ulb.dm.clustering.DataPoint;
import ulb.dm.clustering.Cluster;;

public class Clarans 
	{
	
	public static List<Cluster> clarans( List<DataPoint> dataset, List<DataPoint> centroids, int maxNeighbors, int numLocal ) {
		
		
		centroids = new ArrayList<DataPoint> ();
		HashMap<String,String> hc1 = new HashMap<String,String> ();
		hc1.put("Actions", "id137");
		hc1.put("C1", "6.47390395996");
		hc1.put("C2", "93.7242014429");	
		DataPoint c1 = new DataPoint(hc1, "Actions");
		centroids.add(c1);
		
		HashMap<String,String> hc2 = new HashMap<String,String> ();
		hc2.put("Actions", "id91");
		hc2.put("C1", "94.1977118379");
		hc2.put("C2", "4.09522116449");	
		DataPoint c2 = new DataPoint(hc2, "Actions");
		centroids.add(c2);
		
		HashMap<String,String> hc3 = new HashMap<String,String> ();
		hc3.put("Actions", "id141");
		hc3.put("C1", "50.3583738409");
		hc3.put("C2", "47.3792286943");	
		DataPoint c3 = new DataPoint(hc3, "Actions");
		centroids.add(c3);	

		
		int n = dataset.size();
		int k = centroids.size();
		
		
		Node initialSol = new Node (dataset, centroids); 
		
		//Iterator i = initialSol.DissimilarityMatrix.keySet().iterator();
		//while (i.hasNext())
		//{
			//String key = (String) i.next();
			//String id = initialSol.DissimilarityMatrix.get(key).centroid.idAttribute;
		//System.out.println("llave "+key+";valor"+initialSol.DissimilarityMatrix.get(key).centroid.attributtes.get(id));
		//List<DataPoint> h = getRandomNode(dataset,centroids);
		//}

	    Node optCentroids =  claransAlgorithm(dataset,initialSol,maxNeighbors, numLocal,k );
		
		
	    List<Cluster> claransClusters = new ArrayList<>();
	    //Create clusters
	    Iterator t = optCentroids.clusters.values().iterator();
	    while(t.hasNext())
	    {
	    	claransClusters.add((Cluster) t.next());
	    }

		return claransClusters;

	}
	
	
	//Clarans 
	public static Node claransAlgorithm (List<DataPoint> dataset,Node initialSol,int maxNeighbors, int numLocal, int k )
	{
		//Use Hilbert solution
		Node randomNode = initialSol;
		Node currentNode = initialSol;
		double minCost = initialSol.distanceCost;
		//Iterate over numlocal
		for (int i = 0; i < numLocal;i++)
		{
			
		  if (i > 0)
				{
				 randomNode = getRandomNode(dataset, k);
				}
			
			for (int j = 0; j < maxNeighbors;j++)
			{
				Node neigh = getRandomNeighbor(dataset,randomNode.centroids);
				if(minCost > neigh.distanceCost)
				{
					currentNode = neigh;
					minCost = neigh.distanceCost;
					j=0;
				}
			}
		}
		
		
		return currentNode;
	}
	
	//Change one centroid randomly
	
	public static Node getRandomNeighbor (List<DataPoint> dataset, List<DataPoint> centroids)
	{
	
	int k = centroids.size();
	int n = dataset.size();
		 
	Random r = new Random();
	int index = r.nextInt(k);

	boolean stop = false;	
	DataPoint centroid = null;
	List<DataPoint> newNode = new ArrayList<DataPoint>(centroids);
    
	
    while (!stop) 
    {    
    	stop = true;	
    	int indexC = r.nextInt(n); 
        centroid = dataset.get(indexC); 
        
        for (int i = 0; i < k; i++)
        {
        	String ncAtt = centroid.attributtes.get(centroid.idAttribute);
        	String cAtt = centroids.get(i).attributtes.get(centroids.get(i).idAttribute);
            if (ncAtt.equalsIgnoreCase(cAtt))
            {
            	stop = false;
                break;
            }
        }
    }
    
    newNode.set(index, centroid);
    
    Node neigh = new Node (dataset, newNode);

    return neigh;
	}
	
	//Create random node
	public static Node getRandomNode (List<DataPoint> dataset, int k)
	{
		ArrayList <Integer> indexes = new ArrayList <Integer>();
		List<DataPoint> newC = new ArrayList<DataPoint>  ();
		int n = dataset.size();
		
		Random r = new Random();
		int index = r.nextInt(n);
		indexes.add(index);
		
		for (int i = 0;i<k;i++)
		{
			boolean enc = false;
			while (!enc)
			{
				index = r.nextInt(n);
				enc = checkRandomNum (indexes, index);
				if(enc)
				{
					indexes.add(index);
				}	
			}
		
		newC.add(dataset.get(index));	
		}
		
		Node randomNode = new Node (dataset, newC);
		
		return randomNode;
		
	}
	

	//Check unique random numbers
	public static boolean checkRandomNum (ArrayList <Integer> numbers, int num)
	{
		for (int i = 0; i < numbers.size(); i++)
		{
			if(num == numbers.get(i))
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	
	
	public static DataPoint getNewCentroid( List<DataPoint> dataset, DataPoint currentCentroid, int maxNeighbor, int numLocal ) {
		
		DataPoint newCentroid = null;

		return newCentroid;
		
	}
	
}
