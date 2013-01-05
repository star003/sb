import processing.core.*; 
import processing.data.*; 
import processing.opengl.*; 

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

//\u043e\u043f\u0438\u0441\u0430\u043d\u0438\u0435 \u043c\u043e\u0438\u0445 \u043e\u0431\u044a\u0435\u043a\u0442\u043e\u0432
 diagPr 	diagPr;
 myGis 	myGis;
 button 	b1,b2;
 prognozGis 	prognozGis;
 getPogoda 	getPogoda;
 clock 	clock;
 clock1 clock1;
 PFont ft;
 
//\u0442\u0430\u0439\u043c\u0435\u0440\u044b
 int m01=0;
 int m02=600000;
//\u0437\u0430\u0433\u043b\u0443\u0448\u043a\u0430 \u043f\u0440\u0438 \u043f\u043e\u0442\u0435\u0440\u0435 \u0441\u0432\u044f\u0437\u0438
String[] ls = {
  "1:-30", "2:-25", "3:-20", "4:-15", 
  "5:-10", "6:-5", "7:0", 
  "8:5", "9:10", "10:15", 
  "11:20", "12:25", "13:30"
}; 
//\u0443\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u0435 \u044d\u043a\u0440\u0430\u043d\u043e\u043c \u043f\u043e \u0443\u043c\u043e\u043b\u0447\u0430\u043d\u0438\u044e 1\u0439
byte sr = 1;
PFont f;

public void setup() {
		size(500, 400); 
		frameRate(1);
			background(255, 255, 204);
				diagPr 	= new diagPr(180,180);
				myGis 	= new myGis(10,20);
				b1 		= new button(440,250,loadImage("Next.png"));
				b2 		= new button(440,190,loadImage("Refresh.png"));
				prognozGis 	= new prognozGis(20,20);
				getPogoda 	= new getPogoda(20,20);
				clock 	= new clock(420,60,50);
				clock1 	= new clock1(360,390);
		diagPr.load();
		diagPr.display();
		myGis.load();
	ft = loadFont("Courier-12.vlw");
	textFont(ft,12);
} //void setup()

public void draw() {
  //\u043a\u0430\u0436\u0434\u044b\u0435 10 \u043c\u0438\u043d\u0443\u0442 \u043f\u0435\u0440\u0435\u0433\u0440\u0443\u0436\u0430\u0435\u043c \u0434\u0430\u043d\u043d\u044b\u0435
  
  if (millis()-m01 > m02) {
    m01 = millis();
    diagPr.load();
    myGis.load();
    //prognozGis.load();
    //getPogoda.load();
  }
		if (sr>3) sr=1;
			//\u043e\u0442\u043e\u0431\u0440\u0430\u0436\u0435\u043d\u0438\u0435 \u0434\u0430\u043d\u043d\u044b\u0445 \u043d\u0430 \u044d\u043a\u0440\u0430\u043d\u0435
			if (sr==1 ) {
				background(255, 255, 204);
				//textFont(f,12);
				b1.display();
				b2.display();
				diagPr.display();
				myGis.y = 20;
				myGis.display();
			}
			else if (sr==2) {
				//textFont(f,16);
				background(255, 255, 204);
				b1.display();
				b2.display();
				prognozGis.load();
				prognozGis.display();
			}
			else if (sr==3) {
				background(255, 255, 204);
				//textFont(f,16);
				b1.display();
				b2.display();
				getPogoda.load();
				getPogoda.display();
			}
	clock1.display();
} //void draw()

	/*
		\u0434\u0430\u043d\u043d\u044b\u0439 \u043a\u043b\u0430\u0441\u0441 \u0434\u043e\u0441\u0442\u0430\u0435\u0442 \u0434\u0430\u043d\u043d\u044b\u0435 \u043e \u043f\u0435\u0440\u0435\u043f\u0430\u0434\u0430\u0445 \u0442\u0435\u043c\u043f\u0435\u0440\u0430\u0442\u0443\u0440\u044b
		\u0432 \u0442\u0435\u043a\u0443\u0449\u0438\u0445 \u0441\u0443\u0442\u043a\u0430\u0445
	*/
	
