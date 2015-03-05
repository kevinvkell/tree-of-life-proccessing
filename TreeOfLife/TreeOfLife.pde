Table table;
ArrayList<Cluster> clusters;

void setup() {
    size(sketchWidth(), sketchHeight());
    table = loadTable("iris_data_reduced.txt", "header, tsv");
    clusters = new ArrayList<Cluster>();

    for(TableRow row : table.rows()) {
        clusters.add(new Cluster(new Traits(row)));
    }

    while(clusters.size() > 1) {
        combineClosestCluster();
    }

    assignY(clusters.get(0), sketchHeight()/2, 0);
    assignX(clusters.get(0), 0, sketchWidth()/findDepth(clusters.get(0)));

    drawTree(clusters.get(0));
}

void draw() {

}

void drawTree(Cluster cluster) {
    fill(255);
    rect(0, 0, sketchWidth(), sketchHeight());

    drawLines(cluster);
    drawCluster(cluster);
}

void mouseClicked() {
    print("(", mouseX, ", ", mouseY, ")\n");
    drawTree(clusters.get(0));
    drawInfo(clusters.get(0), mouseX, mouseY);
}

void drawInfo(Cluster cluster, int x, int y) {
    if(cluster == null) {
        return;
    }

    if(dist(cluster.x, cluster.y, x, y) < 10) {
        print("hit!\n");
        fill(0);
        textSize(10);
        text(cluster.traits.toString(), cluster.x, cluster.y, 100, 80);
    }
    else {
        drawInfo(cluster.child1, x, y);
        drawInfo(cluster.child2, x, y);
    }
}

public int sketchWidth() {
    return int(displayWidth * 0.8);
} 

public int sketchHeight() {
    return int(displayHeight * 0.8);
}

void drawCluster(Cluster cluster) {
    if(cluster == null) {
        return;
    }

    String name = cluster.traits.name;
    if(name == null) {
        fill(123);
    } else if(name.equals("Iris-virginica")) {
        fill(228,26,28);
    } else if(name.equals("Iris-setosa")) {
        fill(55,126,184);
    } else if(name.equals("Iris-versicolor")) {
        fill(77,175,74);
    }


    ellipse(cluster.x, cluster.y, 10, 10);
    drawCluster(cluster.child1);
    drawCluster(cluster.child2);
}

void drawLines(Cluster cluster) {
    stroke(123);
    if(cluster.child1 != null) {
        line(cluster.x, cluster.y, cluster.child1.x, cluster.child1.y);
        drawLines(cluster.child1);
    }
    if(cluster.child2 != null) {
        line(cluster.x, cluster.y, cluster.child2.x, cluster.child2.y);
        drawLines(cluster.child2);
    }
}

void assignY(Cluster cluster, int altitude, int parentAltitude) {
    if(cluster == null) {
        return;
    }

    cluster.y = altitude;
    assignY(cluster.child1, altitude + abs(parentAltitude - altitude)/2, altitude);
    assignY(cluster.child2, altitude - abs(parentAltitude - altitude)/2, altitude);
}

void assignX(Cluster cluster, int xPos, int interval) {
    if(cluster == null) {
        return;
    }

    cluster.x = xPos;
    assignX(cluster.child1, xPos + interval, interval);
    assignX(cluster.child2, xPos + interval, interval);
}

int findDepth(Cluster cluster) {
    if(cluster == null) {
        return 0;
    }

    return 1 + max(findDepth(cluster.child1), findDepth(cluster.child2));
}

int farthestUp(Cluster cluster) {
    if(cluster == null) {
        return 0;
    }

    return 1 + farthestUp(cluster.child1);
}

int farthestDown(Cluster cluster) {
    if(cluster == null) {
        return 0;
    }

    return 1 + farthestDown(cluster.child1);
}

void combineClosestCluster() {
    float shortestDistance = -1;
    Cluster minCluster1 = null;
    Cluster minCluster2 = null;

    for(Cluster cluster1 : clusters) {
        for(Cluster cluster2 : clusters) {
            if(cluster1 == cluster2) {
                continue;
            }
            if(shortestDistance == -1) {
                minCluster1 = cluster1;
                minCluster2 = cluster2;
                shortestDistance = calculateDistance(cluster1.traits, cluster2.traits);
                continue;
            }
            float distance = calculateDistance(cluster1.traits, cluster2.traits);
            if(distance < shortestDistance) {
                shortestDistance = distance;
                minCluster1 = cluster1;
                minCluster2 = cluster2;
            }
        }
    }

    clusters.remove(minCluster1);
    clusters.remove(minCluster2);

    clusters.add(new Cluster(averageTraits(minCluster1.traits, minCluster2.traits), minCluster1, minCluster2));
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

    sum += abs(iris1.sepalLength - iris2.sepalLength);
    sum += abs(iris1.sepalWidth - iris2.sepalWidth);
    sum += abs(iris1.petalLength - iris2.petalLength);
    sum += abs(iris1.petalWidth - iris2.petalWidth);

    return sum;
}
