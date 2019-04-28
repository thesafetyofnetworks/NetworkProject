package RSA;
import java.math.BigInteger;
import java.util.Random;

public class MR {
    private static final int ORDER =256;// 随机数的数量级
    private static final int MIN = 1000; // 选择的随机数的最小位数值
    private static final BigInteger two =new BigInteger("2");
    private static final BigInteger one =new BigInteger("1");

    public static boolean MillerRabin(BigInteger num){
        //base cases
        for (int i = 0; i < 5; i++){
            if(!MR.isPrime(num))
                return false;
        }//end for loop
        return true;
    }//end method
        //base cases
        public static byte[] getByte(int m) {
            String sb = "";
            while (m > 0) {
                sb = (m % 2) + sb;
                m = m / 2;
            }
            return sb.getBytes();
        }


        /**
         * 平方-乘法计算指数模运算 a^m % n
         *
         * @param a底数
         * @param m指数
         * @param n被mod数
         * @return
         */
        public static BigInteger Square_and_Mutiply(BigInteger a, BigInteger b, BigInteger n) {
            BigInteger ans=BigInteger.ONE,buff = a;
            while(b.compareTo(BigInteger.ZERO)!=0)
            {

                if(b.and(BigInteger.ONE).compareTo(BigInteger.ZERO)!=0)

                    ans=(ans.multiply(buff)).mod(n);
                buff=buff.multiply(buff).mod(n);

		         /*res = mod_mul(res,a,n);
		         a = mod_mul(a,a,n);
		         */
                // System.out.println("b??"+b);
                b = b.shiftRight(1);

            }
            //System.out.println("qpow ends");
            return ans;
        }


        /**
         * 验证一个数是否为素数，将n-1改写为2^k * m的形式，其中m是奇数，在{2,...,n-1}中随机选取一个整数a;
         *
         * @param n
         * @return 如果是素数返回true, 否则返回false
         */
        public static boolean isPrime(BigInteger n) {
            int[] arr = intTOIndex(n.subtract(BigInteger.valueOf(1)));// n-1 用2的幂表示
            int k = arr[0];
            int m = arr[1];
            Random r = new Random();// 在{2,...,n-1}随机选择一个整数a
            BigInteger  randomNumber;
            int a = 0;
            do {
               // a = r.nextInt(n.bitLength()-1);
                randomNumber = new BigInteger(ORDER, r);
            } while (randomNumber.compareTo(BigInteger.valueOf(2))<0);          //小于2
            BigInteger b = Square_and_Mutiply(randomNumber, n.subtract(BigInteger.ONE), n);
            if (b.compareTo(BigInteger.valueOf(1))==0) return true;
            for (int i = 0; i < k;b = (b.multiply(b)).remainder(n),i++) {
                if (b.compareTo(n.subtract(BigInteger.valueOf(1)))==0||b.compareTo(BigInteger.ONE)==0) return true;
            }
            return false;
        }

        /**
         * 将一个数改为2^k * m的形式，其中m是奇数
         *
         * @param n
         * @return arr[0]=k,arr[1]=m
         */
        public static int[] intTOIndex(BigInteger n) {
            int[] arr = new int[2];
            int k = 0;
            BigInteger x;
            // 当n为奇数是停止循环
            do {
                k++;
                n=n.shiftRight(1);
                x = n.and(BigInteger.valueOf(1));
            } while (x .compareTo(BigInteger.valueOf(0))==0);
            arr[0] = k;
            arr[1] = n.intValue();
            return arr;
        }

    public static BigInteger Egcd(BigInteger a, BigInteger b)
    {
        BigInteger x,   y;
        if(b.equals(BigInteger.ZERO)){
            x=BigInteger.ONE;
            y=BigInteger.ZERO;
            return a;
        }
        BigInteger x0=BigInteger.ONE,y0=BigInteger.ZERO,x1=BigInteger.ZERO,y1=BigInteger.ONE,x2,y2;
        BigInteger r,p;
        p=a.divide(b);
        r=a.mod(b);
        while(r.compareTo(BigInteger.ZERO)>0){
            x2=x0.subtract(x1.multiply(p)) ;
            y2=y0.subtract(y1.multiply(p)) ;
            x0=x1;x1=x2;y0=y1;y1=y2;
            a=b;b=r;
            p=a.divide(b);
            r=a.mod(b);
        }
        x=x1;
        y=y1;
        return y;
    }
    /**
     * 获取一个随机数为并且检查其为素数
     *
     * @return
     */
    public static BigInteger getPrime() {
        BigInteger x = new BigInteger("0");
        Random rand = new Random();
        while (x.remainder(BigInteger.valueOf(2)).compareTo(BigInteger.valueOf(0))==0 || !isPrime(x) ) {      //得到一个强素数
            x = BigInteger.probablePrime(ORDER,rand);
        }
        return x;
    }
    public static BigInteger getgcd(BigInteger Fn) {        //d e不一定是素数
        BigInteger temp=MR.getRandom();
        while(!Fn.gcd(temp).equals(one)){
            temp=MR.getRandom();
        }
        return temp;
    }
    public static BigInteger getRandom() {
        BigInteger x = new BigInteger("3");
        Random r = new Random();// 在{2,...,n-1}随机选择一个整数a
        Random rand = new Random();
        BigInteger  randomNumber;
        int a = 0;
        do {
            a = r.nextInt(ORDER)+20;
            randomNumber = new BigInteger(a, rand);
        } while (randomNumber.compareTo(BigInteger.valueOf(2)) < 0 || randomNumber.remainder(BigInteger.valueOf(2)).compareTo(BigInteger.valueOf(0))==0);
        return randomNumber;
    }
}