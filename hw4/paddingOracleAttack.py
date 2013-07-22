#!/usr/bin/env python
import urllib2
import sys

BLOCK_SIZE = 16
TARGET = 'http://crypto-class.appspot.com/po?er='
KNOWN_CIPHERTEXT = "f20bdba6ff29eed7b046d1df9fb7000058b1ffb4210a580f748b4ac714c001bd4a61044426fb515dad3f21f18aa577c0bdf302936266926ff37dbf7035d5eeb4"


#--------------------------------------------------------------
# padding oracle
#--------------------------------------------------------------
class PaddingOracle(object):
    def query(self, q):
        target = TARGET + urllib2.quote(q)    # Create query URL
        req = urllib2.Request(target)         # Send HTTP request to server
        try:
            f = urllib2.urlopen(req)          # Wait for response
        except urllib2.HTTPError, e:          
            print "We got: %d" % e.code       # Print response code
            if e.code == 404 or e.code == 200:
                return True # good padding
            return False # bad padding

    def createNewQuery(self, inHEX, xorBlock):
        xoredText = "".join([chr(ord(x) ^ ord(y)) for (x, y) in zip(xorBlock, inHEX[:-BLOCK_SIZE])])
        return  xoredText + inHEX[-BLOCK_SIZE:] 

    def composeXorText(self, revialedtext, padding, guess):
        paddingtext = chr(0) * (BLOCK_SIZE - padding) + chr(guess ^ padding)

        for char in revialedtext:
            paddingtext +=  chr(padding ^ ord(char)) 

        return paddingtext
        

def decodeBlock(currentcipher):
    dictionary = " etaoinsrhldcumfpgwybvkxjqzQWERTYUIOPASDFGHJKLZXCVBNM.,!/:1234567890\t\n"

    currentcipher = currentcipher.decode('hex')

    po = PaddingOracle()

    result = ""

    for padding in xrange(1, BLOCK_SIZE + 1): # current padding try
        for guess in dictionary: 
                
            textToXorWith = po.composeXorText(result, padding, ord(guess))
            msg = po.createNewQuery(currentcipher, textToXorWith) 
                     
            if po.query(msg.encode('hex')):       # Issue HTTP query with the given argument
                result = guess + result
                print "Found char:::::: [" + guess + "] :::::: \nHEX result:::::: " + result.encode('hex') +"\nRESULT STRING ::::::" + result
                break
                
            if guess is len(dictionary) - 1: 
                print "some Error with algorithm"
                return

    return result

                    
def main():
    
    PLAINTEXT = ""
    hexBlockSize = BLOCK_SIZE * 2

    for block in xrange(1,len(KNOWN_CIPHERTEXT)/hexBlockSize):
        PLAINTEXT += decodeBlock(KNOWN_CIPHERTEXT[(block - 1) * hexBlockSize:(block + 1) * hexBlockSize])
        print "Solution:" + PLAINTEXT

if __name__ == "__main__":
    main()
