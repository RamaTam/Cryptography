import gmpy2
p = 13407807929942597099574024998205846127479365820592393377723561443721764030073546976801874298166903427690031858186486050853753882811946569946433649006084171
g = 11717829880366207009516117596335367088558084999998952205599979459063929499736583746670572176471460312928594829675428279466566527115212748467589894601965568
h = 3239475104050450443565264378728065788649097520952449527834792452971981976143292558073856937958553180532878928001494706097394108577585732452307673444020333

B = 2**20
gB = gmpy2.powmod(g, B,p)


class Status:
    prevProgress = -1

    def printProgress(self, x):
        done = 100 * x // B
        if done % 10 and Status.prevProgress is not done:
            Status.prevProgress = done
            print done
    def reset(self):
        Status.prevProgress =-1

def HdivGpowX1(x1):
    # h/g**x1 (mod p)
    denom = gmpy2.invert(gmpy2.powmod(g, x1, p), p)
    return  gmpy2.f_mod (gmpy2.mul(h, denom), p)
     

def GpowBpowX0(x0):
    # (g**B)**x0 (mod p)
    return gmpy2.powmod(gB, x0, p)


def buildHash():

    dic = {}
    s = Status()
    ################# part 1
    print "Building HashMap: " 

    for x1 in range(0, B):
        s.printProgress(x1)
        dic[HdivGpowX1(x1)] = x1

    print "Done Building HashMap len: " + str(len(dic)) + " should be = " + str(B)
    s.reset()

    ################# part 2
    print("Searching Result: ")

    for x0 in range(0, B):
        
        s.printProgress(x0)
        
        if GpowBpowX0(x0) in dic:
            x1 = dic[GpowBpowX0(x0)]
            result = x0 * B + x1
            print "x0: " + str(x0) + " x1: " + str(x1) + "\n RESULT: " + str(result)
            break

    print "Done Searching"

if __name__ == '__main__':
    buildHash()
