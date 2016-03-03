


---


# Install Eclipse Project #
Prerequisites is that the Maven settings are already defined (as described in
[Development](Development.md)).

  * Use the Maven Eclipse Plug-In
  * In Eclipse create a new SVN project using the `trunc` directory.
  * Update Maven dependencies.

The Maven dependencies are updated and the `eMatrixServletRMI.jar` and
`m1jsystem.jar` are added (which should be checked).


---


# Maven Profiles #
Three different profiles exists which could be activated:
| **Name of the Profile** | **Description** |
|:------------------------|:----------------|
| `no-tests`              | No tests are executed. |
| `test-all`              | All tests are executed. |
| `release-profile`       | The assembly plug-in prepares the packages with the source code and the Javadoc pages. |


---


# Preparing Maven Site #
The site generated from maven are used for reporting purposes. The current
released version could found at http://update.mxupdate.org/.
The site generates reports for
  * [Find-Bug](http://findbugs.sourceforge.net/)
  * [PMD](http://pmd.sourceforge.net/)
  * [PMD's Copy/Paste Detector](http://pmd.sourceforge.net/cpd.html)
  * JavaDocs
  * Source Code
  * Surefire (Tests)

Because the plug-ins requires a lot of memory, the maximum memory for Java must
be defined as option:
```
export MAVEN_OPTS=-Xmx1024m
```
Because the tests runs a long time, the site could be created without Surefire report by calling
```
mvn site -P no-tests
```
If all tests should be executed, following command must be called
```
mvn site -P test-all
```


---


# Upload new Version to Google Code Downloads #
The [gcupload](http://code.google.com/p/gcupload-maven-plugin/) maven plug-in is
used to handle the upload of a new MxUpdate Update version to the the download
section at the MxUpdate Google Code page.

To use the plug-in the `release-profile` must be actived.

## Local configuration ##
To use this Maven goal following properties must be defined in the user specific
`setting.xml` file :
```
<settings>
    :
  <servers>
       :
    <server>
      <id>googlecode</id>
      <username>[YOUR_GOOGLE_ACCOUNT_NAME]</username>
      <password>[SVN_PASSWORD_FROM_PROFILE]</password>
    </server>
       :
  </servers>
    :
</settings>
```

## Local Test ##
To create the packages without upload following maven call must be done:
```
mvn install -P release-profile
```

## Upload to Google Code ##
To upload a new MxUpdate Update version, following maven call must be done:
```
mvn gcupload:gcupload -P release-profile
```
First the source code and the Javadoc are packed and compressed. Then the
packages are uploaded.