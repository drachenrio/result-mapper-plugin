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

import org.kohsuke.stapler.DataBoundConstructor;

public class BuildResultSetter extends Builder {

   static String SUCCESS = "SUCCESS";
   static String UNSTABLE = "UNSTABLE";
   static String FAILURE = "FAILURE";
   static String NOT_BUILT = "NOT_BUILT";
   static String ABORTED = "ABORTED";

   private String result;
   private boolean skipFollowingBuildSteps;
   
    @DataBoundConstructor
    public BuildResultSetter(String result, boolean skipFollowingBuildSteps) {
        this.result = result;
        this.skipFollowingBuildSteps = skipFollowingBuildSteps;
    }

    public String getResult() {
        return result;
    }

    public boolean getSkipFollowingBuildSteps() {
        return skipFollowingBuildSteps;
    }

    public void setSkipFollowingBuildSteps(boolean skipFollowingBuildSteps) {
        this.skipFollowingBuildSteps = skipFollowingBuildSteps;
    }

    @Override
    public boolean perform(AbstractBuild<?,?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        PrintStream ps = listener.getLogger();

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

        ps.println(String.format("BuildResultSetter returns '%b'", skipFollowingBuildSteps ? false : true));
        return skipFollowingBuildSteps ? false : true;
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @SuppressWarnings("rawtypes")
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return aClass == FreeStyleProject.class;
        }

        @Override
        public String getDisplayName() {
            return Messages.BuildResultSetter_DescriptorImpl_DisplayName();
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
}
