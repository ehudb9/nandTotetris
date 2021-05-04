// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

//init i=0, mult=0
    @i
    M=0
    @mult
    M=0

(LOOP)
//if (i==R0) goto STORE
    @i
    D=M
    @R0
    D=D-M
    @STORE
    D;JEQ

//multiple and store the value

    @mult
    D=M
    @R1
    D=D+M
    @mult
    M=D

//i=i+1
    @i
    M=M+1

//goto loop
    @LOOP
     0;JMP

(STORE)
// after we finish to mult store answer in R2
    @mult
    D=M
    @R2
    M=D
    @END
(END)
    @END
    0;JMP