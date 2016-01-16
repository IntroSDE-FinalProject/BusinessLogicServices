# BusinessLogicServices

The Business Logic Service is a REST web service. It is the layer responsible for logic in the architecture. It performs manipulations of the data and decisions. This layer receives requests from User Interface and from the [Process Centric Service](https://github.com/introsde-2015-FinalProject/ProcessCentricServices) and gets the data from the [Storage Service](https://github.com/introsde-2015-FinalProject/StorageServices) to send results back.

[API Documentation (apiary)](http://docs.localdatabaseservice.apiary.io/#)
[URL of the server (heroku)](https://bls-desolate-falls-2352.herokuapp.com/sdelab/)

### Install
In order to execute this server locally you need the following technologies (in the brackets you see the version used to develop):

* Java (jdk1.8.0)
* ANT (version 1.9.4)

Then, clone the repository. Run in your terminal:

```
git clone https://github.com/introsde-2015-FinalProject/BusinessLogicServices.git && cd BusinessLogicServices
```

and run the following command:
```
ant generate
ant install
```

`ant generate` run *xjc*. It compiles an [XML schema](xmlSchemaBLS.xsd) file into fully annotated Java classes.

### Getting Started
To run the server locally then run:
```
ant start
```