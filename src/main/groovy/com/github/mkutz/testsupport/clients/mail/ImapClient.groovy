/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.github.mkutz.testsupport.clients.mail

import javax.mail.Flags
import javax.mail.Folder
import javax.mail.Message
import javax.mail.Session
import javax.mail.Store
import javax.mail.Message.RecipientType
import javax.mail.search.AndTerm
import javax.mail.search.FromStringTerm
import javax.mail.search.RecipientStringTerm
import javax.mail.search.SearchTerm
import javax.mail.search.SubjectTerm

class ImapClient {

    String host
    String username
    String recipient
    String password
    int port = 143

    private Session session
    private Store store
    private Folder inbox
    private Properties properties = properties

    /**
     * Constructor.
     * 
     * @param recipient the recipient's e-mail address as a String.
     * @param host host name of the IMAP server
     * @param username the user name to connect to the IMAP server.
     * @param password the password to authenticate at the IMAP server.
     * @param port port to connect to on the IMAP server (default is 143).
     * 
     * @throws AddressException if the given <code>recipient</code> is not a valid {@link InternetAddress}
     */
    def ImapClient(String recipient, String host, String username, String password, int port = 143) {
        this.recipient = recipient
        this.host = host
        this.username = username
        this.password = password
        this.port = port

        this.properties = new Properties()
        this.properties.setProperty("mail.store.protocol", "imap")
        this.properties.setProperty("mail.imap.host", host)
        this.properties.setProperty("mail.imap.port", "${port}")
    }

    /**
     * @return the all {@link Message}s sent to the given <code>recipient</code>.
     */
    public List<Message> getMessages() {
        SearchTerm searchTerm = new AndTerm(new RecipientStringTerm(RecipientType.TO, recipient))
        return openInbox().search(searchTerm)
    }

    /**
     * @param subjectPattern pattern for the subject of the desired message (see {@link SubjectTerm}).
     * @return all {@link Message}s containing the given <code>pattern</code> in the mail subject.
     */
    public List<Message> getMessagesBySubjectPattern(String subjectPattern) {
        SearchTerm searchTerm = new AndTerm(new SubjectTerm(subjectPattern),
                new RecipientStringTerm(RecipientType.TO, recipient))
        return openInbox().search(searchTerm)
    }

    /**
     * @param fromPattern from address pattern of the desired message (see {@link FromTerm}).
     * @return all {@link Message}s from the given <code>fromAddress</code>.
     */
    public List<Message> getMessagesByFromPattern(String fromPattern) {
        SearchTerm searchTerm = new AndTerm(new FromStringTerm(fromPattern),
                new RecipientStringTerm(RecipientType.TO, recipient))
        return openInbox().search(searchTerm)
    }

    /**
     * @param subjectPattern pattern for the subject of the desired message (see {@link SubjectTerm}).
     * @return the latest {@link Message} sent to the given <code>recipient</code>.
     */
    public Message getLatestMessage(String subjectPattern = "") {
        Message latestMessage = null

        Message[] messages = getMessages(subjectPattern)
        if (messages.length > 0) {
            latestMessage = messages[-1]
        }

        return latestMessage
    }

    /**
     * Deletes all messages sent to {@link #recipient} from the inbox.
     */
    public void cleanInbox() {
        openInbox().getMessages().each { it.setFlag(Flags.Flag.DELETED, true) }
    }

    private void deleteMessage(Message message) {
        message.setFlag(Flags.Flag.DELETED, true)
    }

    private Session getSession() {
        if (session == null) {
            session = Session.getDefaultInstance(properties, null)
        }
        return session
    }

    private Store getStore() {
        if (store == null || !store.connected) {
            store = getSession().getStore("imap")
            store.connect(host, port, username, password)
        }
        return store
    }

    private Folder openInbox() {
        if (inbox != null) {
            inbox.close(true)
            inbox = null
        }
        inbox = getStore().getFolder("INBOX")
        inbox.open(Folder.READ_WRITE)

        return inbox
    }

    void finalize() {
        inbox.close(true)
        store.close()
    }
}