package ulb.dm.dbscan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ulb.dm.clustering.Cluster;
import ulb.dm.clustering.DataPoint;
import ulb.dm.clustering.DataPointType;

public class DBScan {
	
	public static List<Cluster> dbscan( List<DataPoint> dataset, double epsilon,  int minPoints ){
		
		List<Cluster> clusterList = new ArrayList<>();
		
		for ( DataPoint p : dataset ) {
			
			if( !p.visited ) {
		
				List<DataPoint> neighborPoints = regionQuery(dataset, p, epsilon);
				
				if ( neighborPoints.size() < minPoints ) {
					p.visited = true;
				} else {
					Cluster currentCluster = new Cluster();
					expandCluster( dataset, p, neighborPoints, currentCluster, epsilon, minPoints);
					clusterList.add( currentCluster );
				}
				
			}
		}
		
		return clusterList;
		
	}
	
	public static void expandCluster( List<DataPoint> dataset, DataPoint p, List<DataPoint> neighborPoints, Cluster currentCluster, double epsilon, int minPoints ) {
		
		p.visited = true;
		p.clustered = true;
		p.pointType = DataPointType.CORE;
		
		currentCluster.dataPoints.add(p);
		
		for(int i = 0; i < neighborPoints.size(); i++){
			
			
		
			DataPoint p1 = neighborPoints.get(i);
			
			if( !p1.visited ) {
				
				p1.visited = true;
				
				List<DataPoint> neighborPoints1 = regionQuery( dataset, p1, epsilon);
				
				if ( neighborPoints1.size() >= minPoints ) {
					
					p1.pointType = DataPointType.CORE;
					
					
					//Joining the neighbors of the neighbors
					for( DataPoint nPoint : neighborPoints1 ) {
						
						if( !neighborPoints.contains( nPoint ) ) {
							neighborPoints.add(nPoint);
						}
					}						
				}
			}
			
			if( !p1.clustered ) {
				p1.clustered = true;
				
				if( p1.pointType == DataPointType.NOISE ) {
					p1.pointType = DataPointType.BORDER;
				}
				
				currentCluster.dataPoints.add(p1);
			}
		}
	}
	
	public static List<DataPoint> regionQuery( List<DataPoint> dataset, DataPoint p, double epsilon ){
		
		List<DataPoint> neighborPoints = new ArrayList<>();
		
		//LOOK FOR EVERY POINT IN THE dataset TO KNOW IF ITS A NEIGHBOUR OF P.
		for( DataPoint d : dataset ) {

			double eDistance = p.getEuclidianDistance( d );
			
			if( d != p && eDistance <= epsilon ) {
				if( !neighborPoints.contains( d ) ) {
					neighborPoints.add( d );
				}
			}			
		}
		
		
		return neighborPoints;
	}
	
}
