create schema if not exists quiz;

ALTER TABLE transportation_mean
ADD UNIQUE (name);

--role
INSERT INTO `role` (`id`, `name`) VALUES ('1', 'ROLE_ADMIN');
INSERT INTO `role` (`id`, `name`) VALUES ('2', 'ROLE_USER');
INSERT INTO `role` (`id`, `name`) VALUES ('3', 'ROLE_MODERATOR');
INSERT INTO `role` (`id`, `name`) VALUES ('4', 'ROLE_PM');
INSERT INTO `role` (`id`, `name`) VALUES ('5', 'ROLE_PAYROLL');

--users
INSERT INTO `users` (`id`,`email`, `first_name`, `last_name`,  `password_hash`, `role_id`) VALUES ('1', 'admin@gmail.com',
'Lincoln', 'Big',
 '$2a$10$c620P3YRQCeZco1txUL.CePkubihTr7cxu4U0J9e5P/VGnEbxtPki', 1); --admin

INSERT INTO `users` (`id`, `email`,`first_name`,`last_name`, `password_hash`, `role_id`) VALUES ('2', 'user@gmail.com',
'Ted', 'Hunter',
 '$2a$10$ND84tsD985a3bprm2XI71.DP7tYe05TE0AdJZTnHOrgsovLGUQFti', 2); --user

INSERT INTO `users` (`id`, `email`,`first_name`,`last_name`, `password_hash`, `role_id`) VALUES ('3', 'moderator@gmail.com',
'Tom', 'Fraser',
 '$2a$10$2uZuxYf5w64vvNC8CCB7PONsS1Lbb2V4EbKlgy2ph7DIma/ssmn8.', 3); --moder

INSERT INTO `users` (`id`, `email`,`first_name`,`last_name`, `password_hash`, `role_id`) VALUES ('4', 'user2@gmail.com',
'Donald', 'Harris',
 '$2a$10$ND84tsD985a3bprm2XI71.DP7tYe05TE0AdJZTnHOrgsovLGUQFti', 2); --user

 INSERT INTO `users` (`id`, `email`,`first_name`,`last_name`, `password_hash`, `role_id`) VALUES ('5', 'pm@gmail.com',
'Elizabeth', 'Hawk',
 '$2a$10$lQGAjUcXzij0SoFbwJep3.8SlDnrmt4dY08KMY2hIKeZ9dTmZJzUS', 4); --proj

  INSERT INTO `users` (`id`, `email`,`first_name`,`last_name`, `password_hash`, `role_id`) VALUES ('6', 'payroll@gmail.com',
'Edwin', 'Hall',
 '$2a$10$XT.mx1XflyiXmYy2GCgsuOGSR51ccqjcqCcDsp9MpQiPukX3pT1ie', 5); --payroll

 --transportation_main
 INSERT INTO `transportation_mean` (`id`, `name`) VALUES ('1', 'car');
 INSERT INTO `transportation_mean` (`id`, `name`) VALUES ('2', 'airplane');
 INSERT INTO `transportation_mean` (`id`, `name`) VALUES ('3', 'train');
 INSERT INTO `transportation_mean` (`id`, `name`) VALUES ('4', 'bus');

--country
INSERT INTO `country` (`id`, `name`) VALUES ('1', 'Germany');
INSERT INTO `country` (`id`, `name`) VALUES ('2', 'Romania');
INSERT INTO `country` (`id`, `name`) VALUES ('3', 'USA');

--city
INSERT INTO `city` (`id`, `name`, `country_id`) VALUES ('1', 'Munich', '1');
INSERT INTO `city` (`id`, `name`, `country_id`) VALUES ('2', 'Bucharest', '2');
INSERT INTO `city` (`id`, `name`, `country_id`) VALUES ('3', 'Chicago', '3');
INSERT INTO `city` (`id`, `name`, `country_id`) VALUES ('4', 'New York', '3');

--request
INSERT INTO `request` (`id`, `details`, `end_date`, `need_of_laptop`, `need_of_phone`,
 `need_of_vpn`, `start_date`, `city_id`,  `transportation_id`, `user_id`, `status`)
 VALUES ('1', 'nothing', '2019-10-01', '0', '0', '0', '2019-09-20', '1',  '1', '2', 'OPEN');

INSERT INTO `request` (`id`, `details`, `end_date`, `need_of_laptop`, `need_of_phone`,
`need_of_vpn`, `start_date`, `city_id`,  `transportation_id`, `user_id`, `status`)
VALUES ('2', 'blabla', '2018-10-01', '0', '1', '0', '2018-09-20', '2', '2', '2', 'OPEN');

INSERT INTO `request` (`id`, `details`, `end_date`, `need_of_laptop`, `need_of_phone`,
`need_of_vpn`, `start_date`, `city_id`,  `transportation_id`, `user_id`, `status`)
VALUES ('3', 'test', '2019-10-01', '0', '1', '1', '2019-09-26', '3',  '4', '2', 'OPEN');


INSERT INTO `request` (`id`, `details`, `end_date`, `need_of_laptop`, `need_of_phone`,
`need_of_vpn`, `start_date`, `city_id`,  `transportation_id`, `user_id`, `status`)
VALUES ('4', 'test2', '2019-08-01', '1', '1', '1', '2019-07-26', '3',  '1', '2', 'OPEN');


