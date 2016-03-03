


---


# Introduction #
Relationships are used to define the behavior for attributes, triggers etc. of
connections between business objects. For a deep instruction see the "MQL Guide"
or "Business Modeler Guide" of the "ENOVIA Studio Modeling Platform".


---


# Handled Properties #
This relationship properties could be handled from MxUpdate:
  * description
  * hidden flag
  * triggers
  * attributes
  * properties
  * prevent duplicate flag
  * from / to side information:
    * meaning description
    * propagate connection / modify
    * cardinality
    * revision and clone action
    * types and relationships


---


# Steps of the Update Flow #

## Cleanup ##
Following steps are done before the CI update file is executed:
  * set to not hidden
  * reset description
  * remove all defined triggers
  * set to not prevent duplicate
  * remove all assigned rules
  * from / to side information:
    * reset meaning description
    * set not propagate connection and modify
    * set cardinality to one
    * set revision action to none
    * set clone action none
    * remove all types and relationships

## Update ##
The CI update file is executed. If the TCL procedure "testAttributes" is
defined, the attributes of the relationship are updated.


---


# TCL Procecure "testAttributes" #
The TCL procedure "testAttributes" is used to test if all attributes are
assigned to the relationship. If some attributes are missed, these attributes
are assigned to the relationship.

## Parameters ##
| **Parameter**                      | **Description** |
|:-----------------------------------|:----------------|
| `‑attributes [ATTR_LIST]`          | TCL list of attributes which must be defined on the relationship |
| `‑ignoreattr [NAME]`               | name of the attribute which is ignored (the attribute could be defined or not on the relationship); the global parameter `DMWithAttrIgnoreRelAttr` also exists |
| `‑relationship [NAME]`             | name of the relationship which is tested |
| `‑removeattr [NAME]`               | name of the attribute which is removed if currently assigned to the relationship; the global parameter `DMWithAttrRemoveRelAttr` also exists |

## Example with Two Attributes ##
The two attributes "Attribute 1" and "Attribute 2" must be defined on a
relationship. Within an update the name of the relationship is defined in the
TCL variable `NAME`.
```
testAttributes -relationship "${NAME}" -attributes [list \
    "Attribute 1" \
    "Attribute 2" \
]
```

## Example with One Attribute to Remove ##
The two attributes "Attribute 1" and "Attribute 2" must be defined on a
relationship. If attribute "Remove Attr" exists on the relationship, this
attribute will be automatically removed. Within an update the name of the
relationship is defined in the TCL variable `NAME`.
```
testAttributes -relationship "${NAME}" -removeattr "Remove Attr" -attributes [list \
    "Attribute 1" \
    "Attribute 2" \
]
```


---


# Parameter Definitions #
| **Name:** `DMRelationSupportRelCons`         <p><b>Default Value:</b> <code>true</code>                                         </p><p>Are from current MX version connections between relationships supported?</p><p>This is a V6 feature and so as default set to true. If an older MX version is used which does not support connections between relationships the value must be set to false.</p> |
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Name:** `DMWithAttrIgnoreRelAttr`          <p><b>Parameter:</b> <code>‑‑ignorerelationshipattributes [ATTRIBUTE_MATCH]</code> </p><p>Pattern defining the match of attributes which are ignored if not defined anymore within the test attributes of relationships.</p>                                                                                                                             |
| **Name:** `DMWithAttrRemoveRelAttr`          <p><b>Parameter:</b> <code>‑‑removerelationshipattributes [ATTRIBUTE_MATCH]</code> </p><p>Pattern defining the match of attributes which are removed if not defined anymore within the test attributes of relationships.</p>                                                                                                                             |


---


# Explanation of Update Error Codes #
| **Error Code** | **Description** |
|:---------------|:----------------|
| 12101          | The given attribute is not defined anymore from TCL procedure `testAttributes` but already assigned to the relationship. The attribute is not automatically removed because otherwise potentially data could be lost. |
| 12102          | A wrong parameter was given the called TCL procedure `testAttributes` which defines the assigned attributes for a relationship. |
| 12103          | The name of the relationship is not the same then defined through the name of the CI file from the called TCL procedure `testAttributes`. |


---


# Example #
```
################################################################################
# RELATIONSHIP:
# ~~~~~~~~~~~~~
# MxUpdateTestRelationship
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# relationship_MxUpdateTestRelationship
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# MxUpdate Test Relationship with two attributes and to / from types and
# relationships.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod relationship "${NAME}" \
    description "MxUpdate Test Relationship with two attributes and to / from types and relationships." \
    !hidden \
    !preventduplicates \
    from \
        !propagatemodify \
        !propagateconnection \
        meaning "From Type" \
        cardinality "N" \
        revision "none" \
        clone "none" \
        add type "MxUpdateTestFromType" \
    to \
        !propagatemodify \
        !propagateconnection \
        meaning "To Relationship" \
        cardinality "N" \
        revision "none" \
        clone "none" \
        add relationship "MxUpdateTestToRelationship"

testAttributes -relationship "${NAME}" -attributes [list \
    "MxUpdateTestAttribute1" \
    "MxUpdateTestAttribute2" \
]
```