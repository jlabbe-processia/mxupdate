


---


# Introduction #
Attributes are used to handle values for business objects and connections. They
are assigned to [types](CI_DM_Type.md), [relationships](CI_DM_Relationship.md) and
[interfaces](CI_DM_Interface.md).


---


# Handled Properties #
This format properties could be handled from MxUpdate:
  * description
  * hidden flag
  * rules
  * default value
  * maximum length (only for string attributes)
  * multiline flag (only for string attributes)
  * flag to define that the value is reseted on clone of business object
  * flag to define that the value is reseted on revision of business object
  * triggers
  * ranges


---


# Steps of the Update Flow #

## Cleanup ##
Following steps are done before the CI update file is executed:
  * set to not hidden
  * reset description and default value
  * reset on clone flag is disabled
  * reset on revision flag is disabled
  * remove all ranges
  * multiple line flag is disabled for string attributes
  * maximum length is set to 0 for string attributes

## Update ##
The CI update file is executed.


---


# Parameter Definitions #
| **Name:** `DMAttrSupportsFlagResetOnClone`    <p><b>Value:</b> <code>true</code> if the MQL command "<code>help attribute</code>" includes the string "<code>resetonclone</code>".</p>   <p>If the flag is set to true attributes supports the "resetonclone" flag.</p> |
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Name:** `DMAttrSupportsFlagResetOnRevision` <p><b>Value:</b> <code>true</code> if the MQL command "<code>help attribute</code>" includes the string "<code>resetonrevision</code>".</p><p>If the flag is set to true attributes supports the "resetonrevision" flag.</p> |
| **Name:** `DMAttrSupportsPropMaxLength`       <p><b>Value:</b> <code>true</code> if the MQL command "<code>help attribute</code>" includes the string "<code>maxlength</code>".</p>      <p>If the flag is set to true string attributes supports the "maxlength" property.</p> |


---


# Example #
```
################################################################################
# ATTRIBUTE:
# ~~~~~~~~~~
# MxUpdateTestAttribute
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# attribute_MxUpdateTestAttribute
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# MxUpdate Test String Attribute.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod attribute "${NAME}" \
    description "MxUpdate Test String Attribute." \
    !hidden \
    !resetonclone \
    !resetonrevision \
    multiline \
    maxlength "100" \
    default ""
```