#include <SoftwareSerial.h>
#define rxPin 2
#define txPin 3
#define baudrate 74880
String msg;
SoftwareSerial hc05(rxPin ,txPin);
void setup(){
   pinMode(rxPin,INPUT);
  pinMode(txPin,OUTPUT);
  
  Serial.begin(9600);
  Serial.println("ENTER AT Commands:");
  hc05.begin(baudrate);
  Serial.println("AT : Test");
  Serial.println("AT+RESET : Reset");
  Serial.println("AT+VERSION? : Get the soft version");
  Serial.println("AT+ORGL : Restore default status");
  Serial.println("AT+ADDR?  : Get module Bluetooth address");
  Serial.println("AT+NAME=Nom : Set device’s name");
  Serial.println("AT+NAME? : inquire device’s name");
  Serial.println("AT+ROLE?: inquire module rol");
  Serial.println("AT+PSWD? :  inquire- passkey");
  Serial.println("AT+UART? :  inquire- serial parameter");
  
}
void loop(){
      readSerialPort();
      if(msg!="") hc05.println(msg);
      
      if (hc05.available()>0){
          Serial.write(hc05.read());
      }
}
void readSerialPort(){
  msg="";
 while (Serial.available()) {
    delay(10);  
    if (Serial.available() >0) {
        char c = Serial.read();   //gets one byte from serial buffer
        msg += c; //makes the string readString
    }
 }
}
