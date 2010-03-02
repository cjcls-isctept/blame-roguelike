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
 * ����������: ���������� �������� �����
 *
 * �������������: � ������� doMsg ��������� ������, ������� � ����������� �� ��������
 *                rules (������� ���������) � padeg (������ �����), ���������� �� �������� �������� �����.
 */
public class Grammar {
    // ������
    public static enum Padeg {
        rod, // �����������
        dat, // ���������
        vin, // �����������
        tvr, // ������������
        pre  // ����������
    };

    /** ������ ��������� ���� */
    private static HashMap sogl;
    /** ������ ������� ��������� */
    private static HashMap zvon;
    /** ������ ������� ��������� */
    private static HashMap ship;
    
    /** ������ ����, ����������� ��� ��������� */
    private static HashMap my0, my1, my2;
    /** ������ ��������� � ������, ����������� �� ������� */
    private static HashMap exclude;

    /** �����������: ������������� ����������� ���������� */
    static {
        // ���������
        sogl = new HashMap();
        sogl.put(1, '�');
        sogl.put(2, '�');
        sogl.put(3, '�');
        sogl.put(4, '�');
        sogl.put(5, '�');
        sogl.put(6, '�');
        sogl.put(7, '�');
        sogl.put(8, '�');
        sogl.put(9, '�');
        sogl.put(10, '�');
        sogl.put(11, '�');
        sogl.put(12, '�');
        sogl.put(13, '�');
        sogl.put(14, '�');
        sogl.put(15, '�');
        sogl.put(16, '�');
        sogl.put(17, '�');
        sogl.put(18, '�');
        sogl.put(19, '�');
        sogl.put(20, '�');
        sogl.put(21, '�');

        // �������
        zvon = new HashMap();
        zvon.put(1, '�');
        zvon.put(2, '�');
        zvon.put(3, '�');
        zvon.put(4, '�');
        zvon.put(5, '�');
        zvon.put(6, '�');
        zvon.put(7, '�');
        zvon.put(8, '�');
        zvon.put(9, '�');

        // �������
        ship = new HashMap();
        ship.put(1, '�');
        ship.put(2, '�');
        ship.put(3, '�');
        ship.put(4, '�');

        my0 = new HashMap();
        my0.put(1, '�');
        my0.put(2, '�');
        my0.put(3, '�');
        my0.put(4, '�');
        my0.put(5, '�');
        my0.put(6, '�');
        my0.put(7, '�');
        my0.put(8, '�');
        my0.put(9, '�');
        my0.put(10, '�');
        my0.put(11, '�');
        my0.put(12, '�');
        my0.put(13, '�');

        my1 = new HashMap();
        my1.put(1, '�');
        my1.put(2, '�');
        my1.put(3, '�');
        my1.put(4, '�');
        my1.put(5, '�');
        my1.put(6, '�');
        my1.put(7, '�');
        my1.put(8, '�');
        my1.put(9, '�');
        my1.put(10, '�');
        my1.put(11, '�');
        my1.put(12, '�');
        my1.put(13, '�');

        my2 = new HashMap();
        my2.put(1, '�');
        my2.put(2, '�');
        my2.put(3, '�');
        my2.put(4, '�');
        my2.put(5, '�');
        my2.put(6, '�');
        my2.put(7, '�');
        my2.put(8, '�');
        my2.put(9, '�');
        my2.put(10, '�');
        my2.put(11, '�');
        my2.put(12, '�');
        my2.put(13, '�');

        exclude = new HashMap();
        exclude.put(1, "���");
        exclude.put(2, "��");
        exclude.put(3, "��");
        exclude.put(4, "������");
        exclude.put(5, "��");
        exclude.put(6, "�");
        exclude.put(7, "�");
        exclude.put(8, "��");
        exclude.put(9, "���");
        exclude.put(10, "��");
        exclude.put(11, "���");
        exclude.put(12, "���");
    }

    /** ���������, �������� �� ������� ����� ���������
     * 
     * @param c ����������� �����
     * @return ��������� ��������
     */
    private static boolean isSogl (char c) {
        return sogl.containsValue(c);
    }
    
    /** ���������, �������� �� ������� ����� �������
     * 
     * @param c ����������� �����
     * @return ��������� ��������
     */
    private static boolean isShip (char c) {
        return ship.containsValue(c);
    }
    
