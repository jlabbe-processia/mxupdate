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

package org.mxupdate.install;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import matrix.db.Context;
import matrix.util.MatrixException;
import matrix.util.Mime64;

import org.mxupdate.mapping.PropertyDef_mxJPO;
import org.mxupdate.mapping.TypeDef_mxJPO;
import org.mxupdate.update.AbstractObject_mxJPO;
import org.mxupdate.update.util.ParameterCache_mxJPO;
import org.mxupdate.update.util.StringUtil_mxJPO;
import org.mxupdate.util.MqlUtil_mxJPO;

/**
 * Installs and updates the data model needed for MxUpdate. The JPO class is
 * automatically called from the &quot;MxInstall.mql&quot; MQL installation
 * script.
 *
 * @author The MxUpdate Team
 * @version $Id$
 */
public class InstallDataModel_mxJPO
{
    /**
     * Key used to store the name of the program where all administration
     * objects must be registered with symbolic names. For an OOTB installation
     * the value is typically &quot;eServiceSchemaVariableMapping.tcl&quot;.
     *
     * @see #registerObject(ParameterCache_mxJPO, AbstractObject_mxJPO)
     */
    private static final String PARAM_SYMB_NAME_PROG = "RegisterSymbolicNames";

    /**
     * Name of the parameter defining the program name where applications must
     * be registered.
     *
     * @see #registerMxUpdate(ParameterCache_mxJPO, String, String)
     */
    private static final String PARAM_PROGAPPL = "RegisterApplicationProg";

    /**
     * Name of the parameter defining the application name.
     *
     * @see #mxMain(Context, String...)
     */
    private static final String PARAM_APPLNAME = "RegisterApplicationName";

    /**
     * Name of the parameter defining the author name.
     *
     * @see #mxMain(Context, String...)
     */
    private static final String PARAM_AUTHOR = "RegisterAuthorName";

    /**
     * Name of the parameter defining the installer name.
     *
     * @see #mxMain(Context, String...)
     */
    private static final String PARAM_INSTALLER = "RegisterInstallerName";

    /**
     * Name of the type definition for the trigger group.
     *
     * @see #installTriggerGroupPolicy(ParameterCache_mxJPO, String, String, String, String, String, String)
     * @see #installTriggerGroupRelation(ParameterCache_mxJPO, String, String, String, String, String, String)
     * @see #installTriggerGroupType(ParameterCache_mxJPO, File, String, String, String, String, String, String)
     */
    private static final String TYPEDEF_TRIGGER_GROUP = "TriggerGroup";
    /**
     * Name of the type definition for the trigger.
     *
     * @see #installTriggerGroupPolicy(ParameterCache_mxJPO, String, String, String, String, String, String)
     * @see #installTriggerGroupRelation(ParameterCache_mxJPO, String, String, String, String, String, String)
     */
    private static final String TYPEDEF_TRIGGER = "Trigger";

    /**
     * Name of the type definition for the policy.
     *
     * @see #installTriggerGroupPolicy(ParameterCache_mxJPO, String, String, String, String, String, String)
     */
    private static final String TYPEDEF_POLICY = "Policy";

    /**
     * Name of the type definition for the relationship.
     *
     * @see #installTriggerGroupRelation(ParameterCache_mxJPO, String, String, String, String, String, String)
     */
    private static final String TYPEDEF_RELATIONSHIP = "Relationship";

    /**
     * Name of the type definition for types.
     *
     * @see #installTriggerGroupType(ParameterCache_mxJPO, File, String, String, String, String, String, String)
     */
    private static final String TYPEDEF_TYPE = "Type";

    /**
     * Name of the type definition for JPO programs.
     *
     * @see #registerPrograms(ParameterCache_mxJPO)
     */
    private static final String TYPEDEF_JPO = "JPO";

    /**
     * Name of the type definition for MQL programs.
     *
     * @see #registerPrograms(ParameterCache_mxJPO)
     */
    private static final String TYPEDEF_MQL = "Program";

    /**
     * Name of the type definition for string attributes.
     *
     * @see #updateAttributes(ParameterCache_mxJPO, String, String, String, String, String, String)
     */
    private static final String TYPEDEF_ATTRIBUTE_STRING = "AttributeString";

