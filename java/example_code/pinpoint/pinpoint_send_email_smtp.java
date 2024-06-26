// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0

// snippet-start:[pinpoint.java.pinpoint_send_email_smtp.complete]

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

    // If you're using Amazon Pinpoint in a region other than US West (Oregon),
    // replace email-smtp.us-west-2.amazonaws.com with the Amazon Pinpoint SMTP
    // endpoint in the appropriate AWS Region.
    static final String smtpEndpoint = "email-smtp.us-west-2.amazonaws.com";

    // The port to use when connecting to the SMTP server.
    static final int port = 587;

    // Replace sender@example.com with your "From" address.
    // This address must be verified with Amazon Pinpoint.
    static final String senderName = "Mary Major";
    static final String senderAddress = "sender@example.com";

    // Replace recipient@example.com with a "To" address. If your account
    // is still in the sandbox, this address must be verified. To specify
    // multiple addresses, separate each address with a comma.
    static final String toAddresses = "recipient@example.com";

    // CC and BCC addresses. If your account is in the sandbox, these
    // addresses have to be verified. To specify multiple addresses, separate
    // each address with a comma.
    static final String ccAddresses = "cc-recipient0@example.com,cc-recipient1@example.com";
    static final String bccAddresses = "bcc-recipient@example.com";

    // Replace smtp_username with your &PINlong; SMTP user name.
    static final String smtpUsername = "AKIAIOSFODNN7EXAMPLE";

    // Use @ASMlong; to expose your &PIN; SMTP password.
    static string smtpPassword = System.getEnv("SMTP_PASSWORD");

    // (Optional) the name of a configuration set to use for this message.
    static final String configurationSet = "ConfigSet";

    // The subject line of the email
    static final String subject = "Amazon Pinpoint test (SMTP interface accessed using Java)";

    // The body of the email for recipients whose email clients don't
    // support HTML content.
    static final String htmlBody = String.join(
            System.getProperty("line.separator"),
            "<h1>Amazon Pinpoint SMTP Email Test</h1>",
            "<p>This email was sent with Amazon Pinpoint using the ",
            "<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
            " for <a href='https://www.java.com'>Java</a>.");

    // The message tags that you want to apply to the email.
    static final String tag0 = "key0=value0";
    static final String tag1 = "key1=value1";

    public static void main(String[] args) throws Exception {

        // Create a Properties object to contain connection configuration information.
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified
        // properties.
        Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information.
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(senderAddress, senderName));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddresses));
        msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccAddresses));
        msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bccAddresses));

        msg.setSubject(subject);
        msg.setContent(htmlBody, "text/html");

        // Add headers for configuration set and message tags to the message.
        msg.setHeader("X-SES-CONFIGURATION-SET", configurationSet);
        msg.setHeader("X-SES-MESSAGE-TAGS", tag0);
        msg.setHeader("X-SES-MESSAGE-TAGS", tag1);

        // Create a transport.
        Transport transport = session.getTransport();

        // Send the message.
        try {
            System.out.println("Sending...");

            // Connect to Amazon Pinpoint using the SMTP username and password you specified
            // above.
            transport.connect(smtpEndpoint, smtpUsername, smtpPassword);

            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email wasn't sent. Error message: "
                    + ex.getMessage());
        } finally {
            // Close the connection to the SMTP server.
            transport.close();
        }
    }
}

// snippet-end:[pinpoint.java.pinpoint_send_email_smtp.complete]
