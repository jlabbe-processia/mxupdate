


---


# Common #

## Why is not XML used? ##
"Pure" XML files are hard to understand and not easily to write. Also the
complete syntax must be re-implemented to support all features from MX. This
means that every developer must learn a new syntax instead using an already
existing and well known syntax like MQL.

So script based files based on MQL syntax are more easily to learn for
developers. And it gives also the possibility to write "extra" code for
modifications (e.g. to assign all attributes to a type).

## Why are the Scripts not embedded within XML? ##
This means an "extra" overhead for the developer (embedding his script code
within XML code) and existing plug-in's for editors could not be used.

## Why was TCL used for the Configuration Item files? ##
The `emxGerLibUpdate` tool had used this file format for many years
also for productive deployments successfully. So the TCL approach was well
proved and tested.

Also it is possible to use existing plug-ins for the preferred editors which
support TCL code. E.g. Eclipse has a good plug-in for TCL.

So TCL itself is widely used and well known in the industry. The updates are
flexible and could be easily developed and managed.

PS: Also the OOTB Schema Installer used TCL scripts to update and to manage the
executions of scripts...

## Why not to use OOTB Schema Installer? ##
The OOTB Schema installer is fine and very good to update existing schemas
depending on delta's (which is required for a product, e.g. thinking about
renaming relationships in one version and adding an attribute in an later
version etc.). But this means that all modifications must be written **MANUALLY**
with deltas. It is not possible to make a quick change e.g. in the "Business
Modeler" and export the modification.

Also not all required configuration items are supported (e.g. triggers,
notifications, web tables). For this configuration items TCL delta scripts must
be written.

## What is the "Configuration Item" philosophy? ##
MxUpdate uses the "Configuration Item" philosophy which means that for all
single configuration item one script exists. This script defines the target,
independent which previous version is currently installed and used. So in
theory only the delta between the target and current existing will be installed.

## How is the Delta between Target and Existing calculated? ##
MxUpdate itself calculates for all items where no data could be lost with an
easy algorithm. First all current existing information is reset. Than the target
is defined (via MQL update).

An example is the update for a menu. First all assigned menus and commands are
removed. Then the script in the file is executed and all menus and commands are
assigned again.


---


# MxUpdate Update Tool #

## Alternatives to Manually Downloads ##

