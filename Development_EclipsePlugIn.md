


---


# Install Eclipse Project #
Prerequisites is that the Maven settings are already defined (as described in
[Development](Development.md)).

  * In Eclipse create a new SVN project using the `trunc` directory.

The Maven dependencies are updated and the `eMatrixServletRMI.jar` and
`FcsClient.jar` are added (which should be checked).


---


# Result of an Maven Build #
After the install of a maven build is done, two jar files are located in the
target directory. The first one in the format
```
    org.mxupdate.eclipse_[VERSION].jar
```
is the Eclipse Plug-In itself, the second one in the format
```
    org.mxupdate.eclipse_[VERSION]-feature.jar
```
holds the `feature.xml` file needed for the site.


---


# Maven Profiles #
Three different profiles exists which could be activated:
| **Name of the Profile** | **Description** |
|:------------------------|:----------------|
| `Windows`               | SWT for Windows is used. |
| `MacOSx`                | SWT for Mac OSX is used (default). |
| `release-profile`       | The assembly plug-in prepares the packages with the compiled plug-in and the Javadoc pages. |


---


# Upload new Version to Google Code Downloads #

## Local Test ##
To create the packages without upload following maven call must be done for
Mac OSX:
```
mvn install -P release-profile,MacOSx
```
For Windows:
```
mvn install -P release-profile,Windows
```

## Upload to Google Code ##
To upload a new MxUpdate Update version, following maven call must be done for
Mac OSX:
```
mvn gcupload:gcupload -P release-profile,MacOSx
```
For Windows:
```
mvn gcupload:gcupload -P release-profile,Windows
```
First the compiled plug-in and the Javadoc are packed and compressed. Then the
packages are uploaded.