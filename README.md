System Test Support
===================

This library's purpose it to support black box system test projects. It includes a variety of convenient test client
classes that can be used by any Java based test.

To use this just include the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>com.github.mkutz.testsupport</groupId>
    <artifactId>system-test-support</artifactId>
    <version>1.0</version>
</dependency>
```

IMAP client
-----------

The `ImapClient`'s original purpose testing a common web registration workflow if which a user may enter their email
address (and further data) and will receive an email with a confirmation link to verify the given address is valid.

Example: getting the latest message for a user and verifying its subject:
```groovy
ImapClient imap = new ImapClient("my@email.com", "imaphost.email.com", "myimapusername", "myimappassword", 993)
assert imap.getLatestMessage().subject == "expected subject"
```

REST client
-----------

It should be used by extending it to create a client specifically for your REST service. It provides
convenient methods to work with a service document concept.

SSH client
----------

The `SshClient` is a full featured SSH client implementation. It allows to execute a command on a remote machine as
well as reading, writing or deleting files on a remote system.

It allows username/password as well as public/private key authentication.

Example: username/password authentication + verify that a log file contains an expected line:
```groovy
SshClient ssh = new SshClient("somehost").withUsername("username").withPassword("password")
ssh.readRemoteFile("/var/log/myapp.log").contains("successfully executed something")
```

Example: RSA public/private key authentication + execute an application and verify its exit code and output:
```groovy
SshClient ssh = new SshClient("somehost", 999).withUsername("username").withRsaKey("${userHomeDirectory}/.ssh/id_rsa", "rsapassphrase)
SshExecutionResult result = ssh.executeCommand("/etc/init.d/myapp restart")
result.exitStatus == 0
result.output.contains("OK myapp started")
```
