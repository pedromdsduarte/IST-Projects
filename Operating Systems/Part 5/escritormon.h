#ifndef __ESCRITOR_H__
#define __ESCRITOR_H__

#define NUM_CYCLES 1024

int randomNumber(int max);
int choose_lock_file(char *filename);
void close_unlock_file(int file);
int choose_letter();

#endif
