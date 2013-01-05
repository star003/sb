 diagPr diagPr;
 button b1,b2;
  int sdvig = 30;
  int sd =0;
 PFont f;
 String[] ls = {
  "1:-30:-25", "2:-25:-20", "3:-20:-15", 
  "4:-15:-10", "5:-10:-5", "6:-5:0", 
  "7:0:5", "8:5:10", "9:10:15", 
  "10:15:20", "11:20:25", "12:25:30", 
  "13:30:35"
 };

void setup() {
  size(800,500);
  frameRate(1);
  background(255, 255, 204);
  diagPr = new diagPr(20,300);
  diagPr.load();
  f = loadFont("Courier-12.vlw");
  textFont(f,12);
  b1 = new button(725,25 ,loadImage("r.png"));
  b2 = new button(625,25 ,loadImage("l.png"));
}

void draw() {
  background(255, 255, 204);
  b1.display();
  b2.display();
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
    int i=-1;
    int k=vert(mx);
    int sj = 25; //сжатие диаграммы 
    int kz =sdvig;//list.length;
    if (kz>list.length-1) kz=list.length-1;
    if (kz<30) kz=30;
    int nz = kz-30;
    
    for (int j = nz; j<kz; j++) {
      i++;   
      String g = list[kz-i-1];
      String tm[] = splitTokens(g, ":");
	  
	  if(float(tm[2])>0){
		fill(153,255,51);
		rect(sj*(1+i), y, 20, int(float(tm[2])*-k));
		fill(0, 0, 102);
		rect(sj*(1+i), y, 20, int(float(tm[1])*-k));
	  }
	  else {
		fill(0, 0, 102);
		rect(sj*(1+i), y, 20, int(float(tm[1])*-k));
		fill(153,255,51);
		rect(sj*(1+i), y, 20, int(float(tm[2])*-k));
	  }
	  
        fill(56,86,232);
        pushMatrix();
        translate(0,-20);
        rotate(radians(90));
			if (float(tm[2])>0) {
				text(tm[2]+"/"+tm[1], y-45+float(tm[2])*-k,sj*(1-i)-55); 
			}
			else {
				text(tm[2]+"/"+tm[1], y+50+float(tm[2])*-k,sj*(1-i)-55);
			}
        
        popMatrix();
        fill(67,193,39);
        
        pushMatrix();
        if (float(tm[2])>0){
          translate(0,-20);
          rotate(radians(90));
          text(tm[0], y+30,sj*(1-i)-55);
        }
        else {
          translate(0,-20);
          rotate(radians(90));
          text(tm[0], y-60,sj*(1-i)-55);
        }
        popMatrix();
      line(x,y,sj*(1+i),y);
    }
    i=0;
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
    if (h >=0 && h<=25) return 100;
    else if (h >26 && h<100) return 20; 
    else if (h >101) return 5;
   return 10; 
  }
}

class button {
 PImage img;
 int x,y;
  button(int x1,int y1,PImage img1) {
   x = x1;
   y = y1;
   img = img1;
  } 
  void display() {
   image(img,x,y,50,50); 
  }
  
  boolean press() {
    if (mouseX>=x && mouseX <= x+50 
      && mouseY>=y && mouseY <= y+50) {
      return true;
    }
    return false;
  }
}
void mousePressed() {
  if (b1.press() == true) sdvig-=30;
  if (b2.press() == true) sdvig+=30;
}
