//**********************************
//*парсим друзей выбраного контакта*
//**********************************
import de.bezier.data.sql.*;
PImage img;
SQLite db;

void setup() {
int level =1;
db = new SQLite( this, "vkget.db" );
clearDb(db);
String[] user = loadStrings("get.txt");
	for (int i0 = 0; i0 < user.length; i0++) {
	//***уровень 0 идем по списку
	level =0;
	log(db,"Поиск по списку для "+user[i0]+" LEVEL "+level);
	String id = user[i0];
	dataMineDb(id,level);
	String[] x1 =getFrend(id) ;
	int m1 =x1.length;
	for (int i1=0 ;i1<x1.length;i1++) {
		level =1;
		dataMineDb(x1[i1],level);
		insCP(db,String.valueOf(level),x1[i1],id);
		log(db,"Поиск друзей для "+id+" обработка "+x1[i1]+" LEVEL "+level);
		//println("Поиск друзей для "+id+" обработка "+x1[i1]+" LEVEL "+level);
		println("LEVEL "+level+" progress "+i1+" / "+String.valueOf(m1));
		
		String[] x2 =getFrend(x1[i1]) ;
		int m2 =x2.length;
		for (int i2=0 ;i2<x2.length;i2++) {
			level =2;
			dataMineDb(x2[i2],level);
			insCP(db,String.valueOf(level),x2[i2],x1[i1]);
			log(db,"Поиск друзей для "+x1[i1]+" обработка "+x2[i2]+" LEVEL "+level);
			//println("Поиск друзей для "+x1[i1]+" обработка "+x2[i2]+" LEVEL "+level);
			println("LEVEL "+level+" progress "+i2+" / "+String.valueOf(m2));
			/*
				String[] x3 =getFrend(x2[i2]) ;
				int m3 =x3.length;
				for (int i3=0 ;i3<x3.length;i3++) {
					level =3;
					dataMineDb(x3[i3],level);
						insCP(db,String.valueOf(level),x3[i3],x2[i2]);
					log(db,"Поиск друзей для "+x2[i2]+" обработка "+x3[i3]+" LEVEL "+level);
					println("LEVEL "+level+" progress "+i3+" / "+String.valueOf(m3));
				} //i3
				*/
			} //i2
			
	}//for i0
	}
	println("---------------");
	log(db,"окончание процесса поиска...");
	clearDb(db);
	db.close();
	exit();
} //setup()
 
 void dataMineDb(String id,int level) {
 //******************************************
 //*только запишем анкету в базу и все
 //******************************************
	String[] h01 	= getAnketa(id,false);
	String l1 = null; //**некоторые расчеты внутри условия их рез надо вынести за условие
		if (uidExist(db,id) != true & id.length()>1 ){
			String[] h1 = getAnketa(id,false);
			insdb(db,h1,id);
			//println("level " + level+ " added "+h1[0]);
			l1 = h1[0];
		}
		//else println(" skipped "+l1);
 } //dataMineDb(String id)
 
 String[] getFrend(String id) {
 //***************************************
 //* вернет мультистроку с списком ID
 //* друзей 
 //***************************************
		String[] dat =  new String[500];
		String[] user = loadStrings("https://api.vk.com/method/friends.get.xml?uid="+id);
		String x= "";
		int i=0;
		for (int j = 0; j < user.length; j++) {
			if (user[j].indexOf("<uid>")!= -1){
				String x1 = trim(getId(user[j]))+":";
				if (x1.length()>1) x+= x1;
			}
		}
		return split(x,":");
}

String[] getAnketa(String s,Boolean saveFile) {
//****************************************************
//*вернет анкету по ID
//*результат мультистрока
//* если saveFile = true качаем файлы картинок
//****************************************************
	String x="";
	String[] mx = new String[6];
	String[] user = loadStrings("https://api.vk.com/method/users.get.xml?uid="
					+s+"&fields=photo_50,city,sex,bdate&order=name");
		String uid = "";		
		for (int j = 0; j < user.length; j++) {
			if (user[j].indexOf("<uid>")!= -1){
				x		+=gt(user[j])+";";
				uid 	=gt(user[j]);
				mx[0] 	=gt(user[j]); 
			}
			if (user[j].indexOf("<photo_50>")!= -1){
				String g = gt(user[j]);
				x		+= g+";";
				mx[3] 	=g;
				if(saveFile == true) {
				try {
					img = loadImage(g);
					img.save("e:\\vkget\\"+uid+".jpg");
				}
				catch (Exception e) {
				}
				}//if
			}
			if (user[j].indexOf("<first_name>")!= -1){
				x		+=gt(user[j])+";";
				mx[2] 	=gt(user[j]);
			}
			if (user[j].indexOf("<last_name>")!= -1){
				x+=gt(user[j])+";";
				mx[1] =gt(user[j]);
			}
			if (user[j].indexOf("<city>")!= -1){
				x+=gt(user[j])+";";
				mx[4] =gt(user[j]);
			}
			if (user[j].indexOf("<bdate>")!= -1){
				x+=gt(user[j])+";";
				mx[5] =gt(user[j]);
			}
	}
	for (int i =0;i<6;i++) {
	 if(mx[i]==null) mx[i]=" ";
	}
//	Arrays.fill(mx,"");
	return split(mx[0]+";"+mx[1]+";"+mx[2]+";"+mx[3]+";"+mx[4]+";"+mx[5],";");
} //getAnketa(String s,Boolean saveFile)

