#include <SoftwareSerial.h>   //Software Serial Port

#define RxD 2
#define TxD 3
#define vitesse_arduino 9600
#define vitesse_bluetooth 9600

SoftwareSerial blueToothSerial(RxD,TxD);

String recvChar;
String emitChar;
String commande_bluetooth;

void setup()
{
  Serial.begin(vitesse_arduino);
  pinMode(RxD, INPUT);
  pinMode(TxD, OUTPUT);
  
  Serial.println("Debut setup");
  blueToothSerial.begin(vitesse_bluetooth);
  blueToothSerial.flush();
  blueToothSerial.print("AT");
  Serial.println(blueToothSerial.readString());
  blueToothSerial.print("AT+NAME?"); //Nom Module
  Serial.print("Nom du module : ");
  Serial.println(blueToothSerial.readString());
  Serial.print("Vitesse de transmission : ");
  blueToothSerial.print("AT+BAUD?"); // Vitesse Transmission
  Serial.println(blueToothSerial.readString());
  Serial.print("Parité : ");
  blueToothSerial.print("AT+CHK?"); // Parité 
  Serial.println(blueToothSerial.readString());
  Serial.print("Role (S=Esclave ; M=Maitre) : ");
  blueToothSerial.print("AT+ROLE?"); // Role
  Serial.println(blueToothSerial.readString());
  Serial.print("Code Pin : ");
  blueToothSerial.print("AT+PIN?"); // Code PIN
  Serial.println(blueToothSerial.readString());
  Serial.print("Version : ");
  blueToothSerial.print("AT+VERSION?"); // Version
  Serial.println(blueToothSerial.readString());
  Serial.print("Adresse MAC : ");
  blueToothSerial.print("AT+ADDR?"); // Température du module
  Serial.println(blueToothSerial.readString());
  Serial.print("Temperature du module : ");
  blueToothSerial.print("AT+TEMP?"); // Température du module
  Serial.print(blueToothSerial.readString());
  Serial.println("°C");
  Serial.println("Fin setup");
  delay(2000);
  blueToothSerial.flush();
}

void loop()
{
  
  if(blueToothSerial.available()>0)
  {
     recvChar = blueToothSerial.readString();
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

//    Serial.print(blueToothSerial.read());
//    Serial.write(blueToothSerial.read());
  }
  if(Serial.available()>0)
  {
    emitChar  = Serial.readString();
    Serial.println(emitChar);
    blueToothSerial.print(emitChar);
//    blueToothSerial.write(Serial.read());
//    blueToothSerial.print(Serial.read());
  }
  if(commande_bluetooth.equalsIgnoreCase("ButtonRecuCapteur"))
  {
    Serial.println("Emission vers le telephone");
    emitChar="&";
    emitChar=emitChar+"Valeur 1";
    emitChar=emitChar+"%";
    blueToothSerial.print(emitChar);
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
