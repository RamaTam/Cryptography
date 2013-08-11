IV ="20814804c1767293b99f1d9cab3bc3e7"
cipher = "ac1e37bfb15599e5f40eef805488281d"

text ="Pay Bob 100$4444"

def xorWith():
    result = "".join([chr(1 ^ 5) if x is "1" else chr(0) for x in text])
    print result.encode('hex') 
    return result

def calculate():
    print "Initial string: " + IV + " | " + cipher
    notHexIV = IV.decode('hex')
    result = "".join([chr(ord(x) ^ ord(y)) for (x, y) in zip(notHexIV, xorWith())])
    print "Result string : " + result.encode('hex') +" | "+ cipher
    
calculate()
