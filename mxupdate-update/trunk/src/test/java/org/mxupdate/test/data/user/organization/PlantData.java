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

package org.mxupdate.test.data.user.organization;

import org.mxupdate.test.AbstractTest;

/**
 * Used to handle plant data for testing persons with business object.
 *
 * @author The MxUpdate Team
 * @version $Id$
 */
public class PlantData
    extends AbstractOrganizationalData<PlantData>
{
    /**
     * Constructor to initialize this plant.
     *
     * @param _test     related test implementation
     * @param _name     name of the plant
     */
    public PlantData(final AbstractTest _test,
                     final String _name)
    {
        super(_test, AbstractTest.CI.OTHER_PLANT, _name);
    }
}