    /**
     * MQL statement to list of MxUpdate Update programs for which the symbolic
     * names must be registered.
     *
     * @see #registerPrograms(ParameterCache_mxJPO)
     */
    private static final String LIST_MXUPDATE_PROGRAMS
            = "escape list program MxUpdate,org.mxupdate* select name isjavaprogram dump @";

    /**
     * Method used as entry from the MQL interface to install / update the data
     * model for MxUpdate. The data model is installed / updated in this order:
     * <ul>
     * <li>install trigger group type</li>
     * <li>install trigger group policy</li>
     * <li>install trigger group relationship</li>
     * <li>update attributes in
     *     {@link #updateAttributes(ParameterCache_mxJPO, String, String, String, String, String, String)}</li>
     * <li>update used business types by appending required MxUpdate attributes
     *     in {@link #updateBusTypes(ParameterCache_mxJPO)}</li>
     * <li>define plug-in properties in
     *     {@link #makePluginProperty(ParameterCache_mxJPO, File)}</li>
     * <li>register the MxUdpate Update programs with their symbolic names in
     *     {@link #registerPrograms(ParameterCache_mxJPO, String, String, String, String, String)}</li>
     * <li>register MxUpdate as application in
     *     {@link #registerMxUpdate(ParameterCache_mxJPO, String, String)}</li>
     * </ul>
     *
     * @param _context      MX context for this request
     * @param _args         first value defines the source installation path
     *                      (in development it is the resources sub directory),
     *                      second value the version of MxUpdate which must be
     *                      installed
     * @throws Exception if installation failed
     * @see #installTriggerGroupType(ParameterCache_mxJPO, File, String, String, String, String, String, String)
     * @see #installTriggerGroupPolicy(ParameterCache_mxJPO, String, String, String, String, String, String)
     * @see #installTriggerGroupRelation(ParameterCache_mxJPO, String, String, String, String, String, String)
     * @see #updateAttributes(ParameterCache_mxJPO, String, String, String, String, String, String)
     * @see #updateBusTypes(ParameterCache_mxJPO)
     * @see #registerPrograms(ParameterCache_mxJPO, String, String, String, String, String)
     * @see #registerMxUpdate(ParameterCache_mxJPO, String, String)
     * @see #makePluginProperty(ParameterCache_mxJPO, File)
     */
    public void mxMain(final Context _context,
                       final String... _args)
            throws Exception
    {
        final File path = new File(_args[0]);
        final String applVersion = _args[1];

        // initialize mapping
        final ParameterCache_mxJPO paramCache = new ParameterCache_mxJPO(_context, false);

        final String applName = paramCache.getValueString(InstallDataModel_mxJPO.PARAM_APPLNAME);
        final String authorName = paramCache.getValueString(InstallDataModel_mxJPO.PARAM_AUTHOR);
        final String installerName = paramCache.getValueString(InstallDataModel_mxJPO.PARAM_INSTALLER);
        final String fileDate = StringUtil_mxJPO.formatFileDate(paramCache, new Date());
        final String installedDate = StringUtil_mxJPO.formatInstalledDate(paramCache, new Date());

        this.installTriggerGroupType(paramCache, path,
                applName, applVersion,
                authorName, installerName,
                fileDate, installedDate);
        this.installTriggerGroupPolicy(paramCache,
                applName, applVersion,
                authorName, installerName,
                fileDate, installedDate);
        this.installTriggerGroupRelation(paramCache,
                applName, applVersion,
                authorName, installerName,
                fileDate, installedDate);
        this.updateAttributes(paramCache,
                applName, applVersion,
                authorName, installerName,
                fileDate, installedDate);
        this.updateBusTypes(paramCache);
        this.makePluginProperty(paramCache, path);
        this.registerPrograms(paramCache,
                applName, applVersion,
                authorName, installerName,
                installedDate);
        this.registerMxUpdate(paramCache, applName, applVersion);
    }

