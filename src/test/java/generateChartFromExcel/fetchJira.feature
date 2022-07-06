Feature: fetch in progress JIRA issues

Background:
 Given url 	'https://vishwasshukla.atlassian.net/'

Scenario: To create a bug in a particular project
	* def inp = read('input1.json')
	* print inp 
	And header Authorization = "Basic dmlzaHdhc3NodWtsYTA1MDdAZ21haWwuY29tOmxJTVdpa2lKQ3V3NzBVTEp4Z1VDREM0QQ=="
	And path '/rest/api/2/issue'
	And request inp
	When method POST
	Then status 201
	And print response

Scenario: To Generate Jira Report
	And header Authorization = "Basic dmlzaHdhc3NodWtsYTA1MDdAZ21haWwuY29tOmxJTVdpa2lKQ3V3NzBVTEp4Z1VDREM0QQ=="
	And path 'rest/api/2/search'
	And param jql = 'project = MYF AND issuetype = "Karate Test" order by created DESC'
	And header Content-Type = 'application/json'
	When method GET
	Then status 200
	And print response	
	* string responseString = response
	* def createTable = Java.type("generateChartFromExcel.JsonFormatedAndConvertHtml")
	* def jsonUri = new createTable().generateHtml(responseString)