    /** ��������������� ������ � ������ �������
     * 
     * @param c ������� ������
     * @return ������, �������������� � ������� ��������
     */
    private static char lowc (char c) {
        return String.valueOf(c).toLowerCase().charAt(1);
    }
   
    /** ���������� ���������� ���� � ������
     * 
     * @param s ������� ������
     * @return ���������� ���� � ������
     */
    private static int strCount (String s) {
        int count = 1;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') count++;
        }
        return count;
    }
    
    /** ������������ ���������� ���� � ������, �� ������� ����� � ��������
     * 
     * @param s ������� ������
     * @return ���������� ���� � ������
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
    
    /** �������� �������� �� �����
     * 
     * @param s
     * @param number
     * @return
     */
    private static String takeWord (String s, int number) {
        // ��������: ������, ��� �������� ������ �����
        if ((number > strCount(s)) || (number < 0))
            return "!error";

        // ���� ������ �����
        int start = 0;  // ������ ������ �����
        int end = 0;    // ������ ����� �����
        if (number > 1) {   // ���� ����� �� ������, ���� ������
            // ����� ������ �����
            start = s.indexOf(' ');
            number--;
            while ((start != -1) && (number > 1)) {
                start = s.indexOf(' ', start+1);
                number--;
            }
            // ������ ����� - ��������� �� ��������
            start++;
            
            // ��������: ���� ����� ������ ������ -1, ������ ���� ������
            // �� ������� �������� number
            if (start == -1)
                return "!error";
            else { // ���� ����� �����
                end = s.indexOf(' ', start);
                // ���� ��������� �� ������ ������ �����������
                // (��������� ����� �� ������� ������)
                // ���������� ����� ������
                if (end == -1)
                    end = s.length();
            }
        }
        else {              // ���� ����� ������
            start = 0;
            end = s.indexOf(' '); // �������� ������ ����� �����
            // ���� ����� ����, ���������� ������� ������
            if (end == -1)
                return s; 
        }
        
        // ���������� ��������� ���������
        return s.substring(start, end);
    }
    
