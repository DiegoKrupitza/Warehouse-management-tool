<html>
<head>
  <title>Welcome to Lagerverwaltung</title>

  <!-- Bootstrape CSS -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
        integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
  <link rel="stylesheet" href="/css/login.css">

</head>
<body style="background: #f4f3ef;">

<div class="container">



  <div class="row">
    <div class="col-md-6 col-md-offset-3 text-center">
      <img src="/img/logoNoBackground.png"/>



      <% if (request.getParameter("error") != null) { %>
      <div class="alert alert-danger alert-dismissible" role="alert">
      <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      <strong>Error!</strong> Username does not exists
    </div>
      <% } %>

      <form action="/login" method="post">

        <div class="form-group">
          <label for="username">Username</label>
          <input type="text" name="username" class="form-control" id="username" placeholder="samsampleman">
        </div>

        <input type="submit" class="btn btn-primary login-button" value="Login"/>
      </form>
    </div>
  </div>
</div>

</body>

<!-- Jquery -->
<script src="https://code.jquery.com/jquery-3.1.1.min.js"
        integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8=" crossorigin="anonymous"></script>

<!-- Bootstrape JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>
</html>