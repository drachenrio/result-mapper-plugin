package org.jenkinsci.plugins.util;

import java.util.ArrayList;
import java.util.List;

import hudson.model.AbstractBuild;
import hudson.model.Run;

@SuppressWarnings("rawtypes")
public class BuildUtil {

    public static String info(AbstractBuild build) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("build.getProject.getFullName: '%s'\n", build.getProject().getFullName()));
        sb.append(String.format("build.getDisplayName: '%s', build.getNumber: '%d'\n", build.getDisplayName(), build.getNumber()));
        sb.append(String.format("build.getBuildStatusSummary: '%s'\n", build.getBuildStatusSummary().message));
        sb.append(String.format("build.getWorkspace.isRemote: '%b'\n", build.getWorkspace().isRemote()));
        sb.append(String.format("build.getWorkspace: '%s'\n", build.getWorkspace().toString()));

        return sb.toString();

    }

    public static List<Run> getPreviousFailedBuilds(AbstractBuild build) {
        Run currRun = build.getPreviousFailedBuild();
        List<Run> list = new ArrayList<>();
        while (currRun != null) {
            list.add(currRun);
            currRun = currRun.getPreviousFailedBuild();
        }
        return list;
    }

	public static Run getPreviousFailedBuild(AbstractBuild build) {
        return build.getPreviousFailedBuild();
    }

    public static Run getLastFailedBuild(AbstractBuild build) {
        return build.getProject().getLastFailedBuild();
    }

    public static Run getLastSuccessfulBuild(AbstractBuild build) {
        return build.getProject().getLastSuccessfulBuild();
    }

    public static Run getLastStableBuild(AbstractBuild build) {
        return build.getProject().getLastStableBuild();
    }
}
