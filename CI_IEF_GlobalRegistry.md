


---


# Introduction #

In the IEF Global Registry Objects each installed integrations is registered.
The integration source itself is done with XML tags. Maximum one object exists
(if at minimum one integration is used). The object is identified with name
"IEF-GlobalRegistry" and revision "-".


---


# Example (Snippet) #
```
################################################################################
# IEFGLOBALREGISTRY:
# ~~~~~~~~~~~~~~~~~~
# IEF-GlobalRegistry________-
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# -
# IEF Global Registry Object
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod bus "${OBJECTID}" \
    description "IEF Global Registry Object" \
    "IEF-RegistryData" "<?xml version='1.0'?><integrationregistry>....</integrationregistry>"
```