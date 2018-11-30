#ifndef __ESCRITOR_H__
#define __ESCRITOR_H__

#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>

#define NUM_FILES 5
#define NUM_CHARS 10
#define X_POS 7



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


int randomNumber(int max);
int choose_file(char *filename);
int choose_letter();
int open_file(char *filename);
#endif
