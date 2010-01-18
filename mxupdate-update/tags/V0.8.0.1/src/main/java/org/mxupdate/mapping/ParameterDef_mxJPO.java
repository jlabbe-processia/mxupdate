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

package org.mxupdate.mapping;


/**
 * Holds the definition of all parameters.
 *
 * @author Tim Moxter
 * @version $Id$
 */
public final class ParameterDef_mxJPO
        extends AbstractValue_mxJPO
{
    /**
     * Enumeration to define the value type.
     */
    public enum Type
    {
        /**
         * If the parameter is boolean, that means the parameter is defined
         * (true) or not (false).
         */
        BOOLEAN,

        /**
         * If the parameter is defined the value type is integer.
         */
        INTEGER,

        /**
         * A list of strings is used for this type. That means that every
         * argument of a parameter is append as string to the list.
         */
        LIST,

        /**
         * A map of strings is used.
         */
        MAP,

        /**
         * If the parameter is defined, the value is set to a string. If e.g.
         * the parameter is defined twice, the second definition of the
         * parameter overwrites the first definition.
         */
        STRING;
    }

    /**
     * Default value of the parameter.
     *
     * @see #getDefaultValue()
     */
    private String defaultValue;

    /**
     * Type of the parameter.
     *
     * @see Type
     * @see #getType()
     */
    private Type type;

    /**
     * The constructor is defined private so that a new instance could only
     * created within this class.
     *
     * @param _name     name of the parameter definition
     */
    private ParameterDef_mxJPO(final String _name)
    {
        super(_name);
    }

    /**
     * Defines the values of the parameter definition.
     *
     * @param _mapping  cache for all mapping
     * @param _key      key to define
     * @param _value    related value to define
     * @throws Exception if key is not found
     */
    protected static void defineValue(final Mapping_mxJPO _mapping,
                                      final String _key,
                                      final String _value)
            throws Exception
    {
        final String enumName = _key.replaceAll("\\..*", "");
        final String key = _key.substring(enumName.length() + 1);

        ParameterDef_mxJPO param = _mapping.getParameterDefJPOsMap().get(enumName);
        if (param == null)  {
            param = new ParameterDef_mxJPO(enumName);
            _mapping.getParameterDefJPOsMap().put(enumName, param);
        }

        if ("Default".equals(key))  {
            param.defaultValue = _value;
        } else if ("Type".equals(key))  {
            param.type = ParameterDef_mxJPO.Type.valueOf(_value.toUpperCase());
        } else  {
            param.defineValues(key, _value);
        }
    }

    /**
     * Getter method for instance variable {@link #defaultValue} returning the
     * default value.
     *
     * @return default value of the parameter
     * @see #defaultValue
     */
    public String getDefaultValue()
    {
        return this.defaultValue;
    }

    /**
     * Getter method for instance variable {@link #type} returning the
     * parameter type.
     *
     * @return parameter type
     * @see #type
     */
    public Type getType()
    {
        return this.type;
    }

    /**
     * Prepares the string representation of the parameter definition as
     * concatenation of the name and type.
     *
     * @return string representation of the parameter definition
     */
    @Override
    public String toString()
    {
        return new StringBuilder()
            .append("[Parameter Definition ")
                .append("'").append(this.getName()).append("', ")
                .append("type = ").append(this.type)
            .append("]")
            .toString();
    }
}
