# okta_client_credentials_flow_spring_boot

This is a simple demo for the Okta Client Credentials Flow https://developer.okta.com/assets-jekyll/blog/client-credentials-spring-security/http-basic-sequence-425e0b5f98c58bdf5b977262eaa422c275c63ec3b2d4c4c670fd866d90c0f69b.png

This is based on the following guide (https://developer.okta.com/blog/2021/05/05/client-credentials-spring-security#create-a-resttemplate-command-line-application) which is unfortunately using an outdated version of Spring Boot. It is using version 2.5.6 which is no longer available and is using Spring Security 5 whereas the current version is now Spring Security 6. I have followed the guide but instead of using version 2.5.6 I am using version 3.3.0. If you are following along, consider replacing all Spring Initializr requests with the version 3.3.0 instead.

I've gone ahead and updated the code using AI and have set it up for my Okta org. The client ids and secrets are all live and should work with my developer org dev-30432529.okta.com.

Notably, the guide was missing a section where the access policies for the org are created. Please follow this guide to create it: https://developer.okta.com/docs/guides/customize-authz-server/main/#create-access-policies. 
In order to run the program, simply run
```
cd secure-server/
./gradlew bootRun
```
Then while the server is running, switch to another window and run

```
cd client-webclient
./gradlew bootRun
```

The output should look something like

```
> Task :bootRun
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.3.0)

2025-02-12T18:43:07.351-06:00  INFO 14064 --- [demo] [           main] com.example.client.DemoApplication       : Starting DemoApplication using Java 17.0.14 with PID 14064 (~/client-webclient/build/classes/java/main started by username in ~/)
2025-02-12T18:43:07.353-06:00  INFO 14064 --- [demo] [           main] com.example.client.DemoApplication       : No active profile set, falling back to 1 default profile: "default"
2025-02-12T18:43:07.894-06:00  INFO 14064 --- [demo] [           main] com.example.client.DemoApplication       : Started DemoApplication in 0.804 seconds (process running for 0.988)
2025-02-12T18:43:08.734-06:00  INFO 14064 --- [demo] [           main] com.example.client.DemoApplication       : Issued: 2025-02-13T00:43:08.732609591Z, Expires: 2025-02-13T01:43:08.732609591Z
2025-02-12T18:43:08.735-06:00  INFO 14064 --- [demo] [           main] com.example.client.DemoApplication       : Scopes: [mod_custom]
2025-02-12T18:43:08.735-06:00  INFO 14064 --- [demo] [           main] com.example.client.DemoApplication       : Token: eyJraWQiOiI3U29BUDdWVjRFckNYbjZwVzhrdmY3Vll4ZHo5OExCeUxCOXd0RUVlYTlZIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULjI4TXVDSVBhY3ZFUUZ4QmdEbTJfcUp1THllNUVidGZtclJKbDRKeEZYS0kiLCJpc3MiOiJodHRwczovL2Rldi0zMDQzMjUyOS5va3RhLmNvbS9vYXV0aDIvZGVmYXVsdCIsImF1ZCI6ImFwaTovL2RlZmF1bHQiLCJpYXQiOjE3Mzk0MDczODgsImV4cCI6MTczOTQxMDk4OCwiY2lkIjoiMG9hbmQxb3RjdWZPRlp2VGQ1ZDciLCJzY3AiOlsibW9kX2N1c3RvbSJdLCJzdWIiOiIwb2FuZDFvdGN1Zk9GWnZUZDVkNyJ9.ePwXEPteJxwUdvzT9QkTuTFz7u_aK7wn8Emgq11PR_QQPJ4gi0YUZEvNRRVfGNIGE2TPcg6G4MwJ2XyIXmD52a8oKUqRyhY2nIUyLi48KvWEN5YzJc-HYv0klqT5KOoIktOAE00xMMUa8RJokM8Mte2LchTLqrdDr7ilB4NGCztoJFOU1BVDfDx-S5FgtZrRXVzC4SrePM7SdtgVT4cjY_4y5eNfIrEviTigCuS3RQHKuLkgKCmjozNoItzUrShf1TyekfNSfDaTyNezQ-hx8VGc8BcaVEf2ZQ19cFidweKchhit_qPIoitDJEydwY8fiASE8FdcD_6XuyhtwFUbog
2025-02-12T18:43:09.485-06:00  INFO 14064 --- [demo] [           main] com.example.client.DemoApplication       : Reply = Welcome, 0oand1otcufOFZvTd5d7

[Incubating] Problems report is available at: ~/client-webclient/build/reports/problems/problems-report.html

BUILD SUCCESSFUL in 9s
4 actionable tasks: 4 executed
```

If you are having trouble, you can test the program manually with the following curl requests: 

`curl -X POST "https://{your okta org}.okta.com/oauth2/default/v1/token" -H "Content-Type: application/x-www-form-urlencoded" -u "{client-id}:{client-secret}" --data "grant_type=client_credentials&scope=mod_custom"`

`curl -X GET "http://localhost:8081/" -H "Accept: text/plain, application/json, application/*+json, */*" -H "Authorization: Bearer {access_token}"`


let me know if you have any questions
