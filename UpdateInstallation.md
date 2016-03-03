


---


# Installation #
[Download](http://code.google.com/p/mxupdate/downloads/list) and uncompress the MxUpdate Update deployment tool. E.g. the tool is unpacked into directory `C:\project\MxUpdate\`.

Start MQL.

Define the MQL environment variable **`MXUPDATE_PATH`** to the path where the **`MxInstall.mql`** file is located. E.g.
```
set env MXUPDATE_PATH C:\project\MxUpdate\
```
If some additional properties must be defined, the MQL environment variable **`MXUPDATE_MAPPING_FILE`** could be defined. The value is the path of the file which defines the additional properties. E.g.
```
set env MXUPDATE_MAPPING_FILE C:\project\MyProjectSettings.properties
```
Run MQL script **`MxInstall.mql`**.
```
run C:\project\MxUpdate\MxInstall.mql
```
Then the MxUpdate Update deployment tool is installed and all MxUpdate JPOs are compiled. The tool could be tested:
```
exec prog MxUpdate --help
```
The short help for all parameters of MxUpdate is shown.


---


# Environment Variables #
Following environment variables could be defined and are supported from
`MxInstall.mql`:
| **Name**                      | **Description** |
|:------------------------------|:----------------|
| `MXUPDATE_PATH`               | Defines the root path of the MxUpdate installation folder where the `MxInstall.mql` is located. If MxUpdate installation is defined as SVN external link, the path must be defined to the directory `src/main`. |
| `MXUPDATE_MAPPING_FILE`       | Defines additional properties which overwrites the original defined properties of MxUpdate. |
| `MXUPDATE_DEVELOPMENT`        | If the value is set to `true`, the new installed JPOs are not recompiled. Instead only a "standard" compile is done. |

The environment variables could be defined as MQL or as shell variables and are
evaluated in this order:
  * as MQL environment variable
  * as global MQL environment variable
  * or as shell variable


---


# Parameter Definitions #
An update of already installed JPOs used from the MxUpdate Update tool is only
done if the last modified date of the file differs from the stored last modified
date in MX. Because TCL could not handle milliseconds for the last modified date
of files, the date format differs from that used from MxUpdate for the update
of configuration items. The date within the installation must be formatted in
TCL and Java. For the same date the result must be equal in TCL and Java.

For an explanation how to change this properties see also the
[property file format](UpdatePropertyFileFormat.md) description.

| **Name**                       | **Default Value**              | **Description** |
|:-------------------------------|:-------------------------------|:----------------|
| InstallFileDateFormatJava      | **`yyyy-MM-dd HH:mm:ss`**      | Date / time format used within Java / JPOs to install the MxUpdate Update tool. |
| InstallFileDateFormatTCL       | **`%Y-%m-%d %H:%M:%S`**        | Date / time format used within TCL (MQL) to install the MxUpdate Update tool. |