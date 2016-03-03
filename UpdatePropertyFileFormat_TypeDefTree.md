


---


# Introduction #

Properties with key "TypeDefTree" defines the type definition tree for the
Eclipse plug-in. The type definition tree "`All`" must exists and defines
the which type definition trees are shown within the Eclipse plug-in.


---


# Sub Keys of "TypeDefTree" #
The sub keys are defined after the "TypeDefTree" Key. Following sub keys are
interpreted by MxUpdate:

| **Type**              | **Description** |
|:----------------------|:----------------|
| Label                 | Label used from the the Eclipse plug-in.|
| SubTypeDefTreeList    | Comma separated list of child type definition tree names. |
| TypeDefList           | Comma separated list of "[TypeDef](UpdatePropertyFileFormat_TypeDef.md)" names. |


---


# Example #
```
TypeDefTree.All.Label                   =
TypeDefTree.All.SubTypeDefTreeList      = DataModel,Integration,Program,User,UserInterface
TypeDefTree.All.TypeDefList             =
    :
TypeDefTree.User.Label                  = User
TypeDefTree.User.SubTypeDefTreeList     = Association,Group,Person,PersonAdmin,Role
TypeDefTree.User.TypeDefList            = Association,Group,Person,PersonAdmin,Role
    :
TypeDefTree.Person.Label                = Person
TypeDefTree.Person.SubTypeDefTreeList   =
TypeDefTree.Person.TypeDefList          = Person
    :
```