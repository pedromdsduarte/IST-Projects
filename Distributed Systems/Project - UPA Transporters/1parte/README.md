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
git clone ... 
```


[4] Instalar módulos de bibliotecas auxiliares

```
cd uddi-naming
mvn clean install
```


-------------------------------------------------------------------------------

### Serviço TRANSPORTER

[1] Construir e executar **servidor**

```
cd transporter-ws
mvn clean install
mvn verify
mvn exec:java -Dws-i=Num
```

[2] Construir **cliente** e executar testes

```
cd transporter-ws-cli
mvn clean install
mvn test
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
### Testar Projecto
Os programas geram prints para a melhora compreenssão da sua execução ao longo do seu tempo de vida

[1] Modificar Inputs Broker-Client

```
cd broker-ws-cli\src\main\java\pt\upa\broker\ws\cli
open BrokerClient.java
modify input zone
cd broker-ws-cli
mvn clean compile
```

[2] Executar Broker-Server

```
cd broker-ws
mvn exec:java
```

[3] Executar Transport-Server

Substituir ? por um valor entre 1 a 9
```
cd transport-ws
mvn exec:java -Dws.i=?
```

[4] Executar Broker-Client

```
cd broker-ws-cli
mvn exec:java
```
-------------------------------------------------------------------------------


...

**FIM**