INSERT INTO `request` (`id`, `details`, `end_date`, `need_of_laptop`, `need_of_phone`,
`need_of_vpn`, `start_date`, `city_id`,  `transportation_id`, `user_id`, `status`)
VALUES ('5', 'test3', '2019-04-01', '1', '1', '1', '2019-03-26', '3', '4', '2', 'OPEN');

INSERT INTO `request` (`id`, `details`, `end_date`, `need_of_laptop`, `need_of_phone`,
`need_of_vpn`, `start_date`, `city_id`,  `transportation_id`, `user_id`, `status`)
VALUES ('6', 'test4', '2017-04-01', '0', '1', '1', '2017-03-26', '2',  '3', '2', 'OPEN');


INSERT INTO `request` (`id`, `details`, `end_date`, `need_of_laptop`, `need_of_phone`,
`need_of_vpn`, `start_date`, `city_id`,  `transportation_id`, `user_id`, `status`)
VALUES ('7', 'test4', '2017-02-01', '1', '1', '1', '2017-01-26', '1',  '4', '2', 'OPEN');

INSERT INTO `request` (`id`, `details`, `end_date`, `need_of_laptop`, `need_of_phone`,
`need_of_vpn`, `start_date`, `city_id`,  `transportation_id`, `user_id`, `status`)
VALUES ('8', 'test5', '2017-06-01', '1', '1', '1', '2017-05-26', '2',  '2', '2', 'ACCEPTED');

--this request is for the second user
INSERT INTO `request` (`id`, `details`, `end_date`, `need_of_laptop`, `need_of_phone`,
`need_of_vpn`, `start_date`, `city_id`,  `transportation_id`, `user_id`, `status`)
VALUES ('9', 'test6', '2017-05-01', '1', '1', '1', '2017-04-26', '4',  '1', '4', 'REJECTED');

--project
INSERT INTO `project` (`id`, `description`, `title`) VALUES ('1', 'VACATION', 'VACATION');
INSERT INTO `project` (`id`, `description`, `title`) VALUES ('2','Security backend', 'Bank of America');
INSERT INTO `project` (`id`, `description`, `title`) VALUES ('3','medical applications', 'OOATH-DOP');
INSERT INTO `project` (`id`, `description`, `title`) VALUES ('4','smart home applications', 'GOOT');

--task
INSERT INTO `task` (`id`, `description`, `identifier`, `project_id`) VALUES ('1','Time-off request', 'Time-off', '1');
INSERT INTO `task` (`id`, `description`, `identifier`, `project_id`) VALUES ('2','Backend Development', 'BE-BOA', '2');
INSERT INTO `task` (`id`, `description`, `identifier`, `project_id`) VALUES ('3','Frontend Development', 'FE-BOA', '2');
INSERT INTO `task` (`id`, `description`, `identifier`, `project_id`) VALUES ('4','Testing', 'UI-AUTOMATE', '3');
INSERT INTO `task` (`id`, `description`, `identifier`, `project_id`) VALUES ('5','Testing', 'UI-CYPRESS', '3');

--timecard
INSERT INTO `timecard` (`id`,`month`,`status`,`year`,`user_id`) VALUES (1,1,'ACCEPTED',2020,2);
INSERT INTO `timecard` (`id`,`month`,`status`,`year`,`user_id`) VALUES (2,1,'ACCEPTED',2020,1);
INSERT INTO `timecard` (`id`,`month`,`status`,`year`,`user_id`) VALUES (3,11,'ACCEPTED',2019,1);
INSERT INTO `timecard` (`id`,`month`,`status`,`year`,`user_id`) VALUES (4,10,'ACCEPTED',2019,1);

--worked task
INSERT INTO `worked_task` (`id`,`date`,`duration`,`task_id`,`timecard_id`) VALUES (1,'2020-02-07',8,1,1);
INSERT INTO `worked_task` (`id`,`date`,`duration`,`task_id`,`timecard_id`) VALUES (2,'2020-02-03',8,1,1);
INSERT INTO `worked_task` (`id`,`date`,`duration`,`task_id`,`timecard_id`) VALUES (3,'2020-02-04',8,1,1);
INSERT INTO `worked_task` (`id`,`date`,`duration`,`task_id`,`timecard_id`) VALUES (4,'2020-02-05',8,1,1);
INSERT INTO `worked_task` (`id`,`date`,`duration`,`task_id`,`timecard_id`) VALUES (5,'2020-02-06',8,1,1);
INSERT INTO `worked_task` (`id`,`date`,`duration`,`task_id`,`timecard_id`) VALUES (6,'2020-02-11',2,3,1);

INSERT INTO `worked_task` (`id`,`date`,`duration`,`task_id`,`timecard_id`) VALUES (7,'2020-02-06',8,1,2);
INSERT INTO `worked_task` (`id`,`date`,`duration`,`task_id`,`timecard_id`) VALUES (8,'2020-02-11',2,3,2);

INSERT INTO `worked_task` (`id`,`date`,`duration`,`task_id`,`timecard_id`) VALUES (9,'2019-12-06',8,1,3);

INSERT INTO `worked_task` (`id`,`date`,`duration`,`task_id`,`timecard_id`) VALUES (10,'2019-11-11',2,3,4);



