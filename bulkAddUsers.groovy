import com.liferay.portal.scripting.executor.groovy.*
import com.liferay.portal.kernel.service.*
import com.liferay.portal.kernel.model.*
import com.liferay.portal.kernel.util.*

USERS = [
	['user03@test.com', 'firstName3', 'lastName3']
]

PASSWORD = 'temporal_2024.'

long[] groupIds = [40769]
long[] roleIds = [40517]
long creatorUserId = 0
long companyId = PortalUtil.getDefaultCompanyId()

Locale locale = LocaleUtil.getMostRelevantLocale()
boolean autoPassword = false
String password2 = PASSWORD
boolean autoScreenName = true
String screenName = "autogenerated"
long facebookId = 0
String openId = ""
String middleName = ""
int prefixId = 0
int suffixId = 0
boolean male = true
int birthdayMonth = Calendar.JANUARY
int birthdayDay = 1
int birthdayYear = 1970
String jobTitle = ""
long[] organizationIds = null
long[] userGroupIds = null
boolean sendEmail = false
ServiceContext serviceContext = new ServiceContext()

try {

	USERS.each{ 
		(emailAddress, firstName, lastName) = it
		
		User user = UserLocalServiceUtil.addUser(creatorUserId, PortalUtil.getDefaultCompanyId(), autoPassword, PASSWORD, password2, 
			autoScreenName, screenName, emailAddress, facebookId, openId, locale, firstName, middleName, lastName, 
			prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds, 
			roleIds, userGroupIds, sendEmail, serviceContext)

		// No password
		user.setPasswordReset(false);
		// No reminder query at first login.
		user.setReminderQueryQuestion("x")
		user.setReminderQueryAnswer("y")
		UserLocalServiceUtil.updateUser(user)

		out.println(emailAddress + ' - '+user.getUserId())
	}
} catch (e) {
	e.printStackTrace(out)
}