    /**
     * All programs evaluated with {@link #LIST_MXUPDATE_PROGRAMS} are checked
     * if they must be registered with a symbolic name and all other specific
     * properties are set. If they are not already registered, the registration
     * of the symbolic names of the programs are done. And also all other
     * properties are set only if they are not already set.
     *
     * @param _paramCache       parameter cache
     * @param _applName         used application name of the MxUpdate Update
     *                          deployment tool
     * @param _applVersion      Mx Update version
     * @param _authorName       used author name
     * @param _installerName    used installer name
     * @param _installedDate    used installed date
     * @throws Exception if registration of the symbolic names for all MxUpdate
     *                   Update programs failed
     * @see #LIST_MXUPDATE_PROGRAMS
     */
    protected void registerPrograms(final ParameterCache_mxJPO _paramCache,
                                    final String _applName,
                                    final String _applVersion,
                                    final String _authorName,
                                    final String _installerName,
                                    final String _installedDate)
        throws Exception
    {
        final TypeDef_mxJPO jpoTypeDef
                = _paramCache.getMapping().getTypeDef(InstallDataModel_mxJPO.TYPEDEF_JPO);
        final TypeDef_mxJPO mqlTypeDef
                = _paramCache.getMapping().getTypeDef(InstallDataModel_mxJPO.TYPEDEF_MQL);

        final String progs = MqlUtil_mxJPO.execMql(_paramCache.getContext(),
                                                   InstallDataModel_mxJPO.LIST_MXUPDATE_PROGRAMS);
        for (final String progLine : new TreeSet<String>(Arrays.asList(progs.split("\n"))))  {
            final String[] progLineArr = progLine.split("@");
            final String progName = progLineArr[0];
            // do we have a JPO?
            final AbstractObject_mxJPO prog;
            if ("true".equalsIgnoreCase(progLineArr[1]))  {
                prog = jpoTypeDef.newTypeInstance(progName);
            } else  {
                prog = mqlTypeDef.newTypeInstance(progName);
            }
            _paramCache.logInfo("check program '" + progName + "'");
            this.registerObject(_paramCache, prog);
            final StringBuilder cmd = new StringBuilder()
                    .append("escape mod program \"").append(StringUtil_mxJPO.convertMql(progName)).append("\" ");
            this.checkProperty(_paramCache, prog, PropertyDef_mxJPO.APPLICATION, _applName, cmd, false);
            this.checkProperty(_paramCache, prog, PropertyDef_mxJPO.AUTHOR, _authorName, cmd, false);
            this.checkProperty(_paramCache, prog, PropertyDef_mxJPO.INSTALLER, _installerName, cmd, false);
            this.checkProperty(_paramCache, prog, PropertyDef_mxJPO.VERSION, _applVersion, cmd, false);
            this.checkProperty(_paramCache, prog, PropertyDef_mxJPO.ORIGINALNAME, progName, cmd, false);
            this.checkProperty(_paramCache, prog, PropertyDef_mxJPO.INSTALLEDDATE, _installedDate, cmd, true);
            MqlUtil_mxJPO.execMql(_paramCache.getContext(), cmd);
        }
    }

    /**
     * Makes the registration of MxUpdate as application.
     *
     * @param _paramCache   parameter cache with MX context
     * @param _applName     used application name of the MxUpdate Update
     *                      deployment tool
     * @param _version      MxUpdate version
     * @throws MatrixException if registration of MxUpdate failed
     * @see #PARAM_PROGAPPL
     */
    protected void registerMxUpdate(final ParameterCache_mxJPO _paramCache,
                                    final String _applName,
                                    final String _version)
            throws MatrixException
    {
        final String progName = _paramCache.getValueString(InstallDataModel_mxJPO.PARAM_PROGAPPL);

        _paramCache.logInfo("register MxUpdate " + _version);
        MqlUtil_mxJPO.execMql(_paramCache.getContext(), new StringBuilder()
                .append("escape mod prog \"").append(StringUtil_mxJPO.convertMql(progName)).append("\" ")
                .append("add property \"appVersion").append(StringUtil_mxJPO.convertMql(_applName)).append("\" ")
                .append("value \"").append(StringUtil_mxJPO.convertMql(_version)).append("\""));
    }