class diagPr {
  int x, y;
  PFont ft;
  String[] list, user;
  int mn, mx;
  diagPr(int x1,int y1) {
    x = x1;
    y = y1;
  }
  public void load() {
    try {
      user = loadStrings("http://star003.dlinkddns.com/04.php");
      list = splitTokens(user[0], "<br>");
    }
    catch (Exception e) {
      list = ls;
    }
	ft = loadFont("Courier-12.vlw");
    String g1 = trend(list);
    String[] g2 = splitTokens(g1, ":");
    mx = PApplet.parseInt(g2[0]);
    mn = PApplet.parseInt(g2[2]);
  } 
  public void display() {
    int k=vert(mx);
	text("sn"+k, 250, 10);
    int sj = 15; //\u0441\u0436\u0430\u0442\u0438\u0435 \u0434\u0438\u0430\u0433\u0440\u0430\u043c\u043c\u044b
    for (int i = 0; i<list.length; i++) {   
	  String g = list[i];
      String tm[] = splitTokens(g, ":");
      fill(0, 102, 51);
      rect(sj*(1+i), y, 5, PApplet.parseInt(PApplet.parseFloat(tm[1])*-k));
      fill(0, 0, 102);
	  byte shag = 0;
      if (i%2!=0) shag = 0;
	  else shag = 15;
        fill(56,86,232);
		//\u044d\u0442\u043e \u0434\u0430\u043d\u043d\u044b\u0435 \u043f\u043e \u043e\u0441\u0438 \u0423 \u0438\u043b\u0438 \u0442\u0435\u043c\u043f\u0435\u0440\u0430\u0442\u0443\u0440\u0430
		textFont(ft,10);
        if (PApplet.parseFloat(tm[1])>0) {
		text(tm[1], sj*(1+i)-5, shag+y-20+PApplet.parseFloat(tm[1])*-k);
		}
        else {
		text(tm[1], sj*(1+i)-5, shag+y+20+PApplet.parseFloat(tm[1])*-k);
		}
			fill(67,193,39);
			textFont(ft,12);
			//\u044d\u0442\u043e \u0434\u0430\u043d\u043d\u044b\u0435 \u043f\u043e \u043e\u0441\u0438 \u0425 \u0438\u043b\u0438 \u0432\u0440\u0435\u043c\u044f
			if (PApplet.parseFloat(tm[1])>0) text(tm[0], sj*(i+1)-5, shag+y-25);
			else text(tm[0], sj*(i+1)-5, shag+y-25);
			textFont(ft,12);
    }
  }
  public String trend(String[] aa) {
    //\u043d\u0430\u0439\u0434\u0435\u043c \u043c\u0438\u043d\u0438\u043c\u0443\u043c\u044b \u0438 \u043c\u0430\u043a\u0441\u0438\u043c\u0443\u043c\u044b \u0432 \u043c\u0430\u0441\u0441\u0438\u0432\u0435 \u0438 \u0438\u0445 \u043f\u043e\u0437\u0438\u0446\u0438\u0438
    String t1[] = splitTokens(aa[0], ":");
    int mn=abs(PApplet.parseInt(t1[1])*10);
    int mx=abs(PApplet.parseInt(t1[1])*10);
    int pmn = 0;
    int pmx = 0;
    for (int i=0 ;i<aa.length;i++) {
      String t[] = splitTokens(aa[i], ":");
      if (abs(PApplet.parseInt(t[1])*10) >mx) {
        mx = abs(PApplet.parseInt(t[1])*10);
        pmx = i;
      } 
      if (abs(PApplet.parseInt(t[1])*10) <mn) {
        mn = abs(PApplet.parseInt(t[1])*10);
        pmn = i;
      }
    }
    return str(mx)+":"+str(pmx)+":"+str(mn)+":"+str(pmn);
  }
  public int vert(int m) {
    int h = abs(m);
    //println(m);
    if (h >=0 && h<=25) return 50;
    else if (h >26 && h<100) return 20; 
    else if (h >101) return 5;
   return 10; 
  }
} //class diagPr(int x1,int y1)

	/*
	\u0434\u0430\u043d\u043d\u044b\u0439 \u043a\u043b\u0430\u0441\u0441 \u0438\u0437\u0432\u043b\u0435\u043a\u0430\u0435\u0442 \u0434\u0430\u043d\u043d\u044b\u0435 \u043e \u0442\u0435\u043a\u0443\u0449\u0435\u0439 \u0442\u0435\u043c\u043f\u0435\u0440\u0430\u0442\u0443\u0440\u0435
	\u0430 \u0442\u0430\u043a \u0436\u0435 \u043e \u0435\u0435 \u043c\u0438\u043d\u0438\u043c\u0443\u043c\u0435 \u0438 \u043c\u0430\u043a\u0441\u0438\u043c\u0443\u043c\u0435 \u0432 \u0442\u0435\u043a\u0443\u0449\u0438\u0445 \u0441\u0443\u0442\u043a\u0430\u0445
	*/
