; Steven Kayvanfar
; CHAR_COUNT FUNCTION
;
; R0 - temp results
; R1 - temp results
; r2 - temp results
; r3 - temp results
; R5 - frame pointer
; R6 - stack pointer
; R7 - ret value

.orig x3300
; *************** CHAR_COUNT SETUP *****************************
STR r7, r6, #-2 ; save the ret addr from r7
STR r5, r6, #-3 ; save the dynamic line from r5
ADD R6, R6, #-4 ; update stack pointer 
ADD R5, R6, #0 ; update frame pointer

	
; *************** CHAR_COUNT CODE *****************************
;   if (*str == 0)
LDR R2, R6, #4
LDR r2, r2, #0
BRNP ELSE


;      result = 0;
AND R0, R0, #0
STR R0, R6, #0
BRNZP ENDIF


;   else { 
;      if (*str == c)
ELSE LDR R3, R6, #5
NOT R3, R3
ADD R3, R3, #1
ADD R3, R2, R3
BRNP ELSE2   


;         result = 1;
ADD R3, R3, #1
STR R3, R6, #0
BRNZP ENDELSE


;      else
;         result = 0;
ELSE2   AND R0, R0, #0
        STR R0, R6, #0


;      result += charCount(str+1, c);
; push second parameter
    EndElse LDR R0, R5, #5
    ADD R6, R6, #-1
    STR R0, R6, #0
; push first parameter
ADD R0, R5, #4
LDR R0, R0, #0
ADD R0, R0, #1
ADD R6, r6, #-1
STR R0, R6, #0
; call function
LD R0, CHAR_COUNT
JSRR R0


; *************** CHAR_COUNT RETURN *****************************

	; return result;
    
        ; add ret val to result of caller
        LDR R0, R6, #3
        LDR R1, R6, #0
        ADD R0, r1, R0
        STR r0, r6, #3    
        ADD R6, R6, #3
        ;set ret val to result
ENDIF   LDR R0, r6, #0
        STR r0, r6, #3
        ADD r6, r6, #1
        ; set r5 to frame pointer on stack and pop frame pointer
        LDR r5, r6, #0
        ADD r6, r6, #1
        ; set r7 to ret addr and pop ret addr
        LDR r7, r6, #0
        ADD r6, r6, #1

        RET


CHAR_Count .fill x3300
	.END
