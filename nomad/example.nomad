job "example" {

  datacenters = ["dc1"]
  group "upperCaseExample" {
    count = 1

    task "upper-case" {
      driver = "java"

      config {
        jar_path = "local/microservice-example.jar"
        jvm_options = [
          "-Xmx256m",
          "-Xms256m"]
      }

      artifact {
        source = "http://localhost:5050/microservice-example.jar"
      }

      service {
        name = "hello-service"
        port = "rest"

        check {
          type = "http"
          path = "/services/health"
          port = "rest"
          interval = "10s"
          timeout = "2s"
        }
      }

      resources {
        cpu = 500
        memory = 256

        network {
          mbits = 5
          port "rest" {
            static = "8080"
          }
        }
      }
    }
  }
}