


---


# Introduction #

Properties with key "TypeDefGroup" defines additional parameters and default
values used from the MxUpdate Deployment Tool.


---


# Sub Keys of "TypeDefGroup" #
The sub keys are defined after the "TypeDefGroup" Key. Following sub keys are
interpreted by MxUpdate:

| **Type**         | **Description** |
|:-----------------|:----------------|
| ParameterDesc    | Description used for the help of the MxUpdate deployment tool.|
| ParameterList    | Comma separated list of parameter names (without '-' and '--' prefixes). The parameters are not case sensitive.<p><i>If a parameter contains only one single character, one minus '-' is used as prefix. If the parameter name contains more than one character, two minus '--' are used as prefix.</i></p><p><i>If no parameter list is defined, the user could not use the parameter within MxUpdate deployment tool.</i></p>|
| TypeDefList      | Comma separated list of "[TypeDef](UpdatePropertyFileFormat_TypeDef.md)" names |


---


# Example #
```
    :
TypeDefGroup.UserInterface.ParameterList    = ui,userinterface
TypeDefGroup.UserInterface.ParameterDesc    = Export / Import of user interface administration objects.
TypeDefGroup.UserInterface.TypeDefList      = Channel,Command,Form,Inquiry,Menu,Portal,Table
    :
```