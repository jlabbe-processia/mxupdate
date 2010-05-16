/*
 * Copyright 2008-2010 The MxUpdate Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision:        $Rev$
 * Last Changed:    $Date$
 * Last Changed By: $Author$
 */

package org.mxupdate.test.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import matrix.util.MatrixException;

import org.mxupdate.test.AbstractTest;
import org.mxupdate.test.ExportParser;
import org.mxupdate.test.data.util.PropertyDef;
import org.testng.Assert;

/**
 * Defines common information from administration objects used to create,
 * update and check them.
 *
 * @param <DATA>    class which is derived from this class
 * @author The MxUpdate Team
 * @version $Id$
 */
public abstract class AbstractAdminData<DATA extends AbstractAdminData<?>>
    extends AbstractData<DATA>
{
    /**
     * Regular expression to defines the list of not allowed characters  of
     * symbolic names which are removed for the calculated symbolic name.
     *
     * @see #AbstractAdminData(AbstractTest, org.mxupdate.test.AbstractTest.CI, String, Set)
     */
    private static final String NOT_ALLOWED_CHARS
            = "[^%&()+-0123456789:=ABCDEFGHIJKLMNOPQRSTUVWXYZ^_abcdefghijklmnopqrstuvwxyz~]";

    /**
     * Symbolic name of the data piece.
     */
    private String symbolicName;

    /**
     * Is the administration object hidden?
     *
     * @see #setHidden(Boolean)
     * @see #isHidden()
     */
    private Boolean hidden = false;

    /**
     * Defines the values which must be defined for exports. They are tested
     * for existence from {@link #checkExport(ExportParser)}. This values must
     * be defined minimum and maximum one time in the configuration item file.
     * The key is the name of the value, the value of the map the expected
     * default value.
     *
     * @see #checkExport(ExportParser)
     */
    private final Map<String,String> requiredExportValues;

    /**
     * All properties for this data piece.
     *
     * @see #addProperty(PropertyDef)
     */
    private final Set<PropertyDef> properties = new HashSet<PropertyDef>();

    /**
     * Constructor to initialize this data piece.
     *
     * @param _test                 related test case
     * @param _ci                   related configuration type
     * @param _name                 name of the administration object
     * @param _requiredExportValues defines the required values of the
     *                              export within the configuration item
     *                              file
     */
    protected AbstractAdminData(final AbstractTest _test,
                                final AbstractTest.CI _ci,
                                final String _name,
                                final Map<String,String> _requiredExportValues)
    {
        super(_test, _ci, _name);
        this.symbolicName = (_ci != null)
                            ? _ci.getMxType() + "_" + this.getName().replaceAll(AbstractAdminData.NOT_ALLOWED_CHARS, "")
                            : null;
        this.requiredExportValues = (_requiredExportValues != null) ? _requiredExportValues : new HashMap<String,String>(0);
    }

    /**
     * Defines the symbolic name of this data piece.
     *
     * @param _symbolicName     new symbolic name
     * @return this original data instance
     * @see #symbolicName
     */
    @SuppressWarnings("unchecked")
    public DATA setSymbolicName(final String _symbolicName)
    {
        this.symbolicName = _symbolicName;
        return (DATA) this;
    }

    /**
     * Returns the symbolic name of the abstract data element.
     *
     * @return symbolic name of the abstract data element
     * @see #symbolicName
     */
    public String getSymbolicName()
    {
        return this.symbolicName;
    }

    /**
     * Defines if this data instance must be hidden.
     *
     * @param _hidden       <i>true</i> if the data instance is hidden;
     *                      otherwise <i>false</i>
     * @return this data instance
     * @see #hidden
     */
    @SuppressWarnings("unchecked")
    public DATA setHidden(final Boolean _hidden)
    {
        this.hidden = _hidden;
        return (DATA) this;
    }

    /**
     * Returns <i>true</i> if this data instance is {@link #hidden}.
     *
     * @return <i>true</i> if this data instance is hidden; otherwise
     *         <i>false</i>
     * @see #hidden
     */
    public Boolean isHidden()
    {
        return this.hidden;
    }

    /**
     * Assigns <code>_property</code> to this data piece.
     *
     * @param _property     property to add / assign
     * @return this data piece instance
     * @see #properties
     */
    @SuppressWarnings("unchecked")
    public DATA addProperty(final PropertyDef _property)
    {
        this.properties.add(_property);
        return (DATA) this;
    }

    /**
     * Returns all assigned {@link #properties} from this data piece.
     *
     * @return all defined properties
     */
    public Set<PropertyDef> getProperties()
    {
        return this.properties;
    }

    /**
     * Returns all {@link #requiredExportValues required export values}.
     *
     * @return required export values
     * @see #requiredExportValues
     */
    public Map<String,String> getRequiredExportValues()
    {
        return this.requiredExportValues;
    }

    /**
     * Creates all depending administration objects for given this instance.
     * Only the depending {@link #properties} could be created.
     *
     * @return this data instance
     * @throws MatrixException if create failed
     * @see #properties
     */
    @SuppressWarnings("unchecked")
    public DATA createDependings()
        throws MatrixException
    {
        for (final PropertyDef prop : this.properties)  {
            if (prop.getTo() != null)  {
                prop.getTo().create();
            }
        }

        return (DATA) this;
    }

    /**
     * Appends the file header for the CI file.
     *
     * @param _cmd  string builder of the CI file content
     */
    protected void append4CIFileHeader(final StringBuilder _cmd)
    {
        _cmd.append("#\n")
            .append("# SYMBOLIC NAME:\n")
            .append("# ~~~~~~~~~~~~~~\n")
            .append("# ").append(this.getSymbolicName()).append("\n\n");
    }

    /**
     * Appends the defined {@link #getValues() values} to the TCL code
     * <code>_cmd</code> of the configuration item file.
     *
     * @param _cmd  string builder with the TCL commands of the configuration
     *              item file
     * @see #values
     */
    protected void append4CIFileValues(final StringBuilder _cmd)
    {
        // values
        for (final Map.Entry<String,String> entry : this.getValues().entrySet())  {
            _cmd.append(' ').append(entry.getKey()).append(" \"")
                .append(AbstractTest.convertTcl(entry.getValue()))
                .append('\"');
        }
        // check for add values
        final Set<String> needAdds = new HashSet<String>();
        this.evalAdds4CheckExport(needAdds);
        for (final String needAdd : needAdds)  {
            _cmd.append(" add ").append(needAdd);
        }
        // properties
        for (final PropertyDef property : this.getProperties())  {
            _cmd.append(" property \"").append(AbstractTest.convertTcl(property.getName())).append("\"");
            if (property.getTo() != null)  {
                _cmd.append(" to ").append(property.getTo().getCI().getMxType()).append(" \"")
                    .append(AbstractTest.convertTcl(property.getTo().getName())).append("\"");
                if (property.getTo().getCI() == AbstractTest.CI.UI_TABLE)  {
                    _cmd.append(" system");
                }
            }
            if (property.getValue() != null)  {
                _cmd.append(" value \"").append(AbstractTest.convertTcl(property.getValue())).append("\"");
            }
        }
    }

    /**
     * Appends the MQL commands to define all {@link #values} and
     * {@link #properties} within a create.
     *
     * @param _cmd  string builder used to append MQL commands
     * @throws MatrixException if used object could not be created
     * @see #values
     */
    protected void append4Create(final StringBuilder _cmd)
        throws MatrixException
    {
        // values
        for (final Map.Entry<String,String> entry : this.getValues().entrySet())  {
            _cmd.append(' ').append(entry.getKey()).append(" \"")
                .append(AbstractTest.convertMql(entry.getValue()))
                .append('\"');
        }
        // properties
        for (final PropertyDef property : this.properties)  {
            _cmd.append(" property \"").append(AbstractTest.convertMql(property.getName())).append("\"");
            if (property.getTo() != null)  {
                property.getTo().create();
                _cmd.append(" to ").append(property.getTo().getCI().getMxType()).append(" \"")
                    .append(AbstractTest.convertMql(property.getTo().getName())).append("\"");
                if (property.getTo().getCI() == AbstractTest.CI.UI_TABLE)  {
                    _cmd.append(" system");
                }
            }
            if (property.getValue() != null)  {
                _cmd.append(" value \"").append(AbstractTest.convertMql(property.getValue())).append("\"");
            }
        }
    }

    /**
     * Checks the export of this data piece if all values are correct defined.
     *
     * @param _exportParser     parsed export
     * @throws MatrixException if check failed
     */
    @Override()
    public void checkExport(final ExportParser _exportParser)
        throws MatrixException
    {
        super.checkExport(_exportParser);
        this.checkExportProperties(_exportParser);
        Assert.assertEquals(_exportParser.getSymbolicName(), this.getSymbolicName(), "check symbolic name");
        // check for defined values
        for (final Map.Entry<String,String> entry : this.getValues().entrySet())  {
            this.checkSingleValue(_exportParser, entry.getKey(), entry.getKey(), "\"" + AbstractTest.convertTcl(entry.getValue()) + "\"");
        }
        // check for all required values
        if (this.requiredExportValues != null)  {
            for (final String valueName : this.requiredExportValues.keySet())  {
                Assert.assertEquals(_exportParser.getLines("/mql/" + valueName + "/@value").size(),
                                    1,
                                    "required check that minimum and maximum one " + valueName + " is defined");
            }
        }
        // check for add values
        final Set<String> needAdds = new HashSet<String>();
        this.evalAdds4CheckExport(needAdds);
        final List<String> foundAdds = _exportParser.getLines("/mql/add/@value");
        Assert.assertEquals(
                foundAdds.size(),
                needAdds.size(),
                "all adds defined (found adds = " + foundAdds + "; need adds = " + needAdds + ")");
        for (final String foundAdd : foundAdds)  {
            Assert.assertTrue(needAdds.contains(foundAdd), "check that add '" + foundAdd + "' is defined (required are " + needAdds + ")");
        }

        // check hidden flag
        if (this.getCI() != null)  {
            final Set<String> main = new HashSet<String>(_exportParser.getLines("/mql/"));
            if ((this.isHidden() != null) && this.isHidden())  {
                Assert.assertTrue(
                        main.contains("hidden") || main.contains("hidden \\"),
                        "check that " + this.getCI().getMxType() + " '" + this.getName() + "' is hidden");
                Assert.assertTrue(
                        !main.contains("!hidden") && !main.contains("!hidden \\"),
                        "check that " + this.getCI().getMxType() + " '" + this.getName() + "' is hidden");
            } else  {
                Assert.assertTrue(
                        !main.contains("hidden") && !main.contains("hidden \\"),
                        "check that " + this.getCI().getMxType() + " '" + this.getName() + "' is hidden");
// not required... especially for UI elements..
//                Assert.assertTrue(
//                        main.contains("!hidden") || main.contains("!hidden \\"),
//                        "check that " + this.getCI().getMxType() + " '" + this.getName() + "' is hidden");
            }
        }
    }

    /**
     * Checks that all properties within the export file are correct defined
     * and equal to the defined properties of this CI file.
     *
     * @param _exportParser     parsed export
     */
    protected void checkExportProperties(final ExportParser _exportParser)
    {
        // only if this instance is a configuration item the check is done..
        if (this.getCI() != null)  {
            final Set<String> propDefs = new HashSet<String>();
            for (final ExportParser.Line rootLine : _exportParser.getRootLines())  {
                if (rootLine.getValue().startsWith("escape add property"))  {
                    final StringBuilder propDef = new StringBuilder().append("mql ").append(rootLine.getValue());
                    for (final ExportParser.Line childLine : rootLine.getChildren())  {
                        propDef.append(' ').append(childLine.getTag()).append(' ')
                               .append(childLine.getValue());
                    }
                    propDefs.add(propDef.toString());
                }
            }
            for (final PropertyDef prop : this.properties)  {
                final String propDefStr = prop.getCITCLString(this.getCI());
                Assert.assertTrue(
                        propDefs.contains(propDefStr),
                        "check that property is defined in ci file (have " + propDefStr + ", but found " + propDefs + ")");
                propDefs.remove(propDefStr);
            }

            Assert.assertEquals(propDefs.size(), 0, "check that not too much properties are defined (have " + propDefs + ")");
        }
    }

    /**
     * Evaluates all 'adds' in the configuration item file (e.g. add
     * setting, ...). Because for the abstract data no adds exists this method
     * is only a dummy.
     *
     * @param _needAdds     set with add strings used to append the adds
     */
    protected void evalAdds4CheckExport(final Set<String> _needAdds)
    {
    }
}
