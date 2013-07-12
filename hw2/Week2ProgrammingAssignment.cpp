#include <iostream>
#include "cryptopp/aes.h"
#include "cryptopp/modes.h"
#include "cryptopp/hex.h"
#include "cryptopp/filters.h"
#include "cryptopp/cryptlib.h"
#include <string>
#include "stdint.h"
using namespace std;
using namespace CryptoPP;


static const byte key1[] = "140b41b22a29beb4061bda66b6747e14";
static const byte ciphertext1[] = "4ca00ff4c898d61e1edbf1800618fb2828a226d160dad07883d04e008a7897ee2e4b7465d5290d0c0e6c6822236e1daafb94ffe0c5da05d9476be028ad7c1d81";

static const byte ciphertext2[] = "5b68629feb8606f9a6667670b75b38a5b4832d0f26e1ab7da33249de7d4afc48e713ac646ace36e872ad5fb8a512428a6e21364b0c374df45503473c5242a253";

static const byte key2[] = "36f18357be4dbd77f050515c73fcf9f2";
static const byte ciphertext3[] = "69dda8455c7dd4254bf353b773304eec0ec7702330098ce7f7520d1cbbb20fc388d1b0adb5054dbd7370849dbf0b88d393f252e764f1f5f7ad97ef79d59ce29f5f51eeca32eabedd9afa9329";

static const byte ciphertext4[] = "770b80259ec33beb2561358a9f2dc617e46218c0a53cbeca695ae45faa8952aa0e311bde9d4e01726d3184c34451";

static const int sizeOfIV = AES::BLOCKSIZE; //16-byte encryption IV


void encodeHex(const  byte * inString1, byte * inString2, size_t length){
  HexEncoder hexEncoder;
  hexEncoder.Put(inString1,length);
  hexEncoder.MessageEnd();
  hexEncoder.Get(inString2,length*2);
}

void decodeHex(const byte * inString1, byte * inString2, size_t length){
  HexDecoder hexDecoder;
  hexDecoder.Put(inString1,length);
  hexDecoder.MessageEnd();
  hexDecoder.Get(inString2,length/2);
}

void decodeAEX_CBC(const byte* key,const byte* iv, const string& cipher, string& recovered ){
  try{
  
    CBC_Mode< AES >::Decryption d;
    d.SetKeyWithIV(key, sizeOfIV, iv);
    StringSource ss(cipher, true, new StreamTransformationFilter(d, new StringSink(recovered )));
  
  }catch(const CryptoPP::Exception& e ){
    cerr << e.what() << endl;
    exit(1);
  }  
}

void decodeAEX_CTR(const byte* key,const byte* iv, const string& cipher, string& recovered ){
  try{
  
    CTR_Mode< AES >::Decryption d;
    d.SetKeyWithIV(key, sizeOfIV, iv);
    StringSource ss(cipher, true, new StreamTransformationFilter(d, new StringSink(recovered )));
  
  }catch(const CryptoPP::Exception& e ){
    cerr << e.what() << endl;
    exit(1);
  }
}


void myDecodeAEX_CTR(const byte* key, byte* iv, byte* cipher, string& recovered, int length){
   
  try{
    byte result[length]; 
    CryptoPP::AES::Encryption e(key, sizeOfIV);

    for(int i = 0;i <= length/sizeOfIV;i++){
      e.ProcessAndXorBlock (iv, cipher + i*sizeOfIV, result + i*sizeOfIV);
      iv[15]++;//this is really bad idea but works for this assignment;p
    }
    
    recovered = string((char*)result, length);

   }catch(const CryptoPP::Exception& e ){
    cerr << e.what() << endl;
    exit(1);
  } 
}

static const string CBC_MODE = "CBC";
static const string CTR_MODE = "CTR";

void decode(const byte* hexKey,const byte* hexciphertext, string mode,int textLength){

  byte key[AES::DEFAULT_KEYLENGTH]; 
  decodeHex(hexKey, key, AES::DEFAULT_KEYLENGTH * 2);
  
  byte ciphertext[textLength];
  decodeHex(hexciphertext, ciphertext, textLength * 2);
  
  string stringcipher = string((char*) (ciphertext + sizeOfIV), (textLength - sizeOfIV));

  string result = "";

  if(mode == CBC_MODE)
    decodeAEX_CBC(key, ciphertext, stringcipher, result);
  else if(mode == CTR_MODE){
    myDecodeAEX_CTR(key, ciphertext, ciphertext + sizeOfIV, result, textLength - sizeOfIV); // my ctr-mode
    //decodeAEX_CTR(key, ciphertext, stringcipher, result); // default crypto++ library version
  }
  cout << "recovered text: " << result << endl;
}



int main( int, char** ) {

  decode(key1, ciphertext1, CBC_MODE, sizeof(ciphertext1)/2);
  decode(key1, ciphertext2, CBC_MODE, sizeof(ciphertext2)/2);

  decode(key2, ciphertext3, CTR_MODE, sizeof(ciphertext3)/2);
  decode(key2, ciphertext4, CTR_MODE, sizeof(ciphertext4)/2); 
  
  return 0;
}


