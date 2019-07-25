from hash_quad import *
import string
import os

class Concordance:

    def __init__(self):
        self.stop_table = None          # hash table for stop words
        self.concordance_table = None   # hash table for concordance

    def load_stop_table(self, filename):
        """ Read stop words from input file (filename) and insert each word as a key into the stop words hash table.
        Starting size of hash table should be 191: self.stop_table = HashTable(191)
        If file does not exist, raise FileNotFoundError"""
        self.stop_table = HashTable(191)
        if os.path.isfile(filename) == False:
            raise FileNotFoundError
        else:
            f = open(filename, "r")
            words = f.read()
            words = words.split()
            #print(words)
            f.close()
            for i in range(len(words)):
                self.stop_table.insert(words[i])
            #print(self.stop_table.table_size)
            #print(self.stop_table.num_items)

    def load_concordance_table(self, filename):
        """ Read words from input text file (filename) and insert them into the concordance hash table, 
        after processing for punctuation, numbers and filtering out words that are in the stop words hash table.
        Do not include duplicate line numbers (word appearing on same line more than once, just one entry for that line)
        Starting size of hash table should be 191: self.concordance_table = HashTable(191)
        If file does not exist, raise FileNotFoundError"""
        self.concordance_table = HashTable(191)
        nums = 1
        if os.path.isfile(filename) == False:
            raise FileNotFoundError
        f = open(filename, "r")
        
        hyphen_list = str.maketrans("-", " ")
        punct_stuff = str.maketrans("","", string.punctuation)
        numb_stuff = str.maketrans("", "", string.digits)
        one_line = f.readline()
        while True:
            if one_line:
                words = one_line.translate(hyphen_list)
                
                words = words.translate(punct_stuff)
                words = words.translate(numb_stuff)
                words = words.lower()
                words = words.split() 
                words = set(words)
                for i in words:
                    if self.stop_table.in_table(i) != True:
##                        boole = False
##                        if self.concordance_table.in_table(words[i]) == True:
##                            g = self.concordance_table.get_index(words[i])
##                            for k in self.concordance_table.hash_table[g].line_num:
##                                if nums == k:
##                                    boole = True
##                        if boole == False:
                            self.concordance_table.insert(i, nums )
                one_line= f.readline()
                nums +=1
            else:
                break
        f.close()
##        for each_line in f:
##            words= each_line
##            words= words.replace('-', ' ')
##            list_words = words.split()    
##            #print(list_words)
##            for l in range(len(list_words)):
##                boo = False
##                good_word = self.punctuation_getter_offer(list_words[l])
##                #print(good_word)
##                for i in range(self.stop_table.table_size):
##                    if self.stop_table.hash_table[i] != None:
##                        #for l in range(len(self.stop_table.hash_table[i].line_num)):
##                        if self.stop_table.hash_table[i].keyword == good_word:
##                                boo = True
##                boole = False
##                if boo == False and good_word != None:
##                    if self.concordance_table.in_table(good_word) == True:
##                        g = self.concordance_table.get_index(good_word)
##                    
##                        for i in self.concordance_table.hash_table[g].line_num:
##                            if num == i:
##                                boole = True
##                if boo == False and boole == False and good_word != None:
##                    self.concordance_table.insert(good_word, num)
##            num +=1
##        f.close()
            
        

    def write_concordance(self, filename):
        """ Write the concordance entries to the output file(filename)
        See sample output files for format."""
        file_contents = {} #my dictionary
        
        for i in range(len(self.concordance_table.hash_table)):
            if self.concordance_table.hash_table[i] != None:
                for l in range(len(self.concordance_table.hash_table[i].line_num)):
                    if l == 0:
                        lines =""
                        lines += str(self.concordance_table.hash_table[i].line_num[l])
                    else:
                            lines = lines + " " + str(self.concordance_table.hash_table[i].line_num[l])
                file_contents[self.concordance_table.hash_table[i].keyword] = lines
        counter = 1
        text= ""
        keys = file_contents.keys()
        keys= sorted(keys)
        #print(keys)
        for x in keys:
            if counter == self.concordance_table.num_items:
                text += x +": " + file_contents[x]
            else:
                text += x + ": " + file_contents[x] +"\n"
                counter +=1
        g = open(filename,'w')
        g.write(text)                
        g.close()
        

##    def punctuation_getter_offer(self, word):
##        dug =""
##        for i in range(len(word)):
##            if word[i] in string.ascii_lowercase:
##                dug += word[i]
##            elif word[i] in string.ascii_uppercase:
##                let = chr(ord(word[i]) +32)
##                word =word.replace(word[i], let)
##                dug += word[i]
##        if dug != "":
##            return dug
##        else:
##            return None
            
        
        
con = Concordance()
con.load_stop_table('stop_words.txt')
con.load_concordance_table("file1.txt")
#print(con.concordance_table.num_items)
bon = Concordance()
bon.load_stop_table('stop_words.txt')
bon.load_concordance_table('file2.txt')
#print(bon.concordance_table.num_items)
