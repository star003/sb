import codeanticode.gsvideo.*;
long n = 0;
long x1 = 20;
boolean ps = false;
GSMovie movie;
PFont font;

void setup() {
  frameRate(1);
  size(1000, 600, P3D);
  movie = new GSMovie(this, "h:\\arh\\07-08.avi");
  movie.play();
  font = loadFont("DejaVuSans-24.vlw");
  textFont(font, 24);
}

void movieEvent(GSMovie myMovie) {
  movie.read();
}

void draw() {
  n+=x1;
  background(0);
  if (movie.width > 1 && movie.height > 1) {
    if (abs(movie.time() - movie.duration()) < 0.01) {
      println("Reached the end");
    }
    //movie.speed(60);
    movie.jump(n);
    image(movie, 0, 0,800,600);
    text(x1,10,50);
  }  
}

void keyPressed() {
    if (keyCode == LEFT) {
      n-=2*x1;
    }
    else if (keyCode == RIGHT) {
      n+=2*x1;
    }
    else if (keyCode == DOWN) {
      x1-=1;
      if (x1<1) x1 = 1;      
      println("increase - "+str(x1));
    }
    else if (keyCode == UP) {
      x1+=1;
      if (x1<1) x1 = 1;
      println("increase + "+str(x1));      
    }
    else if (keyCode == ENTER) {
      if (ps == false ) ps = true;
       else ps = false;
       if (ps == true){
        movie.pause();
        println("pause");
      }
      else {
       n-=4*x1; 
      movie.jump(n);
      movie.play();
      movie.goToBeginning();
      println("begin");
      }
     }
 }
