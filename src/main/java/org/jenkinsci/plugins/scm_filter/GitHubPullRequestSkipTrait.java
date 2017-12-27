package org.jenkinsci.plugins.scm_filter;

import hudson.Extension;
import hudson.plugins.git.GitSCM;
import hudson.scm.SCMDescriptor;

import jenkins.scm.api.SCMHead;
import jenkins.scm.api.trait.*;
import jenkins.scm.api.trait.SCMBuilder;
import jenkins.scm.api.trait.SCMSourceContext;
import jenkins.scm.api.trait.SCMSourceRequest;

import org.jenkinsci.plugins.github_branch_source.GitHubSCMBuilder;
import org.jenkinsci.plugins.github_branch_source.GitHubSCMSourceRequest;
import org.jenkinsci.plugins.github_branch_source.PullRequestSCMHead;

import org.kohsuke.github.GHPullRequest;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Iterator;


public class GitHubPullRequestSkipTrait extends SCMSourceTrait {


    @DataBoundConstructor
    public GitHubPullRequestSkipTrait () {
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void decorateContext (SCMSourceContext<?, ?> context) {
        context.withFilter(new GitHubPullRequestSkipTrait.ExcludePRsSCMHeadFilter());
    }


    /**
     * Our descriptor.
     */
    @Extension
    @SuppressWarnings("unused") // instantiated by Jenkins
    public static class PullRequestSkipTraitDescriptorImpl extends SCMSourceTraitDescriptor {


        /**
         * {@inheritDoc}
         */
        @Override
        @Nonnull
        public String getDisplayName () {
            return "Pull request title filtering behaviour";
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isApplicableToSCM (@Nonnull SCMDescriptor<?> scm) {
            return scm instanceof GitSCM.DescriptorImpl;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isApplicableToBuilder (@Nonnull Class<? extends SCMBuilder> builderClass) {
            return GitHubSCMBuilder.class.isAssignableFrom(builderClass);
        }
    }


    /**
     * Filter that excludes pull requests according to its PR title (if it contains [ci skip] or [skip ci], case insensitive).
     */
    public static class ExcludePRsSCMHeadFilter extends SCMHeadFilter {


        ExcludePRsSCMHeadFilter () {
        }


        @Override
        public boolean isExcluded (
                @Nonnull SCMSourceRequest scmSourceRequest,
                @Nonnull SCMHead scmHead
        ) throws IOException, InterruptedException {

            if (scmHead instanceof PullRequestSCMHead) {
                Iterable<GHPullRequest> pulls = ((GitHubSCMSourceRequest) scmSourceRequest).getPullRequests();
                Iterator<GHPullRequest> pullIterator = pulls.iterator();
                String prName = scmHead.getName();

                while (pullIterator.hasNext()) {
                    GHPullRequest pull = pullIterator.next();

                    if (prName.equals("PR-" + pull.getNumber())) {
                        String title = pull.getTitle().toLowerCase();
                        return title.contains("[ci skip]") || title.contains("[skip ci]");
                    }
                }
            }

            return false;
        }
    }
}