//------------------------------------------------------------------------------
    /*::: ��������� �� ������� :::*/
    
    /** ���������: ����������� �����
     * 
     * @param string ������� ������
     * @param gender ���
     * @param isSingular ������������ ����� (false - ������������� �����)
     * @return ��������������� �����
     */
    public static String rodPad (String string, int gender, boolean isSingular) {
        int maxi = strCount1(string); // ���������� ����
        StringBuffer s;
        StringBuffer result = new StringBuffer();

        // ���� ��� ������� �����
        for (int i = 1; i <= maxi; i++) {
            s = new StringBuffer(takeWord(string, i));
            int leng = s.length();

            switch (s.charAt(leng-1)) {
                // 1-�� ��������� + ����� ��������
                case '�':
                    //<��><����>�
                    if (!isSingular && isSogl(s.charAt(leng-3)) && isSogl(s.charAt(leng-2))) {
                        if (s.charAt(leng-3) == '�') {
                            s.setCharAt(leng-3, '�');
                            s.setCharAt(leng-1, (char)0);
                            break;
                        }
                        if (s.charAt(leng-3) == '�') {
                            s.setCharAt(leng-3, '�');
                            s = s.append("�");
                            break;
                        }
                        s.setCharAt(leng-1, (char)0);
                        break;
                    }
                    //<����><����>�
                    if (!isSingular && isSogl(s.charAt(leng-3)) && isSogl(s.charAt(leng-2))) {
                        if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-1, (char)0);
                            break;
                        }
                        s.setCharAt(leng-1, s.charAt(leng-2));
                        s.setCharAt(leng-2, '�');
                        break;
                    }
                    if (my0.containsValue(s.charAt(leng-2)))
                        s.setCharAt(leng-1, '�');
                    else
                        s.setCharAt(leng-1, '�');
                    break;

                case '�':
                    // �������. ����� -�
                    if (isSingular) {
                        if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-1, '�');
                            s = s.append("��");
                        } // �������� ��������� "-��"
                        else if (isShip(s.charAt(leng-3)) && s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                        }
                        else if (s.charAt(leng-2) == '�' && i != strCount(string)) {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                        }
                        else if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                        }
                        else
                            s.setCharAt(leng-1, '�');
                    }
                    else { // ������������� ����� -�
                        if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                        }
                        else if (isSogl(s.charAt(leng-2))) {
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                        }
                        else if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-1, '�');
                        }
                    }
                    break;

                // 2- �� ���������
                case '�':
                    if (my1.containsValue(s.charAt(leng-2))) {
                        s.setCharAt(leng-1, '�');
                        break;
                    }
                    //-��
                    if (s.charAt(leng-2) == '�') {
                        if (i != strCount(string)) {
                            if (s.charAt(leng-3) == '�' || s.charAt(leng-3) == '�') {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                                s = s.append("�");
                                break;
                            }
                            if (s.charAt(leng-3) == '�' || s.charAt(leng-3) == '�' || s.charAt(leng-3) == 'c') {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                                s = s.append("��");
                                break;
                            }
                            {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                                s = s.append("�");
                                break;
                            }
                        }
                        else {
                            s.setCharAt(leng-2, '�');
                            break;
                        }
                    }
                    // -��
                    if (s.charAt(leng-2) == '�') {
                        if (i == strCount(string)) {
                            s.setCharAt(leng-1, '�');
                            break;
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                            break;
                        }
                    }
                    //-��
                    if (s.charAt(leng-2) == '�') {
                        if (i != strCount(string)) {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                            break;
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                            break;
                        }
                    }
                    s.setCharAt(leng-1, '�');
                    break;

                case '�':
                    if (isSogl(s.charAt(leng-2))) {
                        s.setCharAt(leng-1, '�');
                    }
                    break;

                case '�':
                    if (i != strCount(string)) { //��������������
                        if (isSingular) {
                            s.setCharAt(leng-1, '�');
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                        }
                    }
                    else { // ���������������
                        if (isSogl(s.charAt(leng-2)) && !isShip(s.charAt(leng-2))) {
                            s.setCharAt(leng-1, '�');
                        }
                        else if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-1, '�');
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                        }
                    }
                    break;

                case '�':
                    if (isSogl(s.charAt(leng-2))) {
                        s.setCharAt(leng-1, '�');
                    }
                    else if (s.charAt(leng-2) == '�') {
                        s.setCharAt(leng-1, '�');
                    }
                    break;

                case '�':
                    if (s.charAt(leng-2) == '�') {
                        s.setCharAt(leng-2, '�');
                        s.setCharAt(leng-1, '�');
                    }
                    else if (s.charAt(leng-2) == '�') {
                        s.setCharAt(leng-2, '�');
                        s.setCharAt(leng-1, '�');
                    }
                    else if (s.charAt(leng-2) == '�' && (leng > 3)) { // -��
                        if (isSogl(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s = s.append("�");
                            }
                            else {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                                s = s.append("�");
                            }
                        }
                        else {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                        }
                    }
                    else if (gender != 1) {
                        s = s.append("�");
                    }
                    break;

                case '�':
                    if (s.charAt(leng-2) == '�' || s.charAt(leng-2) == '�') {
                        if (isSogl(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s = s.append("�");
                            }
                            else {
                                if (s.charAt(leng-3) == '�') {
                                    s.setCharAt(leng-2, '�');
                                    s.setCharAt(leng-1, '�');
                                    s = s.append("�");
                                }
                                else {
                                    s.setCharAt(leng-2, '�');
                                    s.setCharAt(leng-1, '�');
                                }
                            }
                        }
                        else {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                        }
                    }
                    else {
                        s = s.append("�");
                    }
                    break;

                // 3- �� ���������
		case '�':
                    if (s.charAt(leng-3) == '�' && s.charAt(leng-2) == '�') {
                        if (gender == 1) {
                            s.setCharAt(leng-3, '�');
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, (char)0);
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                        }
                    }
                    else {
                        if (gender == 1) {
                            s.setCharAt(leng-1, '�');
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                        }
                    }
                    break;

		case '�':
                    if (gender != 1) {
                        s = s.append("�");
                    }
                    break;

                case '�': // ������������ ��� ������������
                    if (!isSingular) {
                        // - ��
                        if (s.charAt(leng-2) == '�') {
                            if (gender == 1) {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                            }
                            else {
                                s.setCharAt(leng-2, '�');
                                s.append("�");
                            }
                        }
                        else { // ��. ����� -�
                            if (isSogl(s.charAt(leng-2))) {
                                s.setCharAt(leng-1, '�');
                                s = s.append("�");
                            }
                            else {
                                if (gender == 1) {
                                    s.setCharAt(leng-1, '�');
                                }
                                else {
                                    s.setCharAt(leng-1, '�');
                                    s = s.append("�");
                                }
                            }
                        }
                    }
                    break;

                case '�':
                    if (!isSingular) {
                        s.setCharAt(leng-1, '�');
                        s = s.append("�");
                    }
                    break;

		case '�':
                    break;

                default : // ���������������
                    if (gender != 1)
                        s = s.append("�");
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
    
    /** ���������: ��������� �����
     * 
     * @param string ������� ������
     * @param gender ���
     * @param isSingular ������������ ����� (false - ������������� �����)
     * @return ��������������� �����
     */
    public static String datPad (String string, int gender, boolean isSingular) {
        int maxi = strCount1(string); // ���������� ����
        StringBuffer s;
        StringBuffer result = new StringBuffer();

        // ���� ��� ������� �����
        for (int i = 1; i <= maxi; i++) {
            s = new StringBuffer(takeWord(string, i));
            int leng = s.length();

            switch (s.charAt(leng-1)) {
                // 1- �� ���������   (+����� ��������)
                case '�':
                    if (!isSingular && isSogl(s.charAt(leng-3)) && isSogl(s.charAt(leng-2))) {
                        s = s.append("�");
                        break;
                    }
                    if (!isSingular) {
                        s = s.append("�");
                    }
                    else {
                        s.setCharAt(leng-1, '�');
                    }
                    break;

                case '�':
                    if (isSingular) { // ��. �����
                        if (s.charAt(leng-2) == '�') { // �������� ��������� "-��" (��.�.)
                            s.setCharAt(leng-1, '�');
                            s = s.append("��");
                        }
                        else if (isShip(s.charAt(leng-3)) && s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                        }
                        else if (s.charAt(leng-2) == '�' && i != strCount(string)) {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                        }
                        else if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                        }
                        else if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-1, '�');
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                        }
                    }
                    else { // ������������� �����
                        s.setCharAt(leng-1, '�');
                        s = s.append("�");
                    }
                    break;

                case '�': // 2- �� ���������
                    if (my1.containsValue(s.charAt(leng-2))) {
                        s.setCharAt(leng-1, '�');
                        break;
                    }
                    if (s.charAt(leng-2) == '�') { //-��
                        if (i != strCount(string)) {
                            if (s.charAt(leng-3) == '�' || s.charAt(leng-3) == '�') {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                                s = s.append("�");
                            }
                            else if (s.charAt(leng-3) == '�' || s.charAt(leng-3) == '�' || s.charAt(leng-3) == '�') {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                                s = s.append("��");
                            }
                            else {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                                s = s.append("�");
                            }
                            break;
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                            break;
                        }
                    }
                    if (s.charAt(leng-2) == '�') {
                        if (i == strCount(string)) {
                            s.setCharAt(leng-1, '�');
                            break;
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                            break;
                        }
                    }
                    if (s.charAt(leng-2) == '�') {
                        if (i != strCount(string)) {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                            break;
                        }
                    }
                    s.setCharAt(leng-1, '�');
                    break;

                case '�':
                    if (isSogl(s.charAt(leng-2))) {
                        s.setCharAt(leng-1, '�');
                    }
                    break;

                case '�':
                    if (i != strCount(string)) { //��������������
                        if (!isSingular) {
                            s.setCharAt(leng-1, '�');
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                        }
                    }
                    else { // ���������������
                        if (isSogl(s.charAt(leng-2)) && !isShip(s.charAt(leng-2))) {
                            s.setCharAt(leng-1, '�');
                        }
                        else if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-1, '�');
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                        }
                    }
                    break;

                case '�':
                    if (isSogl(s.charAt(leng-2))) {
                        s.setCharAt(leng-1, '�');
                    }
                    else if (s.charAt(leng-2) == '�') {
                        s.setCharAt(leng-1, '�');
                    }
                    break;

                case '�':
                    if (s.charAt(leng-2) == '�') {
                        s.setCharAt(leng-2, '�');
                        s.setCharAt(leng-1, '�');
                    }
                    else if (s.charAt(leng-2) == '�') {
                        s.setCharAt(leng-2, '�');
                        s.setCharAt(leng-1, '�');
                    }
                    else if (s.charAt(leng-2) == '�' && leng > 3) { // -��
                        if (isSogl(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s = s.append("�");
                            }
                            else {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                                s = s.append("�");
                            }
                        }
                        else {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                        }
                    }
                    else if (gender != 1) {
                        s = s.append("�");
                    }
                    break;

                case '�':
                    if (s.charAt(leng-2) == '�' || s.charAt(leng-2) == '�') {
                        if (isSogl(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s = s.append("�");
                            }
                            else {
                                if (s.charAt(leng-3) == '�') {
                                    s.setCharAt(leng-2, '�');
                                    s.setCharAt(leng-1, '�');
                                    s = s.append("�");
                                }
                                else {
                                    s.setCharAt(leng-2, '�');
                                    s.setCharAt(leng-1, '�');
                                }
                            }
                        }
                        else {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                        }
                    }
                    else {
                        s = s.append("�");
                    }
                    break;


				case '�': // 3- �� ���������
                    if (s.charAt(leng-3) == '�' && s.charAt(leng-2) == '�') {
                        if (gender != 1) {
                            s.setCharAt(leng-3, '�');
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, (char)0);
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                        }
                    }
                    else if (gender == 1) {
                        s.setCharAt(leng-1, '�');
                    }
                    else {
                        s.setCharAt(leng-1, '�');
                    }
                    break;

                case '�':
                    if (gender != 1) {
                        s = s.append("�");
                    }
                    break;

                case '�': // ������������ ��� ������������
                    if (!isSingular) {
                        if (s.charAt(leng-2) == '�') { // - ��
                            if (gender == 1) {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                            }
                            else {
                                s.setCharAt(leng-1, '�');
                                s = s.append("�");
                            }
                        }
                        else { // ��. ����� -�
                            if (isSogl(s.charAt(leng-2))) {
                                s.setCharAt(leng-1, '�');
                                s = s.append("�");
                            }
                            else {
                                if (gender == 1) {
                                    s.setCharAt(leng-1, '�');
                                }
                                else {
                                    s.setCharAt(leng-1, '�');
                                    s = s.append("�");
                                }
                            }
                        }
                    }
                    break;

                case '�':
                    if (!isSingular) {
                        s.setCharAt(leng-1, '�');
                        s = s.append("�");
                    }
                    break;

                case '�':
					break;

                default : // ���������������
                    if (gender != 1) {
                        s = s.append("�");
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
    
    /** ���������: ����������� �����
     * 
     * @param string ������� ������
     * @param gender ���
     * @param isAnimate ����������� (false - �������������)
     * @param isSingular ������������ ����� (false - ������������� �����)
     * @return ��������������� �����
     */
    public static String vinPad (String string, int gender, boolean isAnimate, boolean isSingular) {
        int maxi = strCount1(string); // ���������� ����
        StringBuffer s;
        StringBuffer result = new StringBuffer();

        // ���� ��� ������� �����
        for (int i = 1; i <= maxi; i++) {
            s = new StringBuffer(takeWord(string, i));
            int leng = s.length();

            if (isAnimate) {
                result = new StringBuffer(rodPad(string, gender, isSingular));
                return result.toString();
            }
            else {
                switch (s.charAt(leng-1)) {
                    case '�':
                        if (my1.containsValue(s.charAt(leng-2))) {
                            s.setCharAt(leng-1, '�');
                        }
                        break;

                    case '�':
                        if (isSingular) {
                            s.setCharAt(leng-1, '�');
                        }

                    case '�':
                        if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                        }
                        else if ((isSogl(s.charAt(leng-2)) || s.charAt(leng-2) == '�') && isSingular) {
                            s.setCharAt(leng-1, '�');
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
    
    /** ���������: ������������ �����
     * 
     * @param string ������� ������
     * @param gender ���
     * @param isSingular ������������ ����� (false - ������������� �����)
     * @return ��������������� �����
     */
    public static String tvrPad (String string, int gender, boolean isSingular) {
        int maxi = strCount1(string); // ���������� ����
        StringBuffer s;
        StringBuffer result = new StringBuffer();

        // ���� ��� ������� �����
        for (int i = 1; i <= maxi; i++) {
            s = new StringBuffer(takeWord(string, i));
            int leng = s.length();

            switch (s.charAt(leng-1)) {
                case '�': // 1- �� ���������   (+����� ��������)
                    if (!isSingular) {
                        s = s.append("��");
                        break;
                    }
                    else {
                        s.setCharAt(leng-1, '�');
                        s = s.append("�");
                    }
                    break;

                case '�':
                    if (isSingular) {
                        if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-1, '�');
                            s = s.append("���");
                        }
                        else if (i != strCount(string) && s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                        }
                        else if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                        }
                        else if (isSogl(s.charAt(leng-3))) {
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                        }
                    }
                    else {
                        if (isSogl(s.charAt(leng-2)) || s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-1, '�');
                            s = s.append("��");
                        }
                    }
                    break;

                case '�': // 2- �� ��������� ��� ������
                    if (s.charAt(leng-2) == '�' && i == strCount(string)) {
                        s.setCharAt(leng-1, '�');
                        s = s.append("�");
                        break;
                    }
                    if (s.charAt(leng-2) == '�') {
                        if (i != strCount(string)) {
                            if (s.charAt(leng-3) == '�' || s.charAt(leng-3) == '�' || s.charAt(leng-3) == '�') {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                            }
                            else if (s.charAt(leng-3) == '�' || s.charAt(leng-3) == '�' || s.charAt(leng-3) == '�') {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                                s = s.append("�");
                            }
                            else {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                            }
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                        }
                    }
                    if (s.charAt(leng-2) == '�') {
                        if (!isShip(s.charAt(leng-3))) {
                            s.setCharAt(leng-2, '�');
                        }
                        else {
                            s.setCharAt(leng-2, '�');
                        }
                        s.setCharAt(leng-1, '�');
                        break;
                    }
                    if (s.charAt(leng-2) == '�' || s.charAt(leng-2) == '�' && i != strCount(string)) {
                        s.setCharAt(leng-1, '�');
                    }
                    else {
                        s.setCharAt(leng-1, '�');
                        s = s.append("�");
                    }
                    break;

                case '�':
                    s = s.append("�");
                    break;

                case '�':
                    if (i != strCount(string)) { //��������������
                        if (!isSingular) {
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                        }
                        else {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                        }
                    }
                    else { //  ���������������
                        s = s.append("�");
                    }
                    break;

                case '�':
                    s = s.append("�");
                    break;

                case '�':
                    if (s.charAt(leng-2) == '�') {
                        s.setCharAt(leng-2, '�');
                        s.setCharAt(leng-1, '�');
                        s = s.append("�");
                    }
                    else if (s.charAt(leng-2) == '�' && leng > 3) {
                        if (isSogl(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                                s = s.append("��");
                            }
                            else {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                                s = s.append("��");
                            }
                        }
                        else {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                            s = s.append("��");
                        }
                    }
                    else if (gender != 1) {
                        s = s.append("��");
                    }
                    break;

                case '�':
                    if (s.charAt(leng-2) == '�' || s.charAt(leng-2) == '�' && leng > 3) {
                        if (isSogl(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s = s.append("��");
                            }
                            else {
                                if (s.charAt(leng-3) == '�') {
                                    s.setCharAt(leng-2, '�');
                                    s.setCharAt(leng-1, '�');
                                    s = s.append("��");
                                }
                                else {
                                    s.setCharAt(leng-2, '�');
                                    s.setCharAt(leng-1, '�');
                                    s = s.append("�");
                                }
                            }
                        }
                        else {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                            s = s.append("��");
                        }
                    }
                    else {
                        s = s.append("��");
                    }
                    break;

                case '�': // 3- �� ���������
                    if (s.charAt(leng-3) == '�' && s.charAt(leng-2) == '�') {
                        if (gender != 1) {
                            s.setCharAt(leng-3, '�');
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                        }
                        else {
                            s = s.append("�");
                        }
                        break;
                    }
                    else if (gender == 1) {
                        s = s.append("�");
                    }
                    else {
                        s.setCharAt(leng-1, '�');
                        s = s.append("�");
                    }
                    break;

                case '�':
                    if (!isSingular) {
                        if (isShip(s.charAt(leng-2)) || s.charAt(leng-2) == '�' ||
                                s.charAt(leng-2) == '�' || s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-1, '�');
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                        }
                        s = s.append("��");
                    }
                    break;

                case '�':
                    if (!isSingular) {
                        s.setCharAt(leng-1, '�');
                        s = s.append("��");
                    }
                    break;

                case '�':
                    break;

                default :
                    if (gender != 1) {
                        s = s.append("��");
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
    
    /** ���������: ���������� �����
     * 
     * @param string ������� ������
     * @param gender ���
     * @param isSingular ������������ ����� (false - ������������� �����)
     * @return ��������������� �����
     */
    public static String prePad (String string, int gender, boolean isSingular) {
        int maxi = strCount1(string); // ���������� ����
        StringBuffer s;
        StringBuffer result = new StringBuffer();

        // ���� ��� ������� �����
        for (int i = 1; i <= maxi; i++) {
            s = new StringBuffer(takeWord(string, i));
            int leng = s.length();

            switch (s.charAt(leng-1)) {
                case '�': // 1- �� ���������   (+����� ��������)
                    if (!isSingular) {
                        s = s.append("�");
                        break;
                    }
                    else {
                        s.setCharAt(leng-1, '�');
                    }
                    break;

                case '�':
                    if (isSingular) {
                        if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-1, '�');
                            s = s.append("��");
                        }
                        else if (i != strCount(string) && s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                        }
                        else if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                        }
                        else if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-1, '�');
                        }
                        else if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-1, '�');
                        }
                        else if (isSogl(s.charAt(leng-2)) || s.charAt(leng-2) == '�') {
                            if (gender == 1) {
                                s.setCharAt(leng-1, '�');
                            }
                            else {
                                s.setCharAt(leng-1, '�');
                            }
                        }
                        else {
                            s.setCharAt(leng-2, '�');
                        }
                    }
                    else if (isSogl(s.charAt(leng-2)) || s.charAt(leng-2) == '�') {
                        s.setCharAt(leng-1, '�');
                        s = s.append("�");
                    }
                    break;

                case '�': // 2- �� ���������
                    if (i == strCount(string)) { //���������������
                        if (s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-1, '�');
                            break;
                        }
                        if (s.charAt(leng-2) == '�') {
                            if (s.charAt(leng-3) == '�') {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                            }
                            else {
                                s.setCharAt(leng-1, '�');
                            }
                        }
                    }
                    else if (s.charAt(leng-2) == '�') {
                        s.setCharAt(leng-1, '�');
                    }
                    else if (s.charAt(leng-2) == '�') {
                        s.setCharAt(leng-2, '�');
                        s.setCharAt(leng-1, '�');
                    }
                    else if (s.charAt(leng-2) == '�') {
                        if (s.charAt(leng-3) == '�' || s.charAt(leng-3) == '�') {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                        }
                        else if (s.charAt(leng-3) == '�' || s.charAt(leng-3) == '�' || s.charAt(leng-3) == '�') {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                        }
                        else {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                        }
                    }
                    else {
                        s.setCharAt(leng-1, '�');
                    }
                    break;

                case '�':
                    s.setCharAt(leng-1, '�');
                    break;

                case '�':
                    if (i != strCount(string)) {
                        if (isSingular) {
                            s.setCharAt(leng-1, '�');
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                        }
                    }
                    else if (s.charAt(leng-2) == '�') {
                        s.setCharAt(leng-1, '�');
                    }
                    break;

                case '�':
                    s.setCharAt(leng-1, '�');
                    break;

                case '�':
                    if (s.charAt(leng-2) == '�') {
                        s.setCharAt(leng-2, '�');
                        s.setCharAt(leng-1, '�');
                        break;
                    }
                    if (s.charAt(leng-2) == '�') {
                        if (isShip(s.charAt(leng-3))) {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                            break;
                        }
                        else {
                            s = s.append("�");
                        }
                    }
                    if (s.charAt(leng-2) == '�') {
                        if (isShip(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s = s.append("�");
                            }
                            else {
                                s.setCharAt(leng-2, '�');
                                s.setCharAt(leng-1, '�');
                                s = s.append("�");
                            }
                        }
                        else {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                        }
                        break;
                    }
                    if (gender != 1) {
                        s = s.append("�");
                    }
                    break;

                case '�':
                    if (s.charAt(leng-2) == '�' || s.charAt(leng-2) == '�' && leng > 3) {
                        if (isSogl(s.charAt(leng-3))) {
                            if (isSogl(s.charAt(leng-4))) {
                                s = s.append("�");
                            }
                            else {
                                if (s.charAt(leng-3) == '�') {
                                    s.setCharAt(leng-2, '�');
                                    s.setCharAt(leng-1, '�');
                                    s = s.append("�");
                                }
                                else {
                                    s.setCharAt(leng-2, '�');
                                    s.setCharAt(leng-1, '�');
                                }
                            }
                        }
                        else {
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, '�');
                            s = s.append("�");
                        }
                    }
                    else {
                        s = s.append("�");
                    }
                    break;

                case '�': // 3- �� ���������
                    if (s.charAt(leng-3) == '�' && s.charAt(leng-2) == '�') {
                        if (gender != 1) {
                            s.setCharAt(leng-3, '�');
                            s.setCharAt(leng-2, '�');
                            s.setCharAt(leng-1, (char)0);
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                        }
                    }
                    else if (gender != 0) {
                        s.setCharAt(leng-1, '�');
                    }
                    else {
                        s.setCharAt(leng-1, '�');
                    }
                    break;

                case '�':
                    if (!isSingular) {
                        if (s.charAt(leng-2) == '�' || s.charAt(leng-2) == '�' || s.charAt(leng-2) == '�' ||
                                s.charAt(leng-2) == '�' || s.charAt(leng-2) == '�' || s.charAt(leng-2) == '�' ||
                                s.charAt(leng-2) == '�' || s.charAt(leng-2) == '�') {
                            s.setCharAt(leng-1, '�');
                        }
                        else {
                            s.setCharAt(leng-1, '�');
                        }
                    }
                    break;

                case '�':
                    if (!isSingular) {
                        s.setCharAt(leng-1, '�');
                        s = s.append("�");
                    }
                    break;

                case '�':
                    break;

                default :
                    if (gender != 1) {
                        s = s.append("�");
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
    
    /** �������������� ���������� �����
     * 
     * @param str ������� ������
     * @param rules ������� ��������� ������ �����
     * @param padeg ����� � ������� ����� �������� �����
     * @return ��������������� �����
     */
    public static String doMsg (String str, String rules, Padeg padeg) {
        int genderValue; // ���,
        boolean numberValue, animateValue; // ����������, �������������
        
        // ���� ����� ���������� - ��������� ��� ����
        if (((str.charAt(str.length()-1) >= 65) && (str.charAt(str.length()-1) <= 90)) ||
                ((str.charAt(str.length()-1) >= 97) && (str.charAt(str.length()-1) <= 122))) {
            return str;
        }

        // ����������� ����
        switch (rules.charAt(0)) {
            case '�': case '�' : genderValue = 0; break;
            case '�': case '�' : genderValue = 1; break;
            case '�': case '�' : default : genderValue = 2;
        }
        // ����������� ����������
        switch (rules.charAt(1)) {
            case '0': case '�': case '�' : numberValue = false; break;
            case '1': case '�': case '�' : default : numberValue = true;
        }
        // ����������� �������������
        switch (rules.charAt(2)) {
            case '�': case '�': animateValue = true; break;
            case '�': case '�': default : animateValue = false;
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
    
    /** ����������� �������� � ����� */
    public static String doWrapString (String string, int width) {
        int i = 0, size = 0;
        
        // �������� ������ ��� ������
        while (i < string.length()) {
            // ���� ������� ���������� '/' ������������, ��� ������� ��� ��
            if (string.charAt(i) == '/') {
                // ���� �������� ������������������ '//'
                if (i < string.length() - 1 && string.charAt(i+1) == '/') {
                    i++;
                    continue;
                }
                // �������� ����������� ������������������ ���� '/xx/'
                if (i < string.length() - 3 && string.charAt(i+3) == '/') {
                    i += 3;
                    continue;
                }
            }
            // ���� ������� ������ � ����� ������ �������� ������ - ��������� �������
            if (string.charAt(i) == ' ' && size > width) {
                i = string.lastIndexOf(' ', i-1); // ��������� � ����������� �������
                string = string.substring(0, i) + "/!n/" + string.substring(i+1); // ��������� ���� �������
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