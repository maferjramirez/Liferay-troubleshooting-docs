import com.liferay.mail.kernel.model.MailMessage
import com.liferay.mail.kernel.service.MailServiceUtil
import javax.mail.internet.InternetAddress

try {

    String remitenteEmail = "noreply@tudominio.com"
    String remitenteNombre = "Liferay Test Admin"
    String destinatarioEmail = "tu_correo@destino.com"
    String destinatarioNombre = "Usuario Destino"
    
    String asunto = "Prueba de envío de correo - Liferay"
    String cuerpo = """
        <h3>¡Prueba exitosa!</h3>
        <p>Este es un correo de prueba enviado desde <strong>Liferay DXP</strong>.</p>
        <p>Fecha y hora: ${new Date()}</p>
    """

    InternetAddress from = new InternetAddress(remitenteEmail, remitenteNombre)
    InternetAddress to = new InternetAddress(destinatarioEmail, destinatarioNombre)

    MailMessage mailMessage = new MailMessage()
    mailMessage.setFrom(from)
    mailMessage.setTo(to)
    mailMessage.setSubject(asunto)
    mailMessage.setBody(cuerpo)
    mailMessage.setHTMLFormat(true)

    MailServiceUtil.sendEmail(mailMessage)

    out.println("Mensaje enviado correctamente. " + destinatarioEmail)
    out.println("Revisa los logs del servidor para confirmar que el servidor SMTP procesó la entrega.")

} catch (Exception e) {
    out.println("Error al intentar enviar el correo: " + e.getMessage())
    e.printStackTrace(out)
}
