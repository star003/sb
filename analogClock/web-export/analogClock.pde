clock clock;

void setup()
{
  size(400, 400);
  frameRate(1);
  clock = new clock(300,200,50);
}

void draw()
{   
  background(255);
  clock.display();
}

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
    //rect(0,r,3,r/12);
    if (i%3==0) ellipse(0,r,r/10,r/10);
    else ellipse(0,r,r/12,r/12);
    popMatrix();
    }
    ellipse(x,y,r/12,r/12);
    step(second(),6,0.9);
    int m = minute();
    step(m,6,0.8);
    int kt = 0;
    if (m<30) kt=0;
    else kt = 2; 
    step(hour(),30+kt,0.7);
}

void step (int ed,int st,float k) {
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
}

