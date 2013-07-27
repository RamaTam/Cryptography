import gmpy2

N1 = gmpy2.mpz("179769313486231590772930519078902473361797697894230657273430081157732675805505620686985379449212982959585501387537164015710139858647833778606925583497541085196591615128057575940752635007475935288710823649949940771895617054361149474865046711015101563940680527540071584560878577663743040086340742855278549092581")

N2 = gmpy2.mpz("648455842808071669662824265346772278726343720706976263060439070378797308618081116462714015276061417569195587321840254520655424906719892428844841839353281972988531310511738648965962582821502504990264452100885281673303711142296421027840289307657458645233683357077834689715838646088239640236866252211790085787877")

N3 = gmpy2.mpz("720062263747350425279564435525583738338084451473999841826653057981916355690188337790423408664187663938485175264994017897083524079135686877441155132015188279331812309091996246361896836573643119174094961348524639707885238799396839230364676670221627018353299443241192173812729276147530748597302192751375739387929")

ciphertext4 = gmpy2.mpz("22096451867410381776306561134883418017410069787892831071731839143676135600120538004282329650473509424343946219751512256465839967942889460764542040581564748988013734864120452325229320176487916666402997509188729971690526083222067771600019329260870009579993724077458967773697817571267229951148662959627934791540")

DEBUG = True

def printValues(A, N, x, p, q, msg=""):
    print msg

    if DEBUG:
        print "A  : " + str(A)
        print "x  : " + str(x)
        print "p  : " + str(p) 
        print "q  : " + str(q)
        print "N  : " + str(N)
        print "Res: " + str(q*p)
        print str(q*p == N) +" solution is:\n"
 
    print str(p) 

def calcXPQ(A,N):
    x = gmpy2.isqrt(A**2 - N)
    p = A - x
    q = A + x
    return x, p, q

#couldn't find default ceil function
def ceilSqrtN(N):
    sqrtN = gmpy2.isqrt_rem(N)
    return sqrtN[0] + 1 if sqrtN[1] else sqrtN[0] 
        
def runPart1(N):
    A = ceilSqrtN(N)
    x, p, q = calcXPQ(A,N)
    printValues(A, N, x, p, q,"1-st solution: ")

def runPart2(N):
    sqrtN = ceilSqrtN(N)
 
    for A in range(sqrtN, sqrtN + 2**20):
        x, p, q = calcXPQ(A,N)
        if gmpy2.mul(p, q) == N:
            printValues(A, N, x,  p, q, "2-nd solution")
            break
        
"""
Couldn't do this part por now
def runPart3(N):
    twoSqrtN = ceilSqrtN(6 * N) + 1 / 8 * gmpy2.isqrt(6)
    sqrtN = ceilSqrtN(N)
    N24 = gmpy2.mul(24, N)

    A = twoSqrtN
    print str(A)
    while A > sqrtN:
        x = gmpy2.sub(gmpy2.mul(4, gmpy2.mul(A, A)), N24)
        
        print " x: "+str(x)
        x = gmpy2.isqrt(x)
        p = A - x
        q = A + x
        if gmpy2.add(3*p, 2*q) == A * 2:
            printValues(A, N, x, p, q, "3-d solution: ")
            break
        A = gmpy2.sub(A, 1)
def runPart3():
    e = 65537
"""
def main():
    runPart1(N1)
    runPart2(N2)
    runPart3(N3)

    
main()
