1st Set of Questions for Basic programming skills:
## Prerequisite
    All Java  test apps have been deployed for Installed Software/framework as follow 
    1. Java version 17+
    2. Apache Maven version 3.9.9

## In order to run tests 
    1. un-archive zip/jar file
    2. run java demo from parent folder with commands shown below.

1. DocumentSearchEngine.java Run Main app from parent folder:
## Run gitbush/poweShell command from folder :
    java src\main\java\com\example\demo\DocumentSearchEngine.java

2. AvoidOrderDuplicationTest Run main function from parent folder 
## Run gitbash/powershell command from parent folder:
    java src\main\java\com\example\demo\AvoidOrderDuplicationTest.java

3. ThreadManagement.java 
## Run gitbush/powershell command from parent folder:
    java src\main\java\com\example\demo\ThreadManagement.java

=======================================

2nd Set of Questions/Answers for Advanced Skills:
## 1 My SQL transaction query. 
- In order to get safely do 1st Request without impacting performance we need to do next steps:
    1. Limit Query Scope ( Use LIMIT 100000) records per 
    2. Create View or Temp Table to make copy of data and run with no Impact on Prod data tables
    3. Use in SELECT DISTINCT statement
    4. Run SQL during Low traffic time
    5. Partitioning can be used if present

SELECT DISTINCT name FROM (
    SELECT name FROM transaction WHERE created_date >= NOW() - INTERVAL 5 MINUTE
    LIMIT 50000 ) AS tr;

## 2 My SQL transaction clean up:
 Answer:
    1. Create Scheduled process (ETL)
    2. Run it during Low traffic time 
    3. Locks for data will be reduced to minimum.
    4. Creat INDEXES ONLINE (to avoid locks) if  not created yet.
    5. Monitoryng progress to avoid conflicts.
    6. Use Partitioning by date if not partitioned yet.

CREATE TABLE transaction_table_partitioned (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(254),
    creattion_date DATETIME,
    status VARCHAR(50),
    PRIMARY KEY (id, creation_date)
)
PARTITION BY RANGE (YEAR(creation_date) * 100 + MONTH(creation_date)) (
    PARTITION p20250501 VALUES LESS THAN (20250502))


## #3 My SQL truncate vs delete
 1. TRUNCATE - removes all rows form the table, resets autoincrement counter for ids/ etc , Faster then DELETE.  Can't be used for ##2 above.
    
 2. DELETE - removes rows based on WHERE clause, can be rolled back. Activated DELETE Triggers.
  DELETE can be used for ##2 above


## Out of Memory Issues in Production:
1. Enable GC Logging:  -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/path/to/
gc.log
2. Enable Heap Dump: -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/path/to/heapdump.hprof
 IT will help to review what is most expensive for meory object
3. JVM Metrics to monitor: jstat -gc <pid> 2000

4. Memory allocation can be increased for debugging: -Xms8g -Xmx16g
5. Tools to use:
    - GC logs  Analyzer GCEasy, Garbage Cat
    - Heap Dumps Analyzer Eclipse MAT
    - Thread Dumps Analyzer FastThread.io
6. Monitoryng:
    - New Relic - JVM
    - App Dynamics - JVM
    - Grafana - JVM

 

## 5 multithreaded java application where the threads appear to dead
## lock in production steps to fix: 
 1. Capture Thread Dump by Useing: 
    - jstack -i pid 
    - kill -3 pid
 2. Monitor JVM:
    - jconsole
    - VisualVM
    - jstat
  3.  Analyze for "BLOCKED" thread state
  4. Stop blocked Thread if it is "deadlock".
        jcmd pid Thread.stop thread_id

  5. Better to use thread timeouts like trylock(10, SECONDS ) / unlock() then unlimited wait. 

