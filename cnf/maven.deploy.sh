./mvn deploy:deploy-file -DgroupId=org.eclipse.gemini -DartifactId=org.eclipse.gemini.jpa -Dversion=1.2.0 -Dpackaging=jar  -Dfile=$HOME/Downloads/org.eclipse.gemini.jpa-1.2.0.jar -DrepositoryId=http://localhost:8081 -Durl=http://localhost:8081/repository/maven-releases/ --settings=$HOME/git/common/cnf/settings.txt -X


./mvn deploy:deploy-file -DgroupId=javax.servlet -DartifactId=javax.servlet -Dversion=3.1.0 -Dpackaging=jar  -Dfile=$HOME/Downloads/javax.servlet-3.1.0.jar -DrepositoryId=http://localhost:8081 -Durl=http://localhost:8081/repository/maven-releases/ --settings=$HOME/git/common/cnf/settings.txt -X



./mvn deploy:deploy-file -DgroupId=javax.mail -DartifactId=javax.mail -Dversion=1.4.1 -Dpackaging=jar  -Dfile=$HOME/Downloads/javax.mail-1.4.1.jar -DrepositoryId=http://localhost:8081 -Durl=http://localhost:8081/repository/maven-releases/ --settings=$HOME/git/common/cnf/settings.txt -X


Downloads/postgresql-9.4.1212.jre7.jar

postgresql-9.4-1204.jdbc4.jar


./mvn deploy:deploy-file -DgroupId=postgres -DartifactId=postgres -Dversion=9.4.1204 -Dpackaging=jar  -Dfile=$HOME/Downloads/postgresql-9.4-1204.jdbc4.jar -DrepositoryId=http://localhost:8081 -Durl=http://localhost:8081/repository/maven-releases/ --settings=$HOME/git/common/cnf/settings.txt -X


./mvn deploy:deploy-file -DgroupId=javax -DartifactId=persistence -Dversion=2.1.0 -Dpackaging=jar  -Dfile=$HOME/Downloads/javax.persistence_2.1.0.v201304241213.jar -DrepositoryId=http://localhost:8081 -Durl=http://localhost:8081/repository/maven-releases/ --settings=$HOME/git/common/cnf/settings.txt
