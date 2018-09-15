package org.jenkinsci.plugins.util;

import hudson.model.Node;
import jenkins.model.Jenkins;

import java.io.PrintStream;
import java.util.List;

public class NodeUtil {

    public static void printNodeList(PrintStream ps) { 
        Jenkins jenkins = Jenkins.getInstance();
        List<Node> jenkinsNodes =jenkins.getNodes();
        for (Node node : jenkinsNodes) {
            ps.println(String.format("node: ", node.getDisplayName()));
        }
    }
}
