#include <SoftwareSerial.h>   //Software Serial Port

#define RxD 2 
#define TxD 3
#define vitesse_arduino 9600
#define vitesse_bluetooth 9600

SoftwareSerial blueToothSerial(RxD,TxD);

String recvChar;
String emitChar;
String SaveChar;
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
//  Serial.print("Vitesse de transmission : ");
//  blueToothSerial.print("AT+BAUD?"); // Vitesse Transmission
//  Serial.println(blueToothSerial.readString());
//  Serial.print("Role (0=Esclave ; 1=Maitre) : ");
//  blueToothSerial.print("AT+ROLE?"); // Role
//  Serial.println(blueToothSerial.readString());
  Serial.print("Code Pin: ");
  blueToothSerial.print("AT+PASS?"); // Code PIN
  Serial.println(blueToothSerial.readString());
//  Serial.print("Version : ");
//  blueToothSerial.print("AT+VERS?"); // Version
//  Serial.println(blueToothSerial.readString());
  Serial.print("Adresse MAC: ");
  blueToothSerial.print("AT+ADDR?"); // Température du module
  Serial.println(blueToothSerial.readString());
//  Serial.print("Save Info: ");
//  blueToothSerial.print("AT+SAVE?"); // Température du module
//  Serial.println(blueToothSerial.readString());
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
     Serial.print("longeur chaine : ");
     Serial.println(lenghtChar);
     Serial.print("Chaine de base : ");
     Serial.println(recvChar);
     SaveChar = recvChar;

     //lorsque CR et LF sont actif
//     recvChar.remove(lenghtChar-2);
//     Serial.print("chaine sans saut : ");
//     Serial.println(recvChar);
//     
//    commande_bluetooth = recvChar;
//    Serial.print("commande ble : ");
//    Serial.println(commande_bluetooth);
//    Serial.print("longeur ble : ");
//    Serial.println(commande_bluetooth.length());
    
    //lorsque CR ou LF sont actif
    recvChar.remove(lenghtChar-1);
    Serial.print("chaine sans saut : ");
    Serial.println(recvChar);
     
    commande_bluetooth = recvChar;
    Serial.print("commande ble : ");
    Serial.println(commande_bluetooth);
    Serial.print("longeur ble : ");
    Serial.println(commande_bluetooth.length());
    
    // Sans CR et LF actif
//    commande_bluetooth = SaveChar;
//    Serial.print("commande ble : ");
//    Serial.println(commande_bluetooth);
//    Serial.print("longeur ble : ");
//    Serial.println(commande_bluetooth.length());
  }
  if(Serial.available()>0)
  {
    emitChar  = Serial.readString();
    Serial.println(emitChar);
    blueToothSerial.print(emitChar);
//    blueToothSerial.write(Serial.read());
//    blueToothSerial.print(Serial.read());
  }

  if(commande_bluetooth=="ButtonRecuCapteur")
  {
    Serial.println("Emission vers le telephone");
    emitChar="Valeur 1 \n";
    blueToothSerial.print(emitChar);
    commande_bluetooth="";
  }
  if(commande_bluetooth=="Button1")
  {
    Serial.println("Actionde la commande Button 1");
    commande_bluetooth="";
  }
  if(commande_bluetooth=="Button2")
  {
    Serial.println("Actionde la commande Button 2");
    commande_bluetooth="";
  }
  if(commande_bluetooth=="Button3")
  {
    Serial.println("Actionde la commande Button 3");
    commande_bluetooth="";
  }
  if(commande_bluetooth=="Button4")
  {
    Serial.println("Actionde la commande Button 4");
    commande_bluetooth="";
  }
}
