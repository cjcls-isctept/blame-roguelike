/*
 * RNG.java
 *
 * Created on 24 Февраль 2008 г., 16:00
 *
 * Функции генератора случайных чисел
 */

package su.msk.dunno.blame.support;

import java.util.Random;

/**
 *
 * @author Sanja
 */
public class RNG {
    private static Random rng = new Random(); // Внутренняя переменная для общения с объектом java.util.Random

    /** Возвращает целое в пределах 0 ... max(int) - 1 */
    public static int getInt() {
        return rng.nextInt();
    }
    
    /** Возвращает целое в пределах 0 ... maxVal - 1
     * @param maxVal Верхняя граница генерируемого значения
     */
    public static int getInt(int maxVal) {
        return rng.nextInt(maxVal);
    }
    
    /** Возвращает целое в пределах minVal ... maxVal 
     * @param minVal Нижняя граница генерируемого значения
     * @param maxVal Верхняя граница генерируемого значения
     */
    public static int getInt(int minVal, int maxVal) {
        return rng.nextInt(maxVal - minVal + 1) + minVal;
    }
}

