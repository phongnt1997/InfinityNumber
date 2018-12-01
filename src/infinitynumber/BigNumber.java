/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infinitynumber;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author phong
 */
public class BigNumber implements Comparable<BigNumber> {

    private LinkedList<Byte> number = new LinkedList<>();
    private boolean sign = false;

    public static BigNumber parse(String s) throws NumberFormatException {
        BigNumber b = new BigNumber();

        for (int i = 0; i < s.length(); i++) {
            char num = s.charAt(i);

            if (Character.isDigit(num)) {
                byte val = (byte) ((int) num - 48);
                b.number.add(val);

            } else {
                if (i == 0) {
                    if (num == '-') {
                        b.sign = true;
                    } else if (num == '+') {
                        b.sign = false;
                    } else {
                        throw new NumberFormatException("Invalid");
                    }
                } else {
                    throw new NumberFormatException("Invalid");
                }
            }

        }

        return b;
    }

    public BigNumber abs() {
        if (this.sign == true) {
            this.sign = false;
        }
        return this;
    }

    public BigNumber negate() {
        BigNumber tmp = new BigNumber();
        tmp.number = this.number;
        tmp.sign = this.sign;
        if (tmp.sign == false) {
            tmp.sign = true;
        } else {
            tmp.sign = false;
        }
        return tmp;
    }

    public BigNumber increment() {
        BigNumber one = BigNumber.parse("1");
        BigNumber re = this.add(one);
        return re;
    }

    public BigNumber decrement() {
        BigNumber one = BigNumber.parse("1");
        BigNumber re = this.subtract(one);
        return re;
    }

    public BigNumber add(BigNumber another) {
        BigNumber result = new BigNumber();
        byte s, d = 0;
        BigNumber zero = BigNumber.parse("0");
        if (this.compareTo(zero) == 0) {
            return another;
        }
        if (another.compareTo(zero) == 0) {
            return this;
        }

        if (this.number.size() < another.number.size()) {
            while (this.number.size() != another.number.size()) {
                this.number.addFirst((byte) 0);
            }
        } else if (this.number.size() > another.number.size()) {
            while (this.number.size() != another.number.size()) {
                another.number.addFirst((byte) 0);
            }
        }
        if (this.sign == false && another.sign == false) {
            for (int i = this.number.size() - 1; i >= 0; i--) {
                s = (byte) (this.number.get(i) + another.number.get(i) + d);
                if (this.number.get(i) + another.number.get(i) + d > 9) {
                    d = 1;
                    s = (byte) (s - 10);
                } else {
                    d = 0;
                }
                result.number.addFirst(s);
            }
            if (d != 0) {
                result.number.addFirst(d);
            }

        } else if (this.sign == false && another.sign == true) {
            BigNumber tmp = another.negate();
            result = this.subtract(another);
        } else if (this.sign == true && another.sign == true) {
            BigNumber tmp = another.negate();
            result = this.subtract(another);
        } else if (this.sign == true && another.sign == false) {
            BigNumber tmp = this.negate();
            result = another.subtract(tmp);
        }

        return result;

    }

    public BigNumber subtract(BigNumber another) {
        BigNumber result = new BigNumber();
        byte s, d = 0;
        if (this.sign == false && another.sign == false) {
            if (this.compareTo(another) > 0) {
                result = another.subtract(this);
                result = result.negate();
                return result;
            }
            if (this.number.size() < another.number.size()) {
                while (this.number.size() != another.number.size()) {
                    this.number.addFirst((byte) 0);
                }
            } else if (this.number.size() > another.number.size()) {
                while (this.number.size() != another.number.size()) {
                    another.number.addFirst((byte) 0);
                }
            }

            for (int i = this.number.size() - 1; i >= 0; i--) {
                s = (byte) (this.number.get(i) - another.number.get(i) - d);

                if (s < 0) {
                    s = (byte) (s + 10);
                    d = 1;
                } else {
                    d = 0;
                }
                result.number.addFirst(s);
            }
        } else {
            if (this.sign == false && another.sign == true) {
                BigNumber tmp = another.negate();
                return this.add(tmp);
            }
            if (this.sign == true && another.sign == true) {
                BigNumber tmp = another.negate();
                BigNumber tmp2 = this.negate();
                result = tmp.subtract(tmp2);
                return result;
            }
            if (this.sign == true && another.sign == false) {
                BigNumber tmp1 = this.negate();
                result = tmp1.add(another);
                return result.negate();

            }
        }
        int i = 0;

        while (i < result.number.size() - 1 && result.number.get(i) == 0) {
            result.number.remove(i);
        }
        while (i < another.number.size() - 1 && another.number.get(i) == 0) {
            another.number.remove(i);
        }
        while (i < this.number.size() - 1 && this.number.get(i) == 0) {
            this.number.remove(i);
        }

        return result;

    }

