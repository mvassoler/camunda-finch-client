package com.finch.camunda.client.legacy;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Util implements Serializable {

    public static final String REGEX_ACCENTS = "[^\\p{ASCII}]";

    public static boolean like(final String str, final String expr) {
        boolean startLike = expr.startsWith("%");
        boolean endLike = expr.endsWith("%");
        String exprNormalizado = removeAcentos(expr);
        if (!startLike) {
            exprNormalizado = "%" + exprNormalizado;
        }
        if (!endLike) {
            exprNormalizado = exprNormalizado + "%";
        }
        final String strNormalizado = removeAcentos(str);
        String regex = quotemeta(exprNormalizado);
        regex = regex.replace("_", ".").replace("%", ".*?");
        Pattern p = Pattern.compile(regex,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        return p.matcher(strNormalizado).matches();
    }

    public static String quotemeta(String s) {
        if (s == null) {
            throw new IllegalArgumentException("String cannot be null");
        }
        int len = s.length();
        if (len == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(len * 2);
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if ("[](){}.*+?$^|#\\".indexOf(c) != -1) {
                sb.append("\\");
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static String removeAcentos(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll(REGEX_ACCENTS, "");
    }

    public static String trataNome(String valor) {
        valor = valor.replaceAll("[ÂÀÁÄÃ]", "A");
        valor = valor.replaceAll("[âãàáä]", "a");
        valor = valor.replaceAll("[ÊÈÉË]", "E");
        valor = valor.replaceAll("[êèéë]", "e");
        valor = valor.replaceAll("[ÎÍÌÏ]", "I");
        valor = valor.replaceAll("[îíìï]", "i");
        valor = valor.replaceAll("[ÔÕÒÓÖ]", "O");
        valor = valor.replaceAll("[ôõòóö]", "o");
        valor = valor.replaceAll("[ÛÙÚÜ]", "U");
        valor = valor.replaceAll("[ûúùü]", "u");
        valor = valor.replaceAll("Ç", "C");
        valor = valor.replaceAll("ç", "c");
        valor = valor.replaceAll("[ýÿ]", "y");
        valor = valor.replaceAll("Ý", "Y");
        valor = valor.replaceAll("ñ", "n");
        valor = valor.replaceAll("Ñ", "N");
        valor = valor.replaceAll("¹", "1");
        valor = valor.replaceAll("²", "2");
        valor = valor.replaceAll("³", "3");
        valor = valor.replaceAll("ª", "a");
        valor = valor.replaceAll("º", "o");
        valor = valor.replaceAll("'", "");
        valor = valor.replaceAll(" ", "_");
        valor = valor.replaceAll(",", "");
        valor = valor.toLowerCase();
        return valor;
    }

    public static String retornaMensagem(String chave, Object... params) {
        Locale locale = recuperaLocale();

        ResourceBundle rb = ResourceBundle.getBundle("messages", locale);
        if (rb.containsKey(chave)) {
            if (params.length > 0) {
                return MessageFormat.format(rb.getString(chave), params);
            } else {
                return rb.getString(chave);
            }
        }

//        return new StringBuilder("Erro ao localizar mensagem: ").append(chave).toString();
        return null;
    }

    public static Locale recuperaLocale() {
        return new Locale("pt", "BR");
    }

}
