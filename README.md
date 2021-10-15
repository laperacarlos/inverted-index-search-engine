## Running

* Run application locally using IDE or go to the directory with jar file and 
    ```
    java -jar search-engine-0.0.1-SNAPSHOT.jar
    ```


## How it works
* After running the application, controller method `addExampleDocuments()` annotated `@PostConstruct` is reading example txt files from directory `resource/documents` using `DataReader` service.
List of documents is indexed by `SearchEngineImpl` method `indexListOfDocuments(List<String> docs)`.
* Type url: http://localhost:8080/search/fox in your browser ("fox" is example query term, try your own) **OR** use `curl`:
  ```
   curl http://localhost:8080/search/fox
  ```
  Result is a JSON file with the array of objects representing Entry class objects and logging info message in the console.
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
Error in example description: search "fox" shall return the list in different order: [document3, document1].