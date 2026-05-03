package test;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.awt.*;

import javax.swing.*;
import java.awt.geom.*;
import java.lang.Math;

import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.IOException;

import java.util.concurrent.TimeUnit;

public class processingkmeans extends JPanel {
	// initialize coordinates
	int marg = 20;
	static String values[][] = new String[500][3];
	static int order[] = new int[10];
	static int nearestCentroidopt[][] = new int[26][1000];
	static int elbow = 3;
	static String newData[][] = new String[131706][1000];
	static ArrayList<String> transactions = new ArrayList<String>();
	static String data[][] = new String[131706][1000];

	public static void main(String args[]) {
		String itemset1k[][] = processTransactions("itemset1.csv", 1000, 8, 3, 5, 7, 6);
		for (int i = 0; i < 500; i++) {
			for (int j = 0; j < 3; j++)
				values[i][j] = itemset1k[i][j];
		}

		// create an instance of JFrame class
		JFrame frame = new JFrame();
		// set size, layout and location for frame.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new processingkmeans());
		frame.setSize(459, 481);
		frame.setLocation(200, 200);
		frame.setVisible(true);

	}

	public static String[][] processTransactions(String filename, int numofTransactions, int numofCategories,
			int transNum, int item, int price, int quantity) {
		ArrayList<String> items = new ArrayList<String>();

		File myObj = new File(filename);
		Scanner s;
		try {
			s = new Scanner(myObj);

			String temp2 = "";
			s.nextLine();
			for (int i = 0; i < numofTransactions; i++) {
				String temp = s.nextLine();
				for (int j = 0; j < numofCategories; j++) {
					if (j == numofCategories - 1)
						data[i][j] = temp;
					else {
						data[i][j] = temp.substring(0, temp.indexOf(","));
						temp = temp.substring(temp.indexOf(",") + 1, temp.length());

					}
				}

			}

			for (int i = 0; i < numofTransactions; i++) {
				int j;
				for (j = 0; j < items.size(); j++) {
					if (items.get(j).substring(0, items.get(j).indexOf(",")).equals(data[i][item])) {
						String name = items.get(j).substring(0, items.get(j).indexOf(","));
						String temp3 = items.get(j).substring(items.get(j).indexOf(",") + 1, items.get(j).length());
						double culmprice = Double.parseDouble(temp3.substring(0, temp3.indexOf(",")));
						temp3 = temp3.substring(temp3.indexOf(",") + 1, temp3.length());
						double quantity2 = Double.parseDouble(temp3.substring(0, temp3.indexOf(",")));
						temp3 = temp3.substring(temp3.indexOf(",") + 1, temp3.length());
						int freq = Integer.parseInt(temp3);
						culmprice += Double.parseDouble(data[i][price]);
						quantity2 += Double.parseDouble(data[i][quantity]);
						freq++;
						items.set(j, name + "," + culmprice + "," + quantity2 + "," + freq);
						break;
					}
				}
				if (j == items.size())
					items.add(data[i][item] + "," + data[i][price] + "," + data[i][quantity] + ",1");
			}

			String kmeansData[][] = new String[items.size()][3];
			for (int i = 0; i < items.size(); i++) {
				String temp3 = items.get(i).substring(items.get(i).indexOf(",") + 1, items.get(i).length());
				double culmprice = Double.parseDouble(temp3.substring(0, temp3.indexOf(",")));
				temp3 = temp3.substring(temp3.indexOf(",") + 1, temp3.length());
				double quantity2 = Double.parseDouble(temp3.substring(0, temp3.indexOf(",")));
				int freq = Integer.parseInt(temp3.substring(temp3.indexOf(",") + 1, temp3.length()));
				kmeansData[i][0] = culmprice / quantity2 + "";
				kmeansData[i][1] = freq + "";
				kmeansData[i][2] = items.get(i).substring(0, items.get(i).indexOf(","));
			}
			double avgPrice = 0;
			double frequency = 0;
			for (int i = 0; i < items.size(); i++) {
				avgPrice += Double.parseDouble(kmeansData[i][0]);
				frequency += Double.parseDouble(kmeansData[i][1]);
			}
			for (int i = 0; i < items.size(); i++)
				kmeansData[i][0] = (Double.parseDouble(kmeansData[i][0]) * frequency / avgPrice) + "";

			s.close();
			return kmeansData;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public Set<Set<String>> loadData(String filePath) throws IOException {
		Set<Set<String>> transactions = new HashSet<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] items = line.split(",");
				Set<String> transaction = new HashSet<>(Arrays.asList(items));
				transactions.add(transaction);
			}
		}
		return transactions;
	}

	public Map<Set<String>, Integer> frequentItemsets(Set<Set<String>> transactions, int minSup) {
		Map<String, Integer> itemCounts = new HashMap<>();
		Map<Set<String>, Integer> freqItemSets = new HashMap<>();

		for (Set<String> transaction : transactions) {
			for (String item : transaction) {
				itemCounts.put(item, itemCounts.getOrDefault(item, 0) + 1);
			}
		}

		for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
			if (entry.getValue() >= minSup) {
				freqItemSets.put(Collections.singleton(entry.getKey()), entry.getValue());
			}
		}
		return freqItemSets;
	}

	public Set<Set<String>> generateCandidates(Set<Set<String>> listKMinusOne, int k) {
		Set<Set<String>> candidates = new HashSet<>();
		for (Set<String> itemSet1 : listKMinusOne) {
			for (Set<String> itemSet2 : listKMinusOne) {
				Set<String> union = new HashSet<>(itemSet1);
				union.addAll(itemSet2);
				if (union.size() == k) {
					candidates.add(union);
				}
			}
		}
		return candidates;
	}

	public Set<Set<String>> pruning(Set<Set<String>> cK, Set<Set<String>> lk1) {
		Set<Set<String>> freq = new HashSet<>();
		for (Set<String> Cand : cK) {
			boolean isfreq = true;
			for (String can : Cand) {
				Set<String> sub = new HashSet<>(Cand);
				sub.remove(can);
				if (!lk1.contains(sub)) {
					isfreq = false;
					break;
				}
			}
			if (isfreq)
				freq.add(Cand);
		}
		return freq;
	}

	public Map<Set<String>, Integer> getAboveMinSup(Set<Set<String>> candidates, Set<Set<String>> transactions,
			int minSup) {
		Map<Set<String>, Integer> candCounts = new HashMap<>();
		for (Set<String> trans : transactions) {
			for (Set<String> cand : candidates) {
				if (trans.containsAll(cand)) {
					candCounts.put(cand, candCounts.getOrDefault(cand, 0) + 1);
				}
			}
		}
		Map<Set<String>, Integer> freqItemSets = new HashMap<>();
		for (Map.Entry<Set<String>, Integer> entry : candCounts.entrySet()) {
			if (entry.getValue() >= minSup) {
				freqItemSets.put(entry.getKey(), entry.getValue());
			}
		}
		return freqItemSets;
	}

	public Map<Set<String>, Integer> apriori(Set<Set<String>> transactions, int minSup) {
		Map<Set<String>, Integer> freqItems = new HashMap<>();
		Map<Set<String>, Integer> minusOne = frequentItemsets(transactions, minSup);
		freqItems.putAll(minusOne);
		for (int k = 2; !minusOne.isEmpty(); k++) {
			Set<Set<String>> ck = generateCandidates(minusOne.keySet(), k);
			Set<Set<String>> freqCands = pruning(ck, minusOne.keySet());
			Map<Set<String>, Integer> lk = getAboveMinSup(freqCands, transactions, minSup);
			freqItems.putAll(lk);
			minusOne = lk;
		}
		return freqItems;
	}

	protected void paintComponent(Graphics grf) {
		int numofPoints = 500;
		int dimensions = 2;

		int r = 100;
		int c[][][] = new int[10][r][numofPoints];

		double inertia[] = new double[26];

		for (int numofClusters = 2; numofClusters < 10; numofClusters++) {
			double[] interClusterDistance = new double[numofPoints];
			algo(c, numofClusters, values, numofPoints, nearestCentroidopt, interClusterDistance, 100, false,
					dimensions, inertia);
			int mindis = 0;
			for (int y = 1; y < 100; y++) {
				if (interClusterDistance[y] < interClusterDistance[mindis])
					mindis = y;
			}
			System.out.println("Number of clusters: " + numofClusters + "  Inertia: " + inertia[numofClusters]);
		}

		for (int i = 4; i < 15; i++) {
			if ((inertia[i] - inertia[i - 1]) / (inertia[i + 1] - inertia[i]) > (inertia[elbow] - inertia[elbow - 1])
					/ (inertia[elbow + 1] - inertia[elbow]))
				elbow = i;
		}
		System.out.println("elbow: " + elbow);

		double maxDunn[] = new double[3];

		for (int j = elbow - 1; j < elbow + 2; j++) {
			for (int i = 0; i < r; i++) {
				double dunn = findDunnIndex(c[j][i], values, dimensions, numofPoints, j);
				if (dunn > maxDunn[j - elbow + 1]) {
					maxDunn[j - elbow + 1] = dunn;
					for (int k = 0; k < numofPoints; k++) {
						nearestCentroidopt[j][k] = c[j][i][k];
					}
				}
			}
		}

		int bestnumofClusters = elbow - 1;
		System.out.println(elbow - 1 + " " + maxDunn[0]);
		for (int i = elbow; i < elbow + 2; i++) {
			System.out.println(i + " " + maxDunn[i - elbow + 1]);
			if (maxDunn[i - elbow + 1] > maxDunn[bestnumofClusters - elbow + 1])
				bestnumofClusters = i;
		}
		elbow = bestnumofClusters;
		/*
		 * for (int f=0;f<q;f++)
		 * System.out.println(c[0][f][0]+" "+c[0][f][1]+ "q"+c[1][f][0]+" "+c[1][f][1]+
		 * "q"+c[2][f][0]+" "+c[2][f][1]+ "q"+c[3][f][0]+" "+c[3][f][1]);
		 * for (int i=0;i<100;i++)
		 * {
		 * System.out.println(values[i][0]+ " , "+ values[i][1]+
		 * " , "+nearestCentroid[i]);
		 * }
		 */
		// create instance of the Graphics to use its methods

		Graphics2D graph = (Graphics2D) grf;

		// Sets the value of a single preference for the rendering algorithms.
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// get width and height

		// draw graph

		// set color for points
		graph.setPaint(Color.RED);
		for (int i = 0; i < numofPoints; i++) {
			System.out.println(nearestCentroidopt[elbow][i] + "; " + values[i][2]);
		}
		// set points to the graph
		for (int i = 0; i < numofPoints; i++) {
			if (nearestCentroidopt[elbow][i] == 0)
				graph.setPaint(Color.RED);
			else if (nearestCentroidopt[elbow][i] == 1)
				graph.setPaint(Color.BLUE);
			else if (nearestCentroidopt[elbow][i] == 2)
				graph.setPaint(Color.GREEN);
			else if (nearestCentroidopt[elbow][i] == 3)
				graph.setPaint(Color.BLACK);
			else if (nearestCentroidopt[elbow][i] == 4)
				graph.setPaint(Color.MAGENTA);
			else if (nearestCentroidopt[elbow][i] == 5)
				graph.setPaint(Color.ORANGE);
			else if (nearestCentroidopt[elbow][i] == 6)
				graph.setPaint(Color.CYAN);
			else if (nearestCentroidopt[elbow][i] == 7)
				graph.setPaint(Color.GRAY);
			else if (nearestCentroidopt[elbow][i] == 8)
				graph.setPaint(Color.YELLOW);
			else if (nearestCentroidopt[elbow][i] == 9)
				graph.setPaint(Color.PINK);
			graph.fill(new Ellipse2D.Double(Double.parseDouble(values[i][0]) * 2 + marg,
					Double.parseDouble(values[i][1]) / 7 + marg, 4, 4));
			// graph.fill(new Ellipse2D.Double(c[i][0]*4+marg, c[i][1]*4+marg, 4, 4));
		}

		double centroids[][] = new double[elbow][3];
		for (int i = 0; i < numofPoints; i++) {
			centroids[nearestCentroidopt[elbow][i]][0] += Double.parseDouble(values[i][0]);
			centroids[nearestCentroidopt[elbow][i]][1] += Double.parseDouble(values[i][1]);
			centroids[nearestCentroidopt[elbow][i]][1]++;
		}

		for (int i = 0; i > elbow - 1; i++) {
			int min = i;
			for (int j = i + 1; j < elbow; j++) {
				if (centroids[order[j]][0] * centroids[order[j]][1]
						/ (centroids[order[j]][2] * centroids[order[j]][2]) < centroids[order[min]][0]
								* centroids[order[min]][1] / (centroids[order[min]][2] * centroids[order[min]][2]))
					min = j;
			}
			int temp = order[i];
			order[i] = order[min];
			order[min] = temp;
		}

		int added = 0;
		for (int i = 0; i < 1000; i++) {
			int j;
			for (j = 0; j < 4; j++) {
				if (nearestCentroidopt[elbow][i] == order[j])
					break;
			}
			if (j == 4) {
				System.out.println(added);
				for (int k = 0; k < 10; k++)
					newData[added][k] = data[i][k];
				added++;
			}
		}
		int transNum = 3;
		String temp2 = "";
		int item = 5;
		for (int i = 0; i < added; i++) {
			System.out.println(newData[i][transNum]);
			if (i == 0)
				temp2 = newData[i][item];
			else if (newData[i - 1][transNum].equals(newData[i][transNum]))
				temp2 += "," + newData[i][item];
			else {
				if (temp2.indexOf(",") != -1) {
					transactions.add(temp2);
				}
				temp2 = newData[i][item];
			}
		}
		String str = "World";
		try {
			FileWriter fileWriter = new FileWriter("transac.txt");
			PrintWriter printWriter = new PrintWriter(fileWriter);

			for (int i = 0; i < 1000; i++)
				printWriter.printf(transactions.get(i) + "\n");
			printWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		apriori_java apriori = new apriori_java();
		try {
			String filePath = "transac.txt"; // Update the file path to your dataset
			int minSup = 2; // Adjust minimum support as needed

			Set<Set<String>> transactions = apriori.loadData(filePath);

			Map<Set<String>, Integer> freqItems = apriori.apriori(transactions, minSup);

			// Sort itemsets by support value in descending order
			List<Map.Entry<Set<String>, Integer>> sortedItemsets = new ArrayList<>(freqItems.entrySet());
			sortedItemsets.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

			// Print sorted itemsets
			for (Map.Entry<Set<String>, Integer> entry : sortedItemsets) {
				if (entry.getKey().size() > 1)
					System.out.println("Itemset: " + entry.getKey() + " - Support: " + entry.getValue());
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
		}
	}

	// read from file

	// assigns centroids to unique random points
	public static void selectRandomCentroids(double[][] centroids, int numofClusters, int dimensions, String[][] values,
			int numofPoints) {
		Random rand = new Random();
		for (int i = 0; i < numofClusters; i++)// selects a random centroid for each cluster
		{
			int k = dimensions;
			do {
				int v = rand.nextInt(numofPoints);
				for (int j = 0; j < dimensions; j++)// assigns centroid to random point
					centroids[i][j] = Double.parseDouble(values[v][j]);
				// searches list to ensure centroid has not already been chosen
				for (int j = 0; j < i; j++) {
					for (k = 0; k < dimensions; k++) // checks all dimensions to see if centroids are equal
					{
						if (centroids[i][k] != centroids[j][k]) // checks if centroid value = another centroids value in
																// current dimension
							break;
					}
					if (k == dimensions)// stop checking centroids if an equal one is found
						break;

				}
			} while (k == dimensions && i != 0);// loops until new centroid is chosen

		}
	}

	// assigns points to their nearest centroids and calculates distances between
	// poitns and their centroids
	public static int assignPointsToNearestCentroid(int d, int numofClusters, int numofPoints, int dimensions,
			int nearestCentroid[], double centroids[][], String values[][]) {
		d = 0;
		for (int i = 0; i < numofPoints; i++) // repeats for all points
		{
			nearestCentroid[i] = 0;
			double mindis = 0;
			for (int j = 0; j < numofClusters; j++) // finds nearest centroid and distance between point and it
			{
				double dis = 0;
				for (int k = 0; k < dimensions; k++) // finds distance between point and centroid
					dis += (Double.parseDouble(values[i][k]) - centroids[j][k])
							* (Double.parseDouble(values[i][k]) - centroids[j][k]); // uses distance formula to add
																					// distance between point and
																					// centroid
				if (dis < mindis || j == 0)// switches centroid if distance is less than previously found minimum
				{
					nearestCentroid[i] = j;
					mindis = dis;
				}
			}
			d += mindis; // intertia

		}
		return d;
	}

	// Recompute centroids
	public static void computeNewCentroids(int numofClusters, int dimensions, int numofPoints, int nearestCentroid[],
			String values[][], double centroids[][]) {
		double sum[][] = new double[dimensions][numofClusters];
		int count[] = new int[numofClusters];

		for (int j = 0; j < numofPoints; j++) // sum point values for each dimension
		{
			for (int k = 0; k < dimensions; k++) // add point to sum for each dimension
				sum[k][nearestCentroid[j]] += Double.parseDouble(values[j][k]);
			count[nearestCentroid[j]]++;
		}

		for (int i = 0; i < numofClusters; i++) // assign centroids to average of points in cluster
		{
			for (int k = 0; k < dimensions; k++) // calculate average of points as new centroid
				centroids[i][k] = sum[k][i] / count[i];
		}
	}

	// checks if K Means should stop
	public static boolean stopKMeans(int q, double c[][][], int numofClusters, int dimensions) {

		if (q == 9999) // stop after 9999 iterations
			return true;
		if (q == 0)
			return false; // continue if first iteration
		for (int i = 0; i < numofClusters; i++) // check if any clusters have changed
		{
			for (int k = 0; k < dimensions; k++)// check if cluster matches cluster from previous iteration
			{
				if (c[i][q][k] != c[i][q - 1][k])
					return false;
			}
		}
		return true; // no clusters have changed (local max found)
	}

	public static void algo(int c[][][], int numofClusters, String values[][], int numofPoints,
			int nearestCentroidopt[][], double interClusterDistance[], int r, boolean dunn, int dimensions,
			double minInertia[]) {

		int nearestCentroid[] = new int[numofPoints];

		for (int p = 0; p < r; p++) {
			double[][] centroids = new double[numofClusters][dimensions];
			selectRandomCentroids(centroids, numofClusters, dimensions, values, numofPoints);
			double distance[] = new double[numofClusters];
			int distanceCount[] = new int[numofClusters];
			int inertia = 0;
			double centroid[][][] = new double[numofClusters][10000][dimensions];
			boolean isdone = false;
			int q = 0;

			while (isdone == false) {
				inertia = assignPointsToNearestCentroid(inertia, numofClusters, numofPoints, dimensions,
						nearestCentroid, centroids, values);
				computeNewCentroids(numofClusters, dimensions, numofPoints, nearestCentroid, values, centroids);
				// System.out.println("centroid #"+0+": "+centroids[0][0]+" ;;
				// "+centroids[0][1]);
				// System.out.println("centroid #"+1+": "+centroids[1][0]+" ;;
				// "+centroids[1][1]);

				for (int j = 0; j < numofClusters; j++) {
					for (int k = 0; k < dimensions; k++)
						centroid[j][q][k] = centroids[j][k];
				}

				isdone = stopKMeans(q, centroid, numofClusters, dimensions);
				q++;
			}

			// System.out.println(inertia);
			// System.out.println(q+" "+inertia);
			if (minInertia[numofClusters] > inertia || p == 0)
				minInertia[numofClusters] = inertia;
			for (int i = 0; i < numofPoints; i++)
				c[numofClusters][p][i] = nearestCentroid[i];

		}
	}

	public static double findDunnIndex(int nearestCentroid[], String values[][], int dimensions, int numofPoints,
			int numofClusters) {
		double maxIntraClusterDistance = 0;
		double minInterClusterDistance = 0;
		double dunnIndex = 0;
		double distance[] = new double[numofClusters];
		double distanceCount[] = new double[numofClusters];
		double erDistance[][] = new double[numofClusters][numofClusters];
		double erDistanceCount[][] = new double[numofClusters][numofClusters];
		for (int i = 0; i < numofPoints - 1; i++) {
			for (int j = i + 1; j < numofPoints; j++) {
				double apart = 0;
				for (int k = 0; k < dimensions; k++)
					apart += (Double.parseDouble(values[i][k]) - Double.parseDouble(values[j][k]))
							* (Double.parseDouble(values[i][k]) - Double.parseDouble(values[j][k]));
				if (nearestCentroid[i] == nearestCentroid[j]) {
					distance[nearestCentroid[i]] += Math.sqrt(apart);
					distanceCount[nearestCentroid[i]]++;
				} else {
					erDistance[nearestCentroid[i]][nearestCentroid[j]] += Math.sqrt(apart);
					erDistanceCount[nearestCentroid[i]][nearestCentroid[j]]++;
				}

			}
		}

		maxIntraClusterDistance = distance[0] / distanceCount[0];
		minInterClusterDistance = erDistance[0][1] / erDistanceCount[0][1];
		for (int i = 1; i < numofClusters; i++) {
			if (distance[i] / distanceCount[i] < maxIntraClusterDistance)
				maxIntraClusterDistance = distance[i] / distanceCount[i];
			for (int j = i + 1; j < numofClusters; j++) {
				if (erDistance[i][j] / erDistanceCount[i][j] > minInterClusterDistance)
					minInterClusterDistance = erDistance[i][j] / erDistanceCount[i][j];
			}
		}

		dunnIndex = minInterClusterDistance / maxIntraClusterDistance;
		return dunnIndex;

	}

}