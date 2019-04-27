package RSA;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RSA {
    private static final int ORDER =256;// 随机数的数量级
    private static final BigInteger two =new BigInteger("2");
    private static final BigInteger one =new BigInteger("1");
    public static BigInteger Encry(BigInteger base,BigInteger exponent,BigInteger n){
        char[] binaryArray = new StringBuilder(exponent.toString(2)).reverse().toString().toCharArray() ;
        int r = binaryArray.length ;
        List<BigInteger> baseArray = new ArrayList<BigInteger>() ;
        BigInteger preBase = base ;
        baseArray.add(preBase);
        for(int i = 0 ; i < r - 1 ; i ++){
            BigInteger nextBase = preBase.multiply(preBase).mod(n) ;
            baseArray.add(nextBase) ;
            preBase = nextBase ;
        }
        BigInteger result = multi(baseArray.toArray(new BigInteger[baseArray.size()]), binaryArray, n) ;
        return result.mod(n) ;
    }
    private static BigInteger multi(BigInteger[] array, char[] bin_array, BigInteger n){
        BigInteger result = BigInteger.ONE ;
        for(int index = 0 ; index < array.length ; index ++){
            BigInteger a = array[index] ;
            if(bin_array[index] == '0'){
                continue ;
            }
            result = result.multiply(a) ;
            result = result.mod(n) ;
        }
        return result ;
    }
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
    private static BigInteger stringTobigint(String a){
        int value=0;
        String c="";
        int b=0;
        int size=a.length()/8;
        for(int i=0;i<size;i++){
            b=(Integer.parseInt(a.substring(i,i+1),2));
            c+=String.valueOf((char)b);
        }
        return new BigInteger(c,2);
    }
    public static void main(String[] args) {
      //  MR.Square_and_Mutiply(BigInteger.valueOf(5),3,BigInteger.valueOf(33));

        BigInteger []keys=getkey();
        BigInteger M=new BigInteger("65546848646848444615313213213215165169999999999999999999999785653657520563467735121515155");
        System.out.println("    原始明文为"+M);
        long startTime = System.currentTimeMillis();
        BigInteger C=M.modPow(keys[0],keys[2]);//Encry(M,e,n);//BigInteger.valueOf(688),BigInteger.valueOf(79),BigInteger.valueOf(3337));
        System.out.println("原始密文加密为"+C);
        System.out.println("    密文解密为"+C.modPow(keys[1],keys[2]));//BigInteger.valueOf(1019),BigInteger.valueOf(3337)));
        long endTime1 = System.currentTimeMillis();
        System.out.println("运行时间:" + (endTime1 - startTime) + "ms");
    }
}
