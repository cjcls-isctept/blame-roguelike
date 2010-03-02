/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package su.msk.dunno.blame.support;

import java.util.HashMap;

/**
 *
 * @author Sanja
 *
 * Назначение: Грамматика русского языка
 *
 * Использование: в функцию doMsg передаётся строка, которая в зависимости от значений
 *                rules (правила склонения) и padeg (нужный падеж), склоняется по правилам русского языка.
 */
public class Grammar {
    // Падежи
    public static enum Padeg {
        rod, // Родительный
        dat, // Дательный
        vin, // Винительный
        tvr, // Творительный
        pre  // Предложный
    };

    /** Список согласных букв */
    private static HashMap sogl;
    /** Список звонких согласных */
    private static HashMap zvon;
    /** Список шипящих согласных */
    private static HashMap ship;
    
    /** Наборы букв, необходимые для сравнения */
    private static HashMap my0, my1, my2;
    /** Список предлогов и союзов, исключаемых из анализа */
    private static HashMap exclude;

    /** Конструктор: инициализация инструмента грамматики */
    static {
        // Согласные
        sogl = new HashMap();
        sogl.put(1, 'б');
        sogl.put(2, 'в');
        sogl.put(3, 'г');
        sogl.put(4, 'д');
        sogl.put(5, 'ж');
        sogl.put(6, 'з');
        sogl.put(7, 'й');
        sogl.put(8, 'к');
        sogl.put(9, 'л');
        sogl.put(10, 'м');
        sogl.put(11, 'н');
        sogl.put(12, 'п');
        sogl.put(13, 'р');
        sogl.put(14, 'с');
        sogl.put(15, 'т');
        sogl.put(16, 'ф');
        sogl.put(17, 'х');
        sogl.put(18, 'ц');
        sogl.put(19, 'ч');
        sogl.put(20, 'ш');
        sogl.put(21, 'щ');

        // Звонкие
        zvon = new HashMap();
        zvon.put(1, 'б');
        zvon.put(2, 'в');
        zvon.put(3, 'г');
        zvon.put(4, 'д');
        zvon.put(5, 'ж');
        zvon.put(6, 'з');
        zvon.put(7, 'н');
        zvon.put(8, 'р');
        zvon.put(9, 'ц');

        // Шипящие
        ship = new HashMap();
        ship.put(1, 'ч');
        ship.put(2, 'ш');
        ship.put(3, 'щ');
        ship.put(4, 'ц');

        my0 = new HashMap();
        my0.put(1, 'б');
        my0.put(2, 'в');
        my0.put(3, 'д');
        my0.put(4, 'з');
        my0.put(5, 'л');
        my0.put(6, 'м');
        my0.put(7, 'н');
        my0.put(8, 'п');
        my0.put(9, 'р');
        my0.put(10, 'с');
        my0.put(11, 'т');
        my0.put(12, 'ф');
        my0.put(13, 'ц');

        my1 = new HashMap();
        my1.put(1, 'е');
        my1.put(2, 'е');
        my1.put(3, 'у');
        my1.put(4, 'ю');
        my1.put(5, 'у');
        my1.put(6, 'а');
        my1.put(7, 'у');
        my1.put(8, 'у');
        my1.put(9, 'у');
        my1.put(10, 'у');
        my1.put(11, 'у');
        my1.put(12, 'у');
        my1.put(13, 'у');

        my2 = new HashMap();
        my2.put(1, 'б');
        my2.put(2, 'в');
        my2.put(3, 'д');
        my2.put(4, 'з');
        my2.put(5, 'л');
        my2.put(6, 'м');
        my2.put(7, 'м');
        my2.put(8, 'п');
        my2.put(9, 'р');
        my2.put(10, 'с');
        my2.put(11, 'м');
        my2.put(12, 'ф');
        my2.put(13, 'ц');

        exclude = new HashMap();
        exclude.put(1, "для");
        exclude.put(2, "от");
        exclude.put(3, "из");
        exclude.put(4, "против");
        exclude.put(5, "на");
        exclude.put(6, "в");
        exclude.put(7, "с");
        exclude.put(8, "за");
        exclude.put(9, "под");
        exclude.put(10, "со");
        exclude.put(11, "про");
        exclude.put(12, "над");
    }

    /** Проверяет, является ли входная буква согласной
     * 
     * @param c Проверяемая буква
     * @return Результат проверки
     */
    private static boolean isSogl (char c) {
        return sogl.containsValue(c);
    }
    
    /** Проверяет, является ли входная буква шипящей
     * 
     * @param c Проверяемая буква
     * @return Результат проверки
     */
    private static boolean isShip (char c) {
        return ship.containsValue(c);
    }
    
    /** Преобразовывает символ в нижний регистр
     * 
     * @param c Входной символ
     * @return Символ, преобразованый к нижнему регистру
     */
    private static char lowc (char c) {
        return String.valueOf(c).toLowerCase().charAt(1);
    }
   
