
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
//import require classes and packages
import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.Random;
import java.lang.Math;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;
import java.util.concurrent.TimeUnit;

//Extends JPanel class
public class processing extends JPanel{
//initialize coordinates
int marg = 20;
static int numofTransactions=80000;
static String kmeansData[][]=new String [numofTransactions][6];
static int kmeansDataLength=0;
static boolean keep[]=new boolean [numofTransactions];
static int numofCategories=8;
static double data[][]=new double [numofTransactions][numofCategories];
static String transNums[]=new String [numofTransactions];

static int transnum=3;
static int item=5;
static long startTime;

static boolean firstrun=true;
	public static void main (String args[])
	{

		startTime = System.nanoTime();
		processTransactions(transNums,data,"scanner_data.csv",numofTransactions,numofCategories,transnum,item,1,0);
	    createKMeansData(transNums,data,transnum,item,6,7);

	      //create an instance of JFrame class
      JFrame frame = new JFrame();
      //set size, layout and location for frame.
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.add(new processing());
      frame.setSize(400, 400);
      frame.setLocation(200, 200);
      frame.setVisible(true);



	}
	
	public static void createKMeansData(String transNums[],double data[][],int transNum, int item, int quantity, int price)
	{
		for(int i=0;i<numofTransactions;i++)
		{
			int j;
			for (j=0;j<kmeansDataLength;j++)
			{
				if(kmeansData[j][2].equals(transNums[i]))
				{
					kmeansData[j][3]=(Double.parseDouble(kmeansData[j][3])+data[i][quantity])+"";
					kmeansData[j][0]=(Integer.parseInt(kmeansData[j][0])+1)+"";
					kmeansData[j][1]=(Double.parseDouble(kmeansData[j][1])+data[i][price])+"";
					int k=i-1;
					while(true)
					{
						if(k<1)
							break;
						if(data[i][transNum]<data[k][transNum]+0.5||data[i][transNum]>data[k][transNum]-0.5)
						{
							kmeansData[j][5]=(Double.parseDouble(kmeansData[j][5])+data[i][price])+"";
							k--;
						}
						else
						{
							break;
						}
					}
					k=i+1;
					while(true)
					{
						if(k>numofTransactions-1)
							break;
						if(data[i][transNum]<data[k][transNum]+0.5||data[i][transNum]>data[k][transNum]-0.5)
						{
							kmeansData[j][5]=(Double.parseDouble(kmeansData[j][5])+data[i][price])+"";
							k++;
						}
						else
						{
							break;
						}
					}
					break;					
				}
			}
			if(j==kmeansDataLength)
			{
				kmeansData[j][0]=1+"";
				double temp1=data[i][price];
				double temp2=data[i][quantity];
				kmeansData[j][1]=temp1+"";
				kmeansData[j][2]=transNums[i];
				kmeansData[j][3]=temp2+"";
				kmeansDataLength++;
				int k=i-1;
				kmeansData[j][5]="0";
				while(true)
				{
					if(k<1)
						break;
					if(data[i][transNum]<data[k][transNum]+0.5&&data[i][transNum]>data[k][transNum]-0.5)
					{
						kmeansData[j][5]=(Double.parseDouble(kmeansData[j][5])+data[k][price])+"";
						k--;
					}
					else
					{
						break;
					}
				}
				k=i+1;
				while(true)
				{
					if(k>numofTransactions-1)
						break;
					if(data[i][transNum]<data[k][transNum]+0.5&&data[i][transNum]>data[k][transNum]-0.5)
					{
						kmeansData[j][5]=(Double.parseDouble(kmeansData[j][5])+data[k][price])+"";
						k++;
					}
					else
					{
						break;
					}
				}
			}
		}
		for (int i=0;i<kmeansDataLength;i++)
		{
			kmeansData[i][4]=Double.parseDouble(kmeansData[i][1])+"";
			kmeansData[i][5]=Double.parseDouble(kmeansData[i][5])/Double.parseDouble(kmeansData[i][3])+"";
			kmeansData[i][1]=Double.parseDouble(kmeansData[i][1])/Double.parseDouble(kmeansData[i][3])+"";
		}
		return;
	}
	
