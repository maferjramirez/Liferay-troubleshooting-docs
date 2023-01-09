# The configured protocol [org.apache.coyote.http11.Http11AprProtocol] requires the APR/native library which is not available

 

> 09-Jan-2023 16:51:29.446 **SEVERE** [main]
> org.apache.catalina.util.LifecycleBase.handleSubClassException **Failed
> to initialize component**
> [Connector[org.apache.coyote.http11.Http11AprProtocol-8443]]  
>     org.apache.catalina.LifecycleException: The configured protocol [org.apache.coyote.http11.Http11AprProtocol] **requires the APR/native
> library which is not available**


Tomcat can use the  [Apache Portable Runtime](https://apr.apache.org/)  to provide superior scalability, performance, and better integration with native server technologies. The Apache Portable Runtime is a highly portable library that is at the heart of Apache HTTP Server 2.x. APR has many uses, including access to advanced IO functionality (such as sendfile, epoll and OpenSSL), OS level functionality (random number generation, system status, etc), and native process handling (shared memory, NT pipes and Unix sockets).

These features allows making Tomcat a general purpose webserver, will enable much better integration with other native web technologies, and overall make Java much more viable as a full fledged webserver platform rather than simply a backend focused technology.

## Solution
APR support requires three main native components to be installed:

-   APR library
-   JNI wrappers for APR used by Tomcat (libtcnative)
-   OpenSSL libraries

#### Linux

Most Linux distributions will ship packages for APR and OpenSSL. The JNI wrapper (libtcnative) will then have to be compiled. It depends on APR, OpenSSL, and the Java headers.

Requirements:

-   APR 1.2+ development headers (libapr1-dev package)
-   OpenSSL 1.0.2+ development headers (libssl-dev package)
-   JNI headers from Java compatible JDK 1.4+
-   GNU development environment (gcc, make)

The wrapper library sources are located in the Tomcat binary bundle, in the  `bin/tomcat-native.tar.gz`  archive. Once the build environment is installed and the source archive is extracted, the wrapper library can be compiled using (from the folder containing the configure script):

***it could be necessary***
```
> yum groupinstall "Development Tools"  
> cd tomcat-native-1.2.33-src/native/  
> yum install apr-devel openssl-devel
```

***Solve using***
```
./configure && make && make install
```
