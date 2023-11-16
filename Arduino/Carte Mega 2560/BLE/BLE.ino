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
//  Serial.print("Vitesse de transmission : ");
//  Serial1.print("AT+BAUD?"); // Vitesse Transmission
//  Serial.println(Serial1.readString());
//  Serial.print("Role (0=Esclave ; 1=Maitre) : ");
//  Serial1.print("AT+ROLE?"); // Role
//  Serial.println(Serial1.readString());
  Serial.print("Code Pin: ");
  Serial1.print("AT+PASS?"); // Code PIN
  Serial.println(Serial1.readString());
//  Serial.print("Version : ");
//  Serial1.print("AT+VERS?"); // Version
//  Serial.println(Serial1.readString());
  Serial.print("Adresse MAC: ");
  Serial1.print("AT+ADDR?"); // Température du module
  Serial.println(Serial1.readString());
//  Serial.print("Save Info: ");
//  Serial1.print("AT+SAVE?"); // Température du module
//  Serial.println(Serial1.readString());
  Serial.println("Fin setup");
  delay(2000);
  Serial1.flush();
}

void loop()
{
  
  if(Serial1.available()>0)
  {
    recvChar = Serial1.readString();
    Serial.println(recvChar);
    commande_bluetooth = recvChar;
    Serial.println(commande_bluetooth);
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

  if(commande_bluetooth=="ButtonRecuCapteur")
  {
    Serial.println("Emission vers le telephone");
    emitChar="&";
    emitChar=emitChar+"Valeur 1";
    emitChar=emitChar+"%";
    Serial1.print(emitChar);
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
