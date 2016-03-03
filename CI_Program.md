


---


# Introduction #
Currently following program objects are supported from MxUpdate:
  * [JPO programs](CI_Program_JPO.md)
  * [MQL programs](CI_Program_MQL.md)
  * [page objects](CI_Program_Page.md)


---


# Encoding Work-Around for old MX versions #
Old MX versions exports two closing square brackets '`]]`' not encoded. This
means that the XML parser used internally from MxUpdate failed. In this case
following error message appears:
```
    org.xml.sax.SAXParseException: The content of elements must consist of well-formed character data or markup.
```
MxUpdate has an implemented work-around that corrects the encoding of the XML
export. To use this work-around parameter **ProgramUseEncodingWorkAround** must
be activated (see [CI\_Program#Parameter\_Definitions](CI_Program#Parameter_Definitions.md)).


---


# Parameter Definitions #
| **Name:** `ProgramUseEncodingWorkAround`     <p><b>Default Value:</b> <code>false</code></p><p>The encoding work around for old MX versions is used.</p> |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------|

The parameters could be changed depending on project needs. For further information see the [Parameter Definition Format](UpdatePropertyFileFormat_ParameterDef.md).