// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

	//set number of words in output
	@8192
	D=A
	@MemOfOut
	M=D
(LOOP)
	//set i=0
	@i
	M=0
	//get and check the keyboard value
	@KBD
	D=M
	//if KBD!=0
	@BLACK
	D,JNE
	
// set the screen to be white
(WHITE)
	// checks if we iterate through the whole screen
	@MemOfOut
	D=M
	@i
	D=D-M
	@LOOP
	D;JEQ
	//set the i'th pixel to white
	@i
	D=M
	@SCREEN
	A=A+D
	M=0
	//i++
	@i
	M=M+1
	//go to WHITE
	@WHITE
	0;JMP

(BLACK)
	//set the screen to be black
	@MemOfOut
	D=M
	@i
	D=D-M
	@LOOP
	D;JEQ
	//set the i'th pixel to black
	@i
	D=M
	@SCREEN
	A=A+D
	M=-1
	//i++
	@i
	M=M+1
	//go to BLACK
	@BLACK
	0;JMP