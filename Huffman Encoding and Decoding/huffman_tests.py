import unittest
import filecmp
import subprocess
from huffman import *

class TestList(unittest.TestCase):
    def test_cnt_freq(self):
        freqlist	= cnt_freq("file2.txt")
        anslist = [2, 4, 8, 16, 0, 2, 0] 
        self.assertListEqual(freqlist[97:104], anslist)

    def test_create_huff_tree(self):
        freqlist = cnt_freq("file2.txt")
        hufftree = create_huff_tree(freqlist)
        self.assertEqual(hufftree.freq, 32)
        self.assertEqual(hufftree.char, 97)
        left = hufftree.left
        self.assertEqual(left.freq, 16)
        self.assertEqual(left.char, 97)
        right = hufftree.right
        self.assertEqual(right.freq, 16)
        self.assertEqual(right.char, 100)

    def test_create_header(self):
        freqlist = cnt_freq("file2.txt")
        self.assertEqual(create_header(freqlist), "97 2 98 4 99 8 100 16 102 2")

    def test_create_code(self):
        freqlist = cnt_freq("file2.txt")
        hufftree = create_huff_tree(freqlist)
        codes = create_code(hufftree)
        self.assertEqual(codes[ord('d')], '1')
        self.assertEqual(codes[ord('a')], '0000')
        self.assertEqual(codes[ord('f')], '0001')
        self.assertEqual(codes[0], '')

    def test_01_textfile(self):
        with self.assertRaises(FileNotFoundError):
            huffman_encode("file_none.txt", "file_none_out.txt")    
        huffman_encode("file_a.txt", "file_a_out.txt")
        huffman_encode("file_blank.txt", "file_blank_out.txt")
        self.assertTrue(filecmp.cmp("file_a_out.txt","file_a_soln.txt"))
        self.assertTrue(filecmp.cmp("file_blank_out.txt","file_blank_soln.txt"))
        huffman_encode("file1.txt", "file1_out.txt")
        # capture errors by running 'diff' on your encoded file with a *known* solution file
        #err = subprocess.call("diff -wb file1_out.txt file1_soln.txt", shell = True)
        #self.assertEqual(err, 0)
        self.assertTrue(filecmp.cmp("file1_out.txt","file1_soln.txt"))
        huffman_encode("file2.txt", "file2_out.txt")
        self.assertTrue(filecmp.cmp("file2_out.txt","file2_soln.txt"))
        #err = subprocess.call("diff -wb file2_out.txt file2_soln.txt", shell = True)
        #self.assertEqual(err, 0)
        huffman_encode("declaration.txt", "declaration_out.txt")
        self.assertTrue(filecmp.cmp("declaration_out.txt","declaration_soln.txt"))
        #err = subprocess.call("diff -wb declaration_out.txt declaration_soln.txt", shell = True)
        #self.assertEqual(err, 0)
        huffman_encode("multiline.txt", "multiline_out.txt")
        self.assertTrue(filecmp.cmp("multiline_out.txt","multiline_soln.txt"))
        
    def test_parse_header(self):
        freqlist = parse_header("97 3 98 4 99 2")
        anslist = [3, 4, 2, 0] 
        self.assertListEqual(freqlist[97:101], anslist)
        freqlist1 = parse_header("")
        anslist = [0] * 256
        self.assertListEqual(freqlist1, anslist)

    def test_decode(self):
        with self.assertRaises(FileNotFoundError):
            huffman_decode("file_none.txt", "file_none_out.txt")
        huffman_decode("file1_out.txt", "file1_dec_out.txt")
        self.assertTrue(filecmp.cmp("file1_dec_out.txt","file1.txt"))
        huffman_decode("file2_out.txt", "file2_dec_out.txt")
        self.assertTrue(filecmp.cmp("file2_dec_out.txt","file2.txt"))
        huffman_decode("declaration_out.txt", "declaration_dec_out.txt")
        self.assertTrue(filecmp.cmp("declaration_dec_out.txt","declaration.txt"))
        huffman_decode("file_a_out.txt", "file_a_dec_out.txt")
        self.assertTrue(filecmp.cmp("file_a_dec_out.txt","file_a.txt"))
        huffman_decode("file_blank_out.txt", "file_blank_dec_out.txt")
        self.assertTrue(filecmp.cmp("file_blank_dec_out.txt","file_blank.txt"))
        huffman_decode("multiline_out.txt", "multiline_dec_out.txt")
        self.assertTrue(filecmp.cmp("multiline_dec_out.txt", "multiline.txt"))

if __name__ == '__main__': 
   unittest.main()
