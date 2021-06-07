CREATE TABLE "keyspace_name"."company"(
	"companyid" text,
	"companyname" text,
	"uniquebusinessidentifier" text,
	PRIMARY KEY("companyid"))
WITH CUSTOM_PROPERTIES = {
	'capacity_mode':{'throughput_mode':'PAY_PER_REQUEST'},
	'point_in_time_recovery':{'status':'disabled'}}