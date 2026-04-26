/*
 */
package ru.ifmo.cs.bcomp;

/**
 * Version build numbers. Use the same props like a git.properties. Setup in 
 * parent project pom.xml in git-commit-id-plugin config.
 * @author serge
 */
public class Version {
    public static final String BRANCH = "${git.branch}";
    public static final String BUILD_VERSION = "${git.build.version}";
    public static final String BUILD_TIME = "${git.build.time}";
    public static final String COMMIT_ID_ABBREV = "${git.commit.id.abbrev}";
    public static final String COMMIT_ID_DESCRIBE = "${git.commit.id.describe}";
    public static final String COMMIT_ID_FULL = "${git.commit.id.full}";
    public static final String COMMIT_TIME = "${git.commit.time}";
    public static final String COMMIT_COUNT = "${git.total.commit.count}";
}
