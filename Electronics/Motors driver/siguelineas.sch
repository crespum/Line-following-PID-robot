EESchema Schematic File Version 2
LIBS:power
LIBS:device
LIBS:transistors
LIBS:conn
LIBS:linear
LIBS:regul
LIBS:74xx
LIBS:cmos4000
LIBS:adc-dac
LIBS:memory
LIBS:xilinx
LIBS:special
LIBS:microcontrollers
LIBS:dsp
LIBS:microchip
LIBS:analog_switches
LIBS:motorola
LIBS:texas
LIBS:intel
LIBS:audio
LIBS:interface
LIBS:digital-audio
LIBS:philips
LIBS:display
LIBS:cypress
LIBS:siliconi
LIBS:opto
LIBS:atmel
LIBS:contrib
LIBS:valves
LIBS:stepper drivers
LIBS:arduino
LIBS:siguelineas-cache
EELAYER 27 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 1 1
Title ""
Date "27 oct 2014"
Rev ""
Comp ""
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
$Comp
L L293D IC1
U 1 1 543979EB
P 4350 4600
F 0 "IC1" H 3950 5500 50  0000 L BNN
F 1 "L293D" H 3950 3600 50  0000 L BNN
F 2 "stepper drivers-DIL16" H 4350 4750 50  0001 C CNN
F 3 "" H 4350 4600 60  0000 C CNN
	1    4350 4600
	1    0    0    -1  
$EndComp
$Comp
L ARDUINO-DEEK-ROBOT U1
U 1 1 543C281A
P 7950 4600
F 0 "U1" H 8650 5950 60  0000 C CNN
F 1 "ARDUINO-DEEK-ROBOT" H 7950 5500 60  0000 C CNN
F 2 "~" H 8650 5950 60  0000 C CNN
F 3 "~" H 8650 5950 60  0000 C CNN
	1    7950 4600
	1    0    0    -1  
$EndComp
$Comp
L CONN_10X2 P1
U 1 1 543C27EB
P 2250 1600
F 0 "P1" H 2250 2150 60  0000 C CNN
F 1 "CONN_10X2" V 2250 1500 50  0000 C CNN
F 2 "" H 2250 1600 60  0000 C CNN
F 3 "" H 2250 1600 60  0000 C CNN
	1    2250 1600
	1    0    0    -1  
$EndComp
Text Label 5150 4300 0    60   ~ 0
M2L
Text Label 5150 4900 0    60   ~ 0
M2R
Text Label 3450 4300 0    60   ~ 0
M1L
Text Label 3450 4900 0    60   ~ 0
M1R
Text Label 1550 1350 0    60   ~ 0
M1L
Text Label 1550 1250 0    60   ~ 0
M1R
Text Label 2750 1250 0    60   ~ 0
M2R
Text Label 2750 1350 0    60   ~ 0
M2L
Text Label 6350 4450 0    60   ~ 0
C1R
Text Label 6350 4250 0    60   ~ 0
C1L
Text Label 6250 5250 0    60   ~ 0
C2L
Text Label 6250 5450 0    60   ~ 0
C2R
Text Label 2750 1150 0    60   ~ 0
GND
$Comp
L GND #PWR01
U 1 1 543D5B74
P 5150 4650
F 0 "#PWR01" H 5150 4650 30  0001 C CNN
F 1 "GND" H 5150 4580 30  0001 C CNN
F 2 "" H 5150 4650 60  0000 C CNN
F 3 "" H 5150 4650 60  0000 C CNN
	1    5150 4650
	1    0    0    -1  
$EndComp
$Comp
L GND #PWR02
U 1 1 543D5BA0
P 3550 4650
F 0 "#PWR02" H 3550 4650 30  0001 C CNN
F 1 "GND" H 3550 4580 30  0001 C CNN
F 2 "" H 3550 4650 60  0000 C CNN
F 3 "" H 3550 4650 60  0000 C CNN
	1    3550 4650
	1    0    0    -1  
$EndComp
$Comp
L GND #PWR03
U 1 1 543D5BDE
P 7950 6500
F 0 "#PWR03" H 7950 6500 30  0001 C CNN
F 1 "GND" H 7950 6430 30  0001 C CNN
F 2 "" H 7950 6500 60  0000 C CNN
F 3 "" H 7950 6500 60  0000 C CNN
	1    7950 6500
	1    0    0    -1  
$EndComp
Text Label 3450 3750 0    60   ~ 0
BAT
Text Label 5100 5300 0    60   ~ 0
BAT
$Comp
L CONN_4 P2
U 1 1 54494802
P 8150 1900
F 0 "P2" V 8100 1900 50  0000 C CNN
F 1 "CONN_4" V 8200 1900 50  0000 C CNN
F 2 "" H 8150 1900 60  0000 C CNN
F 3 "" H 8150 1900 60  0000 C CNN
	1    8150 1900
	0    -1   -1   0   
