xquery version "1.0";

<html>
    <head>
        <title>XQuery - Part 1</title>
    </head>
    <body>
    {
        for $act in /session/activities/activity
        return (
            <h2>{data($act/title)}</h2>,
            <p>{data($act/description)}</p>,
            <hr/>
        )
    }
    </body>
</html>