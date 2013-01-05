
digit digit;

void setup () {
  size(340,160);
  digit = new digit();
  digit.x1 =20;
  digit.y1 =20;
  digit.w =20;
  }
  
  void draw() {
    background(0);
   String s = clock();
    digit.y1 = 20;
    digit.display(s);
    digit.y1 =80;
    digit.display(dat());
    }
    
    class digit{
      int x1; //начало Х
      int y1; //начало У
      int h; // не исп.
      int w; //высота символа в пикс
      int g; //равно х1
      
    void hor(int x,int y) {
    int h= int(0.3*w);
    fill(0,255,0);
    beginShape();
    vertex(x, y);
    vertex(x+0.15*w, y-0.5*h);
    vertex(x+0.85*w, y-0.5*h);
    vertex(x+w, y);
    vertex(x+0.85*w, y+0.5*h);
    vertex(x+0.15*w, y+0.5*h);
    vertex(x, y); 
    endShape(CLOSE);
      }
      
    void wert(int x,int y) {
    fill(0,255,0);
    int h= int(0.3*w);
    beginShape();
    vertex(x, y);
    vertex(x+0.5*h, y+0.15*w);
    vertex(x+0.5*h, y+0.85*w);
    vertex(x,y+w);
    vertex(x-0.5*h,y+0.85*w);
    vertex(x-0.5*h,y+0.15*w);
    vertex(x, y);
    endShape();
      }
    void sq() {
    fill(255); 
   int h= int(0.3*w); 
    rect(x1-0.47*h,y1-0.47*h,w+h,2*w+h);
    }  
    void point() {
     rect(x1+1.15*w,y1+2*w,0.25*w,0.25*w); 

    }
    
    void twoPoint() {
     rect(x1+0.5*w,y1+0.5*w,0.25*w,0.25*w); 
     rect(x1+0.5*w,y1+1.25*w,0.25*w,0.25*w); 
    }
      
      void d0() {
        hor(x1,y1);
        hor(x1,y1+2*w);
        wert(x1+w,y1); 
        wert(x1+w,y1+w); 
        wert(x1,y1); 
        wert(x1,y1+w); 
        
      }
      
      void d1() {
      wert(x1+w,y1); 
      wert(x1+w,y1+w); 
      }
      
      void d2() {
       hor(x1,y1);
      wert(x1+w,y1); 
       hor(x1,y1+w);
      wert(x1,y1+w); 
       hor(x1,y1+2*w); 
      }
      
      void d3() {
       hor(x1,y1);
      wert(x1+w,y1); 
       hor(x1,y1+w);
      wert(x1+w,y1+w); 
       hor(x1,y1+2*w); 
      }
      void d4() {
       //hor(x1,y1);
      wert(x1+w,y1); 
      wert(x1,y1); 
       hor(x1,y1+w);
      wert(x1+w,y1+w); 
       
      }
      
      void d5() {
       hor(x1,y1);
       wert(x1,y1); 
       hor(x1,y1+w);
       wert(x1+w,y1+w); 
       hor(x1,y1+2*w); 
      }
      
      void d6() {
       hor(x1,y1);
       wert(x1,y1); 
       hor(x1,y1+w);
       wert(x1+w,y1+w); 
       hor(x1,y1+2*w); 
       wert(x1,y1+w);
      }
      void d7() {
      hor(x1,y1);  
      wert(x1+w,y1); 
      wert(x1+w,y1+w); 
      }
     void d8() {
       wert(x1+w,y1); 
       hor(x1,y1);
       wert(x1,y1); 
       hor(x1,y1+w);
       wert(x1+w,y1+w); 
       hor(x1,y1+2*w); 
       wert(x1,y1+w);
      } 
       void d9() {
       wert(x1+w,y1); 
       hor(x1,y1);
       wert(x1,y1); 
       hor(x1,y1+w);
       wert(x1+w,y1+w); 
       hor(x1,y1+2*w); 
       
      } 
      
       void dp() {
        hor(x1,y1+w);
       } 
      
      void display(String s) {
         for (int i=0;i<s.length();i++) {
          x1 =g + int(i*w*1.5)+int(w);
          razr(s.charAt(i));
        }
      }
      
      void razr(char s) {
        if (s=='0') d0();
        if (s=='1') d1();
        if (s=='2') d2();
        if (s=='3') d3();
        if (s=='4') d4();
        if (s=='5') d5();
        if (s=='6') d6();
        if (s=='7') d7();
        if (s=='8') d8();
        if (s=='9') d9();
        if (s=='-') dp();
        if (s=='.') point();
        if (s==':') twoPoint();
      }
    }
    
String clock() {
 
 //text(fwZero(day())+"-"+fwZero(month())+"-"+year(),cpx,cpy); 
 int m=0;
 int h=0;
   m = minute();
   h = hour();
return (fwZero(h)+":"+fwZero(m)+":"+fwZero(second())); 
}

String dat(){
 return fwZero(day())+"-"+fwZero(month())+"-"+year();
}
 
//блок вспомогательных функций  
String fwZero(int z) {
  if (z<10 & z!=0) {
    return "0"+str(z);
  }
    else if (z>=10) {
    return str(z);  
    }
    else if (z==0) {
    return "00";  
    }
    else return "";
}