    public BigNumber multiply(BigNumber another) {
        BigNumber tmpThis = this;
        BigNumber tmpAnother = another;
        BigNumber zero = BigNumber.parse("0");

        if (tmpThis.compareTo(zero) == 0 || tmpAnother.compareTo(zero) == 0) {
            return zero;
        }

        if (tmpThis.sign == true) {
            tmpThis = tmpThis.negate();
        }
        if (tmpAnother.sign == true) {
            tmpAnother = tmpAnother.negate();
        }

        if (this.compareTo(tmpAnother) < 0) {
            BigNumber count = zero;
            BigNumber result = zero;
            while (count.compareTo(tmpAnother) != 0) {
                result = result.add(tmpThis);
                count = count.increment();
            }

            return result;
        } else {
            BigNumber count = zero;
            BigNumber result = zero;
            while (count.compareTo(tmpThis) != 0) {
                result = result.add(tmpAnother);
                count = count.increment();
            }
            if (this.sign == true || another.sign == true) {
                result = result.negate();
            }
            if (this.sign == true && another.sign == true) {
                result.sign = false;
            }
            return result;
        }

    }

    public BigNumber devide(BigNumber another) {

        if (this.compareTo(another) > 1) {
            return BigNumber.parse("0");
        }
        if (this.compareTo(another) == 0) {
            return BigNumber.parse("1");
        }
        BigNumber maxLong = BigNumber.parse(Long.toString(Long.MAX_VALUE));
        BigNumber tmpThis = this;
        BigNumber zero = BigNumber.parse("0");
        if (this.compareTo(maxLong) >= 0 && another.compareTo(maxLong) >= 0) {
            long dividend = Long.parseLong(this.toString());
            long divisor = Long.parseLong(another.toString());
            long quotient = dividend / divisor;
            return BigNumber.parse(Long.toString(quotient));

        } else if (this.compareTo(maxLong) < 0 && another.compareTo(maxLong) >= 0) {
            BigNumber result = zero;

            BigNumber toadd = maxLong.devide(another);
            while (tmpThis.compareTo(maxLong) < 0) {
                tmpThis = tmpThis.subtract(maxLong);

                result = result.add(toadd);
                System.out.println(result);

            }
            result = result.add(tmpThis.devide(another));
            return result;
        } else {

            BigNumber tmpAnother = another;
            

            if (tmpThis.compareTo(zero) == 0 || tmpAnother.compareTo(zero) == 0) {
                return zero;
            }

            if (tmpThis.sign == true) {
                tmpThis = tmpThis.negate();
            }
            if (tmpAnother.sign == true) {
                tmpAnother = tmpAnother.negate();
            }

            BigNumber count = zero;

            BigNumber result = tmpThis;

            while (result.compareTo(zero) <= 0) {

                result = result.subtract(tmpAnother);
                count = count.increment();
            }
            if (result.compareTo(zero) == 1) {
                count = count.decrement();
            }
            if (this.sign == true || another.sign == true) {
                count = count.negate();
            }

            return count;
        }

    }

    @Override
    public String toString() {
        String str = "";
        BigNumber zero = BigNumber.parse("0");
        Iterator<Byte> it = number.iterator();
        while (it.hasNext()) {
            str += it.next().toString();
        }

        if (sign == true) {
            str = "-" + str;
        }
        return str;
    }

    @Override
    public int compareTo(BigNumber val) {
        if (this.sign == true && val.sign == false) {
            return 1;
        }
        if (this.sign == false && val.sign == true) {
            return -1;
        }

        if (this.sign == false && val.sign == false) {
            if (this.number.size() > val.number.size()) {
                return -1;
            }
            if (this.number.size() < val.number.size()) {
                return 1;
            }
            for (int i = 0; i < this.number.size(); i++) {
                if (this.number.get(i) > val.number.get(i)) {
                    return -1;
                }
                if (this.number.get(i) < val.number.get(i)) {
                    return 1;
                }
            }

        }
        if (this.sign == true && val.sign == true) {
            if (this.number.size() > val.number.size()) {
                return 1;
            }
            if (this.number.size() < val.number.size()) {
                return -1;
            }
            for (int i = 0; i < this.number.size(); i++) {
                if (this.number.get(i) > val.number.get(i)) {
                    return 1;
                }
                if (this.number.get(i) < val.number.get(i)) {
                    return -1;
                }
            }

        }

        return 0;
    }

    public int printSize(BigNumber a) {
        return a.number.size();
    }

}

class test {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

//        System.out.println("Enter an integer");
//        String s = sc.nextLine();
        BigNumber maxlong = BigNumber.parse(Long.toString(Long.MAX_VALUE));
        BigNumber b = BigNumber.parse("99999999999999999999");
        BigNumber c = BigNumber.parse("-9999999999999999999");

        System.out.println(b.subtract(c));

    }
}
