<%@ page import="domain.Artikelreservierung" %>
<%@ page import="java.util.List" %>
<%@ page import="domain.Geschlecht" %>
<%@ page import="domain.Kategorie" %>
<%@ page import="domain.User" %>

<% User user = (User) request.getAttribute("user"); %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <link rel="apple-touch-icon" sizes="76x76" href="/img/theme/apple-icon.png">
    <link rel="icon" type="image/png" sizes="96x96" href="/img/theme/favicon.png">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <title>DomDie Lagerverwaltung | Add User</title>

    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport'/>
    <meta name="viewport" content="width=device-width"/>


    <!-- Bootstrap core CSS     -->
    <link href="/css/theme/bootstrap.min.css" rel="stylesheet"/>

    <!-- Animation library for notifications   -->
    <link href="/css/theme/animate.min.css" rel="stylesheet"/>

    <!--  Paper Dashboard core CSS    -->
    <link href="/css/theme/paper-dashboard.css" rel="stylesheet"/>


    <!--  Fonts and icons     -->
    <link href="http://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css" rel="stylesheet">
    <link href='https://fonts.googleapis.com/css?family=Muli:400,300' rel='stylesheet' type='text/css'>
    <link href="/css/theme/themify-icons.css" rel="stylesheet">

</head>
<body>

<div class="wrapper">
    <div class="sidebar" data-background-color="white" data-active-color="danger">


        <div class="sidebar-wrapper">
            <div class="logo">
                <a href="#" class="simple-text">
                    DomDie Lagerverwaltung
                </a>
            </div>

            <ul class="nav">
                <li>
                    <a href="/home">
                        <i class="ti-panel"></i>
                        <p>Dashboard</p>
                    </a>
                </li>
                <li class="active">
                    <a href="/user">
                        <i class="ti-user"></i>
                        <p>User Profile</p>
                    </a>
                </li>
                <li>
                    <a href="/article">
                        <i class="ti-headphone"></i>
                        <p>Article</p>
                    </a>
                </li>
            </ul>
        </div>
    </div>

    <div class="main-panel">
        <nav class="navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar bar1"></span>
                        <span class="icon-bar bar2"></span>
                        <span class="icon-bar bar3"></span>
                    </button>
                    <a class="navbar-brand" href="#">Add User</a>
                </div>
                <div class="collapse navbar-collapse">
                    <ul class="nav navbar-nav navbar-right">

                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <p><%= session.getAttribute("username") %>
                                </p>
                                <b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu">
                                <li><a href="#"><i class="ti-settings"></i> Settings</a></li>
                                <li><a href="#"><i class="ti-info"></i> Infos</a></li>
                                <li class="divider"></li>
                                <li><a href="/logout"><i class="ti-lock"></i> Logout</a></li>
                            </ul>
                        </li>


                    </ul>

                </div>
            </div>
        </nav>


        <div class="content">
            <div class="container-fluid">
                <form method="post" action="/user/new/">
                    <div class="row">
                        <div class="col-lg-12 col-md-12 col-sm-12">
                            <div class="card">
                                <div class="content">


                                    <div class="form-group">
                                        <label>Username</label>
                                        <input type="text" name="username" minlength="1"
                                               class="form-control border-input"

                                        >
                                    </div>

                                    <div class="form-group">
                                        <label>Vorname</label>
                                        <input type="text" name="vorname" minlength="1"
                                               class="form-control border-input"
                                        >
                                    </div>

                                    <div class="form-group">
                                        <label>Nachname</label>
                                        <input type="text" name="nachname" minlength="1"
                                               class="form-control border-input"
                                        >
                                    </div>

                                    <div class="form-group">
                                        <label>Email</label>
                                        <input type="email" name="email" class="form-control border-input"
                                        >
                                    </div>


                                    <div class="form-group">
                                        <label for="ges1">Geschlecht:</label>
                                        <select class="form-control border-input" name="geschlecht" id="ges1">
                                            <option value="<% out.print(Geschlecht.MAENNLICH.name()); %>"><% out.print(Geschlecht.MAENNLICH.name()); %></option>
                                            <option value="<% out.print(Geschlecht.WEIBLICH.name()); %>"><% out.print(Geschlecht.WEIBLICH.name()); %></option>
                                        </select>
                                    </div>

                                    <!-- das Datum vlt noch abspeichern für die Session also auch für das createdAt!!!! -->

                                    <div class="submit-button-area col-xs-12">
                                        <input type="submit" name="submit" value="Add User"
                                               class="btn btn-success col-xs-12">
                                    </div>
                                    <div class="clearfix"></div>
                                </div>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                    </div><!-- end .row -->
                </form>
            </div>
        </div>


        <footer class="footer">
            <div class="container-fluid">
                <nav class="pull-left">
                    <ul>

                        <li>
                            <a href="http://www.creative-tim.com">
                                Creative Tim
                            </a>
                        </li>
                        <li>
                            <a href="http://blog.creative-tim.com">
                                Blog
                            </a>
                        </li>
                        <li>
                            <a href="http://www.creative-tim.com/license">
                                Licenses
                            </a>
                        </li>
                    </ul>
                </nav>
                <div class="copyright pull-right">
                    &copy;
                    <script>document.write(new Date().getFullYear())</script>
                    , made with <i class="fa fa-heart heart"></i> by <a href="http://www.creative-tim.com">Creative
                    Tim</a>
                </div>
            </div>
        </footer>

    </div>
</div>


</body>

<!--   Core JS Files   -->
<script src="/js/theme/jquery-1.10.2.js" type="text/javascript"></script>
<script src="/js/theme/bootstrap.min.js" type="text/javascript"></script>

<!--  Checkbox, Radio & Switch Plugins -->
<script src="/js/theme/bootstrap-checkbox-radio.js"></script>

<!--  Charts Plugin -->
<script src="/js/theme/chartist.min.js"></script>

<!--  Notifications Plugin    -->
<script src="/js/theme/bootstrap-notify.js"></script>


<!-- Paper Dashboard Core javascript and methods for Demo purpose -->
<script src="/js/theme/paper-dashboard.js"></script>


<script type="text/javascript">

    $(document).ready(function (e) {



        <% if(request.getParameter("success") != null) { %>

        $.notify({
            icon: 'ti-rocket',
            message: "Succesfully updated!"

        }, {
            type: 'success',
            timer: 4000
        });

        <% } %>

    });
</script>

</html>


</div>

