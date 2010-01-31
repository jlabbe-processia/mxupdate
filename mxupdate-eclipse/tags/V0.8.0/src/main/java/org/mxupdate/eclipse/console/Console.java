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

package org.mxupdate.eclipse.console;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.mxupdate.eclipse.Messages;

/**
 * MxUpdate specific eclipse plug-in console.
 *
 * @author The MxUpdate Team
 * @version $Id$
 */
public class Console
        extends MessageConsole
        implements IPropertyChangeListener
{
    /**
     * Map of all console streams depending on the log level (and the different
     * colors for each console).
     *
     * @see LogLevel
     */
    private final Map<ConsolePreference,MessageConsoleStream> streams
            = new HashMap<ConsolePreference,MessageConsoleStream>();

    /**
     * Creates new MxUpdate console with all {@link #streams} depending on the
     * log levels {@link LogLevel}.
     */
    public Console()
    {
        super(Messages.getString("plugin.console.label"), null); //$NON-NLS-1$
        for (final ConsolePreference pref : ConsolePreference.values())  {
            final MessageConsoleStream stream = this.newMessageStream();
            stream.setActivateOnWrite(true);
            stream.setColor(new Color(null, pref.getRGB()));
            this.streams.put(pref, stream);
        }
        ConsolePreference.addListener(this);
    }

    /**
     *
     * @param _text     text for the info logging
     * @see #println(ConsolePreference, String, Throwable)
     */
    public void logInfo(final String _text)
    {
        this.logInfo(_text, null);
    }

    /**
     *
     * @param _text     text for the info logging
     * @param _ex       exception instance
     * @see #println(ConsolePreference, String, Throwable)
     */
    public void logInfo(final String _text,
                        final Throwable _ex)
    {
        this.println(ConsolePreference.INFO, _text, _ex);
    }

    /**
     *
     * @param _text     text for the error logging
     * @see #println(ConsolePreference, String, Throwable)
     */
    public void logError(final String _text)
    {
        this.logError(_text, null);
    }

    /**
     *
     * @param _text     text for the error logging
     * @param _ex       exception instance
     * @see #println(ConsolePreference, String, Throwable)
     */
    public void logError(final String _text,
                         final Throwable _ex)
    {
        this.println(ConsolePreference.ERROR, _text, _ex);
    }

    /**
     * Prints a text and stack trace of related exception. The MxUpdate console
     * is always shown before the text is printed.
     *
     * @param _logLevel   log level (used to add to the output)
     * @param _text       text to print
     * @param _e          exception with stack trace (or null if no exception is
     *                    defined)
     */
    private void println(final ConsolePreference _logLevel,
                         final String _text,
                         final Throwable _e)
    {
        ConsolePlugin.getDefault().getConsoleManager().showConsoleView(this);
        final MessageConsoleStream stream = this.streams.get(_logLevel);
        final StringBuilder text = new StringBuilder().append(_text);

        if (_e != null)  {
            final StringWriter sw = new StringWriter();
            _e.printStackTrace(new PrintWriter(sw));
            text.append('\n')
                .append(sw.toString());
        }
        for (final String line : text.toString().split("\n"))  {
            stream.print(_logLevel.getConsoleText());
            stream.print(" ");
            stream.println(line);
        }
        try {
            stream.flush();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace(System.out);
        }
    }

    /**
     * If the console is removed, also the event listening on the preferences
     * must be removed.
     */
    @Override
    protected void dispose()
    {
        ConsolePreference.removeListener(this);
        super.dispose();
    }

    /**
     * If the console preferences are changed with the console preference page
     * {@link ConsolePreferencesPage} all {@link #streams} must be updated to
     * new defined colors.
     *
     * @param _event    property change event (not used)
     * @see #streams
     */
    public void propertyChange(final PropertyChangeEvent _event)
    {
        for (final Map.Entry<ConsolePreference, MessageConsoleStream> entry : this.streams.entrySet())  {
            entry.getValue().setColor(new Color(null, entry.getKey().getRGB()));
        }
    }
}
