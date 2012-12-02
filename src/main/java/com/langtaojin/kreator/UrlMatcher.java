package com.langtaojin.kreator;

import com.langtaojin.kreator.exception.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UrlMatcher {
    final Logger log = LoggerFactory.getLogger(getClass());
    final String url;
    int[] orders;
    Pattern pattern;
    static final String[] EMPTY_STRINGS = new String[0];
    static final String SAFE_CHARS = "/$-_.+!*'(),";

    public UrlMatcher(String url) {
        this.url = url;
        StringBuilder sb = new StringBuilder(url.length() + 20);
        sb.append('^');
        List paramList = new ArrayList();
        Set paramSet = new HashSet();
        int start = 0;
        while (true) {
            int n = url.indexOf('$', start);
            if ((n != -1) && (n < url.length() - 1) && (isParamIndex(url.charAt(n + 1)))) {
                int i = url.charAt(n + 1) - '0';

                paramSet.add(Integer.valueOf(i));
                paramList.add(Integer.valueOf(i));
                addExactMatch(sb, url.substring(start, n));
                addParameterMatch(sb);
                start = n + 2;
            } else {
                addExactMatch(sb, url.substring(start, url.length()));
                break;
            }
        }

        if (paramList.size() != paramSet.size())
            throw new ConfigException("Duplicate parameters.");
        for (int i = 1; i <= paramSet.size(); i++) {
            if (!paramSet.contains(Integer.valueOf(i)))
                throw new ConfigException("Missing parameter '$" + i + "'.");
        }
        this.orders = new int[paramList.size()];
        for (int i = 0; i < paramList.size(); i++) {
            this.orders[i] = (((Integer) paramList.get(i)).intValue() - 1);
        }
        sb.append('$');
        this.pattern = Pattern.compile(sb.toString());
    }

    public int getArgumentCount() {
        return this.orders.length;
    }

    boolean isParamIndex(char c) {
        return (c >= '1') && (c <= '9');
    }

    void addParameterMatch(StringBuilder sb) {
        sb.append("([^\\/]*)");
    }

    void addExactMatch(StringBuilder sb, String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c >= 'a') && (c <= 'z')) {
                sb.append(c);
            } else if ((c >= 'A') && (c <= 'Z')) {
                sb.append(c);
            } else if ((c >= '0') && (c <= '9')) {
                sb.append(c);
            } else {
                int n = "/$-_.+!*'(),".indexOf(c);
                if (n == -1) {
                    this.log.warn("Warning: URL contains unsafe character '" + c + "'.");
                    sb.append("\\u").append(toHex(c));
                } else {
                    sb.append('\\').append(c);
                }
            }
        }
    }

    String toHex(char c) {
        int i = c;
        return Integer.toHexString(i).toUpperCase();
    }

    public String[] getMatchedParameters(String url) {
        Matcher m = this.pattern.matcher(url);
        if (!m.matches())
            return null;
        if (this.orders.length == 0)
            return EMPTY_STRINGS;
        String[] params = new String[this.orders.length];
        for (int i = 0; i < this.orders.length; i++) {
            params[this.orders[i]] = m.group(i + 1);
        }
        return params;
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if ((obj instanceof UrlMatcher)) {
            return ((UrlMatcher) obj).url.equals(this.url);
        }
        return false;
    }

    public int hashCode() {
        return this.url.hashCode();
    }

}
