#!/usr/bin/env python

from Crypto.Hash import SHA256
import os;

def calcSHA(filename, bufSize):

    with open(filename, 'rb') as f:
        data = f.read()
    
    numofBlocks = len(data)/bufSize
    
    if len(data)%bufSize:
        numofBlocks += 1 
    
    h = ''

    for i in reversed(xrange(0, numofBlocks)):
        currBlock = data[i * bufSize : (i+1) * bufSize] + h 
        h = SHA256.new(currBlock).digest()
        
                
    print ''.join(x.encode('hex') for x in h) #SHA256.new(currBlock).hexdigest()

#03c08f4ee0b576fe319338139c045c89c3e8e9409633bea29442e21425006ea8
calcSHA("video1.mp4", 1024)
calcSHA("video2.mp4", 1024)
