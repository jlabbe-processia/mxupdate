# Introduction #

Properties with key "TypeDef" defines all existings administration types which could be imported and
exported with the MxUpdate Deployment Tool.

# Sub Keys of "TypeDef" #

The sub keys are defined after the "TypeDef" Key. Following sub keys are interpreted by MxUpdate:

| **Type**                   | **Description** |
|:---------------------------|:----------------|
| AdminSuffix                | Suffix for admin administration objects<p>_E.g. for web tables <code>system</code> must be defined.</p>_ |
| AdminType                  | Name of the type for admin administration objects |
| BusIgnoreAttributes        | Comma separated list of ignored attributes for business administration objects  |
| BusPolicy                  | Policy for business administration objects<p>_With this policy new business objects are created.</p>_ |
| BusRelsBoth                | Comma separated list of to / from relationships for business administration objects |
| BusRelsFrom                | Comma separated list of from relationships for business administration objects |
| BusRelsTo                  | Comma separated list of to relationships for business administration objects |
| BusType                    | Type for business administration objects |
| BusTypeDerived             | If set to _true_ business objects are using types which are derived from the defined business type. The default value is _false_. |
| BusVault                   | Vault for business administration objects <p><i>Business objects are created in this vault.</i></p> |
| FileMatchLast              | Defines that a match must be done at last (after a file does not match any other "TypeDef" definition); value must set to **true** <p><i>E.g. if no file prefix and file suffix is defined, a match must be done as last. Otherwise a file matches always.</i></p>  |
| FilePath                   | File path used to export. |
| FilePrefix                 | Prefix used for file names. |
| FileSuffix                 | Suffix used for file names. |
| JPO                        | Defines the name of the JPO which implements the export / import |
| OrderNo                    | Defines the order number within an update. If no order number is defined, the update is done after all other updates. |
| ParameterDesc              | Description of the parameter used in the help. |
| ParameterDescOpposite      | Description of the opposite parameters used in the help.<p><i>Opposite parameters are used to define matches file names which must be ignored.</i></p> |
| ParameterList              | Comma separated list of parameters. The parameters are not case sensitive. <p><i>If a parameter contains only one single character, one minus '-' is used as prefix. If the parameter name contains more than one character, two minus '--' are used as prefix.</i></p><p><b>Example</b><br /><code>TypeDef.Policy.ParameterList = p,pol,policy</code><br />So for policies three parameters are defined:<br /><code>-p</code><br /><code>--pol</code><br /><code>--policy</code></p> |
| ParameterListOpposite      | Comma separated list of opposite parameters. The parameters are not case sensitive. <p><i>The list is defined in the same way as the ParameterList sub key.</i></p> |
| TextLogging                | Text used as logging entry. |
| TextTitle                  | Title entry in the MxUpdate file. |
| Icon                       | Path to the icon used from the MxUpdate Eclipse plug-in. |
| Wiki                       | Name of the Wiki file used to reference. |