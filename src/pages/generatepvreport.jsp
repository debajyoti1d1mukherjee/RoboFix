<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="css/style.css" type="text/css" />

<link rel="stylesheet" href="css/style.css" type="text/css" />

<style>
.body {
background-color:#424242;
font-family: Verdana, Arial, Helvetica, sans-serif;
font-size: 12px;
}
p {color:#FFFFFF;}
tr.even { background: #FFBF00; }
tr.odd { background: #FFFFFF; }
th,h2{color:#FFFFFF;}
A:link {color: #FFBF00; 
		text-decoration:none}
A:visited {color: #FFBF00; text-decoration:none}
A:hover {color: #FFFFFF;text-decoration:underline; opacity: 0.6; }
.generatebutton {
    background-color: #FFFFFF;
  -moz-border-radius: 15px;
  -webkit-border-radius: 15px;
  border: 5px solid #FFBF00;
  padding: 5px;
}

table{
border-color: #FFFFFF;
}
</style>

</head>
<!-- Load jQuery from Google's CDN -->
    <!-- Load jQuery UI CSS  -->
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
    
    <!-- Load jQuery JS -->
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <!-- Load jQuery UI Main JS  -->
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    
    <!-- Load SCRIPT.JS which will create datepicker for input field  -->
    <script src="scripts/script.js"></script>
    
   <!-- 	 <link rel="stylesheet" href="runnable.css" /> -->
   <script type="text/javascript">
   /*  jQuery ready function. Specify a function to execute when the DOM is fully loaded.  */
   $(document).ready(
     
     /* This is the function that will get executed after the DOM is fully loaded */
     function () {
       $( "#fromDate" ).datepicker({
         changeMonth: true,//this option for allowing user to select month
         changeYear: true //this option for allowing user to select from year range
       });
       $("#toDate").datepicker();
     
       $("#generateButton").click(function() {
      		//alert("Test");
          //var fromDate = $("#fromDate");
          //var returning = $("#toDate");
          var fromDate = document.getElementById('fromDate');
          var toDate = document.getElementById('toDate');

          if ((fromDate.value == "" || fromDate.value==null) || (toDate.value == "" || toDate.value==null)) {
  			alert("Please select from date or to date.");
  			return false;
          } 
      });
     
     });
   </script>
<body class="body">
	<!-- <h1>Message : ${message}</h1> -->
	<form:form method="get" action="generatePVReport" modelAttribute="report">
	<table width="100%">
		<tr>
			<td></td>
			<td></td>
			<td align="right"><a href="report">Show Commissiong Report</a></td>
		</tr>
		<tr>
			<td><table width="70%"><tr>
			<td><p>From Date: <form:input type="text" path="fromDate" readonly="true" /></p></td>
			<td><p>To Date: <form:input type="text" path="toDate" readonly="true" /></p></td>
			<td><input type="submit" value="Generate Report" class="generatebutton" id="generateButton"></td>
			</tr>
			</table></td>
		</tr>
		<!-- Display Commissioning 
		<c:if test="${not empty commisionLists}">
		<tr><td colspan="3"><table border="1">
			<thead>
			<tr><td colspan="8"><h2>Commissioning</h2></td></tr>
			<tr>
				<th>Created Date</th>
				<th>Entity Status</th>
				<th>OnAir Entity Id</th>
				<th>Series Id</th>
				<th>Type</th>
				<th>UUID</th>
				<th>Last Processed</th>
				<th>Release Time</th>
			</tr>
			<thead>
 			<c:forEach var="episode" items="${commisionLists}" varStatus="loop">
				<tr class="${loop.index % 2 == 0 ? 'even' : 'odd'}">
					<td>${episode.created}</td>
					<td>${episode.entityStatus}</td>
					<td>${episode.onairentityid}</td>
					<td>${episode.seriesid}</td>
					<td>${episode.type}</td>
					<td>${episode.uid}</td>
					<td>${episode.lastprocessed}</td>
					<td>${episode.releaseTime}</td>
				</tr>
				
			</c:forEach>
			<tr><td><a href="generateReport?page=previous">Previous</a></td><td><b>${pageText}</b></td><td><a href="generateReport?page=next">Next</a></td></tr>
		 </table></td></tr>
	</c:if>-->
	<tr><td>&nbsp;</td></tr>
	<!-- Display Publication -->
	<c:if test="${not empty pvLists}">
		<tr><td colspan="3"><table border="1">
			<thead>
			<tr><td colspan="9"><h2>Publication Event</h2></td></tr>
			<tr>
				<th>Created Date</th>
				<th>Entity Status</th>
				<th>Episode Completed</th>
				<th>Last Processed</th>
				<th>OnAir Entity Id</th>
				<th>Processed State</th>
				<th>PV Released</th>
				<th>Release Time</th>
				<th>UUID</th>
			</tr>
			</thead>
 			<c:forEach var="pevents" items="${pvLists}" varStatus="loop">
				<tr class="${loop.index % 2 == 0 ? 'even' : 'odd'}">
					<td>${pevents.created}</td>
					<td>${pevents.entityStatus}</td>
					<td>${pevents.episodecompleted}</td>
					<td>${pevents.lastprocessed}</td>
					<td>${pevents.onairentityid}</td>
					<td>${pevents.processedstate}</td>
					<td>${pevents.pvreleased}</td>
					<td>${pevents.releaseTime}</td>
					<td>${pevents.uid}</td>
				</tr>
				
			</c:forEach>
			<tr><td colspan="6">&nbsp;</td><td><a href="generatePVReport?page=previous">Previous</a></td><td><b><p>${pageText}</p></b></td><td><a href="generatePVReport?page=next">Next</a></td></tr>
		 </table></td></tr>
	</c:if>
	</table>
	</form:form>
	
</body>
</html>