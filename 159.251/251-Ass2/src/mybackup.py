#! /usr/bin/python
__author__ = 'Mitchell Osborne & Anthony Crowcroft'

import os
import os.path
import shutil
import hashlib
import pickle
import sys

import time
import logging


class MyBackup:
    def __init__(self):
        self.programName = "myBackup"
        self.archiveDir = os.path.expanduser(os.path.join("~", "Desktop", "Archive"))
        self.objectDir = os.path.join(self.archiveDir, "Objects")
        self.logFileName = os.path.join(self.archiveDir, self.programName + ".log")
        self.indexFileName = os.path.join(self.archiveDir, "index" + ".pkl")
        self.currDir = os.getcwd()
        self.index = {}

        # Create archive folder
        if os.path.exists(self.archiveDir) and os.path.isdir(self.archiveDir):
            try:
                self.setupLogging()
                self.indexFile = open(self.indexFileName, "rb")
                self.index = pickle.load(self.indexFile)
                self.indexFile.close()
                logging.info("Index Loaded")
            except IOError:
                print "Setup Failed to load archive index"
        else:
            try:
                os.makedirs(self.objectDir)
                self.setupLogging()
                logging.info("Archive Created")
                self.indexFile = open(self.indexFileName, "wb")
                pickle.dump(self.index, self.indexFile)
                self.indexFile.close()
            except IOError:
                print "Setup failed to create necessary files"
            print "Archive Files Created"

    def setupLogging(self):
        logging.basicConfig(filename=self.logFileName, filemode='a', level=logging.INFO,
                            format=time.ctime() + " - %(name)s - %(levelname)s - %(message)s")
        self.logger = logging.getLogger(self.programName)
        logging.info("Logging Configured")

    def cleanup(self):
        logging.shutdown()

    def store(self, directory):
        if directory != os.getcwd():
                if not os.path.isabs(directory):
                    directory = os.path.join(os.getcwd(), directory)
                    if not os.path.isdir(directory):
                        print "Invalid Directory"
                        return
        storeRoot = os.path.split(directory)[-1]
        self.logger.info("Scanning " + directory)
        for root, folders, files in os.walk(directory):
            for f in files:
                originalPath = os.path.join(root, f)
                hashValue = hashFile(originalPath)
                if os.path.isfile(originalPath) and not self.ifExists(hashValue):
                    newPath = os.path.join(self.objectDir, hashValue)
                    shutil.copy2(originalPath, newPath)
                    print originalPath[originalPath.index(storeRoot):]
                    self.index[originalPath[originalPath.index(storeRoot):]] = hashValue
                    self.logger.info("Added " + os.path.join(root, f) + " to Archive")
                    print "Added " + os.path.join(root, f) + " to Archive"
                else:
                    print originalPath[originalPath.index(storeRoot):]
                    self.index[originalPath[originalPath.index(storeRoot):]] = hashValue
                    self.logger.info("Added " + os.path.join(root, f) + " to Archive")
                    print "Added " + os.path.join(root, f) + " to Archive"
        try:
            self.indexFile = open(self.indexFileName, "wb")
            pickle.dump(self.index, self.indexFile)
            self.indexFile.close()
            self.logger.info("Index Updated")
        except IOError:
            print "Failed to save index"
            self.logger.warning("Failed to save index")


    def list(self, pattern=None):
        for f in self.index.keys():
            if pattern is not None and pattern not in f:
                continue
            print f

    def test(self):
        print "Testing Archive"
        self.logger.info("Testing Archive")
        countI = 0

        # Collect files stored
        files = os.listdir(self.objectDir)

        # Check values in index with file names
        for key, value in self.index.iteritems():
            if value in files:
                countI += 1
            else:
                print key, "not found in archive"
                self.logger.info("File Not Found")
        print ""

        countF = 0
        files = [os.path.join(self.objectDir, f) for f in files]
        for f in files:
            if hashFile(f) == os.path.basename(f):
                countF += 1
            else:
                print os.path.basename(f), " - Inconsistent Hash"
                self.logger.info(os.path.basename(f) + " - Inconsistent Hash")

        print "Number Of Matching Index: ", countI
        print "Number Of Matching Hash: ", countF


    def get(self, pattern):
        print "Finding Requested File"
        self.logger.info("GET search: " + pattern)
        foundFiles = []
        for f in self.index:
            currentFile = os.path.split(f)
            if pattern in currentFile[-1]:
                foundFiles.append(f)

        if len(foundFiles) == 1:
            toGet = foundFiles[0]
        elif len(foundFiles) > 1:
            # print "Please Select Which File you were wanting to get"
            i = 1
            for file in foundFiles:
                current = os.path.split(file)
                print "%d : %s" % (i, current[-1])
                i += 1
            uInput = int(raw_input('Which File were you looking to get from the Archive?'))
            if uInput > len(foundFiles):
                print "Input out of range"
                return
            toGet = foundFiles[uInput - 1]
        else:
            print "None found"
            return

        restorePath = os.path.join(self.currDir, os.path.split(toGet)[-1])
        if os.path.isfile(restorePath):
            permisson = raw_input('Would you like to overwrite the existing file in this location? Y, N: ')
            if permisson == "y" or "Y":
                archivePath = os.path.join(self.objectDir, self.index[toGet])
                shutil.copy2(archivePath, restorePath)
            elif permisson == "n" or "N":
                print "Overwrite Canceled"
            else:
                print "Get cancelled please use accepted Commands"
        else:
            archivePath = os.path.join(self.objectDir, self.index[toGet])
            shutil.copy2(archivePath, restorePath)

    def restore(self, directory=os.getcwd()):
        inv_index = {v: k for k, v in self.index.iteritems()}
        try:
            if directory != os.getcwd():
                if not os.path.isabs(directory):
                    directory = os.path.join(os.getcwd(), directory)
                    if not os.path.isdir(directory):
                        print "Invalid Directory"
                        return
            print "Restoring archive to ", directory
            self.logger.info("Restoring Archive to " + directory)
            for root, folders, files in os.walk(self.objectDir):
                for f in files:
                    fileName = inv_index[f]
                    shutil.copy2(os.path.join(root, f), os.path.join(directory, fileName))
                    print "Restored ", fileName
                    self.logger.info("Restored " + fileName)
        except IOError:
            print "Failed to restore entire archive"
            self.logger.warn("Failed to restore archive")

    def ifExists(self, hashValue):
        for root, folders, files in os.walk(self.objectDir):
            for f in files:
                if hashValue == f:
                    return True

        return False


