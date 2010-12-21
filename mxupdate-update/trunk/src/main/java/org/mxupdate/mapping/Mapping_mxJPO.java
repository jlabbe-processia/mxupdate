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

package org.mxupdate.mapping;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import matrix.util.MatrixException;

import org.mxupdate.update.util.MqlUtil_mxJPO;
import org.mxupdate.update.util.ParameterCache_mxJPO;

/**
 * The class is used to map from used names within the MxUpdate JPOs and the
 * internal used names within MX.
 *
 * @author The MxUpdate Team
 * @version $Id$
 */
public class Mapping_mxJPO
{
    /**
     * Name of the MX program where the mapping definition is stored as
     * properties.
     */
    private static final String PROP_NAME = "org.mxupdate.mapping.properties";

    /**
     * Used prefix of property definition within the property file.
     *
     * @see #Mapping_mxJPO(ParameterCache_mxJPO)
     */
    private static final String PREFIX_PROPERTYDEF = "PropertyDef.";

    /**
     * Length of the prefix of property definition within the property file.
     *
     * @see #Mapping_mxJPO(ParameterCache_mxJPO)
     */
    private static final int LENGTH_PROPERTYDEF = Mapping_mxJPO.PREFIX_PROPERTYDEF.length();

    /**
     * Used prefix of parameter definition within the property file.
     *
     * @see #Mapping_mxJPO(ParameterCache_mxJPO)
     */
    private static final String PREFIX_PARAMETERDEF = "ParameterDef.";

    /**
     * Length of the {@link #PREFIX_PARAMETERDEF prefix of parameter definition}
     * within the property file.
     *
     * @see #Mapping_mxJPO(ParameterCache_mxJPO)
     */
    private static final int LENGTH_PARAMETERDEF = Mapping_mxJPO.PREFIX_PARAMETERDEF.length();

    /**
     * Properties holding all mapping definitions.
     */
    private final Properties properties = new Properties();

    /**
     * Maps from the mode enumeration {@link Mode_mxJPO} to the related
     * instance holding the parameter names and description.
     *
     * @see Mode_mxJPO#defineValue(Mapping_mxJPO, String, String)
     * @see #getModeMap()
     */
    private final Map<Mode_mxJPO,AbstractValue_mxJPO> modeMap
            = new HashMap<Mode_mxJPO,AbstractValue_mxJPO>();

    /**
     * Maps from the name of the parameter to the parameter instance.
     *
     * @see ParameterDef_mxJPO#defineValue(Mapping_mxJPO, String, String)
     * @see #getAllParameterDefs()
     * @see #getParameterDef(String)
     * @see #getParameterDefMap()
     */
    private final Map<String,ParameterDef_mxJPO> parameterDefMap = new HashMap<String,ParameterDef_mxJPO>();

    /**
     * Mapping between internal used admin property definitions and the MX
     * attribute names.
     *
     * @see PropertyDef_mxJPO#getAttrName(ParameterCache_mxJPO)
     * @see #getPropertyAttributes()
     */
    private final Map<PropertyDef_mxJPO,String> propertyAttributes = new HashMap<PropertyDef_mxJPO,String>();

    /**
     * Mapping between internal used admin property definitions and the MX
     * admin property names.
     *
     * @see PropertyDef_mxJPO#getPropName(ParameterCache_mxJPO)
     * @see #getPropertyEnum2Names()
     */
    private final Map<PropertyDef_mxJPO,String> propertyEnum2Names = new HashMap<PropertyDef_mxJPO,String>();

    /**
     * Mapping between MX admin property name and the internal used admin
     * property definition.
     *
     * @see PropertyDef_mxJPO#getEnumByPropName(ParameterCache_mxJPO, String)
     * @see #getPropertyNames2Enum()
     */
    private final Map<String,PropertyDef_mxJPO> propertyNames2Enum = new HashMap<String,PropertyDef_mxJPO>();

    /**
     * Maps from the name of the type definition group to the related type
     * definition group instance.
     *
     * @see TypeDef_mxJPO#defineValue(ParameterCache_mxJPO, Mapping_mxJPO, String, String)
     * @see #getTypeDef(String)
     * @see #getTypeDefMap()
     */
    private final Map<String,TypeDef_mxJPO> typeDefMap = new HashMap<String,TypeDef_mxJPO>();

    /**
     * Sorted type definition used to define the order of the update.
     *
     * @see #getAllTypeDefsSorted()
     */
    private final Set<TypeDef_mxJPO> typeDefSorted = new TreeSet<TypeDef_mxJPO>();

