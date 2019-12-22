## How To Run 

* package project 
* copy microservice-example.jar to artifact dir
* run 'python -m SimpleHTTPServer 5050' from artifact dir
* run 'sudo nohup consul agent -dev  -config-dir=/home/yael/IdeaProjects/microserviceExample/config &'
* run 'sudo systemctl stop systemd-resolved && * sudo systemctl disable systemd-resolved'
* changed /etc/resolv.conf line from 'namespace 127.0.0.53' to 'namespace 127.0.0.1'
* run 'sudo nomad agent -dev'
* run 'nomad job run /home/yael/IdeaProjects/microserviceExample/nomad/infrastructure.nomad
* ensure kafka and zookeeper came up ok through consul ui (http://localhost:8500/ui/dc1/services)  
* run 'nomad job run /home/yael/IdeaProjects/microserviceExample/nomad/example.nomad
 
