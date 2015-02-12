int height = 500;
int width = 1000;

Table table;
ArrayList<Cluster> clusters;

void setup() {
    size(width, height);
    table = loadTable("iris_data2.txt", "header, tsv");
    clusters = new ArrayList<Cluster>();

    for(TableRow row : table.rows()) {
        clusters.add(new Cluster(new Traits(row)));
    }

    while(clusters.size() > 1) {
        combineClosestCluster();
    }
}

// void drawTree(Cluster node, int depth, int x, int y) {
//     ellipse(x, y, 5, 5);
//     if(node.child1 != null) {
//         int newHeight = max(height - y)
//         drawTree(node.child1, depth + 1, )
//     }
// }

boolean isLeaf(Cluster cluster) {

    if(cluster.name == null) {
        return false;
    }

    return true;
}

void combineClosestCluster() {
    float shortestDistance = null;
    Cluster cluster1;
    Cluster cluster2;

    for(int i=0; i<clusters.size(); i++) {
        for(int j=i+1; j<clusters.size(); j++) {
            if(shortestDistance = null) {
                cluster1 = clusters.get(i);
                cluster2 = clusters.get(j);
                continue;
            }

            float distance = calculateDistance(clusters.get(i), clusters.get(j)) < shortestDistance;
            if(distance < shortestDistance) {
                shortestDistance = distance;
                cluster1 = clusters.get(i);
                cluster2 = clusters.get(j);
            }
        }
    }

    clusters.remove(cluster1);
    clusters.remove(cluster2);
    clusters.add(new Cluster(averageTraits(cluster1.traits, cluster2.traits), cluster1, cluster2));
}

Traits averageTraits(Traits traits1, Traits traits2) {
    float sepalLength = (traits1.sepalLength + traits2.sepalLength)/2;
    float sepalWidth = (traits1.sepalWidth + traits2.sepalWidth)/2;
    float petalLength = (traits1.petalLength + traits2.petalLength)/2;
    float petalWidth = (traits1.petalWidth + traits2.petalWidth)/2;

    return new Traits(sepalLength, sepalWidth, petalLength, petalWidth);
}

float calculateDistance(Traits iris1, Traits iris2) {
    float sum = 0;

    sum += abs(iris1.traits.sepalLength - iris2.traits.sepalLength);
    sum += abs(iris1.traits.sepalWidth - iris2.traits.sepalWidth);
    sum += abs(iris1.traits.petalLength - iris2.traits.petalLength);
    sum += abs(iris1.traits.petalWidth - iris2.traits.petalWidth);

    return sum;
}

class Traits {
    float sepalLength;
    float sepalWidth;
    float petalLength;
    float petalWidth;
    String name = null;

    Traits(TableRow row) {
        sepalLength = row.getFloat("sepal_length");
        sepalWidth = row.getFloat("sepal_width");
        petalLength = row.getFloat("petal_length");
        petalWidth = row.getFloat("petal_width");
        name = row.getString("sample_number");
    }

    Traits(float newSepalLength, float newSepalWidth, float newPetalLength, float newPetalWidth) {
        sepalLength = newSepalLength;
        sepalWidth = newSepalWidth;
        petalLength = newPetalLength;
        petalWidth = newPetalWidth;
    }
}

class Cluster {
    Traits traits;
    Cluster child1;
    Cluster child2;

    Cluster(Traits newTraits) {
        traits = newTraits;
        child1 = null;
        child2 = null;
    }

    Cluster(Traits newTraits, Cluster newChild1, Cluster newChild2) {
        traits = newTraits;
        child1 = newChild1;
        child2 = newChild2;
    }
}
