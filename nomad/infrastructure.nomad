job "infrastructure" {

  datacenters = ["dc1"]
  group "messageBus" {
    count = 1

    task "zookeeper" {
      driver = "raw_exec"

      config {
        command = "/home/yael/installations/kafka_2.12-2.3.0/bin/zookeeper-server-start.sh"
        args = ["local/zookeeper.properties"]
      }

      artifact {
        source = "http://localhost:5050/zookeeper.properties"
      }

      service {
        name = "Zookeeper"
        port = "zk"
        tags = ["ZK"]

        check {
          type = "tcp"
          port = "zk"
          interval = "10s"
          timeout = "2s"
        }

      }

      resources {
        memory = 1024
        network {
          mbits = 200
          port "zk" {
            static = "2181"
          }
        }
      }

    }

    task "kafka" {
      driver = "raw_exec"

      config {
        command = "/home/yael/installations/kafka_2.12-2.3.0/bin/kafka-server-start.sh"
        args = ["local/server.properties"]
      }

      artifact {
        source = "http://localhost:5050/server.properties"
      }

      service {
        name = "kafka-server"
        port = "kafka"
        tags = ["kafka"]

        check {
          type = "tcp"
          port = "kafka"
          interval = "10s"
          timeout = "2s"
        }
      }

      resources {
        memory = 1024
        network {
          mbits = 200
          port "kafka" {
            static = "9092"
          }
        }
      }
    }

  }
}