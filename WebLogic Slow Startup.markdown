# WebLogic Slow Startup

This is because JVM library used for random number generation relies on  /dev/random  by default for UNIX platforms. This can potentially slow down the startup before returning a result.

Although `/dev/random`  is more secure, using  `/dev/urandom`  instead if the startup is slow. 

Testing libraries diff
	
    [liferay@liferay-desarrollo@AWS 09:25:38 ~]
    $ time head -1 /dev/random
    2?????c??????8???mtz?0KY??4 .]?\???^&?xTH	
    
    real	9m54.610s
    user	0m0.000s
    sys	0m0.002s
    
    [liferay@liferay-desarrollo@AWS 09:35:40 ~]
    $ time head -1 /dev/urandom
    ?J?As?py????{18????X2??i?@[?{
                                 ?Z?|?y??z??
                                            ?????D՝g3?]2u??w????W]?6?JN~??ʤ?j]l:?L?/OY}?
    
    real	0m0.002s
    user	0m0.000s
    sys	0m0.002s

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
