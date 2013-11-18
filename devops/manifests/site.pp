exec {'apt-update':
  command => '/usr/bin/apt-get update',
}

Exec['apt-update'] -> Package <| |>

node default {

  include apt
  include stdlib
  include java

  include oracle::server
  include oracle::swap

  class { 'oracle::xe':
    package_file => '/opt/querydsl/oracle-xe-11.2.0-1.0.x86_64.rpm.zip',
    scripts_dir => '/opt/querydsl/sql-snippets',
  }

  oracle::xe::script {'oracle.sql': }

  user { "vagrant":
    groups => "dba",
    # So that we let Oracle installer create the group
    require => Service["oracle-xe"],
  }

  class { 'mysql::server':
    root_password => 'querydsl',

    override_options => {
      'mysqld' => {
        'bind_address' => '0.0.0.0',
        'skip-name-resolve' => true,
      }  
    },
  }

  class { 'mysqlscript':
    snippets_dir => '/opt/querydsl/sql-snippets',
  }

  mysqlscript::run_script { 'mysql.sql': }

  class { 'postgresql::server':
    /* Enable access from host machine when using Vagrant */
    listen_addresses  => '*',
    ipv4acls          => ['host all querydsl 0.0.0.0/0 md5'],
    postgres_password => 'querydsl',
  }

  postgresql::server::db { 'querydsl':
    user     => 'querydsl',
    password => 'querydsl'
  }

  include cubrid

}