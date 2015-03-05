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

public void draw() {

}

public void drawTree(Cluster cluster) {
    fill(255);
    rect(0, 0, sketchWidth(), sketchHeight());

    drawLines(cluster);
    drawCluster(cluster);
}

public void mouseClicked() {
    print("(", mouseX, ", ", mouseY, ")\n");
    drawTree(clusters.get(0));
    drawInfo(clusters.get(0), mouseX, mouseY);
}

public void drawInfo(Cluster cluster, int x, int y) {
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
    return PApplet.parseInt(displayWidth * 0.8f);
} 

public int sketchHeight() {
    return PApplet.parseInt(displayHeight * 0.8f);
}

public void drawCluster(Cluster cluster) {
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

public void drawLines(Cluster cluster) {
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
        "\t" + (child2 == null ? "" : child2.toString());

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
        name = row.getString("class");
    }

    Traits(float newSepalLength, float newSepalWidth, float newPetalLength, float newPetalWidth) {
        sepalLength = newSepalLength;
        sepalWidth = newSepalWidth;
        petalLength = newPetalLength;
        petalWidth = newPetalWidth;
    }

    public String toString() {
        return "Sepal Length: " + (float)(Math.round(sepalLength*100.0f)/100.0f) + " "
            + "Sepal Width: " + (float)(Math.round(sepalWidth*100.0f)/100.0f) + " "
            + "Petal Length: " + (float)(Math.round(petalLength*100.0f)/100.0f) + " "
            + "Petal Width: " + (float)(Math.round(petalWidth*100.0f)/100.0f) + " "
            + (name==null ? "" : "Type: " + name);
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
