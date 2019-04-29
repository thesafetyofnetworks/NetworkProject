package RSA;

import com.mysql.cj.protocol.Message;
import com.sun.security.auth.UnixNumericGroupPrincipal;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Random;

public class RSA {
    private static final int ORDER =2048/2;// 随机数的数量级
    private static final BigInteger two =new BigInteger("2");
    private static final BigInteger one =new BigInteger("1");
    public static BigInteger[] getkey(){
        BigInteger[]keys=new BigInteger[3];
        Random rnd = new Random();
        long startTime = System.currentTimeMillis();
        BigInteger temp=BigInteger.probablePrime(ORDER,rnd);
        BigInteger p= temp.multiply(two).add(one);                       //得到p
        while(!MR.isPrime(p)){
            temp=BigInteger.probablePrime(ORDER,rnd);
            p= temp.multiply(two).add(one);
        }
        temp=BigInteger.probablePrime(ORDER,rnd);
        BigInteger q= temp.multiply(two).add(one);                        //得到q
        while(!MR.isPrime(q)){
            temp=BigInteger.probablePrime(ORDER,rnd);
            q= temp.multiply(two).add(one);
        }
        BigInteger n=p.multiply(q);                                          //得到n
        System.out.println("p:"+p);
        System.out.println("q:"+q);
        System.out.println("n : "+n.toString());
        BigInteger Fn=(p.subtract(one)).multiply(q.subtract(one));             //得到欧拉函数值Fn
        System.out.println("Fn : "+Fn.toString());
        BigInteger e=BigInteger.valueOf(65537);
        if(Fn.gcd(e)!=BigInteger.valueOf(1))
        MR.getgcd(Fn);                                      //获取到e
        BigInteger d=MR.Egcd(Fn,e);                          //获取到d
        if(d.compareTo(BigInteger.ZERO)<0){
            d=d.add(Fn);                          //获取到d
        }
        System.out.println("e :"+e);
        System.out.println("d :"+d);
        System.out.println("生成参数正确性验证：");
        System.out.println("gcd(Fn,e)="+(Fn.gcd(e)));
        System.out.println("d*e mod fn="+(d.multiply(e)).mod(Fn));
        keys[0]=e;
        keys[1]=d;
        keys[2]=n;
        return keys;
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
    public static String RSA_encry(String Message,String Pk,String n) throws UnsupportedEncodingException {
      //  final Base64.Encoder encoder = Base64.getEncoder();
     //   byte[] textByte = Message.getBytes("UTF-8");
     //   Message= encoder.encodeToString(textByte);
        Message="11"+charTobyte(Message);
        BigInteger M=new BigInteger(Message);
           BigInteger P=new BigInteger(Pk);
            BigInteger N=new BigInteger(n);
            BigInteger C=M.modPow(P,N);
            return C.toString();
            }
    public static String RSA_dencry(String Message,String Pk,String n) throws UnsupportedEncodingException {
      //  final Base64.Decoder decoder = Base64.getDecoder();
        BigInteger C=new BigInteger(Message);
        BigInteger D=new BigInteger(Pk);
        BigInteger N=new BigInteger(n);
        BigInteger M=C.modPow(D,N);
        String  Mo=byteTochar(M.toString().substring(2));
        return Mo;//new String(decoder.decode(Mo), "UTF-8");
    }
    public static String signature(String Message,String d,String n) throws UnsupportedEncodingException {          //生成签名
        String H= Security.md5digest(Message);   //先获取摘要信息
        String sign=RSA_encry(H,d,n);
        return sign;
    }
    public static boolean verifysign(String sign,String Message, String e, String n) throws UnsupportedEncodingException {          //验证签名
        String H = Security.md5digest(Message);   //先获取摘要信息
        String sign1=RSA_dencry(sign,e,n);         //将解密后的签名与摘要比较
        if(sign1.equals(H))
            return true;
        else
            return false;
    }
    public static boolean verifycert (String ID,String Pk,String sign) throws UnsupportedEncodingException {
        String e="";
        String n="";
        String get=Security.md5digest(ID+"@"+Pk);
        String C=RSA_dencry(sign,e,n);
        if(get.equals(C))
        return true;
        else
            return false;
    }
    public static void main(String[] args) throws UnsupportedEncodingException{
      //  MR.Square_and_Mutiply(BigInteger.valueOf(5),3,BigInteger.valueOf(33));
   //     BigInteger []keys=getkey();
        String keys[]={"65537",
        "40620975511514444922216285965739074629524184209666903468575486745552599706507784118145004534381937373448303494370104583725179040809461031140361397169364216206182702478302552457752232388013462323111474276311493655819732530771154778217638276135353729518041167676539262098111833771374171700799479320568389301993297104947547714917814197345395171771611427705037391756390954340231730770007928922629471679046514368238665767386989689956865849678237616087874456195592424076806725136595579516285118648876756543668700742013877914742513465845836770843332905741740787215743834430682237399393060248898270517497002280887998813919793",
                "63718929442271952533922659964974670512090149845594539315941399589355689970449991568953306897314241973281030782923253808128220650970072943940781830691446209609971176934430693643458785400029638110812749871867552889455668043780497144567959782266220138138436189708409612736332102677705818352209082724559371222708377261301490747107220542349665376136509629788771633366449328510967133327832882202643290712782634315826970578578407451775165146357891890096690172664371421017982988978546098246551670703278409622714758569396591397546768678052883661472887556073799960978796135376937443662021273963924847864865257049430817697401453"
        };
        String M="didi@di";
    //    String H= Security.md5digest(M);
   //     System.out.println(H);
        String sign=signature(M,keys[1],keys[2]);
        System.out.println(verifysign(sign,M,keys[0],keys[2]));
        //String M="0011nznxnzyqwuyeuqweuywequweyqwyeuqwyeuqyweq13130011yeuqwyeuqyweq131313130011";
     //   BigInteger a=new BigInteger(keys[2]);
       // String v=a.toString(2);
        long startTime = System.currentTimeMillis();
       String C=RSA_encry(M,keys[0].toString(),keys[2].toString());
      //  BigInteger M=new BigInteger("65546848646848444615313213213215165169999999999999999999999785653657520563467735121515155");
       System.out.println("原始密文加密为"+C);
        System.out.println("    密文解密为"+RSA_dencry(C,keys[1].toString(),keys[2].toString()));//BigInteger.valueOf(1019),BigInteger.valueOf(3337)));
        long endTime1 = System.currentTimeMillis();
        System.out.println("运行时间:" + (endTime1 - startTime) + "ms");
    }
}