	public static ArrayList <String> processTransactions(String transNums[],double data[][],String filename, int numofTransactions, int numofCategories, int transNum, int item, int date,int op)
	{
		ArrayList <String> transactions = new ArrayList <String>();
		File myObj = new File(filename);
		Scanner s;
		try {
			s = new Scanner (myObj);
			
			String temp2="";
			s.nextLine();
			for (int i=0;i<numofTransactions;i++)
			{
				String temp=s.nextLine();
				if(true)
				{
					for (int j=0;j<numofCategories;j++)
					{
						if (j==item)
						{
							transNums[i]=temp.substring(0,temp.indexOf(","));
						}
						else if (j==date||j==4)
						{
						}
						else if (j==numofCategories-1||(j==numofCategories-2&&transNum==numofCategories-1))
							data[i][j]=Double.parseDouble(temp);
						else
						{
							//System.out.println(i+" "+j+ " "+temp.substring(0,temp.indexOf(",")));
							data[i][j]=Double.parseDouble(temp.substring(0,temp.indexOf(",")));						
						}
						if (j!=numofCategories-1)
							temp=temp.substring(temp.indexOf(",")+1,temp.length());
					
					}
				}
				//find j where kmeansData[j][3]==transNums[i]
				int j;
				for (j=0;j<kmeansDataLength;j++)
				{

					if(kmeansData[j][2].equals(transNums[i]))
					{
						//System.out.println(kmeansData[j][2]+" "+transNums[i]+" "+keep[j]);
						break;
					}
				}
				if(op==1&&keep[j]==true)
				{
					//System.out.println(i);
					if (i==0)
						temp2=transNums[i]+"";
					else if (data[i-1][transNum]==data[i][transNum])
						temp2+=","+transNums[i];
					else
					{
						if(temp2.indexOf(",")!=-1)
						{
							transactions.add(temp2);
						}
						temp2=transNums[i]+"";
					}
				}
				//System.out.print(data[i][4]);
				
			}
			s.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		return transactions;

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
            if (isfreq) freq.add(Cand);
        }
        return freq;
    }

