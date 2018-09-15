package org.jenkinsci.plugins.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import org.jenkinsci.plugins.util.EnvUtil.Token;

import hudson.EnvVars;

public class ShellUtil {

    public static void executeShellCommand(String shellCommand, EnvVars env, PrintStream ps) throws IOException, InterruptedException {
        ps.println(String.format("> execute command: '%s'", shellCommand));
        String command = replaceEnvVars(shellCommand, env, ps);
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(shellCommand);
        pr.waitFor();

        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = "";
        StringBuilder sb = new StringBuilder();
        while ((line=buf.readLine())!=null) {
            sb.append(line).append("\n");
        }
        int exitValue = pr.exitValue();
        ps.println(sb.toString());
        ps.println("> exit: " + exitValue);
    }

    private static String replaceEnvVars(String command, EnvVars env, PrintStream ps) {
        EnvUtil util = new EnvUtil();
        List<Token> tokens = util.findTokens(command);
        if (tokens.isEmpty()) return command;
        ps.println("command before replacement: " + command);
        for (Token token : tokens) {
            String envVaule = env.get(token.getToken());
            token.setReplaceStr(envVaule != null ? envVaule : "");
        }
        String resultCommand = util.replaceTokens(command, tokens);
        ps.println("command after replacement: " + resultCommand);
        return resultCommand;
    }
}
