/*
 * Author   : Okan Engin Başar
 * Title    : Case Study for Bundle News App
 * Date     : 17/02/2024
 */

*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
* CASE 1
*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

CaseOneMain class contains the requested filter method with the following signature:

private static List<Integer> filter(String[][] inputArrays);

CaseOneMain.java can be run to see the output of the given array of arrays. The output will be written to stdout.

*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*



*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
* CASE 2
*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

1. Install and run Apache ActiveMQ as described below in the MQ SETUP section
2. Run socket server and MQ consumers:
	SocketServerApp.java 
	ActiveMQToMongoDBApp.java 
	ActiveMQToSQLApp.java
3. Run SocketClientApp.java

Known issues:
- Instead of throwing RuntimeExceptions, better logging should be applied. Log4j is already added to dependencies.
- Constants containing username/password values should be encrypted and stored out of the codebase.
- Unit tests and integration tests are missing. jUnit is already added to dependencies.
- Apache ActiveMQ installation is done. MySQL database and MongoDB installations are not done and could not be tested. 
- All apps are considered to have a single server architecture. Distributed multi-cluster installations are not considered.
- Single client-server communication is tested. Multi-client connections are not considered.
- The output file is not emptied. Data is appended to the same file on different runs and should be emptied manually.
- No health check services are implemented for apps. In case of an error or exception, the app will terminate and not restart automatically.

*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*



*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
* CASE 2 - MQ SETUP
*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

1. Download Apache ActiveMQ from https://activemq.apache.org/components/artemis/download/

2. Unzip the achieve and run the following command:

bin/artemis create ~/mq --user admin --password admin

3. Respond "Allow anonymous access?" as Y

4. MQ is installed. Now run with the following command:

~/mq/bin/artemis run

5. MQ is up and ready! Listening on default port 61616

*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
