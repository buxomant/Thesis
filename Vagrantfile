# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure(2) do |config|
  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://atlas.hashicorp.com/search.
  config.vm.box = "bento/centos-7.5"

  config.vm.network :forwarded_port, guest: 5432, host: 5432 # postgresql

  config.vm.provision "ansible_local" do |ansible|
      ansible.provisioning_path = "/vagrant/infrastructure"
      ansible.inventory_path = "environments/development"
      ansible.playbook = "development.yml"
    end
end
