import unittest
import filecmp
from concordance import *

class ConcordanceTests(unittest.TestCase):
    def test_01(self):
        conc = Concordance()
        conc.load_stop_table("stop_words.txt")
        conc.load_concordance_table("file1.txt")
        conc.write_concordance("file1_con.txt")
        self.assertTrue(filecmp.cmp("file1_con.txt", "file1_sol.txt"))
        
    def test_02(self):
        conc = Concordance()
        conc.load_stop_table("stop_words.txt")
        conc.load_concordance_table("file2.txt")
        conc.write_concordance("file2_con.txt")
        self.assertTrue(filecmp.cmp("file2_con.txt", "file2_sol.txt"))

    def test_03(self):
        conc = Concordance()
        conc.load_stop_table("stop_words.txt")
        conc.load_concordance_table("declaration.txt")
        conc.write_concordance("declaration_con.txt")
        self.assertTrue(filecmp.cmp("declaration_con.txt", "declaration_sol.txt"))

    def test_04(self):
      conc = Concordance()
      with self.assertRaises(FileNotFoundError):
         conc.load_stop_table("crack.txt")

    def test_05(self):
      conc = Concordance()
      with self.assertRaises(FileNotFoundError):
         conc.load_concordance_table("pipe.txt")

    def test_06(self):
      conc = Concordance()
      conc.load_stop_table("stop_words.txt")
      conc.load_concordance_table("punctuation.txt")
      conc.write_concordance("punctuation_con.txt")
      self.assertTrue(filecmp.cmp("punctuation_con.txt", "punctuation_sol.txt"))

    def test_07(self):
      conc = Concordance() #stop words
      conc.load_stop_table("stop_words.txt")
      conc.load_concordance_table("stop_words.txt")
      conc.write_concordance("empty_stop_con.txt")
      self.assertTrue(filecmp.cmp("empty_stop_con.txt", "punctuation_sol.txt"))

    def test_08(self):
      conc = Concordance()
      conc.load_stop_table("stop_words.txt")
      conc.load_concordance_table("empty.txt")
      conc.write_concordance("empty_con.txt")
      self.assertTrue(filecmp.cmp("empty_con.txt", "punctuation_sol.txt"))


            
if __name__ == '__main__':
    unittest.main()


