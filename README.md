# camel-poc
camel spring boot hawtio, restapi ,file synchronize,Socket protocol conversion http..

http://127.0.0.1:8081/actuator/hawtio

{
	"info": {
		"_postman_id": "5c8b6fef-d5bd-4b93-b720-9dbc488912a7",
		"name": "camel-poc",
		"schema": "https://schema.getpostman.com/json/collection/v2.0.0/collection.json",
		"_exporter_id": "9290221"
	},
	"item": [
		{
			"name": "http://127.0.0.1:8081/api/uploadFile",
			"request": {
				"method": "POST",
				"header": [],
				"body": {
					"mode": "formdata",
					"formdata": [
						{
							"key": "file",
							"type": "file",
							"src": ""
						}
					]
				},
				"url": "http://127.0.0.1:8081/api/uploadFile"
			},
			"response": []
		},
		{
			"name": "http://127.0.0.1:8081/api/gateway",
			"request": {
				"method": "GET",
				"header": [],
				"url": "http://127.0.0.1:8081/api/gateway"
			},
			"response": []
		},
		{
			"name": "http://127.0.0.1:8081/api/user",
			"event": [
				{
					"listen": "prerequest",
					"script": {
						"exec": [
							"1"
						],
						"type": "text/javascript"
					}
				}
			],
			"protocolProfileBehavior": {
				"disableBodyPruning": true
			},
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "formdata",
					"formdata": [
						{
							"key": "name",
							"value": "2222",
							"type": "text"
						}
					]
				},
				"url": "http://127.0.0.1:8081/api/user"
			},
			"response": []
		},
		{
			"name": "http://127.0.0.1:8081/api/socket?mes=2",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://127.0.0.1:8081/api/socket?mes=3",
					"protocol": "http",
					"host": [
						"127",
						"0",
						"0",
						"1"
					],
					"port": "8081",
					"path": [
						"api",
						"socket"
					],
					"query": [
						{
							"key": "mes",
							"value": "3"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "http://127.0.0.1:8081/api/get_gbk",
			"request": {
				"method": "GET",
				"header": [],
				"url": "http://127.0.0.1:8081/api/get_gbk"
			},
			"response": []
		},
		{
			"name": "http://127.0.0.1:8081/api/user/",
			"request": {
				"method": "POST",
				"header": [],
				"body": {
					"mode": "formdata",
					"formdata": [
						{
							"key": "name",
							"value": "222",
							"type": "text"
						}
					]
				},
				"url": "http://127.0.0.1:8081/api/user"
			},
			"response": []
		},
		{
			"name": "http://127.0.0.1:8081/api/user/ Copy",
			"request": {
				"method": "PUT",
				"header": [],
				"body": {
					"mode": "formdata",
					"formdata": [
						{
							"key": "name",
							"value": "2222222",
							"type": "text"
						}
					]
				},
				"url": "http://127.0.0.1:8081/api/user/2"
			},
			"response": []
		},
		{
			"name": "http://127.0.0.1:8081/api/user/ Copy 2",
			"protocolProfileBehavior": {
				"disableBodyPruning": true
			},
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "formdata",
					"formdata": [
						{
							"key": "name",
							"value": "222",
							"type": "text"
						}
					]
				},
				"url": "http://127.0.0.1:8081/api/user/2"
			},
			"response": []
		},
		{
			"name": "http://127.0.0.1:8081/actuator/metrics/",
			"request": {
				"method": "GET",
				"header": [],
				"url": "http://127.0.0.1:8081/actuator/metrics/"
			},
			"response": []
		},
		{
			"name": "http://127.0.0.1:8081/api/rest_https",
			"request": {
				"method": "GET",
				"header": [],
				"url": "http://127.0.0.1:8081/api/rest_https"
			},
			"response": []
		}
	]
}
