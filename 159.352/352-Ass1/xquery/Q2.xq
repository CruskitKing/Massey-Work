xquery version "1.0";

<html>
    <head>
        <title>XQuery - Part 2</title>
    </head>
    <body>
        <h2>{data(/session/descendant::header[2])}</h2>
        <ul>
        {
            for $idea in /session/ideas/idea
            return <li>{data($idea)}</li>
        }
        </ul>
    </body>
</html>