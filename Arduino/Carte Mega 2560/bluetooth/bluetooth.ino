#define RxD 18
#define TxD 19
#define vitesse_arduino 9600
#define vitesse_bluetooth 9600

String recvChar;
String emitChar;
String commande_bluetooth;

void setup()
{
  Serial.begin(vitesse_arduino);
  pinMode(RxD, INPUT);
  pinMode(TxD, OUTPUT);
  
  Serial.println("Debut setup");
  Serial1.begin(vitesse_bluetooth);
  Serial1.flush();
  Serial1.print("AT");
  Serial.println(Serial1.readString());
  Serial1.print("AT+NAME?"); //Nom Module
  Serial.print("Nom du module : ");
  Serial.println(Serial1.readString());
  Serial.print("Vitesse de transmission : ");
  Serial1.print("AT+BAUD?"); // Vitesse Transmission
  Serial.println(Serial1.readString());
  Serial.print("Parité : ");
  Serial1.print("AT+CHK?"); // Parité 
  Serial.println(Serial1.readString());
  Serial.print("Role (S=Esclave ; M=Maitre) : ");
  Serial1.print("AT+ROLE?"); // Role
  Serial.println(Serial1.readString());
  Serial.print("Code Pin : ");
  Serial1.print("AT+PIN?"); // Code PIN
  Serial.println(Serial1.readString());
  Serial.print("Version : ");
  Serial1.print("AT+VERSION?"); // Version
  Serial.println(Serial1.readString());
  Serial.print("Adresse MAC : ");
  Serial1.print("AT+ADDR?"); // Température du module
  Serial.println(Serial1.readString());
  Serial.print("Temperature du module : ");
  Serial1.print("AT+TEMP?"); // Température du module
  Serial.print(Serial1.readString());
  Serial.println("°C");
  Serial.println("Fin setup");
  delay(2000);
  Serial1.flush();
}

void loop()
{
  
  if(Serial1.available()>0)
  {
     recvChar = Serial1.readString();
     int lenghtChar=recvChar.length();
//     Serial.print("longeur chaine : ");
//     Serial.println(lenghtChar);
//     Serial.print("Chaine de base : ");
//     Serial.println(recvChar);
     
     recvChar.remove(lenghtChar-2);
//     Serial.print("chaine sans saut : ");
//     Serial.println(recvChar);
     
    commande_bluetooth = recvChar;
//    Serial.print("commande ble : ");
//    Serial.println(commande_bluetooth);
//    Serial.print("longeur ble : ");
//    Serial.println(commande_bluetooth.length());

//    Serial.print(Serial1.read());
//    Serial.write(Serial1.read());
  }
  if(Serial.available()>0)
  {
    emitChar  = Serial.readString();
    Serial.println(emitChar);
    Serial1.print(emitChar);
//    Serial1.write(Serial.read());
//    Serial1.print(Serial.read());
  }
  if(commande_bluetooth.equalsIgnoreCase("ButtonRecuCapteur"))
  {
    Serial.println("Emission vers le telephone");
    emitChar="&";
    emitChar=emitChar+"Valeur 1";
    emitChar=emitChar+"%";
    Serial1.print(emitChar);
    commande_bluetooth="";
  }
  if(commande_bluetooth.equalsIgnoreCase("Button1"))
  {
    Serial.println("Action de la commande Button 1");
    commande_bluetooth="";
  }
  if(commande_bluetooth.equalsIgnoreCase("Button2"))
  {
    Serial.println("Action de la commande Button 2");
    commande_bluetooth="";
  }
  if(commande_bluetooth.equalsIgnoreCase("Button3"))
  {
    Serial.println("Action de la commande Button 3");
    commande_bluetooth="";
  }
  if(commande_bluetooth.equalsIgnoreCase("Button4"))
  {
    Serial.println("Action de la commande Button 4");
    commande_bluetooth="";
  }
}
