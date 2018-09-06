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

(MAINLOOP)
    @count
    M=0		// Helper variable to paint screen

    @KBD
    D=M
    @NOTPRESSED
    D;JEQ
    @PRESSED
    0;JMP

(PRESSED)
    D=0
    @color
    M=!D	// All 16 bits set to 1
    @PAINTLOOP
    0;JMP
(NOTPRESSED)
    @color
    M=0		// All bits set to 0
    @PAINTLOOP
    0;JMP

(PAINTLOOP)
    @count
    D=M
    @SCREEN
    D=D+A
    @screenptr
    M=D

    @color
    D=M		// Set register to right number due color
    @screenptr
    A=M		// Points to right screen register
    M=D		// Set screen register to color

    @count
    MD=M+1
    @8192
    D=A-D
    @PAINTLOOP
    D;JGT
    

    @MAINLOOP
    0;JMP


    