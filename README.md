# CEDictLanguageAssistant
Simple lightweight desktop language assistant which translates user highlighted text to aid in vocabulary learning for Mandarin. Written in Java, using the H2 database engine and Swing for GUI.  

## Requirements
- Java 17 or later
  - Download the latest version of Java here: https://www.java.com/en/download/
- Maven 3.9.6 or later
  - Downlaod the latest version of Maven here: https://maven.apache.org/download.cgi

 ## Build with Maven
 1. ```mvn install```
 2. ```mvn clean package```
 3. ```java -jar ./target/cedict-jar-with-dependencies.jar run```

## Or you can find the precompiled bytecode in the 'target' directory

## (Optional)
After modification, build then run ```java -jar ./target/cedict-jar-with-dependencies.jar setup``` to revalidate the embedded databases
