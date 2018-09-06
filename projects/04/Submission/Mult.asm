// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)


// Sum R0 times R1 to R2

    @R2
    M=0		// Clear R2
    @count	// Number of sums
    M=0

(LOOP)
    @count
    D=M
    @R0
    D=D-M
    @END
    D;JEQ	// Check if count is less than R0
    
    @R1
    D=M
    @R2
    M=M+D	// Sum R1 and R2 and store at R2
    @count
    M=M+1
    @LOOP
    0;JMP

(END)
    @END
    0;JMP	// End with a infinite loop