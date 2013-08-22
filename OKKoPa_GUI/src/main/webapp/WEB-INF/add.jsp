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
        
        <p>${help}</p>
        
        <p> - - - - - - - - </p>
        
        <p>${warning}</p>

        <form name="newReference"
              action="${pageContext.request.contextPath}/add"
              method="post">
            <table>
                <tr>
                    <td>Käyttäjätunnus</td><td><input type="text" name="id" /></td>
                </tr>
                <tr>
                    <td>Viite</td><td><input type="text" name="code" /></td>
                </tr>
                <tr>
                    <td></td><td><input type="submit" name="Lähetä" /></td>
                </tr>
            </table>
        </form>

    </body>
</html>
