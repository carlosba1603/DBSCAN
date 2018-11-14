package ulb.dm.clarans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ulb.dm.clustering.Cluster;
import ulb.dm.clustering.DataPoint;

public class Node 
{

	public List<DataPoint> centroids;
	public double distanceCost;
	public HashMap<String,Cluster> clusters =  new HashMap<String,Cluster>();
	
	
	HashMap <String,DissimilarityPoint> DissimilarityMatrix = new HashMap <String,DissimilarityPoint>();
	
	public Node (List<DataPoint> dataset, List<DataPoint> centroids)
	{
		this.centroids = centroids;
		for (int i = 1; i < centroids.size();i++)
		{
			Cluster c = new Cluster(centroids.get(i));
			String att = centroids.get(i).idAttribute;
			clusters.put(centroids.get(i).attributtes.get(att), c);
		}
		
		
		for (int i = 0; i < dataset.size();i++)
		{
			String id = dataset.get(i).idAttribute;
			DissimilarityPoint cen = getCentroid(dataset.get(i),centroids);
			distanceCost = distanceCost+cen.distance;
			DissimilarityMatrix.put(dataset.get(i).attributtes.get(id), cen);
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
		
		Cluster c = clusters.get(minDataPoint.attributtes.get(minDataPoint.idAttribute));
		c.dataPoints.add(point);
		
		return new DissimilarityPoint (minDataPoint,point);
	}
	
	//Get datalist by centroid
	
	
	
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
