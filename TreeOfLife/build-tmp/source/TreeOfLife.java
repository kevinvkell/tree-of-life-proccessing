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

int height = 500;
int width = 1000;

Table table;

public void setup() {
    size(width, height);
    table = loadTable("iris_data2.txt", "header, tsv");

    for(TableRow row : table.rows()) {
        print(row.getString("sample_number"), '\n');
    }
}

public void draw() {

}

public void calculateDistances() {

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
