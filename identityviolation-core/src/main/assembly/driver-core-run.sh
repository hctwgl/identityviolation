#!/usr/bin/env bash
cd /mapbar/app/tomcat/driver-core
export LC_CTYPE=zh_CN.GB18030
path='/mapbar/app/tomcat/driver-core'
year=`date +%Y`
month=`date +%m`
day=`date +%d`
echo Running Spider...

if [ ! -d $path/logs/$year/$month/$day ];then
        mkdir -p $path/logs/$year/$month/$day
fi

echo `date` > $path/logs/$year/$month/$day/driver-core.log 2>&1
/mapbar/app/tools/java/jdk1.7.0_21/bin/java -Dhadoop.log.dir=logs -Dhadoop.log.file=hadoop.log -Xmx1024m -Xms1024m -Xmn512m -Xss1M -XX:+UseConcMarkSweepGC -XX:PermSize=64m -XX:+UseParNewGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -Djava.awt.headless=true -classpath .:./bin:./lib/mysql-connector-java-5.1.35.jar:./lib/junit-4.8.2.jar:./lib/jsoup-1.8.3.jar:./lib/htmlparser-2.1.jar:./lib/htmlexer-2.1.jar:./lib/httpclient-4.3.6.jar:./lib/httpcore-4.3.3.jar:./lib/commons-logging-1.1.3.jar:./lib/commons-codec-1.6.jar:./lib/jedis-2.4.2.jar:./lib/commons-pool2-2.0.jar:./lib/log4j-1.2.17.jar:./lib/fastjson-1.2.5.jar:./lib/slf4j-api-1.7.5.jar:./lib/slf4j-log4j12-1.7.5.jar:./lib/swingx-1.0.jar:./lib/filters-2.0.235.jar:./lib/swing-worker-1.1.jar:./lib/jai_imageio.jar:./lib/jai_core-1.1.3.jar:./lib/dom4j-1.6.1.jar:./lib/xml-apis-1.0.b2.jar:./lib/mapbar-util-net.jar:./lib/com_mapbar_mail-1.4.0.jar com.mapbar.traffic.score.relay.RelayProcess 
echo ---------------------------------------------------------- >> $path/logs/$year/$month/$day/driver-core.log 2>&1

echo  The end!
