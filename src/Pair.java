class Pair {
    double x,y;
    Pair(double x, double y) {
        this.x = x;
        this.y = y;
    }
    int compareTo(Pair p2) {
        if ((this.x < p2.x) || (this.x == p2.x && this.y < p2.y)) return -1;
        else if (this.x > p2.x || (this.x == p2.x && this.y > p2.y)) return 1;
        return 0;
    }
    boolean equals(Pair p2) {
        return (this.x == p2.x && this.y == p2.y);
    }

    public String toString() {
        String foo = this.x + "," + this.y;
        return foo;
    }
}
