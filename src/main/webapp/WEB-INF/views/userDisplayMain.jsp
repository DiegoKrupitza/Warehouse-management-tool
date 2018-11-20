<%--
  Created by IntelliJ IDEA.
  User: Dominik
  Date: 22.01.2017
  Time: 21:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="domain.User" %>
<%@ page import="java.util.List" %>
<%@ page import="domain.Geschlecht" %>
<%
    List<User> userList = (List<User>) request.getAttribute("allUser");
    for (User user : userList
            ) {

%>
<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
    <div class="card">
        <div class="header">
            <h3 class="title"><% out.print(user.getUsername()); %><br>
            </h3>
        </div>
        <div class="content">
            <p><span class="user-info">Username: </span><span><% out.print(user.getUsername()); %></span></p>
            <p><span class="user-info">Email: </span><span><% out.print(user.getEmail()); %></span></p>
            <p><span class="user-info">Created At: </span><span><%
                out.print(user.getBeautifulDate(user.getCreatedAt())); %></span>
            </p>
            <div class="more-info" id="more-info-<% out.print(user.getId()); %>">
                <p><span class="user-info">Vorname: </span><span><% out.print(user.getVorname()); %></span></p>
                <p><span class="user-info">Nachname: </span><span><% out.print(user.getLastname()); %></span></p>
                <p><span class="user-info">Geschlecht: </span><span><%
                    out.print(user.getBeautifulSex(user.getGeschlecht())); %></span></p>
            </div><!-- more-info -->
            <div class="footer user-footer text-center">
                <div class="col-xs-4 text-center">
                    <a href="/user/edit/<% out.print(user.getId()); %>"><i
                            class="ti-pencil-alt2 icon-warning"></i>
                    </a>
                </div>

                <div class="col-xs-4 text-center">
                    <a href="#" id="animated-more-info" data-id="<% out.print(user.getId()); %>"><i
                            class="ti-eye"></i>
                    </a>
                </div>

                <div class="col-xs-4 text-center">
                    <a href="#" id="delete-action-link" data-name="<% out.print(user.getUsername()); %>"
                       data-id="<% out.print(user.getId()); %>"><i class="ti-trash icon-danger"></i></a>
                </div>
                <div class="clearfix"></div>
            </div>
        </div>
    </div>
</div>
<%
    }
%>