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
import org.kohsuke.github.GHRepository;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.regex.Pattern;


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
     * Filter that excludes pull requests according to its PR title
     * if it contains [ci skip], [skip ci], or [wip], case insensitive.
     * The words "ci" and "skip" can be separated by either a dash, an underscore, or a single space.
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
                GHRepository repository = ((GitHubSCMSourceRequest) scmSourceRequest).getRepository();

                // name for pull requests are formatted as "PR-<number>"
                int prNumber = Integer.parseInt(scmHead.getName().substring(3));

                GHPullRequest pull = repository.getPullRequest(prNumber);
                String title = pull.getTitle().toLowerCase();

                Pattern p = Pattern.compile("\\[(wip|ci[ -_]skip|skip[ -_]ci)\\]", Pattern.CASE_INSENSITIVE);
                return p.matcher(title).find();
            }

            return false;
        }
    }
}
