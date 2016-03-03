


---


# Introduction #
Commands are used to define buttons with an action for the web user interface.
For a deep instruction see the "MQL Guide" or "Business Modeler Guide"
of the "ENOVIAvStudio Modeling Platform".


---


# Handled Command Properties #
This command properties could be handled from MxUpdate:
  * description
  * hidden flag
  * "alt" and label text
  * hyperlink reference (HRef)
  * user references (assigned users)
  * settings
  * properties


---


# Steps of the Update Flow #
## Cleanup ##
Following steps are done before the TCL update file is executed:
  * The description is set to an empty string.
  * The command is set to not hidden.
  * The "alt" and label text is set to an empty string, too.
  * The hyperlink reference is removed.
  * All assigned users are removed.
  * All settings are removed.

## Update ##
The TCL update file is executed.


---


# Parameter Definitions #
No further parameters are defined.


---


# Example #

```
################################################################################
# COMMAND:
# ~~~~~~~~
# TestCommand
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# command_TestCommand
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# Command for test purposes.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod command "${NAME}" \
    description "Command for test purposes." \
    label "Test Label" \
    href "\$\{COMMON_DIR\}/emxTable.jsp?table=TestTable&inquiry=TestInquiry" \
    alt "" \
    add setting "Target Location" "content"
```