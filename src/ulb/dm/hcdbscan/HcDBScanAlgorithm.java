package ulb.dm.hcdbscan;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import ulb.dm.clarans.Clarans;
import ulb.dm.clustering.Cluster;
import ulb.dm.clustering.DataPoint;
import ulb.dm.clustering.DataPointType;
import ulb.dm.dbscan.DBScan;
import ulb.dm.hilbert.HilbertCurveIndexing;

public class HcDBScanAlgorithm {

	public static String idAttribute;
	public static Double maxPoint;
	public static double epsilon = 10.0;
	public static double alfa = 0.155;
	public static int minPoints = 4;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Validate if the console input has the csv file name and the class label.
		if (args.length < 5) {
			System.out.println("java HcDBScanAlgorithm [file.csv] [idColumn_name] [epsilon] [minPoints] [alpha]  ");
			return;
		}

		
		List<DataPoint> dataset = new ArrayList<>();
		
		

		try {

			epsilon = Double.parseDouble( args[2] );
			minPoints = Integer.parseInt( args[3] );
			alfa = Double.parseDouble( args[4] );
			
			// Read the dataset
			dataset = readCSVFile(args[0], args[1]);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		List<Cluster> hcDbscan = hcDbscan(  dataset, alfa, epsilon,  minPoints );
		
		System.out.println("\n=== Normal DBSCAN === epsilon: "+epsilon+" minPoints: "+minPoints+"\n");
		
		dataset = new ArrayList<>();
		try {

			// Read the dataset
			dataset = readCSVFile(args[0], args[1]);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		List<Cluster> dbscan = dbscan(  dataset, epsilon,  minPoints );
		
		

	}
	
	public static List<Cluster> dbscan(List<DataPoint> dataset, double epsilon, int minPoints) {
		
		List<Cluster> dbscan = DBScan.dbscan(  dataset, epsilon,  minPoints );
		Map<String, Cluster> labelClusterList = labelClusterList(dbscan, "DBSCAN");
		
		for( Cluster c : labelClusterList.values() ) {
			
			System.out.println( c.label+" size: "+c.dataPoints.size()+c );
			
		}
		
		System.out.println("\nDBSCAN_Noise\n");
		
		for( DataPoint p : dataset ) {
			
			if( p.pointType == DataPointType.NOISE )
				System.out.println( p );
			
		}
		
		return dbscan;
	}

	public static List<Cluster> hcDbscan(List<DataPoint> dataset, double alfa, double epsilon, int minPoints) {

		List<Cluster> hcDbscanClusterList;
		List<Cluster> clusterListBeforeMerge = new ArrayList<>();

		// Get the centroids for CLARANS
		int curveOrder = 8;
		List<DataPoint> centroids = HilbertCurveIndexing.getHilbertCentroids(dataset, curveOrder, maxPoint);

		// Get clarans clusters for DBSCAN
		int maxNeighbors = (int) Math.round(0.0125*centroids.size()*(dataset.size()-centroids.size())); 
		int numLocal = 2;

		List<Cluster> claransClusterList = Clarans.clarans(dataset, centroids, maxNeighbors, numLocal);

		// Apply DBSCAN on each CLARANS cluster
		for (Cluster claransCluster : claransClusterList) {

			List<DataPoint> clusterDataset = new ArrayList<DataPoint>(claransCluster.dataPoints);

			List<Cluster> dbscanClusterList = DBScan.dbscan(clusterDataset, epsilon, minPoints);

			clusterListBeforeMerge.addAll(dbscanClusterList);

		}
		
		Map<String, Cluster> labelClusterList = labelClusterList(clusterListBeforeMerge, "DBSCAN");
		
		System.out.println("=== DBSCAN ===\n");
		
		for( Cluster c : labelClusterList.values() ) {
			
			System.out.println( c.label+" size: "+c.dataPoints.size()+c );
			
		}
		
		System.out.println("\nDBSCAN_Noise\n");
		
		for( DataPoint p : dataset ) {
			
			if( p.pointType == DataPointType.NOISE )
				System.out.println( p );
			
		}
		
		/*System.out.println("\nDBSCAN_CORE\n");
		
		for( DataPoint p : dataset ) {
			
			if( p.pointType == DataPointType.CORE )
				System.out.println( p );
			
		}*/

		// Merge all DBSCAN clusters
		hcDbscanClusterList = mergeDbscanClusters(dataset, labelClusterList, alfa, epsilon);

		return hcDbscanClusterList;

	}

	public static Map<String, Cluster> labelClusterList(List<Cluster> clusters , String label ) {

		Map<String, Cluster> clusterMap = new LinkedHashMap<>();

		for (int i = 0; i < clusters.size(); i++) {

			Cluster c = clusters.get(i);
			c.label = label + "_" + i;

			clusterMap.put(c.label, c);

		}

		return clusterMap;

	}

	public static List<Cluster> mergeDbscanClusters(List<DataPoint> dataset, Map<String, Cluster> clusterMap, double alfa, double epsilon) {

		List<Cluster> clusterList = new ArrayList<>();

		Map<String, Double> adjacentList = getRIAdjacentList(clusterMap, epsilon);

		for (Cluster c : clusterMap.values()) {

			if (!c.visited) {

				List<Cluster> neighborClusterList = regionClusterQuery(clusterMap, adjacentList, c, alfa);

				Cluster newCluster = new Cluster();

				expandCluster(clusterMap, adjacentList, c, neighborClusterList, newCluster, alfa);

				// fix noisy datapoints

				List<DataPoint> noisyList = getNoisyPts(dataset);

				Map<String, Cluster> hmap = new HashMap<>();

				hmap.put("", newCluster);

				fixNoisyPts(hmap, noisyList, epsilon);

				clusterList.add(newCluster);

			}
		}

		System.out.println("\n======================= HC-DBSCAN ========================= alfa: "+alfa+" epsilon: "+epsilon+"\n");

		Map<String, Cluster> labelClusterList = labelClusterList( clusterList, "HC-DBSCAN");
		
		for( Cluster c : labelClusterList.values() ) {
					
			System.out.println( c.label+" size: "+c.dataPoints.size()+c );
					
		}

		System.out.println("\nHC-DBSCAN_Noise\n");
		
		for( DataPoint p : dataset ) {
			
			if( p.pointType == DataPointType.NOISE )
				System.out.println( p );
			
		}

		return clusterList;

	}

	public static Map<String, Double> getRIAdjacentList(Map<String, Cluster> clusterMap, double epsilon) {

		HashMap<String, Double> interconectivityAdjacentList = new HashMap<>();

		for (Cluster c : clusterMap.values()) {

			for (Cluster d : clusterMap.values()) {

				if (c != d) {

					if (!(interconectivityAdjacentList.containsKey(c.label + "-" + d.label)
							|| interconectivityAdjacentList.containsKey(d.label + "-" + c.label))) {

						double currentAlfa = getRIAlfa(c, d, epsilon);

						if (currentAlfa != 0.0)
							interconectivityAdjacentList.put(c.label + "-" + d.label, currentAlfa);

					}

				}

			}

		}

		return interconectivityAdjacentList;
	}

	public static List<DataPoint> getNoisyPts(List<DataPoint> dataset) { // creating a list includes all noisyPoints

		List<DataPoint> noisyList = new ArrayList<>();

		for (DataPoint currentPts : dataset) {
			if (currentPts.pointType == DataPointType.NOISE) {
				noisyList.add(currentPts);
			}

		}

		return noisyList;

	}

	public static void fixNoisyPts(Map<String, Cluster> clusterMap, List<DataPoint> noisyList, double epsilon) {

		for (DataPoint currentNoise : noisyList) {
			mergeNoisyPts(clusterMap, currentNoise, epsilon);
		}

	}

	private static void mergeNoisyPts(Map<String, Cluster> clusterMap, DataPoint currentNoise, double epsilon) {
		// check every point in a cluster and compare the distance, if it meets with the
		// requirements, merge currentNoise to a cluster
		double eDistance;

		for (Cluster currentCluster : clusterMap.values()) {
			
			List<DataPoint> datapoints = new ArrayList<>( currentCluster.dataPoints );
			
			for ( int i = 0; i < datapoints.size(); i++ ) {
				DataPoint p = datapoints.get(i);
				
				if ( p.pointType == DataPointType.CORE ) {
					
					eDistance = currentNoise.getEuclidianDistance(p);
					
					if ( eDistance <= epsilon ) {
						currentNoise.pointType = DataPointType.BORDER;
						currentNoise.visited = true;
						currentNoise.clustered = true;
						currentCluster.dataPoints.add(currentNoise);

					}

				}
			}

		}

	}

	public static double getRIAlfa(Cluster c, Cluster d, double epsilon) {

		double alfa = 0.0;

		List<Double> edges = getEdges(c, d, epsilon);

		if (edges.size() > 0)
			alfa = (double) edges.size() / getSumEdges(edges);

		return alfa;

	}

	public static List<Double> getEdges(Cluster c1, Cluster c2, double epsilon) {

		List<Double> edges = new ArrayList<>();

		for (DataPoint d1 : c1.dataPoints) {
			if (d1.pointType == DataPointType.BORDER || d1.pointType == DataPointType.CORE ) {
				for (DataPoint d2 : c2.dataPoints) {
					if (d2.pointType == DataPointType.BORDER || d2.pointType == DataPointType.CORE ) {

						double eDistance = d1.getEuclidianDistance(d2);

						if (eDistance <= epsilon) {
							edges.add(eDistance);
						}

					}
				}
			}
		}

		return edges;

	}

	public static double getSumEdges(List<Double> edges) {

		double sum = 0.0;

		for (Double edge : edges) {
			sum += edge;
		}

		return sum;

	}

	public static void expandCluster(Map<String, Cluster> clusterMap, Map<String, Double> adjacentList, Cluster c,
			List<Cluster> neighborClusters, Cluster newCluster, double alfa) {

		c.visited = true;
		c.clustered = true;

		newCluster.dataPoints.addAll(c.dataPoints);

		for (int i = 0; i < neighborClusters.size(); i++) {

			Cluster c1 = neighborClusters.get(i);

			if (!c1.visited) {

				c1.visited = true;

				List<Cluster> neighborPoints1 = regionClusterQuery(clusterMap, adjacentList, c1, alfa);

				for (Cluster nCluster : neighborPoints1) {
					if (!neighborClusters.contains(nCluster)) {
						neighborClusters.add(nCluster);
					}
				}

			}

			if (!c1.clustered) {
				c1.clustered = true;

				newCluster.dataPoints.addAll(c1.dataPoints);
			}
		}
	}

	public static List<Cluster> regionClusterQuery(Map<String, Cluster> clusterMap, Map<String, Double> adjacentList,
			Cluster c, double alfa) {

		List<Cluster> neighborClusterList = new ArrayList<>();

		// LOOK FOR EVERY POINT IN THE dataset TO KNOW IF ITS A NEIGHBOUR OF P.
		for (String key : adjacentList.keySet()) {

			String clusterKeys[] = key.split("-");

			if (clusterKeys[0].equals(c.label) || clusterKeys[1].equals(c.label)) {

				double aDistance = adjacentList.get(key);

				if (aDistance <= alfa) {

					String neighborLabel = clusterKeys[0].equals(c.label) ? clusterKeys[1] : clusterKeys[0];

					Cluster neighbour = clusterMap.get(neighborLabel);

					if( neighbour == null ) {
						System.out.println( neighborLabel );
					}
					
					if (!neighborClusterList.contains(neighbour)) {
						neighborClusterList.add(neighbour);
					}
				}
			}
		}

		return neighborClusterList;
	}

	/*
	 * Read the dataset from the csv file
	 */
	private static List<DataPoint> readCSVFile(String fileName, String idAttribute) throws FileNotFoundException {

		File csvFile = new File(fileName);
		Scanner scanner = new Scanner(csvFile);
		ArrayList <Double> allPoints = new ArrayList <Double>();

		List<DataPoint> dataset = new ArrayList<>();

		String[] attributes = scanner.nextLine().split(",");

		while (scanner.hasNext()) {

			String[] data = scanner.nextLine().split(",");
			HashMap<String, String> row = new HashMap<>();

			for (int i = 0; i < Math.min(attributes.length,3); i++) 
			{
					row.put(attributes[i], data[i]);
			
					if (!attributes[i].equals(idAttribute))
					{
						allPoints.add(Double.parseDouble(data[i]));
					}
				
			}

			dataset.add(new DataPoint(row, idAttribute));

		}
		
		//Choose max to normalize for Hilbert curve
		double maxPointN = 0;
		for (int k = 0; k < allPoints.size(); k++)
		{
			if (maxPointN < allPoints.get(k)) 
			{
				maxPointN = allPoints.get(k);
			}
		}
		
		maxPoint = maxPointN;

		scanner.close();

		return dataset;

	}

}
