# BusinessLogicServices

| REST                                     | Description |
|------------------------------------------|-------------|
| **Person**                               |             |
| GET /person/{personId}                   |             |
| DELETE /person/{personId}                |             |
| GET /person/{personId}/currentHealth     |             |
| GET /person/{personId}/measure           |             |
| GET /person/{personId}/reminder          |             |
| GET /person/{personId}/suggestion        |             |
| POST /person/{personId}/reminder         |             |
| GET /person/{personId}/target            |             |
| POST /person/{personId}/target           |             |
|                                          |             |
| **Doctor**                               |             |
| GET /doctor/{doctorId}                   |             |
| DELETE /doctor/{doctorId}                |             |
| GET /doctor/{doctorId}/patients          |             |
|                                          |             |
| **Family**                               |             |
| GET /family/{familyId}/person/{personId} |             |