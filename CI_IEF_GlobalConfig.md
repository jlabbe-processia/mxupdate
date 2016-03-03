


---


# Introduction #
The IEF Global Configuration objects are used to configure integrations and the
related mapping between the CAD application and MX.

The configuration item itself is stored as business object. The IEF Global
Configuration depends on the used integration. This means that all
configuration items are from a type derived from `MCADInteg-GlobalConfig`.
So the type of the configuration object is important and must be part of the
file name.

The file name is a concatenation of the global configuration type, the name and
revision. As separator 16 underscores are used between the type and name and 8
underscores between name and revision.

If no global configuration type is defined in the file name (e.g. if an upgrade
from `emxGerLibUpdate` is done), the default global configuration type
`MCADInteg-GlobalConfig` is used.


---


# Example (Snippet) #
```
################################################################################
# IEFGLOBALCONFIG:
# ~~~~~~~~~~~~~~~~
# ECADInteg-GlobalConfig________________MxUpdateIntegrationTest________1
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# 1
# The global configuration object for MxUpdate integration test.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod bus "${OBJECTID}" \
    description "The global configuration object for MxUpdate integration test." \
          :
    "IEF-CSELaunchBinaryDetails" "" \
    "IEF-CheckObsoleteAcrossRevisions" "false" \
    "IEF-CheckUUIDConflict" "FALSE" \
    "IEF-CreateVersionObjects" "TRUE" \
    "IEF-DerivedOutputParameterObjTypeMapping" "" \
    "IEF-DerivedOutputTypeDefaultParameterObjectMapping" "" \
          :
```