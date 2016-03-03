


---


# Introduction #
Trigger objects are used from the "emxTriggerManager" to define called JPOs
with defined arguments.


---


# Example #
```
################################################################################
# TRIGGER:
# ~~~~~~~~
# TRIGGER_TypeMxUpdateTestTypeCreateAction________TestTrigger
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# TestTrigger
# Test Trigger to call JPO 'MxUpdateTestProgram' with action create.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod bus "${OBJECTID}" \
    description "Test Trigger to call JPO 'MxUpdateTestProgram' with action create." \
    "eService Constructor Arguments" "" \
    "eService Error Type" "Error" \
    "eService Method Name" "mxMain" \
    "eService Program Argument 1" "\$\{OBJECTID\}" \
    "eService Program Argument 2" "" \
    "eService Program Argument 3" "" \
    "eService Program Argument 4" "" \
    "eService Program Argument 5" "" \
    "eService Program Argument 6" "" \
    "eService Program Argument 7" "" \
    "eService Program Argument 8" "" \
    "eService Program Argument 9" "" \
    "eService Program Argument 10" "" \
    "eService Program Argument 11" "" \
    "eService Program Argument 12" "" \
    "eService Program Argument 13" "" \
    "eService Program Argument 14" "" \
    "eService Program Argument 15" "" \
    "eService Program Argument Desc 1" "id of the MxUpdate Test Type object" \
    "eService Program Argument Desc 2" "" \
    "eService Program Argument Desc 3" "" \
    "eService Program Argument Desc 4" "" \
    "eService Program Argument Desc 5" "" \
    "eService Program Argument Desc 6" "" \
    "eService Program Argument Desc 7" "" \
    "eService Program Argument Desc 8" "" \
    "eService Program Argument Desc 9" "" \
    "eService Program Argument Desc 10" "" \
    "eService Program Argument Desc 11" "" \
    "eService Program Argument Desc 12" "" \
    "eService Program Argument Desc 13" "" \
    "eService Program Argument Desc 14" "" \
    "eService Program Argument Desc 15" "" \
    "eService Program Name" "MxUpdateTestProgram" \
    "eService Sequence Number" "1" \
    "eService Target States" ""
mql escape promote bus "${OBJECTID}"
```