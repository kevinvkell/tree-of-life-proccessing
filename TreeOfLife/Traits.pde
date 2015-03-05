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

    String toString() {
        return "Sepal Length: " + sepalLength + "\t"
            + "Sepal Width: " + sepalWidth + "\t"
            + "Petal Length: " + petalLength + "\t"
            + "Petal Width: " + petalWidth + "\t\n";
    }
}