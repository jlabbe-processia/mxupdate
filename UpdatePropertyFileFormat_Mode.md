# Introduction #
Properties with key "Mode" defines the allowed parameters for the four different modes (delete, export, help and import).

# Four Different Modes #
The MxUpdate deployment tool knows four different modes. The modes are identified by special wording.

| **Type** | **Description** |
|:---------|:----------------|
| DELETE   | Defines the delete mode |
| EXPORT   | Defines the export mode |
| HELP     | Defines the help mode |
| IMPORT   | Defines the import / update mode |

# Sub Keys of "Mode" #
The sub keys are defined after the "Mode" Key. Following sub keys are interpreted by MxUpdate:

| **Type** | **Description** |
|:---------|:----------------|
| ParameterDesc | Description used for the help of the MxUpdate deployment tool.|
| ParameterList |  Comma separated list of parameter names (without '-' and '--' prefixes)<p><i>If a parameter contains only one single character, one minus '-' is used as prefix. If the parameter name contains more than one character, two minus '--' are used as prefix.</i></p><p><i>If no parameter list is defined, the user could not use the parameter within MxUpdate deployment tool.</i></p>|