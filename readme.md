# Text2Hex

A simple little command line java app that allows you to write raw Hex code to a file quickly!

It can read from a Text File, or it can even accept data directly!

So you can use it as a standalone program, or as part of a script!

 
* (This is a early version it needs some cleaning.)
 
## How to use:
 
As with any java application, you can simply run it via command prompt with:

```sh 
java -jar Text2Hex.jar [input type] [data] [output file]
 ```
Here are the arguments:
 
### [Input type]
 
* -f   (Load file)
* -i    (Manual text input)
 
### [Data]

* Path to a file to read, 

 Example "C:\files\mydata.txt" (REQUIRES -f)
 
* Hex data,
 
Example: A1 B2 C3 (or) A4B3C2 (REQUIRES -i)
 
[Output file]
 
* Path to output file.
 
 Example: c:\hexcode.bin