def hashFile(filePath):
    fileObject = open(filePath)
    fileContent = fileObject.read()
    hashFile = hashlib.sha1(fileContent)
    hexValue = hashFile.hexdigest()
    return hexValue


if __name__ == '__main__':

    if len(sys.argv) < 2:
        print """
List of commands:

    init - Initialise the archive

    store <directory> - Store all files in the given directory into the archive. Includes files of sub-directories

    list [<pattern>] - Display all files in the archive where the file name matches the given pattern

    test - tests all files in the archive for anomalies

    get <filename or pattern> - Will restore the file if only one file is found.Will list first 50 files containing the pattern.

    restore <directory> - Restores all files in the archive to the given directory
"""
    elif sys.argv[1] == "init":
        backup = MyBackup()
        backup.cleanup()
    elif sys.argv[1] == "store":
        if len(sys.argv) == 2:
            print "Please specify a directory to archive"
        elif len(sys.argv) == 3:
            backup = MyBackup()
            backup.store(sys.argv[2])
            backup.cleanup()
        else:
            print "Invalid Parameters"
    elif sys.argv[1] == "list":
        backup = MyBackup()
        if len(sys.argv) == 2:
            backup.list()
        elif len(sys.argv) == 3:
            backup.list(sys.argv[2])
        else:
            print "Invalid Parameters"
            backup.cleanup()
    elif sys.argv[1] == "test":
        backup = MyBackup()
        backup.test()
        backup.cleanup()
    elif sys.argv[1] == "get":
        if len(sys.argv) == 3:
            backup = MyBackup()
            backup.get(sys.argv[2])
            backup.cleanup()
        else:
            print "Invalid Parameters"
    elif sys.argv[1] == "restore":
        backup = MyBackup()
        if len(sys.argv) == 2:
            backup.restore()
        elif len(sys.argv) == 3:
            backup.restore(sys.argv[2])
        else:
            print "Invalid Parameters"
        backup.cleanup()
    else:
        print "Invalid Command"
