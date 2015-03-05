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

    String toString() {
        return traits.toString() + 
        "\n\t" + (child1 == null ? "" : child1.toString()) +
        "\n\t" + (child2 == null ? "" : child2.toString());

    }
}
