<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>DeStress</title>
                <link rel="stylesheet" type="text/css" href="DeStress.css"/>
            </head>
            <body>
                <center>
                    <h1>DeStress</h1>
                    <h2>Session <xsl:value-of select="session/@sessionid"/></h2>
                    <h2><xsl:value-of select="session/session_description"/></h2>
                </center>
                <xsl:value-of select="session/descendant::content[1]" />
                <xsl:apply-templates select="session/activities"/>
                <h2><xsl:value-of select="session/descendant::header[1]"/></h2>
                <xsl:value-of select="session/descendant::content[2]"/>
                <h2><xsl:value-of select="session/descendant::header[2]"/></h2>
                <xsl:apply-templates select="session/ideas"/>
                <h2><xsl:value-of select="session/descendant::header[3]"/></h2>
                <p><xsl:value-of select="session/descendant::content[3]"/></p>
                <xsl:apply-templates select="session/exercises"/>
                <img>
                    <xsl:attribute name="src">
                        <xsl:value-of select="session/image/@src"/>
                    </xsl:attribute>
                    <xsl:attribute name="id">footer-image</xsl:attribute>
                </img>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="session/activities">
        <xsl:for-each select="activity">
            <hr />
            <h3><xsl:value-of select="title"/></h3>
            <p><xsl:value-of select="description"/></p>
            <xsl:for-each select="image">
                <img>
                    <xsl:attribute name="src">
                        <xsl:value-of select="@src"/>
                    </xsl:attribute>
                </img>
            </xsl:for-each>
        </xsl:for-each>
        <hr />
    </xsl:template>

    <xsl:template match="session/ideas">
        <ul>
            <xsl:for-each select="idea">
                <li>
                    <xsl:value-of select="."/>
                </li>
            </xsl:for-each>
        </ul>
    </xsl:template>

    <xsl:template match="session/exercises">
        <form>
            <xsl:for-each select="exercise">
                <xsl:value-of select="question"/><br/>
                <xsl:for-each select="answer">
                    <textarea cols="50">
                        <xsl:attribute name="rows">
                            <xsl:value-of select="@lines"/>
                        </xsl:attribute>Your Answer
                    </textarea>
                    <br/>
                </xsl:for-each>
                <br/>
            </xsl:for-each>
            <br/>
        </form>
    </xsl:template>
</xsl:stylesheet>