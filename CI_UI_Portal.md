


---


# Introduction #
A portal is a collection of channels. A portal is used as power view in the
user interface. For a deep instruction see the "MQL Guide" or "Business Modeler
Guide" of the "ENOVIAvStudio Modeling Platform".

The configuration item 'portal' of the MxUpdate Update tool does not handle
portals which are defined as user's workspace item.


---


# Handled Properties #
This portal properties could be handled from MxUpdate:
  * description
  * hidden flag
  * "alt" and label text
  * href
  * settings
  * assigned channels


---


# Steps of the Update Flow #

## Cleanup ##
Following steps are done before the TCL update file is executed:
  * The description is set to an empty string.
  * The portal is set to not hidden.
  * The label, href and alt are set to an empty string.
  * All settings are removed.
  * All properties are removed.
  * All assigned channels are removed.

## Update ##
The TCL update file is executed.


---


# Parameter Definitions #
No further parameters are defined.


---


# Example #
```
################################################################################
# PORTAL:
# ~~~~~~~
# TestPortal
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# portal_TestPortal
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# Portal for test purposes.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod portal "${NAME}" \
    description "Portal for test purposes." \
    label "" \
    add setting "Registered Suite" "MxUpdateCentral" \
    place "TestChannel" after ""
```