/*
 * Copyright 2008-2011 The MxUpdate Team
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mxupdate.mapping.TypeDef_mxJPO;
import org.mxupdate.update.AbstractAdminObject_mxJPO;
import org.mxupdate.update.util.ParameterCache_mxJPO;
import org.mxupdate.update.util.StringUtil_mxJPO;

/**
 * Handles the export and the update of the format configuration item.
 *
 * @author The MxUpdate Team
 * @version $Id$
 */
public class Format_mxJPO
    extends AbstractAdminObject_mxJPO
{
    /**
     * Set of all ignored URLs from the XML definition for formats.
     *
     * @see #parse(ParameterCache_mxJPO, String, String)
     */
    private static final Set<String> IGNORED_URLS = new HashSet<String>();
    static  {
        // to be ignored, because identically to fileType
        Format_mxJPO.IGNORED_URLS.add("/fileCreator");
    }

    /**
     * Reference to the edit program.
     */
    private String commandEdit = null;

    /**
     * Reference to the print program.
     */
    private String commandPrint = null;

    /**
     * Reference to the view program.
     */
    private String commandView = null;

    /**
     * File suffix of the format.
     */
    private String fileSuffix = null;

    /**
     * File type of the format.
     */
    private String fileType = null;

    /**
     * Mime type of the format.
     */
    private String mimeType = null;

    /**
     * Version of the format.
     */
    private String version = null;

    /**
     * Constructor used to initialize the type definition enumeration.
     *
     * @param _typeDef  defines the related type definition enumeration
     * @param _mxName   MX name of the format object
     */
    public Format_mxJPO(final TypeDef_mxJPO _typeDef,
                        final String _mxName)
    {
        super(_typeDef, _mxName);
    }

    /**
     * Parses all format specific URLs.
     *
     * @param _paramCache   parameter cache with MX context
     * @param _url          URL to parse
     * @param _content      content of the URL to parse
     * @return <i>true</i> if <code>_url</code> could be parsed; otherwise
     *         <i>false</i>
     */
    @Override()
    protected boolean parse(final ParameterCache_mxJPO _paramCache,
                            final String _url,
                            final String _content)
    {
        final boolean parsed;
        if (Format_mxJPO.IGNORED_URLS.contains(_url))  {
            parsed = true;
        } else if ("/editCommand".equals(_url))  {
            this.commandEdit = _content;
            parsed = true;
        } else if ("/printCommand".equals(_url))  {
            this.commandPrint = _content;
            parsed = true;
        } else if ("/viewCommand".equals(_url))  {
            this.commandView = _content;
            parsed = true;

        } else if ("/fileSuffix".equals(_url))  {
            this.fileSuffix = _content;
            parsed = true;
        } else if ("/fileType".equals(_url))  {
            this.fileType = _content;
            parsed = true;
        } else if ("/mimeType".equals(_url))  {
            this.mimeType = _content;
            parsed = true;

        } else if ("/version".equals(_url))  {
            this.version = _content;
            parsed = true;

        } else  {
            parsed = super.parse(_paramCache, _url, _content);
        }
        return parsed;
    }

    /**
     * Writes specific information about the cached format to the given
     * writer instance.
     *
     * @param _paramCache   parameter cache
     * @param _out          appendable instance to the TCL update file
     * @throws IOException if the TCL update code for the format could not be
     *                     written
     */
    @Override()
    protected void writeObject(final ParameterCache_mxJPO _paramCache,
                               final Appendable _out)
        throws IOException
    {
        _out.append(" \\\n    ").append(this.isHidden() ? "hidden" : "!hidden")
            .append(" \\\n    version \"").append(StringUtil_mxJPO.convertTcl(this.version)).append('\"')
            .append(" \\\n    suffix \"").append(StringUtil_mxJPO.convertTcl(this.fileSuffix)).append('\"')
            .append(" \\\n    type \"").append(StringUtil_mxJPO.convertTcl(this.fileType)).append('\"')
            .append(" \\\n    mime \"").append(StringUtil_mxJPO.convertTcl(this.mimeType)).append('\"')
            .append(" \\\n    view \"").append(StringUtil_mxJPO.convertTcl(this.commandView)).append('\"')
            .append(" \\\n    edit \"").append(StringUtil_mxJPO.convertTcl(this.commandEdit)).append('\"')
            .append(" \\\n    print \"").append(StringUtil_mxJPO.convertTcl(this.commandPrint)).append('\"');
    }


    /**
     * The method overwrites the original method to append the MQL statements
     * in the <code>_preMQLCode</code> to reset this format. Following
     * steps are done:
     * <ul>
     * <li>set to not hidden</li>
     * <li>reset description, version, suffix, mime type, file type</li>
     * <li>remove view / edit / print program</li>
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
    @Override()
    protected void update(final ParameterCache_mxJPO _paramCache,
                          final CharSequence _preMQLCode,
                          final CharSequence _postMQLCode,
                          final CharSequence _preTCLCode,
                          final Map<String,String> _tclVariables,
                          final File _sourceFile)
        throws Exception
    {
        final StringBuilder preMQLCode = new StringBuilder()
                .append("escape mod ").append(this.getTypeDef().getMxAdminName())
                .append(" \"").append(StringUtil_mxJPO.convertMql(this.getName())).append('\"')
                .append(" !hidden description \"\" version \"\" suffix \"\" mime \"\" type \"\" view \"\" edit \"\" print \"\";\n");

        // append already existing pre MQL code
        preMQLCode.append(_preMQLCode);

        super.update(_paramCache, preMQLCode, _postMQLCode, _preTCLCode, _tclVariables, _sourceFile);
    }
}
