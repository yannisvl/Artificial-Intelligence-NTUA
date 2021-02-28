import java.util.ArrayList;
import java.util.List;

class Node {
    double X, Y;
    int id, lineid;
    String name;
    List<Node> L;
    Node parent;
    double eurist ;
    double fromstart;
    double total, real_dist, real_from_start;
    double rating;

    public double getX() {
        return X;
    }
    public double getY() {
        return Y;
    }
    public void setX(double val) {
        X = val;
    }
    public void setY(double val) {
        Y = val;
    }
    public int getid() {
        return id;
    }
    public void setid(int val) {
        id = val;
    }
    public String getname() {
        return name;
    }
    public void setname(String n) {
        name = n;
    }

    public void setparent(Node val) {
        parent = val;
    }

    public void seteurist(double val) { eurist=val; }
    public void setfromstart(double val) { fromstart = val; }
    public void settotal(double val) {
        total = val;
    }
    public void setL(){L = new ArrayList<>();}
    public void insertneighbor(Node neighbor) {
        L.add(neighbor);
    }




    public String toString() {
        String foo = this.id + " " + this.X + " " + this.Y;
        return foo;
    }


    int compareTo(Node n2){
        if (this.total < n2.total) return -1;
        else if (this.total > n2.total) return 1;
        else return 0;
    }

    int compareTo2(Node n2) {
        if (this.X < n2.X || (this.X == n2.X && this.Y < n2.Y)) return -1;
        else if (this.X > n2.X || (this.X == n2.X && this.Y > n2.Y)) return 1;
        return 0;
    }

    int compareTo3(Node n2){
        if (this.rating > n2.rating) return -1;
        else if (this.rating < n2.rating) return 1;
        else return 0;
    }
}