    /** Возвращает количество слов в строке
     * 
     * @param s Входная строка
     * @return Количество слов в строке
     */
    private static int strCount (String s) {
        int count = 1;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') count++;
        }
        return count;
    }
    
    /** Подсчитывает количество слов в строке, не включая союзы и предлоги
     * 
     * @param s Входная строка
     * @return Количество слов в строке
     */
    private static int strCount1 (String s) {
        int cnt = strCount(s);

        for (int i = 1; i < cnt; i++) {
            if (exclude.containsValue(takeWord(s, i))) {
                return i - 1;
            }
        }
        return cnt;
    }
    
    /** Получает подслово из слова
     * 
     * @param s
     * @param number
     * @return
     */
    private static String takeWord (String s, int number) {
        // Проверка: ошибка, при указании номера слова
        if ((number > strCount(s)) || (number < 0))
            return "!error";

        // Ищем нужное слово
        int start = 0;  // Индекс начала слова
        int end = 0;    // Индекс конца слова
        if (number > 1) {   // Если слово не первое, ищем нужное
            // Поиск начала слова
            start = s.indexOf(' ');
            number--;
            while ((start != -1) && (number > 1)) {
                start = s.indexOf(' ', start+1);
                number--;
            }
            // Начало слова - следующее за пробелом
            start++;
            
            // Проверка: если поиск начала вернул -1, значит была ошибка
            // во входном значении number
            if (start == -1)
                return "!error";
            else { // Ищем конец слова
                end = s.indexOf(' ', start);
                // Если следующий за словом пробел отсутствует
                // (последнее слово во входной строке)
                // возвращаем конец строки
                if (end == -1)
                    end = s.length();
            }
        }
        else {              // Если слово первое
            start = 0;
            end = s.indexOf(' '); // Получаем индекс конца слова
            // Если слово одно, возвращаем входную строку
            if (end == -1)
                return s; 
        }
        
        // Возвращаем найденную подстроку
        return s.substring(start, end);
    }
    
