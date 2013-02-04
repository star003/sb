import ipcapture.*;
	PFont f;
	IPCapture cam;
	PImage prevFrame,tek,cur;
	int g	=0;
	float threshold = 20;
	float diff;
	float br=0;
	float br1=0;
void setup() {
  size(640,550);
  background(1);
  cam = new IPCapture(this, "http://192.168.100.176:8080/?action=stream", "", "");
  cam.start();
  prevFrame = createImage(cam.width,cam.height,RGB);
  tek 		= createImage(cam.width,cam.height,RGB);
  f = loadFont( "Courier-12.vlw" );
  textFont(f,24);
}

void draw() {
  if (cam.isAvailable()) {
		if (cam.width > 0) {
			prevFrame 	= cam.get(0,0,cam.width,cam.height); 
			cam.read();
			tek 		= cam.get(0,0,cam.width,cam.height); 
		}
		else {
			cam.read();
			tek 		= cam.get(0,0,cam.width,cam.height); 
			prevFrame 	= tek;
		}
		
	for (int i = 0; i < tek.pixels.length; i++) {
		float r1 = red(tek.pixels[i]); 
		float g1 = green(tek.pixels[i]); 
		float b1 = blue(tek.pixels[i]);
		
		float r2 = red(prevFrame.pixels[i]); 
		float g2 = green(prevFrame.pixels[i]); 
		float b2 = blue(prevFrame.pixels[i]);
		br 	+= brightness(tek.pixels[i]);
		diff 	= dist(r1,g1,b1,r2,g2,b2);
			if  (abs(diff)>threshold) g=255;
			else g = 0;
    }
		br1 = br/tek.pixels.length;
		if (g > 0 ) {
			background(0);
			image(tek,0,0,320,240);
			text(st()+" "+diff+" br "+br1,10,510);
			//tek.save("e:\\cap\\guard\\"+st()+".jpg");
		}
	}
	prevFrame 	= null;
	tek			= null;
	br = 0;
}

/*
void keyPressed() {
  if (key == ' ') {
    if (cam.isAlive()) cam.stop();
    else cam.start();
  }
}
*/

String st() {
   return str(year())+"-"+str(month())+"-"
		+str(day())+"_"+str(hour())+"-"
		+str(minute())+"-"+str(second());
}


