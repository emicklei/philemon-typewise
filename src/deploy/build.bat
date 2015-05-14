del *.log
xcopy /y d:\maven\repository/commons-codec/jars/commons-codec-1.3.jarcommons-codec-1.3.jar
jarsigner -keystore keystore -storepass password commons-codec-1.3.jar typewise

xcopy /y d:\maven\repository/commons-logging/jars/commons-logging-1.0.4.jar
jarsigner -keystore keystore -storepass password commons-logging-1.0.4.jar typewise


xcopy /y d:\maven\repository\philemon\jars\philemon-typewise-1.0-rc89.jar
xcopy /y d:\maven\repository\philemon\jars\philemon-typewise-1.0-rc89.jar

xcopy /y d:\maven\repository\philemon\jars\philemon-typewise-1.0-rc89.jar
xcopy /y d:\maven\repository\philemon\jars\philemon-utils-1.0.1.jar
xcopy /y d:\java\workspace1\philemon-typewise\src\deploy\typewise.jnlp
REM xcopy /y d:\java\workspace1\philemon-typewise\src\resources\log4j.properties

jar -cvf log4j_properties.jar log4j.properties
jar -cvf log4j_xml.jar log4j.xml


jarsigner -keystore keystore -storepass password commons-httpclient-3.0.jar typewise

jarsigner -keystore keystore -storepass password swt.jar typewise
jarsigner -keystore keystore -storepass password swt_native_lib.jar typewise
jarsigner -keystore keystore -storepass password log4j-1.2.8.jar typewise
jarsigner -keystore keystore -storepass password log4j_properties.jar typewise
jarsigner -keystore keystore -storepass password log4j_xml.jar typewise
jarsigner -keystore keystore -storepass password geronimo-spec-servlet-2.4-rc2.jar typewise
jarsigner -keystore keystore -storepass password swt.jar typewise
jarsigner -keystore keystore -storepass password swt_native_lib.jar typewise
jarsigner -keystore keystore -storepass password log4j-1.2.8.jar typewise
jarsigner -keystore keystore -storepass password geronimo-spec-servlet-2.4-rc2.jar typewise

jarsigner -keystore keystore -storepass password philemon-typewise-1.0-rc89.jar typewise
jarsigner -keystore keystore -storepass password philemon-utils-1.0.1.jar typewise
jarsigner -keystore keystore -storepass password log4j_properties.jar typewise
jarsigner -keystore keystore -storepass password log4j_xml.jar typewise
jarsigner -keystore keystore -storepass password log4j_properties.jar typewise
jarsigner -keystore keystore -storepass password log4j_xml.jar typewise

