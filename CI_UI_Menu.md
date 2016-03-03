


---


# Introduction #
Menus are used to define group buttons for the web user interface. In some
cases a menu could also be a button with action. For a deep instruction see the
"MQL Guide" or "Business Modeler Guide" of the "ENOVIAvStudio Modeling
Platform".


---


# Handled Menu Properties #
This menu properties could be handled from MxUpdate:
  * description
  * hidden flag
  * "alt" and label text
  * hyperlink reference (HRef)
  * settings
  * sub commands and menus
  * properties


---


# Steps of the Update Flow #
## Cleanup ##
Following steps are done before the TCL update file is executed:
  * The description is set to an empty string.
  * The menu is set to not hidden.
  * The "alt" and label text is set to an empty string, too.
  * The hyperlink reference is removed.
  * All settings are removed.
  * All properties are removed.
  * All sub commands and menus are removed.

## Update ##
The TCL update file is executed.


---


# Parameter Definitions #
No further parameters are defined.


---


# Example #
```
################################################################################
# MENU:
# ~~~~~
# MxUpdateToolbarMenu
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# menu_MxUpdateToolbarMenu
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# Menu for test purposes.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod menu "${NAME}" \
    description "Menu for test purposes." \
    label "emxFramework.Common.Actions" \
    href "" \
    alt "" \
    add setting "Registered Suite" "Framework" \
    add command "MxUpdate_Test"
```