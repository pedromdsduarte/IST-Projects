#make clean  >& /dev/null ;
#make   >& /dev/null ;
./zu test.zu -g;
yasm -felf32 test.asm
ld -m elf_i386 -o test.exe test.o -lrts
./test.exe