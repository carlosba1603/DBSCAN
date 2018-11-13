package ulb.dm.hcdbscan;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import ulb.dm.clarans.Clarans;
import ulb.dm.clustering.Cluster;
import ulb.dm.clustering.DataPoint;
import ulb.dm.clustering.DataPointType;
import ulb.dm.dbscan.DBScan;
import ulb.dm.hilbert.HilbertCurveIndexing;

public class HcDBScanAlgorithm {
	 
	public static String idAttribute;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Validate if the console input has the csv file name and the class label.
		if( args.length < 2 ) {
			System.out.println("java ID3 [file.csv] [idColumn name]");
			return;
		}
				
		
		List<DataPoint> dataset = new ArrayList<>();
		double epsilon = 3.0;
		double alfa = 10.0;
		int minPoints = 5;
		
		try {
			
			//Read the dataset
			dataset = readCSVFile( args[0], args[1] );
			
		} catch ( FileNotFoundException e ){
			e.printStackTrace();
		}
		
		
		
		List<Cluster> hcDbscan = hcDbscan(  dataset, alfa, epsilon,  minPoints );
		
		for( Cluster c : hcDbscan ) {
			
			System.out.println("Size: "+c.dataPoints.size());
			
		}
		
		System.out.println("Finished");
	}
	
	public static List<Cluster> hcDbscan( List<DataPoint> dataset, double alfa, double epsilon,  int minPoints ){
		
		List<Cluster> hcDbscanClusterList;
		List<Cluster> clusterListBeforeMerge = new ArrayList<>();
		
		
		
		//Get the centroids for CLARANS
		List<DataPoint> centroids = HilbertCurveIndexing.getCentroids(dataset, true );
		
		
		//Get clarans clusters for DBSCAN
		int maxNeighbors = 0; //To confirm with Ainhoa 
		int numLocal = 0;
		
		List<Cluster> claransClusterList = Clarans.clarans(dataset, centroids, maxNeighbors,numLocal);
		
		//Apply DBSCAN on each CLARANS cluster
		for( Cluster claransCluster : claransClusterList ) {
			
			List<DataPoint> clusterDataset = new ArrayList<DataPoint>( claransCluster.dataPoints );
			
			List<Cluster> dbscanClusterList = DBScan.dbscan( clusterDataset, epsilon, minPoints );
			
			clusterListBeforeMerge.addAll( dbscanClusterList );
			
		}
		
		//Merge all DBSCAN clusters
		hcDbscanClusterList = mergeDbscanClusters( labelClusterList( clusterListBeforeMerge ), alfa );
		
		return hcDbscanClusterList;
		
	}
	
	public static Map<String,Cluster> labelClusterList( List<Cluster> clusters ){
		
		Map<String,Cluster> clusterMap = new HashMap<>();
		
		for( int i = 0 ; i < clusters.size(); i++ ) {
			
			Cluster c  = clusters.get(i);
			c.label = ""+i;
			
			clusterMap.put(""+i, c);
			
		}
		
		return clusterMap;
		
	}
	
	public static List<Cluster> mergeDbscanClusters( Map<String,Cluster> clusterMap, double alfa ){
		
		List<Cluster> clusterList = new ArrayList<>();
		
		Map<String,Double> adjacentList = getAdjacentList( clusterMap );
		
		for ( Cluster c : clusterMap.values() ) {
			
			if( !c.visited ) {
		
				List<Cluster> neighborClusterList = regionClusterQuery(clusterMap, adjacentList, c, alfa);
				
				Cluster newCluster = new Cluster();
				
				expandCluster( clusterMap, adjacentList, c, neighborClusterList, newCluster, alfa );
					
				//fix noisy datapoints
				
				clusterList.add( newCluster );
				
				
			}
		}
		
		return clusterList;
		
	}
	
	public static Map<String,Double> getAdjacentList( Map<String,Cluster>  clusterMap ){
		
		//TODO implement algorithm
		
		return new HashMap<String,Double>();
	}
	
	
	public static void expandCluster( Map<String,Cluster> clusterMap, Map<String,Double> adjacentList, Cluster c, List<Cluster> neighborClusters, Cluster newCluster, double alfa ) {
		
		c.visited = true;
		c.clustered = true;
		
		
		newCluster.dataPoints.addAll( c.dataPoints );
		
		for(int i = 0; i < neighborClusters.size(); i++){
			
			Cluster c1 = neighborClusters.get(i);
			
			if( !c1.visited ) {
				
				c1.visited = true;
				
				List<Cluster> neighborPoints1 = regionClusterQuery( clusterMap, adjacentList, c1, alfa);
				
				for( Cluster nCluster : neighborPoints1 ) {
						if( !neighborClusters.contains( nCluster ) ) {
							neighborClusters.add(nCluster);
						}
				}
											
				
			}
			
			if( !c1.clustered ) {
				c1.clustered = true;
				
				newCluster.dataPoints.addAll(c1.dataPoints);
			}
		}
	}
	
	public static List<Cluster> regionClusterQuery( Map<String,Cluster> clusterMap, Map<String,Double> adjacentList, Cluster c, double alfa ){
		
		List<Cluster> neighborClusterList = new ArrayList<>();
		
		//LOOK FOR EVERY POINT IN THE dataset TO KNOW IF ITS A NEIGHBOUR OF P.
		for( String key : adjacentList.keySet() ) {

			if( key.contains(c.label) ) {
			
				double aDistance = adjacentList.get(key);
				
				if( aDistance <= alfa ) {
					
					String neighborLabel = key.replace(c.label, "").replaceAll("-", "");
					
					Cluster neighbour = clusterMap.get( neighborLabel );
					
					if( !neighborClusterList.contains( neighbour ) ) {
						neighborClusterList.add( neighbour );
					}
				}
			}
		}
		
		return neighborClusterList;
	}
	
	
	/*
	 * Read the dataset from the csv file
	 * */
	private static List<DataPoint> readCSVFile( String fileName, String idAttribute ) throws FileNotFoundException{
		
		File csvFile = new File( fileName );
		Scanner scanner = new Scanner( csvFile );

		List<DataPoint> dataset = new ArrayList<>();
		
		String[] attributes = scanner.nextLine().split(",");
		
        while(scanner.hasNext()){
        	
        	String[] data = scanner.nextLine().split(",");
        	HashMap<String,String> row = new HashMap<>();
        	
        	for( int i = 0; i < attributes.length; i++ ) {
        		if(!attributes[i].equals("C3"))
        			row.put(attributes[i], data[i]);
        	}
    		
        	dataset.add(new DataPoint( row, idAttribute ) );
    		
        }
        
        scanner.close();
        
        return dataset;
		
	}
	

	
	

}

