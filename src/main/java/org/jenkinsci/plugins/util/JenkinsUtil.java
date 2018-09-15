package org.jenkinsci.plugins.util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.Result;
import hudson.model.Cause.UpstreamCause;

public class JenkinsUtil {

    /**
     * Any Immediate Upstream Projects Result As given result argument
     * @param build
     * @param result Result of SUCCESS,UNSTABLE,FAILURE,NOT_BUILT,ABORTED
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean anyImmediateUpstreamProjectsResultAs(AbstractBuild build, Result result) {
        boolean ret = false;
        List<Cause> causes = build.getCauses();
        for (Cause cause : causes) {
            if (!(cause instanceof UpstreamCause)) {
                continue;
            }
            if (result == ((UpstreamCause)cause).getUpstreamRun().getResult()) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    /**
     * Return true if one of the the immediate causes failed 
     *
     * @param build
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    public boolean anyImmediateUpstreamProjectsFailed(AbstractBuild build) {
        return anyImmediateUpstreamProjectsResultAs(build, Result.FAILURE);
    }

    @SuppressWarnings({ "rawtypes" })
    public boolean anyImmediateUpstreamProjectsAborted(AbstractBuild build) {
        return anyImmediateUpstreamProjectsResultAs(build, Result.ABORTED);
    }

    @SuppressWarnings({ "rawtypes" })
    public boolean anyImmediateUpstreamProjectsNotBuilt(AbstractBuild build) {
        return anyImmediateUpstreamProjectsResultAs(build, Result.NOT_BUILT);
    }

    @SuppressWarnings({ "rawtypes" })
    public boolean anyImmediateUpstreamProjectsUnstable(AbstractBuild build) {
        return anyImmediateUpstreamProjectsResultAs(build, Result.UNSTABLE);
    }

    @SuppressWarnings({ "rawtypes" })
    public boolean anyImmediateUpstreamProjectsSuccess(AbstractBuild build) {
        return anyImmediateUpstreamProjectsResultAs(build, Result.SUCCESS);
    }

    /**
     * Return true if all of the the immediate causes successful 
     *
     * @param build
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean allImmediateUpstreamProjectsSuccess(AbstractBuild build) {
        boolean ret = true;
        List<UpstreamCause> causes = build.getCauses();
        for (UpstreamCause cause : causes) {
            ret &= (Result.SUCCESS == cause.getUpstreamRun().getResult());
        }
        return ret;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void collectUpstreamCausesIds(AbstractBuild build, StringBuffer sb) {
        List<Cause> causes = build.getCauses();
        for (Cause cause : causes) {
            List<Cause> list = new ArrayList<>();
            list.add(cause);
            collectUpstreamCausesIds(list, sb);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getUpstreamCausesList(AbstractBuild build, List<UpstreamCause> list) {
        List<Cause> causes = build.getCauses();
        for (Cause cause : causes) {
            List<Cause> causeList = new ArrayList<>();
            causeList.add(cause);
            getUpstreamCausesList(causeList, list);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void printUpstreamCauses(AbstractBuild build, PrintStream ps) {
        ps.println("--- UpstreamCausess ---");
        List<Cause> causes = build.getCauses();
        for (Cause cause : causes) {
            List<Cause> list = new ArrayList<>();
            list.add(cause);
            printUpstreamCauses(list, ps, 0);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public UpstreamCause getRoot(AbstractBuild build) {
        Stack<UpstreamCause> stack = new Stack<>(); 
        getRoot(build.getCauses(), stack);
        return stack.size() > 0 ? stack.pop() : null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void printUpstreamProjects(AbstractBuild build, PrintStream ps, String envName) {
        ps.println("--- UpstreamProjects ---");
        List<AbstractProject> projects = build.getProject().getUpstreamProjects();
        printUpstreamProjects(projects, ps, 0, envName);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void printDownstreamProjects(AbstractBuild build, PrintStream ps) {
        ps.println("--- DownstreamProjects ---");
        List<AbstractProject> projects = build.getProject().getDownstreamProjects();
        printDownstreamProjects(projects, ps, 0);
    }

    private void collectUpstreamCausesIds(List<Cause> causes, StringBuffer sb) {
        if (causes == null)
            return;
        for (Cause cause : causes) {
            if (!(cause instanceof UpstreamCause)) {
                continue;
            }
            UpstreamCause upCause = (UpstreamCause) cause;
            sb.append("::").append(upCause.getUpstreamProject());
            collectUpstreamCausesIds(upCause.getUpstreamCauses(), sb);
        }
    }

    private void getUpstreamCausesList(List<Cause> causes, List<UpstreamCause> list) {
        if (causes == null)
            return;
        for (Cause cause : causes) {
            if (!(cause instanceof UpstreamCause)) {
                continue;
            }
            UpstreamCause upCause = (UpstreamCause) cause;
            list.add(upCause);
            getUpstreamCausesList(upCause.getUpstreamCauses(), list);
        }
    }

    private void printUpstreamCauses(List<Cause> causes, PrintStream ps, int spaces) {
        if (causes == null)
            return;
        for (Cause cause : causes) {
            if (!(cause instanceof UpstreamCause)) {
                continue;
            }
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < spaces; i++) {
                sb.append(" ");
            }
            UpstreamCause upCause = (UpstreamCause) cause;
            ps.println(String.format("%s '%s', build: %d, Result: %s, url: '%s'", sb.toString(),
                    upCause.getUpstreamProject(), upCause.getUpstreamBuild(),
                    upCause.getUpstreamRun().getResult(),
                    upCause.getUpstreamUrl()));
            spaces += 2;
            printUpstreamCauses(upCause.getUpstreamCauses(), ps, spaces);
            spaces -= 2;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void printUpstreamProjects(List<AbstractProject> projects, PrintStream ps, int spaces, String envName) {
        if (projects == null)
            return;
        for (AbstractProject prj : projects) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < spaces; i++) {
                sb.append(" ");
            }
            ps.println(String.format("%s '%s', workspace: '%s'", sb.toString(),
                    prj.getFullDisplayName(), prj.getWorkspace().toString()));

            spaces += 2;
            printUpstreamProjects(prj.getUpstreamProjects(), ps, spaces, envName);
            spaces -= 2;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void printDownstreamProjects(List<AbstractProject> projects, PrintStream ps, int spaces) {
        if (projects == null)
            return;
        for (AbstractProject prj : projects) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < spaces; i++) {
                sb.append(" ");
            }
            ps.println(String.format("%s '%s', workspace: '%s'", sb.toString(),
                    prj.getFullDisplayName(), prj.getWorkspace().toString()));
            spaces += 2;
            printDownstreamProjects(prj.getDownstreamProjects(), ps, spaces);
            spaces -= 2;
        }
    }

    private void getRoot(List<Cause> causes, Stack<UpstreamCause> stack) {
        if (causes == null)
            return;
        for (Cause cause : causes) {
            if (!(cause instanceof UpstreamCause)) {
                continue;
            }
            UpstreamCause upCause = (UpstreamCause) cause;
            stack.push(upCause);
            getRoot(upCause.getUpstreamCauses(), stack);
            break; // find the root through the first dependency path
        }
    }
}
