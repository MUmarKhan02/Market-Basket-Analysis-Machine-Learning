# Market Basket Analysis - Machine Learning Project

## Project Overview

This project implements a **Market Basket Analysis system** that combines K-means clustering with the Apriori algorithm to identify patterns in sales transactions. The system analyzes customer purchasing behavior to discover frequently bought items and product associations.

### Key Features
- **K-means Clustering**: Groups similar transaction characteristics into clusters for better market segmentation
- **Apriori Algorithm**: Discovers frequent itemsets and association rules from transactions
- **Dunn Index Calculation**: Evaluates cluster quality to determine optimal number of clusters
- **Elbow Method**: Uses inertia analysis to identify the optimal number of clusters
- **Data Visualization**: GUI-based visualization of clusters in 2D space with color-coded points
- **Transaction Processing**: Filters and processes transaction data based on clustering results

## Project Structure

```
Market Basket Analysis/
├── processingkmeans.java      # Main implementation (ACTIVE - Use this)
├── processingnok.java         # Simplified variant (lightweight version)
├── processing.java            # Original version (OUTDATED - Reference only)
├── scanner_data.csv           # Sample transaction dataset
├── Sales Transaction v.4a.csv # Alternative transaction dataset
├── transac.txt                # Generated transaction file (runtime)
├── filename2.txt              # Generated itemsets output (runtime)
└── README.md                  # This file
```

## File Descriptions

### processingkmeans.java ⭐ **RECOMMENDED**
The main and most polished version of the project.

**Functionality:**
- Reads transaction data from CSV files
- Processes and aggregates transaction items
- Performs K-means clustering with multiple iterations (100 runs)
- Calculates Dunn Index for cluster validation
- Determines optimal clusters using elbow method and Dunn Index
- Filters "high-value" transactions based on cluster assignment
- Executes Apriori algorithm on filtered transactions
- Generates GUI visualization showing clustered data points with color coding
- Outputs frequent itemsets to file

**Main Components:**
- `processTransactions()`: Reads and parses CSV data
- `algo()`: K-means clustering algorithm
- `findDunnIndex()`: Cluster quality metric calculation
- `apriori()`: Frequent itemset mining
- `paintComponent()`: 2D GUI visualization
- Output: `transac.txt` (transactions) and frequent itemsets in console

### processingnok.java 
A simplified, lightweight version focusing only on core Apriori functionality.

**Functionality:**
- Does NOT include K-means clustering or visualization
- Directly processes transactions from CSV
- Runs Apriori algorithm with minimum support threshold
- Outputs frequent itemsets to `filename2.txt`
- Ideal for quick apriori-only analysis

**Use Case:** When you only need frequent itemset analysis without clustering

### processing.java ⚠️ **OUTDATED**
The original version (797 lines) - kept for reference and historical purposes.

**Note:** This file contains the same functionality as `processingkmeans.java` but with:
- Less organized code structure
- No package declaration
- Redundant implementations
- More complex variable management

**Recommendation:** Use `processingkmeans.java` instead. This file is archived for comparison only.

---

## Algorithm Explanations

### K-means Clustering
Partitions transaction data into K clusters by:
1. Randomly selecting K initial centroids
2. Assigning points to nearest centroid
3. Recalculating centroids as cluster means
4. Repeating until convergence (100 iterations tested per K value)

**Purpose:** Group transactions with similar revenue patterns and purchase quantities

### Apriori Algorithm
Discovers frequent itemsets and association rules by:
1. Finding items that meet minimum support threshold
2. Generating candidate itemsets of increasing size
3. Pruning candidates that don't satisfy frequency requirements
4. Returning all frequent itemsets with support counts

**Output:** Itemsets sorted by support value (descending)

### Dunn Index
Cluster validity measure calculated as:
```
Dunn Index = (Minimum Inter-Cluster Distance) / (Maximum Intra-Cluster Distance)
```
**Higher values = Better clustering** (well-separated, compact clusters)

### Elbow Method
Identifies optimal clusters by finding the "elbow" point where:
- Inertia reduction rate decreases significantly
- Trade-off between model complexity and fit quality

---

## How to Run

### Prerequisites
- Java 8 or higher
- CSV transaction data file (e.g., `scanner_data.csv` or `itemset1.csv`)

### Steps to Execute

1. **Compile the main file:**
   ```bash
   javac processingkmeans.java
   ```

2. **Run the program:**
   ```bash
   java processingkmeans
   ```

3. **Expected Output:**
   - Console: Cluster analysis results, inertia values, Dunn Index scores
   - `transac.txt`: Filtered transactions used for Apriori
   - `filename2.txt`: Frequent itemsets with support counts
   - GUI Window: 2D visualization with color-coded cluster assignments

### Input Data Format
CSV file with columns: `[TransactionID, Item, Price, Quantity, Date, ...]`

Example:
```
trans_001,Item_A,19.99,2,2024-01-15
trans_001,Item_B,29.99,1,2024-01-15
trans_002,Item_C,9.99,3,2024-01-15
```

---

## Key Configuration Parameters

Edit these in the source code to adjust behavior:

| Parameter | Location | Purpose |
|-----------|----------|---------|
| `numofTransactions` | Line 29 (processingkmeans) | Number of transactions to process |
| `minSup` | Line 408 (processingkmeans) | Minimum support threshold for Apriori |
| `r = 100` | Line 250 (processingkmeans) | Number of K-means iterations per cluster size |
| `Cluster range` | Line 248 | Tests cluster sizes from 2 to 9 |

---

## Output Interpretation

### Console Output Example
```
Number of clusters: 2  Inertia: 1234567.89
Number of clusters: 3  Inertia: 987654.32
...
elbow: 4
4 0.856
5 0.923
```

**Dunn Index values:** Higher is better (0.5-1.0 is typical)

### filename2.txt Output
```
Itemset: [Item_A, Item_B] - Support: 45
Itemset: [Item_C, Item_D, Item_E] - Support: 32
...
```

**Support:** Number of transactions containing the itemset

---

## Project Team & Contributions

- **Apriori Algorithm & Dunn Index:** Implemented core machine learning algorithms
- **K-means Clustering & Visualization:** Implemented clustering and GUI components
- **Code Organization & Refinement:** Final project structure and optimization

---

## Technical Details

### Cluster Quality Metrics
- **Inertia:** Sum of squared distances to nearest cluster center
- **Dunn Index:** Ratio of minimum inter-cluster to maximum intra-cluster distance
- **Elbow Point:** Identifies optimal clusters by analyzing inertia rate of change

### Data Processing Pipeline
1. **Read CSV** → 2. **Parse Transactions** → 3. **Aggregate Items** → 
4. **Normalize Data** → 5. **Apply K-means** → 6. **Evaluate Clusters** → 
7. **Filter Transactions** → 8. **Run Apriori** → 9. **Output Results**




## License

This project is provided as-is for educational purposes.

---

**Last Updated:** May 2026  
**Status:** Complete - Project Submission Version