    /**
     * Map between the JPO name and the class name used within MX for type
     * definition.
     *
     * @see TypeDef_mxJPO#defineValue(ParameterCache_mxJPO, Mapping_mxJPO, String, String)
     * @see TypeDef_mxJPO#defineJPOClass(ParameterCache_mxJPO, Mapping_mxJPO, String)
     * @see #getTypeDefJPOsMap()
     */
    private final Map<String,String> typeDefJPOsMap = new HashMap<String,String>();

    /**
     * Maps from the name of the type definition group to the related type
     * definition group instance.
     *
     * @see TypeDefGroup_mxJPO#defineValue(Mapping_mxJPO, String, String)
     * @see #getTypeDefGroup(String)
     * @see #getAllTypeDefGroups()
     * @see #getTypeDefGroupMap()
     */
    private final Map<String,TypeDefGroup_mxJPO> typeDefGroupMap = new HashMap<String,TypeDefGroup_mxJPO>();

    /**
     * Maps from the name of the type definition tree to the related type
     * definition tree instance.
     *
     * @see TypeDefTree_mxJPO#defineValue(Mapping_mxJPO, String, String)
     * @see #getTypeDefTree(String)
     * @see #getAllTypeDefTrees()
     * @see #getTypeDefTreeMap()
     */
    private final Map<String,TypeDefTree_mxJPO> typeDefTreeMap = new HashMap<String,TypeDefTree_mxJPO>();

    /**
     *
     * @param _paramCache   parameter cache
     * @throws MatrixException if the property program {@link #PROP_NAME} could
     *                         not be read
     * @throws IOException     if the properties could not be parsed
     */
    public Mapping_mxJPO(final ParameterCache_mxJPO _paramCache)
            throws MatrixException, IOException, Exception
    {
        this.properties.putAll(this.readProperties(_paramCache));

        // map attributes and types
        for (final Map.Entry<Object,Object> entry : this.properties.entrySet())  {
            final String key = (String) entry.getKey();
            final String value = (String) entry.getValue();
            if (key.startsWith("Mode."))  {
                Mode_mxJPO.defineValue(this, key.substring(5), value);
            } else if (key.startsWith(Mapping_mxJPO.PREFIX_PARAMETERDEF))  {
                ParameterDef_mxJPO.defineValue(this, key.substring(Mapping_mxJPO.LENGTH_PARAMETERDEF), value);
            } else if (key.startsWith(Mapping_mxJPO.PREFIX_PROPERTYDEF))  {
                PropertyDef_mxJPO.defineValue(this, key.substring(Mapping_mxJPO.LENGTH_PROPERTYDEF), value);
            } else if (key.startsWith("TypeDef."))  {
                TypeDef_mxJPO.defineValue(_paramCache, this, key.substring(8), value);
            } else if (key.startsWith("TypeDefGroup."))  {
                TypeDefGroup_mxJPO.defineValue(this, key.substring(13), value);
            } else if (key.startsWith("TypeDefTree."))  {
                TypeDefTree_mxJPO.defineValue(this, key.substring(12), value);
            }
        }
        // executes the checks for the parameter definitions
        final Map<String,String> checkResult = new HashMap<String,String>();
        for (final ParameterDef_mxJPO param : this.parameterDefMap.values())  {
            param.evalCheck(_paramCache, checkResult);
        }

        this.typeDefSorted.addAll(this.typeDefMap.values());
    }

    /**
     * Reads the code of the program {@link #PROP_NAME} and evaluates the code
     * of the program as properties.
     *
     * @param _paramCache   parameter cache
     * @return read properties from MX
     * @throws MatrixException  if the program could not be read
     * @throws IOException      if the code of the program could not be parsed
     *                          as properties
     * @see #PROP_NAME
     */
    protected Properties readProperties(final ParameterCache_mxJPO _paramCache)
        throws MatrixException, IOException
    {
        final String code = MqlUtil_mxJPO.execMql(_paramCache.getContext(),
                new StringBuilder()
                        .append("print prog \"").append(Mapping_mxJPO.PROP_NAME)
                        .append("\" select code dump"),
                false);

        final InputStream is = new ByteArrayInputStream(code.getBytes());
        final Properties properties = new Properties();
        properties.load(is);

        return properties;
    }

    /**
     * Returns the mapping of the mode enumeration item and the related
     * instance holding the parameter names and description.
     *
     * @return mapping between mode enumeration and related instance
     * @see #modeMap
     */
    protected Map<Mode_mxJPO,AbstractValue_mxJPO> getModeMap()
    {
        return this.modeMap;
    }

    /**
     * Returns for given name the related parameter instance.
     *
     * @param _name name of the searched parameter instance
     * @return found parameter instance (or <code>null</code> if not found)
     * @see #parameterDefMap
     */
    public ParameterDef_mxJPO getParameterDef(final String _name)
    {
        return this.parameterDefMap.get(_name);
    }

