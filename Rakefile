desc "Default Task"
task :default => [ :classdoc ]

task :classdoc do
  cp = "D:/java/workspace1/philemon-experimental/bin"
  cp += ";d:/maven/repository/qdox/jars/qdox-1.5.jar"
  cp += ";d:/maven/repository/philemon/jars/philemon-utils-1.0.1.jar"  
  cmd = "java -cp #{cp} com.philemonworks.tools.ClassCommentsToXDoc"
  src_home = "D:/java/workspace1/philemon-typewise/src/java/"
  target_home = "D:/java/workspace1/philemon-typewise/xdocs/"

  ["com.philemonworks.boa",
  "com.philemonworks.typewise",
  "com.philemonworks.typewise.cwt",
  "com.philemonworks.typewise.cwt.custom",
  "com.philemonworks.typewise.html",
  "com.philemonworks.typewise.internal",
  "com.philemonworks.typewise.message",
  "com.philemonworks.typewise.server",
  "com.philemonworks.typewise.swt",
  "com.philemonworks.typewise.swt.client",
  "com.philemonworks.typewise.swt.custom",
  "com.philemonworks.typewise.tools"  
  ].each { |pkg|
    args = "#{src_home}#{pkg.gsub('.','/')} javadoc #{target_home}#{pkg}.xml"
    system(cmd + " " + args)
  }
  end