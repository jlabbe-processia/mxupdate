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

package org.mxupdate.update.userinterface;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.mxupdate.update.util.StringUtil_mxJPO.convertTcl;

/**
 *
 * @author tmoxter
 * @version $Id$
 */
class TableColumn_mxJPO
{
    String name = null;
    String label = null;
    String href = null;
    String rangeHref = null;
    String alt = null;

    String expression = null;
    boolean isBusinessObject = false;
    boolean isRelationship = false;

    String sortType = null;

    String lastSetting = null;

    final Stack<Setting_mxJPO> settings = new Stack<Setting_mxJPO>();

    final Set<String> users = new TreeSet<String>();

    void parse(final String _url,
               final String _content)
    {
        if ("/alt".equals(_url))  {
            this.alt = _content;
        } else if ("/href".equals(_url))  {
            this.href = _content;
        } else if ("/rangeHref".equals(_url))  {
            this.rangeHref = _content;
        } else if ("/label".equals(_url))  {
            this.label = _content;
        } else if ("/name".equals(_url))  {
            this.name = _content;
        } else if ("/sortType".equals(_url))  {
            this.sortType = _content;

        } else if ("/expression".equals(_url) || "/fieldValue".equals(_url))  {
            this.expression = _content;
        } else if ("/usesBusinessObject".equals(_url))  {
            this.isBusinessObject = true;
        } else if ("/usesRelationship".equals(_url))  {
            this.isRelationship = true;

        } else if ("/global".equals(_url))  {
            this.users.add("all");
        } else if ("/fieldUserList/user".equals(_url))  {
            this.users.add(_content);

        } else if ("/fieldSettingList/fieldSetting".equals(_url))  {
            this.settings.add(new Setting_mxJPO());
        } else if ("/fieldSettingList/fieldSetting/fieldSettingName".equals(_url))  {
            this.settings.peek().name = _content;
        } else if ("/fieldSettingList/fieldSetting/fieldSettingValue".equals(_url))  {
            this.settings.peek().value = _content;
        }
    }

    void write(final Writer _out)
            throws IOException
    {
        _out.append(" \\\n        name \"").append(convertTcl(this.name)).append("\"")
            .append(" \\\n        label \"").append((this.label != null) ? convertTcl(this.label) : "").append("\"");
        if (isBusinessObject && (this.expression != null))  {
            _out.append(" \\\n        businessobject \"").append(convertTcl(this.expression)).append("\"");
        }
        if (isRelationship && (this.expression != null))  {
            _out.append(" \\\n        relationship \"").append(convertTcl(this.expression)).append("\"");
        }

        _out.append(" \\\n        range \"").append((this.rangeHref != null) ? convertTcl(this.rangeHref) : "").append("\"")
            .append(" \\\n        href \"").append((this.href != null) ? convertTcl(this.href) : "").append("\"")
            .append(" \\\n        alt \"").append((this.alt != null) ? convertTcl(this.alt) : "").append("\"");
        for (final String user : this.users)  {
            _out.append(" \\\n        user \"").append(convertTcl(user)).append("\"");
        }
        if ("3".equals(this.sortType))  {
            _out.append(" \\\n        sorttype other");
        }

        final Map<String,String> tmpSettings  = new TreeMap<String,String>();
        for (final Setting_mxJPO setting : this.settings)  {
            tmpSettings.put(setting.name, setting.value);
        }
        for (final Map.Entry<String,String> setting : tmpSettings.entrySet())  {
            final String value = (setting.getValue() == null)
                                 ? ""
                                 : setting.getValue();
            _out.append(" \\\n        setting \"").append(convertTcl(setting.getKey())).append("\" \"").append(convertTcl(value)).append("\"");
        }
    }

    /**
     * Returns the name of the form field / table column. The method is the
     * getter method for instance variable {@see #name}.
     *
     * @return name of field / column name
     * @see #name
     */
    public String getName()
    {
        return this.name;
    }

    @Override
    public String toString()
    {
        return "[name="+name+", label="+label+", expression="+expression+", sortType="+sortType+", settings="+this.settings+"]";
    }
}