    public Map<Set<String>, Integer> getAboveMinSup(Set<Set<String>> candidates, Set<Set<String>> transactions, int minSup) {
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

    
    
    





    


	protected void paintComponent(Graphics grf)
	{
		
		int numofPoints =kmeansDataLength;
		int dimensions=2;
		int nearestCentroidopt[][]=new int [26][numofPoints];
		int r=100;
		int c[][][]=new int[10][r][numofPoints];
		
		double[][] values=new double[numofPoints][dimensions];
		for (int i=0;i<kmeansDataLength;i++)
		{
			values[i][0]=Double.parseDouble(kmeansData[i][4]);
			values[i][1]=Double.parseDouble(kmeansData[i][5]);
		}
		double inertia[]=new double[26];		
		//readFromFile(values, dimensions);

		//System.out.println(values[0][0]);
		double average[]=new double[2];
		for (int i=0;i<numofPoints;i++)
		{
			average[0]+=values[i][0];
			average[1]+=values[i][1];
		}
		double averageOffset=average[1]/average[0];
		//System.out.println(averageOffset);
		for (int i=0;i<numofPoints;i++)
		{
			//uncomment
			values[i][0]*=averageOffset;
		}
		//System.out.println(values[0][0]);
		
		for (int numofClusters=2; numofClusters<10;numofClusters++)
		{
			double[] interClusterDistance= new double [100];
			algo (c,numofClusters, values,  numofPoints, nearestCentroidopt,  interClusterDistance,r,false,dimensions,inertia);	
			int mindis=0;
			for (int y=1;y<100;y++)
			{
				if(interClusterDistance[y]<interClusterDistance[mindis])
				mindis=y;
			}
			System.out.println("Number of clusters: "+numofClusters +"  Inertia: "+inertia[numofClusters]);
		}
		int elbow=3;
		for (int i=4;i<15;i++)
		{
			if((inertia[i]-inertia[i-1])/(inertia[i+1]-inertia[i])>(inertia[elbow]-inertia[elbow-1])/(inertia[elbow+1]-inertia[elbow]))
			elbow=i;
		}
		System.out.println("elbow: "+elbow);

		double maxDunn[]=new double[3];
		
		for (int j=elbow-1;j<elbow+2;j++)
		{
			for(int i=0;i<r;i++)
			{
				double dunn=findDunnIndex(c[j][i],values,dimensions,numofPoints,j);
				//System.out.println(j+ ":::"+dunn);
				if(dunn>maxDunn[j-elbow+1])
				{
					maxDunn[j-elbow+1]=dunn;
						for(int k=0;k<numofPoints;k++)
						{
							nearestCentroidopt[j][k]=c[j][i][k];
						}
				}
			}
		}
			
		int bestnumofClusters=elbow-1;
		System.out.println(elbow-1 + " " +maxDunn[0]);
		for(int i=elbow;i<elbow+2;i++)
		{
			System.out.println(i + " " +maxDunn[i-elbow+1]);
			if(maxDunn[i-elbow+1]>maxDunn[bestnumofClusters-elbow+1])
				bestnumofClusters=i;
		}
		elbow=bestnumofClusters;

		
		
		
		
		super.paintComponent(grf);
		Graphics2D graph = (Graphics2D)grf;
		
		//Sets the value of a single preference for the rendering algorithms.
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//get width and height
		int width = getWidth();
		int height = getHeight();
		
		//draw graph
		graph.draw(new Line2D.Double(marg, marg, marg, height-marg));
		graph.draw(new Line2D.Double(marg, height-marg, width-marg, height-marg));
		
		
		
		//set color for points
		graph.setPaint(Color.RED);
	
		//set points to the graph
		for(int i=0; i<numofPoints; i++)
		{
			if(nearestCentroidopt[elbow][i]==0)
				graph.setPaint(Color.RED);
			else if(nearestCentroidopt[elbow][i]==1)
				graph.setPaint(Color.BLUE);
			else if(nearestCentroidopt[elbow][i]==2)
				graph.setPaint(Color.GREEN);
			else if(nearestCentroidopt[elbow][i]==3)
				graph.setPaint(Color.BLACK);
			else if(nearestCentroidopt[elbow][i]==4)
				graph.setPaint(Color.MAGENTA);
			else if(nearestCentroidopt[elbow][i]==5)
				graph.setPaint(Color.ORANGE);
			else if(nearestCentroidopt[elbow][i]==6)
				graph.setPaint(Color.CYAN);
			else if(nearestCentroidopt[elbow][i]==7)
				graph.setPaint(Color.GRAY);
			else if(nearestCentroidopt[elbow][i]==8)
				graph.setPaint(Color.YELLOW);
			else if(nearestCentroidopt[elbow][i]==9)
				graph.setPaint(Color.PINK);
			//System.out.println("Revenue: "+values[i][0]+" Avg revenue of purchases alongside: "+values[i][1]+" centroid: "+nearestCentroidopt[elbow][i]);
			graph.fill(new Ellipse2D.Double(values[i][0]+marg, 340-values[i][1], 4, 4));
			//graph.fill(new Ellipse2D.Double(c[i][0]*4+marg, c[i][1]*4+marg, 4, 4));
			
			//find centroids
		}
			
			double centroidLocation[][] = new double [elbow][3];
			for (int p=0;p<kmeansDataLength;p++)
			{
				centroidLocation[nearestCentroidopt[elbow][p]][0]+=values[p][0];
				centroidLocation[nearestCentroidopt[elbow][p]][1]+=values[p][1];
				centroidLocation[nearestCentroidopt[elbow][p]][2]++;
			}
			//calculate which centroids to get rid of
			int indices[]=new int [elbow];
			for (int i=0;i<elbow;i++)
			{
				centroidLocation[i][0]/=centroidLocation[i][2];
				centroidLocation[i][1]/=centroidLocation[i][2];
				centroidLocation[i][2]=centroidLocation[i][0]+centroidLocation[i][1];
				indices[i]=i;
			}
			
			for (int i=0;i<elbow-1;i++)
			{
				int min=indices[i];
				int q=i;
				for (int j=i;j<elbow;j++)
				{
					if(centroidLocation[indices[j]][1]<centroidLocation[min][1])
					{
						min=indices[j];
						q=j;
					}
				}
				indices[q]=indices[i];
				indices[i]=min;
				System.out.println(indices[i]);
			}
			
			//assign keep based on nearest centroid
			for(int i=0;i<kmeansDataLength;i++)
			{
				int threshold=1;
				int j;
				for(j=0;j<elbow;j++)
				{
					if(nearestCentroidopt[elbow][i]==indices[j])
						break;
				}
				if(j<=threshold)
					keep[i]=false;
				else
					keep[i]=true;
				//System.out.println(keep[i]);
			}
			apriori_java apriori = new apriori_java();
			if(firstrun==true) {
	        try {
	        	firstrun=false;
	        		ArrayList <String> itemset1Transactions = processTransactions(transNums,data,"scanner_data.csv",numofTransactions,numofCategories,transnum,item,1,1);
	    		System.out.println(itemset1Transactions.size());
	    		String str = "World";
	    		try {
	    			FileWriter fileWriter = new FileWriter("transac.txt");
	    		    PrintWriter printWriter = new PrintWriter(fileWriter);
	    		    
	    			for(int i=0;i<itemset1Transactions.size();i++)
	    				printWriter.printf(itemset1Transactions.get(i)+"\n");
	    			printWriter.close();
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		System.out.println("hello");
	            String filePath = "transac.txt"; // Update the file path to your dataset
	            int minSup = 2; // Adjust minimum support as needed

	            Set<Set<String>> transactions = apriori.loadData(filePath);

	            Map<Set<String>, Integer> freqItems = apriori.apriori(transactions, minSup);

	            // Sort itemsets by support value in descending order
	            List<Map.Entry<Set<String>, Integer>> sortedItemsets = new ArrayList<>(freqItems.entrySet());
	            sortedItemsets.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

	            // Print sorted itemsets
	            try {
        		      FileWriter myWriter = new FileWriter("filename2.txt");
      	            for (Map.Entry<Set<String>, Integer> entry : sortedItemsets) {
    	            	if(entry.getKey().size()>1)
    	            	{
    	        		      myWriter.write("Itemset: "+ entry.getKey() + " - Support: " + entry.getValue()+"\n");
    	            	}
    	            }
        		      myWriter.close();
        		      System.out.println("Successfully wrote to the file.");
        		    } catch (IOException e) {
        		      System.out.println("An error occurred.");
        		      e.printStackTrace();
        		    }

	        } catch (IOException e) {
	            System.err.println("Error reading file: " + e.getMessage());
	        }
			}
	        System.out.println("hi2");
	        long stopTime = System.nanoTime();
	        System.out.println("execution time: "+(stopTime - startTime)/100000000);
	}



	//read from file
	public static void readFromFile (double[][] values, int dimensions)
	{
		try 
		{
			File myObj = new File("values.txt");
			Scanner myReader = new Scanner(myObj);
			int i=0;
			while (i<100)
			{
				for (int j=0; j<dimensions;j++)
				values[i][j]= myReader.nextDouble();
			i++;
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	
//assigns centroids to unique random points
public static void selectRandomCentroids(double [][] centroids, int numofClusters,int dimensions, double [][]values, int numofPoints)
{
	
	Random rand = new Random();
	for (int i=0;i<numofClusters;i++)//selects a random centroid for each cluster
	{
		int k=dimensions;
		do
		{
			int v=rand.nextInt(numofPoints);
			for (int j=0;j<dimensions;j++)//assigns centroid to random point
				centroids[i][j]=values[v][j];
			//searches list to ensure centroid has not already been chosen
			for (int j=0;j<i;j++)
			{
				for (k=0;k<dimensions;k++) //checks all dimensions to see if centroids are equal
				{
					if(centroids[i][k]!=centroids[j][k]) //checks if centroid value = another centroids value  in current dimension
						break;
				}
				if(k==dimensions)//stop checking centroids if an equal one is found
					break;

			}
		}while(k==dimensions&&i!=0);//loops until new centroid is chosen

	}
}

//assigns points to their nearest centroids and calculates distances between poitns and their centroids
public static long assignPointsToNearestCentroid(long d, int numofClusters, int numofPoints, int dimensions, int nearestCentroid[], double centroids[][], double values[][])
{
d=0;
for (int i=0;i<numofPoints;i++) //repeats for all points
{
  nearestCentroid[i]=0;
  double mindis=0;
  for (int j=0;j<numofClusters;j++) //finds nearest centroid and distance between point and it
  {
     double dis=0;
     for (int k=0;k<dimensions;k++) //finds distance between point and centroid
        dis+=(values[i][k]-centroids[j][k])*(values[i][k]-centroids[j][k]); //uses distance formula to add distance between point and centroid
     if(dis<mindis||j==0)//switches centroid if distance is less than previously found minimum
     {
        nearestCentroid[i]=j;
        mindis=dis;
     }
  }
  d+=mindis; //intertia 

}
return d;
}

//Recompute centroids
public static void computeNewCentroids(int numofClusters, int dimensions, int numofPoints, int nearestCentroid[], double values[][], double centroids[][])
{
double sum[][]= new double [dimensions] [numofClusters];
  int count[]= new int[numofClusters];

  for (int j=0;j<numofPoints;j++) //sum point values for each dimension
  {
        for (int k=0;k<dimensions;k++) //add point to sum for each dimension
           sum[k][nearestCentroid[j]]+=values[j][k];
        count[nearestCentroid[j]]++;
   }

  for (int i=0;i<numofClusters;i++) //assign centroids to average of points in cluster
  {
     for (int k=0;k<dimensions;k++) //calculate average of points as new centroid
        centroids[i][k]=sum[k][i]/count[i];
   }
}

//checks if K Means should stop
public static boolean stopKMeans(int q,double c[][][], int numofClusters, int dimensions)
{
	
if (q==9999) //stop after 9999 iterations
  return true;
if (q==0)
   return false; //continue if first iteration
for(int i=0;i<numofClusters;i++) //check if any clusters have changed
{
     for (int k=0;k<dimensions;k++)//check if cluster matches cluster from previous iteration
     {
        if(c[i][q][k]!=c[i][q-1][k])
           return false;
     }
}
return true; //no clusters have changed (local max found)
}

public static void algo (int c [][][],int numofClusters, double values[][], int numofPoints, int nearestCentroidopt[][],  double interClusterDistance[], int r, boolean dunn, int dimensions, double minInertia[])
{
	
	int nearestCentroid[]=new int[numofPoints];

	for(int p=0;p<r;p++)
	{
		double [][] centroids = new double [numofClusters][dimensions];
		selectRandomCentroids(centroids,numofClusters,dimensions,values,numofPoints);
		double distance[]= new double [numofClusters];
		int distanceCount[]= new int [numofClusters];
		long inertia=0;
		double centroid[][][]= new double[numofClusters][1000][dimensions];
		boolean isdone=false;
		int q=0;
			
		while(isdone==false&&q<1000)
		{
			inertia=assignPointsToNearestCentroid( inertia,  numofClusters, numofPoints, dimensions, nearestCentroid, centroids,values);
			computeNewCentroids(numofClusters, dimensions, numofPoints, nearestCentroid, values, centroids);
			//System.out.println("centroid #"+0+": "+centroids[0][0]+" ;; "+centroids[0][1]);
			//System.out.println("centroid #"+1+": "+centroids[1][0]+" ;; "+centroids[1][1]);
			
			for (int j=0;j<numofClusters;j++)
			{
				for (int k=0;k<dimensions;k++)
				{
					//System.out.println(k+" "+j+" "+q);
					centroid[j][q][k]=centroids[j][k];
				}
					
			}
			
			isdone=stopKMeans(q,centroid,numofClusters, dimensions);
			q++;
		}
		
		//System.out.println(inertia);
		// System.out.println(q+" "+inertia);
		if(minInertia[numofClusters]>inertia||p==0)
			minInertia[numofClusters]=inertia;
		for (int i=0;i<numofPoints;i++)
			c[numofClusters][p][i]=nearestCentroid[i];
		
			
	}
}

public static double findDunnIndex(int nearestCentroid[],double values[][],int dimensions,int numofPoints,int numofClusters)
{
	double maxInterClusterDistance=0;
	double minIntraClusterDistance=0;
	double dunnIndex=0;
	double distance[]=new double [numofClusters];
	double distanceCount[]=new double [numofClusters];
	double raDistance[][]=new double [numofClusters][numofClusters];
	double raDistanceCount[][]=new double [numofClusters][numofClusters];
	for (int i=0;i<numofPoints-1;i++)
	{
		for (int j=i+1;j<numofPoints;j++)
		{
			double apart=0;
			for (int k=0;k<dimensions;k++)
				apart+=(values[i][k]-values[j][k])*(values[i][k]-values[j][k]);
			if(nearestCentroid[i]==nearestCentroid[j])
			{
				distance[nearestCentroid[i]]+=Math.sqrt(apart);
				distanceCount[nearestCentroid[i]]++;
			}
			else
			{
				raDistance[nearestCentroid[i]][nearestCentroid[j]]+=Math.sqrt(apart);
				raDistanceCount[nearestCentroid[i]][nearestCentroid[j]]++;
			}
				
		}
	}
	
	minIntraClusterDistance=(distance[0]/distanceCount[0]);
	maxInterClusterDistance=(raDistance[0][1]/raDistanceCount[0][1]);
	for (int i=1;i<numofClusters;i++)
	{
		if (distance[i]/distanceCount[i]>minIntraClusterDistance)
			minIntraClusterDistance=(distance[i]/distanceCount[i]);
		for (int j=i+1;j<numofClusters;j++)
		{
			if(raDistance[i][j]/raDistanceCount[i][j]<maxInterClusterDistance)
				maxInterClusterDistance=(raDistance[i][j]/raDistanceCount[i][j]);
		}
	}
	
	//System.out.println(numofClusters+": "+maxIntraClusterDistance+": "+minInterClusterDistance);
	
	dunnIndex =maxInterClusterDistance/minIntraClusterDistance;
	return dunnIndex;

}


}