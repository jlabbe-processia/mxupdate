


---


# Introduction #
The MxUpdate update steps for configuration items is typically done by removing all values and add them again. For dimensions this is not possible, because otherwise there is potentially some data lost. So for dimensions a "special" format is defined.


---


# Updates where Data could be lost #

## Update of the Default Unit ##
Theoretically an unit could be defined as default if the dimension was not used (from business objects). Otherwise this means the default unit could not be changed if already set. A MQL system error will be thrown in this case. Instead a manual conversion must be done.

Because it could be that in a development system no data exists which references a dimension where the default unit must be changed, an exception will be thrown for this case.

The behavior could be overwritten by defining the parameters `‑‑dimensionAllowUpdateDefaultUnit`.

## Update of Multiplier or Offset for an Unit ##
As default the MxUpdate Update does not allow to update the multiplier or offset of units. If the MxUpdate Update found out that this must be done, an exception is thrown and the update itself is stopped.

This MxUpdate Update makes this, because if a multiplier or offset is changed, the original value from the user input will be changed, because the calculated value depending on the default unit will be same.

The behavior could be overwritten by defining the parameters `‑‑dimensionAllowUpdateUnitMultiplier` or `‑‑dimensionAllowUpdateUnitOffset`.

## Remove of an Unit ##
If the MxUpdate Update found out that an unit must be removed, an exception is thrown. Otherwise if the MxUpdate Update does not check this, MX itself could potentially throw an exception. Because the unit of a dimension is removed only if the unit itself was not referenced (used) within a business object instance. Otherwise the update failed with a MQL system error. So in this case a manual conversion must be done.

The idea behind is that e.g. a development system does not contain the complete productive data. So the exception will be also thrown in the development (and not the first time when an update of a productive system is tried).

The behavior could be overwritten by defining the parameters `‑‑dimensionAllowRemoveUnit`.


---


# Parameter Definitions #
| **Name:** `DMDimAllowRemoveUnit`             <p><b>Parameter:</b> <code>‑‑dimensionAllowRemoveUnit</code></p>          <p><b>Default Value:</b> <code>false</code></p><p>If the parameter is defined, the MxUpdate Update removes undefined units. Otherwise an exception is thrown if an unit must be removed.</p> |
|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Name:** `DMDimAllowUpdateDefUnit`          <p><b>Parameter:</b> <code>‑‑dimensionAllowUpdateDefaultUnit</code></p>   <p><b>Default Value:</b> <code>false</code></p><p>If the parameter is defined, the MxUdpate Update allows to change the default unit of a dimension. Otherwise an exception is thrown if the default unit must be changed.</p> |
| **Name:** `DMDimAllowUpdateUnitMult`         <p><b>Parameter:</b> <code>‑‑dimensionAllowUpdateUnitMultiplier</code></p><p><b>Default Value:</b> <code>false</code></p><p>If the parameter is defined, the MxUpdate Update allows to change the multiplier of units. Otherwise an exception is thrown if the multiplier must be changed.</p> |
| **Name:** `DMDimAllowUpdateUnitOffs`         <p><b>Parameter:</b> <code>‑‑dimensionAllowUpdateUnitOffset</code></p>    <p><b>Default Value:</b> <code>false</code></p><p>If the parameter is defined, the MxUpdate Update allows to change the offset of units. Otherwise an exception is thrown if the offset must be changed.</p> |


---


# Explanation of Update Error Codes #
| **Error Code** | **Description** |
|:---------------|:----------------|
| 10601          | The multiplier of a unit of a dimension is tried to update which means that potentially some data could be loosed. |
| 10602          | The offset of a unit of a dimension is tried to update which means that potentially some data could be loosed. |
| 10603          | The default unit of a dimension is tried to update which means that potentially some data could be loosed. FYI: MX itself also tries to prevent this if a default unit is already defined, but it could be that this case does only happens e.g. in non development systems.... |
| 10604          | An unit of a dimension is tried to remove which means that potentially some data could be loosed. FYI: MX itself also tries to prevent this if a unit is used, but it could be that this case does only happens e.g. in non development systems.... |