    /**
     * The trigger group type is installed.
     *
     * @param _paramCache       parameter cache with MX context
     * @param _path             installation path (needed to fetch the icons)
     * @param _applName         used application name of the MxUpdate Update
     *                          deployment tool
     * @param _applVersion      Mx Update version
     * @param _authorName       used author name
     * @param _installerName    used installer name
     * @param _fileDate         used file date
     * @param _installedDate    used installed date
     * @throws Exception if the trigger group type could not be installed
     */
    protected void installTriggerGroupType(final ParameterCache_mxJPO _paramCache,
                                           final File _path,
                                           final String _applName,
                                           final String _applVersion,
                                           final String _authorName,
                                           final String _installerName,
                                           final String _fileDate,
                                           final String _installedDate)
            throws Exception
    {

        final TypeDef_mxJPO trigGrpTypeDef
                = _paramCache.getMapping().getTypeDef(InstallDataModel_mxJPO.TYPEDEF_TRIGGER_GROUP);
        _paramCache.logInfo("check type '" + trigGrpTypeDef.getMxBusType() + "'");
        final String installed = MqlUtil_mxJPO.execMql(_paramCache.getContext(), new StringBuilder()
                .append("escape list type \"")
                        .append(StringUtil_mxJPO.convertMql(trigGrpTypeDef.getMxBusType())).append('\"'));
        if ("".equals(installed))  {
            _paramCache.logDebug("    - create");
            MqlUtil_mxJPO.execMql(_paramCache.getContext(),
                    new StringBuilder()
                        .append("escape add type \"")
                        .append(StringUtil_mxJPO.convertMql(trigGrpTypeDef.getMxBusType()))
                        .append("\""));
        }
        _paramCache.logDebug("    - update description");
        final StringBuilder cmd = new StringBuilder()
                .append("escape mod type \"")
                .append(StringUtil_mxJPO.convertMql(trigGrpTypeDef.getMxBusType())).append("\" ")
                .append("description \"Type used to group trigger definitions.\" ");

        // update type icon
        final File file = new File(_path, trigGrpTypeDef.getIconPath());
        if (file.exists())  {
            _paramCache.logDebug("    - update type icon");
            cmd.append("icon \"").append(StringUtil_mxJPO.convertMql(file.toString())).append("\" ");
        }

        // append property settings
        final AbstractObject_mxJPO instance = _paramCache.getMapping()
                                            .getTypeDef(InstallDataModel_mxJPO.TYPEDEF_TYPE)
                                            .newTypeInstance(trigGrpTypeDef.getMxBusType());
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.APPLICATION, _applName, cmd, false);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.AUTHOR, _authorName, cmd, false);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.INSTALLER, _installerName, cmd, false);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.VERSION, _applVersion, cmd, false);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.ORIGINALNAME,
                           trigGrpTypeDef.getMxBusType(), cmd, false);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.FILEDATE, _fileDate, cmd, true);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.INSTALLEDDATE, _installedDate, cmd, true);
        MqlUtil_mxJPO.execMql(_paramCache.getContext(), cmd);

        this.registerObject(_paramCache, instance);
    }


    /**
     * The trigger group policy is installed. The trigger group policy is a
     * copy of the trigger policy without the state 'Inactive' but with same
     * defined access.
     *
     * @param _paramCache       parameter cache with MX context
     * @param _applName         used application name of the MxUpdate Update
     *                          deployment tool
     * @param _applVersion      Mx Update version
     * @param _authorName       used author name
     * @param _installerName    used installer name
     * @param _fileDate         used file date
     * @param _installedDate    used installed date
     * @throws Exception if the trigger group relationship could not be
     *                   installed
     */
    protected void installTriggerGroupPolicy(final ParameterCache_mxJPO _paramCache,
                                             final String _applName,
                                             final String _applVersion,
                                             final String _authorName,
                                             final String _installerName,
                                             final String _fileDate,
                                             final String _installedDate)
            throws Exception
    {
        final TypeDef_mxJPO trigTypeDef
                = _paramCache.getMapping().getTypeDef(InstallDataModel_mxJPO.TYPEDEF_TRIGGER);
        final TypeDef_mxJPO trigGrpTypeDef
                = _paramCache.getMapping().getTypeDef(InstallDataModel_mxJPO.TYPEDEF_TRIGGER_GROUP);
        final String polName = trigGrpTypeDef.getMxBusPolicy();

        _paramCache.logInfo("check policy '" + polName + "'");
        final String installed = MqlUtil_mxJPO.execMql(_paramCache.getContext(), new StringBuilder()
                .append("escape list policy \"").append(StringUtil_mxJPO.convertMql(polName)).append('\"'));
        if ("".equals(installed))  {
            _paramCache.logDebug("    - create");
            MqlUtil_mxJPO.execMql(_paramCache.getContext(),
                    new StringBuilder()
                        .append("escape copy policy \"")
                        .append(StringUtil_mxJPO.convertMql(trigTypeDef.getMxBusPolicy()))
                        .append("\" \"")
                        .append(StringUtil_mxJPO.convertMql(polName))
                        .append("\" remove state Inactive"));
        }

        _paramCache.logDebug("    - update description");
        _paramCache.logDebug("    - update types");
        final StringBuilder cmd = new StringBuilder()
                .append("escape mod policy \"")
                .append(StringUtil_mxJPO.convertMql(polName)).append("\" ")
                .append("description \"Policy used to group trigger definitions.\" ")
                .append("remove type all add type \"")
                    .append(StringUtil_mxJPO.convertMql(trigTypeDef.getMxBusType())).append("\" ");

        // append property settings
        final AbstractObject_mxJPO instance = _paramCache.getMapping()
                                            .getTypeDef(InstallDataModel_mxJPO.TYPEDEF_POLICY)
                                            .newTypeInstance(polName);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.APPLICATION, _applName, cmd, false);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.AUTHOR, _authorName, cmd, false);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.INSTALLER, _installerName, cmd, false);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.VERSION, _applVersion, cmd, false);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.ORIGINALNAME, polName, cmd, false);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.FILEDATE, _fileDate, cmd, true);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.INSTALLEDDATE, _installedDate, cmd, true);
        MqlUtil_mxJPO.execMql(_paramCache.getContext(), cmd);

        this.registerObject(_paramCache, instance);
    }

    /**
     * The trigger group relationship is installed.
     *
     * @param _paramCache       parameter cache with MX context
     * @param _applName         used application name of the MxUpdate Update
     *                          deployment tool
     * @param _applVersion      Mx Update version
     * @param _authorName       used author name
     * @param _installerName    used installer name
     * @param _fileDate         used file date
     * @param _installedDate    used installed date
     * @throws Exception if the trigger group relationship could not be
     *                   installed
     */
    protected void installTriggerGroupRelation(final ParameterCache_mxJPO _paramCache,
                                               final String _applName,
                                               final String _applVersion,
                                               final String _authorName,
                                               final String _installerName,
                                               final String _fileDate,
                                               final String _installedDate)
            throws Exception
    {
        final TypeDef_mxJPO trigTypeDef
                = _paramCache.getMapping().getTypeDef(InstallDataModel_mxJPO.TYPEDEF_TRIGGER);
        final TypeDef_mxJPO trigGrpTypeDef
                = _paramCache.getMapping().getTypeDef(InstallDataModel_mxJPO.TYPEDEF_TRIGGER_GROUP);
        final String relName = trigGrpTypeDef.getMxBusRelsBoth().iterator().next();

        _paramCache.logInfo("check relationship '" + relName + "'");
        final String installed = MqlUtil_mxJPO.execMql(_paramCache.getContext(), new StringBuilder()
                .append("escape list relationship \"").append(StringUtil_mxJPO.convertMql(relName)).append('\"'));
        if ("".equals(installed))  {
            _paramCache.logDebug("    - create");
            MqlUtil_mxJPO.execMql(_paramCache.getContext(),
                    new StringBuilder()
                        .append("escape add relationship \"")
                        .append(StringUtil_mxJPO.convertMql(relName))
                        .append("\""));
        }
        _paramCache.logDebug("    - update description");
        _paramCache.logDebug("    - update from / to types");
        _paramCache.logDebug("    - update prevent duplicates");
        final StringBuilder cmd = new StringBuilder()
                .append("escape mod relationship \"")
                .append(StringUtil_mxJPO.convertMql(relName)).append("\" ")
                .append("preventduplicates ")
                .append("description \"Relationship used to group trigger definitions.\" ")
                .append("from remove type all add type \"")
                        .append(StringUtil_mxJPO.convertMql(trigGrpTypeDef.getMxBusType())).append("\" ")
                .append("to remove type all add type \"")
                        .append(StringUtil_mxJPO.convertMql(trigTypeDef.getMxBusType())).append("\" ")
                        .append(" add type \"")
                        .append(StringUtil_mxJPO.convertMql(trigGrpTypeDef.getMxBusType())).append("\" ");

        // append property settings
        final AbstractObject_mxJPO instance = _paramCache.getMapping()
                                            .getTypeDef(InstallDataModel_mxJPO.TYPEDEF_RELATIONSHIP)
                                            .newTypeInstance(relName);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.APPLICATION, _applName, cmd, false);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.AUTHOR, _authorName, cmd, false);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.INSTALLER, _installerName, cmd, false);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.VERSION, _applVersion, cmd, false);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.ORIGINALNAME, relName, cmd, false);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.FILEDATE, _fileDate, cmd, true);
        this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.INSTALLEDDATE, _installedDate, cmd, true);
        MqlUtil_mxJPO.execMql(_paramCache.getContext(), cmd);

        this.registerObject(_paramCache, instance);
    }

    /**
     * Creates / updates all needed attributes used as MxUpdate properties.
     *
     * @param _paramCache       parameter cache
     * @param _applName         used application name of the MxUpdate Update
     *                          deployment tool
     * @param _applVersion      new MxUpdate version
     * @param _authorName       used author name
     * @param _installerName    used installer name
     * @param _fileDate         used file date
     * @param _installedDate    used installed date
     * @throws Exception if update of attribute failed
     */
    protected void updateAttributes(final ParameterCache_mxJPO _paramCache,
                                    final String _applName,
                                    final String _applVersion,
                                    final String _authorName,
                                    final String _installerName,
                                    final String _fileDate,
                                    final String _installedDate)
            throws Exception
    {
        for (final PropertyDef_mxJPO propDef : PropertyDef_mxJPO.values())  {
            if ((propDef.getAttrName(_paramCache) != null) && !"".equals(propDef.getAttrName(_paramCache)))  {
                _paramCache.logInfo("check attribute '" + propDef.getAttrName(_paramCache) + "'");

                final String exists = MqlUtil_mxJPO.execMql(_paramCache.getContext(),
                        new StringBuilder().append("list attribute '")
                                           .append(propDef.getAttrName(_paramCache))
                                           .append('\''));
                if ("".equals(exists))  {
                    _paramCache.logDebug("    - create");
                    MqlUtil_mxJPO.execMql(_paramCache.getContext(),
                            new StringBuilder()
                                .append("escape add attribute \"")
                                .append(StringUtil_mxJPO.convertMql(propDef.getAttrName(_paramCache)))
                                .append("\" type string;"));
                }

                final StringBuilder cmd = new StringBuilder()
                    .append("escape mod attribute \"")
                    .append(StringUtil_mxJPO.convertMql(propDef.getAttrName(_paramCache))).append("\" ");

                final AbstractObject_mxJPO instance
                        = _paramCache.getMapping()
                                     .getTypeDef(InstallDataModel_mxJPO.TYPEDEF_ATTRIBUTE_STRING)
                                     .newTypeInstance(propDef.getAttrName(_paramCache));

                // check for correct property entries
                this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.APPLICATION, _applName, cmd, false);
                this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.AUTHOR, _authorName, cmd, false);
                this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.INSTALLER, _installerName, cmd, false);
                this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.VERSION, _applVersion, cmd, false);
                this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.ORIGINALNAME,
                                   propDef.getAttrName(_paramCache), cmd, false);
                this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.FILEDATE, _fileDate, cmd, true);
                this.checkProperty(_paramCache, instance, PropertyDef_mxJPO.INSTALLEDDATE, _installedDate, cmd, true);

                MqlUtil_mxJPO.execMql(_paramCache.getContext(), cmd);

                this.registerObject(_paramCache, instance);
            }
        }
    }

    /**
     * Updates all business type for which no defined administration type
     * exists. This includes:
     * <ul>
     * <li>append all required MxUpdate administrative attributes to business
     *     types</li>
     * </ul>
     *
     * @param _paramCache   parameter cache with MX context
     * @throws Exception if the update of the business types failed
     */
    protected void updateBusTypes(final ParameterCache_mxJPO _paramCache)
            throws Exception
    {
        for (final TypeDef_mxJPO typeDef : _paramCache.getMapping().getAllTypeDefs())  {
            if ((typeDef.getMxBusType() != null) && !"".equals(typeDef.getMxBusType())
                    && (typeDef.getMxAdminName() == null)
                    && (!typeDef.isBusCheckExists() || typeDef.existsBusType(_paramCache.getContext())))  {
                _paramCache.logInfo("check type "+typeDef.getMxBusType());
                for (final PropertyDef_mxJPO propDef : PropertyDef_mxJPO.values())  {
                    if ((propDef.getAttrName(_paramCache) != null) && !"".equals(propDef.getAttrName(_paramCache)))  {
                        final StringBuilder cmd = new StringBuilder()
                                .append("print type \"").append(typeDef.getMxBusType())
                                .append("\" select attribute[").append(propDef.getAttrName(_paramCache))
                                .append("] dump");
                        if ("false".equalsIgnoreCase(MqlUtil_mxJPO.execMql(_paramCache.getContext(), cmd)))  {
                            _paramCache.logDebug("    - add missing attribute '"
                                    + propDef.getAttrName(_paramCache) + "'");
                            MqlUtil_mxJPO.execMql(_paramCache.getContext(), new StringBuilder()
                                    .append("mod type \"").append(typeDef.getMxBusType())
                                    .append("\" add attribute \"").append(propDef.getAttrName(_paramCache))
                                    .append('\"'));
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates and stores the properties for the plugin. A property entry is
     * created for each type definition of {@link TypeDef_mxJPO} for which an
     * icon (path) is defined. The format of the properties is defined in
     * {@link org.mxupdate.plugin.GetProperties_mxJPO}.
     *
     * @param _paramCache       parameter cache
     * @param _sourcePath       reference to the source path
     * @throws IOException      if the file of icon not exists or could not
     *                          opened
     * @throws MatrixException  if the update of the plugin properties program
     *                          failed
     */
    protected void makePluginProperty(final ParameterCache_mxJPO _paramCache,
                                      final File _sourcePath)
            throws IOException, MatrixException
    {
        // prepare properties as set
        final Set<String> props = new TreeSet<String>();
        for (final TypeDef_mxJPO typeDef : _paramCache.getMapping().getAllTypeDefs())  {
            if (typeDef.getIconPath() != null)  {

                final File file = new File(_sourcePath, typeDef.getIconPath());
                final String icon;
                final InputStream in = new FileInputStream(file);
                try  {
                    final byte[] bin = new byte[in.available()];
                    in.read(bin);
                    icon = Mime64.encode(bin);
                } finally  {
                    in.close();
                }

                props.add(new StringBuilder()
                            .append(typeDef.getName())
                            .append(".FilePrefix = ")
                            .append(typeDef.getFilePrefix() == null ? "" : typeDef.getFilePrefix())
                            .append('\n')
                            .append(typeDef.getName())
                            .append(".FileSuffix = ")
                            .append(typeDef.getFileSuffix() == null ? "" : typeDef.getFileSuffix())
                            .append('\n')
                            .append(typeDef.getName())
                            .append(".Icon = ")
                            .append(icon).toString());
            }
        }

        // make string of properties set
        final StringBuilder propString = new StringBuilder();
        for (final String prop : props)  {
            propString.append(prop).append('\n');
        }

        // write properties
        final String installed = MqlUtil_mxJPO.execMql(_paramCache.getContext(),
                                                       "list prog 'org.mxupdate.plugin.plugin.properties'");
        if ("".equals(installed))  {
            MqlUtil_mxJPO.execMql(_paramCache.getContext(), "escape add prog 'org.mxupdate.plugin.plugin.properties'");
        }
        MqlUtil_mxJPO.execMql(_paramCache.getContext(), new StringBuilder()
                .append("escape mod prog 'org.mxupdate.plugin.plugin.properties' code \"")
                .append(StringUtil_mxJPO.convertMql(propString)).append("\""));
    }

    /**
     * Checks a property value and updates the property if not defined (if
     * parameter <code>_onlyIfNotDefined</code> is <i>true</i>) or not equal to
     * new value (if parameter <code>_onlyIfNotDefined</code> is <i>false</i>).
     *
     * @param _paramCache       parameter cache
     * @param _instance         instance used to extract current property value
     * @param _propDef          property definition
     * @param _newValue         new property value
     * @param _cmd              string builder used to append MQL code
     * @param _onlyIfNotDefined new property value is only defined if currently
     *                          no value is defined
     * @throws MatrixException if current value from the instance object could
     *                         not be evaluated
     */
    protected void checkProperty(final ParameterCache_mxJPO _paramCache,
                                 final AbstractObject_mxJPO _instance,
                                 final PropertyDef_mxJPO _propDef,
                                 final String _newValue,
                                 final StringBuilder _cmd,
                                 final boolean _onlyIfNotDefined)
            throws MatrixException
    {
        final String current = _instance.getPropValue(_paramCache, _propDef);
        if ((!_newValue.equals(current) && !_onlyIfNotDefined)
            || (((current == null) || "".equals(current)) && _onlyIfNotDefined))  {
            _paramCache.logDebug("    - define " + _propDef + " '" + _newValue + "'");
            _cmd.append("add property \"")
                .append(StringUtil_mxJPO.convertMql(_propDef.getPropName(_paramCache)))
                .append("\" value \"").append(StringUtil_mxJPO.convertMql(_newValue)).append("\" ");
        }
    }

    /**
     * Register symbolic name for given administration object
     * <code>_instance</code>.
     *
     * @param _paramCache   parameter cache with the MX context
     * @param _instance     instance which must be registered
     * @throws Exception if the registration of the symbolic name failed
     */
    protected void registerObject(final ParameterCache_mxJPO _paramCache,
                                  final AbstractObject_mxJPO _instance)
            throws Exception
    {
        final String newSymbName = _instance.getTypeDef().getMxAdminName() + "_"
                                    + _instance.getName().replaceAll(" ", "");
        final Set<String> symbolicNames = new HashSet<String>();

        final String symbProg = _paramCache.getValueString(InstallDataModel_mxJPO.PARAM_SYMB_NAME_PROG);

        // reads symbolic names of the administration objects
        final String symbProgIdxOf = new StringBuilder().append(" on program ").append(symbProg).append(' ').toString();
        final StringBuilder cmd = new StringBuilder()
                .append("escape list property on program \"")
                            .append(StringUtil_mxJPO.convertMql(symbProg)).append("\" to ")
                    .append(_instance.getTypeDef().getMxAdminName())
                    .append(" \"").append(_instance.getName()).append("\" ")
                    .append(_instance.getTypeDef().getMxAdminSuffix());
        for (final String symbName : MqlUtil_mxJPO.execMql(_paramCache.getContext(), cmd).split("\n"))  {
            if (!"".equals(symbName))  {
                symbolicNames.add(symbName.substring(0, symbName.indexOf(symbProgIdxOf)));
            }
        }
        // check symbolic names
        final StringBuilder update = new StringBuilder();
        if (!symbolicNames.contains(newSymbName))  {
            _paramCache.logDebug("    - register symbolic name '" + newSymbName + "'");
            update.append("escape add property \"").append(StringUtil_mxJPO.convertMql(newSymbName)).append("\" ")
                    .append(" on program \"").append(StringUtil_mxJPO.convertMql(symbProg)).append("\" to ")
                    .append(_instance.getTypeDef().getMxAdminName())
                    .append(" \"").append(StringUtil_mxJPO.convertMql(_instance.getName())).append("\" ")
                    .append(_instance.getTypeDef().getMxAdminSuffix())
                    .append(";\n");
        }
        for (final String exSymbName : symbolicNames)  {
            if (!newSymbName.equals(exSymbName))  {
                _paramCache.logDebug("    - remove symbolic name '" + exSymbName + "'");
                update.append("escape delete property \"")
                                .append(StringUtil_mxJPO.convertMql(exSymbName)).append("\" ")
                        .append(" on program \"").append(StringUtil_mxJPO.convertMql(symbProg)).append("\" to ")
                        .append(_instance.getTypeDef().getMxAdminName())
                        .append(" \"").append(StringUtil_mxJPO.convertMql(_instance.getName())).append("\" ")
                        .append(_instance.getTypeDef().getMxAdminSuffix())
                        .append(";\n");
            }
        }

        MqlUtil_mxJPO.execMql(_paramCache.getContext(), update);
    }
}
