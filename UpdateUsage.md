


---


# Introduction #
After the MxUpdate Update Deployment Tool is [installed](UpdateInstallation.md), the tool could be used via MQL console. Three different "modes" exists:
  * export
  * update (import / create)
  * delete
The parameter names depends on the configured [properties](UpdatePropertyFileFormat.md) which could be project specific. To get an overview over all parameter call
```
exec prog MxUpdate --help
```
to get all parameters with arguments and short description.


---


# Export Configuration Items #
To export configuration items parameter `‑‑export` (short `‑e`) must be defined. The export path must be defined with `‑‑path`. At minimum one path must be defined. Because the export must know where to export the configuration items, only one path at maximum could be defined.
With the other parameters the configuration item types with matching names are defined.

## Further Export Configurations ##
The content of the header of update files could be configured depending on parameters.
| **Parameter**          | **Description** |
|:-----------------------|:----------------|
| `‑‑exportapplication`  | Exports the name of the application and writes this name in the header of the TCL update file (below the author). For JPOs and programs the parameter is not supported. |
| `‑‑exportinstaller`    | Exports the name of the installer and writes this name in the header of the TCL update file (below the author). For JPOs and programs the parameter is not supported. |
| `‑‑exportnoauthor`     | The author of the administration object is NOT written in the header of the TCL update file. |
| `‑‑exportoriginalname` | Exports the name of the original name and writes this name in the header of the TCL update file (below the symbolic name). For JPOs and programs and all business objects like triggers the parameter is not supported. |
| `‑‑exportversion`      | Exports the version and writes this version in the header of the TCL update file (below the application and author). For JPOs and programs the parameter is not supported. |
This default configuration could defined project specific (see [Configuration of the Parameter Definitions](UpdatePropertyFileFormat_ParameterDef.md))

## Examples ##
### Export all Configuration Items ###
```
exec prog MxUpdate --export --path C:\Temp\DBSchema --all *
```

### Export all JPOs ###
```
exec prog MxUpdate --export --path C:\Temp\DBSchema --jpo *
```

### Export all JPOs except JPOs starting with org ###
```
exec prog MxUpdate --export --path C:\Temp\DBSchema --jpo * --ignorejpo org*
```


---


# Update Configuration Items #
To export configuration items parameter `‑‑update` (short `-u`) must be defined. Depending on a defined parameter `‑‑path` two cases exists:
  * If at minimum one path is defined with parameter `‑‑path` the `<MATCH>` arguments are patterns used to evaluate matching names names without prefix and suffix.
  * If no path parameter is defined, the `<MATCH>` arguments are defined as file names. A defined asterisk '`*`' is interpreted as file wild card.

## 'Update is Required' Check ##
An update of an existing object must not be done always. The default configuration is, that an update is done always.
| **Parameter**          | **Description** |
|:-----------------------|:----------------|
| `‑‑checkfiledate`      | Check if an update is required by comparing the defined version against the value of the version property. E.g. if parameter '`‑‑usefiledateasversion`' is defined, the last modified date in seconds of the file is used to compare against the version property within database. |
| `‑‑checkversion`       | Check if an update is required by comparing the last modified date against the value of the file date property.|

## Version Definition ##
| **Parameter**               | **Description** |
|:----------------------------|:----------------|
| `‑‑usefiledateasversion`    | The last modified date in seconds of the file is used as version information. |
| `‑‑version <VERSIONNUMBER>` | Defines the version of administration objects (e.g. 1-0). |

## Symbolic Names ##
| **Parameter**               | **Description** |
|:----------------------------|:----------------|
| `‑‑calculatesymbolicnames`  | With this parameter the symbolic names are always calculated and not extracted from the MxUpdate file header. This is e.g. useful if symbolic names in the header of MxUpdate files are not defined correctly. |

## Further Configurations ##
| **Parameter**                           | **Description** |
|:----------------------------------------|:----------------|
| `‑‑defaultapplication <APPLICATIONAME>` | Defines the default name of application which is defined as property / attribute on administration objects. The value is only used for the application on the administration objects if no application is in the update script or as parameter defined. (Default 'Unknown') |
| `‑‑defaultauthor <AUTHORNAME>`          | Defines the default name of author which is defined as property / attribute on administration objects. The value is only used for the author on the administration objects if no author is in the update script or as parameter defined. (Default 'The MxUpdate Team') |
| `‑‑defaultinstaller <INSTALLERNAME>`    | Defines the default name of installer which is defined as property / attribute on administration objects. The value is only used for the installer on the administration objects if no installer is in the update script or as parameter defined. (Default 'The MxUpdate Team') |
| `‑‑author <AUTHORNAME>`                 | Defines the author for administration objects. The default author and the author in the update script is overwritten. |
| `‑‑installer <INSTALLERNAME>`           | Defines the installer for administration objects. The default installer name and existing installer names in update scripts are overwritten. |


---


# Delete Configuration Items #
To export configuration items parameter `‑‑update` (short `‑u`) must be defined. The path of update files which are checked must be defined with parameter `‑‑delete`. All other parameter defines the matching administration objects.