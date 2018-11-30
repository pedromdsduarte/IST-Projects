all: escritormon monitor leitormon

escritormon: escritormon.c escritormon.h header.h
	gcc -g -Wall -pedantic -o escritormon -pthread escritormon.c 


monitor: monitor2.c 
	gcc -Wall -pedantic -o monitor2 monitor2.c header.h
	

leitormon: leitormon.c
	gcc -pedantic -ansi -Wall -o leitormon -pthread leitormon.c

