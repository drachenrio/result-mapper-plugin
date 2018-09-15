package org.jenkinsci.plugins.util;

import java.io.File;

import hudson.model.AbstractProject;

public class ProjectUtil {

	public static String info(AbstractProject prj) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("prj.getProject.getFullName: '%s'\n", prj.getFullName()));
        sb.append(String.format("prj.getDisplayName: '%s'\n", prj.getDisplayName()));
        sb.append(String.format("prj.getWorkspace.isRemote: '%b'\n", prj.getWorkspace().isRemote()));
        File buildDir = prj.getBuildDir();
        if (buildDir != null) {
            sb.append(String.format("prj.getBuildDir.getPath: '%s'\n", prj.getBuildDir().getPath()));
        }
        return sb.toString();

    }
}
