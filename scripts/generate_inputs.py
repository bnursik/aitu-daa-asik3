import json
import os
import random
import string

def random_node_names(v_count):
    # Use letters A, B, C... then AA, AB... if more than 26 nodes
    names = []
    alphabet = string.ascii_uppercase
    for i in range(v_count):
        name = ""
        n = i
        while True:
            name = alphabet[n % 26] + name
            n //= 26
            if n == 0:
                break
        names.append(name)
    return names

def generate_dataset(file_path, num_graphs, min_v, max_v, density_factor, seed=42):
    random.seed(seed)
    os.makedirs(os.path.dirname(file_path), exist_ok=True)

    data = {"graphs": []}

    for i in range(num_graphs):
        v_count = random.randint(min_v, max_v)
        nodes = random_node_names(v_count)
        edges = []

        # Ensure basic connectivity with a spanning tree
        for v in range(1, v_count):
            u = random.randint(0, v - 1)
            w = random.randint(1, 100)
            edges.append({"from": nodes[u], "to": nodes[v], "weight": w})

        # Add random extra edges for density
        extra_edges = v_count * density_factor
        added = set((min(e["from"], e["to"]), max(e["from"], e["to"])) for e in edges)
        for _ in range(extra_edges):
            u, v = random.sample(range(v_count), 2)
            pair = (min(nodes[u], nodes[v]), max(nodes[u], nodes[v]))
            if pair not in added:
                w = random.randint(1, 100)
                edges.append({"from": nodes[u], "to": nodes[v], "weight": w})
                added.add(pair)

        graph_obj = {
            "id": i + 1,
            "nodes": nodes,
            "edges": edges
        }
        data["graphs"].append(graph_obj)

    with open(file_path, "w") as f:
        json.dump(data, f, indent=2)

    print(f"✅ {file_path} created | graphs={num_graphs} | V≈{min_v}-{max_v} | density={density_factor}")

# === Generate all datasets ===
generate_dataset("src/main/resources/data/assign3_small_input.json", 5, 5, 30, 2)
generate_dataset("src/main/resources/data/assign3_medium_input.json", 10, 30, 300, 3)
generate_dataset("src/main/resources/data/assign3_large_input.json", 10, 300, 1000, 5)
generate_dataset("src/main/resources/data/assign3_extralarge_input.json", 5, 1000, 2000, 8)
