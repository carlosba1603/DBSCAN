package ulb.dm.clarans;

import java.util.HashMap;
import java.util.List;

import ulb.dm.clustering.DataPoint;

public class Node 
{

	HashMap <String,DissimilarityPoint> DissimilarityMatrix = new HashMap <String,DissimilarityPoint>();
	
	public Node (List<DataPoint> dataset, List<DataPoint> centroids)
	{
		for (int i = 0; i < dataset.size();i++)
		{
			DissimilarityMatrix.put(dataset.get(i).idAttribute, getCentroid(dataset.get(i),centroids));
		}
	}
	
	//Get point's centroid
	public  DissimilarityPoint getCentroid (DataPoint point, List<DataPoint> centroids )
	{
		DataPoint minDataPoint = centroids.get(0);
		double minDistance = centroids.get(0).getEuclidianDistance(point);
		for (int i = 1; i < centroids.size();i++)
		{
			double dist = centroids.get(i).getEuclidianDistance(point);
			if (dist < minDistance)
			{
				minDataPoint = centroids.get(i);
				minDistance = dist;
			}
		}
		
		return new DissimilarityPoint (minDataPoint,point);
	}
	
	public class DissimilarityPoint 
	{
		public DataPoint centroid;
		public DataPoint point;
		public double distance;
		
		public DissimilarityPoint (DataPoint centroid, DataPoint point)
		{
			this.centroid = centroid;
			this.point = point;
			distance = centroid.getEuclidianDistance(point);
		}
	}
}
