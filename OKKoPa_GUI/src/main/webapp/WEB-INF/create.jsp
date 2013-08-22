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
        
        <form name="getReference"
              action="${pageContext.request.contextPath}/getreference"
              method="post">
            <table>
                <tr>
                    <td>Viitteiden määrä</td><td><select name="amount">
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
                            <option value="600">600</option>
                            <option value="700">700</option>
                            <option value="800">800</option>
                            <option value="900">900</option>
                            <option value="1000">1000</option>
                            <option value="2000">2000</option>
                            <option value="3000">3000</option>
                            <option value="4000">4000</option>
                            <option value="5000">5000</option>
                            <option value="6000">6000</option>
                            <option value="7000">7000</option>
                            <option value="8000">8000</option>
                            <option value="9000">9000</option>
                            <option value="10000">10000</option>
                        </select></td>
                </tr>
                <tr>
                    <td>Merkkien määrä</td><td><select name="size">
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                            <option value="6" selected="selected">6</option>
                            <option value="7">7</option>
                            <option value="8">8</option>
                            <option value="9">9</option>
                            <option value="10">10</option>
                            <option value="11">11</option>
                            <option value="12">12</option>
                            <option value="13">13</option>
                            <option value="14">14</option>
                            <option value="15">15</option>
                        </select></td>
                </tr>
                <tr>
                    <td>Käytetäänkö kirjaimia</td><td>
                        <input type="radio" name="letters" value="yes" checked="checked">Kyllä
                        <input type="radio" name="letters" value="no">Ei</td>
                </tr>
                <tr>
                    <td>Mitä palautetaan</td><td>
                        <input type="radio" name="back" value="txt">txt-tiedosto viitteistä</td><td></td></tr><tr><td></td><td>
                        <input type="radio" name="back" value="zip" checked="checked">zip-tiedosto luoduista QR-koodeista</td>
                </tr>
                <tr>
                    <td></td><td><input type="submit" name="Lähetä" /></td>
                </tr>
            </table>
        </form>
    </body>
</html>
