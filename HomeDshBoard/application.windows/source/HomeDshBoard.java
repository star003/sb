import processing.core.*; 
import processing.xml.*; 

import processing.net.*; 

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

public class HomeDshBoard extends PApplet {

//import processing.opengl.*;
//import processing.pdf.*;

Server myServer;

PFont f;
String[][] a;
String[] buf =  new String[99];
PImage img;
//\u0442\u0430\u0439\u043c\u0435\u0440\u044b
int m=0;
int m1=0;
int m2=0;
int m3=0;
int m01=0;
int m02=2000; //\u0442\u0430\u0439\u043c\u0435\u0440 \u043f\u043e\u0441\u044b\u043b\u043a\u0438 \u043f\u0430\u043a\u0435\u0442\u0430 \u0434\u043b\u044f \u0441\u043f\u044f\u0449\u0435\u0439 \u0441\u043e\u0431\u0430\u043a\u0438
byte val=0;

ya ya;
myGis myGis;
gisGis gisGis; 
prognozGis prognozGis;
digit digit;
getPogoda getPogoda;

int dx = 800;
int dy = 400;
//\u0411\u043b\u043e\u043a \u0433\u0438\u0441\u043c\u0435\u0442\u0435\u043e
int gx =450;
int gy =90;
//\u0431\u043b\u043e\u043a \u043c\u043e\u0438\u0445 \u0434\u0430\u043d\u043d\u044b\u0435
int mx =400;
int my =20;
//\u0431\u043b\u043e\u043a \u043f\u0440\u043e\u0433\u043d\u043e\u0437\u0430
int px =400;
int py =280;

public void setup() {
 size(dx,dy); 
 myServer = new Server(this, 5204);
// if (frame != null) {
//    frame.setResizable(true);
//  }
 background(255,255,255);
 //smooth();
frameRate(2);
prognozGis = new prognozGis();
prognozGis.x=px;
prognozGis.y=py;
prognozGis.load();

 myGis = new myGis();
 myGis.x =mx;
 myGis.y =my;
 myGis.load();
 
 //gisGis = new gisGis();
 //gisGis.x = gx;
 //gisGis.y = gy;
 //gisGis.load();
 
 ya = new ya();
 ya.x=5;
 ya.y=5;
 ya.w=320;
 ya.h=240;
 ya.loadY();
 ya.loadI(PApplet.parseInt(random(1,99)));
 
 f = loadFont( "Courier-12.vlw" );
 textFont(f,16);
 digit = new digit();
 digit.x1=10;
 digit.y1=260;
 digit.w =20;
 digit.g =10;
 
 getPogoda = new getPogoda();
 getPogoda.load();
 
}

public void draw() {
 background(255,255,255); 
 
 if (millis()-m01 >m02) {
    m01 = millis();
    if (val>100) val = 0;
    val ++;
    myServer.write(val);
  }
  
 fill(0);
 for (int y1=0;y1<9;y1++)  {
 text(getPogoda.op[y1]+getPogoda.todayYa[y1],gx,gy+(y1*20));
 }

 ya.display();
 fill(0,0,0); 
 myGis.display();
 //gisGis.display();
 prognozGis.display();
 if (minute()%2 == 0){
    if (m1 !=1){ 
    m++;
    m1 = 1;
    ya.loadI(PApplet.parseInt(random(1,99)));
    try {
    myGis.load(); 
  }
  catch (Exception e) {
  }
  }
  }
   else {
     m1 = 0;
   }
   if (minute()%30 == 0){
    if (m2 !=1){ 
    m3++;
    m2 = 1;
    
    //gisGis.load();
    getPogoda.load();
    prognozGis.load();
    ya.loadY();
  }
  }
   else {
     m2 = 0;
   }
   digit.display(clock());
}

public String dt(String[][] s) {
  String x="";
  if (a==null) return "-";
  for (int i = 0;i<a.length;i++){
  x +=a[i][0];
  }
   return trim(x);
}
class watcDog {
 public void rec() {

 }
 
}
class gisGis{
  //\u0442\u0435\u043a\u0443\u0449\u0438\u0435 \u0434\u0430\u043d\u043d\u044b\u0435 \u0433\u0438\u0441\u043c\u0435\u0442\u0435\u043e
 int x;
 int y;
 String[] prognoz = new String[7]; 
 public void load() {
  String[] tem_ = new String[4];
  String[] vrm_ = new String[4];
  int [] pos =  {335,340,344,348,356,360,363};
  String [] reg = {"[\u0410-\u042f\u0430-\u044f]","[\u0410-\u042f\u0430-\u044f,\\s]","[-+0-9]","[\u0410-\u042f\u0430-\u044f\\-\\s]","[0-9]{3}","[0-9]","\\d\\d\\:\\d\\d"};
  int a1=0;
  int a2=0;
  String[] user = loadStrings("http://www.gismeteo.ru/city/daily/4298/");
  String s1 = user[0];
  for (int i =0 ;i<7;i++) {
    //println(user[pos[i]]);
    a = matchAll(user[pos[i]+4],reg[i]);
    prognoz[i] = dt(a);
  }
 }
  public void display() {
 text(prognoz[0]+" "+prognoz[6],x,y); 
 text("\u041e\u0431\u043b\u0430\u0447\u043d\u043e\u0441\u0442\u044c:"+prognoz[1],gx,gy+20); 
 text("\u0422\u0435\u043c\u043f\u0435\u0440\u0430\u0442\u0443\u0440\u0430: "+prognoz[2],x,y+40); 
 text(prognoz[3],gx,gy+60); 
 text("\u0414\u0430\u0432\u043b\u0435\u043d\u0438\u0435: "+prognoz[4],x,y+80); 
 text("\u0412\u043b\u0430\u0436\u043d\u043e\u0441\u0442\u044c: "+prognoz[5],x,y+100); 
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
public String delKav(String s) {
String[] list = split(s, '"');
if (list.length<2) {
return "";
}
else {
return list[1];
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

class myGis{
  //\u0434\u0430\u043d\u043d\u044b\u0435 \u0441 \u043c\u043e\u0435\u0433\u043e \u043a\u043e\u043c\u043f\u0430
  int x;
  int y;
  String[] cur = new String[3];
public void display() {
  fill(0,0,0); 
 text("\u0422\u0435\u043a\u0443\u0449\u0430\u044f: "+cur[0],x,y); 
 text("\u041c\u0438\u043d\u0438\u043c\u0443\u043c: "+cur[1],x,y+20); 
 text("\u041c\u0430\u043a\u0441\u0438\u043c\u0443\u043c: "+cur[2],x,y+40); 
}
public void load() {
  String[] tm = new String[3];
  String[] vrem= new String[3];
  String[] user = loadStrings("http://star003.dlinkddns.com/03.php");
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
       
    cur[0] = vrem[0]+" "+tm[0];
    cur[1] = vrem[1]+" "+tm[1];
    cur[2] = vrem[2]+" "+tm[2];
}

catch (Exception e) {
    cur[0] = "n/a";
    cur[1] = "n/a";
    cur[2] = "n/a";
}
}
}

class ya {
  //\u043a\u0430\u0440\u0442\u0438\u043d\u043a\u0430 \u044f\u043d\u0434\u0435\u043a\u0441 ))
 int x;
 int y;
 int w;
 int h;
 String[] d;
 PImage img;
 String[] dat = new String[300];
 
public void display() {
  try {
  image(img,x,y,w,h);
  }
  catch (Exception e) {
}
}
public void loadI(int t) {
  try {
  img = loadImage(dat[t]+".jpg");
  }
  catch (Exception e) {
}
}

public void loadY() {
int v=0;
String[] user = loadStrings("http://api-fotki.yandex.ru/api/podhistory/");
for (int j = 0; j < user.length; j++) {
  if (user[j].indexOf("<title>") != -1) {
    dat[v] =delKav(user[j+4]); 
    v++;
  }
}
}
}

class getPogoda {
  String[] todayYa = new String[12];
  String[] op = new String[12];
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
}
//class inf {
//  int x;
//  int y;
//  String tek,mn,mx;
//  //inf(int x,int y,String tek,String mn,String mx){
//  //};
//  void display() {
//    f = loadFont( "Courier-12.vlw" );
//  
//  stroke(255,0,204);
//  strokeWeight(5);
//  rect(x,y,400,150);
//  fill(255,255,255);
//  textFont(f,60); 
//  fill(255,0,204);
//  text(tek,x+3,y+90);
//  text(mn,x+200,y+50);
//  text(mx,x+200,y+130);
//  fill(255,255,255);
//  }
//  
//}
    class digit{
      int x1; //\u043d\u0430\u0447\u0430\u043b\u043e \u0425
      int y1; //\u043d\u0430\u0447\u0430\u043b\u043e \u0423
      int h; // \u043d\u0435 \u0438\u0441\u043f.
      int w; //\u0432\u044b\u0441\u043e\u0442\u0430 \u0441\u0438\u043c\u0432\u043e\u043b\u0430 \u0432 \u043f\u0438\u043a\u0441
      int g; //\u0440\u0430\u0432\u043d\u043e \u04451
      
    public void hor(int x,int y) {
    int h= PApplet.parseInt(0.3f*w);
    fill(0,255,0);
    beginShape();
    vertex(x, y);
    vertex(x+0.15f*w, y-0.5f*h);
    vertex(x+0.85f*w, y-0.5f*h);
    vertex(x+w, y);
    vertex(x+0.85f*w, y+0.5f*h);
    vertex(x+0.15f*w, y+0.5f*h);
    vertex(x, y); 
    endShape(CLOSE);
      }
      
    public void wert(int x,int y) {
    fill(0,255,0);
    int h= PApplet.parseInt(0.3f*w);
    beginShape();
    vertex(x, y);
    vertex(x+0.5f*h, y+0.15f*w);
    vertex(x+0.5f*h, y+0.85f*w);
    vertex(x,y+w);
    vertex(x-0.5f*h,y+0.85f*w);
    vertex(x-0.5f*h,y+0.15f*w);
    vertex(x, y);
    endShape();
      }
    public void sq() {
    fill(255); 
   int h= PApplet.parseInt(0.3f*w); 
    rect(x1-0.47f*h,y1-0.47f*h,w+h,2*w+h);
    }  
    public void point() {
     rect(x1+1.15f*w,y1+2*w,0.25f*w,0.25f*w); 

    }
    
    public void twoPoint() {
     rect(x1+0.5f*w,y1+0.5f*w,0.25f*w,0.25f*w); 
     rect(x1+0.5f*w,y1+1.25f*w,0.25f*w,0.25f*w); 
    }
      
      public void d0() {
        hor(x1,y1);
        hor(x1,y1+2*w);
        wert(x1+w,y1); 
        wert(x1+w,y1+w); 
        wert(x1,y1); 
        wert(x1,y1+w); 
        
      }
      
      public void d1() {
      wert(x1+w,y1); 
      wert(x1+w,y1+w); 
      }
      
      public void d2() {
       hor(x1,y1);
      wert(x1+w,y1); 
       hor(x1,y1+w);
      wert(x1,y1+w); 
       hor(x1,y1+2*w); 
      }
      
      public void d3() {
       hor(x1,y1);
      wert(x1+w,y1); 
       hor(x1,y1+w);
      wert(x1+w,y1+w); 
       hor(x1,y1+2*w); 
      }
      public void d4() {
       //hor(x1,y1);
      wert(x1+w,y1); 
      wert(x1,y1); 
       hor(x1,y1+w);
      wert(x1+w,y1+w); 
       
      }
      
      public void d5() {
       hor(x1,y1);
       wert(x1,y1); 
       hor(x1,y1+w);
       wert(x1+w,y1+w); 
       hor(x1,y1+2*w); 
      }
      
      public void d6() {
       hor(x1,y1);
       wert(x1,y1); 
       hor(x1,y1+w);
       wert(x1+w,y1+w); 
       hor(x1,y1+2*w); 
       wert(x1,y1+w);
      }
      public void d7() {
      hor(x1,y1);  
      wert(x1+w,y1); 
      wert(x1+w,y1+w); 
      }
     public void d8() {
       wert(x1+w,y1); 
       hor(x1,y1);
       wert(x1,y1); 
       hor(x1,y1+w);
       wert(x1+w,y1+w); 
       hor(x1,y1+2*w); 
       wert(x1,y1+w);
      } 
       public void d9() {
       wert(x1+w,y1); 
       hor(x1,y1);
       wert(x1,y1); 
       hor(x1,y1+w);
       wert(x1+w,y1+w); 
       hor(x1,y1+2*w); 
       
      } 
      
       public void dp() {
        hor(x1,y1+w);
       } 
      
      public void display(String s) {
         for (int i0=0;i0<s.length();i0++) {
          x1 =g + PApplet.parseInt(i0*w*1.5f)+PApplet.parseInt(w);
          razr(s.charAt(i0));
        }
      }
      
      public void razr(char s) {
        if (s=='0') d0();
        if (s=='1') d1();
        if (s=='2') d2();
        if (s=='3') d3();
        if (s=='4') d4();
        if (s=='5') d5();
        if (s=='6') d6();
        if (s=='7') d7();
        if (s=='8') d8();
        if (s=='9') d9();
        if (s=='-') dp();
        if (s=='.') point();
        if (s==':') twoPoint();
      }
    }
    
    public String clock() {
 
 //text(fwZero(day())+"-"+fwZero(month())+"-"+year(),cpx,cpy); 
 int m=0;
 int h=0;
   m = minute();
   h = hour();
return (fwZero(h)+":"+fwZero(m)+":"+fwZero(second())); 
}

public String dat(){
 return fwZero(day())+"-"+fwZero(month())+"-"+year();
}
 
//\u0431\u043b\u043e\u043a \u0432\u0441\u043f\u043e\u043c\u043e\u0433\u0430\u0442\u0435\u043b\u044c\u043d\u044b\u0445 \u0444\u0443\u043d\u043a\u0446\u0438\u0439  
public String fwZero(int z) {
  if (z<10 & z!=0) {
    return "0"+str(z);
  }
    else if (z>=10) {
    return str(z);  
    }
    else if (z==0) {
    return "00";  
    }
    else return "";
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "HomeDshBoard" });
  }
}
