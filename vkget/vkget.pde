//**********************************
//*парсим друзей выбраного контакта*
//**********************************
import de.bezier.data.sql.*;
PImage img;
SQLite db;

void setup() {
int level =1;
db = new SQLite( this, "vkget.db" );
db.connect();
//db.query( "delete from vk;");
//db.query( "delete from link;");
db.query( " delete from log;" );
log(db,"Начало процесса поиска...");

String[] user = loadStrings("get.txt");
	for (int j = 0; j < user.length; j++) {
	level =1;
	log(db,"Поиск по списку для "+user[j]+" LEVEL "+level);
		String id = user[j];
		dataMineDb(id,level);
			String[] x1 =getFrend(id) ;
				level =2;
				for (int i=0 ;i<x1.length;i++) {
					log(db,"Поиск друзей для "+user[j]+" обработка "+x1[i]+" LEVEL "+level);
					//insCP(db,String.valueOf(level),x1[i],user[j]);
					dataMineDb(x1[i],level);
				}//for x1
	}//for j
	println("---------------");
	log(db,"окончание процесса поиска...");
	exit();
} //setup()
 
 void dataMineDb(String id,int level) {
 //******************************************
 //*level уровень поиска 1 = от корня		*
 //*собираем инфу по друзьям для ID			*
 //*просто свалим данные в дб без скачивания*
 //******************************************
	String[] h01 	= getAnketa(id,false);
	String[] f 		= getFrend(id);
	//String [] vm 	= new String[10];
	for (int i=0;i<f.length;i++) {
	//****level +1 пошли глядеть друзей - друзей
	//	Arrays.fill(vm,"");
	//***поиск по 3му уровню*******
	String l1 = null; //**некоторые расчеты внутри условия их рез надо вынести за условие
		if (uidExist(db,f[i]) != true & f[i].length()>3 ){
			String[] h1 = getAnketa(f[i],false);
			insdb(db,h1,id);
			println(" added "+h1[0]);
			l1 = h1[0];
		}
		else println(" skipped "+l1);
		//****если не все данные то связь не записываем
		insCP(db,String.valueOf(level),l1,id);
	}
	//*****тут надо фиксировать взаимосвязь******
	println("end " + id);
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
				if (x1.length()>3) x+= x1;
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

void dataMine(String id) {
 //*******************************************
 //*генерация веб страницы на основе данных	
 //*будет переделана из базы
 //*******************************************
	PrintWriter p = createWriter("e:\\vkget\\"+id+".html"); 
	p.println("<html>");
	p.println("		<body>");
	String[] h01 = getAnketa(id,true);
	p.println("<table>");
	p.println("	<tr>");
	p.println("	<td>");
	p.println("	<img src='"+h01[0]+".jpg'/>");
	p.println("</td>");
	p.println("<td>");
	p.println("<b><span style='font-size: 150%;'>"+h01[2]+" "+h01[1]+"</b></span><br>");
	p.println("</td>");
	p.println("	</tr>");
	p.println("</table>");
p.println("<br>");
p.println("			<table border='1' bordercolor ='0'>");
	String[] f = getFrend(id);
	String [] vm = new String[10];
	for (int i=0;i<f.length;i++) {
	p.println("				<tr>");
		String[] h1 = getAnketa(f[i],true);
		Arrays.fill(vm,"");
		for (int j=0;j<h1.length;j++) {
			vm[j] = h1[j];
		}
		p.println("				<td>");
			p.println("<p><a href='"+vm[0]+".html'>"+vm[0]+"</a></p>");
		p.println("				</td>");
		p.println("				<td>");
			p.println(vm[2]);
		p.println("				</td>");
		p.println("				<td>");
			p.println(vm[1]);
		p.println("				</td>");
		p.println("				<td>");
			p.println("<img src='"+h1[0]+".jpg'/>");
		p.println("				</td>");
		p.println("			</tr>");
		if (uidExist(db,h1[0]) != true){
			insdb(db,h1,id);
			println(" added "+h1[0]);
		}
		else println(" skipped "+h1[0]);
	}
	p.println("			</table>");
	p.println("		</body>");
	p.println("</html>");
	p.flush();
	p.close(); //завершаем работу с файлом
	println("end " + id);
	 //выходим из программы
 } //dataMine(String id)
 
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
 //**************************
	if (s.length>2){
        db.query( "INSERT INTO vk (uid,first_name,last_name,puid,bdate,sity,photo) VALUES ('"+s[0]+"','"+s[2]+"','"+s[1]+"','"+id+"','"+s[5]+"','"+s[4]+"','"+s[3]+"');");
		}
 } //insdb(SQLite db,String[] s,String id)
 
 void insCP(SQLite db,String level,String uid,String puid) {
 //**************************
 //**занесем в БД связи		*	
 //**************************
 
 if(uid.length()>3 & puid.length()>3) db.query( "INSERT INTO link (level,puid,cuid) VALUES ('"+level+"','"+puid+"','"+uid+"');");
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