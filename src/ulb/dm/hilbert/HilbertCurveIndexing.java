package ulb.dm.hilbert;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ulb.dm.clustering.Cluster;
import ulb.dm.clustering.DataPoint;

//Hilbert curve implementation
public class HilbertCurveIndexing 
{
	
	//Main method 
	public static List<DataPoint> getHilbertCentroids( List<DataPoint> dataset, int curveOrder, double maxPoint )
	{
		
		List<DataPoint> indexedData = indexData(dataset, curveOrder, maxPoint);
		
		//Order centroids
		List<DataPoint> centroids = orderAndCluster(indexedData);	
		
		System.out.println("=== HILBERT CENTROIDS ===\n");
		for (int c = 0; c < centroids.size();c++)
		{
			System.out.println(centroids.get(c));
		}
		return centroids;
	}
	
	
	//Indexing data to Hilbert curve by using referenced code and normalizing it
	public static List<DataPoint> indexData( List<DataPoint> dataset, int curveOrder, double maxPoint )
	{
		ArrayList<DataPoint> indexedPoints = new ArrayList <DataPoint>();
		for (int i = 0; i <dataset.size(); i++)
		{
			
			DataPoint p = dataset.get(i);
			Iterator it = p.attributtes.keySet().iterator();
			int limDim = 0;
			int x = 0;
			int y = 0;
			while( it.hasNext() && limDim <=2  ) 
			{
				String key = (String) it.next();
				if( !key.equalsIgnoreCase( p.idAttribute ) ) 
				{
					if (limDim == 0)
					{
					    double ogX = Double.parseDouble(p.attributtes.get(key));
					    double normalizedX = Math.min((ogX/maxPoint)*(curveOrder-1), maxPoint);
						x = (int) Math.round(normalizedX);
						
					}
					else  
					{
						double ogY = Double.parseDouble(p.attributtes.get(key));
					    double normalizedY = Math.min((ogY/maxPoint)*(curveOrder-1),maxPoint);
						y = (int) Math.round(normalizedY);
					}
					limDim++;
					
				}
			}
			if (limDim == 2)
				{
					int hEncode = encode(x,y,curveOrder);
					p.hilbertIndex = hEncode;
					indexedPoints.add(p);
				}
					
				
				
			}	
		return indexedPoints;
		
		}

	//Order and cluster centroids
	public static List<DataPoint> orderAndCluster (List<DataPoint> indexedData)
	{
	
		List<DataPoint> centroids = new ArrayList <DataPoint>();
		//Sort
		Collections.sort(indexedData);
	 
		//Cluster with a difference of 1
	 
		List <ArrayList<DataPoint>> clusters = new ArrayList <ArrayList<DataPoint>> ();
	
		 ArrayList<DataPoint> cluster = new ArrayList <DataPoint>  ();
		 for (int i = 1; i<indexedData.size();i++)
		 {
			 if(i == 1)
				 cluster.add(indexedData.get(0));
			 if(indexedData.get(i).hilbertIndex-indexedData.get(i-1).hilbertIndex <=1)
				 cluster.add(indexedData.get(i));
			 else
			 {
				 clusters.add(cluster);
				 cluster = new ArrayList <DataPoint>  ();
			     cluster.add(indexedData.get(i));
			 }
				 
	 }
		    //Add last cluster
		    clusters.add(cluster);
	 
	 
		 //Get median
		 for (int j = 0; j <clusters.size();j++)
		 {
			 if (clusters.get(j).size() == 1)
				 centroids.add(clusters.get(j).get(0));
			 
			 else
			 {
				 int medianIndex = getMedian(clusters.get(j));
				 centroids.add(clusters.get(j).get(medianIndex));
			 }
		 }
	 
	 return centroids;
	}
	
	
	//Get median
	public static int getMedian (List <DataPoint> data)
	{
		 int medianIndex;
		 double x = data.size();
		 double xdiv2 = x/2;
		 if (data.size() % 2 == 0)
			    medianIndex = data.size()/2;
		else
			    medianIndex  = (int) Math.ceil(xdiv2);
		 
		 return medianIndex;
	}
	
	
	
//Referenced code	
	
	/**
	 * Find the Hilbert order (=vertex index) for the given grid cell 
	 * coordinates.
	 * @param x cell column (from 0)
	 * @param y cell row (from 0)
	 * @param r resolution of Hilbert curve (grid will have Math.pow(2,r) 
	 * rows and cols)
	 * @return Hilbert order 
	 */
	public static int encode(int x, int y, int r) {

	    int mask = (1 << r) - 1;
	    int hodd = 0;
	    int heven = x ^ y;
	    int notx = ~x & mask;
	    int noty = ~y & mask;
	    int temp = notx ^ y;

	    int v0 = 0, v1 = 0;
	    for (int k = 1; k < r; k++) {
	        v1 = ((v1 & heven) | ((v0 ^ noty) & temp)) >> 1;
	        v0 = ((v0 & (v1 ^ notx)) | (~v0 & (v1 ^ noty))) >> 1;
	    }
	    hodd = (~v0 & (v1 ^ x)) | (v0 & (v1 ^ noty));

	    return interleaveBits(hodd, heven);
	}
	
	/**
	 * Interleave the bits from two input integer values
	 * @param odd integer holding bit values for odd bit positions
	 * @param even integer holding bit values for even bit positions
	 * @return the integer that results from interleaving the input bits
	 *
	 * @todo: I'm sure there's a more elegant way of doing this !
	 */
	private static int interleaveBits(int odd, int even) {
	    int val = 0;
	    // Replaced this line with the improved code provided by Tuska
	    // int n = Math.max(Integer.highestOneBit(odd), Integer.highestOneBit(even));
	    int max = Math.max(odd, even);
	    int n = 0;
	    while (max > 0) {
	        n++;
	        max >>= 1;
	    }

	    for (int i = 0; i < n; i++) {
	        int bitMask = 1 << i;
	        int a = (even & bitMask) > 0 ? (1 << (2*i)) : 0;
	        int b = (odd & bitMask) > 0 ? (1 << (2*i+1)) : 0;
	        val += a + b;
	    }

	    return val;
	}

	

	
	
}



