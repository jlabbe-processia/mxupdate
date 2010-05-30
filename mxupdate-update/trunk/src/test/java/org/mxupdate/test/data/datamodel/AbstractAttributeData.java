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

package org.mxupdate.test.data.datamodel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import matrix.util.MatrixException;

import org.mxupdate.test.AbstractTest;
import org.mxupdate.test.data.program.AbstractProgramData;
import org.mxupdate.update.util.StringUtil_mxJPO;

/**
 * The class is used to define all types of attributes, to create them and test
 * the result.
 *
 * @author The MxUpdate Team
 * @version $Id$
 * @param <T>   defines the class which is derived from this class
 */
public abstract class AbstractAttributeData<T extends AbstractAttributeData<?>>
    extends AbstractDataWithTrigger<T>
{
    /**
     * Within export the description and default value must be defined.
     */
    private static final Map<String,String> REQUIRED_EXPORT_VALUES = new HashMap<String,String>();
    static  {
        AbstractAttributeData.REQUIRED_EXPORT_VALUES.put("description", "");
        AbstractAttributeData.REQUIRED_EXPORT_VALUES.put("default", "");
    }

    /**
     * Attribute type of the attribute (string, integer, ....).
     */
    private final String attrType;

    /**
     * Ranges of this attribute.
     *
     * @see #addRange(AbstractRange)
     */
    private final Set<AbstractRange> ranges = new HashSet<AbstractRange>();

    /**
     *
     * @param _test         related test instance
     * @param _ci           related configuration type of this attribute
     * @param _name         name of this attribute
     * @param _attrType     type of this attribute
     */
    protected AbstractAttributeData(final AbstractTest _test,
                                    final AbstractTest.CI _ci,
                                    final String _name,
                                    final String _attrType)
    {
        super(_test, _ci, _name, AbstractAttributeData.REQUIRED_EXPORT_VALUES);
        this.attrType = _attrType;
    }

    /**
     * Defines a new attribute range.
     *
     * @param _range    range to assign to this attribute
     * @return this instance
     * @see #ranges
     */
    @SuppressWarnings("unchecked")
    public T addRange(final AbstractRange _range)
    {
        this.ranges.add(_range);
        return (T) this;
    }

    /**
     * Creates a this attribute with all values and settings.
     *
     * @return this attribute instance
     * @throws MatrixException if create failed
     */
    @Override()
    @SuppressWarnings("unchecked")
    public T create()
        throws MatrixException
    {
        if (!this.isCreated())  {
            this.setCreated(true);

            this.createDependings();

            final StringBuilder cmd = new StringBuilder();
            cmd.append("escape add attribute \"").append(AbstractTest.convertMql(this.getName()))
               .append("\" type ").append(this.attrType);

            // append hidden flag
            if (this.isHidden() != null)  {
                cmd.append(' ');
                if (!this.isHidden())  {
                    cmd.append('!');
                }
                cmd.append("hidden");
            }

            this.append4Create(cmd);

            for (final AbstractRange range : this.ranges)  {
                range.appendCreate(cmd);
            }

            this.getTest().mql(cmd);
        }
        return (T) this;
    }

    /**
     * {@inheritDoc}
     * Creates depending range programs.
     */
    @Override()
    @SuppressWarnings("unchecked")
    public T createDependings()
        throws MatrixException
    {
        super.createDependings();

        // create range programs
        for (final AbstractRange range : this.ranges)  {
            if (range instanceof AbstractAttributeData.RangeProgram)  {
                ((AbstractAttributeData.RangeProgram) range).program.create();
            }
        }

        return (T) this;
    }

    /**
     * Prepares the configuration item update file depending on the
     * configuration of this command.
     *
     * @return code for the configuration item update file
     */
    @Override
    public String ciFile()
    {
        final StringBuilder cmd = new StringBuilder()
                .append("mql escape mod attribute \"${NAME}\"");

        // append hidden flag
        if (this.isHidden() != null)  {
            cmd.append(' ');
            if (!this.isHidden())  {
                cmd.append('!');
            }
            cmd.append("hidden");
        }

        this.append4CIFileValues(cmd);

        return cmd.toString();
    }

    /**
     * Evaluates all 'adds' for ranges in the configuration item file.
     *
     * @param _needAdds     set with add strings used to append the adds for
     *                      {@link #ranges}
     */
    @Override()
    protected void evalAdds4CheckExport(final Set<String> _needAdds)
    {
        super.evalAdds4CheckExport(_needAdds);
        for (final AbstractRange range : this.ranges)  {
            range.evalAdds4CheckExport(_needAdds);
        }
    }

    /**
     * Used to define ranges with one value.
     */
    public abstract static class AbstractRange
    {
        /**
         * Comparator of this range.
         */
        final String comparator;

        /**
         * Value of this range.
         */
        final String value;

        /**
         * Initializes the range values.
         *
         * @param _comparator   comparator for the range
         * @param _value        range value itself
         */
        protected AbstractRange(final String _comparator,
                                final String _value)
        {
            this.comparator = _comparator;
            this.value = _value;
        }

        /**
         * Appends this range will so that the range is defined while the
         * attribute is created.
         *
         * @param _cmd  string builder where the MQL commands will be appended
         */
        protected void appendCreate(final StringBuilder _cmd)
        {
            _cmd.append(" range ").append(this.comparator)
                .append(" \"").append(AbstractTest.convertMql(this.value))
                .append("\"");
        }

        /**
         * Appends the add statement in TCL code for this range.
         *
         * @param _needAdds     set with add strings used to append the adds
         *                      for this range
         */
        protected void evalAdds4CheckExport(final Set<String> _needAdds)
        {
            final StringBuilder cmd = new StringBuilder()
                    .append("range ").append(this.comparator)
                    .append(" \"").append(AbstractTest.convertTcl(this.value))
                    .append("\"");
            _needAdds.add(cmd.toString());
        }
    }

    /**
     * Equal range definition.
     */
    public static class RangeEqual
        extends AbstractAttributeData.AbstractRange
    {
        /**
         * Equal range constructor with the value.
         *
         * @param _value    value to compare
         */
        public RangeEqual(final String _value)
        {
            super("=", _value);
        }
    }

    /**
     * Not equal range definition.
     */
    public static class RangeNotEqual
        extends AbstractAttributeData.AbstractRange
    {
        /**
         * Not equal range constructor with the value.
         *
         * @param _value    value to compare
         */
        public RangeNotEqual(final String _value)
        {
            super("!=", _value);
        }
    }

    /**
     * Less than range definition.
     */
    public static class RangeLessThan
        extends AbstractAttributeData.AbstractRange
    {
        /**
         * Less than range constructor with the value.
         *
         * @param _value    value to compare
         */
        public RangeLessThan(final String _value)
        {
            super("<", _value);
        }
    }

    /**
     * Greater than range definition.
     */
    public static class RangeGreaterThan
        extends AbstractAttributeData.AbstractRange
    {
        /**
         * Greater than range constructor with the value.
         *
         * @param _value    value to compare
         */
        public RangeGreaterThan(final String _value)
        {
            super(">", _value);
        }
    }

    /**
     * Less equal than range definition.
     */
    public static class RangeLessEqualThan
        extends AbstractAttributeData.AbstractRange
    {
        /**
         * Less equal than range constructor with the value.
         *
         * @param _value    value to compare
         */
        public RangeLessEqualThan(final String _value)
        {
            super("<", _value);
        }
    }

    /**
     * Greater equal than range definition.
     */
    public static class RangeGreaterEqualThan
        extends AbstractAttributeData.AbstractRange
    {
        /**
         * Greater equal than range constructor with the value.
         *
         * @param _value    value to compare
         */
        public RangeGreaterEqualThan(final String _value)
        {
            super(">=", _value);
        }
    }

    /**
     * SMatch range definition.
     */
    public static class RangeSMatch
        extends AbstractAttributeData.AbstractRange
    {
        /**
         * SMatch range constructor with the value.
         *
         * @param _value    value to compare
         */
        public RangeSMatch(final String _value)
        {
            super("smatch", _value);
        }
    }

    /**
     * Not smatch range definition.
     */
    public static class RangeNotSMatch
        extends AbstractAttributeData.AbstractRange
    {
        /**
         * Not smatch range constructor with the value.
         *
         * @param _value    value to compare
         */
        public RangeNotSMatch(final String _value)
        {
            super("!smatch", _value);
        }
    }

    /**
     * Match range definition.
     */
    public static class RangeMatch
        extends AbstractAttributeData.AbstractRange
    {
        /**
         * Match range constructor with the value.
         *
         * @param _value    value to compare
         */
        public RangeMatch(final String _value)
        {
            super("match", _value);
        }
    }

    /**
     * Not match range definition.
     */
    public static class RangeNotMatch
        extends AbstractAttributeData.AbstractRange
    {
        /**
         * Not match range constructor with the value.
         *
         * @param _value    value to compare
         */
        public RangeNotMatch(final String _value)
        {
            super("!match", _value);
        }
    }

    /**
     * Program range definition.
     */
    public static class RangeProgram
        extends AbstractAttributeData.AbstractRange
    {
        /** Related program of the range. */
        private final AbstractProgramData<?> program;

        /**
         * Program range constructor with the value.
         *
         * @param _program  related program
         * @param _input    input string
         */
        public RangeProgram(final AbstractProgramData<?> _program,
                            final String _input)
        {
            super("program", _input);
            this.program = _program;
        }

        /**
         * Appends this range will so that the range is defined while the
         * attribute is created.
         *
         * @param _cmd  string builder where the MQL commands will be appended
         */
        @Override()
        protected void appendCreate(final StringBuilder _cmd)
        {
            _cmd.append(" range ").append(this.comparator)
                .append(" \"").append(StringUtil_mxJPO.convertMql(this.program.getName()))
                .append("\" input \"").append(AbstractTest.convertMql(this.value))
                .append("\"");
        }

        /**
         * Appends the add statement in TCL code for this range.
         *
         * @param _needAdds     set with add strings used to append the adds
         *                      for this range
         */
        @Override()
        protected void evalAdds4CheckExport(final Set<String> _needAdds)
        {
            final StringBuilder cmd = new StringBuilder()
                    .append("range ").append(this.comparator)
                    .append(" \"").append(StringUtil_mxJPO.convertMql(this.program.getName())).append("\"");
            if ((this.value != null) && !this.value.isEmpty())  {
                cmd.append(" input \"").append(AbstractTest.convertTcl(this.value)).append("\"");
            }
            _needAdds.add(cmd.toString());
        }
    }

    /**
     * Between range definition.
     */
    public static class RangeBetween
        extends AbstractAttributeData.AbstractRange
    {
        /** Inclusive first value? */
        private final boolean inclusive1;

        /** Second value. */
        private final String value2;

        /** Inclusive second value? */
        private final boolean inclusive2;

        /**
         * Between range constructor.
         *
         * @param _value1       first value
         * @param _inclusive1   inclusive first value?
         * @param _value2       second value
         * @param _inclusive2   inclusive second value?
         */
        public RangeBetween(final String _value1,
                            final boolean _inclusive1,
                            final String _value2,
                            final boolean _inclusive2)
        {
            super("between", _value1);
            this.inclusive1 = _inclusive1;
            this.value2 = _value2;
            this.inclusive2 = _inclusive2;
        }

        /**
         * Appends this range will so that the range is defined while the
         * attribute is created.
         *
         * @param _cmd  string builder where the MQL commands will be appended
         */
        @Override()
        protected void appendCreate(final StringBuilder _cmd)
        {
            _cmd.append(" range ").append(this.comparator)
                .append(" \"").append(AbstractTest.convertMql(this.value))
                .append("\" ").append(this.inclusive1 ? "inclusive" : "exclusive")
                .append(" \"").append(AbstractTest.convertMql(this.value2))
                .append("\" ").append(this.inclusive2 ? "inclusive" : "exclusive");
        }

        /**
         * Appends the add statement in TCL code for this range.
         *
         * @param _needAdds     set with add strings used to append the adds
         *                      for this range
         */
        @Override()
        protected void evalAdds4CheckExport(final Set<String> _needAdds)
        {
            final StringBuilder cmd = new StringBuilder()
                .append("range ").append(this.comparator)
                .append(" \"").append(AbstractTest.convertTcl(this.value))
                .append("\" ").append(this.inclusive1 ? "inclusive" : "exclusive")
                .append(" \"").append(AbstractTest.convertTcl(this.value2))
                .append("\" ").append(this.inclusive2 ? "inclusive" : "exclusive");
            _needAdds.add(cmd.toString());
        }
    }
}
