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

## Examples
![image](https://github.com/davidhaoyan/CEDictLanguageAssistant/assets/60042375/808cf71b-2944-409d-9b80-ddb2824c2282)

![image](https://github.com/davidhaoyan/CEDictLanguageAssistant/assets/60042375/ae500def-ad4d-4554-bcce-6dbf9af4d563)

![image](https://github.com/davidhaoyan/CEDictLanguageAssistant/assets/60042375/622de0ba-2d4d-48f4-a6b7-38e93da982d2)

![image](https://github.com/davidhaoyan/CEDictLanguageAssistant/assets/60042375/57c5678e-aee4-4919-83f4-24565beca0c7)


