diag diagS,diagM,diagH;
void setup() {
  size(700, 250);
  background(0);
  frameRate(2);
  diagS = new diag(30, 20, 78,0);
  
}

void draw() {
  background(0);
  fill(0);
  diagS.display(74);
  //diagM.display(minute());
  //diagH.display(hour());
}

class diag {
  int x, y, r, p;
  diag(int x1, int y1, int r1 ,int p1) {
    x = x1; //позиция по Х
    y = y1;
    r = r1; //длянна шкалы
    p = p1; //% от начала на красную шкалу
  }
  void display(int r2) {
    stroke(255);
    rect(x-5, y-5, r*8+5, 30);
    noStroke();
    for (int i =0 ; i<r2;i++) {
      if (i<int(p*r*0.01))  fill(255, 51, 0) ;
      else  fill(51, 255, 0) ;
      rect(x+8*i, y, 5, 20);
      fill(255);
      text(r2,x-25,y+10);
    }
    fill(0);
    noStroke();
  }
}

