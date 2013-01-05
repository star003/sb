 myGis 	myGis;
 clock1 clock1;
 PFont ft;
 gradysnik gradysnik,gradysnik1,gradysnik2;
 
 strelka strelka;
 //�������
 int m01=0;
 int m02=300000;
 float tt;
 int vect = 0;

void setup() {
		size(210, 330); 
		frameRate(1);
		background(255, 255, 204);
		myGis 	= new myGis(10,280);
		myGis.load();
		tt = int(myGis.tekT()*10);
		clock1 		= new clock1(195,315);
		int sd 		= 20;
		gradysnik 	= new gradysnik(sd,10);
		gradysnik1 	= new gradysnik(sd+70,10);
		gradysnik2 	= new gradysnik(sd+140,10);
		strelka		= new strelka(200,280,15);
	ft = loadFont("Courier-12.vlw");
	textFont(ft,12);
} //void setup()

void draw() {
  //������ 5 ����� ����������� ������
  
  if (millis()-m01 > m02) {
    m01 = millis();
    myGis.load();
	background(255, 255, 204);
	if (int(myGis.tekT()*10)> tt ) {
		vect = 1;
		tt = int(myGis.tekT()*10);
	}
	else if (int(myGis.tekT()*10)< tt ) {
		vect = -1;
		tt = int(myGis.tekT()*10);
	}
	else vect = 0;
  }
	myGis.display();
	clock1.display();
	gradysnik.display(myGis.tekT());
	gradysnik1.display(myGis.minT());
	gradysnik2.display(myGis.maxT());
	 if (vect == -1) {fill(0,153,255);text("D",190,280);strelka.down();}
	 else if (vect == 1 ) {fill(255,0,0);text("U",190,280);strelka.up();}
	 else if (vect == 0 ) {text("N",190,280);strelka.stop();}
	 else {text("er",190,280);}
	 //text(tt,10,330);
	 //text(myGis.tekT(),10,350);
	 //else if (vect == 0 ) strelka.up();
} //void draw()


class myGis {
  //������ � ����� �����
  int x;
  int y;
  PFont ft1;
  myGis(int x1,int y1) {
    x = x1;
    y = y1;
  } 
  String[] cur = new String[3];
  float [] tmp = new float [3];
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
			tmp[j] = float(list[j*2]);
		}
    }
    catch (Exception e) {
      cur[0] = "n/a";
      cur[1] = "n/a";
      cur[2] = "n/a";
    }
  }
  float tekT() {
   return tmp[0];
  } 
  float minT() {
   return tmp[1];
  } 
  float maxT() {
   return tmp[2];
  } 
}//class myGis(int x1,int y1)

class clock1 {
	int x,y;
	clock1 (int x1,int y1) {
		x = x1;
		y = y1;
	}
	void display() {
		
		if (second()%2 == 0) {
			fill(102,255,102);
			rect(x,y,10,5);
		}
		else {		
			fill(255,0,0);
			rect(x,y,10,5);
		}
		fill(67,193,39);
	}	
}

class gradysnik {
 /*
	������ ��������� 
	x , y ���������� �����
	.display(z - �������� �� -40 �� +40)
 */
 int x;
 int y;
 gradysnik(int x1, int y1) {
	x = x1;
	y = y1;
 }
 void display(float z) {
 int sj = 3;
 int hv = 3;
	for (int i=0;i<81;i++) {
		fill(255,255,255);
		rect(x,y+sj*(i+1),12,hv);
			if (i%10 == 0  )  {
				rect(x-3,y+sj*(i+1),17,hv);
				fill(0,0,0);
				text(40-i,x+20,y+sj*(i+1)+5);
			}
			if (z<=0) {
				if (80-i<z+40) {
					zal(z);
					rect(x+3,y+sj*(i+1),5,hv);
				}
			}
			else {
				if (80-i<z+40) {
					zal(z);
					rect(x+3,y+sj*(i+1),5,hv);
				}
			}
	}
		fill(67,193,39);
 }
 void zal(float t) {
	if (t<=-25 ) fill(0,0,102);
	if (t>-25 & t <=-10 ) fill(51,0,255);
	if (t>-9  & t <=-5 )  fill(0,153,255);
	if (t>-5  & t <=0 )   fill(0,204,255);
	if (t>0   & t <=5 )   fill(0,102,0);
	if (t>5   & t <=12 )  fill(51,204,0);
	if (t>12  & t <=20 )  fill(153,204,0);
	if (t>20  & t <=25 )  fill(255,204,0);
	if (t>25 ) 			  fill(204,0,0);
 }
} //class gradysnik 

class strelka {
	/*
		������ ������� .up - ����� , .down ����
		xx(x , y , h - ������ ������� ����.)
	*/
	int x;
	int y;
	int h;
	int w;
	
	strelka (int x1,int y1,int w1) {
		x = x1;
		y = y1;
		h = int(w1*0.3);
		w = w1;
	}
	
	void st(int r) {
		if (r ==1) fill(0,153,255);
		else fill(204,0,0);
			noStroke();
			beginShape();
				vertex(x,y);
				vertex(x+h,y);
				vertex(x+h,y+0.7*w);
				vertex(x+1.5*h,y+0.7*w);
					vertex(x+0.5*h,y+w);
				vertex(x-0.5*h,y+0.7*w);
				vertex(x,y+0.7*w);
				vertex(x,y);
			endShape(CLOSE);
			stroke(0);
	}
	void up() {
		pushMatrix();
		translate(x, y);
		rotate(radians(180));
		x=0;
		y=-1*w;
		st(0);
		popMatrix();
	}
	void down() {
		st(1);
	}
	
	void stop() {
		fill(204,0,0);
		rect(x,y,5,5);
	}
} //class strelka {
