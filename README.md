## Step by step to set up after clone this repo:

### 1. Rename project in `pom.xml` and root package

### 2. Change database connection in

`src/main/resources/application-dev.properties`
`src/main/resources/application-prod.properties`

### 3. Modify other codes for your purpose

### 4. To run the project, you need to configure it in the IDE or you can also run it in the terminal using the command:

```shell
./mnvw spring-boot:run
```

---

### You can see the current endpoint by accessing Swagger using URI: `{hostname}/swagger-ui/index.html`