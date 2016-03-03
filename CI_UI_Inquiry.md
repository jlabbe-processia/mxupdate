


---


# Introduction #
An inquiry is used to produce a list of objects which e.g. could be used to
select objects which are shown in a web table. For a deep instruction see the
"MQL Guide" or "Business Modeler Guide" of the "ENOVIAvStudio Modeling
Platform".

Configuration item update files for inquiries are separated into two different
pieces. The first piece handles the update itself of an inquiry, the second
piece is the inquiry code.


---


# Handled Inquiry Properties #
This inquiry properties could be handled from MxUpdate:
  * description
  * hidden flag
  * format and pattern
  * inquiry code
  * arguments


---


# Separator between TCL Update Code and Inquiry Code #
The inquiry code and the TCL update code are defined in the same configuration
item file. Before the separator a comment is defined as information:
```
# do not change the next three lines, they are needed as separator information:
```
As separator following text is used:
```
################################################################################
# INQUIRY CODE                                                                 #
################################################################################
```
Below the separator the original code from the inquiry must be defined in the
same syntax as described in the MX manuals.


---


# Steps of the Update Flow #

## Cleanup ##
Following steps are done before the TCL update file is executed:
  * The description is set to zero length string.
  * The inquiry is set to not hidden.
  * The pattern and format is reset.
  * The inquiry code is removed.
  * All arguments are removed.

## Update ##
The inquiry code defined in the configuration item file is extracted and
written in a temporary file. The path of the file is defined as TCL variable
`FILE`. When the TCL update code is executed the inquiry code is updated to
the value defined in the file.


---


# Parameter Definitions #
| **Name:** `UIInquirySeparatorComment`        <p><b>Default Value:</b> <i>see the inquiry code separator</i> </p><p>Defines the used comment in front of the separator between the inquiry TCL update code and the inquiry code itself.</p> |
|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Name:** `UIInquirySeparatorText`           <p><b>Default Value:</b> <i>see the inquiry code separator</i> </p><p>Defines the used text for the separator between the inquiry TCL update code and the inquiry code itself.</p>            |

The parameters could be changed depending on project needs. For further
information see the [Parameter Definition Format](UpdatePropertyFileFormat_ParameterDef.md).


---


# Example #

```
################################################################################
# INQUIRY:
# ~~~~~~~~
# MxUpdate_Test
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# inquiry_MxUpdate_Test
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# Inquiry for test purposes.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod inquiry "${NAME}" \
    description "Inquiry for test purposes." \
    pattern "\$\{OID\}" \
    format "\$\{OID\}" \
    file [file join "${FILE}"] \
    add argument "ID" "objectId"

# do not change the next three lines, they are needed as separator information:
################################################################################
# INQUIRY CODE                                                                 #
################################################################################

print bus ${ID} select id dump "
"
```