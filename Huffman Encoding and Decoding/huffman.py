import os
class HuffmanNode:
    def __init__(self, char, freq):
        self.char = char   # stored as an integer - the ASCII character code value
        self.freq = freq   # the freqency associated with the node
        self.left = None   # Huffman tree (node) to the left
        self.right = None  # Huffman tree (node) to the right

    def set_left(self, node):
        self.left = node

    def set_right(self, node):
        self.right = node

def comes_before(a, b):
    """Returns True if tree rooted at node a comes before tree rooted at node b, False otherwise"""
    if a.freq < b.freq:
        return True
    elif a.freq == b.freq:
        if a.char < b.char:
            return True
        else:
            return False
    else:
        return False
    

def combine(a, b):
    """Creates and returns a new Huffman node with children a and b, with the "lesser node" on the left
    The new node's frequency value will be the sum of the a and b frequencies
    The new node's char value will be the lesser of the a and b char ASCII values"""
    comb_freq = a.freq + b.freq
    if a.char < b.char:
        new = HuffmanNode(a.char, comb_freq)
    else:
        new = HuffmanNode(b.char,comb_freq)
    new.set_left(a)
    new.set_right(b)
    return new
    
    
def cnt_freq(filename):
    """Opens a text file with a given file name (passed as a string) and counts the 
    frequency of occurrences of all the characters within that file"""
    with open(filename, "r") as f:
        inpfile = f.read()
##    print (inpfile.read())
    freq_list = [0]*256
    for line in inpfile:
        for chr in line:
            freq_list[ord(chr)] +=1

    return freq_list
            
            

def create_huff_tree(char_freq):
    """Create a Huffman tree for characters with non-zero frequency
    Returns the root node of the Huffman tree"""
    huff_list = []
    for i in range(len(char_freq)):
        huffy = HuffmanNode(i,char_freq[i])
        if char_freq[i] != 0:
            if len(huff_list) == 0:
                huff_list.append(huffy)
            elif len(huff_list) > 0:
                for l in range(len(huff_list)):
                    if comes_before(huffy, huff_list[l]):
                        huff_list.insert(l, huffy)
                        break
                    elif l == len(huff_list) -1:
                        huff_list.append(huffy)
    while len(huff_list) > 1:
        x = huff_list.pop(0)
        y = huff_list.pop(0)
        z = combine(x,y)
        if len(huff_list) == 0:
            huff_list.append(z)
        else:
            for l in range(len(huff_list)):
                if comes_before(z, huff_list[l]):
                    huff_list.insert(l, z)
                    break
                elif l == len(huff_list) -1:
                    huff_list.append(z)
    if len(huff_list) != 0:                
        return huff_list[0]
    else:
        return None
    

def create_code(node):
    """Returns an array (Python list) of Huffman codes. For each character, use the integer ASCII representation 
    as the index into the arrary, with the resulting Huffman code for that character stored at that location"""
    huff_list = [""]*256
    code = ""
    if node == None:
        return None
    return _create_code_helper(node, code, huff_list)
                

def create_header(freqs):
    """Input is the list of frequencies. Creates and returns a header for the output file
    Example: For the frequency list asscoaied with "aaabbbbcc, would return “97 3 98 4 99 2” """
    freq_str = ""
    for i in range(len(freqs)):
        if freqs[i] != 0:
            if freq_str == "":
                freq_str += str(i) + ' ' + str(freqs[i])
            else:
                freq_str += " " + str(i) + " " + str(freqs[i])
    return freq_str
            
                 

def huffman_encode(in_file, out_file):
    """Takes inout file name and output file name as parameters
    Uses the Huffman coding process on the text from the input file and writes encoded text to output file
    Take not of special cases - empty file and file with only one unique character"""
    if os.path.isfile(in_file) == False:
        raise FileNotFoundError
        
    ct_freq = cnt_freq(in_file)
    one_check = 0
    for i in ct_freq:
        if i != 0:
            one_check+=1
    huff_tree = create_huff_tree(ct_freq)
    c_code = create_code(huff_tree)
    header = create_header(ct_freq)
    f = open(in_file, "r")
    inpfile = f.read()
    translate = ""
    for line in inpfile:
        for chr in line:
            translate += c_code[ord(chr)]
    g = open(out_file, "w")
    
    if inpfile != "":
        if one_check == 1:
            g.write(header)
        else:
            g.write(header + '\n' + translate)
    f.close()
    g.close()

def _create_code_helper(node, code, hlist):
    if node.left != None:
        hlist = _create_code_helper(node.left, code + "0", hlist)
    if node.right != None:
        hlist = _create_code_helper(node.right, code + "1", hlist)
        
    else:
        hlist[node.char] = code
    return hlist

def huffman_decode(encoded_file, decode_file):
    if os.path.isfile(encoded_file) == False:
        raise FileNotFoundError
    f = open(encoded_file, "r")
    enc_file = f.readline()
    #enc_file = f.read()
    #x = enc_file.split("\n")
    freq_list = parse_header(enc_file)
    
    one_check = 0
    for i in freq_list:
        if i != 0:
            one_check+=1
    tree = create_huff_tree(freq_list)
    translate = ""
    sec_enc = f.readline()
    char = chr
    ben = tree 
    for char in sec_enc:
        if char == "0":
            if ben.left.left == None:
                translate += chr(ben.left.char)
                ben = tree
            else:
                ben = ben.left
        else:
            if ben.right.right == None:
                translate += chr(ben.right.char)
                ben = tree
            else:
                ben = ben.right
    g = open(decode_file, "w")
    
    if enc_file != "":
        
        if one_check == 1:
            one= ""
            one_freq = tree.freq
            one_char = tree.char
            for i in range(one_freq):
                one += chr(one_char)
            g.write(one)
        else:
            g.write(translate)
    f.close()
    g.close()
    

def parse_header(header_string):
    freq_list = [0] * 256
    x = header_string.split()
    for i in range(int(len(x)/2)):
        freq_list[int(x[2*i])] = int(x[2*i + 1])
    return freq_list
        
        
        

    

