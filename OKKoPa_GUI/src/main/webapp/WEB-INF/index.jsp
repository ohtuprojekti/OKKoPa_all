<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>OKKoPa</title>
    </head>
    <body>
        <h1>OKKoPa</h1>
        <ul>
            <li><a href="${pageContext.request.contextPath}/add">${add}</a>
                <ul>
                    <li>${addInfo}</li>
                </ul>
            </li>
            <li><a href="${pageContext.request.contextPath}/create">${create}</a>
                <ul>
                    <li>${createInfo}</li>
                </ul>
            </li>
            <li><a href="${pageContext.request.contextPath}/front">${front}</a>
                <ul>
                    <li>${frontInfo}</li>
                </ul>
            </li>
        </ul>
    </body>
</html>