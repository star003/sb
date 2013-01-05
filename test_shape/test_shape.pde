 
 strelka strelka,strelka1;
int i=0; 

void setup() {
	size(640,480);
	frameRate(1);
	strelka = new strelka(200,200,15);
	strelka1 = new strelka(250,200,15);
	//fill(100);
}

void draw() {
	background(255);
	i++;
	if (i%2 ==0) strelka1.up();
	else strelka.down();
}

class strelka {
	/*
		рисуем стрелку .up - вверх , .down вниз
		xx(x , y , h - высота стрелки пикс.)
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
	}
	void up() {
		pushMatrix();
		translate(x, y);
		rotate(radians(180));
		x=0;
		y=-1*w;
		st(0);
		popMatrix();
		/*
		print("x "+x);
		print(" y "+y);
		println(" w "+w);
		*/
	}
	void down() {
		st(1);
	}
} //class strelka {
