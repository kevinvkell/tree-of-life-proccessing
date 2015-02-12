# tree-of-life-proccessing
Generates a tree based on iris data using processing

This doesn't quite work. It does the clustering part, but I never got to the drawing part. 

It does the clustering based on the magnitude of differences between each iris sample. 
It generates a distance by finding the sum of the absoloute value of the differences between
the four measurements for each iris sample. It first makes a cluster out of each measurement, 
then it finds the closest two clusters and merges them. The new cluster has the average of
it's children's measurements. It does this until there is only one cluster. The part I did not 
finish is the part that draws these clusters into a tree. I was going to try to draw the tree
recursively, keeping track of the depth of recursion. I would draw the node based on it's depth. 

I used the algorithm at http://home.deib.polimi.it/matteucc/Clustering/tutorial_html/hierarchical.html

To run, open the TreeOfLife.pde file in processing and press play. 