    /**
     * Returns the list of all parameter instances.
     *
     * @return list of all parameter instances
     * see #MAP
     */
    public Collection<ParameterDef_mxJPO> getAllParameterDefs()
    {
        return this.parameterDefMap.values();
    }

    /**
     * Returns the mapping of parameter names and the related parameter
     * definition instance.
     *
     * @return mapping between type name and the related parameter definition
     *         instance
     * @see #parameterDefMap
     */
    protected Map<String,ParameterDef_mxJPO> getParameterDefMap()
    {
        return this.parameterDefMap;
    }

    /**
     * Returns the mapping between internal used admin property definitions and
     * the MX attribute names.
     *
     * @return mapping
     * @see #propertyAttributes
     */
    protected Map<PropertyDef_mxJPO,String> getPropertyAttributes()
    {
        return this.propertyAttributes;
    }

    /**
     * Returns the mapping between internal used admin property definitions and
     * the MX admin property names.
     *
     * @return mapping
     * @see #propertyEnum2Names
     */
    protected Map<PropertyDef_mxJPO,String> getPropertyEnum2Names()
    {
        return this.propertyEnum2Names;
    }

    /**
     * Returns the mapping between MX admin property name and the internal
     * used admin property definition.
     *
     * @return mapping
     * @see #propertyNames2Enum
     */
    protected Map<String,PropertyDef_mxJPO> getPropertyNames2Enum()
    {
        return this.propertyNames2Enum;
    }

    /**
     * Returns for given name the related type definition instance.
     *
     * @param _name name of the searched type definition instance
     * @return found type definition instance (or <code>null</code> if not
     *         found)
     * @see #typeDefMap
     */
    public TypeDef_mxJPO getTypeDef(final String _name)
    {
        return this.typeDefMap.get(_name);
    }

    /**
     * Returns the sorted list of all type definition instances.
     *
     * @return list of all sorted type definition instances
     * @see #typeDefSorted
     */
    public Collection<TypeDef_mxJPO> getAllTypeDefsSorted()
    {
        return this.typeDefSorted;
    }

    /**
     * Returns the mapping between the type definition name and the related
     * type definition instance.
     *
     * @return mapping between type definition name and the related type
     *         definition instance
     * @see #typeDefMap
     */
    protected Map<String,TypeDef_mxJPO> getTypeDefMap()
    {
        return this.typeDefMap;
    }

    /**
     * Returns the mapping of JPOs between names used within MX and the related
     * Java class name for type definition classes.
     *
     * @return mapping between type name and the related type definition
     *         instance
     * @see #typeDefJPOsMap
     */
    protected Map<String,String> getTypeDefJPOsMap()
    {
        return this.typeDefJPOsMap;
    }

    /**
     * Returns for given name the related type definition group instance.
     *
     * @param _name name of the searched type definition group instance
     * @return found type definition group instance (or <code>null</code> if
     *         not found)
     * @see #typeDefGroupMap
     */
    public TypeDefGroup_mxJPO getTypeDefGroup(final String _name)
    {
        return this.typeDefGroupMap.get(_name);
    }

    /**
     * Returns the list of all type definition group instances.
     *
     * @return list of all type definition group instances
     * @see #typeDefGroupMap
     */
    public Collection<TypeDefGroup_mxJPO> getAllTypeDefGroups()
    {
        return this.typeDefGroupMap.values();
    }

    /**
     * Returns the mapping between the type definition group name and the
     * related type definition group instance.
     *
     * @return mapping between type definition group name and the related type
     *         definition group instance
     * @see #typeDefGroupMap
     */
    protected Map<String,TypeDefGroup_mxJPO> getTypeDefGroupMap()
    {
        return this.typeDefGroupMap;
    }

    /**
     * Returns for given name the related type definition tree instance.
     *
     * @param _name name of the searched type definition tree instance
     * @return found type definition tree instance (or <code>null</code> if
     *         not found)
     * @see #typeDefTreeMap
     */
    public TypeDefTree_mxJPO getTypeDefTree(final String _name)
    {
        return this.typeDefTreeMap.get(_name);
    }

    /**
     * Returns the list of all type definition tree instances.
     *
     * @return list of all type definition tree instances
     * @see #typeDefTreeMap
     */
    public Collection<TypeDefTree_mxJPO> getAllTypeDefTrees()
    {
        return this.typeDefTreeMap.values();
    }

    /**
     * Returns the mapping between the type definition tree name and the
     * related type definition tree instance.
     *
     * @return mapping between type definition tree name and the related type
     *         definition tree instance
     * @see #typeDefTreeMap
     */
    protected Map<String,TypeDefTree_mxJPO> getTypeDefTreeMap()
    {
        return this.typeDefTreeMap;
    }
}