### Using SVN External Link ###
Instead of manually downloading the MxUpdate Update deployment tool, an
external link could be defined if [Subversion](http://subversion.tigris.org/) is
used. E.g. following property entry "`svn:externals`" for a directory must
be defined to reference version 0.5.0:
```
source http://mxupdate.googlecode.com/svn/mxupdate-update/tags/V0.5.0
```
Because for [development](UpdateDevelopment.md) another directory structure is used,
the `MXUPDATE_PATH` must set to the `src/main` directory.
The **`MxInstall.mql`** is located in `src/main/resources` sub path. The
script itself checks that a development structure is defined and installs
MxUpdate Update depending on this directory structure.

## Support of old MX versions ##

### Dimensions ###
If the used MX version within the project does not support the configuration
item "[Dimension](CI_DM_Dimension.md)" the parameter list of the type definition
"Dimension" and type defintion groups "Admin" and "DataModel" must be changed
that they do not include definitions for the configuration item
"[Dimension](CI_DM_Dimension.md)". Also the "extra" parameters for the
[dimensions](CI_DM_Dimension.md) must be removed from the parameter list. So
following lines must be added to the additional property file:
```
TypeDef.Dimension.ParameterList                     =
TypeDef.Dimension.ParameterListOpposite             =

TypeDefGroup.Admin.TypeDefList                      = IEFGlobalConfig,IEFGlobalRegistry,\
                                                      JPO,Program,Page,\
                                                      AttributeBoolean,AttributeDate,AttributeString,\
                                                      AttributeInteger,AttributeReal,\
                                                      Expression,Format,Interface,Policy,Relationship,Rule,Type,\
                                                      Notification,Trigger,TriggerGroup,\
                                                      NumberGenerator,ObjectGenerator,\
                                                      Association,Group,Person,PersonAdmin,Role,\
                                                      Channel,Command,Form,Inquiry,Menu,Portal,Table

TypeDefGroup.DataModel.TypeDefList                  = AttributeBoolean,AttributeDate,AttributeString,\
                                                      AttributeInteger,AttributeReal,\
                                                      Expression,Format,Interface,Policy,Relationship,Rule,Type,\
                                                      Notification,Trigger,TriggerGroup,\
                                                      NumberGenerator,ObjectGenerator

ParameterDef.DMDimAllowRemoveUnit.ParameterList     =
ParameterDef.DMDimAllowUpdateDefUnit.ParameterList  =
ParameterDef.DMDimAllowUpdateUnitMult.ParameterList =
ParameterDef.DMDimAllowUpdateUnitOffs.ParameterList =
```
For a deeper explanation see key "[TypeDef](UpdatePropertyFileFormat_TypeDef.md)"
and key "[TypeDefGroup](UpdatePropertyFileFormat_TypeDefGroup.md)" of the property
file format.

### Encoding Issue of Programs ###
Old MX versions does not encodes for programs double closing square brackets
correct. In this case following error is shown:
```
    org.xml.sax.SAXParseException: The content of elements must consist of
    well-formed character data or markup.
```
Then the encoding work-around for programs must be activated:
```
ParameterDef.ProgramUseEncodingWorkAround.Default = true
```
For more information see the documentation for the
[program configuration item](CI_Program.md).

### Relationship as from / to side for Relationships ###
If the used MX version within the project does not support the feature to use
also relationships as from / to side the property "DMRelationSupportRelCons"
must be changed and set to `false`. So following line must be added to the
additional property file:
```
ParameterDef.DMRelationSupportRelCons.Default = false
```
The installed default value is `true`.

### Role Types ###
If the used MX version within the project does not support the role type
feature, property "UserRoleSupportRoleType" must be changed and set to
`false`. So following line must be added to the additional property file:
```
ParameterDef.UserRoleSupportRoleType.Default = false
```
The installed default value is `true`.

### Products on Persons ###
If the used MX version does not uses products on person, the property
"UserPersonIgnoreProducts" must be changed:
```
ParameterDef.UserPersonIgnoreProducts.Default               = *
```
The default value is nothing. So now the products are not handled for all
persons (including the administration persons).
For for information see also the configuration item
[administration person](CI_User_PersonAdmin.md).

## How could I use MxUpdate Update together with emxGerLibUpdate? ##
"MxUpdate Update" and "emxGerLibUpdate" could be used together within one
installation. The recommendation is to use only one tool.

The Tool "emxGerLibUpdate" uses and installs other attribute and type names to
handle administration objects which are business objects. For attributes this
belongs to the author, installed date and version. For types it is the process
type (which is called in MxUpdate [trigger group](CI_DM_TriggerGroup.md)). So
following lines must be defined to use MxUpdate Update and emxGerLibUpdate
together in one environment:

```
# use attribute names from emxGerLibUpdate
PropertyDef.Author.AttributeName        = emxGerLibUpdateAuthor
PropertyDef.InstalledDate.AttributeName = emxGerLibUpdateInstalledDate
PropertyDef.Version.AttributeName       = emxGerLibUpdateVersion

# emxGerLibUpdate calls trigger group "Process"
TypeDef.TriggerGroup.BusPolicy          = emxGerLibUpdateProcess
TypeDef.TriggerGroup.BusRelsBoth        = emxGerLibUpdateSubProcess
TypeDef.TriggerGroup.BusType            = emxGerLibUpdateProcess
TypeDef.TriggerGroup.FilePath           = datamodel/process
TypeDef.TriggerGroup.FilePrefix         = PROCESS_
TypeDef.TriggerGroup.TextLogging        = process
TypeDef.TriggerGroup.TextTitle          = PROCESS
TypeDef.TriggerGroup.ParameterDesc      = Export / Import of processes.
TypeDef.TriggerGroup.ParameterList      = process
```
For a deeper explanation see key "[TypeDef](UpdatePropertyFileFormat_TypeDef.md)"
and key "[PropertyDef](UpdatePropertyFileFormat_PropertyDef.md)" of the property
file format.

## How could I remove automatically Attributes from Types, Relationships or Interfaces? ##
The "standard" behavior if an attribute is missed from a [type](CI_DM_Type.md),
[relationship](CI_DM_Relationship.md) or [interface](CI_DM_Inteface.md) is, that the
MxUpdate Update Tool throws an error because potentially some data could be
lost. This means typically that the attributes must be removed within a
pre-update script. Within "pure" development this is not a nice feature because
in this case the developer must write "extra" scripts because only some
modification of the data model is done.

Instead some configuration of the mapping properties could be done that
attributes are automatically removed. This behavior should be only configured
within development. It is **NOT** **RECOMMENDED** to use this configuration for
deployments on productive systems!

The parameters which defines the matches to remove attributes must be adapted.
It could be defined a star `*` to match all attributes. Maybe a good idea is
to define the match including a prefix (so that only custom attributes are
automatically removed).

There are two possibilities to define the parameters. They could be predefined
as properties in the mapping or as parameters when executing MxUpdate.

### Remove Attributes Automatically from Interfaces ###
**Defined via Parameter**
```
--removeinterfaceattributes *
```

**Defined via Mapping**
```
ParameterDef.DMWithAttrRemoveIntAttr.Default = *
```

### Remove Attributes Automatically from Relationships ###
**Defined via Parameter**
```
--removerelationshipattributes *
```

**Defined via Mapping**
```
ParameterDef.DMWithAttrRemoveRelAttr.Default = *
```

### Remove Attributes Automatically from Types ###
**Defined via Parameter**
```
--removetypeattributes *
```

**Defined via Mapping**
```
ParameterDef.DMWithAttrRemoveIntAttr.Default = *
```

## How could I predefine the Installer? ##
If MxUpdate is installed the used default name of the installer for
configuration items without an already defined installer is "The MxUpdate Team".
This value could be changed by defining following property in the mapping:
```
ParameterDef.DefaultInstaller.Default = YOUR_VALUE
```

## How are symbolic names handled? ##
Symbolic names are typically defined in the configuration item header


---


# MxUpdate Eclipse Plug-In #

## Why must the MX ADK installed as Eclipse Plug-In? ##
If the ADK is not installed as Eclipse Plug-In, the MxUdate Eclipse Plug-In
must include the ADK itself. Because of the legal this means that for each
new MxUpdate Eclipse Plug-In version each user must create himself the plug-in
by e.g. coping the ADK manually into the MxUpdate Eclipse Plug-In. Otherwise
if the ADK is separated in an own plug-in the MxUpdate Eclipse Plug-In could
be deployed separately with the Eclipse standard way, e.g. via a site.

## Why using the MxUpdate Eclipse Plug-In site? ##
If the MxUpdate Eclipse Plug-In site is used, an update of the Plug-In could
be done via the standard Eclipse update functionality.

## Could I install the MxUpdate Eclipse Plug-In manually? ##
Yes, the plug-in is also located under
the [downloads](http://code.google.com/p/mxupdate/downloads/list). A description
also exists. The preferred way is to install the plug-in via
the [MxUpdate Eclipse Plug-In site](http://mxupdate.googlecode.com/svn/mxupdate-eclipse-site/tags/).

## I've compiled with Maven the Eclipse Plug-In, but where must I copy the ..-feature.jar ? ##
The `org.mxupdate.eclipse_X.X.X-feature.jar` is only required if you are
working with an Eclipse update site. If you are directly install the Plug-In in
your Eclipse, you could ignore this created jar file.

For more information see the [Eclipse Plug-In development](Development_EclipsePlugIn.md)
description.


---


# Configuration Items #

## How could I define that a type is not derived from any other type? ##
The MxUpdate tool wants that a type is always derived from another type. If a
type is not derived from any type, the "hidden" type "ADMINISTRATION" could be
used.

**Example (snippet):**
```
mql escape mod type "${NAME}" \
    description "" \
    derived "ADMINISTRATION" \
    !hidden \
    abstract false
```
