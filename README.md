Project1 - Team 15
==================

This repository contains the required five programs for Project1. 

To compile the main program, enter the following at the command line:

javac Gallhp/*.java

To execute the main program, enter the following at the command line:

java Gallhp.Demo

the program invokes a GUI in which the user can select the program variant
to run from a drop-down menu, and then enter the requested values in
the remaining windows.  Press the "run" button to execute the program.

* * * * * * * * * * * * * * * * * * * * * * * * * * *  * * * *

To compile any of the individual programs, enter the following at the 
command line:

javac <name of program>/*.java

where <name of program> corresponds to any of the four program variants:

Tpdahp
Tpfahp
Twfahp
Tpdohp

Example:  javac Tpdahp/*.java


To execute any of the individual programs, enter the following at the
command line:

java <name of program>  -d # -l # -r # -t # -b #

where <name of program> corresponds to any of the four program variants:

Tpdahp
Tpfahp
Twfahp
Tpdohp

Example: java Tpdahp -d # -t # -b # -l # -r #

* Note: the user must supply the necessary values for the input
parameters as detailed below:

d - an integer value between 1 and 25
t - an integer value between 0 and 100
b - an integer value between 0 and 100
l - an integer value between 0 and 100
r - an integer value between 0 and 100


Example:  java Tpdahp -d 3 -t 100 -b 0 -l 75 -r 50

