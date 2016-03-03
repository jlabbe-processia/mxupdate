


---


# Steps to setup a new development-project from scratch #

This section describes the setup of a new development project with Eclipse.

## Prerequisites ##

The following prerequisites are needed:
  * Install Eclipse
  * Create new workspace in Eclipse
  * Install MxUpdate
  * Install MxUpdate Eclipse Plug-In

## Create new project in Eclipse ##

Create new project in Eclipse by selecting _File/New/Project_

## Create folder vendor for source-data ##

Create new folder _vendor_ inside the new project.

This folder will contain the source-files.

Create new folder _JPOs_ inside the _vendor_-Folder.

This folder will contain the source-JPOs.

Create new folder _lib_ inside the _vendor_-Folder.

This folder will contain the libraries.

## Export source JPOs via MQL ##

Start RMI-MQL.

Export all JPOs with the following command to **C:\TEMP\JPOs**:
```
exec prog MxUpdate --export --jpo * --path C:\TEMP\JPOs;
```

Move all files and folders from **C:\TEMP\JPOs\jpo** to **\vendor\JPOs** in the new project-structure.

Delete folder **org** and file **MxUpdate\_mxJPO.java**.

## Set build path for JPOs in Eclipse ##

In Eclipse select project with right mouse-click.

In Pop-Up-Menu select _Build Path\Configure Build Path_.

Select **Add Folder** and define folder **\vendor\JPOs**.

## Copy libraries needed in new Eclipse-Project ##

Copy all library-files from Application Server, e.g. **c:\tomcat\WEB-INF\lib**, to folder **\vendor\lib**.

Copy file **servlet-api.jar** from Application Server, e.g. **c:\tomcat\lib**, to **\vendor\lib**.

## Defined libraries needed in new Eclipse-Project ##

In Eclipse select project with right mouse-click.

In Pop-Up-Menu select _Build Path\Add Library\User Library_.

Create new **User Library**.

Mark new **User Library** and select **Add JARs**

Select folder **\vendor\lib** and add all JARs from this folder.

## Create folder-structure for Project-Files ##

Create following folder-structure under project-root:
```
  \10_pre_environment
  \20_environment
  \30_pre_mxupdate
  \40_mxupdate
  \40_mxupdate\datamodel
  \40_mxupdate\datamodel\attribute
  \40_mxupdate\datamodel\dimension
  \40_mxupdate\datamodel\expression
  \40_mxupdate\datamodel\format
  \40_mxupdate\datamodel\interface
  \40_mxupdate\datamodel\notification
  \40_mxupdate\datamodel\numbergenerator
  \40_mxupdate\datamodel\objectgenerator
  \40_mxupdate\datamodel\policy
  \40_mxupdate\datamodel\relationship
  \40_mxupdate\datamodel\rule
  \40_mxupdate\datamodel\trigger
  \40_mxupdate\datamodel\triggergroup
  \40_mxupdate\datamodel\type
  \40_mxupdate\integration
  \40_mxupdate\integration\ebomsync
  \40_mxupdate\integration\globalconfig
  \40_mxupdate\integration\globalregistry
  \40_mxupdate\program
  \40_mxupdate\program\csharp
  \40_mxupdate\program\jpo
  \40_mxupdate\program\mql
  \40_mxupdate\program\page
  \40_mxupdate\user
  \40_mxupdate\user\association
  \40_mxupdate\user\group
  \40_mxupdate\user\person
  \40_mxupdate\user\personadmin
  \40_mxupdate\user\role
  \40_mxupdate\userinterface
  \40_mxupdate\userinterface\channel
  \40_mxupdate\userinterface\command
  \40_mxupdate\userinterface\form
  \40_mxupdate\userinterface\inquiry
  \40_mxupdate\userinterface\menu
  \40_mxupdate\userinterface\portal
  \40_mxupdate\userinterface\table
  \50_post_mxupdate
  \60_environment
  \70_post_environment
```

## Define JPO-Folder of project as source folder in Eclipse ##

In Eclipse select folder **\40\_mxupdate\program\jpo** with right mouse-click.
In Pop-Up-Menu select _Build Path\Use as Source Folder_.

