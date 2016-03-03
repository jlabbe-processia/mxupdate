

# Introduction #

Currently within a lot of MX projects the existing configuration (attributes, types, also commands etc.) are handled via MQL update scripts. One good example for that are the installation scripts which are delivered with the installation packages of the different centrals.


---


# Configuration Items #
As describes in [Wikipedia](http://www.wikipedia.org) the term [Configuration Items](http://en.wikipedia.org/wiki/Configuration_item) refers to the fundamental structural unit of a configuration management system. In MX the fundamental structure unit are e.g. for the data model attributes, types, policies..., for the user interface e.g. commands, menus, web forms and web tables. So the basic idea behind the "Configuration Item" methodic is to store a complete description of a fundamental structure unit in one single file. This means that each file could be handled easily within a source code repository (like [Subversion](http://subversion.tigris.org/) etc.).

Instead of described each versions and a delta (like in the XML update files delivered from MX) the idea behind the method is to describe the target of an administration object. E.g. for a command the HRef, all settings and all properties are described in this file. If the command must be changed a developer could see the complete command description. A modification of the command is for the developer than very easy. If then the command is deployed the existing command in MX is updated to the new target description in the file.


---


# File Names #
Each file name of a configuration item has a prefix and a suffix. The suffix
itself is always `.tcl`. The prefix depends on the configuration item type
(e.g. for a command the prefix `COMMAND_` is used). As an exception from
this rule are the program configuration items [JPO's](CI_Program_JPO.md) and
[MQL Program's](CI_Program_MQL.md).

## Encoding Rules ##
Within MX a configuration item could use some special characters which are not
handled from the file system. Therefore this special characters are encoded.

This characters are not encoded:
  * numbers
  * alphabetic characters (lower and upper case)
  * left or right parenthesis
  * plus '+' or minus '-'
  * commas ',' or points '.'
  * spaces ' '
  * equals signs '`=`'
  * underscores '`_`'

All other characters are converted with the algorithm:
  * the "at symbol" '@' will be converted to double at symbol '@@'
  * if a character is in the range of 0 and 254 (ASCII character), then the character is converted to a two characters long hexa-decimal code with '@' as prefix (e.g. double quotes '=' is converted to '@22')
  * characters greater than 254 are converted to a four characters long hexa-decimal code with '@u' as prefix (e.g. the euro sign '€' is converted to '@u20AC')


---


# Common TCL Procedures #
## Logging Procedures ##
For logging purposes special TCL procedures are defined which maps directly to
them defined in the MxUpdate Update tool. So the logged text is only printed
depending on the used logging level. The TCL procedures are
  * `logError TEXT`
  * `logWarning TEXT`
  * `logInfo TEXT`
  * `logDebug TEXT`
  * `logTrace TEXT`

## TCL Procedure "puts" ##
The TCL procedure `puts` is mapped to the above defined TCL procedure
`logDebug`.


---


# Common Parameter Definitions #
A lot of parameters for the MxUpdate Update tool could be defined. Some parameters could as default only defined within the property file.

## Symbolic Names ##
The parameters are used to control the behavior related to symbolic names. Symbolic names are used within MX for referencing administration objects so that the real name could be changed without changing the code where the administration object is referenced.
| **Name:** `CalcSymbolicNames`           <p><b>Parameter:</b> <code>‑‑calculatesymbolicnames</code></p><p><b>Default Value</b>: <code>false</code></p><p>With this parameter the symbolic names are always calculated and not extracted from the MxUpdate file header. This is e.g. useful if symbolic names in the header of MxUpdate files are not defined correctly.</p> |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Name:** `CalcSymbolicNameRegExp`                                                        <p><b>Default Value</b>: allowed are numbers, upper or lower case characters, percent sign <code>%</code>, ampersand <code>&amp;</code>, left <code>(</code> or right <code>)</code> parenthesis, plus <code>+</code> or minus <code>-</code>, colon <code>:</code>, equal sign <code>=</code>, caret <code>^</code>, underscore <code>_</code> and tilde <code>~</code></p><p>Defines the regular expression for special characters which are not allowed for symbolic names. This special characters are replaced "nothing", means by zero length string.</p> |
| **Name:** `RegisterSymbolicNames`                                                         <p><b>Default Value</b>: <code>eServiceSchemaVariableMapping.tcl</code></p><p>Defines the name of the program where all administration objects are registered with symbolic names.</p>                                                                                           |


---


# Explanation of Common Update Error Codes #
| **Error Code** | **Description** |
|:---------------|:----------------|
| 60301          | The JPO caller program `org.mxupdate.update.util.JPOCaller` is called without any required arguments. |
| 60302          | The JPO caller program `org.mxupdate.update.util.JPOCaller` is called with no correct argument defining the case. |
| 90602          | The file name is not correct defined and could not be converted back to a configuration item name. The exception could occur within updates. |