class myGis {
  //\u0434\u0430\u043d\u043d\u044b\u0435 \u0441 \u043c\u043e\u0435\u0433\u043e \u043a\u043e\u043c\u043f\u0430
  int x;
  int y;
  PFont ft1;
  myGis(int x1,int y1) {
    x = x1;
    y = y1;
  } 
  String[] cur = new String[3];
  public void display() {
    fill(0, 0, 0); 
	textFont(ft1,12);
    text("cur: " +cur[0], x, y);
	text("min: " +cur[1], x, y+15);
	text("max: " +cur[2], x, y+30);
  }
  public void load() {
    String[] user;
    String[] tm 	= new String[3];
    String[] vrem	= new String[3];
    user = loadStrings("http://star003.dlinkddns.com/03.php");
		ft1 = loadFont("Courier-12.vlw");
		
    try { 
      String[] list = splitTokens(user[0], "<br>");
		for (byte j=0 ;j<3;j++) {
			cur[j] = list[j*2]+" , "+list[j*2+1];
		}
    }
    catch (Exception e) {
      cur[0] = "n/a";
      cur[1] = "n/a";
      cur[2] = "n/a";
    }
  }
}//class myGis(int x1,int y1)
	/*
	\u0440\u0438\u0441\u0443\u0435\u043c \u043a\u043d\u043e\u043f\u043a\u0438 \u0438 \u043f\u0440\u043e\u0433\u0440\u0430\u043c\u0438\u0440\u0443\u0435\u043c \u043e\u0442\u043a\u043b\u0438\u043a\u0438 \u043d\u0430 \u0438\u0445 \u043d\u0430\u0436\u0430\u0442\u0438\u0435
	*/

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
  if (b2.press() == true) {
    //\u043e\u0431\u043d\u043e\u0432\u0438\u043c \u0434\u0430\u043d\u043d\u044b\u0435
    diagPr.load();
    myGis.load();
    prognozGis.load();
    getPogoda.load(); 
  } 
} //class button(int x1,int y1,PImage img1)
	/*
		\u0434\u043e\u0441\u0442\u0430\u0435\u043c \u0434\u0430\u043d\u043d\u044b\u0435 \u043e \u0442\u0435\u043a\u0443\u0449\u0435\u0439 \u043f\u043e\u0433\u043e\u0434\u0435 \u0441 \u044f\u043d\u0434\u0435\u043a\u0441\u0430
	*/
