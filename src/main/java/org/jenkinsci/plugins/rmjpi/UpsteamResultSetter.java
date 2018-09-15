package org.jenkinsci.plugins.rmjpi;

import java.io.IOException;
import java.io.PrintStream;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.ListBoxModel;

import org.jenkinsci.plugins.util.JenkinsUtil;
import org.kohsuke.stapler.DataBoundConstructor;

public class UpsteamResultSetter extends Builder {

    static String SUCCESS = "SUCCESS";
    static String UNSTABLE = "UNSTABLE";
    static String FAILURE = "FAILURE";
    static String NOT_BUILT = "NOT_BUILT";
    static String ABORTED = "ABORTED";

    private boolean success;
    private boolean unstable;
    private boolean failure;
    private boolean notBuilt;
    private boolean aborted;
    private String result;
    private boolean skipFollowingBuildSteps;

    @DataBoundConstructor
    public UpsteamResultSetter(String result, boolean success, boolean unstable, boolean failure,
            boolean notBuilt, boolean aborted, boolean skipFollowingBuildSteps) {
        this.success = success;
        this.unstable = unstable;
        this.failure = failure;
        this.notBuilt = notBuilt;
        this.aborted = aborted;
        this.result = result;
        this.skipFollowingBuildSteps = skipFollowingBuildSteps;
    }

    public boolean getSuccess() {
        return success;
    }

    public boolean getUnstable() {
        return unstable;
    }

    public boolean getFailure() {
        return failure;
    }

    public boolean getNotBuilt() {
        return notBuilt;
    }

    public boolean getAborted() {
        return aborted;
    }

    public String getResult() {
        return result;
    }

    public boolean getSkipFollowingBuildSteps() {
        return skipFollowingBuildSteps;
    }

    @Override
    public boolean perform(AbstractBuild<?,?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        PrintStream ps = listener.getLogger();

        // collect upstream project result
        JenkinsUtil jUtil = new JenkinsUtil();
        UpstreamProjectResult upResult = new UpstreamProjectResult();
        upResult.setSuccess(jUtil.anyImmediateUpstreamProjectsSuccess(build));
        upResult.setUnstable(jUtil.anyImmediateUpstreamProjectsUnstable(build));
        upResult.setFailure(jUtil.anyImmediateUpstreamProjectsFailed(build));
        upResult.setNotBuilt(jUtil.anyImmediateUpstreamProjectsNotBuilt(build));
        upResult.setAborted(jUtil.anyImmediateUpstreamProjectsAborted(build));

        if (getSuccess() && upResult.isSuccess() || getUnstable() && upResult.isUnstable() ||
            getFailure() && upResult.isFailure() || getNotBuilt() && upResult.isNotBuilt() ||
            getAborted() && upResult.isAborted()) {
            // converted to selected result
            if (SUCCESS.endsWith(result)) {
               build.setResult(Result.SUCCESS);
            } else if (UNSTABLE.endsWith(result)) {
               build.setResult(Result.UNSTABLE);
            } else if (FAILURE.endsWith(result)) {
               build.setResult(Result.FAILURE);
            } else if (NOT_BUILT.endsWith(result)) {
               build.setResult(Result.NOT_BUILT);
            } else if (ABORTED.endsWith(result)) {
               build.setResult(Result.ABORTED);
            }
            ps.println(String.format("UpsteamResultSetter returns '%b'", skipFollowingBuildSteps ? false : true));
            return skipFollowingBuildSteps ? false : true;
        }

        return true;
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return aClass == FreeStyleProject.class;
        }

        @Override
        public String getDisplayName() {
            return Messages.UpstreamResultSetter_DescriptorImpl_DisplayName();
        }

        public ListBoxModel doFillResultItems() {
            ListBoxModel items = new ListBoxModel();
            items.add("Select one", "");
            items.add(SUCCESS, SUCCESS);
            items.add(UNSTABLE, UNSTABLE);
            items.add(FAILURE, FAILURE);
            items.add(NOT_BUILT, NOT_BUILT);
            items.add(ABORTED, ABORTED);
            return items;
        }
    }

    public static class UpstreamProjectResult {
        private boolean success;
        private boolean unstable;
        private boolean failure;
        private boolean notBuilt;
        private boolean aborted;

        public boolean isSuccess() {
            return success;
        }
        public void setSuccess(boolean success) {
            this.success = success;
        }
        public boolean isUnstable() {
            return unstable;
        }
        public void setUnstable(boolean unstable) {
            this.unstable = unstable;
        }
        public boolean isFailure() {
            return failure;
        }
        public void setFailure(boolean failure) {
            this.failure = failure;
        }
        public boolean isNotBuilt() {
            return notBuilt;
        }
        public void setNotBuilt(boolean notBuilt) {
            this.notBuilt = notBuilt;
        }
        public boolean isAborted() {
            return aborted;
        }
        public void setAborted(boolean aborted) {
            this.aborted = aborted;
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("success=").append(success).append(", unstable=").append(unstable)
            .append(", failure=").append(failure).append(", notBuilt=").append(notBuilt)
            .append(", aborted=").append(aborted);
            return sb.toString();
        }
    }
}
