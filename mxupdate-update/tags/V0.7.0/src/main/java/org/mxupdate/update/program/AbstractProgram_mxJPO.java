/*
 * Copyright 2008-2009 The MxUpdate Team
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

package org.mxupdate.update.program;

import java.io.File;
import java.util.Date;

import matrix.util.MatrixException;

import org.mxupdate.mapping.PropertyDef_mxJPO;
import org.mxupdate.mapping.TypeDef_mxJPO;
import org.mxupdate.update.AbstractObject_mxJPO;
import org.mxupdate.update.util.MqlUtil_mxJPO;
import org.mxupdate.update.util.ParameterCache_mxJPO;
import org.mxupdate.update.util.StringUtil_mxJPO;

/**
 *
 * @author The MxUpdate Team
 * @version $Id$
 */
public abstract class AbstractProgram_mxJPO
        extends AbstractObject_mxJPO
{
    /**
     * Defines the serialize version unique identifier.
     */
    private static final long serialVersionUID = -6353366924945315894L;

    /**
     *
     * @param _typeDef  type definition of the program
     * @param _mxName   MX name of the program object
     */
    protected AbstractProgram_mxJPO(final TypeDef_mxJPO _typeDef,
                                    final String _mxName)
    {
        super(_typeDef, _mxName);
    }

    /**
     * All basic properties are read and initialized. Basic attributes are:
     * <ul>
     * <li>name</li>
     * <li>author</li>
     * <li>application</li>
     * <li>installation date</li>
     * <li>installer</li>
     * <li>original name</li>
     * <li>version</li>
     * </ul>
     *
     * @param _paramCache   parameter cache
     * @throws MatrixException if the &quot;basic&quot; properties could not be
     *                         read from the program object
     */
    @Override
    protected void parse(final ParameterCache_mxJPO _paramCache)
            throws MatrixException
    {
        // set author depending on the properties
        this.setAuthor(this.getPropValue(_paramCache, PropertyDef_mxJPO.AUTHOR));
        // set application depending on the properties
        this.setApplication(this.getPropValue(_paramCache, PropertyDef_mxJPO.APPLICATION));
        // sets the installation date depending on the properties
        this.setInstallationDate(this.getPropValue(_paramCache, PropertyDef_mxJPO.INSTALLEDDATE));
        // sets the installer depending on the properties
        this.setInstaller(this.getPropValue(_paramCache, PropertyDef_mxJPO.INSTALLER));
        // sets the original name depending on the properties
        this.setOriginalName(this.getPropValue(_paramCache, PropertyDef_mxJPO.ORIGINALNAME));
        // sets the version depending on the properties
        this.setVersion(this.getPropValue(_paramCache, PropertyDef_mxJPO.VERSION));
    }

    /**
     * Deletes administration object from given type with given name.
     *
     * @param _paramCache   parameter cache
     * @throws Exception if delete failed
     */
    @Override
    public void delete(final ParameterCache_mxJPO _paramCache)
            throws Exception
    {
        final StringBuilder cmd = new StringBuilder()
                .append("delete ").append(this.getTypeDef().getMxAdminName())
                .append(" \"").append(this.getName()).append("\" ")
                .append(this.getTypeDef().getMxAdminSuffix());
        MqlUtil_mxJPO.execMql(_paramCache, cmd);
    }

    /**
     * Current program is updated. The update has following steps:
     * <ul>
     * <li>all not &quot;standard&quot; properties are removed</li>
     * <li>program / JPO is updated</li>
     * <li>&quot;standard&quot; properties are set (version, file date,
     *     installer, original name, application and author)</li>
     * <li>registration of the symbolic name</li>
     * </ul>
     *
     * @param _paramCache   parameter cache
     * @param _updateCode   update code (which is executed in front of)
     * @param _newVersion   new version of the program / JPO to set
     * @param _file         file to update (to get the file date)
     * @throws MatrixException if update of the program / JPO failed
     */
    protected void update(final ParameterCache_mxJPO _paramCache,
                          final CharSequence _updateCode,
                          final String _newVersion,
                          final File _file)
            throws MatrixException
    {
        // append statement to reset execute user
        final StringBuilder cmd = new StringBuilder()
                .append("mod ").append(this.getTypeDef().getMxAdminName()).append(" \"").append(this.getName())
                .append("\" execute user \"\";\n");

        this.appendResetProperties(_paramCache, cmd);

        // append update code
        cmd.append(_updateCode);

        // append standard properties
        this.appendProperties(_paramCache, cmd, _newVersion, _file);

        // read symbolic names
        this.readSymbolicNames(_paramCache);

        // append registration of symbolic names
        this.appendSymbolicNameRegistration(_paramCache,
                                            this.calcDefaultSymbolicName(_paramCache),
                                            cmd);

        // and execute alls
        MqlUtil_mxJPO.execMql(_paramCache, cmd);
    }

    /**
     * Appends the MQL code to reset the not standard properties.
     *
     * @param _paramCache   parameter cache
     * @param _cmd          string builder with the update code
     * @throws MatrixException if property information from this object could
     *                         not be fetched
     */
    protected void appendResetProperties(final ParameterCache_mxJPO _paramCache,
                                         final StringBuilder _cmd)
        throws MatrixException
    {
        // append MQL statements to reset properties
        final String prpStr = MqlUtil_mxJPO.execMql(_paramCache,
                                      new StringBuilder().append("escape print ")
                                              .append(this.getTypeDef().getMxAdminName()).append(" \"")
                                              .append(StringUtil_mxJPO.convertMql(this.getName()))
                                              .append("\" select property.name property.to dump ' @@@@@@'"));
        final String[] prpArr = prpStr.toString().split("(@@@@@@)");
        final int length = (prpArr.length + 1) / 2;
        for (int idxName = 0, idxTo = length; idxName < length; idxName++, idxTo++)  {
            final String name = prpArr[idxName].trim();
            final PropertyDef_mxJPO propDef = PropertyDef_mxJPO.getEnumByPropName(_paramCache, name);
            if (propDef == null)  {
// TODO: if to is defined, the remove must be specified the to ....
                final String to = (idxTo < length) ? prpArr[idxTo].trim() : "";
                _cmd.append("escape mod ").append(this.getTypeDef().getMxAdminName()).append(" \"")
                    .append(StringUtil_mxJPO.convertMql(this.getName()))
                    .append("\" remove property \"").append(name).append("\";\n");
            }
        }
    }

    /**
     * Appends the MQL code to set the &quot;standard&quot; properties to the
     * <code>_cmd</code> string builder.
     *
     * @param _paramCache   parameter cache
     * @param _cmd          string builder with the update code
     * @param _newVersion   new version of the program / JPO to set
     * @param _file         file to update (to get the file date)
     */
    protected void appendProperties(final ParameterCache_mxJPO _paramCache,
                                    final StringBuilder _cmd,
                                    final String _newVersion,
                                    final File _file)
    {
        // append update code
        _cmd.append("escape mod ").append(this.getTypeDef().getMxAdminName())
            .append(" \"").append(StringUtil_mxJPO.convertMql(this.getName())).append('\"');

        // define version property
        _cmd.append(" add property \"").append(PropertyDef_mxJPO.VERSION.getPropName(_paramCache)).append("\" ")
            .append("value \"").append(_newVersion != null ? _newVersion : "").append('\"');
        // define file date property
        _cmd.append(" add property \"").append(PropertyDef_mxJPO.FILEDATE.getPropName(_paramCache)).append("\" ")
            .append("value \"")
            .append(StringUtil_mxJPO.formatFileDate(_paramCache, new Date(_file.lastModified()))).append('\"');
        // is installed date property defined?
        if ((this.getInstallationDate() == null) || "".equals(this.getInstallationDate()))  {
            final String date = StringUtil_mxJPO.formatInstalledDate(_paramCache, new Date());
            _paramCache.logTrace("    - define installed date '" + date + "'");
            _cmd.append(" add property \"")
               .append(PropertyDef_mxJPO.INSTALLEDDATE.getPropName(_paramCache)).append("\" ")
               .append("value \"").append(date).append('\"');
        }
        // exists no installer property or installer property not equal?
        final String installer;
        if (_paramCache.contains(ParameterCache_mxJPO.KEY_INSTALLER))  {
            installer = _paramCache.getValueString(ParameterCache_mxJPO.KEY_INSTALLER);
        } else if ((this.getInstaller() == null) || "".equals(this.getInstaller()))  {
            installer = _paramCache.getValueString(ParameterCache_mxJPO.KEY_DEFAULTINSTALLER);
        } else  {
            installer = null;
        }
        if (installer != null)  {
            _paramCache.logTrace("    - define installer '" + installer + "'");
            _cmd.append(" add property \"").append(PropertyDef_mxJPO.INSTALLER.getPropName(_paramCache)).append("\" ")
               .append("value \"").append(installer).append('\"');
        }
        // is original name property defined?
        if ((this.getOriginalName() == null) && "".equals(this.getOriginalName()))  {
            _paramCache.logTrace("    - define original name '" + this.getName() + "'");
            _cmd.append(" add property \"")
                .append(PropertyDef_mxJPO.ORIGINALNAME.getPropName(_paramCache)).append("\" ")
                .append("value \"").append(this.getName()).append('\"');
        }
        // exists no application property or application property not equal?
        String appl = null;
        if (_paramCache.contains(ParameterCache_mxJPO.KEY_APPLICATION))  {
            appl = _paramCache.getValueString(ParameterCache_mxJPO.KEY_APPLICATION);
        }
        if ((appl == null) || "".equals(appl))  {
            appl = _paramCache.getValueString(ParameterCache_mxJPO.KEY_DEFAULTAPPLICATION);
        }
        if (appl == null)  {
            appl = "";
        }
        if (!appl.equals(this.getApplication()))  {
            _paramCache.logTrace("    - define application '" + appl + "'");
            _cmd.append(" add property \"").append(PropertyDef_mxJPO.APPLICATION.getPropName(_paramCache)).append("\" ")
               .append("value \"").append(appl).append('\"');
        }
        // exists no author property or author property not equal?
        final String author;
        if (_paramCache.contains(ParameterCache_mxJPO.KEY_AUTHOR))  {
            author = _paramCache.getValueString(ParameterCache_mxJPO.KEY_AUTHOR);
        } else if ((this.getAuthor() == null) || "".equals(this.getAuthor()))  {
            author = _paramCache.getValueString(ParameterCache_mxJPO.KEY_DEFAULTAUTHOR);
        } else  {
            author = null;
        }
        if (author != null)  {
            _paramCache.logTrace("    - define author '" + author + "'");
            _cmd.append(" add property \"").append(PropertyDef_mxJPO.AUTHOR.getPropName(_paramCache)).append("\" ")
               .append("value \"").append(author).append('\"');
        }
        _cmd.append(";\n");
    }
}
