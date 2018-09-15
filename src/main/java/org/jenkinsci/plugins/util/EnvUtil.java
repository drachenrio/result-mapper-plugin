package org.jenkinsci.plugins.util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import hudson.EnvVars;

public class EnvUtil {

    static String START_CHARS = "${";
    static String END_CHARS = "}";

    public static void printEnvVars(EnvVars env, PrintStream ps, String envName) {
        boolean contains = false;
        if (envName != null && !envName.isEmpty()) {
            contains = true;
            ps.println(String.format("%s--- EnvVars contains '%s' ---", "\n", envName));
            envName = envName.toLowerCase();
        } else {
            ps.println(String.format("%s--- EnvVars ---", "\n"));
        }
        Iterator<String> it = env.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (contains) {
                if (key.toLowerCase().contains(envName)) {
                    ps.println(String.format("%s : %s", key, env.get(key)));
                }
            }
        }
    }

    public static class Token {
        String token;
        String replaceStr;
        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
        public String getReplaceStr() {
            return replaceStr;
        }
        public void setReplaceStr(String replaceStr) {
            this.replaceStr = replaceStr;
        }
    }

    public List<Token> findTokens(String command) {
        return findTokens(command, START_CHARS, END_CHARS);
    }

    public List<Token> findTokens(String command, String startChars, String endChars) {
        List<Token> list = new ArrayList<>();
        while (true) {
            int start = command.indexOf(startChars);
            if (start < 0) break;
            int end = command.indexOf(endChars);
            if (end < 0) break; // closing chars not matched
            String token = command.substring(start + startChars.length(), end);
            Token oToken = new Token();
            oToken.setToken(token);
            list.add(oToken);
            StringBuffer buf = new StringBuffer(command);
            buf.replace(start, end + endChars.length(), "");
            command = buf.toString();
        }
        return list;
    }

    public String replaceTokens(String command, List<Token> tokens) {
        int i = 0;
        while (true) {
            if (i >= tokens.size()) break;
            int start = command.indexOf(START_CHARS);
            if (start < 0) break;
            int end = command.indexOf(END_CHARS);
            if (end < 0) break; // closing chars not matched
            String token = command.substring(start + START_CHARS.length(), end);
            Token oToken = tokens.get(i);
            oToken.setToken(token);
            StringBuffer buf = new StringBuffer(command);
            buf.replace(start, end + END_CHARS.length(), oToken.getReplaceStr());
            command = buf.toString();
            i++;
        }
        return command;
    }
}
