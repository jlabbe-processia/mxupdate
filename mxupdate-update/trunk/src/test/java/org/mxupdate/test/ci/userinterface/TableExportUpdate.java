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

package org.mxupdate.test.ci.userinterface;

import matrix.util.MatrixException;

import org.mxupdate.test.AbstractDataExportUpdate;
import org.mxupdate.test.AbstractTest;
import org.mxupdate.test.ExportParser;
import org.mxupdate.test.data.user.AbstractUserData;
import org.mxupdate.test.data.user.GroupData;
import org.mxupdate.test.data.user.RoleData;
import org.mxupdate.test.data.userinterface.FieldData;
import org.mxupdate.test.data.userinterface.TableData;
import org.mxupdate.test.data.util.PropertyDef;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test cases for the export / update of web tables.
 *
 * @author The MxUpdate Team
 * @version $Id$
 */
public class TableExportUpdate
    extends AbstractDataExportUpdate<TableData>
{
    /**
     * Creates for given <code>_name</code> a new table instance.
     *
     * @param _name     name of the table instance
     * @return table instance
     */
    @Override()
    protected TableData createNewData(final String _name)
    {
        return new TableData(this, _name);
    }

    /**
     * Data provider for test tables.
     *
     * @return object array with all test tables
     */
    @DataProvider(name = "tables")
    public Object[][] getTables()
    {
        return this.prepareData("table",
                new Object[]{
                        "table without anything (to test required fields)",
                        new TableData(this, "hello \" test")},
                new Object[]{
                        "simple table",
                        new TableData(this, "hallo \" test")
                            .setValue("description", "\"\\\\ hallo")},
                new Object[]{
                        "simple form with two fields",
                        new TableData(this, "hallo \" test")
                            .setValue("description", "\"\\\\ hallo")
                            .newField("field \"1\"").getFormTable()
                            .newField("field \"2\"").getFormTable()},
                new Object[]{
                        "simple form with complex field",
                        new TableData(this, "hallo \" test")
                            .setValue("description", "\"\\\\ hallo")
                            .newField("field")
                                    .setValue("label", "an \"label\"")
                                    .setValue("range", "an \"range\"")
                                    .setValue("href", "an \"url\"")
                                    .setValue("alt", "an \"alt\"")
                                    .setValue("update", "an \"alt\"")
                                    .setSetting("first \"key\"", "first \"value\"")
                                    .setSetting("second \"key\"", "second \"value\"")
                                    .getFormTable()},
                new Object[]{
                        "form with business object select",
                        new TableData(this, "hallo \" test")
                            .setValue("description", "\"\\\\ hallo")
                            .newField("field")
                                    .setValue("businessobject", "select \"expression\"")
                                    .getFormTable()},
                new Object[]{
                        "form with relationship select",
                        new TableData(this, "hallo \" test")
                            .setValue("description", "\"\\\\ hallo")
                            .newField("field")
                                    .setValue("relationship", "select \"expression\"")
                                    .getFormTable()},
                new Object[]{
                        "form with one role and one group",
                        new TableData(this, "hallo \" test")
                            .newField("field")
                                    .addUser(new RoleData(this, "user \"role\""))
                                    .addUser(new GroupData(this, "user \"group\""))
                                    .getFormTable()}
        );
    }

    /**
     * Cleanup all test web tables.
     *
     * @throws MatrixException if cleanup failed
     */
    @BeforeMethod()
    @AfterClass()
    public void cleanup()
        throws MatrixException
    {
        this.cleanup(AbstractTest.CI.UI_TABLE);
        this.cleanup(AbstractTest.CI.PERSONADMIN);
        this.cleanup(AbstractTest.CI.ROLE);
        this.cleanup(AbstractTest.CI.GROUP);
    }

    /**
     * Tests a new created table and the related export.
     *
     * @param _description  description of the test case
     * @param _table        table to test
     * @throws Exception if test failed
     */
    @Test(enabled=false,dataProvider = "tables", description = "test export of new created table")
    public void simpleExport(final String _description,
                             final TableData _table)
        throws Exception
    {
        _table.create();
        _table.checkExport(_table.export());
    }

    /**
     * Tests an update of non existing table. The result is tested with by
     * exporting the table and checking the result.
     *
     * @param _description  description of the test case
     * @param _table        table to test
     * @throws Exception if test failed
     */
    @Test(dataProvider = "tables", description = "test update of non existing table")
    public void simpleUpdate(final String _description,
                             final TableData _table)
        throws Exception
    {
        // create users
        for (final FieldData<TableData> field : _table.getFields())  {
            for (final AbstractUserData<?> user : field.getUsers())  {
                user.create();
            }
        }
        // create referenced property value
        for (final PropertyDef prop : _table.getProperties())  {
            if (prop.getTo() != null)  {
                prop.getTo().create();
            }
        }

        // first update with original content
        this.update(_table);
        final ExportParser exportParser = _table.export();
        _table.checkExport(exportParser);

        // second update with delivered content
        this.update(_table.getCIFileName(), exportParser.getOrigCode());
        _table.checkExport(_table.export());
    }
}
