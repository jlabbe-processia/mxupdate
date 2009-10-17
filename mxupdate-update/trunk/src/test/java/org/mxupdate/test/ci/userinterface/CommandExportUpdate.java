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
import org.mxupdate.test.data.user.PersonAdminData;
import org.mxupdate.test.data.user.RoleData;
import org.mxupdate.test.data.userinterface.CommandData;
import org.mxupdate.test.data.util.PropertyDef;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test cases for the export of commands.
 *
 * @author The MxUpdate Team
 * @version $Id$
 */
public class CommandExportUpdate
    extends AbstractDataExportUpdate<CommandData>
{
    /**
     * Creates for given <code>_name</code> a new command instance.
     *
     * @param _name     name of the command instance
     * @return command instance
     */
    @Override()
    protected CommandData createNewData(final String _name)
    {
        return new CommandData(this, _name);
    }

    /**
     * Data provider for test commands.
     *
     * @return object array with all test commands
     */
    @DataProvider(name = "commands")
    public Object[][] getCommands()
    {
        return this.prepareData("command",
                new Object[]{
                        "command without anything (to test required fields)",
                        new CommandData(this, "hello \" test")},
                new Object[]{
                        "complex command with settings",
                        new CommandData(this, "hello \" test")
                                .setValue("label", "command label \" \\ ' #")
                                .setValue("description", "\"\\\\ hallo")
                                .setValue("href", "${COMMON_DIR}/emxTree.jsp?mode=insert")
                                .setValue("alt", "${COMMON_DIR}/emxTreeAlt.jsp?mode=insert")
                                .setSetting("Setting 1", "Setting Value ' 1")
                                .setSetting("Setting 2", "Value2")},
                new Object[]{
                        "complex command with users",
                        new CommandData(this, "command")
                                .setValue("label", "aaa.bbb.ccc")
                                .setValue("description", "\"\\\\ hallo")
                                .setValue("href", "${COMMON_DIR}/emxTree.jsp?mode=insert")
                                .setValue("alt", "${COMMON_DIR}/emxTreeAlt.jsp?mode=insert")
                                .setSetting("Setting 1", "Setting Value ' 1")
                                .addUser(new RoleData(this, "assigned \"role\""))
                                .addUser(new PersonAdminData(this, "assigned \"person\""))});
    }

    /**
     * Cleanup all test commands.
     *
     * @throws MatrixException if cleanup failed
     */
    @BeforeMethod()
    @AfterClass()
    public void cleanup()
        throws MatrixException
    {
        this.cleanup(AbstractTest.CI.UI_COMMAND);
        this.cleanup(AbstractTest.CI.ROLE);
        this.cleanup(AbstractTest.CI.PERSONADMIN);
    }

    /**
     * Tests a new created command and the related export.
     *
     * @param _description  description of the test case
     * @param _command      command to test
     * @throws Exception if test failed
     */
    @Test(dataProvider = "commands", description = "test export of new created commands")
    public void testExport(final String _description,
                           final CommandData _command)
        throws Exception
    {
        _command.create();
        _command.checkExport(_command.export());
    }

    /**
     * Tests an update of non existing command. The result is tested with by
     * exporting the command and checking the result.
     *
     * @param _description  description of the test case
     * @param _command      command to test
     * @throws Exception if test failed
     */
    @Test(dataProvider = "commands", description = "test update of non existing command")
    public void testUpdate(final String _description,
                           final CommandData _command)
        throws Exception
    {
        // create referenced property value
        for (final PropertyDef prop : _command.getProperties())  {
            if (prop.getTo() != null)  {
                prop.getTo().create();
            }
        }
        // create assigned users
        for (final AbstractUserData<?> user : _command.getUsers())  {
            user.create();
        }

        // first update with original content
        this.update(_command);
        final ExportParser exportParser = _command.export();
        _command.checkExport(exportParser);

        // second update with delivered content
        this.update(_command.getCIFileName(), exportParser.getOrigCode());
        _command.checkExport(_command.export());
    }
}
