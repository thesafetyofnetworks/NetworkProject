package DES;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class DES_Method {

    public static String getrandomDESkey(){
        int Min = 1;
        int Max =26 ;
        String bs="";
        String C="";
        int value=1;
        for (int i=1;i<9;i++){
            char a= (char)(64+Min + (int)(Math.random() * ((Max - Min) + 1)));
            value = 1 << 8 | a;              //10进制转为长度为4的2进制字符串
            bs = Integer.toBinaryString(value);
            C += bs.substring(1);
        }
        return C;
    }
    public static String[] Produce_keys(String str) {
        String[]keys=new String[16];
       // str=exchange64_56(str);
        int key_mov[]={1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
        String left=str.substring(0,str.length()/2);
        String right=str.substring(str.length()/2,str.length());
        //0101101100000000000000000001
        //0000000000000000000000000001
        for(int i=0;i<16;i++){
            left=left.substring(key_mov[i],left.length())+left.substring(0,key_mov[i]);
            right=right.substring(key_mov[i],left.length())+right.substring(0,key_mov[i]);
            keys[i]=exchange56_48(left+right);
        }
        return keys;
    }
    private static String exchange64_56(String str){
        int []P={57,49,41,33,25,17,9,1,58,50,42,34,26,18,
                10,2,59,51,43,35,27,19,11,3,60,52,44,36,
                63,55,47,39,31,23,15,7,62,54,46,38,30,22,
                14,6,61,53,45,37,29,21,13,5,28,20,12,4};        //从一开始排序
        StringBuilder s=new StringBuilder(str);
        for(int i=0;i<56;i++)
        {
            s.replace(i,i+1, String.valueOf(str.charAt(P[i]-1)));
        }
        return s.toString().substring(0,56);
    }
    private static String exchange56_48(String str){
        int []P={14,17,11,24,1,5,3,28,15,6,21,10,
                23,19,12,4,26,8,16,7,27,20,13,2,
                41,52,31,37,47,55,30,40,51,45,33,48,
                44,49,39,56,34,53,46,42,50,36,29,32};
        StringBuilder s=new StringBuilder(str);
        for(int i=0;i<48;i++)
        {
            s.replace(i,i+1, String.valueOf(str.charAt(P[i]-1)));
        }
        return s.toString().substring(0,48);
    }
    private static String exchange32_48(String str){
        int []E={
                32,1,2,3,4,5,
                4,5,6,7,8,9,
                8,9,10,11,12,13,
                12,13,14,15,16,17,
                16,17,18,19,20,21,
                20,21,22,23,24,25,
                24,25,26,27,28,29,
                28,29,30,31,32,1
        };
        String s="";
        for(int i=0;i<48;i++)
        {
            s+= String.valueOf(str.charAt(E[i]-1));
        }
        return s;
    }
    private static String exchange(int []E, String str){      //进行P盒置换32-32或者64-64
        String s="";
        for(int i=0;i<E.length;i++)
        {
            s+= String.valueOf(str.charAt(E[i]-1));
        }
        return s;
    }
    private static String Xor(String str1, String str2){
        String s="";
        for(int i=0;i<str1.length();i++)
            if(str1.charAt(i)==str2.charAt(i)){
                s+="0";
            }
            else{
                s+="1";
            }
        return s;
    }
    private static String Fun(String right, String key){     //进行异或及S盒代换
        int[][][] SBox = {
                // S1盒
                {
                        {14, 4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,7},
                        {0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8},
                        {4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0},
                        {15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6,13}
                },
                // S2盒
                {
                        {15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10},
                        {3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5},
                        {0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15},
                        {13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,9}
                },
                // S3盒
                {
                        {10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,8},
                        {13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15, 1},
                        {13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,7},
                        {1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12}
                },
                // S4盒
                {
                        {7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15},
                        {13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,9},
                        {10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,4},
                        {3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14}
                },
                // S5盒
                {
                        {2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9},
                        {14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,6},
                        {4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14},
                        {11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,3}
                },
                // S6盒
                {
                        {12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7, 5,11},
                        {10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11, 3,8},
                        {9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11, 6},
                        {4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13}
                },
                // S7盒
                {
                        {4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1},
                        {13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,6},
                        {1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2},
                        {6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12}
                },
                // S8盒
                {
                        {13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,7},
                        {1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2},
                        {7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8},
                        {2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11}
                }
        };
        //P盒
        int []P={16,7,20,21,29,12,28,17,
                1,15,23,26,5,18,31,10,
                2,8,24,14,32,27,3,9,
                19,13,30,6,22,11,4,25};
        String str=Xor(right,key);
        String strs[]=new String[8];
        String out="";
        for(int i=0;i<8;i++)
            strs[i]=str.substring(i*str.length()/8,(i+1)*str.length()/8);   //将48位数据分组
        int col=0,row=0,value=0;
        String a=""; String bs="";
        for(int i=0;i<8;i++){
            a = String.valueOf(strs[i].charAt(0))+ String.valueOf(strs[i].charAt(strs[i].length()-1));
            row= Integer.valueOf(a,2);
            col= Integer.valueOf(strs[i].substring(1,strs[i].length()-1),2);
            value = 1 << 4 | SBox[i][row][col];              //10进制转为长度为4的2进制字符串
            bs= Integer.toBinaryString(value);
            out+=bs.substring(1);
        }           //得到32位数据
        //P盒置换
        out=exchange(P,out);
        return out;
    }
    public static String Encry(String M, String[]keys) {
        int[] IP = {58, 50, 42, 34, 26, 18, 10, 2,
                60, 52, 44, 36, 28, 20, 12, 4,
                62, 54, 46, 38, 30, 22, 14, 6,
                64, 56, 48, 40, 32, 24, 16, 8,
                57, 49, 41, 33, 25, 17, 9, 1,
                59, 51, 43, 35, 27, 19, 11, 3,
                61, 53, 45, 37, 29, 21, 13, 5,
                63, 55, 47, 39, 31, 23, 15, 7
        };
        int []IP_={40,8,48,16,56,24,64,32,
                39,7,47,15,55,23,63,31,
                38,6,46,14,54,22,62,30,
                37,5,45,13,53,21,61,29,
                36,4,44,12,52,20,60,28,
                35,3,43,11,51,19,59,27,
                34,2,42,10,50,18,58,26,
                33,1,41,9,49,17,57,25
        };
        M=exchange(IP,M);   //IP置换
        String left = M.substring(0, M.length() / 2);
        String right = M.substring(M.length() / 2, M.length());
        String temp="";
        for (int i = 0; i < 16; i++) {
            String str = exchange32_48(right);
            str = Fun(str, keys[i]);      //得到F（right,key）
            temp=right;
            right=Xor(str,left);
            left=temp;
        }
        //1010001111100010111101110111111000111101111011000110011001000000
        //1110111000011100011111000001010100100111011111110011100001001111   置换之后
        //1110111000011100011111000001010100100111011111110011100001001111
        //1010001111100010111101110111111000111101111011000110011001000000
      //  1110111000011100011111000001010100100111011111110011100001001111
        temp=right;
        right=left;
        left=temp;
        //System.out.println("第16轮SW"+left+"\t"+right);
        String C=exchange(IP_,left+right);
        return C;
    }
    private static String charTobyte(String a){
        int value=0;
        String c=""; String bs="";
        int size=a.length();
        for(int i=0;i<size;i++){
            value = 1 << 8 | a.charAt(i);              //10进制转为长度为4的2进制字符串
            bs = Integer.toBinaryString(value);
            c += bs.substring(1);
        }
        return c;
    }
    private static String byteTochar(String a){
        int value=0;
        String c="";
        int b=0;
        int size=a.length()/8;
        for(int i=0;i<size;i++){
            b=(Integer.parseInt(a.substring(i*8,(i+1)*8),2));
            c+= String.valueOf((char)b);
        }
        return c;
    }
    public static String DES_encry(String text, String keys[])throws UnsupportedEncodingException {
      final Base64.Encoder encoder = Base64.getEncoder();
        byte[] textByte = text.getBytes("UTF-8");
        String a = encoder.encodeToString(textByte);
        while(a.length()%8!=0){
            text+=" ";
            textByte = text.getBytes("UTF-8");
            a=encoder.encodeToString(textByte);
        }
        String M=charTobyte(a);
        String C="";
        for(int i=0;i<M.length()/64;i++)
            C+=Encry(M.substring(i*64,(i+1)*64),keys);
        return C;
    }
    public static String DES_dencry(String C, String dekeys[])throws UnsupportedEncodingException {
        final Base64.Decoder decoder = Base64.getDecoder();
        String M="";
        for(int i=0;i<C.length()/64;i++)
            M+=Encry(C.substring(i*64,(i+1)*64),dekeys);
        M=byteTochar(M);
        return new String(decoder.decode(M), "UTF-8");
    }
    public static void main(String args[]) throws UnsupportedEncodingException {
        System.out.println(getrandomDESkey());
        String text = "哈哈哈哈哈哈咯咯咯咯咯咯嘀嘀嘀咯咯咯咯咯咯aaaa";            //1个中文4byte 1个字符2byte
        long startTime = System.currentTimeMillis();
        String keys[]=Produce_keys("11111111111111111111111111111111111111111111111111111000");
        // int coode=M.hashCode();
        System.out.println("    原始明文为"+text);
       String C=DES_encry(text,keys);
        String dekeys[]=new String[16];
        for(int i=0;i<16;i++){
            dekeys[i]=keys[15-i];
        }
        System.out.println("原始密文加密为"+C);
        String M=DES_dencry(C,dekeys);
        System.out.println("    密文解密为"+M);
        System.out.println();
        long endTime = System.currentTimeMillis();
        System.out.println("运行时间:" + (endTime - startTime) + "ms");
    }
}