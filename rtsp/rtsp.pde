//import codeanticode.gsvideo.*;
//GSPipeline pipe;

import codeanticode.gsvideo.*;
import hypermedia.video.*;
GSPipeline pipe;
OpenCV opencv;

void setup() {
        size(640,480);
        pipe = new GSPipeline(this, "vfssrc location=http://192.168.100.176:8080/?action=stream"); 
        pipe.play();
    }

void pipeEvent(GSPipeline pipe) {
  pipe.read();
}

void draw() {
        if (pipe.available()) {
		pipe.read();
            //pipe.play();
            image(pipe, 0, 0,640,480);
      }
}
