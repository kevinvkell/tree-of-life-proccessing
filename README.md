# tree-of-life-proccessing
Generates a tree based on iris data using processing

##Changes
I've changed this project in that before it did not work at all, but now it works pretty well.
I had to change the way it clustered the nodes since before there was an issue where it was not
actually grouping similar clusters. I also added a method of traversing the resulting clusters
and determining the x and y coordinates of each node. Using that data, I draw the nodes and connect
them with lines. 

##Use
<code>TreeOfLife.pde</code> reads the file named <code>iris_data_reduced.txt</code> and generates nodes based
on each entry's data. It clusters the nodes together with other nodes with similar attributes.
I read from this file, because I found that my method of drawing the tree works best 
for smaller data sets than I was working with originally. The original data set exists in the other two file
in the Data folder. The terminal nodes are labeled different colors based on the type of iris they are. 
The tree shows that irises of the same type are grouped more closely together. By clicking on a node, a user
can see the attributes at that node. 
