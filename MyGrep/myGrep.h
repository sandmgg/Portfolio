/* Steven Kayvanfar
 * myGrep.h  file */


typedef struct node Node;

struct node {
    char *line;
    int lineNumb;
    int wordNumb;
    Node *next;
};
    
    