class getPogoda {
  String[] todayYa = new String[12];
  String op[] = {"\u043f\u043e\u0433\u043e\u0434\u0430 \u043d\u0430:","\u0422\u0435\u043a\u0443\u0449\u0430\u044f \u0442\u0435\u043c\u043f\u0435\u0440\u0430\u0442\u0443\u0440\u0430:","\u0422\u0435\u043a\u0443\u0449\u0430\u044f \u0442\u0435\u043c\u043f\u0435\u0440\u0430\u0442\u0443\u0440\u0430:",
			"\u041d\u0430\u043f\u0440\u0432\u043b\u0435\u043d\u0438\u0435 \u0432\u0435\u0442\u0440\u0430:","\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0432\u0435\u0442\u0440\u0430:","\u0412\u043b\u0430\u0436\u043d\u043e\u0441\u0442\u044c:",
			"\u0414\u0430\u0432\u043b\u0435\u043d\u0438\u0435:","\u0412\u043e\u0441\u0445\u043e\u0434 \u0441\u043e\u043b\u043d\u0446\u0430:","\u0417\u0430\u0445\u043e\u0434 \u0441\u043e\u043b\u043d\u0446\u0430:",
			"\u0424\u0430\u0437\u0430 \u043b\u0443\u043d\u044b:","\u0412\u043e\u0441\u0445\u043e\u0434 \u043b\u0443\u043d\u044b:","\u0417\u0430\u0445\u043e\u0434 \u043b\u0443\u043d\u044b:"};
  int x,y;
  getPogoda(int x1,int y1) {
    x = x1;
    y = y1;
  }
  public void load() {
	
	boolean pw =false;
	int cp =0;
	String[] user = loadStrings("http://export.yandex.ru/weather-ng/forecasts/27225.xml?"+str(PApplet.parseInt(random(1000,100000))));
	String[] dt1 ={"<uptime>","<temperature color","<weather_type>",
				"<wind_direction>","<wind_speed>","<humidity>",
				"<pressure units","<sunrise>","<sunset>",
				"<moon_phase code","<moonrise>","<moonset>"};
  for (int i = 0; i < user.length; i++) {
		for (byte j=0;j<12;j++) {
			if (user[i].indexOf(dt1[j]) !=-1) {
				todayYa[j] =gt(user[i]);
			}
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
 }	//class getPogoda

class prognozGis {
		//\u043f\u0440\u043e\u0433\u043d\u043e\u0437 4 \u0434\u043d\u044f
	int x;
	int y;
		prognozGis (int x1,int y1){
			x = x1;
			y = y1;
		}
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
	String[] dt1 = {"<FORECAST","<TEMPERATURE ","<PHENOMENA "};
		for (int i=0;i < user.length;i++) {
			if (user[i].indexOf(dt1[0]) != -1) {
				vrm_[a1] =getDay(user[i]); 
				a1++;
			}
			if (user[i].indexOf(dt1[1]) != -1) {
				tem_[a2] = getTemp(user[i]); 
				a2++;
			}
			if (user[i].indexOf(dt1[2]) != -1) {
				os_[a2] = getOs(user[i]); 
				a3++;
			}
		}
		for (byte j=0;j<4;j++) {
			prognoz1[j]=vrm_[j]+" "+tem_[j]+" "+os_[j];
		}
    }
  
	public String getOs(String s) {
		String dtt[] = {"0","1","2","\u0414\u043e\u0436\u0434\u044c","4","\u041b\u0438\u0432\u0435\u043d\u044c","\u0421\u043d\u0435\u0433","\u0421\u043d\u0435\u0433","\u0413\u0440\u043e\u0437\u0430","\u041d\u0435\u0442 \u0434\u0430\u043d\u043d\u044b\u0445","\u0411\u0435\u0437 \u043e\u0441\u0430\u0434\u043a\u043e\u0432"};
		String[] list = split(s, '"');
			if (list.length<2) {
			return "";
			}
			else return dtt[PApplet.parseInt(list[3])];
	} 
} //class prognozGis

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
	/*
	\u0440\u0438\u0441\u0443\u0435\u043c \u0441\u0442\u0440\u0435\u043b\u043e\u0447\u043d\u044b\u0435 \u0447\u0430\u0441\u044b
	*/
class clock {
  //x,y, \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b \u0446\u0435\u043d\u0442\u0440\u0430, r \u0440\u0430\u0434\u0438\u0443\u0441
	int x,y,r;
		clock (int x1,int y1,int r1) {
			x = x1;
			y = y1;
			r = r1; 
		}
 
	public void display() {
		for (int i=0;i<12;i+=1) { 
			pushMatrix();
			translate(x, y);
			rotate(radians(i*30));
			fill(0);
			rect(0,r,3,r/12);
			popMatrix();
		}
			float kt = 0;
			ellipse(x,y,r/12,r/12);
			step(second(),6,0.9f);
			int m = minute();
			step(m,6,0.8f);
				if (m>30) kt=1.5f;
				else kt=0.5f;
			step(hour(),30+kt,0.7f);
	}

	public void step (int ed,float st,float k) {
	//ed = minute,second st = \u0448\u0430\u0433, k \u0434\u043b\u0438\u043d\u043d\u0430 \u0441\u0442\u0440\u0435\u043b\u043a\u0438
		pushMatrix();
		translate(x, y);
		rotate(radians(ed*st-90));
		fill(0);
		secSt(0,0,PApplet.parseInt(r*k));
		popMatrix();
	}

	public void secSt(int x,int y,int l) {
		beginShape();
		line(x,y,x+l,y);
		line(x+l,y,x-r/12+l,y+3);
		line(x+l,y,x-r/12+l,y-3); 
		endShape();  
	}
} //class clock (int x1,int y1,int r1)

class clock1 {
	int x,y;
	clock1 (int x1,int y1) {
		x = x1;
		y = y1;
	}
	public void display() {
		for (int i=0;i<10;i++) {
			fill(102,255,102);
			rect(x+12*(i+1),y,10,5);
			fill(255,0,0);
			rect(x+12*(second()%10+1),y,10,5);
		}
		fill(67,193,39);
	}	
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "miniPrognoz" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
