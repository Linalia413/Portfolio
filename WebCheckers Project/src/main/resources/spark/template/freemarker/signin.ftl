<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="10">
    <title>Web Checkers | ${title}</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
    <div class="page">

        <h1>Web Checkers | ${title}</h1>

        <!-- Provide a navigation bar -->
        <div class="navigation">
            <a href="/">home</a>
        </div>

        <div class="body">
            <h2>Sign in:</h2>
            <br>
            <form action="/signinpost" method="POST">
                Name:
                <input name="name">
                <br>
                <br>
                <input type="submit" name="submit">
            </form>
        </div>
    </div>
</body>

</html>
