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

package org.mxupdate.update.datamodel;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.mxupdate.mapping.TypeDef_mxJPO;
import org.mxupdate.update.util.ParameterCache_mxJPO;

import static org.mxupdate.update.util.StringUtil_mxJPO.convertTcl;

/**
 * Data model type class.
 *
 * @author Tim Moxter
 * @version $Id$
 */
public class Type_mxJPO
        extends AbstractDMWithAttributes_mxJPO
{
    /**
     * Defines the serialize version unique identifier.
     */
    private static final long serialVersionUID = 7501426743362043943L;

    /**
     * Is the type abstract?
     */
    private boolean abstractFlag = false;

    /**
     * From which type is this type derived?
     */
    private String derived = "ADMINISTRATION";

    /**
     * Constructor used to initialize the type definition enumeration.
     *
     * @param _typeDef  defines the related type definition enumeration
     * @param _mxName   MX name of the type object
     */
    public Type_mxJPO(final TypeDef_mxJPO _typeDef,
                      final String _mxName)
    {
        super(_typeDef, _mxName);
    }

    /**
     * @param _url      URL to parse
     * @param _content  content of the URL to parse
     */
    @Override
    protected void parse(final String _url,
                         final String _content)
    {
        if ("/abstract".equals(_url))  {
            this.abstractFlag = true;

        } else if ("/derivedFrom".equals(_url))  {
            // to be ignored ...
        } else if ("/derivedFrom/typeRefList".equals(_url))  {
            // to be ignored ...
        } else if ("/derivedFrom/typeRefList/typeRef".equals(_url))  {
            this.derived = _content;

        } else  {
            super.parse(_url, _content);
        }
    }

    /**
     *
     * @param _paramCache   parameter cache
     * @param _out          appendable instance to the TCL update file
     * @throws IOException if the TCL update code for the type could not be
     *                     written
     */
    @Override
    protected void writeObject(final ParameterCache_mxJPO _paramCache,
                               final Appendable _out)
            throws IOException
    {
        _out.append(" \\\n    derived \"").append(convertTcl(this.derived)).append("\"")
            .append(" \\\n    ").append(isHidden() ? "" : "!").append("hidden")
            .append(" \\\n    abstract ").append(Boolean.toString(this.abstractFlag));
        this.writeTriggers(_out);
    }

    /**
     * The method overwrites the original method to append the MQL statements
     * in the <code>_preMQLCode</code> to reset this type. Following steps are
     * done:
     * <ul>
     * <li>set not hidden</li>
     * <li>reset description</li>
     * </ul>
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
    @Override
    protected void update(final ParameterCache_mxJPO _paramCache,
                          final CharSequence _preMQLCode,
                          final CharSequence _postMQLCode,
                          final CharSequence _preTCLCode,
                          final Map<String,String> _tclVariables,
                          final File _sourceFile)
            throws Exception
    {
        final StringBuilder preMQLCode = new StringBuilder()
                .append("mod ").append(this.getTypeDef().getMxAdminName())
                .append(" \"").append(this.getName()).append('\"')
                .append(" !hidden description \"\";\n");

        // append already existing pre MQL code
        preMQLCode.append(_preMQLCode);

        super.update(_paramCache, preMQLCode, _postMQLCode, _preTCLCode, _tclVariables, _sourceFile);
    }
}
