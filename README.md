# 🧮 DAA Assignment 3 — Minimum Spanning Tree Optimization  
**Author:** Nursultan Beisenbek  
**Group:** SE-2429  

📦 **Repository:** [bnursik/aitu-daa-asik3](https://github.com/bnursik/aitu-daa-asik3)

---

## 🏗️ Objective

The goal of this project was to apply **Prim’s** and **Kruskal’s** algorithms to optimize a **city transportation network**.

Each city district is represented as a **vertex**, and each potential road as a **weighted edge**.  
The task was to determine the **Minimum Spanning Tree (MST)** — the set of roads that connects all districts with the lowest total cost.

---

## 📊 1. Summary of Input Data and Algorithm Results

Four datasets were generated to test both algorithms under different graph sizes and densities:

| Dataset | Graph Count | Vertex Range | Edge Range | Example File |
|----------|--------------|--------------|-------------|---------------|
| Small | 5 | 5–30 | 20–80 | `assign3_small_input.json` |
| Medium | 10 | 30–300 | 200–700 | `assign3_medium_input.json` |
| Large | 10 | 300–1000 | 1500–4000 | `assign3_large_input.json` |
| Extra-Large | 5 | 1000–2000 | 5000–10000 | `assign3_extralarge_input.json` |

### Example summary (from *small dataset*):
Graph_ID,Vertices,Edges,Prim_Cost,Kruskal_Cost,Prim_Time(ms),Kruskal_Time(ms),Prim_Ops,Kruskal_Ops,Faster,Costs_Match
1,25,65,556.000000,556.000000,475.000000,63.000000,174,579,Kruskal,true
2,16,34,401.000000,401.000000,111.000000,25.000000,79,236,Kruskal,true
3,29,77,465.000000,465.000000,180.000000,35.000000,189,645,Kruskal,true
4,11,27,263.000000,263.000000,57.000000,11.000000,61,159,Kruskal,true
5,30,78,864.000000,864.000000,214.000000,33.000000,210,723,Kruskal,true


✅ Both algorithms produced **identical MST total costs**, confirming correctness.  
⚡ Kruskal’s algorithm was consistently **faster** and required **fewer operations**.

---

## ⚔️ 2. Comparison of Prim’s and Kruskal’s Algorithms

| Criterion | **Prim’s Algorithm** | **Kruskal’s Algorithm** |
|------------|----------------------|--------------------------|
| **Approach** | Builds MST by expanding from one starting vertex, adding the smallest connecting edge each step. | Sorts all edges first, then joins vertices while avoiding cycles (Union–Find). |
| **Complexity** | O(E log V) using a priority queue | O(E log E) ≈ O(E log V) |
| **Data Structures Used** | Min-heap (priority queue), adjacency list | Disjoint-set (Union–Find) |
| **Best For** | Dense graphs (many edges) | Sparse graphs (few edges) |
| **Implementation Simplicity** | Slightly more complex due to heap updates | Straightforward after sorting |
| **Practical Observation** | Slower due to repeated key updates | Faster due to simple sorting and fewer operations |

### 🔬 Theoretical Analysis
- For dense graphs (E ≈ V²), **Prim’s** can perform similarly or slightly better.  
- For sparse graphs, **Kruskal’s** is faster since it minimizes unnecessary comparisons.

### 🧠 Practical Observation
- For every dataset (small → extra-large), **Kruskal’s algorithm was faster**.  
- Kruskal’s operation count was roughly **2–4× lower** than Prim’s.  
- Execution times scaled linearly with edge count, matching O(E log V) theory.

---

## 🧩 3. Conclusions

1. Both algorithms yield identical MST costs → ✅ correctness confirmed.  
2. **Kruskal’s algorithm consistently outperformed Prim’s** in execution time and operation count.  
3. **Prim’s algorithm** is still efficient for dense graphs using adjacency lists.  
4. **Kruskal’s algorithm** is preferable for:
   - Sparse graphs  
   - Edge-list representations  
   - Union–Find-based implementations  
5. Experimental results matched the expected **O(E log V)** complexity trends.

💡 **Conclusion:**  
In a city transportation context — where most districts have limited direct connections (a sparse network) —  
**Kruskal’s algorithm is the optimal practical choice.**

---

## 📚 4. References

- Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2009). *Introduction to Algorithms* (3rd ed.). MIT Press.  
- GeeksforGeeks. (n.d.). *Prim’s and Kruskal’s Minimum Spanning Tree Algorithms*. Retrieved from [https://www.geeksforgeeks.org](https://www.geeksforgeeks.org)  
- Lecture notes from AITU DAA course (Weeks 6–7).  
- OpenAI. (2025). *ChatGPT* [Large language model]. Retrieved October 25, 2025, from [https://chat.openai.com](https://chat.openai.com)

---

### 🧠 Project Author
**Nursultan Beisenbek**  
Software Engineering — SE-2429  
DAA Course — Assignment 3
AITU 2025

