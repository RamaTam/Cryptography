#include <iostream>
#include <string>
#include "cryptopp/aes.h"
#include "cryptopp/hex.h"

// #include "cryptopp/modes.h"
// #include "cryptopp/filters.h"
// #include "cryptopp/cryptlib.h"
// using namespace CryptoPP;

using namespace std;

static const byte key1[] = "140b41b22a29beb4061bda66b6747e14";
static const byte ciphertext1[] = "4ca00ff4c898d61e1edbf1800618fb2828a226d160dad07883d04e008a7897ee2e4b7465d5290d0c0e6c6822236e1daafb94ffe0c5da05d9476be028ad7c1d81";
static const byte ciphertext2[] = "5b68629feb8606f9a6667670b75b38a5b4832d0f26e1ab7da33249de7d4afc48e713ac646ace36e872ad5fb8a512428a6e21364b0c374df45503473c5242a253";

static const byte key2[] = "36f18357be4dbd77f050515c73fcf9f2";
static const byte ciphertext3[] = "69dda8455c7dd4254bf353b773304eec0ec7702330098ce7f7520d1cbbb20fc388d1b0adb5054dbd7370849dbf0b88d393f252e764f1f5f7ad97ef79d59ce29f5f51eeca32eabedd9afa9329";
static const byte ciphertext4[] = "770b80259ec33beb2561358a9f2dc617e46218c0a53cbeca695ae45faa8952aa0e311bde9d4e01726d3184c34451";

static const int ivSize = CryptoPP::AES::BLOCKSIZE; //16-byte encryption IV

void testResults(const byte* hexKey, const byte* hexciphertext,const string mode, const int textLength);

string myDecodeAEX_CBC(const byte* key, const byte* iv, const byte* cipher, const int length);
string myDecodeAEX_CTR(const byte* key, const byte* iv, const byte* cipher, const int length);
string myEncodeAEX_CBC(const byte* key, const byte* iv, const byte* text, const int length);
string myEncodeAEX_CTR(const byte* key, const byte* iv, const byte* text, const int length);

void incrementCounter(byte* iv,const int position);
void xorBlock(byte* one, const byte* two, const int length);

void encodeHex(const byte * inString1, byte * inString2,const size_t length);
void decodeHex(const byte * inString1, byte * inString2,const size_t length);
  
//void decodeAEX_CBC(const byte* key,const byte* iv, const string& ciphertext, string& plaintext);
//void decodeAEX_CTR(const byte* key,const byte* iv, const string& ciphertext, string& plaintext);
//void encodeAEX_CBC(const byte* key,const byte* iv, string& ciphertext, const string& plaintext);
//void encodeAEX_CTR(const byte* key,const byte* iv, string& ciphertext, const string& plaintext);

static const string CBC_MODE = "CBC";
static const string CTR_MODE = "CTR";

int main( int, char** ) {

  testResults(key1, ciphertext1, CBC_MODE, sizeof(ciphertext1)/2);
  testResults(key1, ciphertext2, CBC_MODE, sizeof(ciphertext2)/2);  
  testResults(key2, ciphertext3, CTR_MODE, sizeof(ciphertext3)/2);    
  testResults(key2, ciphertext4, CTR_MODE, sizeof(ciphertext4)/2); 
 

  return 0;
}

void testResults(const byte* hexKey, const byte* hexciphertext, const string mode, const int textLength){
  
  //preparing input data
  byte key[CryptoPP::AES::DEFAULT_KEYLENGTH]; 
  decodeHex(hexKey, key, CryptoPP::AES::DEFAULT_KEYLENGTH * 2);
  
  byte ivAndCipher[textLength];
  decodeHex(hexciphertext, ivAndCipher, textLength * 2);
  
  byte iv [ivSize];
  memcpy(iv, ivAndCipher, ivSize);

  byte* ciphertext = ivAndCipher + ivSize;
  
  int msglength = textLength - ivSize;

  string result = "";

  //testing decoding
  if(mode == CBC_MODE)
    result = myDecodeAEX_CBC(key, iv, ciphertext, msglength);   
  else if(mode == CTR_MODE)
    result = myDecodeAEX_CTR(key, iv, ciphertext, msglength);
  
  cout << "recovered text: " << result << endl;
  
  //testing encoding
  if(mode == CBC_MODE)
    result = myEncodeAEX_CBC(key, iv,(const byte*) result.data(), result.size());   
  else if(mode == CTR_MODE)
    result = myEncodeAEX_CTR(key, iv,(const byte*) result.data(), result.size());
  
  byte hexResult[result.size()*2];
  encodeHex((const byte*)result.data(), hexResult , result.size());

  string input  = string((char*) hexciphertext, textLength * 2);
  string output = string((char*) hexResult, result.size()  * 2);
  cout << "i:" << input << endl << "o:" << output << endl;
  
}

/*
 *
 * My implementation of CBC and CTR encodings 
 *
 */

