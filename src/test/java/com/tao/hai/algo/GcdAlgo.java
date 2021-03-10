package com.tao.hai.algo;

import org.w3c.dom.Element;

import java.io.DataOutputStream;
import java.io.StringWriter;

/**
 * 最大公约数算法
 */
public class GcdAlgo {

    /**
     * 辗转相除算法，又叫欧几里得算法
     * 定理：两个正整数a和b(a>b)，它们的最大公约数等于a除以b的余数c和b之间的最大公约数
     * 局限性：当两个整数较大时，做a%b取模运算的性能会比较差
     */
    public static int getGcd(int a, int b) {
        int max = a > b ? a : b;
        int min = a > b ? b : a;
        if (max % min == 0) {
            return min;
        }
        return getGcd(max % min, min);
    }

    /**
     * 更相减损术
     * 原理：两个正整数a和b(a>b)，它们的最大公约数等于a-b的差值c和较小数b的最大公约数
     * 局限性：如果两个数值相差悬殊时，则该方法不合适
     */
    public static int getGcdMinus(int a, int b) {
        if (a == b) {
            return a;
        }
        int max = a > b ? a : b;
        int min = a > b ? b : a;
        return getGcdMinus(max - min, min);
    }

    /**
     * 整合欧几里得算法和更相减算法并进行移位计算
     * 原理：如果a和b均为偶数时，gcd(a,b)=2*gcd(a/2,b/2)=2*gcd(a>>1,b>>1);
     * 当a为偶数，b为奇数时，gcd(a,b)=gcd(a/2,b)=gcd(a>>1,b);
     * 当a为奇数，b为偶数时，gcd(a,b)=gcd(a,b/2)=gcd(a,b>>1);
     * 当a为奇数，b为奇数时，gcd(a,b)=gcd(b,a-b)，此时a-b必然是偶数，然后又可以进行移位运算
     */
    public static int getGcdBest(int a, int b) {
        if (a == b) {
            return a;
        }
        //都是偶数
        if ((a & 1) == 0 && (b & 1) == 0) {
            return getGcdBest(a >> 1, b >> 1) << 1;
        }
        //a偶数,b奇数
        if ((a & 1) == 0 && (b & 1) != 0) {
            return getGcdBest(a >> 1, b);
        }
        //a奇数,b偶数
        if ((a & 1) != 0 && (b & 1) == 0) {
            return getGcdBest(a, b >> 1);
        }
        //a奇数,b奇数
        int max = a > b ? a : b;
        int min = a > b ? b : a;
        return getGcdBest(max - min, min);
    }

    public static void main(String[] args) {
        int a = 25;
        int b = 5;
        System.out.println("getGcd=" + getGcd(a, b));
        System.out.println("getGcdMinus=" + getGcdMinus(a, b));
        System.out.println("getGcdBest=" + getGcdBest(a, b));
    }

}
