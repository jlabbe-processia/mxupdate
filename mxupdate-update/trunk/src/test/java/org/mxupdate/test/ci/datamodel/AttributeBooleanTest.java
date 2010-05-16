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

package org.mxupdate.test.ci.datamodel;

import org.mxupdate.test.data.datamodel.AttributeBooleanData;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test cases for the update and export of boolean attributes.
 *
 * @author The MxUpdate Team
 * @version $Id$
 */
@Test()
public class AttributeBooleanTest
    extends AbstractAttributeTest<AttributeBooleanData>
{
    /**
     * Creates for given <code>_name</code> a new boolean attribute instance.
     *
     * @param _name     name of the attribute instance
     * @return attribute instance
     */
    @Override()
    protected AttributeBooleanData createNewData(final String _name)
    {
        return new AttributeBooleanData(this, _name);
    }

    /**
     * Data provider for test boolean attributes.
     *
     * @return object array with all test boolean attributes
     */
    @DataProvider(name = "data")
    public Object[][] getAttributes()
    {
        return this.prepareData("boolean attribute", "TRUE", "FALSE");
    }
}
