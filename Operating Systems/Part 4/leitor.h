#ifndef __LEITOR_H__
#define __LEITOR_H__

int open_lock_file(char *filename);
void close_unlock_file(file);
int confirma_string(char * buffer, char letters[NUM_STRINGS][NUM_CHARS]);

#endif
