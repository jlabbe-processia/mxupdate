


---


# Introduction #
Associations are a list of persons which could be defined as expression.


---


# Example #
```
################################################################################
# ASSOCIATION:
# ~~~~~~~~~~~~
# MxUpdate_PublicCheckin
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# association_MxUpdate_PublicCheckin
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# Used to give rights in a state expression testing if user has check in
# rights (like OOTB public read).
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod association "${NAME}" \
    description "Used to give rights in a state expression testing if user has check in rights (like OOTB public read)." \
    !hidden \
    definition "\"!User Agent\""
```