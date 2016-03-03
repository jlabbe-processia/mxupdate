


---


# Introduction #
Formats are used for files check-in on business objects. They are defined on
policies. For a deep instruction see the "MQL Guide" or "Business Modeler Guide"
of the "ENOVIAvStudio Modeling Platform".


---


# Handled Properties #
This format properties could be handled from MxUpdate:
  * description
  * hidden flag
  * version
  * file suffix
  * file type
  * mime type
  * view program
  * edit program
  * print program
  * properties


---


# Steps of the Update Flow #

## Cleanup ##
Following steps are done before the TCL update file is executed:
  * set to not hidden
  * reset description, version, suffix, mime type, file type
  * remove view / edit / print program
  * remove all assigned properties

## Update ##
The TCL update file is executed.


---


# Parameter Definitions #
No further parameters are defined.


---


# Example #
```
################################################################################
# FORMAT:
# ~~~~~~~
# TestFormat
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# format_TestFormat
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# Format for test purposes.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod format "${NAME}" \
    description "Format for test purposes." \
    !hidden \
    version "1" \
    suffix "txt" \
    type "text" \
    mime "text/plain" \
    view "View Program" \
    edit "Edit Program" \
    print "Print Program"
```