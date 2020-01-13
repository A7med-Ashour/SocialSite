package help;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {

	public static enum BODYTYPE{
			HTML ("text/html"),
			TEXT ("text/plain");
		
			private final String value;
			
			private BODYTYPE(String s) {
				this.value = s;
			}
			
			public String toString() {
				return this.value;
			}
			
		}
	
	public static void sendMail(String from,String to,String subject, BODYTYPE type,String body) throws Exception {
		
		
		 /* GET MESSAGE SESSION */
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", 587);       
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth","true");
        
        Session session = Session.getDefaultInstance(props);
        //session.setDebug(true);

        /* GET MESSAGE  */
        Message message = new MimeMessage(session);
        message.setSubject(subject);
        message.setContent(body, type.toString());

        /* ADDRESS THE MESSAGE */
        Address fromAddress = new InternetAddress(from);
        Address toAddress = new InternetAddress(to);
        message.setFrom(fromAddress);
        message.setRecipient(Message.RecipientType.TO, toAddress);

        /* SEND THE MESSAGE */
        Transport transport = session.getTransport();
        transport.connect(from,"cust2021serv");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
	}
}
