#define RxD 18 //Broche UART1(Mega) ou D2 (Uno)
#define TxD 19 //Broche UART1(Mega) ou D3 (Uno)
#define vitesse_arduino 9600 //Vitesse de transmission pour le moniteur serie
#define vitesse_bluetooth 9600 // Vitesse de tramision du composant bluetooth 

String recvChar; //Reception de la chaine de caractere depuis le telephone
String SaveChar; // Savegarde de la chaine de caractere recu
String emitChar; // Emission de la chaine de caractere vers le telephone
String commande_bluetooth; //sert lors des comparaison dans les conditions pour actionner les actionneurs

void setup()
{
  Serial.begin(vitesse_arduino); //Initialisation du moniteur serie à la bonne vitesse de transmission
  pinMode(RxD, INPUT);
  pinMode(TxD, OUTPUT);
  
  Serial.println("Debut setup");
  Serial1.begin(vitesse_bluetooth); //Initialisation du bluetooth à la bonne vitesse de transmission
  Serial1.flush(); //Nettoyage des buffers du bluetooth
  Serial1.print("AT"); //Verification que le bluetooth fonctionne doit retourner AT sur le telephone
  Serial.println(Serial1.readString()); //Verification que le bluetooth fonctionne doit retourner OK sur le moniteur Serie
  //Configuration Bluetooth si besoin
//  Serial1.print("AT+NAME?"); //Nom Module
//  Serial.print("Nom du module : ");
//  Serial.println(Serial1.readString());
//  Serial.print("Vitesse de transmission : ");
//  Serial1.print("AT+BAUD?"); // Vitesse Transmission
//  Serial.println(Serial1.readString());
//  Serial.print("Parité : ");
//  Serial1.print("AT+CHK?"); // Parité 
//  Serial.println(Serial1.readString());
//  Serial.print("Role (S=Esclave ; M=Maitre) : ");
//  Serial1.print("AT+ROLE?"); // Role
//  Serial.println(Serial1.readString());
//  Serial.print("Code Pin : ");
//  Serial1.print("AT+PIN?"); // Code PIN
//  Serial.println(Serial1.readString());
//  Serial.print("Version : ");
//  Serial1.print("AT+VERSION?"); // Version
//  Serial.println(Serial1.readString());
//  Serial.print("Adresse MAC : ");
//  Serial1.print("AT+ADDR?"); // Température du module
//  Serial.println(Serial1.readString());
//  Serial.print("Temperature du module : ");
//  Serial1.print("AT+TEMP?"); // Température du module
//  Serial.print(Serial1.readString());
//  Serial.println("°C");
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
    
    Sans CR et LF actif
   commande_bluetooth = SaveChar;
   Serial.print("commande ble : ");
   Serial.println(commande_bluetooth);
   Serial.print("longeur ble : ");
   Serial.println(commande_bluetooth.length());
    
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
    emitChar="Valeur 1\n";
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