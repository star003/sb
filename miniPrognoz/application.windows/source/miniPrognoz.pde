////описание моих объектов
diagPr diagPr;
myGis myGis;
button b1;
prognozGis prognozGis;
getPogoda getPogoda;
//таймеры
int m01=0;
int m02=600000;
//заглушка при потере связи
String[] ls = {
  "1:-30", "2:-25", "3:-20", "4:-15", "5:-10", "6:-5", "7:0", "8:5", "9:10", "10:15", "11:20", "12:25", "13:30"
}; 
//управление экраном по умолчанию 1й
byte sr = 1;
PImage pr, nx;
PFont f;

void setup() {
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

void draw() {
  //каждые 10 минут перегружаем данные
  
  if (millis()-m01 >m02) {
    m01 = millis();
    diagPr.load();
    myGis.load();
    prognozGis.load();
    getPogoda.load();
  }
  if (sr>3) sr=1;
  //отображение данных на экране
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
  void load() {
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
    mx = int(g2[0]);
    mn = int(g2[2]);
  } 
  void display() {
    int k=10;
    int sj = 15; //сжатие диаграммы
    if (int(mx) <=150) k = 10;
    if (int(mx) >=151) k = 5;  
    for (int i = 0; i<list.length; i++) {   
      String g = list[list.length-i-1];
      String tm[] = splitTokens(g, ":");
      fill(0, 102, 51);
      rect(sj*(1+i), y, 5, int(tm[1])*-k);
      fill(0, 0, 102);
      if (i%2!=0) {
        if (int(tm[1])>0) text(tm[1], sj*(1+i), y-10+int(tm[1])*-k);
        else text(tm[1], sj*(1+i), y+20+int(tm[1])*-k);

        if (int(tm[1])>0) text(tm[0], sj*(i+1), y+25);
        else text(tm[0], sj*(i+1), y-25);
      }
    }
  }
  String trend(String[] aa) {
    //найдем минимумы и максимумы в массиве и их позиции
    String t1[] = splitTokens(aa[0], ":");
    int mn=int(t1[1])*10;
    int mx=int(t1[1])*10;
    int pmn = 0;
    int pmx = 0;
    for (int i=0 ;i<aa.length;i++) {
      String t[] = splitTokens(aa[i], ":");
      if (int(t[1])*10 >mx) {
        mx = int(t[1])*10;
        pmx = i;
      } 
      if (int(t[1])*10 <mn) {
        mn = int(t[1])*10;
        pmn = i;
      }
    }
    return str(mx)+":"+str(pmx)+":"+str(mn)+":"+str(pmn);
  }
}

class myGis {
  //данные с моего компа
  int x;
  int y;
  String[] cur = new String[3];
  void display() {
    fill(0, 0, 0); 
    text("cur: "+cur[0]+" min: "+cur[1]+" max: "+cur[2], x, y);
  }
  void load() {
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
}


class getPogoda {
  String[] todayYa = new String[12];
  String[] op = new String[12];
  int x,y;
  void load() {
    op[0]="погода на:";
    op[1]="Текущая температура:";
    op[2]="Явления:";
    op[3]="Напрвление ветра:";
    op[4]="Скорость ветра:";
    op[5]="Влажность:";
    op[6]="Давление:";
    op[7]="Восход солнца:";
    op[8]="Заход солнца:";
    op[9]="Фаза луны:";
    op[10]="Восход луны:";
    op[11]="Заход луны:";
  boolean pw =false;
  int cp =0;
  String[] user = loadStrings("http://export.yandex.ru/weather-ng/forecasts/27225.xml?"+str(int(random(1000,100000))));
  
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
}

class prognozGis {
  //прогноз 4 дня
  int x;
  int y;
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
String getOs(String s) {
String[] list = split(s, '"');
if (list.length<2) {
return "";
}
  if(int(list[3])==10) {
    return "Без осадков";
  }
  if(int(list[3])==4) {
    return "Дождь";
  }   
  if(int(list[3])==5) {
    return "Ливень";
  } 
  if(int(list[3])==8) {
    return "Гроза";
  } 
  if(int(list[3])==6 && int(list[3])==7) {
    return "Снег";
  } 
 if(int(list[3])==9) {
    return "Нет данных";
  } 
 else  return list[3];
} 
}
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
