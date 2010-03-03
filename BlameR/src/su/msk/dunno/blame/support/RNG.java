/*
 * RNG.java
 *
 * Created on 24 ������� 2008 �., 16:00
 *
 * ������� ���������� ��������� �����
 */

package su.msk.dunno.blame.support;

import java.util.Random;

/**
 *
 * @author Sanja
 */
public class RNG {
    private static Random rng = new Random(); // ���������� ���������� ��� ������� � �������� java.util.Random

    /** ���������� ����� � �������� 0 ... max(int) - 1 */
    public static int getInt() {
        return rng.nextInt();
    }
    
    /** ���������� ����� � �������� 0 ... maxVal - 1
     * @param maxVal ������� ������� ������������� ��������
     */
    public static int getInt(int maxVal) {
        return rng.nextInt(maxVal);
    }
    
    /** ���������� ����� � �������� minVal ... maxVal 
     * @param minVal ������ ������� ������������� ��������
     * @param maxVal ������� ������� ������������� ��������
     */
    public static int getInt(int minVal, int maxVal) {
        return rng.nextInt(maxVal - minVal + 1) + minVal;
    }
}

