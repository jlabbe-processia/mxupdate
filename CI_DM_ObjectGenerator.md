


---


# Introduction #
An object generator is used to create new business object instances depending on
a number generator.


---


# Example #
```
################################################################################
# OBJECTGENERATOR:
# ~~~~~~~~~~~~~~~~
# type_MxUpdateTestType
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# Object Generator for MxUpdate Test Type Objects to define the prefix.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql mod bus "${OBJECTID}" \
    description "Object Generator for MxUpdate Test Type Objects to define the prefix." \
    "eService Name Prefix" "TEST-" \
    "eService Name Suffix" "" \
    "eService Processing Time Limit" "60" \
    "eService Retry Count" "5" \
    "eService Retry Delay" "60" \
    "eService Safety Policy" "policy_MxUpdateTestPolicy" \
    "eService Safety Vault" "vault_eServiceProduction"
mql connect bus  "${OBJECTID}" \
    relationship "eService Number Generator" \
    to "eService Number Generator" "type_MxUpdateTestType" ""
```