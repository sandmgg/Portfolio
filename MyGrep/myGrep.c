
/* Steven Kayvanfar
   Asgn8: MyGrep */
#include <stdio.h>
#include <string.h>
#include "myGrep.h"
#include <stdlib.h>
int main(int argc, char *argv[]) {
    if (argc != 3) {
        printf("myGrep: improper number of arguments\n");
        printf("Usage: ./a.out <filename> <word>\n");
        return 1;
    }
    else {
        int i;
        FILE *infile = fopen(argv[1], "r");
        char line[100];
        int lines = 0;
        char longestLine[100]; 
        char cpyLine[100];
        int length = 0;
        int words = 0;
        int occurrences = 0;
        char *holder;
        Node *head = NULL;
        Node *tail = NULL;
        Node *temp = NULL;
        if (!infile) {
            printf("Unable to open file: %s\n", argv[1]);
            return 1;
        }
        for (i=0; i<3; i++) {
            printf("%s ", argv[i]);
        }
        printf("\n");
        while(fgets(line, 101, infile)) {
            if (strlen(line) -1 > length) {
                length = strlen(line);
                strncpy(longestLine, line, 100);
            }
            words =0;
            strncpy(cpyLine, line, 100);
            holder = strtok(cpyLine, " ,.!?;:\"\\()\n");
            while (holder != NULL) {
                if (strcmp(holder, argv[2])==0) {
                    temp = (Node*)malloc(sizeof(Node));
                    if (temp == NULL) 
                        printf("malloc error");
                    temp->line = (char*)malloc(100*sizeof(char));
                    strncpy(temp->line, line, 100);
                    temp->next = NULL;
                    temp->lineNumb = lines;
                    temp->wordNumb =  words;
                    if (head == NULL) {
                        head = temp;
                        tail = temp;
                    }
                    else {
                        tail->next = temp;
                        tail = temp;
                    }
                    occurrences++;
                }
                holder = strtok(NULL, " ,.!?;:\"\\()\n");
                words++;
            }
            lines++;
        }
        printf("longest line: %s", longestLine);
        printf("num chars: %d\n", length);
        printf("num lines: %d\n", lines); 
        printf("total occurrences of word: %d\n", occurrences);           
        fclose(infile);
        while (head != NULL) {
            printf("line %d; word %d; %s", head->lineNumb, head->wordNumb, head->line); 
            temp = head;
            head = head->next;
            free(temp->line);
            free(temp);
        }
        return 0;   
    }
}
