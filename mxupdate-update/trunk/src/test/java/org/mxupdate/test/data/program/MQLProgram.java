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

package org.mxupdate.test.data.program;

import matrix.util.MatrixException;

import org.mxupdate.test.AbstractTest;

/**
 * The class is used to define all MQL program objects used to create / update
 * and to export.
 *
 * @author The MxUpdate Team
 * @version $Id$
 */
public class MQLProgram
    extends AbstractProgram<MQLProgram>
{
    /**
     * Initializes this MQL program.
     *
     * @param _test     related test instance
     * @param _name     name of the MQL program
     */
    public MQLProgram(final AbstractTest _test,
                      final String _name)
    {
        super(_test, AbstractTest.CI.MQL_PROGRAM, _name, "", "program/mql", null);
    }

    /**
     * @return always null
     */
    @Override
    public String ciFile()
    {
        return null;
    }

    /**
     * Creates this MQL program within MX.
     *
     * @return this MQL program instance
     * @throws MatrixException if create of the MQL program failed
     */
    @Override
    public MQLProgram create()
        throws MatrixException
    {
        if (!this.isCreated())  {
            this.setCreated(true);
            this.getTest().mql("escape add program \"" + AbstractTest.convertMql(this.getName()) + "\" mql");
        }
        return this;
    }
}
