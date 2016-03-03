


---


# Introduction #
A channels is a collection of commands. Multiple channels are used to define the
content of MX portals. For a deep instruction see the "MQL Guide" or "Business
Modeler Guide" of the "ENOVIAvStudio Modeling Platform".

The configuration item 'channel' of the MxUpdate Update tool does not handle
channels which are defined as user's workspace item.


---


# Handled Properties #
This channel properties could be handled from MxUpdate:
  * description
  * hidden flag
  * height
  * "alt" and label text
  * href
  * assigned commands


---


# Steps of the Update Flow #

## Cleanup ##
Following steps are done before the TCL update file is executed:
  * The description is set to an empty string.
  * The channel is set to not hidden.
  * The "alt" and label text is also set to an empty string.
  * The channel height is set to "0".
  * All assigned commands are removed.

## Update ##
The TCL update file is executed.


---


# Parameter Definitions #
No further parameters are defined.


---


# Example #
```
################################################################################
# CHANNEL:
# ~~~~~~~~
# TestChannel
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# channel_TestChannel
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# Channel for test purposes.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod channel "${NAME}" \
    description "Channel for test purposes." \
    label "Test Label" \
    height "100" \
    add setting "Registered Suite" "MxUpdateCentral" \
    place "First Test Command" after "" \
    place "Second Test Command" after ""
```