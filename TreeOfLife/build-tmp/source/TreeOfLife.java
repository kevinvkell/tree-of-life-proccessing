import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class TreeOfLife extends PApplet {

Table table;
ArrayList<Cluster> clusters;

public void setup() {
    size(sketchWidth(), sketchHeight());
    table = loadTable("iris_data2.txt", "header, tsv");
    clusters = new ArrayList<Cluster>();

    for(TableRow row : table.rows()) {
        clusters.add(new Cluster(new Traits(row)));
    }

    while(clusters.size() > 1) {
        combineClosestCluster();
    }

    assignY(clusters.get(0), sketchHeight()/2, 0);
    assignX(clusters.get(0), 0, sketchWidth()/findDepth(clusters.get(0)));

    drawCluster(clusters.get(0));
}

public int sketchWidth() {
    return 2000;
    // return int(displayWidth * 0.8);
} 

public int sketchHeight() {
    return 1000;
    // return int(displayHeight * 0.8);
}

public void drawCluster(Cluster cluster) {
    if(cluster == null) {
        return;
    }

    print("(", cluster.x, ", ", cluster.y, ")\n");

    fill(123);
    ellipse(cluster.x, cluster.y, 5, 5);
    drawCluster(cluster.child1);
    drawCluster(cluster.child2);
}

public void assignY(Cluster cluster, int altitude, int parentAltitude) {
    if(cluster == null) {
        return;
    }

    cluster.y = altitude;
    assignY(cluster.child1, altitude + abs(parentAltitude - altitude)/2, altitude);
    assignY(cluster.child2, altitude - abs(parentAltitude - altitude)/2, altitude);
}

public void assignX(Cluster cluster, int xPos, int interval) {
    if(cluster == null) {
        return;
    }

    cluster.x = xPos;
    assignX(cluster.child1, xPos + interval, interval);
    assignX(cluster.child2, xPos + interval, interval);
}

public int findDepth(Cluster cluster) {
    if(cluster == null) {
        return 0;
    }

    return 1 + max(findDepth(cluster.child1), findDepth(cluster.child2));
}

public int farthestUp(Cluster cluster) {
    if(cluster == null) {
        return 0;
    }

    return 1 + farthestUp(cluster.child1);
}

public int farthestDown(Cluster cluster) {
    if(cluster == null) {
        return 0;
    }

    return 1 + farthestDown(cluster.child1);
}

public void combineClosestCluster() {
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

public Traits averageTraits(Traits traits1, Traits traits2) {
    float sepalLength = (traits1.sepalLength + traits2.sepalLength)/2;
    float sepalWidth = (traits1.sepalWidth + traits2.sepalWidth)/2;
    float petalLength = (traits1.petalLength + traits2.petalLength)/2;
    float petalWidth = (traits1.petalWidth + traits2.petalWidth)/2;

    return new Traits(sepalLength, sepalWidth, petalLength, petalWidth);
}

public float calculateDistance(Traits iris1, Traits iris2) {
    float sum = 0;

    sum += abs(iris1.sepalLength - iris2.sepalLength);
    sum += abs(iris1.sepalWidth - iris2.sepalWidth);
    sum += abs(iris1.petalLength - iris2.petalLength);
    sum += abs(iris1.petalWidth - iris2.petalWidth);

    return sum;
}
class Cluster {
    Traits traits;
    Cluster child1;
    Cluster child2;
    int x;
    int y;

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

    public String toString() {
        return traits.toString() + 
        "\n\t" + (child1 == null ? "" : child1.toString()) +
        "\n\t" + (child2 == null ? "" : child2.toString());

    }
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

    public String toString() {
        return "Sepal Length: " + sepalLength + "\t"
            + "Sepal Width: " + sepalWidth + "\t"
            + "Petal Length: " + petalLength + "\t"
            + "Petal Width: " + petalWidth + "\t\n";
    }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "TreeOfLife" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
