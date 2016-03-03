


---


# Introduction #
Expressions are evaluated against business objects or connections. For a deep
instruction see the "MQL Guide" or "Business Modeler Guide" of the
"ENOVIAvStudio Modeling Platform".


---


# Handled Properties #
This expression properties could be handled from MxUpdate:
  * description
  * hidden flag
  * expression value (expression itself)
  * properties


---


# Steps of the Update Flow #

## Cleanup ##
Following steps are done before the TCL update file is executed:
  * set to not hidden
  * reset description
  * remove value (no expression)

## Update ##
The TCL update file is executed.


---


# Example #
```
################################################################################
# EXPRESSION:
# ~~~~~~~~~~~
# TestExpression
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# expression_TestExpression
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# Expression for test purposes.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod expression "${NAME}" \
    description "Expression for test purposes." \
    !hidden \
    value "current"
```