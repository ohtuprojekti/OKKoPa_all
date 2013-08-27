
read -p "Make sure that Tomcat is running and webapps/ROOT folder is deleted on remote server. Press [enter] to continue."
echo "Building..."
mvn install -DskipITs -DskipTests

echo "Starting file transfer"
scp -i omakopio OKKoPa_GUI/target/OKKoPa_GUI-1.0.war tkt_okko@staff2013.cs.helsinki.fi:tomcat/webapps/ROOT.war 
scp -i omakopio OKKoPa_core/target/OKKoPa.jar tkt_okko@staff2013.cs.helsinki.fi:tomcat/webapps/ROOT
scp -i omakopio OKKoPa_core/target/*.xml tkt_okko@staff2013.cs.helsinki.fi:tomcat/webapps/ROOT
scp -i omakopio OKKoPa_core/target/keystore tkt_okko@staff2013.cs.helsinki.fi:tomcat/webapps/ROOT
