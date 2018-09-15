package org.jenkinsci.plugins.util;

import hudson.model.Run;

public class RunUtil {

    public static String info(Run run) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("run.getBuildStatusSummary: '%s'\n", run.getBuildStatusSummary().message));
        sb.append(String.format("run.getParent.getFullName: '%s'\n", run.getParent().getFullName()));
        sb.append(String.format("run.getDisplayName: '%s', run.getNumber: '%d'\n", run.getDisplayName(), run.getNumber()));
        sb.append(String.format("run.getParent.getBuildDir: '%s'\n", run.getParent().getBuildDir().toString()));

        sb.append(String.format("run.getHasArtifacts: '%s'\n", run.getHasArtifacts()));
        return sb.toString();

    }
}
