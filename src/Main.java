//everything in the main class

import java.io.*;
import java.util.*;

import java.io.IOException;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

public class Main {
    private static String KML="hello";
    private static boolean flag=true;
    private static double lasttotal = 20.0;
    private static TreeSet<Node> opan = new TreeSet<Node>((o1, o2) -> o1.compareTo(o2));
    private static TreeSet<Node> closed = new TreeSet<Node>((o1, o2) -> o1.compareTo(o2));

    private static TreeSet<Integer> opani = new TreeSet<Integer>();
    private static TreeSet<Integer> closedi = new TreeSet<Integer>();

    private static TreeMap<Integer, Node> LeGraph = new TreeMap<Integer, Node>();
    private static TreeMap<Node, Integer> TaxiID = new TreeMap<Node, Integer>((o1, o2) -> o1.compareTo2(o2));

    private static List<Node> targets = new ArrayList<Node>();
    private static ArrayList<Node> L1 = new ArrayList<>();
    private static Node client = new Node();

    private static double epsilon = 0.001;
    private static int sols=0;

    //EUCLIDEAN
    private static double dist(Node i, Node j) {
        return Math.sqrt(Math.pow(i.X-j.X,2) + Math.pow(i.Y-j.Y,2));
    }

    //MANHATTAN
    //private static double dist(Node i, Node j) {return Math.abs(i.X-j.X) + Math.abs(i.Y-j.Y);}

    /*
    //MEAN OF MANHATTAN AND EUCLIDEAN
     */
    //private static double dist(Node i, Node j) {return (eucl(i, j) + manhattan(i, j) )/ 2;}

    private static double mindFromGoals(List<Node> a, Node s) {
        double temp, mintaxi = 100;
        for (Node n : a) {
            temp = dist(n, s);
            if (temp < mintaxi) mintaxi = temp;
        }
        return mintaxi;
    }

    private static boolean output(Node n, int[] arr, int sols, List<Node> targets, ArrayList<Node> L1) {
        if (arr[TaxiID.get(n)] > 0) return false;
        targets.remove(n);
        int sol2 = sols+1;
        System.out.print("Solution " + sol2 + ", Taxi ");
        System.out.println(TaxiID.get(n));
        arr[TaxiID.get(n)]++;
        System.out.print("Path: ");
        while (n!=null) {
            L1.add(n);
            System.out.print(n.id);
            System.out.print(" ");
            n = n.parent;
        }
        if (L1.get(L1.size()-1).id == -1) L1.remove(L1.get(L1.size()-1));
        System.out.println();
        return true;
    }

    private static void printSorted(SortedSet<Node> a){
        int i ;
        for (Node n : a){
            if (n.parent==null) i=0;
            else i=n.parent.id;
            System.out.println(n.id);
        }
        System.out.println();
    }

    private static void printSortedMap(SortedMap<Integer, Node> a){
        int i;
        for (Node n : a.values()){
            if (n.parent==null) i=0;
            else i=n.parent.id;
            System.out.print(("(" + n.id + "," + n.X + "," + n.Y + "," + i + ")"));
            System.out.print(" ");
        }
        System.out.println();
    }


