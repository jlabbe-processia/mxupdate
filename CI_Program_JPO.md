


---


# Introduction #
The source code of JPOs are handled as configuration item. The name of the configuration item file is the name as used within MX appended by suffix `_mxJPO.java`. If the JPO name includes points ('.'), this points are interpreted as package name and the name of the file is the text after the last point appended `_mxJPO.java`.

## Example without Package ##
JPO with name `emxPart` has the configuration item file name `emxPart_mxJPO.java`.

## Example with Package ##
JPO with name `org.mxupdate.Update` has the file name `Update_mxJPO.java` (in sub directory `org/mxupdate`).


---


# Package Handling of JPOs #
Package names of JPOs are not handled like normal Java code.

## Update / Insert JPOs ##
If a JPO is updated or inserted, the name of the package is set in the front of the JPO file name. The package itself in the JPO source code is removed.

## Extract JPOs ##
If the JPO is extracted, the MX name of the JPO is used to extract the package name and the file name of the JPO. The code for the package is automatically added to the JPO source code (in the beginning of the source code).


---


# "Standard" Property and Symbolic Name Handling #
All non "standard" properties are removed before the JPO is updated and the embedded TCL code is execute. At the end all the "standard" properties "version", "file date", "application" and "author" are updated (depending on the used parameters). The "standard" properties "installer" and "original name" are only set if not already defined.

The symbolic name of the JPO is the automatically calculated string `program_[PROGRAM_NAME]`. If the `[PROGRAM_NAME]` includes some spaces or slashes, they are removed.


---


# Embedded TCL Code #
Sometimes some further TCL update code is required, e.g. to define a program user, or some properties for the JPO. The MxUpdate Update deployment tool could handle this within the same JPO source code.

## Start and End Markers ##
To identify the TCL update code some markers must be defined in the JPO source code. For the beginning of TCL update code the marker
```
################################################################################
# START NEEDED MQL UPDATE FOR THIS JPO PROGRAM                                 #
################################################################################
```
is used. At the end, the marker
```
################################################################################
# END NEEDED MQL UPDATE FOR THIS JPO PROGRAM                                   #
################################################################################
```
must be defined.

Each line of the TCL update code and the markers starts with double slashes '`//`'.

## Example ##
The following TCL update code defines an extra property "Execute" with the value "true". The code could be added e.g. in the Java file header.
```
//################################################################################
//# START NEEDED MQL UPDATE FOR THIS JPO PROGRAM                                 #
//################################################################################
//
//mql mod program "${NAME}"  \
//    add property "Execute" value "true"
//
//################################################################################
//# END NEEDED MQL UPDATE FOR THIS JPO PROGRAM                                   #
//################################################################################
```


---


# Parameter Definitions #
| **Name:** `Compile`                          <p><b>Parameter:</b> <code>‑‑compile</code></p>             <p><b>Default Value:</b> <code>false</code> </p>           <p>Compiles all updated JPO programs.</p> |
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Name:** `JPOTclUpdateMarkEnd`                                                              <p><b>Default Value:</b> <i>see the end marker</i> </p>  <p>Defines the string to mark the end of embedded TCL update code within JPO source code.</p> |
| **Name:** `JPOTclUpdateMarkStart`                                                            <p><b>Default Value:</b> <i>see the start marker</i> </p><p>Defines the string to mark the start of embedded TCL update code within JPO source code.</p> |
| **Name:** `JPOTclUpdateNeeded`               <p><b>Parameter:</b> <code>‑‑noJPOTclUpdateNeeded</code></p><p><b>Default Value:</b> <code>true</code> </p>            <p>Embedded TCL update code within a JPO is not executed. Instead a warning is shown because of existing TCL update code.</p><p><b>Attention!</b> The default value is <code>true</code>, but if the parameter is defined the flag will be <code>false</code>!</p> |

The parameters could be changed depending on project needs. For further information see the [Parameter Definition Format](UpdatePropertyFileFormat_ParameterDef.md).