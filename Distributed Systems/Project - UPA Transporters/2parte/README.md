# Projeto de Sistemas Distribuídos 2015-2016 #

Grupo de SD 24 - Campus Alameda

Gonçalo Fialho 79112 goncalo.f.pires@ist.utl.pt

Gonçalo Lourenço Ferreira 78596 goncalolourencoferreira@tecnico.ulisboa.pt

Pedro Duarte 78328 pedro.m.duarte@tecnico.ulisboa.pt 


Repositório:
[tecnico-distsys/A_24-project](https://github.com/tecnico-distsys/A_24-project/)

-------------------------------------------------------------------------------

## Instruções de instalação 


### Ambiente

[0] Iniciar sistema operativo

Windows


[1] Iniciar servidores de apoio

JUDDI:
```
juddi-3.3.2_tomcat-7.0.64_9090\bin\startup.bat
```


[2] Criar pasta temporária

```
cd C:\
mkdir UPATransportes
```


[3] Obter código fonte do projeto (versão entregue)

```
git clone -b SD_R1 https://github.com/tecnico-distsys/A_24-project/
```


[4] Instalar módulos de bibliotecas auxiliares

```
cd uddi-naming
mvn clean install
```

-------------------------------------------------------------------------------

### Handlers

[1] Construir handlers

```
cd ws-handlers
mvn install
```


...


-------------------------------------------------------------------------------

### Serviço CA

[1] Construir e executar **servidor**

```
cd ca-ws
mvn install
mvn exec:java
```

[2] Construir **cliente** e executar testes

```
cd ca-ws-cli
mvn install
```

...

-------------------------------------------------------------------------------

### Serviço TRANSPORTER

[1] Construir e executar **servidor**

```
mvn install
mvn exec:java -Dws.i=1
mvn exec:java -Dws.i=2
```

[2] Construir **cliente** e executar testes

```
cd transporter-ws-cli
mvn install
```

...


-------------------------------------------------------------------------------

### Serviço BROKER

[1] Construir e executar **servidor**

```
cd broker-ws
mvn install
mvn exec:java
mvn exec:java -Dws.i=2
```

[2] Construir **cliente** e executar testes

```
cd broker-ws-cli
mvn install
mvn exec:java
```

...


-------------------------------------------------------------------------------

### Serviço BROKER

[1] Construir e executar **servidor**

```
cd broker-ws
mvn clean install
mvn verify
mvn exec:java
```


[2] Construir **cliente** e executar testes

```
cd broker-ws-cli
mvn clean install
mvn test
```

...

-------------------------------------------------------------------------------


...

**FIM**
