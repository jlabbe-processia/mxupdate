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

package org.mxupdate.test.update.datamodel;

import java.io.StringReader;
import java.lang.reflect.Method;

import org.mxupdate.test.util.TestParameterCache;
import org.mxupdate.update.datamodel.Dimension_mxJPO;
import org.mxupdate.update.datamodel.dimension.DimensionDefParser_mxJPO;
import org.mxupdate.update.util.ParameterCache_mxJPO;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Class is used to test the dimension parser {@link DimensionDefParser_mxJPO} with
 * some examples.
 *
 * @author The MxUpdate Team
 * @version $Id$
 */
public class DimensionParserTest
{
    /**
     * Start of the command to update the dimension to extract the code.
     */
    private static final String START_INDEX = "updateDimension \"${NAME}\"  {";

    /**
     * Length of the string of the command to update the dimension.
     *
     * @see #START_INDEX
     */
    private static final int START_INDEX_LENGTH = DimensionParserTest.START_INDEX.length();

    /**
     * Returns data providers used for testing parses.
     *
     * @return test source code to parse
     */
    @DataProvider(name = "dimensions")
    public Object[][] getCodes()
    {
        return new Object[][]{
                new Object[]{
                        "simple hidden dimension definition without units",
                        "",
                        "description \"\" hidden \"true\""},
                new Object[]{
                        "simple not hidden (in uppercase) dimension definition without units",
                        "description \"\" hidden \"false\"",
                        "description \"\" hidden FALSE"},
                new Object[]{
                        "simple hidden (first character in uppercase) dimension definition without units",
                        "description \"\" hidden \"true\"",
                        "description \"\" hidden \"True\""},
                new Object[]{
                        "complex dimension definition",
                        "",
                          "description \"ein test\"\n"
                        + "hidden \"false\"\n"
                        + "unit \"name1\" {\n"
                        + "  default true\n"
                        + "  description \"description1\"\n"
                        + "  label \"label1\"\n"
                        + "  multiplier 1.0\n"
                        + "  offset 0.0\n"
                        + "  setting \"SettingKey2\" \"SettingValue2\"\n"
                        + "  setting \"to\" \"SettingValue\"\n"
                        + "  property \"Zest\" value \"Zest\"\n"
                        + "  property \"_test3\" value \"Test3\"\n"
                        + "  property \"abc\" value \"abc\"\n"
                        + "  property \"abc2\" value \"abc2\"\n"
                        + "  property \"test2\" value \"Test2\"\n"
                        + "  property \"test\" to type \"Part\" value \"Test\"\n"
                        + "}\n"
                        + "unit \"name2\" {\n"
                        + "  description \"description 2\"\n"
                        + "  label \"label2\"\n"
                        + "  multiplier 10.0\n"
                        + "  offset 20.0\n"
                        + "  system \"Duration Units\" to unit \"name2\"\n"
                        + "}"
                },
                new Object[]{
                        "dimension with unit with negative offset",
                        "",
                        "description \"ein test\"\n"
                        + "hidden \"false\"\n"
                        + "unit \"name1\" {\n"
                        + "  default true\n"
                        + "  description \"description1\"\n"
                        + "  label \"label1\"\n"
                        + "  multiplier 1.0\n"
                        + "  offset 0.0\n"
                        + "}\n"
                        + "unit \"name2\" {\n"
                        + "  description \"description 2\"\n"
                        + "  label \"label2\"\n"
                        + "  multiplier 10.0\n"
                        + "  offset -20.0\n"
                        + "}"
                },
                new Object[]{
                        "dimension with unit with negative multiplier",
                        "",
                        "description \"ein test\"\n"
                        + "hidden \"false\"\n"
                        + "unit \"name1\" {\n"
                        + "  default true\n"
                        + "  description \"description1\"\n"
                        + "  label \"label1\"\n"
                        + "  multiplier 1.0\n"
                        + "  offset 0.0\n"
                        + "}\n"
                        + "unit \"name2\" {\n"
                        + "  description \"description 2\"\n"
                        + "  label \"label2\"\n"
                        + "  multiplier -10.0\n"
                        + "  offset 20.0\n"
                        + "}"
                }
        };
    }

    /**
     * Parsed the <code>_definition</code> code and compares the result with
     * <code>_toTest</code>.
     *
     * @param _description  description of the test
     * @param _toTest       expected result (if empty string
     *                      <code>_definition</code> is the expected result)
     * @param _definition   text of the definition to test
     * @throws Exception if <code>_definition</code> could not parsed
     */
    @Test(dataProvider = "dimensions")
    public void testDimension(final String _description,
                              final String _toTest,
                              final String _definition)
            throws Exception
    {
        final ParameterCache_mxJPO paramCache = new TestParameterCache();

        final DimensionDefParser_mxJPO parser = new DimensionDefParser_mxJPO(new StringReader(_definition));
        final Dimension_mxJPO dimension = parser.dimension(paramCache,
                                                           paramCache.getMapping().getTypeDef("Dimension"),
                                                           "Test");

        final StringBuilder bck = new StringBuilder();
        final Method write = dimension.getClass()
                .getDeclaredMethod("write", ParameterCache_mxJPO.class, Appendable.class);
        write.setAccessible(true);
        write.invoke(dimension, paramCache, bck);

        final StringBuilder oldDefBuilder = new StringBuilder();
        for (final String line : ("".equals(_toTest)) ? _definition.split("\n") : _toTest.split("\n"))  {
            oldDefBuilder.append(line.trim()).append(' ');
        }
        int length = 0;
        String oldDef = oldDefBuilder.toString();
        while (length != oldDef.length())  {
            length = oldDef.length();
            oldDef = oldDef.replaceAll("  ", " ");
        }

        final String temp = bck.substring(bck.indexOf(DimensionParserTest.START_INDEX)
                                                      + DimensionParserTest.START_INDEX_LENGTH + 1,
                                          bck.length() - 2)
                               .toString();
        final StringBuilder newDef = new StringBuilder();
        for (final String line : temp.split("\n"))  {
            newDef.append(line.trim()).append(' ');
        }

        Assert.assertEquals(oldDef.trim(), newDef.toString().trim());
    }
}
