<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${message}</title>
    </head>
    <body>
        <h1>${message}</h1>

        <pre>${help}</pre>
        
        <p> - - - - - - - - </p>
        
        <form name="getReference"
              action="${pageContext.request.contextPath}/getreference"
              method="post">
            <table>
                <tr>
                    <td>${amount}</td><td><select name="amount">
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                            <option value="10">10</option>
                            <option value="20">20</option>
                            <option value="30">30</option>
                            <option value="40">40</option>
                            <option value="50">50</option>
                            <option value="60">60</option>
                            <option value="70">70</option>
                            <option value="80">80</option>
                            <option value="90">90</option>
                            <option value="100" selected="selected">100</option>
                            <option value="200">200</option>
                            <option value="300">300</option>
                            <option value="400">400</option>
                            <option value="500">500</option>
                        </select></td>
                </tr>
                <tr>
                    <td>${size}</td><td>${codeSize}</td>
                </tr>
                <tr>
                    <td>${letters}</td><td>${lettersWhat}</td>
                </tr>
                <tr>
                    <td>${back}</td><td>
                        <input type="radio" name="back" value="txt">${backTxt}</td><td></td></tr><tr><td></td><td>
                        <input type="radio" name="back" value="zip" checked="checked">${backZip}</td>
                </tr>
                <tr>
                    <td></td><td><input type="submit" value="${submit}" /></td>
                </tr>
            </table>
        </form>
    </body>
</html>