#ifndef __HEADER_H__
#define __HEADER_H__

#include <stdlib.h>
#include <string.h>

#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/file.h>
#include <sys/time.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <pthread.h>
#include <signal.h>

#define NUM_FILES 5
#define NUM_STRINGS 10
#define NUM_CHARS 11
#define NUM_LINES 1024
#define X_POS 7
#define EXIT "sair"
#define IL "il"
#define IE "ie"

#define AAA "aaaaaaaaa\n"
#define BBB "bbbbbbbbb\n"
#define CCC "ccccccccc\n"
#define DDD "ddddddddd\n"
#define EEE "eeeeeeeee\n"
#define FFF "fffffffff\n"
#define GGG "ggggggggg\n"
#define HHH "hhhhhhhhh\n"
#define III "iiiiiiiii\n"
#define JJJ "jjjjjjjjj\n"

#define FILE_NAME "SO2014-x.txt"
#define FILE_NAME_SIZE 13

#define NUM_THREADS_LEITOR 5
#define BUFFER_SIZE 10
#define THREAD_SHARED 0
#define END 0

int open_file(char *filename);
int randomNumber(int max);
int choose_file(char *filename);
int choose_letter();

char letters[NUM_STRINGS][NUM_CHARS] = {AAA,BBB,CCC,DDD,EEE,FFF,GGG,HHH,III,JJJ};

#endif
