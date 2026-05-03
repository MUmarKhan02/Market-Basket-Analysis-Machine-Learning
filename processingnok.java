package test;

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

public class processingnok {
	
	
	public static void main (String args[])
	{
	
		ArrayList <String> itemset1Transactions = processTransactions("itemset1.csv",100000,8,3,5);
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

		 apriori_java apriori = new apriori_java();
	        try {
	            String filePath = "transac.txt"; // Update the file path to your dataset
	            int minSup = 2; // Adjust minimum support as needed

	            Set<Set<String>> transactions = apriori.loadData(filePath);

	            Map<Set<String>, Integer> freqItems = apriori.apriori(transactions, minSup);

	            // Sort itemsets by support value in descending order
	            List<Map.Entry<Set<String>, Integer>> sortedItemsets = new ArrayList<>(freqItems.entrySet());
	            sortedItemsets.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

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
	
	public static ArrayList <String> processTransactions(String filename, int numofTransactions, int numofCategories, int transNum, int item)
	{
		ArrayList <String> transactions = new ArrayList <String>();
		File myObj = new File(filename);
		Scanner s;
		try {
			s = new Scanner (myObj);
			String data[][]=new String [numofTransactions][numofCategories];
			String temp2="";
			s.nextLine();
			for (int i=0;i<numofTransactions;i++)
			{
				String temp=s.nextLine();
				for (int j=0;j<numofCategories;j++)
				{
					if (j==numofCategories-1)
						data[i][j]=temp;
					else
					{
						data[i][j]=temp.substring(0,temp.indexOf(","));
						temp=temp.substring(temp.indexOf(",")+1,temp.length());
						
					}
				}
				if (i==0)
					temp2=data[i][item];
				else if (data[i-1][transNum].equals(data[i][transNum]))
					temp2+=","+data[i][item];
				else
				{
					if(temp2.indexOf(",")!=-1)
					{
						transactions.add(temp2);
					}
					temp2=data[i][item];
				}
				
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
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

}
