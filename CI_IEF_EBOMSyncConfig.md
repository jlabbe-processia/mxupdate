


---


# Introduction #
The IEF EBOM Sync Configuration objects are used to configure EBOM sync's.

The configuration item itself is stored as business object. The IEF EBOM Sync
Configuration depends on the used integration. This means that all
configuration items are from a type derived from `IEF-EBOMSyncConfig`.
So the type of the EBOM Sync Configuration object is important and must be part
of the file name.

The file name is a concatenation of the EBOM Sync Configuration type, the name
and revision. As separator 16 underscores are used between the type and name and
8 underscores between name and revision.



---


# Example (Snippet) #
```
################################################################################
# IEFEBOMSYNC:
# ~~~~~~~~~~~~
# ECAD-EBOMSyncConfig________________MxUpdateEBOMSyncTest________-
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# -
# The EBOM sync configuration object for MxUpdate integration test.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod bus "${OBJECTID}" \
    description "The EBOM sync configuration object for MxUpdate integration test." \
          :
    "IEF-EBOMSync-AssignPartToMajor" "FALSE" \
    "IEF-EBOMSync-DrawingRelationInfo" "" \
    "IEF-EBOMSync-FailOnNotFindingMatchingPart" "TRUE" \
    "IEF-EBOMSync-GenerateCollapsedEBOM" "TRUE" \
    "IEF-EBOMSync-NewPartRevision" "1" \
    "IEF-EBOMSync-ObjectAttrMapping" "" \
          :
```