    private static void printArray(List<Node> a){
        for (Node n : a){
            System.out.print("(" + n.id + "," + n.X + "," + n.Y + ")");
            System.out.print(" ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] arr = new int[40];
        String csvFile1 = "nodes.csv";
        String csvFile2 = "taxis.csv";
        String csvFile3 = "client.csv";
        String csvFile4 = "lines.csv";
        String csvFile5 = "traffic.csv";
        BufferedReader br = null;
        BufferedReader br2 = null;
        BufferedReader br3 = null;
        BufferedReader br4 = null;
        BufferedReader br5 = null;
        String line = "";
        String csvSplitBy = ",";

        TreeMap<Pair, Integer> UsedNodes = new TreeMap<Pair, Integer>(new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                return (o1.compareTo(o2));
            }
        });

        TreeMap<Integer, Pair> Roads = new TreeMap<Integer, Pair>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (o1.compareTo(o2));
            }
        });

        TreeMap<Integer, Line> Lines = new TreeMap<Integer, Line>();

        Map<Integer, Pair> LeMap;

        Node n = new Node();
        Node q = new Node();

        for (Node m : LeGraph.values()) {
            m.setL();
        }

        try {

            PrintWriter next = new PrintWriter("facts1.pl", "UTF-8");
            PrintWriter belongs = new PrintWriter("facts2.pl", "UTF-8");
            PrintWriter trafic = new PrintWriter("facts3.pl", "UTF-8");
            PrintWriter odos = new PrintWriter("facts4.pl", "UTF-8");

            PrintWriter writer = new PrintWriter("kml-file.kml", "UTF-8");
            writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<kml xmlns=\"http://earth.google.com/kml/2.1\">\n" +
                    "<Document>\n" +
                    "<name>Taxi Routes</name>\n" +//IF TAB IS NEEDED (char)(9)+
                    "<Style id=\"green\">\n" +
                    "<LineStyle>\n" +
                    "<color>ff009900</color>\n" +
                    "<width>4</width>\n" +
                    "</LineStyle>\n" +
                    "</Style>\n" +
                    "<Style id=\"red\">\n"+
                    "<LineStyle>\n" +
                    "<color>ff0000ff</color>\n" +
                    "<width>4</width>\n" +
                    "</LineStyle>\n" +
                    "</Style>\n");


            //OUR GLOBAL K!!!!!!!!!!
            //OUR GLOBAL K!!!!!!!!!!
            //OUR GLOBAL K!!!!!!!!!!
            //OUR GLOBAL K!!!!!!!!!!
            //OUR GLOBAL K!!!!!!!!!!

            int K = 5;

            //OUR GLOBAL K!!!!!!!!!!
            //OUR GLOBAL K!!!!!!!!!!
            //OUR GLOBAL K!!!!!!!!!!
            //OUR GLOBAL K!!!!!!!!!!
            //OUR GLOBAL K!!!!!!!!!!

            int i=0;

            while (i<40) {
                arr[i] = 0;
                i++;
            }

            Node n3;
            Pair p3;
            int r3, id3;
            double d3,d4;

            br = new BufferedReader(new FileReader(csvFile1));
            br2 = new BufferedReader(new FileReader(csvFile2));
            br3 = new BufferedReader(new FileReader(csvFile3));
            br4 = new BufferedReader(new FileReader(csvFile4));
            br5 = new BufferedReader(new FileReader(csvFile5));
            int szbef=0, szaft=0, curid=1;
            br.readLine();
            br2.readLine();
            br3.readLine();
            br4.readLine();
            br5.readLine();
            Node x = new Node();
            x.setX(0);
            x.setY(0);
            x.id = -1;

            int cnt = 0;

            while ((line = br4.readLine()) != null) {
                if (line.length()==0) break;
                String[] grammi = line.split(csvSplitBy);
                Line l = new Line();
                cnt=0;
                for (String s : grammi) {
                    if (!("".equals(s))) {
                        if (cnt==0) { l.id = Integer.parseInt(s);}
                        else if (cnt==1) { l.highway = s;
                            odos.println("highway(" + l.id + "," + s + ").");}
                        else if (cnt==2) { l.name = s;}
                        else if (cnt==3) { l.oneway = s; }
                        else if (cnt==4) { l.lit = s; }
                        else if (cnt==5) { l.lanes = Integer.parseInt(s);
                            odos.println("lanes(" + l.id +","+ l.lanes+").");}
                        else if (cnt==6) { l.maxspeed = Integer.parseInt(s);
                            odos.println("maxspeed(" + l.id + "," + l.maxspeed + ").");}
                        else if (cnt==7) { l.railway = s;  }
                        else if (cnt==8) { l.boundary = s;  }
                        else if (cnt==9) { l.access = s; }
                        else if (cnt==10) { l.natural = s;  }
                        else if (cnt==11) { l.barrier = s;  }
                        else if (cnt==12) { l.tunnel = s; }
                        else if (cnt==13) { l.bridge = s; }
                        else if (cnt==14) { l.incline = s; }
                        else if (cnt==15) { l.waterway = s;  }
                        else if (cnt==16) { l.busway = s;}
                        else if (cnt==17) { l.toll = s;
                            odos.println("toll(" + l.id + "," + s + ").");}
                    }
                    cnt++;
                }
                Lines.put(l.id, l);
            }


            while ((line = br.readLine()) != null) {

                String[] node = line.split(csvSplitBy);

                d3 = Double.parseDouble(node[0]);
                d4 = Double.parseDouble(node[1]);
                if (!UsedNodes.containsKey(new Pair(d3, d4))) {
                    UsedNodes.put(new Pair(d3, d4), curid);
                }
                szaft = UsedNodes.size();

                Node nod;


                if (szaft > szbef) {
                    nod = new Node();
                    nod.setX(d3);
                    nod.setY(d4);
                    nod.setid(curid);
                    nod.L = new ArrayList<Node>();
                    nod.lineid = Integer.parseInt(node[2]);

                    nod.eurist = 0;
                    nod.real_from_start=0;
                    nod.real_dist=0;
                    nod.fromstart = 0;
                    nod.total = 0;
                    LeGraph.put(curid, nod);
                    szbef = szaft;
                    curid++;
                }

                else {
                    p3 = new Pair(d3,d4);
                    nod = LeGraph.get(UsedNodes.get(p3));
                }

                r3 = Integer.parseInt(node[2]);
                p3 = Roads.get(r3);
                belongs.println("belongsTo(" + nod.id + ", " + r3 + ").");

                if (p3 == null) {
                    Roads.put(r3,new Pair(d3, d4));
                }

                else {
                    id3 = UsedNodes.get(p3);
                    n3 = LeGraph.get(id3);
                    n3.insertneighbor(nod);

                    next.println("next(" + n3.id + ", " + nod.id + ").");

                    nod.insertneighbor(n3);

                    if (Lines.get(r3).oneway!="yes") {

                        next.println("next(" + nod.id + ", " + n3.id + ").");

                    }

                    Roads.put(r3,new Pair(d3, d4));
                }
            }



            line = br3.readLine();
            String[] nodec = line.split(csvSplitBy);
            d3 = Double.parseDouble(nodec[0]);
            d4 = Double.parseDouble(nodec[1]);
            Node nod = new Node();
            nod.setX(d3);
            nod.setY(d4);
            nod.setid(curid);
            nod.L = new ArrayList<Node>();
            nod.eurist=20;
            nod.fromstart=0;
            nod.total=0;

            Node closest=LeGraph.get(1);
            double dif=20, temp;
            for (Node search : LeGraph.values()) {
                temp = dist(search, nod);
                if (temp<dif) {
                    //System.out.print(temp + " ");
                    //System.out.println(search.id);
                    dif = temp;
                    closest = search;
                }
            }

            //System.out.println(closest.id);

            if (dif!=0) {
                nod.L.add(closest);
                LeGraph.put(curid++,nod);
                client = nod;
                //client.parent = x;
                client.setfromstart(0);
                client.settotal(0);
                //System.out.println(client.id);
            }

            else {
                //client.parent = x;
                client = closest;
                client.setfromstart(0);
                client.settotal(0);
            }

            double dest_x = Double.parseDouble(nodec[2]);
            double dest_y = Double.parseDouble(nodec[3]);
            double client_dist = Math.sqrt(Math.pow(d3-dest_x,2) + Math.pow(d4-dest_y,2));
            int persons = Integer.parseInt(nodec[5]);
            String client_lang = nodec[6];
            int luggage = Integer.parseInt(nodec[7]);
            String mytime = nodec[4];
            String[] exacttime = mytime.split(":");

            boolean[] diwra = new boolean[12];

            for (i=0; i<12; i++) diwra[i]=false;


            if (exacttime[0].equals("01") || exacttime[0].equals("02") ) diwra[0]=true;
            else if (exacttime[0].equals("03") || exacttime[0].equals("04") ) diwra[1]=true;
            else if (exacttime[0].equals("05") || exacttime[0].equals("06") ) diwra[2]=true;
            else if (exacttime[0].equals("07") || exacttime[0].equals("08") ) diwra[3]=true;
            else if (exacttime[0].equals("09") || exacttime[0].equals("10") ) diwra[4]=true;
            else if (exacttime[0].equals("11") || exacttime[0].equals("12") ) diwra[5]=true;
            else if (exacttime[0].equals("13") || exacttime[0].equals("14") ) diwra[6]=true;
            else if (exacttime[0].equals("15") || exacttime[0].equals("16") ) diwra[7]=true;
            else if (exacttime[0].equals("17") || exacttime[0].equals("18") ) diwra[8]=true;
            else if (exacttime[0].equals("19") || exacttime[0].equals("20") ) diwra[9]=true;
            else if (exacttime[0].equals("21") || exacttime[0].equals("22") ) diwra[10]=true;
            else if (exacttime[0].equals("23") || exacttime[0].equals("00") ) diwra[11]=true;



            String reg1 = "\\|";
            String reg2 = "=";
            while ((line = br5.readLine()) != null) {
                if (line.length() == 0) break;
                String[] traf = line.split(csvSplitBy);
                if (traf.length == 3) {
                    String[] orar = traf[2].split(reg1);
                    for (String s : orar) {

                        String[] orar2 = s.split(reg2);

                        if (orar2[0].equals("01:00-03:00") && diwra[0]) { Lines.get(Integer.parseInt(traf[0])).traffic[0] = orar2[1];
                            trafic.println("traf(" + traf[0] + ", " + orar2[1] + ").");continue; }
                        if (orar2[0].equals("03:00-05:00") && diwra[1]) { Lines.get(Integer.parseInt(traf[0])).traffic[1] = orar2[1];
                            trafic.println("traf(" + traf[0] + ", " + orar2[1] + ").");continue; }
                        if (orar2[0].equals("05:00-07:00") && diwra[2]) { Lines.get(Integer.parseInt(traf[0])).traffic[2] = orar2[1];
                            trafic.println("traf(" + traf[0] + ", " + orar2[1] + ").");continue; }
                        if (orar2[0].equals("07:00-09:00") && diwra[3]) { Lines.get(Integer.parseInt(traf[0])).traffic[3] = orar2[1];
                            trafic.println("traf(" + traf[0] + ", " + orar2[1] + ").");continue;  }
                        if (orar2[0].equals("09:00-11:00") && diwra[4]) { Lines.get(Integer.parseInt(traf[0])).traffic[4] = orar2[1];
                            trafic.println("traf(" + traf[0] + ", " + orar2[1] + ").");continue;  }
                        if (orar2[0].equals("11:00-13:00") && diwra[5]) { Lines.get(Integer.parseInt(traf[0])).traffic[5] = orar2[1];
                            trafic.println("traf(" + traf[0] + ", " + orar2[1] + ").");continue; }
                        if (orar2[0].equals("13:00-15:00") && diwra[6]) { Lines.get(Integer.parseInt(traf[0])).traffic[6] = orar2[1];
                            trafic.println("traf(" + traf[0] + ", " + orar2[1] + ").");continue; }
                        if (orar2[0].equals("15:00-17:00") && diwra[7]) { Lines.get(Integer.parseInt(traf[0])).traffic[7] = orar2[1];
                            trafic.println("traf(" + traf[0] + ", " + orar2[1] + ").");continue; }
                        if (orar2[0].equals("17:00-19:00") && diwra[8]) { Lines.get(Integer.parseInt(traf[0])).traffic[8] = orar2[1];
                            trafic.println("traf(" + traf[0] + ", " + orar2[1] + ").");continue; }
                        if (orar2[0].equals("19:00-21:00") && diwra[9]) { Lines.get(Integer.parseInt(traf[0])).traffic[9] = orar2[1];
                            trafic.println("traf(" + traf[0] + ", " + orar2[1] + ").");continue; }
                        if (orar2[0].equals("21:00-23:00") && diwra[10]) { Lines.get(Integer.parseInt(traf[0])).traffic[10] = orar2[1];
                            trafic.println("traf(" + traf[0] + ", " + orar2[1] + ").");continue; }
                        if (orar2[0].equals("23:00-01:00") && diwra[11]) { Lines.get(Integer.parseInt(traf[0])).traffic[11] = orar2[1];
                            trafic.println("traf(" + traf[0] + ", " + orar2[1] + ").");continue; }
                    }
                }
            }


            next.close();
            belongs.close();
            trafic.close();
            odos.close();
            JIPEngine jip = new JIPEngine();
            jip.consultFile("facts1.pl");
            jip.consultFile("facts2.pl");
            jip.consultFile("facts3.pl");
            jip.consultFile("facts4.pl");
            jip.consultFile("rules.pl");

            JIPTermParser parser = jip.getTermParser();

            JIPQuery jipQuery;
            JIPTerm term;




            int taxiid=1, num_of_taxis=0;
            String available, capacity, languages, long_distance, type;
            double rate;
            int uplim, downlim, max_lugg;
            String[] cap, lang, typ;

            while ((line = br2.readLine()) != null) {
                String node[] = line.split(csvSplitBy);
                d3 = Double.parseDouble(node[0]);
                d4 = Double.parseDouble(node[1]);
                available = node[3];
                capacity = node[4];
                cap = capacity.split("-");
                downlim =  Integer.parseInt(cap[0]);
                uplim =  Integer.parseInt(cap[1]);
                languages = node[5];
                lang = languages.split("\\|");
                rate = Double.parseDouble(node[6]);
                long_distance = node[7];
                type = node[8];

                typ = type.split("\t");
                type = typ[0];
                if (type.equals("subcompact")) max_lugg = 2;
                else if (type.equals("compact")) max_lugg = 3;
                else if (type.equals("large")) max_lugg = 5;
                else max_lugg = 4;

                //System.out.println(typ[0] + " " + lang[0] + " " + downlim + " " + uplim + " " + long_distance + " " + rating + " " + available + " " + max_lugg);

                Node nodc = new Node();
                nodc.setX(d3);
                nodc.setY(d4);
                nodc.setid(curid);
                nodc.L = new ArrayList<Node>();
                nodc.eurist=0;
                nodc.fromstart=0;
                nodc.total=0;
                nodc.real_dist=0;
                nodc.real_from_start=0;
                dif = 20.0;



                for (Node search : LeGraph.values()) {
                    temp = dist(search, nodc);
                    if (temp<dif) {
                        dif = temp;
                        closest = search;
                    }
                }

                if (dif!=0) {
                    nodc.L.add(closest);
                    closest.L.add(nodc);
                    LeGraph.put(curid++, nodc);
                    if (!(persons < downlim || persons > uplim || available.equals("no") || !Arrays.asList(lang).contains(client_lang) || luggage > max_lugg || (client_dist>0.06 && long_distance.equals("no")))){
                        nodc.rating=rate;
                        targets.add(nodc);
                        num_of_taxis ++;
                    }
                    TaxiID.put(nodc, taxiid);
                }

                else {
                    if (!(persons < downlim || persons > uplim || available.equals("no") || !Arrays.asList(lang).contains(client_lang) || luggage > max_lugg || (client_dist>0.06 && long_distance.equals("no")))){
                        closest.rating=rate;
                        targets.add(closest);
                        num_of_taxis ++;
                    }
                    TaxiID.put(closest,taxiid);
                }
                taxiid++;
            }

            System.out.println("Number of accepted taxis : " + num_of_taxis);
            System.out.println(client_dist);

            System.out.println("");
            opani.add(client.id);
            opan.add(client);

            boolean ended=false;


            String myDist;
            double mintax, new_dist;
            int newind=0;

            if (K>num_of_taxis){
                K = num_of_taxis;
            }

            System.out.println("K = " + K);

            System.out.println("first k accepted taxis sorted by rating in decreasing order");

            Collections.sort(targets,Node::compareTo3);
            for (Node w : targets) {
                System.out.println("Taxi " + TaxiID.get(w) + " with rating " + w.rating);
            }
            System.out.println();



            while (K>0) {
                int remainingtaxis=0;
                if (targets.isEmpty()) {
                    for(Map.Entry<Node,Integer> entry : TaxiID.entrySet()) {
                        if (arr[entry.getValue()] == 0) {
                            arr[entry.getValue()]++;
                            targets.add(entry.getKey());
                            if (++remainingtaxis == K) break;
                        }
                    }
                }
                while (!opan.isEmpty()) {

                    q = opan.first();

                    opani.remove(q.id);

                    opan.pollFirst();

                    closedi.add(q.id);
                    closed.add(q);

                    for (Node element : (q.L)) {

                        jipQuery = jip.openSynchronousQuery(parser.parseTerm("check_1(" + q.id + "," + element.id + ",Z)."));
                        term = jipQuery.nextSolution();
                        if (term != null) {
                            //System.out.println(q.id + " " + element.id + " " );
                            myDist = term.getVariablesTable().get("Z").toString();
                            //System.out.println(myDist);
                            new_dist = Double.parseDouble(myDist);
                        } else {
                            new_dist=1;
                        }

                        if (targets.contains(element)) {
                            element.parent = q;
                            element.fromstart = q.fromstart + new_dist*dist(element, q);
                            element.real_from_start = q.real_from_start + dist(element, q);
                            if ((element.fromstart < lasttotal) && flag) {
                                flag=false;
                                lasttotal = element.fromstart;
                            }
                            element.total = element.fromstart;
                            element.real_dist = element.real_from_start;
                            if (output(element,arr,sols,targets,L1)) {
                                sols++;
                                K--;
                                System.out.print("Weighted Distance: ");
                                System.out.println(element.total);
                                System.out.print("Real Distance: ");
                                System.out.println(element.real_dist);
                                System.out.println();
                                if (K==0) {
                                    writer.print("<Placemark>\n" +
                                            "<name>Route " + sols + " - Taxi " + (TaxiID.get(L1.get(newind))) + "</name>\n" +
                                            "<styleUrl>#red</styleUrl>\n" +
                                            "<LineString>\n" +
                                            "<altitudeMode>relative</altitudeMode>\n" +
                                            "<coordinates>\n");
                                    Node no=L1.get(newind);
                                    while (newind < L1.size()) {
                                        no = L1.get(newind);
                                        writer.print(no.getX() + "," + no.getY() + ",0\n");
                                        newind++;
                                    }
                                    writer.print("</coordinates>\n" +
                                            "</LineString>\n" +
                                            "</Placemark>\n");
                                    ended=true;
                                    break;
                                }
                            }
                        if (sols == 1) {
                            writer.print("<Placemark>\n" +
                                    "<name>Route " + sols + " - Taxi " + TaxiID.get(L1.get(newind)) + "</name>\n" +
                                    "<styleUrl>#green</styleUrl>\n" +
                                    "<LineString>\n" +
                                    "<altitudeMode>relative</altitudeMode>\n" +
                                    "<coordinates>\n");
                            Node no=L1.get(newind);
                            while (newind < L1.size()) {
                                no = L1.get(newind);
                                writer.print(no.getX() + "," + no.getY() + ",0\n");
                                newind++;
                            }
                            writer.print("</coordinates>\n" +
                                    "</LineString>\n" +
                                    "</Placemark>\n");
                        }
                        else {
                            writer.print("<Placemark>\n" +
                                    "<name>Route " + sols + " - Taxi " + (TaxiID.get(L1.get(newind))) + "</name>\n" +
                                    "<styleUrl>#red</styleUrl>\n" +
                                    "<LineString>\n" +
                                    "<altitudeMode>relative</altitudeMode>\n" +
                                    "<coordinates>\n");
                            Node no=L1.get(newind);
                            while (newind < L1.size()) {
                                no = L1.get(newind);
                                writer.print(no.getX() + "," + no.getY() + ",0\n");
                                newind++;
                            }
                            writer.print("</coordinates>\n" +
                                    "</LineString>\n" +
                                    "</Placemark>\n");
                        }
                        }
                        element.fromstart = q.fromstart + new_dist*dist(element, q);
                        element.real_from_start = q.real_from_start + dist(element, q);
                        mintax = mindFromGoals(targets, element);
                        temp = mintax + element.fromstart;


                        if (closedi.contains(element.id) && (q.id == element.id)) continue;

                        if (opani.contains(element.id)) {
                            if (element.total < temp) {
                                continue;
                            }
                        }
                        if (closedi.contains(element.id)) {

                            if (element.total < temp) {
                                continue;
                            }
                        }
                        element.parent = q;
                        element.eurist = mintax;
                        element.total = temp;
                        opan.add(element);
                        opani.add(element.id);
                    }
                    if (ended) break;
                }
            }



            writer.println("</Document>");
            writer.println("</kml>");
            writer.close();


        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } finally{
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
