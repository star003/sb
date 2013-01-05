import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class miniPrognoz extends PApplet {

////\u043e\u043f\u0438\u0441\u0430\u043d\u0438\u0435 \u043c\u043e\u0438\u0445 \u043e\u0431\u044a\u0435\u043a\u0442\u043e\u0432
diagPr diagPr;
myGis myGis;
button b1;
prognozGis prognozGis;
getPogoda getPogoda;
//\u0442\u0430\u0439\u043c\u0435\u0440\u044b
int m01=0;
int m02=600000;
//\u0437\u0430\u0433\u043b\u0443\u0448\u043a\u0430 \u043f\u0440\u0438 \u043f\u043e\u0442\u0435\u0440\u0435 \u0441\u0432\u044f\u0437\u0438
String[] ls = {
  "1:-30", "2:-25", "3:-20", "4:-15", "5:-10", "6:-5", "7:0", "8:5", "9:10", "10:15", "11:20", "12:25", "13:30"
}; 
//\u0443\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u0435 \u044d\u043a\u0440\u0430\u043d\u043e\u043c \u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e 1\u0439
byte sr = 1;
PImage pr, nx;
PFont f;

public void setup() {
  size(500, 350); 
  f = loadFont("Courier-12.vlw");

  frameRate(5);
  background(255, 255, 204);
  diagPr = new diagPr();
  diagPr.y = 180;
  diagPr.load();
  diagPr.display();
  myGis = new myGis();
  myGis.load();
  myGis.x = 10;
  myGis.y = 20;
  
    nx = loadImage("Next.png");
  
  b1 = new button(400,250,nx);
  
  prognozGis = new prognozGis();
  prognozGis.x=20;
  prognozGis.y=20;
  prognozGis.load();

  getPogoda = new getPogoda();
  getPogoda.x = 20;
  getPogoda.y = 20;
  getPogoda.load();
}

public void draw() {
  //\u043a\u0430\u0436\u0434\u044b\u0435 10 \u043c\u0438\u043d\u0443\u0442 \u043f\u0435\u0440\u0435\u0433\u0440\u0443\u0436\u0430\u0435\u043c \u0434\u0430\u043d\u043d\u044b\u0435
  
  if (millis()-m01 >m02) {
    m01 = millis();
    diagPr.load();
    myGis.load();
    prognozGis.load();
    getPogoda.load();
  }
  if (sr>3) sr=1;
  //\u043e\u0442\u043e\u0431\u0440\u0430\u0436\u0435\u043d\u0438\u0435 \u0434\u0430\u043d\u043d\u044b\u0445 \u043d\u0430 \u044d\u043a\u0440\u0430\u043d\u0435
  if (sr==1 ) {
    background(255, 255, 204);
      textFont(f,12);
    b1.display();
    diagPr.display();
    myGis.y = 20;
    myGis.display();
  }
  else if (sr==2) {
    textFont(f,16);
    background(255, 255, 204);
    b1.display();
    //b2.display();
    prognozGis.display();
  }
  else if (sr==3) {
    background(255, 255, 204);
    textFont(f,16);
    b1.display();
    //b2.display();
    getPogoda.display();
  }
}

class diagPr {
  int x, y;
  String[] list, user;
  int mn, mx;
  public void load() {
    try {
      user = loadStrings("http://star003.dlinkddns.com/04.php");
      list = splitTokens(user[0], "<br>");
    }
    catch (Exception e) {
      list = ls;
    }
    //println(trend(list));
    String g1 = trend(list);
    String[] g2 = splitTokens(g1, ":");
    mx = PApplet.parseInt(g2[0]);
    mn = PApplet.parseInt(g2[2]);
  } 
  public void display() {
    int k=10;
    int sj = 15; //\u0441\u0436\u0430\u0442\u0438\u0435 \u0434\u0438\u0430\u0433\u0440\u0430\u043c\u043c\u044b
    if (PApplet.parseInt(mx) <=150) k = 10;
    if (PApplet.parseInt(mx) >=151) k = 5;  
    for (int i = 0; i<list.length; i++) {   
      String g = list[list.length-i-1];
      String tm[] = splitTokens(g, ":");
      fill(0, 102, 51);
      rect(sj*(1+i), y, 5, PApplet.parseInt(tm[1])*-k);
      fill(0, 0, 102);
      if (i%2!=0) {
        if (PApplet.parseInt(tm[1])>0) text(tm[1], sj*(1+i), y-10+PApplet.parseInt(tm[1])*-k);
        else text(tm[1], sj*(1+i), y+20+PApplet.parseInt(tm[1])*-k);

        if (PApplet.parseInt(tm[1])>0) text(tm[0], sj*(i+1), y+25);
        else text(tm[0], sj*(i+1), y-25);
      }
    }
  }
  public String trend(String[] aa) {
    //\u043d\u0430\u0439\u0434\u0435\u043c \u043c\u0438\u043d\u0438\u043c\u0443\u043c\u044b \u0438 \u043c\u0430\u043a\u0441\u0438\u043c\u0443\u043c\u044b \u0432 \u043c\u0430\u0441\u0441\u0438\u0432\u0435 \u0438 \u0438\u0445 \u043f\u043e\u0437\u0438\u0446\u0438\u0438
    String t1[] = splitTokens(aa[0], ":");
    int mn=PApplet.parseInt(t1[1])*10;
    int mx=PApplet.parseInt(t1[1])*10;
    int pmn = 0;
    int pmx = 0;
    for (int i=0 ;i<aa.length;i++) {
      String t[] = splitTokens(aa[i], ":");
      if (PApplet.parseInt(t[1])*10 >mx) {
        mx = PApplet.parseInt(t[1])*10;
        pmx = i;
      } 
      if (PApplet.parseInt(t[1])*10 <mn) {
        mn = PApplet.parseInt(t[1])*10;
        pmn = i;
      }
    }
    return str(mx)+":"+str(pmx)+":"+str(mn)+":"+str(pmn);
  }
}

class myGis {
  //\u0434\u0430\u043d\u043d\u044b\u0435 \u0441 \u043c\u043e\u0435\u0433\u043e \u043a\u043e\u043c\u043f\u0430
  int x;
  int y;
  String[] cur = new String[3];
  public void display() {
    fill(0, 0, 0); 
    text("cur: "+cur[0]+" min: "+cur[1]+" max: "+cur[2], x, y);
  }
  public void load() {
    String[] user;
    //String[] user = {""};
    String[] tm = new String[3];
    String[] vrem= new String[3];
    
    user = loadStrings("http://star003.dlinkddns.com/03.php");
    
    int a1=0;
    int a2=0;
    int a3=0;

    try { 
      String[] list = splitTokens(user[0], "<br>");
      tm[0] =list[1]; 
      tm[1] =list[3]; 
      tm[2] =list[5]; 
      vrem[0] =list[0]; 
      vrem[1] =list[2]; 
      vrem[2] =list[4]; 

      cur[0] = vrem[0]+" , "+tm[0];
      cur[1] = vrem[1]+" , "+tm[1];
      cur[2] = vrem[2]+" , "+tm[2];
    }

    catch (Exception e) {
      cur[0] = "n/a";
      cur[1] = "n/a";
      cur[2] = "n/a";
    }
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
  public void display() {
   image(img,x,y,50,50); 
  }
  
  public boolean press() {
    if (mouseX>=x && mouseX <= x+50 
      && mouseY>=y && mouseY <= y+50) {
      return true;
    }
    return false;
  }
}
public void mousePressed() {
  if (b1.press() == true) sr++; 
}


class getPogoda {
  String[] todayYa = new String[12];
  String[] op = new String[12];
  int x,y;
  public void load() {
    op[0]="\u043f\u043e\u0433\u043e\u0434\u0430 \u043d\u0430:";
    op[1]="\u0422\u0435\u043a\u0443\u0449\u0430\u044f \u0442\u0435\u043c\u043f\u0435\u0440\u0430\u0442\u0443\u0440\u0430:";
    op[2]="\u042f\u0432\u043b\u0435\u043d\u0438\u044f:";
    op[3]="\u041d\u0430\u043f\u0440\u0432\u043b\u0435\u043d\u0438\u0435 \u0432\u0435\u0442\u0440\u0430:";
    op[4]="\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0432\u0435\u0442\u0440\u0430:";
    op[5]="\u0412\u043b\u0430\u0436\u043d\u043e\u0441\u0442\u044c:";
    op[6]="\u0414\u0430\u0432\u043b\u0435\u043d\u0438\u0435:";
    op[7]="\u0412\u043e\u0441\u0445\u043e\u0434 \u0441\u043e\u043b\u043d\u0446\u0430:";
    op[8]="\u0417\u0430\u0445\u043e\u0434 \u0441\u043e\u043b\u043d\u0446\u0430:";
    op[9]="\u0424\u0430\u0437\u0430 \u043b\u0443\u043d\u044b:";
    op[10]="\u0412\u043e\u0441\u0445\u043e\u0434 \u043b\u0443\u043d\u044b:";
    op[11]="\u0417\u0430\u0445\u043e\u0434 \u043b\u0443\u043d\u044b:";
  boolean pw =false;
  int cp =0;
  String[] user = loadStrings("http://export.yandex.ru/weather-ng/forecasts/27225.xml?"+str(PApplet.parseInt(random(1000,100000))));
  
  for (int i = 0; i < user.length; i++) {
    if (user[i].indexOf("<uptime>")!=-1 & pw==false) {
    todayYa[cp] =gt(user[i]);
    cp++; 
    }
    if (user[i].indexOf("<temperature color")!=-1& pw==false) {
    todayYa[cp] =gt(user[i]);
    cp++;
  
  }
    if (user[i].indexOf("<weather_type>")!=-1 & pw==false) {
    todayYa[cp] =gt(user[i]);
    cp++;
  
  }
    
    if (user[i].indexOf("<wind_direction>")!=-1 & pw==false) {
    todayYa[cp] =gt(user[i]);
    cp++;
  
  }
    
    if (user[i].indexOf("<wind_speed>")!=-1 & pw==false) {
    todayYa[cp] =gt(user[i]);
    cp++;
  
  }
    
    if (user[i].indexOf("<humidity>")!=-1 & pw==false) {
    todayYa[cp] =gt(user[i]);
    cp++;
  
  }
    if (user[i].indexOf("<pressure units")!=-1 & pw==false) {
    pw =true;
    todayYa[cp] =gt(user[i]);
    cp++;
  
  }
    
    if (user[i].indexOf("<sunrise>")!=-1) {
    todayYa[cp] =gt(user[i]);
    cp++;
  
  }
    if (user[i].indexOf("<sunset>")!=-1) {
    todayYa[cp] =gt(user[i]);
    cp++;
  
  }
    if (user[i].indexOf("<moon_phase code")!=-1) {
    todayYa[cp] =gt(user[i]);
    cp++;
  
  }
    if (user[i].indexOf("<moonrise>")!=-1) {
    todayYa[cp] =gt(user[i]);
    cp++;
  
  }
    if (user[i].indexOf("<moonset>")!=-1) {
    todayYa[cp] =gt(user[i]);
    cp++;
  
    break;
    }
  }
 }
 public String gt(String s) {
 String[] x = split(s,'>');
 String[] x1 = split(x[1],'<');
 return x1[0]; 
}
 public void display() {
   fill(0);
   for (int y1=0;y1<9;y1++)  {
   text(getPogoda.op[y1]+getPogoda.todayYa[y1],x,y+(y1*20));
 }
 }
}

class prognozGis {
  //\u043f\u0440\u043e\u0433\u043d\u043e\u0437 4 \u0434\u043d\u044f
  int x;
  int y;
  String[] prognoz1 = new String[4];
  public void display() {
    fill(0,0,0); 
     for (int j=0;j<4;j++) {
       text(prognoz1[j],x,y+(20*j)); 
     }
     fill(255,255,255); 
  }
  public void load() {
  String[] tem_ = new String[4];
  String[] vrm_ = new String[4];
  String[] os_ = new String[4];
  int a1=0;
  int a2=0;
  int a3=0;
  String[] user = loadStrings("http://informer.gismeteo.ru/xml/27225_1.xml");
  String s1 = user[0];
  for (int i=0;i < user.length;i++) {
    if (user[i].indexOf("<FORECAST") != -1) {
      vrm_[a1] =getDay(user[i]); 
      a1++;
     }
    if (user[i].indexOf("<TEMPERATURE ") != -1) {
      tem_[a2] = getTemp(user[i]); 
       a2++;
    }
    if (user[i].indexOf("<PHENOMENA ") != -1) {
      os_[a2] = getOs(user[i]); 
       a3++;
    }
  }
  prognoz1[0]=vrm_[0]+" "+tem_[0]+" "+os_[0];
  prognoz1[1]=vrm_[1]+" "+tem_[1]+" "+os_[1];
  prognoz1[2]=vrm_[2]+" "+tem_[2]+" "+os_[2];
  prognoz1[3]=vrm_[3]+" "+tem_[3]+" "+os_[3];
}
public String getOs(String s) {
String[] list = split(s, '"');
if (list.length<2) {
return "";
}
  if(PApplet.parseInt(list[3])==10) {
    return "\u0411\u0435\u0437 \u043e\u0441\u0430\u0434\u043a\u043e\u0432";
  }
  if(PApplet.parseInt(list[3])==4) {
    return "\u0414\u043e\u0436\u0434\u044c";
  }   
  if(PApplet.parseInt(list[3])==5) {
    return "\u041b\u0438\u0432\u0435\u043d\u044c";
  } 
  if(PApplet.parseInt(list[3])==8) {
    return "\u0413\u0440\u043e\u0437\u0430";
  } 
  if(PApplet.parseInt(list[3])==6 && PApplet.parseInt(list[3])==7) {
    return "\u0421\u043d\u0435\u0433";
  } 
 if(PApplet.parseInt(list[3])==9) {
    return "\u041d\u0435\u0442 \u0434\u0430\u043d\u043d\u044b\u0445";
  } 
 else  return list[3];
} 
}
public String getTemp(String s) {
String[] list = split(s, '"');
if (list.length<2) {
return "";
}
else {
return list[3]+"..."+list[1];
} 
}
public String getDay(String s) {
String[] list = split(s, '"');
if (list.length<2) {
return "";
}
else {
return list[1]+"."+list[3]+"."+list[5]+"  "+list[7]+"h";
} 
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "miniPrognoz" });
  }
}
