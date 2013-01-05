diagPr diagPr;
String[] ls = {
  "1:-30", "2:-25", "3:-20", "4:-15", "5:-10", "6:-5", "7:0", "8:5", "9:10", "10:15", "11:20", "12:25", "13:30"
};

void setup() {
  size(800,600);
  frameRate(1);
  background(255, 255, 204);
  diagPr = new diagPr(20,300);
  diagPr.load();
}

void draw() {
  diagPr.display();
}

class diagPr {
  int x, y;
  String[] list, user;
  int mn, mx;
  diagPr(int x1,int y1) {
    x = x1;
    y = y1;
  }
  void load() {
    try {
      user = loadStrings("http://star003.dlinkddns.com/05.php");
      list = splitTokens(user[0], "<br>");
    }
    catch (Exception e) {
      list = ls;
    }
    //println(trend(list));
    String g1 = trend(list);
    String[] g2 = splitTokens(g1, ":");
    mx = int(g2[0]);
    mn = int(g2[2]);
  } 
  void display() {
    int k=vert(mx);
    int sj = 12; //сжатие диаграммы
    for (int i = 0; i<list.length; i++) {   
      String g = list[list.length-i-1];
      String tm[] = splitTokens(g, ":");
      fill(0, 102, 51);
      rect(sj*(1+i), y, 5, int(float(tm[2])*-k));
      fill(0, 0, 102);
      
      rect(sj*(1+i), y, 5, int(float(tm[1])*-k));
      fill(102, 0, 0);
      
      if (i%2!=0) {
        fill(56,86,232);
        pushMatrix();
        translate(0,-20);
        rotate(radians(90));
        if (float(tm[2])>0) text(tm[2], sj*(1+i), y-10+float(tm[2])*-k);
        else text(tm[2], sj*(1+i), y+20+float(tm[2])*-k);
        
        popMatrix();
        fill(67,193,39);
        /*
        pushMatrix();
        if (float(tm[2])>0){
          translate(sj*(i+1), y+25);
          rotate(radians(90));
          text(tm[0], sj*(i+1), y+25);
        }
        else {
          translate(sj*(i+1), y-25);
          rotate(radians(90));
          text(tm[0], sj*(i+1), y-25);
        }
        popMatrix();
        */
      }
      
      line(x,y,sj*(1+i),y);
    }
    
  }
  String trend(String[] aa) {
    //найдем минимумы и максимумы в массиве и их позиции
    String t1[] = splitTokens(aa[0], ":");
    int mn=int(t1[2])*10;
    int mx=int(t1[2])*10;
    int pmn = 0;
    int pmx = 0;
    for (int i=0 ;i<aa.length;i++) {
      String t[] = splitTokens(aa[i], ":");
      if (int(t[2])*10 >mx) {
        mx = int(t[2])*10;
        pmx = i;
      } 
      if (int(t[2])*10 <mn) {
        mn = int(t[2])*10;
        pmn = i;
      }
    }
    return str(mx)+":"+str(pmx)+":"+str(mn)+":"+str(pmn);
  }
  int vert(int m) {
    int h = abs(m);
    //println(m);
    if (h >=0 && h<=25) return 50;
    else if (h >26 && h<100) return 10; 
    else if (h >101) return 5;
   return 10; 
  }
}


