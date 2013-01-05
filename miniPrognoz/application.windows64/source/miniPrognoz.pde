//описание моих объектов
 diagPr 	diagPr;
 myGis 	myGis;
 button 	b1,b2;
 prognozGis 	prognozGis;
 getPogoda 	getPogoda;
 clock 	clock;
 clock1 clock1;
 PFont ft;
 
//таймеры
 int m01=0;
 int m02=600000;
//заглушка при потере связи
String[] ls = {
  "1:-30", "2:-25", "3:-20", "4:-15", 
  "5:-10", "6:-5", "7:0", 
  "8:5", "9:10", "10:15", 
  "11:20", "12:25", "13:30"
}; 
//управление экраном по умолчанию 1й
byte sr = 1;
PFont f;

void setup() {
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

void draw() {
  //каждые 10 минут перегружаем данные
  
  if (millis()-m01 > m02) {
    m01 = millis();
    diagPr.load();
    myGis.load();
    //prognozGis.load();
    //getPogoda.load();
  }
		if (sr>3) sr=1;
			//отображение данных на экране
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
		данный класс достает данные о перепадах температуры
		в текущих сутках
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
  void load() {
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
    mx = int(g2[0]);
    mn = int(g2[2]);
  } 
  void display() {
    int k=vert(mx);
	text("sn"+k, 250, 10);
    int sj = 15; //сжатие диаграммы
    for (int i = 0; i<list.length; i++) {   
	  String g = list[i];
      String tm[] = splitTokens(g, ":");
      fill(0, 102, 51);
      rect(sj*(1+i), y, 5, int(float(tm[1])*-k));
      fill(0, 0, 102);
	  byte shag = 0;
      if (i%2!=0) shag = 0;
	  else shag = 15;
        fill(56,86,232);
		//это данные по оси У или температура
		textFont(ft,10);
        if (float(tm[1])>0) {
		text(tm[1], sj*(1+i)-5, shag+y-20+float(tm[1])*-k);
		}
        else {
		text(tm[1], sj*(1+i)-5, shag+y+20+float(tm[1])*-k);
		}
			fill(67,193,39);
			textFont(ft,12);
			//это данные по оси Х или время
			if (float(tm[1])>0) text(tm[0], sj*(i+1)-5, shag+y-25);
			else text(tm[0], sj*(i+1)-5, shag+y-25);
			textFont(ft,12);
    }
  }
  String trend(String[] aa) {
    //найдем минимумы и максимумы в массиве и их позиции
    String t1[] = splitTokens(aa[0], ":");
    int mn=abs(int(t1[1])*10);
    int mx=abs(int(t1[1])*10);
    int pmn = 0;
    int pmx = 0;
    for (int i=0 ;i<aa.length;i++) {
      String t[] = splitTokens(aa[i], ":");
      if (abs(int(t[1])*10) >mx) {
        mx = abs(int(t[1])*10);
        pmx = i;
      } 
      if (abs(int(t[1])*10) <mn) {
        mn = abs(int(t[1])*10);
        pmn = i;
      }
    }
    return str(mx)+":"+str(pmx)+":"+str(mn)+":"+str(pmn);
  }
  int vert(int m) {
    int h = abs(m);
    //println(m);
    if (h >=0 && h<=25) return 50;
    else if (h >26 && h<100) return 20; 
    else if (h >101) return 5;
   return 10; 
  }
} //class diagPr(int x1,int y1)

	/*
	данный класс извлекает данные о текущей температуре
	а так же о ее минимуме и максимуме в текущих сутках
	*/
class myGis {
  //данные с моего компа
  int x;
  int y;
  PFont ft1;
  myGis(int x1,int y1) {
    x = x1;
    y = y1;
  } 
  String[] cur = new String[3];
  void display() {
    fill(0, 0, 0); 
	textFont(ft1,12);
    text("cur: " +cur[0], x, y);
	text("min: " +cur[1], x, y+15);
	text("max: " +cur[2], x, y+30);
  }
  void load() {
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
	рисуем кнопки и програмируем отклики на их нажатие
	*/

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
  if (b1.press() == true) sr++;
  if (b2.press() == true) {
    //обновим данные
    diagPr.load();
    myGis.load();
    prognozGis.load();
    getPogoda.load(); 
  } 
} //class button(int x1,int y1,PImage img1)
	/*
		достаем данные о текущей погоде с яндекса
	*/
class getPogoda {
  String[] todayYa = new String[12];
  String op[] = {"погода на:","Текущая температура:","Текущая температура:",
			"Напрвление ветра:","Скорость ветра:","Влажность:",
			"Давление:","Восход солнца:","Заход солнца:",
			"Фаза луны:","Восход луны:","Заход луны:"};
  int x,y;
  getPogoda(int x1,int y1) {
    x = x1;
    y = y1;
  }
  void load() {
	
	boolean pw =false;
	int cp =0;
	String[] user = loadStrings("http://export.yandex.ru/weather-ng/forecasts/27225.xml?"+str(int(random(1000,100000))));
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
 
 String gt(String s) {
 String[] x = split(s,'>');
 String[] x1 = split(x[1],'<');
 return x1[0]; 
}
 void display() {
   fill(0);
		for (int y1=0;y1<9;y1++)  {
			text(getPogoda.op[y1]+getPogoda.todayYa[y1],x,y+(y1*20));
		}
	}
 }	//class getPogoda

class prognozGis {
		//прогноз 4 дня
	int x;
	int y;
		prognozGis (int x1,int y1){
			x = x1;
			y = y1;
		}
	String[] prognoz1 = new String[4];
	
  void display() {
		fill(0,0,0); 
			for (int j=0;j<4;j++) {
				text(prognoz1[j],x,y+(20*j)); 
			}
		fill(255,255,255); 
    }
  
  void load() {
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
  
	String getOs(String s) {
		String dtt[] = {"0","1","2","Дождь","4","Ливень","Снег","Снег","Гроза","Нет данных","Без осадков"};
		String[] list = split(s, '"');
			if (list.length<2) {
			return "";
			}
			else return dtt[int(list[3])];
	} 
} //class prognozGis

	String getTemp(String s) {
		String[] list = split(s, '"');
			if (list.length<2) {
				return "";
			}
			else {
				return list[3]+"..."+list[1];
			} 
	}
		String getDay(String s) {
			String[] list = split(s, '"');
				if (list.length<2) {
					return "";
				}
				else {
					return list[1]+"."+list[3]+"."+list[5]+"  "+list[7]+"h";
				} 
		}
	/*
	рисуем стрелочные часы
	*/
class clock {
  //x,y, координаты центра, r радиус
	int x,y,r;
		clock (int x1,int y1,int r1) {
			x = x1;
			y = y1;
			r = r1; 
		}
 
	void display() {
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
			step(second(),6,0.9);
			int m = minute();
			step(m,6,0.8);
				if (m>30) kt=1.5;
				else kt=0.5;
			step(hour(),30+kt,0.7);
	}

	void step (int ed,float st,float k) {
	//ed = minute,second st = шаг, k длинна стрелки
		pushMatrix();
		translate(x, y);
		rotate(radians(ed*st-90));
		fill(0);
		secSt(0,0,int(r*k));
		popMatrix();
	}

	void secSt(int x,int y,int l) {
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
	void display() {
		for (int i=0;i<10;i++) {
			fill(102,255,102);
			rect(x+12*(i+1),y,10,5);
			fill(255,0,0);
			rect(x+12*(second()%10+1),y,10,5);
		}
		fill(67,193,39);
	}	
}