//------------------------------------------------------------------------------
    /*::: Склонение по падежам :::*/
    
    /** Склонение: родительный падеж
     * 
     * @param string Входная строка
     * @param gender Род
     * @param isSingular Единственное число (false - множественное число)
     * @return Преобразованное слово
     */
    public static String rodPad (String string, int gender, boolean isSingular) {
        int maxi = strCount1(string); // Количество слов
        StringBuffer s;
        StringBuffer result = new StringBuffer();

        // Цикл для каждого слова
        for (int i = 1; i <= maxi; i++) {
            s = new StringBuffer(takeWord(string, i));
            int leng = s.length();

            switch (s.charAt(leng-1)) {
                // 1-ое склонение + часть третьего
                case 'а':
                    //<гл><согл>а
                    if (!isSingular && isSogl(s.charAt(leng-3)) && isSogl(s.charAt(leng-2))) {
                        if (s.charAt(leng-3) == 'е') {
                            s.setCharAt(leng-3, 'ё');
                            s.setCharAt(leng-1, (char)0);
                            break;
                        }
                        if (s.charAt(leng-3) == 'о') {
                            s.setCharAt(leng-3, 'о');
                            s = s.append("в");
                            break;
                        }
                        s.setCharAt(leng-1, (char)0);
                        break;
                    }
                    //<согл><согл>а
                    if (!isSingular && isSogl(s.charAt(leng-3)) && isSogl(s.charAt(leng-2))) {
                        if (s.charAt(leng-2) == 'д') {
                            s.setCharAt(leng-1, (char)0);
                            break;
                        }
                        s.setCharAt(leng-1, s.charAt(leng-2));
                        s.setCharAt(leng-2, 'е');
                        break;
                    }
                    if (my0.containsValue(s.charAt(leng-2)))
                        s.setCharAt(leng-1, 'ы');
                    else
                        s.setCharAt(leng-1, 'и');
                    break;

                case 'я':
                    // Единств. число -я
                    if (isSingular) {
                        if (s.charAt(leng-2) == 'м') {
                            s.setCharAt(leng-1, 'е');
                            s = s.append("ни");
                        } // проверка окончания "-мя"
                        else if (isShip(s.charAt(leng-3)) && s.charAt(leng-2) == 'а') {
                            s.setCharAt(leng-2, 'о');
                            s.setCharAt(leng-1, 'й');
                        }
                        else if (s.charAt(leng-2) == 'а' && i != strCount(string)) {
                            s.setCharAt(leng-2, 'о');
                            s.setCharAt(leng-1, 'й');
                        }
                        else if (s.charAt(leng-2) == 'я') {
                            s.setCharAt(leng-2, 'е');
                            s.setCharAt(leng-1, 'й');
                        }
                        else
                            s.setCharAt(leng-1, 'и');
                    }
                    else { // Множественное число -я
                        if (s.charAt(leng-2) == 'ь') {
                            s.setCharAt(leng-1, 'е');
                            s = s.append("в");
                        }
                        else if (isSogl(s.charAt(leng-2))) {
                            s.setCharAt(leng-1, 'е');
                            s = s.append("й");
                        }
                        else if (s.charAt(leng-2) == 'и') {
                            s.setCharAt(leng-1, 'й');
                        }
                    }
                    break;

                // 2- ое склонение
                case 'й':
                    if (my1.containsValue(s.charAt(leng-2))) {
                        s.setCharAt(leng-1, 'я');
                        break;
                    }
                    //-ий
                    if (s.charAt(leng-2) == 'и') {
                        if (i != strCount(string)) {
                            if (s.charAt(leng-3) == 'к' || s.charAt(leng-3) == 'г') {
                                s.setCharAt(leng-2, 'о');
                                s.setCharAt(leng-1, 'г');
                                s = s.append("о");
                                break;
                            }
                            if (s.charAt(leng-3) == 'б' || s.charAt(leng-3) == 'з' || s.charAt(leng-3) == 'c') {
                                s.setCharAt(leng-2, 'ь');
                                s.setCharAt(leng-1, 'е');
                                s = s.append("го");
                                break;
                            }
                            {
                                s.setCharAt(leng-2, 'е');
                                s.setCharAt(leng-1, 'г');
                                s = s.append("о");
                                break;
                            }
                        }
                        else {
                            s.setCharAt(leng-2, 'я');
                            break;
                        }
                    }
                    // -ой
                    if (s.charAt(leng-2) == 'о') {
                        if (i == strCount(string)) {
                            s.setCharAt(leng-1, 'я');
                            break;
                        }
                        else {
                            s.setCharAt(leng-1, 'г');
                            s = s.append("о");
                            break;
                        }
                    }
                    //-ый
                    if (s.charAt(leng-2) == 'ы') {
                        if (i != strCount(string)) {
                            s.setCharAt(leng-2, 'о');
                            s.setCharAt(leng-1, 'г');
                            s = s.append("о");
                            break;
                        }
                        else {
                            s.setCharAt(leng-1, 'я');
                            break;
                        }
                    }
                    s.setCharAt(leng-1, 'и');
                    break;

                case 'о':
                    if (isSogl(s.charAt(leng-2))) {
                        s.setCharAt(leng-1, 'а');
                    }
                    break;

                case 'е':
                    if (i != strCount(string)) { //прилагательное
                        if (isSingular) {
                            s.setCharAt(leng-1, 'х');
                        }
                        else {
                            s.setCharAt(leng-1, 'г');
                            s = s.append("о");
                        }
                    }
                    else { // существительное
                        if (isSogl(s.charAt(leng-2)) && !isShip(s.charAt(leng-2))) {
                            s.setCharAt(leng-1, 'я');
                        }
                        else if (s.charAt(leng-2) == 'ь') {
                            s.setCharAt(leng-1, 'я');
                        }
                        else {
                            s.setCharAt(leng-1, 'а');
                        }
                    }
                    break;

                case 'ё':
                    if (isSogl(s.charAt(leng-2))) {
                        s.setCharAt(leng-1, 'я');
                    }
                    else if (s.charAt(leng-2) == 'ь') {
                        s.setCharAt(leng-1, 'я');
                    }
                    break;

                case 'к':
                    if (s.charAt(leng-2) == 'о') {
                        s.setCharAt(leng-2, 'к');
                        s.setCharAt(leng-1, 'а');
                    }
                    else if (s.charAt(leng-2) == 'е') {
                        s.setCharAt(leng-2, 'к');
                        s.setCharAt(leng-1, 'а');
                    }
                    else if (s.charAt(leng-2) == 'ё' && (leng > 3)) { // -ёк
                        if (isSogl(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s = s.append("а");
                            }
                            else {
                                s.setCharAt(leng-2, 'ь');
                                s.setCharAt(leng-1, 'к');
                                s = s.append("а");
                            }
                        }
                        else {
                            s.setCharAt(leng-2, 'й');
                            s.setCharAt(leng-1, 'к');
                            s = s.append("а");
                        }
                    }
                    else if (gender != 1) {
                        s = s.append("а");
                    }
                    break;

                case 'ц':
                    if (s.charAt(leng-2) == 'е' || s.charAt(leng-2) == 'я') {
                        if (isSogl(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s = s.append("а");
                            }
                            else {
                                if (s.charAt(leng-3) == 'л') {
                                    s.setCharAt(leng-2, 'ь');
                                    s.setCharAt(leng-1, 'ц');
                                    s = s.append("а");
                                }
                                else {
                                    s.setCharAt(leng-2, 'ц');
                                    s.setCharAt(leng-1, 'а');
                                }
                            }
                        }
                        else {
                            s.setCharAt(leng-2, 'й');
                            s.setCharAt(leng-1, 'ц');
                            s = s.append("а");
                        }
                    }
                    else {
                        s = s.append("а");
                    }
                    break;

                // 3- ье склонение
		case 'ь':
                    if (s.charAt(leng-3) == 'е' && s.charAt(leng-2) == 'н') {
                        if (gender == 1) {
                            s.setCharAt(leng-3, 'н');
                            s.setCharAt(leng-2, 'я');
                            s.setCharAt(leng-1, (char)0);
                        }
                        else {
                            s.setCharAt(leng-1, 'и');
                        }
                    }
                    else {
                        if (gender == 1) {
                            s.setCharAt(leng-1, 'и');
                        }
                        else {
                            s.setCharAt(leng-1, 'я');
                        }
                    }
                    break;

		case 'в':
                    if (gender != 1) {
                        s = s.append("а");
                    }
                    break;

                case 'и': // Несклоняемые или множественые
                    if (!isSingular) {
                        // - ки
                        if (s.charAt(leng-2) == 'к') {
                            if (gender == 1) {
                                s.setCharAt(leng-2, 'о');
                                s.setCharAt(leng-1, 'к');
                            }
                            else {
                                s.setCharAt(leng-2, 'о');
                                s.append("в");
                            }
                        }
                        else { // Мн. число -и
                            if (isSogl(s.charAt(leng-2))) {
                                s.setCharAt(leng-1, 'е');
                                s = s.append("й");
                            }
                            else {
                                if (gender == 1) {
                                    s.setCharAt(leng-1, 'й');
                                }
                                else {
                                    s.setCharAt(leng-1, 'е');
                                    s = s.append("в");
                                }
                            }
                        }
                    }
                    break;

                case 'ы':
                    if (!isSingular) {
                        s.setCharAt(leng-1, 'о');
                        s = s.append("в");
                    }
                    break;

		case 'у':
                    break;

                default : // Разносклоняемые
                    if (gender != 1)
                        s = s.append("а");
            }

            if (i == 1) {
                result = s;
            }
            else {
                result = result.append(" ");
                result = result.append(s);
            }
        }

        return result.toString();
    }
    
    /** Склонение: дательный падеж
     * 
     * @param string Входная строка
     * @param gender Род
     * @param isSingular Единственное число (false - множественное число)
     * @return Преобразованное слово
     */
    public static String datPad (String string, int gender, boolean isSingular) {
        int maxi = strCount1(string); // Количество слов
        StringBuffer s;
        StringBuffer result = new StringBuffer();

        // Цикл для каждого слова
        for (int i = 1; i <= maxi; i++) {
            s = new StringBuffer(takeWord(string, i));
            int leng = s.length();

            switch (s.charAt(leng-1)) {
                // 1- ое склонение   (+часть третьего)
                case 'а':
                    if (!isSingular && isSogl(s.charAt(leng-3)) && isSogl(s.charAt(leng-2))) {
                        s = s.append("м");
                        break;
                    }
                    if (!isSingular) {
                        s = s.append("м");
                    }
                    else {
                        s.setCharAt(leng-1, 'е');
                    }
                    break;

                case 'я':
                    if (isSingular) { // Ед. число
                        if (s.charAt(leng-2) == 'м') { // проверка окончания "-мя" (ср.р.)
                            s.setCharAt(leng-1, 'е');
                            s = s.append("ни");
                        }
                        else if (isShip(s.charAt(leng-3)) && s.charAt(leng-2) == 'а') {
                            s.setCharAt(leng-2, 'о');
                            s.setCharAt(leng-1, 'й');
                        }
                        else if (s.charAt(leng-2) == 'а' && i != strCount(string)) {
                            s.setCharAt(leng-2, 'о');
                            s.setCharAt(leng-1, 'й');
                        }
                        else if (s.charAt(leng-2) == 'я') {
                            s.setCharAt(leng-2, 'е');
                            s.setCharAt(leng-1, 'й');
                        }
                        else if (s.charAt(leng-2) == 'и') {
                            s.setCharAt(leng-1, 'и');
                        }
                        else {
                            s.setCharAt(leng-1, 'е');
                        }
                    }
                    else { // Множественное число
                        s.setCharAt(leng-1, 'я');
                        s = s.append("м");
                    }
                    break;

                case 'й': // 2- ое склонение
                    if (my1.containsValue(s.charAt(leng-2))) {
                        s.setCharAt(leng-1, 'ю');
                        break;
                    }
                    if (s.charAt(leng-2) == 'и') { //-ий
                        if (i != strCount(string)) {
                            if (s.charAt(leng-3) == 'к' || s.charAt(leng-3) == 'г') {
                                s.setCharAt(leng-2, 'о');
                                s.setCharAt(leng-1, 'м');
                                s = s.append("у");
                            }
                            else if (s.charAt(leng-3) == 'б' || s.charAt(leng-3) == 'з' || s.charAt(leng-3) == 'с') {
                                s.setCharAt(leng-2, 'ь');
                                s.setCharAt(leng-1, 'е');
                                s = s.append("му");
                            }
                            else {
                                s.setCharAt(leng-2, 'е');
                                s.setCharAt(leng-1, 'м');
                                s = s.append("у");
                            }
                            break;
                        }
                        else {
                            s.setCharAt(leng-1, 'ю');
                            break;
                        }
                    }
                    if (s.charAt(leng-2) == 'о') {
                        if (i == strCount(string)) {
                            s.setCharAt(leng-1, 'ю');
                            break;
                        }
                        else {
                            s.setCharAt(leng-1, 'м');
                            s = s.append("у");
                            break;
                        }
                    }
                    if (s.charAt(leng-2) == 'ы') {
                        if (i != strCount(string)) {
                            s.setCharAt(leng-2, 'о');
                            s.setCharAt(leng-1, 'м');
                            s = s.append("у");
                            break;
                        }
                    }
                    s.setCharAt(leng-1, 'и');
                    break;

                case 'о':
                    if (isSogl(s.charAt(leng-2))) {
                        s.setCharAt(leng-1, 'а');
                    }
                    break;

                case 'е':
                    if (i != strCount(string)) { //прилагательное
                        if (!isSingular) {
                            s.setCharAt(leng-1, 'х');
                        }
                        else {
                            s.setCharAt(leng-1, 'г');
                            s = s.append("о");
                        }
                    }
                    else { // существительное
                        if (isSogl(s.charAt(leng-2)) && !isShip(s.charAt(leng-2))) {
                            s.setCharAt(leng-1, 'я');
                        }
                        else if (s.charAt(leng-2) == 'ь') {
                            s.setCharAt(leng-1, 'я');
                        }
                        else {
                            s.setCharAt(leng-1, 'а');
                        }
                    }
                    break;

                case 'ё':
                    if (isSogl(s.charAt(leng-2))) {
                        s.setCharAt(leng-1, 'я');
                    }
                    else if (s.charAt(leng-2) == 'ь') {
                        s.setCharAt(leng-1, 'я');
                    }
                    break;

                case 'к':
                    if (s.charAt(leng-2) == 'о') {
                        s.setCharAt(leng-2, 'к');
                        s.setCharAt(leng-1, 'у');
                    }
                    else if (s.charAt(leng-2) == 'е') {
                        s.setCharAt(leng-2, 'к');
                        s.setCharAt(leng-1, 'у');
                    }
                    else if (s.charAt(leng-2) == 'ё' && leng > 3) { // -ёк
                        if (isSogl(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s = s.append("у");
                            }
                            else {
                                s.setCharAt(leng-2, 'ь');
                                s.setCharAt(leng-1, 'к');
                                s = s.append("у");
                            }
                        }
                        else {
                            s.setCharAt(leng-2, 'й');
                            s.setCharAt(leng-1, 'к');
                            s = s.append("у");
                        }
                    }
                    else if (gender != 1) {
                        s = s.append("у");
                    }
                    break;

                case 'ц':
                    if (s.charAt(leng-2) == 'е' || s.charAt(leng-2) == 'я') {
                        if (isSogl(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s = s.append("а");
                            }
                            else {
                                if (s.charAt(leng-3) == 'л') {
                                    s.setCharAt(leng-2, 'ь');
                                    s.setCharAt(leng-1, 'ц');
                                    s = s.append("а");
                                }
                                else {
                                    s.setCharAt(leng-2, 'ц');
                                    s.setCharAt(leng-1, 'а');
                                }
                            }
                        }
                        else {
                            s.setCharAt(leng-2, 'й');
                            s.setCharAt(leng-1, 'ц');
                            s = s.append("а");
                        }
                    }
                    else {
                        s = s.append("а");
                    }
                    break;


				case 'ь': // 3- ье склонение
                    if (s.charAt(leng-3) == 'е' && s.charAt(leng-2) == 'н') {
                        if (gender != 1) {
                            s.setCharAt(leng-3, 'н');
                            s.setCharAt(leng-2, 'я');
                            s.setCharAt(leng-1, (char)0);
                        }
                        else {
                            s.setCharAt(leng-1, 'и');
                        }
                    }
                    else if (gender == 1) {
                        s.setCharAt(leng-1, 'и');
                    }
                    else {
                        s.setCharAt(leng-1, 'ю');
                    }
                    break;

                case 'в':
                    if (gender != 1) {
                        s = s.append("а");
                    }
                    break;

                case 'и': // Несклоняемые или множественые
                    if (!isSingular) {
                        if (s.charAt(leng-2) == 'к') { // - ки
                            if (gender == 1) {
                                s.setCharAt(leng-2, 'о');
                                s.setCharAt(leng-1, 'к');
                            }
                            else {
                                s.setCharAt(leng-1, 'о');
                                s = s.append("в");
                            }
                        }
                        else { // Мн. число -и
                            if (isSogl(s.charAt(leng-2))) {
                                s.setCharAt(leng-1, 'е');
                                s = s.append("й");
                            }
                            else {
                                if (gender == 1) {
                                    s.setCharAt(leng-1, 'й');
                                }
                                else {
                                    s.setCharAt(leng-1, 'е');
                                    s = s.append("в");
                                }
                            }
                        }
                    }
                    break;

                case 'ы':
                    if (!isSingular) {
                        s.setCharAt(leng-1, 'о');
                        s = s.append("в");
                    }
                    break;

                case 'у':
					break;

                default : // Разносклоняемые
                    if (gender != 1) {
                        s = s.append("у");
                    }
            }

            if (i == 1) {
                result = s;
            }
            else {
                result = result.append(" ");
                result = result.append(s);
            }
        }

        return result.toString();
    }
    
    /** Склонение: винительный падеж
     * 
     * @param string Входная строка
     * @param gender Род
     * @param isAnimate Одушевлённый (false - неодушевлённый)
     * @param isSingular Единственное число (false - множественное число)
     * @return Преобразованное слово
     */
    public static String vinPad (String string, int gender, boolean isAnimate, boolean isSingular) {
        int maxi = strCount1(string); // Количество слов
        StringBuffer s;
        StringBuffer result = new StringBuffer();

        // Цикл для каждого слова
        for (int i = 1; i <= maxi; i++) {
            s = new StringBuffer(takeWord(string, i));
            int leng = s.length();

            if (isAnimate) {
                result = new StringBuffer(rodPad(string, gender, isSingular));
                return result.toString();
            }
            else {
                switch (s.charAt(leng-1)) {
                    case 'й':
                        if (my1.containsValue(s.charAt(leng-2))) {
                            s.setCharAt(leng-1, 'я');
                        }
                        break;

                    case 'а':
                        if (isSingular) {
                            s.setCharAt(leng-1, 'у');
                        }

                    case 'я':
                        if (s.charAt(leng-2) == 'а') {
                            s.setCharAt(leng-2, 'у');
                            s.setCharAt(leng-1, 'ю');
                        }
                        else if ((isSogl(s.charAt(leng-2)) || s.charAt(leng-2) == 'ь') && isSingular) {
                            s.setCharAt(leng-1, 'ю');
                        }
                        break;
                }
            }

            if (i == 1) {
                result = s;
            }
            else {
                result = result.append(" ");
                result = result.append(s);
            }
        }

        return result.toString();
    }
    
    /** Склонение: творительный падеж
     * 
     * @param string Входная строка
     * @param gender Род
     * @param isSingular Единственное число (false - множественное число)
     * @return Преобразованное слово
     */
    public static String tvrPad (String string, int gender, boolean isSingular) {
        int maxi = strCount1(string); // Количество слов
        StringBuffer s;
        StringBuffer result = new StringBuffer();

        // Цикл для каждого слова
        for (int i = 1; i <= maxi; i++) {
            s = new StringBuffer(takeWord(string, i));
            int leng = s.length();

            switch (s.charAt(leng-1)) {
                case 'а': // 1- ое склонение   (+часть третьего)
                    if (!isSingular) {
                        s = s.append("ми");
                        break;
                    }
                    else {
                        s.setCharAt(leng-1, 'о');
                        s = s.append("й");
                    }
                    break;

                case 'я':
                    if (isSingular) {
                        if (s.charAt(leng-2) == 'м') {
                            s.setCharAt(leng-1, 'е');
                            s = s.append("нем");
                        }
                        else if (i != strCount(string) && s.charAt(leng-2) == 'а') {
                            s.setCharAt(leng-2, 'о');
                            s.setCharAt(leng-1, 'й');
                        }
                        else if (s.charAt(leng-2) == 'я') {
                            s.setCharAt(leng-2, 'е');
                            s.setCharAt(leng-1, 'й');
                        }
                        else if (isSogl(s.charAt(leng-3))) {
                            s.setCharAt(leng-1, 'ё');
                            s = s.append("й");
                        }
                        else {
                            s.setCharAt(leng-1, 'е');
                            s = s.append("й");
                        }
                    }
                    else {
                        if (isSogl(s.charAt(leng-2)) || s.charAt(leng-2) == 'ь') {
                            s.setCharAt(leng-1, 'я');
                            s = s.append("ми");
                        }
                    }
                    break;

                case 'й': // 2- ое склонение или прилаг
                    if (s.charAt(leng-2) == 'о' && i == strCount(string)) {
                        s.setCharAt(leng-1, 'е');
                        s = s.append("м");
                        break;
                    }
                    if (s.charAt(leng-2) == 'и') {
                        if (i != strCount(string)) {
                            if (s.charAt(leng-3) == 'г' || s.charAt(leng-3) == 'к' || s.charAt(leng-3) == 'ж') {
                                s.setCharAt(leng-2, 'и');
                                s.setCharAt(leng-1, 'м');
                            }
                            else if (s.charAt(leng-3) == 'б' || s.charAt(leng-3) == 'з' || s.charAt(leng-3) == 'с') {
                                s.setCharAt(leng-2, 'ь');
                                s.setCharAt(leng-1, 'и');
                                s = s.append("м");
                            }
                            else {
                                s.setCharAt(leng-2, 'е');
                                s.setCharAt(leng-1, 'м');
                            }
                        }
                        else {
                            s.setCharAt(leng-1, 'е');
                            s = s.append("м");
                        }
                    }
                    if (s.charAt(leng-2) == 'о') {
                        if (!isShip(s.charAt(leng-3))) {
                            s.setCharAt(leng-2, 'ы');
                        }
                        else {
                            s.setCharAt(leng-2, 'и');
                        }
                        s.setCharAt(leng-1, 'м');
                        break;
                    }
                    if (s.charAt(leng-2) == 'ы' || s.charAt(leng-2) == 'и' && i != strCount(string)) {
                        s.setCharAt(leng-1, 'м');
                    }
                    else {
                        s.setCharAt(leng-1, 'е');
                        s = s.append("м");
                    }
                    break;

                case 'о':
                    s = s.append("м");
                    break;

                case 'е':
                    if (i != strCount(string)) { //прилагательное
                        if (!isSingular) {
                            s.setCharAt(leng-1, 'м');
                            s = s.append("и");
                        }
                        else {
                            s.setCharAt(leng-2, 'ы');
                            s.setCharAt(leng-1, 'м');
                        }
                    }
                    else { //  существительное
                        s = s.append("м");
                    }
                    break;

                case 'ё':
                    s = s.append("м");
                    break;

                case 'к':
                    if (s.charAt(leng-2) == 'о') {
                        s.setCharAt(leng-2, 'к');
                        s.setCharAt(leng-1, 'о');
                        s = s.append("м");
                    }
                    else if (s.charAt(leng-2) == 'ё' && leng > 3) {
                        if (isSogl(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s.setCharAt(leng-2, 'ь');
                                s.setCharAt(leng-1, 'к');
                                s = s.append("ом");
                            }
                            else {
                                s.setCharAt(leng-2, 'ь');
                                s.setCharAt(leng-1, 'к');
                                s = s.append("ом");
                            }
                        }
                        else {
                            s.setCharAt(leng-2, 'й');
                            s.setCharAt(leng-1, 'к');
                            s = s.append("ом");
                        }
                    }
                    else if (gender != 1) {
                        s = s.append("ом");
                    }
                    break;

                case 'ц':
                    if (s.charAt(leng-2) == 'е' || s.charAt(leng-2) == 'я' && leng > 3) {
                        if (isSogl(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s = s.append("ом");
                            }
                            else {
                                if (s.charAt(leng-3) == 'л') {
                                    s.setCharAt(leng-2, 'ь');
                                    s.setCharAt(leng-1, 'ц');
                                    s = s.append("ем");
                                }
                                else {
                                    s.setCharAt(leng-2, 'ц');
                                    s.setCharAt(leng-1, 'о');
                                    s = s.append("м");
                                }
                            }
                        }
                        else {
                            s.setCharAt(leng-2, 'й');
                            s.setCharAt(leng-1, 'ц');
                            s = s.append("ем");
                        }
                    }
                    else {
                        s = s.append("ем");
                    }
                    break;

                case 'ь': // 3- ье склонение
                    if (s.charAt(leng-3) == 'е' && s.charAt(leng-2) == 'н') {
                        if (gender != 1) {
                            s.setCharAt(leng-3, 'н');
                            s.setCharAt(leng-2, 'е');
                            s.setCharAt(leng-1, 'м');
                        }
                        else {
                            s = s.append("ю");
                        }
                        break;
                    }
                    else if (gender == 1) {
                        s = s.append("ю");
                    }
                    else {
                        s.setCharAt(leng-1, 'е');
                        s = s.append("м");
                    }
                    break;

                case 'и':
                    if (!isSingular) {
                        if (isShip(s.charAt(leng-2)) || s.charAt(leng-2) == 'ж' ||
                                s.charAt(leng-2) == 'к' || s.charAt(leng-2) == 'х') {
                            s.setCharAt(leng-1, 'а');
                        }
                        else {
                            s.setCharAt(leng-1, 'я');
                        }
                        s = s.append("ми");
                    }
                    break;

                case 'ы':
                    if (!isSingular) {
                        s.setCharAt(leng-1, 'а');
                        s = s.append("ми");
                    }
                    break;

                case 'у':
                    break;

                default :
                    if (gender != 1) {
                        s = s.append("ом");
                    }
            }

            if (i == 1) {
                result = s;
            }
            else {
                result = result.append(" ");
                result = result.append(s);
            }
        }

        return result.toString();
    }
    
    /** Склонение: предложный падеж
     * 
     * @param string Входная строка
     * @param gender Род
     * @param isSingular Единственное число (false - множественное число)
     * @return Преобразованное слово
     */
    public static String prePad (String string, int gender, boolean isSingular) {
        int maxi = strCount1(string); // Количество слов
        StringBuffer s;
        StringBuffer result = new StringBuffer();

        // Цикл для каждого слова
        for (int i = 1; i <= maxi; i++) {
            s = new StringBuffer(takeWord(string, i));
            int leng = s.length();

            switch (s.charAt(leng-1)) {
                case 'а': // 1- ое склонение   (+часть третьего)
                    if (!isSingular) {
                        s = s.append("х");
                        break;
                    }
                    else {
                        s.setCharAt(leng-1, 'е');
                    }
                    break;

                case 'я':
                    if (isSingular) {
                        if (s.charAt(leng-2) == 'м') {
                            s.setCharAt(leng-1, 'е');
                            s = s.append("ни");
                        }
                        else if (i != strCount(string) && s.charAt(leng-2) == 'а') {
                            s.setCharAt(leng-2, 'о');
                            s.setCharAt(leng-1, 'й');
                        }
                        else if (s.charAt(leng-2) == 'я') {
                            s.setCharAt(leng-2, 'е');
                            s.setCharAt(leng-1, 'й');
                        }
                        else if (s.charAt(leng-2) == 'ь') {
                            s.setCharAt(leng-1, 'е');
                        }
                        else if (s.charAt(leng-2) == 'и') {
                            s.setCharAt(leng-1, 'и');
                        }
                        else if (isSogl(s.charAt(leng-2)) || s.charAt(leng-2) == 'ь') {
                            if (gender == 1) {
                                s.setCharAt(leng-1, 'е');
                            }
                            else {
                                s.setCharAt(leng-1, 'и');
                            }
                        }
                        else {
                            s.setCharAt(leng-2, 'е');
                        }
                    }
                    else if (isSogl(s.charAt(leng-2)) || s.charAt(leng-2) == 'ь') {
                        s.setCharAt(leng-1, 'я');
                        s = s.append("х");
                    }
                    break;

                case 'й': // 2- ое склонение
                    if (i == strCount(string)) { //существительное
                        if (s.charAt(leng-2) == 'о') {
                            s.setCharAt(leng-1, 'е');
                            break;
                        }
                        if (s.charAt(leng-2) == 'и') {
                            if (s.charAt(leng-3) == 'ш') {
                                s.setCharAt(leng-2, 'е');
                                s.setCharAt(leng-1, 'м');
                            }
                            else {
                                s.setCharAt(leng-1, 'и');
                            }
                        }
                    }
                    else if (s.charAt(leng-2) == 'о') {
                        s.setCharAt(leng-1, 'м');
                    }
                    else if (s.charAt(leng-2) == 'ы') {
                        s.setCharAt(leng-2, 'о');
                        s.setCharAt(leng-1, 'м');
                    }
                    else if (s.charAt(leng-2) == 'и') {
                        if (s.charAt(leng-3) == 'н' || s.charAt(leng-3) == 'ж') {
                            s.setCharAt(leng-2, 'е');
                            s.setCharAt(leng-1, 'м');
                        }
                        else if (s.charAt(leng-3) == 'б' || s.charAt(leng-3) == 'з' || s.charAt(leng-3) == 'с') {
                            s.setCharAt(leng-2, 'ь');
                            s.setCharAt(leng-1, 'е');
                            s = s.append("м");
                        }
                        else {
                            s.setCharAt(leng-2, 'о');
                            s.setCharAt(leng-1, 'м');
                        }
                    }
                    else {
                        s.setCharAt(leng-1, 'е');
                    }
                    break;

                case 'о':
                    s.setCharAt(leng-1, 'е');
                    break;

                case 'е':
                    if (i != strCount(string)) {
                        if (isSingular) {
                            s.setCharAt(leng-1, 'м');
                        }
                        else {
                            s.setCharAt(leng-1, 'х');
                        }
                    }
                    else if (s.charAt(leng-2) == 'и') {
                        s.setCharAt(leng-1, 'и');
                    }
                    break;

                case 'ё':
                    s.setCharAt(leng-1, 'е');
                    break;

                case 'к':
                    if (s.charAt(leng-2) == 'о') {
                        s.setCharAt(leng-2, 'к');
                        s.setCharAt(leng-1, 'е');
                        break;
                    }
                    if (s.charAt(leng-2) == 'е') {
                        if (isShip(s.charAt(leng-3))) {
                            s.setCharAt(leng-2, 'к');
                            s.setCharAt(leng-1, 'е');
                            break;
                        }
                        else {
                            s = s.append("а");
                        }
                    }
                    if (s.charAt(leng-2) == 'ё') {
                        if (isShip(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s = s.append("е");
                            }
                            else {
                                s.setCharAt(leng-2, 'ь');
                                s.setCharAt(leng-1, 'к');
                                s = s.append("е");
                            }
                        }
                        else {
                            s.setCharAt(leng-2, 'й');
                            s.setCharAt(leng-1, 'к');
                            s = s.append("е");
                        }
                        break;
                    }
                    if (gender != 1) {
                        s = s.append("е");
                    }
                    break;

                case 'ц':
                    if (s.charAt(leng-2) == 'е' || s.charAt(leng-2) == 'я' && leng > 3) {
                        if (isSogl(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s = s.append("е");
                            }
                            else {
                                if (s.charAt(leng-3) == 'л') {
                                    s.setCharAt(leng-2, 'ь');
                                    s.setCharAt(leng-1, 'ц');
                                    s = s.append("е");
                                }
                                else {
                                    s.setCharAt(leng-2, 'ц');
                                    s.setCharAt(leng-1, 'е');
                                }
                            }
                        }
                        else {
                            s.setCharAt(leng-2, 'й');
                            s.setCharAt(leng-1, 'ц');
                            s = s.append("е");
                        }
                    }
                    else {
                        s = s.append("е");
                    }
                    break;

                case 'ь': // 3- ье склонение
                    if (s.charAt(leng-3) == 'е' && s.charAt(leng-2) == 'н') {
                        if (gender != 1) {
                            s.setCharAt(leng-3, 'н');
                            s.setCharAt(leng-2, 'е');
                            s.setCharAt(leng-1, (char)0);
                        }
                        else {
                            s.setCharAt(leng-1, 'и');
                        }
                    }
                    else if (gender != 0) {
                        s.setCharAt(leng-1, 'и');
                    }
                    else {
                        s.setCharAt(leng-1, 'е');
                    }
                    break;

                case 'и':
                    if (!isSingular) {
                        if (s.charAt(leng-2) == 'г' || s.charAt(leng-2) == 'ж' || s.charAt(leng-2) == 'к' ||
                                s.charAt(leng-2) == 'х' || s.charAt(leng-2) == 'ч' || s.charAt(leng-2) == 'ш' ||
                                s.charAt(leng-2) == 'щ' || s.charAt(leng-2) == 'а') {
                            s.setCharAt(leng-1, 'а');
                        }
                        else {
                            s.setCharAt(leng-1, 'я');
                        }
                    }
                    break;

                case 'ы':
                    if (!isSingular) {
                        s.setCharAt(leng-1, 'а');
                        s = s.append("х");
                    }
                    break;

                case 'у':
                    break;

                default :
                    if (gender != 1) {
                        s = s.append("е");
                    }
            }

            if (i == 1) {
                result = s;
            }
            else {
                result = result.append(" ");
                result = result.append(s);
            }
        }

        return result.toString();
    }

