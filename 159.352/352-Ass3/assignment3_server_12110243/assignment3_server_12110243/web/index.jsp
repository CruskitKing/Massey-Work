<%-- 
    Document   : index
    Created on : 2/06/2015, 3:37:06 PM
    Author     : mitch
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.rmi.server.UID" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>xlog - 12110243</title>
        <script src="static/jquery-1.11.3.min.js"></script>
        <script>
            var exc = {
                "id":<%= new UID().hashCode() %>,
                "class":"java.lang.NullPointerException",
                "timestamp":<%= System.currentTimeMillis() %>,
                "message":"Test Exception",
                "stacktrace":"Test Stacktrace"
            };
            
            function addException() {
                $.post("http://localhost:8084/assignment3_server_12110243/xlog",
                        {
                            "exception": JSON.stringify(exc)
                        },
                        function(){loadExceptions();});
            };
            
            function loadExceptions() {
                $('#exceptionTable').empty();
                $('#exceptionTable').append("<thead><th>ID</th><th>Class</th><th>TimeStamp</th></thead>");
                $.getJSON("http://localhost:8084/assignment3_server_12110243/xlog/all", function(response) {
                    for (var i=0;i<response.length;i++) {
                        var obj = response[i];
                        $('#exceptionTable').append("<tr><td>"+obj["id"]+"</td><td>"+obj["class"]+"</td><td>"+new Date(obj["timestamp"]).toGMTString()+"</td><td><button onclick=\"displayException("+obj["id"]+")\">Details</button></td></tr>");
                    }   
                });
            }
            
            function displayException(id) {
                $.getJSON("http://localhost:8084/assignment3_server_12110243/xlog/"+id.toString(),function(response){
                    alert("ID: "+response["id"]+"\nClass: "+response["class"]+"\nTimestamp: "+new Date(response["timestamp"]).toGMTString()+"\nMessage: "+response["message"]+"\nStacktrace: "+response["stacktrace"]);
                });
            }
            
            function onStart() {
                addException();
                addException();
                addException();
                loadExceptions();
            }
            window.onload = onStart;
        </script>
    </head>
    <body>
        <button onclick="loadExceptions()">Refresh</button>
        <button onclick="addException()">Add Test</button>
        <table id="exceptionTable" border="1">
            <thead>
                <th>ID</th>
                <th>Class</th>
                <th>TimeStamp</th>
            </thead>
        </table>
    </body>
</html>
