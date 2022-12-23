# WebLogic Slow Startup

This is because JVM library used for random number generation relies on  /dev/random  by default for UNIX platforms. This can potentially slow down the startup before returning a result.

Although `/dev/random`  is more secure, using  `/dev/urandom`  instead if the startup is slow. 

Testing libraries diff

## Solutions
For various scopes, here are some treatments that can speed up the startup of a WebLogic server.

### Java Scope
Modify the setting in  java.security  of the default JVM.

    [root@admin ~]$ vi /usr/java/default/jre/lib/security/java.security  
    ...  
    securerandom.source=file:/dev/urandom

### Session Scope
Alternatively, we can add a  JAVA_OPTIONS  environment variable for every logon session of the user.

    [oracle@admin ~]$ vi .bash_profile  
    ...  
    export JAVA_OPTIONS=-Djava.security.egd=file:/dev/./urandom  
    ...  
    [oracle@admin ~]$ . .bash_profile
### Domain Scope

Although we can set  JAVA_OPTIONS  in the session scope, adding it to the scripts is a more selective way to do it, no matter where we execute them. For example, exporting  JAVA_OPTIONS  in the beginning of  startWebLogic.sh:

    [oracle@admin ~]$ vi $DOMAIN_HOME/bin/startWebLogic.sh  
    #!/bin/sh  
    export JAVA_OPTIONS=-Djava.security.egd=file:/dev/./urandom
