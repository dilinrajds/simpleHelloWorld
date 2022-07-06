package generateChartFromExcel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonFormatedAndConvertHtml{
	
   public String generateHtml(String jsonArrayString) throws JSONException, IOException {
	   String response = "Pass";
	 //String jsonArrayString = FileUtils.readFileToString(new File(jsonFilePath), StandardCharsets.UTF_8);
      JSONObject input;
      int pass = 0;
      int fail = 0;
      JSONArray outputArray =  new JSONArray();
      JSONObject chartValue = new JSONObject();
      JSONObject passChart = new JSONObject();
      JSONObject failChart = new JSONObject();
      
      try {
         input = new JSONObject(jsonArrayString);
         JSONArray issues = input.getJSONArray("issues");
         String dates[]=new String[issues.length()];
         int chartDateLength = 0;
         for(int i = 0; i < issues.length();i++) {
             JSONObject innerObj = issues.getJSONObject(i);  
             JSONObject output = new JSONObject();
             JSONObject fields = innerObj.getJSONObject("fields");
             
             
             String Karatestatus = (String) fields.getJSONObject("customfield_10044").get("value");
             String Date = (String) fields.get("created");
             Date = Date.substring(0, 10);
             String KarateExecutionTime = (String) fields.get("customfield_10049");
             String KarateScenario = (String) fields.get("customfield_10042");  
             String KarateFeature = (String) fields.get("customfield_10043");
             String KarateRelease = (String) fields.get("customfield_10045");
             String KarateTestType = (String) fields.get("customfield_10046");
             String KarateError = (String) fields.get("customfield_10048");
             
             output.put("Karate Scenario", KarateScenario);
             output.put("Karate Feature", KarateFeature);
             output.put("Karate Execution Time", KarateExecutionTime);
             output.put("Karate Release", KarateRelease);
             output.put("Karate Test Type", KarateTestType);
             output.put("Karate Error", KarateError);
             output.put("Karate status", Karatestatus);
             output.put("Karate Issue Report Date", Date);
             
             try {
            	 if(chartValue.get(Date) != null) {
            		 int number = (int) chartValue.get(Date);
            		 number++ ;
            		 chartValue.put(Date, number);
            	 }
             }
             catch (Exception e) {
            	 chartValue.put(Date,1);
            	 dates[chartDateLength] = Date;
            	 chartDateLength++;
            	 
             }
             if(Karatestatus.equalsIgnoreCase("Pass")) {
            	 pass++;
             }else {
            	 fail++;
             }
             if(Karatestatus.equalsIgnoreCase("Pass")) {
            	 try {
                	 if(passChart.get(Date) != null) {
                		 int number = (int) passChart.get(Date);
                		 number++ ;
                		 passChart.put(Date, number);
                	 }
                 }
                 catch (Exception e) {
                	 passChart.put(Date,1);
                	 failChart.put(Date,0);
                 }
             }else {
            	 try {
                	 if(failChart.get(Date) != null) {
                		 int number = (int) failChart.get(Date);
                		 number++ ;
                		 failChart.put(Date, number);
                	 }
                 }
                 catch (Exception e) {
                	 failChart.put(Date,1);
                	 passChart.put(Date,0);
                 }
             }
             outputArray.put(output);
         }
         JSONObject outputArrayFormated =  new JSONObject();
         outputArrayFormated.put("Sheet1", outputArray);
         
         
         //create value in string for chart
         String chartValueToString = null;
         int datesvalue[]=new int[chartValue.length()];
         for(int i = 0; i < chartValue.length();i++) {
        	 datesvalue[i] = (int) chartValue.get(dates[i]);
         }
         
         for(String date : dates) {
        	 if(date != null) {
        		 if(chartValueToString != null) {
        			 chartValueToString = chartValueToString + ",\"";
        		 }else {
        			 chartValueToString = "[\"";
        		 }
        		 chartValueToString = chartValueToString + date + "\"" ;
        	 }
         }
         chartValueToString = chartValueToString + "]";
         int failList[]=new int[failChart.names().length()];
         int passList[]=new int[passChart.names().length()];
         int fi =0;
         int pi = 0;
         for(String date : dates) {
        	 if(date != null) {
             int keyvalue = (int) failChart.get(date);
             failList[fi] =  keyvalue;
             fi++;
        	 }
         }
         
         for(String date : dates) {
        	 if(date != null) {
             int keyvalue = (int) passChart.get(date);
             passList[pi] =  keyvalue;
             pi++;
        	 }
         }
         System.out.println("Date  " +chartValueToString);
         System.out.println("Pass  " +Arrays.toString(passList));
         System.out.println("Fail  " +Arrays.toString(failList));
         
        //JSONArray issues2 = outputArrayFormated.getJSONArray("Sheet1");
        //System.out.println(issues2.length());
       //  JSONObject innerObj2 = outputArray.getJSONObject(0);
     //  System.out.println(innerObj2.length());

         
         
         File f = new File("JiraReport.html");
         BufferedWriter bw = new BufferedWriter(new FileWriter(f));
         bw.write("<!DOCTYPE HTML>\r\n"
         		+ "<html>\r\n"
         		+ "<head>\r\n"
         		+ "	<meta charset=\"utf-8\" />\r\n"
         		+ "	<title>Convert Excel to HTML Table using JavaScript</title>\r\n"
         		+ "	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\r\n"
         		+ "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" "
         		+ "integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\r\n"
         		+ "\r\n"
         		+ "</head>\r\n"
         		+ "<body>\r\n"
         		+ "    <div class=\"container\">\r\n"
         		+ "    	<h2 class=\"text-center mt-4 mb-4\">Karate Jira Issue Report</h2>\r\n"
         		+ "    	<div class=\"card\">\r\n"
         		+ "    	</div>\r\n"
         		+ "        <div id=\"excel_data\" class=\"mt-5\">");
         
         
         bw.write("<table class=\"table table-striped table-bordered\">");
         
         for(int row = 0; row < outputArray.length(); row++)
         {
        	 
        	// bw.write("</tr>");
        	 JSONObject innerObj2 = outputArray.getJSONObject(row);

                 if(row == 0)
                 {

                	 bw.write("<th>Karate Scenario</th>");
                	 bw.write("<th>Karate Feature</th>");
                	 bw.write("<th>Karate Execution Time</th>");
                	 bw.write("<th>Karate Release</th>");
                	 bw.write("<th>Karate Test Type</th>");
                	 bw.write("<th>Karate Error</th>");
                	 bw.write("<th>Karate status</th>");
                	 bw.write("<th>Karate Issue Report Date</th>");
                	 bw.write("</tr>");
                	 bw.write("<td>"+innerObj2.get("Karate Scenario")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Feature")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Execution Time")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Release")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Test Type")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Error")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate status")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Issue Report Date")+"</td>");

                 }
                 else
                 {
                	 bw.write("</tr>");
                	 bw.write("<td>"+innerObj2.get("Karate Scenario")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Feature")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Execution Time")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Release")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Test Type")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Error")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate status")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Issue Report Date")+"</td>");

                 }
        	 
         }
        
         bw.write("<center><embed type=\"text/html\" src=\"Chart.html\" width=\"800\" height=\"500\">");

         bw.write("<center><embed type=\"text/html\" src=\"PieChart.html\" width=\"800\" height=\"500\">");
         bw.write("<br>");
         bw.write("</div>\r\n"
         		+ "    </div>\r\n"
         		+ "</body>\r\n"
         		+ "</html>");
         bw.close();
          
         
         File fc = new File("Chart.html");
         BufferedWriter bwc = new BufferedWriter(new FileWriter(fc));
         
         bwc.write("<!DOCTYPE html>\r\n"
         		+ "<html>\r\n"
         		+ "<head>\r\n"
         		+ "  <script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js\"></script>\r\n"
         		+ "</head>\r\n"
         		+ "\r\n"
         		+ "<body>\r\n"
         		+ "   <center><br>\r\n"
         		+ "    <canvas id=\"bar-chart\" width=\"800\" height=\"400\" style=\"border:1px solid\"></canvas><br>\r\n"
         		+ "   \r\n"
         		+ "    <script>\r\n"
         		+ "     \r\n"
         		+ "new Chart(document.getElementById(\"bar-chart\"), {\r\n"
         		+ "    type: 'bar',\r\n"
         		+ "    data: {\r\n"
         		+ "      labels: ");
         bwc.write(chartValueToString);
         bwc.write(",\r\n"
         		+ "      datasets: [\r\n"
         		+ "        {\r\n"
         		+ "          label: \"Pass\",\r\n"
         		+ "          backgroundColor: \"#3e95cd\",\r\n"
         		+ "          data:");
         bwc.write(Arrays.toString(passList));
         
         bwc.write("}, {\r\n"
         		+ "          label: \"Fail\",\r\n"
         		+ "          backgroundColor: \"#8e5ea2\",\r\n"
         		+ "          data:");
         
         bwc.write(Arrays.toString(failList));
         bwc.write(" }\r\n"
         		+ "      ]\r\n"
         		+ "    },\r\n"
         		+ "    options: {\r\n"
         		+ "      title: {\r\n"
         		+ "        display: true,\r\n"
         		+ "        text: 'Karate Test Report'\r\n"
         		+ "      },\r\n"
         		+ "	  responsive: false,\r\n"
         		+ "	   scales: {\r\n"
         		+ "        yAxes: [{\r\n"
         		+ "            ticks: {\r\n"
         		+ "                beginAtZero: true\r\n"
         		+ "            }\r\n"
         		+ "        }]\r\n"
         		+ "    }\r\n"
         		+ "    }\r\n"
         		+ "});\r\n"
         		+ "    </script>\r\n"
         		+ "   \r\n"
         		+ "   \r\n"
         		+ "</body>\r\n"
         		+ "</html>");
     bwc.close();
     
     File fp = new File("PieChart.html");
     BufferedWriter bwp = new BufferedWriter(new FileWriter(fp));
     bwp.write("<!DOCTYPE html>\r\n"
     		+ "<html>\r\n"
     		+ "<head>\r\n"
     		+ "  <script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js\"></script>\r\n"
     		+ "</head>\r\n"
     		+ "\r\n"
     		+ "<body>\r\n"
     		+ "   <center><br>\r\n"
     		+ "    <canvas id=\"pie-chart\" width=\"800\" height=\"450\"></canvas>\r\n"
     		+ "   \r\n"
     		+ "    <script>\r\n"
     		+ "     \r\n"
     		+ "new Chart(document.getElementById(\"pie-chart\"), {\r\n"
     		+ "    type: 'pie',\r\n"
     		+ "    data: {\r\n"
     		+ "      labels: [\"Pass\", \"Fail\"],\r\n"
     		+ "      datasets: [{\r\n"
     		+ "        label: \"Population (millions)\",\r\n"
     		+ "        backgroundColor: [\"#3e95cd\", \"#8e5ea2\"],\r\n"
     		+ "        data: [");
     bwp.write(pass+","+fail);
     bwp.write("]\r\n"
     		+ "      }]\r\n"
     		+ "    },\r\n"
     		+ "    options: {\r\n"
     		+ "      title: {\r\n"
     		+ "        display: true,\r\n"
     		+ "        text: 'Total Test Failure'\r\n"
     		+ "      },\r\n"
     		+ "	  responsive: false,\r\n"
     		+ "    }\r\n"
     		+ "});\r\n"
     		+ "    </script>\r\n"
     		+ "   \r\n"
     		+ "   \r\n"
     		+ "</body>\r\n"
     		+ "</html>");
     bwp.close();
     
      }
      catch(Exception e) {
         e.printStackTrace();
         response = "Fail";
      }
      return response;
   }
   
   public static void main(String[] args) throws JSONException, IOException {
	  
		
		  String jsonFilePath = "target/IssuesJiraTemp.json"; String response = new
		  JsonFormatedAndConvertHtml().generateHtml(jsonFilePath);
		  System.out.println(response);
		 
   }
}