package help;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
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
	
	public static void sendMail(String from,String to,String subject, BODYTYPE type,String body) {
		
		
		 /* GET MESSAGE SESSION */
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", 587);       
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.quitwait","false");
        Session session = Session.getDefaultInstance(props);
        //session.setDebug(true);

        /* GET MESSAGE  */
        Message message = new MimeMessage(session);
        
        Transport transport = null;
        
        try {
			message.setSubject(subject);
			 message.setContent(body, type.toString());
			 
			 /* ADDRESS THE MESSAGE */
	        Address fromAddress = new InternetAddress(from);
	        Address toAddress = new InternetAddress(to);
	        message.setFrom(fromAddress);
	        message.setRecipient(Message.RecipientType.TO, toAddress);
	        
	        /* SEND THE MESSAGE */
	        transport = session.getTransport();
	        transport.connect(from,"cust2021serv");
	        transport.sendMessage(message, message.getAllRecipients());
		        
		} catch (MessagingException e) {
			e.printStackTrace();
		}finally {
			if(transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
       

        

       
	}
}