//------------------------------------------------------------------------------    
    
    /** Синтаксический анализатор фразы
     * 
     * @param str Входная строка
     * @param rules Правила склонения данной фразы
     * @param padeg Падеж в который нужно склонить фразу
     * @return Преобразованная фраза
     */
    public static String doMsg (String str, String rules, Padeg padeg) {
        int genderValue; // Род,
        boolean numberValue, animateValue; // Количество, одушевлённость
        
        // Если слово английское - оставляем как есть
        if (((str.charAt(str.length()-1) >= 65) && (str.charAt(str.length()-1) <= 90)) ||
                ((str.charAt(str.length()-1) >= 97) && (str.charAt(str.length()-1) <= 122))) {
            return str;
        }

        // Определение рода
        switch (rules.charAt(0)) {
            case 'м': case 'М' : genderValue = 0; break;
            case 'ж': case 'Ж' : genderValue = 1; break;
            case 'с': case 'С' : default : genderValue = 2;
        }
        // Определение количества
        switch (rules.charAt(1)) {
            case '0': case 'м': case 'М' : numberValue = false; break;
            case '1': case 'е': case 'Е' : default : numberValue = true;
        }
        // Определение одушевлённости
        switch (rules.charAt(2)) {
            case 'о': case 'О': animateValue = true; break;
            case 'н': case 'Н': default : animateValue = false;
        }

        switch (padeg) {
            case rod: return rodPad(str, genderValue, numberValue);
            case dat: return datPad(str, genderValue, numberValue);
            case vin: return vinPad(str, genderValue, animateValue, numberValue);
            case tvr: return tvrPad(str, genderValue, numberValue);
            case pre: return prePad(str, genderValue, numberValue);
            default : return str;
        }
    }
    
    /** Расставляет переносы в слове */
    public static String doWrapString (String string, int width) {
        int i = 0, size = 0;
        
        // Проходит циклом всю строку
        while (i < string.length()) {
            // Если попался спецсимвол '/' обрабатываем, при условии что не
            if (string.charAt(i) == '/') {
                // Если попалась последовательность '//'
                if (i < string.length() - 1 && string.charAt(i+1) == '/') {
                    i++;
                    continue;
                }
                // Попалась управляющая последовательность вида '/xx/'
                if (i < string.length() - 3 && string.charAt(i+3) == '/') {
                    i += 3;
                    continue;
                }
            }
            // Если попался пробел и кусок строки превысил ширину - вставляем перенос
            if (string.charAt(i) == ' ' && size > width) {
                i = string.lastIndexOf(' ', i-1); // Переходим к предыдущему пробелу
                string = string.substring(0, i) + "/!n/" + string.substring(i+1); // Вставляем туда перенос
                i += 4;
                size = 0;
            } else {
                i++;
                size++;
            }
        }
        
        return string;
    }
}