xquery version "1.0";

<html>
    <head>
        <title>DeStress</title>
        <link rel="stylesheet" type="text/css" href="DeStress.css"/>
    </head>
    <body>
        <center>
            <h1>DeStress</h1>
            <h2>Session {data(/session/@sessionid)}</h2>
            <h2>{data(/session/session_description)}</h2>
        </center>
        <p>{data(/session/descendant::content[1])}</p>
        {
            for $act in /session/activities/activity
            return (
                <hr/>,
                <h3>{data($act/title)}</h3>,
                <p>{data($act/description)}</p>,
                for $img in $act/image
                    return <img src="{$img/@src}"/>
            )
        }
        <h2>{data(/session/descendant::header[1])}</h2>
        <p>{data(/session/descendant::content[2])}</p>
        <h2>{data(/session/descendant::header[2])}</h2>
        <ul>
        {
            for $idea in /session/ideas/idea
            return <li>{data($idea)}</li>
        }
        </ul>
        <h2>{data(/session/descendant::header[3])}</h2>
        <p>{data(/session/descendant::content[3])}</p>
        <form>
        {
            for $exercise in /session/exercises/exercise
            return (
                <p>{data($exercise/question)}</p>,
                <br/>,
                for $answer in $exercise/answer
                return <textarea cols="50" rows="{data($answer/@lines)}">Your Answer</textarea>
            )
        }
        </form>
        <img name="footer-image" src="{session/image/@src}"/>
    </body>
</html>