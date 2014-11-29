Line following robot
========================

This robot was developed for the [OSHWDem 2014](http://oshwdem.org/ "OSHWDem A Coruña") by Mateo Ramos and Xabi Crespo. It has two functionalities:
+ Obviously, it completes a circuit following a black line based on a PID algorithm.
+ It connects to an Android app through bluetooth and sends data about the wheels speed so that the Android phone can draw the circuit.

This project is divided into three main folders:
+ [Electronics](https://github.com/xcrespo/Line-following-PID-robot/tree/master/Electronics): contains the design of all the PCBs including wheels encoders, infrared sensors and one last board which contains the motor drivers, headers for the Bluetooth board and headers for a Arduino mini pro.
+ [Android/BluetoothManager](https://github.com/xcrespo/Line-following-PID-robot/tree/master/Android/BluetoothManager): contains the Android app in charge of communicating with the robot and print the circuit.
+ [Arduino/robot](https://github.com/xcrespo/Line-following-PID-robot/tree/master/Arduino/robot): contains the Arduino sketch 

![alt text](https://github.com/xcrespo/Line-following-PID-robot/blob/master/media/2014-11-08%2015.34.30.jpg "Robot image")

