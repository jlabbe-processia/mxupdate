


---


# Introduction #
Roles are used to define for persons specific access depending on there role.
The three different kind of roles for standard roles, organizational roles and
project roles exists. For a deep instruction see the "MQL Guide" or
"Business Modeler Guide" of the "ENOVIA Studio Modeling Platform".


---


# Special Handling of Role Types #
This three different kind of roles exists:
  * standard roles
  * organizational roles
  * project roles
If the kind of the role is changed it could not be reseted if some child roles
are assigned to this role. So the kind of the roles be always defined with
correct MQL keywords.


---


# Handled Properties #

This type properties could be handled from MxUpdate:
  * description
  * hidden flag
  * kind of role (role type)
  * properties
  * parent roles


---


# Steps of the Update Flow #
Following steps are done before the CI update file is executed:
  * set to not hidden
  * reset description
  * remove all parent roles


---


# Parameter Definitions #
| **Name:** `UserIgnoreWSO4Roles`              <p><b>Parameter:</b> <code>‑‑ignoreworkspaceobjectsforrole [ROLE_MATCH]</code>, <code>‑‑ignorewso4role [ROLE_MATCH]</code> </p><p>Defines the match of roles for which workspace objects are not handled (neither exported nor updated).</p><p>Attention! A workspace object defined in the TCL update file are not ignored and will be created!</p> |
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Name:** `UserRoleSupportRoleType`          <p><b>Default Value:</b> <code>true</code>                                                                          </p><p>Are from current MX version role types supported? This is a V6 feature and so as default set to true. If an older MX version is used which does not support role types the value must be set to <code>false</code>.</p>   |


---


# Example #
```
################################################################################
# ROLE:
# ~~~~~
# MxUpdate_Role
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# role_MxUpdate_Role
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# Role for test purposes.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod role "${NAME}" \
    description "Role for test purposes." \
    !hidden \
    asarole
mql escape mod role "Employee" child "${NAME}"
```