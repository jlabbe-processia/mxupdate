


---


# Introduction #
Types are used to define the behavior for attributes, triggers etc. of business
objects. For a deep instruction see the "MQL Guide" or "Business Modeler Guide"
of the "ENOVIA Studio Modeling Platform".


---


# Handled Properties #
This type properties could be handled from MxUpdate:
  * description
  * hidden flag
  * abstract flag
  * methods
  * triggers
  * attributes
  * properties


---


# Steps of the Update Flow #

## Cleanup ##
Following steps are done before the CI update file is executed:
  * set to not hidden
  * set to not abstract
  * reset description
  * remove all defined methods
  * remove all defined triggers

## Update ##
The CI update file is executed. If the TCL procedure "testAttributes" is
defined, the attributes of the types are updated.


---


# TCL Procecure "testAttributes" #
The TCL procedure "testAttributes" is used to test if all attributes are
assigned to the type. If some attributes are missed, these attributes are
assigned to the type.

## Parameters ##
| **Parameter**                      | **Description** |
|:-----------------------------------|:----------------|
| `‑attributes [ATTR_LIST]`          | TCL list of attributes which must be defined on the type |
| `‑ignoreattr [NAME]`               | name of the attribute which is ignored (the attribute could be defined or not on the type); the global parameter `DMWithAttrIgnoreTypeAttr` also exists |
| `‑removeattr [NAME]`               | name of the attribute which is removed if currently assigned to the type; the global parameter `DMWithAttrRemoveTypeAttr` also exists |
| `‑type [NAME]`                     | name of the type which is tested |

## Example with Two Attributes ##
The two attributes "Attribute 1" and "Attribute 2" must be defined on a type.
Within an update the name of the type is defined in the TCL variable `NAME`.
```
testAttributes -type "${NAME}" -attributes [list \
    "Attribute 1" \
    "Attribute 2" \
]
```

## Example with One Attribute to Remove ##
The two attributes "Attribute 1" and "Attribute 2" must be defined on a type.
If attribute "Remove Attr" exists on the type, this attribute will be
automatically removed. Within an update the name of the type is defined in the TCL
variable `NAME`.
```
testAttributes -type "${NAME}" -removeattr "Remove Attr" -attributes [list \
    "Attribute 1" \
    "Attribute 2" \
]
```


---


# Parameter Definitions #
| **Name:** `DMWithAttrIgnoreTypeAttr`         <p><b>Parameter:</b> <code>‑‑ignoretypeattributes [ATTRIBUTE_MATCH]</code> </p><p>Pattern defining the match of attributes which are ignored if not defined anymore within the test attributes of types.</p> |
|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Name:** `DMWithAttrRemoveTypeAttr`         <p><b>Parameter:</b> <code>‑‑removetypeattributes [ATTRIBUTE_MATCH]</code> </p><p>Pattern defining the match of attributes which are removed if not defined anymore within the test attributes of types.</p> |


---


# Explanation of Update Error Codes #
| **Error Code** | **Description** |
|:---------------|:----------------|
| 12101          | The given attribute is not defined anymore from TCL procedure `testAttributes` but already assigned to the type. The attribute is not automatically removed because otherwise potentially data could be lost. |
| 12102          | A wrong parameter was given the called TCL procedure `testAttributes` which defines the assigned attributes for a type. |
| 12103          | The name of the type is not the same then defined through the name of the CI file from the called TCL procedure `testAttributes`. |


---


# Example #
```
################################################################################
# TYPE:
# ~~~~~
# MxUpdateTestType
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# type_MxUpdateTestType
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# MxUpdate Test Type with two attributes.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod type "${NAME}" \
    description "MxUpdate Test Type with two attributes." \
    derived "ADMINISTRATION" \
    !hidden \
    abstract "false"

testAttributes -type "${NAME}" -attributes [list \
    "MxUpdateTestAttribute1" \
    "MxUpdateTestAttribute2" \
]
```
