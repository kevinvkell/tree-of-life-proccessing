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

    String toString() {
        return "Sepal Length: " + (float)(Math.round(sepalLength*100.0)/100.0) + " "
            + "Sepal Width: " + (float)(Math.round(sepalWidth*100.0)/100.0) + " "
            + "Petal Length: " + (float)(Math.round(petalLength*100.0)/100.0) + " "
            + "Petal Width: " + (float)(Math.round(petalWidth*100.0)/100.0) + " ";
    }
}