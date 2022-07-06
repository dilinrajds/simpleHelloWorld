package generateChartFromExcel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonFormatedAndConvertHtml{
	
   public String generateHtml(String jsonFilePath) throws JSONException, IOException {
	   String response = "Pass";
	  String jsonArrayString = FileUtils.readFileToString(new File(jsonFilePath), StandardCharsets.UTF_8);
      JSONObject input;
      
      JSONArray outputArray =  new JSONArray();
      try {
         input = new JSONObject(jsonArrayString);
         JSONArray issues = input.getJSONArray("issues");
         int pass = 0;
         int fail = 0;
         
         for(int i = 0; i < issues.length();i++) {
             JSONObject innerObj = issues.getJSONObject(i);  
             JSONObject output = new JSONObject();
             JSONObject fields = innerObj.getJSONObject("fields");
             
             
             String Karatestatus = (String) fields.getJSONObject("customfield_10044").get("value");
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
             output.put("Karates status", Karatestatus);
             if(Karatestatus.equalsIgnoreCase("Pass")) {
            	 pass++;
             }else {
            	 fail++;
             }
             outputArray.put(output);
         }
         
         JSONObject outputArrayFormated =  new JSONObject();
         outputArrayFormated.put("Sheet1", outputArray);
         
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
         		+ "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\r\n"
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
                	 bw.write("<th>Karates status</th>");
                	 bw.write("</tr>");
                	 bw.write("<td>"+innerObj2.get("Karate Scenario")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Feature")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Execution Time")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Release")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Test Type")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karate Error")+"</td>");
                	 bw.write("<td>"+innerObj2.get("Karates status")+"</td>");

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
                	 bw.write("<td>"+innerObj2.get("Karates status")+"</td>");

                 }
        	 
         }
        

         bw.write("</div>\r\n"
         		+ "    </div>\r\n"
         		+ "</body>\r\n"
         		+ "</html>");
         bw.close();
          
         /*try {
             FileWriter file = new FileWriter("TestKarate.json");
             file.write(outputArrayFormated.toString());
             file.close();
          } catch (IOException e) {
            
             e.printStackTrace();
          }
         String Response = new JsonToExcelConverterWithoutUpdate().jsonFileToExcelFile("TestKarate.json","TestKarate.xlsx",".xlsx"); // Create excel
         
         
         File file = new File("TestKarate.csv");
         String csv = CDL.toString(outputArray);
         FileUtils.writeStringToFile(file, csv); // Create csv
        // System.out.println("Data has been Sucessfully Writeen to "+ file);*/
        // System.out.println("Doneeeeeeeee");
      }
      catch(Exception e) {
         e.printStackTrace();
         response = "Fail";
      }
      return response;
   }
   
   public static void main(String[] args) throws JSONException, IOException {
	  
	  String jsonFilePath = "target/IssuesJiraTemp.json"; 
	  String response = new JsonFormatedAndConvertHtml().generateHtml(jsonFilePath);
	  System.out.println(response);
   }
}