$EndComp
Text Label 8000 2500 1    60   ~ 0
BAT
Text Label 8100 2500 1    60   ~ 0
GND
Text Label 6850 3650 0    60   ~ 0
PIN2
Text Label 6850 3850 0    60   ~ 0
PIN3
Text Label 6850 4050 0    60   ~ 0
PIN4
Text Label 6850 4650 0    60   ~ 0
PIN7
Text Label 6850 4850 0    60   ~ 0
PIN8
Text Label 9050 5750 0    60   ~ 0
A0
Text Label 9050 5550 0    60   ~ 0
A1
Text Label 9050 5350 0    60   ~ 0
A2
Text Label 1550 1750 0    60   ~ 0
PIN4
Text Label 1550 1650 0    60   ~ 0
PIN7
Text Label 1550 1550 0    60   ~ 0
PIN8
Text Label 9050 3850 0    60   ~ 0
BAT
Text Label 6850 5050 0    60   ~ 0
PIN9
Text Label 6850 5650 0    60   ~ 0
PIN12
Text Label 6850 5850 0    60   ~ 0
PIN13
Text Label 1550 1450 0    60   ~ 0
PIN9
Text Label 1550 1950 0    60   ~ 0
PIN2
Text Label 1550 1850 0    60   ~ 0
PIN3
Text Label 2800 2050 0    60   ~ 0
BAT
Text Label 1550 2050 0    60   ~ 0
GND
$Comp
L GND #PWR04
U 1 1 544B930E
P 1550 2050
F 0 "#PWR04" H 1550 2050 30  0001 C CNN
F 1 "GND" H 1550 1980 30  0001 C CNN
F 2 "" H 1550 2050 60  0000 C CNN
F 3 "" H 1550 2050 60  0000 C CNN
	1    1550 2050
	0    1    1    0   
$EndComp
Text Label 3150 5300 0    60   ~ 0
BAT_MOTORS
Text Label 1400 1150 0    60   ~ 0
BAT_MOTORS
Wire Wire Line
	3350 4300 3750 4300
Wire Wire Line
	3750 4700 3750 4500
Wire Wire Line
	4950 4500 4950 4700
Wire Wire Line
	2650 1150 2950 1150
Wire Wire Line
	4950 4300 5400 4300
Wire Wire Line
	4950 4900 5400 4900
Wire Wire Line
	2650 1250 2950 1250
Wire Wire Line
	1850 1250 1500 1250
Wire Wire Line
	2650 1350 2950 1350
Wire Wire Line
	1850 1350 1500 1350
Wire Wire Line
	5500 5100 4950 5100
Wire Wire Line
	5750 4100 4950 4100
Wire Wire Line
	6200 3150 3150 3150
Wire Wire Line
	3150 3150 3150 5100
Wire Wire Line
	5900 3450 3350 3450
Wire Wire Line
	3150 5100 3750 5100
Wire Wire Line
	3350 3450 3350 4100
Wire Wire Line
	3350 4100 3750 4100
Wire Wire Line
	3750 4900 3350 4900
Wire Wire Line
	5150 4650 5150 4600
Wire Wire Line
	5150 4600 4950 4600
Connection ~ 4950 4600
Wire Wire Line
	3550 4650 3550 4600
Wire Wire Line
	3550 4600 3750 4600
Connection ~ 3750 4600
Wire Wire Line
	7950 6300 7950 6500
Connection ~ 7950 6300
Wire Wire Line
	3750 3900 3650 3900
Wire Wire Line
	3650 3900 3650 3750
Wire Wire Line
	3450 3750 5050 3750
Wire Wire Line
	5050 3750 5050 3900
Connection ~ 3650 3750
Wire Wire Line
	6850 4250 5900 4250
Wire Wire Line
	6850 4450 6200 4450
Wire Wire Line
	5900 4250 5900 3450
Wire Wire Line
	6200 4450 6200 3150
Wire Wire Line
	5750 4100 5750 5450
Wire Wire Line
	8000 2250 8000 2500
Wire Wire Line
	8100 2250 8100 2500
Wire Wire Line
	8200 2250 8200 2850
Wire Wire Line
	8300 2950 8300 2250
Wire Wire Line
	1850 1450 1500 1450
Wire Wire Line
	1850 1550 1500 1550
Wire Wire Line
	1850 1650 1500 1650
Wire Wire Line
	2650 1450 2950 1450
Wire Wire Line
	2650 1550 2950 1550
Wire Wire Line
	2650 1650 2950 1650
Wire Wire Line
	1850 1750 1500 1750
Wire Wire Line
	8250 3050 8250 2950
Wire Wire Line
	8450 2850 8450 3050
Wire Wire Line
	8250 2950 8300 2950
Wire Wire Line
	8200 2850 8450 2850
Wire Wire Line
	5050 3900 4950 3900
Wire Wire Line
	5750 5450 6850 5450
Wire Wire Line
	6850 5250 5500 5250
Wire Wire Line
	5500 5250 5500 5100
Wire Wire Line
	2650 1750 2950 1750
Wire Wire Line
	1400 1150 1850 1150
Wire Wire Line
	1850 2050 1550 2050
Wire Wire Line
	2650 2050 2950 2050
Wire Wire Line
	1850 1850 1500 1850
Wire Wire Line
	1850 1950 1500 1950
Wire Wire Line
	7450 6300 7450 6400
Wire Wire Line
	7450 6400 8450 6400
Connection ~ 7950 6400
Wire Wire Line
	7700 6300 7700 6400
Connection ~ 7700 6400
Wire Wire Line
	8200 6400 8200 6300
Wire Wire Line
	8450 6400 8450 6300
Connection ~ 8200 6400
Wire Wire Line
	4950 5300 5250 5300
Wire Wire Line
	3100 5300 3750 5300
Wire Wire Line
	2650 1950 3150 1950
$EndSCHEMATC
