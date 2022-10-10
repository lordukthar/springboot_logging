# springboot_logging

Add zalando logging which produces this in the console

LogFormatUtils.java -> traceDebug(119) - [57acd445-1] HTTP GET "/aja/users" 
AbstractHandlerMapping.java -> lambda$getHandler$1(189) - [57acd445-1] Mapped to com.example.demo_logging.routes.UserRoutes$$Lambda$749/0x00000008010c6a90@83c77f9
CompositeLog.java -> debug(127) - [57acd445-1] Encoding [User[name=Wim]]
CompositeLog.java -> debug(127) - [57acd445-1] Encoding [User[name=Simon]]
CompositeLog.java -> debug(127) - [57acd445-1] Encoding [User[name=Siva]]
CompositeLog.java -> debug(127) - [57acd445-1] Encoding [User[name=Josh]]
LogFormatUtils.java -> traceDebug(119) - [57acd445-1] Completed 200 OK

Then I added logback and MDC which pr´oduces this
DEBUG Request-ID 261dc891-11fc-4ad1-99b4-160e181449ed 

This request ID is returned as a reponse header like this

exchange.getResponse()
.getHeaders().add("requestId",  uniqueId.toString());

Can bee seen with curl/postman when calling:

http://localhost:8080/aja/users

#https://youtrack.jetbrains.com/issue/IDEA-234930/Spring-Webflux-support-references-to-request-parameters-in-the-RouterFunction-predicates
#https://jmeter.apache.org/usermanual/component_reference.html#HTTP_Header_Manager