String getId (String s) {
//***********************************
//*разбор строки uid (не исп.)
//***********************************
	String[][] a = matchAll(s,"[0-9]{1,12}");
	return a[0][0];
} //getId (String s)

String gt(String s) {
//***************************
//*вернет что между тэгами
//***************************
 String[] x = split(s,'>');
 String[] x1 = split(x[1],'<');
 if (x1[0]==null) return "0";
 else return x1[0]; 
} //gt(String s)

String times() {
//*******************************
//*вернет время в строковом виде*
//*******************************
 int m=0;
 int h=0;
   m = minute();
   h = hour();
return (fwZero(h)+":"+fwZero(m)+":"+fwZero(second())); 
} //times()

String dates() {
//******************************
//*вернет дату в строковом виде*
//******************************
 int d=0;
 int m=0;
 int y=0;
   d = day();
   m = month();
   y = year();
return (fwZero(y)+"-"+fwZero(m)+"-"+fwZero(d)); 
} //dates()

String fwZero(int z) {
//*************************************************
//** допишем нуль перед числом если оно менее 10  *
//*************************************************  
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
}//fwZero(int z)

void log(SQLite db,String mes){
		if (mes.length()>2){
        db.query( "INSERT INTO log (message,date,time) VALUES ('"+mes+"','"+dates()+"','"+times()+"');" );
		}
 } //insdb(SQLite db,String[] s,String id)
 
 void insdb(SQLite db,String[] s,String id) {
 //**************************
 //**занесем в БД анкету	*	
 //*пойдем вдоль на Н уровней
 //**************************
	if (s.length>2){
        db.query( "INSERT INTO vk (uid,first_name,last_name,puid,bdate,sity,photo) VALUES ('"+s[0]+"','"+s[2]+"','"+s[1]+"','"+id+"','"+s[5]+"','"+s[4]+"','"+s[3]+"');");
		}
 } //insdb(SQLite db,String[] s,String id)
 
 void insCP(SQLite db,String level,String uid,String puid) {
 //**************************
 //**занесем в БД связи		*	
 //**************************
 try {
 if(uid.length()>1 & puid.length()>1) db.query( "INSERT INTO link (level,puid,cuid) VALUES ('"+level+"','"+puid+"','"+uid+"');");
 }
 catch (Exception e) {}
 } //insCP(SQLite db,String level,String uid,String puid)
 
 boolean uidExist(SQLite db,String uid){
 //**************************************
 //*проверим uid по базе если есть то	*
 //*истина иначе ложь					*
 //**************************************
 int i = 0;
 db.query( "SELECT uid FROM vk where uid='"+uid+"'" );
 while (db.next()) {
		i++;
	}
 if (i>0) return true;
	else return false;
 } //uidExist(SQLite db,String uid)
 
 boolean linkExist(SQLite db,String uid,String puid,String level){
 //**************************************
 //*проверим uid по базе если есть то	*
 //*истина иначе ложь					*
 //**************************************
 int i = 0;
 db.query( "SELECT level FROM link WHERE cuid='"+uid+"' and puid='"+puid+"' and level='"+level+"'");
 while (db.next()) {
		i++;
	}
 if (i>0) return true;
	else return false;
 } //uidExist(SQLite db,String uid)
 
 void clearDb(SQLite db) {
 db.connect();
//*******чистим дубли связей********
db.query( " delete from dulink;" );
//db.connect();
db.query( "INSERT INTO dulink ( puid, cuid, level ) "
			+"SELECT link.puid, link.cuid, link.level "
			+"FROM link "
			+"GROUP BY link.puid, link.cuid, link.level;");
//db.connect();			
db.query( "delete from link;");
//db.connect();
db.query( "INSERT INTO link ( puid, cuid, level ) "
			+"SELECT dulink.puid, dulink.cuid, dulink.level "
			+"FROM dulink;");	
//db.connect();			
db.query( " delete from dulink;" );
//db.connect();
			
//db.query( "delete from vk;");
//db.query( "delete from link;");
db.query( " delete from log;" );

log(db,"Начало процесса поиска...");
db.close();
db.connect();
 }