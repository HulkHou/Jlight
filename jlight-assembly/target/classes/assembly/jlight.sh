#!/bin/bash

###################################
# Please change these parameters according to your real env.
###################################
#JAVA_HOME=$JAVA_HOME

# set ulimit
ulimit -s 20480

# application directory
cd `dirname $0`
APP_HOME=`pwd`

# Java main class to start this program
APP_MAIN_CLASS=com.lew.jlight.web.Application

# get the full classpath, includes all the jars in lib directory
# especially the config directory, it will be added to the first of classpath
CLASSPATH=${APP_HOME}/config/
for i in "$APP_HOME"/lib/*.jar;do
   CLASSPATH="$CLASSPATH":"$i"
done

# read the port and consul settings in the configure file
HTTP_PORT=`sed '/server.port/!d;s/.*=//' config/application.properties | tr -d '\r'`

# Java JVM lunch parameters
if [ x"$JAVA_OPTS" == x ];then
    JAVA_OPTS="-Djava.awt.headless=true -Djava.net.preferIPv4Stack=true"
fi
if [ x"$JAVA_MEM_OPTS" == x ];then
    JAVA_MEM_OPTS="-server -Xms512m -Xmx2g -Xmn256m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
fi
JAVA_DEBUG_OPTS=""
JAVA_DEBUG_PORT=8000
JAVA_DEBUG_ENABLE=false
if [ "$2" == "debug" ]; then
    JAVA_DEBUG_ENABLE=true
    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=$JAVA_DEBUG_PORT,server=y,suspend=n "
fi

# path of log file, because logback can't create missing directory, we need to help it by shell script
LOGS_DIR="$APP_HOME/logs"
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
    #echo "created logs directory: path=$LOGS_DIR"
fi
STDOUT_FILE=$LOGS_DIR/out.log
if [ ! -f $STDOUT_FILE ]; then
    touch $STDOUT_FILE
    #echo "created logs/out.log file: path=$STDOUT_FILE"
fi


# waiting timeout for starting, in seconds
START_WAIT_TIMEOUT=30

GET_PID_BY_ALL_PORT() {
   pidUsed=$(GET_PID_BY_HTTP_PORT)
   echo ${pidUsed}
}

GET_PID_BY_HTTP_PORT() {
   if [ x"$HTTP_PORT" != x ];then
      echo `lsof -i :${HTTP_PORT} | grep LISTEN | awk '{print $2}'`
   fi
}

GET_PID_BY_DEBUG_PORT() {
   echo `lsof -i :${JAVA_DEBUG_PORT} | grep LISTEN | awk '{print $2}'`
}

###################################
#(function)start process
###################################
start() {
   PID=$(GET_PID_BY_ALL_PORT)

   if [ x"$PID" != x ];then
      echo "==================== Failed! ====================="
      isJLight=`ps -ef | grep $PID | grep -v grep | grep jlight-web`

      if [ x"$isJLight" != x ]; then
         isThisJLight=`ps -ef | grep $PID | grep -v grep | grep jlight-web | grep "$APP_HOME"`
         if [ x"$isJLight" != x ]; then
            echo "========   JLight  is already started!   ========"
            echo "========             (pid=$PID)          ========"

         else
            echo "========  Port is used by other JLight!  ========"
            echo "========             (pid=$PID)          ========"

         fi
      else
         echo "========  Port is used by other process!  ========"
         echo "========              (pid=$PID)         ========"

      fi

      echo "=================================================="
      echo "try: ps -ef | grep $PID | grep -v grep"

      status
   else
      if [ "$JAVA_DEBUG_ENABLE" = true ]; then
         checkJavaDebugPort
      fi

      echo "               Starting Server ..."
      echo
      # open these to see detail information for debugging
      #echo "JAVA_OPTS: $JAVA_OPTS"
      #echo
      #echo "JAVA_MEM_OPTS: $JAVA_MEM_OPTS"
      #echo
      #echo "CLASSPATH: $CLASSPATH"
      #echo

      nohup $JAVA_HOME/bin/java $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS -classpath $CLASSPATH $APP_MAIN_CLASS >$STDOUT_FILE 2>&1 & tail -f $STDOUT_FILE

      sleep 3

      starttime=0
      while  [ x"$PID" == x ]; do
         if [[ "$starttime" -lt ${START_WAIT_TIMEOUT} ]]; then
            sleep 1
            ((starttime++))
            echo -e ".\c"
            PID=$(GET_PID_BY_HTTP_PORT)
         else
            echo "JLight Server failed to start"
            echo "The port HTTP_PORT doesn't open in ${START_WAIT_TIMEOUT} seconds!"
            echo "check logs/out.log to see the details"
            status
            exit -1
         fi
      done

      echo
      echo "The process of JLight Server is started: pid=$PID"
      echo "Warning! ** It's not equal to success! **"
      echo "Please check logs/out.log to see the final result."
      status
   fi
}

###################################
# (function) check port of java debug
###################################
checkJavaDebugPort() {
   PID=$(GET_PID_BY_DEBUG_PORT)

   if [ x"$PID" != x ];then
      echo "Warning: java debug port $JAVA_DEBUG_PORT is in use by process $PID!"
      echo
      echo "===================================================================="
      lsof -i :$JAVA_DEBUG_PORT
      echo "===================================================================="
      echo
      echo "failed to start, try to fix it before starting JLight server:"
      echo "    |-- close the process $PID to free this port $JAVA_DEBUG_PORT."
      echo "    |-- Or change the JAVA_DEBUG_PORT (in this script) to another one."

      exit -1
   fi

   echo "=== Java debug enabled on port $JAVA_DEBUG_PORT ==="
}

###################################
# (function) stop process
###################################
stop() {
   PID=$(GET_PID_BY_HTTP_PORT)

   if [ x"$PID" == x ];then
      echo "==================== Failed! ====================="
      echo "========    Can't find JLight Server!    ========"
      echo "========           (by port=$HTTP_PORT)         ========"
      echo "=================================================="
      status
      return
   fi

   isThisJLight=`ps -ef | grep $PID | grep -v grep | grep jlight-web | grep "$APP_HOME"`
   if [ x"$isThisJLight" == x ]; then
      echo "==================== Failed! ====================="
      echo "=====   Another Program is using the port!   ====="
      echo "=====               (pid=$PID)              ====="
      echo "=================================================="
      echo "try: ps -ef | grep $PID | grep -v grep"
      status
      return
   fi

   echo "JLight Server is running with port $HTTP_PORT: pid=$PID"
   echo "trying to stop JLight Server (pid=$PID) ..."
   kill -15 $PID
   sleep 3
   PID=$(GET_PID_BY_HTTP_PORT)
   while [ x"$PID" != x ]; do
      echo -n "."
      kill $PID
      sleep 1
      PID=$(GET_PID_BY_HTTP_PORT)
   done

   echo
   echo "====================== OK ========================="
   echo "=====         JLight Server stopped         ====="
   echo "==================================================="
   status
}

###################################
# (function) check running status of process
###################################
status() {
   PID=$(GET_PID_BY_ALL_PORT)

   echo ""
   echo ""------------------- status -----------------------""

   if [ x"$PID" != x ]; then
      isJLight=`ps -ef | grep $PID | grep -v grep | grep jlight-web`
      if [ x"$isJLight" != x ]; then
         isThisJLight=`ps -ef | grep $PID | grep -v grep | grep jlight-web | grep "$APP_HOME"`
         if [ x"$isThisJLight" != x ]; then
            echo "      JLight server (pid=$PID) is running and using ports:"
         else
            echo "       Another  server (pid=$PID) is running and using ports:"
         fi
      else
         echo "          Another process (pid=$PID) is using ports:"
      fi
   else
      echo "             ports (${HTTP_PORT}) are not in use!"
   fi
    echo ""--------------------------------------------------""

   if [ x"$HTTP_PORT" != x ]; then
      PID=$(GET_PID_BY_HTTP_PORT)
      if [ x"$PID" != x ]; then
         echo "          HTTP_PORT ${HTTP_PORT} is in use by process $PID"
      else
         echo "          HTTP_PORT ${HTTP_PORT} is not in use"
      fi
   fi
   echo ""
}

###################################
# (function) print env variables
###################################
info() {
   echo
   echo "************* [OS] ***************"
   echo "OS Release: " `head -n 1 /etc/issue`
   echo "OS Infomation: " `uname -a`
   echo
   echo "*************  [JVM]  ***************"
   echo "JAVA_HOME: $JAVA_HOME"
   echo "JAVA_OPTS: $JAVA_OPTS"
   echo "JAVA_MEM_OPTS: $JAVA_MEM_OPTS"
   echo
   echo "*************  [CLASSPATH]  ***************"
   echo "CLASSPATH: $CLASSPATH"
   echo
   echo "*************  [APPLICATION]  ***************"
   echo "JAVA_MAIN_CLASS: $APP_MAIN_CLASS"
   echo "APP_HOME=$APP_HOME"
   echo "****************************"
}

###################################
# get the first argument of this script, then check it
# this argument should be one of : {start|stop|restart|status|info}
# if not, print help information
###################################
case "$1" in
   'start')
      start
      ;;
   'stop')
     stop
     ;;
   'status')
     status
     ;;
   'info')
     info
     ;;
  *)
     echo "Usage: $0 {start|stop|status|info}"
     exit 1
esac