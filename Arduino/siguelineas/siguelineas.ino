
#include <avr/interrupt.h>

const int iMotorLeftForward = 5; 
const int iMotorRightForward = 10;
const int iMotorLeftReverse = 6; 
const int iMotorRightReverse = 11; 

const int pinInt0 = 2;
const int pinInt1 = 3;

const int iHighThreshold = 800;
const int iLowThreshold = 120;

int iRace = 0;

// Sensors from left to right
const int iSnsArray[] = {A0, A1, A2, A3, A4, A5};

byte bSerial = 0;
byte bState = 'S'; // Standby
byte bEPos = 0;
byte bCommand = 0;
byte bMode = 3;
byte bTurns[8] = {0};

int iMotorSpeed = 190;

float kp = 15;
float ki = 0.00015;
float kd = 10;

volatile int iTurnsRight;
volatile int iTurnsLeft;

void setup() {
    // sets the MOTORS control pins as outputs:
    pinMode(iMotorLeftForward, OUTPUT);
    pinMode(iMotorRightForward, OUTPUT);
    pinMode(iMotorLeftReverse, OUTPUT);
    pinMode(iMotorRightReverse, OUTPUT);
    digitalWrite(iMotorLeftForward, LOW);
    digitalWrite(iMotorRightForward, LOW);
    digitalWrite(iMotorLeftReverse, LOW);
    digitalWrite(iMotorRightReverse, LOW);
    
    // set the SENSORS pins as inputs
    for(int i=0; i<=5; i++)
      pinMode(iSnsArray[i], INPUT);

    cli();          // disable global interrupts
    
    // TIMER CONFIGURATION
    TCCR2A = 0;              // Set entire TCCR2A register to 0
    TCCR2B = 0;              // Same for TCCR2B
    OCR2A = 256;           // Set CTC to 1s with 1024 prescale
    TCCR2B |= (1 << WGM22);  // Turn on CTC mode  
    TCCR2B |= (1 << CS20);   // Set 1024 prescaler
    TCCR2B |= (1 << CS21);      
    TCCR2B |= (1 << CS22);    
    TIMSK2 |= (1 << OCIE2A); // Enable timer compare interrupt
    
    pinMode(pinInt0, INPUT);        // Enable PIN2 for interrupts (INT0)
    digitalWrite(pinInt0, HIGH);    // Enable pullup resistor
    pinMode(pinInt1, INPUT);        // Enable PIN3 for interrupts (INT0)
    digitalWrite(pinInt1, HIGH);    // Enable pullup resistor
    
    EIMSK |= (1 << INT0);     // Enable external interrupt INT0
    EICRA |= (1 << ISC01);    // Trigger INT0 on falling edge
    EIMSK |= (1 << INT1);     // Enable external interrupt INT1
    EICRA |= (1 << ISC11);    // Trigger INT1 on falling edge
    
    sei();                    // Enable global interrupts
    
    // initialize serial communication at 9600 bits per second:
    Serial.begin(9600);
}

void loop() {    
    
    //if some date is sent, reads it and saves in state
    if(Serial.available() > 0) {
      bSerial = Serial.read();
      switch(bState) {
        case 'S': // Standby
          if (bSerial == 'M') bState = 'M';
          else if (bSerial == 'V') bState = 'V';
          else if (bSerial == 'R') bState = 'R';
          else if (bSerial == 'E') bState = 'E';
          break;
        case 'M': // Robot mode
          bMode = bSerial;
          bState = 'S';
          break;
        case 'R': // Modo RC
          if (bSerial == 'A') vForward();
          else if (bSerial == 'B') vReverse();
          else if (bSerial == 'C') vLeft();
          else if (bSerial == 'D') vRight();
          else if (bSerial == 'E') vStop();
          bState = 'S';
          break;
        case 'V': // Robot max speed parameter
          if (bSerial == 'A') iMotorSpeed = 100;
          else if (bSerial == 'B') iMotorSpeed = 130;
          else if (bSerial == 'C') iMotorSpeed = 160;
          else if (bSerial == 'D') iMotorSpeed = 190;
          else if (bSerial == 'E') iMotorSpeed = 220;
          else if (bSerial == 'F') iMotorSpeed = 255;
          bState = 'S';
          break;/*
        case 'K': // Robot PID parameters
            bTurns[bEPos++] = bSerial;
            if (bEPos == 4) { // Read 4 bytes containing the number of turns
              bEPos = 0;
              bState = 'S';
            }
          }              
          break; */                   
        case 'E': 
          bTurns[bEPos++] = bSerial;
          if (bEPos == 4) { // Read 4 bytes containing the number of turns
            bEPos = 0;
            bState = 'S';
          }
          break;
        default:
          break;
      }
    }  
      
    // MODE 1 : RC
    if (bMode == 1) {
        vMode_RC();
    }
    
    // MODE 2 : Competition simple
    else if (bMode == 2) {
      digitalWrite(13,HIGH);
        vMode_Simple();
    }

    // MODE 2 : Competition PID
    else if (bMode == 3) {
            digitalWrite(13,LOW);

    if(iRace){
        PID_program();
    } else if ((analogRead(iSnsArray[2]) < iLowThreshold) &&
               (analogRead(iSnsArray[3]) < iLowThreshold)) {
        iRace = 1;
    }      
  }       
}
         
