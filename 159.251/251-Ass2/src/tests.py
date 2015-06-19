__author__ = 'cloud202'

import unittest
import mybackup
import shutil
import os.path

class MyTestCase(unittest.TestCase):
    def setUp(self):
        self.backup = mybackup.myBackup()
        self.backup.store(os.path.expanduser(os.path.join("~", "Desktop", "testing")))

    def tearDown(self):
        # self.backup.cleanup()
        f = open(self.backup.logFileName, 'r')
        for line in f.readlines():
            print line

    def test_something(self):
        self.assertEqual(True, True)


if __name__ == '__main__':
    unittest.main()
