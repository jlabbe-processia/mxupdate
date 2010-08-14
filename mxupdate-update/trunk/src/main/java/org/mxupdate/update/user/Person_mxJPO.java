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

package org.mxupdate.update.user;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import matrix.db.BusinessObject;
import matrix.db.BusinessObjectWithSelect;
import matrix.db.Context;
import matrix.util.MatrixException;
import matrix.util.StringList;

import org.mxupdate.mapping.PropertyDef_mxJPO;
import org.mxupdate.mapping.TypeDef_mxJPO;
import org.mxupdate.update.AbstractObject_mxJPO;
import org.mxupdate.update.BusObject_mxJPO;
import org.mxupdate.update.util.MqlUtil_mxJPO;
import org.mxupdate.update.util.ParameterCache_mxJPO;
import org.mxupdate.update.util.StringUtil_mxJPO;
import org.xml.sax.SAXException;

/**
 *
 * @author The MxUpdate Team
 * @version $Id$
 */
public class Person_mxJPO
    extends AbstractObject_mxJPO
{
    /**
     * TCL procedure to update the state of a person business object.
     *
     * @see PersonAdmin#update(ParameterCache_mxJPO, CharSequence, CharSequence, CharSequence, Map, File)
     * @see PersonBus#write(ParameterCache_mxJPO, Appendable)
     */
    private static final String TCL_SET_STATE
            = "proc setState {_newState}  {\n"
                + "global OBJECTID\n"
                + "set sCurrent [mql print bus ${OBJECTID} select current dump]\n"
                + "if {\"${_newState}\" != \"${sCurrent}\"}  {\n"
                    + "set lsStates [split [mql print bus ${OBJECTID} select policy.state dump '\\n'] '\\n']\n"
                    + "if {[lsearch ${lsStates} \"${sCurrent}\"] < [lsearch ${lsStates} \"${_newState}\"]}  {\n"
                        + "mql promote bus ${OBJECTID}\n"
                        + "logDebug \"    - activate business object\"\n"
                    + "} else  {\n"
                        + "mql demote bus ${OBJECTID}\n"
                        + "logDebug \"    - deactivate business object\"\n"
                    + "}\n"
                + "}\n"
            + "}\n";

    /**
     * Dummy procedure with logging information that the state update is
     * ignored.
     *
     * @see PersonAdmin#update(ParameterCache_mxJPO, CharSequence, CharSequence, CharSequence, Map, File)
     */
    private static final String TCL_SET_STATE_DUMMY
            = "proc setState {_newState}  {\n"
                + "logDebug \"    - ignoring update of state '${_newState}'\""
            + "}\n";

    /**
     * Defines the parameter for the match of persons for which states are not
     * handled (neither exported nor updated).
     *
     * @see PersonAdmin#update(ParameterCache_mxJPO, CharSequence, CharSequence, CharSequence, Map, File)
     * @see PersonBus#write(ParameterCache_mxJPO, Appendable)
     */
    private static final String PARAM_IGNORE_STATE = "UserPersonIgnoreState";

    /**
     * Administration person instance (used to parse the admin part of a
     * person).
     */
    private final PersonAdmin personAdmin;

    /**
     * Business object person instance (used to parse the business object part
     * of a person).
     */
    private final PersonBus personBus;

    /**
     * Constructor used to initialize the type definition enumeration.
     *
     * @param _typeDef  defines the related type definition enumeration
     * @param _mxName   MX name of the person object
     */
    public Person_mxJPO(final TypeDef_mxJPO _typeDef,
                        final String _mxName)
    {
        super(_typeDef, _mxName);
        this.personAdmin = new PersonAdmin(_typeDef, _mxName);
        this.personBus = new PersonBus(_typeDef, _mxName);
    }

    /**
     * Symbolic names from the {@link #personAdmin} are returned.
     *
     * @return symbolic names from the administration part of the person
     */
    @Override()
    protected Set<String> getSymbolicNames()
    {
        return this.personAdmin.getSymbolicNames();
    }

    /**
     * The list of all person names are evaluated with the help of the business
     * person objects. From the return values of the query for all business
     * person objects, only the business object name is returned in the set
     * (because the revision of the person business object is always a
     * &quot;-&quot;).
     *
     * @param _paramCache   parameter cache
     * @return set of person names
     * @throws MatrixException if the query for persons failed
     * @see #personBus
     */
    @Override()
    public Set<String> getMxNames(final ParameterCache_mxJPO _paramCache)
        throws MatrixException
    {
        final Set<String> persons = this.personBus.getMxNames(_paramCache);
        final Set<String> ret = new TreeSet<String>();
        for (final String busName : persons)  {
            ret.add(busName.split(BusObject_mxJPO.SPLIT_NAME)[0]);
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     * <p>The properties are handled on the administration part of the person.
     * So the property values are evaluated from {@link #personAdmin}.</p>
     */
    @Override()
    public String getPropValue(final ParameterCache_mxJPO _paramCache,
                               final PropertyDef_mxJPO _prop)
        throws MatrixException
    {
        return this.personAdmin.getPropValue(_paramCache, _prop);
    }

    /**
     * If the person must be parsed, first the admin object of the person is
     * parsed and then the related person business object.
     *
     * @param _paramCache   parameter cache
     * @see #personAdmin
     * @see #personBus
     */
    @Override()
    protected void parse(final ParameterCache_mxJPO _paramCache)
        throws MatrixException, SAXException, IOException
    {
        this.personAdmin.parse(_paramCache);
        this.personBus.parse(_paramCache);
    }

    /**
     * Deletes the person business object and person administration object.
     *
     * @param _paramCache   parameter cache
     * @throws Exception if delete of person failed
     */
    @Override()
    public void delete(final ParameterCache_mxJPO _paramCache)
        throws Exception
    {
        this.personBus.delete(_paramCache);
        this.personAdmin.delete(_paramCache);
    }

    /**
     * Creates the person administration object and person business object.
     *
     * @param _paramCache   parameter cache
     * @throws Exception if create of person failed
     */
    @Override()
    public void create(final ParameterCache_mxJPO _paramCache)
        throws Exception
    {
        this.personAdmin.create(_paramCache);
        this.personBus.create(_paramCache);
    }

    @Override()
    public void update(final ParameterCache_mxJPO _paramCache,
                       final File _file,
                       final String _newVersion)
        throws Exception
    {
        this.personBus.parse(_paramCache);
        this.personAdmin.update(_paramCache, _file, _newVersion);
    }

    @Override()
    protected void write(final ParameterCache_mxJPO _paramCache,
                         final Appendable _out)
        throws IOException
    {
        this.personAdmin.write(_paramCache, _out);
        _out.append('\n');
        this.personBus.write(_paramCache, _out);
        this.personAdmin.writeEnd(_paramCache, _out);
    }

    /**
     * Handles the administration part of the person. The class is used that
     * some methods could be called from this person class.
     */
    private final class PersonAdmin
        extends PersonAdmin_mxJPO
    {
        /**
         * Constructor used to initialize the administration object instance
         * for persons.
         *
         * @param _typeDef  related type definition for the person
         * @param _mxName   MX name of the person object
         */
        private PersonAdmin(final TypeDef_mxJPO _typeDef,
                            final String _mxName)
        {
            super(_typeDef, _mxName);
        }

        /**
         * Because the original method
         * {@link PersonAdmin_mxJPO#getSymbolicNames()}
         * is protected, but called from
         * {@link Person_mxJPO#getSymbolicNames()}, the original
         * method must be overwritten and only called.
         *
         * @return set of symbolic names for the administration part of the
         *         person
         */
        @Override()
        protected Set<String> getSymbolicNames()
        {
            return super.getSymbolicNames();
        }

        /**
         * Because the original method
         * {@link PersonAdmin_mxJPO#parse(ParameterCache_mxJPO)}
         * is protected, but called from
         * {@link Person_mxJPO#parse(ParameterCache_mxJPO)}, the original
         * method must be overwritten and only called. So the original method
         * could be used to parse the business administration part of the
         * person.
         *
         * @param _paramCache   parameter cache
         */
        @Override()
        protected void parse(final ParameterCache_mxJPO _paramCache)
            throws MatrixException, SAXException, IOException
        {
            super.parse(_paramCache);
        }

        /**
         *
         * @param _paramCache   parameter cache
         * @param _out          writer instance
         * @throws IOException if the TCL update code for the person could not
         *                     be written
         */
        @Override()
        public void write(final ParameterCache_mxJPO _paramCache,
                          final Appendable _out)
            throws IOException
        {
            this.writeHeader(_paramCache, _out);
            _out.append("mql mod ")
                .append(this.getTypeDef().getMxAdminName())
                .append(" \"${NAME}\"");
            if (!"".equals(this.getTypeDef().getMxAdminSuffix()))  {
                _out.append(" ").append(this.getTypeDef().getMxAdminSuffix());
            }
            _out.append(" \\\n    description \"").append(StringUtil_mxJPO.convertTcl(this.getDescription())).append("\"");
            this.writeObject(_paramCache, _out);
            this.writeProperties(_paramCache, _out);
        }

        /**
         * The method overwrites the original method to
         * <ul>
         * <li>reset the description</li>
         * <li>set the version and author attribute</li>
         * <li>reset all not ignored attributes</li>
         * <li>define the TCL variable &quot;OBJECTID&quot; with the object id of
         *     the represented business object</li>
         * </ul>
         * The original method of the super class if called surrounded with a
         * history off, because if the update itself is done the modified basic
         * attribute and the version attribute of the business object is updated.
         * <br/>
         * The new generated MQL code is set in the front of the already defined
         * MQL code in <code>_preMQLCode</code> and appended to the MQL statements
         * in <code>_postMQLCode</code>.
         *
         * @param _paramCache       parameter cache
         * @param _preMQLCode       MQL statements which must be called before the
         *                          TCL code is executed
         * @param _postMQLCode      MQL statements which must be called after the
         *                          TCL code is executed
         * @param _preTCLCode       TCL code which is defined before the source
         *                          file is sourced
         * @param _tclVariables     map of all TCL variables where the key is the
         *                          name and the value is value of the TCL variable
         *                          (the value is automatically converted to TCL
         *                          syntax!)
         * @param _sourceFile       souce file with the TCL code to update
         * @throws Exception if the update from derived class failed
         */
        @Override()
        protected void update(final ParameterCache_mxJPO _paramCache,
                              final CharSequence _preMQLCode,
                              final CharSequence _postMQLCode,
                              final CharSequence _preTCLCode,
                              final Map<String,String> _tclVariables,
                              final File _sourceFile)
            throws Exception
        {
            final StringBuilder preTCLCode = new StringBuilder()
                    .append(_preTCLCode)
                    .append('\n');

            // append TCL set state if not ignored (otherwise dummy TCL proc)
            final Collection<String> matchStates = _paramCache.getValueList(Person_mxJPO.PARAM_IGNORE_STATE);
            if ((matchStates == null) || !StringUtil_mxJPO.match(this.getName(), matchStates))  {
                preTCLCode.append(Person_mxJPO.TCL_SET_STATE);
            } else  {
                preTCLCode.append(Person_mxJPO.TCL_SET_STATE_DUMMY);
            }

            final StringBuilder preMQLCode = new StringBuilder();

            // post update MQL statements
            final StringBuilder postMQLCode = new StringBuilder()
                    .append(_postMQLCode);

            final Map<String,String> tclVariables = new HashMap<String,String>();
            Person_mxJPO.this.personBus.prepareUpdate(_paramCache.getContext(), preMQLCode, postMQLCode, tclVariables);
            tclVariables.putAll(_tclVariables);

            // update must be done with history off (because not required...)
            try  {
                MqlUtil_mxJPO.setHistoryOff(_paramCache);
                super.update(_paramCache, preMQLCode, postMQLCode, preTCLCode, tclVariables, _sourceFile);
            } finally  {
                MqlUtil_mxJPO.setHistoryOn(_paramCache);
            }
        }
    }

    /**
     * Handles the business object part of a person. Class is also needed so
     * that protected methods could be called from this class.
     */
    private static final class PersonBus
        extends BusObject_mxJPO
    {
        /**
         * Current state of the business object person.
         */
        private String status;

        /**
         * Person is employee of company.
         */
        private String employeeOf;

        /**
         * Person is member of company.
         */
        private String memberOf;

        /**
         * Person has member access of company.
         */
        private String memberAccess;

        /**
         * Set of all member roles.
         */
        private final Set<String> memberRoles = new TreeSet<String>();

        /**
         * Set of all companies for which the person is company representative.
         */
        private final Set<String> representativeOf = new TreeSet<String>();

        /**
         * Constructor used to initialize the business object instance for
         * persons.
         *
         * @param _typeDef  related type definition for the person
         * @param _mxName   MX name of the person object
         */
        private PersonBus(final TypeDef_mxJPO _typeDef,
                          final String _mxName)
        {
            super(_typeDef,
                 new StringBuilder().append(_mxName)
                                    .append(BusObject_mxJPO.SPLIT_NAME)
                                    .append('-').toString());
        }

        /**
         * Parsed the business object of the person.
         *
         * @param _paramCache   parameter cache
         */
        @Override()
        protected void parse(final ParameterCache_mxJPO _paramCache)
            throws MatrixException
        {
            super.parse(_paramCache);

            // exists a business object?
            if (this.getBusName() != null)  {
                final BusinessObject bus = new BusinessObject(this.getBusType(),
                                                              this.getBusName(),
                                                              this.getBusRevision(),
                                                              this.getBusVault());

// TODO: data model check needed... only one company connected? etc.
                // read current state, employee of, member of information
                final StringList selects = new StringList(1);
                selects.addElement("current");
                selects.addElement("to[Employee].from.name");
                selects.addElement("to[Member|from.type==Company].from.name");
                selects.addElement("to[Member|from.type==Company].attribute[Project Access]");
                selects.addElement("to[Member|from.type==Company].attribute[Project Role]");
                selects.addElement("to[Organization Representative|from.type==Company].from.name");

                final BusinessObjectWithSelect s = bus.select(_paramCache.getContext(), selects);

                // current state
                this.status = s.getSelectData("current");

                // employee
                this.employeeOf = s.getSelectData("to[Employee].from.name");

                // member
                this.memberOf = s.getSelectData("to[Member].from.name");
                this.memberAccess = s.getSelectData("to[Member].attribute[Project Access]");
                for (final String role : s.getSelectData("to[Member].attribute[Project Role]").split("~"))  {
                    if ((role != null) && !"".equals(role))  {
                        this.memberRoles.add(role);
                    }
                }

                // company representatives of
                final StringList companies = s.getSelectDataList("to[Organization Representative].from.name");
                if (companies != null)  {
                    for (final Object company : companies)  {
                        this.representativeOf.add((String) company);
                    }
                }
            }
        }

        /**
         * Appends the part for the business object to the TCL update code.
         *
         * @param _paramCache   parameter cache
         * @param _out          appendable instance where the TCL update code
         *                      for the business object part must be written
         * @throws IOException if the TCL update code could not written
         */
        @Override()
        public void write(final ParameterCache_mxJPO _paramCache,
                          final Appendable _out)
            throws IOException
        {
            _out.append("mql mod bus \"${OBJECTID}\"")
                .append(" \\\n    description \"").append(StringUtil_mxJPO.convertTcl(this.getBusDescription())).append("\"");
            for (final AttributeValue attr : this.getAttrValuesSorted())  {
                _out.append(" \\\n    \"").append(StringUtil_mxJPO.convertTcl(attr.name))
                    .append("\" \"").append(StringUtil_mxJPO.convertTcl(attr.value)).append("\"");
              }
            // member of
            _out.append("\n# member of")
                .append("\nset lsCur [split [mql print bus \"${OBJECTID}\" select \"to\\[Member|from.type==Company\\].from.name\" dump \"\\n\"] \"\\n\"]")
                .append("\nforeach sCur ${lsCur}  {")
                .append("\n  puts \"    - remove as member from '${sCur}'\"")
                .append("\n  mql disconnect bus \"${OBJECTID}\" \\")
                .append("\n      relationship \"Member\" \\")
                .append("\n      from Company \"${sCur}\" -")
                .append("\n}");
            if ((this.memberOf != null) && !this.memberOf.isEmpty())  {
                _out.append("\nputs \"    - assign as member from '").append(StringUtil_mxJPO.convertTcl(this.memberOf)).append("'\"")
                    .append("\nmql connect bus \"${OBJECTID}\" \\")
                    .append("\n    relationship \"Member\" \\")
                    .append("\n    from Company \"").append(StringUtil_mxJPO.convertTcl(this.memberOf)).append("\" - \\")
                    .append("\n    \"Project Access\" \"").append(StringUtil_mxJPO.convertTcl(this.memberAccess)).append("\" \\")
                    .append("\n    \"Project Role\" \"")
                            .append(StringUtil_mxJPO.convertTcl(StringUtil_mxJPO.joinTcl('~', false, this.memberRoles, null))).append("\"");
            }
            // employees
            final List<String> employees = new ArrayList<String>();
            if (this.employeeOf != null)  {
                employees.add(this.employeeOf);
            }
            _out.append(this.writeCons("employee",
                                       employees,
                                       "Employee"))
            // organization representatives
                .append(this.writeCons("organization representative",
                                       this.representativeOf,
                                       "Organization Representative"));
            // state (if not ignored)
            final Collection<String> matchStates = _paramCache.getValueList(Person_mxJPO.PARAM_IGNORE_STATE);
            if ((matchStates == null) || !StringUtil_mxJPO.match(this.getName(), matchStates))  {
                _out.append("\nsetState \"").append(this.status).append('\"');
            }
        }

        protected StringBuilder writeCons(final String _title,
                                          final Collection<String> _values,
                                          final String _relationship)
        {
            final StringBuilder ret = new StringBuilder()
                    .append("\n# ").append(_title)
                    .append("\nset lsCur [split [mql print bus \"${OBJECTID}\" select \"to\\[")
                                                .append(_relationship).append("\\].from.name\" dump \"\\n\"] \"\\n\"]")
                    .append("\nset lsNew [list");
            for (final String repr : _values)  {
if (!repr.isEmpty())  {
                ret.append(" \"").append(StringUtil_mxJPO.convertTcl(repr)).append('\"');
}
            }
            ret.append("]")
               .append("\nforeach sCur ${lsCur}  {")
               .append("\n  if {[lsearch ${lsNew} \"${sCur}\"] < 0}  {")
               .append("\n    puts \"    - remove as ").append(_title).append(" from '${sCur}'\"")
               .append("\n    mql disconnect bus \"${OBJECTID}\" \\")
               .append("\n        relationship \"").append(_relationship).append("\" \\")
               .append("\n        from Company \"${sCur}\" -")
               .append("\n  }")
               .append("\n}")
               .append("\nforeach sNew ${lsNew}  {")
               .append("\n  if {[lsearch ${lsCur} \"${sNew}\"] < 0}  {")
               .append("\n    puts \"    - assign as ").append(_title).append(" from '${sNew}'\"")
               .append("\n    mql connect bus \"${OBJECTID}\" \\")
               .append("\n        relationship \"").append(_relationship).append("\" \\")
               .append("\n        from Company \"${sNew}\" -")
               .append("\n  }")
               .append("\n}");

            return ret;
        }

        /**
         * Sets the TCL variable <code>OBJECTID</code> to current business
         * object.
         *
         * @param _context          context for this request
         * @param _preMQLCode       pre MQL code
         * @param _postMQLCode      post MQL code
         * @param _tclVariables     map with all TCL variables
         * @throws MatrixException if update failed
         */
        protected void prepareUpdate(final Context _context,
                                     final StringBuilder _preMQLCode,
                                     final StringBuilder _postMQLCode,
                                     final Map<String,String> _tclVariables)
            throws MatrixException
        {
            // found the business object
            final BusinessObject bus = new BusinessObject(this.getBusType(),
                                                          this.getBusName(),
                                                          this.getBusRevision(),
                                                          this.getBusVault());
            final String objectId = bus.getObjectId(_context);

            // prepare map of all TCL variables incl. id of business object
            _tclVariables.put("OBJECTID", objectId);
        }
    }
}
