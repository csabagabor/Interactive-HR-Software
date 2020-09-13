# Interactive-HR-Software

# Technologies used:
Backend:
- MySQL
- RabbitMQ
- gRPC
- MongoDB
- Spring Boot
- Authentication with JWT tokens
- Mockito + H2 + JUnit4 for tests

Frontend:
- Vue.js
- Sock.js + WebSocket

# System Architecture
![image](https://user-images.githubusercontent.com/37183688/93024105-67c8d480-f5fc-11ea-870f-44a9ece0e912.png)


# How to run it:
1. First compile the `model` backend module. Then you can compile the `hr` module which depends on `model`.
Without Maven compilation it won't work, because the Java classes are created during this compilation phase from the 
protobuf files(gRPC).
2. You have to have MySQL version 5, MongoDB 4.0.4 and RabbitMQ 3 installed. If you have Docker installed, you can simply start a MongoDB and RabbitMQ instance with the following commands:
```
docker run -d -p 27017-27019:27017-27019 --name mongodb mongo:4.0.4
docker run -d -p 5672:5672 -p 15672:15672 --name my-rab rabbitmq:3management
```
3. When running the app for the first time, pass `-Drun.arguments="mongodb-update"` as an argument. This will initialize MongoDB.

4. The frontend can be simply started by `npm install` and then `npm run serve`

# Roles
There are several roles in the system: Moderator, Admin, Project Manager, Regular user, Payroll team


# Screenshots
![login](https://user-images.githubusercontent.com/37183688/93024182-3270b680-f5fd-11ea-9782-216e037aee61.png)

![home](https://user-images.githubusercontent.com/37183688/93024183-34d31080-f5fd-11ea-9c05-66bf93ddbf34.png)

![moderator_accept_reject_request](https://user-images.githubusercontent.com/37183688/93024197-4caa9480-f5fd-11ea-86b1-96ba8200d5e5.png)

![moderator_projects](https://user-images.githubusercontent.com/37183688/93024332-626c8980-f5fe-11ea-9014-4a5bf7b734cc.png)

![pm_approve_reject_timecard](https://user-images.githubusercontent.com/37183688/93024201-50d6b200-f5fd-11ea-83a8-861aa791cc84.png)

![timecard2](https://user-images.githubusercontent.com/37183688/93024203-5502cf80-f5fd-11ea-8ae2-f5472dbb67b3.png)

![image](https://user-images.githubusercontent.com/37183688/93024248-b32fb280-f5fd-11ea-94d2-322d2ee88e49.png)

![pm_view_timecard_of_user](https://user-images.githubusercontent.com/37183688/93024204-5633fc80-f5fd-11ea-970d-9bd0966058c0.png)

![request_date_picker](https://user-images.githubusercontent.com/37183688/93024205-58965680-f5fd-11ea-8ff1-2144e2e27d30.png)

![request1](https://user-images.githubusercontent.com/37183688/93024207-5c29dd80-f5fd-11ea-8253-04faafde05a6.png)

![change_pass](https://user-images.githubusercontent.com/37183688/93024225-82e81400-f5fd-11ea-9f8d-56302f874c1a.png)

![myprof](https://user-images.githubusercontent.com/37183688/93024226-84194100-f5fd-11ea-806d-b913551c04b0.png)

![payroll](https://user-images.githubusercontent.com/37183688/93024229-87acc800-f5fd-11ea-8b86-98f87b7455c8.png)



