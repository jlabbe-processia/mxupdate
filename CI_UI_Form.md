



---


# Introduction #
A form is used to display information related to an object. For a deep
instruction see the "MQL Guide" or "Business Modeler Guide" of the
"ENOVIAvStudio Modeling Platform".


---


# Handled Form Properties #
This form properties could be handled from MxUpdate:
  * description
  * hidden flag
  * fields


---


# Steps of the Update Flow #
## Cleanup ##
Following steps are done before the TCL update file is executed:
  * The description is set to an empty string.
  * The form is set to not hidden.
  * All fields of the web form are removed.

## Update ##
The description is updated and all fields are new created and appended.

## Correct Order of Fiels ##
Because there is an issue in MX of correct order for new created and appended
fields (after all fields was removed), MxUpdate specific TCL procedure
`orderFields` must be called. This TCL procedure orders all fields
correctly (by reordering them via MQL statements).


---


# Parameter Definitions #
No further parameters are defined.


---


# Example #

```
################################################################################
# FORM:
# ~~~~~
# type_MxUpdate_Product
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# form_type_MxUpdate_Product
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# Form for test purposes.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod form "${NAME}" \
    description "Form for test purposes." \
    field \
        name "type" \
        label "emxFramework.Basic.Type" \
        businessobject "\$<type>" \
        range "" \
        href "" \
        alt "" \
        setting "Admin Type" "Type" \
        setting "Editable" "false" \
        setting "Field Type" "basic" \
        setting "Registered Suite" "Framework" \
        setting "Show Type Icon" "true" \
    field \
        name "name" \
        label "emxFramework.Basic.Name" \
        businessobject "\$<name>" \
        range "" \
        href "" \
        alt "" \
        setting "Admin Type" "Name" \
        setting "Editable" "true" \
        setting "Field Type" "basic" \
        setting "Input Type" "textbox" \
        setting "Registered Suite" "Framework" \
        setting "Required" "true" \
    field \
        name "revision" \
        label "emxFramework.Basic.Revision" \
        businessobject "\$<revision>" \
        range "" \
        href "" \
        alt "" \
        setting "Admin Type" "Revision" \
        setting "Editable" "true" \
        setting "Field Type" "basic" \
        setting "Input Type" "textbox" \
        setting "Registered Suite" "Framework" \
        setting "Required" "true"

orderFields "${NAME}" [list \
    "type" \
    "name" \
    "revision" \
]
```