sudo yum -y install epel-release
sudo yum -y update
sudo yum install -y yum-utils
sudo yum install -y git java-1.8.0-openjdk maven wget unzip
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
sudo yum install -y docker-ce docker-ce-cli containerd.io
sudo systemctl start docker
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
sudo usermod -aG docker $USER
sudo gpasswd -a $USER docker
newgrp docker

#wget https://services.gradle.org/distributions/gradle-7.0-bin.zip -P /tmp
#sudo unzip -d /opt/gradle /tmp/gradle-7.0-bin.zip
#echo 'export GRADLE_HOME=/opt/gradle/gradle-7.0' >> ~/.bashrc
#echo 'export PATH=${GRADLE_HOME}/bin:${PATH}' >> ~/.bashrc
#source ~/.bashrc