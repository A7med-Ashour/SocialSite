package help;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {

	public static enum BODYTYPE{TEXT_HTML,TEXT_PLAIN};
	public static void sendMail(String from,String to,String subject, BODYTYPE type,String body) {
		
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", "localhost");
		props.put("mail.smtp.port", 25);
		
		Session session = Session.getInstance(props);
		session.setDebug(true);
		
		Message message = new MimeMessage(session);
		
		try {
			message.setFrom(new InternetAddress(from));
			message.setRecipient(Message.RecipientType.TO,new InternetAddress(to) );
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
