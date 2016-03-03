


---


# Introduction #
For the development of MxUpdate [Maven](http://maven.apache.org) is used. For
the specific configuration see the related developer description for the
[MxUpdate Update Tool](Development_Update.md), the
[MxUpdate Eclipse Plug-In](Development_EclipsePlugIn.md) and the
[MxUpdate Manual](Development_Manual.md).


---


# Maven Environment Specific Settings #
Depending on the environment some specific settings could be defined. For maven
the settings are located in the file `~/.m2/settings.xml` (home
directory of current user and then sub directory `.m2`).

## Link to the MX Environment ##
The used link to the MX environment is defined via following properties in the
settings:
| **Key**                         | **Description** |
|:--------------------------------|:----------------|
| org.mxupdate.mx.url             | URL to the MX database, if not defined the MX shared library is used. |
| org.mxupdate.mx.user            | User for the login. |
| org.mxupdate.mx.password        | Password for the login. |
| org.mxupdate.mx.jar.ematrix     | File path for the `eMatrixServletRMI.jar` file. |
| org.mxupdate.mx.jar.m1jsystem   | File path for the `m1jsystem.jar` file. |

**Example:**
The MX Jar libraries are located in the Eclipse `mxupdate` project
(directory). The same Jar files are used for the Eclipse Plug-In and Update
Tool.
```
<settings>
    :
  <profiles>
       :
    <profile>
      <id>MxUpdate Configuration</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <mxversion>V6-R2010x</mxversion>
        <org.mxupdate.mx.url>http://172.16.62.128:8080/enovia</org.mxupdate.mx.url>
        <org.mxupdate.mx.user>creator</org.mxupdate.mx.user>
        <org.mxupdate.mx.password></org.mxupdate.mx.password>
        <org.mxupdate.mx.jar.ematrix>${basedir}/../mxupdate/lib/eMatrixServletRMI.${mxversion}.jar</org.mxupdate.mx.jar.ematrix>
        <org.mxupdate.mx.jar.m1jsystem>${basedir}/../mxupdate/lib/m1jsystem.${mxversion}.jar</org.mxupdate.mx.jar.m1jsystem>
      </properties>
    </profile>
       :
  </profiles>
    :
</settings>
```

## HTTP Proxy for Maven ##
Maven downloads via HTTP protocol required dependencies (e.g. the test-ng Java
libraries). In the case an HTTP proxy must be defined, the settings for
Maven must be changed.

Following snippet must be added to the setting file:
```
<settings>
    :
  <proxies>
    <proxy>
       <active>true</active>
       <protocol>http</protocol>
       <host>proxy.somewhere.com</host>
       <port>8080</port>
         :
     </proxy>
  </proxies>
    :
</settings>
```
For more information see also the
[proxy guide](http://maven.apache.org/guides/mini/guide-proxies.html) from Maven.