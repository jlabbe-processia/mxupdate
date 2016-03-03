



---


# Introduction #
A web table is used to display multiple business objects with related
information. For a deep instruction see the "MQL Guide" or "Business Modeler
Guide" of the "ENOVIAvStudio Modeling Platform".


---


# Handled Table Properties #
This table properties could be handled from MxUpdate:
  * description
  * hidden flag
  * fields


---


# Steps of the Update Flow #
## Cleanup ##
Following steps are done before the TCL update file is executed:
  * The description is set to an empty string.
  * The table is set to not hidden.
  * All columns of the web table are removed.

## Update ##
The TCL update file is executed.


---


# Parameter Definitions #
No further parameters are defined.


---


# Example #
```
################################################################################
# TABLE:
# ~~~~~~
# type_MxUpdate_Product
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# table_type_MxUpdate_Product
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# Table for test purposes.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod table "${NAME}" system \
    description "Table for test purposes." \
    column \
        name "type" \
        label "emxFramework.Basic.Type" \
        businessobject "\$<type>" \
        range "" \
        href "" \
        alt "" \
        setting "Registered Suite" "Framework" \
        setting "Show Type Icon" "true" \
    column \
        name "name" \
        label "emxFramework.Basic.Name" \
        businessobject "\$<name>" \
        range "" \
        href "\$\{COMMON_DIR\}/emxTree.jsp?mode=insert" \
        alt "" \
        setting "Registered Suite" "Framework" \
        setting "Target Location" "content" \
    column \
        name "revision" \
        label "emxFramework.Basic.Revision" \
        businessobject "\$<revision>" \
        range "" \
        href "" \
        alt "" \
        setting "Registered Suite" "Framework" \
    column \
        name "description" \
        label "emxFramework.Basic.Description" \
        businessobject "\$<description>" \
        range "" \
        href "" \
        alt "" \
        setting "Registered Suite" "Framework"
```