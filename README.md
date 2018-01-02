# GitHub Skip PR by Title Plugin

Jenkins plugin for ignoring GitHub pull requests based on PR title.

## Getting Started

### Prerequisites

> See [Jenkins Plugin Tutorial][plugin-tutorial] for more information

1. Install [Maven 3](https://maven.apache.org) and JDK 8.0 or later.
1. Add the following to your `~/.m2/settings.xml` (Windows users will find them
 in `%USERPROFILE%\.m2\settings.xml`):

```XML
<settings>
  <pluginGroups>
    <pluginGroup>org.jenkins-ci.tools</pluginGroup>
  </pluginGroups>

  <profiles>
    <!-- Give access to Jenkins plugins -->
    <profile>
      <id>jenkins</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <repositories>
        <repository>
          <id>repo.jenkins-ci.org</id>
          <url>https://repo.jenkins-ci.org/public/</url>
        </repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
          <id>repo.jenkins-ci.org</id>
          <url>https://repo.jenkins-ci.org/public/</url>
        </pluginRepository>
      </pluginRepositories>
    </profile>
  </profiles>
  <mirrors>
    <mirror>
      <id>repo.jenkins-ci.org</id>
      <url>https://repo.jenkins-ci.org/public/</url>
      <mirrorOf>m.g.o-public</mirrorOf>
    </mirror>
  </mirrors>
</settings>
```

### Building

To build the plugin, run `mvn package` while inside the repository directory on
the terminal. A `.hpi` file will be created inside the `target/` directory.

To install the plugin:
1. Navigate to the *Manage Jenkins* > *Manage Plugins* page in the web UI.
1. Click on the *Advanced* tab.
1. Choose the `.hpi` file under the *Upload Plugin* section.
1. *Upload* the plugin file
![][upload-plugin]

Once a plugin file has been uploaded, the Jenkins master must be manually
restarted in order for the changes to take effect.

See [Managing Plugins][manage-plugin] for more information.

### Usage

When configuring a GitHub Organization Folder or a Multibranch Pipeline item
with GitHub branch source, you may add the behavior called "Pull request title
filtering behaviour". This behavior does not have any parameter.

![][usage]

Once added, all scanned pull requests with either `[ci skip]`, `[skip ci]`, or
`[wip]` anywhere on the PR title (case insensitive) will be ignored. Separator
between the words "ci" and "skip" on the former two can either be
a dash, an underscore, or a single space.

This plugin works best with these settings for branch discovery:
* **Discover branches > Strategy**: Exclude branches that are also filed as PRs
* **Filter by name (with wildcards) > Include**: "master PR-*"

## Running the tests

Run `mvn test` while inside the repository directory on the terminal.

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Maven](https://maven.apache.org) - Dependency Management

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the
process for submitting pull requests to us.

## Versioning

We use [SemVer](https://semver.org/) for versioning. For the versions available,
see the [tags on this repository][tags].

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE) file
for details


<!-- Links -->
[plugin-tutorial]: https://wiki.jenkins.io/display/JENKINS/Plugin+tutorial#Plugintutorial-SettingUpEnvironment
[upload-plugin]: https://jenkins.io/doc/book/resources/managing/plugin-manager-upload.png
[manage-plugin]: https://jenkins.io/doc/book/managing/plugins/#advanced-installation
[tags]: https://github.com/anyTV/jenkins-github-skip-pr-by-title-plugin/tags
[usage]: https://cdn.pbrd.co/images/H11t3h1.png 
