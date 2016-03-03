# Introduction #

With the property file the "behavior" of MxUpdate Update is described. With the property file the allowed parameters, descriptions, administration types etc. are defined. In a project a specific property file could be used (see [Installation](UpdateInstallation.md)).


# Format #
In the property file an entry is a concatenation of a key, a name and a sub key with the following format:
```
<KEY>.<NAME>.<SUBKEY>
```
The keys and their related sub keys are described in:
  * [Key "Mode"](UpdatePropertyFileFormat_Mode.md)
  * [Key "ParameterDef"](UpdatePropertyFileFormat_ParameterDef.md)
  * [Key "PropertyDef"](UpdatePropertyFileFormat_PropertyDef.md)
  * [Key "TypeDef"](UpdatePropertyFileFormat_TypeDef.md)
  * [Key "TypeDefGroup"](UpdatePropertyFileFormat_TypeDefGroup.md)
  * [Key "TypeDefTree"](UpdatePropertyFileFormat_TypeDefTree.md)