string myEncodeAEX_CBC(const byte* key, const byte* iv, const byte* text, const int length){
  
  int padding = 0;

  if (length%ivSize!=0)
    padding = length%ivSize;
  else
    padding +=ivSize;
  
  int bufferLength= length + padding;

  byte paddedText [bufferLength];  //same as text, but with padding
  memcpy(paddedText, text, length);
  fill_n(paddedText + length, padding, padding); 

  byte result[ivSize + bufferLength ];
  memcpy(result, iv, ivSize);  
  
  byte* ctr = (byte * )iv;
  
  CryptoPP::AES::Encryption e (key, CryptoPP::AES::DEFAULT_KEYLENGTH);

  for(int i =0;i < bufferLength/ivSize;i++){
    byte* textblock = paddedText + i * ivSize;
    
    xorBlock(textblock, ctr, ivSize);
    e.ProcessBlock (textblock, result + (i + 1) * ivSize);
    ctr = result + (i + 1) * ivSize;
  }

   return string((char*)result, bufferLength+ivSize);
  
}

//wow CTR mode is awesome!;p
string myEncodeAEX_CTR(const byte* key, const byte* iv, const byte* text, const int length){
  return string((char*)iv, ivSize) + myDecodeAEX_CTR(key, iv, text, length);
}

  
/*
 *
 * My implementation of CBC and CTR decodings 
 *
 */

string myDecodeAEX_CBC(const byte* key, const byte* iv, const byte* cipher, const int length){
  
  byte result[length]; 
  int padding = 0;
  byte* ctr = (byte * )iv;
  
  CryptoPP::AES::Decryption e(key, CryptoPP::AES::DEFAULT_KEYLENGTH);
  
  for(int i = 0;i < length/ivSize; i++){
    e.ProcessAndXorBlock (cipher + i * ivSize, ctr, result + i * ivSize);
    
    if(i == length/ivSize - 1)
      padding = result[length - 1];
    
    ctr = (byte*)(cipher + i * ivSize); 
  }

  return string((char*)result, length - padding);

}


string myDecodeAEX_CTR(const byte* key, const byte* iv, const byte* cipher, const int length){
   
  int bufferLength = length;
  if(length%ivSize != 0)
    bufferLength += length%ivSize;
  
  byte result[bufferLength]; 

  byte ctr[ivSize];
  memcpy( ctr,iv, ivSize);

  CryptoPP::AES::Encryption e(key, CryptoPP::AES::DEFAULT_KEYLENGTH);

  for(int i = 0;i < bufferLength/ivSize;i++){     
    e.ProcessAndXorBlock (ctr, cipher + i * ivSize, result + i * ivSize);
    incrementCounter(ctr, ivSize - 1);
    
  }
    
  return string((char*)result, length);
   
}

//CBC-Mode encryption related helper function
void xorBlock(byte* one, const byte* two, const int length){
  for(int i=0;i<length;i++){
    one[i]^=two[i];
  }
}

//CTR-Mode releated helper function to increment counter
void incrementCounter(byte* iv,const int position){
  if (position<0)
    return;

  iv[position]++;

  if(iv[position]==0)
    incrementCounter(iv, position-1);
}

void encodeHex(const  byte * inString1, byte * inString2,const size_t length){
  CryptoPP::HexEncoder hexEncoder;
  hexEncoder.Put(inString1,length);
  hexEncoder.MessageEnd();
  hexEncoder.Get(inString2,length*2);
}

void decodeHex(const byte * inString1, byte * inString2,const size_t length){
  CryptoPP::HexDecoder hexDecoder;
  hexDecoder.Put(inString1,length);
  hexDecoder.MessageEnd();
  hexDecoder.Get(inString2,length/2);
}

/*
 
//
// Default crypto++ style decoding of AEX
//
 

void decodeAEX_CBC(const byte* key,const byte* iv, const string& ciphertext, string& plaintext ){
  try{
  
    CBC_Mode< AES >::Decryption d;
    d.SetKeyWithIV(key, CryptoPP::AES::DEFAULT_KEYLENGTH, iv);
    StringSource ss(ciphertext, true, new StreamTransformationFilter(d, new StringSink(plaintext )));
  
  }catch(const CryptoPP::Exception& e ){
    cerr << e.what() << endl;
    exit(1);
  }  
}

void decodeAEX_CTR(const byte* key,const byte* iv, const string& ciphertext, string& plaintext ){
  try{
  
    CTR_Mode< AES >::Decryption d;
    d.SetKeyWithIV(key, CryptoPP::AES::DEFAULT_KEYLENGTH, iv);
    StringSource ss(ciphertext, true, new StreamTransformationFilter(d, new StringSink(plaintext )));
  
  }catch(const CryptoPP::Exception& e ){
    cerr << e.what() << endl;
    exit(1);
  }
}

  
//
// Default crypto++ style encoding of AEX
//
   

void encodeAEX_CBC(const byte* key,const byte* iv, const string& ciphertext, string& plaintext ){
  try{
  
    CBC_Mode< AES >::Encryption d;
    d.SetKeyWithIV(key, CryptoPP::AES::DEFAULT_KEYLENGTH, iv);
    StringSource ss(ciphertext, true, new StreamTransformationFilter(d, new StringSink(plaintext )));
  
  }catch(const CryptoPP::Exception& e ){
    cerr << e.what() << endl;
    exit(1);
  }  
}

void encodeAEX_CTR(const byte* key,const byte* iv, const string& ciphertext, string& plaintext ){
  try{
  
    CTR_Mode< AES >::Encryption d;
    d.SetKeyWithIV(key, CryptoPP::AES::DEFAULT_KEYLENGTH, iv);
    StringSource ss(ciphertext, true, new StreamTransformationFilter(d, new StringSink(plaintext )));
  
  }catch(const CryptoPP::Exception& e ){
    cerr << e.what() << endl;
    exit(1);
  }
}


*/
