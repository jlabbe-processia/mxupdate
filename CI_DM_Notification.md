


---


# Introduction #
The notification objects are used from the generic email utility to define the
subject, text and recipients of notifications (emails). The generic email
utility is described in the "Business Process Services Administrator's Guide"
(Live Collaboration Admin).

The notification object itself is internally stored in MX as business object
from type "Notification" with policy "Business Rule" in vault
"eService Administration".


---


# Example #
```
################################################################################
# NOTIFICATION:
# ~~~~~~~~~~~~~
# TestNotification________TestRevision
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# TestRevision
# Notification object for test purposes.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod bus "${OBJECTID}" \
    description "Notification object for test purposes." \
    "Attachments" "" \
    "Body HTML" "" \
    "Body Text" "Test Body" \
    "Dynamic Bcc List" "" \
    "Dynamic Cc List" "" \
    "Dynamic To List" "" \
    "Filter" "" \
    "From Agent" "\$\{USER\}" \
    "Preprocess JPO" "" \
    "Registered Suite" "MxUpdate" \
    "Reply To" "" \
    "Static Bcc List" "" \
    "Static Cc List" "" \
    "Static To List" "" \
    "Subject Text" "Text Subject" \
    "URL Suffix" ""
mql promote bus "${OBJECTID}"
```