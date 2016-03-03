


---


# Introduction #
A number generator is used to create new numbers which e.g. could be used for
names of business objects.


---


# Example #
```
################################################################################
# NUMBERGENERATOR:
# ~~~~~~~~~~~~~~~~
# type_MxUpdateTestType
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# Number Generator for MxUpdate Test Type Objects.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql mod bus "${OBJECTID}" \
    description "Number Generator for MxUpdate Test Type Objects."

# update of the next number attribute only if not already set
set sTmp [mql print bus "${OBJECTID}" select attribute\[eService Next Number\] dump]
if {[string length "${sTmp}"]==0}  {
  mql mod bus "${OBJECTID}" "eService Next Number" "0000001"
}
```