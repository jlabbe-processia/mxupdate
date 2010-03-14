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

package org.mxupdate.eclipse.mxadapter.connectors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.sshd.ClientChannel;
import org.apache.sshd.ClientSession;
import org.apache.sshd.SshClient;
import org.apache.sshd.common.util.NoCloseInputStream;
import org.apache.sshd.common.util.NoCloseOutputStream;
import org.mxupdate.eclipse.Activator;
import org.mxupdate.eclipse.Messages;

/**
 * Connector to MX via SSH and executed MQL console.
 *
 * @author The MxUpdate Team
 * @version $Id$
 */
public class SSHConnector
    extends AbstractConnector
{
    /**
     * String which returns from the print context used to check the result of
     * an execution.
     */
    private static final String CHECK_CONTEXT = "context vault "; //$NON-NLS-1$

    /**
     * MQL statement to print the current context which is used to check for
     * the result of an execution.
     */
    private static final String PRINT_CONTEXT = "print context;"; //$NON-NLS-1$

    /**
     * SSH client.
     */
    private final SshClient client;

    /**
     * SSH client session.
     */
    private final ClientSession session;

    /**
     * Client channel.
     */
    private final ClientChannel channel;

    /**
     * Must the MX communication logged?
     *
     * @see #in
     * @see #logInput
     * @see #out
     * @see #logOutput
     * @see #err
     * @see #logError
     */
    private final boolean log;

    /**
     * Buffer for the input logging.
     *
     * @see #logInput(char)
     */
    private final StringBuilder logInput = new StringBuilder();

    /**
     * Buffer for the output logging.
     *
     * @see #logOutput(byte[], int, int)
     */
    private final StringBuilder logOutput = new StringBuilder();

    /**
     * Buffer for the error logging.
     *
     * @see #logError(char)
     */
    private final StringBuilder logError = new StringBuilder();

    /**
     * Input buffer used from the {@link #in} to store the input from the SSH
     * server / MQL console.
     */
    private final Vector<Integer> inBuf = new Vector<Integer>();

    /**
     * Input stream from the SSH server / MQL console.
     */
    private final OutputStream in = new OutputStream()  {

        @Override()
        public void write(final int _char)
            throws IOException
        {
            SSHConnector.this.inBuf.add(_char);
            if (SSHConnector.this.log)  {
                SSHConnector.this.logInput((char) _char);
            }
        }
    };

    /**
     * Input buffer used from the {@link #err} to store the errors from the SSH
     * server / MQL console.
     */
    private final Vector<Integer> errBuf = new Vector<Integer>();

    /**
     * Error stream from the SSH server / MQL console.
     */
    private final OutputStream err = new OutputStream()  {

        @Override()
        public void write(final int _char)
            throws IOException
        {
            SSHConnector.this.errBuf.add(_char);
            if (SSHConnector.this.log)  {
                SSHConnector.this.logError((char) _char);
            }
        }
    };

    /**
     * Output buffer used from {@link #out} to buffer the values which must be
     * sent to the SSH server.
     */
    private final Vector<String> chars = new Vector<String>();

    /**
     * Output stream to the SSH server / MQL console.
     */
    private final InputStream out = new InputStream()  {

        @Override()
        public int read() throws IOException
        {
            throw new IOException("not implemented"); //$NON-NLS-1$
        }

        @Override()
        public int read(final byte[] _abyte0,
                        final int _start,
                        final int _len)
            throws IOException
        {
            int ret = 0;

            if (!SSHConnector.this.chars.isEmpty())  {
                final byte[] buf = SSHConnector.this.chars.remove(0).getBytes();
                if (buf.length > _len)  {
                    ret = _len;
                    final byte[] tmp = new byte[buf.length - _len];
                    System.arraycopy(buf, _len, tmp, 0, buf.length - _len);
                    SSHConnector.this.chars.add(0, new String(tmp));
                } else  {
                    ret = buf.length;
                }
                System.arraycopy(buf, 0, _abyte0, _start, ret);
            }

            if (SSHConnector.this.log)  {
                SSHConnector.this.logOutput(_abyte0, _start, ret);
            }

            return ret;
        }
    };

    /**
     * Initializes / opens the SSH connection to the SSH server and starts the
     * MQL console on this SSH server.
     *
     * @param _sshServer            name of the SSH server
     * @param _sshPort              port of the SSH server
     * @param _sshUser              user of the SSH server
     * @param _sshPassword          password of the SSH server
     * @param _mqlPath              path of the MQL program on the SSH server
     * @param _mqlUser              name of the MX user
     * @param _mqlPassword          password of the MX user
     * @param _log                  must the MX communication logged?
     * @param _updateByFileContent  must the update done by sending also the
     *                              file content?
     * @throws Exception if the SSH connection could not be opened
     */
    public SSHConnector(final String _sshServer,
                        final int _sshPort,
                        final String _sshUser,
                        final String _sshPassword,
                        final String _mqlPath,
                        final String _mqlUser,
                        final String _mqlPassword,
                        final boolean _log,
                        final boolean _updateByFileContent)
        throws Exception
    {
        super(_updateByFileContent);

        this.log = _log;

        this.client = SshClient.setUpDefaultClient();
        this.client.start();

        this.session = this.client.connect(_sshServer, _sshPort).await().getSession();
        this.session.authPassword(_sshUser, _sshPassword);

        final int ret = this.session.waitFor(ClientSession.WAIT_AUTH | ClientSession.CLOSED | ClientSession.AUTHED, 0);
        if ((ret & ClientSession.CLOSED) != 0) {
            throw new Exception(Messages.getString("MxSSHClient.SSHServerClosed", _sshServer)); //$NON-NLS-1$
        }
        if ((ret & ClientSession.WAIT_AUTH) != 0)  {
            throw new Exception(Messages.getString("MxSSHClient.WrongSSHPassword", _sshServer)); //$NON-NLS-1$
        }
        this.channel = this.session.createChannel(ClientChannel.CHANNEL_EXEC, _mqlPath + " -k -t\n"); //$NON-NLS-1$

        this.channel.setIn(new NoCloseInputStream(this.out));
        this.channel.setOut(new NoCloseOutputStream(this.in));

        this.channel.setErr(new NoCloseOutputStream(this.err));
        this.channel.open();

        // login into MQL (and check if not failed!)
        final StringBuilder cmd = new StringBuilder()
                .append("escape set context user \"").append(this.convertMql(_mqlUser)) //$NON-NLS-1$
                        .append("\" pass \"").append(this.convertMql(_mqlPassword)).append("\";") //$NON-NLS-1$ //$NON-NLS-2$
                .append(SSHConnector.PRINT_CONTEXT)
                .append('\n');
        this.chars.add(cmd.toString());
        final String bck = this.readLine();
        // check if login was successfully
        if (!bck.startsWith(SSHConnector.CHECK_CONTEXT))  {
            this.disconnect();
            throw new Exception(Messages.getString("MxSSHClient.LoginFailed")); //$NON-NLS-1$
        }
        if (!this.errBuf.isEmpty())  {
            this.disconnect();
            throw new Exception(Messages.getString("MxSSHClient.LoginFailedMQLError", this.readError())); //$NON-NLS-1$
        }
    }

    /**
     * {@inheritDoc}
     * Calls directly the dispatcher in the MQL console for given arguments
     * within one line. In this line also a
     * {@link #PRINT_CONTEXT print context} is made so that in the case of an
     * error also a value is returned. In this error case an exception is
     * thrown with the text from the {@link #err error stream}.
     */
    public String execute(final String _arg1,
                          final String _arg2,
                          final String _arg3)
        throws Exception
    {
        // prepare MQL statement with encoded parameters
        final StringBuilder cmd = new StringBuilder()
            .append("exec prog ").append("org.mxupdate.plugin.Dispatcher \"") //$NON-NLS-1$ //$NON-NLS-2$
            .append(_arg1).append("\" \"") //$NON-NLS-1$
            .append(_arg2).append("\" \"") //$NON-NLS-1$
            .append(_arg3).append("\";") //$NON-NLS-1$
            .append(SSHConnector.PRINT_CONTEXT)
            .append('\n');

        this.chars.add(cmd.toString());

        // get result (must not be the context, otherwise an error happened!)
        final String ret = this.readLine();
        if (ret.startsWith((SSHConnector.CHECK_CONTEXT)))  {
            throw new Exception(Messages.getString("MxSSHClient.ExecuteFailed", this.readError())); //$NON-NLS-1$
        }

        // in second line the context info must be returned
        final String checkLine = this.readLine();
        if (!checkLine.startsWith((SSHConnector.CHECK_CONTEXT)))  {
            throw new Exception(Messages.getString("MxSSHClient.ExecuteFailed", this.readError())); //$NON-NLS-1$
        }

        return ret;
    }

    /**
     * Reads one line from the console. The method waits till the line is
     * complete.
     *
     * @return string of one line
     * @throws InterruptedException if the thread is interrupted
     */
    protected String readLine()
        throws InterruptedException
    {
        while (!this.inBuf.contains(10))  {
            Thread.sleep(1000);
        }

        final StringBuilder ret = new StringBuilder();
        int bck;
        while ((bck = this.inBuf.remove(0)) != 10)  {
            ret.append((char) bck);
        }

        return ret.toString();
    }

    /**
     * Reads current stack of read errors.
     *
     * @return error string
     * @see #errBuf
     */
    protected String readError()
    {
        final StringBuilder ret = new StringBuilder();
        while (!this.errBuf.isEmpty())  {
            ret.append((char) ((int) this.errBuf.remove(0)));
        }
        return ret.toString();
    }

    /**
     * Closes the {@link #session} and the {@link #client}.
     */
    public void disconnect()
    {
        try  {
            this.session.close(false);
        } finally {
            this.client.stop();
        }
    }

    /**
     * Converts given string by escaping the &quot; so that in escape mode on
     * string could be handled with &quot; and '.
     *
     * @param _text     character stream to convert
     * @return converted string
     */
    protected String convertMql(final CharSequence _text)
    {
        return (_text != null)
               ? _text.toString().replaceAll("\\\\", "\\\\\\\\") //$NON-NLS-1$ //$NON-NLS-2$
                                 .replaceAll("\\\"", "\\\\\"") //$NON-NLS-1$ //$NON-NLS-2$
               : ""; //$NON-NLS-1$
    }

    /**
     * Used to log the MX communication for the input purpose. The
     * {@link #logInput} buffer is written to the MX console if a new line is
     * sent from the {@link #in input stream}.
     *
     * @param _char     character to log
     * @see #in
     * @see #logInput
     */
    protected void logInput(final char _char)
    {
        synchronized(this.logInput)  {
            if (_char == '\n')  {
                Activator.getDefault().getConsole().logTrace(">INBOUND: " + this.logInput.toString());
                this.logInput.delete(0, this.logInput.length());
            } else  {
                this.logInput.append(_char);
            }
        }
    }

    /**
     * Used to log the MX communication for the error purpose. The
     * {@link #logError} buffer is written to the MX console if a new line is
     * sent from the {@link #out output stream}.
     *
     * @param _buffer       byte array with the logging characters
     * @param _start        start position within the <code>_buffer</code>
     * @param _len          length
     * @see #out
     * @see #logOutput
     */
    protected void logOutput(final byte[] _buffer,
                             final int _start,
                             final int _len)
    {
        synchronized(this.logOutput)  {
            for (int idx = _start; idx < (_start + _len); idx++)  {
                if (_buffer[idx] == (byte) '\n')  {
                    Activator.getDefault().getConsole().logTrace("<OUTBOUND: " + this.logOutput.toString());
                    this.logOutput.delete(0, this.logOutput.length());
                } else  {
                    this.logOutput.append((char) _buffer[idx]);
                }
            }
        }
    }

    /**
     * Used to log the MX communication for the error purpose. The
     * {@link #logError} buffer is written to the MX console if a new line is
     * sent from the {@link #err error stream}.
     *
     * @param _char     character to log
     * @see #err
     * @see #logError
     */
    protected void logError(final char _char)
    {
        synchronized(this.logError)  {
            if (_char == '\n')  {
                Activator.getDefault().getConsole().logTrace(">SSH-ERROR: " + this.logError.toString());
                this.logError.delete(0, this.logError.length());
            } else  {
                this.logError.append(_char);
            }
        }
    }
}
