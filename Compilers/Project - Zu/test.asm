; EXTERN printi
extern	printi
; EXTERN prints
extern	prints
; EXTERN printd
extern	printd
; EXTERN println
extern	println
; EXTERN argc
extern	argc
; EXTERN argv
extern	argv
; EXTERN envp
extern	envp
; TEXT
segment	.text
; ALIGN
align	4
; GLOBAL _main, :function
global	_main:function
; LABEL _main
_main:
; ENTER 8
	push	ebp
	mov	ebp, esp
	sub	esp, 8
; INT 0
	push	dword 0
; LOCAL 8
	lea	eax, [ebp+8]
	push	eax
; STORE
	pop	ecx
	pop	eax
	mov	[ecx], eax
; LOCA -4
	pop	eax
	mov	[ebp-4], eax
; INT 2
	push	dword 2
; DUP
	push	dword [esp]
; LOCAL -4
	lea	eax, [ebp+-4]
	push	eax
; LOAD
	pop	eax
	push	dword [eax]
; MUL
	pop	eax
	imul	dword eax, [esp]
	mov	[esp], eax
; ALLOC
	pop	eax
	sub	esp, eax
; STORE
	pop	ecx
	pop	eax
	mov	[ecx], eax
; TRASH 4
	add	esp, 4
; LOCV 8
	push	dword [ebp+8]
; POP
	pop	eax
; LEAVE
	leave
; RET
	ret
