
terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "4.6.0"
    }
  }
}

provider "docker" {
  host = "unix:///Users/pedrosalcedo/.docker/run/docker.sock"
}

resource "docker_network" "study_tracker_network" {
  name = "cen4802-terraform-network"
}

resource "docker_container" "study_tracker" {
  name    = "cen4802-terraform-app"
  image   = "cen4802-app:latest"
  restart = "no"

  ports {
    internal = 8080
    external = 8082
  }

  networks_advanced {
    name = docker_network.study_tracker_network.name
  }
}