float error = 0;
float previousError = 0;
float totalError = 0;

float power = 0;

int PWM_Left = 0;
int PWM_Right = 0;

void PID_program() { 
    int avgSensor = iReadArray();
    
    previousError = error; // save previous error for differential 
    error = avgSensor - map(3500,0,6000,0,1023); // Count how much robot deviate from center
    totalError += error; // Accumulate error for integral
    
    power = (kp*error) + (kd*(error-previousError)) + (ki*totalError);
    
    if( power>iMotorSpeed ) { power = iMotorSpeed; }
    if( power<-1*iMotorSpeed ) { power = -1*iMotorSpeed; }
    if(power<0) // Turn left
    {
      PWM_Right = iMotorSpeed;
      PWM_Left = iMotorSpeed - abs(int(power));
    }
    
    else // Turn right
    {
      PWM_Right = iMotorSpeed - int(power);
      PWM_Left = iMotorSpeed;
    }

    analogWrite(iMotorLeftForward,PWM_Left);
    analogWrite(iMotorRightForward,PWM_Right);   
}

int iLastRead;

int iReadArray() {
    int iRead = 0;
    int iActive = 0;

    for(int i=0; i<=5; i++) {        
        if(analogRead(iSnsArray[i]) < iLowThreshold) {
          iRead += (i+1)*1000;
          iActive++;
        }
    }

    iRead = map(iRead/iActive, 0, 6000, 0, 1023);
    if(!iRead) return iLastRead;
    else {
        iLastRead = iRead;  
        return iRead;
    }
}

void vMode_RC() {
  
}

void vForward() {
   analogWrite(iMotorLeftForward, iMotorSpeed);
   analogWrite(iMotorLeftReverse, 0);
   analogWrite(iMotorRightForward, iMotorSpeed);
   analogWrite(iMotorRightReverse, 0);
}

void vReverse() {
        analogWrite(iMotorLeftForward, 0);
        analogWrite(iMotorLeftReverse, iMotorSpeed);
        analogWrite(iMotorRightForward, 0);
        analogWrite(iMotorRightReverse, iMotorSpeed);
 
}

void vLeft() {
        analogWrite(iMotorLeftForward, 0);
        analogWrite(iMotorLeftReverse, 0);
        analogWrite(iMotorRightForward, iMotorSpeed);
        analogWrite(iMotorRightReverse, 0);
  
}

void vRight() {
        analogWrite(iMotorLeftForward, iMotorSpeed);
        analogWrite(iMotorLeftReverse, 0);
        analogWrite(iMotorRightForward, 0);
        analogWrite(iMotorRightReverse, 0);
   
}

void vStop() {
        analogWrite(iMotorLeftForward, 0);
        analogWrite(iMotorLeftReverse, 0);
        analogWrite(iMotorRightForward, 0);
        analogWrite(iMotorRightReverse, 0);
}

//**********************************************************************
int valorIzq;
int valorDer;

void vMode_Simple() {
  
  valorIzq = analogRead(A2);
  valorDer = analogRead(A3);
  
  if(valorIzq < iLowThreshold)
  {
    giroIzquierda();
  }
  else if(valorDer < iLowThreshold)
  {
    giroDerecha();
  }
}

void giroIzquierda()
{
    analogWrite(iMotorLeftForward, 0);
    analogWrite(iMotorLeftReverse, 0);
    analogWrite(iMotorRightForward, iMotorSpeed);
    analogWrite(iMotorRightReverse, 0);
}


void giroDerecha()
{
    analogWrite(iMotorLeftForward, iMotorSpeed);
    analogWrite(iMotorLeftReverse, 0);
    analogWrite(iMotorRightForward, 0);
    analogWrite(iMotorRightReverse, 0);
}

//**********************************************************************

//INT0 Count number of turns of right motor
ISR(INT0_vect)
{
    iTurnsRight++;
}

//INT0 Count number of turns of left motor
ISR(INT1_vect)
{
    iTurnsLeft++;
}

volatile int interrumpe = 0; 
byte envio[9] = {0xFF,'E','R',0,0,'E','L',0,0};

//Send number of turns of both motors each x ms
ISR(TIMER2_COMPA_vect)
{
  interrumpe++;
  if(interrumpe == 5)
  {
    interrumpe = 0;
     
 
    envio[3] = highByte(iTurnsRight);
    envio[4] = lowByte(iTurnsRight);
    envio[7] = highByte(iTurnsLeft);
    envio[8] = lowByte(iTurnsLeft);
    
    Serial.write(envio,9);
    
    iTurnsRight = 0;
    iTurnsLeft = 0;
  }
}
