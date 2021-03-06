## Running

* Build the project using command:
  ```
    mvn clean install package 
    ```

* Run application locally using IDE or go to the directory with jar file and run command:
    ```
    java -jar search-engine-0.0.1-SNAPSHOT.jar
    ```

## How it works

* After running the application, controller method `addExampleDocuments()` annotated `@PostConstruct` is adding example
  list of documents. List of documents is indexed by `SearchEngineImpl` method `indexListOfDocuments(List<String> docs)`
  . IDF value is calculated using smooth weighting scheme. Each of documents is stored in memory using HashMap<>()
  implementation `Map<String, String> docsMap`.
* Type url: http://localhost:8080/search?q=fox in your browser ("fox" is example query term, try your own) **OR**
  use `curl`:
  ```
   curl http://localhost:8080/search?q=fox
  ```
  Result is a JSON file with the array of objects representing Entry class objects and logging info message in the
  console.
* Shut down application in your IDE **OR** use `curl`:
  ```
   curl -X POST localhost:8080/actuator/shutdown
  ```

## Example documents

* **doc1**:
  ```
  the brown fox jumped over the brown dog
  ```
* **doc2**:
  ```
  the lazy brown dog sat in the corner
  ```
* **doc3**:
  ```
  the red fox bit the lazy dog
  ```

## Notes

* Error in example description: search "fox" shall return the list in different order: [document3, document1].